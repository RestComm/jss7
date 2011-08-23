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

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponseIndication;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class Server implements MAPDialogListener, MAPServiceSupplementaryListener, MAPServiceSmsListener {

	private static Logger logger = Logger.getLogger(Server.class);

	private MAPFunctionalTest runningTestCase;
	private SccpAddress thisAddress;
	private SccpAddress remoteAddress;

	private MAPStack mapStack;
	private MAPProvider mapProvider;

	private MapServiceFactory mapServiceFactory;

	private boolean _S_recievedMAPOpenInfo, _S_recievedMAPCloseInfo;
	private boolean _S_recievedMAPOpenInfoExtentionContainer;
	private boolean _S_recievedProcessUnstructuredSSIndication;
	private boolean _S_recievedSmsRequestIndication;
	private String unexpected = "";

	private FunctionalTestScenario step;

	Server(MAPStack mapStack, MAPFunctionalTest runningTestCase,
			SccpAddress thisAddress, SccpAddress remoteAddress) {
		super();
		this.mapStack = mapStack;
		this.runningTestCase = runningTestCase;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.mapProvider = this.mapStack.getMAPProvider();

		this.mapServiceFactory = this.mapProvider.getMapServiceFactory();

		this.mapProvider.addMAPDialogListener(this);
		this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
		this.mapProvider.getMAPServiceSms().addMAPServiceListener(this);
		
		this.mapProvider.getMAPServiceSupplementary().acivate();
		this.mapProvider.getMAPServiceSms().acivate();
	}

	public boolean isFinished() {

		switch( this.step ) {
		case Action_Dialog_A:
			return _S_recievedProcessUnstructuredSSIndication
			&& _S_recievedMAPOpenInfo && _S_recievedMAPCloseInfo && _S_recievedMAPOpenInfoExtentionContainer;

		case Action_Dialog_B:
			return _S_recievedMAPOpenInfo;

		case Action_Dialog_C:
			return true;

		case Action_Dialog_D:
			return _S_recievedProcessUnstructuredSSIndication
			&& _S_recievedMAPOpenInfo;

		case Action_Dialog_E:
			return _S_recievedProcessUnstructuredSSIndication
			&& _S_recievedMAPOpenInfo && _S_recievedMAPCloseInfo && _S_recievedMAPOpenInfoExtentionContainer;

		case Action_Dialog_F:
			return true;

		case Action_Sms_AlertServiceCentre:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_SendRoutingInfoForSM:
		case Action_Sms_ReportSMDeliveryStatus:
		case Action_Sms_InformServiceCentre:
			return _S_recievedSmsRequestIndication && _S_recievedMAPOpenInfo;
		}
		
		return false;
	}

	public String getStatus() {
		String status = "Scenario: " + this.step + "\n";

		switch( this.step ) {
		case Action_Dialog_A:
			status += "_S_recievedMAPCloseInfo[" + _S_recievedMAPCloseInfo + "]"
			+ "\n";
			status += "_S_recievedMAPOpenInfo[" + _S_recievedMAPOpenInfo + "]"
			+ "\n";
			status += "_S_recievedProcessUnstructuredSSIndication["
				+ _S_recievedProcessUnstructuredSSIndication + "]" + "\n";
			status += "_S_recievedMAPOpenInfoExtentionContainer["
				+ _S_recievedMAPOpenInfoExtentionContainer + "]" + "\n";
			break;
			
		case Action_Dialog_B:
			status += "_S_recievedMAPOpenInfo[" + _S_recievedMAPOpenInfo + "]"
			+ "\n";
			break;
			
		case Action_Dialog_C:
			break;
			
		case Action_Dialog_D:
			status += "_S_recievedMAPOpenInfo[" + _S_recievedMAPOpenInfo + "]"
			+ "\n";
			status += "_S_recievedProcessUnstructuredSSIndication["
				+ _S_recievedProcessUnstructuredSSIndication + "]" + "\n";
			break;
			
		case Action_Dialog_E:
			status += "_S_recievedMAPCloseInfo[" + _S_recievedMAPCloseInfo + "]"
			+ "\n";
			status += "_S_recievedMAPOpenInfo[" + _S_recievedMAPOpenInfo + "]"
			+ "\n";
			status += "_S_recievedProcessUnstructuredSSIndication["
				+ _S_recievedProcessUnstructuredSSIndication + "]" + "\n";
			status += "_S_recievedMAPOpenInfoExtentionContainer["
				+ _S_recievedMAPOpenInfoExtentionContainer + "]" + "\n";
			break;
			
		case Action_Dialog_F:
			break;

		case Action_Sms_AlertServiceCentre:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_SendRoutingInfoForSM:
		case Action_Sms_ReportSMDeliveryStatus:
		case Action_Sms_InformServiceCentre:
			status += "_S_recievedMAPOpenInfo[" + _S_recievedMAPOpenInfo + "]"
			+ "\n";
			status += "_S_recievedSmsRequestIndication[" + _S_recievedSmsRequestIndication + "]"
			+ "\n";
		}
		return status + unexpected;
	}
	
	public void reset() {
		this._S_recievedMAPOpenInfo = false;
		this._S_recievedMAPCloseInfo = false;
		this._S_recievedMAPOpenInfoExtentionContainer = false;
		this._S_recievedProcessUnstructuredSSIndication = false;
		this._S_recievedSmsRequestIndication = false;
	}
	
	public void setStep (FunctionalTestScenario step) {
		this.step = step;
	}

	/**
	 * MAPDialog Listener's
	 */
	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {
		
		switch( this.step ) {
		case Action_Dialog_A:
			logger.debug("Sending MAPAcceptInfo ");
			try {
				mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
				mapDialog.send();
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;

		case Action_Dialog_D:
			logger.debug("Sending MAPCloseInfo ");
			try {
				mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
				mapDialog.close(false);
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;

		case Action_Dialog_E:
			logger.debug("Sending MAPAcceptInfo ");
			try {
				mapDialog.send();
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;

		case Action_Component_A:
			logger.debug("Sending MAPAcceptInfo ");
			try {
				mapDialog.send();
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;
			
		case Action_Sms_AlertServiceCentre:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_SendRoutingInfoForSM:
		case Action_Sms_ReportSMDeliveryStatus:
		case Action_Sms_InformServiceCentre:
			try {
				mapDialog.send();
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;
		}
	}
	
	@Override
	public void onDialogRequest(MAPDialog mapDialog,
			AddressString destReference, AddressString origReference,
			MAPExtensionContainer extensionContainer) {
		
		switch( this.step ) { 
		case Action_Dialog_A:
		case Action_Dialog_D:
		case Action_Dialog_E:
			if( MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer) )
				_S_recievedMAPOpenInfoExtentionContainer = true;
			
			this._S_recievedMAPOpenInfo = true;
			break;

		case Action_Dialog_B:
			logger.debug("Received MAPOpenInfo ");
			this._S_recievedMAPOpenInfo = true;

			logger.debug("Sending MAPRefuseInfo ");
			try {
				mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
				mapDialog.refuse(Reason.invalidDestinationReference);
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;
			
		case Action_Sms_AlertServiceCentre:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_SendRoutingInfoForSM:
		case Action_Sms_ReportSMDeliveryStatus:
		case Action_Sms_InformServiceCentre:
			this._S_recievedMAPOpenInfo = true;
			break;
		}
	}

	@Override
	public void onDialogAccept(MAPDialog mapDialog,
			MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogReject(MAPDialog mapDialog,
			MAPRefuseReason refuseReason, MAPProviderError providerError,
			ApplicationContextName alternativeApplicationContext,
			MAPExtensionContainer extensionContainer) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onDialogTimeout(MAPDialog mapDialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogUserAbort(MAPDialog mapDialog,
			MAPUserAbortChoice userReason,
			MAPExtensionContainer extensionContainer) {
		logger.debug("Received MAPUserAbortInfo");
		
		switch( this.step ) { 
		case Action_Dialog_E:
			if( MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer) )
				_S_recievedMAPOpenInfoExtentionContainer = true;
			
			if (userReason.isProcedureCancellationReason()
					&& userReason.getProcedureCancellationReason() == ProcedureCancellationReason.handoverCancellation)
			this._S_recievedMAPCloseInfo = true;
			break;
		}
	}

	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog,
			MAPAbortProviderReason abortProviderReason,
			MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
		logger.debug("Received MAPProviderAbortInfo");
	}

	@Override
	public void onDialogClose(MAPDialog mapDialog) {
		
		logger.debug("Received MAPCloseInfo");
		this._S_recievedMAPCloseInfo = true;
	}

	@Override
	public void onDialogNotice(MAPDialog mapDialog,
			MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		// TODO Auto-generated method stub
		
	}

	public void onDialogResease(MAPDialog mapDialog) {
		int i1=0;
		i1 = 1;		
	}

	@Override
	public void onProviderErrorComponent(MAPDialog mapDialog, Long invokeId, MAPProviderError providerError) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInvokeTimeout(MAPDialog mapDialog, Long invoke) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * MAP Service Supplementary Listeners
	 */
	public void onProcessUnstructuredSSIndication(
			ProcessUnstructuredSSIndication procUnstrInd) {
		
		switch( this.step ) {
		case Action_Dialog_A:
		case Action_Dialog_D:
		case Action_Dialog_E:
			String ussdString = procUnstrInd.getUSSDString().getString();
			AddressString msisdn = procUnstrInd.getMSISDNAddressString();
			logger.debug("Received ProcessUnstructuredSSIndication " + ussdString
					+ " from MSISDN " + msisdn.getAddress());

			if (!ussdString.equals(MAPFunctionalTest.USSD_STRING)) {
				unexpected += " Received USSDString " + ussdString
				+ ". But was expected " + MAPFunctionalTest.USSD_STRING;
			} else {
				this._S_recievedProcessUnstructuredSSIndication = true;

				MAPDialogSupplementary mapDialog = procUnstrInd.getMAPDialog();
				Long invokeId = procUnstrInd.getInvokeId();

				USSDString ussdStringObj = this.mapServiceFactory
				.createUSSDString(MAPFunctionalTest.USSD_MENU);

				try {
					mapDialog.addUnstructuredSSRequest((byte) 0x0F, ussdStringObj);

				} catch (MAPException e) {
					logger.error(e);
					throw new RuntimeException(e);
				}

				logger.debug("InvokeId =  " + invokeId);
			}
			break;
			
		case Action_Component_A: {
			MAPDialogSupplementary mapDialog = procUnstrInd.getMAPDialog();
			Long invokeId = procUnstrInd.getInvokeId();

			MAPErrorMessage msg = this.mapProvider.getMAPErrorMessageFactory().createMAPErrorMessageParameterless(55L);
			try {
				mapDialog.sendErrorComponent(invokeId, msg);
			} catch (MAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
			break;

		}

	}

	public void onUnstructuredSSIndication(UnstructuredSSIndication unstrInd) {
		// TODO Auto-generated method stub

	}

	/**
	 * MAP Service Sms Listeners
	 */
	@Override
	public void onMoForwardShortMessageIndication(MoForwardShortMessageRequestIndication moForwSmInd) {

		MAPDialogSms d = moForwSmInd.getMAPDialog();
		
		SM_RP_DA sm_RP_DA = moForwSmInd.getSM_RP_DA();
		SM_RP_OA sm_RP_OA = moForwSmInd.getSM_RP_OA();
		byte[] sm_RP_UI = moForwSmInd.getSM_RP_UI();
		MAPExtensionContainer extensionContainer = moForwSmInd.getExtensionContainer();
		IMSI imsi2 = moForwSmInd.getIMSI();

		Assert.assertNotNull(sm_RP_DA);
		Assert.assertNotNull(sm_RP_DA.getIMSI());
		Assert.assertEquals((long) (sm_RP_DA.getIMSI().getMCC()), 250);
		Assert.assertEquals((long) (sm_RP_DA.getIMSI().getMNC()), 99);
		Assert.assertEquals(sm_RP_DA.getIMSI().getMSIN(), "1357999");
		Assert.assertNotNull(sm_RP_OA);
		Assert.assertNotNull(sm_RP_OA.getMsisdn());
		Assert.assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
		Assert.assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
		Assert.assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
		Assert.assertNotNull(sm_RP_UI);
		Assert.assertTrue(Arrays.equals(sm_RP_UI, new byte[] { 21, 22, 23, 24, 25 }));
		Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
		Assert.assertNotNull(imsi2);
		Assert.assertEquals((long) (imsi2.getMCC()), 250);
		Assert.assertEquals((long) (imsi2.getMNC()), 7);
		Assert.assertEquals(imsi2.getMSIN(), "123456789");

		this._S_recievedSmsRequestIndication = true;
		
		byte[] sm_RP_UI2 = new byte[] { 21, 22, 23, 24, 25 };
		try {
			d.addMoForwardShortMessageResponse(moForwSmInd.getInvokeId(), sm_RP_UI2, MAPExtensionContainerTest.GetTestExtensionContainer());
		} catch (MAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onMoForwardShortMessageRespIndication(MoForwardShortMessageResponseIndication moForwSmRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMtForwardShortMessageIndication(MtForwardShortMessageRequestIndication mtForwSmInd) {

		MAPDialogSms d = mtForwSmInd.getMAPDialog();
		
		SM_RP_DA sm_RP_DA = mtForwSmInd.getSM_RP_DA();
		SM_RP_OA sm_RP_OA = mtForwSmInd.getSM_RP_OA();
		byte[] sm_RP_UI = mtForwSmInd.getSM_RP_UI();
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
		Assert.assertTrue(Arrays.equals(sm_RP_UI, new byte[] { 21, 22, 23, 24, 25 }));
		Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
		Assert.assertTrue(moreMessagesToSend);

		this._S_recievedSmsRequestIndication = true;
		
		byte[] sm_RP_UI2 = new byte[] { 21, 22, 23, 24, 25 };
		try {
			d.addMtForwardShortMessageResponse(mtForwSmInd.getInvokeId(), sm_RP_UI2, MAPExtensionContainerTest.GetTestExtensionContainer());
		} catch (MAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onMtForwardShortMessageRespIndication(MtForwardShortMessageResponseIndication mtForwSmRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendRoutingInfoForSMIndication(SendRoutingInfoForSMRequestIndication sendRoutingInfoForSMInd) {
		
		MAPDialogSms d = sendRoutingInfoForSMInd.getMAPDialog();
		
		ISDNAddressString msisdn = sendRoutingInfoForSMInd.getMsisdn();
		Boolean sm_RP_PRI = sendRoutingInfoForSMInd.getSm_RP_PRI();
		AddressString sca = sendRoutingInfoForSMInd.getServiceCentreAddress();
		MAPExtensionContainer extensionContainer = sendRoutingInfoForSMInd.getExtensionContainer();
		Boolean gprsSupportIndicator = sendRoutingInfoForSMInd.getGprsSupportIndicator();
		SM_RP_MTI sM_RP_MTI = sendRoutingInfoForSMInd.getSM_RP_MTI();
		byte[] sM_RP_SMEA = sendRoutingInfoForSMInd.getSM_RP_SMEA();

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
		Assert.assertTrue(Arrays.equals(sM_RP_SMEA, new byte[] { 90, 91 }));

		this._S_recievedSmsRequestIndication = true;
		
		IMSI imsi = this.mapServiceFactory.createIMSI(250L, 99L, "777000");
		ISDNAddressString networkNodeNumber = this.mapServiceFactory.createISDNAddressString(AddressNature.network_specific_number, NumberingPlan.national,
				"111000111");
		LMSI lmsi = this.mapServiceFactory.createLMSI(new byte[] { 75, 74, 73, 72 });
		AdditionalNumberType additionalNumberType = AdditionalNumberType.sgsn;
		ISDNAddressString additionalNumber = this.mapServiceFactory.createISDNAddressString(AddressNature.subscriber_number, NumberingPlan.private_plan,
				"000111000");
		LocationInfoWithLMSI locationInfoWithLMSI = this.mapServiceFactory.createLocationInfoWithLMSI(networkNodeNumber, lmsi,
				MAPExtensionContainerTest.GetTestExtensionContainer(), additionalNumberType, additionalNumber);
		try {
			d.addSendRoutingInfoForSMResponse(sendRoutingInfoForSMInd.getInvokeId(), imsi, locationInfoWithLMSI,
					MAPExtensionContainerTest.GetTestExtensionContainer());
		} catch (MAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onSendRoutingInfoForSMRespIndication(SendRoutingInfoForSMResponseIndication sendRoutingInfoForSMRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReportSMDeliveryStatusIndication(ReportSMDeliveryStatusRequestIndication reportSMDeliveryStatusInd) {
		
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

		this._S_recievedSmsRequestIndication = true;
		
		ISDNAddressString storedMSISDN = this.mapServiceFactory.createISDNAddressString(AddressNature.network_specific_number, NumberingPlan.national,
				"111000111");

		try {
			d.addReportSMDeliveryStatusResponse(reportSMDeliveryStatusInd.getInvokeId(), storedMSISDN,
					MAPExtensionContainerTest.GetTestExtensionContainer());
		} catch (MAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onReportSMDeliveryStatusRespIndication(ReportSMDeliveryStatusResponseIndication reportSMDeliveryStatusRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInformServiceCentreIndication(InformServiceCentreRequestIndication informServiceCentreInd) {
		
		MAPExtensionContainer extensionContainer = informServiceCentreInd.getExtensionContainer();
		ISDNAddressString storedMSISDN = informServiceCentreInd.getStoredMSISDN();
		MWStatus mwStatus = informServiceCentreInd.getMwStatus();
		int absentSubscriberDiagnosticSM = informServiceCentreInd.getAbsentSubscriberDiagnosticSM();
		int additionalAbsentSubscriberDiagnosticSM = informServiceCentreInd.getAdditionalAbsentSubscriberDiagnosticSM();

		Assert.assertNotNull(storedMSISDN);
		Assert.assertEquals(storedMSISDN.getAddressNature(), AddressNature.international_number);
		Assert.assertEquals(storedMSISDN.getNumberingPlan(), NumberingPlan.ISDN);
		Assert.assertEquals(storedMSISDN.getAddress(), "111222333");
		Assert.assertNotNull(mwStatus);
		Assert.assertFalse(mwStatus.getScAddressNotIncluded());
		Assert.assertTrue(mwStatus.getMnrfSet());
		Assert.assertFalse(mwStatus.getMcefSet());
		Assert.assertTrue(mwStatus.getMnrgSet());
		Assert.assertNotNull(absentSubscriberDiagnosticSM);
		Assert.assertEquals((int) absentSubscriberDiagnosticSM, 555);
		Assert.assertNotNull(additionalAbsentSubscriberDiagnosticSM);
		Assert.assertEquals((int) additionalAbsentSubscriberDiagnosticSM, 444);
		Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

		this._S_recievedSmsRequestIndication = true;
	}

	@Override
	public void onAlertServiceCentreIndication(AlertServiceCentreRequestIndication alertServiceCentreInd) {

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

		this._S_recievedSmsRequestIndication = true;

		try {
			d.addAlertServiceCentreResponse(alertServiceCentreInd.getInvokeId());
		} catch (MAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onAlertServiceCentreRespIndication(AlertServiceCentreResponseIndication alertServiceCentreInd) {
		// TODO Auto-generated method stub
		
	}
}
