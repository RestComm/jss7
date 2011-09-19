/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.functional;

import static org.testng.Assert.*;
import org.testng.*;import org.testng.annotations.*;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class MAPFunctionalTest extends SccpHarness {

	private static Logger logger = Logger.getLogger(MAPFunctionalTest.class);
	protected static final String USSD_STRING = "*133#";
	protected static final String USSD_MENU = "Select 1)Wallpaper 2)Ringtone 3)Games";
	private static final int _WAIT_TIMEOUT = 5000;

	private MAPStackImpl stack1;
	private MAPStackImpl stack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;
	private Server server;

	@BeforeClass
	public static void setUpClass() throws Exception {
		System.out.println("setUpClass");
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		System.out.println("tearDownClass");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@BeforeTest
	public void setUp() {
		// this.setupLog4j();
		System.out.println("setUpTest");
		super.setUp();

		// this.setupLog4j();

		// create some fake addresses.
		

		peer1Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1, null, 8);
		peer2Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2, null, 8);

		this.stack1 = new MAPStackImplWrapper(this.sccpProvider1, 8);
		this.stack2 = new MAPStackImplWrapper(this.sccpProvider2, 8);

		this.stack1.start();
		this.stack2.start();
		// create test classes
		this.client = new Client(this.stack1, this, peer1Address, peer2Address);
		this.server = new Server(this.stack2, this, peer2Address, peer1Address);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */

	@AfterTest
	public void tearDown() {
		System.out.println("tearDownTest");
		this.stack1.stop();
		this.stack2.stop();
		super.tearDown();
	}
	
	private void setupLog4j() {

		InputStream inStreamLog4j = getClass().getResourceAsStream("/log4j.properties");

		Properties propertiesLog4j = new Properties();

		try {
			propertiesLog4j.load(inStreamLog4j);
			PropertyConfigurator.configure(propertiesLog4j);
		} catch (Exception e) {
			e.printStackTrace();
			BasicConfigurator.configure();
		}

		logger.debug("log4j configured");

	}


	@Test(groups = { "functional.flow","dialog"})
	public void testSimpleTCWithDialog() throws Exception {
		server.setStep(FunctionalTestScenario.Action_Dialog_A);
		client.setStep(FunctionalTestScenario.Action_Dialog_A);
		client.actionA();
		client.start();
		waitForEnd();
		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());

	}

	@Test(groups = { "functional.flow","dialog"})
	public void testComplexTCWithDialog() throws Exception {
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Dialog_A);
		client.setStep(FunctionalTestScenario.Action_Dialog_A);
		client.actionA();
		waitForEnd();
		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Dialog_B);
		client.setStep(FunctionalTestScenario.Action_Dialog_B);
		client.actionB();
		waitForEnd();
		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());

		((MAPServiceSupplementaryImplWrapper) this.stack2.getMAPProvider().getMAPServiceSupplementary()).setTestMode(1);
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Dialog_C);
		client.setStep(FunctionalTestScenario.Action_Dialog_C);
		client.actionB();
		waitForEnd();
		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());
		((MAPServiceSupplementaryImplWrapper) this.stack2.getMAPProvider().getMAPServiceSupplementary()).setTestMode(0);

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Dialog_D);
		client.setStep(FunctionalTestScenario.Action_Dialog_D);
		client.actionB();
		waitForEnd();
		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Dialog_E);
		client.setStep(FunctionalTestScenario.Action_Dialog_E);
		client.actionB();
		waitForEnd();
		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());

		 ((MAPProviderImplWrapper) this.stack2.getMAPProvider()).setTestMode(1);
		 server.reset();
		 client.reset();
		 server.setStep(FunctionalTestScenario.Action_Dialog_F);
		 client.setStep(FunctionalTestScenario.Action_Dialog_F);
		 client.actionB();
		 waitForEnd();
		assertTrue(client.isFinished(), "Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(), "Server side did not finish: " + server.getStatus());
		 ((MAPProviderImplWrapper) this.stack2.getMAPProvider()).setTestMode(0);
	}

	@Test(groups = { "functional.flow","dialog"})
	public void testComponents() throws Exception {
		
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Component_A);
		client.setStep(FunctionalTestScenario.Action_Component_A);
		client.actionB();
		waitForEnd();
		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Component_B);
		client.setStep(FunctionalTestScenario.Action_Component_B);
		client.actionB();
		waitForEnd();
		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Component_D);
		client.setStep(FunctionalTestScenario.Action_Component_D);
		client.actionB();
		waitForEnd();
		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Component_E);
		client.setStep(FunctionalTestScenario.Action_Component_E);
		client.actionB();
		waitForEnd();
		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Component_F);
		client.setStep(FunctionalTestScenario.Action_Component_F);
		client.actionB();
		waitForEnd();
		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Component_G);
		client.setStep(FunctionalTestScenario.Action_Component_G);
		client.actionB();
		waitForEnd();
		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());

	}

	@Test
	public void testV1() throws Exception {

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_V1_A);
		client.setStep(FunctionalTestScenario.Action_V1_A);
		client.actionD();
		waitForEnd();
		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_V1_B);
		client.setStep(FunctionalTestScenario.Action_V1_B);
		client.actionD();
		waitForEnd();
		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_V1_C);
		client.setStep(FunctionalTestScenario.Action_V1_C);
		client.actionD();
		waitForEnd();
		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());
		
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_V1_D);
		client.setStep(FunctionalTestScenario.Action_V1_D);
		client.actionD();
		waitForEnd();
		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_V1_E);
		client.setStep(FunctionalTestScenario.Action_V1_E);
		client.actionD();
		waitForEnd();
		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());
	}

	@Test(groups = { "functional.flow","dialog"})
	public void testSmsService() throws Exception {
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Sms_AlertServiceCentre);
		client.setStep(FunctionalTestScenario.Action_Sms_AlertServiceCentre);
		client.actionC();
		waitForEnd();
		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());
		
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Sms_ForwardSM);
		client.setStep(FunctionalTestScenario.Action_Sms_ForwardSM);
		client.actionC();
		waitForEnd();
		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());
		
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Sms_MoForwardSM);
		client.setStep(FunctionalTestScenario.Action_Sms_MoForwardSM);
		client.actionC();
		waitForEnd();
		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());
		
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Sms_MtForwardSM);
		client.setStep(FunctionalTestScenario.Action_Sms_MtForwardSM);
		client.actionC();
		waitForEnd();
		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());
		
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Sms_ReportSMDeliveryStatus);
		client.setStep(FunctionalTestScenario.Action_Sms_ReportSMDeliveryStatus);
		client.actionC();
		waitForEnd();
		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());
		
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Sms_SendRoutingInfoForSM);
		client.setStep(FunctionalTestScenario.Action_Sms_SendRoutingInfoForSM);
		client.actionC();
		waitForEnd();
		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());
	}

	@Test(groups = { "functional.flow","dialog"})
	public void testMsgLength() throws Exception {

		this.saveTrafficInFile();
		
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_TestMsgLength_B);
		client.setStep(FunctionalTestScenario.Action_TestMsgLength_B);
		client.actionE();
		waitForEnd();
		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());
	}

	@Test(groups = { "functional.flow","dialog"})
	public void testA() throws Exception {

//		MapParameterFactory msf = this.stack1.getMAPProvider().getMapParameterFactory();
//		IMEI a1 = msf.createIMEI("12345678901234");
//
//		AsnOutputStream aos = new AsnOutputStream();
//		a1.encode(aos);
//		byte[] buf = aos.toByteArray();
//
//		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(buf));
//		IMEIImpl a11 = new IMEIImpl();
//		a11.decode(ais, 0, false, 0, buf.length);
//
//		 int fff=0;
//		 fff++;
		
//		MapParameterFactory msf = this.stack1.getMAPProvider().getMapParameterFactory();
//
//		ISDNAddressString n1 = msf.createISDNAddressString(AddressNature.subscriber_number, NumberingPlan.ISDN, "98765");
//		LMSI n2 = msf.createLMSI(new byte[] { 21, 22, 23, 24 });
//		ISDNAddressString n3 = msf.createISDNAddressString(AddressNature.network_specific_number, NumberingPlan.land_mobile, "7654321");
//		
//		LocationInfoWithLMSI a1 = msf.createLocationInfoWithLMSI(n1, n2, MAPFunctionalTest.GetTestExtensionContainer(msf), AdditionalNumberType.msc, n3);		
//
//		AsnOutputStream aos = new AsnOutputStream();
//		a1.encode(aos);
//		byte[] buf = aos.toByteArray();
//
//		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(buf));
//		LocationInfoWithLMSIImpl a11 = new LocationInfoWithLMSIImpl();
//		a11.decode(ais, 0, false, 0, buf.length);
//		
//		boolean b1 = MAPFunctionalTest.CheckTestExtensionContainer(a11.getExtensionContainer());
//		int fff=0;
//		fff++;
		
		
//		MapParameterFactory msf = this.stack1.getMAPProvider().getMapParameterFactory();
//		MAPOpenInfoImpl a1 = new MAPOpenInfoImpl();
//		a1.setExtensionContainer(MAPFunctionalTest.GetTestExtensionContainer(msf));
//		AddressString n1 = msf.createAddressString(AddressNature.subscriber_number, NumberingPlan.ISDN, "1234567"); 		
//		AddressString n2 = msf.createAddressString(AddressNature.network_specific_number, NumberingPlan.land_mobile, "7654321");
//		a1.setOrigReference(n1); 		
//		a1.setDestReference(n2); 		
//
//		AsnOutputStream aos = new AsnOutputStream();
//		a1.encode(aos);
//		byte[] buf = aos.toByteArray();
//
//		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(buf));
//		MAPOpenInfoImpl a11 = new MAPOpenInfoImpl();
//		int tag = ais.readTag();
//		a11.decode(ais);
//		
//		boolean b1 = MAPFunctionalTest.CheckTestExtensionContainer(a11.getExtensionContainer());
//		
//		int fff=0;
//		fff++;

		
//		MapParameterFactory msf = this.stack1.getMAPProvider().getMapParameterFactory();
//		ISDNAddressString a1 = msf.createISDNAddressString(AddressNature.subscriber_number, NumberingPlan.ISDN, "98765");
//
//		AsnOutputStream aos = new AsnOutputStream();
//		a1.encode(aos);
//		byte[] buf = aos.toByteArray();
//
//		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(buf));
//		ISDNAddressString a2 = new ISDNAddressStringImpl();
//		a2.decode(ais, 0, true, 0, buf.length);
//		
//		int fff=0;
//		fff++;

		
//		MapParameterFactory msf = this.stack1.getMAPProvider().getMapParameterFactory();
//		
////		AddressString asx = msf.createAddressString(AddressNature.subscriber_number, NumberingPlan.ISDN, "98765");
////		SM_RP_DAImpl sm = (SM_RP_DAImpl)msf.createSM_RP_DA(asx);
//		
////		IMSIImpl imsi = (IMSIImpl)msf.createIMSI(250L, 7L, "12345");
////		SM_RP_DAImpl sm = (SM_RP_DAImpl)msf.createSM_RP_DA(imsi);
//
////		byte[] data = new byte[] { 31, 32, 33, 34 };
////		LMSIImpl lmsi = (LMSIImpl)msf.createLMSI(data);
////		SM_RP_DAImpl sm = (SM_RP_DAImpl)msf.createSM_RP_DA(lmsi);
//		
////		SM_RP_DAImpl sm = (SM_RP_DAImpl)msf.createSM_RP_DA();
//		
////		LMSIImpl lmsi = new LMSIImpl();
////		byte[] data = new byte[] { 31, 32, 33, 34 };
////		lmsi.setData(data);
////		sm.setLMSI(lmsi);
//		
//		SM_RP_OAImpl sm = (SM_RP_OAImpl)msf.createSM_RP_OA();
//
////		ISDNAddressString msisdn = msf.createISDNAddressString(AddressNature.subscriber_number, NumberingPlan.ISDN, "98765");
////		sm.setMsisdn(msisdn);
////		AddressString serviceCentreAddressOA = msf.createAddressString(AddressNature.subscriber_number, NumberingPlan.ISDN, "98765");
////		sm.setServiceCentreAddressOA(serviceCentreAddressOA);
//
//		
//		AsnOutputStream aos = new AsnOutputStream();
//		sm.encode(aos);
//		byte[] buf = aos.toByteArray();
//		
//		aos.reset();
//		aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, sm.getEnccodingTag());
//		aos.writeLength(buf.length);
//		aos.write(buf);
//		buf = aos.toByteArray();
//		
//
//		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(buf));
//		int tag = ais.readTag();
//		int length = ais.readLength();
//		if (length != ais.available()) {
//			int fff = 0;
//		}
//		byte[] buf2 = new byte[length];
//		ais.read(buf2);
//		AsnInputStream ais2 = new AsnInputStream(new ByteArrayInputStream(buf2));
//		
////		SM_RP_DAImpl sm2 = new SM_RP_DAImpl();
//		SM_RP_OAImpl sm2 = new SM_RP_OAImpl();
//		sm2.setDecodedTag(tag);
//		sm2.decode(ais2);
//		
//		int iii=0;
//		iii++;

		
//		server.reset();
//		client.reset();
//		server.setStep(FunctionalTestScenario.Action_Component_A);
//		client.setStep(FunctionalTestScenario.Action_Component_A);
//		client.actionC();
//		waitForEnd();
//		assertTrue( client.isFinished(),"Client side did not finish: " + client.getStatus());
//		assertTrue( server.isFinished(),"Server side did not finish: " + server.getStatus());
	}
	
	private void waitForEnd() {
		try {
			Date startTime = new Date();
			while (true) {
				if (client.isFinished() && server.isFinished())
					break;

				Thread.currentThread().sleep(100);

//				if (new Date().getTime() - startTime.getTime() > _WAIT_TIMEOUT)
//					break;

				 Thread.currentThread().sleep(1000000);
			}
		} catch (InterruptedException e) {
			fail("Interrupted on wait!");
		}
	}
}
