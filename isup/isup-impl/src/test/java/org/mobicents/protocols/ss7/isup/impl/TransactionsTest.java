package org.mobicents.protocols.ss7.isup.impl;

import java.util.Properties;

import org.mobicents.protocols.ss7.isup.ISUPStack;
import org.mobicents.protocols.ss7.stream.PipeMtpProviderImpl;

import junit.framework.TestCase;


public class TransactionsTest extends TestCase {

	private PipeMtpProviderImpl provider1;
	private PipeMtpProviderImpl provider2;
	private ISUPStack isupStack1;
	private ISUPStack isupStack2;
	
	
	
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.provider1 = new PipeMtpProviderImpl();
		this.provider2= this.provider1.getOther();
		Properties props1 = new Properties();
		props1.setProperty("isup.opc","123");
        props1.setProperty("isup.dpc","321");
        props1.setProperty("isup.sls","0");
        props1.setProperty("isup.ssf","0");

        
        Properties props2 = new Properties();
		props2.setProperty("isup.opc","321");
        props2.setProperty("isup.dpc","123");
        props2.setProperty("isup.sls","0");
        props2.setProperty("isup.ssf","0");

        
        this.isupStack1 = new ISUPStackImpl(this.provider1, props1);
        this.isupStack2 = new ISUPStackImpl(this.provider2, props2);
		
        
        this.isupStack1.start();
        this.isupStack2.start();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		this.isupStack1.stop();
        this.isupStack2.stop();
		//this.provider1.stop();
		//this.provider2.stop();
		this.provider1 = null;
		this.provider2 = null;
	}

	public void testIAM() throws Exception
	{
		
		ClientIAM client = new ClientIAM(this.isupStack1);
		ServerIAM server = new ServerIAM(this.isupStack2);
		this.isupStack1.getIsupProvider().addListener(client);
		this.isupStack2.getIsupProvider().addListener(server);
		provider1.indicateLinkUp();
		provider2.indicateLinkUp();
		
		
		
		server.start();
		client.start();
		
		
		Thread.currentThread().sleep(10000);
		
		assertTrue("Client status: "+client.getStatus(),client.isPassed());
		assertTrue("Server status: "+server.getStatus(),server.isPassed());
		
		
	}
	
	public void testREL() throws Exception
	{
		
		ClientREL client = new ClientREL(this.isupStack1);
		ServerREL server = new ServerREL(this.isupStack2);
		this.isupStack1.getIsupProvider().addListener(client);
		this.isupStack2.getIsupProvider().addListener(server);
		provider1.indicateLinkUp();
		provider2.indicateLinkUp();
		
		
		
		server.start();
		client.start();
		
		
		Thread.currentThread().sleep(10000);
		
		assertTrue("Client status: "+client.getStatus(),client.isPassed());
		assertTrue("Server status: "+server.getStatus(),server.isPassed());
		
		
	}
	public void testGRS() throws Exception
	{
		
		ClientGRS client = new ClientGRS(this.isupStack1);
		ServerGRS server = new ServerGRS(this.isupStack2);
		this.isupStack1.getIsupProvider().addListener(client);
		this.isupStack2.getIsupProvider().addListener(server);
		provider1.indicateLinkUp();
		provider2.indicateLinkUp();
		
		
		
		server.start();
		client.start();
		
		
		Thread.currentThread().sleep(10000);
		
		assertTrue("Client status: "+client.getStatus(),client.isPassed());
		assertTrue("Server status: "+server.getStatus(),server.isPassed());
		
		
	}
}
