package org.mobicents.protocols.ss7.map.functional;

/**
 * 
 * @author amit bhayani
 *
 */
public enum EventType {
	// Dialog EventType
	DialogDelimiter, DialogEricssonRequest, DialogRequest, DialogAccept, DialogReject, DialogUserAbort, DialogProviderAbort, DialogClose, DialogNotice, DialogRelease, DialogTimeout,

	//Service EventType
	ErrorComponent, ProviderErrorComponent, RejectComponent, InvokeTimeout, 
	
	// Supplementary EventType
	ProcessUnstructuredSSRequestIndication, ProcessUnstructuredSSResponseIndication, UnstructuredSSRequestIndication, UnstructuredSSResponseIndication, UnstructuredSSNotifyRequestIndication,

	// SMS EventType
	ForwardShortMessageIndication, ForwardShortMessageRespIndication, MoForwardShortMessageIndication, MoForwardShortMessageRespIndication, MtForwardShortMessageIndication, MtForwardShortMessageRespIndication, SendRoutingInfoForSMIndication, SendRoutingInfoForSMRespIndication, ReportSMDeliveryStatusIndication, ReportSMDeliveryStatusRespIndication, InformServiceCentreIndication, AlertServiceCentreIndication, AlertServiceCentreRespIndication;
}
