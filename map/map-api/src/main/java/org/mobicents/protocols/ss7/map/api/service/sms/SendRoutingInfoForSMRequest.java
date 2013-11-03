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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;

/**
 *
 MAP V1-2-3:
 *
 * MAP V3: sendRoutingInfoForSM OPERATION ::= { --Timer m ARGUMENT RoutingInfoForSM-Arg RESULT RoutingInfoForSM-Res ERRORS {
 * systemFailure | dataMissing | unexpectedDataValue | facilityNotSupported | unknownSubscriber | teleserviceNotProvisioned |
 * callBarred | absentSubscriberSM} CODE local:45 }
 *
 * MAP V2: SendRoutingInfoForSM ::= OPERATION --Timer m ARGUMENT routingInfoForSM-Arg RoutingInfoForSM-Arg RESULT
 * routingInfoForSM-Res RoutingInfoForSM-Res ERRORS { SystemFailure, DataMissing, UnexpectedDataValue, FacilityNotSupported,
 * UnknownSubscriber, TeleserviceNotProvisioned, AbsentSubscriber, CallBarred}
 *
 *
 * MAP V3: RoutingInfoForSM-Arg ::= SEQUENCE { msisdn [0] ISDN-AddressString, sm-RP-PRI [1] BOOLEAN, serviceCentreAddress [2]
 * AddressString, extensionContainer [6] ExtensionContainer OPTIONAL, ... , gprsSupportIndicator [7] NULL OPTIONAL, --
 * gprsSupportIndicator is set only if the SMS-GMSC supports -- receiving of two numbers from the HLR sm-RP-MTI [8] SM-RP-MTI
 * OPTIONAL, sm-RP-SMEA [9] SM-RP-SMEA OPTIONAL }
 *
 * MAP V2: RoutingInfoForSM-Arg ::= SEQUENCE { msisdn [0] ISDN-AddressString, sm-RP-PRI [1] BOOLEAN, serviceCentreAddress [2]
 * AddressString, teleservice [5] TeleserviceCode OPTIONAL, -- teleservice must be absent in version greater 1 ...}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SendRoutingInfoForSMRequest extends SmsMessage {

    ISDNAddressString getMsisdn();

    boolean getSm_RP_PRI();

    AddressString getServiceCentreAddress();

    MAPExtensionContainer getExtensionContainer();

    boolean getGprsSupportIndicator();

    SM_RP_MTI getSM_RP_MTI();

    SM_RP_SMEA getSM_RP_SMEA();

    // for MAP V1 only
    TeleserviceCode getTeleservice();

}
