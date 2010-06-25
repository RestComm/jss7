package org.mobicents.protocols.ss7.map.api;

import java.util.Properties;

import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;

/**
 * 
 * @author amit bhayani
 *
 */
public interface MAPStack {
	
	public MAPProvider getMAPProvider();
	
	public void stop();
	
	public void start() throws IllegalStateException, StartFailedException;
	
	public void configure(Properties properties);
}
