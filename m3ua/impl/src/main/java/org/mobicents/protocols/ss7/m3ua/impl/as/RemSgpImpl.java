package org.mobicents.protocols.ss7.m3ua.impl.as;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.M3UASelectionKey;
import org.mobicents.protocols.ss7.m3ua.M3UASelector;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.Sgp;
import org.mobicents.protocols.ss7.m3ua.impl.CommunicationListener.CommunicationState;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAOAMMessages;
import org.mobicents.protocols.ss7.m3ua.impl.scheduler.M3UAScheduler;
import org.mobicents.protocols.ss7.m3ua.impl.tcp.TcpProvider;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

public class RemSgpImpl implements Sgp {

    private static Logger logger = Logger.getLogger(RemSgpImpl.class);

    private FastList<As> appServers = new FastList<As>();
    private FastList<AspFactory> aspfactories = new FastList<AspFactory>();

    private M3UAProvider m3uaProvider;
    private M3UASelector selector;

    private boolean started = false;
    
    private M3UAScheduler m3uaScheduler = new M3UAScheduler();

    public RemSgpImpl() {
    }

    public PayloadData poll() {
        for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
            As as = n.getValue();
            PayloadData payload = as.poll();
            if (payload != null) {
                return payload;
            }
        }
        return null;
    }

    public void send(byte[] msu) throws Exception {
        ProtocolData data = m3uaProvider.getParameterFactory().createProtocolData(0, msu);

        PayloadData payload = (PayloadData) this.m3uaProvider.getMessageFactory().createMessage(
                MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD);
        payload.setData(data);

        // TODO : Algo to select correct AS depending on above ProtocolData and
        // Routing Key. Also check if AS is ACTIVE else throw error?
        As as = this.appServers.get(0);
        payload.setRoutingContext(as.getRoutingContext());
        as.write(payload);

    }

    public void start() throws IOException {
        m3uaProvider = TcpProvider.provider();
        selector = m3uaProvider.openSelector();

        this.started = true;
    }

    /**
     * Expected command is m3ua as create rc <rc> <ras-name>
     * 
     * @param args
     * @return
     * @throws Exception
     */
    public As createAppServer(String args[]) throws Exception {

        if (args.length < 6) {
            // minimum 6 args needed
            throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
        }

        String rcKey = args[3];
        if (rcKey == null || rcKey.compareTo("rc") != 0) {
            throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
        }

        String rc = args[4];
        if (rc == null) {
            throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
        }

        RoutingContext rcObj = m3uaProvider.getParameterFactory().createRoutingContext(
                new long[] { Long.parseLong(rc) });

        String name = args[5];

        for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
            As as = n.getValue();
            if (as.getName().compareTo(name) == 0) {
                throw new Exception(String.format(M3UAOAMMessages.CREATE_AS_FAIL_NAME_EXIST, name));
            }
        }

        AsImpl as = new AsImpl(name, rcObj, null, null, this.m3uaProvider);
        m3uaScheduler.execute(as.getFSM());
        appServers.add(as);
        return as;

    }

    /**
     * Command to craete ASPFactory is "m3ua asp create ip <local-ip> port
     * <local-port> remip <remip> remport <remport> <asp-name>"
     * 
     * @param args
     * @return
     */
    public AspFactory createAspFactory(String[] args) throws Exception {

        if (args.length != 12) {
            throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
        }

        if (args[3] == null || args[3].compareTo("ip") != 0) {
            throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
        }

        if (args[4] == null) {
            throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
        }
        String ip = args[4];

        if (args[5] == null || args[5].compareTo("port") != 0) {
            throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
        }

        int port = Integer.parseInt(args[6]);

        if (args[7] == null || args[7].compareTo("remip") != 0) {
            throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
        }

        if (args[8] == null) {
            throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
        }
        String remIp = args[8];

        if (args[9] == null || args[9].compareTo("remport") != 0) {
            throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
        }

        int remPort = Integer.parseInt(args[10]);

        if (args[11] == null) {
            throw new Exception(M3UAOAMMessages.INVALID_COMMAND);
        }

        String name = args[11];

        for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
            AspFactory fact = n.getValue();
            if (fact.getName().compareTo(name) == 0) {
                throw new Exception(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_NAME_EXIST, name));
            }

            if (fact.getIp().compareTo(ip) == 0 && fact.getPort() == port) {
                throw new Exception(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_IPPORT_EXIST, ip, port));
            }
        }

        AspFactory factory = new LocalAspFactory(name, ip, port, remIp, remPort, this.m3uaProvider);
        aspfactories.add(factory);
        return factory;
    }

    public Asp assignAspToAs(String asName, String aspName) throws Exception {
        // check ASP and AS exist with given name
        As as = null;
        for (FastList.Node<As> n = appServers.head(), end = appServers.tail(); (n = n.getNext()) != end;) {
            if (n.getValue().getName().compareTo(asName) == 0) {
                as = n.getValue();
                break;
            }
        }

        if (as == null) {
            throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_AS, asName));
        }

        AspFactory aspFactroy = null;
        for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
            if (n.getValue().getName().compareTo(aspName) == 0) {
                aspFactroy = n.getValue();
                break;
            }
        }

        if (aspFactroy == null) {
            throw new Exception(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_NO_ASP, aspName));
        }

        Asp asp = aspFactroy.createAsp();
        m3uaScheduler.execute(asp.getFSM());
        as.addAppServerProcess(asp);

        return asp;
    }

    public void startAsp(String aspName) throws Exception {

        LocalAspFactory localAspFact = (LocalAspFactory) this.getAspFactory(aspName);

        if (localAspFact == null) {
            throw new Exception(String.format("No ASP found by name %s", aspName));
        }

        M3UAChannel channel = this.m3uaProvider.openChannel();
        channel.bind(new InetSocketAddress(localAspFact.getIp(), localAspFact.getPort()));

        M3UASelectionKey skey = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        skey.attach(localAspFact);

        channel.connect(new InetSocketAddress(localAspFact.getRemIp(), localAspFact.getRemPort()));

        if (channel.isConnectionPending()) {

            // TODO Loop? Or may be sleep for while?
            while (!channel.isConnected()) {
                channel.finishConnect();
            }
        }
        
        localAspFact.setChannel(channel);
        
        localAspFact.start();
        logger.info(String.format("Started ASP name=%s local-ip=%s local-pot=%d rem-ip=%s rem-port=%d", localAspFact
                .getName(), localAspFact.getIp(), localAspFact.getPort(), localAspFact.getRemIp(), localAspFact
                .getRemPort()));

        localAspFact.onCommStateChange(CommunicationState.UP);
    }

    public void stopAsp(String aspName) throws Exception {
        LocalAspFactory localAspFact = (LocalAspFactory) this.getAspFactory(aspName);

        if (localAspFact == null) {
            throw new Exception(String.format("No ASP found by name %s", aspName));
        }

        localAspFact.stop();
    }

    public void stop() {
        // Nothing to do
    }

    public void perform() throws IOException {

        if (!started) {
            return;
        }
        
        m3uaScheduler.tick();
        
        FastList<M3UASelectionKey> selections = selector.selectNow();

        for (FastList.Node<M3UASelectionKey> n = selections.head(), end = selections.tail(); (n = n.getNext()) != end;) {
            M3UASelectionKey key = n.getValue();
            if (key.isReadable()) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Transmitting data from M3UA channel to hardware");
                }
                read(key);
            }

            if (key.isWritable()) {
                write(key);
            }
        }
    }

    private void read(M3UASelectionKey key) {
        M3UAChannel channel = null;
        try {
            channel = (M3UAChannel) key.channel();
            M3UAMessage message = channel.receive();
            ((AspFactory) key.attachment()).read(message);
        } catch (IOException e) {
            logger.error(String.format("IOException while reading for Aspfactory name=%s ", ((AspFactory) key
                    .attachment()).getName()), e);

            try {
                channel.close();
            } catch (Exception ex) {
                // Ignore
            }

            ((AspFactory) key.attachment()).onCommStateChange(CommunicationState.LOST);

        }
    }

    private void write(M3UASelectionKey key) {
        M3UAChannel channel = null;
        try {
            channel = (M3UAChannel) key.channel();
            AspFactory factory = ((AspFactory) key.attachment());
            M3UAMessage message = null;
            while ((message = factory.txPoll()) != null) {
                channel.send(message);
            }
        } catch (IOException e) {
            logger.error(String.format("IOException while writting for Aspfactory name=%s ", ((AspFactory) key
                    .attachment()).getName()), e);

            try {
                channel.close();
            } catch (Exception ex) {
                // Ignore
            }

            // TODO Transition to COMM_DOWN
            ((AspFactory) key.attachment()).onCommStateChange(CommunicationState.LOST);

        }
    }

    private AspFactory getAspFactory(String aspName) {
        for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
            AspFactory aspFactory = n.getValue();
            if (aspFactory.getName().compareTo(aspName) == 0) {
                return aspFactory;
            }
        }
        return null;
    }
}
