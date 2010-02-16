/**
 * Start time:13:36:04 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:36:04 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface OptionalBackwardCallIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x29;

	/**
	 * See Q.763 3.37 In-band information indicator
	 */
	public final static boolean _IBII_NO_INDICATION = false;
	/**
	 * See Q.763 3.37 In-band information indicator
	 */
	public final static boolean _IBII_AVAILABLE = true;

	/**
	 * See Q.763 3.37 Call diversion may occur indicator
	 */
	public final static boolean _CDI_NO_INDICATION = false;

	/**
	 * See Q.763 3.37 Call diversion may occur indicator
	 */
	public final static boolean _CDI_MAY_OCCUR = true;

	/**
	 * See Q.763 3.37 Simple segmentation indicator
	 */
	public final static boolean _SSIR_NO_ADDITIONAL_INFO = false;

	/**
	 * See Q.763 3.37 Simple segmentation indicator
	 */
	public final static boolean _SSIR_ADDITIONAL_INFO = true;

	/**
	 * See Q.763 3.37 MLPP user indicator
	 */
	public final static boolean _MLLPUI_NO_INDICATION = false;

	/**
	 * See Q.763 3.37 MLPP user indicator
	 */
	public final static boolean _MLLPUI_USER = true;
	
	
	public boolean isInbandInformationIndicator();

	public void setInbandInformationIndicator(boolean inbandInformationIndicator);

	public boolean isCallDiversionMayOccurIndicator();

	public void setCallDiversionMayOccurIndicator(boolean callDiversionMayOccurIndicator);

	public boolean isSimpleSegmentationIndicator();

	public void setSimpleSegmentationIndicator(boolean simpleSegmentationIndicator);

	public boolean isMllpUserIndicator();

	public void setMllpUserIndicator(boolean mllpUserIndicator);
}
