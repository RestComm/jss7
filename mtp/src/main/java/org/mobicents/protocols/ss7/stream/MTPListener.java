package org.mobicents.protocols.ss7.stream;

public interface MTPListener {

	public void receive(byte[] msg);
	public void linkUp();
	public void linkDown();
	
}
