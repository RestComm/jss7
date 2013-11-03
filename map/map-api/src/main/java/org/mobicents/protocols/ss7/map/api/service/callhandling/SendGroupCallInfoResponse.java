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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

import org.mobicents.protocols.ss7.map.api.primitives.ASCICallReference;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalSubscriptions;

/**
 *
 SendGroupCallInfoRes ::= SEQUENCE { anchorMSC-Address [0] ISDN-AddressString OPTIONAL, asciCallReference [1]
 * ASCI-CallReference OPTIONAL, imsi [2] IMSI OPTIONAL, additionalInfo [3] AdditionalInfo OPTIONAL, additionalSubscriptions [4]
 * AdditionalSubscriptions OPTIONAL, kc [5] Kc OPTIONAL, extensionContainer [6] ExtensionContainer OPTIONAL, ... }
 *
 * Kc ::= OCTET STRING (SIZE (8))
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SendGroupCallInfoResponse extends CallHandlingMessage {

     ISDNAddressString getAnchorMscAddress();

     ASCICallReference getAsciCallReference();

     IMSI getImsi();

     AdditionalInfo getAdditionalInfo();

     AdditionalSubscriptions getAdditionalSubscriptions();

     byte[] getKc();

     MAPExtensionContainer getExtensionContainer();

}
