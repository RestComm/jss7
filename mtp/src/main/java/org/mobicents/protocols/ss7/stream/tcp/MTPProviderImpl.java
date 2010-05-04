package org.mobicents.protocols.ss7.stream.tcp;

import org.mobicents.protocols.ss7.stream.MTPProvider;


public abstract class MTPProviderImpl implements MTPProvider{

	
	public abstract void start() throws StartFailedException,IllegalStateException;
	public abstract void close() throws IllegalStateException;
	
	
}
