package org.mobicents.protocols.ss7.sctp;

public interface MTPListener {

	public void receive(int si, int ssi, byte[] msg);
	
}
