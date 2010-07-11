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
package org.mobicents.protocols.ss7.mtp.provider;

import java.lang.reflect.Constructor;
import java.util.Properties;

import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.ss7.mtp.provider.m3ua.M3UAProvider;

/**
 * @author baranowb
 *
 */
public class MtpProviderFactory {
	
	/**
	 * Property which controls driver.
	 */
	public static final String PROPERTY_MTP_DRIVER = "mtp.driver";
	
	public static final String DRIVER_M3UA = "m3ua";
	
	private final static MtpProviderFactory instance = new MtpProviderFactory();
	/**
	 * 
	 */
	private MtpProviderFactory() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the instance
	 */
	public static MtpProviderFactory getInstance() {
		return instance;
	}

	
	public MtpProvider getProvider(Properties props) throws ConfigurationException
	{
		MtpProvider p = null;
		//only TCP supported now
		String d = props.getProperty(PROPERTY_MTP_DRIVER, DRIVER_M3UA);
		if(d.toLowerCase().equals(DRIVER_M3UA))
		{
			p = new M3UAProvider();
		}else
		{
			//try as class
			
			try {
				Class clazz = Class.forName(d);
				Constructor<MtpProvider> c =  clazz.getConstructor(null);
				p =  c.newInstance(null);
				
			} catch (Exception e) {
				throw new UnsupportedOperationException("Failed to create driver for class: "+d,e);
			}
			
		}
		p.configure(props);
		return p;
		
	}
	
}
