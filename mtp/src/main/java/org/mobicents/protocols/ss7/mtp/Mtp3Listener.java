package org.mobicents.protocols.ss7.mtp;

public interface Mtp3Listener {

	/**
	 * Callback method from lower layers MTP3-. This is called once MTP3
	 * determines that link is stable and is able to send/receive messages
	 * properly. This method should be called only once. Every linkup event.
	 */
	public void linkUp();

	/**
	 * Callback method from MTP3 layer, informs upper layers that link is not
	 * operable.
	 */
	public void linkDown();

	/**
	 * Mtp3 invokes this method once proper MSU is detected. Arguemnt contains full Mtp3 MSU. 
	 * @param msgBuff
	 */
	public void receive(byte[] msgBuff); // http://pt.com/page/tutorials/ss7-tutorial/mtp
	
}
