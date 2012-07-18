package org.mobicents.protocols.ss7.map.functional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
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
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 * 
 * @author amit bhayani
 *
 */
public class EventTestHarness implements MAPDialogListener, MAPServiceSupplementaryListener, MAPServiceSmsListener, MAPServiceMobilityListener {
	
	private Logger logger = null;

	protected List<TestEvent> observerdEvents = new ArrayList<TestEvent>();
	protected int sequence = 0;
	
	EventTestHarness(Logger logger){
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

	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
		this.logger.debug("onDialogReject");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogReject, mapDialog, sequence++);
		this.observerdEvents.add(te);
	}

	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason,
			MAPExtensionContainer extensionContainer) {
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

	public void onProviderErrorComponent(MAPDialog mapDialog, Long invokeId, MAPProviderError providerError) {
		this.logger.debug("onProviderErrorComponent");
		TestEvent te = TestEvent.createReceivedEvent(EventType.ProviderErrorComponent, mapDialog, sequence++);
		this.observerdEvents.add(te);
	}

	public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem) {
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
		TestEvent te = TestEvent.createReceivedEvent(EventType.ProcessUnstructuredSSResponseIndication,
				procUnstrResInd, sequence++);
		this.observerdEvents.add(te);
	}

	public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
		this.logger.debug("onUnstructuredSSRequest");
		TestEvent te = TestEvent
				.createReceivedEvent(EventType.UnstructuredSSRequestIndication, unstrReqInd, sequence++);
		this.observerdEvents.add(te);
	}

	public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
		this.logger.debug("onUnstructuredSSResponse");
		TestEvent te = TestEvent.createReceivedEvent(EventType.UnstructuredSSResponseIndication, unstrResInd,
				sequence++);
		this.observerdEvents.add(te);
	}

	public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
		this.logger.debug("onUnstructuredSSNotifyRequest");
		TestEvent te = TestEvent.createReceivedEvent(EventType.UnstructuredSSNotifyRequestIndication, unstrNotifyInd,
				sequence++);
		this.observerdEvents.add(te);
	}
	
	public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd){
		this.logger.debug("onUnstructuredSSNotifyResponse");
		
	}

	public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
		this.logger.debug("onForwardShortMessageRequest");
		TestEvent te = TestEvent.createReceivedEvent(EventType.ForwardShortMessageIndication, forwSmInd, sequence++);
		this.observerdEvents.add(te);
	}

	public void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd) {
		this.logger.debug("onForwardShortMessageResponse");
		TestEvent te = TestEvent.createReceivedEvent(EventType.ForwardShortMessageRespIndication, forwSmRespInd,
				sequence++);
		this.observerdEvents.add(te);
	}

	public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
		this.logger.debug("onMoForwardShortMessageRequest");
		TestEvent te = TestEvent
				.createReceivedEvent(EventType.MoForwardShortMessageIndication, moForwSmInd, sequence++);
		this.observerdEvents.add(te);
	}

	public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
		this.logger.debug("onMoForwardShortMessageResponse");
		TestEvent te = TestEvent.createReceivedEvent(EventType.MoForwardShortMessageRespIndication, moForwSmRespInd,
				sequence++);
		this.observerdEvents.add(te);
	}

	public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {
		this.logger.debug("onMtForwardShortMessageRequest");
		TestEvent te = TestEvent
				.createReceivedEvent(EventType.MtForwardShortMessageIndication, mtForwSmInd, sequence++);
		this.observerdEvents.add(te);
	}

	public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {
		this.logger.debug("onMtForwardShortMessageResponse");
		TestEvent te = TestEvent.createReceivedEvent(EventType.MtForwardShortMessageRespIndication, mtForwSmRespInd,
				sequence++);
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
		TestEvent te = TestEvent.createReceivedEvent(EventType.SendRoutingInfoForSMRespIndication,
				sendRoutingInfoForSMRespInd, sequence++);
		this.observerdEvents.add(te);
	}

	public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
		this.logger.debug("onReportSMDeliveryStatusRequest");
		TestEvent te = TestEvent.createReceivedEvent(EventType.ReportSMDeliveryStatusIndication,
				reportSMDeliveryStatusInd, sequence++);
		this.observerdEvents.add(te);
	}

	public void onReportSMDeliveryStatusResponse(
			ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {
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
		TestEvent te = TestEvent.createReceivedEvent(EventType.AlertServiceCentreIndication, alertServiceCentreInd,
				sequence++);
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
			fail("Size of received events: " + this.observerdEvents.size() + ", does not equal expected events: " + expectedEvents.size() + "\n"
					+ doStringCompare(expectedEvents, observerdEvents));
		}

		for (int index = 0; index < expectedEvents.size(); index++) {
			assertEquals(expectedEvents.get(index), observerdEvents.get(index),"Received event does not match, index[" + index + "]");
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

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.MAPServiceListener#onMAPMessage(org.mobicents.protocols.ss7.map.api.MAPMessage)
	 */
	public void onMAPMessage(MAPMessage mapMessage) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.MAPDialogListener#onDialogRequestEricsson(org.mobicents.protocols.ss7.map.api.MAPDialog, org.mobicents.protocols.ss7.map.api.primitives.AddressString, org.mobicents.protocols.ss7.map.api.primitives.AddressString, org.mobicents.protocols.ss7.map.api.primitives.IMSI, org.mobicents.protocols.ss7.map.api.primitives.AddressString)
	 */
	public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference, IMSI eriImsi, AddressString eriVlrNo) {
		this.logger.debug("onDialogRequestEricsson");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogEricssonRequest, mapDialog, sequence++);
		this.observerdEvents.add(te);		
	}

	public void onUpdateLocationRequest(UpdateLocationRequest ind) {
		this.logger.debug("onUpdateLocationRequest");
		TestEvent te = TestEvent.createReceivedEvent(EventType.UpdateLocation, ind,
				sequence++);
		this.observerdEvents.add(te);
	}

	public void onUpdateLocationResponse(UpdateLocationResponse ind) {
		this.logger.debug("onUpdateLocationResponse");
		TestEvent te = TestEvent.createReceivedEvent(EventType.UpdateLocationResp, ind,
				sequence++);
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

	public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest request) {
		this.logger.debug("onAnyTimeInterrogationRequest");
		TestEvent te = TestEvent.createReceivedEvent(EventType.AnyTimeInterrogation, request,
				sequence++);
		this.observerdEvents.add(te);
	}

	public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse response) {
		this.logger.debug("onAnyTimeInterrogationResponse");
		TestEvent te = TestEvent.createReceivedEvent(EventType.AnyTimeInterrogationResp, response, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onCheckImeiRequest(CheckImeiRequest request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckImeiResponse(CheckImeiResponse response) {
		// TODO Auto-generated method stub
		
	}
}

