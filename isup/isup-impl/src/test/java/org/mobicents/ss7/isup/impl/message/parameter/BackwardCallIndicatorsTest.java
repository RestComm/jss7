/**
 * Start time:12:21:06 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.mobicents.ss7.isup.ISUPComponent;
import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.impl.message.parameter.BackwardCallIndicatorsImpl;

/**
 * Start time:12:21:06 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * Class to test BCI
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class BackwardCallIndicatorsTest extends ParameterHarness {

	public BackwardCallIndicatorsTest() {
		super();
		// TODO Auto-generated constructor stub
		super.badBodies.add(new byte[1]);
		super.badBodies.add(new byte[3]);

		super.goodBodies.add(getBody1());
		super.goodBodies.add(getBody2());
	}

	private byte[] getBody1() {

		byte[] body = new byte[2];
		// Chardi IND : 10 - charge
		// Called part status ind: 01 - sub free
		// Called part category ind: 10 - pay phone
		// e2emethod ind: 10 - sccp
		// whole: 10100110
		body[0] = (byte) 0xA6;

		// Interworking : 1 - encoutnered
		// e2einfo ind : 0 - no info
		// ISUP ind : 1 - all the way
		// hold ind : 1 - requested
		// ISDN acc ind : 1 - terminating acc isdn
		// echo ctrl dev: 0 - not included
		// SCCP m ind : 10 - connection oriented only
		// whole : 10011101
		body[1] = (byte) 0x9D;
		return body;
	}

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ParameterRangeInvalidException {
		BackwardCallIndicatorsImpl bci = new BackwardCallIndicatorsImpl(getBody1());

		String[] methodNames = { "getChargeIndicator", "getCalledPartysStatusIndicator", "getCalledPartysCategoryIndicator", "getEndToEndMethodIndicator", "isInterworkingIndicator",
				"isEndToEndInformationIndicator", "isIsdnUserPartIndicator", "isHoldingIndicator", "isIsdnAccessIndicator", "isEchoControlDeviceIndicator", "getSccpMethodIndicator" };
		Object[] expectedValues = { bci._CHARGE_INDICATOR_CHARGE, bci._CPSI_SUBSCRIBER_FREE, bci._CPCI_PAYPHONE, bci._ETEMI_SCCP, 
				bci._II_IE, bci._ETEII_NO_IA, bci._ISDN_UPI_UATW, bci._HI_REQUESTED,bci._ISDN_AI_TA_ISDN, bci._ECDI_IECD_NOT_INCLUDED, bci._SCCP_MI_CONNECTION_ORIENTED };
		super.testValues(bci, methodNames, expectedValues);
	}

	private byte[] getBody2() {
		byte[] body = new byte[2];
		// Chardi IND : 01 - no charge
		// Called part status ind: 10 - conn when free
		// Called part category ind: 10 - pay phone
		// e2emethod ind: 11 - pass alond and sccp
		// whole: 11101001
		body[0] = (byte) 0xE9;

		// Interworking : 1 - encoutnered
		// e2einfo ind : 0 - no info
		// ISUP ind : 1 - all the way
		// hold ind : 1 - requested
		// ISDN acc ind : 1 - terminating acc isdn
		// echo ctrl dev: 1 - not included
		// SCCP m ind : 10 - connection oriented only
		// whole : 10111101
		body[1] = (byte) 0xBD;
		return body;
	}
	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ParameterRangeInvalidException {
		BackwardCallIndicatorsImpl bci = new BackwardCallIndicatorsImpl(getBody2());

		String[] methodNames = { "getChargeIndicator", "getCalledPartysStatusIndicator", "getCalledPartysCategoryIndicator", "getEndToEndMethodIndicator", "isInterworkingIndicator",
				"isEndToEndInformationIndicator", "isIsdnUserPartIndicator", "isHoldingIndicator", "isIsdnAccessIndicator", "isEchoControlDeviceIndicator", "getSccpMethodIndicator" };
		Object[] expectedValues = { bci._CHARGE_INDICATOR_NOCHARGE, bci._CPSI_CONNECT_WHEN_FREE, bci._CPCI_PAYPHONE, bci._ETEMI_SCCP_AND_PASSALONG, 
				bci._II_IE, bci._ETEII_NO_IA, bci._ISDN_UPI_UATW, bci._HI_REQUESTED,bci._ISDN_AI_TA_ISDN, bci._ECDI_IECD_INCLUDED, bci._SCCP_MI_CONNECTION_ORIENTED };
		super.testValues(bci, methodNames, expectedValues);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent
	 * ()
	 */
	@Override
	public ISUPComponent getTestedComponent() throws ParameterRangeInvalidException {
		return new org.mobicents.ss7.isup.impl.message.parameter.BackwardCallIndicatorsImpl(new byte[2]);
	}

}
