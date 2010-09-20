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
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;
import org.mobicents.protocols.ss7.mtp.provider.MtpListener;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.Selector;
import org.mobicents.protocols.ss7.sccp.impl.ud.UnitDataImpl;
import org.mobicents.protocols.ss7.sccp.impl.ud.XUnitDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.ud.MessageType;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public class SccpProviderImpl implements SccpProvider, MtpListener {
	//FIXME: Make this abstract class.
    private final static Logger logger = Logger.getLogger(SccpProviderImpl.class);
    protected static final ThreadGroup THREAD_GROUP = new ThreadGroup("Sccp-Provider");
    public static final String CONFIG_PROVIDER = "sccp.provider";
    public static final String CONFIG_OPC = "sccp.opc";
    public static final String CONFIG_DPC = "sccp.dpc";
    public static final String CONFIG_SLS = "sccp.sls";
    public static final String CONFIG_SSI = "sccp.ssi";
    public static final String CONFIG_THREADS = "sccp.threads";    //protected List<SccpListener> listeners = new ArrayList<SccpListener>();
    protected SccpListener listener = null;
    protected RoutingLabel routingLabel;
    protected Executor executor;
    protected int threadCount = 4;    //Linksets
    private List<MtpProvider> linksets;

    //route selector
    private Selector selector;
    
    public SccpProviderImpl() {
        //initialize route selector
        selector = new Selector();
    }

    /* (non-Javadoc)
     * @see org.mobicents.protocols.ss7.sccp.SccpProvider#configure(java.util.Properties)
     */
    public void configure(Properties props) throws ConfigurationException {
        int opc = Integer.parseInt(props.getProperty(SccpProviderImpl.CONFIG_OPC));
        int dpc = Integer.parseInt(props.getProperty(SccpProviderImpl.CONFIG_DPC));
        int sls = Integer.parseInt(props.getProperty(SccpProviderImpl.CONFIG_SLS));
        int ssi = Integer.parseInt(props.getProperty(SccpProviderImpl.CONFIG_SSI));
        int si = Mtp3._SI_SERVICE_SCCP;
        this.routingLabel = new RoutingLabel(opc, dpc, sls, si, ssi);
        this.threadCount = Integer.parseInt(props.getProperty(SccpProviderImpl.CONFIG_THREADS, "" + threadCount));
        this.executor = Executors.newFixedThreadPool(this.threadCount);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.mobicents.protocols.ss7.sccp.SccpProvider#addSccpListener(org.mobicents
     * .protocols.ss7.sccp.SccpListener)
     */
    public void addSccpListener(SccpListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        this.listener = listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.mobicents.protocols.ss7.sccp.SccpProvider#removeSccpListener(org.
     * mobicents.protocols.ss7.sccp.SccpListener)
     */
    public void removeSccpListener(SccpListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        this.listener = null;
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.sccp.SccpProvider#start().
     */
    public void start() throws IllegalStateException {
        //check linksets
        if (linksets == null) {
            throw new IllegalStateException("Linkset is not defined");
        }

        //start executor
        executor = Executors.newFixedThreadPool(threadCount);

        //assign listener for each linkset provided and start linksets
        for (MtpProvider linkset : linksets) {
            linkset.setMtpListener(this);
            try {
                linkset.start();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.sccp.SccpProvider#stop().
     */
    public void stop() {
        //if linksets are not defined exit silently
        if (linksets == null) {
            return;
        }

        //stop linksets
        for (MtpProvider linkset : linksets) {
            linkset.stop();
        }
    }

    private void translateGlobalTitle(UnitDataImpl unitData) {
        //FIXME implement GT translation here
    }
    
    private void route(int opc, UnitDataImpl unitData) throws IOException {
        //select destination point code
        int dpc = selector.getAdjasentPointCode(opc, unitData.getCallingParty(), unitData.getCalledParty());
        
        //if dpc =-1 then message will be processed localy
        if (dpc == -1) {
            listener.onMessage(unitData.getCallingParty(), unitData.getCalledParty(), unitData.getData());
            return;
        }
        
        //if dpc was selected then lookup respecive linkset
        for (MtpProvider linkset : linksets) {
            if (linkset.getAdjacentPointCode() == dpc) {
                //FIXME append routing label
                linkset.send(unitData.getData());
            }
        }
    }
    
    public void receive(final byte[] msu) {
        MessageHandler handler = new MessageHandler(msu);
        executor.execute(handler);
    }

    protected byte[] encodeToMSU(SccpAddress calledParty, SccpAddress callingParty, byte[] data, RoutingLabel ar) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProtocolClassImpl pc = new ProtocolClassImpl(0, 0);
        UnitDataImpl unitData = new UnitDataImpl(pc, calledParty, callingParty, data);
        unitData.encode(out);
        byte[] buf = out.toByteArray();
        // this.mtp3.send(si, ssf,buf);
        // this.txBuffer.add(ByteBuffer.wrap(buf));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (ar != null) {
            // we know how to route!
            bos.write(ar.getBackRouteHeader());
            bos.write(buf);
            buf = bos.toByteArray();
        } else {
            // we must build it. damn that Dialogic...
            bos.write(routingLabel.getBackRouteHeader());
            bos.write(buf);
            buf = bos.toByteArray();
            if (logger.isInfoEnabled()) {
                logger.info("Sccp Provider default MTP3 Label: " + routingLabel);
            }

        }
        if (logger.isInfoEnabled()) {
            logger.info("Sending SCCP buff: " + Arrays.toString(buf));
        }
        return buf;
    }

    public void send(SccpAddress calledParty, SccpAddress callingParty, byte[] data, RoutingLabel ar) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setLinksets(List<MtpProvider> linksets) {
        this.linksets = linksets;
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.sccp.SccpProvider#send(org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, org.mobicents.protocols.ss7.sccp.parameter.SccpAddress, byte[]). 
     */
    public void send(SccpAddress calledParty, SccpAddress callingParty, byte[] data) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void linkUp() {
    }

    public void linkDown() {
    }

    private class MessageHandler implements Runnable {
        
        //MSU as input stream
        private ByteArrayInputStream data;
        
        protected MessageHandler(byte[] msu) {
            //skip routing label
            data = new ByteArrayInputStream(msu, 5, msu.length);
        }
        
        public void run() {
            //wrap stream with DataInputStream
            DataInputStream in = new DataInputStream(data);
            
            //determine message type
            int mt = 0;
            
            try {
                mt = in.readUnsignedByte();
            } catch (IOException e) {
            }
            //construct message according to media type
            switch (mt) {
                case MessageType.UDT:
                    UnitDataImpl unitData = new UnitDataImpl();
                    
                    try {
                        unitData.decode(in);
                    } catch (IOException e) {
                    }
                    
                    translateGlobalTitle(unitData);
                    
                    try {
                        route(0, unitData);
                    } catch (IOException e) {
                        logger.error("Can not route message: " + e.getMessage());
                    }
                    
                    break;
                // 0x11
                case MessageType.XUDT:
                    XUnitDataImpl xunitData = new XUnitDataImpl();
                    
                    try {
                        xunitData.decode(in);
                    } catch (IOException e) {
                    }

                    translateGlobalTitle(xunitData);
                    
                    try {
                        route(0, xunitData);
                    } catch (IOException e) {
                        logger.error("Can not route message: " + e.getMessage());
                    }
                    
                    break;
                default:
                    logger.error("Undefined message type, MT:" + mt);

            }
        }
    }
}
