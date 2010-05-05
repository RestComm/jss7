/**
 * 
 */
package org.mobicents.protocols.ss7.stream.tcp;


import java.util.Properties;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mobicents.protocols.ss7.mtp.Mtp2;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.stream.MTPListener;

/**
 * @author baranowb
 *
 */
public class M3UserTest implements MTPListener{
	
	// ^ thats why i prefered mtps as interfacess......
	private M3UserAgent agent;
	private M3UserConnector connector;
	private ExtendedMtp3 mtp3;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		
		
		linkDown = false;
		linkUp = false;
		data = null;
		
		
		
		
		this.mtp3 = new ExtendedMtp3("TEST_MTP");
		
		agent = new M3UserAgent();
		agent.setLayer3(this.mtp3);
		agent.setAddress("127.0.0.1");
		agent.setPort(1345);
		
		Properties props = new Properties();

		props.put("server.ip", "127.0.0.1");
		props.put("server.port", "1345");
		
		connector = new M3UserConnector(props);
		connector.addMtpListener(this);
		
		
		
		agent.start();
		connector.start();
		long startTime = System.currentTimeMillis();
		while(!agent.isConnected() || !connector.isConnected())
		{
			Thread.currentThread().sleep(500);
			if(startTime+5000<System.currentTimeMillis())
			{
				junit.framework.Assert.assertTrue("Failed to establish connection!",false);
			}
			
		}
		//0, 0, 0, 0, 30, 15, 126, -86, -128, -128, -61, 46, 126, 0, 30, 15, 126, 0, 30, 15, 126, -86, -128, 0, 82, -90, 126, 0, 30, 15
		Thread.currentThread().sleep(1000);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		agent.stop();
		connector.stop();
	}
	
	@Test
	public void testLinkState() throws Exception
	{
		this.agent.linkUp();
		Thread.currentThread().sleep(500);
		this.agent.linkDown();
		Thread.currentThread().sleep(500);
		Assert.assertTrue("Did not receive linkup!", this.linkUp);
		Assert.assertTrue("Did not receive linkdown!", this.linkDown);
		
	}
	
	
	
	
	//MTPListener methods
	
	


	private boolean linkDown = false;
	private boolean linkUp = false;
	private byte[] data;
	public void linkDown() {
		linkDown = true;
		
	}

	public void linkUp() {
		linkUp = true;
		
	}

	public void receive(byte[] msg) {
		data = msg;
		
	}
	
	
	
	
	private class ExtendedMtp3 extends Mtp3
	{

		public ExtendedMtp3(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}


		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#linkFailed(org.mobicents.protocols.ss7.mtp.Mtp2)
		 */
		@Override
		public void linkFailed(Mtp2 link) {
			super.mtpUser.linkDown();
		}


		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#linkUp(org.mobicents.protocols.ss7.mtp.Mtp2)
		 */
		@Override
		public void linkUp(Mtp2 link) {
			super.mtpUser.linkUp();
		}


		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#send(byte[])
		 */
		@Override
		public boolean send(byte[] msg) {
			return false;
		}
		
	}



}
