/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryResponseMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitStateIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;


/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for CQR
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CQRTest extends MessageHarness {
	
	
	
	public void testTwo_Params() throws Exception
	{
		byte[] message = getDefaultBody();
		//CircuitGroupQueryResponseMessage grs=new CircuitGroupQueryResponseMessageImpl(this,message);
		CircuitGroupQueryResponseMessage grs=super.messageFactory.createCQR();
		grs.decodeElement(message);

		
		try{
			RangeAndStatus RS = (RangeAndStatus) grs.getParameter(RangeAndStatus._PARAMETER_CODE);
			assertNotNull("Range And Status return is null, it should not be",RS);
			if(RS == null)
				return;
			byte range = RS.getRange();
			assertEquals("Range is wrong,",0x01, range);
			byte[] b=RS.getStatus();
			assertNull("RangeAndStatus.getRange() is not null",b);
		
			
		}catch(Exception e)
		{
			e.printStackTrace();
			super.fail("Failed on get parameter["+CallReference._PARAMETER_CODE+"]:"+e);
		}
		try{
			CircuitStateIndicator CSI = (CircuitStateIndicator) grs.getParameter(CircuitStateIndicator._PARAMETER_CODE);
			assertNotNull("Circuit State Indicator return is null, it should not be",CSI);
			if(CSI == null)
				return;
			assertNotNull("CircuitStateIndicator getCircuitState return is null, it should not be",CSI.getCircuitState());
			byte[] circuitState = CSI.getCircuitState();
			assertEquals("CircuitStateIndicator.getCircuitState() length is nto correct,",3, circuitState.length);
			assertEquals("CircuitStateIndicator.getCircuitState()[0] value is not correct,",1, CSI.getMaintenanceBlockingState(circuitState[0]));
			assertEquals("CircuitStateIndicator.getCircuitState()[1] value is not correct,",2, CSI.getMaintenanceBlockingState(circuitState[1]));
			assertEquals("CircuitStateIndicator.getCircuitState()[2] value is not correct,",3, CSI.getMaintenanceBlockingState(circuitState[2]));
		}catch(Exception e)
		{
			e.printStackTrace();
			super.fail("Failed on get parameter["+CallReference._PARAMETER_CODE+"]:"+e);
		}

	}
	@Override
	protected byte[] getDefaultBody() {
		//FIXME: for now we strip MTP part
		byte[] message={

				0x0C
				,(byte) 0x0B
				,CircuitGroupQueryResponseMessage._MESSAGE_CODE_CQR

				,0x02 // ptr to variable part
				,0x03
				
				//no optional, so no pointer
				//RangeAndStatus._PARAMETER_CODE
				,0x01
				,0x01
				//CircuitStateIndicator
				,0x03
				,0x01
				,0x02
				,0x03
			
				
		};

		return message;
	}
	@Override
	protected ISUPMessage getDefaultMessage() {
		return super.messageFactory.createCQR();
	}
}
