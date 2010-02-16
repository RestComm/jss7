/**
 * Start time:11:58:14 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:11:58:14 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface CallingPartyNumber extends AbstractNAINumberInterface, ISUPParameter {
	public static final int _PARAMETER_CODE = 0x0A;

	/**
	 * numbering plan indicator indicator value. See Q.763 - 3.9d
	 */
	public final static int _NPI_ISDN = 1;
	/**
	 * numbering plan indicator indicator value. See Q.763 - 3.9d
	 */
	public final static int _NPI_DATA = 3;
	/**
	 * numbering plan indicator indicator value. See Q.763 - 3.9d
	 */
	public final static int _NPI_TELEX = 4;

	// FIXME: change to boolean
	/**
	 * number incomplete indicator indicator value. See Q.763 - 3.10c
	 */
	public final static int _NI_INCOMPLETE = 1;
	/**
	 * number incomplete indicator indicator value. See Q.763 - 3.10c
	 */
	public final static int _NI_COMPLETE = 0;

	/**
	 * address presentation restricted indicator indicator value. See Q.763 -
	 * 3.10e
	 */
	public final static int _APRI_ALLOWED = 0;

	/**
	 * address presentation restricted indicator indicator value. See Q.763 -
	 * 3.10e
	 */
	public final static int _APRI_RESTRICTED = 1;

	/**
	 * address presentation restricted indicator indicator value. See Q.763 -
	 * 3.10e
	 */
	public final static int _APRI_NOT_AVAILABLE = 2;

	/**
	 * address presentation restricted indicator indicator value. See Q.763 -
	 * 3.10e
	 */
	public final static int _APRI_RESERVED = 3;

	/**
	 * screening indicator indicator value. See Q.763 - 3.10f
	 */
	public final static int _SI_USER_PROVIDED_NOTVERIFIED = 0;
	/**
	 * screening indicator indicator value. See Q.763 - 3.10f
	 */
	public final static int _SI_USER_PROVIDED_VERIFIED_PASSED = 1;
	/**
	 * screening indicator indicator value. See Q.763 - 3.10f
	 */
	public final static int _SI_USER_PROVIDED_FAILED = 2;

	/**
	 * screening indicator indicator value. See Q.763 - 3.10f
	 */
	public final static int _SI_NETWORK_PROVIDED = 3;

	public int getNumberingPlanIndicator();

	public void setNumberingPlanIndicator(int numberingPlanIndicator);

	public int getNumberIncompleteIndicator();

	public void setNumberIncompleteIndicator(int numberIncompleteIndicator);

	public int getAddressRepresentationREstrictedIndicator();

	public void setAddressRepresentationREstrictedIndicator(int addressRepresentationREstrictedIndicator);

	public int getScreeningIndicator();

	public void setScreeningIndicator(int screeningIndicator);

}
