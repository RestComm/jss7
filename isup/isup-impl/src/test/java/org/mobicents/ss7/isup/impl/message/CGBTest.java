/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message;

import org.mobicents.ss7.isup.message.CircuitGroupBlockingMessage;
import org.mobicents.ss7.isup.message.ISUPMessage;
import org.mobicents.ss7.isup.message.parameter.CallReference;
import org.mobicents.ss7.isup.message.parameter.RangeAndStatus;


/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for CGB
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CGBTest extends MessageHarness {
	
	
	
	public void testTwo_Params() throws Exception
	{
		//FIXME: for now we strip MTP part
		byte[] message={
				0x0C
				,(byte) 0x0B
				,CircuitGroupBlockingMessage._MESSAGE_CODE_CGB
				//Circuit group supervision message type
				,0x01 // hardware failure oriented
				,0x01 // ptr to variable part
				//no optional, so no pointer
				//RangeAndStatus._PARAMETER_CODE
				,0x05
				,0x01
				,0x02
				,0x03
				,0x04
				,0x05

		};

		//CircuitGroupBlockingMessage cgb=new CircuitGroupBlockingMessageImpl(this,message);
		CircuitGroupBlockingMessage cgb=super.messageFactory.createCGB();
		cgb.decodeElement(message);

		
		try{
			RangeAndStatus RS = (RangeAndStatus) cgb.getParameter(RangeAndStatus._PARAMETER_CODE);
			assertNotNull("Range And Status return is null, it should not be",RS);
			if(RS == null)
				return;
			byte range = RS.getRange();
			assertEquals("Range is wrong,",0x01, range);
			byte[] b=RS.getStatus();
			assertNotNull("RangeAndStatus.getRange() is null",b);
			if(b == null)
			{
				return;
			}	
			assertEquals("Length of param is wrong",4 ,b.length);
			if(b.length != 4)
				return;
			assertTrue("RangeAndStatus.getRange() is wrong,", super.makeCompare(b, new byte[]{
					0x02
					,0x03
					,0x04
					,0x05
					}));
			
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
				,CircuitGroupBlockingMessage._MESSAGE_CODE_CGB
				//Circuit group supervision message type
				,0x01 // hardware failure oriented
				,0x01 // ptr to variable part
				//no optional, so no pointer
				//RangeAndStatus._PARAMETER_CODE
				,0x05
				,0x01
				,0x02
				,0x03
				,0x04
				,0x05

		};
		return message;
	}

	@Override
	protected ISUPMessage getDefaultMessage() {
		return super.messageFactory.createCGB();
	}
}
