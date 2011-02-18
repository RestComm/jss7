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
import org.mobicents.protocols.ss7.m3ua.impl.SigGateway;
import org.mobicents.protocols.ss7.m3ua.impl.CommunicationListener.CommunicationState;
import org.mobicents.protocols.ss7.m3ua.impl.tcp.TcpProvider;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

public class RemSigGatewayImpl implements SigGateway {

    private static Logger logger = Logger.getLogger(RemSigGatewayImpl.class);

    private FastList<As> appServers = new FastList<As>();
    private FastList<AspFactory> aspfactories = new FastList<AspFactory>();

    private M3UAProvider m3uaProvider;
    private M3UASelector selector;

    private boolean started = false;

    public RemSigGatewayImpl() {
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

    public As createAppServer(String name, RoutingContext rc, RoutingKey rk, TrafficModeType trMode) {
        // TODO : Check for duplication of RoutingKey and name
        AsImpl as = new AsImpl(name, rc, rk, trMode, this.m3uaProvider);
        appServers.add(as);
        return as;
    }

    public AspFactory createAspFactory(String name, String ip, int port, String remIp, int remPort) {
        // TODO : Check for duplication of ip and port and name
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
            throw new Exception(String.format("No Application Server found for given name %s", asName));
        }

        AspFactory aspFactroy = null;
        for (FastList.Node<AspFactory> n = aspfactories.head(), end = aspfactories.tail(); (n = n.getNext()) != end;) {
            if (n.getValue().getName().compareTo(aspName) == 0) {
                aspFactroy = n.getValue();
                break;
            }
        }

        if (aspFactroy == null) {
            throw new Exception(String.format("No Application Server Process found for given name %s", aspName));
        }

        Asp asp = aspFactroy.createAsp();
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
