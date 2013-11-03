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

import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;

/**
 *
 MAP V1: TraceSubscriberActivity ::= OPERATION--Timer s ARGUMENT traceSubscriberActivityArg TraceSubscriberActivityArg
 *
 * TraceSubscriberActivityArg ::= SEQUENCE { imsi [0] IMSI OPTIONAL, traceReference [1] TraceReference, traceType [2] TraceType,
 * omc-Id [3] AddressString OPTIONAL, callReference [4] CallReference OPTIONAL}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface TraceSubscriberActivityRequest extends OamMessage {

    IMSI getImsi();

    TraceReference getTraceReference();

    TraceType getTraceType();

    AddressString getOmcId();

    CallReference getCallReference();

}
