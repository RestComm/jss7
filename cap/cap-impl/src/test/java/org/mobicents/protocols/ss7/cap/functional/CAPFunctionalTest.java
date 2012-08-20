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

package org.mobicents.protocols.ss7.cap.functional;

import static org.testng.Assert.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mobicents.protocols.ss7.cap.CAPStackImpl;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageSystemFailure;
import org.mobicents.protocols.ss7.cap.api.errors.UnavailableNetworkResource;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.MonitorMode;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AudibleIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.NAINumber;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class CAPFunctionalTest extends SccpHarness {

	private static Logger logger = Logger.getLogger(CAPFunctionalTest.class);
	private static final int _WAIT_TIMEOUT = 500;
	private static final int _TCAP_DIALOG_RELEASE_TIMEOUT = 0;

	private CAPStackImpl stack1;
	private CAPStackImpl stack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;
	private Server server;

	@BeforeClass
	public void setUpClass() throws Exception {

		System.out.println("setUpClass");
	}

	@AfterClass
	public void tearDownClass() throws Exception {
		System.out.println("tearDownClass");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@BeforeMethod
	public void setUp() {
		// this.setupLog4j();
		System.out.println("setUpTest");
		
		this.sccpStack1Name = "CAPFunctionalTestSccpStack1";
		this.sccpStack2Name = "CAPFunctionalTestSccpStack2";
		
		super.setUp();

		// this.setupLog4j();

		// create some fake addresses.
		

		peer1Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1, null, 8);
		peer2Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2, null, 8);

		this.stack1 = new CAPStackImplWrapper(this.sccpProvider1, 8);
		this.stack2 = new CAPStackImplWrapper(this.sccpProvider2, 8);

		this.stack1.start();
		this.stack2.start();

		// create test classes
//		this.client = new Client(this.stack1, this, peer1Address, peer2Address);
//		this.server = new Server(this.stack2, this, peer2Address, peer1Address);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */

	@AfterMethod
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

	/**
	 * InitialDP + Error message SystemFailure
	 * 
	 * TC-BEGIN + InitialDP
	 * TC-END + Error message SystemFailure
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testInitialDp_Error() throws Exception {
		
		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onErrorComponent(CAPDialog capDialog, Long invokeId, CAPErrorMessage capErrorMessage) {
				super.onErrorComponent(capDialog, invokeId, capErrorMessage);

				assertTrue(capErrorMessage.isEmSystemFailure());
				CAPErrorMessageSystemFailure em = capErrorMessage.getEmSystemFailure();
				assertEquals(em.getUnavailableNetworkResource(), UnavailableNetworkResource.endUserFailure);
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			private int dialogStep;
			private long processUnstructuredSSRequestInvokeId = 0l;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));
				CAPErrorMessage capErrorMessage = this.capErrorMessageFactory.createCAPErrorMessageSystemFailure(UnavailableNetworkResource.endUserFailure);
				try {
					ind.getCAPDialog().sendErrorComponent(ind.getInvokeId(), capErrorMessage);
				} catch (CAPException e) {
					this.error("Error while trying to send Response SystemFailure", e);
				}
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				try {
					capDialog.close(false);
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ErrorComponent, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Circuit switch call simple
	 * 
	 * TC-BEGIN + InitialDP
	 *   TC-CONTINUE + RequestReportBCSMEvent
	 *   TC-CONTINUE + ApplyCharging + Connect
	 * TC-CONTINUE + EventReportBCSM (OAnswer)
	 * TC-CONTINUE + ApplyChargingReport
	 * <call... waiting till DialogTimeout>
	 *   TC-CONTINUE + ActivityTestRequest
	 * TC-CONTINUE + ActivityTestResponse
	 * TC-CONTINUE + EventReportBCSM (ODisconnect)
	 *   TC-END (empty)
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testCircuitCall1() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
				super.onRequestReportBCSMEventRequest(ind);

				assertEquals(ind.getBCSMEventList().size(), 7);

				BCSMEvent ev = ind.getBCSMEventList().get(0);
				assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
				assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
				assertNull(ev.getLegID());
				ev = ind.getBCSMEventList().get(1);
				assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oCalledPartyBusy);
				assertEquals(ev.getMonitorMode(), MonitorMode.interrupted);
				assertNull(ev.getLegID());
				ev = ind.getBCSMEventList().get(2);
				assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oNoAnswer);
				assertEquals(ev.getMonitorMode(), MonitorMode.interrupted);
				assertNull(ev.getLegID());
				ev = ind.getBCSMEventList().get(3);
				assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oAnswer);
				assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
				assertNull(ev.getLegID());
				ev = ind.getBCSMEventList().get(4);
				assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
				assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
				assertEquals(ev.getLegID().getSendingSideID(), LegType.leg1);
				ev = ind.getBCSMEventList().get(5);
				assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
				assertEquals(ev.getMonitorMode(), MonitorMode.interrupted);
				assertEquals(ev.getLegID().getSendingSideID(), LegType.leg2);
				ev = ind.getBCSMEventList().get(6);
				assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oAbandon);
				assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
				assertNull(ev.getLegID());

			}

			@Override
			public void onApplyChargingRequest(ApplyChargingRequest ind) {
				super.onApplyChargingRequest(ind);

				assertEquals(ind.getAChBillingChargingCharacteristics().getMaxCallPeriodDuration(), 1000);
				assertTrue(ind.getAChBillingChargingCharacteristics().getReleaseIfdurationExceeded());
				assertNull(ind.getAChBillingChargingCharacteristics().getTariffSwitchInterval());
				assertEquals(ind.getPartyToCharge().getSendingSideID(), LegType.leg1);
				assertNull(ind.getExtensions());
				assertNull(ind.getAChChargingAddress());
			}

			@Override
			public void onConnectRequest(ConnectRequest ind) {
				super.onConnectRequest(ind);

				try {
					assertEquals(ind.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
					CalledPartyNumber calledPartyNumber = ind.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber();
					assertTrue(calledPartyNumber.getAddress().equals("5599999988"));
					assertEquals(calledPartyNumber.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(calledPartyNumber.getNumberingPlanIndicator(), CalledPartyNumber._NPI_ISDN);
					assertEquals(calledPartyNumber.getInternalNetworkNumberIndicator(), CalledPartyNumber._INN_ROUTING_ALLOWED);
				} catch (CAPException e) {
					e.printStackTrace();
					fail("Exception while checking ConnectRequest imdication", e);
				}
				assertNull(ind.getAlertingPattern());
				assertNull(ind.getCallingPartysCategory());
				assertNull(ind.getChargeNumber());
				assertNull(ind.getGenericNumbers());
				assertNull(ind.getLegToBeConnected());
				assertNull(ind.getOriginalCalledPartyID());

				dialogStep = 1;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;

				try {
					switch (dialogStep) {
					case 1: // after ConnectRequest
						OAnswerSpecificInfo oAnswerSpecificInfo;
						ReceivingSideID legID;
						MiscCallInfo miscCallInfo;
						EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capParameterFactory.createEventSpecificInformationBCSM(oAnswerSpecificInfo);
						dlg.addEventReportBCSMRequest(EventTypeBCSM.oAnswer, eventSpecificInformationBCSM, legID, miscCallInfo, null);
						dlg.send();

						
						
						
						
//						ArrayList<BCSMEvent> bcsmEventList = new ArrayList<BCSMEvent>();
//						BCSMEvent ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.routeSelectFailure, MonitorMode.notifyAndContinue,
//								null, null, false);
//						bcsmEventList.add(ev);
//						ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oCalledPartyBusy, MonitorMode.interrupted, null, null, false);
//						bcsmEventList.add(ev);
//						ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oNoAnswer, MonitorMode.interrupted, null, null, false);
//						bcsmEventList.add(ev);
//						ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oAnswer, MonitorMode.notifyAndContinue, null, null, false);
//						bcsmEventList.add(ev);
//						LegID legId = this.inapParameterFactory.createLegID(true, LegType.leg1);
//						ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oDisconnect, MonitorMode.notifyAndContinue, legId, null, false);
//						bcsmEventList.add(ev);
//						legId = this.inapParameterFactory.createLegID(true, LegType.leg2);
//						ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oDisconnect, MonitorMode.interrupted, legId, null, false);
//						bcsmEventList.add(ev);
//						ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oAbandon, MonitorMode.notifyAndContinue, null, null, false);
//						bcsmEventList.add(ev);
//						dlg.addRequestReportBCSMEventRequest(bcsmEventList, null);
//						this.observerdEvents.add(TestEvent.createSentEvent(EventType.RequestReportBCSMEventRequest, null, sequence++));
//						dlg.send();
//
//						CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics = this.capParameterFactory
//								.createCAMELAChBillingChargingCharacteristics(1000, true, null, null, null, false);
//						SendingSideID partyToCharge = this.capParameterFactory.createSendingSideID(LegType.leg1);
//						dlg.addApplyChargingRequest(aChBillingChargingCharacteristics, partyToCharge, null, null);
//						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ApplyChargingRequest, null, sequence++));
//
//						ArrayList<CalledPartyNumberCap> calledPartyNumber = new ArrayList<CalledPartyNumberCap>();
//						CalledPartyNumber cpn = this.isupParameterFactory.createCalledPartyNumber();
//						cpn.setAddress("5599999988");
//						cpn.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
//						cpn.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
//						cpn.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
//						CalledPartyNumberCap cpnc = this.capParameterFactory.createCalledPartyNumberCap(cpn);
//						calledPartyNumber.add(cpnc);
//						DestinationRoutingAddress destinationRoutingAddress = this.capParameterFactory.createDestinationRoutingAddress(calledPartyNumber);
//						dlg.addConnectRequest(destinationRoutingAddress, null, null, null, null, null, null, null, null, null, null, null, null,
//								false, false, false, null, false);
//						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ConnectRequest, null, sequence++));
//						dlg.send();

						break;
					}
					
					
					capDialog.close(false);
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				dialogStep = 1;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;
				
				try {
					switch (dialogStep) {
					case 1: // after InitialDp
						ArrayList<BCSMEvent> bcsmEventList = new ArrayList<BCSMEvent>();
						BCSMEvent ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.routeSelectFailure, MonitorMode.notifyAndContinue,
								null, null, false);
						bcsmEventList.add(ev);
						ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oCalledPartyBusy, MonitorMode.interrupted, null, null, false);
						bcsmEventList.add(ev);
						ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oNoAnswer, MonitorMode.interrupted, null, null, false);
						bcsmEventList.add(ev);
						ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oAnswer, MonitorMode.notifyAndContinue, null, null, false);
						bcsmEventList.add(ev);
						LegID legId = this.inapParameterFactory.createLegID(true, LegType.leg1);
						ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oDisconnect, MonitorMode.notifyAndContinue, legId, null, false);
						bcsmEventList.add(ev);
						legId = this.inapParameterFactory.createLegID(true, LegType.leg2);
						ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oDisconnect, MonitorMode.interrupted, legId, null, false);
						bcsmEventList.add(ev);
						ev = this.capParameterFactory.createBCSMEvent(EventTypeBCSM.oAbandon, MonitorMode.notifyAndContinue, null, null, false);
						bcsmEventList.add(ev);
						dlg.addRequestReportBCSMEventRequest(bcsmEventList, null);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.RequestReportBCSMEventRequest, null, sequence++));
						dlg.send();

						CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics = this.capParameterFactory
								.createCAMELAChBillingChargingCharacteristics(1000, true, null, null, null, false);
						SendingSideID partyToCharge = this.capParameterFactory.createSendingSideID(LegType.leg1);
						dlg.addApplyChargingRequest(aChBillingChargingCharacteristics, partyToCharge, null, null);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ApplyChargingRequest, null, sequence++));

						ArrayList<CalledPartyNumberCap> calledPartyNumber = new ArrayList<CalledPartyNumberCap>();
						CalledPartyNumber cpn = this.isupParameterFactory.createCalledPartyNumber();
						cpn.setAddress("5599999988");
						cpn.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
						cpn.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
						cpn.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
						CalledPartyNumberCap cpnc = this.capParameterFactory.createCalledPartyNumberCap(cpn);
						calledPartyNumber.add(cpnc);
						DestinationRoutingAddress destinationRoutingAddress = this.capParameterFactory.createDestinationRoutingAddress(calledPartyNumber);
						dlg.addConnectRequest(destinationRoutingAddress, null, null, null, null, null, null, null, null, null, null, null, null,
								false, false, false, null, false);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ConnectRequest, null, sequence++));
						dlg.send();

						break;
					}
					
					
					capDialog.close(false);
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

//		te = TestEvent.createReceivedEvent(EventType.ErrorComponent, null, count++, stamp);
//		clientExpectedEvents.add(te);
//
//		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
//		clientExpectedEvents.add(te);
//
//		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
//		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

//		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
//		serverExpectedEvents.add(te);
//
//		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
//		serverExpectedEvents.add(te);
//
//		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
//		serverExpectedEvents.add(te);

		client.sendInitialDp();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}


//	@Test(groups = { "functional.flow","functional"})
//	public void testSimple() throws Exception {
//		server.reset();
//		client.reset();
//		server.setStep(FunctionalTestScenario.Action_InitilDp);
//		client.setStep(FunctionalTestScenario.Action_InitilDp);
//		client.actionA();
//		waitForEnd();
//		assertTrue(client.isFinished(),"Client side did not finish: " + client.getStatus());
//		assertTrue(server.isFinished(),"Server side did not finish: " + server.getStatus());
//	}

	private void waitForEnd() {
		try {
			Date startTime = new Date();
//			while (true) {
//				if (client.isFinished() && server.isFinished())
//					break;
//
//				Thread.currentThread().sleep(100);
//
//				if (new Date().getTime() - startTime.getTime() > _WAIT_TIMEOUT)
//					break;

				
				Thread.currentThread().sleep(_WAIT_TIMEOUT);
//				 Thread.currentThread().sleep(1000000);
//			}
		} catch (InterruptedException e) {
			fail("Interrupted on wait!");
		}
	}
}

