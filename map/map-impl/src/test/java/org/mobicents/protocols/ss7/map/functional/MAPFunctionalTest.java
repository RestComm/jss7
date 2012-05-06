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
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
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
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
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
	private static final int _WAIT_TIMEOUT = _TCAP_DIALOG_RELEASE_TIMEOUT + 5000;

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
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
				}
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
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
				}
			}
			
			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				this.dialogStep++;
				try {
					if (this.dialogStep == 1) {
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.UnstructuredSSRequestIndication, null, sequence++));
						mapDialog.send();
					} else {
						this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSResponseIndication, null, sequence++));
						mapDialog.close(false);
					}
				} catch (MAPException e) {
					this.error("Error while trying to send Response", e);
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
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
					logger.error(e);
					throw new RuntimeException(e);
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
					this.error("Error while trying to send Error Component", e);
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
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
					this.error("Error while trying to send Error Component", e);
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
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

	@Test(groups = { "functional.flow", "dialog" })
	public void testComponentMistypedComponent() throws Exception {
		//Action_Component_G

		Client client = new Client(stack1, this, peer1Address, peer2Address) {

			@Override
			public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem) {
				super.onRejectComponent(mapDialog, invokeId, problem);
				GeneralProblemType generalProblemType = problem.getGeneralProblemType();
				assertNotNull(generalProblemType);
				assertEquals(generalProblemType,GeneralProblemType.MistypedComponent);
				assertTrue(problem.getInvokeProblemType() == null);
				assertTrue(problem.getReturnErrorProblemType() == null);
				assertTrue(problem.getReturnResultProblemType() == null);
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
					mapDialog.sendRejectComponent(procUnstrReqInd.getInvokeId(), problem);
				} catch (MAPException e) {
					this.error("Error while trying to send Duplicate InvokeId Component", e);
					throw new RuntimeException(e);
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
					throw new RuntimeException(e);
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

	private void waitForEnd() {
		try {
			Thread.currentThread().sleep(_WAIT_TIMEOUT);
		} catch (InterruptedException e) {
			fail("Interrupted on wait!");
		}
	}

}
