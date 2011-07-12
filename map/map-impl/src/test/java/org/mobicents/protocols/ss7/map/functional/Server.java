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
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
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
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponseIndication;
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
		}
		return status + unexpected;
	}
	
	public void reset() {
		this._S_recievedMAPOpenInfo = false;
		this._S_recievedMAPCloseInfo = false;
		this._S_recievedMAPOpenInfoExtentionContainer = false;
		this._S_recievedProcessUnstructuredSSIndication = false;
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
				mapDialog.setExtentionContainer(MAPFunctionalTest.GetTestExtensionContainer(this.mapServiceFactory));
				mapDialog.send();
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;

		case Action_Dialog_D:
			logger.debug("Sending MAPCloseInfo ");
			try {
				mapDialog.setExtentionContainer(MAPFunctionalTest.GetTestExtensionContainer(this.mapServiceFactory));
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
			if( MAPFunctionalTest.CheckTestExtensionContainer(extensionContainer) )
				_S_recievedMAPOpenInfoExtentionContainer = true;
			
			this._S_recievedMAPOpenInfo = true;
			break;

		case Action_Dialog_B:
			logger.debug("Received MAPOpenInfo ");
			this._S_recievedMAPOpenInfo = true;

			logger.debug("Sending MAPRefuseInfo ");
			try {
				mapDialog.setExtentionContainer(MAPFunctionalTest.GetTestExtensionContainer(this.mapServiceFactory));
				mapDialog.refuse(Reason.invalidDestinationReference);
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
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
			if( MAPFunctionalTest.CheckTestExtensionContainer(extensionContainer) )
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

			MAPErrorMessage msg = this.mapProvider.getMAPErrorMessageFactory().createMessageParameterless(55L);
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
		IMSI imsi = moForwSmInd.getIMSI();
		
		boolean b1 = false;
		if (extensionContainer != null) {
			b1 = MAPFunctionalTest.CheckTestExtensionContainer(extensionContainer);
		}
		
		byte[] sm_RP_UI2 = new byte[] { 21, 22, 23, 24, 25 };
		try {
			d.addMoForwardShortMessageResponse(moForwSmInd.getInvokeId(), sm_RP_UI2,  MAPFunctionalTest.GetTestExtensionContainer(this.mapServiceFactory));
//			d.addMoForwardShortMessageResponse(moForwSmInd.getInvokeId(), null,  null);
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
