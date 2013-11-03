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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V2-3:
 *
 * MAP V3: sendAuthenticationInfo OPERATION ::= { --Timer m ARGUMENT SendAuthenticationInfoArg -- optional -- within a dialogue
 * sendAuthenticationInfoArg shall not be present in -- subsequent invoke components. If received in a subsequent invoke
 * component -- it shall be discarded.
 *
 * RESULT SendAuthenticationInfoRes -- optional ERRORS { systemFailure | dataMissing | unexpectedDataValue | unknownSubscriber}
 * CODE local:56 }
 *
 * MAP V2: SendAuthenticationInfo ::= OPERATION --Timer m ARGUMENT sendAuthenticationInfoArg SendAuthenticationInfoArg RESULT
 * sendAuthenticationInfoRes SendAuthenticationInfoRes -- optional ERRORS { SystemFailure, DataMissing, UnexpectedDataValue,
 * UnknownSubscriber}
 *
 *
 * MAP V3: SendAuthenticationInfoArg ::= SEQUENCE { imsi [0] IMSI, numberOfRequestedVectors NumberOfRequestedVectors,
 * segmentationProhibited NULL OPTIONAL, immediateResponsePreferred [1] NULL OPTIONAL, re-synchronisationInfo
 * Re-synchronisationInfo OPTIONAL, extensionContainer [2] ExtensionContainer OPTIONAL, ..., requestingNodeType [3]
 * RequestingNodeType OPTIONAL, requestingPLMN-Id [4] PLMN-Id OPTIONAL, numberOfRequestedAdditional-Vectors [5]
 * NumberOfRequestedVectors OPTIONAL, additionalVectorsAreForEPS [6] NULL OPTIONAL }
 *
 * MAP V2: SendAuthenticationInfoArg ::= IMSI
 *
 * NumberOfRequestedVectors ::= INTEGER (1..5)
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SendAuthenticationInfoRequest extends MobilityMessage {

    IMSI getImsi();

    int getNumberOfRequestedVectors();

    boolean getSegmentationProhibited();

    boolean getImmediateResponsePreferred();

    ReSynchronisationInfo getReSynchronisationInfo();

    MAPExtensionContainer getExtensionContainer();

    RequestingNodeType getRequestingNodeType();

    PlmnId getRequestingPlmnId();

    Integer getNumberOfRequestedAdditionalVectors();

    boolean getAdditionalVectorsAreForEPS();

    long getMapProtocolVersion();

}
