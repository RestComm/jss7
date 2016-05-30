package org.mobicents.protocols.ss7.map.functional;

/**
 *
 * @author amit bhayani
 *
 */
public enum EventType {
    // Dialog EventType
    DialogDelimiter, DialogEricssonRequest, DialogRequest, DialogAccept, DialogReject, DialogUserAbort, DialogProviderAbort, DialogClose, DialogNotice, DialogRelease, DialogTimeout,

    // Service EventType
    ErrorComponent, ProviderErrorComponent, RejectComponent, InvokeTimeout,

    // Supplementary EventType
    RegisterSS, RegisterSSResp, EraseSS, EraseSSResp, ActivateSS, ActivateSSResp, DeactivateSS, DeactivateSSResp, InterrogateSS, InterrogateSSResp, ProcessUnstructuredSSRequestIndication, ProcessUnstructuredSSResponseIndication, UnstructuredSSRequestIndication, UnstructuredSSResponseIndication, UnstructuredSSNotifyRequestIndication, GetPassword, GetPasswordResp, RegisterPassword, RegisterPasswordResp,

    // SMS EventType
    ForwardShortMessageIndication, ForwardShortMessageRespIndication, MoForwardShortMessageIndication, MoForwardShortMessageRespIndication, MtForwardShortMessageIndication, MtForwardShortMessageRespIndication, SendRoutingInfoForSMIndication, SendRoutingInfoForSMRespIndication, ReportSMDeliveryStatusIndication, ReportSMDeliveryStatusRespIndication, InformServiceCentreIndication, AlertServiceCentreIndication, AlertServiceCentreRespIndication, ReadyForSM, ReadyForSMResp, NoteSubscriberPresent,

    // mobility.authentication EventType
    SendAuthenticationInfo_V3, SendAuthenticationInfo_V2, SendAuthenticationInfoResp_V2, SendAuthenticationInfoResp_V3, AuthenticationFailureReport, AuthenticationFailureReport_Resp,

    // mobility.locationManagement EventType
    UpdateLocation, UpdateLocationResp, CancelLocation, CancelLocationResp, SendIdentification, SendIdentificationResp, UpdateGprsLocation, UpdateGprsLocationResp, PurgeMS, PurgeMSResp,
    
    // mobility.faultRecovery EventType
    Reset, ForwardCheckSSIndication, RestoreData, RestoreDataResp,

    // mobility.subscriberInformation EventType
    AnyTimeInterrogation, AnyTimeInterrogationResp, AnyTimeSubscriptionInterrogation, AnyTimeSubscriptionInterrogationRes, ProvideSubscriberInfo, ProvideSubscriberInfoResp,

    // mobility.SubscriberManagementServices EventType
    InsertSubscriberData, InsertSubscriberDataResp, DeleteSubscriberData, DeleteSubscriberDataResp,

    // mobility.imei EventType
    CheckImei, CheckImeiResp,

    // Call Handling Service
    ProvideRoamingNumber, ProvideRoamingNumberResp, SendRoutingInformation, SendRoutingInformationResp, IstCommand, IstCommandResp,

    // lsm EventType
    ProvideSubscriberLocation, ProvideSubscriberLocationResp, SubscriberLocationReport, SubscriberLocationReportResp, SendRoutingInfoForLCS, SendRoutingInfoForLCSResp,

    // oam Service
    SendImsi, SendImsiResp, ActivateTraceMode, ActivateTraceModeResp,

    // PdpContextActivation Service
    SendRoutingInfoForGprs, SendRoutingInfoForGprsResp;
}
