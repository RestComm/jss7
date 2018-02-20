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

package org.restcomm.protocols.ss7.map.api.service.oam;

import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;

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
