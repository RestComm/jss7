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
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.ParseException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.mtp.provider.MtpListener;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;
import org.mobicents.protocols.ss7.mtp.provider.MtpProviderFactory;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageType;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public class SccpProviderImpl extends AbstractSccpProvider implements SccpProvider, MtpListener, Serializable {
	//TODO: Make oleg suffer.
	private static final ThreadGroup THREAD_GROUP = new ThreadGroup("SCCP");
	public static final String CONFIG_THREADS = "sccp.threads";
	private static final Logger logger = Logger.getLogger(SccpProviderImpl.class);
    
   
	private List<MtpProvider> linksets;

	//private HashMap<SccpAddress, SccpListener> listeners = new HashMap<SccpAddress, SccpListener>();
	private SccpListener listener;
	
	private SccpStackImpl stack;

	private Executor executor;
	private int threadCount = 2; 
    
    SccpProviderImpl(SccpStackImpl sccpStack) {
    	super(sccpStack);
    }

    /* (non-Javadoc)
     * @see org.mobicents.protocols.ss7.sccp.SccpProvider#configure(java.util.Properties)
     */
   void configure(Properties props) throws ConfigurationException {
  	  this.threadCount = Integer.parseInt(props.getProperty(SccpProviderImpl.CONFIG_THREADS, "" + threadCount));
      this.executor = Executors.newFixedThreadPool(this.threadCount, new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(THREAD_GROUP,r);
		}
	});
      //do specific conf, we need linksets
      
      this.linksets = new ArrayList<MtpProvider>();
      
      //TODO: add support for more linksets
      MtpProvider mtp = MtpProviderFactory.getInstance().getProvider(props);
      this.linksets.add(mtp);
    }

//    /*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * org.mobicents.protocols.ss7.sccp.SccpProvider#registerSccpListener(org
//	 * .mobicents.protocols.ss7.sccp.parameter.SccpAddress,
//	 * org.mobicents.protocols.ss7.sccp.SccpListener)
//	 */
//	@Override
//	public void registerSccpListener(SccpAddress localAddress, SccpListener listener) {
//		if (listener == null) {
//            throw new NullPointerException("Listener must not be null.");
//        }
//    	if (localAddress == null) {
//            throw new NullPointerException("Address must not be null.");
//        }
//    	
//    	if(this.listeners.containsKey(localAddress))
//    	{
//    		throw new IllegalArgumentException("Listener for: "+localAddress+", is already registered.");
//    	}
//    	this.listeners.put(localAddress, listener);
//
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * org.mobicents.protocols.ss7.sccp.SccpProvider#deregisterSccpListener(
//	 * org.mobicents.protocols.ss7.sccp.parameter.SccpAddress)
//	 */
//	@Override
//	public void deregisterSccpListener(SccpAddress localAddress) {
//		this.listeners.remove(localAddress);
//
//	}

   public void addSccpListener(SccpListener sccpListener)
   {
	   this.listener = sccpListener;
   }
   public void removeSccpListener(SccpListener sccpListener)
   {
	   this.listener = null;
   }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.sccp.SccpProvider#start().
     */
    public void start() throws StartFailedException {
        //start executor

        //assign listener for each linkset provided and start linksets
        //TODO: make this via reassign op.
        // - remove from old
        // - add in new
        // - update linkup info
        if (linksets != null) {
            for (MtpProvider linkset : linksets) {
                linkset.setMtpListener(this);
                try {
                    linkset.start();
                } catch (Exception e) {
                    throw new StartFailedException("Failed to start MtpProvider: "+linkset,e);
                }
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
        	linkset.setMtpListener(null);
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

    public void linkUp() {
    	//TODO:
    }

    public void linkDown() {
    	//TODO
    }

    
    
    protected void deliver(SccpMessage message)
	{
		

        switch (message.getType()) {
            case MessageType.UDT:
            case MessageType.XUDT:
            	
//            	 if (stack.getRouter() != null) {
//            		 int apc = stack.getRouter().getRoute(message.getCalledPartyAddress(), message.getCallingPartyAddress());
//            	    	if(apc>0)
//            	    	{
//            	    		//its remote
//	            	    	deliverExternaly(message);
//            	    	}else
//            	    	{
//            	    		deliverLocaly(message);
//            	    	}
//            	    	
//         			 return;
//                  }else
                  {
                 	 deliverLocaly(message);
                  }
                //process messages
                break;
        }
    }

    protected void deliverExternaly(SccpMessage message) //TODO: add throws.
    {
    	
    	MtpProvider mtp = this.linksets.get(0);
    	try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			((SccpMessageImpl)message).encode(baos);
			byte[] b = baos.toByteArray();
			mtp.send(b);
		}catch(IOException e)
		{
			if(logger.isEnabledFor(Level.ERROR))
    		{
    			logger.error("Faield to send MSU: "+message,e);
    		}
		}
//    	int apc = stack.getRouter().getRoute(message.getCalledPartyAddress(), message.getCallingPartyAddress());
//    	if(apc>0)
//    	{
//    		//check for link
//    		for(MtpProvider mtp:this.linksets)
//    		{
//    			if(mtp.getAdjacentPointCode() == apc)
//    			{
//    				try{
//	    				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	    				((SccpMessageImpl)message).encode(baos);
//	    				byte[] b = baos.toByteArray();
//	    				mtp.send(b);
//    				}catch(IOException e)
//    				{
//    					if(logger.isEnabledFor(Level.ERROR))
//    		    		{
//    		    			logger.error("Faield to send MSU: "+message,e);
//    		    		}
//    				}
//    			}
//    		}
//    	}else
//    	{
//    		if(logger.isEnabledFor(Level.WARN))
//    		{
//    			logger.warn("Router does not have info how to route: "+message);
//    		}
//    	}
    }
     
    protected void deliverLocaly(SccpMessage message)
    {
    	//SccpListener lst = listeners.get(message.getCalledPartyAddress());
    	SccpListener lst = listener;
    	if(lst!=null)
    	{
    		//eat careless user exception.
    		try{
    			lst.onMessage(message);
    		}catch(RuntimeException re)
    		{
    			if(logger.isEnabledFor(Level.ERROR))
    			{
    				logger.error("Failed during SCCP message delivery.",re);
    			}
    		}
    	}else
    	{
    		if(logger.isEnabledFor(Level.WARN))
    		{
    			logger.warn("No listener for: "+message.getCalledPartyAddress());
    		}
    	}
    }

	/**
	 * Checks routing info for ougoing message. 
	 * 
	 * @param message
	 
	 */
	public void checkOutgoingRoutes(SccpMessage message)
	{
		//check if PCs are -1, that means it should be local?
		if(message.getCalledPartyAddress().getSignalingPointCode()<0 || stack.getRouter() == null)
		{
			//we do local try
			deliverLocaly(message);
		}else
		{
			deliverExternaly(message);
		}
	}

	private class MessageHandler implements Runnable {
        //MSU as input stream
        private ByteArrayInputStream data;

        protected MessageHandler(byte[] msu) {
            //skip routing label
            data = new ByteArrayInputStream(msu, 5, msu.length);
        }

        public void run() {
        	try{
        	SccpMessageImpl msg = parse(data);
        	//if msg == null, its not for us?
        	if(msg!=null) 
        	{
        		deliver(msg);
        	}
        	
        	}catch(ParseException pe)
        	{
        		pe.printStackTrace();
        	}
    }
   }


//    /**
//     * Binds trunk object to the JNDI under the jndiName.
//     */
//    private void rebind() throws NamingException {
//        Context ctx = new InitialContext();
//        String tokens[] = jndiName.split("/");
//
//        for (int i = 0; i < tokens.length - 1; i++) {
//            if (tokens[i].trim().length() > 0) {
//                try {
//                    ctx = (Context) ctx.lookup(tokens[i]);
//                } catch (NamingException e) {
//                    ctx = ctx.createSubcontext(tokens[i]);
//                }
//            }
//        }
//
//        ctx.bind(tokens[tokens.length - 1], this);
//    }
}
