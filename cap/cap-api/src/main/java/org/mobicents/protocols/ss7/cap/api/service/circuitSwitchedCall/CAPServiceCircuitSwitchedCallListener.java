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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.cap.api.CAPServiceListener;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface CAPServiceCircuitSwitchedCallListener extends CAPServiceListener {

	public void onInitialDPRequestIndication(InitialDPRequest ind);

	public void onRequestReportBCSMEventRequestIndication(RequestReportBCSMEventRequest ind);

	public void onApplyChargingRequestIndication(ApplyChargingRequest ind);

	public void onEventReportBCSMRequestIndication(EventReportBCSMRequest ind);

	public void onContinueRequestIndication(ContinueRequest ind);

	public void onApplyChargingReportRequestIndication(ApplyChargingReportRequest ind);

	public void onReleaseCalltRequestIndication(ReleaseCallRequest ind);

	public void onConnectRequestIndication(ConnectRequest ind);

	public void onCallInformationRequestRequestIndication(CallInformationRequestRequest ind);

	public void onCallInformationReportRequestIndication(CallInformationReportRequest ind);

	public void onActivityTestRequestIndication(ActivityTestRequest ind);

	public void onActivityTestResponseIndication(ActivityTestResponse ind);

	public void onAssistRequestInstructionsRequestIndication(AssistRequestInstructionsRequest ind);

	public void onEstablishTemporaryConnectionRequestIndication(EstablishTemporaryConnectionRequest ind);

	public void onDisconnectForwardConnectionRequestIndication(DisconnectForwardConnectionRequest ind);

	public void onConnectToResourceRequestIndication(ConnectToResourceRequest ind);

	public void onResetTimerRequestIndication(ResetTimerRequest ind);

	public void onFurnishChargingInformationRequestIndication(FurnishChargingInformationRequest ind);

	public void onSendChargingInformationRequestIndication(SendChargingInformationRequest ind);

	public void onSpecializedResourceReportRequestIndication(SpecializedResourceReportRequest ind);

	public void onPlayAnnouncementRequestIndication(PlayAnnouncementRequest ind);

	public void onPromptAndCollectUserInformationRequestIndication(PromptAndCollectUserInformationRequest ind);

	public void onPromptAndCollectUserInformationResponseIndication(PromptAndCollectUserInformationResponse ind);

	public void onCancelRequestIndication(CancelRequest ind);

}
