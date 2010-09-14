package org.mobicents.protocols.ss7.sccp;

import java.util.Properties;

import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;

/**
 * 
 * @author baranowb
 * @deprecated 
 */
public interface SccpStack {

	public void start() throws IllegalStateException, StartFailedException;

	public void stop();

	public void configure(Properties properties) throws ConfigurationException;

	public SccpProvider getSccpProvider();

}
