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
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V1-2-3:

MAP V3: cancelLocation OPERATION ::= {
-- Timer m
ARGUMENT CancelLocationArg
RESULT CancelLocationRes
-- optional ERRORS { dataMissing | unexpectedDataValue }
CODE local:3 }

MAP V2: CancelLocation ::= OPERATION
--Timer m
ARGUMENT cancelLocationArg CancelLocationArg
RESULT ERRORS {
  DataMissing,
  -- DataMissing must not be used in version 1
  UnexpectedDataValue,
  UnidentifiedSubscriber
}
-- UnidentifiedSubscriber must not be used in version greater 1


MAP V3: CancelLocationArg ::= [3] SEQUENCE {
  identity                       Identity,
  cancellationType               CancellationType OPTIONAL,
  extensionContainer             ExtensionContainer OPTIONAL,
  ...,
  typeOfUpdate                   [0] TypeOfUpdate OPTIONAL,
  mtrf-SupportedAndAuthorized    [1] NULL OPTIONAL,
  mtrf-SupportedAndNotAuthorized [2] NULL OPTIONAL,
  newMSC-Number                  [3] ISDN-AddressString OPTIONAL,
  newVLR-Number                  [4] ISDN-AddressString OPTIONAL,
  new-lmsi                       [5] LMSI OPTIONAL
}
-- mtrf-SupportedAndAuthorized and mtrf-SupportedAndNotAuthorized shall not
-- both be present

MAP V2: CancelLocationArg ::= CHOICE {
  imsi            IMSI,
  imsi-WithLMSI   IMSI-WithLMSI
}

Identity ::= CHOICE {
  imsi            IMSI,
  imsi-WithLMSI   IMSI-WithLMSI
}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CancelLocationRequest extends MobilityMessage {

    IMSI getImsi();

    IMSIWithLMSI getImsiWithLmsi();

    CancellationType getCancellationType();

    MAPExtensionContainer getExtensionContainer();

    TypeOfUpdate getTypeOfUpdate();

    boolean getMtrfSupportedAndAuthorized();

    boolean getMtrfSupportedAndNotAuthorized();

    ISDNAddressString getNewMSCNumber();

    ISDNAddressString getNewVLRNumber();

    LMSI getNewLmsi();

    long getMapProtocolVersion();

}
