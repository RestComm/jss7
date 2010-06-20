/**
 * 
 */
package org.mobicents.protocols.ss7.stream.tcp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mobicents.protocols.ss7.mtp.Mtp2;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.protocols.ss7.mtp.SelectorFactory;

/**
 * @author baranowb
 *
 */
public class M3User2Test implements Mtp3Listener{
	
	// ^ thats why i prefered mtps as interfacess......
	private M3UserAgent agent;
	private M3UserConnector connector;
	private ExtendedMtp3 mtp3;
	
	


	private boolean linkDown = false;
	private boolean linkUp = false;
	private List<byte[]> rcvConnector;
	private List<byte[]> rcvAgent;
	private int index;
	private ArrayList<byte[]> sendData;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		
		
		linkDown = false;
		linkUp = false;
		rcvConnector = null;
		
		rcvConnector = new ArrayList<byte[]>();
		rcvAgent = new ArrayList<byte[]>();
		
		this.mtp3 = new ExtendedMtp3();
		
		agent = new M3UserAgent();
		agent.setMtp3(this.mtp3);
		agent.setLocalAddress("127.0.0.1");
		agent.setLocalPort(1345);
		
		Properties props = new Properties();

		props.put("server.ip", "127.0.0.1");
		props.put("server.port", "1345");
		
		connector = new M3UserConnector(props);
		connector.addMtp3Listener(this);
		
		index = 0;
		sendData = new ArrayList<byte[]>();
		sendData.add(new byte[]{0, 0, 0, 0, 30, 15, 126, -86, -128, -128, -61, 46, 126, 0, 30, 15, 126, 0, 30, 15, 126, -86, -128, 0, 82, -90, 126, 0, 30, 15});
		sendData.add(new byte[]{1});
		sendData.add(new byte[]{2});
		sendData.add(new byte[]{1,2,3,4,5,6,7,8,89,90,124,46,67,27,28});
		sendData.add(new byte[]{0, 0, 0, 0, 30, 15, 126, -86, -128, -128, -61, 46, 126, 0, 30, 15, 126, 0, 30, 15, 126, -86, -128, 0, 82, -90, 126, 0, 30, 15});
		sendData.add(new byte[]{0, 0, 0, 0, 30, 15, 126, -86, -128, -128, -61, 46, 126, 0, 30, 15, 126, 0, 30, 15, 126, -86, -128, 0, 82, -90, 126, 0, 30, 15,0, 0, 0, 0, 30, 15, 126, -86, -128, -128, -61, 46, 126, 0, 30, 15, 126, 0, 30, 15, 126, -86, -128, 0, 82, -90, 126, 0, 30, 15,0, 0, 0, 0, 30, 15, 126, -86, -128, -128, -61, 46, 126, 0, 30, 15, 126, 0, 30, 15, 126, -86, -128, 0, 82, -90, 126, 0, 30, 15});
		sendData.add(new byte[]{0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1});
		sendData.add(new byte[]{1,1,1,1,1,1,1,1,1,12,2,33,4,54,5,56,6,67});
		
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
		//test link transport cap
		
		
		connector.start();
		Thread.sleep(15000);
		
		agent.start();
		//
		Thread.currentThread().sleep(7000);
		
		long startTime = System.currentTimeMillis();
		while(!agent.isConnected() || !connector.isConnected())
		{
			Thread.currentThread().sleep(500);
			if(startTime+15000<System.currentTimeMillis())
			{
				junit.framework.Assert.assertTrue("Failed to establish connection!",false);
			}
			
		}
		this.agent.getLinkStateProtocol().linkUp();
		
		
		this.agent.getLinkStateProtocol().receive(sendData.get(index++));
		
		Thread.currentThread().sleep(20000);
		this.agent.getLinkStateProtocol().linkDown();
		Thread.currentThread().sleep(100);
		Assert.assertTrue("Did not receive linkup!", this.linkUp);
		Assert.assertTrue("Did not receive linkdown!", this.linkDown);
		Assert.assertEquals("Not enough data received1!",sendData.size(), rcvConnector.size());
		Assert.assertEquals("Not enough data received2!",sendData.size(), rcvAgent.size());
		for(int index = 0;index<rcvConnector.size();index++)
		{
			byte[] expected = sendData.get(index);
			byte[] rcvConn = rcvConnector.get(index);
			byte[] rcvAgent = this.rcvAgent.get(index);
			Assert.assertTrue("Expected data does not match received1:\n"+Arrays.toString(expected)+"\n"+Arrays.toString(rcvConn), Arrays.equals(expected, rcvConn));
			Assert.assertTrue("Expected data does not match received2:\n"+Arrays.toString(expected)+"\n"+Arrays.toString(rcvAgent), Arrays.equals(expected, rcvAgent));
		}
	}
	
	

	//MTPListener methods


	public void linkDown() {
		linkDown = true;
		
	}

	public void linkUp() {
		linkUp = true;
		
	}

	public void receive(byte[] msg) {
		rcvConnector.add(msg);
		try {
			connector.send(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	private class ExtendedMtp3 implements Mtp3
	{
		private Mtp3Listener listener;

		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#addMtp3Listener(org.mobicents.protocols.ss7.mtp.Mtp3Listener)
		 */
		public void addMtp3Listener(Mtp3Listener lst) {
			this.listener = lst;
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#getDpc()
		 */
		public int getDpc() {
			// TODO Auto-generated method stub
			return 0;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#getOpc()
		 */
		public int getOpc() {
			// TODO Auto-generated method stub
			return 0;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#removeMtp3Listener(org.mobicents.protocols.ss7.mtp.Mtp3Listener)
		 */
		public void removeMtp3Listener(Mtp3Listener lst) {
			this.listener = null;
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#send(byte[])
		 */
		public boolean send(byte[] msg) {
			rcvAgent.add(msg);
			if(index<sendData.size())
			{
				agent.getLinkStateProtocol().receive(sendData.get(index++));
			}
			return true;
		}

		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#setDpc(int)
		 */
		public void setDpc(int dpc) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#setLinks(java.util.List)
		 */
		public void setLinks(List<Mtp2> channels) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#setOpc(int)
		 */
		public void setOpc(int opc) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#setSelectorFactory(org.mobicents.protocols.ss7.mtp.SelectorFactory)
		 */
		public void setSelectorFactory(SelectorFactory selectorFactory) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#start()
		 */
		public void start() throws IOException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see org.mobicents.protocols.ss7.mtp.Mtp3#stop()
		 */
		public void stop() throws IOException {
			// TODO Auto-generated method stub
			
		}
		
	}
	


}
