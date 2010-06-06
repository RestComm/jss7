package org.mobicents.protocols.ss7.mtp;

public interface Mtp2Listener {
	/**
	 * Indicates that particular link is in service, it can send and receive data.
	 * @param mtp2
	 */
	void linkInService(Mtp2 mtp2);
	/**
	 * Indicates that particular link is out of sync, it can not
	 * @param mtp2
	 */
	void linkFailed(Mtp2 mtp2);
	/**
	 * Indicates that particular link received valid MSU
	 * @param sio
	 * @param sif
	 * @param mtp2
	 */
	void onMessage(int sio, byte[] sif, Mtp2 mtp2);
	
	/**
	 * Registers link once it starts. Indicates that link initiated sync procedure.
	 * @param mtp2
	 */
	void registerLink(Mtp2 mtp2);
	/**
	 * Unregisters link once it is closed.
	 * @param mtp2
	 */
	void unregisterLink(Mtp2 mtp2);
}
