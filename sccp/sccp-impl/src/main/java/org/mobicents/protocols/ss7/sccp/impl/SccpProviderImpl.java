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
import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;
import org.mobicents.protocols.ss7.mtp.provider.MtpListener;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.message.MessageType;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public class SccpProviderImpl implements SccpProvider, MtpListener, Serializable {
	//FIXME: Make this abstract class.
    protected static final transient ThreadGroup THREAD_GROUP = new ThreadGroup("SCCP");
    public static final String CONFIG_PROVIDER = "sccp.provider";
    public static final String CONFIG_OPC = "sccp.opc";
    public static final String CONFIG_DPC = "sccp.dpc";
    public static final String CONFIG_SLS = "sccp.sls";
    public static final String CONFIG_SSI = "sccp.ssi";
    public static final String CONFIG_THREADS = "sccp.threads";    //protected List<SccpListener> listeners = new ArrayList<SccpListener>();
    protected SccpListener listener = null;
    protected transient RoutingLabel routingLabel;
    protected transient Executor executor;
    protected int threadCount = 4;    //Linksets
    private transient List<MtpProvider> linksets;
    private String jndiName;
    private MessageFactoryImpl messageFactory = new MessageFactoryImpl();
    private ParameterFactoryImpl parameterFactory = new ParameterFactoryImpl();
    private Router router;
    private final transient static Logger logger = Logger.getLogger(SccpProviderImpl.class);

    public SccpProviderImpl() {
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
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
    public void start() throws Exception {
        //start executor
        executor = Executors.newFixedThreadPool(threadCount);

        //assign listener for each linkset provided and start linksets
        if (linksets != null) {
            for (MtpProvider linkset : linksets) {
                linkset.setMtpListener(this);
                try {
                    linkset.start();
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        }
        logger.info("Binding SCCP to the JNDI name: " + jndiName);        
        rebind();
        
        logger.info("Bound SCCP to the JNDI name: " + jndiName);
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

    public void receive(final byte[] msu) {
        MessageHandler handler = new MessageHandler(msu);
        executor.execute(handler);
    }

    public void send(SccpMessage message) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ((SccpMessageImpl) message).encode(out);
        linksets.get(0).send(out.toByteArray());
    }

    public void setLinksets(List<MtpProvider> linksets) {
        this.linksets = linksets;
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
            
            //getting service infrmation octet
            int sio = 0;
            try {
                sio = in.read() & 0xff;
            } catch (IOException e) {
            }
            
            //getting service indicator
            int si = sio & 0x0f;
            
            //ignore message if this is not sccp service.
            if (si != 3) {
                return;
            }

            //determine message type
            int mt = 0;

            try {
                mt = in.readUnsignedByte();
            } catch (IOException e) {
            }

            SccpMessageImpl message = null;
            try {
                message = messageFactory.createMessage(mt, in);
            } catch (IOException e) {
            }

            switch (message.getType()) {
                case MessageType.UDT:
                case MessageType.XUDT:
                    if (router == null) {
                        listener.onMessage(message);
                    }
                    //process messages
                    break;
            }
        }
    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public ParameterFactory getParameterFactory() {
        return parameterFactory;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    /**
     * Binds trunk object to the JNDI under the jndiName.
     */
    private void rebind() throws NamingException {
        Context ctx = new InitialContext();
        String tokens[] = jndiName.split("/");

        for (int i = 0; i < tokens.length - 1; i++) {
            if (tokens[i].trim().length() > 0) {
                try {
                    ctx = (Context) ctx.lookup(tokens[i]);
                } catch (NamingException e) {
                    ctx = ctx.createSubcontext(tokens[i]);
                }
            }
        }

        ctx.bind(tokens[tokens.length - 1], this);
    }
}
