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

package org.mobicents.protocols.ss7.cap.api.service.sms;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.primitives.TimerID;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELsequence1SMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.RPCause;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSEvent;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingScheme;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifier;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriod;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface CAPDialogSms extends CAPDialog {
    Long addConnectSMSRequest(SMSAddressString callingPartysNumber, CalledPartyBCDNumber destinationSubscriberNumber, ISDNAddressString smscAddress,
            CAPExtensions extensions) throws CAPException;

    Long addConnectSMSRequest(int customInvokeTimeout, SMSAddressString callingPartysNumber, CalledPartyBCDNumber destinationSubscriberNumber,
            ISDNAddressString smscAddress, CAPExtensions extensions) throws CAPException;

    Long addEventReportSMSRequest(EventTypeSMS eventTypeSMS, EventSpecificInformationSMS eventSpecificInformationSMS, MiscCallInfo miscCallInfo,
            CAPExtensions extensions) throws CAPException;

    Long addEventReportSMSRequest(int customInvokeTimeout, EventTypeSMS eventTypeSMS, EventSpecificInformationSMS eventSpecificInformationSMS,
            MiscCallInfo miscCallInfo, CAPExtensions extensions) throws CAPException;

    Long addFurnishChargingInformationSMSRequest(FCIBCCCAMELsequence1SMS fciBCCCAMELsequence1) throws CAPException;

    Long addFurnishChargingInformationSMSRequest(int customInvokeTimeout, FCIBCCCAMELsequence1SMS fciBCCCAMELsequence1) throws CAPException;

    Long addInitialDPSMSRequest(int serviceKey, CalledPartyBCDNumber destinationSubscriberNumber, SMSAddressString callingPartyNumber,
            EventTypeSMS eventTypeSMS, IMSI imsi, LocationInformation locationInformationMSC, LocationInformationGPRS locationInformationGPRS,
            ISDNAddressString smscCAddress, TimeAndTimezone timeAndTimezone, TPShortMessageSpecificInfo tPShortMessageSpecificInfo,
            TPProtocolIdentifier tPProtocolIdentifier, TPDataCodingScheme tPDataCodingScheme, TPValidityPeriod tPValidityPeriod, CAPExtensions extensions,
            CallReferenceNumber smsReferenceNumber, ISDNAddressString mscAddress, ISDNAddressString sgsnNumber, MSClassmark2 mSClassmark2,
            GPRSMSClass gprsMSClass, IMEI imei, ISDNAddressString calledPartyNumber) throws CAPException;

    Long addInitialDPSMSRequest(int customInvokeTimeout, int serviceKey, CalledPartyBCDNumber destinationSubscriberNumber, SMSAddressString callingPartyNumber,
            EventTypeSMS eventTypeSMS, IMSI imsi, LocationInformation locationInformationMSC, LocationInformationGPRS locationInformationGPRS,
            ISDNAddressString smscCAddress, TimeAndTimezone timeAndTimezone, TPShortMessageSpecificInfo tPShortMessageSpecificInfo,
            TPProtocolIdentifier tPProtocolIdentifier, TPDataCodingScheme tPDataCodingScheme, TPValidityPeriod tPValidityPeriod, CAPExtensions extensions,
            CallReferenceNumber smsReferenceNumber, ISDNAddressString mscAddress, ISDNAddressString sgsnNumber, MSClassmark2 mSClassmark2,
            GPRSMSClass gprsMSClass, IMEI imei, ISDNAddressString calledPartyNumber) throws CAPException;

    Long addReleaseSMSRequest(RPCause rpCause) throws CAPException;

    Long addReleaseSMSRequest(int customInvokeTimeout, RPCause rpCause) throws CAPException;

    Long addRequestReportSMSEventRequest(ArrayList<SMSEvent> smsEvents, CAPExtensions extensions) throws CAPException;

    Long addRequestReportSMSEventRequest(int customInvokeTimeout, ArrayList<SMSEvent> smsEvents, CAPExtensions extensions) throws CAPException;

    Long addResetTimerSMSRequest(TimerID timerID, int timerValue, CAPExtensions extensions) throws CAPException;

    Long addResetTimerSMSRequest(int customInvokeTimeout, TimerID timerID, int timerValue, CAPExtensions extensions) throws CAPException;

    Long addContinueSMSRequest() throws CAPException;

    Long addContinueSMSRequest(int customInvokeTimeout) throws CAPException;
}
