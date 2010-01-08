/**
 * Start time:13:37:40 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:37:40 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface OptionalForwardCallIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x08;
	
	
	/**
	 * See Q.763 3.38 Simple segmentation indicator : no additional information
	 * will be sent
	 */
	public final static boolean _SSI_NO_ADDITIONAL_INFO = false;

	/**
	 * See Q.763 3.38 Simple segmentation indicator : additional information
	 * will be sent in a segmentation message
	 */
	public final static boolean _SSI_ADDITIONAL_INFO = true;

	/**
	 * See Q.763 3.38 Connected line identity request indicator :
	 */
	public final static boolean _CLIRI_NOT_REQUESTED = false;

	/**
	 * See Q.763 3.38 Connected line identity request indicator :
	 */
	public final static boolean _CLIRI_REQUESTED = true;
	/**
	 * See Q.763 3.38 Closed user group call indicator : non-CUG call
	 */
	public final static int _CUGCI_NON_CUG_CALL = 0;

	/**
	 * See Q.763 3.38 Closed user group call indicator : closed user group call,
	 * outgoing access allowed
	 */
	public final static int _CUGCI_CUG_CALL_OAL = 2;

	/**
	 * See Q.763 3.38 Closed user group call indicator : closed user group call,
	 * outgoing access not allowed
	 */
	public final static int _CUGCI_CUG_CALL_OANL = 3;

	

	public int getClosedUserGroupCallIndicator();

	public void setClosedUserGroupCallIndicator(int closedUserGroupCallIndicator);

	public boolean isSimpleSegmentationIndicator();

	public void setSimpleSegmentationIndicator(boolean simpleSegmentationIndicator);

	public boolean isConnectedLineIdentityRequestIndicator();

	public void setConnectedLineIdentityRequestIndicator(boolean connectedLineIdentityRequestIndicator);
}
