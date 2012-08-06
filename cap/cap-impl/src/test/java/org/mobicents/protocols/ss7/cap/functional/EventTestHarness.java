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

package org.mobicents.protocols.ss7.cap.functional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPDialogListener;
import org.mobicents.protocols.ss7.cap.api.CAPMessage;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPComponentErrorReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCallListener;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationRequestRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 * 
 * @author amit bhayani
 * @author servey vetyutnev
 *
 */
public class EventTestHarness implements CAPDialogListener, CAPServiceCircuitSwitchedCallListener {
	
	private Logger logger = null;

	protected List<TestEvent> observerdEvents = new ArrayList<TestEvent>();
	protected int sequence = 0;

	EventTestHarness(Logger logger){
		this.logger = logger;
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
	@Override
	public void onDialogDelimiter(CAPDialog capDialog) {
		this.logger.debug("onDialogDelimiter");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
		this.logger.debug("onDialogRequest");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRequest, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
		this.logger.debug("onDialogAccept");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogAccept, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
		this.logger.debug("onDialogUserAbort");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
		this.logger.debug("onDialogProviderAbort");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onDialogClose(CAPDialog capDialog) {
		this.logger.debug("onDialogClose");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogClose, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onDialogRelease(CAPDialog capDialog) {
		this.logger.debug("onDialogRelease");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRelease, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onDialogTimeout(CAPDialog capDialog) {
		this.logger.debug("onDialogTimeout");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogTimeout, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		this.logger.debug("onDialogNotice");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogNotice, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onErrorComponent(CAPDialog capDialog, Long invokeId, CAPErrorMessage capErrorMessage) {
		this.logger.debug("onErrorComponent");
		TestEvent te = TestEvent.createReceivedEvent(EventType.ErrorComponent, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onRejectComponent(CAPDialog capDialog, Long invokeId, Problem problem) {
		this.logger.debug("onRejectComponent");
		TestEvent te = TestEvent.createReceivedEvent(EventType.RejectComponent, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onInvokeTimeout(CAPDialog capDialog, Long invokeId) {
		this.logger.debug("onInvokeTimeout");
		TestEvent te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onProviderErrorComponent(CAPDialog capDialog, Long invokeId, CAPComponentErrorReason providerError) {
		this.logger.debug("onProviderErrorComponent");
		TestEvent te = TestEvent.createReceivedEvent(EventType.ProviderErrorComponent, capDialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onCAPMessage(CAPMessage capMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitialDPRequest(InitialDPRequest ind) {
		this.logger.debug("onInitialDPRequestIndication");
		TestEvent te = TestEvent.createReceivedEvent(EventType.InitialDpIndication, ind, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onApplyChargingRequest(ApplyChargingRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContinueRequest(ContinueRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onApplyChargingReportRequest(ApplyChargingReportRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReleaseCallRequest(ReleaseCallRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectRequest(ConnectRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallInformationRequestRequest(CallInformationRequestRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallInformationReportRequest(CallInformationReportRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityTestRequest(ActivityTestRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityTestResponse(ActivityTestResponse ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectToResourceRequest(ConnectToResourceRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResetTimerRequest(ResetTimerRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendChargingInformationRequest(SendChargingInformationRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelRequest(CancelRequest ind) {
		// TODO Auto-generated method stub
		
	}

}
