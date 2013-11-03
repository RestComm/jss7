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

package org.mobicents.protocols.ss7.map.api.service.oam;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 MAP V1-2-3:
 *
 * MAP V3: activateTraceMode OPERATION ::= { --Timer m ARGUMENT ActivateTraceModeArg RESULT ActivateTraceModeRes -- optional
 * ERRORS { systemFailure | dataMissing | unexpectedDataValue | facilityNotSupported | unidentifiedSubscriber |
 * tracingBufferFull} CODE local:50 }
 *
 * MAP V2: DeactivateTraceMode ::= OPERATION--Timer m ARGUMENT deactivateTraceModeArg DeactivateTraceModeArg RESULT ERRORS {
 * SystemFailure, DataMissing, UnexpectedDataValue, FacilityNotSupported, UnidentifiedSubscriber}
 *
 * MAP V3: DeactivateTraceModeArg ::= SEQUENCE { imsi [0] IMSI OPTIONAL, traceReference [1] TraceReference, extensionContainer
 * [2] ExtensionContainer OPTIONAL, ..., traceReference2 [3] TraceReference2 OPTIONAL }
 *
 * MAP V2: DeactivateTraceModeArg ::= SEQUENCE { imsi[0] IMSI OPTIONAL, traceReference[1] TraceReference, ...}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface DeactivateTraceModeRequest extends OamMessage {

    IMSI getImsi();

    TraceReference getTraceReference();

    MAPExtensionContainer getExtensionContainer();

    TraceReference2 getTraceReference2();

}
