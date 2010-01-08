/**
 * Start time:13:18:50 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.message.parameter;

/**
 * Start time:13:18:50 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface InstructionIndicators extends ISUPParameter {
	// FIXME: XXX
	public static final int _PARAMETER_CODE = 0;
	//FIXME: add C defs
	
	/**
	 * See Q.763 3.41 Transit at intermediate exchange indicator : transit
	 * interpretation
	 */
	public static final boolean _TI_TRANSIT_INTEPRETATION = false;
	/**
	 * See Q.763 3.41 Transit at intermediate exchange indicator :
	 */
	public static final boolean _TI_ETE_INTEPRETATION = true;
	/**
	 * See Q.763 3.41 Release call indicator : do not release
	 */
	public static final boolean _RCI_DO_NOT_RELEASE = false;
	/**
	 * See Q.763 3.41 Release call indicator : reelase call
	 */
	public static final boolean _RCI_RELEASE = true;

	/**
	 * See Q.763 3.41 Discard message indicator : do not discard message (pass
	 * on)
	 */
	public static final boolean _DMI_DO_NOT_DISCARD = false;
	/**
	 * See Q.763 3.41 Discard message indicator : discard message
	 */
	public static final boolean _DMI_DISCARD = true;

	/**
	 * See Q.763 3.41 Discard parameter indicator : do not discard parameter
	 * (pass on)
	 */
	public static final boolean _DPI_DO_NOT_DISCARD = false;
	/**
	 * See Q.763 3.41 Discard parameter indicator : discard parameter
	 */
	public static final boolean _DPI_INDICATOR_DISCARD = true;

	/**
	 * See Q.763 3.41 Pass on not possible indicator : release call
	 */
	public static final int _PONPI_RELEASE_CALL = 0;

	/**
	 * See Q.763 3.41 Pass on not possible indicator : discard message
	 */
	public static final int _PONPI_DISCARD_MESSAGE = 1;

	/**
	 * See Q.763 3.41 Pass on not possible indicator : discard parameter
	 */
	public static final int _PONPI_DISCARD_PARAMETER = 2;

	/**
	 * See Q.763 3.41 Broadband/narrowband interworking indicator : pass on
	 */
	public static final int _BII_PASS_ON = 0;

	/**
	 * See Q.763 3.41 Broadband/narrowband interworking indicator : discard
	 * message
	 */
	public static final int _BII_DISCARD_MESSAGE = 1;

	/**
	 * See Q.763 3.41 Broadband/narrowband interworking indicator : release call
	 */
	public static final int _BII_RELEASE_CALL = 2;

	/**
	 * See Q.763 3.41 Broadband/narrowband interworking indicator : discard
	 * parameter
	 */
	public static final int _BII_DISCARD_PARAMETER = 3;
	
	public boolean isTransitAtIntermediateExchangeIndicator();

	public void setTransitAtIntermediateExchangeIndicator(boolean transitAtIntermediateExchangeIndicator);

	public boolean isReleaseCallindicator();

	public void setReleaseCallindicator(boolean releaseCallindicator);

	public boolean isSendNotificationIndicator();

	public void setSendNotificationIndicator(boolean sendNotificationIndicator);

	public boolean isDiscardMessageIndicator();

	public void setDiscardMessageIndicator(boolean discardMessageIndicator);

	public boolean isDiscardParameterIndicator();

	public void setDiscardParameterIndicator(boolean discardParameterIndicator);

	public int getPassOnNotPossibleIndicator();

	public void setPassOnNotPossibleIndicator(int passOnNotPossibleIndicator2);

	public int getBandInterworkingIndicator();

	public void setBandInterworkingIndicator(int bandInterworkingIndicator);

	public boolean isSecondOctetPresenet();

	public void setSecondOctetPresenet(boolean secondOctetPresenet);

	public byte[] getRaw();

	public void setRaw(byte[] raw);

	public boolean isUseAsRaw();

	public void setUseAsRaw(boolean useAsRaw);

}
