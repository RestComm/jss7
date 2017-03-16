/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingCategory;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExtProtocolId;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkResource;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.ProtocolId;
import org.mobicents.protocols.ss7.map.api.primitives.SignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CamelInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPDialogCallHandling;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SuppressMTSS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.FailureCause;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.LocationArea;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriority;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.mobicents.protocols.ss7.map.api.service.oam.MAPDialogOam;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceReference;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceType;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.MAPDialogPdpContextActivation;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertReason;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryNotIntended;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSForBSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.primitives.ExtExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.primitives.SignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.TMSIImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.ADDInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.EPSInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.IMSIWithLMSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.ISRInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.LACImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.LocationAreaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.PagingAreaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.SGSNCapabilityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.RequestedSubscriptionInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.mobicents.protocols.ss7.map.service.sms.AlertServiceCentreRequestImpl;
import org.mobicents.protocols.ss7.map.service.sms.SM_RP_SMEAImpl;
import org.mobicents.protocols.ss7.map.service.sms.SmsSignalInfoImpl;
import org.mobicents.protocols.ss7.map.smstpdu.AddressFieldImpl;
import org.mobicents.protocols.ss7.map.smstpdu.DataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.smstpdu.ProtocolIdentifierImpl;
import org.mobicents.protocols.ss7.map.smstpdu.SmsSubmitTpduImpl;
import org.mobicents.protocols.ss7.map.smstpdu.UserDataImpl;
import org.mobicents.protocols.ss7.map.smstpdu.ValidityPeriodImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.OperationCodeImpl;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

import java.util.ArrayList;

import static org.testng.Assert.assertNull;

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

    protected MAPDialogSupplementary clientDialog;
    protected MAPDialogSms clientDialogSms;
    protected MAPDialogMobility clientDialogMobility;
    protected MAPDialogLsm clientDialogLsm;
    protected MAPDialogCallHandling clientDialogCallHandling;
    protected MAPDialogOam clientDialogOam;
    protected MAPDialogPdpContextActivation clientDialogPdpContextActivation;

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
        this.mapProvider.getMAPServiceLsm().addMAPServiceListener(this);
        this.mapProvider.getMAPServiceCallHandling().addMAPServiceListener(this);
        this.mapProvider.getMAPServiceOam().addMAPServiceListener(this);
        this.mapProvider.getMAPServicePdpContextActivation().addMAPServiceListener(this);

        this.mapProvider.getMAPServiceSupplementary().acivate();
        this.mapProvider.getMAPServiceSms().acivate();
        this.mapProvider.getMAPServiceMobility().acivate();
        this.mapProvider.getMAPServiceLsm().acivate();
        this.mapProvider.getMAPServiceCallHandling().acivate();
        this.mapProvider.getMAPServiceOam().acivate();
        this.mapProvider.getMAPServicePdpContextActivation().acivate();
    }

    public void start() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);

        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        clientDialog.send();
    }

    public void actionA() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send();
    }

    public void actionEricssonDialog() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "1115550000");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "888777");
        AddressString eriImsi = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "12345");
        AddressString eriVlrNo = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "556677");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialog.addEricssonData(eriImsi, eriVlrNo);

        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        savedInvokeId = clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null,
                msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send();
    }

    public void sendReportSMDeliveryStatusV1() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version1);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(
                AddressNature.network_specific_number, NumberingPlan.national, "999000");
        clientDialogSms.addReportSMDeliveryStatusRequest(msisdn1, serviceCentreAddress, null, null, null, false, false, null,
                null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendAlertServiceCentreRequestV1() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                MAPApplicationContextVersion.version1);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.subscriber_number,
                NumberingPlan.national, "0011");
        clientDialogSms.addAlertServiceCentreRequest(msisdn, serviceCentreAddress);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));
        clientDialogSms.send();

        clientDialogSms.release();

    }

    public void sendEmptyV1Request() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                MAPApplicationContextVersion.version1);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);

        // this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendV1BadOperationCode() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                MAPApplicationContextVersion.version1);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));

        Invoke invoke = ((MAPProviderImpl) this.mapProvider).getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest();

        // Operation Code - setting wrong code
        OperationCode oc = ((MAPProviderImpl) this.mapProvider).getTCAPProvider().getComponentPrimitiveFactory()
                .createOperationCode();
        oc.setLocalOperationCode(999L);

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.subscriber_number,
                NumberingPlan.national, "0011");
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

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
                MAPApplicationContextVersion.version1);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);

        IMSI imsi1 = this.mapParameterFactory.createIMSI("250991357999");
        SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);

        clientDialogSms.addForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardShortMessageIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendAlertServiceCentreRequestV2() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.subscriber_number,
                NumberingPlan.national, "0011");
        clientDialogSms.addAlertServiceCentreRequest(msisdn, serviceCentreAddress);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AlertServiceCentreIndication, null, sequence++));
        clientDialogSms.send();
    }

    public void sendForwardShortMessageRequestV2() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
                MAPApplicationContextVersion.version2);

        // AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
        // NumberingPlan.ISDN, "31628968300");
        // AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
        // NumberingPlan.land_mobile,
        // "204208300008002");
        // clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
        // this.remoteAddress, destReference);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);
        // clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        IMSI imsi1 = this.mapParameterFactory.createIMSI("250991357999");
        SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);

        clientDialogSms.addForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, true);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardShortMessageIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendMoForwardShortMessageRequest() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
                MAPApplicationContextVersion.version3);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        IMSI imsi1 = this.mapParameterFactory.createIMSI("250991357999");
        SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);

        AddressFieldImpl da = new AddressFieldImpl(TypeOfNumber.InternationalNumber,
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "700007");
        ProtocolIdentifierImpl pi = new ProtocolIdentifierImpl(0);
        ValidityPeriodImpl vp = new ValidityPeriodImpl(100);
        DataCodingSchemeImpl dcs = new DataCodingSchemeImpl(0);
        UserDataImpl ud = new UserDataImpl("Hello, world !!!", dcs, null, null);
        SmsSubmitTpduImpl tpdu = new SmsSubmitTpduImpl(false, true, false, 55, da, pi, vp, ud);
        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(tpdu, null);

        IMSI imsi2 = this.mapParameterFactory.createIMSI("25007123456789");

        clientDialogSms.addMoForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI,
                MAPExtensionContainerTest.GetTestExtensionContainer(), imsi2);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.MoForwardShortMessageIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendMtForwardShortMessageRequest() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMTRelayContext,
                MAPApplicationContextVersion.version3);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        LMSI lmsi1 = this.mapParameterFactory.createLMSI(new byte[] { 49, 48, 47, 46 });
        SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(lmsi1);
        AddressString msisdn1 = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_ServiceCentreAddressOA(msisdn1);
        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 21, 22, 23, 24, 25 }, null);
        clientDialogSms.addMtForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, true,
                MAPExtensionContainerTest.GetTestExtensionContainer());

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.MtForwardShortMessageIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendReportSMDeliveryStatus3() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version3);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(
                AddressNature.network_specific_number, NumberingPlan.national, "999000");
        SMDeliveryOutcome sMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
        Integer sbsentSubscriberDiagnosticSM = 555;
        Boolean gprsSupportIndicator = true;
        Boolean deliveryOutcomeIndicator = true;
        SMDeliveryOutcome additionalSMDeliveryOutcome = SMDeliveryOutcome.successfulTransfer;
        Integer additionalAbsentSubscriberDiagnosticSM = 444;
        clientDialogSms.addReportSMDeliveryStatusRequest(msisdn1, serviceCentreAddress, sMDeliveryOutcome,
                sbsentSubscriberDiagnosticSM, MAPExtensionContainerTest.GetTestExtensionContainer(), gprsSupportIndicator,
                deliveryOutcomeIndicator, additionalSMDeliveryOutcome, additionalAbsentSubscriberDiagnosticSM);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));
        clientDialogSms.send();
    }

    public void sendReportSMDeliveryStatus2() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version2);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(
                AddressNature.network_specific_number, NumberingPlan.national, "999000");
        SMDeliveryOutcome sMDeliveryOutcome = SMDeliveryOutcome.absentSubscriber;
        clientDialogSms.addReportSMDeliveryStatusRequest(sequence, msisdn1, serviceCentreAddress, sMDeliveryOutcome, null,
                null, false, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));
        clientDialogSms.send();
    }

    public void sendSendRoutingInfoForSM() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version3);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        ISDNAddressString msisdn1 = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        AddressString servCenAddr1 = this.mapParameterFactory.createAddressString(AddressNature.network_specific_number,
                NumberingPlan.national, "999000");
        clientDialogSms.addSendRoutingInfoForSMRequest(msisdn1, false, servCenAddr1, MAPExtensionContainerTest
                .GetTestExtensionContainer(), true, SM_RP_MTI.SMS_Status_Report, new SM_RP_SMEAImpl(new byte[] { 90, 91 }),
                SMDeliveryNotIntended.onlyIMSIRequested, true, null, false, false, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForSMIndication, null, sequence++));
        clientDialogSms.send();

    }

    public void sendSendAuthenticationInfo_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.infoRetrievalContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("4567890");
        clientDialogMobility.addSendAuthenticationInfoRequest(imsi, 3, true, true, null, null, RequestingNodeType.sgsn, null,
                5, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendAuthenticationInfo_V3, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendSendAuthenticationInfo_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.infoRetrievalContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("456789000");
        clientDialogMobility.addSendAuthenticationInfoRequest(imsi, 0, false, false, null, null, null, null, null, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendAuthenticationInfo_V2, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendUpdateLocation() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("45670000");
        ISDNAddressString mscNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "8222333444");
        ISDNAddressString vlrNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.network_specific_number,
                NumberingPlan.ISDN, "700000111");
        LMSI lmsi = this.mapParameterFactory.createLMSI(new byte[] { 1, 2, 3, 4 });
        IMEI imeisv = this.mapParameterFactory.createIMEI("987654321098765");
        ADDInfo addInfo = this.mapParameterFactory.createADDInfo(imeisv, false);
        clientDialogMobility.addUpdateLocationRequest(imsi, mscNumber, null, vlrNumber, lmsi, null, null, true, false, null,
                addInfo, null, false, true);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.UpdateLocation, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendCancelLocation() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = new IMSIImpl("1111122222");
        LMSI lmsi = this.mapParameterFactory.createLMSI(new byte[] { 0, 3, 98, 39 });
        IMSIWithLMSI imsiWithLmsi = new IMSIWithLMSIImpl(imsi, lmsi);
        CancellationType cancellationType = CancellationType.getInstance(1);

        ArrayList<MAPPrivateExtension> al = new ArrayList<MAPPrivateExtension>();
        al.add(this.mapParameterFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 4 }, new byte[] { 11, 12, 13, 14, 15 }));
        al.add(this.mapParameterFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 6 }, null));
        al.add(this.mapParameterFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 5 }, new byte[] { 21, 22, 23, 24, 25,
                26 }));

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        TypeOfUpdate typeOfUpdate = TypeOfUpdate.getInstance(0);
        boolean mtrfSupportedAndAuthorized = false;
        boolean mtrfSupportedAndNotAuthorized = false;
        ISDNAddressString newMSCNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        ISDNAddressString newVLRNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22229");
        LMSI newLmsi = this.mapParameterFactory.createLMSI(new byte[] { 0, 3, 98, 39 });

        clientDialogMobility.addCancelLocationRequest(imsi, imsiWithLmsi, cancellationType, extensionContainer, typeOfUpdate,
                mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CancelLocation, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendCancelLocation_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = new IMSIImpl("1111122222");
        LMSI lmsi = this.mapParameterFactory.createLMSI(new byte[] { 0, 3, 98, 39 });

        clientDialogMobility.addCancelLocationRequest(imsi, null, null, null, null, false, false, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CancelLocation, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendSendIdentification_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.interVlrInfoRetrievalContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        TMSIImpl tmsi = new TMSIImpl(new byte[] { 1, 2, 3, 4 });

        clientDialogMobility.addSendIdentificationRequest(tmsi, null, false, null, null, null, null, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendIdentification, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendSendIdentification_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.interVlrInfoRetrievalContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        TMSIImpl tmsi = new TMSIImpl(new byte[] { 1, 2, 3, 4 });

        clientDialogMobility.addSendIdentificationRequest(tmsi, null, false, null, null, null, null, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendIdentification, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendUpdateGprsLocation_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.gprsLocationUpdateContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = new IMSIImpl("111222");
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        GSNAddress sgsnAddress = new GSNAddressImpl(new byte[] { 23, 5, 38, 48, 81, 5 });
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        SGSNCapability sgsnCapability = new SGSNCapabilityImpl(true, extensionContainer, null, false, null, null, null, false,
                null, null, false, null);
        boolean informPreviousNetworkEntity = true;
        boolean psLCSNotSupportedByUE = true;
        GSNAddress vGmlcAddress = new GSNAddressImpl(new byte[] { 23, 5, 38, 48, 81, 5 });
        ADDInfo addInfo = new ADDInfoImpl(new IMEIImpl("12341234"), false);
        EPSInfo epsInfo = new EPSInfoImpl(new ISRInformationImpl(true, true, true));
        boolean servingNodeTypeIndicator = true;
        boolean skipSubscriberDataUpdate = true;
        UsedRATType usedRATType = UsedRATType.gan;
        boolean gprsSubscriptionDataNotNeeded = true;
        boolean nodeTypeIndicator = true;
        boolean areaRestricted = true;
        boolean ueReachableIndicator = true;
        boolean epsSubscriptionDataNotNeeded = true;
        UESRVCCCapability uesrvccCapability = UESRVCCCapability.ueSrvccSupported;

        clientDialogMobility.addUpdateGprsLocationRequest(imsi, sgsnNumber, sgsnAddress, extensionContainer, sgsnCapability,
                informPreviousNetworkEntity, psLCSNotSupportedByUE, vGmlcAddress, addInfo, epsInfo, servingNodeTypeIndicator,
                skipSubscriberDataUpdate, usedRATType, gprsSubscriptionDataNotNeeded, nodeTypeIndicator, areaRestricted,
                ueReachableIndicator, epsSubscriptionDataNotNeeded, uesrvccCapability);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.UpdateGprsLocation, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendPurgeMS_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.msPurgingContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = new IMSIImpl("111222");
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        
        clientDialogMobility.addPurgeMSRequest(imsi, null, sgsnNumber, null);
        
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.PurgeMS, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendPurgeMS_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.msPurgingContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = new IMSIImpl("111222");
        ISDNAddressString vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        
        clientDialogMobility.addPurgeMSRequest(imsi, vlrNumber, null, null);
        
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.PurgeMS, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendReset_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.resetContext, MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressString hlrNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "22220000");
        clientDialogMobility.addResetRequest(NetworkResource.hlr, hlrNumber, null);
        // NetworkResource networkResource, ISDNAddressString hlrNumber, ArrayList<IMSI> hlrList

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Reset, null, sequence++));
        clientDialogMobility.send();

        clientDialogMobility.release();
    }

    public void sendReset_V1() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.resetContext, MAPApplicationContextVersion.version1);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressString hlrNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "22220000");
        clientDialogMobility.addResetRequest(NetworkResource.hlr, hlrNumber, null);
        // NetworkResource networkResource, ISDNAddressString hlrNumber, ArrayList<IMSI> hlrList

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.Reset, null, sequence++));
        clientDialogMobility.send();

        clientDialogMobility.release();
    }

    public void sendForwardCheckSSIndicationRequest_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        clientDialogMobility.addForwardCheckSSIndicationRequest();

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ForwardCheckSSIndication, null, sequence++));
        clientDialogMobility.send();

        clientDialogMobility.release();
    }

    public void sendRestoreData() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = new IMSIImpl("00000222229999");

        clientDialogMobility.addRestoreDataRequest(imsi, null, null, null, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.RestoreData, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendProvideRoamingNumber() throws Exception {

        this.mapProvider.getMAPServiceCallHandling().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.roamingNumberEnquiryContext,
                MAPApplicationContextVersion.version3);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ArrayList<MAPPrivateExtension> al = new ArrayList<MAPPrivateExtension>();
        al.add(this.mapParameterFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 4 }, new byte[] { 11, 12, 13, 14, 15 }));
        al.add(this.mapParameterFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 6 }, null));
        al.add(this.mapParameterFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 5 }, new byte[] { 21, 22, 23, 24, 25,
                26 }));

        IMSI imsi = new IMSIImpl("011220200198227");
        ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22227");
        LMSI lmsi = new LMSIImpl(new byte[] { 0, 3, 98, 39 });

        MAPExtensionContainer extensionContainerForExtSigInfo = this.mapParameterFactory.createMAPExtensionContainer(al,
                new byte[] { 31, 32, 33 });
        byte[] data_ = new byte[] { 10, 20, 30, 40 };
        SignalInfo signalInfo = new SignalInfoImpl(data_);
        ProtocolId protocolId = ProtocolId.gsm_0806;
        ExternalSignalInfo gsmBearerCapability = new ExternalSignalInfoImpl(signalInfo, protocolId,
                extensionContainerForExtSigInfo);
        ExternalSignalInfo networkSignalInfo = new ExternalSignalInfoImpl(signalInfo, protocolId,
                extensionContainerForExtSigInfo);

        boolean suppressionOfAnnouncement = false;
        ISDNAddressString gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22226");
        CallReferenceNumberImpl callReferenceNumber = new CallReferenceNumberImpl(new byte[] { 19, -6, 61, 61, -22 });
        boolean orInterrogation = false;
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingCategory.Category5);
        boolean ccbsCall = false;
        SupportedCamelPhases supportedCamelPhasesInInterrogatingNode = new SupportedCamelPhasesImpl(true, true, false, false);
        MAPExtensionContainer extensionContainerforAddSigInfo = this.mapParameterFactory.createMAPExtensionContainer(al,
                new byte[] { 31, 32, 33 });
        ExtExternalSignalInfoImpl additionalSignalInfo = new ExtExternalSignalInfoImpl(signalInfo,
                ExtProtocolId.getExtProtocolId(0), extensionContainerforAddSigInfo);
        boolean orNotSupportedInGMSC = false;
        boolean prePagingSupported = false;
        boolean longFTNSupported = false;
        boolean suppressVtCsi = false;
        OfferedCamel4CSIsImpl offeredCamel4CSIsInInterrogatingNode = new OfferedCamel4CSIsImpl(false, false, false, false,
                true, true, true);
        boolean mtRoamingRetrySupported = false;
        ArrayList<LocationArea> locationAreas = new ArrayList<LocationArea>();
        LACImpl lac = new LACImpl(123);
        LocationAreaImpl la = new LocationAreaImpl(lac);
        locationAreas.add(la);
        PagingAreaImpl pagingArea = new PagingAreaImpl(locationAreas);
        EMLPPPriority callPriority = EMLPPPriority.getEMLPPPriority(0);
        boolean mtrfIndicator = false;
        ISDNAddressString oldMSCNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22225");

        clientDialogCallHandling.addProvideRoamingNumberRequest(imsi, mscNumber, msisdn, lmsi, gsmBearerCapability,
                networkSignalInfo, suppressionOfAnnouncement, gmscAddress, null, orInterrogation, null, null, ccbsCall, null,
                null, orNotSupportedInGMSC, prePagingSupported, longFTNSupported, suppressVtCsi, null, mtRoamingRetrySupported,
                null, null, mtrfIndicator, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideRoamingNumber, null, sequence++));
        clientDialogCallHandling.send();

    }

    public void sendProvideRoamingNumber_V2() throws Exception {

        this.mapProvider.getMAPServiceCallHandling().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.roamingNumberEnquiryContext,
                MAPApplicationContextVersion.version2);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ArrayList<MAPPrivateExtension> al = new ArrayList<MAPPrivateExtension>();
        al.add(this.mapParameterFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 4 }, new byte[] { 11, 12, 13, 14, 15 }));
        al.add(this.mapParameterFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 6 }, null));
        al.add(this.mapParameterFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 5 }, new byte[] { 21, 22, 23, 24, 25,
                26 }));

        IMSI imsi = new IMSIImpl("011220200198227");
        ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");

        clientDialogCallHandling.addProvideRoamingNumberRequest(imsi, mscNumber, null, null, null, null, false, null, null,
                false, null, null, false, null, null, false, false, false, false, null, false, null, null, false, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideRoamingNumber, null, sequence++));
        clientDialogCallHandling.send();

    }

    public void sendIstCommand() throws Exception {

        this.mapProvider.getMAPServiceCallHandling().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.ServiceTerminationContext,
                MAPApplicationContextVersion.version3);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = new IMSIImpl("011220200198227");

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        clientDialogCallHandling.addIstCommandRequest(imsi, extensionContainer);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.IstCommand, null, sequence++));
        clientDialogCallHandling.send();

    }


    public void sendAnyTimeInterrogation() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.anyTimeEnquiryContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("33334444");
        SubscriberIdentity subscriberIdentity = this.mapParameterFactory.createSubscriberIdentity(imsi);
        RequestedInfo requestedInfo = this.mapParameterFactory.createRequestedInfo(true, true, null, false, null, false, false,
                false);
        ISDNAddressString gsmSCFAddress = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11112222");

        clientDialogMobility.addAnyTimeInterrogationRequest(subscriberIdentity, requestedInfo, gsmSCFAddress, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AnyTimeInterrogation, null, sequence++));
        clientDialogMobility.send();
    }

    public void sendAnyTimeSubscriptionInterrogation() throws Exception {
        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext applicationContext = MAPApplicationContext.getInstance(MAPApplicationContextName.anyTimeInfoHandlingContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(applicationContext, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressString gsmSCFAddress = mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "1234567890");
        ISDNAddressString subscriberNumber = mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "111222333");
        SubscriberIdentity subscriberIdentity = mapParameterFactory.createSubscriberIdentity(subscriberNumber);
        RequestedSubscriptionInfo requestedSubscriptionInfo = new RequestedSubscriptionInfoImpl(null, false,
                RequestedCAMELSubscriptionInfo.oCSI, true, false, null, AdditionalRequestedCAMELSubscriptionInfo.mtSmsCSI,
                false, true, false, false, false, false, false);

        clientDialogMobility.addAnyTimeSubscriptionInterrogationRequest(subscriberIdentity, requestedSubscriptionInfo,
                gsmSCFAddress, null, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AnyTimeSubscriptionInterrogation, null, sequence++));
        clientDialogMobility.send();
    }

    public void sendProvideSubscriberInfo() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberInfoEnquiryContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("33334444");
        RequestedInfo requestedInfo = this.mapParameterFactory.createRequestedInfo(true, true, null, false, null, false, false,
                false);

        clientDialogMobility.addProvideSubscriberInfoRequest(imsi, null, requestedInfo, null, null);
        // IMSI imsi, LMSI lmsi, RequestedInfo requestedInfo, MAPExtensionContainer extensionContainer, EMLPPPriority callPriority

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideSubscriberInfo, null, sequence++));
        clientDialogMobility.send();
    }

    public void sendProvideSubscriberLocation() throws Exception {

        this.mapProvider.getMAPServiceLsm().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                MAPApplicationContextVersion.version3);

        clientDialogLsm = this.mapProvider.getMAPServiceLsm().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        LocationType locationType = this.mapParameterFactory.createLocationType(LocationEstimateType.cancelDeferredLocation,
                null);
        ISDNAddressString mlcNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11112222");

        clientDialogLsm.addProvideSubscriberLocationRequest(locationType, mlcNumber, null, false, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProvideSubscriberLocation, null, sequence++));
        clientDialogLsm.send();
    }

    public void sendSubscriberLocationReport() throws Exception {

        this.mapProvider.getMAPServiceLsm().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                MAPApplicationContextVersion.version3);

        clientDialogLsm = this.mapProvider.getMAPServiceLsm().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        LCSClientID lcsClientID = this.mapParameterFactory.createLCSClientID(LCSClientType.plmnOperatorServices, null, null,
                null, null, null, null);
        ISDNAddressString networkNodeNumber = this.mapParameterFactory.createISDNAddressString(
                AddressNature.international_number, NumberingPlan.ISDN, "11113333");
        LCSLocationInfo lcsLocationInfo = this.mapParameterFactory.createLCSLocationInfo(networkNodeNumber, null, null, false,
                null, null, null, null, null);

        clientDialogLsm.addSubscriberLocationReportRequest(LCSEvent.emergencyCallOrigination, lcsClientID, lcsLocationInfo,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, false,
                null, null, null, null, false, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SubscriberLocationReport, null, sequence++));
        clientDialogLsm.send();
    }

    public void sendSendRoutingInforForLCS() throws Exception {

        this.mapProvider.getMAPServiceLsm().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcGatewayContext,
                MAPApplicationContextVersion.version3);

        clientDialogLsm = this.mapProvider.getMAPServiceLsm().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressString mlcNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11112222");
        IMSI imsi = this.mapParameterFactory.createIMSI("5555544444");
        SubscriberIdentity targetMS = this.mapParameterFactory.createSubscriberIdentity(imsi);

        clientDialogLsm.addSendRoutingInfoForLCSRequest(mlcNumber, targetMS, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForLCS, null, sequence++));
        clientDialogLsm.send();
    }

    public void sendCheckImei() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMEI imei = this.mapParameterFactory.createIMEI("111111112222222");
        RequestedEquipmentInfo requestedEquipmentInfo = this.mapParameterFactory.createRequestedEquipmentInfo(true, false);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        clientDialogMobility.addCheckImeiRequest(imei, requestedEquipmentInfo, extensionContainer);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        clientDialogMobility.send();
    }

    public void sendCheckImei_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMEI imei = this.mapParameterFactory.createIMEI("333333334444444");

        clientDialogMobility.addCheckImeiRequest(imei, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        clientDialogMobility.send();
    }

    public void sendCheckImei_Huawei_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMEI imei = this.mapParameterFactory.createIMEI("333333334444444");
        IMSI imsi = this.mapParameterFactory.createIMSI("999999998888888");

        clientDialogMobility.addCheckImeiRequest_Huawei(imei, null, null, imsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        clientDialogMobility.send();
    }

    public void sendCheckImei_ForDelayedTest() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version2);

        AddressString origReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11335577");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "22446688");
        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress,
                origReference, this.remoteAddress, destReference);
        clientDialogMobility.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        IMEI imei = this.mapParameterFactory.createIMEI("333333334444444");

        clientDialogMobility.addCheckImeiRequest(imei, null, null);
        clientDialogMobility.addCheckImeiRequest(imei, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));

        assertNull(clientDialogMobility.getTCAPMessageType());

        clientDialogMobility.send();
    }

    public void sendCheckImei_ForDelayedTest2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMEI imei = this.mapParameterFactory.createIMEI("333333334444444");

        clientDialogMobility.addCheckImeiRequest(imei, null, null);
        clientDialogMobility.addCheckImeiRequest(imei, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CheckImei, null, sequence++));

        assertNull(clientDialogMobility.getTCAPMessageType());

        clientDialogMobility.send();
    }

    public void send_sendRoutingInfoForSMRequest_reportSMDeliveryStatusRequest() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                MAPApplicationContextVersion.version3);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "11223344");
        AddressString serviceCentreAddress = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "1122334455");
        clientDialogSms.addSendRoutingInfoForSMRequest(msisdn, true, serviceCentreAddress, null, false, null, null, null, false, null, false, false, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForSMIndication, null, sequence++));

        clientDialogSms.addReportSMDeliveryStatusRequest(msisdn, serviceCentreAddress, SMDeliveryOutcome.absentSubscriber,
                null, null, false, false, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReportSMDeliveryStatusIndication, null, sequence++));

        clientDialogSms.send();
        // * TC-BEGIN + sendRoutingInfoForSMRequest + reportSMDeliveryStatusRequest
    }

    public void sendInsertSubscriberData_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext,
                MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);
        MAPExtensionContainer extensionContainer = null;
//        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        IMSI imsi = this.mapParameterFactory.createIMSI("1111122222");
        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "22234");
        Category category = this.mapParameterFactory.createCategory(5);
        SubscriberStatus subscriberStatus = SubscriberStatus.operatorDeterminedBarring;
        ArrayList<ExtBearerServiceCode> bearerServiceList = new ArrayList<ExtBearerServiceCode>();
        ExtBearerServiceCode extBearerServiceCode = this.mapParameterFactory
                .createExtBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        bearerServiceList.add(extBearerServiceCode);
        ArrayList<ExtTeleserviceCode> teleserviceList = new ArrayList<ExtTeleserviceCode>();
        ExtTeleserviceCode extTeleservice = this.mapParameterFactory
                .createExtTeleserviceCode(TeleserviceCodeValue.allSpeechTransmissionServices);
        teleserviceList.add(extTeleservice);
        boolean roamingRestrictionDueToUnsupportedFeature = true;
        ISDNAddressString sgsnNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "22228");
        ArrayList<ExtSSInfo> provisionedSS = null;
        ODBData odbData = null;
        ArrayList<ZoneCode> regionalSubscriptionData = null;
        ArrayList<VoiceBroadcastData> vbsSubscriptionData = null;
        ArrayList<VoiceGroupCallData> vgcsSubscriptionData = null;
        VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = null;
        NAEAPreferredCI naeaPreferredCI = null;
        GPRSSubscriptionData gprsSubscriptionData = null;
        boolean roamingRestrictedInSgsnDueToUnsupportedFeature = true;
        NetworkAccessMode networkAccessMode = null;
        LSAInformation lsaInformation = null;
        boolean lmuIndicator = true;
        LCSInformation lcsInformation = null;
        Integer istAlertTimer = null;
        AgeIndicator superChargerSupportedInHLR = null;
        MCSSInfo mcSsInfo = null;
        CSAllocationRetentionPriority csAllocationRetentionPriority = null;
        SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo = null;
        ChargingCharacteristics chargingCharacteristics = null;
        AccessRestrictionData accessRestrictionData = null;
        Boolean icsIndicator = null;
        EPSSubscriptionData epsSubscriptionData = null;
        ArrayList<CSGSubscriptionData> csgSubscriptionDataList = null;
        boolean ueReachabilityRequestIndicator = true;
        DiameterIdentity mmeName = null;
        Long subscribedPeriodicRAUTAUtimer = null;
        boolean vplmnLIPAAllowed = true;
        Boolean mdtUserConsent = null;
        Long subscribedPeriodicLAUtimer = null;

        clientDialogMobility.addInsertSubscriberDataRequest(imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData,
                vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo, extensionContainer, naeaPreferredCI,
                gprsSubscriptionData, roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode, lsaInformation,
                lmuIndicator, lcsInformation, istAlertTimer, superChargerSupportedInHLR, mcSsInfo,
                csAllocationRetentionPriority, sgsnCamelSubscriptionInfo, chargingCharacteristics, accessRestrictionData,
                icsIndicator, epsSubscriptionData, csgSubscriptionDataList, ueReachabilityRequestIndicator, sgsnNumber,
                mmeName, subscribedPeriodicRAUTAUtimer, vplmnLIPAAllowed, mdtUserConsent, subscribedPeriodicLAUtimer);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InsertSubscriberData, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendInsertSubscriberData_V2() throws Exception {
        this.mapProvider.getMAPServiceMobility().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext,
                MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("1111122222");
        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "22234");
        Category category = this.mapParameterFactory.createCategory(5);
        SubscriberStatus subscriberStatus = SubscriberStatus.operatorDeterminedBarring;
        ArrayList<ExtBearerServiceCode> bearerServiceList = new ArrayList<ExtBearerServiceCode>();
        ExtBearerServiceCode extBearerServiceCode = this.mapParameterFactory
                .createExtBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        bearerServiceList.add(extBearerServiceCode);
        ArrayList<ExtTeleserviceCode> teleserviceList = new ArrayList<ExtTeleserviceCode>();
        ExtTeleserviceCode extTeleservice = this.mapParameterFactory
                .createExtTeleserviceCode(TeleserviceCodeValue.allSpeechTransmissionServices);

        teleserviceList.add(extTeleservice);
        boolean roamingRestrictionDueToUnsupportedFeature = true;
        ArrayList<ExtSSInfo> provisionedSS = null;
        ODBData odbData = null;
        ArrayList<ZoneCode> regionalSubscriptionData = null;
        ArrayList<VoiceBroadcastData> vbsSubscriptionData = null;
        ArrayList<VoiceGroupCallData> vgcsSubscriptionData = null;
        VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo = null;

        clientDialogMobility.addInsertSubscriberDataRequest(imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData,
                vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InsertSubscriberData, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendDeleteSubscriberData_V3() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("1111122222");

        ArrayList<ExtBasicServiceCode> basicServiceList = new ArrayList<ExtBasicServiceCode>();
        ExtBearerServiceCode extBearerServiceCode = this.mapParameterFactory
                .createExtBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        ExtBasicServiceCode ebsc = this.mapParameterFactory.createExtBasicServiceCode(extBearerServiceCode);
        basicServiceList.add(ebsc);
        extBearerServiceCode = this.mapParameterFactory.createExtBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        ebsc = this.mapParameterFactory.createExtBasicServiceCode(extBearerServiceCode);
        basicServiceList.add(ebsc);

        ArrayList<SSCode> ssList = new ArrayList<SSCode>();
        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.allForwardingSS);
        ssList.add(ssCode);
        ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.allLineIdentificationSS);
        ssList.add(ssCode);

        clientDialogMobility.addDeleteSubscriberDataRequest(imsi, basicServiceList, ssList, false, null, false, false, false, null, null, false, null, false,
                false, null, false, false, null, false, false);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.DeleteSubscriberData, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendDeleteSubscriberData_V2() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext, MAPApplicationContextVersion.version2);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("1111122222");
        ZoneCode egionalSubscriptionIdentifier = this.mapParameterFactory.createZoneCode(10);

        clientDialogMobility.addDeleteSubscriberDataRequest(imsi, null, null, true, egionalSubscriptionIdentifier, false, false, false, null, null, false,
                null, false, false, null, false, false, null, false, false);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.DeleteSubscriberData, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendSendRoutingInformation_V3() throws Exception {
        this.mapProvider.getMAPServiceCallHandling().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationInfoRetrievalContext,
                MAPApplicationContextVersion.version3);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        InterrogationType interrogationType = InterrogationType.forwarding;
        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "29113123311");

        ISDNAddressString gmscAddress = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "49883700292");

        CUGCheckInfo cugCheckInfo = null;
        Integer numberOfForwarding = null;
        boolean orInterrogation = false;
        Integer orCapability = null;
        CallReferenceNumber callReferenceNumber = null;
        ForwardingReason forwardingReason = null;
        ExtBasicServiceCode basicServiceGroup = null;
        ExternalSignalInfo networkSignalInfo = null;
        CamelInfo camelInfo = null;
        boolean suppressionOfAnnouncement = false;
        MAPExtensionContainer extensionContainer = null;
        AlertingPattern alertingPattern = null;
        boolean ccbsCall = false;
        Integer supportedCCBSPhase = null;
        ExtExternalSignalInfo additionalSignalInfo = null;
        ISTSupportIndicator istSupportIndicator = null;
        boolean prePagingSupported = false;
        CallDiversionTreatmentIndicator callDiversionTreatmentIndicator = null;
        boolean longFTNSupported = false;
        boolean suppressVtCSI = false;
        boolean suppressIncomingCallBarring = false;
        boolean gsmSCFInitiatedCall = false;
        ExtBasicServiceCode basicServiceGroup2 = null;
        ExternalSignalInfo networkSignalInfo2 = null;
        SuppressMTSS suppressMTSS = null;
        boolean mtRoamingRetrySupported = false;
        EMLPPPriority callPriority = null;

        clientDialogCallHandling.addSendRoutingInformationRequest(msisdn, cugCheckInfo, numberOfForwarding, interrogationType,
                orInterrogation, orCapability, gmscAddress, callReferenceNumber, forwardingReason, basicServiceGroup,
                networkSignalInfo, camelInfo, suppressionOfAnnouncement, extensionContainer, alertingPattern, ccbsCall,
                supportedCCBSPhase, additionalSignalInfo, istSupportIndicator, prePagingSupported,
                callDiversionTreatmentIndicator, longFTNSupported, suppressVtCSI, suppressIncomingCallBarring,
                gsmSCFInitiatedCall, basicServiceGroup2, networkSignalInfo2, suppressMTSS, mtRoamingRetrySupported,
                callPriority);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInformation, null, sequence++));
        clientDialogCallHandling.send();

    }

    public void sendSendRoutingInformation_V2() throws Exception {
        this.mapProvider.getMAPServiceCallHandling().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationInfoRetrievalContext,
                MAPApplicationContextVersion.version2);

        clientDialogCallHandling = this.mapProvider.getMAPServiceCallHandling().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "29113123311");

        clientDialogCallHandling.addSendRoutingInformationRequest(msisdn, null, null, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInformation, null, sequence++));
        clientDialogCallHandling.send();

    }

    public void sendSendImsi() throws Exception {
        this.mapProvider.getMAPServiceOam().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.imsiRetrievalContext,
                MAPApplicationContextVersion.version2);

        clientDialogOam = this.mapProvider.getMAPServiceOam().createNewDialog(appCnt, this.thisAddress, null,
                this.remoteAddress, null);

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "9992222");

        clientDialogOam.addSendImsiRequest(msisdn);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendImsi, null, sequence++));
        clientDialogOam.send();

    }


    public void sendUnrecognizedOperation() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        Invoke invoke = ((MAPProviderImpl) clientDialog.getService().getMAPProvider()).getTCAPProvider()
                .getComponentPrimitiveFactory().createTCInvokeRequest();
        invoke.setInvokeId(10L);
        OperationCode opCode = new OperationCodeImpl();
        opCode.setLocalOperationCode(1000L);
        invoke.setOperationCode(opCode);
        clientDialog.sendInvokeComponent(invoke);

        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send();
    }

    public void sendRegisterSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        BearerServiceCode bearerService = this.mapParameterFactory.createBearerServiceCode(BearerServiceCodeValue.padAccessCA_9600bps);
        BasicServiceCode basicService = this.mapParameterFactory.createBasicServiceCode(bearerService);
        clientDialog.addRegisterSSRequest(ssCode, basicService, null, null, null, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.RegisterSS, null, sequence++));
        clientDialog.send();
    }

    public void sendEraseSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        SSForBSCode ssForBSCode = this.mapParameterFactory.createSSForBSCode(ssCode, null, false);
        clientDialog.addEraseSSRequest(ssForBSCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.EraseSS, null, sequence++));
        clientDialog.send();
    }

    public void sendActivateSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        SSForBSCode ssForBSCode = this.mapParameterFactory.createSSForBSCode(ssCode, null, false);
        clientDialog.addActivateSSRequest(ssForBSCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivateSS, null, sequence++));
        clientDialog.send();
    }

    public void sendDeactivateSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        SSForBSCode ssForBSCode = this.mapParameterFactory.createSSForBSCode(ssCode, null, false);
        clientDialog.addDeactivateSSRequest(ssForBSCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.DeactivateSS, null, sequence++));
        clientDialog.send();
    }

    public void sendInterrogateSS() throws Exception {
        this.mapProvider.getMAPServiceSupplementary().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
        SSForBSCode ssForBSCode = this.mapParameterFactory.createSSForBSCode(ssCode, null, false);
        clientDialog.addInterrogateSSRequest(ssForBSCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InterrogateSS, null, sequence++));
        clientDialog.send();
    }

    public void sendReadyForSM() throws Exception {
        this.mapProvider.getMAPServiceSms().acivate();
        MAPApplicationContext appCnt = null;
        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.mwdMngtContext, MAPApplicationContextVersion.version3);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("88888777773333");
        clientDialogSms.addReadyForSMRequest(imsi, AlertReason.memoryAvailable, false, null, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReadyForSM, null, sequence++));
        clientDialogSms.send();
    }

    public void sendNoteSubscriberPresent() throws Exception {

        this.mapProvider.getMAPServiceSms().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.mwdMngtContext, MAPApplicationContextVersion.version1);

        clientDialogSms = this.mapProvider.getMAPServiceSms().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("88888777773333");
        clientDialogSms.addNoteSubscriberPresentRequest(imsi);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.NoteSubscriberPresent, null, sequence++));
        clientDialogSms.send();

        clientDialogSms.release();

    }

    public void sendSendRoutingInfoForGprsRequest() throws Exception {

        this.mapProvider.getMAPServicePdpContextActivation().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.gprsLocationInfoRetrievalContext, MAPApplicationContextVersion.version4);

        clientDialogPdpContextActivation = this.mapProvider.getMAPServicePdpContextActivation().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("88888777773333");
        byte[] addressData = new byte[] { (byte) 192, (byte) 168, 4, 22 };
        GSNAddress ggsnAddress = this.mapParameterFactory.createGSNAddress(GSNAddressAddressType.IPv4, addressData);
        ISDNAddressString ggsnNumber = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628838002");
        clientDialogPdpContextActivation.addSendRoutingInfoForGprsRequest(imsi, ggsnAddress, ggsnNumber, null);
        //        IMSI imsi, GSNAddress ggsnAddress, ISDNAddressString ggsnNumber, MAPExtensionContainer extensionContainer

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.SendRoutingInfoForGprs, null, sequence++));
        clientDialogPdpContextActivation.send();

    }

    public void sendActivateTraceModeRequest_Oam() throws Exception {

        this.mapProvider.getMAPServicePdpContextActivation().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.tracingContext, MAPApplicationContextVersion.version3);

        clientDialogOam = this.mapProvider.getMAPServiceOam().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("88888777773333");
        byte[] traceReferenceData = new byte[] { 19 };
        TraceReference traceReference = this.mapParameterFactory.createTraceReference(traceReferenceData);
        TraceType traceType = this.mapParameterFactory.createTraceType(21);
        clientDialogOam.addActivateTraceModeRequest(imsi, traceReference, traceType, null, null, null, null, null, null, null, null, null);
//        IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
//        MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
//        TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivateTraceMode, null, sequence++));
        clientDialogOam.send();

    }

    public void sendActivateTraceModeRequest_Mobility() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("88888777773333");
        byte[] traceReferenceData = new byte[] { 19 };
        TraceReference traceReference = this.mapParameterFactory.createTraceReference(traceReferenceData);
        TraceType traceType = this.mapParameterFactory.createTraceType(21);
        clientDialogMobility.addActivateTraceModeRequest(imsi, traceReference, traceType, null, null, null, null, null, null, null, null, null);
//        IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
//        MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
//        TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivateTraceMode, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendAuthenticationFailureReport() throws Exception {

        this.mapProvider.getMAPServiceMobility().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.authenticationFailureReportContext, MAPApplicationContextVersion.version3);

        clientDialogMobility = this.mapProvider.getMAPServiceMobility().createNewDialog(appCnt, this.thisAddress, null, this.remoteAddress, null);

        IMSI imsi = this.mapParameterFactory.createIMSI("88888777773333");
        clientDialogMobility.addAuthenticationFailureReportRequest(imsi, FailureCause.wrongNetworkSignature, null, null, null, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AuthenticationFailureReport, null, sequence++));
        clientDialogMobility.send();

    }

    public void sendRegisterPassword() throws Exception {

        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = null;

        appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.networkFunctionalSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference, this.remoteAddress, destReference);

        SSCode ssCode = this.mapParameterFactory.createSSCode(SupplementaryCodeValue.allCondForwardingSS);
        clientDialog.addRegisterPasswordRequest(ssCode);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.RegisterPassword, null, sequence++));
        clientDialog.send();

    }

    public void sendMystypedParameter() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        Invoke invoke = ((MAPProviderImpl) clientDialog.getService().getMAPProvider()).getTCAPProvider()
                .getComponentPrimitiveFactory().createTCInvokeRequest();
        invoke.setInvokeId(10L);
        OperationCode opCode = new OperationCodeImpl();
        opCode.setLocalOperationCode((long) MAPOperationCode.processUnstructuredSS_Request);
        invoke.setOperationCode(opCode);

        Parameter par = ((MAPProviderImpl) clientDialog.getService().getMAPProvider()).getTCAPProvider()
                .getComponentPrimitiveFactory().createParameter();
        par.setData(new byte[] { 1, 1, 1, 1, 1 });
        invoke.setParameter(par);

        clientDialog.sendInvokeComponent(invoke);

        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send();
    }

    public void actionAAA() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                this.remoteAddress, destReference);
        clientDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);

        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);
        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);
        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);
        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);
        logger.debug("Sending USSDString" + MAPFunctionalTest.USSD_STRING);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send();
    }

    public void actionB() throws MAPException {
        this.mapProvider.getMAPServiceSupplementary().acivate();

        MAPApplicationContext appCnt = MAPApplicationContext.getInstance(
                MAPApplicationContextName.networkUnstructuredSsContext, MAPApplicationContextVersion.version2);

        AddressString orgiReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628968300");
        AddressString destReference = this.mapParameterFactory.createAddressString(AddressNature.international_number,
                NumberingPlan.land_mobile, "204208300008002");

        ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(AddressNature.international_number,
                NumberingPlan.ISDN, "31628838002");

        SccpAddress badAddr = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 3333, 6);
        clientDialog = this.mapProvider.getMAPServiceSupplementary().createNewDialog(appCnt, this.thisAddress, orgiReference,
                badAddr, destReference);
        clientDialog.setReturnMessageOnError(true);

        USSDString ussdString = this.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_STRING);
        clientDialog.addProcessUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdString, null, msisdn);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ProcessUnstructuredSSRequestIndication, null, sequence++));
        clientDialog.send();
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
