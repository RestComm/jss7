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

package org.mobicents.protocols.ss7.map.api.service.sms;

import org.mobicents.protocols.ss7.map.api.MAPServiceListener;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPServiceSmsListener extends MAPServiceListener {

    void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd);

    void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd);

    void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd);

    void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd);

    void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd);

    void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd);

    void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd);

    void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd);

    void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd);

    void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd);

    void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreInd);

    void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd);

    void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreInd);

    void onReadyForSMRequest(ReadyForSMRequest request);

    void onReadyForSMResponse(ReadyForSMResponse response);

    void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest request);

}
