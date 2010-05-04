package org.mobicents.protocols.ss7.mtp;

public interface MtpUser {

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
	 * 
	 * @param service
	 *            - type, this is generaly content of service part - contains
	 *            constant defined for ISUP, SCCP or any other
	 * @param subservice
	 *            - as above, it contains other 4 bits of SIO byte.
	 * @param msgBuff
	 */
	public void receive(byte sls,byte linksetId, int service, int subservice, byte[] msgBuff);


}
