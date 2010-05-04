package org.mobicents.protocols.ss7.stream;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Properties;

import org.mobicents.protocols.ss7.stream.tcp.M3UserConnector;
import org.mobicents.protocols.ss7.stream.tcp.MTPProviderImpl;


public class MTPProviderFactory {

	private static final MTPProviderFactory instance = new MTPProviderFactory();
	
	public static final String _POPERTY_DRIVER_CLASS="driver";
	
	public static final String _DRIVER_TCP="TCP_IP";
	private final static String[] _DRIVER_SUPPORTED = new String[]{_DRIVER_TCP};
	public MTPProvider getProvider(Properties properties) throws IllegalArgumentException
	{
		if(properties==null)
		{
			throw new NullPointerException("Properties must not be null");
			
		}
		String d = properties.getProperty(_POPERTY_DRIVER_CLASS);
		if(d == null)
		{
			throw new IllegalArgumentException("There is no property indicating driver class! Add: "+_POPERTY_DRIVER_CLASS+", supported values: "+Arrays.toString(_DRIVER_SUPPORTED)+", or fqn class name");
		}
		
		if(d.equals(_DRIVER_TCP))
		{
			try {
				
				M3UserConnector mtpProvider = new M3UserConnector(properties);
				mtpProvider.start();
				return mtpProvider;
			} catch (Exception e) {
				
				throw new IllegalArgumentException("Failed to create driver class.",e);
			}
		}else
		{
			try {
				Class cc = Class.forName(d);
				Constructor c =cc.getConstructor(Properties.class);
				MTPProviderImpl mtpProvider = (MTPProviderImpl) c.newInstance(properties);
				mtpProvider.start();
				return mtpProvider;
			} catch (Exception e) {
				
				throw new IllegalArgumentException("Failed to create driver class.",e);
			}
		}
		
		
	}

	private MTPProviderFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static MTPProviderFactory getInstance() {
		return instance;
	}
	
}
