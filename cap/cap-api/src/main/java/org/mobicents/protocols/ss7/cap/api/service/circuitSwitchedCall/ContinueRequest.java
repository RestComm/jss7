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
 continue OPERATION ::= { RETURN RESULT FALSE ALWAYS RESPONDS FALSE CODE opcode-continue} -- Direction: gsmSCF -> gsmSSF,
 * Timer: Tcue -- This operation is used to request the gsmSSF to proceed with call processing at the -- DP at which it
 * previously suspended call processing to await gsmSCF instructions -- (i.e. proceed to the next point in call in the BCSM).
 * The gsmSSF continues call -- processing without substituting new data from gsmSCF.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ContinueRequest extends CircuitSwitchedCallMessage {

}
