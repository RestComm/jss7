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

import java.io.Serializable;

/**
 *
 TracePropagationList ::= SEQUENCE { traceReference [0] TraceReference OPTIONAL, traceType [1] TraceType OPTIONAL,
 * traceReference2 [2] TraceReference2 OPTIONAL, traceRecordingSessionReference [3] TraceRecordingSessionReference OPTIONAL,
 * rnc-TraceDepth [4] TraceDepth OPTIONAL, rnc-InterfaceList [5] RNC-InterfaceList OPTIONAL, msc-s-TraceDepth [6] TraceDepth
 * OPTIONAL, msc-s-InterfaceList [7] MSC-S-InterfaceList OPTIONAL, msc-s-EventList [8] MSC-S-EventList OPTIONAL, mgw-TraceDepth
 * [9] TraceDepth OPTIONAL, mgw-InterfaceList [10] MGW-InterfaceList OPTIONAL, mgw-EventList [11] MGW-EventList OPTIONAL, ...}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface TracePropagationList extends Serializable {

    TraceReference getTraceReference();

    TraceType getTraceType();

    TraceReference2 getTraceReference2();

    TraceRecordingSessionReference getTraceRecordingSessionReference();

    TraceDepth getTraceDepth();

    RNCInterfaceList getRNCInterfaceList();

    TraceDepth getMscSTraceDepth();

    MSCSInterfaceList getMscSInterfaceList();

    MSCSEventList getMSCSEventList();

    TraceDepth getMgwTraceDepth();

    MGWInterfaceList getMGWInterfaceList();

    MGWEventList getMGWEventList();

}
