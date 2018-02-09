/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.ss7.management.console;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javolution.util.FastList;
import javolution.util.FastSet;

import org.apache.log4j.Logger;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextFactory;
import org.jboss.security.audit.AuditEvent;
import org.jboss.security.audit.AuditLevel;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.mobicents.protocols.ss7.scheduler.Task;
import org.mobicents.ss7.management.transceiver.ChannelException;
import org.mobicents.ss7.management.transceiver.ChannelProvider;
import org.mobicents.ss7.management.transceiver.ChannelSelectionKey;
import org.mobicents.ss7.management.transceiver.ChannelSelector;
import org.mobicents.ss7.management.transceiver.Message;
import org.mobicents.ss7.management.transceiver.MessageFactory;
import org.mobicents.ss7.management.transceiver.ShellChannel;
import org.mobicents.ss7.management.transceiver.ShellChannelExt;
import org.mobicents.ss7.management.transceiver.ShellServerChannel;

/**
 * @author amit bhayani
 *
 */
public abstract class ShellServer extends Task implements ShellServerMBean {
    Logger logger = Logger.getLogger(ShellServer.class);

    public static final String CONNECTED_MESSAGE = "Connected to %s %s %s";
    public static final String CONNECTED_AUTHENTICATING_MESSAGE = "Authenticating against configured security realm";
    public static final String CONNECTED_AUTHENTICATION_FAILED = "Authentication failed";

    public static final String AUDIT_MESSAGE = "message";
    public static final String AUDIT_COMMAND = "command";
    public static final String AUDIT_COMMAND_RESPONSE = "response";

    Version version = Version.instance;

    private ChannelProvider provider;
    private ShellServerChannel serverChannel;
    private ChannelSelectionKey skey;
    private ChannelSelector selector;

    private ConcurrentHashMap<ChannelSelectionKey, ShellChannelExt> channelsMap = new ConcurrentHashMap<ChannelSelectionKey, ShellChannelExt>();

    private MessageFactory messageFactory = null;

    private volatile boolean started = false;

    private String address;

    private int port;

    private String securityDomain = null;
    private SecurityContext securityContext = null;

    private final FastList<ShellExecutor> shellExecutors = new FastList<ShellExecutor>();

    private static final int EXECUTION_TIMEOUT = 25;
    private static final int THREAD_POOL_SIZE = 16;
    private ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public ShellServer(Scheduler scheduler, List<ShellExecutor> shellExecutors) throws IOException {
        super(scheduler);
        this.shellExecutors.addAll(shellExecutors);
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the securityDomain
     */
    @Override
    public String getSecurityDomain() {
        return securityDomain;
    }

    /**
     * @param securityDomain the securityDomain to set
     */
    @Override
    public void setSecurityDomain(String securityDomain) {
        this.securityDomain = securityDomain;
    }

    public void start() throws IOException, NamingException {
        logger.info("Starting SS7 management shell environment");
        provider = ChannelProvider.provider();
        serverChannel = provider.openServerChannel();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(address, port);
        serverChannel.bind(inetSocketAddress);

        selector = provider.openSelector();
        skey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        messageFactory = ChannelProvider.provider().createMessageFactory();

        this.logger.info(String.format("ShellExecutor listening at %s", inetSocketAddress));

        this.started = true;
        this.activate(false);
        scheduler.submit(this, Scheduler.MANAGEMENT_QUEUE);

        if (this.securityDomain != null) {
            InitialContext initialContext = new InitialContext();

            startSecurityManager(initialContext, securityDomain);
        }
    }

    protected abstract void startSecurityManager(InitialContext initialContext, String securityDomain) throws NamingException;

    protected abstract void putPrincipal(Map map, Principal principal);

    protected abstract boolean isAuthManagementLoaded();

    protected abstract boolean isValid(Principal principal, Object credential);

    protected abstract String getLocalSecurityDomain();

    public void stop() {
        this.started = false;

        try {
            skey.cancel();
            Set<Entry<ChannelSelectionKey, ShellChannelExt>> channelsEntrySet = channelsMap.entrySet();

            for (Entry<ChannelSelectionKey, ShellChannelExt> entry : channelsEntrySet) {
                ShellChannelExt channel = entry.getValue();
                if (channel.isConnected())
                    channel.close();
            }

            channelsMap.clear();

            serverChannel.close();
            selector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();
        this.logger.info("Stopped ShellExecutor service");
    }

    @Override
    public int getQueueNumber() {
        return Scheduler.MANAGEMENT_QUEUE;
    }

    public long perform() {
        if (!this.started)
            return 0;

        FastSet<ChannelSelectionKey> keys = null;
        try {
            keys = selector.selectNow();
        } catch (ChannelException ce) {
            logger.error("An error occured while selecting selector key", ce);
            if (ce.getKey() != null) {
                ChannelSelectionKey key = ce.getKey();
                ShellChannelExt channel = (ShellChannelExt) key.channel();
                channelsMap.remove(key);
                try {
                    this.closeChannel(key, channel);
                } catch (IOException e1) {
                    logger.error("IO Exception while closing Channel", e1);
                }
            }
        } catch (Exception e) {
            logger.error("An error occured while selecting selector key", e);
        }

        try {
            if (keys != null) {
                for (FastSet.Record record = keys.head(), end = keys.tail(); (record = record.getNext()) != end;) {
                    ChannelSelectionKey key = (ChannelSelectionKey) keys.valueOf(record);
                    String txMessage = "";
                    String rxMessage = "";

                    if (!key.isValid()) {
                        ShellChannelExt channel = (ShellChannelExt) key.channel();
                        channelsMap.remove(key);
                        try {
                            this.closeChannel(key, channel);
                        } catch (IOException e1) {
                            logger.error("IO Exception while closing Channel", e1);
                        }
                    } else if (key.isAcceptable()) {
                        try {
                            accept();
                        } catch (IOException e) {
                            logger.error("IO Exception while operating on ChannelSelectionKey. Cannot accept new channel", e);
                        } catch (Exception e) {
                            logger.error("Exception while operating on ChannelSelectionKey. Cannot accept new channel", e);
                        }
                    } else if (key.isReadable()) {
                        ShellChannelExt chan = (ShellChannelExt) key.channel();
                        try {
                            Message msg = (Message) chan.receive();

                            if (msg != null) {
                                rxMessage = msg.toString();
                                logger.info("received command : " + rxMessage);
                                if (rxMessage.compareTo("disconnect") == 0) {
                                    txMessage = "Bye";

                                    if (this.securityDomain != null) {
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put(AUDIT_MESSAGE, "logout success");
                                        putPrincipal(map, chan.getPrincipal());
                                        this.securityContext.getAuditManager().audit(new AuditEvent(AuditLevel.SUCCESS, map));
                                    }

                                    chan.send(messageFactory.createMessage(txMessage));
                                } else if (this.securityDomain != null && chan.getUserName() == null) {
                                    // The first incoming message should be username
                                    chan.setUserName(rxMessage);
                                    txMessage = " ";
                                    chan.send(messageFactory.createMessage(txMessage));
                                    // TODO Authentication
                                } else if (this.securityDomain != null && chan.getPassword() == null) {
                                    // The second incoming message should be
                                    // password
                                    chan.setPassword(rxMessage);
                                    txMessage = "";

                                    if (!isAuthManagementLoaded()) {
                                        logger.error("Cant authenticate because AuthenticationManagement is null!");

                                    } else {
                                        chan.setPrincipal(new SimplePrincipal(chan.getUserName()));
                                        boolean isValid = this.isValid(chan.getPrincipal(), chan.getPassword());
                                        if (!isValid) {
                                            chan.send(messageFactory.createMessage(CONNECTED_AUTHENTICATION_FAILED));
                                            logger.warn(String.format("Authentication to CLI failed for username=%s",
                                                    chan.getUserName()));
                                            txMessage = "Bye";
                                        } else {

                                            // Audit Stuff
                                            this.securityContext = SecurityContextFactory
                                                    .createSecurityContext(getLocalSecurityDomain());

                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put(AUDIT_MESSAGE, "login success");
                                            putPrincipal(map, chan.getPrincipal());
                                            this.securityContext.getAuditManager()
                                                    .audit(new AuditEvent(AuditLevel.SUCCESS, map));

                                            txMessage = " ";
                                            chan.send(messageFactory.createMessage(txMessage));
                                        }
                                    }

                                } else {
                                    String[] options = rxMessage.split(" ");
                                    ShellExecutor shellExecutor = null;
                                    for (FastList.Node<ShellExecutor> n = this.shellExecutors.head(), end1 = this.shellExecutors
                                            .tail(); (n = n.getNext()) != end1;) {
                                        ShellExecutor value = n.getValue();
                                        if (value.handles(options[0])) {
                                            shellExecutor = value;
                                            break;
                                        }
                                    }

                                    if (shellExecutor == null) {
                                        logger.warn(String.format(
                                                "Received command=\"%s\" for which no ShellExecutor is configured ",
                                                rxMessage));

                                        if (this.securityDomain != null) {
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put(AUDIT_COMMAND, rxMessage);
                                            map.put(AUDIT_COMMAND_RESPONSE, "Invalid command");
                                            putPrincipal(map, chan.getPrincipal());
                                            this.securityContext.getAuditManager().audit(new AuditEvent(AuditLevel.INFO, map));
                                        }

                                        chan.send(messageFactory.createMessage("Invalid command"));
                                    } else {
                                        ShellExecutorTask task = new ShellExecutorTask(scheduler, shellExecutor, rxMessage,
                                                options, chan, key);
                                        scheduler.submit(task, Scheduler.MANAGEMENT_QUEUE);
                                    }
                                }

                                if (txMessage.length() > 0) {
                                    logger.info("Channel has something to write: " + txMessage);
                                    if (txMessage.compareTo("Bye") == 0) {
                                        ShellChannelExt channel = (ShellChannelExt) key.channel();
                                        channelsMap.remove(key);
                                        try {
                                            this.closeChannel(key, channel);
                                        } catch (IOException e1) {
                                            logger.error("IO Exception while closing Channel", e1);
                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            logger.error(
                                    "IO Exception while operating on ChannelSelectionKey. Client CLI connection will be closed now",
                                    e);
                            try {
                                this.closeChannel(key, chan);
                            } catch (IOException e1) {
                                logger.error("IO Exception while closing Channel", e1);
                            }
                        } catch (Exception e) {
                            logger.error(
                                    "Exception while operating on ChannelSelectionKey. Client CLI connection will be closed now",
                                    e);
                            try {
                                this.closeChannel(key, chan);
                            } catch (IOException e1) {
                                logger.error("IO Exception while closing Channel", e1);
                            }
                        }

                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (this.started)
            scheduler.submit(this, Scheduler.MANAGEMENT_QUEUE);

        return 0;
    }

    private void accept() throws IOException {
        ShellChannelExt channel = (ShellChannelExt) serverChannel.accept();
        if (logger.isDebugEnabled()) {
            logger.info("Accepting client connection. Remote Address= " + channel.getRemoteAddress());
        }

        ChannelSelectionKey skey = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        channelsMap.put(skey, channel);

        if (this.securityDomain == null) {
            channel.send(messageFactory.createMessage(String.format(CONNECTED_MESSAGE, this.version.getProperty("name"),
                    this.version.getProperty("version"), this.version.getProperty("vendor"))));
        } else {
            String tmp = String.format(CONNECTED_MESSAGE, this.version.getProperty("name"), this.version.getProperty("version"),
                    this.version.getProperty("vendor"));
            tmp = tmp + " " + CONNECTED_AUTHENTICATING_MESSAGE;
            channel.send(messageFactory.createMessage(tmp));
        }
    }

    private void closeChannel(ChannelSelectionKey key, ShellChannel channel) throws IOException {
        key.cancel();
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                logger.error("Error closing channel", e);
            }

            // if (logger.isDebugEnabled()) {
            logger.info("Closed client connection. Remote Address= " + channel.getRemoteAddress());
            // }
        }
        // skey.cancel();
        // skey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    class SingleTaskCallable implements Callable<String> {
        private String[] options;
        private ShellExecutor shellExecutor;

        public SingleTaskCallable(ShellExecutor shellExecutor, String[] options) {
            this.shellExecutor = shellExecutor;
            this.options = options;
        }

        @Override
        public String call() throws Exception {
            return shellExecutor.execute(this.options);
        }
    }

    class ShellExecutorTask extends Task {

        private ShellExecutor shellExecutor;
        private String rxMessage;
        private String[] options;
        private ShellChannelExt chan;
        private ChannelSelectionKey key;

        public ShellExecutorTask(Scheduler scheduler, ShellExecutor shellExecutor, String rxMessage, String[] options,
                ShellChannelExt chan, ChannelSelectionKey key) {
            super(scheduler);
            this.shellExecutor = shellExecutor;
            this.rxMessage = rxMessage;
            this.options = options;
            this.chan = chan;
            this.key = key;
        }

        @Override
        public int getQueueNumber() {
            return Scheduler.MANAGEMENT_QUEUE;
        }

        @Override
        public long perform() {
            try {
                String txMessage = null;

                Future<String> future = executor.submit(new SingleTaskCallable(shellExecutor, options));
                try {
                    txMessage = future.get(EXECUTION_TIMEOUT, TimeUnit.SECONDS);
                } catch (Exception ex) {
                    logger.error("An error occured while waiting on command response", ex);
                }

                if (securityDomain != null) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(ShellServer.AUDIT_COMMAND, rxMessage);
                    map.put(ShellServer.AUDIT_COMMAND_RESPONSE, txMessage);
                    putPrincipal(map, chan.getPrincipal());
                    securityContext.getAuditManager().audit(new AuditEvent(AuditLevel.INFO, map));
                }

                if (txMessage == null)
                    chan.send(messageFactory.createMessage("Operation has timed out on server"));
                else
                    chan.send(messageFactory.createMessage(txMessage));
            } catch (IOException e) {
                logger.error("IO Exception while operating on ChannelSelectionKey. Client CLI connection will be closed now",
                        e);
                try {
                    closeChannel(key, chan);
                } catch (IOException e1) {
                    logger.error("IO Exception while closing Channel", e1);
                }
            } catch (Exception e) {
                logger.error("Exception while operating on ChannelSelectionKey. Client CLI connection will be closed now", e);
                try {
                    closeChannel(key, chan);
                } catch (IOException e1) {
                    logger.error("IO Exception while closing Channel", e1);
                }
            }
            return 0;
        }

    }
}
