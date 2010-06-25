/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.protocols.ss7.sccp.impl;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.RoutingLabel;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.intel.SccpIntelHDCProviderImpl;
import org.mobicents.protocols.ss7.sccp.impl.m3ua.SccpM3UAProviderImpl;
import org.mobicents.protocols.ss7.sccp.impl.sctp.SccpDefaultProviderImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;

/**
 *
 * @author Oleg Kulikov
 */
class SccpProviderFactory {
    
	private final static Logger logger = Logger.getLogger(SccpProviderFactory.class);
    public final static String DRIVER_INTEL = "INTEL_HDC";
    public final static String DRIVER_M3UA = "M3UA";
    public final static String DRIVER_DEFAULT = "DEFAULT";

    
    /** Creates a new instance of SccpPeer */
    public SccpProviderFactory() {

    }
    
    public SccpProvider getProvider(Properties properties){
    	
    	
    	String driver = properties.getProperty(SccpProviderImpl.CONFIG_PROVIDER, DRIVER_DEFAULT);
    	
        
        if (driver.equals(DRIVER_INTEL)) {
            return new SccpIntelHDCProviderImpl(properties);
        } else if (driver.equals(DRIVER_M3UA)) {
            return new SccpM3UAProviderImpl(properties);
        } else if (driver.equals(DRIVER_DEFAULT))
        {
        	return new SccpDefaultProviderImpl(properties);
        }else return new SccpProviderImpl(properties){

			

			@Override
			public void stop() {
				// TODO Auto-generated method stub
				
			}

			public void addSccpListener(SccpListener listener) {
				// TODO Auto-generated method stub
				
			}

			public void removeSccpListener(SccpListener listener) {
				// TODO Auto-generated method stub
				
			}

			public void send(SccpAddress calledParty, SccpAddress callingParty, byte[] data, RoutingLabel ar) throws IOException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void start() throws IllegalStateException, StartFailedException {
				// TODO Auto-generated method stub
				
			}

			};
    }
}



//if(logger.isInfoEnabled())
//{
//	logger.info("Loading configuration properties:  "+fileName+" from: "+getClass().getResource(fileName));
//}
//Properties properties = new Properties();
//InputStream is = getClass().getResourceAsStream(fileName);
//if(is == null)
//{
//	//its not in local CL - that means its just not aroung this class, not in the same jar, lets check global
//	logger.info("Failed to find local resource: "+fileName+", checking context classloader: "+Thread.currentThread().getContextClassLoader().getResource(fileName));
//	is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
//}
//properties.load(is);
