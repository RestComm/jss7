/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.mobicents.protocols.ss7.isup.ISUPComponent;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class CircuitAssigmentMapTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public CircuitAssigmentMapTest() throws IOException {
		super.badBodies.add(new byte[1]);
		super.badBodies.add(new byte[6]);

		super.goodBodies.add(getBody1());
		
	}

	private byte[] getBody1() throws IOException {
		
		// we will use odd number of digits, so we leave zero as MSB
		byte[] body = new byte[5];
		body[0]=12;
		body[1]=120;
		body[2]=67;
		return body;
	}

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		CircuitAssigmentMapImpl bci = new CircuitAssigmentMapImpl(getBody1());
	
		int format = 120;
	
		format |= 67 << 8;
		format |= 0 << 16;
		format |= 0 << 24;
		String[] methodNames = { "getMapType","getMapFormat"};
		Object[] expectedValues = { 12, format};
		super.testValues(bci, methodNames, expectedValues);
	}

	
	public void testBoundries() throws IOException, ParameterRangeInvalidException
	{
		CircuitAssigmentMapImpl bci = new CircuitAssigmentMapImpl(getBody1());
		
		try{
			bci.disableCircuit(0);
			fail("Failed, disabled circuit 0");
		}catch(IllegalArgumentException e)
		{
			
		}
		try{
			bci.disableCircuit(32);
			fail("Failed, disabled circuit 32");
		}catch(IllegalArgumentException e)
		{
			
		}
		
		try{
			bci.enableCircuit(0);
			fail("Enabled, disabled circuit 0");
		}catch(IllegalArgumentException e)
		{
			
		}
		try{
			bci.enableCircuit(32);
			fail("Enabled, disabled circuit 32");
		}catch(IllegalArgumentException e)
		{
			
		}
	}
	public void testEnableDissable() throws IOException, ParameterRangeInvalidException
	{
		CircuitAssigmentMapImpl bci = new CircuitAssigmentMapImpl(getBody1());
		
		assertFalse("Circuit was enabled, it should not.", bci.isCircuitEnabled(30));
		bci.enableCircuit(30);
		assertTrue("Circuit was not enabled, it should not.", bci.isCircuitEnabled(30));
		bci.disableCircuit(30);
		assertFalse("Circuit was not disabled, it should not.", bci.isCircuitEnabled(30));
		super.makeCompare(getBody1(), bci.encodeElement());
		
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
		return new CircuitAssigmentMapImpl(new byte[5]);
	}

}
