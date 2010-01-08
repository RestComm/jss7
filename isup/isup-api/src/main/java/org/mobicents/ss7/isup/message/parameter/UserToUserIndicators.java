/**
 * Start time:14:23:10 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:14:23:10 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface UserToUserIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x2A;

	//FIXME: Add C defs
	
	/**
	 * Service 1,2,3 request : no info
	 */
	public static final int _REQ_Sx_NO_INFO = 0;
	/**
	 * Service 1,2,3 request : not essential
	 */
	public static final int _REQ_Sx_RNE = 2;
	/**
	 * Service 1,2,3 request : essential
	 */
	public static final int _REQ_Sx_RE = 3;

	/**
	 * Service 1,2,3 request : no info
	 */
	public static final int _RESP_Sx_NO_INFO = 0;
	/**
	 * Service 1,2,3 request : not provided
	 */
	public static final int _RESP_Sx_NOT_PROVIDED = 1;

	/**
	 * Service 1,2,3 request : provided
	 */
	public static final int _RESP_Sx_PROVIDED = 2;

	/**
	 * See Q.763 3.60 Network discard indicator : no information
	 */
	public static final boolean _NDI_NO_INFO = false;

	/**
	 * See Q.763 3.60 Network discard indicator : user-to-user information
	 * discarded by the network
	 */
	public static final boolean _NDI_UTUIDBTN = true;
	
	public boolean isResponse();

	public void setResponse(boolean response);

	public int getServiceOne();

	public void setServiceOne(int serviceOne);

	public int getServiceTwo();

	public void setServiceTwo(int serviceTwo);

	public int getServiceThree();

	public void setServiceThree(int serviceThree);

	public boolean isNetworkDiscardIndicator();

	public void setNetworkDiscardIndicator(boolean networkDiscardIndicator);

}
