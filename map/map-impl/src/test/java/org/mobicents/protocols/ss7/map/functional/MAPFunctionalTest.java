package org.mobicents.protocols.ss7.map.functional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.PipeSccpProviderImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPFunctionalTest extends TestCase {
	
	private static Logger logger = Logger.getLogger(MAPFunctionalTest.class);
	
	protected static final String USSD_STRING = "*133#";
	protected static final String USSD_MENU="Select 1)Wallpaper 2)Ringtone 3)Games";
	
	private static final int _WAIT_TIMEOUT = 5000;

	private PipeSccpProviderImpl provider1;
	private PipeSccpProviderImpl provider2;
	
	private MAPStackImpl stack1;
	private MAPStackImpl stack2;
	
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	
	private Client client;
	private Server server;

	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.setupLog4j();
		
		this.provider1 = new PipeSccpProviderImpl();
		this.provider2 = this.provider1.getOther();
		
		//create some fake addresses.
		this.peer1Address = this.provider1.getSccpParameterFactory().getSccpAddress();
		GlobalTitle gt = this.provider1.getSccpParameterFactory().getGlobalTitle100();
		//dont set more, until statics are defined!
		gt.setDigits("5557779");
		this.peer1Address.setGlobalTitle(gt);
		
		this.peer2Address = this.provider1.getSccpParameterFactory().getSccpAddress();
		gt = this.provider1.getSccpParameterFactory().getGlobalTitle100();
		//dont set more, until statics are defined!
		gt.setDigits("5888879");
		this.peer2Address.setGlobalTitle(gt);
		
		
		this.stack1 = new MAPStackImpl(this.provider1);
		this.stack2 = new MAPStackImpl(this.provider2);
		
		this.stack1.start();
		this.stack2.start();
		//create test classes
		this.client = new Client(this.stack1,this, peer1Address, peer2Address);
		this.server = new Server(this.stack2, this, peer2Address, peer1Address);
	}
	
	
	private void setupLog4j(){

		InputStream inStreamLog4j = getClass().getResourceAsStream("/log4j.properties");
		Properties propertiesLog4j = new Properties();
		try {
			propertiesLog4j.load(inStreamLog4j);
			PropertyConfigurator.configure(propertiesLog4j);
		} catch (IOException e) {
			e.printStackTrace();
			BasicConfigurator.configure();
		}

		logger.debug("log4j configured");		
		
	}
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		this.stack1.stop();
		this.stack2.stop();
	}
	@Test
	public void testSimpleTCWithDialog() throws Exception
	{
		client.start();
		waitForEnd();
		assertTrue("Client side did not finish: "+client.getStatus(),client.isFinished());
		assertTrue("Server side did not finish: "+server.getStatus(),server.isFinished());
	}
	
	
	
	private void waitForEnd()
	{
		try {
			Thread.currentThread().sleep(_WAIT_TIMEOUT);
		} catch (InterruptedException e) {
			fail("Interrupted on wait!");
		}
	}
	
	
	
	
}
