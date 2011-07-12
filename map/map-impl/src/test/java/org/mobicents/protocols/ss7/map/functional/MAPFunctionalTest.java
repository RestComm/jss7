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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.MAPServiceSupplementaryImpl;

import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.dialog.MAPOpenInfoImpl;
import org.mobicents.protocols.ss7.map.service.sms.LocationInfoWithLMSIImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_DAImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_OAImpl;

import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

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
	@Before
	public void setUp() {
		// this.setupLog4j();

		super.setUp();

		// this.setupLog4j();

		// create some fake addresses.
		GlobalTitle gt1 = GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "123");
		GlobalTitle gt2 = GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "321");

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */

	@After
	public void tearDown() {

		this.stack1.stop();
		this.stack2.stop();
		super.tearDown();
	}

	@Test
	public void testSimpleTCWithDialog() throws Exception {
		server.setStep(FunctionalTestScenario.Action_Dialog_A);
		client.setStep(FunctionalTestScenario.Action_Dialog_A);
		client.actionA();
		client.start();
		waitForEnd();
		assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
		assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());

	}

	@Test
	public void testComplexTCWithDialog() throws Exception {
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Dialog_A);
		client.setStep(FunctionalTestScenario.Action_Dialog_A);
		client.actionA();
		waitForEnd();
		assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
		assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Dialog_B);
		client.setStep(FunctionalTestScenario.Action_Dialog_B);
		client.actionB();
		waitForEnd();
		assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
		assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());

		((MAPServiceSupplementaryImplWrapper) this.stack2.getMAPProvider().getMAPServiceSupplementary()).setTestMode(1);
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Dialog_C);
		client.setStep(FunctionalTestScenario.Action_Dialog_C);
		client.actionB();
		waitForEnd();
		assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
		assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());
		((MAPServiceSupplementaryImplWrapper) this.stack2.getMAPProvider().getMAPServiceSupplementary()).setTestMode(0);

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Dialog_D);
		client.setStep(FunctionalTestScenario.Action_Dialog_D);
		client.actionB();
		waitForEnd();
		assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
		assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.Action_Dialog_E);
		client.setStep(FunctionalTestScenario.Action_Dialog_E);
		client.actionB();
		waitForEnd();
		assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
		assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());

		 ((MAPProviderImplWrapper) this.stack2.getMAPProvider()).setTestMode(1);
		 server.reset();
		 client.reset();
		 server.setStep(FunctionalTestScenario.Action_Dialog_F);
		 client.setStep(FunctionalTestScenario.Action_Dialog_F);
		 client.actionB();
		 waitForEnd();
		 assertTrue("Client side did not finish: " + client.getStatus(),
		 client.isFinished());
		 assertTrue("Server side did not finish: " + server.getStatus(),
		 server.isFinished());
		 ((MAPProviderImplWrapper) this.stack2.getMAPProvider()).setTestMode(0);
	}


	@Test
	public void testA() throws Exception {

//		MapServiceFactory msf = this.stack1.getMAPProvider().getMapServiceFactory();
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
		
		
//		MapServiceFactory msf = this.stack1.getMAPProvider().getMapServiceFactory();
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

		
//		MapServiceFactory msf = this.stack1.getMAPProvider().getMapServiceFactory();
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

		
//		MapServiceFactory msf = this.stack1.getMAPProvider().getMapServiceFactory();
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
//		assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
//		assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());
	}
	
	private void waitForEnd() {
		try {
			Date startTime = new Date();
			while (true) {
				if (client.isFinished() && server.isFinished())
					break;

				Thread.currentThread().sleep(100);

				if (new Date().getTime() - startTime.getTime() > _WAIT_TIMEOUT)
					break;

//				 Thread.currentThread().sleep(1000000);
			}
		} catch (InterruptedException e) {
			fail("Interrupted on wait!");
		}
	}

	public static MAPExtensionContainer GetTestExtensionContainer(MapServiceFactory mapServiceFactory) {
		ArrayList<MAPPrivateExtension> al = new ArrayList<MAPPrivateExtension>();
		al.add(mapServiceFactory
				.createMAPPrivateExtension(new long[] { 1, 2, 3, 4 }, new byte[] { 11, 12, 13, 14, 15 }));
		al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 6 }, null));
		al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 5 }, new byte[] { 21, 22, 23, 24, 25,
				26 }));

		MAPExtensionContainer cnt = mapServiceFactory.createMAPExtensionContainer(al, new byte[] { 31, 32, 33 });

		return cnt;
	}

	protected static Boolean CheckTestExtensionContainer(MAPExtensionContainer extContainer) {
		if (extContainer == null || extContainer.getPrivateExtensionList().size() != 3)
			return false;

		for (int i = 0; i < 3; i++) {
			MAPPrivateExtension pe = extContainer.getPrivateExtensionList().get(i);
			long[] lx = null;
			byte[] bx = null;

			switch (i) {
			case 0:
				lx = new long[] { 1, 2, 3, 4 };
				bx = new byte[] { 11, 12, 13, 14, 15 };
				break;
			case 1:
				lx = new long[] { 1, 2, 3, 6 };
				bx = null;
				break;
			case 2:
				lx = new long[] { 1, 2, 3, 5 };
				bx = new byte[] { 21, 22, 23, 24, 25, 26 };
				break;
			}

			if (pe.getOId() == null || !Arrays.equals(pe.getOId(), lx))
				return false;
			if (bx == null) {
				if (pe.getData() != null)
					return false;
			} else {
				if (pe.getData() == null || !Arrays.equals(pe.getData(), bx))
					return false;
			}
		}

		byte[] by = new byte[] { 31, 32, 33 };
		if (extContainer.getPcsExtensions() == null || !Arrays.equals(extContainer.getPcsExtensions(), by))
			return false;

		return true;
	}
}
