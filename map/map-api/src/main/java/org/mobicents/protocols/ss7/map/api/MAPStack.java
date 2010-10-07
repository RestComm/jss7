package org.mobicents.protocols.ss7.map.api;

import java.util.Properties;

import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;

/**
 * 
 * @author amit bhayani
 *
 */
public interface MAPStack {
	
	public MAPProvider getMAPProvider();
	
	public void stop();
	
	public void start() throws IllegalStateException, StartFailedException;
	
	//public void configure(Properties properties) throws ConfigurationException;
}
