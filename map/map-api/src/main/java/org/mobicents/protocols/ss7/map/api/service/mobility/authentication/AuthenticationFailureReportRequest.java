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

package org.mobicents.protocols.ss7.map.api.service.mobility.authentication;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V3: authenticationFailureReport OPERATION ::= { --Timer m ARGUMENT AuthenticationFailureReportArg RESULT
 * AuthenticationFailureReportRes -- optional ERRORS { systemFailure | unexpectedDataValue | unknownSubscriber} CODE local:15 }
 *
 * AuthenticationFailureReportArg ::= SEQUENCE { imsi IMSI, failureCause FailureCause, extensionContainer ExtensionContainer
 * OPTIONAL, ... , re-attempt BOOLEAN OPTIONAL, accessType AccessType OPTIONAL, rand RAND OPTIONAL, vlr-Number [0]
 * ISDN-AddressString OPTIONAL, sgsn-Number [1] ISDN-AddressString OPTIONAL }
 *
 * RAND ::= OCTET STRING (SIZE (16))
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface AuthenticationFailureReportRequest extends MobilityMessage {

    IMSI getImsi();

    FailureCause getFailureCause();

    MAPExtensionContainer getExtensionContainer();

    boolean getReAttempt();

    AccessType getAccessType();

    byte[] getRand();

    ISDNAddressString getVlrNumber();

    ISDNAddressString getSgsnNumber();

}
