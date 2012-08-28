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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.mobicents.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.TripletList;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIuA;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIuB;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsSubmitTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.TripletListTest;
import org.mobicents.protocols.ss7.map.service.mobility.imei.CheckImeiRequestImpl;
import org.mobicents.protocols.ss7.map.service.sms.SmsSignalInfoImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSResponseImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.testng.Assert;
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
public class MAPFunctionalTest extends SccpHarness {

	private static Logger logger = Logger.getLogger(MAPFunctionalTest.class);
	protected static final String USSD_STRING = "*133#";
	protected static final String USSD_MENU = "Select 1)Wallpaper 2)Ringtone 3)Games";
	protected static final String USSD_RESPONSE = "1";
	protected static final String USSD_FINAL_RESPONSE = "Thank you";

	private static final int _TCAP_DIALOG_RELEASE_TIMEOUT = 0;
	private static final int _WAIT_TIMEOUT = _TCAP_DIALOG_RELEASE_TIMEOUT + 500;

	private MAPStackImpl stack1;
	private MAPStackImpl stack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;

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

		this.sccpStack1Name = "MAPFunctionalTestSccpStack1";
		this.sccpStack2Name = "MAPFunctionalTestSccpStack2";

		super.setUp();

		// this.setupLog4j();

		// create some fake addresses.

		peer1Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), null, getSSN());
		peer2Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), null, getSSN());

		this.stack1 = new MAPStackImplWrapper(this.sccpProvider1, getSSN());
		this.stack2 = new MAPStackImplWrapper(this.sccpProvider2, getSSN());

		this.stack1.start();
		this.stack2.start();
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
	 * Below are test for MAP Dialog normal and abnormal actions
	 */

	/**
	 * Complex TC Dialog
	 * 
	 * TC-BEGIN + ExtensionContainer + addProcessUnstructuredSSRequest
	 * TC-CONTINUE + ExtensionContainer + addUnstructuredSSRequest 
	 * TC-CONTINUE + addUnstructuredSSResponse 
	 * TC-END + addProcessUnstructuredSSResponse
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testComplexTCWithDialog() throws Exception {
		
		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
				super.onUnstructuredSSRequest(unstrReqInd);

				String ussdString = unstrReqInd.getUSSDString().getString();
				AddressString msisdn = unstrReqInd.getMSISDNAddressString();
				this.debug("Received UnstructuredSSRequestIndication " + ussdString);

				assertEquals(MAPFunctionalTest.USSD_MENU, ussdString);

				MAPDialogSupplementary mapDialog = unstrReqInd.getMAPDialog();
				Long invokeId = unstrReqInd.getInvokeId();

				USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_RESPONSE);
				try {
					mapDialog.addUnstructuredSSResponse(invokeId, (byte) 0x0F, ussdStringObj);
				} catch (MAPException e) {
					this.error("Erro while trying to send UnstructuredSSResponse", e);
					fail("Erro while trying to add UnstructuredSSResponse");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				this.dialogStep++;
				try {
					if (this.dialogStep == 1) {
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.UnstructuredSSResponseIndication, null, sequence++));
						mapDialog.send();
					}
				} catch (MAPException e) {
					this.error("Error while trying to send Response", e);
					fail("Erro while trying to send UnstructuredSSResponse");
				}
			}

			@Override
			public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
				super.onDialogAccept(mapDialog, extensionContainer);
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			private int dialogStep;
			private long processUnstructuredSSRequestInvokeId = 0l;

			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				String ussdString = procUnstrReqInd.getUSSDString().getString();
				AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
				this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
				MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
				processUnstructuredSSRequestInvokeId = procUnstrReqInd.getInvokeId();
				this.debug("InvokeId =  " + processUnstructuredSSRequestInvokeId);
				USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_MENU);
				try {
					mapDialog.addUnstructuredSSRequest((byte) 0x0F, ussdStringObj, null, null);
				} catch (MAPException e) {
					this.error("Error while trying to send UnstructuredSSRequest", e);
					fail("Erro while trying to add UnstructuredSSRequest");
				}
			}

			@Override
			public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
				super.onUnstructuredSSResponse(unstrResInd);
				String ussdString = unstrResInd.getUSSDString().getString();
				logger.debug("Received UnstructuredSSResponse " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_RESPONSE, ussdString);
				MAPDialogSupplementary mapDialog = unstrResInd.getMAPDialog();
				USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_FINAL_RESPONSE);
				try {
					mapDialog.addProcessUnstructuredSSResponse(processUnstructuredSSRequestInvokeId, (byte) 0x0F, ussdStringObj);
				} catch (MAPException e) {
					logger.error(e);
					fail("Erro while trying to add ProcessUnstructuredSSResponse");
				}
			}

			@Override
			public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
					MAPExtensionContainer extensionContainer) {
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
				super.onDialogRequest(mapDialog, destReference, origReference, extensionContainer);
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				this.dialogStep++;
				try {
					if (this.dialogStep == 1) {
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null, sequence++));
						mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
						mapDialog.send();
					} else {
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null, sequence++));
						mapDialog.close(false);
					}
				} catch (MAPException e) {
					this.error("Error while trying to send Response", e);
					fail("Erro while trying to send UnstructuredSSRequest or ProcessUnstructuredSSResponse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.UnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.UnstructuredSSResponseIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++, stamp);
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

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.UnstructuredSSResponseIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.actionA();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Ending Dialog in the middle of conversation by "close(true)" - without sending components 
	 * 
	 * TC-BEGIN + ExtensionContainer + addProcessUnstructuredSSRequest
	 * TC-CONTINUE + ExtensionContainer + addUnstructuredSSRequest 
	 * TC-END
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testDialogEndAtTheMiddleConversation() throws Exception {
		
		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
				super.onUnstructuredSSRequest(unstrReqInd);

				String ussdString = unstrReqInd.getUSSDString().getString();
				AddressString msisdn = unstrReqInd.getMSISDNAddressString();
				this.debug("Received UnstructuredSSRequestIndication " + ussdString);

				assertEquals(MAPFunctionalTest.USSD_MENU, ussdString);

				MAPDialogSupplementary mapDialog = unstrReqInd.getMAPDialog();
				Long invokeId = unstrReqInd.getInvokeId();

				USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_RESPONSE);
				try {
					mapDialog.addUnstructuredSSResponse(invokeId, (byte) 0x0F, ussdStringObj);
				} catch (MAPException e) {
					this.error("Erro while trying to send UnstructuredSSResponse", e);
					fail("Erro while trying to add UnstructuredSSResponse");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				this.dialogStep++;
				try {
					if (this.dialogStep == 1) {
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.UnstructuredSSResponseIndication, null, sequence++));
						mapDialog.close(true);
					}
				} catch (MAPException e) {
					this.error("Error while trying to send Response", e);
					fail("Error while trying to send UnstructuredSSResponse");
				}
			}

			@Override
			public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
				super.onDialogAccept(mapDialog, extensionContainer);
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			private int dialogStep;
			private long processUnstructuredSSRequestInvokeId = 0l;

			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				String ussdString = procUnstrReqInd.getUSSDString().getString();
				AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
				this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
				MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
				processUnstructuredSSRequestInvokeId = procUnstrReqInd.getInvokeId();
				this.debug("InvokeId =  " + processUnstructuredSSRequestInvokeId);
				USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_MENU);
				try {
					mapDialog.addUnstructuredSSRequest((byte) 0x0F, ussdStringObj, null, null);
				} catch (MAPException e) {
					this.error("Error while trying to send UnstructuredSSRequest", e);
					fail("Erro while trying to add UnstructuredSSRequest");
				}
			}

			@Override
			public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
				super.onUnstructuredSSResponse(unstrResInd);
				String ussdString = unstrResInd.getUSSDString().getString();
				logger.debug("Received UnstructuredSSResponse " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_RESPONSE, ussdString);
				MAPDialogSupplementary mapDialog = unstrResInd.getMAPDialog();
				USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_FINAL_RESPONSE);
				try {
					mapDialog.addProcessUnstructuredSSResponse(processUnstructuredSSRequestInvokeId, (byte) 0x0F, ussdStringObj);
				} catch (MAPException e) {
					logger.error(e);
					fail("Erro while trying to add ProcessUnstructuredSSResponse");
				}
			}

			@Override
			public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
					MAPExtensionContainer extensionContainer) {
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
				super.onDialogRequest(mapDialog, destReference, origReference, extensionContainer);
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				this.dialogStep++;
				try {
					if (this.dialogStep == 1) {
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null, sequence++));
						mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
						mapDialog.send();
					} else {
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null, sequence++));
						mapDialog.close(false);
					}
				} catch (MAPException e) {
					this.error("Error while trying to send Response", e);
					fail("Erro while trying to send UnstructuredSSRequest or ProcessUnstructuredSSResponse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.UnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.UnstructuredSSResponseIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.actionA();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Server reject a Dialog with InvalidDestinationReference reason
	 * 
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * refuse() -> TC-ABORT + MapRefuseInfo + ExtensionContainer
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testDialogRefuse() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
					ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
				super.onDialogReject(mapDialog, refuseReason, providerError, alternativeApplicationContext, extensionContainer);
				assertEquals(refuseReason, MAPRefuseReason.InvalidDestinationReference);
				assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				String ussdString = procUnstrReqInd.getUSSDString().getString();
				AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
				this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);

			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.DialogUserAbort, null, sequence++));
					mapDialog.refuse(Reason.invalidDestinationReference);
				} catch (MAPException e) {
					this.error("Error while trying to send Refuse", e);
					fail("Error while trying to send Refuse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogReject, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DialogUserAbort, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.actionA();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Server reject a Dialog because of ApplicationContextName does not supported
	 * (Bad ACN is simulated)
	 * 
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-ABORT(Reason=ACN_Not_Supprted) + alternativeApplicationContextName 
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testInvalidApplicationContext() throws Exception {

		((MAPServiceSupplementaryImplWrapper) this.stack2.getMAPProvider().getMAPServiceSupplementary()).setTestMode(1);

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
					ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
				super.onDialogReject(mapDialog, refuseReason, providerError, alternativeApplicationContext, extensionContainer);
				assertEquals(refuseReason, MAPRefuseReason.ApplicationContextNotSupported);
				assertNotNull(alternativeApplicationContext);
				assertTrue(Arrays.equals(alternativeApplicationContext.getOid(), new long[] { 1, 2, 3 }));
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address);

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogReject, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.actionA();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * User-Abort as a response to TC-CONTINUE by a Client
	 * 
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-CONTINUE + addUnstructuredSSRequest 
	 * TC-ABORT(MAP-UserAbortInfo) + ExtensionContainer 
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testDialogUserAbort() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			@Override
			public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
				super.onUnstructuredSSRequest(unstrReqInd);

				String ussdString = unstrReqInd.getUSSDString().getString();
				AddressString msisdn = unstrReqInd.getMSISDNAddressString();
				this.debug("Received UnstructuredSSRequestIndication " + ussdString);

				assertEquals(MAPFunctionalTest.USSD_MENU, ussdString);
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);

				try {
					mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
					MAPUserAbortChoice choice = this.mapParameterFactory.createMAPUserAbortChoice();
					choice.setProcedureCancellationReason(ProcedureCancellationReason.handoverCancellation);
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.DialogUserAbort, null, sequence++));
					mapDialog.abort(choice);
				} catch (MAPException e) {
					this.error("Error while trying to send UserAbort", e);
					fail("Error while trying to send UserAbort");
				}
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				String ussdString = procUnstrReqInd.getUSSDString().getString();
				AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
				this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
				MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
				USSDString ussdStringObj = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_MENU);
				try {
					mapDialog.addUnstructuredSSRequest((byte) 0x0F, ussdStringObj, null, null);
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null, sequence++));
					mapDialog.send();
				} catch (MAPException e) {
					this.error("Error while trying to send UnstructuredSSRequest", e);
					fail("Error while trying to send UnstructuredSSRequest");
				}
			}

			@Override
			public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
				super.onDialogUserAbort(mapDialog, userReason, extensionContainer);
				assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
				assertTrue(userReason.isProcedureCancellationReason());
				assertEquals(ProcedureCancellationReason.handoverCancellation, userReason.getProcedureCancellationReason());
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.UnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DialogUserAbort, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.actionA();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Simulating a ProviderAbort from a Server (InvalidPDU)
	 * 
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-ABORT(MAP-ProviderAbortInfo) 
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testReceivedDialogAbortInfo() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource,
					MAPExtensionContainer extensionContainer) {

				super.onDialogProviderAbort(mapDialog, abortProviderReason, abortSource, extensionContainer);

				this.debug("Received DialogProviderAbort " + abortProviderReason.toString());
				assertEquals(abortProviderReason, MAPAbortProviderReason.InvalidPDU);
				assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address);

		((MAPProviderImplWrapper) this.stack2.getMAPProvider()).setTestMode(1);

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.actionA();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Ericsson-style OpenInfo Dialog
	 * 
	 * TC-BEGIN + Ericsson-style MAP-OpenInfo + addProcessUnstructuredSSRequest
	 * TC-END
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testEricssonDialog() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

			@Override
			public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference, IMSI eriImsi,
					AddressString eriVlrNo) {
				super.onDialogRequestEricsson(mapDialog, destReference, origReference, eriImsi, eriVlrNo);
				assertNotNull(eriImsi);
				assertEquals(eriImsi.getData(), "12345");

				assertNotNull(eriVlrNo);
				assertEquals(eriVlrNo.getAddress(), "556677");

				assertNotNull(destReference);
				assertEquals(destReference.getAddress(), "888777");

				assertNotNull(origReference);
				assertEquals(origReference.getAddress(), "1115550000");
			}

			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				String ussdString = procUnstrReqInd.getUSSDString().getString();
				AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
				this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.DialogClose, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while trying to send empty response for received ProcessUnstructuredSSRequest", e);
					fail("Error while trying to send empty response for received ProcessUnstructuredSSRequest");
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogEricssonRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DialogClose, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.actionEricssonDialog();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}


	/**
	 * Below are test for MAP Component processing
	 */

	/**
	 * Sending ReturnError (MAPErrorMessageSystemFailure) component from the Server as a response to ProcessUnstructuredSSRequest 
	 * 
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + ReturnError(systemFailure)  
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testComponentErrorMessageSystemFailure() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			@Override
			public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
				super.onErrorComponent(mapDialog, invokeId, mapErrorMessage);
				assertTrue(mapErrorMessage.isEmSystemFailure());
				MAPErrorMessageSystemFailure mes = mapErrorMessage.getEmSystemFailure();
				assertNotNull(mes);
				assertTrue(mes.getAdditionalNetworkResource() == null);
				assertTrue(mes.getNetworkResource() == null);
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				String ussdString = procUnstrReqInd.getUSSDString().getString();
				AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
				this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
				MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
				MAPErrorMessage msg = this.mapErrorMessageFactory.createMAPErrorMessageSystemFailure(2, null, null, null);
				try {
					mapDialog.sendErrorComponent(procUnstrReqInd.getInvokeId(), msg);
				} catch (MAPException e) {
					this.error("Error while trying to add Error Component", e);
					fail("Error while trying to add Error Component");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while trying to send Error Component", e);
					fail("Error while trying to send Error Component");
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
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

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.actionA();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Sending ReturnError (SM-DeliveryFailure + SM-DeliveryFailureCause) component from the Server as a response to ProcessUnstructuredSSRequest 
	 * 
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + ReturnError(SM-DeliveryFailure + SM-DeliveryFailureCause)  
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testComponentErrorMessageSMDeliveryFailure() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			@Override
			public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
				super.onErrorComponent(mapDialog, invokeId, mapErrorMessage);
				assertTrue(mapErrorMessage.isEmSMDeliveryFailure());
				MAPErrorMessageSMDeliveryFailure mes = mapErrorMessage.getEmSMDeliveryFailure();
				assertNotNull(mes);
				assertEquals(mes.getSMEnumeratedDeliveryFailureCause(), SMEnumeratedDeliveryFailureCause.scCongestion);
				assertTrue(mes.getSignalInfo() == null);
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				String ussdString = procUnstrReqInd.getUSSDString().getString();
				AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
				this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
				MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
				MAPErrorMessage msg = this.mapErrorMessageFactory.createMAPErrorMessageSMDeliveryFailure(SMEnumeratedDeliveryFailureCause.scCongestion, null,
						null);
				try {
					mapDialog.sendErrorComponent(procUnstrReqInd.getInvokeId(), msg);
				} catch (MAPException e) {
					this.error("Error while trying to add Error Component", e);
					fail("Error while trying to add Error Component");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while trying to send Error Component", e);
					fail("Error while trying to send Error Component");
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
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

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.actionA();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Responses as ReturnResult (this case is simulated) and ReturnResultLast 
	 * 
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-CONTINUE + ReturnResult (addProcessUnstructuredSSResponse)
	 * TC-CONTINUE  
	 * TC-END + ReturnResultLast (addProcessUnstructuredSSResponse) 
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testComponentD() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			int responseReceived = 0;

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					mapDialog.send();
				} catch (MAPException e) {
					this.error("Error while sending response", e);
					fail("Error while trying to send empty response");
				}
			}

			@Override
			public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResponse) {
				super.onProcessUnstructuredSSResponse(procUnstrResponse);
				String ussdString = procUnstrResponse.getUSSDString().getString();
				this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
				responseReceived++;
				assertEquals(ussdString, "Your balance is 500");

			}

			@Override
			public void onDialogRelease(MAPDialog mapDialog) {
				super.onDialogRelease(mapDialog);
				assertEquals(this.responseReceived, 2);
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

			private long processUnstructuredSSRequestInvokeId = 0l;
			private int dialogStep;

			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				String ussdString = procUnstrReqInd.getUSSDString().getString();
				AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
				this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);

				MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();

				processUnstructuredSSRequestInvokeId = procUnstrReqInd.getInvokeId();

				ReturnResult returnResult = ((MAPProviderImpl) this.mapProvider).getTCAPProvider().getComponentPrimitiveFactory().createTCResultRequest();
				returnResult.setInvokeId(processUnstructuredSSRequestInvokeId);

				// Operation Code
				OperationCode oc = TcapFactory.createOperationCode();
				oc.setLocalOperationCode((long) MAPOperationCode.processUnstructuredSS_Request);
				returnResult.setOperationCode(oc);

				USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory().createUSSDString("Your balance is 500");
				byte ussdDataCodingScheme = (byte) 0x0F;
				ProcessUnstructuredSSResponseImpl req = new ProcessUnstructuredSSResponseImpl(ussdDataCodingScheme, ussdStrObj);
				AsnOutputStream aos = new AsnOutputStream();
				try {
					req.encodeData(aos);

					Parameter p = ((MAPProviderImpl) this.mapProvider).getTCAPProvider().getComponentPrimitiveFactory().createParameter();
					p.setTagClass(req.getTagClass());
					p.setPrimitive(req.getIsPrimitive());
					p.setTag(req.getTag());
					p.setData(aos.toByteArray());
					returnResult.setParameter(p);

					mapDialog.sendReturnResultComponent(returnResult);
				} catch (MAPException e) {
					this.error("Error while trying to send ProcessUnstructuredSSResponse", e);
					fail("Error while trying to send ProcessUnstructuredSSResponse");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				this.dialogStep++;
				try {
					if (this.dialogStep == 1) {
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null, sequence++));
						mapDialog.send();
					} else {
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null, sequence++));
						USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory().createUSSDString("Your balance is 500");
						byte ussdDataCodingScheme = (byte) 0x0F;
						((MAPDialogSupplementary) mapDialog).addProcessUnstructuredSSResponse(this.processUnstructuredSSRequestInvokeId, ussdDataCodingScheme,
								ussdStrObj);

						mapDialog.close(false);
					}
				} catch (MAPException e) {
					this.error("Error while trying to send Response", e);
					fail("Error while trying to send ProcessUnstructuredSSResponse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++, stamp);
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

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.actionA();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Responses as Reject (DuplicateInvokeID) component from the Server as a response to ProcessUnstructuredSSRequest
	 * 
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + Reject (invokeProblem)  
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testComponentDuplicateInvokeID() throws Exception {
		// Action_Component_E

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			@Override
			public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem) {
				super.onRejectComponent(mapDialog, invokeId, problem);
				InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
				assertNotNull(invokeProblemType);
				assertEquals(invokeProblemType, InvokeProblemType.DuplicateInvokeID);
				assertTrue(problem.getGeneralProblemType() == null);
				assertTrue(problem.getReturnErrorProblemType() == null);
				assertTrue(problem.getReturnResultProblemType() == null);
				assertEquals((long) invokeId, 1);
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				String ussdString = procUnstrReqInd.getUSSDString().getString();
				AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
				this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
				MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();

				Problem problem = this.mapProvider.getMAPParameterFactory().createProblemInvoke(InvokeProblemType.DuplicateInvokeID);
				try {
					mapDialog.sendRejectComponent(procUnstrReqInd.getInvokeId(), problem);
				} catch (MAPException e) {
					this.error("Error while trying to send Duplicate InvokeId Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while trying to send Error Component", e);
					fail("Error while trying to send Duplicate InvokeId Component");
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
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

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.actionA();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Responses as ReturnError component from the Server as a response to ProcessUnstructuredSSRequest
	 * but the error received because of "close(true)"
	 * 
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + ReturnError(systemFailure) - using "close(true)" - so no ReturnError must be sent !   
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testComponentErrorCloseTrue() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			@Override
			public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
				super.onErrorComponent(mapDialog, invokeId, mapErrorMessage);
				assertTrue(mapErrorMessage.isEmSMDeliveryFailure());
				MAPErrorMessageSMDeliveryFailure mes = mapErrorMessage.getEmSMDeliveryFailure();
				assertNotNull(mes);
				assertEquals(mes.getSMEnumeratedDeliveryFailureCause(), SMEnumeratedDeliveryFailureCause.scCongestion);
				assertTrue(mes.getSignalInfo() == null);
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				String ussdString = procUnstrReqInd.getUSSDString().getString();
				AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
				this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
				MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
				MAPErrorMessage msg = this.mapErrorMessageFactory.createMAPErrorMessageSMDeliveryFailure(SMEnumeratedDeliveryFailureCause.scCongestion, null,
						null);
				try {
					mapDialog.sendErrorComponent(procUnstrReqInd.getInvokeId(), msg);
				} catch (MAPException e) {
					this.error("Error while trying to add Error Component", e);
					fail("Error while trying to add Error Component");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));
					mapDialog.close(true);
				} catch (MAPException e) {
					this.error("Error while trying to send Error Component", e);
					fail("Error while trying to send Error Component");
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
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

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.actionA();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Responses as Reject (MistypedComponent without invokeId!) component from the Server as a response to ProcessUnstructuredSSRequest
	 * 
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + Reject (generalProblem-MistypedComponent) without invokeId!  
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testComponentGeneralProblemTypeComponent() throws Exception {
		// Action_Component_G

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			@Override
			public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem) {
				super.onRejectComponent(mapDialog, invokeId, problem);
				GeneralProblemType generalProblemType = problem.getGeneralProblemType();
				assertNotNull(generalProblemType);
				assertEquals(generalProblemType, GeneralProblemType.MistypedComponent);
				assertTrue(problem.getInvokeProblemType() == null);
				assertTrue(problem.getReturnErrorProblemType() == null);
				assertTrue(problem.getReturnResultProblemType() == null);
				assertNull(invokeId);
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				String ussdString = procUnstrReqInd.getUSSDString().getString();
				AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
				this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
				assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
				MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();

				Problem problem = this.mapProvider.getMAPParameterFactory().createProblemGeneral(GeneralProblemType.MistypedComponent);
				try {
					mapDialog.sendRejectComponent(null, problem);
				} catch (MAPException e) {
					this.error("Error while trying to add Duplicate InvokeId Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.ErrorComponent, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while trying to send Error Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.RejectComponent, null, count++, stamp);
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

		te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ErrorComponent, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.actionA();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Below are test for MAP V1 Dialogs
	 */

	/**
	 * TC-BEGIN+INVOKE(opCode=47) -> TC-END+RRL(opCode=47) (47=reportSM-DeliveryStatus)
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testV1ReportSMDeliveryStatus() throws Exception {
		// Action_V1_A
		Client client = new Client(stack1, this, peer1Address, peer2Address);

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

			@Override
			public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
				super.onReportSMDeliveryStatusRequest(reportSMDeliveryStatusInd);
				MAPDialogSms d = reportSMDeliveryStatusInd.getMAPDialog();

				ISDNAddressString msisdn = reportSMDeliveryStatusInd.getMsisdn();
				AddressString sca = reportSMDeliveryStatusInd.getServiceCentreAddress();
				SMDeliveryOutcome sMDeliveryOutcome = reportSMDeliveryStatusInd.getSMDeliveryOutcome();
				Integer absentSubscriberDiagnosticSM = reportSMDeliveryStatusInd.getAbsentSubscriberDiagnosticSM();
				MAPExtensionContainer extensionContainer = reportSMDeliveryStatusInd.getExtensionContainer();
				Boolean gprsSupportIndicator = reportSMDeliveryStatusInd.getGprsSupportIndicator();
				Boolean deliveryOutcomeIndicator = reportSMDeliveryStatusInd.getDeliveryOutcomeIndicator();
				SMDeliveryOutcome additionalSMDeliveryOutcome = reportSMDeliveryStatusInd.getAdditionalSMDeliveryOutcome();
				Integer additionalAbsentSubscriberDiagnosticSM = reportSMDeliveryStatusInd.getAdditionalAbsentSubscriberDiagnosticSM();

				Assert.assertNotNull(msisdn);
				Assert.assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
				Assert.assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
				Assert.assertEquals(msisdn.getAddress(), "111222333");
				Assert.assertNotNull(sca);
				Assert.assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
				Assert.assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
				Assert.assertEquals(sca.getAddress(), "999000");
				Assert.assertEquals(sMDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);
				Assert.assertNull(absentSubscriberDiagnosticSM);
				Assert.assertFalse(gprsSupportIndicator);
				Assert.assertFalse(deliveryOutcomeIndicator);
				Assert.assertNull(additionalSMDeliveryOutcome);
				Assert.assertNull(additionalAbsentSubscriberDiagnosticSM);
				Assert.assertNull(extensionContainer);

				try {
					d.addReportSMDeliveryStatusResponse(reportSMDeliveryStatusInd.getInvokeId(), null, null);
				} catch (MAPException e) {
					this.error("Error while trying to add ReportSMDeliveryStatusResponse", e);
					fail("Error while trying to add ReportSMDeliveryStatusResponse");
				}

			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusRespIndication, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while trying to send ReportSMDeliveryStatusResponse", e);
					fail("Error while trying to send ReportSMDeliveryStatusResponse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusRespIndication, null, count++, stamp);
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

		te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusRespIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendReportSMDeliveryStatusV1();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN+INVOKE(opCode=49) -> release()
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testV1AlertServiceCentreRequest() throws Exception {
		// Action_V1_B

		Client client = new Client(stack1, this, peer1Address, peer2Address);

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

			@Override
			public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {
				super.onAlertServiceCentreRequest(alertServiceCentreInd);
				MAPDialogSms d = alertServiceCentreInd.getMAPDialog();

				ISDNAddressString msisdn = alertServiceCentreInd.getMsisdn();
				AddressString serviceCentreAddress = alertServiceCentreInd.getServiceCentreAddress();

				Assert.assertNotNull(msisdn);
				Assert.assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
				Assert.assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
				Assert.assertEquals(msisdn.getAddress(), "111222333");
				Assert.assertNotNull(serviceCentreAddress);
				Assert.assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.subscriber_number);
				Assert.assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.national);
				Assert.assertEquals(serviceCentreAddress.getAddress(), "0011");
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				mapDialog.release();
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.AlertServiceCentreIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendAlertServiceCentreRequestV1();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN(empty - no components) -> TC-ABORT V1
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testV1AlertServiceCentreRequestReject() throws Exception {
		// Action_V1_C

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
					ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
				super.onDialogReject(mapDialog, refuseReason, providerError, alternativeApplicationContext, extensionContainer);
				assertNotNull(refuseReason);
				assertEquals(refuseReason, MAPRefuseReason.PotentialVersionIncompatibility);
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address);

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogReject, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.sendEmptyV1Request();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN(unsupported opCode) -> TC-ABORT V1
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testV1AlertServiceCentreRequestReject2() throws Exception {
		// Action_V1_D

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
					ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
				super.onDialogReject(mapDialog, refuseReason, providerError, alternativeApplicationContext, extensionContainer);
				assertNotNull(refuseReason);
				assertEquals(refuseReason, MAPRefuseReason.PotentialVersionIncompatibility);
			}
		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address);

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogReject, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.sendV1BadOperationCode();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN+INVOKE(opCode=46) -> TC-CONTINUE(empty) -> TC-ABORT(UserReason) (->Abort V1)
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testV1ForwardShortMessageRequest() throws Exception {
		// Action_V1_E

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					MAPUserAbortChoice choice = this.mapParameterFactory.createMAPUserAbortChoice();
					choice.setProcedureCancellationReason(ProcedureCancellationReason.handoverCancellation);
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.DialogUserAbort, null, sequence++));
					mapDialog.abort(choice);
				} catch (MAPException e) {
					this.error("Error while trying to send Abort", e);
					fail("Error while trying to send Abort");
				}
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
				super.onForwardShortMessageRequest(forwSmInd);
				MAPDialogSms d = forwSmInd.getMAPDialog();

				SM_RP_DA sm_RP_DA = forwSmInd.getSM_RP_DA();
				SM_RP_OA sm_RP_OA = forwSmInd.getSM_RP_OA();
				SmsSignalInfo sm_RP_UI = forwSmInd.getSM_RP_UI();

				Assert.assertNotNull(sm_RP_DA);
				Assert.assertNotNull(sm_RP_DA.getIMSI());
				Assert.assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
				Assert.assertNotNull(sm_RP_OA);
				Assert.assertNotNull(sm_RP_OA.getMsisdn());
				Assert.assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
				Assert.assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
				Assert.assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
				Assert.assertNotNull(sm_RP_UI);
				Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
				Assert.assertFalse(forwSmInd.getMoreMessagesToSend());

			}

			@Override
			public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
					MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
				super.onDialogProviderAbort(mapDialog, abortProviderReason, abortSource, extensionContainer);
				
				Assert.assertEquals(abortProviderReason, MAPAbortProviderReason.AbnormalMAPDialogue);
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
//					this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardShortMessageRespIndication, null, sequence++));
					mapDialog.send();
				} catch (MAPException e) {
					this.error("Error while sending the empty ForwardShortMessageResponse", e);
					fail("Error while sending the empty ForwardShortMessageResponse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ForwardShortMessageIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.DialogUserAbort, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ForwardShortMessageIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendForwardShortMessageRequestV1();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Below are test from testSmsService
	 */

	/**
	 * TC-BEGIN + AlertServiceCentreRequest
	 * TC-END  
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testV2AlertServiceCentreRequest() throws Exception {
		// Action_Sms_AlertServiceCentre

		Client client = new Client(stack1, this, peer1Address, peer2Address);

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {

			@Override
			public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {
				super.onAlertServiceCentreRequest(alertServiceCentreInd);
				MAPDialogSms d = alertServiceCentreInd.getMAPDialog();

				ISDNAddressString msisdn = alertServiceCentreInd.getMsisdn();
				AddressString serviceCentreAddress = alertServiceCentreInd.getServiceCentreAddress();

				Assert.assertNotNull(msisdn);
				Assert.assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
				Assert.assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
				Assert.assertEquals(msisdn.getAddress(), "111222333");
				Assert.assertNotNull(serviceCentreAddress);
				Assert.assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.subscriber_number);
				Assert.assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.national);
				Assert.assertEquals(serviceCentreAddress.getAddress(), "0011");

				try {
					d.addAlertServiceCentreResponse(alertServiceCentreInd.getInvokeId());
				} catch (MAPException e) {
					this.error("Error when adding AlertServiceCentreResponse", e);
					fail("Error when adding AlertServiceCentreResponse");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreRespIndication, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while trying to send AlertServiceCentreResponse", e);
					fail("Error when sending AlertServiceCentreResponse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.AlertServiceCentreRespIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.AlertServiceCentreIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.AlertServiceCentreRespIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendAlertServiceCentreRequestV2();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN + ForwardSMRequest_V2
	 * TC-END + ForwardSMResponse_V2  
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testV2ForwardShortMessageRequest() throws Exception {
		// Action_Sms_ForwardSM

		Client client = new Client(stack1, this, peer1Address, peer2Address);

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
				super.onForwardShortMessageRequest(forwSmInd);
				MAPDialogSms d = forwSmInd.getMAPDialog();

				SM_RP_DA sm_RP_DA = forwSmInd.getSM_RP_DA();
				SM_RP_OA sm_RP_OA = forwSmInd.getSM_RP_OA();
				SmsSignalInfo sm_RP_UI = forwSmInd.getSM_RP_UI();

				Assert.assertNotNull(sm_RP_DA);
				Assert.assertNotNull(sm_RP_DA.getIMSI());
				Assert.assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
				Assert.assertNotNull(sm_RP_OA);
				Assert.assertNotNull(sm_RP_OA.getMsisdn());
				Assert.assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
				Assert.assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
				Assert.assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
				Assert.assertNotNull(sm_RP_UI);
				Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
				Assert.assertTrue(forwSmInd.getMoreMessagesToSend());
				try {
					d.addForwardShortMessageResponse(forwSmInd.getInvokeId());
				} catch (MAPException e) {
					this.error("Error while adding ForwardShortMessageResponse", e);
					fail("Error when adding ForwardShortMessageResponse");
				}

			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardShortMessageRespIndication, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending the empty ForwardShortMessageResponse", e);
					fail("Error when sending ForwardShortMessageResponse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ForwardShortMessageIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ForwardShortMessageRespIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ForwardShortMessageIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ForwardShortMessageRespIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendForwardShortMessageRequestV2();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN + MoForwardSMRequest
	 * TC-END + MoForwardSMResponse  
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testMoForwardShortMessageRequest() throws Exception {
		// Action_Sms_MoForwardSM

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
				super.onMoForwardShortMessageResponse(moForwSmRespInd);
				SmsSignalInfo sm_RP_UI = moForwSmRespInd.getSM_RP_UI();
				MAPExtensionContainer extensionContainer = moForwSmRespInd.getExtensionContainer();

				Assert.assertNotNull(sm_RP_UI);
				Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
				super.onMoForwardShortMessageRequest(moForwSmInd);
				MAPDialogSms d = moForwSmInd.getMAPDialog();

				SM_RP_DA sm_RP_DA = moForwSmInd.getSM_RP_DA();
				SM_RP_OA sm_RP_OA = moForwSmInd.getSM_RP_OA();
				SmsSignalInfo sm_RP_UI = moForwSmInd.getSM_RP_UI();
				MAPExtensionContainer extensionContainer = moForwSmInd.getExtensionContainer();
				IMSI imsi2 = moForwSmInd.getIMSI();

				Assert.assertNotNull(sm_RP_DA);
				Assert.assertNotNull(sm_RP_DA.getIMSI());
				Assert.assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
				Assert.assertNotNull(sm_RP_OA);
				Assert.assertNotNull(sm_RP_OA.getMsisdn());
				Assert.assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
				Assert.assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
				Assert.assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
				Assert.assertNotNull(sm_RP_UI);

				try {
					SmsSubmitTpdu tpdu = (SmsSubmitTpdu) sm_RP_UI.decodeTpdu(true);
					tpdu.getUserData().decode();
					Assert.assertFalse(tpdu.getRejectDuplicates());
					Assert.assertTrue(tpdu.getReplyPathExists());
					Assert.assertFalse(tpdu.getStatusReportRequest());
					Assert.assertEquals(tpdu.getMessageReference(), 55);
					Assert.assertEquals(tpdu.getDestinationAddress().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
					Assert.assertEquals(tpdu.getDestinationAddress().getNumberingPlanIdentification(), NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
					Assert.assertTrue(tpdu.getDestinationAddress().getAddressValue().equals("700007"));
					Assert.assertEquals(tpdu.getProtocolIdentifier().getCode(), 0);
					Assert.assertEquals((int) tpdu.getValidityPeriod().getRelativeFormatValue(), 100);
					Assert.assertEquals(tpdu.getUserData().getDataCodingScheme().getCode(), 0);
					Assert.assertTrue(tpdu.getUserData().getDecodedMessage().equals("Hello, world !!!"));
				} catch (MAPException e) {
					this.error("Erro while trying to decode SmsSubmitTpdu", e);
					fail("Erro while trying to decode SmsSubmitTpdu");
				}

				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
				Assert.assertNotNull(imsi2);
				Assert.assertEquals(imsi2.getData(), "25007123456789");

				SmsSignalInfo sm_RP_UI2 = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);
				try {
					d.addMoForwardShortMessageResponse(moForwSmInd.getInvokeId(), sm_RP_UI2, MAPExtensionContainerTest.GetTestExtensionContainer());
				} catch (MAPException e) {
					this.error("Error while adding MoForwardShortMessageResponse", e);
					fail("Error while adding MoForwardShortMessageResponse");
				}

			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.MoForwardShortMessageRespIndication, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending the empty ForwardShortMessageResponse", e);
					fail("Error while sending the empty ForwardShortMessageResponse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.MoForwardShortMessageIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageRespIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.MoForwardShortMessageRespIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendMoForwardShortMessageRequest();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN + MtForwardSMRequest
	 * TC-END + MtForwardSMResponse  
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testMtForwardShortMessageRequest() throws Exception {
		// Action_Sms_MtForwardSM

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {
				super.onMtForwardShortMessageResponse(mtForwSmRespInd);
				SmsSignalInfo sm_RP_UI = mtForwSmRespInd.getSM_RP_UI();
				MAPExtensionContainer extensionContainer = mtForwSmRespInd.getExtensionContainer();

				Assert.assertNotNull(sm_RP_UI);
				Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {
				super.onMtForwardShortMessageRequest(mtForwSmInd);

				MAPDialogSms d = mtForwSmInd.getMAPDialog();

				SM_RP_DA sm_RP_DA = mtForwSmInd.getSM_RP_DA();
				SM_RP_OA sm_RP_OA = mtForwSmInd.getSM_RP_OA();
				SmsSignalInfo sm_RP_UI = mtForwSmInd.getSM_RP_UI();
				MAPExtensionContainer extensionContainer = mtForwSmInd.getExtensionContainer();
				Boolean moreMessagesToSend = mtForwSmInd.getMoreMessagesToSend();

				Assert.assertNotNull(sm_RP_DA);
				Assert.assertNotNull(sm_RP_DA.getLMSI());
				Assert.assertTrue(Arrays.equals(sm_RP_DA.getLMSI().getData(), new byte[] { 49, 48, 47, 46 }));
				Assert.assertNotNull(sm_RP_OA);
				Assert.assertNotNull(sm_RP_OA.getServiceCentreAddressOA());
				Assert.assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddressNature(), AddressNature.international_number);
				Assert.assertEquals(sm_RP_OA.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.ISDN);
				Assert.assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddress(), "111222333");
				Assert.assertNotNull(sm_RP_UI);
				Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
				Assert.assertTrue(moreMessagesToSend);

				SmsSignalInfo sm_RP_UI2 = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);

				try {
					d.addMtForwardShortMessageResponse(mtForwSmInd.getInvokeId(), sm_RP_UI2, MAPExtensionContainerTest.GetTestExtensionContainer());
				} catch (MAPException e) {
					this.error("Error while adding MtForwardShortMessageResponse", e);
					fail("Error while adding MtForwardShortMessageResponse");
				}

			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.MtForwardShortMessageRespIndication, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending the empty ForwardShortMessageResponse", e);
					fail("Error while sending the empty ForwardShortMessageResponse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.MtForwardShortMessageIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.MtForwardShortMessageRespIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.MtForwardShortMessageIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.MtForwardShortMessageRespIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendMtForwardShortMessageRequest();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN + ReportSMDeliveryStatusRequest
	 * TC-END + ReportSMDeliveryStatusResponse  
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testReportSMDeliveryStatusRequest() throws Exception {
		// Action_Sms_ReportSMDeliveryStatus

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {
				super.onReportSMDeliveryStatusResponse(reportSMDeliveryStatusRespInd);
				ISDNAddressString storedMSISDN = reportSMDeliveryStatusRespInd.getStoredMSISDN();
				MAPExtensionContainer extensionContainer = reportSMDeliveryStatusRespInd.getExtensionContainer();

				Assert.assertNotNull(storedMSISDN);
				Assert.assertEquals(storedMSISDN.getAddressNature(), AddressNature.network_specific_number);
				Assert.assertEquals(storedMSISDN.getNumberingPlan(), NumberingPlan.national);
				Assert.assertEquals(storedMSISDN.getAddress(), "111000111");
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
				super.onReportSMDeliveryStatusRequest(reportSMDeliveryStatusInd);

				MAPDialogSms d = reportSMDeliveryStatusInd.getMAPDialog();

				ISDNAddressString msisdn = reportSMDeliveryStatusInd.getMsisdn();
				AddressString sca = reportSMDeliveryStatusInd.getServiceCentreAddress();
				SMDeliveryOutcome sMDeliveryOutcome = reportSMDeliveryStatusInd.getSMDeliveryOutcome();
				Integer absentSubscriberDiagnosticSM = reportSMDeliveryStatusInd.getAbsentSubscriberDiagnosticSM();
				MAPExtensionContainer extensionContainer = reportSMDeliveryStatusInd.getExtensionContainer();
				Boolean gprsSupportIndicator = reportSMDeliveryStatusInd.getGprsSupportIndicator();
				Boolean deliveryOutcomeIndicator = reportSMDeliveryStatusInd.getDeliveryOutcomeIndicator();
				SMDeliveryOutcome additionalSMDeliveryOutcome = reportSMDeliveryStatusInd.getAdditionalSMDeliveryOutcome();
				Integer additionalAbsentSubscriberDiagnosticSM = reportSMDeliveryStatusInd.getAdditionalAbsentSubscriberDiagnosticSM();

				Assert.assertNotNull(msisdn);
				Assert.assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
				Assert.assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
				Assert.assertEquals(msisdn.getAddress(), "111222333");
				Assert.assertNotNull(sca);
				Assert.assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
				Assert.assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
				Assert.assertEquals(sca.getAddress(), "999000");
				Assert.assertEquals(sMDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);

				Assert.assertNotNull(absentSubscriberDiagnosticSM);
				Assert.assertEquals((int) absentSubscriberDiagnosticSM, 555);
				Assert.assertTrue(gprsSupportIndicator);
				Assert.assertTrue(deliveryOutcomeIndicator);
				Assert.assertEquals(additionalSMDeliveryOutcome, SMDeliveryOutcome.successfulTransfer);
				Assert.assertNotNull(additionalAbsentSubscriberDiagnosticSM);
				Assert.assertEquals((int) additionalAbsentSubscriberDiagnosticSM, 444);
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

				ISDNAddressString storedMSISDN = this.mapParameterFactory.createISDNAddressString(AddressNature.network_specific_number,
						NumberingPlan.national, "111000111");

				try {
					d.addReportSMDeliveryStatusResponse(reportSMDeliveryStatusInd.getInvokeId(), storedMSISDN,
							MAPExtensionContainerTest.GetTestExtensionContainer());
				} catch (MAPException e) {
					this.error("Error while adding ReportSMDeliveryStatusResponse", e);
				}

			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusRespIndication, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending the empty ForwardShortMessageResponse", e);
					fail("Error while sending the empty ForwardShortMessageResponse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusRespIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusRespIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendReportSMDeliveryStatus();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN + SendRoutingInfoForSMRequest
	 * TC-END + SendRoutingInfoForSMResponse + InformServiceCentreRequest
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testSendRoutingInfoForSM() throws Exception {
		// Action_Sms_SendRoutingInfoForSM

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd) {
				super.onSendRoutingInfoForSMResponse(sendRoutingInfoForSMRespInd);
				IMSI imsi = sendRoutingInfoForSMRespInd.getIMSI();
				MAPExtensionContainer extensionContainer = sendRoutingInfoForSMRespInd.getExtensionContainer();
				LocationInfoWithLMSI locationInfoWithLMSI = sendRoutingInfoForSMRespInd.getLocationInfoWithLMSI();
				ISDNAddressString networkNodeNumber = locationInfoWithLMSI.getNetworkNodeNumber();
				LMSI lmsi = locationInfoWithLMSI.getLMSI();
				MAPExtensionContainer extensionContainer2 = locationInfoWithLMSI.getExtensionContainer();
				AdditionalNumberType additionalNumberType = locationInfoWithLMSI.getAdditionalNumberType();
				ISDNAddressString additionalNumber = locationInfoWithLMSI.getAdditionalNumber();

				Assert.assertNotNull(imsi);
				Assert.assertEquals(imsi.getData(), "25099777000");
				Assert.assertNotNull(networkNodeNumber);
				Assert.assertEquals(networkNodeNumber.getAddressNature(), AddressNature.network_specific_number);
				Assert.assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.national);
				Assert.assertEquals(networkNodeNumber.getAddress(), "111000111");
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer2));
				Assert.assertNotNull(lmsi);
				Assert.assertTrue(Arrays.equals(lmsi.getData(), new byte[] { 75, 74, 73, 72 }));
				Assert.assertEquals(additionalNumberType, AdditionalNumberType.sgsn);
				Assert.assertNotNull(additionalNumber);
				Assert.assertEquals(additionalNumber.getAddressNature(), AddressNature.subscriber_number);
				Assert.assertEquals(additionalNumber.getNumberingPlan(), NumberingPlan.private_plan);
				Assert.assertEquals(additionalNumber.getAddress(), "000111000");
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {
				super.onSendRoutingInfoForSMRequest(sendRoutingInfoForSMInd);

				MAPDialogSms d = sendRoutingInfoForSMInd.getMAPDialog();

				ISDNAddressString msisdn = sendRoutingInfoForSMInd.getMsisdn();
				Boolean sm_RP_PRI = sendRoutingInfoForSMInd.getSm_RP_PRI();
				AddressString sca = sendRoutingInfoForSMInd.getServiceCentreAddress();
				MAPExtensionContainer extensionContainer = sendRoutingInfoForSMInd.getExtensionContainer();
				Boolean gprsSupportIndicator = sendRoutingInfoForSMInd.getGprsSupportIndicator();
				SM_RP_MTI sM_RP_MTI = sendRoutingInfoForSMInd.getSM_RP_MTI();
				SM_RP_SMEA sM_RP_SMEA = sendRoutingInfoForSMInd.getSM_RP_SMEA();

				Assert.assertNotNull(msisdn);
				Assert.assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
				Assert.assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
				Assert.assertEquals(msisdn.getAddress(), "111222333");
				Assert.assertFalse(sm_RP_PRI);
				Assert.assertNotNull(sca);
				Assert.assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
				Assert.assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
				Assert.assertEquals(sca.getAddress(), "999000");
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
				Assert.assertTrue(gprsSupportIndicator);
				Assert.assertEquals(sM_RP_MTI, SM_RP_MTI.SMS_Status_Report);
				Assert.assertTrue(Arrays.equals(sM_RP_SMEA.getData(), new byte[] { 90, 91 }));

				IMSI imsi = this.mapParameterFactory.createIMSI("25099777000");
				ISDNAddressString networkNodeNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.network_specific_number,
						NumberingPlan.national, "111000111");
				LMSI lmsi = this.mapParameterFactory.createLMSI(new byte[] { 75, 74, 73, 72 });
				AdditionalNumberType additionalNumberType = AdditionalNumberType.sgsn;
				ISDNAddressString additionalNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.subscriber_number,
						NumberingPlan.private_plan, "000111000");
				LocationInfoWithLMSI locationInfoWithLMSI = this.mapParameterFactory.createLocationInfoWithLMSI(networkNodeNumber, lmsi,
						MAPExtensionContainerTest.GetTestExtensionContainer(), additionalNumberType, additionalNumber);

				ISDNAddressString storedMSISDN = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN,
						"111222333");
				MWStatus mwStatus = this.mapParameterFactory.createMWStatus(false, true, false, true);
				Integer absentSubscriberDiagnosticSM = 555;
				Integer additionalAbsentSubscriberDiagnosticSM = 444;

				try {
					d.addSendRoutingInfoForSMResponse(sendRoutingInfoForSMInd.getInvokeId(), imsi, locationInfoWithLMSI,
							MAPExtensionContainerTest.GetTestExtensionContainer());
					d.addInformServiceCentreRequest(storedMSISDN, mwStatus, MAPExtensionContainerTest.GetTestExtensionContainer(),
							absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM);
				} catch (MAPException e) {
					this.error("Error while adding SendRoutingInfoForSMResponse", e);
					fail("Error while adding SendRoutingInfoForSMResponse");
				}

			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForSMRespIndication, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending the empty ForwardShortMessageResponse", e);
					fail("Error while sending the empty ForwardShortMessageResponse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.SendRoutingInfoForSMIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForSMRespIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.InformServiceCentreIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForSMIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SendRoutingInfoForSMRespIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendSendRoutingInfoForSM();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}
	
	/**
	 * testMsgLength test
	 */

	/**
	 * Sending a short SMS message (20 bytes)
	 * This message is fit to the TC-BEGIN message with Dialog portion
	 * 
	 * TC-BEGIN+MtForward(Short SMS) -> TC-END+MtForward(Response)
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testAction_TestMsgLength_A() throws Exception {
		// Action_Sms_MoForwardSM

		Client_TestMsgLength client = new Client_TestMsgLength(stack1, this, peer1Address, peer2Address, 20); // 170

		Server_TestMsgLength server = new Server_TestMsgLength(this.stack2, this, peer2Address, peer1Address);

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.MoForwardShortMessageIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageRespIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.MoForwardShortMessageRespIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendMoForwardShortMessageRequest_WithLengthChecking();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}	

	/**
	 * Sending a long SMS message (170 bytes)
	 * This message is not fit to the TC-BEGIN message with Dialog portion
	 * In the TC-BEGIN message only Dialog portion is sent,
	 * MtForward message is sent in the second (TC-CONTINUE) message 
	 * 
	 * TC-BEGIN -> TC-CONTINUE -> TC-CONTINUE+MtForward(Long SMS) -> TC-END+MtForward(Response)
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testAction_TestMsgLength_B() throws Exception {
		// Action_Sms_MoForwardSM

		Client_TestMsgLength client = new Client_TestMsgLength(stack1, this, peer1Address, peer2Address, 170);

		Server_TestMsgLength server = new Server_TestMsgLength(this.stack2, this, peer2Address, peer1Address);

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();

		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.MoForwardShortMessageIndication, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageRespIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageIndication, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.MoForwardShortMessageRespIndication, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendMoForwardShortMessageRequest_WithLengthChecking();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}	
	
	private class Client_TestMsgLength extends Client {

		protected boolean messageIsSent = false;
		protected int dataLength;

		public Client_TestMsgLength(MAPStack mapStack, MAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress, int dataLength) {
			super(mapStack, runningTestCase, thisAddress, remoteAddress);
			
			this.dataLength = dataLength;
		}

		public void sendMoForwardShortMessageRequest_WithLengthChecking() throws Exception {
			this.mapProvider.getMAPServiceSms().acivate();

			MAPApplicationContext appCnt = null;
			appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext, MAPApplicationContextVersion.version3);
			AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
			AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
					"204208300008002");

			clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
			clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

			sendMoForwardShortMessageRequest_WithLengthChecking_2(this.dataLength, clientDialogSms);

		}

		protected void sendMoForwardShortMessageRequest_WithLengthChecking_2(int dataLength, MAPDialogSms dlg) throws MAPException {
			SmsSignalInfo sm_RP_UI;
			sm_RP_UI = new SmsSignalInfoImpl(new byte[dataLength], null);
			Arrays.fill(sm_RP_UI.getData(), (byte) 11);

			IMSI imsi1 = this.mapParameterFactory.createIMSI("250991357999");
			SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
			ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
			IMSI imsi2 = this.mapParameterFactory.createIMSI("25007123456789");

			Long invokeId = dlg.addMoForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, imsi2);

			int maxMsgLen = dlg.getMaxUserDataLength();
			int curMsgLen = dlg.getMessageUserDataLengthOnSend();
			if (curMsgLen > maxMsgLen)
				dlg.cancelInvocation(invokeId);
			else {
				this.observerdEvents.add(TestEvent.createSentEvent(EventType.MoForwardShortMessageIndication, null, sequence++));
				messageIsSent = true;
			}

			dlg.send();
		}

		@Override
		public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
			super.onMoForwardShortMessageResponse(moForwSmRespInd);
			SmsSignalInfo sm_RP_UI = moForwSmRespInd.getSM_RP_UI();
			MAPExtensionContainer extensionContainer = moForwSmRespInd.getExtensionContainer();

			Assert.assertNotNull(sm_RP_UI);
			Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
			Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

		}

		@Override
		public void onDialogDelimiter(MAPDialog mapDialog) {
			super.onDialogDelimiter(mapDialog);

			if (!this.messageIsSent) {
				try {
					sendMoForwardShortMessageRequest_WithLengthChecking_2(this.dataLength, (MAPDialogSms) mapDialog);
				} catch (MAPException e) {
					this.error("Error while trying invoke sendMoForwardShortMessageRequest_WithLengthChecking_2", e);
					fail("Erro while trying to invoke sendMoForwardShortMessageRequest_WithLengthChecking_2");
				}
			}
		}

		@Override
		public void onDialogClose(MAPDialog mapDialog) {
			super.onDialogClose(mapDialog);
		}
	};

	private class Server_TestMsgLength extends Server {
		Server_TestMsgLength(MAPStack mapStack, MAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
			super(mapStack, runningTestCase, thisAddress, remoteAddress);
			// TODO Auto-generated constructor stub
		}

		protected boolean messageIsReceived = false;

		@Override
		public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
			super.onMoForwardShortMessageRequest(moForwSmInd);
			MAPDialogSms d = moForwSmInd.getMAPDialog();

			SM_RP_DA sm_RP_DA = moForwSmInd.getSM_RP_DA();
			SM_RP_OA sm_RP_OA = moForwSmInd.getSM_RP_OA();
			SmsSignalInfo sm_RP_UI = moForwSmInd.getSM_RP_UI();
			MAPExtensionContainer extensionContainer = moForwSmInd.getExtensionContainer();
			IMSI imsi2 = moForwSmInd.getIMSI();

			Assert.assertNotNull(sm_RP_DA);
			Assert.assertNotNull(sm_RP_DA.getIMSI());
			Assert.assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
			Assert.assertNotNull(sm_RP_OA);
			Assert.assertNotNull(sm_RP_OA.getMsisdn());
			Assert.assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
			Assert.assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
			Assert.assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
			Assert.assertNotNull(sm_RP_UI);

//			Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
			Assert.assertNotNull(imsi2);
			Assert.assertEquals(imsi2.getData(), "25007123456789");

			SmsSignalInfo sm_RP_UI2 = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);
			try {
				d.addMoForwardShortMessageResponse(moForwSmInd.getInvokeId(), sm_RP_UI2, MAPExtensionContainerTest.GetTestExtensionContainer());
			} catch (MAPException e) {
				this.error("Error while adding MoForwardShortMessageResponse", e);
				fail("Error while adding MoForwardShortMessageResponse");
			}

			messageIsReceived = true;
		}

		@Override
		public void onDialogDelimiter(MAPDialog mapDialog) {
			super.onDialogDelimiter(mapDialog);
			try {
				if (messageIsReceived) {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.MoForwardShortMessageRespIndication, null, sequence++));
					mapDialog.close(false);
				} else
					mapDialog.send();
			} catch (MAPException e) {
				this.error("Error while sending the empty ForwardShortMessageResponse", e);
				fail("Error while sending the empty ForwardShortMessageResponse");
			}
		}
	};

	/**
	 * TC-BEGIN + sendAuthenticationInfoRequest_V3
	 * TC-END + sendAuthenticationInfoResponse_V3
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testSendAuthenticationInfo_V3() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind) {
				super.onSendAuthenticationInfoResponse(ind);

				AuthenticationSetList asl = ind.getAuthenticationSetList();
				AuthenticationTriplet at = asl.getTripletList().getAuthenticationTriplets().get(0);

				Assert.assertEquals(ind.getMapProtocolVersion(), 3);
				Assert.assertEquals(asl.getMapProtocolVersion(), 3);
				Assert.assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
				Assert.assertTrue(Arrays.equals(at.getRand(), TripletListTest.getRandData()));
				Assert.assertTrue(Arrays.equals(at.getSres(), TripletListTest.getSresData()));
				Assert.assertTrue(Arrays.equals(at.getKc(), TripletListTest.getKcData()));
				Assert.assertNull(asl.getQuintupletList());
				Assert.assertNull(ind.getEpsAuthenticationSetList());
				Assert.assertNull(ind.getExtensionContainer());
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind) {
				super.onSendAuthenticationInfoRequest(ind);

				MAPDialogMobility d = ind.getMAPDialog();

				IMSI imsi = ind.getImsi();

				Assert.assertEquals(ind.getMapProtocolVersion(), 3);
				Assert.assertTrue(imsi.getData().equals("4567890"));
				Assert.assertEquals(ind.getNumberOfRequestedVectors(), 3);
				Assert.assertTrue(ind.getSegmentationProhibited());
				Assert.assertTrue(ind.getImmediateResponsePreferred());
				Assert.assertNull(ind.getReSynchronisationInfo());
				Assert.assertNull(ind.getExtensionContainer());
				Assert.assertEquals(ind.getRequestingNodeType(), RequestingNodeType.sgsn);
				Assert.assertNull(ind.getRequestingPlmnId());
				Assert.assertEquals((int)ind.getNumberOfRequestedAdditionalVectors(), 5);
				Assert.assertFalse(ind.getAdditionalVectorsAreForEPS());

				ArrayList<AuthenticationTriplet> authenticationTriplets = new ArrayList<AuthenticationTriplet>();
				AuthenticationTriplet at = this.mapParameterFactory.createAuthenticationTriplet(TripletListTest.getRandData(), TripletListTest.getSresData(),
						TripletListTest.getKcData());
				authenticationTriplets.add(at);
				TripletList tripletList = this.mapParameterFactory.createTripletList(authenticationTriplets);
				AuthenticationSetList asl = this.mapParameterFactory.createAuthenticationSetList(tripletList);

				try {
					d.addSendAuthenticationInfoResponse(ind.getInvokeId(), asl, null, null);
				} catch (MAPException e) {
					this.error("Error while adding SendAuthenticationInfoResponse", e);
					fail("Error while adding SendAuthenticationInfoResponse");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendAuthenticationInfoResp_V3, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending the empty SendAuthenticationInfoResp_V3", e);
					fail("Error while sending the empty SendAuthenticationInfoResp_V3");
				}
			}
		};
		
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.SendAuthenticationInfo_V3, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SendAuthenticationInfoResp_V3, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SendAuthenticationInfo_V3, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SendAuthenticationInfoResp_V3, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendSendAuthenticationInfo_V3();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN + sendAuthenticationInfoRequest_V2
	 * TC-END + sendAuthenticationInfoResponse_V2
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testSendAuthenticationInfo_V2() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind) {
				super.onSendAuthenticationInfoResponse(ind);

				AuthenticationSetList asl = ind.getAuthenticationSetList();
				AuthenticationTriplet at = asl.getTripletList().getAuthenticationTriplets().get(0);

				Assert.assertEquals(ind.getMapProtocolVersion(), 2);
				Assert.assertEquals(asl.getMapProtocolVersion(), 2);
				Assert.assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
				Assert.assertTrue(Arrays.equals(at.getRand(), TripletListTest.getRandData()));
				Assert.assertTrue(Arrays.equals(at.getSres(), TripletListTest.getSresData()));
				Assert.assertTrue(Arrays.equals(at.getKc(), TripletListTest.getKcData()));
				Assert.assertNull(asl.getQuintupletList());
				Assert.assertNull(ind.getEpsAuthenticationSetList());
				Assert.assertNull(ind.getExtensionContainer());
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind) {
				super.onSendAuthenticationInfoRequest(ind);

				MAPDialogMobility d = ind.getMAPDialog();

				IMSI imsi = ind.getImsi();

				Assert.assertEquals(ind.getMapProtocolVersion(), 2);
				Assert.assertTrue(imsi.getData().equals("456789000"));
				Assert.assertEquals(ind.getNumberOfRequestedVectors(), 0);
				Assert.assertFalse(ind.getSegmentationProhibited());
				Assert.assertFalse(ind.getImmediateResponsePreferred());
				Assert.assertNull(ind.getReSynchronisationInfo());
				Assert.assertNull(ind.getExtensionContainer());
				Assert.assertNull(ind.getRequestingNodeType());
				Assert.assertNull(ind.getRequestingPlmnId());
				Assert.assertNull(ind.getNumberOfRequestedAdditionalVectors());
				Assert.assertFalse(ind.getAdditionalVectorsAreForEPS());

				ArrayList<AuthenticationTriplet> authenticationTriplets = new ArrayList<AuthenticationTriplet>();
				AuthenticationTriplet at = this.mapParameterFactory.createAuthenticationTriplet(TripletListTest.getRandData(), TripletListTest.getSresData(),
						TripletListTest.getKcData());
				authenticationTriplets.add(at);
				TripletList tripletList = this.mapParameterFactory.createTripletList(authenticationTriplets);
				AuthenticationSetList asl = this.mapParameterFactory.createAuthenticationSetList(tripletList);

				try {
					d.addSendAuthenticationInfoResponse(ind.getInvokeId(), asl, null, null);
				} catch (MAPException e) {
					this.error("Error while adding SendAuthenticationInfoResponse", e);
					fail("Error while adding SendAuthenticationInfoResponse");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendAuthenticationInfoResp_V2, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending the empty SendAuthenticationInfoResp_V2", e);
					fail("Error while sending the empty SendAuthenticationInfoResp_V2");
				}
			}
		};
		
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.SendAuthenticationInfo_V2, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SendAuthenticationInfoResp_V2, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SendAuthenticationInfo_V2, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SendAuthenticationInfoResp_V2, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendSendAuthenticationInfo_V2();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN + updateLocation
	 * TC-END + updateLocationResponse
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testUpdateLocation() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onUpdateLocationResponse(UpdateLocationResponse ind) {
				super.onUpdateLocationResponse(ind);

				ISDNAddressString hlrNumber = ind.getHlrNumber();

				Assert.assertEquals(hlrNumber.getAddressNature(), AddressNature.international_number);
				Assert.assertEquals(hlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
				Assert.assertTrue(hlrNumber.getAddress().equals("765765765"));
				Assert.assertNull(ind.getExtensionContainer());
				Assert.assertTrue(ind.getAddCapability());
				Assert.assertFalse(ind.getPagingAreaCapability());
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onUpdateLocationRequest(UpdateLocationRequest ind) {
				super.onUpdateLocationRequest(ind);

				MAPDialogMobility d = ind.getMAPDialog();

				IMSI imsi = ind.getImsi();
				ISDNAddressString mscNumber = ind.getMscNumber();
				ISDNAddressString vlrNumber = ind.getVlrNumber();
				LMSI lmsi = ind.getLmsi();
				ADDInfo addInfo = ind.getADDInfo();

				Assert.assertEquals(ind.getMapProtocolVersion(), 3);
				Assert.assertTrue(imsi.getData().equals("45670000"));
				Assert.assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
				Assert.assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);
				Assert.assertTrue(mscNumber.getAddress().equals("8222333444"));
				Assert.assertNull(ind.getRoamingNumber());
				Assert.assertEquals(vlrNumber.getAddressNature(), AddressNature.network_specific_number);
				Assert.assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
				Assert.assertTrue(vlrNumber.getAddress().equals("700000111"));
				Assert.assertTrue(Arrays.equals(lmsi.getData(), new byte[] { 1, 2, 3, 4 }));
				Assert.assertNull(ind.getExtensionContainer());
				Assert.assertNull(ind.getVlrCapability());
				Assert.assertTrue(ind.getInformPreviousNetworkEntity());
				Assert.assertFalse(ind.getCsLCSNotSupportedByUE());
				Assert.assertNull(ind.getVGmlcAddress());
				Assert.assertTrue(addInfo.getImeisv().getIMEI().equals("987654321098765"));
				Assert.assertNull(ind.getPagingArea());
				Assert.assertFalse(ind.getSkipSubscriberDataUpdate());
				Assert.assertTrue(ind.getRestorationIndicator());


				ISDNAddressString hlrNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "765765765");

				try {
					d.addUpdateLocationResponse(ind.getInvokeId(), hlrNumber, null, true, false);
				} catch (MAPException e) {
					this.error("Error while adding UpdateLocationResponse", e);
					fail("Error while adding UpdateLocationResponse");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.UpdateLocationResp, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending the empty UpdateLocationResponse", e);
					fail("Error while sending the empty UpdateLocationResponse");
				}
			}
		};
		
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.UpdateLocation, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.UpdateLocationResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.UpdateLocation, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.UpdateLocationResp, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendUpdateLocation();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN + anyTimeInterrogationRequest
	 * TC-END + anyTimeInterrogationResponse
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testAnyTimeInterrogation() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse ind) {
				super.onAnyTimeInterrogationResponse(ind);

				SubscriberInfo si = ind.getSubscriberInfo();
				SubscriberState ss = si.getSubscriberState();
				Assert.assertEquals(ss.getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
				Assert.assertNull(ss.getNotReachableReason());
				Assert.assertNull(si.getLocationInformation());
				Assert.assertNull(si.getExtensionContainer());
				Assert.assertNull(si.getGPRSMSClass());
				Assert.assertNull(si.getIMEI());
				Assert.assertNull(si.getLocationInformationGPRS());
				Assert.assertNull(si.getMNPInfoRes());
				Assert.assertNull(si.getMSClassmark2());
				Assert.assertNull(si.getPSSubscriberState());
				Assert.assertNull(ind.getExtensionContainer());
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest ind) {
				super.onAnyTimeInterrogationRequest(ind);

				MAPDialogMobility d = ind.getMAPDialog();
				SubscriberIdentity subscriberIdentity = ind.getSubscriberIdentity();
				Assert.assertTrue(subscriberIdentity.getIMSI().getData().equals("33334444"));
				RequestedInfo requestedInfo = ind.getRequestedInfo();
				Assert.assertTrue(requestedInfo.getLocationInformation());
				Assert.assertTrue(requestedInfo.getSubscriberState());
				Assert.assertFalse(requestedInfo.getCurrentLocation());
				Assert.assertNull(requestedInfo.getRequestedDomain());
				Assert.assertFalse(requestedInfo.getImei());
				Assert.assertFalse(requestedInfo.getMsClassmark());
				ISDNAddressString gsmSCFAddress = ind.getGsmSCFAddress();
				Assert.assertTrue(gsmSCFAddress.getAddress().equals("11112222"));
				Assert.assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
				Assert.assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);


				SubscriberState ss = this.mapParameterFactory.createSubscriberState(SubscriberStateChoice.camelBusy, null);
				SubscriberInfo si = this.mapParameterFactory.createSubscriberInfo(null, ss, null, null, null, null, null, null, null);

				try {
					d.addAnyTimeInterrogationResponse(ind.getInvokeId(), si, null);
				} catch (MAPException e) {
					this.error("Error while adding AnyTimeInterrogationResponse", e);
					fail("Error while adding AnyTimeInterrogationResponse");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.AnyTimeInterrogationResp, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending the empty AnyTimeInterrogationResponse", e);
					fail("Error while sending the empty AnyTimeInterrogationResponse");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.AnyTimeInterrogation, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.AnyTimeInterrogationResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.AnyTimeInterrogation, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.AnyTimeInterrogationResp, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendAnyTimeInterrogation();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN + provideSubscriberLocationRequest
	 * TC-END + provideSubscriberLocationResponse
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testProvideSubscriberLocation() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onProvideSubscriberLocationResponse(ProvideSubscriberLocationResponse ind) {
				super.onProvideSubscriberLocationResponse(ind);

				Assert.assertTrue(Arrays.equals(ind.getLocationEstimate().getData(), new byte[] { 50 }));
				Assert.assertEquals((int) ind.getAgeOfLocationEstimate(), 6);
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest ind) {
				super.onProvideSubscriberLocationRequest(ind);

				MAPDialogLsm d = ind.getMAPDialog();

				Assert.assertEquals(ind.getLocationType().getLocationEstimateType(), LocationEstimateType.cancelDeferredLocation);
				Assert.assertTrue(ind.getMlcNumber().getAddress().equals("11112222"));


				ExtGeographicalInformation locationEstimate = this.mapParameterFactory.createExtGeographicalInformation(new byte[] { 50 });

				try {
					d.addProvideSubscriberLocationResponse(ind.getInvokeId(), locationEstimate, null, null, 6, null, null, false, null, false, null, null,
							false, null, null, null);
				} catch (MAPException e) {
					this.error("Error while adding ProvideSubscriberLocationResponse", e);
					fail("Error while adding ProvideSubscriberLocationResponse");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideSubscriberLocationResp, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending close()", e);
					fail("Error while sending close()");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.ProvideSubscriberLocation, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ProvideSubscriberLocationResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.ProvideSubscriberLocation, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.ProvideSubscriberLocationResp, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendProvideSubscriberLocation();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN + subscriberLocationReportRequest
	 * TC-END + subscriberLocationReportResponse
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testSubscriberLocationReport() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse ind) {
				super.onSubscriberLocationReportResponse(ind);

				Assert.assertTrue(ind.getNaESRD().getAddress().equals("11114444"));
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest ind) {
				super.onSubscriberLocationReportRequest(ind);

				MAPDialogLsm d = ind.getMAPDialog();

				Assert.assertEquals(ind.getLCSEvent(), LCSEvent.emergencyCallOrigination);
				Assert.assertEquals(ind.getLCSClientID().getLCSClientType(), LCSClientType.plmnOperatorServices);
				Assert.assertTrue(ind.getLCSLocationInfo().getNetworkNodeNumber().getAddress().equals("11113333"));


				ISDNAddressString naEsrd = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "11114444");

				try {
					d.addSubscriberLocationReportResponse(ind.getInvokeId(), naEsrd, null, null);
				} catch (MAPException e) {
					this.error("Error while adding SubscriberLocationReportResponse", e);
					fail("Error while adding SubscriberLocationReportResponse");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.SubscriberLocationReportResp, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending close()", e);
					fail("Error while sending close()");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.SubscriberLocationReport, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SubscriberLocationReportResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SubscriberLocationReport, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SubscriberLocationReportResp, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendSubscriberLocationReport();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * TC-BEGIN + sendRoutingInforForLCSRequest
	 * TC-END + sendRoutingInforForLCSResponse
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testSendRoutingInforForLCS() throws Exception {

		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse ind) {
				super.onSendRoutingInfoForLCSResponse(ind);

				Assert.assertTrue(ind.getTargetMS().getIMSI().getData().equals("6666644444"));
				Assert.assertTrue(ind.getLCSLocationInfo().getNetworkNodeNumber().getAddress().equals("11114444"));
			}

		};

		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest ind) {
				super.onSendRoutingInfoForLCSRequest(ind);

				MAPDialogLsm d = ind.getMAPDialog();

				Assert.assertTrue(ind.getMLCNumber().getAddress().equals("11112222"));
				Assert.assertTrue(ind.getTargetMS().getIMSI().getData().equals("5555544444"));


				IMSI imsi = this.mapParameterFactory.createIMSI("6666644444");
				SubscriberIdentity targetMS = this.mapParameterFactory.createSubscriberIdentity(imsi);
				ISDNAddressString networkNodeNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "11114444");;
				LCSLocationInfo lcsLocationInfo = this.mapParameterFactory.createLCSLocationInfo(networkNodeNumber, null, null, false, null, null, null, null,
						null);

				try {
					d.addSendRoutingInfoForLCSResponse(ind.getInvokeId(), targetMS, lcsLocationInfo, null, null, null, null, null);
				} catch (MAPException e) {
					this.error("Error while adding SendRoutingInfoForLCSResponse", e);
					fail("Error while adding SendRoutingInfoForLCSResponse");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForLCSResp, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending close()", e);
					fail("Error while sending close()");
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.SendRoutingInfoForLCS, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForLCSResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForLCS, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.SendRoutingInfoForLCSResp, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		client.sendSendRoutingInforForLCS();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}


	/**
	 * TC-BEGIN + checkImeiRequest
	 * TC-END + checkImeiResponse
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testCheckImei() throws Exception {
		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onCheckImeiResponse(CheckImeiResponse ind) {
				super.onCheckImeiResponse(ind);
				
				Assert.assertTrue(ind.getEquipmentStatus().equals(EquipmentStatus.blackListed));
				Assert.assertTrue(ind.getBmuef().getUESBI_IuA().getData().get(0));
				Assert.assertFalse(ind.getBmuef().getUESBI_IuB().getData().get(0));
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
			};
		};
		
		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onCheckImeiRequest(CheckImeiRequest ind) {
				super.onCheckImeiRequest(ind);
				
				MAPDialogMobility d = ind.getMAPDialog();
				
				Assert.assertTrue(ind.getIMEI().getIMEI().equals("111111112222222"));
				Assert.assertTrue(ind.getRequestedEquipmentInfo().getEquipmentStatus());
				Assert.assertFalse(ind.getRequestedEquipmentInfo().getBmuef());
				Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
				
				BitSetStrictLength bsUESBIIuA = new BitSetStrictLength(1);
				bsUESBIIuA.set(0);
				BitSetStrictLength bsUESBIIuB = new BitSetStrictLength(1);
				
				UESBIIuA uesbiIuA = this.mapParameterFactory.createUESBIIuA(bsUESBIIuA); 
				UESBIIuB uesbiIuB = this.mapParameterFactory.createUESBIIuB(bsUESBIIuB);
				UESBIIu bmuef = this.mapParameterFactory.createUESBIIu(uesbiIuA, uesbiIuB);
				
				MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
				try {
					d.addCheckImeiResponse(ind.getInvokeId(), EquipmentStatus.blackListed, bmuef, extensionContainer);
				} catch (MAPException e) {
					this.error("Error while adding CheckImeiResponse", e);
					fail("Error while adding CheckImeiResponse");
				}
			}
			
			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImeiResp, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending close()", e);
					fail("Error while sending close()");
				}
			}
		};
		
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.CheckImei, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CheckImeiResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);
		
		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CheckImei, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CheckImeiResp, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);
		
		client.sendCheckImei();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}
	
	/**
	 * TC-BEGIN + checkImeiRequest_V2
	 * TC-END + checkImeiResponse_V2
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testCheckImei_V2() throws Exception {
		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onCheckImeiResponse(CheckImeiResponse ind) {
				super.onCheckImeiResponse(ind);
				
				Assert.assertTrue(ind.getEquipmentStatus().equals(EquipmentStatus.blackListed));
				Assert.assertNull(ind.getBmuef());
				Assert.assertNull(ind.getExtensionContainer());
			};
		};
		
		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onCheckImeiRequest(CheckImeiRequest ind) {
				super.onCheckImeiRequest(ind);
				
				MAPDialogMobility d = ind.getMAPDialog();
				
				Assert.assertTrue(ind.getIMEI().getIMEI().equals("333333334444444"));
				Assert.assertNull(ind.getRequestedEquipmentInfo());
				Assert.assertNull(ind.getExtensionContainer());
				
				try {
					d.addCheckImeiResponse(ind.getInvokeId(), EquipmentStatus.blackListed, null, null);
				} catch (MAPException e) {
					this.error("Error while adding CheckImeiResponse_V2", e);
					fail("Error while adding CheckImeiResponse_V2");
				}
			}
			
			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImeiResp, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending close()", e);
					fail("Error while sending close()");
				}
			}
		};
		
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.CheckImei, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CheckImeiResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);
		
		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CheckImei, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CheckImeiResp, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);
		
		client.sendCheckImei_V2();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}
	
	/**
	 * TC-BEGIN + checkImeiRequest_V2_Huawei_extention_test
	 * TC-END + checkImeiResponse_V2
	 */
	@Test(groups = { "functional.flow", "dialog" })
	public void testCheckImei_Huawei_V2() throws Exception {
		Client client = new Client(stack1, this, peer1Address, peer2Address) {
			@Override
			public void onCheckImeiResponse(CheckImeiResponse ind) {
				super.onCheckImeiResponse(ind);
				
				Assert.assertTrue(ind.getEquipmentStatus().equals(EquipmentStatus.blackListed));
				Assert.assertNull(ind.getBmuef());
				Assert.assertNull(ind.getExtensionContainer());
			};
		};
		
		Server server = new Server(this.stack2, this, peer2Address, peer1Address) {
			@Override
			public void onCheckImeiRequest(CheckImeiRequest ind) {
				super.onCheckImeiRequest(ind);
				
				MAPDialogMobility d = ind.getMAPDialog();
				
				Assert.assertTrue(ind.getIMEI().getIMEI().equals("333333334444444"));
				Assert.assertNull(ind.getRequestedEquipmentInfo());
				Assert.assertNull(ind.getExtensionContainer());
				CheckImeiRequestImpl impl = (CheckImeiRequestImpl) ind;
				Assert.assertTrue(impl.getIMSI().getData().equals("999999998888888"));
				
				try {
					d.addCheckImeiResponse(ind.getInvokeId(), EquipmentStatus.blackListed, null, null);
				} catch (MAPException e) {
					this.error("Error while adding CheckImeiResponse_V2", e);
					fail("Error while adding CheckImeiResponse_V2");
				}
			}
			
			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImeiResp, null, sequence++));
					mapDialog.close(false);
				} catch (MAPException e) {
					this.error("Error while sending close()", e);
					fail("Error while sending close()");
				}
			}
		};
		
		long stamp = System.currentTimeMillis();
		int count = 0;
		// Client side events
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.CheckImei, null, count++, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogAccept, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CheckImeiResp, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogClose, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		clientExpectedEvents.add(te);
		
		count = 0;
		// Server side events
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.DialogRequest, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.CheckImei, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);

		te = TestEvent.createSentEvent(EventType.CheckImeiResp, null, count++, stamp);
		serverExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++, (stamp + _TCAP_DIALOG_RELEASE_TIMEOUT));
		serverExpectedEvents.add(te);
		
		client.sendCheckImei_Huawei_V2();
		waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}
	
	private void waitForEnd() {
		try {
			Thread.currentThread().sleep(_WAIT_TIMEOUT);
		} catch (InterruptedException e) {
			fail("Interrupted on wait!");
		}
	}

}
