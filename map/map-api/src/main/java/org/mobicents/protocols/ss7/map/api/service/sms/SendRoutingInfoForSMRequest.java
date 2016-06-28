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

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;

/**
 *
<code>
MAP V1-2-3:

MAP V3: sendRoutingInfoForSM OPERATION ::= { --Timer m
  ARGUMENT RoutingInfoForSM-Arg
  RESULT RoutingInfoForSM-Res
  ERRORS { systemFailure | dataMissing | unexpectedDataValue | facilityNotSupported | unknownSubscriber | teleserviceNotProvisioned | callBarred | absentSubscriberSM }
  CODE local:45
}

MAP V2: SendRoutingInfoForSM ::= OPERATION --Timer m
  ARGUMENT routingInfoForSM-Arg RoutingInfoForSM-Arg
  RESULT routingInfoForSM-Res RoutingInfoForSM-Res
  ERRORS { SystemFailure, DataMissing, UnexpectedDataValue, FacilityNotSupported, UnknownSubscriber, TeleserviceNotProvisioned, AbsentSubscriber, CallBarred }

MAP V3: RoutingInfoForSM-Arg ::= SEQUENCE {
  msisdn                  [0] ISDN-AddressString,
  sm-RP-PRI               [1] BOOLEAN,
  serviceCentreAddress    [2] AddressString,
  extensionContainer      [6] ExtensionContainer OPTIONAL,
  ... ,
  gprsSupportIndicator    [7] NULL OPTIONAL,
  -- gprsSupportIndicator is set only if the SMS-GMSC supports
  -- receiving of two numbers from the HLR
  sm-RP-MTI               [8] SM-RP-MTI OPTIONAL,
  sm-RP-SMEA              [9] SM-RP-SMEA OPTIONAL,
  sm-deliveryNotIntended  [10] SM-DeliveryNotIntended OPTIONAL,
  ip-sm-gwGuidanceIndicator   [11] NULL OPTIONAL,
  imsi                    [12] IMSI OPTIONAL,
  t4-Trigger-Indicator    [14] NULL OPTIONAL,
  singleAttemptDelivery   [13] NULL OPTIONAL,
  correlationID           [15] CorrelationID OPTIONAL
}

MAP V2: RoutingInfoForSM-Arg ::= SEQUENCE {
  msisdn                  [0] ISDN-AddressString,
  sm-RP-PRI               [1] BOOLEAN,
  erviceCentreAddress     [2] AddressString,
  teleservice             [5] TeleserviceCode OPTIONAL,
  -- teleservice must be absent in version greater 1 ...
}
</code>
 *
 *
 * @author sergey vetyutnev
 * @author eva ogallar
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

    SMDeliveryNotIntended getSmDeliveryNotIntended();

    boolean getIpSmGwGuidanceIndicator();

    IMSI getImsi();

    boolean getT4TriggerIndicator();

    boolean getSingleAttemptDelivery();

    // TODO: CorrelationID parameter is not still implemented
    // CorrelationID getCorrelationID();

    // for MAP V1 only
    TeleserviceCode getTeleservice();

}
