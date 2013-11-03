/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.cap.functional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.cap.CAPDialogImpl;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParameterFactory;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.CAPStack;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.MonitorMode;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedDigits;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSend;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Tone;
import org.mobicents.protocols.ss7.cap.api.service.gprs.CAPDialogGprs;
import org.mobicents.protocols.ss7.cap.api.service.gprs.InitialDpGprsRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.RequestReportGPRSEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSCause;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.cap.api.service.sms.CAPDialogSms;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.RPCause;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.InitialDPRequestImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.InitialDpGprsRequestImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ChargingResultImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ElapsedTimeImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSCauseImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSEventSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.RPCauseImpl;
import org.mobicents.protocols.ss7.inap.api.INAPParameterFactory;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;
import org.mobicents.protocols.ss7.inap.primitives.MiscCallInfoImpl;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.NAINumber;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.RAIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class Client extends EventTestHarness {

    private static Logger logger = Logger.getLogger(Client.class);

    // private CAPFunctionalTest runningTestCase;
    private SccpAddress thisAddress;
    private SccpAddress remoteAddress;

    protected CAPStack capStack;
    protected CAPProvider capProvider;

    protected CAPParameterFactory capParameterFactory;
    protected MAPParameterFactory mapParameterFactory;
    protected INAPParameterFactory inapParameterFactory;
    protected ISUPParameterFactory isupParameterFactory;

    // private boolean _S_receivedUnstructuredSSIndication, _S_sentEnd;

    protected CAPDialogCircuitSwitchedCall clientCscDialog;
    protected CAPDialogGprs clientGprsDialog;
    protected CAPDialogSms clientSmsDialog;

    // private FunctionalTestScenario step;

    private long savedInvokeId;

    // private int dialogStep;
    // private String unexpected = "";

    Client(CAPStack capStack, CAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
        super(logger);

        this.capStack = capStack;
        // this.runningTestCase = runningTestCase;
        this.thisAddress = thisAddress;
        this.remoteAddress = remoteAddress;
        this.capProvider = this.capStack.getCAPProvider();

        this.capParameterFactory = this.capProvider.getCAPParameterFactory();
        this.mapParameterFactory = this.capProvider.getMAPParameterFactory();
        this.inapParameterFactory = this.capProvider.getINAPParameterFactory();
        this.isupParameterFactory = this.capProvider.getISUPParameterFactory();

        this.capProvider.addCAPDialogListener(this);

        this.capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);
        this.capProvider.getCAPServiceGprs().addCAPServiceListener(this);
        this.capProvider.getCAPServiceSms().addCAPServiceListener(this);

        this.capProvider.getCAPServiceCircuitSwitchedCall().acivate();
        this.capProvider.getCAPServiceGprs().acivate();
        this.capProvider.getCAPServiceSms().acivate();
    }

    public void sendInitialDp(CAPApplicationContext appCnt) throws CAPException {

        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                this.remoteAddress);

        InitialDPRequest initialDp = getTestInitialDp();
        clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
                initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
                initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
                initialDp.getExtensions(), initialDp.getHighLayerCompatibility(), initialDp.getAdditionalCallingPartyNumber(),
                initialDp.getBearerCapability(), initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),
                initialDp.getRedirectionInformation(), initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(),
                initialDp.getCarrier(), initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
                initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
                initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
                initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(), initialDp.getCallForwardingSSPending(),
                initialDp.getInitialDPArgExtension());

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendAssistRequestInstructionsRequest() throws CAPException {

        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(
                CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff, this.thisAddress, this.remoteAddress);

        GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
        genericNumber.setAddress("333111222");
        genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
        genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
        // genericNumber.setNumberIncompleter(GenericNumber._NI_COMPLETE);
        genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_ISDN);
        genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLED_NUMBER);
        genericNumber.setScreeningIndicator(GenericNumber._SI_NETWORK_PROVIDED);
        Digits correlationID = this.capParameterFactory.createDigits_GenericNumber(genericNumber);
        IPSSPCapabilities ipSSPCapabilities = this.capParameterFactory.createIPSSPCapabilities(true, false, true, false, false,
                null);
        clientCscDialog.addAssistRequestInstructionsRequest(correlationID, ipSSPCapabilities, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AssistRequestInstructionsRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendEstablishTemporaryConnectionRequest_CallInformationRequest() throws CAPException {

        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(
                CAPApplicationContext.CapV4_scf_gsmSSFGeneric, this.thisAddress, this.remoteAddress);

        GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
        genericNumber.setAddress("333111222");
        genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
        genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
        // genericNumber.setNumberIncompleter(GenericNumber._NI_COMPLETE);
        genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_ISDN);
        genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLED_NUMBER);
        genericNumber.setScreeningIndicator(GenericNumber._SI_NETWORK_PROVIDED);
        Digits assistingSSPIPRoutingAddress = this.capParameterFactory.createDigits_GenericNumber(genericNumber);
        clientCscDialog.addEstablishTemporaryConnectionRequest(assistingSSPIPRoutingAddress, null, null, null, null, null,
                null, null, null, null, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.EstablishTemporaryConnectionRequest, null, sequence++));

        ArrayList<RequestedInformationType> requestedInformationTypeList = new ArrayList<RequestedInformationType>();
        requestedInformationTypeList.add(RequestedInformationType.callStopTime);
        clientCscDialog.addCallInformationRequestRequest(requestedInformationTypeList, null, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.CallInformationRequestRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendActivityTestRequest(int invokeTimeout) throws CAPException {

        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(
                CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF, this.thisAddress, this.remoteAddress);

        clientCscDialog.addActivityTestRequest(invokeTimeout);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivityTestRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendEventReportBCSMRequest_1() throws CAPException {

        CauseIndicators causeIndicators = this.isupParameterFactory.createCauseIndicators();
        causeIndicators.setLocation(CauseIndicators._LOCATION_USER);
        causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
        causeIndicators.setCauseValue(CauseIndicators._CV_ALL_CLEAR);
        CauseCap releaseCause = this.capParameterFactory.createCauseCap(causeIndicators);
        ODisconnectSpecificInfo oDisconnectSpecificInfo = this.capParameterFactory.createODisconnectSpecificInfo(releaseCause);
        ReceivingSideID legID = this.capParameterFactory.createReceivingSideID(LegType.leg1);
        MiscCallInfo miscCallInfo = this.inapParameterFactory.createMiscCallInfo(MiscCallInfoMessageType.notification, null);
        EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capParameterFactory
                .createEventSpecificInformationBCSM(oDisconnectSpecificInfo);
        clientCscDialog.addEventReportBCSMRequest(EventTypeBCSM.oDisconnect, eventSpecificInformationBCSM, legID, miscCallInfo,
                null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.EventReportBCSMRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendBadDataNoAcn() throws CAPException {

        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(null, this.thisAddress,
                this.remoteAddress);

        try {
            Dialog tcapDialog = ((CAPDialogImpl) clientCscDialog).getTcapDialog();
            TCBeginRequest tcBeginReq = ((CAPProviderImpl) this.capProvider).getTCAPProvider().getDialogPrimitiveFactory()
                    .createBegin(tcapDialog);
            tcapDialog.send(tcBeginReq);
        } catch (TCAPSendException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendReferensedNumber() throws CAPException {

        clientGprsDialog = this.capProvider.getCAPServiceGprs().createNewDialog(CAPApplicationContext.CapV3_gsmSCF_gprsSSF,
                this.thisAddress, this.remoteAddress);
        CAPGprsReferenceNumber capGprsReferenceNumber = this.capParameterFactory.createCAPGprsReferenceNumber(1005, 1006);
        clientGprsDialog.setGprsReferenceNumber(capGprsReferenceNumber);

        clientGprsDialog.send();
    }

    public void testMessageUserDataLength() throws CAPException {

        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(
                CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF, this.thisAddress, this.remoteAddress);

        GenericNumber genericNumber = this.isupParameterFactory.createGenericNumber();
        genericNumber.setAddress("333111222");
        genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
        genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
        // genericNumber.setNumberIncompleter(GenericNumber._NI_COMPLETE);
        genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_ISDN);
        genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLED_NUMBER);
        genericNumber.setScreeningIndicator(GenericNumber._SI_NETWORK_PROVIDED);
        Digits correlationID = this.capParameterFactory.createDigits_GenericNumber(genericNumber);
        IPSSPCapabilities ipSSPCapabilities = this.capParameterFactory.createIPSSPCapabilities(true, false, true, false, false,
                null);
        clientCscDialog.addAssistRequestInstructionsRequest(correlationID, ipSSPCapabilities, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.AssistRequestInstructionsRequest, null, sequence++));

        int i1 = clientCscDialog.getMessageUserDataLengthOnSend();
        assertEquals(i1, 65);

        // this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        // clientCscDialog.send();
    }

    // public void sendReferensedNumber2() throws CAPException {
    //
    // clientGprsDialog = this.capProvider.getCAPServiceGprs().createNewDialog(CAPApplicationContext.CapV3_gsmSCF_gprsSSF,
    // this.thisAddress,
    // this.remoteAddress);
    // CAPGprsReferenceNumber capGprsReferenceNumber = this.capParameterFactory.createCAPGprsReferenceNumber(1000000, 1006);
    // clientGprsDialog.setGprsReferenceNumber(capGprsReferenceNumber);
    //
    // ApplicationContextName acn = ((CAPProviderImpl) this.capProvider).getTCAPProvider().getDialogPrimitiveFactory()
    // .createApplicationContextName(clientGprsDialog.getApplicationContext().getOID());
    //
    // Dialog tcapDialog = ((CAPDialogImpl)clientGprsDialog).getTcapDialog();
    // TCBeginRequest tcBeginReq = ((CAPProviderImplWrapper) this.capProvider).encodeTCBegin(tcapDialog, acn,
    // clientGprsDialog.getGprsReferenceNumber());
    //
    // try {
    // tcapDialog.send(tcBeginReq);
    // } catch (TCAPSendException e) {
    // throw new CAPException(e.getMessage(), e);
    // }
    // clientGprsDialog.setGprsReferenceNumber(null);
    //
    // // ((CAPDialogImpl)clientGprsDialog).setState(CAPDialogState.InitialSent);
    //
    // }

    // public void sendBadDataUnknownAcn() throws CAPException {
    //
    // clientCscDialog =
    // this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF,
    // this.thisAddress, this.remoteAddress);
    //
    // try {
    // Dialog tcapDialog = ((CAPDialogImpl)clientCscDialog).getTcapDialog();
    // TCBeginRequest tcBeginReq =
    // ((CAPProviderImpl)this.capProvider).getTCAPProvider().getDialogPrimitiveFactory().createBegin(tcapDialog);
    // ApplicationContextName acn = new ApplicationContextNameImpl();
    // acn.setOid(new long[] { 0, 4, 0, 0, 1, 0, 11, 25 });
    // tcBeginReq.setApplicationContextName(acn);
    // tcapDialog.send(tcBeginReq);
    // } catch (TCAPSendException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    public void releaseDialog() {
        clientCscDialog.release();
    }

    public static boolean checkTestInitialDp(InitialDPRequest ind) {

        try {
            if (ind.getServiceKey() != 321)
                return false;

            if (ind.getCalledPartyNumber() == null)
                return false;
            if (ind.getCalledPartyNumber().getCalledPartyNumber() == null)
                return false;
            if (ind.getCalledPartyNumber().getCalledPartyNumber().getNatureOfAddressIndicator() != NAINumber._NAI_INTERNATIONAL_NUMBER)
                return false;
            if (!ind.getCalledPartyNumber().getCalledPartyNumber().getAddress().equals("11223344"))
                return false;
            if (ind.getCalledPartyNumber().getCalledPartyNumber().getNumberingPlanIndicator() != CalledPartyNumber._NPI_ISDN)
                return false;
            if (ind.getCalledPartyNumber().getCalledPartyNumber().getInternalNetworkNumberIndicator() != CalledPartyNumber._INN_ROUTING_NOT_ALLOWED)
                return false;

            return true;

        } catch (CAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public InitialDPRequest getTestInitialDp() {

        try {
            CalledPartyNumber calledPartyNumber = this.isupParameterFactory.createCalledPartyNumber();
            calledPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
            calledPartyNumber.setAddress("11223344");
            calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
            calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
            CalledPartyNumberCap calledPartyNumberCap = this.capParameterFactory.createCalledPartyNumberCap(calledPartyNumber);
            calledPartyNumberCap = new CalledPartyNumberCapImpl(calledPartyNumber);

            InitialDPRequestImpl res = new InitialDPRequestImpl(321, calledPartyNumberCap, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null, null, null, false, null, null, null, null,
                    null, null, null, null, false, null, false);

            return res;
        } catch (CAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        // int serviceKey, CalledPartyNumberCap calledPartyNumber, CallingPartyNumberCap callingPartyNumber,
        // CallingPartysCategoryInap callingPartysCategory, CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities,
        // LocationNumberCap locationNumber, OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
        // HighLayerCompatibilityInap highLayerCompatibility, AdditionalCallingPartyNumberCap additionalCallingPartyNumber,
        // BearerCapability bearerCapability,
        // EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap
        // redirectionInformation, CauseCap cause,
        // ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex, CUGInterlock
        // cugInterlock,
        // boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState, LocationInformation locationInformation,
        // ExtBasicServiceCode extBasicServiceCode, CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress,
        // CalledPartyBCDNumber calledPartyBCDNumber, TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending,
        // InitialDPArgExtension initialDPArgExtension, boolean isCAPVersion3orLater
    }

    public void checkRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
        assertEquals(ind.getBCSMEventList().size(), 7);

        BCSMEvent ev = ind.getBCSMEventList().get(0);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.routeSelectFailure);
        assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
        assertNull(ev.getLegID());
        ev = ind.getBCSMEventList().get(1);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oCalledPartyBusy);
        assertEquals(ev.getMonitorMode(), MonitorMode.interrupted);
        assertNull(ev.getLegID());
        ev = ind.getBCSMEventList().get(2);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oNoAnswer);
        assertEquals(ev.getMonitorMode(), MonitorMode.interrupted);
        assertNull(ev.getLegID());
        ev = ind.getBCSMEventList().get(3);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oAnswer);
        assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
        assertNull(ev.getLegID());
        ev = ind.getBCSMEventList().get(4);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
        assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
        assertEquals(ev.getLegID().getSendingSideID(), LegType.leg1);
        ev = ind.getBCSMEventList().get(5);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
        assertEquals(ev.getMonitorMode(), MonitorMode.interrupted);
        assertEquals(ev.getLegID().getSendingSideID(), LegType.leg2);
        ev = ind.getBCSMEventList().get(6);
        assertEquals(ev.getEventTypeBCSM(), EventTypeBCSM.oAbandon);
        assertEquals(ev.getMonitorMode(), MonitorMode.notifyAndContinue);
        assertNull(ev.getLegID());
    }

    public void sendInitialDp2() throws CAPException {

        CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                this.remoteAddress);

        CAPGprsReferenceNumber grn = this.capParameterFactory.createCAPGprsReferenceNumber(101, 102);
        clientCscDialog.setGprsReferenceNumber(grn);

        InitialDPRequest initialDp = getTestInitialDp();
        clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
                initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
                initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
                initialDp.getExtensions(), initialDp.getHighLayerCompatibility(), initialDp.getAdditionalCallingPartyNumber(),
                initialDp.getBearerCapability(), initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),
                initialDp.getRedirectionInformation(), initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(),
                initialDp.getCarrier(), initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
                initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
                initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
                initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(), initialDp.getCallForwardingSSPending(),
                initialDp.getInitialDPArgExtension());
        clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
                initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
                initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
                initialDp.getExtensions(), initialDp.getHighLayerCompatibility(), initialDp.getAdditionalCallingPartyNumber(),
                initialDp.getBearerCapability(), initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),
                initialDp.getRedirectionInformation(), initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(),
                initialDp.getCarrier(), initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
                initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
                initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
                initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(), initialDp.getCallForwardingSSPending(),
                initialDp.getInitialDPArgExtension());

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendInitialDp3() throws CAPException {

        CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                this.remoteAddress);

        InitialDPRequest initialDp = getTestInitialDp();
        clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
                initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
                initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
                initialDp.getExtensions(), initialDp.getHighLayerCompatibility(), initialDp.getAdditionalCallingPartyNumber(),
                initialDp.getBearerCapability(), initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),
                initialDp.getRedirectionInformation(), initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(),
                initialDp.getCarrier(), initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
                initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
                initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
                initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(), initialDp.getCallForwardingSSPending(),
                initialDp.getInitialDPArgExtension());
        clientCscDialog.addInitialDPRequest(initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
                initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
                initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
                initialDp.getExtensions(), initialDp.getHighLayerCompatibility(), initialDp.getAdditionalCallingPartyNumber(),
                initialDp.getBearerCapability(), initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),
                initialDp.getRedirectionInformation(), initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(),
                initialDp.getCarrier(), initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
                initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
                initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
                initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(), initialDp.getCallForwardingSSPending(),
                initialDp.getInitialDPArgExtension());

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendInitialDp_playAnnouncement() throws CAPException {

        CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                this.remoteAddress);

        InitialDPRequest initialDp = getTestInitialDp();
        clientCscDialog.addInitialDPRequest(1000000, initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
                initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
                initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
                initialDp.getExtensions(), initialDp.getHighLayerCompatibility(), initialDp.getAdditionalCallingPartyNumber(),
                initialDp.getBearerCapability(), initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),
                initialDp.getRedirectionInformation(), initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(),
                initialDp.getCarrier(), initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
                initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
                initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
                initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(), initialDp.getCallForwardingSSPending(),
                initialDp.getInitialDPArgExtension());

        Tone tone = this.capParameterFactory.createTone(10, null);
        InformationToSend informationToSend = this.capParameterFactory.createInformationToSend(tone);
        clientCscDialog.addPlayAnnouncementRequest(1000000, informationToSend, null, null, null, null, null);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.PlayAnnouncementRequest, null, sequence++));
        clientCscDialog.send();
    }

    public void sendInvokesForUnexpectedResultError() throws CAPException {

        CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                this.remoteAddress);

        InitialDPRequest initialDp = getTestInitialDp();
        clientCscDialog.addInitialDPRequest(1000000, initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
                initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
                initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
                initialDp.getExtensions(), initialDp.getHighLayerCompatibility(), initialDp.getAdditionalCallingPartyNumber(),
                initialDp.getBearerCapability(), initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),
                initialDp.getRedirectionInformation(), initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(),
                initialDp.getCarrier(), initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
                initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
                initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
                initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(), initialDp.getCallForwardingSSPending(),
                initialDp.getInitialDPArgExtension());
        clientCscDialog.addInitialDPRequest(1000000, initialDp.getServiceKey(), initialDp.getCalledPartyNumber(),
                initialDp.getCallingPartyNumber(), initialDp.getCallingPartysCategory(), initialDp.getCGEncountered(),
                initialDp.getIPSSPCapabilities(), initialDp.getLocationNumber(), initialDp.getOriginalCalledPartyID(),
                initialDp.getExtensions(), initialDp.getHighLayerCompatibility(), initialDp.getAdditionalCallingPartyNumber(),
                initialDp.getBearerCapability(), initialDp.getEventTypeBCSM(), initialDp.getRedirectingPartyID(),
                initialDp.getRedirectionInformation(), initialDp.getCause(), initialDp.getServiceInteractionIndicatorsTwo(),
                initialDp.getCarrier(), initialDp.getCugIndex(), initialDp.getCugInterlock(), initialDp.getCugOutgoingAccess(),
                initialDp.getIMSI(), initialDp.getSubscriberState(), initialDp.getLocationInformation(),
                initialDp.getExtBasicServiceCode(), initialDp.getCallReferenceNumber(), initialDp.getMscAddress(),
                initialDp.getCalledPartyBCDNumber(), initialDp.getTimeAndTimezone(), initialDp.getCallForwardingSSPending(),
                initialDp.getInitialDPArgExtension());

        CollectedDigits collectedDigits = this.capParameterFactory.createCollectedDigits(2, 3, null, null, null, null, null,
                null, null, null, null);
        CollectedInfo collectedInfo = this.capParameterFactory.createCollectedInfo(collectedDigits);
        clientCscDialog.addPromptAndCollectUserInformationRequest(collectedInfo, null, null, null, null, null);
        clientCscDialog.addPromptAndCollectUserInformationRequest(collectedInfo, null, null, null, null, null);

        clientCscDialog.addActivityTestRequest();
        clientCscDialog.addActivityTestRequest();

        CauseIndicators causeIndicators = this.isupParameterFactory.createCauseIndicators();
        causeIndicators.setLocation(CauseIndicators._LOCATION_USER);
        causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
        causeIndicators.setCauseValue(CauseIndicators._CV_ALL_CLEAR);
        CauseCap releaseCause = this.capParameterFactory.createCauseCap(causeIndicators);
        clientCscDialog.addReleaseCallRequest(releaseCause);
        clientCscDialog.addReleaseCallRequest(releaseCause);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.PromptAndCollectUserInformationRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivityTestRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivityTestRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReleaseCallRequest, null, sequence++));
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReleaseCallRequest, null, sequence++));

        clientCscDialog.send();
    }

    public void sendDummyMessage() throws CAPException {

        CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
        SccpAddress dummyAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 3333, null, 6);
        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                dummyAddress);

        clientCscDialog.send();
    }

    public void actionB() throws CAPException {
        CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
        SccpAddress dummyAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 3333, null, 6);
        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
                dummyAddress);
        clientCscDialog.setReturnMessageOnError(true);

        clientCscDialog.send();
    }

    // public void sendEmpty() throws CAPException, TCAPSendException {
    // CAPApplicationContext appCnt = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
    // clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(appCnt, this.thisAddress,
    // this.remoteAddress);
    //
    // TCBeginRequest req =
    // ((CAPProviderImpl)((CAPDialogImpl)clientCscDialog).getService().getCAPProvider()).getTCAPProvider().getDialogPrimitiveFactory().createBegin(((CAPDialogImpl)clientCscDialog).getTcapDialog());
    // req.setDestinationAddress(this.remoteAddress);
    // req.setOriginatingAddress(this.thisAddress);
    // ((CAPDialogImpl)clientCscDialog).getTcapDialog().send(req);
    //
    // // clientCscDialog.send();
    // }

    public void sendInitialDpGprs(CAPApplicationContext appCnt) throws CAPException {

        clientGprsDialog = this.capProvider.getCAPServiceGprs().createNewDialog(appCnt, this.thisAddress, this.remoteAddress);

        InitialDpGprsRequest initialDp = getTestInitialDpGprsRequest();
        clientGprsDialog.addInitialDpGprsRequest(initialDp.getServiceKey(), initialDp.getGPRSEventType(),
                initialDp.getMsisdn(), initialDp.getImsi(), initialDp.getTimeAndTimezone(), initialDp.getGPRSMSClass(),
                initialDp.getEndUserAddress(), initialDp.getQualityOfService(), initialDp.getAccessPointName(),
                initialDp.getRouteingAreaIdentity(), initialDp.getChargingID(), initialDp.getSGSNCapabilities(),
                initialDp.getLocationInformationGPRS(), initialDp.getPDPInitiationType(), initialDp.getExtensions(),
                initialDp.getGSNAddress(), initialDp.getSecondaryPDPContext(), initialDp.getImei());

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDpGprsRequest, null, sequence++));
        clientGprsDialog.send();
    }

    public InitialDpGprsRequest getTestInitialDpGprsRequest() {
        int serviceKey = 2;
        GPRSEventType gprsEventType = GPRSEventType.attachChangeOfPosition;
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");
        IMSI imsi = new IMSIImpl("1111122222");
        TimeAndTimezone timeAndTimezone = new TimeAndTimezoneImpl(2005, 11, 24, 13, 10, 56, 0);

        InitialDpGprsRequestImpl res = new InitialDpGprsRequestImpl(serviceKey, gprsEventType, msisdn, imsi, timeAndTimezone,
                null, null, null, null, null, null, null, null, null, null, null, false, null);

        return res;
    }

    public static boolean checkTestInitialDpGprsRequest(InitialDpGprsRequest ind) {
        if (ind.getServiceKey() != 2)
            return false;
        if (ind.getGPRSEventType() != GPRSEventType.attachChangeOfPosition)
            return false;
        if (ind.getMsisdn() == null)
            return false;
        if (!ind.getMsisdn().getAddress().equals("22234"))
            return false;
        if (ind.getMsisdn().getAddressNature() != AddressNature.international_number)
            return false;
        if (ind.getMsisdn().getNumberingPlan() != NumberingPlan.ISDN)
            return false;
        if (!ind.getImsi().getData().equals("1111122222"))
            return false;
        if (ind.getTimeAndTimezone().getYear() != 2005)
            return false;
        if (ind.getTimeAndTimezone().getMonth() != 11)
            return false;
        if (ind.getTimeAndTimezone().getDay() != 24)
            return false;
        if (ind.getTimeAndTimezone().getHour() != 13)
            return false;
        if (ind.getTimeAndTimezone().getMinute() != 10)
            return false;
        if (ind.getTimeAndTimezone().getSecond() != 56)
            return false;
        if (ind.getTimeAndTimezone().getTimeZone() != 0)
            return false;
        return true;
    }

    public static boolean checkRequestReportGPRSEventRequest(RequestReportGPRSEventRequest ind) {

        if (ind.getGPRSEvent().size() != 1)
            return false;
        if (ind.getGPRSEvent().get(0).getGPRSEventType() == GPRSEventType.attachChangeOfPosition)
            return false;
        if (ind.getGPRSEvent().get(0).getMonitorMode() == MonitorMode.notifyAndContinue)
            return false;
        if (ind.getPDPID().getId() != 2)
            return false;

        return true;
    }

    public void sendApplyChargingReportGPRSRequest() throws CAPException {
        ElapsedTimeImpl elapsedTime = new ElapsedTimeImpl(new Integer(5320));
        ChargingResult chargingResult = new ChargingResultImpl(elapsedTime);
        boolean active = true;
        clientGprsDialog.addApplyChargingReportGPRSRequest(chargingResult, null, active, null, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ApplyChargingReportGPRSRequest, null, sequence++));
        clientGprsDialog.send();
    }

    public void sendActivityTestGPRSRequest(CAPApplicationContext appCnt) throws CAPException {

        clientGprsDialog = this.capProvider.getCAPServiceGprs().createNewDialog(appCnt, this.thisAddress, this.remoteAddress);
        clientGprsDialog.addActivityTestGPRSRequest();
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ActivityTestGPRSRequest, null, sequence++));
        clientGprsDialog.send();
    }

    public void sendEventReportGPRSRequest(CAPApplicationContext appCnt) throws CAPException {

        clientGprsDialog = this.capProvider.getCAPServiceGprs().createNewDialog(appCnt, this.thisAddress, this.remoteAddress);

        GPRSEventType gprsEventType = GPRSEventType.attachChangeOfPosition;
        MiscCallInfo miscGPRSInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.notification, null);
        LAIFixedLengthImpl lai;
        try {
            lai = new LAIFixedLengthImpl(250, 1, 4444);
        } catch (MAPException e) {
            throw new CAPException(e.getMessage(), e);
        }
        CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
        RAIdentityImpl ra = new RAIdentityImpl(new byte[] { 11, 12, 13, 14, 15, 16 });
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 });
        ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "654321");
        LSAIdentityImpl lsa = new LSAIdentityImpl(new byte[] { 91, 92, 93 });
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
        LocationInformationGPRS locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn, lsa, null, true,
                gdi, true, 13);
        GPRSEventSpecificInformation gprsEventSpecificInformation = new GPRSEventSpecificInformationImpl(
                locationInformationGPRS);
        PDPID pdpID = new PDPIDImpl(1);
        clientGprsDialog.addEventReportGPRSRequest(gprsEventType, miscGPRSInfo, gprsEventSpecificInformation, pdpID);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.EventReportGPRSRequest, null, sequence++));
        clientGprsDialog.send();
    }

    public void sendReleaseGPRSRequest(CAPApplicationContext appCnt) throws CAPException {

        clientGprsDialog = this.capProvider.getCAPServiceGprs().createNewDialog(appCnt, this.thisAddress, this.remoteAddress);
        GPRSCause gprsCause = new GPRSCauseImpl(5);
        PDPID pdpID = new PDPIDImpl(2);
        clientGprsDialog.addReleaseGPRSRequest(gprsCause, pdpID);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReleaseGPRSRequest, null, sequence++));
        clientGprsDialog.send();
    }

    public void sendInitialDpSmsRequest(CAPApplicationContext appCnt) throws CAPException {

        clientSmsDialog = this.capProvider.getCAPServiceSms().createNewDialog(appCnt, this.thisAddress, this.remoteAddress);

        CalledPartyBCDNumber destinationSubscriberNumber = this.capParameterFactory.createCalledPartyBCDNumber(AddressNature.international_number, NumberingPlan.ISDN,
                "123678");
        SMSAddressString callingPartyNumber = this.capParameterFactory.createSMSAddressString(AddressNature.international_number, NumberingPlan.ISDN, "123999");
        IMSI imsi = this.mapParameterFactory.createIMSI("12345678901234");
        clientSmsDialog.addInitialDPSMSRequest(15, destinationSubscriberNumber, callingPartyNumber, EventTypeSMS.smsDeliveryRequested, imsi, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null);
        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitialDPSMSRequest, null, sequence++));

        clientSmsDialog.send();
    }

    public void sendInitiateCallAttemptRequest() throws CAPException {

        clientCscDialog = this.capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(CAPApplicationContext.CapV4_scf_gsmSSFGeneric, this.thisAddress,
                this.remoteAddress);

        ArrayList<CalledPartyNumberCap> calledPartyNumberArr = new ArrayList<CalledPartyNumberCap>();
        CalledPartyNumber cpn = this.isupParameterFactory.createCalledPartyNumber();
        cpn.setNatureOfAddresIndicator(3);
        cpn.setAddress("1113330");
        CalledPartyNumberCap cpnCap = this.capParameterFactory.createCalledPartyNumberCap(cpn);
        calledPartyNumberArr.add(cpnCap);
        DestinationRoutingAddress destinationRoutingAddress = this.capParameterFactory.createDestinationRoutingAddress(calledPartyNumberArr);
        clientCscDialog.addInitiateCallAttemptRequest(destinationRoutingAddress, null, null, null, null, null, null, false);

        this.observerdEvents.add(TestEvent.createSentEvent(EventType.InitiateCallAttemptRequest, null, sequence++));
        clientCscDialog.send();
    }

//    public void sendReleaseSmsRequest(CAPApplicationContext appCnt) throws CAPException {
//
//        clientSmsDialog = this.capProvider.getCAPServiceSms().createNewDialog(appCnt, this.thisAddress,
//                this.remoteAddress);
//        RPCause rpCause = new RPCauseImpl(3);
//        clientSmsDialog.addReleaseSMSRequest(rpCause);
//        this.observerdEvents.add(TestEvent.createSentEvent(EventType.ReleaseSMSRequest, null, sequence++));
//        clientSmsDialog.send();
//    }

    public void debug(String message) {
        this.logger.debug(message);
    }

    public void error(String message, Exception e) {
        this.logger.error(message, e);
    }
}
