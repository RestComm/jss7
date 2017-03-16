package org.mobicents.protocols.ss7.map.functional;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.IstCommandRequest;
import org.mobicents.protocols.ss7.map.api.service.callhandling.IstCommandResponse;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandlingListener;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberRequest;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberResponse;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.mobicents.protocols.ss7.map.api.service.oam.ActivateTraceModeRequest_Oam;
import org.mobicents.protocols.ss7.map.api.service.oam.ActivateTraceModeResponse_Oam;
import org.mobicents.protocols.ss7.map.api.service.oam.MAPServiceOamListener;
import org.mobicents.protocols.ss7.map.api.service.oam.SendImsiRequest;
import org.mobicents.protocols.ss7.map.api.service.oam.SendImsiResponse;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivationListener;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsRequest;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReadyForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ActivateSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ActivateSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.DeactivateSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.DeactivateSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.EraseSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.EraseSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GetPasswordRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GetPasswordResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.InterrogateSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterPasswordRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterPasswordResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 *
 * @author amit bhayani
 *
 */
public class EventTestHarness implements MAPDialogListener, MAPServiceSupplementaryListener, MAPServiceSmsListener, MAPServiceMobilityListener,
        MAPServiceLsmListener, MAPServiceCallHandlingListener, MAPServiceOamListener, MAPServicePdpContextActivationListener {

    private Logger logger = null;

    protected List<TestEvent> observerdEvents = new ArrayList<TestEvent>();
    protected int sequence = 0;

    EventTestHarness(Logger logger) {
        this.logger = logger;
    }

    public void onDialogDelimiter(MAPDialog mapDialog) {
        this.logger.debug("onDialogDelimiter");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            MAPExtensionContainer extensionContainer) {
        this.logger.debug("onDialogRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRequest, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
        this.logger.debug("onDialogAccept");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogAccept, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
            ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
        this.logger.debug("onDialogReject");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogReject, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
        this.logger.debug("onDialogUserAbort");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
            MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
        this.logger.debug("onDialogProviderAbort");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onDialogClose(MAPDialog mapDialog) {
        this.logger.debug("onDialogClose");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogClose, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        this.logger.debug("onDialogNotice");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogNotice, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onDialogRelease(MAPDialog mapDialog) {
        this.logger.debug("onDialogRelease");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRelease, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onDialogTimeout(MAPDialog mapDialog) {
        this.logger.debug("onDialogTimeout");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogTimeout, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
        this.logger.debug("onErrorComponent");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ErrorComponent, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        this.logger.debug("onRejectComponent");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RejectComponent, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
        this.logger.debug("onInvokeTimeout");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
        this.logger.debug("onProcessUnstructuredSSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSRequestIndication, procUnstrReqInd,
                sequence++);
        this.observerdEvents.add(te);
    }

    public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResInd) {
        this.logger.debug("onProcessUnstructuredSSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSResponseIndication, procUnstrResInd,
                sequence++);
        this.observerdEvents.add(te);
    }

    public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
        this.logger.debug("onUnstructuredSSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.UnstructuredSSRequestIndication, unstrReqInd, sequence++);
        this.observerdEvents.add(te);
    }

    public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
        this.logger.debug("onUnstructuredSSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.UnstructuredSSResponseIndication, unstrResInd, sequence++);
        this.observerdEvents.add(te);
    }

    public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
        this.logger.debug("onUnstructuredSSNotifyRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.UnstructuredSSNotifyRequestIndication, unstrNotifyInd,
                sequence++);
        this.observerdEvents.add(te);
    }

    public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
        this.logger.debug("onUnstructuredSSNotifyResponse");

    }

    public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
        this.logger.debug("onForwardShortMessageRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ForwardShortMessageIndication, forwSmInd, sequence++);
        this.observerdEvents.add(te);
    }

    public void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd) {
        this.logger.debug("onForwardShortMessageResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ForwardShortMessageRespIndication, forwSmRespInd, sequence++);
        this.observerdEvents.add(te);
    }

    public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
        this.logger.debug("onMoForwardShortMessageRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageIndication, moForwSmInd, sequence++);
        this.observerdEvents.add(te);
    }

    public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
        this.logger.debug("onMoForwardShortMessageResponse");
        TestEvent te = TestEvent
                .createReceivedEvent(EventType.MoForwardShortMessageRespIndication, moForwSmRespInd, sequence++);
        this.observerdEvents.add(te);
    }

    public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {
        this.logger.debug("onMtForwardShortMessageRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.MtForwardShortMessageIndication, mtForwSmInd, sequence++);
        this.observerdEvents.add(te);
    }

    public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {
        this.logger.debug("onMtForwardShortMessageResponse");
        TestEvent te = TestEvent
                .createReceivedEvent(EventType.MtForwardShortMessageRespIndication, mtForwSmRespInd, sequence++);
        this.observerdEvents.add(te);
    }

    public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {
        this.logger.debug("onSendRoutingInfoForSMRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForSMIndication, sendRoutingInfoForSMInd,
                sequence++);
        this.observerdEvents.add(te);
    }

    public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd) {
        this.logger.debug("onSendRoutingInfoForSMResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForSMRespIndication, sendRoutingInfoForSMRespInd,
                sequence++);
        this.observerdEvents.add(te);
    }

    public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
        this.logger.debug("onReportSMDeliveryStatusRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusIndication, reportSMDeliveryStatusInd,
                sequence++);
        this.observerdEvents.add(te);
    }

    public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {
        this.logger.debug("onReportSMDeliveryStatusResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusRespIndication,
                reportSMDeliveryStatusRespInd, sequence++);
        this.observerdEvents.add(te);
    }

    public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreInd) {
        this.logger.debug("onInformServiceCentreRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InformServiceCentreIndication, informServiceCentreInd,
                sequence++);
        this.observerdEvents.add(te);
    }

    public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {
        this.logger.debug("onAlertServiceCentreRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.AlertServiceCentreIndication, alertServiceCentreInd, sequence++);
        this.observerdEvents.add(te);
    }

    public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreInd) {
        this.logger.debug("onAlertServiceCentreResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.AlertServiceCentreRespIndication, alertServiceCentreInd,
                sequence++);
        this.observerdEvents.add(te);
    }

    public void compareEvents(List<TestEvent> expectedEvents) {
        if (expectedEvents.size() != this.observerdEvents.size()) {
            fail("Size of received events: " + this.observerdEvents.size() + ", does not equal expected events: "
                    + expectedEvents.size() + "\n" + doStringCompare(expectedEvents, observerdEvents));
        }

        for (int index = 0; index < expectedEvents.size(); index++) {
            assertEquals(expectedEvents.get(index), observerdEvents.get(index), "Received event does not match, index[" + index
                    + "]");
        }
    }

    protected String doStringCompare(List expectedEvents, List observerdEvents) {
        StringBuilder sb = new StringBuilder();
        int size1 = expectedEvents.size();
        int size2 = observerdEvents.size();
        int count = size1;
        if (count < size2) {
            count = size2;
        }

        for (int index = 0; count > index; index++) {
            String s1 = size1 > index ? expectedEvents.get(index).toString() : "NOP";
            String s2 = size2 > index ? observerdEvents.get(index).toString() : "NOP";
            sb.append(s1).append(" - ").append(s2).append("\n\n");
        }
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPServiceListener#onMAPMessage(org.mobicents.protocols.ss7.map.api.MAPMessage)
     */
    public void onMAPMessage(MAPMessage mapMessage) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogRequestEricsson(org.mobicents.protocols.ss7.map.api.MAPDialog
     * , org.mobicents.protocols.ss7.map.api.primitives.AddressString,
     * org.mobicents.protocols.ss7.map.api.primitives.AddressString, org.mobicents.protocols.ss7.map.api.primitives.IMSI,
     * org.mobicents.protocols.ss7.map.api.primitives.AddressString)
     */
    public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            AddressString eriImsi, AddressString eriVlrNo) {
        this.logger.debug("onDialogRequestEricsson");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogEricssonRequest, mapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    public void onUpdateLocationRequest(UpdateLocationRequest ind) {
        this.logger.debug("onUpdateLocationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.UpdateLocation, ind, sequence++);
        this.observerdEvents.add(te);
    }

    public void onUpdateLocationResponse(UpdateLocationResponse ind) {
        this.logger.debug("onUpdateLocationResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.UpdateLocationResp, ind, sequence++);
        this.observerdEvents.add(te);
    }

    public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind) {
        TestEvent te;
        if (ind.getMapProtocolVersion() >= 3) {
            this.logger.debug("onSendAuthenticationInfoRequest_V3");
            te = TestEvent.createReceivedEvent(EventType.SendAuthenticationInfo_V3, ind, sequence++);
        } else {
            this.logger.debug("onSendAuthenticationInfoRequest_V2");
            te = TestEvent.createReceivedEvent(EventType.SendAuthenticationInfo_V2, ind, sequence++);
        }
        this.observerdEvents.add(te);
    }

    public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind) {
        TestEvent te;
        if (ind.getMapProtocolVersion() >= 3) {
            this.logger.debug("onSendAuthenticationInfoResp_V3");
            te = TestEvent.createReceivedEvent(EventType.SendAuthenticationInfoResp_V3, ind, sequence++);
        } else {
            this.logger.debug("onSendAuthenticationInfoResp_V2");
            te = TestEvent.createReceivedEvent(EventType.SendAuthenticationInfoResp_V2, ind, sequence++);
        }
        this.observerdEvents.add(te);
    }

    @Override
    public void onAuthenticationFailureReportRequest(AuthenticationFailureReportRequest request) {
        this.logger.debug("onAuthenticationFailureReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.AuthenticationFailureReport, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onAuthenticationFailureReportResponse(AuthenticationFailureReportResponse response) {
        this.logger.debug("onAuthenticationFailureReportResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.AuthenticationFailureReport_Resp, response, sequence++);
        this.observerdEvents.add(te);
    }

    public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest request) {
        this.logger.debug("onAnyTimeInterrogationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.AnyTimeInterrogation, request, sequence++);
        this.observerdEvents.add(te);
    }

    public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse response) {
        this.logger.debug("onAnyTimeInterrogationResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.AnyTimeInterrogationResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    public void onAnyTimeSubscriptionInterrogationRequest(AnyTimeSubscriptionInterrogationRequest request) {
        this.logger.debug("onAnyTimeSubscriptionInterrogationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.AnyTimeSubscriptionInterrogation, request, sequence++);
        this.observerdEvents.add(te);
    }

    public void onAnyTimeSubscriptionInterrogationResponse(AnyTimeSubscriptionInterrogationResponse response) {
        this.logger.debug("onAnyTimeSubscriptionInterrogationResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.AnyTimeSubscriptionInterrogationRes, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest request) {
        this.logger.debug("onProvideSubscriberInfoRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ProvideSubscriberInfo, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse response) {
        this.logger.debug("onProvideSubscriberInfoResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ProvideSubscriberInfoResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCheckImeiRequest(CheckImeiRequest request) {
        this.logger.debug("onCheckImeiRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CheckImei, request, sequence++);
        this.observerdEvents.add(te);

    }

    @Override
    public void onCheckImeiResponse(CheckImeiResponse response) {
        this.logger.debug("onCheckImeiResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CheckImeiResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest request) {
        this.logger.debug("onProvideSubscriberLocationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ProvideSubscriberLocation, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onProvideSubscriberLocationResponse(ProvideSubscriberLocationResponse response) {
        this.logger.debug("onProvideSubscriberLocationResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ProvideSubscriberLocationResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest request) {
        this.logger.debug("onSubscriberLocationReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SubscriberLocationReport, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse response) {
        this.logger.debug("onSubscriberLocationReportResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SubscriberLocationReportResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest request) {
        this.logger.debug("onSendRoutingInforForLCSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForLCS, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse response) {
        this.logger.debug("onSendRoutingInforForLCSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForLCSResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCancelLocationRequest(CancelLocationRequest request) {
        this.logger.debug("onCancelLocationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CancelLocation, request, sequence++);
        this.observerdEvents.add(te);

    }

    @Override
    public void onCancelLocationResponse(CancelLocationResponse response) {
        this.logger.debug("onCancelLocationResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CancelLocationResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onProvideRoamingNumberRequest(ProvideRoamingNumberRequest request) {
        this.logger.debug("onProvideRoamingNumberRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ProvideRoamingNumber, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onProvideRoamingNumberResponse(ProvideRoamingNumberResponse response) {
        this.logger.debug("onProvideRoamingNumberResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ProvideRoamingNumberResp, response, sequence++);
        this.observerdEvents.add(te);

    }

    @Override
    public void onSendRoutingInformationRequest(SendRoutingInformationRequest request) {
        this.logger.debug("onSendRoutingInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendRoutingInformation, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSendRoutingInformationResponse(SendRoutingInformationResponse response) {
        this.logger.debug("onSendRoutingInformationResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendRoutingInformationResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest request) {
        this.logger.debug("onInsertSubscriberDataRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InsertSubscriberData, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse response) {
        this.logger.debug("onInsertSubscriberDataResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InsertSubscriberDataResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest request) {
        this.logger.debug("onDeleteSubscriberDataRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DeleteSubscriberData, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse response) {
        this.logger.debug("onDeleteSubscriberDataResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DeleteSubscriberDataResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSendIdentificationRequest(SendIdentificationRequest request) {
        this.logger.debug("onSendIdentificationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendIdentification, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSendIdentificationResponse(SendIdentificationResponse response) {
        this.logger.debug("onSendIdentificationResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendIdentificationResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onUpdateGprsLocationRequest(UpdateGprsLocationRequest request) {
        this.logger.debug("onUpdateGprsLocationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.UpdateGprsLocation, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onUpdateGprsLocationResponse(UpdateGprsLocationResponse response) {
        this.logger.debug("onUpdateGprsLocationResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.UpdateGprsLocationResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onPurgeMSRequest(PurgeMSRequest request) {
        this.logger.debug("onPurgeMSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.PurgeMS, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onPurgeMSResponse(PurgeMSResponse response) {
        this.logger.debug("onPurgeMSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.PurgeMSResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onResetRequest(ResetRequest request) {
        this.logger.debug("onResetRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.Reset, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onForwardCheckSSIndicationRequest(ForwardCheckSSIndicationRequest request) {
        this.logger.debug("ForwardCheckSSIndicationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ForwardCheckSSIndication, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onRestoreDataRequest(RestoreDataRequest request) {
        this.logger.debug("onRestoreDataRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RestoreData, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onRestoreDataResponse(RestoreDataResponse response) {
        this.logger.debug("onRestoreDataResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RestoreDataResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSendImsiRequest(SendImsiRequest request) {
        this.logger.debug("onSendImsiRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendImsi, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSendImsiResponse(SendImsiResponse response) {
        this.logger.debug("onSendImsiResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendImsiResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onRegisterSSRequest(RegisterSSRequest request) {
        this.logger.debug("onRegisterSSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RegisterSS, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onRegisterSSResponse(RegisterSSResponse response) {
        this.logger.debug("onRegisterSSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RegisterSSResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onEraseSSRequest(EraseSSRequest request) {
        this.logger.debug("onEraseSSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.EraseSS, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onEraseSSResponse(EraseSSResponse response) {
        this.logger.debug("onEraseSSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.EraseSSResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onActivateSSRequest(ActivateSSRequest request) {
        this.logger.debug("onActivateSSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivateSS, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onActivateSSResponse(ActivateSSResponse response) {
        this.logger.debug("onActivateSSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivateSSResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDeactivateSSRequest(DeactivateSSRequest request) {
        this.logger.debug("onDeactivateSSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DeactivateSS, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDeactivateSSResponse(DeactivateSSResponse response) {
        this.logger.debug("onDeactivateSSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DeactivateSSResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onInterrogateSSRequest(InterrogateSSRequest request) {
        this.logger.debug("onInterrogateSSRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InterrogateSS, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onInterrogateSSResponse(InterrogateSSResponse response) {
        this.logger.debug("onInterrogateSSResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InterrogateSSResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onReadyForSMRequest(ReadyForSMRequest request) {
        this.logger.debug("onReadyForSMRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ReadyForSM, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onReadyForSMResponse(ReadyForSMResponse response) {
        this.logger.debug("onReadyForSMResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ReadyForSMResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest request) {
        this.logger.debug("onNoteSubscriberPresentRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.NoteSubscriberPresent, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSendRoutingInfoForGprsRequest(SendRoutingInfoForGprsRequest request) {
        this.logger.debug("onSendRoutingInfoForGprsRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForGprs, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSendRoutingInfoForGprsResponse(SendRoutingInfoForGprsResponse response) {
        this.logger.debug("onSendRoutingInfoForGprsResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForGprsResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onActivateTraceModeRequest_Oam(ActivateTraceModeRequest_Oam request) {
        this.logger.debug("onActivateTraceModeRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivateTraceMode, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onActivateTraceModeResponse_Oam(ActivateTraceModeResponse_Oam response) {
        this.logger.debug("onActivateTraceModeResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivateTraceModeResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onActivateTraceModeRequest_Mobility(ActivateTraceModeRequest_Mobility request) {
        this.logger.debug("onActivateTraceModeRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivateTraceMode, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onActivateTraceModeResponse_Mobility(ActivateTraceModeResponse_Mobility response) {
        this.logger.debug("onActivateTraceModeResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivateTraceModeResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onGetPasswordRequest(GetPasswordRequest request) {
        this.logger.debug("onGetPasswordRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.GetPassword, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onGetPasswordResponse(GetPasswordResponse response) {
        this.logger.debug("onGetPasswordResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.GetPasswordResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onRegisterPasswordRequest(RegisterPasswordRequest request) {
        this.logger.debug("onRegisterPasswordRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RegisterPassword, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onRegisterPasswordResponse(RegisterPasswordResponse response) {
        this.logger.debug("onRegisterPasswordResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RegisterPasswordResp, response, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onIstCommandRequest(IstCommandRequest request) {
        this.logger.debug("onSendIstCommandRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.IstCommand, request, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onIstCommandResponse(IstCommandResponse response) {
        this.logger.debug("onSendIstCommandResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.IstCommandResp, response, sequence++);
        this.observerdEvents.add(te);
    }
}
