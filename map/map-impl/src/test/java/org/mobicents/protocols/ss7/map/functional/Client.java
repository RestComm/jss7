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

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
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
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class Client extends EventTestHarness {

	private static Logger logger = Logger.getLogger(Client.class);

	protected SccpAddress thisAddress;
	protected SccpAddress remoteAddress;

	private MAPStack mapStack;
	protected MAPProvider mapProvider;

	protected MAPParameterFactory mapParameterFactory;

	// private boolean finished = true;
	private String unexpected = "";

	private MAPDialogSupplementary clientDialog;
	protected MAPDialogSms clientDialogSms;
	protected MAPDialogMobility clientDialogMobility;

	private long savedInvokeId;

	public Client(MAPStack mapStack, MAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
		super(logger);
		this.mapStack = mapStack;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.mapProvider = this.mapStack.getMAPProvider();

		this.mapParameterFactory = this.mapProvider.getMAPParameterFactory();

		this.mapProvider.addMAPDialogListener(this);
		this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
		this.mapProvider.getMAPServiceSms().addMAPServiceListener(this);
		this.mapProvider.getMAPServiceMobility().addMAPServiceListener(this);

		this.mapProvider.getMAPServiceSupplementary().acivate();
		this.mapProvider.getMAPServiceSms().acivate();
		this.mapProvider.getMAPServiceMobility().acivate();
	}

	public void start() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();

		MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
				MAPApplicationContextVersion.version2);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

		clientDialog = this.mapProvider.getMAPServiceSupplementary()
				.createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

		USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

		clientDialog.addProcessUnstructuredSSRequest((byte) 0x0F, ussdString, null, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

		clientDialog.send();
	}

	public void actionA() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();

		MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
				MAPApplicationContextVersion.version2);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

		clientDialog = this.mapProvider.getMAPServiceSupplementary()
				.createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

		clientDialog.addProcessUnstructuredSSRequest((byte) 0x0F, ussdString, null, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
		clientDialog.send();
	}

	public void actionEricssonDialog() throws MAPException {
		this.mapProvider.getMAPServiceSupplementary().acivate();

		MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkUnstructuredSsContext,
				MAPApplicationContextVersion.version2);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "1115550000");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile, "888777");
		IMSI eriImsi = this.mapParameterFactory.createIMSI("12345");
		AddressString eriVlrNo = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile, "556677");

		ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");

		clientDialog = this.mapProvider.getMAPServiceSupplementary()
				.createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialog.addEricssonData(eriImsi, eriVlrNo);

		USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

		savedInvokeId = clientDialog.addProcessUnstructuredSSRequest((byte) 0x0F, ussdString, null, msisdn);

		logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);
		this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
		clientDialog.send();
	}

	public void sendReportSMDeliveryStatusV1() throws Exception {

		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext, MAPApplicationContextVersion.version1);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

		ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.network_specific_number, NumberingPlan.national,
				"999000");
		SMDeliveryOutcome sMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
		clientDialogSms.addReportSMDeliveryStatusRequest(msisdn1, serviceCentreAddress, sMDeliveryOutcome, null, null, false, false, null, null);
		this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));
		clientDialogSms.send();

	}

	public void sendAlertServiceCentreRequestV1() throws Exception {

		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext, MAPApplicationContextVersion.version1);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

		ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.subscriber_number, NumberingPlan.national, "0011");
		clientDialogSms.addAlertServiceCentreRequest(msisdn, serviceCentreAddress);

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));
		clientDialogSms.send();

		clientDialogSms.release();

	}

	public void sendEmptyV1Request() throws Exception {

		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext, MAPApplicationContextVersion.version1);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

//		this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));
		clientDialogSms.send();

	}

	public void sendV1BadOperationCode() throws Exception {

		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext, MAPApplicationContextVersion.version1);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));

		Invoke invoke = ((MAPProviderImpl) this.mapProvider).getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();

		// Operation Code - setting wrong code
		OperationCode oc = ((MAPProviderImpl) this.mapProvider).getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
		oc.setLocalOperationCode(999L);

		ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.subscriber_number, NumberingPlan.national, "0011");
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

		clientDialogSms.send();

	}

	public void sendForwardShortMessageRequestV1() throws Exception {

		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext, MAPApplicationContextVersion.version1);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

		IMSI imsi1 = this.mapParameterFactory.createIMSI("250991357999");
		SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
		ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
		SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);

		clientDialogSms.addForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, false);

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardShortMessageIndication, null, sequence++));
		clientDialogSms.send();

	}

	public void sendAlertServiceCentreRequestV2() throws Exception {

		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext, MAPApplicationContextVersion.version2);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.subscriber_number, NumberingPlan.national, "0011");
		clientDialogSms.addAlertServiceCentreRequest(msisdn, serviceCentreAddress);

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));
		clientDialogSms.send();
	}

	public void sendForwardShortMessageRequestV2() throws Exception {

		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext, MAPApplicationContextVersion.version2);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		IMSI imsi1 = this.mapParameterFactory.createIMSI("250991357999");
		SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
		ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
		SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);

		clientDialogSms.addForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, true);

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardShortMessageIndication, null, sequence++));
		clientDialogSms.send();

	}

	public void sendMoForwardShortMessageRequest() throws Exception {

		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext, MAPApplicationContextVersion.version3);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		IMSI imsi1 = this.mapParameterFactory.createIMSI("250991357999");
		SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
		ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);

		AddressFieldImpl da = new AddressFieldImpl(TypeOfNumber.InternationalNumber, NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "700007");
		ProtocolIdentifierImpl pi = new ProtocolIdentifierImpl(0);
		ValidityPeriodImpl vp = new ValidityPeriodImpl(100);
		DataCodingSchemeImpl dcs = new DataCodingSchemeImpl(0);
		UserDataImpl ud = new UserDataImpl("Hello, world !!!", dcs, null, null);
		SmsSubmitTpduImpl tpdu = new SmsSubmitTpduImpl(false, true, false, 55, da, pi, vp, ud);
		SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(tpdu, null);

		IMSI imsi2 = this.mapParameterFactory.createIMSI("25007123456789");

		clientDialogSms.addMoForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, MAPExtensionContainerTest.GetTestExtensionContainer(), imsi2);

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.MoForwardShortMessageIndication, null, sequence++));
		clientDialogSms.send();

	}

	public void sendMtForwardShortMessageRequest() throws Exception {

		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMTRelayContext, MAPApplicationContextVersion.version3);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		LMSI lmsi1 = this.mapParameterFactory.createLMSI(new byte[] { 49, 48, 47, 46 });
		SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(lmsi1);
		AddressString msisdn1 = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_ServiceCentreAddressOA(msisdn1);
		SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);
		clientDialogSms.addMtForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, true, MAPExtensionContainerTest.GetTestExtensionContainer());

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.MtForwardShortMessageIndication, null, sequence++));
		clientDialogSms.send();

	}
	
	public void sendReportSMDeliveryStatus() throws Exception {

		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext, MAPApplicationContextVersion.version3);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.network_specific_number, NumberingPlan.national,
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

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));
		clientDialogSms.send();

	}	
	
	public void sendSendRoutingInfoForSM() throws Exception {

		this.mapProvider.getMAPServiceSms().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext, MAPApplicationContextVersion.version3);

		AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile,
				"204208300008002");

		clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);
		clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		AddressString servCenAddr1 = this.mapParameterFactory.createAddressString(AddressNature.network_specific_number, NumberingPlan.national, "999000");
		clientDialogSms.addSendRoutingInfoForSMRequest(msisdn1, false, servCenAddr1, MAPExtensionContainerTest.GetTestExtensionContainer(), true,
				SM_RP_MTI.SMS_Status_Report, new SM_RP_SMEAImpl(new byte[] { 90, 91 }));

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForSMIndication, null, sequence++));
		clientDialogSms.send();

	}		

	public void sendSendAuthenticationInfo_V3() throws Exception {

		this.mapProvider.getMAPServiceMobility().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.infoRetrievalContext, MAPApplicationContextVersion.version3);

		clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

		IMSI imsi = this.mapParameterFactory.createIMSI("4567890");
		clientDialogMobility.addSendAuthenticationInfoRequest(imsi, 3, true, true, null, null, RequestingNodeType.sgsn, null, 5, false);

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendAuthenticationInfo_V3, null, sequence++));
		clientDialogMobility.send();

	}		
	
	public void sendSendAuthenticationInfo_V2() throws Exception {

		this.mapProvider.getMAPServiceMobility().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.infoRetrievalContext, MAPApplicationContextVersion.version2);

		clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

		IMSI imsi = this.mapParameterFactory.createIMSI("456789000");
		clientDialogMobility.addSendAuthenticationInfoRequest(imsi, 0, false, false, null, null, null, null, null, false);

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendAuthenticationInfo_V2, null, sequence++));
		clientDialogMobility.send();

	}		

	public void sendUpdateLocation() throws Exception {

		this.mapProvider.getMAPServiceMobility().acivate();

		MAPApplicationContext appCnt = null;

		appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext, MAPApplicationContextVersion.version3);

		clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

		IMSI imsi = this.mapParameterFactory.createIMSI("45670000");
		ISDNAddressString mscNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "8222333444");
		ISDNAddressString vlrNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.network_specific_number, NumberingPlan.ISDN, "700000111");
		LMSI lmsi = this.mapParameterFactory.createLMSI(new byte[] { 1, 2, 3, 4 });
		IMEI imeisv = this.mapParameterFactory.createIMEI("987654321098765");
		ADDInfo addInfo = this.mapParameterFactory.createADDInfo(imeisv, false);
		clientDialogMobility.addUpdateLocationRequest(imsi, mscNumber, null, vlrNumber, lmsi, null, null, true, false, null, addInfo, null, false, true);

		this.observerdEvents.add(TestEvent.createSentEvent(EventType.UpdateLocation, null, sequence++));
		clientDialogMobility.send();

	}		

	public MAPDialog getMapDialog() {
		return this.clientDialog;
	}

	public void debug(String message) {
		this.logger.debug(message);
	}

	public void error(String message, Exception e) {
		this.logger.error(message, e);
	}

}
