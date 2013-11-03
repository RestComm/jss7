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

import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BCSMFailure;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentFailure;

/**
 *
 entityReleased {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT EntityReleasedArg {bound} RETURN RESULT FALSE ALWAYS
 * RESPONDS FALSE CODE opcode-entityReleased} -- Direction: gsmSSF -> gsmSCF, Timer: Ter -- This operation is used by the gsmSSF
 * to inform the gsmSCF of an error or exception
 *
 * EntityReleasedArg {PARAMETERS-BOUND : bound} ::= CHOICE { callSegmentFailure [0] CallSegmentFailure {bound}, bCSM-Failure [1]
 * BCSM-Failure {bound} }
 *
 * @author sergey vetyutnev
 *
 */
public interface EntityReleasedRequest extends CircuitSwitchedCallMessage {

    CallSegmentFailure getCallSegmentFailure();

    BCSMFailure getBcsmFailure();

}