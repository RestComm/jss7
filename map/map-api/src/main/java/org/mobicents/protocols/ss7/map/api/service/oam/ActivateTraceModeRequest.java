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

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
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
 * MAP V2: ActivateTraceMode ::= OPERATION--Timer m ARGUMENT activateTraceModeArg ActivateTraceModeArg RESULT ERRORS {
 * SystemFailure, DataMissing, UnexpectedDataValue, FacilityNotSupported, UnidentifiedSubscriber, TracingBufferFull}
 *
 * MAP V3: ActivateTraceModeArg ::= SEQUENCE { imsi [0] IMSI OPTIONAL, traceReference [1] TraceReference, traceType [2]
 * TraceType, omc-Id [3] AddressString OPTIONAL, extensionContainer [4] ExtensionContainer OPTIONAL, ..., traceReference2 [5]
 * TraceReference2 OPTIONAL, traceDepthList [6] TraceDepthList OPTIONAL, traceNE-TypeList [7] TraceNE-TypeList OPTIONAL,
 * traceInterfaceList [8] TraceInterfaceList OPTIONAL, traceEventList [9] TraceEventList OPTIONAL, traceCollectionEntity [10]
 * GSN-Address OPTIONAL, mdt-Configuration [11] MDT-Configuration OPTIONAL }
 *
 * MAP V2: ActivateTraceModeArg ::= SEQUENCE { imsi[0] IMSI OPTIONAL, traceReference[1] TraceReference, traceType[2] TraceType,
 * omc-Id[3] AddressString OPTIONAL, ...}
 *
 * @author sergey vetyutnev
 *
 */
public interface ActivateTraceModeRequest extends OamMessage {

    IMSI getImsi();

    TraceReference getTraceReference();

    TraceType getTraceType();

    AddressString getOmcId();

    MAPExtensionContainer getExtensionContainer();

    TraceReference2 getTraceReference2();

    TraceDepthList getTraceDepthList();

    TraceNETypeList getTraceNeTypeList();

    TraceInterfaceList getTraceInterfaceList();

    TraceEventList getTraceEventList();

    GSNAddress getTraceCollectionEntity();

    MDTConfiguration getMdtConfiguration();

}
