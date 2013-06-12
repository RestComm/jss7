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
