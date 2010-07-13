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

import java.lang.reflect.Constructor;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.provider.intel.SccpIntelHDCProviderImpl;
import org.mobicents.protocols.ss7.sccp.impl.provider.stream.SccpDefaultProviderImpl;

/**
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
class SccpProviderFactory {

	private final static Logger logger = Logger.getLogger(SccpProviderFactory.class);
	public final static String DRIVER_INTEL = "intel_hdc";
	// public final static String DRIVER_M3UA = "M3UA";
	public final static String DRIVER_DEFAULT = "default";

	/** Creates a new instance of SccpPeer */
	public SccpProviderFactory() {

	}

	public SccpProvider getProvider(Properties properties) throws ConfigurationException {

		String driver = properties.getProperty(AbstractSccpProviderImpl.CONFIG_PROVIDER, DRIVER_DEFAULT);
		SccpProvider p = null;
		if (driver.toLowerCase().equals(DRIVER_INTEL)) {
			p = new SccpIntelHDCProviderImpl();
			// } else if (driver.equals(DRIVER_M3UA)) {
			// return new SccpM3UAProviderImpl(properties);
		} else if (driver.toLowerCase().equals(DRIVER_DEFAULT)) {
			p = new SccpDefaultProviderImpl();
			
		} else
		{
			
			try {
				Class clazz = Class.forName(driver);
				Constructor<SccpProvider> c =  clazz.getConstructor(null);
				p =  c.newInstance(null);
				
				
			}catch (Exception e) {
				throw new UnsupportedOperationException("Failed to create driver for class: "+driver,e);
			} 
		
		}
		p.configure(properties);
		return p;
	}
}


