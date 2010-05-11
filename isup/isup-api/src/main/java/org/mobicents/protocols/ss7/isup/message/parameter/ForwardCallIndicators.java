/**
 * Start time:12:49:54 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:49:54 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ForwardCallIndicators extends ISUPParameter {
	//FIXME: check this against Q, if it has everything.
	public static final int _PARAMETER_CODE = 0x07;

	
	/**
	 * See q.763 3.5 National/international call indicator (Note 1) : call to be
	 * treated as a national call
	 */
	public static final boolean _NCI_NATIONAL_CALL = false;

	/**
	 * See q.763 3.5 National/international call indicator (Note 1) : call to be
	 * treated as an international call
	 */
	public static final boolean _NCI_INTERNATIONAL_CALL = true;

	/**
	 * See q.763 3.5 End-to-end method indicator (Note 2) : no end-to-end method
	 * available (only link-by-link method available)
	 */
	public static final int _ETEMI_NOMETHODAVAILABLE = 0;
	/**
	 * See q.763 3.5 End-to-end method indicator (Note 2) : pass-along method
	 * available (national use)
	 */
	public static final int _ETEMI_PASSALONG = 1;
	/**
	 * See q.763 3.5 End-to-end method indicator (Note 2) : SCCP method
	 * available
	 */
	public static final int _ETEMI_SCCP = 2;
	/**
	 * See q.763 3.5 End-to-end method indicator (Note 2) : pass-along and SCCP
	 * methods available (national use)
	 */
	public static final int _ETEMI_SCCP_AND_PASSALONG = 3;
	/**
	 * See q.763 3.5 End-to-end information indicator (national use) (Note 2) :
	 * no end-to-end information available
	 */
	public static final boolean _ETEII_NOT_AVAILABLE = false;
	/**
	 * See q.763 3.5 End-to-end information indicator (national use) (Note 2) :
	 * end-to-end information available
	 */
	public static final boolean _ETEII_AVAILABLE = true;
	/**
	 * See q.763 3.5 Interworking indicator (Note 2)
	 */
	public static final boolean _II_NOT_ENCOUTNERED = false;
	/**
	 * See q.763 3.5 Interworking indicator (Note 2)
	 */
	public static final boolean _II_ENCOUTNERED = true;
	/**
	 * See q.763 3.5 ISDN access indicator : originating access non-ISDN
	 */
	public static final boolean _ISDN_AI_OA_N_ISDN = false;
	/**
	 * See q.763 3.5 ISDN access indicator : originating access ISDN
	 */
	public static final boolean _ISDN_AI_OA_ISDN = true;

	/**
	 * See q.763 3.5 SCCP method indicator (Note 2) : no indication
	 */
	public static final int _SCCP_MI_NOINDICATION = 0;
	/**
	 * See q.763 3.5 SCCP method indicator (Note 2) : connectionless method
	 * available (national use)
	 */
	public static final int _SCCP_MI_CONNECTIONLESS = 1;
	/**
	 * See q.763 3.5 SCCP method indicator (Note 2) : connection oriented method
	 * available
	 */
	public static final int _SCCP_MI_CONNECTION_ORIENTED = 2;
	/**
	 * See q.763 3.5 SCCP method indicator (Note 2) : connectionless and
	 * connection oriented methods available (national use)
	 */
	public static final int _SCCP_MI_CL_AND_CO = 3;

	/**
	 * See q.763 3.23 ISDN user part indicator (Note 2) : ISDN user part not
	 * used all the way
	 */
	public static final boolean _ISDN_UPI_NOTUSED = false;
	/**
	 * See q.763 3.23 ISDN user part indicator (Note 2) : ISDN user part used
	 * all the way
	 */
	public static final boolean _ISDN_UPI_USED = true;

	/**
	 * See q.763 3.23 ISDN user part preference indicator : ISDN user part
	 * preferred all the way
	 */
	public static final int _ISDN_UPRI_PREFERED_ALL_THE_WAY = 0;

	/**
	 * See q.763 3.23 ISDN user part preference indicator : ISDN user part not
	 * required all the way
	 */
	public static final int _ISDN_UPRI_NRATW = 1;

	/**
	 * See q.763 3.23 ISDN user part preference indicator : ISDN user part
	 * required all the way
	 */
	public static final int _ISDN_UPRI_RATW = 2;
	
	
	public boolean isNationalCallIdentificator();

	public void setNationalCallIdentificator(boolean nationalCallIdentificator);

	public int getEndToEndMethodIndicator();

	public void setEndToEndMethodIndicator(int endToEndMethodIndicator);

	public boolean isInterworkingIndicator();

	public void setInterworkingIndicator(boolean interworkingIndicator);

	public boolean isEndToEndInformationIndicator();

	public void setEndToEndInformationIndicator(boolean endToEndInformationIndicator);

	public boolean isIsdnUserPartIndicator();

	public void setIsdnUserPartIndicator(boolean isdnUserPartIndicator);

	public int getIsdnUserPartReferenceIndicator();

	public void setIsdnUserPartReferenceIndicator(int isdnUserPartReferenceIndicator);

	public int getSccpMethodIndicator();

	public void setSccpMethodIndicator(int sccpMethodIndicator);

	public boolean isIsdnAccessIndicator();

	public void setIsdnAccessIndicator(boolean isdnAccessIndicator);

}
