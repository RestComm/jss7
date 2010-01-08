package org.mobicents.ss7.sctp;

import java.lang.reflect.Constructor;
import java.util.Properties;

public class MTPProviderFactory {

	private static final MTPProviderFactory instance = new MTPProviderFactory();
	
	public static final String _POPERTY_DRIVER_CLASS="driver";
	
	public static final String _DRIVER_TCP="TCP_IP";
	
	public MTPProvider getProvider(Properties properties) throws IllegalArgumentException
	{
		if(properties==null)
		{
			throw new NullPointerException("Properties must not be null");
			
		}
		String d = properties.getProperty(_POPERTY_DRIVER_CLASS);
		if(d == null)
		{
			throw new IllegalArgumentException("There is no property indicating driver!");
		}
		
		if(d.equals(_DRIVER_TCP))
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
		}else
		{
			throw new IllegalArgumentException("There is no driver class specified.");
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
