package org.mobicents.protocols.ss7.mtp.provider;

public interface MtpListener {
	/**
	 * Callback method from lower layers of MTP-. This is called once MTP
	 * determines that link is stable and is able to send/receive messages
	 * properly. This method should be called only once. 
	 */
	public void linkUp();

	/**
	 * Callback method from MTP layer, informs upper layers that link is not
	 * operable.
	 */
	public void linkDown();

	/**
	 * MTP invokes this method once proper MSU is detected. Argument contains
	 * full MTP MSU.
	 * 
	 * @param msgBuff
	 */
	public void receive(byte[] msgBuff); // http://pt.com/page/tutorials/ss7-tutorial/mtp
}
