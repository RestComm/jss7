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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

/**
 *
 * <p>
 * The activity test (AT) is used for testing the CAMEL dialogue between the gsmSCF and the gsmSSF. The SCP may send CAP AT at
 * regular intervals to the gsmSSF, e.g. every 15 min. The only function of CAP AT is to verify the existence of the CAMEL
 * dialogue. When the gsmSSF receives CAP AT, it returns an empty RESULT to the gsmSCF. If the gsmSCF does not receive an
 * operation RESULT within the operation time for CAP AT, e.g. 5 s, then the gsmSCF terminates the CAMEL service. CAP AT is
 * normally sent by the SCP platform, not by the CAMEL service. The arrival of CAP AT in the gsmSSF has no impact on any call
 * handling process or on the BCSM. The sending of CAP AT is not dependent on the phase of the call or on the gsmSSF FSM state.
 *
 * </p>
 * activityTest OPERATION ::= { RETURN RESULT TRUE CODE opcode-activityTest} -- Direction: gsmSCF -> gsmSSF, Timer: T at -- This
 * operation is used to check for the continued existence of a relationship -- between the gsmSCF and gsmSSF, assist gsmSSF or
 * gsmSRF. If the relationship is -- still in existence, then the gsmSSF will respond. If no reply is received, -- then the
 * gsmSCF will assume that the gsmSSF, assist gsmSSF or gsmSRF has failed -- in some way.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ActivityTestRequest extends CircuitSwitchedCallMessage {

}
