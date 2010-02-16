/**
 * Start time:11:00:19 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:11:00:19 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface BackwardCallIndicators extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x11;
	
	
	/**
	 * See q.763 3.5 Charge indicator no indication
	 */
	public static final int _CHARGE_INDICATOR_NOINDICATION = 0;
	/**
	 * See q.763 3.5 Charge indicator no charge
	 */
	public static final int _CHARGE_INDICATOR_NOCHARGE = 1;
	/**
	 * See q.763 3.5 Charge indicator charge
	 */
	public static final int _CHARGE_INDICATOR_CHARGE = 2;
	
	
	/**
	 * See q.763 3.5 Called party's status indicator no indication
	 */
	public static final int _CPSI_NO_INDICATION = 0;
	/**
	 * See q.763 3.5 Called party's status indicator subscriber free
	 */
	public static final int _CPSI_SUBSCRIBER_FREE = 1;
	/**
	 * See q.763 3.5 Called party's status indicator connect when free (national
	 * use)
	 */
	public static final int _CPSI_CONNECT_WHEN_FREE = 2;
	
	
	/**
	 * See q.763 3.5 Called party's category indicator
	 */
	public static final int _CPCI_NOINDICATION = 0;
	/**
	 * See q.763 3.5 Called party's category indicator
	 */
	public static final int _CPCI_ORDINARYSUBSCRIBER = 1;
	/**
	 * See q.763 3.5 Called party's category indicator
	 */
	public static final int _CPCI_PAYPHONE = 2;
	
	
	/**
	 * See q.763 3.5 End-to-end method indicator (Note 2)
	 */
	public static final int _ETEMI_NOMETHODAVAILABLE = 0;
	/**
	 * See q.763 3.5 End-to-end method indicator (Note 2)
	 */
	public static final int _ETEMI_PASSALONG = 1;
	/**
	 * See q.763 3.5 End-to-end method indicator (Note 2)
	 */
	public static final int _ETEMI_SCCP = 2;
	/**
	 * See q.763 3.5 End-to-end method indicator (Note 2)
	 */
	public static final int _ETEMI_SCCP_AND_PASSALONG = 3;
	
	
	/**
	 * See q.763 3.5 Interworking indicator (Note 2) no interworking encountered
	 * (Signalling System No. 7 all the way)
	 */
	public static final boolean _II_NO_IE = false;
	/**
	 * See q.763 3.5 Interworking indicator (Note 2) interworking encountered
	 */
	public static final boolean _II_IE = true;
	
	
	/**
	 * See q.763 3.5 End-to-end information indicator (national use) (Note 2) no
	 * end-to-end information available
	 */
	public static final boolean _ETEII_NO_IA = false;
	/**
	 * See q.763 3.5 End-to-end information indicator (national use) (Note 2)
	 * end-to-end information available
	 */
	public static final boolean _ETEII_IA = true;
	
	
	/**
	 * See q.763 3.5 ISDN user part indicator (Note 2) ISDN user part not used
	 * all the way
	 */
	public static final boolean _ISDN_UPI_NOT_UATW = false;
	/**
	 * See q.763 3.5 ISDN user part indicator (Note 2) ISDN user part used all
	 * the way
	 */
	public static final boolean _ISDN_UPI_UATW = true;
	
	
	/**
	 * See q.763 3.5 ISDN access indicator terminating access non-ISDN
	 */
	public static final boolean _ISDN_AI_TA_NOT_ISDN = false;
	/**
	 * See q.763 3.5 ISDN access indicator terminating access ISDN
	 */
	public static final boolean _ISDN_AI_TA_ISDN = true;
	
	
	/**
	 * See q.763 3.5 Echo control device indicator incoming echo control device
	 * not included
	 */
	public static final boolean _ECDI_IECD_NOT_INCLUDED = false;
	/**
	 * See q.763 3.5 Echo control device indicator incoming echo control device
	 * included
	 */
	public static final boolean _ECDI_IECD_INCLUDED = true;
	
	
	/**
	 * See q.763 3.5 Holding indicator (national use)
	 */
	public static final boolean _HI_NOT_REQUESTED = false;
	/**
	 * See q.763 3.5 Holding indicator (national use)
	 */
	public static final boolean _HI_REQUESTED = true;
	
	
	/**
	 * See q.763 3.5 SCCP method indicator (Note 2) no indication
	 */
	public static final int _SCCP_MI_NO_INDICATION = 0;
	/**
	 * See q.763 3.5 SCCP method indicator (Note 2) connectionless method
	 * available (national use)
	 */
	public static final int _SCCP_MI_CONNECTIONLESS = 1;
	/**
	 * See q.763 3.5 SCCP method indicator (Note 2) connection oriented method
	 * available
	 */
	public static final int _SCCP_MI_CONNECTION_ORIENTED = 2;
	/**
	 * See q.763 3.5 SCCP method indicator (Note 2) connectionless and
	 * connection oriented methods available (national use)
	 */
	public static final int _SCCP_MI_CONNLESS_AND_CONN_ORIENTED = 3;
	
	

	public int getChargeIndicator();

	public void setChargeIndicator(int chargeIndicator);

	public int getCalledPartysStatusIndicator();

	public void setCalledPartysStatusIndicator(int calledPartysStatusIndicator);

	public int getCalledPartysCategoryIndicator();

	public void setCalledPartysCategoryIndicator(int calledPartysCategoryIndicator);

	public int getEndToEndMethodIndicator();

	public void setEndToEndMethodIndicator(int endToEndMethodIndicator);

	public boolean isInterworkingIndicator();

	public void setInterworkingIndicator(boolean interworkingIndicator);

	public boolean isEndToEndInformationIndicator();

	public void setEndToEndInformationIndicator(boolean endToEndInformationIndicator);

	public boolean isIsdnUserPartIndicator();

	public void setIsdnUserPartIndicator(boolean isdnUserPartIndicator);

	public boolean isIsdnAccessIndicator();

	public void setIsdnAccessIndicator(boolean isdnAccessIndicator);

	public boolean isEchoControlDeviceIndicator();

	public void setEchoControlDeviceIndicator(boolean echoControlDeviceIndicator);

	public boolean isHoldingIndicator();

	public void setHoldingIndicator(boolean holdingIndicator);

	public int getSccpMethodIndicator();

	public void setSccpMethodIndicator(int sccpMethodIndicator);

}
