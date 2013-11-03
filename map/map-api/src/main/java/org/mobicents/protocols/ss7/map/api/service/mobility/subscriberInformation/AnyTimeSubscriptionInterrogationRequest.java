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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V3:
 *
 * anyTimeSubscriptionInterrogation OPERATION ::= { --Timer m ARGUMENT AnyTimeSubscriptionInterrogationArg RESULT
 * AnyTimeSubscriptionInterrogationRes ERRORS { atsi-NotAllowed | dataMissing | unexpectedDataValue | unknownSubscriber |
 * bearerServiceNotProvisioned | teleserviceNotProvisioned | callBarred | illegalSS-Operation | ss-NotAvailable |
 * informationNotAvailable} CODE local:62 }
 *
 * AnyTimeSubscriptionInterrogationArg ::= SEQUENCE { subscriberIdentity [0] SubscriberIdentity, requestedSubscriptionInfo [1]
 * RequestedSubscriptionInfo, gsmSCF-Address [2] ISDN-AddressString, extensionContainer [3] ExtensionContainer OPTIONAL,
 * longFTN-Supported [4] NULL OPTIONAL, ...}
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface AnyTimeSubscriptionInterrogationRequest extends MobilityMessage {

    SubscriberIdentity getSubscriberIdentity();

    RequestedSubscriptionInfo getRequestedSubscriptionInfo();

    ISDNAddressString getGsmScfAddress();

    MAPExtensionContainer getExtensionContainer();

    boolean getLongFTNSupported();

}
