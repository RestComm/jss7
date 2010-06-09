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

package org.mobicents.protocols.ss7.sccp;

import java.lang.reflect.Constructor;
import java.util.Properties;

import org.mobicents.protocols.ss7.stream.tcp.MTPProviderImpl;

/**
 *
 * @author Oleg Kulikov
 * @author baranowb
 */
public class SccpFactory {
    
	private final static SccpFactory _INSTANCE = new SccpFactory();
	
	
	public static final String _DRIVER_TCP = "TCP";
	private static final String _CLASS_TCP = "org.mobicents.protocols.ss7.sccp.impl.sctp.SccpSCTPProviderImpl";
	public static final String _DRIVER_INTEL = "INTEL";
	private static final String _CLASS_INTEL = "org.mobicents.protocols.ss7.sccp.impl.intel.SccpIntelHDCProviderImpl";
	public static final String _DRIVER_M3 = "M3";
	private static final String _CLASS_M3 = "org.mobicents.protocols.ss7.sccp.impl.m3ua.SccpM3UAProviderImpl";
    /** Creates a new instance of SccpFactory */
    private SccpFactory() {
    }
    
    public static SccpFactory getInstance()
    {
    	return _INSTANCE;
    }
    
    
    public SccpProvider getProvider(String driverName, Properties properties) {
    	if(properties==null)
		{
			throw new NullPointerException("Properties must not be null");
			
		}
    	
    	if(driverName==null)
		{
			throw new NullPointerException("Driver name must not be null");
			
		}
    
    	String className = null;
    	if(driverName.equals(_DRIVER_TCP))
    	{
    		className = _CLASS_TCP;
    	}else if(driverName.equals(_DRIVER_INTEL))
    	{
    		className = _CLASS_INTEL;	
    	}else if(driverName.equals(_DRIVER_M3))
    	{
    		className = _CLASS_M3;
    	}else
    	{
    		className = driverName;
    	}
    	
    	
    	try {
			Class cc = Class.forName(className);
			Constructor c =cc.getConstructor(Properties.class);
			SccpProvider provider = (SccpProvider) c.newInstance(properties);
	
			return provider;
		} catch (Exception e) {
			
			throw new IllegalArgumentException("Failed to create driver class.",e);
		}
    }
    
}
