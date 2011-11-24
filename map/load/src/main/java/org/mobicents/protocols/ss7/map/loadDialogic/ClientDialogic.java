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

package org.mobicents.protocols.ss7.map.loadDialogic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponseIndication;
import org.mobicents.protocols.ss7.map.load.TestHarness;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_DAImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_OAImpl;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.impl.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.ss7.hardware.dialogic.DialogicMtp3UserPart;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ClientDialogic extends TestHarnessDialogic {

	private static Logger logger;


	// MAP
	private MAPStackImpl mapStack;
	private MAPProvider mapProvider;

	// SCCP
	private SccpStackImpl sccpStack;
	private SccpResource sccpResource;

	// Dialogic
	private DialogicMtp3UserPart dialogic;

	// MTP Details
	protected final int CLIENT_SPC = 2;
	protected final int SERVET_SPC = 1;
	protected final int NETWORK_INDICATOR = 2;
	protected final int SERVICE_INIDCATOR = 3; //SCCP
	protected final int SSN = 8;
	
	protected final SccpAddress SCCP_CLIENT_ADDRESS = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, CLIENT_SPC, null, SSN);
	protected final SccpAddress SCCP_SERVER_ADDRESS = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, SERVET_SPC, null, SSN);

	private static int endCount = 0;
	private static int nbConcurrentDialogs = 0;
	
	private static ClientDialogic client;

	private static int MAXCONCURRENTDIALOGS;
	
	protected void initializeStack() throws Exception {

		// Initialize Dialogic first
		this.initDialogic();

		// Initialize SCCP
		this.initSCCP();

		// Initialize MAP
		this.initMAP();
	}

	private void initDialogic() throws Exception {
		
		this.dialogic = new DialogicMtp3UserPart();
		this.dialogic.setSourceModuleId(0x3d);
		this.dialogic.setDestinationModuleId(0x22);
		
		this.dialogic.start();
	}

	private void initSCCP() {
		this.sccpStack = new SccpStackImplWrapper(logger);
		this.sccpStack.setLocalSpc(CLIENT_SPC);
		this.sccpStack.setNi(NETWORK_INDICATOR);
		this.sccpStack.setMtp3UserPart(this.dialogic);

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

		System.out.println("this.mapProvider = " + this.mapProvider);

		this.mapProvider.addMAPDialogListener(this);
		this.mapProvider.getMAPServiceSms().addMAPServiceListener(this);

		this.mapProvider.getMAPServiceSms().acivate();

		this.mapStack.start();

		this.mapStack.getMAPProvider().getMAPServiceSupplementary().acivate();
	}
	
	private void initiateSms() throws MAPException {

		//System.out.println("initiateUSSD");

		// First create Dialog
		MAPDialogSms mapDialog = this.mapProvider.getMAPServiceSms().createNewDialog(
				MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMTRelayContext,
						MAPApplicationContextVersion.version2), SCCP_CLIENT_ADDRESS, null, SCCP_SERVER_ADDRESS, null);

		IMSIImpl imsi = new IMSIImpl("987654321");
		AddressString serviceCentreAddressOA = new AddressStringImpl(AddressNature.national_significant_number, NumberingPlan.ISDN, "123456789");

		SM_RP_DA sm_RP_DA = new SM_RP_DAImpl(imsi);
		SM_RP_OAImpl sm_RP_OA = new SM_RP_OAImpl();
		sm_RP_OA.setServiceCentreAddressOA(serviceCentreAddressOA);
		byte[] sm_RP_UI = new byte[] { 0x2c, 0x09, 0x04, 0x21, 0x43, 0x65, (byte) 0x87, (byte) 0xf9, 0x04, 0x00, 0x11, 0x11, 0x22, 0x71, 0x50, (byte) 0x93,
				0x00, 0x0c, (byte) 0xe7, (byte) 0xf7, (byte) 0x9b, 0x0c, 0x6a, (byte) 0xbf, (byte) 0xe5, (byte) 0xee, (byte) 0xb4, (byte) 0xfb, 0x0c };
		
		mapDialog.addForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, false);

//		nbConcurrentDialogs.incrementAndGet();

		mapDialog.send();
		
		endCount++;
		incrementNbConcurrentDialogs();
		logger.error("-- smsSent");
		
	}

	private static int getNbConcurrentDialogs(){
		synchronized (client) {
			return nbConcurrentDialogs;

		}
	}

	private static void incrementNbConcurrentDialogs(){
		synchronized (client) {
			nbConcurrentDialogs++;
		}
	}

	private static void decrementNbConcurrentDialogs(){
		synchronized (client) {
			nbConcurrentDialogs--;
		}
	}

	public static void main(String args[]) {

//		int noOfCalls = Integer.parseInt(args[0]);
//		int noOfConcurrentCalls = Integer.parseInt(args[1]);
//
//		NDIALOGS = noOfCalls;
//		MAXCONCURRENTDIALOGS = noOfConcurrentCalls;
	
//		InputStream inStreamLog4j = TestHarness.class.getResourceAsStream("/log4j.properties");
//		
//		System.out.println("Input Stream = " + inStreamLog4j);
//		
//		Properties propertiesLog4j = new Properties();
//		try {
//			propertiesLog4j.load(inStreamLog4j);
//			PropertyConfigurator.configure(propertiesLog4j);
//		} catch (IOException e) {
//			e.printStackTrace();
//			BasicConfigurator.configure();
//		}
		
		BasicConfigurator.configure();
	
		logger = Logger.getLogger(ClientDialogic.class);

		logger.error("-- started");

		client = new ClientDialogic();

		int NDIALOGS = 10;
		MAXCONCURRENTDIALOGS = 5;
		
		try {
			logger.error("-- beforeInit");
			
			client.initializeStack();
			
			logger.error("-- afterInit");

			Thread.sleep(10000);

			while (endCount < NDIALOGS) {
				while (getNbConcurrentDialogs() >= MAXCONCURRENTDIALOGS) {

//					logger.warn("nbConcurrentInvite = " + client.nbConcurrentDialogs.intValue()
//							+ " Waiting for max CRCX count to go down!");

					synchronized (client) {
						try {
							client.wait();
						} catch (Exception ex) {
						}
					}
				}

//				if (client.endCount == 0) {
//					client.start = System.currentTimeMillis();
//				}

				client.initiateSms();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(600000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	
	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource,
			MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogClose(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogResease(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogTimeout(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderErrorComponent(MAPDialog mapDialog, Long invokeId, MAPProviderError providerError) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForwardShortMessageIndication(ForwardShortMessageRequestIndication forwSmInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForwardShortMessageRespIndication(ForwardShortMessageResponseIndication forwSmRespInd) {
		// TODO Auto-generated method stub

		logger.error("onForwardShortMessageRespIndication: " + forwSmRespInd.getMAPDialog().getDialogId());

		decrementNbConcurrentDialogs();
		synchronized (this) {
			if (nbConcurrentDialogs < MAXCONCURRENTDIALOGS / 2)
				this.notify();
		}

	}

	@Override
	public void onMoForwardShortMessageIndication(MoForwardShortMessageRequestIndication moForwSmInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoForwardShortMessageRespIndication(MoForwardShortMessageResponseIndication moForwSmRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMtForwardShortMessageIndication(MtForwardShortMessageRequestIndication mtForwSmInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMtForwardShortMessageRespIndication(MtForwardShortMessageResponseIndication mtForwSmRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendRoutingInfoForSMIndication(SendRoutingInfoForSMRequestIndication sendRoutingInfoForSMInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendRoutingInfoForSMRespIndication(SendRoutingInfoForSMResponseIndication sendRoutingInfoForSMRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReportSMDeliveryStatusIndication(ReportSMDeliveryStatusRequestIndication reportSMDeliveryStatusInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReportSMDeliveryStatusRespIndication(ReportSMDeliveryStatusResponseIndication reportSMDeliveryStatusRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInformServiceCentreIndication(InformServiceCentreRequestIndication informServiceCentreInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAlertServiceCentreIndication(AlertServiceCentreRequestIndication alertServiceCentreInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAlertServiceCentreRespIndication(AlertServiceCentreResponseIndication alertServiceCentreInd) {
		// TODO Auto-generated method stub
		
	}

}
