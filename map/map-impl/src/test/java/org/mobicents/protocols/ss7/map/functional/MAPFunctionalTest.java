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
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.dialog.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.service.supplementary.MAPServiceSupplementaryImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author amit bhayani
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

		this.stack1 = new MAPStackImpl(this.sccpProvider1, 8);
		this.stack2 = new MAPStackImpl(this.sccpProvider2, 8);

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
		server.setStep(FunctionalTestScenario.actionA);
		client.setStep(FunctionalTestScenario.actionA);
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
		server.setStep(FunctionalTestScenario.actionA);
		client.setStep(FunctionalTestScenario.actionA);
		client.actionA();
		waitForEnd();
		assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
		assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.actionB);
		client.setStep(FunctionalTestScenario.actionB);
		client.actionB();
		waitForEnd();
		assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
		assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());

		((MAPServiceSupplementaryImpl) this.stack2.getMAPProvider().getMAPServiceSupplementary()).setTestMode(1);
		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.actionC);
		client.setStep(FunctionalTestScenario.actionC);
		client.actionB();
		waitForEnd();
		assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
		assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());
		((MAPServiceSupplementaryImpl) this.stack2.getMAPProvider().getMAPServiceSupplementary()).setTestMode(0);

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.actionD);
		client.setStep(FunctionalTestScenario.actionD);
		client.actionB();
		waitForEnd();
		assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
		assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());

		server.reset();
		client.reset();
		server.setStep(FunctionalTestScenario.actionE);
		client.setStep(FunctionalTestScenario.actionE);
		client.actionB();
		waitForEnd();
		assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
		assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());

		// ((MAPProviderImpl) this.stack2.getMAPProvider()).setTestMode(1);
		// server.reset();
		// client.reset();
		// server.setStep(FunctionalTestScenario.actionF);
		// client.setStep(FunctionalTestScenario.actionF);
		// client.actionB();
		// waitForEnd();
		// assertTrue("Client side did not finish: " + client.getStatus(),
		// client.isFinished());
		// assertTrue("Server side did not finish: " + server.getStatus(),
		// server.isFinished());
		// ((MAPProviderImpl) this.stack2.getMAPProvider()).setTestMode(0);
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

				// Thread.currentThread().sleep(_WAIT_TIMEOUT);
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
