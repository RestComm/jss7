package org.mobicents.protocols.ss7.mtp;

public interface Mtp2Listener {

	void linkInService(Mtp2 mtp2);

	void linkFailed(Mtp2 mtp2);
	
	void onMessage(int sio, byte[] sif, Mtp2 mtp2);
}
