/**
 * Start time:17:28:44 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.mobicents.protocols.ss7.isup.ISUPComponent;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.InstructionIndicatorsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ParameterCompatibilityInformationImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.InstructionIndicators;

/**
 * Start time:17:28:44 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class ParameterCompatibilityInformationTest extends ParameterHarness{

	private static final boolean[][] flags = new boolean[4][5];
	private static final int[][] intFlags = new int[4][2];
	private static final int _TRANSIT_EXCHANGE_INDEX = 0;
	private static final int _RLEASE_CALL_INDEX = 1;
	private static final int _SEND_NOTIFICATION_INDEX = 2;
	private static final int _DICARD_MESSAGE_INDEX = 3;
	private static final int _DISCARD_PARAMETER_INDEX = 4;
	private static final int _PASS_NOT_POSSIBLE_INDEX = 0;
	private static final int _BAND_INTERWORKING_INDEX = 1;
	
	static
	{
		intFlags[0][_BAND_INTERWORKING_INDEX] = 3;
		//1
		intFlags[0][_PASS_NOT_POSSIBLE_INDEX]=1;
		//0
		flags[0][_DISCARD_PARAMETER_INDEX]=false;
		//1
		flags[0][_DICARD_MESSAGE_INDEX]=true;
		//0
		flags[0][_SEND_NOTIFICATION_INDEX]=false;
		//1
		flags[0][_RLEASE_CALL_INDEX]=true;
		//1
		flags[0][_TRANSIT_EXCHANGE_INDEX]=true;
		
		//index = 1, its raw, we dont include
		
		//101111
		intFlags[2][_BAND_INTERWORKING_INDEX] = 1;
		//1
		intFlags[2][_PASS_NOT_POSSIBLE_INDEX]=1;
		//0
		flags[2][_DISCARD_PARAMETER_INDEX]=false;
		//1
		flags[2][_DICARD_MESSAGE_INDEX]=true;
		//1
		flags[2][_SEND_NOTIFICATION_INDEX]=true;
		//1
		flags[2][_RLEASE_CALL_INDEX]=true;
		//1
		flags[2][_TRANSIT_EXCHANGE_INDEX]=true;
		
		//111011
		intFlags[3][_BAND_INTERWORKING_INDEX] = 2;
		//1
		intFlags[3][_PASS_NOT_POSSIBLE_INDEX]=1;
		//1
		flags[3][_DISCARD_PARAMETER_INDEX]=true;
		//1
		flags[3][_DICARD_MESSAGE_INDEX]=true;
		//0
		flags[3][_SEND_NOTIFICATION_INDEX]=false;
		//1
		flags[3][_RLEASE_CALL_INDEX]=true;
		//1
		flags[3][_TRANSIT_EXCHANGE_INDEX]=true;
		
		
	}
	
	public ParameterCompatibilityInformationTest() {
		super();
		super.goodBodies.add(getBody1());
	}


	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		ParameterCompatibilityInformationImpl bci = new ParameterCompatibilityInformationImpl(getBody1());
	
		assertEquals("Wrong number of instructions. ",4, bci.size());
		
		//Yeah this is different
		
		for(int index = 0 ; index< bci.size() ; index++)
		{
			//this is raw, we dotn validate
			if(index ==1)
				continue;
			
			InstructionIndicators ii = bci.getInstructionIndicators(index);
			
			String[] methodNames = { "getBandInterworkingIndicator", 
									 "getPassOnNotPossibleIndicator", 
									 "isDiscardParameterIndicator", 
									 "isDiscardMessageIndicator", 
									 "isSendNotificationIndicator",
									 "isReleaseCallindicator",
									 "isTransitAtIntermediateExchangeIndicator"};
		
			Object[] expectedValues = { 
			intFlags[index][_BAND_INTERWORKING_INDEX] ,
			//1
			intFlags[index][_PASS_NOT_POSSIBLE_INDEX],
			//0
			flags[index][_DISCARD_PARAMETER_INDEX],
			//1
			flags[index][_DICARD_MESSAGE_INDEX],
			//0
			flags[index][_SEND_NOTIFICATION_INDEX],
			//1
			flags[index][_RLEASE_CALL_INDEX],
			//1
			flags[index][_TRANSIT_EXCHANGE_INDEX]};
			super.testValues(ii, methodNames, expectedValues);
			
		}
		
		
		
		byte parameterCode = bci.getParameterCode(0);
		assertEquals("Wrong parameter code",1, parameterCode);
		
		
		
	}
	
	
	private byte[] getBody1() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		bos.write(1);
		bos.write(0x80 | 0x2B);
		bos.write(3);
		
		bos.write(2);
		bos.write(0x80 | 0x2B);
		bos.write(0x80 | 3);
		bos.write(12);
		
		bos.write(3);
		bos.write(0x80 | 0x2F);
		bos.write(1);
		
		bos.write(4);
		bos.write(0x80 | 0x3B);
		bos.write(2);
		return bos.toByteArray();
	}


	@Override
	public ISUPComponent getTestedComponent() {
		return new ParameterCompatibilityInformationImpl();
	}

}
