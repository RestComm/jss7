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

package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
MAP V2-3:

MAP V3: purgeMS OPERATION ::= {
--Timer m
ARGUMENT PurgeMS-Arg RESULT
  PurgeMS-Res  -- optional
  ERRORS { dataMissing | unexpectedDataValue| unknownSubscriber}
CODE local:67 }

MAP V2: PurgeMS ::= OPERATION
--Timer m
ARGUMENT purgeMS-Arg PurgeMS-Arg
RESULT

MAP V3: PurgeMS-Arg ::= [3] SEQUENCE {
  imsi         IMSI,
  vlr-Number   [0] ISDN-AddressString OPTIONAL,
  sgsn-Number  [1] ISDN-AddressString OPTIONAL,
  extensionContainer ExtensionContainer OPTIONAL,
  ...
}

MAP V2: PurgeMS-Arg ::= SEQUENCE {
  imsi         IMSI,
  vlr-Number   ISDN-AddressString,
  ...
}

 *
 * @author sergey vetyutnev
 *
 */
public interface PurgeMSRequest extends MobilityMessage {

    IMSI getImsi();

    ISDNAddressString getVlrNumber();

    ISDNAddressString getSgsnNumber();

    MAPExtensionContainer getExtensionContainer();

}
