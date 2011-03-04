/**
 * Start time:13:23:12 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:13:23:12 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface LocationNumber extends NAINumber, Number, ISUPParameter {
	public static final int _PARAMETER_CODE = 0x3F;
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
	 * internal network number indicator indicator value. See Q.763 - 3.9c
	 */
	public final static int _INN_ROUTING_ALLOWED = 0;
	/**
	 * internal network number indicator indicator value. See Q.763 - 3.9c
	 */
	public final static int _INN_ROUTING_NOT_ALLOWED = 1;

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
	 * screening indicator indicator value. See Q.763 - 3.30f
	 */
	public final static int _SI_USER_PROVIDED_VERIFIED_PASSED = 1;

	/**
	 * screening indicator indicator value. See Q.763 - 3.30f
	 */
	public final static int _SI_NETWORK_PROVIDED = 3;

	public int getNumberingPlanIndicator();

	public void setNumberingPlanIndicator(int numberingPlanIndicator);

	public int getInternalNetworkNumberIndicator();

	public void setInternalNetworkNumberIndicator(int internalNetworkNumberIndicator);

	public int getAddressRepresentationRestrictedIndicator();

	public void setAddressRepresentationRestrictedIndicator(int addressRepresentationREstrictedIndicator);

	public int getScreeningIndicator();

	public void setScreeningIndicator(int screeningIndicator);

}
