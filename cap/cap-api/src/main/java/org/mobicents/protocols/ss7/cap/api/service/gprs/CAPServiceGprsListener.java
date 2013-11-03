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

package org.mobicents.protocols.ss7.cap.api.service.gprs;

import org.mobicents.protocols.ss7.cap.api.CAPServiceListener;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface CAPServiceGprsListener extends CAPServiceListener {

    void onInitialDpGprsRequest(InitialDpGprsRequest ind);

    void onRequestReportGPRSEventRequest(RequestReportGPRSEventRequest ind);

    void onApplyChargingGPRSRequest(ApplyChargingGPRSRequest ind);

    void onEntityReleasedGPRSRequest(EntityReleasedGPRSRequest ind);

    void onEntityReleasedGPRSResponse(EntityReleasedGPRSResponse ind);

    void onConnectGPRSRequest(ConnectGPRSRequest ind);

    void onContinueGPRSRequest(ContinueGPRSRequest ind);

    void onReleaseGPRSRequest(ReleaseGPRSRequest ind);

    void onResetTimerGPRSRequest(ResetTimerGPRSRequest ind);

    void onFurnishChargingInformationGPRSRequest(FurnishChargingInformationGPRSRequest ind);

    void onCancelGPRSRequest(CancelGPRSRequest ind);

    void onSendChargingInformationGPRSRequest(SendChargingInformationGPRSRequest ind);

    void onApplyChargingReportGPRSRequest(ApplyChargingReportGPRSRequest ind);

    void onApplyChargingReportGPRSResponse(ApplyChargingReportGPRSResponse ind);

    void onEventReportGPRSRequest(EventReportGPRSRequest ind);

    void onEventReportGPRSResponse(EventReportGPRSResponse ind);

    void onActivityTestGPRSRequest(ActivityTestGPRSRequest ind);

    void onActivityTestGPRSResponse(ActivityTestGPRSResponse ind);

}