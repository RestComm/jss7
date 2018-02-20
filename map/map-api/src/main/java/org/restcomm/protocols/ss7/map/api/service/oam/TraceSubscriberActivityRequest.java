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

import org.restcomm.protocols.ss7.isup.message.parameter.CallReference;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;

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
