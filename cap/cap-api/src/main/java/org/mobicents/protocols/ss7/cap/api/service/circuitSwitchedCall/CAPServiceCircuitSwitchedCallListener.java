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

	public void onInitialDPRequest(InitialDPRequest ind);

	public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind);

	public void onApplyChargingRequest(ApplyChargingRequest ind);

	public void onEventReportBCSMRequest(EventReportBCSMRequest ind);

	public void onContinueRequest(ContinueRequest ind);

	public void onApplyChargingReportRequest(ApplyChargingReportRequest ind);

	public void onReleaseCallRequest(ReleaseCallRequest ind);

	public void onConnectRequest(ConnectRequest ind);

	public void onCallInformationRequestRequest(CallInformationRequestRequest ind);

	public void onCallInformationReportRequest(CallInformationReportRequest ind);

	public void onActivityTestRequest(ActivityTestRequest ind);

	public void onActivityTestResponse(ActivityTestResponse ind);

	public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind);

	public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind);

	public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind);

	public void onConnectToResourceRequest(ConnectToResourceRequest ind);

	public void onResetTimerRequest(ResetTimerRequest ind);

	public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind);

	public void onSendChargingInformationRequest(SendChargingInformationRequest ind);

	public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind);

	public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind);

	public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind);

	public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind);

	public void onCancelRequest(CancelRequest ind);

}
