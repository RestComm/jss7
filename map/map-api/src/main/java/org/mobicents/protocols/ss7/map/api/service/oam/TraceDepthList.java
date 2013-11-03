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
 TraceDepthList ::= SEQUENCE { msc-s-TraceDepth [0] TraceDepth OPTIONAL, mgw-TraceDepth [1] TraceDepth OPTIONAL,
 * sgsn-TraceDepth [2] TraceDepth OPTIONAL, ggsn-TraceDepth [3] TraceDepth OPTIONAL, rnc-TraceDepth [4] TraceDepth OPTIONAL,
 * bmsc-TraceDepth [5] TraceDepth OPTIONAL, ... , mme-TraceDepth [6] TraceDepth OPTIONAL, sgw-TraceDepth [7] TraceDepth
 * OPTIONAL, pgw-TraceDepth [8] TraceDepth OPTIONAL, eNB-TraceDepth [9] TraceDepth OPTIONAL }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface TraceDepthList extends Serializable {

    TraceDepth getMscSTraceDepth();

    TraceDepth getMgwTraceDepth();

    TraceDepth getSgsnTraceDepth();

    TraceDepth getGgsnTraceDepth();

    TraceDepth getRncTraceDepth();

    TraceDepth getBmscTraceDepth();

    TraceDepth getMmeTraceDepth();

    TraceDepth getSgwTraceDepth();

    TraceDepth getPgwTraceDepth();

    TraceDepth getEnbTraceDepth();

}
