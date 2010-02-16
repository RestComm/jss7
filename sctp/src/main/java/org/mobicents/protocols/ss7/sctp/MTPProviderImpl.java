package org.mobicents.protocols.ss7.sctp;


abstract class MTPProviderImpl implements MTPProvider{

	
	public abstract void start() throws StartFailedException,IllegalStateException;
	public abstract void close() throws IllegalStateException;
	
	
}
