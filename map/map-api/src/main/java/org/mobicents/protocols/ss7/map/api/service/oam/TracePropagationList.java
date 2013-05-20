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

package org.mobicents.protocols.ss7.map.api.service.oam;

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
public interface TracePropagationList {

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
