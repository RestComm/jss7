/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import org.junit.Test;
import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for ACM
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ACMTest extends MessageHarness {

	@Test
	public void testTwo_Params() throws Exception {
		//FIXME: for now we strip MTP part
		//FIXME: This one fail!
		byte[] message = getDefaultBody();

		// AddressCompleteMessageImpl acm=new
		// AddressCompleteMessageImpl(this,message);
		AddressCompleteMessage acm = super.messageFactory.createACM();
		acm.decodeElement(message);

		assertNotNull("BackwardCallIndicator is null", acm.getBackwardCallIndicators());
		assertNotNull("OptionalBackwardCallIndicator is null", acm.getOptionalBakwardCallIndicators());
		assertNotNull("Cause Indicator is null", acm.getCauseIndicators());

		BackwardCallIndicators bci = acm.getBackwardCallIndicators();
		assertEquals("BackwardCallIndicator charge indicator does not match", bci.getChargeIndicator(), 1);
		assertEquals("BackwardCallIndicator called party status does not match", bci.getCalledPartysStatusIndicator(), 0);
		assertEquals("BackwardCallIndicator called party category does not match", bci.getCalledPartysCategoryIndicator(), 0);
		assertFalse(bci.isInterworkingIndicator());
		assertFalse(bci.isEndToEndInformationIndicator());
		assertFalse(bci.isIsdnAccessIndicator());
		assertFalse(bci.isHoldingIndicator());
		assertTrue(bci.isEchoControlDeviceIndicator());
		assertEquals("BackwardCallIndicator sccp method does not match", bci.getSccpMethodIndicator(), 0);

		CircuitIdentificationCode cic = acm.getCircuitIdentificationCode();
		assertNotNull("CircuitIdentificationCode must not be null", cic);
		assertEquals("CircuitIdentificationCode value does not match", cic.getCIC(), getDefaultCIC());

	}

	@Override
	protected byte[] getDefaultBody() {
		byte[] message = {

		0x0C, (byte) 0x0B, 0x06, 0x01, 0x20, 0x01, 0x29, 0x01, 0x01, 0x12, 0x02, (byte) 0x82, (byte) 0x9C, 0x00
		
		};
		return message;
	}

	@Override
	protected ISUPMessage getDefaultMessage() {
		return super.messageFactory.createACM();
	}

}
