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

package org.mobicents.protocols.ss7.map.api.service.sms;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 MAP V1-2-3:
 *
 * MAP V3: reportSM-DeliveryStatus OPERATION ::= { --Timer s ARGUMENT ReportSM-DeliveryStatusArg RESULT
 * ReportSM-DeliveryStatusRes -- optional ERRORS { dataMissing | unexpectedDataValue | unknownSubscriber |
 * messageWaitingListFull} CODE local:47 }
 *
 * MAP V2: ReportSM-DeliveryStatus ::= OPERATION --Timer s ARGUMENT reportSM-DeliveryStatusArg ReportSM-DeliveryStatusArg RESULT
 * storedMSISDN ISDN-AddressString -- optional -- storedMSISDN must be absent in version 1 -- storedMSISDN must be present in
 * version greater 1 ERRORS { DataMissing, -- DataMissing must not be used in version 1 UnexpectedDataValue, UnknownSubscriber,
 * MessageWaitingListFull}
 *
 * MAP V3: ReportSM-DeliveryStatusArg ::= SEQUENCE { msisdn ISDN-AddressString, serviceCentreAddress AddressString,
 * sm-DeliveryOutcome SM-DeliveryOutcome, absentSubscriberDiagnosticSM [0] AbsentSubscriberDiagnosticSM OPTIONAL,
 * extensionContainer [1] ExtensionContainer OPTIONAL, ..., gprsSupportIndicator [2] NULL OPTIONAL, -- gprsSupportIndicator is
 * set only if the SMS-GMSC supports -- handling of two delivery outcomes deliveryOutcomeIndicator [3] NULL OPTIONAL, --
 * DeliveryOutcomeIndicator is set when the SM-DeliveryOutcome -- is for GPRS additionalSM-DeliveryOutcome [4]
 * SM-DeliveryOutcome OPTIONAL, -- If received, additionalSM-DeliveryOutcome is for GPRS -- If DeliveryOutcomeIndicator is set,
 * then AdditionalSM-DeliveryOutcome shall be absent additionalAbsentSubscriberDiagnosticSM [5] AbsentSubscriberDiagnosticSM
 * OPTIONAL -- If received additionalAbsentSubscriberDiagnosticSM is for GPRS -- If DeliveryOutcomeIndicator is set, then
 * AdditionalAbsentSubscriberDiagnosticSM -- shall be absent }
 *
 * MAP V2: ReportSM-DeliveryStatusArg ::= SEQUENCE { msisdn ISDN-AddressString, serviceCentreAddress AddressString,
 * sm-DeliveryOutcome SM-DeliveryOutcome OPTIONAL, -- sm-DeliveryOutcome must be absent in version 1 -- sm-DeliveryOutcome must
 * be present in version greater 1 ...}
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ReportSMDeliveryStatusRequest extends SmsMessage {

    ISDNAddressString getMsisdn();

    AddressString getServiceCentreAddress();

    SMDeliveryOutcome getSMDeliveryOutcome();

    Integer getAbsentSubscriberDiagnosticSM();

    MAPExtensionContainer getExtensionContainer();

    boolean getGprsSupportIndicator();

    boolean getDeliveryOutcomeIndicator();

    SMDeliveryOutcome getAdditionalSMDeliveryOutcome();

    Integer getAdditionalAbsentSubscriberDiagnosticSM();

}
