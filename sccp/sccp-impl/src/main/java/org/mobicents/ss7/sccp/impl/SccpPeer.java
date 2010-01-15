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
package org.mobicents.ss7.sccp.impl;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.ss7.sccp.SccpProvider;
import org.mobicents.ss7.sccp.impl.intel.SccpIntelHDCProviderImpl;
import org.mobicents.ss7.sccp.impl.m3ua.SccpM3UAProviderImpl;
import org.mobicents.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.ss7.sccp.impl.sctp.SccpSCTPProviderImpl;
import org.mobicents.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author Oleg Kulikov
 */
public class SccpPeer {
    
	private final static Logger logger = Logger.getLogger(SccpPeer.class);
    public final static String INTEL_DRIVER = "INTEL_HDC";
    public final static String M3UA = "M3UA";
    public final static String ZAP = "ZAP";
    private String driver;
    
    /** Creates a new instance of SccpPeer */
    public SccpPeer(String driver) {
        this.driver = driver;
    }
    
    public SccpProvider getProvider(String fileName) throws Exception {
    	if(logger.isInfoEnabled())
    	{
    		logger.info("Loading configuration properties:  "+fileName);
    	}
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream(fileName));
        
        if (driver.equals(INTEL_DRIVER)) {
            return new SccpIntelHDCProviderImpl(properties);
        } else if (driver.equals(M3UA)) {
            return new SccpM3UAProviderImpl(properties);
        } else if(driver.equals(ZAP))
        {
        	return new SccpSCTPProviderImpl(properties);
        }else return new SccpProviderImpl(){

			public void send(SccpAddressImpl calledParty,
					SccpAddressImpl callingParty, byte[] data)
					throws IOException {
				// TODO Auto-generated method stub
				
			}

			public void shutdown() {
				// TODO Auto-generated method stub
				
			}

			public void send(SccpAddress calledParty, SccpAddress callingParty,
					byte[] data) throws IOException {
				// TODO Auto-generated method stub
				
			}};
    }
}
