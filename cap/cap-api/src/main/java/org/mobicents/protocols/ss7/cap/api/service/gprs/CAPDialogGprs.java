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

import java.util.ArrayList;

import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.primitives.TimerID;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOver;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSCause;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilities;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface CAPDialogGprs extends CAPDialog {

    Long addInitialDpGprsRequest(int serviceKey, GPRSEventType gprsEventType, ISDNAddressString msisdn, IMSI imsi,
            TimeAndTimezone timeAndTimezone, GPRSMSClass gprsMSClass, EndUserAddress endUserAddress,
            QualityOfService qualityOfService, AccessPointName accessPointName, RAIdentity routeingAreaIdentity,
            GPRSChargingID chargingID, SGSNCapabilities sgsnCapabilities, LocationInformationGPRS locationInformationGPRS,
            PDPInitiationType pdpInitiationType, CAPExtensions extensions, GSNAddress gsnAddress, boolean secondaryPDPContext,
            IMEI imei) throws CAPException;

    Long addInitialDpGprsRequest(int customInvokeTimeout, int serviceKey, GPRSEventType gprsEventType,
            ISDNAddressString msisdn, IMSI imsi, TimeAndTimezone timeAndTimezone, GPRSMSClass gprsMSClass,
            EndUserAddress endUserAddress, QualityOfService qualityOfService, AccessPointName accessPointName,
            RAIdentity routeingAreaIdentity, GPRSChargingID chargingID, SGSNCapabilities sgsnCapabilities,
            LocationInformationGPRS locationInformationGPRS, PDPInitiationType pdpInitiationType, CAPExtensions extensions,
            GSNAddress gsnAddress, boolean secondaryPDPContext, IMEI imei) throws CAPException;

    Long addRequestReportGPRSEventRequest(ArrayList<GPRSEvent> gprsEvent, PDPID pdpID) throws CAPException;

    Long addRequestReportGPRSEventRequest(int customInvokeTimeout, ArrayList<GPRSEvent> gprsEvent, PDPID pdpID)
            throws CAPException;

    Long addApplyChargingGPRSRequest(ChargingCharacteristics chargingCharacteristics, Integer tariffSwitchInterval,
            PDPID pdpID) throws CAPException;

    Long addApplyChargingGPRSRequest(int customInvokeTimeout, ChargingCharacteristics chargingCharacteristics,
            Integer tariffSwitchInterval, PDPID pdpID) throws CAPException;

    Long addEntityReleasedGPRSRequest(GPRSCause gprsCause, PDPID pdpID) throws CAPException;

    Long addEntityReleasedGPRSRequest(int customInvokeTimeout, GPRSCause gprsCause, PDPID pdpID) throws CAPException;

    void addEntityReleasedGPRSResponse(long invokeId) throws CAPException;

    Long addConnectGPRSRequest(AccessPointName accessPointName, PDPID pdpID) throws CAPException;

    Long addConnectGPRSRequest(int customInvokeTimeout, AccessPointName accessPointName, PDPID pdpID)
            throws CAPException;

    Long addContinueGPRSRequest(PDPID pdpID) throws CAPException;

    Long addContinueGPRSRequest(int customInvokeTimeout, PDPID pdpID) throws CAPException;

    Long addReleaseGPRSRequest(GPRSCause gprsCause, PDPID pdpID) throws CAPException;

    Long addReleaseGPRSRequest(int customInvokeTimeout, GPRSCause gprsCause, PDPID pdpID) throws CAPException;

    Long addResetTimerGPRSRequest(TimerID timerID, int timerValue) throws CAPException;

    Long addResetTimerGPRSRequest(int customInvokeTimeout, TimerID timerID, int timerValue) throws CAPException;

    Long addFurnishChargingInformationGPRSRequest(
            CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics) throws CAPException;

    Long addFurnishChargingInformationGPRSRequest(int customInvokeTimeout,
            CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics) throws CAPException;

    Long addCancelGPRSRequest(PDPID pdpID) throws CAPException;

    Long addCancelGPRSRequest(int customInvokeTimeout, PDPID pdpID) throws CAPException;

    Long addSendChargingInformationGPRSRequest(
            CAMELSCIGPRSBillingChargingCharacteristics sciGPRSBillingChargingCharacteristics) throws CAPException;

    Long addSendChargingInformationGPRSRequest(int customInvokeTimeout,
            CAMELSCIGPRSBillingChargingCharacteristics sciGPRSBillingChargingCharacteristics) throws CAPException;

    Long addApplyChargingReportGPRSRequest(ChargingResult chargingResult, QualityOfService qualityOfService,
            boolean active, PDPID pdpID, ChargingRollOver chargingRollOver) throws CAPException;

    Long addApplyChargingReportGPRSRequest(int customInvokeTimeout, ChargingResult chargingResult,
            QualityOfService qualityOfService, boolean active, PDPID pdpID, ChargingRollOver chargingRollOver)
            throws CAPException;

    void addApplyChargingReportGPRSResponse(long invokeId) throws CAPException;

    Long addEventReportGPRSRequest(GPRSEventType gprsEventType, MiscCallInfo miscGPRSInfo,
            GPRSEventSpecificInformation gprsEventSpecificInformation, PDPID pdpID) throws CAPException;

    Long addEventReportGPRSRequest(int customInvokeTimeout, GPRSEventType gprsEventType, MiscCallInfo miscGPRSInfo,
            GPRSEventSpecificInformation gprsEventSpecificInformation, PDPID pdpID) throws CAPException;

    void addEventReportGPRSResponse(long invokeId) throws CAPException;

    Long addActivityTestGPRSRequest() throws CAPException;

    Long addActivityTestGPRSRequest(int customInvokeTimeout) throws CAPException;

    void addActivityTestGPRSResponse(long invokeId) throws CAPException;

}