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

	public void onInitialDPRequestIndication(InitialDPRequestIndication ind);

	public void onRequestReportBCSMEventRequestIndication(RequestReportBCSMEventRequestIndication ind);

	public void onApplyChargingRequestIndication(ApplyChargingRequestIndication ind);

	public void onEventReportBCSMRequestIndication(EventReportBCSMRequestIndication ind);

	public void onContinueRequestIndication(ContinueRequestIndication ind);

	public void onApplyChargingReportRequestIndication(ApplyChargingReportRequestIndication ind);

	public void onReleaseCalltRequestIndication(ReleaseCallRequestIndication ind);

	public void onConnectRequestIndication(ConnectRequestIndication ind);

	public void onCallInformationRequestRequestIndication(CallInformationRequestRequestIndication ind);

	public void onCallInformationReportRequestIndication(CallInformationReportRequestIndication ind);

	public void onActivityTestRequestIndication(ActivityTestRequestIndication ind);

	public void onActivityTestResponseIndication(ActivityTestResponseIndication ind);

	public void onAssistRequestInstructionsRequestIndication(AssistRequestInstructionsRequestIndication ind);

	public void onEstablishTemporaryConnectionRequestIndication(EstablishTemporaryConnectionRequestIndication ind);

	public void onDisconnectForwardConnectionRequestIndication(DisconnectForwardConnectionRequestIndication ind);

	public void onConnectToResourceRequestIndication(ConnectToResourceRequestIndication ind);

	public void onResetTimerRequestIndication(ResetTimerRequestIndication ind);

	public void onFurnishChargingInformationRequestIndication(FurnishChargingInformationRequestIndication ind);

	public void onSendChargingInformationRequestIndication(SendChargingInformationRequestIndication ind);

	public void onSpecializedResourceReportRequestIndication(SpecializedResourceReportRequestIndication ind);

	public void onPlayAnnouncementRequestIndication(PlayAnnouncementRequestIndication ind);

	public void onPromptAndCollectUserInformationRequestIndication(PromptAndCollectUserInformationRequestIndication ind);

	public void onPromptAndCollectUserInformationResponseIndication(PromptAndCollectUserInformationResponseIndication ind);

	public void onCancelRequestIndication(CancelRequestIndication ind);

}
