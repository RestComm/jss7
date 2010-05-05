package org.mobicents.protocols.ss7.stream.tcp;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.Test;
import org.mobicents.protocols.ss7.stream.HDLCHandler;

import junit.framework.TestCase;

public class HDLCHandlerTest extends TestCase {

	private HDLCHandler senderHandler = null;
	private HDLCHandler receviverHandler = null;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		senderHandler = new HDLCHandler();
		receviverHandler = new HDLCHandler();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	@Test
	public void testSend()
	{
		byte[] s1 = new byte[]{1,2,3,4,5,6,7,8};
		ByteBuffer bb1 = ByteBuffer.wrap(s1);
		

		
		senderHandler.addToTxBuffer(bb1);
		ByteBuffer processed = ByteBuffer.allocate(400);
		senderHandler.processTx(processed);
		processed.flip();
		ByteBuffer[] received = receviverHandler.processRx(processed);
		if(received==null)
		{
			fail("No data received!");
		}else
		{
			//we should have one here
			assertEquals("Expected single buffer.",1, received.length);
			ByteBuffer rcv = received[0];
			assertTrue("Failed, wrong data! Sent/Rcv: \n"+Arrays.toString(s1)+"\n"+Arrays.toString(rcv.array()), Arrays.equals(s1, rcv.array()));
		}	
	}
	
	
	@Test
	public void testSend2()
	{
		byte[] s1 = new byte[]{85, 1, 1};
		byte[] s2 = new byte[]{85, 1, 0};
		ByteBuffer bb1 = ByteBuffer.wrap(s1);
		ByteBuffer bb2 = ByteBuffer.wrap(s2);
		

		
		senderHandler.addToTxBuffer(bb1);
		senderHandler.addToTxBuffer(bb2);
		ByteBuffer processed = ByteBuffer.allocate(400);
		senderHandler.processTx(processed);
		processed.flip();
		ByteBuffer[] received = receviverHandler.processRx(processed);
		if(received==null)
		{
			fail("No data received!");
		}else
		{
			//we should have one here
			assertEquals("Expected two buffers.",2, received.length);
			ByteBuffer rcv = received[0];
			assertTrue("Failed, wrong data! Sent/Rcv: \n"+Arrays.toString(s1)+"\n"+Arrays.toString(rcv.array()), Arrays.equals(s1, rcv.array()));
			rcv = received[1];
			assertTrue("Failed, wrong data! Sent/Rcv: \n"+Arrays.toString(s2)+"\n"+Arrays.toString(rcv.array()), Arrays.equals(s2, rcv.array()));
		}	
	}

}
