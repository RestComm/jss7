package org.mobicents.protocols.ss7.sccp;

import java.util.Properties;

import org.mobicents.protocols.ss7.stream.tcp.StartFailedException;

/**
 * 
 * @author baranowb
 * 
 */
public interface SccpStack {

	public void start() throws IllegalStateException, StartFailedException;

	public void stop();

	public void configure(Properties properties);

	public SccpProvider getSccpProvider();

}
