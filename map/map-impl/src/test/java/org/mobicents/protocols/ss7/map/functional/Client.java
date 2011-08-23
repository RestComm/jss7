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
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponseIndication;
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
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponseIndication;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class Client implements MAPDialogListener, MAPServiceSupplementaryListener, MAPServiceSmsListener {

	private static Logger logger = Logger.getLogger(Client.class);

	private MAPFunctionalTest runningTestCase;
	private SccpAddress thisAddress;
	private SccpAddress remoteAddress;

	private MAPStack mapStack;
	private MAPProvider mapProvider;

	private MapServiceFactory mapServiceFactory;

	// private boolean finished = true;
	private String unexpected = "";
	private boolean _S_receivedUnstructuredSSIndication, _S_sentEnd;
	private boolean _S_receivedMAPOpenInfoExtentionContainer;
	private boolean _S_receivedAbortInfo;
	private boolean _S_receivedEndInfo;
	private boolean _S_recievedSmsRespIndication;

	private MAPDialogSupplementary clientDialog;
	private MAPDialogSms clientDialogSms;

	private FunctionalTestScenario step;

	Client(MAPStack mapStack, MAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
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
	}

	public void start() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();

		MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
				MAPApplicationContextVersion.version2);

		AddressString orgiReference = this.mapServiceFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapServiceFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		AddressString msisdn = this.mapServiceFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

		clientDialog = this.mapProvider.getMAPServiceSupplementary()
				.createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

		USSDString ussdString = this.mapServiceFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

		clientDialog.addProcessUnstructuredSSRequest((byte) 0x0F, ussdString, null, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

		clientDialog.send();
	}

	public void actionA() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();

		MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
				MAPApplicationContextVersion.version2);

		AddressString orgiReference = this.mapServiceFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapServiceFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		AddressString msisdn = this.mapServiceFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

		// clientDialog =
		// this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt,
		// this.thisAddress, null, this.remoteAddress,
		// null);
		clientDialog = this.mapProvider.getMAPServiceSupplementary()
				.createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		USSDString ussdString = this.mapServiceFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

		clientDialog.addProcessUnstructuredSSRequest((byte) 0x0F, ussdString, null, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

		clientDialog.send();
	}

	public void actionB() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();

		MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
				MAPApplicationContextVersion.version2);

		AddressString orgiReference = this.mapServiceFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapServiceFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		AddressString msisdn = this.mapServiceFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

		clientDialog = this.mapProvider.getMAPServiceSupplementary()
				.createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

		USSDString ussdString = this.mapServiceFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

		clientDialog.addProcessUnstructuredSSRequest((byte) 0x0F, ussdString, null, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

		clientDialog.send();
	}

	public void actionC() throws Exception {
		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
				MAPApplicationContextVersion.version3);

		AddressString orgiReference = this.mapServiceFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapServiceFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		switch (this.step) {
		case Action_Sms_AlertServiceCentre: {
			ISDNAddressString msisdn = this.mapServiceFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			AddressString serviceCentreAddress = this.mapServiceFactory.createAddressString(AddressNature.subscriber_number, NumberingPlan.national, "0011");
			clientDialogSms.addAlertServiceCentreRequest(msisdn, serviceCentreAddress);
		}
			break;

		case Action_Sms_MoForwardSM: {
			IMSI imsi1 = this.mapServiceFactory.createIMSI(250L, 99L, "1357999");
			SM_RP_DA sm_RP_DA = this.mapServiceFactory.createSM_RP_DA(imsi1);
			ISDNAddressString msisdn1 = this.mapServiceFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			SM_RP_OA sm_RP_OA = this.mapServiceFactory.createSM_RP_OA_Msisdn(msisdn1);
			byte[] sm_RP_UI = new byte[] { 21, 22, 23, 24, 25 };
			IMSI imsi2 = this.mapServiceFactory.createIMSI(250L, 07L, "123456789");

			clientDialogSms.addMoForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, MAPExtensionContainerTest.GetTestExtensionContainer(), imsi2);
		}
			break;

		case Action_Sms_MtForwardSM: {
			LMSI lmsi1 = this.mapServiceFactory.createLMSI(new byte[] { 49, 48, 47, 46 });
			SM_RP_DA sm_RP_DA = this.mapServiceFactory.createSM_RP_DA(lmsi1);
			AddressString msisdn1 = this.mapServiceFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			SM_RP_OA sm_RP_OA = this.mapServiceFactory.createSM_RP_OA_ServiceCentreAddressOA(msisdn1);
			byte[] sm_RP_UI = new byte[] { 21, 22, 23, 24, 25 };
			clientDialogSms.addMtForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, true, MAPExtensionContainerTest.GetTestExtensionContainer());
		}
			break;

		case Action_Sms_SendRoutingInfoForSM: {
			ISDNAddressString msisdn1 = this.mapServiceFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			AddressString servCenAddr1 = this.mapServiceFactory.createAddressString(AddressNature.network_specific_number, NumberingPlan.national, "999000");
			clientDialogSms.addSendRoutingInfoForSMRequest(msisdn1, false, servCenAddr1, MAPExtensionContainerTest.GetTestExtensionContainer(), true,
					SM_RP_MTI.SMS_Status_Report, new byte[] { 90, 91 });
		}
			break;

		case Action_Sms_ReportSMDeliveryStatus: {
			ISDNAddressString msisdn1 = this.mapServiceFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			AddressString serviceCentreAddress = this.mapServiceFactory.createAddressString(AddressNature.network_specific_number, NumberingPlan.national,
					"999000");
			SMDeliveryOutcome sMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
			Integer sbsentSubscriberDiagnosticSM = 555;
			Boolean gprsSupportIndicator = true;
			Boolean deliveryOutcomeIndicator = true;
			SMDeliveryOutcome additionalSMDeliveryOutcome = SMDeliveryOutcome.successfulTransfer;
			Integer additionalAbsentSubscriberDiagnosticSM = 444;
			clientDialogSms.addReportSMDeliveryStatusRequest(msisdn1, serviceCentreAddress, sMDeliveryOutcome, sbsentSubscriberDiagnosticSM,
					MAPExtensionContainerTest.GetTestExtensionContainer(), gprsSupportIndicator, deliveryOutcomeIndicator, additionalSMDeliveryOutcome,
					additionalAbsentSubscriberDiagnosticSM);
		}
			break;

		case Action_Sms_InformServiceCentre: {
			ISDNAddressString storedMSISDN = this.mapServiceFactory
					.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			MWStatus mwStatus = this.mapServiceFactory.createMWStatus(false, true, false, true);
			Integer absentSubscriberDiagnosticSM = 555;
			Integer additionalAbsentSubscriberDiagnosticSM = 444;
			clientDialogSms.addInformServiceCentreRequest(storedMSISDN, mwStatus, MAPExtensionContainerTest.GetTestExtensionContainer(),
					absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM);
		}
			break;
		}

		clientDialogSms.send();
	}

	public boolean isFinished() {

		// return this.finished && _S_receivedUnstructuredSSIndication
		// && _S_sentEnd && _S_recievedMAPOpenInfoExtentionContainer;
		switch (this.step) {
		case Action_Dialog_A:
			return _S_receivedUnstructuredSSIndication && _S_sentEnd && _S_receivedMAPOpenInfoExtentionContainer;
		case Action_Dialog_B:
			return _S_receivedAbortInfo && _S_receivedMAPOpenInfoExtentionContainer;
		case Action_Dialog_C:
			return _S_receivedAbortInfo;
		case Action_Dialog_D:
			return _S_receivedUnstructuredSSIndication && _S_receivedEndInfo && _S_receivedMAPOpenInfoExtentionContainer;
		case Action_Dialog_E:
			return _S_receivedUnstructuredSSIndication && _S_sentEnd;
		case Action_Dialog_F:
			return _S_receivedAbortInfo;

		case Action_Sms_AlertServiceCentre:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_SendRoutingInfoForSM:
		case Action_Sms_ReportSMDeliveryStatus:
			return _S_recievedSmsRespIndication;
		case Action_Sms_InformServiceCentre:
			return true;
		}

		return false;
	}

	public String getStatus() {
		String status = "Scenario: " + this.step + "\n";

		switch (this.step) {
		case Action_Dialog_A:
			status += "_S_receivedUnstructuredSSIndication[" + _S_receivedUnstructuredSSIndication + "]" + "\n";
			status += "_S_recievedMAPOpenInfoExtentionContainer[" + _S_receivedMAPOpenInfoExtentionContainer + "]" + "\n";
			status += "_S_sentEnd[" + _S_sentEnd + "]" + "\n";
			break;

		case Action_Dialog_B:
			status += "_S_receivedAbortInfo[" + _S_receivedAbortInfo + "]" + "\n";
			status += "_S_recievedMAPOpenInfoExtentionContainer[" + _S_receivedMAPOpenInfoExtentionContainer + "]" + "\n";
			break;

		case Action_Dialog_C:
			status += "_S_receivedAbortInfo[" + _S_receivedAbortInfo + "]" + "\n";
			break;

		case Action_Dialog_D:
			status += "_S_receivedUnstructuredSSIndication[" + _S_receivedUnstructuredSSIndication + "]" + "\n";
			status += "_S_recievedMAPOpenInfoExtentionContainer[" + _S_receivedMAPOpenInfoExtentionContainer + "]" + "\n";
			status += "_S_receivedEndInfo[" + _S_receivedEndInfo + "]" + "\n";
			break;

		case Action_Dialog_E:
			status += "_S_receivedUnstructuredSSIndication[" + _S_receivedUnstructuredSSIndication + "]" + "\n";
			status += "_S_sentEnd[" + _S_sentEnd + "]" + "\n";
			break;

		case Action_Dialog_F:
			status += "_S_receivedAbortInfo[" + _S_receivedAbortInfo + "]" + "\n";
			break;

		case Action_Sms_AlertServiceCentre:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_SendRoutingInfoForSM:
		case Action_Sms_ReportSMDeliveryStatus:
			status += "_S_recievedSmsRespIndication[" + _S_recievedSmsRespIndication + "]" + "\n";
		}

		return status + unexpected;
	}

	public void reset() {
		// this.finished = true;
		this._S_receivedUnstructuredSSIndication = false;
		this._S_sentEnd = false;
		this._S_receivedMAPOpenInfoExtentionContainer = false;
		this._S_receivedAbortInfo = false;
		this._S_receivedEndInfo = false;
		this._S_recievedSmsRespIndication = false;
	}

	public void setStep(FunctionalTestScenario step) {
		this.step = step;
	}

	public MAPDialog getMapDialog() {
		return this.clientDialog;
	}

	/**
	 * MAPDialog Listener's
	 */
	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {

		switch (this.step) {
		case Action_Dialog_A:
			logger.debug("Calling Client.end()");
			try {
				mapDialog.close(true);
				_S_sentEnd = true;
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;

		case Action_Dialog_E:
			logger.debug("Sending MAPUserAbortInfo ");
			try {
				_S_sentEnd = true;
				mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
				MAPUserAbortChoice choice = this.mapServiceFactory.createMAPUserAbortChoice();
				choice.setProcedureCancellationReason(ProcedureCancellationReason.handoverCancellation);
				mapDialog.abort(choice);
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;

		}
	}

	@Override
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference, MAPExtensionContainer extensionContainer) {
	}

	@Override
	public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {

		switch (this.step) {
		case Action_Dialog_A:
			if (MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer))
				_S_receivedMAPOpenInfoExtentionContainer = true;

			logger.debug("Received onMAPAcceptInfo ");
			break;

		case Action_Dialog_D:
			if (MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer))
				_S_receivedMAPOpenInfoExtentionContainer = true;

			this._S_receivedEndInfo = true;

			logger.debug("Received onMAPAcceptInfo ");
			break;

		case Action_Dialog_E:
			logger.debug("Received onMAPAcceptInfo ");
			break;
		}
	}

	@Override
	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
		switch (this.step) {
		case Action_Dialog_B:
			if (refuseReason == MAPRefuseReason.InvalidDestinationReference) {
				logger.debug("Received InvalidDestinationReference");
				_S_receivedAbortInfo = true;

				if (MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer))
					_S_receivedMAPOpenInfoExtentionContainer = true;
			}
			break;

		case Action_Dialog_C:
			if (refuseReason == MAPRefuseReason.ApplicationContextNotSupported) {
				logger.debug("Received ApplicationContextNotSupported");

				if (alternativeApplicationContext != null && Arrays.equals(alternativeApplicationContext.getOid(), new long[] { 1, 2, 3 })) {
					_S_receivedAbortInfo = true;
				}
			}
			break;
		}
	}

	@Override
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
	}

	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource,
			MAPExtensionContainer extensionContainer) {
		switch (this.step) {
		case Action_Dialog_F:
			logger.debug("Received DialogProviderAbort " + abortProviderReason.toString());
			if (abortProviderReason == MAPAbortProviderReason.InvalidPDU)
				_S_receivedAbortInfo = true;
			if (MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer))
				_S_receivedMAPOpenInfoExtentionContainer = true;
			break;
		}
	}

	@Override
	public void onDialogClose(MAPDialog mapDialog) {
	}

	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
	}

	public void onDialogResease(MAPDialog mapDialog) {
		int i1 = 0;
		i1 = 1;
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
	public void onInvokeTimeout(MAPDialog mapDialog, Long invoke) {
		// TODO Auto-generated method stub

	}

	/**
	 * Supplementary Service Listeners
	 */
	public void onProcessUnstructuredSSRequestIndication(ProcessUnstructuredSSRequestIndication procUnstrReqInd) {
	}

	public void onUnstructuredSSResponseIndication(UnstructuredSSResponseIndication unstrResInd) {
		switch (this.step) {
		case Action_Dialog_A:
		case Action_Dialog_D:
		case Action_Dialog_E:
			logger.debug("Received UnstructuredSSIndication " + unstrResInd.getUSSDString().getString());
			_S_receivedUnstructuredSSIndication = true;
		}
	}

	/**
	 * Supplementary Service Listeners
	 */
	@Override
	public void onMoForwardShortMessageIndication(MoForwardShortMessageRequestIndication moForwSmInd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMoForwardShortMessageRespIndication(MoForwardShortMessageResponseIndication moForwSmRespInd) {

		byte[] sm_RP_UI = moForwSmRespInd.getSM_RP_UI();
		MAPExtensionContainer extensionContainer = moForwSmRespInd.getExtensionContainer();

		Assert.assertNotNull(sm_RP_UI);
		Assert.assertTrue(Arrays.equals(sm_RP_UI, new byte[] { 21, 22, 23, 24, 25 }));
		Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

		this._S_recievedSmsRespIndication = true;
	}

	@Override
	public void onMtForwardShortMessageIndication(MtForwardShortMessageRequestIndication mtForwSmInd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMtForwardShortMessageRespIndication(MtForwardShortMessageResponseIndication mtForwSmRespInd) {

		byte[] sm_RP_UI = mtForwSmRespInd.getSM_RP_UI();
		MAPExtensionContainer extensionContainer = mtForwSmRespInd.getExtensionContainer();

		Assert.assertNotNull(sm_RP_UI);
		Assert.assertTrue(Arrays.equals(sm_RP_UI, new byte[] { 21, 22, 23, 24, 25 }));
		Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

		this._S_recievedSmsRespIndication = true;
	}

	@Override
	public void onProcessUnstructuredSSResponseIndication(ProcessUnstructuredSSResponseIndication procUnstrResInd) {

	}

	@Override
	public void onUnstructuredSSRequestIndication(UnstructuredSSRequestIndication unstrReqInd) {
		switch (this.step) {
		case Action_Dialog_A:
		case Action_Dialog_D:
		case Action_Dialog_E:
			logger.debug("Received UnstructuredSSIndication " + unstrReqInd.getUSSDString().getString());
			_S_receivedUnstructuredSSIndication = true;
		}
	}

	@Override
	public void onSendRoutingInfoForSMIndication(SendRoutingInfoForSMRequestIndication sendRoutingInfoForSMInd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendRoutingInfoForSMRespIndication(SendRoutingInfoForSMResponseIndication sendRoutingInfoForSMRespInd) {

		IMSI imsi = sendRoutingInfoForSMRespInd.getIMSI();
		MAPExtensionContainer extensionContainer = sendRoutingInfoForSMRespInd.getExtensionContainer();
		LocationInfoWithLMSI locationInfoWithLMSI = sendRoutingInfoForSMRespInd.getLocationInfoWithLMSI();
		ISDNAddressString networkNodeNumber = locationInfoWithLMSI.getNetworkNodeNumber();
		LMSI lmsi = locationInfoWithLMSI.getLMSI();
		MAPExtensionContainer extensionContainer2 = locationInfoWithLMSI.getExtensionContainer();
		AdditionalNumberType additionalNumberType = locationInfoWithLMSI.getAdditionalNumberType();
		ISDNAddressString additionalNumber = locationInfoWithLMSI.getAdditionalNumber();

		Assert.assertNotNull(imsi);
		Assert.assertEquals((long) (imsi.getMCC()), 250);
		Assert.assertEquals((long) (imsi.getMNC()), 99);
		Assert.assertEquals(imsi.getMSIN(), "777000");
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

		this._S_recievedSmsRespIndication = true;
	}

	@Override
	public void onReportSMDeliveryStatusIndication(ReportSMDeliveryStatusRequestIndication reportSMDeliveryStatusInd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReportSMDeliveryStatusRespIndication(ReportSMDeliveryStatusResponseIndication reportSMDeliveryStatusRespInd) {

		ISDNAddressString storedMSISDN = reportSMDeliveryStatusRespInd.getStoredMSISDN();
		MAPExtensionContainer extensionContainer = reportSMDeliveryStatusRespInd.getExtensionContainer();

		Assert.assertNotNull(storedMSISDN);
		Assert.assertEquals(storedMSISDN.getAddressNature(), AddressNature.network_specific_number);
		Assert.assertEquals(storedMSISDN.getNumberingPlan(), NumberingPlan.national);
		Assert.assertEquals(storedMSISDN.getAddress(), "111000111");
		Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

		this._S_recievedSmsRespIndication = true;
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

		this._S_recievedSmsRespIndication = true;
	}

}
