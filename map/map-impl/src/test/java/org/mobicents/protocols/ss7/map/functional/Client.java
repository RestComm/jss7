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

import java.nio.charset.Charset;
import java.util.Arrays;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
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
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
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
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.smstpdu.AddressField;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserDataHeader;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityPeriod;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.sms.AlertServiceCentreRequestImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_SMEAImpl;
import org.mobicents.protocols.ss7.map.service.sms.SmsSignalInfoImpl;
import org.mobicents.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.mobicents.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.mobicents.protocols.ss7.map.smstpdu.SmsSubmitTpduImpl;
import org.mobicents.protocols.ss7.map.smstpdu.UserDataImpl;
import org.mobicents.protocols.ss7.map.smstpdu.ValidityPeriodImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
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

	private MAPParameterFactory MAPParameterFactory;

	// private boolean finished = true;
	private String unexpected = "";
	private boolean _S_receivedUnstructuredSSIndication, _S_sentEnd;
	private boolean _S_receivedMAPOpenInfoExtentionContainer;
	private boolean _S_receivedAbortInfo;
	private boolean _S_receivedEndInfo;
	private boolean _S_receivedContinueInfo;
	private boolean _S_recievedSmsRespIndication;
	private boolean _S_recievedInformSC;
	private boolean _S_receivedError;
	private boolean _S_receivedResult;
	private boolean _S_receivedResult2;

	private MAPDialogSupplementary clientDialog;
	private MAPDialogSms clientDialogSms;

	private FunctionalTestScenario step;
	
	private long savedInvokeId;

	Client(MAPStack mapStack, MAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
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
	}

	public void start() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();

		MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
				MAPApplicationContextVersion.version2);

		AddressString orgiReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		ISDNAddressString msisdn = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

		clientDialog = this.mapProvider.getMAPServiceSupplementary()
				.createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

		USSDString ussdString = this.MAPParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

		clientDialog.addProcessUnstructuredSSRequest((byte) 0x0F, ussdString, null, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

		clientDialog.send();
	}

	public void actionA() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();

		MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
				MAPApplicationContextVersion.version2);

		AddressString orgiReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		ISDNAddressString msisdn = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

		// clientDialog =
		// this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt,
		// this.thisAddress, null, this.remoteAddress,
		// null);
		clientDialog = this.mapProvider.getMAPServiceSupplementary()
				.createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		USSDString ussdString = this.MAPParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

		clientDialog.addProcessUnstructuredSSRequest((byte) 0x0F, ussdString, null, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

		clientDialog.send();
	}

	public void actionB() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();

		MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
				MAPApplicationContextVersion.version2);

		AddressString orgiReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		ISDNAddressString msisdn = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

		clientDialog = this.mapProvider.getMAPServiceSupplementary()
				.createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

		USSDString ussdString = this.MAPParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

		savedInvokeId = clientDialog.addProcessUnstructuredSSRequest((byte) 0x0F, ussdString, null, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

		clientDialog.send();
	}

	public void actionBB() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();

		MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
				MAPApplicationContextVersion.version2);

		AddressString orgiReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "1115550000");
		AddressString destReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile, "888777");
		IMSI eriImsi = this.MAPParameterFactory.createIMSI("12345");
		AddressString eriVlrNo = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile, "556677");

		ISDNAddressString msisdn = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

		clientDialog = this.mapProvider.getMAPServiceSupplementary()
				.createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialog.addEricssonData(eriImsi, eriVlrNo);

		USSDString ussdString = this.MAPParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

		savedInvokeId = clientDialog.addProcessUnstructuredSSRequest((byte) 0x0F, ussdString, null, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

		clientDialog.send();
	}

	public void actionC() throws Exception {
		
		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;
		switch (this.step) {
		case Action_Sms_MoForwardSM:
			appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext, MAPApplicationContextVersion.version3);
			break;
		case Action_Sms_MtForwardSM:
			appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMTRelayContext, MAPApplicationContextVersion.version3);
			break;
		case Action_Sms_SendRoutingInfoForSM:
		case Action_Sms_ReportSMDeliveryStatus:
			appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext, MAPApplicationContextVersion.version3);
			break;
		case Action_Sms_AlertServiceCentre:
			appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext, MAPApplicationContextVersion.version2);
			break;
		case Action_Sms_ForwardSM:
			appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext, MAPApplicationContextVersion.version2);
			break;
		}

		AddressString orgiReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		switch (this.step) {
		case Action_Sms_AlertServiceCentre: {
			ISDNAddressString msisdn = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			AddressString serviceCentreAddress = this.MAPParameterFactory.createAddressString(AddressNature.subscriber_number, NumberingPlan.national, "0011");
			clientDialogSms.addAlertServiceCentreRequest(msisdn, serviceCentreAddress);
		}
			break;

		case Action_Sms_MoForwardSM: {
			IMSI imsi1 = this.MAPParameterFactory.createIMSI("250991357999");
			SM_RP_DA sm_RP_DA = this.MAPParameterFactory.createSM_RP_DA(imsi1);
			ISDNAddressString msisdn1 = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			SM_RP_OA sm_RP_OA = this.MAPParameterFactory.createSM_RP_OA_Msisdn(msisdn1);

			AddressFieldImpl da = new AddressFieldImpl(TypeOfNumber.InternationalNumber, NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "700007");
			ProtocolIdentifierImpl pi = new ProtocolIdentifierImpl(0);
			ValidityPeriodImpl vp = new ValidityPeriodImpl(100);
			DataCodingSchemeImpl dcs = new DataCodingSchemeImpl(0);
			UserDataImpl ud = new UserDataImpl("Hello, world !!!", dcs, null, null);
			SmsSubmitTpduImpl tpdu = new SmsSubmitTpduImpl(false, true, false, 55, da, pi, vp, ud);
			SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(tpdu, null);

			IMSI imsi2 = this.MAPParameterFactory.createIMSI("25007123456789");

			clientDialogSms.addMoForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, MAPExtensionContainerTest.GetTestExtensionContainer(), imsi2);
		}
			break;

		case Action_Sms_MtForwardSM: {
			LMSI lmsi1 = this.MAPParameterFactory.createLMSI(new byte[] { 49, 48, 47, 46 });
			SM_RP_DA sm_RP_DA = this.MAPParameterFactory.createSM_RP_DA(lmsi1);
			AddressString msisdn1 = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			SM_RP_OA sm_RP_OA = this.MAPParameterFactory.createSM_RP_OA_ServiceCentreAddressOA(msisdn1);
			SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);
			clientDialogSms.addMtForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, true, MAPExtensionContainerTest.GetTestExtensionContainer());
		}
			break;

		case Action_Sms_ForwardSM: {
			IMSI imsi1 = this.MAPParameterFactory.createIMSI("250991357999");
			SM_RP_DA sm_RP_DA = this.MAPParameterFactory.createSM_RP_DA(imsi1);
			ISDNAddressString msisdn1 = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			SM_RP_OA sm_RP_OA = this.MAPParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
			SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);

			clientDialogSms.addForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, true);
		}
			break;

		case Action_Sms_SendRoutingInfoForSM: {
			ISDNAddressString msisdn1 = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			AddressString servCenAddr1 = this.MAPParameterFactory.createAddressString(AddressNature.network_specific_number, NumberingPlan.national, "999000");
			clientDialogSms.addSendRoutingInfoForSMRequest(msisdn1, false, servCenAddr1, MAPExtensionContainerTest.GetTestExtensionContainer(), true,
					SM_RP_MTI.SMS_Status_Report, new SM_RP_SMEAImpl(new byte[] { 90, 91 }));
		}
			break;

		case Action_Sms_ReportSMDeliveryStatus: {
			ISDNAddressString msisdn1 = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			AddressString serviceCentreAddress = this.MAPParameterFactory.createAddressString(AddressNature.network_specific_number, NumberingPlan.national,
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
		}

		clientDialogSms.send();
	}

	public void actionD() throws Exception {
		
		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;
		switch (this.step) {
		case Action_V1_A:
			appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext, MAPApplicationContextVersion.version1);
			break;
		case Action_V1_B:
		case Action_V1_C:
		case Action_V1_D:
			appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext, MAPApplicationContextVersion.version1);
			break;
		case Action_V1_E:
			appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext, MAPApplicationContextVersion.version1);
			break;
		}

		AddressString orgiReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

		switch (this.step) {
		case Action_V1_A: {
			ISDNAddressString msisdn1 = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			AddressString serviceCentreAddress = this.MAPParameterFactory.createAddressString(AddressNature.network_specific_number, NumberingPlan.national,
					"999000");
			SMDeliveryOutcome sMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
			clientDialogSms.addReportSMDeliveryStatusRequest(msisdn1, serviceCentreAddress, sMDeliveryOutcome, null, null, false, false, null, null);
		}
		break;

		case Action_V1_B: {
			ISDNAddressString msisdn = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			AddressString serviceCentreAddress = this.MAPParameterFactory.createAddressString(AddressNature.subscriber_number, NumberingPlan.national, "0011");
			clientDialogSms.addAlertServiceCentreRequest(msisdn, serviceCentreAddress);
		}
			break;
		
		case Action_V1_D: {
			Invoke invoke = ((MAPProviderImpl) this.mapProvider).getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

			// Operation Code - setting wrong code
			OperationCode oc = ((MAPProviderImpl) this.mapProvider).getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
			oc.setLocalOperationCode(999L);

			ISDNAddressString msisdn = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			AddressString serviceCentreAddress = this.MAPParameterFactory.createAddressString(AddressNature.subscriber_number, NumberingPlan.national, "0011");
			AlertServiceCentreRequestImpl req = new AlertServiceCentreRequestImpl(msisdn, serviceCentreAddress);
			AsnOutputStream aos = new AsnOutputStream();
			req.encodeData(aos);

			Parameter p = ((MAPProviderImpl) this.mapProvider).getTCAPProvider().getComponentPrimitiveFactory().createParameter();
			p.setTagClass(req.getTagClass());
			p.setPrimitive(req.getIsPrimitive());
			p.setTag(req.getTag());
			p.setData(aos.toByteArray());
			invoke.setParameter(p);
			invoke.setOperationCode(oc);

			Long invokeId = ((MAPDialogImpl) clientDialogSms).getTcapDialog().getNewInvokeId();
			invoke.setInvokeId(invokeId);

			clientDialogSms.sendInvokeComponent(invoke);
		}
			break;		
		case Action_V1_E: {
			IMSI imsi1 = this.MAPParameterFactory.createIMSI("250991357999");
			SM_RP_DA sm_RP_DA = this.MAPParameterFactory.createSM_RP_DA(imsi1);
			ISDNAddressString msisdn1 = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			SM_RP_OA sm_RP_OA = this.MAPParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
			SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);

			clientDialogSms.addForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, false);
		}
			break;		
		}

		clientDialogSms.send();
		
		if (this.step == FunctionalTestScenario.Action_V1_B) {
			clientDialogSms.release();
		}
	}

	public void actionE() throws Exception {

		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;
		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext, MAPApplicationContextVersion.version3);
		AddressString orgiReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.MAPParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		SmsSignalInfo sm_RP_UI;
		if (this.step == FunctionalTestScenario.Action_TestMsgLength_A) {
			sm_RP_UI = new SmsSignalInfoImpl(new byte[20], null);
			Arrays.fill(sm_RP_UI.getData(), (byte) 11);
		} else {
			sm_RP_UI = new SmsSignalInfoImpl(new byte[170], null);
			Arrays.fill(sm_RP_UI.getData(), (byte) 22);
		}
		
		IMSI imsi1 = this.MAPParameterFactory.createIMSI("250991357999");
		SM_RP_DA sm_RP_DA = this.MAPParameterFactory.createSM_RP_DA(imsi1);
		ISDNAddressString msisdn1 = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		SM_RP_OA sm_RP_OA = this.MAPParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
		IMSI imsi2 = this.MAPParameterFactory.createIMSI("25007123456789");

		Long invokeId = clientDialogSms.addMoForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, imsi2);
		
		int maxMsgLen = clientDialogSms.getMaxUserDataLength();
		int curMsgLen = clientDialogSms.getMessageUserDataLengthOnSend();
		if (curMsgLen > maxMsgLen)
			clientDialogSms.cancelInvocation(invokeId);

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
		case Action_Dialog_Eri:
			return _S_receivedEndInfo;

		case Action_Sms_AlertServiceCentre:
		case Action_Sms_ForwardSM:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_ReportSMDeliveryStatus:
		case Action_TestMsgLength_A:
		case Action_TestMsgLength_B:
			return _S_recievedSmsRespIndication && _S_receivedEndInfo;
		case Action_Sms_SendRoutingInfoForSM:
			return _S_recievedSmsRespIndication && _S_recievedInformSC && _S_receivedEndInfo;
			
		case Action_V1_A:
			return _S_recievedSmsRespIndication && _S_receivedEndInfo;
		case Action_V1_B:
			return true;
		case Action_V1_C:
		case Action_V1_D:
			return _S_receivedAbortInfo;
		case Action_V1_E:
			return _S_receivedContinueInfo;

		case Action_Component_A:
		case Action_Component_B:
		case Action_Component_E:
		case Action_Component_G:
			return _S_receivedEndInfo && _S_receivedError;
		case Action_Component_D:
			return _S_receivedEndInfo && _S_receivedResult && _S_receivedResult2;
		case Action_Component_F:
			return _S_receivedEndInfo && !_S_receivedError;
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

		case Action_Dialog_Eri:
			status += "_S_receivedEndInfo[" + _S_receivedEndInfo + "]" + "\n";
			break;

		case Action_Sms_AlertServiceCentre:
		case Action_Sms_ForwardSM:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_ReportSMDeliveryStatus:
		case Action_TestMsgLength_A:
		case Action_TestMsgLength_B:
			status += "_S_recievedSmsRespIndication[" + _S_recievedSmsRespIndication + "]" + "\n";
			status += "_S_receivedEndInfo[" + _S_receivedEndInfo + "]" + "\n";
			break;
		case Action_Sms_SendRoutingInfoForSM:
			status += "_S_recievedSmsRespIndication[" + _S_recievedSmsRespIndication + "]" + "_S_recievedInformSC[" + _S_recievedInformSC + "]" + "\n";
			status += "_S_receivedEndInfo[" + _S_receivedEndInfo + "]" + "\n";
			break;
			
		case Action_V1_A:
			status += "_S_recievedSmsRespIndication[" + _S_recievedSmsRespIndication + "]" + "\n";
			status += "_S_receivedEndInfo[" + _S_receivedEndInfo + "]" + "\n";
			break;
		case Action_V1_B:
			status += "OK" + "\n";
			break;
		case Action_V1_C:
		case Action_V1_D:
			status += "_S_receivedAbortInfo[" + _S_receivedAbortInfo + "]" + "\n";
			break;
		case Action_V1_E:
			status += "_S_receivedContinueInfo[" + _S_receivedContinueInfo + "]" + "\n";
			break;

		case Action_Component_A:
		case Action_Component_B:
		case Action_Component_E:
		case Action_Component_F:
		case Action_Component_G:
			status += "_S_receivedEndInfo[" + _S_receivedEndInfo + "]" + "\n";
			status += "_S_receivedError[" + _S_receivedError + "]" + "\n";
			break;
		case Action_Component_D:
			status += "_S_receivedEndInfo[" + _S_receivedEndInfo + "]" + "\n";
			status += "_S_receivedResult[" + _S_receivedResult + "]" + "\n";
			status += "_S_receivedResult2[" + _S_receivedResult2 + "]" + "\n";
			break;
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
		this._S_recievedInformSC = false;
		this._S_receivedContinueInfo = false;
		this._S_receivedError = false;
		this._S_receivedResult = false;
		this._S_receivedResult2 = false;
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
				MAPUserAbortChoice choice = this.MAPParameterFactory.createMAPUserAbortChoice();
				choice.setProcedureCancellationReason(ProcedureCancellationReason.handoverCancellation);
				mapDialog.abort(choice);
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;

		case Action_V1_E:
			logger.debug("Sending MAPUserAbortInfo "); // MAP V1 - Test blocking of sending user info in TC-ABORT
			try {
				MAPUserAbortChoice choice = this.MAPParameterFactory.createMAPUserAbortChoice();
				choice.setProcedureCancellationReason(ProcedureCancellationReason.handoverCancellation);
				mapDialog.abort(choice);
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;

		case Action_Component_D:
			try {
				mapDialog.send();
			} catch (MAPException e) {
				logger.error(e);
				throw new RuntimeException(e);
			}
			break;
			
		case Action_TestMsgLength_A:
		case Action_TestMsgLength_B:
			try {
				if (!_S_recievedSmsRespIndication) {
					SmsSignalInfo sm_RP_UI;
					if (this.step == FunctionalTestScenario.Action_TestMsgLength_A) {
						sm_RP_UI = new SmsSignalInfoImpl(new byte[20], null);
						Arrays.fill(sm_RP_UI.getData(), (byte) 11);
					} else {
						sm_RP_UI = new SmsSignalInfoImpl(new byte[170], null);
						Arrays.fill(sm_RP_UI.getData(), (byte) 22);
					}

					IMSI imsi1 = this.MAPParameterFactory.createIMSI("250991357999");
					SM_RP_DA sm_RP_DA = this.MAPParameterFactory.createSM_RP_DA(imsi1);
					ISDNAddressString msisdn1 = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN,
							"111222333");
					SM_RP_OA sm_RP_OA = this.MAPParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
					IMSI imsi2 = this.MAPParameterFactory.createIMSI("25007123456789");

					Long invokeId = clientDialogSms.addMoForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, imsi2);

					int maxMsgLen = clientDialogSms.getMaxUserDataLength();
					int curMsgLen = clientDialogSms.getMessageUserDataLengthOnSend();
					Assert.assertTrue(curMsgLen <= maxMsgLen);
					
					mapDialog.send();
				}
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
		case Action_Dialog_Eri:
			if (MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer))
				_S_receivedMAPOpenInfoExtentionContainer = true;

			this._S_receivedEndInfo = true;

			logger.debug("Received onMAPAcceptInfo ");
			break;

		case Action_Dialog_E:
			logger.debug("Received onMAPAcceptInfo ");
			break;
			
		case Action_Sms_AlertServiceCentre:
		case Action_Sms_MoForwardSM:
		case Action_Sms_MtForwardSM:
		case Action_Sms_SendRoutingInfoForSM:
		case Action_Sms_ReportSMDeliveryStatus:
		case Action_Sms_ForwardSM:
		case Action_TestMsgLength_A:
		case Action_TestMsgLength_B:
		case Action_V1_A:
		case Action_Component_A:
		case Action_Component_B:
		case Action_Component_D:
		case Action_Component_E:
		case Action_Component_F:
		case Action_Component_G:
			this._S_receivedEndInfo = true;
			break;
			
		case Action_V1_E:
			_S_receivedContinueInfo = true;
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
			
		case Action_V1_C:
		case Action_V1_D:
			if (refuseReason == MAPRefuseReason.PotentialVersionIncompatibility) {
				logger.debug("Received InvalidDestinationReference");
				_S_receivedAbortInfo = true;
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
		int fff = 0;
		fff++;
	}

	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
	}

	public void onDialogRelease(MAPDialog mapDialog) {
		int i1 = 0;
		i1 = 1;
	}

	@Override
	public void onDialogTimeout(MAPDialog mapDialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
		switch (this.step) {
		case Action_Component_A:
		case Action_Component_E: {
			Assert.assertTrue(mapErrorMessage.isEmSystemFailure());
			MAPErrorMessageSystemFailure mes = mapErrorMessage.getEmSystemFailure();
			Assert.assertNull(mes.getAdditionalNetworkResource());
			Assert.assertNull(mes.getNetworkResource());
			_S_receivedError = true;
		}
			break;
			
		case Action_Component_B: {
			Assert.assertTrue(mapErrorMessage.isEmSMDeliveryFailure());
			MAPErrorMessageSMDeliveryFailure mes = mapErrorMessage.getEmSMDeliveryFailure();
			Assert.assertEquals(mes.getSMEnumeratedDeliveryFailureCause(), SMEnumeratedDeliveryFailureCause.scCongestion);
			Assert.assertNull(mes.getSignalInfo());
			_S_receivedError = true;
		}
			break;
		}

	}

	@Override
	public void onProviderErrorComponent(MAPDialog mapDialog, Long invokeId, MAPProviderError providerError) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem) {
		switch (this.step) {
		case Action_Component_E:
		case Action_Component_G:
			_S_receivedError = true;
			break;
		}
	}

	@Override
	public void onInvokeTimeout(MAPDialog mapDialog, Long invoke) {
		// TODO Auto-generated method stub

	}

	/**
	 * Supplementary Service Listeners
	 */
	public void onProcessUnstructuredSSRequestIndication(ProcessUnstructuredSSRequest procUnstrReqInd) {
	}

	public void onUnstructuredSSResponseIndication(UnstructuredSSResponse unstrResInd) {
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
	public void onMoForwardShortMessageIndication(MoForwardShortMessageRequest moForwSmInd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMoForwardShortMessageRespIndication(MoForwardShortMessageResponse moForwSmRespInd) {

		SmsSignalInfo sm_RP_UI = moForwSmRespInd.getSM_RP_UI();
		MAPExtensionContainer extensionContainer = moForwSmRespInd.getExtensionContainer();

		Assert.assertNotNull(sm_RP_UI);
		Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
		Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

		this._S_recievedSmsRespIndication = true;
	}

	@Override
	public void onMtForwardShortMessageIndication(MtForwardShortMessageRequest mtForwSmInd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMtForwardShortMessageRespIndication(MtForwardShortMessageResponse mtForwSmRespInd) {

		SmsSignalInfo sm_RP_UI = mtForwSmRespInd.getSM_RP_UI();
		MAPExtensionContainer extensionContainer = mtForwSmRespInd.getExtensionContainer();

		Assert.assertNotNull(sm_RP_UI);
		Assert.assertTrue(Arrays.equals(sm_RP_UI.getData(), new byte[] { 21, 22, 23, 24, 25 }));
		Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

		this._S_recievedSmsRespIndication = true;
	}

	@Override
	public void onProcessUnstructuredSSResponseIndication(ProcessUnstructuredSSResponse procUnstrResInd) {
		if (this.step == FunctionalTestScenario.Action_Component_D) {
			if (_S_receivedResult)
				_S_receivedResult2 = true;
			else
				_S_receivedResult = true;
		}
	}

	@Override
	public void onUnstructuredSSRequestIndication(UnstructuredSSRequest unstrReqInd) {
		switch (this.step) {
		case Action_Dialog_A:
		case Action_Dialog_D:
		case Action_Dialog_E:
			logger.debug("Received UnstructuredSSIndication " + unstrReqInd.getUSSDString().getString());
			_S_receivedUnstructuredSSIndication = true;
		}
	}

	@Override
	public void onSendRoutingInfoForSMIndication(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendRoutingInfoForSMRespIndication(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd) {

		IMSI imsi = sendRoutingInfoForSMRespInd.getIMSI();
		MAPExtensionContainer extensionContainer = sendRoutingInfoForSMRespInd.getExtensionContainer();
		LocationInfoWithLMSI locationInfoWithLMSI = sendRoutingInfoForSMRespInd.getLocationInfoWithLMSI();
		ISDNAddressString networkNodeNumber = locationInfoWithLMSI.getNetworkNodeNumber();
		LMSI lmsi = locationInfoWithLMSI.getLMSI();
		MAPExtensionContainer extensionContainer2 = locationInfoWithLMSI.getExtensionContainer();
		AdditionalNumberType additionalNumberType = locationInfoWithLMSI.getAdditionalNumberType();
		ISDNAddressString additionalNumber = locationInfoWithLMSI.getAdditionalNumber();

		Assert.assertNotNull(imsi);
//		Assert.assertEquals((long) (imsi.getMCC()), 250);
//		Assert.assertEquals((long) (imsi.getMNC()), 99);
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

		this._S_recievedSmsRespIndication = true;
	}

	@Override
	public void onReportSMDeliveryStatusIndication(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReportSMDeliveryStatusRespIndication(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {

		ISDNAddressString storedMSISDN = reportSMDeliveryStatusRespInd.getStoredMSISDN();
		MAPExtensionContainer extensionContainer = reportSMDeliveryStatusRespInd.getExtensionContainer();

		if (this.step == FunctionalTestScenario.Action_V1_A) {
			Assert.assertNull(storedMSISDN);
			Assert.assertNull(extensionContainer);
		} else {
			Assert.assertNotNull(storedMSISDN);
			Assert.assertEquals(storedMSISDN.getAddressNature(), AddressNature.network_specific_number);
			Assert.assertEquals(storedMSISDN.getNumberingPlan(), NumberingPlan.national);
			Assert.assertEquals(storedMSISDN.getAddress(), "111000111");
			Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
		}

		this._S_recievedSmsRespIndication = true;
	}

	@Override
	public void onInformServiceCentreIndication(InformServiceCentreRequest informServiceCentreInd) {
		
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

		this._S_recievedInformSC = true;
	}

	@Override
	public void onAlertServiceCentreIndication(AlertServiceCentreRequest alertServiceCentreInd) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAlertServiceCentreRespIndication(AlertServiceCentreResponse alertServiceCentreInd) {

		this._S_recievedSmsRespIndication = true;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener#onUnstructuredSSNotifyIndication(org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyIndication)
	 */
	@Override
	public void onUnstructuredSSNotifyRequestIndication(UnstructuredSSNotifyRequest unstrNotifyInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForwardShortMessageIndication(ForwardShortMessageRequest forwSmInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onForwardShortMessageRespIndication(ForwardShortMessageResponse forwSmRespInd) {

		this._S_recievedSmsRespIndication = true;
	}

	@Override
	public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference, IMSI eriImsi, AddressString eriVlrNo) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener#onUnstructuredSSNotifyResponseIndication(org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponseIndication)
	 */
	@Override
	public void onUnstructuredSSNotifyResponseIndication(UnstructuredSSNotifyResponse unstrNotifyInd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMAPMessage(MAPMessage mapMessage) {
		// TODO Auto-generated method stub
		
	}

}
