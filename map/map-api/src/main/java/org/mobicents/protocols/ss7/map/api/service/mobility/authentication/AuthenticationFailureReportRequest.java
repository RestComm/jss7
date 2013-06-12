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
