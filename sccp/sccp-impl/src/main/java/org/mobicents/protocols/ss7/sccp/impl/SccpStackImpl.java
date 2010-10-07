/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.mtp.provider.MtpListener;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageType;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.message.XUnitData;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author baranowb
 *
 */
public class SccpStackImpl implements SccpStack, MtpListener {

    private State state = State.IDLE;    //provider ref, this can be real provider or pipe, for tests.
    private SccpProviderImpl sccpProvider;
    //Router, ref to objec tproviding routing info. It is passed to provider
    //Here we have it for faster access?
    private Router router;
    protected List<MtpProvider> linksets;    //main executor
    private Executor executor;
    private static final Logger logger = Logger.getLogger(SccpStackImpl.class);

    public SccpStackImpl() {
        sccpProvider = new SccpProviderImpl(this); //why this is present? 
    }

    /**
     * Constructor for test purposes only!
     * @param sccpProvider
     */
    public SccpStackImpl(SccpProvider sccpProvider) {
        //Oleg, this is not valid way to create stack, via such hack....
        this.sccpProvider = (SccpProviderImpl) sccpProvider;
        this.router = new DefaultRouterImpl(this);
        this.state = State.CONFIGURED;
    }

//    /*
//     * (non-Javadoc)
//     *
//     * @see
//     * org.mobicents.protocols.ss7.sccp.SccpStack#configure(java.util.Properties
//     * )
//     */
//    public void configure(Properties properties) throws ConfigurationException {
//        if (state != State.IDLE) {
//            throw new IllegalStateException("Stack already been configured or is already running!");
//        }
//        this.sccpProvider = new SccpProviderImpl(this);
////        this.sccpProvider.configure(properties);
//
//        this.state = State.CONFIGURED;
//    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpStack#getSccpProvider()
     */
    public SccpProvider getSccpProvider() {
        return sccpProvider;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpStack#start()
     */
    public void start() throws IllegalStateException, StartFailedException {
        logger.info("Starting ...");
        executor = Executors.newFixedThreadPool(1);
        if (linksets != null) {
            for (MtpProvider linkset : linksets) {
                linkset.setMtpListener(this);
                try {
                    linkset.start();
                } catch (Exception e) {
                    throw new StartFailedException("Failed to start MtpProvider: " + linkset, e);
                }
            }
        }
        this.state = State.RUNNING;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpStack#stop()
     */
    public void stop() {
    }

    /* (non-Javadoc)
     * @see org.mobicents.protocols.ss7.sccp.SccpStack#setRouter(org.mobicents.protocols.ss7.sccp.Router)
     */
    @Override
    public void setRouter(Router router) {
        //TODO: make router configurable through API(creation and route mgmgt)
        this.router = router;
    }

    private enum State {
        IDLE, CONFIGURED, RUNNING;
    }

    public void setLinksets(List<MtpProvider> linksets) {
        this.linksets = linksets;
    }

    public void setTransferType(int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //this method will be depricated
    public void linkUp() {
    }

    //this method will be depricated
    public void linkDown() {
    }

    public void receive(byte[] msu) {
        MessageHandler handler = new MessageHandler(msu);
        executor.execute(handler);
    }

    private MtpProvider findLinkset(int pc) {
        for (MtpProvider linkset : linksets) {
            if (linkset.getAdjacentPointCode() == pc) {
                return linkset;
            }
        }
        return null;
    }

    protected void send(SccpMessage message) throws IOException {
        MessageHandler handler = new MessageHandler(message);
        executor.execute(handler);
    }

    private void send(MtpProvider linkset, SccpMessage message) throws IOException {
        //prepare routing label
        int opc = linkset.getOriginalPointCode();
        int dpc = linkset.getAdjacentPointCode();
        int si = 3;//SCCP
        int ni = 2;//assume national
        int sls = 0;//mtp3 will select correct
        int ssi = ni << 2;

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        //encoding routing label
        bout.write((byte) ssi);
        bout.write((byte) (((ssi & 0x0F) << 4) | (si & 0x0F)));
        bout.write((byte) dpc);
        bout.write((byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6)));
        bout.write((byte) (opc >> 2));
        bout.write((byte) (((opc >> 10) & 0x0F) | ((sls & 0x0F) << 4)));

        ((SccpMessageImpl) message).encode(bout);
        linkset.send(bout.toByteArray());
    }

    private int findDestination(SccpAddress calledPartyAddress) {
        if (router != null) {
            return router.getRoute(calledPartyAddress, null);
        }
        return -1;
    }

    /**
     * Routes SCCP message.
     * 
     * @param address the called party address
     * @param message message to be routed
     */
    private void route(SccpAddress address, SccpMessage message) throws IOException {
        //address is explicit parameter because not each message contains it
        int pointCode = this.findDestination(address);
        if (pointCode == -1) {
            sccpProvider.notify(address, message);
            return;
        }

        MtpProvider linkset = this.findLinkset(pointCode);
        if (linkset == null) {
            logger.warn("Unknown route for " + pointCode);
            return;
        }

        send(linkset, message);
    }

    private class MessageHandler implements Runnable {
        //MSU as input stream
        private ByteArrayInputStream data;
        private SccpMessage message;

        protected MessageHandler(byte[] msu) {
            //skip routing label
            data = new ByteArrayInputStream(msu, 5, msu.length);
            message = null;
        }

        protected MessageHandler(SccpMessage message) {
            this.message = message;
        }

        private SccpMessage parse() throws IOException {
            // wrap stream with DataInputStream
            DataInputStream in = new DataInputStream(data);
            int sio = 0;
            sio = in.read() & 0xff;

            // getting service indicator
            int si = sio & 0x0f;

            //ignore message if this is not sccp service.
            if (si != 3) {
                return null;
            }

            // determine message type
            int mt = in.readUnsignedByte();
            return ((MessageFactoryImpl) sccpProvider.getMessageFactory()).createMessage(mt, in);
        }

        public void run() {
            if (message == null) {
                try {
                    message = parse();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            //not each message suppose routing or delivery to listner
            switch (message.getType()) {
                case MessageType.UDT:
                    UnitData udt = (UnitData) message;
                    try {
                        route(udt.getCalledPartyAddress(), udt);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                //XUDT message has extra fields then UDT so it also 
                //processed in general separately separately    
                case MessageType.XUDT:
                    //reassembly probably required
                    XUnitData xudt = (XUnitData) message;
                    try {
                        route(xudt.getCalledPartyAddress(), xudt);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
