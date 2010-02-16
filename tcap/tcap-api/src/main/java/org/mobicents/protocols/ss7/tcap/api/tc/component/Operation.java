package org.mobicents.protocols.ss7.tcap.api.tc.component;

public interface Operation {

	
	/**
	 * Returns code of this operation object.
	 * 
	 * @return
	 */
	public int getCode();

	/**
	 * 
	 * @return <ul>
	 *         <li><b>true</b></li> - if operation code is now known to stack.
	 *         <li><b></b></li> - if operation code is known to stack and operation has been decoded properly.
	 *         </ul>
	 */
	public boolean isRaw();

	/**
	 * Returns raw content. This method MAY return byte[] if operation is not
	 * raw. In case operation code is not known, this method returns byte[]
	 * representing operations received in request.
	 * 
	 * @return
	 */
	public byte[] getRawContent();

}
