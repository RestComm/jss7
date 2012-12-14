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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mobicents.protocols.ss7.cap.CAPStackImpl;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageSystemFailure;
import org.mobicents.protocols.ss7.cap.api.errors.UnavailableNetworkResource;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.mobicents.protocols.ss7.cap.api.primitives.DateAndTime;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.TimerID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationRequestRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCBeforeAnswer;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAI_GSM0224;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedDigits;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FCIBCCCAMELsequence1;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSend;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeInformation;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Tone;
import org.mobicents.protocols.ss7.cap.api.service.gprs.CAPDialogGprs;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.NAINumber;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.MessageType;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
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
	public void setUp() throws Exception {
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
	 * ACN=CAP-v1-gsmSSF-to-gsmSCF
	 * 
	 * TC-BEGIN + InitialDPRequest
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

		client.sendInitialDp(CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF);
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Circuit switch call simple messageflow 1
	 * ACN=CAP-v2-gsmSSF-to-gsmSCF
	 * 
	 * TC-BEGIN + InitialDPRequest
	 *   TC-CONTINUE + RequestReportBCSMEventRequest
	 *   TC-CONTINUE + FurnishChargingInformationRequest
	 *   TC-CONTINUE + ApplyChargingRequest + ConnectRequest
	 *   TC-CONTINUE + ContinueRequest
	 *   TC-CONTINUE + SendChargingInformationRequest
	 * TC-CONTINUE + EventReportBCSMRequest (OAnswer)
	 * TC-CONTINUE + ApplyChargingReportRequest
	 * <call... waiting till DialogTimeout>
	 *   TC-CONTINUE + ActivityTestRequest
	 * TC-CONTINUE + ActivityTestResponse
	 * TC-CONTINUE + EventReportBCSMRequest (ODisconnect)
	 *   TC-END (empty)
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testCircuitCall1() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			private int dialogStep;
			private long activityTestInvokeId;

			@Override
			public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
				super.onRequestReportBCSMEventRequest(ind);

				this.checkRequestReportBCSMEventRequest(ind);
			}

			public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind) {
				super.onFurnishChargingInformationRequest(ind);

				byte[] freeFormatData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
				assertTrue(Arrays.equals(ind.getFCIBCCCAMELsequence1().getFreeFormatData(), freeFormatData));
				assertEquals(ind.getFCIBCCCAMELsequence1().getPartyToCharge().getSendingSideID(), LegType.leg1);
				assertEquals(ind.getFCIBCCCAMELsequence1().getAppendFreeFormatData(), AppendFreeFormatData.append);
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
			}

			public void onActivityTestRequest(ActivityTestRequest ind) {
				super.onActivityTestRequest(ind);

				activityTestInvokeId = ind.getInvokeId();
				dialogStep = 2;
			}

			public void onContinueRequest(ContinueRequest ind) {
				super.onContinueRequest(ind);
			}

			public void onSendChargingInformationRequest(SendChargingInformationRequest ind) {
				super.onSendChargingInformationRequest(ind);

				CAI_GSM0224 aocInitial = ind.getSCIBillingChargingCharacteristics().getAOCBeforeAnswer().getAOCInitial();
				assertEquals((int)aocInitial.getE1(), 1);
				assertEquals((int)aocInitial.getE2(), 2);
				assertEquals((int)aocInitial.getE3(), 3);
				assertNull(aocInitial.getE4());
				assertNull(aocInitial.getE5());
				assertNull(aocInitial.getE6());
				assertNull(aocInitial.getE7());
				assertNull(ind.getSCIBillingChargingCharacteristics().getAOCBeforeAnswer().getAOCSubsequent());
				assertNull(ind.getSCIBillingChargingCharacteristics().getAOCSubsequent());
				assertNull(ind.getSCIBillingChargingCharacteristics().getAOCExtension());
				assertEquals(ind.getPartyToCharge().getSendingSideID(), LegType.leg2);
				assertNull(ind.getExtensions());

				dialogStep = 1;
			}
			
			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;

				try {
					switch (dialogStep) {
					case 1: // after ConnectRequest
						OAnswerSpecificInfo oAnswerSpecificInfo = this.capParameterFactory.createOAnswerSpecificInfo(null, false, false, null, null, null);
						ReceivingSideID legID = this.capParameterFactory.createReceivingSideID(LegType.leg2);
						MiscCallInfo miscCallInfo = this.inapParameterFactory.createMiscCallInfo(MiscCallInfoMessageType.notification, null);
						EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capParameterFactory.createEventSpecificInformationBCSM(oAnswerSpecificInfo);
						dlg.addEventReportBCSMRequest(EventTypeBCSM.oAnswer, eventSpecificInformationBCSM, legID, miscCallInfo, null);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.EventReportBCSMRequest, null, sequence++));
						dlg.send();

						ReceivingSideID partyToCharge = this.capParameterFactory.createReceivingSideID(LegType.leg1);
						TimeInformation timeInformation = this.capParameterFactory.createTimeInformation(2000);
						TimeDurationChargingResult timeDurationChargingResult = this.capParameterFactory.createTimeDurationChargingResult(partyToCharge,
								timeInformation, true, false, null, null);
						dlg.addApplyChargingReportRequest(timeDurationChargingResult);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ApplyChargingReportRequest, null, sequence++));
						dlg.send();

						dialogStep = 0;
						
						break;

					case 2: // after ActivityTestRequest
						dlg.addActivityTestResponse(activityTestInvokeId);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivityTestResponse, null, sequence++));
						dlg.send();

						dialogStep = 0;
						
						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			private int dialogStep = 0;
			private boolean firstEventReportBCSMRequest = true;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				dialogStep = 1;
			}

			public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
				super.onEventReportBCSMRequest(ind);

				if (firstEventReportBCSMRequest) {
					firstEventReportBCSMRequest = false;

					assertEquals(ind.getEventTypeBCSM(), EventTypeBCSM.oAnswer);
					assertNotNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo());
					assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getDestinationAddress());
					assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getChargeIndicator());
					assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getExtBasicServiceCode());
					assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getExtBasicServiceCode2());
					assertFalse(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getForwardedCall());
					assertFalse(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getOrCall());
					assertEquals(ind.getLegID().getReceivingSideID(), LegType.leg2);
					assertNull(ind.getExtensions());
				} else {
					try {
						assertEquals(ind.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
						assertNotNull(ind.getEventSpecificInformationBCSM().getODisconnectSpecificInfo());
						CauseIndicators ci = ind.getEventSpecificInformationBCSM().getODisconnectSpecificInfo().getReleaseCause().getCauseIndicators();
						assertEquals(ci.getCauseValue(), CauseIndicators._CV_ALL_CLEAR);
						assertEquals(ci.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
						assertEquals(ci.getLocation(), CauseIndicators._LOCATION_USER);
						assertEquals(ind.getLegID().getReceivingSideID(), LegType.leg1);
						assertEquals(ind.getMiscCallInfo().getMessageType(), MiscCallInfoMessageType.notification);
						assertNull(ind.getMiscCallInfo().getDpAssignment());
						assertNull(ind.getExtensions());
					} catch (CAPException e) {
						this.error("Exception while checking EventReportBCSMRequest - the second message", e);
					}

					dialogStep = 2;
				}
			}

			public void onApplyChargingReportRequest(ApplyChargingReportRequest ind) {
				super.onApplyChargingReportRequest(ind);

				TimeDurationChargingResult tdr = ind.getTimeDurationChargingResult(); 
				assertEquals(tdr.getPartyToCharge().getReceivingSideID(), LegType.leg1);
				assertEquals((int) tdr.getTimeInformation().getTimeIfNoTariffSwitch(), 2000);
				assertNull(tdr.getAChChargingAddress());
				assertFalse(tdr.getCallLegReleasedAtTcpExpiry());
				assertNull(tdr.getExtensions());
				assertTrue(tdr.getLegActive());
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDp

						RequestReportBCSMEventRequest rrc = this.getRequestReportBCSMEventRequest();
						dlg.addRequestReportBCSMEventRequest(rrc.getBCSMEventList(), rrc.getExtensions());
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.RequestReportBCSMEventRequest, null, sequence++));
						dlg.send();

						byte[] freeFormatData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
						SendingSideID partyToCharge1 = this.capParameterFactory.createSendingSideID(LegType.leg1);
						FCIBCCCAMELsequence1 FCIBCCCAMELsequence1 = this.capParameterFactory.createFCIBCCCAMELsequence1(freeFormatData, partyToCharge1,
								AppendFreeFormatData.append);
						dlg.addFurnishChargingInformationRequest(FCIBCCCAMELsequence1);
						dlg.send();
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.FurnishChargingInformationRequest, null, sequence++));
						
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

						dlg.addContinueRequest();
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ContinueRequest, null, sequence++));
						dlg.send();

						CAI_GSM0224 aocInitial = this.capParameterFactory.createCAI_GSM0224(1, 2, 3, null, null, null, null);
						AOCBeforeAnswer aocBeforeAnswer = this.capParameterFactory.createAOCBeforeAnswer(aocInitial, null);
						SCIBillingChargingCharacteristics sciBillingChargingCharacteristics = this.capParameterFactory
								.createSCIBillingChargingCharacteristics(aocBeforeAnswer);
						SendingSideID partyToCharge2 = this.capParameterFactory.createSendingSideID(LegType.leg2);
						dlg.addSendChargingInformationRequest(sciBillingChargingCharacteristics, partyToCharge2, null);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendChargingInformationRequest, null, sequence++));
						dlg.send();
						
						dialogStep = 0;

						break;

					case 2: // after oDisconnect
						dlg.close(false);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}

			public void onDialogTimeout(CAPDialog capDialog) {
				super.onDialogTimeout(capDialog);
				
				capDialog.keepAlive();

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;
				try {
					dlg.addActivityTestRequest(500);
					dlg.send();
				} catch (CAPException e) {
					this.error("Error while trying to send ActivityTestRequest", e);
				}
				this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivityTestRequest, null, sequence++));
			}
		};

		long _DIALOG_TIMEOUT = 2000;
		long _SLEEP_BEFORE_ODISCONNECT = 3000;
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RequestReportBCSMEventRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.FurnishChargingInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ApplyChargingRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ConnectRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SendChargingInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.EventReportBCSMRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ApplyChargingReportRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ActivityTestResponse, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.EventReportBCSMRequest, null, count++, stamp + _SLEEP_BEFORE_ODISCONNECT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp + _SLEEP_BEFORE_ODISCONNECT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _SLEEP_BEFORE_ODISCONNECT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.RequestReportBCSMEventRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.FurnishChargingInformationRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ApplyChargingRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ConnectRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SendChargingInformationRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportBCSMRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ApplyChargingReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ActivityTestRequest, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestResponse, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.EventReportBCSMRequest, null, count++, stamp + _SLEEP_BEFORE_ODISCONNECT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp + _SLEEP_BEFORE_ODISCONNECT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _SLEEP_BEFORE_ODISCONNECT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		// setting dialog timeout little interval to invoke onDialogTimeout on SCF side
		server.capStack.getTCAPStack().setInvokeTimeout(_DIALOG_TIMEOUT - 100);
		server.capStack.getTCAPStack().setDialogIdleTimeout(_DIALOG_TIMEOUT);
		client.suppressInvokeTimeout();
		client.sendInitialDp(CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF);

		// waiting here for DialogTimeOut -> ActivityTest
		Thread.currentThread().sleep(_SLEEP_BEFORE_ODISCONNECT);
		
		// sending an event of call finishing
		client.sendEventReportBCSMRequest_1();

		waitForEnd();
//		Thread.currentThread().sleep(1000000);
	
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Circuit switch call play announcement and disconnect
	 * ACN = capssf-scfGenericAC V3
	 * 
	 * TC-BEGIN + InitialDPRequest
	 *   TC-CONTINUE + RequestReportBCSMEventRequest
	 *   TC-CONTINUE + ConnectToResourceRequest
	 *   TC-CONTINUE + PlayAnnouncementRequest
	 * TC-CONTINUE + SpecializedResourceReportRequest
	 *   TC-CONTINUE + DisconnectForwardConnectionRequest
	 *   TC-END + ReleaseCallRequest
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testPlayAnnouncment() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
				super.onRequestReportBCSMEventRequest(ind);

				this.checkRequestReportBCSMEventRequest(ind);
			}

			public void onConnectToResourceRequest(ConnectToResourceRequest ind) {
				super.onConnectToResourceRequest(ind);

				try {
					CalledPartyNumber cpn = ind.getResourceAddress_IPRoutingAddress().getCalledPartyNumber();
					assertTrue(cpn.getAddress().equals("111222333"));
					assertEquals(cpn.getInternalNetworkNumberIndicator(), CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
					assertEquals(cpn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(cpn.getNumberingPlanIndicator(), CalledPartyNumber._NPI_ISDN);
				} catch (CAPException e) {
					this.error("Error while checking ConnectToResourceRequest", e);
				}
				assertFalse(ind.getResourceAddress_Null());
				assertNull(ind.getCallSegmentID());
				assertNull(ind.getExtensions());
				assertNull(ind.getServiceInteractionIndicatorsTwo());
			}

			private long playAnnounsmentInvokeId;

			public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
				super.onPlayAnnouncementRequest(ind);

				assertEquals(ind.getInformationToSend().getTone().getToneID(), 10);
				assertEquals((int)ind.getInformationToSend().getTone().getDuration(), 100);
				assertTrue(ind.getDisconnectFromIPForbidden());
				assertTrue(ind.getRequestAnnouncementCompleteNotification());
				assertFalse(ind.getRequestAnnouncementStartedNotification());
				assertNull(ind.getCallSegmentID());
				assertNull(ind.getExtensions());
				
				playAnnounsmentInvokeId = ind.getInvokeId();

				dialogStep = 1;
			}

			public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind) {
				super.onDisconnectForwardConnectionRequest(ind);
			}

			public void onReleaseCallRequest(ReleaseCallRequest ind) {
				super.onReleaseCallRequest(ind);

				CauseIndicators ci;
				try {
					ci = ind.getCause().getCauseIndicators();
					assertEquals(ci.getCauseValue(), CauseIndicators._CV_SEND_SPECIAL_TONE);
					assertEquals(ci.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
					assertNull(ci.getDiagnostics());
					assertEquals(ci.getLocation(), CauseIndicators._LOCATION_INTERNATIONAL_NETWORK);
				} catch (CAPException e) {
					this.error("Error while checking ReleaseCallRequest", e);
				}
			}

			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;

				try {
					switch (dialogStep) {
					case 1: // after PlayAnnouncementRequest
						dlg.addSpecializedResourceReportRequest_CapV23(playAnnounsmentInvokeId);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, sequence++));
						dlg.send();

						dialogStep = 0;
						
						break;
					}
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

			public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
				super.onSpecializedResourceReportRequest(ind);

				assertFalse(ind.getFirstAnnouncementStarted());
				assertFalse(ind.getAllAnnouncementsComplete());

				assertEquals((long) ind.getLinkedId(), playAnnounsmentInvokeId);
				assertEquals((long) ind.getLinkedInvoke().getOperationCode().getLocalOperationCode(), CAPOperationCode.playAnnouncement);

				dialogStep = 2;
			}

			private long playAnnounsmentInvokeId;

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDp
						RequestReportBCSMEventRequest rrc = this.getRequestReportBCSMEventRequest();
						dlg.addRequestReportBCSMEventRequest(rrc.getBCSMEventList(), rrc.getExtensions());
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.RequestReportBCSMEventRequest, null, sequence++));
						dlg.send();

						CalledPartyNumber calledPartyNumber = this.isupParameterFactory.createCalledPartyNumber();
						calledPartyNumber.setAddress("111222333");
						calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
						calledPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
						calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
						CalledPartyNumberCap resourceAddress_IPRoutingAddress = this.capParameterFactory.createCalledPartyNumberCap(calledPartyNumber);
						dlg.addConnectToResourceRequest(resourceAddress_IPRoutingAddress, false, null, null, null);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ConnectToResourceRequest, null, sequence++));
						dlg.send();

						Tone tone = this.capParameterFactory.createTone(10, 100);
						InformationToSend informationToSend = this.capParameterFactory.createInformationToSend(tone);

						playAnnounsmentInvokeId = dlg.addPlayAnnouncementRequest(informationToSend, true, true, null, null, invokeTimeoutSuppressed);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.PlayAnnouncementRequest, null, sequence++));
						dlg.send();

						dialogStep = 0;

						break;

					case 2: // after SpecializedResourceReportRequest
						dlg.addDisconnectForwardConnectionRequest();
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.DisconnectForwardConnectionRequest, null, sequence++));
						dlg.send();

						CauseIndicators causeIndicators = this.isupParameterFactory.createCauseIndicators();
						causeIndicators.setCauseValue(CauseIndicators._CV_SEND_SPECIAL_TONE);
						causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
						causeIndicators.setDiagnostics(null);
						causeIndicators.setLocation(CauseIndicators._LOCATION_INTERNATIONAL_NETWORK);
						CauseCap cause = this.capParameterFactory.createCauseCap(causeIndicators);
						dlg.addReleaseCallRequest(cause);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReleaseCallRequest, null, sequence++));
						dlg.close(false);

						dialogStep = 0;

						break;
					}
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

		te = TestEvent.createReceivedEvent(EventType.RequestReportBCSMEventRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ConnectToResourceRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PlayAnnouncementRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DisconnectForwardConnectionRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ReleaseCallRequest, null, count++, stamp);
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

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.RequestReportBCSMEventRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ConnectToResourceRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PlayAnnouncementRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DisconnectForwardConnectionRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ReleaseCallRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);

		waitForEnd();
	
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Assist SSF dialog (V4)
	 * ACN = capssf-scfAssistHandoffAC V4
	 * 
	 * TC-BEGIN + AssistRequestInstructionsRequest
	 *   TC-CONTINUE + ResetTimerRequest
	 *   TC-CONTINUE + PromptAndCollectUserInformationRequest
	 * TC-CONTINUE + SpecializedResourceReportRequest
	 * TC-CONTINUE + PromptAndCollectUserInformationResponse
	 *   TC-END + CancelRequest
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testAssistSsf() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			private int dialogStep;
			private long promptAndCollectUserInformationInvokeId;

			public void onResetTimerRequest(ResetTimerRequest ind) {
				super.onResetTimerRequest(ind);

				assertEquals(ind.getTimerID(), TimerID.tssf);
				assertEquals(ind.getTimerValue(), 1001);
				assertNull(ind.getCallSegmentID());
				assertNull(ind.getExtensions());
			}

			public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
				super.onPromptAndCollectUserInformationRequest(ind);

				promptAndCollectUserInformationInvokeId = ind.getInvokeId();

				CollectedDigits cd = ind.getCollectedInfo().getCollectedDigits();
				assertEquals((int) cd.getMinimumNbOfDigits(), 1);
				assertEquals((int) cd.getMaximumNbOfDigits(), 11);
				assertNull(cd.getCancelDigit());
				assertNull(cd.getEndOfReplyDigit());
				assertNull(cd.getFirstDigitTimeOut());
				assertNull(cd.getStartDigit());
				assertTrue(ind.getDisconnectFromIPForbidden());
				assertNull(ind.getInformationToSend());
				assertNull(ind.getExtensions());
				assertNull(ind.getCallSegmentID());
				assertNull(ind.getRequestAnnouncementStartedNotification());

				dialogStep = 1;
			}

			private boolean cancelRequestFirst = true;
			
			public void onCancelRequest(CancelRequest ind) {
				super.onCancelRequest(ind);

				if (cancelRequestFirst) {
					cancelRequestFirst = false;
					assertTrue(ind.getAllRequests());
					assertNull(ind.getInvokeID());
					assertNull(ind.getCallSegmentToCancel());
				} else {
					assertFalse(ind.getAllRequests());
					assertEquals((int) ind.getInvokeID(), 10);
					assertNull(ind.getCallSegmentToCancel());
				}
			}

			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;

				try {
					switch (dialogStep) {
					case 1: // after PromptAndCollectUserInformationRequest
						dlg.addSpecializedResourceReportRequest_CapV4(promptAndCollectUserInformationInvokeId, false, true);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, sequence++));
						dlg.send();

						GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
						genericNumber.setAddress("444422220000");
						genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
						genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_SUBSCRIBER_NUMBER);
						genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_DATA);
						genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLING_PARTY_NUMBER);
						genericNumber.setScreeningIndicator(GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
						Digits digitsResponse = this.capParameterFactory.createDigits(genericNumber);
						dlg.addPromptAndCollectUserInformationResponse_DigitsResponse(promptAndCollectUserInformationInvokeId, digitsResponse);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationResponse, null, sequence++));
						dlg.send();
						
						dialogStep = 0;
						
						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			private int dialogStep = 0;
			private long PromptAndCollectUserInformationRequestInvokeId;

			public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind) {
				super.onAssistRequestInstructionsRequest(ind);

				try {
//					assertNull(ind.getCorrelationID().getGenericDigits());
					GenericNumber gn = ind.getCorrelationID().getGenericNumber();
					assertTrue(gn.getAddress().equals("333111222"));
					assertEquals(gn.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
					assertEquals(gn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(gn.getNumberingPlanIndicator(), GenericNumber._NPI_ISDN);
					assertEquals(gn.getNumberQualifierIndicator(), GenericNumber._NQIA_CALLED_NUMBER);
					assertEquals(gn.getScreeningIndicator(), GenericNumber._SI_NETWORK_PROVIDED);
					
					IPSSPCapabilities ipc = ind.getIPSSPCapabilities();
					assertTrue(ipc.getIPRoutingAddressSupported());
					assertFalse(ipc.getVoiceBackSupported());
					assertTrue(ipc.getVoiceInformationSupportedViaSpeechRecognition());
					assertFalse(ipc.getVoiceInformationSupportedViaVoiceRecognition());
					assertFalse(ipc.getGenerationOfVoiceAnnouncementsFromTextSupported());
					assertNull(ipc.getExtraData());

					assertNull(ind.getExtensions());
				} catch (CAPException e) {
					this.error("Error while checking AssistRequestInstructionsRequest", e);
				}

				dialogStep = 1;
			}

			public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind) {
				super.onPromptAndCollectUserInformationResponse(ind);

				try {
					GenericNumber gn = ind.getDigitsResponse().getGenericNumber();
					assertTrue(gn.getAddress().equals("444422220000"));
					assertEquals(gn.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
					assertEquals(gn.getNatureOfAddressIndicator(), NAINumber._NAI_SUBSCRIBER_NUMBER);
					assertEquals(gn.getNumberingPlanIndicator(), GenericNumber._NPI_DATA);
					assertEquals(gn.getNumberQualifierIndicator(), GenericNumber._NQIA_CALLING_PARTY_NUMBER);
					assertEquals(gn.getScreeningIndicator(), GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
				} catch (CAPException e) {
					this.error("Error while checking PromptAndCollectUserInformationResponse", e);
				}

				dialogStep = 2;
			}

			public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
				super.onSpecializedResourceReportRequest(ind);

				assertFalse(ind.getAllAnnouncementsComplete());
				assertTrue(ind.getFirstAnnouncementStarted());
				
				assertEquals((long) ind.getLinkedId(), PromptAndCollectUserInformationRequestInvokeId);
				assertEquals((long) ind.getLinkedInvoke().getOperationCode().getLocalOperationCode(), CAPOperationCode.promptAndCollectUserInformation);
			}

			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;

				try {
					switch (dialogStep) {
					case 1: // after AssistRequestInstructionsRequest
						dlg.addResetTimerRequest(TimerID.tssf, 1001, null, null);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ResetTimerRequest, null, sequence++));
						dlg.send();

						CollectedDigits collectedDigits = this.capParameterFactory.createCollectedDigits(1, 11, null, null, null, null, null, null, null, null,
								null);
						CollectedInfo collectedInfo = this.capParameterFactory.createCollectedInfo(collectedDigits);
						PromptAndCollectUserInformationRequestInvokeId = dlg.addPromptAndCollectUserInformationRequest(collectedInfo, true, null, null, null, null);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationRequest, null, sequence++));
						dlg.send();

						dialogStep = 0;

						break;

					case 2: // after SpecializedResourceReportRequest
						dlg.addCancelRequest_AllRequests();
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.CancelRequest, null, sequence++));
						dlg.send();

						dlg.addCancelRequest_InvokeId(10);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.CancelRequest, null, sequence++));
						dlg.close(false);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.AssistRequestInstructionsRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ResetTimerRequest, null, count++, stamp);
		clientExpectedEvents.add(te);
		
		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, stamp);
		clientExpectedEvents.add(te);
		
		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationResponse, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelRequest, null, count++, stamp);
		clientExpectedEvents.add(te);
		
		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelRequest, null, count++, stamp);
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

		te = TestEvent.createReceivedEvent(EventType.AssistRequestInstructionsRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ResetTimerRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationResponse, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendAssistRequestInstructionsRequest();

		waitForEnd();
	
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * ScfSsf test (V4)
	 * ACN = capscf-ssfGenericAC V4
	 * 
	 * TC-BEGIN + establishTemporaryConnection + callInformationRequest
	 *   TC-END + callInformationReport
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testScfSsf() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			private int dialogStep;

			public void onCallInformationReportRequest(CallInformationReportRequest ind) {
				super.onCallInformationReportRequest(ind);

				ArrayList<RequestedInformation> al = ind.getRequestedInformationList();
				assertEquals(al.size(), 1);
				DateAndTime dt = al.get(0).getCallStopTimeValue();
				assertEquals(dt.getYear(), 2012);
				assertEquals(dt.getMonth(), 11);
				assertEquals(dt.getDay(), 30);
				assertEquals(dt.getHour(), 23);
				assertEquals(dt.getMinute(), 50);
				assertEquals(dt.getSecond(), 40);
				assertNull(ind.getExtensions());
				assertNull(ind.getLegID());
			}

			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			private int dialogStep = 0;

			public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind) {
				super.onEstablishTemporaryConnectionRequest(ind);

				try {
					GenericNumber gn = ind.getAssistingSSPIPRoutingAddress().getGenericNumber();
					assertTrue(gn.getAddress().equals("333111222"));
					assertEquals(gn.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
					assertEquals(gn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(gn.getNumberingPlanIndicator(), GenericNumber._NPI_ISDN);
					assertEquals(gn.getNumberQualifierIndicator(), GenericNumber._NQIA_CALLED_NUMBER);
					assertEquals(gn.getScreeningIndicator(), GenericNumber._SI_NETWORK_PROVIDED);

					assertNull(ind.getCallingPartyNumber());
					assertNull(ind.getCallSegmentID());
					assertNull(ind.getCarrier());
					assertNull(ind.getChargeNumber());
					assertNull(ind.getCorrelationID());
					assertNull(ind.getExtensions());
					assertNull(ind.getNAOliInfo());
					assertNull(ind.getOriginalCalledPartyID());
					assertNull(ind.getScfID());
					assertNull(ind.getServiceInteractionIndicatorsTwo());
				} catch (CAPException e) {
					this.error("Error while trying checking EstablishTemporaryConnectionRequest", e);
				}
			}

			public void onCallInformationRequestRequest(CallInformationRequestRequest ind) {
				super.onCallInformationRequestRequest(ind);

				ArrayList<RequestedInformationType> al = ind.getRequestedInformationTypeList();
				assertEquals(al.size(), 1);
				assertEquals(al.get(0), RequestedInformationType.callStopTime);
				assertNull(ind.getExtensions());
				assertNull(ind.getLegID());

				dialogStep = 1;
			}

			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;

				try {
					switch (dialogStep) {
					case 1: // after CallInformationRequestRequest
						ArrayList<RequestedInformation> requestedInformationList = new ArrayList<RequestedInformation>();
						DateAndTime dt = this.capParameterFactory.createDateAndTime(2012, 11, 30, 23, 50, 40);
						RequestedInformation ri = this.capParameterFactory.createRequestedInformation_CallStopTime(dt);
						requestedInformationList.add(ri);
						dlg.addCallInformationReportRequest(requestedInformationList, null, null);
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.CallInformationReportRequest, null, sequence++));
						dlg.close(false);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.EstablishTemporaryConnectionRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CallInformationRequestRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CallInformationReportRequest, null, count++, stamp);
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

		te = TestEvent.createReceivedEvent(EventType.EstablishTemporaryConnectionRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CallInformationRequestRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CallInformationReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendEstablishTemporaryConnectionRequest_CallInformationRequest();

		waitForEnd();

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Abnormal test 
	 * ACN = CAP-v2-assist-gsmSSF-to-gsmSCF
	 * 
	 * TC-BEGIN + ActivityTestRequest
	 *   TC-CONTINUE <no ActivityTestResponse>
	 * resetInvokeTimer() before InvokeTimeout
	 * InvokeTimeout
	 * TC-CONTINUE + CancelRequest + cancelInvocation() -> CancelRequest will not go to Server
	 * TC-CONTINUE + ResetTimerRequest
	 *   reject ResetTimerRequest
	 * DialogUserAbort: AbortReason=not_allowed_procedures
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testAbnormal() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			private int dialogStep;
			private long resetTimerRequestInvokeId;

			public void onInvokeTimeout(CAPDialog capDialog, Long invokeId) {
				super.onInvokeTimeout(capDialog, invokeId);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;

				try {
					long invId = dlg.addCancelRequest_AllRequests();
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.CancelRequest, null, sequence++));
					dlg.cancelInvocation(invId);
					dlg.send();

					resetTimerRequestInvokeId = dlg.addResetTimerRequest(TimerID.tssf, 2222, null, null);
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.ResetTimerRequest, null, sequence++));
					dlg.send();
				} catch (CAPException e) {
					this.error("Error while checking CancelRequest or ResetTimerRequest", e);
				}
			}

			public void onRejectComponent(CAPDialog capDialog, Long invokeId, Problem problem) {
				super.onRejectComponent(capDialog, invokeId, problem);

				assertEquals(resetTimerRequestInvokeId, (long) invokeId);
				assertEquals(problem.getInvokeProblemType(), InvokeProblemType.MistypedParameter);

				dialogStep = 1;
			}

			public void onDialogRelease(CAPDialog capDialog) {
				super.onDialogRelease(capDialog);
			}

			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;
				try {
					switch (dialogStep) {
					case 1: // after RejectComponent
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.DialogUserAbort, null, sequence++));
						dlg.abort(CAPUserAbortReason.missing_reference);

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			private int dialogStep = 0;

			public void onActivityTestRequest(ActivityTestRequest ind) {
				super.onActivityTestRequest(ind);

				dialogStep = 1;
			}

			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);

				assertEquals(generalReason, CAPGeneralAbortReason.UserSpecific);
				assertEquals(userReason, CAPUserAbortReason.missing_reference);
			}

			public void onDialogRelease(CAPDialog capDialog) {
				super.onDialogRelease(capDialog);
			}
			
			long resetTimerRequestInvokeId;

			public void onResetTimerRequest(ResetTimerRequest ind) {
				super.onResetTimerRequest(ind);

				resetTimerRequestInvokeId = ind.getInvokeId();

				dialogStep = 2;
			}

			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;

				try {
					switch (dialogStep) {
					case 1: // after ActivityTestRequest
						dlg.send();

						dialogStep = 0;

						break;

					case 2: // after ResetTimerRequest
						Problem problem = this.capParameterFactory.createProblemInvoke(InvokeProblemType.MistypedParameter);
						try {
							dlg.sendRejectComponent(resetTimerRequestInvokeId, problem);
							this.observerdEvents.add(TestEvent.createSentEvent(EventType.RejectComponent, null, sequence++));
						} catch (CAPException e) {
							this.error("Error while sending reject", e);
						}

						dlg.send();

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		int _ACTIVITY_TEST_INVOKE_TIMEOUT = 1000;
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ActivityTestRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ResetTimerRequest, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DialogUserAbort, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);


		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ResetTimerRequest, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.RejectComponent, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _ACTIVITY_TEST_INVOKE_TIMEOUT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendActivityTestRequest(_ACTIVITY_TEST_INVOKE_TIMEOUT);

		Thread.sleep(_ACTIVITY_TEST_INVOKE_TIMEOUT);
		waitForEnd();

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * DialogTimeout test
	 * ACN=CAP-v3-gsmSSF-to-gsmSCF
	 * 
	 * TC-BEGIN + InitialDPRequest
	 *   TC-CONTINUE empty
	 * (no answer - DialogTimeout at both sides)
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testDialogTimeout() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			public void onDialogTimeout(CAPDialog capDialog) {
				super.onDialogTimeout(capDialog);
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

			private int dialogStep;
			
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
					case 1: // after InitialDpRequest
						dlg.send();

						dialogStep = 0;

						break;
					}
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}

			public void onDialogTimeout(CAPDialog capDialog) {
				super.onDialogTimeout(capDialog);
			}
		};

		long _DIALOG_TIMEOUT = 2000;
		long _SLEEP_BEFORE_ODISCONNECT = 3000;
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, count++, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _DIALOG_TIMEOUT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, count++, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _DIALOG_TIMEOUT + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		// setting dialog timeout little interval to invoke onDialogTimeout on SCF side
		server.capStack.getTCAPStack().setInvokeTimeout(_DIALOG_TIMEOUT - 100);
		server.capStack.getTCAPStack().setDialogIdleTimeout(_DIALOG_TIMEOUT);
		server.suppressInvokeTimeout();
		client.capStack.getTCAPStack().setInvokeTimeout(_DIALOG_TIMEOUT - 100);
		client.capStack.getTCAPStack().setDialogIdleTimeout(_DIALOG_TIMEOUT);
		client.suppressInvokeTimeout();
		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);

		// waiting here for DialogTimeOut -> ActivityTest
		Thread.currentThread().sleep(_SLEEP_BEFORE_ODISCONNECT);

		waitForEnd();
//		Thread.currentThread().sleep(1000000);
	
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * ACNNotSuported test
	 * ACN=CAP-v3-gsmSSF-to-gsmSCF
	 * 
	 * TC-BEGIN + InitialDPRequest (Server service is down -> ACN not supported)
	 *   TC-ABORT + ACNNotSuported
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testACNNotSuported() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);

				assertEquals(generalReason, CAPGeneralAbortReason.ACNNotSupported);
				assertNull(userReason);
				assertEquals(capDialog.getTCAPMessageType(), MessageType.Abort);
			}
			
			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;
			}

			public void onDialogTimeout(CAPDialog capDialog) {
				super.onDialogTimeout(capDialog);
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		server.capProvider.getCAPServiceCircuitSwitchedCall().deactivate();
		
		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);

		waitForEnd();
	
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Bad data sending at TC-BEGIN test - no ACN
	 * 
	 * 
	 * TC-BEGIN + no ACN
	 *   TC-ABORT + BadReceivedData
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testBadDataSendingNoAcn() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);

				assertEquals(generalReason, CAPGeneralAbortReason.BadReceivedData);
				assertNull(userReason);
				assertEquals(capDialog.getTCAPMessageType(), MessageType.Abort);
			}
			
			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;
			}

			public void onDialogTimeout(CAPDialog capDialog) {
				super.onDialogTimeout(capDialog);
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		
		client.sendBadDataNoAcn();

		waitForEnd();
	
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-CONTINUE from Server after dialogRelease at Client 
	 * 
	 * 
	 * TC-BEGIN + InitialDP
	 * relaseDialog
	 *   TC-CONTINUE
	 * ProviderAbort
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testProviderAbort() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

			}

			public void onDialogRelease(CAPDialog capDialog) {
				super.onDialogRelease(capDialog);
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);
			}

			public void onDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
				super.onDialogProviderAbort(capDialog, abortCause);

				assertEquals(abortCause, PAbortCauseType.UnrecognizedTxID);
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall)capDialog;
			}
		};

		long _DIALOG_RELEASE_DELAY = 100;
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++, stamp + _DIALOG_RELEASE_DELAY);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _DIALOG_RELEASE_DELAY + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendInitialDp(CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF);
		client.releaseDialog();
		Thread.sleep(_DIALOG_RELEASE_DELAY);
		server.sendAccept();

		waitForEnd();
		
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * referensedNumber 
	 * 
	 * 
	 * TC-BEGIN + referensedNumber
	 *   TC-END + referensedNumber
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testReferensedNumber() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			private int dialogSteps = 0;

			public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogAccept(capDialog, capGprsReferenceNumber);

				assertEquals((int)capGprsReferenceNumber.getDestinationReference(), 10005);
				assertEquals((int)capGprsReferenceNumber.getOriginationReference(), 10006);
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogGprs dlg = (CAPDialogGprs)capDialog;
			}

			public void onDialogRelease(CAPDialog capDialog) {
				super.onDialogRelease(capDialog);
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			private int dialogSteps = 0;

			public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogRequest(capDialog, capGprsReferenceNumber);

				assertEquals((int)capGprsReferenceNumber.getDestinationReference(), 1005);
				assertEquals((int)capGprsReferenceNumber.getOriginationReference(), 1006);

				dialogSteps = 1;
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogGprs dlg = (CAPDialogGprs)capDialog;

				if (dialogSteps == 1) {
					dialogSteps = 0;

					try {
						CAPGprsReferenceNumber capGprsReferenceNumber = this.capParameterFactory.createCAPGprsReferenceNumber(10005, 10006);
						dlg.setGprsReferenceNumber(capGprsReferenceNumber);
						dlg.close(false);
					} catch (CAPException e) {
						this.error("Error while trying to send/close() Dialog", e);
					}
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendReferensedNumber();

		waitForEnd();
		
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * broken referensedNumber 
	 * 
	 * 
	 * TC-BEGIN + broken referensedNumber
	 *   TC-ABORT
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testReferensedNumber_BadVal() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			private int dialogSteps = 0;

			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);
				
				assertEquals(generalReason, CAPGeneralAbortReason.BadReceivedData);
				assertNull(userReason);
				assertEquals(capDialog.getTCAPMessageType(), MessageType.Abort);
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogGprs dlg = (CAPDialogGprs)capDialog;
			}

			public void onDialogRelease(CAPDialog capDialog) {
				super.onDialogRelease(capDialog);
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogGprs dlg = (CAPDialogGprs)capDialog;
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		((CAPProviderImplWrapper) server.capProvider).setTestMode(1);
		client.sendReferensedNumber();

		waitForEnd();
		
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * 
	 * TC-BEGIN + broken referensedNumber
	 *   TC-ABORT
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testMessageUserDataLength() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			}

			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);
				assertEquals(capDialog.getTCAPMessageType(), MessageType.Abort);
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.testMessageUserDataLength();

//		waitForEnd();
//		
//		client.compareEvents(clientExpectedEvents);
//		server.compareEvents(serverExpectedEvents);

	}

	/** 
	 * Some not real test for testing:
	 * - sendDelayed() / closeDelayed()
	 * - getTCAPMessageType()
	 * - saving origReferense, destReference, extContainer in MAPDialog
	 * TC-BEGIN + referensedNumber + initialDPRequest + initialDPRequest
	 *   TC-CONTINUE + sendDelayed(ContinueRequest) + sendDelayed(ContinueRequest)
	 * TC-END + closeDelayed(CancelRequest) + sendDelayed(CancelRequest)
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testDelayedSendClose() throws Exception {
		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			int dialogStep = 0;

			public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogAccept(capDialog, capGprsReferenceNumber);

				assertEquals((int)capGprsReferenceNumber.getDestinationReference(), 201);
				assertEquals((int)capGprsReferenceNumber.getOriginationReference(), 202);

				assertEquals(capDialog.getTCAPMessageType(), MessageType.Continue);
			}

			public void onContinueRequest(ContinueRequest ind) {
				super.onContinueRequest(ind);

				CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();
				assertEquals(d.getTCAPMessageType(), MessageType.Continue);

				assertEquals((int)d.getReceivedGprsReferenceNumber().getDestinationReference(), 201);
				assertEquals((int)d.getReceivedGprsReferenceNumber().getOriginationReference(), 202);

				try {
					d.addCancelRequest_AllRequests();
					if (dialogStep == 0) {
						d.closeDelayed(false);
					} else {
						d.sendDelayed();
					}
					dialogStep++;
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.CancelRequest, null, sequence++));
				} catch (CAPException e) {
					this.error("Error while adding CancelRequest/sending", e);
					fail("Error while adding CancelRequest/sending");
				}
			};
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			int dialogStep = 0;

			public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogRequest(capDialog, capGprsReferenceNumber);

				assertEquals((int)capGprsReferenceNumber.getDestinationReference(), 101);
				assertEquals((int)capGprsReferenceNumber.getOriginationReference(), 102);

				CAPGprsReferenceNumber grn = this.capParameterFactory.createCAPGprsReferenceNumber(201, 202);
				capDialog.setGprsReferenceNumber(grn);
			}

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();

				assertTrue(Client.checkTestInitialDp(ind));

				assertEquals((int)d.getReceivedGprsReferenceNumber().getDestinationReference(), 101);
				assertEquals((int)d.getReceivedGprsReferenceNumber().getOriginationReference(), 102);

				if (dialogStep < 2) {
					assertEquals(d.getTCAPMessageType(), MessageType.Begin);

					try {
						d.addContinueRequest();
						d.sendDelayed();
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ContinueRequest, null, sequence++));
					} catch (CAPException e) {
						this.error("Error while adding ContinueRequest/sending", e);
						fail("Error while adding ContinueRequest/sending");
					}
				} else {
					assertEquals(d.getTCAPMessageType(), MessageType.End);
				}

				dialogStep++;
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueRequest, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ContinueRequest, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CancelRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);
		
		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CancelRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);
		
		client.sendInitialDp2();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/** 
	 * Some not real test for testing:
	 * - closeDelayed(true)
	 * - getTCAPMessageType()
	 * TC-BEGIN + initialDPRequest + initialDPRequest
	 *   TC-END + Prearranged + [ContinueRequest + ContinueRequest]
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testDelayedClosePrearranged() throws Exception {
		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			int dialogStep = 0;

			public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogAccept(capDialog, capGprsReferenceNumber);

				assertNull(capGprsReferenceNumber);

				assertEquals(capDialog.getTCAPMessageType(), MessageType.End);
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			int dialogStep = 0;

			public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogRequest(capDialog, capGprsReferenceNumber);

				assertNull(capGprsReferenceNumber);

				assertEquals(capDialog.getTCAPMessageType(), MessageType.Begin);
			}

			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();

				assertTrue(Client.checkTestInitialDp(ind));

				assertNull(d.getReceivedGprsReferenceNumber());

				assertEquals(d.getTCAPMessageType(), MessageType.Begin);

				try {
					d.addContinueRequest();
					if (dialogStep == 0)
						d.sendDelayed();
					else
						d.closeDelayed(true);
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.ContinueRequest, null, sequence++));
				} catch (CAPException e) {
					this.error("Error while adding ContinueRequest/sending", e);
					fail("Error while adding ContinueRequest/sending");
					}

				dialogStep++;
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);
		
		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ContinueRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);
		
		client.sendInitialDp3();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/** 
	 * Testing for some special error cases:
	 *   - linkedId to an operation that does not support linked operations
	 *   - linkedId to a missed operation
	 * 
	 * TC-BEGIN + initialDPRequest + playAnnouncement
	 *   TC-CONTINUE + SpecializedResourceReportRequest to initialDPRequest (unsupported) + 
	 *     SpecializedResourceReportRequest to a missed operation (linkedId==bad==50) +
	 *     SpecializedResourceReportRequest to a correct operation
	 * TC-END
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testBadInvokeLinkedId() throws Exception {
		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			int dialogStep = 0;

			@Override
			public void onDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
				super.onDialogNotice(capDialog, noticeProblemDiagnostic);

				dialogStep++;

				switch (dialogStep) {
				case 1:
					assertEquals(noticeProblemDiagnostic, CAPNoticeProblemDiagnostic.LinkedResponseUnexpected);
					break;
				case 2:
					assertEquals(noticeProblemDiagnostic, CAPNoticeProblemDiagnostic.UnknownLinkedIdReceived);
					break;
				}
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					dlg.close(false);
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			int dialogStep = 0;
			long invokeId1;
			long invokeId2;

			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				invokeId1 = ind.getInvokeId();
			}

			public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
				super.onPlayAnnouncementRequest(ind);

				invokeId2 = ind.getInvokeId();
			}

			public void onRejectComponent(CAPDialog capDialog, Long invokeId, Problem problem) {
				super.onRejectComponent(capDialog, invokeId, problem);

				dialogStep++;

				switch (dialogStep) {
				case 1:
					assertEquals(problem.getType(), ProblemType.Invoke);
					assertEquals(problem.getInvokeProblemType(), InvokeProblemType.LinkedResponseUnexpected);
					break;
				case 2:
					assertEquals(problem.getType(), ProblemType.Invoke);
					assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnrechognizedLinkedID);
					break;
				}
			}

			@Override
			public void onDialogDelimiter(CAPDialog capDialog) {
				super.onDialogDelimiter(capDialog);

				CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

				try {
					dlg.addSpecializedResourceReportRequest_CapV23(invokeId1);
					dlg.addSpecializedResourceReportRequest_CapV23((long) 50);
					dlg.addSpecializedResourceReportRequest_CapV23(invokeId2);

					this.observerdEvents.add(TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, sequence++));
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, sequence++));
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, sequence++));

					dlg.send();
				} catch (CAPException e) {
					this.error("Error while trying to send/close() Dialog", e);
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InitialDpRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.PlayAnnouncementRequest, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogNotice, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogNotice, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SpecializedResourceReportRequest, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);
		
		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.PlayAnnouncementRequest, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SpecializedResourceReportRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);
		
		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);
		
		client.sendInitialDp_playAnnouncement();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

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

