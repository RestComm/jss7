package org.mobicents.protocols.ss7.tcap.api.tc.dialog;

public interface DialogPortion extends Cloneable{

	//SEE Q.773 and decode this stuff.....
	/**
	 * Determines if dialog message is request or response.
	 * 
	 * @return <ul>
	 *         <li><b>true</b></li> - if message is dialog request
	 *         <li><b>false</b></li> - if message is dialog response
	 *         </ul>
	 */
	public boolean isRequest();
	/**
	 * Determines if dialog message is request or response.
	 * 
	 * @param b <ul>
	 *         <li><b>true</b></li> - if message is dialog request
	 *         <li><b>false</b></li> - if message is dialog response
	 *         </ul>
	 */
	public void setRequest(boolean b);

}
