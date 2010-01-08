/**
 * Start time:13:07:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:07:30 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface GenericNumber extends AbstractNAINumberInterface, AbstractNumberInterface, ISUPParameter {
	public static final int _PARAMETER_CODE = 0xC0;

	/**
	 * number qualifier indicator indicator value. See Q.763 - 3.26a
	 */
	public final static int _NQIA_CALLED_NUMBER = 1;
	/**
	 * number qualifier indicator indicator value. See Q.763 - 3.26a
	 */
	public final static int _NQIA_CONNECTED_NUMBER = 5;
	/**
	 * number qualifier indicator indicator value. See Q.763 - 3.26a
	 */
	public final static int _NQIA_CALLING_PARTY_NUMBER = 6;
	/**
	 * number qualifier indicator indicator value. See Q.763 - 3.26a
	 */
	public final static int _NQIA_ORIGINAL_CALLED_NUMBER = 7;
	/**
	 * number qualifier indicator indicator value. See Q.763 - 3.26a
	 */
	public final static int _NQIA_REDIRECTING_NUMBER = 8;
	/**
	 * number qualifier indicator indicator value. See Q.763 - 3.26a
	 */
	public final static int _NQIA_REDIRECTION_NUMBER = 9;

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

	/**
	 * number incomplete indicator indicator value. See Q.763 - 3.10c
	 */
	public final static boolean _NI_INCOMPLETE = true;
	/**
	 * number incomplete indicator indicator value. See Q.763 - 3.10c
	 */
	public final static boolean _NI_COMPLETE = false;
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
	 * 3.16d
	 */
	public final static int _APRI_SPARE = 3;

	/**
	 * screening indicator indicator value. See Q.763 - 3.26g
	 */
	public final static int _SI_USER_PROVIDED_NVERIFIED_PASSED = 0;
	/**
	 * screening indicator indicator value. See Q.763 - 3.26g
	 */
	public final static int _SI_USER_PROVIDED_VERIFIED_PASSED = 1;
	/**
	 * screening indicator indicator value. See Q.763 - 3.26g
	 */
	public final static int _SI_USER_PROVIDED_VERIFIED_FAILED = 2;

	/**
	 * screening indicator indicator value. See Q.763 - 3.26g
	 */
	public final static int _SI_NETWORK_PROVIDED = 3;

	public int getNumberQualifierIndicator();

	public void setNumberQualifierIndicator(int numberQualifierIndicator);

	public int getNumberingPlanIndicator();

	public void setNumberingPlanIndicator(int numberingPlanIndicator);

	public int getAddressRepresentationRestrictedIndicator();

	public void setAddressRepresentationRestrictedIndicator(int addressRepresentationREstrictedIndicator);

	public boolean isNumberIncomplete();

	public void setNumberIncompleter(boolean numberIncomplete);

	public int getScreeningIndicator();

	public void setScreeningIndicator(int screeningIndicator);

}
