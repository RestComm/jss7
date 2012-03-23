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

import org.apache.log4j.Logger;
import org.mobicents.protocols.sctp.ManagementImpl;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagement;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponseIndication;
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
public class Server extends TestHarness {

	private static Logger logger = Logger.getLogger(Server.class);

	// MAP
	private MAPStackImpl mapStack;
	private MAPProvider mapProvider;

	// SCCP
	private SccpStackImpl sccpStack;
	private SccpResource sccpResource;

	// M3UA
	private M3UAManagement serverM3UAMgmt;

	// SCTP
	private ManagementImpl sctpManagement;

	protected void initializeStack() throws Exception {

		this.initSCTP();

		// Initialize M3UA first
		this.initM3UA();

		// Initialize SCCP
		this.initSCCP();

		// Initialize MAP
		this.initMAP();

		// 7. Start ASP
		serverM3UAMgmt.startAsp("RASP1");
	}

	private void initSCTP() throws Exception {
		this.sctpManagement = new ManagementImpl("Server");
		this.sctpManagement.setSingleThread(true);
		this.sctpManagement.setConnectDelay(10000);
		this.sctpManagement.start();

		// 1. Create SCTP Server
		sctpManagement.addServer(SERVER_NAME, SERVER_IP, SERVER_PORT);

		// 2. Create SCTP Server Association
		sctpManagement.addServerAssociation(CLIENT_IP, CLIENT_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME);

		// 3. Start Server
		sctpManagement.startServer(SERVER_NAME);

	}

	private void initM3UA() throws Exception {
		this.serverM3UAMgmt = new M3UAManagement("Server");
		this.serverM3UAMgmt.setTransportManagement(this.sctpManagement);
		this.serverM3UAMgmt.start();

		// Step 1 : Create App Server

		RoutingContext rc = factory.createRoutingContext(new long[] { 100l });
		TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
		As as = this.serverM3UAMgmt.createAs("RAS1", Functionality.SGW, ExchangeType.SE, IPSPType.CLIENT, rc,
				trafficModeType, null);

		// Step 2 : Create ASP
		AspFactory aspFactor = this.serverM3UAMgmt.createAspFactory("RASP1", SERVER_ASSOCIATION_NAME);

		// Step3 : Assign ASP to AS
		Asp asp = this.serverM3UAMgmt.assignAspToAs("RAS1", "RASP1");

		// Step 4: Add Route. Remote point code is 2
		this.serverM3UAMgmt.addRoute(CLIENT_SPC, -1, -1, "RAS1");
	}

	private void initSCCP() {
		this.sccpStack = new SccpStackImpl("MapLoadServerSccpStack");
		this.sccpStack.setLocalSpc(SERVET_SPC);
		this.sccpStack.setNi(NETWORK_INDICATOR);
		this.sccpStack.setMtp3UserPart(this.serverM3UAMgmt);

		this.sccpStack.start();

		// Clean orevious resources if present
		// this.sccpResource = new SccpResource();
		// this.sccpResource.start();
		//
		// this.sccpStack.setSccpResource(this.sccpResource);

		RemoteSignalingPointCode rspc = new RemoteSignalingPointCode(CLIENT_SPC, 0, 0);
		RemoteSubSystem rss = new RemoteSubSystem(CLIENT_SPC, SSN, 0);
		this.sccpStack.getSccpResource().addRemoteSpc(0, rspc);
		this.sccpStack.getSccpResource().addRemoteSsn(0, rss);

	}

	private void initMAP() {
		this.mapStack = new MAPStackImpl(this.sccpStack.getSccpProvider(), SSN);
		this.mapProvider = this.mapStack.getMAPProvider();

		this.mapProvider.addMAPDialogListener(this);
		this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);

		this.mapProvider.getMAPServiceSupplementary().acivate();

		this.mapStack.start();
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
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			MAPExtensionContainer extensionContainer) {
		if (logger.isDebugEnabled()) {
			logger.debug(String
					.format("onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s MAPExtensionContainer=%s",
							mapDialog.getDialogId(), destReference, origReference, extensionContainer));
		}
	}

	@Override
	public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			IMSI imsi, AddressString vlr) {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s ",
					mapDialog.getDialogId(), destReference, origReference));
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
			logger.debug(String.format("onDialogAccept for DialogId=%d MAPExtensionContainer=%s",
					mapDialog.getDialogId(), extensionContainer));
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
		logger.error(String
				.format("onDialogReject for DialogId=%d MAPRefuseReason=%s MAPProviderError=%s ApplicationContextName=%s MAPExtensionContainer=%s",
						mapDialog.getDialogId(), refuseReason, providerError, alternativeApplicationContext,
						extensionContainer));
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
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason,
			MAPExtensionContainer extensionContainer) {
		logger.error(String.format("onDialogUserAbort for DialogId=%d MAPUserAbortChoice=%s MAPExtensionContainer=%s",
				mapDialog.getDialogId(), userReason, extensionContainer));
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
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
			MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
		logger.error(String
				.format("onDialogProviderAbort for DialogId=%d MAPAbortProviderReason=%s MAPAbortSource=%s MAPExtensionContainer=%s",
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
		logger.error(String.format("onDialogNotice for DialogId=%d MAPNoticeProblemDiagnostic=%s ",
				mapDialog.getDialogId(), noticeProblemDiagnostic));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogResease
	 * (org.mobicents.protocols.ss7.map.api.MAPDialog)
	 */
	@Override
	public void onDialogRelease(MAPDialog mapDialog) {
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
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("onProcessUnstructuredSSRequestIndication for DialogId=%d", procUnstrReqInd
					.getMAPDialog().getDialogId()));
		}
		try {
			long invokeId = procUnstrReqInd.getInvokeId();

			USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory().createUSSDString(
					"USSD String : Hello World <CR> 1. Balance <CR> 2. Texts Remaining");
			byte ussdDataCodingScheme = (byte) 0x0F;
			MAPDialogSupplementary dialog = procUnstrReqInd.getMAPDialog();

			dialog.setUserObject(invokeId);

			ISDNAddressString msisdn = this.mapProvider.getMAPParameterFactory().createISDNAddressString(
					AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

			dialog.addUnstructuredSSRequest(ussdDataCodingScheme, ussdStrObj, null, msisdn);
			dialog.send();
		} catch (MAPException e) {
			logger.error("Error while sending UnstructuredSSRequest ", e);
		}
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
		// Server shouldn't be getting ProcessUnstructuredSSResponseIndication
		logger.error(String.format("onProcessUnstructuredSSResponseIndication for Dialog=%d and invokeId=%d",
				procUnstrResInd.getMAPDialog().getDialogId(), procUnstrResInd.getInvokeId()));
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
		// Server shouldn't be getting UnstructuredSSRequestIndication
		logger.error(String.format("onUnstructuredSSRequestIndication for Dialog=%d and invokeId=%d", unstrReqInd
				.getMAPDialog().getDialogId(), unstrReqInd.getInvokeId()));
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
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("onUnstructuredSSResponseIndication for DialogId=%d", unstrResInd.getMAPDialog()
					.getDialogId()));
		}
		try {
			USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory().createUSSDString("Your balance is 500");
			byte ussdDataCodingScheme = (byte) 0x0F;
			MAPDialogSupplementary dialog = unstrResInd.getMAPDialog();

			AddressString msisdn = this.mapProvider.getMAPParameterFactory().createAddressString(
					AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

			dialog.addProcessUnstructuredSSResponse(((Long) dialog.getUserObject()).longValue(), ussdDataCodingScheme,
					ussdStrObj);
			dialog.close(false);
		} catch (MAPException e) {
			logger.error("Error while sending UnstructuredSSRequest ", e);
		}
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
		logger.error(String.format("onUnstructuredSSNotifyRequestIndication for Dialog=%d and invokeId=%d",
				unstrNotifyInd.getMAPDialog().getDialogId(), unstrNotifyInd.getInvokeId()));
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
		logger.error(String.format("onErrorComponent for Dialog=%d and invokeId=%d MAPErrorMessage=%s",
				mapDialog.getDialogId(), invokeId, mapErrorMessage));
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
		logger.error(String.format("onProviderErrorComponent for Dialog=%d and invokeId=%d MAPProviderError=%s",
				mapDialog.getDialogId(), invokeId, providerError));
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
		logger.error(String.format("onRejectComponent for Dialog=%d and invokeId=%d Problem=%s",
				mapDialog.getDialogId(), invokeId, problem));
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

	public static void main(String args[]) {
		final Server server = new Server();
		try {
			server.initializeStack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUnstructuredSSNotifyResponseIndication(UnstructuredSSNotifyResponseIndication arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMAPMessage(MAPMessage arg0) {
		//We ignore this
	}

}
