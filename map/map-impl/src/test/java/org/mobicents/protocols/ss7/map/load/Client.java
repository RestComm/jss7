/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.load;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.as.ClientM3UAManagement;
import org.mobicents.protocols.ss7.m3ua.impl.as.ClientM3UAProcess;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponseIndication;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.impl.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 * @author amit bhayani
 * 
 */
public class Client extends TestHarness {

	private static Logger logger = Logger.getLogger(Client.class);

	// MAP
	private MAPStackImpl mapStack;
	private MAPProvider mapProvider;

	// SCCP
	private SccpStackImpl sccpStack;
	private SccpResource sccpResource;

	// M3UA
	private ClientM3UAManagement clientM3UAMgmt;
	private ClientM3UAProcess clientM3UAProcess;

	// a ramp-up period is required for performance testing.
	int endCount = 0;

	AtomicInteger nbConcurrentDialogs = new AtomicInteger(0);

	long start = 0l;

	protected void initializeStack() throws Exception {
		// Initialize M3UA first
		this.initM3UA();

		// Initialize SCCP
		this.initSCCP();
		
		//Initialize MAP
		this.initMAP();
	}

	private void initM3UA() throws Exception {
		this.clientM3UAMgmt = new ClientM3UAManagement();
		this.clientM3UAMgmt.start();

		// start the ClientM3UAProcess
		this.clientM3UAProcess = new ClientM3UAProcess();
		this.clientM3UAProcess.setClientM3UAManagement(this.clientM3UAMgmt);
		this.clientM3UAProcess.start();

		// Step 1 : Create App Server
		As as = this.clientM3UAMgmt.createAppServer(String.format("m3ua as create rc %d AS1", ROUTING_CONTEXT).split(" "));

		// Step 2 : Create ASP
		AspFactory aspFactor = this.clientM3UAMgmt.createAspFactory(String.format("m3ua asp create ip %s port %d remip %s remport %d ASP1", CLIENT_IP,
				CLIENT_PORT, SERVER_IP, SERVER_PORT).split(" "));

		// Step3 : Assign ASP to AS
		Asp asp = this.clientM3UAMgmt.assignAspToAs("AS1", "ASP1");

		// Step 4: Add Route. Remote point code is 2
		this.clientM3UAMgmt.addRouteAsForDpc(SERVET_SPC, "AS1");

		// Set 5: Finally start ASP
		this.clientM3UAMgmt.managementStartAsp("ASP1");
	}

	private void initSCCP() {
		this.sccpStack = new SccpStackImpl();
		this.sccpStack.setLocalSpc(CLIENT_SPC);
		this.sccpStack.setNi(NETWORK_INDICATOR);
		this.sccpStack.setMtp3UserPart(this.clientM3UAProcess);

		sccpResource = new SccpResource();
		sccpResource.start();

		this.sccpStack.setSccpResource(this.sccpResource);

		RemoteSignalingPointCode rspc = new RemoteSignalingPointCode(SERVET_SPC, 0, 0);
		RemoteSubSystem rss = new RemoteSubSystem(SERVET_SPC, SSN, 0);
		this.sccpStack.getSccpResource().addRemoteSpc(0, rspc);
		this.sccpStack.getSccpResource().addRemoteSsn(0, rss);

		this.sccpStack.start();

	}

	private void initMAP() {
		
		System.out.println("initMAP");
		
		this.mapStack = new MAPStackImpl(this.sccpStack.getSccpProvider(), SSN);
		this.mapProvider = this.mapStack.getMAPProvider();
		
		System.out.println("this.mapProvider = "+this.mapProvider);

		this.mapProvider.addMAPDialogListener(this);
		this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);

		this.mapProvider.getMAPServiceSupplementary().acivate();

		this.mapStack.start();

		this.mapStack.getMAPProvider().getMAPServiceSupplementary().acivate();
	}

	private void initiateUSSD() throws MAPException {
		
		System.out.println("initiateUSSD");
		System.out.println(this.mapProvider);
		System.out.println(this.mapProvider.getMAPServiceSupplementary());
		
		// First create Dialog
		MAPDialogSupplementary mapDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(
				MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2),
				SCCP_CLIENT_ADDRESS, null, SCCP_SERVER_ADDRESS, null);

		byte ussdDataCodingScheme = 0x0f;

		// USSD String: *125*+31628839999#
		// The Charset is null, here we let system use default Charset (UTF-7 as
		// explained in GSM 03.38. However if MAP User wants, it can set its own
		// impl of Charset
		USSDString ussdString = this.mapProvider.getMapParameterFactory().createUSSDString("*125*+31628839999#", null);

		ISDNAddressString msisdn = this.mapProvider.getMapParameterFactory().createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN,
				"31628838002");

		mapDialog.addProcessUnstructuredSSRequest(ussdDataCodingScheme, ussdString, null, msisdn);
		
		nbConcurrentDialogs.incrementAndGet();
		
		// This will initiate the TC-BEGIN with INVOKE component
		mapDialog.send();
	}

	public static void main(String args[]) {

		int noOfCalls = Integer.parseInt(args[0]);
		int noOfConcurrentCalls = Integer.parseInt(args[1]);

		// logger.info("Number of calls to be completed = " + noOfCalls +
		// " Number of concurrent calls to be maintained = " +
		// noOfConcurrentCalls);

		NDIALOGS = noOfCalls;
		MAXCONCURRENTDIALOGS = noOfConcurrentCalls;

		final Client client = new Client();

		try {
			client.initializeStack();
			
			Thread.sleep(5000);

			while (client.endCount < NDIALOGS) {
				while (client.nbConcurrentDialogs.intValue() >= MAXCONCURRENTDIALOGS) {

					logger.info("nbConcurrentInvite = " + client.nbConcurrentDialogs.intValue() + " Waiting for max CRCX count to go down!");

					synchronized (client) {
						try {
							client.wait();
						} catch (Exception ex) {
						}
					}
				}// end of while (client.nbConcurrentDialogs.intValue() >=
					// MAXCONCURRENTDIALOGS)

				if (client.endCount == 0) {
					client.start = System.currentTimeMillis();
				}

				client.initiateUSSD();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPServiceListener#onErrorComponent
	 * (org.mobicents.protocols.ss7.map.api.MAPDialog, java.lang.Long,
	 * org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage)
	 */
	@Override
	public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
		logger.error(String.format("onErrorComponent for Dialog=%d and invokeId=%d MAPErrorMessage=%s", mapDialog.getDialogId(), invokeId, mapErrorMessage));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.MAPServiceListener#
	 * onProviderErrorComponent(org.mobicents.protocols.ss7.map.api.MAPDialog,
	 * java.lang.Long,
	 * org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError)
	 */
	@Override
	public void onProviderErrorComponent(MAPDialog mapDialog, Long invokeId, MAPProviderError providerError) {
		logger.error(String.format("onProviderErrorComponent for Dialog=%d and invokeId=%d MAPProviderError=%s", mapDialog.getDialogId(), invokeId,
				providerError));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPServiceListener#onRejectComponent
	 * (org.mobicents.protocols.ss7.map.api.MAPDialog, java.lang.Long,
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Problem)
	 */
	@Override
	public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem) {
		logger.error(String.format("onRejectComponent for Dialog=%d and invokeId=%d Problem=%s", mapDialog.getDialogId(), invokeId, problem));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPServiceListener#onInvokeTimeout
	 * (org.mobicents.protocols.ss7.map.api.MAPDialog, java.lang.Long)
	 */
	@Override
	public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
		logger.error(String.format("onInvokeTimeout for Dialog=%d and invokeId=%d", mapDialog.getDialogId(), invokeId));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.
	 * MAPServiceSupplementaryListener
	 * #onProcessUnstructuredSSRequestIndication(org
	 * .mobicents.protocols.ss7.map.
	 * api.service.supplementary.ProcessUnstructuredSSRequestIndication)
	 */
	@Override
	public void onProcessUnstructuredSSRequestIndication(ProcessUnstructuredSSRequestIndication procUnstrReqInd) {
		// This error condition. Client should never receive the
		// ProcessUnstructuredSSRequestIndication
		logger.error(String.format("onProcessUnstructuredSSRequestIndication for Dialog=%d and invokeId=%d", procUnstrReqInd.getMAPDialog().getDialogId(),
				procUnstrReqInd.getInvokeId()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.
	 * MAPServiceSupplementaryListener
	 * #onProcessUnstructuredSSResponseIndication(
	 * org.mobicents.protocols.ss7.map
	 * .api.service.supplementary.ProcessUnstructuredSSResponseIndication)
	 */
	@Override
	public void onProcessUnstructuredSSResponseIndication(ProcessUnstructuredSSResponseIndication procUnstrResInd) {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Rx ProcessUnstructuredSSResponseIndication.  USSD String=%s", procUnstrResInd.getUSSDString()));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.
	 * MAPServiceSupplementaryListener
	 * #onUnstructuredSSRequestIndication(org.mobicents
	 * .protocols.ss7.map.api.service
	 * .supplementary.UnstructuredSSRequestIndication)
	 */
	@Override
	public void onUnstructuredSSRequestIndication(UnstructuredSSRequestIndication unstrReqInd) {

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Rx UnstructuredSSRequestIndication. USSD String=%s ", unstrReqInd.getUSSDString()));
		}
		MAPDialogSupplementary mapDialog = unstrReqInd.getMAPDialog();

		try {
			byte ussdDataCodingScheme = 0x0f;

			USSDString ussdString = this.mapProvider.getMapParameterFactory().createUSSDString("1", null);

			AddressString msisdn = this.mapProvider.getMapParameterFactory().createAddressString(AddressNature.international_number, NumberingPlan.ISDN,
					"31628838002");

			mapDialog.addUnstructuredSSResponse(unstrReqInd.getInvokeId(), ussdDataCodingScheme, ussdString);
			mapDialog.send();

		} catch (MAPException e) {
			logger.error(String.format("Error while sending UnstructuredSSResponse for Dialog=%d", mapDialog.getDialogId()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.
	 * MAPServiceSupplementaryListener
	 * #onUnstructuredSSResponseIndication(org.mobicents
	 * .protocols.ss7.map.api.service
	 * .supplementary.UnstructuredSSResponseIndication)
	 */
	@Override
	public void onUnstructuredSSResponseIndication(UnstructuredSSResponseIndication unstrResInd) {
		// This error condition. Client should never receive the
		// UnstructuredSSResponseIndication
		logger.error(String.format("onUnstructuredSSResponseIndication for Dialog=%d and invokeId=%d", unstrResInd.getMAPDialog().getDialogId(),
				unstrResInd.getInvokeId()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.
	 * MAPServiceSupplementaryListener
	 * #onUnstructuredSSNotifyRequestIndication(org
	 * .mobicents.protocols.ss7.map.api
	 * .service.supplementary.UnstructuredSSNotifyRequestIndication)
	 */
	@Override
	public void onUnstructuredSSNotifyRequestIndication(UnstructuredSSNotifyRequestIndication unstrNotifyInd) {
		// This error condition. Client should never receive the
		// UnstructuredSSNotifyRequestIndication
		logger.error(String.format("onUnstructuredSSNotifyRequestIndication for Dialog=%d and invokeId=%d", unstrNotifyInd.getMAPDialog().getDialogId(),
				unstrNotifyInd.getInvokeId()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogDelimiter
	 * (org.mobicents.protocols.ss7.map.api.MAPDialog)
	 */
	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("onDialogDelimiter for DialogId=%d", mapDialog.getDialogId()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogRequest
	 * (org.mobicents.protocols.ss7.map.api.MAPDialog,
	 * org.mobicents.protocols.ss7.map.api.primitives.AddressString,
	 * org.mobicents.protocols.ss7.map.api.primitives.AddressString,
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
	 */
	@Override
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference, MAPExtensionContainer extensionContainer) {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s MAPExtensionContainer=%s",
					mapDialog.getDialogId(), destReference, origReference, extensionContainer));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogAccept(
	 * org.mobicents.protocols.ss7.map.api.MAPDialog,
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
	 */
	@Override
	public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("onDialogAccept for DialogId=%d MAPExtensionContainer=%s", mapDialog.getDialogId(), extensionContainer));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogReject(
	 * org.mobicents.protocols.ss7.map.api.MAPDialog,
	 * org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason,
	 * org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError,
	 * org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName,
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
	 */
	@Override
	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
		logger.error(String.format("onDialogReject for DialogId=%d MAPRefuseReason=%s MAPProviderError=%s ApplicationContextName=%s MAPExtensionContainer=%s",
				mapDialog.getDialogId(), refuseReason, providerError, alternativeApplicationContext, extensionContainer));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogUserAbort
	 * (org.mobicents.protocols.ss7.map.api.MAPDialog,
	 * org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice,
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
	 */
	@Override
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
		logger.error(String.format("onDialogUserAbort for DialogId=%d MAPUserAbortChoice=%s MAPExtensionContainer=%s", mapDialog.getDialogId(), userReason,
				extensionContainer));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogProviderAbort
	 * (org.mobicents.protocols.ss7.map.api.MAPDialog,
	 * org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason,
	 * org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource,
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
	 */
	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource,
			MAPExtensionContainer extensionContainer) {
		logger.error(String.format("onDialogProviderAbort for DialogId=%d MAPAbortProviderReason=%s MAPAbortSource=%s MAPExtensionContainer=%s",
				mapDialog.getDialogId(), abortProviderReason, abortSource, extensionContainer));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogClose(org
	 * .mobicents.protocols.ss7.map.api.MAPDialog)
	 */
	@Override
	public void onDialogClose(MAPDialog mapDialog) {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("DialogClose for Dialog=%d", mapDialog.getDialogId()));
		}

		int ndialogs = nbConcurrentDialogs.decrementAndGet();

		if (ndialogs > MAXCONCURRENTDIALOGS) {
			System.out.println("Concurrent invites = " + ndialogs);
		}
		synchronized (this) {
			if (ndialogs < MAXCONCURRENTDIALOGS / 2)
				this.notify();
		}

		this.endCount++;

		if (this.endCount == NDIALOGS) {
			long current = System.currentTimeMillis();
			float sec = (float) (current - start) / 1000f;

			logger.info("Total time in sec = " + sec);
			logger.info("Thrupt = " + (float) (NDIALOGS / sec));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogNotice(
	 * org.mobicents.protocols.ss7.map.api.MAPDialog,
	 * org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic)
	 */
	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		logger.error(String.format("onDialogNotice for DialogId=%d MAPNoticeProblemDiagnostic=%s ", mapDialog.getDialogId(), noticeProblemDiagnostic));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogResease
	 * (org.mobicents.protocols.ss7.map.api.MAPDialog)
	 */
	@Override
	public void onDialogResease(MAPDialog mapDialog) {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("onDialogResease for DialogId=%d", mapDialog.getDialogId()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogTimeout
	 * (org.mobicents.protocols.ss7.map.api.MAPDialog)
	 */
	@Override
	public void onDialogTimeout(MAPDialog mapDialog) {
		logger.error(String.format("onDialogTimeout for DialogId=%d", mapDialog.getDialogId()));
	}
}
