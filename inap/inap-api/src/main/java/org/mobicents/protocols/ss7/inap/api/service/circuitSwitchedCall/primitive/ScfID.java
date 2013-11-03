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

package org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

/**
*

ScfID {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE(bound.&minScfIDLength..bound.&maxScfIDLength))
-- defined by network operator.
-- Indicates the SCF identity.
-- Used to derive the INAP address of the SCF to establish a connection between a requesting FE
-- and the specified SCF.
-- When ScfID is used in an operation which may cross an internetwork boundary, its encoding must
-- be understood in both networks; this requires bilateral agreement on the encoding.
-- Refer to 3.5/ETS 300 009-1 "calling party address" parameter for encoding. It indicates the SCCP
address of the SCF.
-- Other encoding schemes are also possible as an operator specific option.

*
* @author sergey vetyutnev
*
*/
public interface ScfID extends Serializable {

    byte[] getData();

    // TODO: add "calling party address" parameter for encoding

}
