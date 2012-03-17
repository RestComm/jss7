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

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
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
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponseIndication;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSResponseIndicationImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Return;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

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

	private MAPParameterFactory MAPParameterFactory;

	private boolean _S_recievedMAPOpenInfo, _S_recievedMAPCloseInfo, _S_eriStyle;
	private boolean _S_recievedMAPAbort;
	private boolean _S_recievedMAPOpenInfoExtentionContainer;
	private boolean _S_recievedProcessUnstructuredSSIndication;
	private boolean _S_recievedSmsRequestIndication;
	private String unexpected = "";
	private int dialogStep;
	private long savedInvokeId;

	private FunctionalTestScenario step;

	Server(MAPStack mapStack, MAPFunctionalTest runningTestCase,
			SccpAddress thisAddress, SccpAddress remoteAddress) {
		super();
		this.mapStack = mapStack;
		this.runningTestCase = runningTestCase;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.mapProvider = this.mapStack.getMAPProvider();

		this.MAPParameterFactory = this.mapProvider.getMAPParameterFactory();

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

		case Action_Dialog_Eri:
			return _S_recievedProcessUnstructuredSSIndication && _S_recievedMAPOpenInfo && _S_eriStyle;

		case Action_Sms_AlertServiceCentre:
		case Action_Sms_ForwardSM:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_SendRoutingInfoForSM:
		case Action_Sms_ReportSMDeliveryStatus:
		case Action_TestMsgLength_A:
		case Action_TestMsgLength_B:
			return _S_recievedSmsRequestIndication && _S_recievedMAPOpenInfo;

		case Action_Component_A:
		case Action_Component_B:
		case Action_Component_E:
		case Action_Component_F:
		case Action_Component_G:
			return _S_recievedProcessUnstructuredSSIndication && _S_recievedMAPOpenInfo;
		case Action_Component_D:
			return _S_recievedProcessUnstructuredSSIndication && _S_recievedMAPOpenInfo;
			
		case Action_V1_A:
		case Action_V1_B:
			return _S_recievedSmsRequestIndication && _S_recievedMAPOpenInfo;
		case Action_V1_C:
		case Action_V1_D:
			return true;
		case Action_V1_E:
			return _S_recievedSmsRequestIndication && _S_recievedMAPOpenInfo && _S_recievedMAPAbort;
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

		case Action_Dialog_Eri:
			status += "_S_recievedMAPOpenInfo[" + _S_recievedMAPOpenInfo + "]" + "\n";
			status += "_S_recievedProcessUnstructuredSSIndication[" + _S_recievedProcessUnstructuredSSIndication + "]" + "\n";
			status += "_S_eriStyle[" + _S_eriStyle + "]" + "\n";
			break;

		case Action_Sms_AlertServiceCentre:
		case Action_Sms_ForwardSM:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_SendRoutingInfoForSM:
		case Action_Sms_ReportSMDeliveryStatus:
		case Action_TestMsgLength_A:
		case Action_TestMsgLength_B:
			status += "_S_recievedMAPOpenInfo[" + _S_recievedMAPOpenInfo + "]"
			+ "\n";
			status += "_S_recievedSmsRequestIndication[" + _S_recievedSmsRequestIndication + "]"
			+ "\n";
			break;

		case Action_Component_A:
		case Action_Component_B:
		case Action_Component_E:
		case Action_Component_F:
		case Action_Component_G:
			status += "_S_recievedProcessUnstructuredSSIndication[" + _S_recievedProcessUnstructuredSSIndication + "]"
			+ "\n";
			status += "_S_recievedMAPOpenInfo[" + _S_recievedMAPOpenInfo + "]"
			+ "\n";
			break;
		case Action_Component_D:
			status += "_S_recievedProcessUnstructuredSSIndication[" + _S_recievedProcessUnstructuredSSIndication + "]"
			+ "\n";
			status += "_S_recievedMAPOpenInfo[" + _S_recievedMAPOpenInfo + "]"
			+ "\n";
			break;
			
		case Action_V1_A:
		case Action_V1_B:
			status += "_S_recievedMAPOpenInfo[" + _S_recievedMAPOpenInfo + "]"
			+ "\n";
			status += "_S_recievedSmsRequestIndication[" + _S_recievedSmsRequestIndication + "]"
			+ "\n";
			break;
		case Action_V1_C:
		case Action_V1_D:
			status += "OK" + "\n";
			break;
		case Action_V1_E:
			status += "_S_recievedMAPOpenInfo[" + _S_recievedMAPOpenInfo + "]"
			+ "\n";
			status += "_S_recievedSmsRequestIndication[" + _S_recievedSmsRequestIndication + "]"
			+ "\n";
			status += "_S_recievedMAPAbort[" + _S_recievedMAPAbort + "]"
			+ "\n";
			break;
		}

		return status + unexpected;
	}
	
	public void reset() {
		this._S_recievedMAPOpenInfo = false;
		this._S_recievedMAPCloseInfo = false;
		this._S_recievedMAPOpenInfoExtentionContainer = false;
		this._S_recievedProcessUnstructuredSSIndication = false;
		this._S_recievedSmsRequestIndication = false;
		this._S_recievedMAPAbort = false;
		this._S_eriStyle = false;
		this.dialogStep = 0;
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

		case Action_Dialog_Eri:
			try {
				mapDialog.close(false);
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;

		case Action_Component_A:
		case Action_Component_B:
		case Action_Component_E:
		case Action_Component_G:
			try {
				mapDialog.close(false);
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;
		case Action_Component_F:
			try {
				mapDialog.close(true);
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;
		case Action_Component_D:
			try {
				this.dialogStep++;
				if (this.dialogStep == 1)
					mapDialog.send();
				else {
					USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory().createUSSDString("Your balance is 500");
					byte ussdDataCodingScheme = (byte) 0x0F;
					((MAPDialogSupplementary) mapDialog).addProcessUnstructuredSSResponse(this.savedInvokeId, ussdDataCodingScheme, ussdStrObj);
					
					mapDialog.close(false);
				}
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;
			
		case Action_Sms_AlertServiceCentre:
		case Action_Sms_ForwardSM:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_SendRoutingInfoForSM:
		case Action_Sms_ReportSMDeliveryStatus:
		case Action_V1_A:
			try {
				mapDialog.close(false);
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;
			
		case Action_V1_B:
			mapDialog.release();
			break;
			
		case Action_V1_E:
			try {
				mapDialog.send();
			} catch (MAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case Action_TestMsgLength_A:
		case Action_TestMsgLength_B:
			try {
				if (!_S_recievedSmsRequestIndication)
					mapDialog.send();
				else
					mapDialog.close(false);
			} catch (MAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		case Action_Sms_ForwardSM:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_SendRoutingInfoForSM:
		case Action_Sms_ReportSMDeliveryStatus:
		case Action_TestMsgLength_A:
		case Action_TestMsgLength_B:
		case Action_V1_A:
		case Action_V1_B:
		case Action_V1_E:
		case Action_Component_A:
		case Action_Component_B:
		case Action_Component_D:
		case Action_Component_E:
		case Action_Component_F:
		case Action_Component_G:
			this._S_recievedMAPOpenInfo = true;
			break;
		}
	}

	@Override
	public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference, IMSI eriImsi, AddressString eriVlrNo) {

		switch (this.step) {

		case Action_Dialog_Eri:
			this._S_recievedMAPOpenInfo = true;
			
			if (eriImsi != null && eriImsi.getData().equals("12345") && eriVlrNo != null && eriVlrNo.getAddress().equals("556677")
					&& destReference != null && destReference.getAddress().endsWith("888777") && origReference != null
					&& origReference.getAddress().endsWith("1115550000")) {
				_S_eriStyle = true;
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
		_S_recievedMAPAbort = true;
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

	public void onDialogRelease(MAPDialog mapDialog) {
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
	public void onProcessUnstructuredSSRequestIndication(ProcessUnstructuredSSRequestIndication procUnstrInd) {
		
		switch( this.step ) {
		case Action_Dialog_A:
		case Action_Dialog_D:
		case Action_Dialog_E: {
			String ussdString = procUnstrInd.getUSSDString().getString();
			AddressString msisdn = procUnstrInd.getMSISDNAddressString();
			logger.debug("Received ProcessUnstructuredSSIndication " + ussdString + " from MSISDN " + msisdn.getAddress());

			if (!ussdString.equals(MAPFunctionalTest.USSD_STRING)) {
				unexpected += " Received USSDString " + ussdString + ". But was expected " + MAPFunctionalTest.USSD_STRING;
			} else {
				this._S_recievedProcessUnstructuredSSIndication = true;

				MAPDialogSupplementary mapDialog = procUnstrInd.getMAPDialog();
				Long invokeId = procUnstrInd.getInvokeId();

				USSDString ussdStringObj = this.MAPParameterFactory.createUSSDString(MAPFunctionalTest.USSD_MENU);

				try {
					mapDialog.addUnstructuredSSRequest((byte) 0x0F, ussdStringObj, null, null);

				} catch (MAPException e) {
					logger.error(e);
					throw new RuntimeException(e);
				}

				logger.debug("InvokeId =  " + invokeId);
			}
		}
			break;

		case Action_Dialog_Eri: {
			String ussdString = procUnstrInd.getUSSDString().getString();
			AddressString msisdn = procUnstrInd.getMSISDNAddressString();
			logger.debug("Received ProcessUnstructuredSSIndication " + ussdString + " from MSISDN " + msisdn.getAddress());

			if (!ussdString.equals(MAPFunctionalTest.USSD_STRING)) {
				unexpected += " Received USSDString " + ussdString + ". But was expected " + MAPFunctionalTest.USSD_STRING;
			} else {
				this._S_recievedProcessUnstructuredSSIndication = true;
			}
		}
			break;
			
		case Action_Component_A:
		case Action_Component_B:
		case Action_Component_D:
		case Action_Component_E:
		case Action_Component_F:
		case Action_Component_G: {
			MAPDialogSupplementary mapDialog = procUnstrInd.getMAPDialog();
			Long invokeId = procUnstrInd.getInvokeId();
			this._S_recievedProcessUnstructuredSSIndication = true;

			switch (this.step) {
			case Action_Component_A:
			case Action_Component_F: {
				MAPErrorMessage msg = this.mapProvider.getMAPErrorMessageFactory().createMAPErrorMessageSystemFailure(2, null, null, null);
				try {
					mapDialog.sendErrorComponent(invokeId, msg);
				} catch (MAPException e) {
					e.printStackTrace();
				}
			}
				break;

			case Action_Component_B: {
				MAPErrorMessage msg = this.mapProvider.getMAPErrorMessageFactory().createMAPErrorMessageSMDeliveryFailure(
						SMEnumeratedDeliveryFailureCause.scCongestion, null, null);
				try {
					mapDialog.sendErrorComponent(invokeId, msg);
				} catch (MAPException e) {
					e.printStackTrace();
				}
			}
				break;

			case Action_Component_D: {
				ReturnResult returnResult = ((MAPProviderImpl)this.mapProvider).getTCAPProvider().getComponentPrimitiveFactory().createTCResultRequest();

				this.savedInvokeId = invokeId;
				returnResult.setInvokeId(invokeId);

				// Operation Code
				OperationCode oc = TcapFactory.createOperationCode();
				oc.setLocalOperationCode((long) MAPOperationCode.processUnstructuredSS_Request);
				returnResult.setOperationCode(oc);

				USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory().createUSSDString("Your balance is 500");
				byte ussdDataCodingScheme = (byte) 0x0F;
				ProcessUnstructuredSSResponseIndicationImpl req = new ProcessUnstructuredSSResponseIndicationImpl(ussdDataCodingScheme, ussdStrObj);
				AsnOutputStream aos = new AsnOutputStream();
				try {
					req.encodeData(aos);
					
					Parameter p = ((MAPProviderImpl)this.mapProvider).getTCAPProvider().getComponentPrimitiveFactory().createParameter();
					p.setTagClass(req.getTagClass());
					p.setPrimitive(req.getIsPrimitive());
					p.setTag(req.getTag());
					p.setData(aos.toByteArray());
					returnResult.setParameter(p);

					mapDialog.sendReturnResultComponent(returnResult);
				} catch (MAPException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
				break;

			case Action_Component_E: {
				Problem problem = this.mapProvider.getMAPParameterFactory().createProblemInvoke(InvokeProblemType.DuplicateInvokeID);
				try {
					mapDialog.sendRejectComponent(invokeId, problem);
				} catch (MAPException e) {
					e.printStackTrace();
				}
			}
				break;

			case Action_Component_G: {
				Problem problem = this.mapProvider.getMAPParameterFactory().createProblemGeneral(GeneralProblemType.MistypedComponent);
				try {
					mapDialog.sendRejectComponent(null, problem);
				} catch (MAPException e) {
					e.printStackTrace();
				}
			}
				break;
			}
		}

			break;

		}

	}

	@Override
	public void onProcessUnstructuredSSResponseIndication(ProcessUnstructuredSSResponseIndication procUnstrResInd){
		
	}

	@Override
	public void onUnstructuredSSRequestIndication(UnstructuredSSRequestIndication unstrReqInd){
		
	}

	@Override
	public void onUnstructuredSSResponseIndication(UnstructuredSSResponseIndication unstrResInd){
		
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
		
		if (this.step == FunctionalTestScenario.Action_TestMsgLength_A || this.step == FunctionalTestScenario.Action_TestMsgLength_B) {
			Assert.assertNotNull(sm_RP_DA);
			Assert.assertNotNull(sm_RP_DA.getIMSI());
//			Assert.assertEquals((long) (sm_RP_DA.getIMSI().getMCC()), 250);
//			Assert.assertEquals((long) (sm_RP_DA.getIMSI().getMNC()), 99);
			Assert.assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
			Assert.assertNotNull(sm_RP_OA);
			Assert.assertNotNull(sm_RP_OA.getMsisdn());
			Assert.assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
			Assert.assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
			Assert.assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
			Assert.assertNotNull(sm_RP_UI);
			byte[] testArr;
			if (this.step == FunctionalTestScenario.Action_TestMsgLength_A) {
				testArr = new byte[20];
				Arrays.fill(testArr, (byte) 11);
			} else {
				testArr = new byte[170];
				Arrays.fill(testArr, (byte) 22);
			}
			
			Assert.assertTrue(Arrays.equals(sm_RP_UI, testArr));
			Assert.assertNull(extensionContainer);
			Assert.assertNotNull(imsi2);
//			Assert.assertEquals((long) (imsi2.getMCC()), 250);
//			Assert.assertEquals((long) (imsi2.getMNC()), 7);
			Assert.assertEquals(imsi2.getData(), "25007123456789");

		} else {
			Assert.assertNotNull(sm_RP_DA);
			Assert.assertNotNull(sm_RP_DA.getIMSI());
//			Assert.assertEquals((long) (sm_RP_DA.getIMSI().getMCC()), 250);
//			Assert.assertEquals((long) (sm_RP_DA.getIMSI().getMNC()), 99);
			Assert.assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
			Assert.assertNotNull(sm_RP_OA);
			Assert.assertNotNull(sm_RP_OA.getMsisdn());
			Assert.assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
			Assert.assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
			Assert.assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
			Assert.assertNotNull(sm_RP_UI);
			Assert.assertTrue(Arrays.equals(sm_RP_UI, new byte[] { 21, 22, 23, 24, 25 }));
			Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
			Assert.assertNotNull(imsi2);
//			Assert.assertEquals((long) (imsi2.getMCC()), 250);
//			Assert.assertEquals((long) (imsi2.getMNC()), 7);
			Assert.assertEquals(imsi2.getData(), "25007123456789");
		}

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
		
		IMSI imsi = this.MAPParameterFactory.createIMSI("25099777000");
		ISDNAddressString networkNodeNumber = this.MAPParameterFactory.createISDNAddressString(AddressNature.network_specific_number, NumberingPlan.national,
				"111000111");
		LMSI lmsi = this.MAPParameterFactory.createLMSI(new byte[] { 75, 74, 73, 72 });
		AdditionalNumberType additionalNumberType = AdditionalNumberType.sgsn;
		ISDNAddressString additionalNumber = this.MAPParameterFactory.createISDNAddressString(AddressNature.subscriber_number, NumberingPlan.private_plan,
				"000111000");
		LocationInfoWithLMSI locationInfoWithLMSI = this.MAPParameterFactory.createLocationInfoWithLMSI(networkNodeNumber, lmsi,
				MAPExtensionContainerTest.GetTestExtensionContainer(), additionalNumberType, additionalNumber);


		ISDNAddressString storedMSISDN = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		MWStatus mwStatus = this.MAPParameterFactory.createMWStatus(false, true, false, true);
		Integer absentSubscriberDiagnosticSM = 555;
		Integer additionalAbsentSubscriberDiagnosticSM = 444;
		
		try {
			d.addSendRoutingInfoForSMResponse(sendRoutingInfoForSMInd.getInvokeId(), imsi, locationInfoWithLMSI,
					MAPExtensionContainerTest.GetTestExtensionContainer());
			d.addInformServiceCentreRequest(storedMSISDN, mwStatus, MAPExtensionContainerTest.GetTestExtensionContainer(),
					absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM);		
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
		if (this.step == FunctionalTestScenario.Action_V1_A) {
			Assert.assertNull(absentSubscriberDiagnosticSM);
			Assert.assertFalse(gprsSupportIndicator);
			Assert.assertFalse(deliveryOutcomeIndicator);
			Assert.assertNull(additionalSMDeliveryOutcome);
			Assert.assertNull(additionalAbsentSubscriberDiagnosticSM);
			Assert.assertNull(extensionContainer);
		} else {
			Assert.assertNotNull(absentSubscriberDiagnosticSM);
			Assert.assertEquals((int) absentSubscriberDiagnosticSM, 555);
			Assert.assertTrue(gprsSupportIndicator);
			Assert.assertTrue(deliveryOutcomeIndicator);
			Assert.assertEquals(additionalSMDeliveryOutcome, SMDeliveryOutcome.successfulTransfer);
			Assert.assertNotNull(additionalAbsentSubscriberDiagnosticSM);
			Assert.assertEquals((int) additionalAbsentSubscriberDiagnosticSM, 444);
			Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
		}

		this._S_recievedSmsRequestIndication = true;
		
		ISDNAddressString storedMSISDN = this.MAPParameterFactory.createISDNAddressString(AddressNature.network_specific_number, NumberingPlan.national,
				"111000111");

		try {
			if (this.step == FunctionalTestScenario.Action_V1_A) {
				d.addReportSMDeliveryStatusResponse(reportSMDeliveryStatusInd.getInvokeId(), null, null);
			} else {
				d.addReportSMDeliveryStatusResponse(reportSMDeliveryStatusInd.getInvokeId(), storedMSISDN,
						MAPExtensionContainerTest.GetTestExtensionContainer());
			}
			
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
		
		if (this.step != FunctionalTestScenario.Action_V1_B) {
			try {
				d.addAlertServiceCentreResponse(alertServiceCentreInd.getInvokeId());
			} catch (MAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onAlertServiceCentreRespIndication(AlertServiceCentreResponseIndication alertServiceCentreInd) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener#onUnstructuredSSNotifyIndication(org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyIndication)
	 */
	@Override
	public void onUnstructuredSSNotifyRequestIndication(UnstructuredSSNotifyRequestIndication unstrNotifyInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForwardShortMessageIndication(ForwardShortMessageRequestIndication forwSmInd) {

		MAPDialogSms d = forwSmInd.getMAPDialog();
		
		SM_RP_DA sm_RP_DA = forwSmInd.getSM_RP_DA();
		SM_RP_OA sm_RP_OA = forwSmInd.getSM_RP_OA();
		byte[] sm_RP_UI = forwSmInd.getSM_RP_UI();

		Assert.assertNotNull(sm_RP_DA);
		Assert.assertNotNull(sm_RP_DA.getIMSI());
//		Assert.assertEquals((long) (sm_RP_DA.getIMSI().getMCC()), 250);
//		Assert.assertEquals((long) (sm_RP_DA.getIMSI().getMNC()), 99);
		Assert.assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
		Assert.assertNotNull(sm_RP_OA);
		Assert.assertNotNull(sm_RP_OA.getMsisdn());
		Assert.assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
		Assert.assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
		Assert.assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
		Assert.assertNotNull(sm_RP_UI);
		Assert.assertTrue(Arrays.equals(sm_RP_UI, new byte[] { 21, 22, 23, 24, 25 }));
		if (this.step == FunctionalTestScenario.Action_V1_E)
			Assert.assertFalse(forwSmInd.getMoreMessagesToSend());
		else
			Assert.assertTrue(forwSmInd.getMoreMessagesToSend());

		this._S_recievedSmsRequestIndication = true;
		
		if (this.step != FunctionalTestScenario.Action_V1_E) {
			try {
				d.addForwardShortMessageResponse(forwSmInd.getInvokeId());
			} catch (MAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onForwardShortMessageRespIndication(ForwardShortMessageResponseIndication forwSmRespInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnstructuredSSNotifyResponseIndication(UnstructuredSSNotifyResponseIndication unstrNotifyInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMAPMessage(MAPMessage mapMessage) {
		// TODO Auto-generated method stub
		
	}
}
