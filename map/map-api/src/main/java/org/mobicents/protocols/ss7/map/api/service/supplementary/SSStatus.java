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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

import java.io.Serializable;

/**
 *
 SS-Status ::= OCTET STRING (SIZE (1))
 *
 * -- bits 8765: 0000 (unused) -- bits 4321: Used to convey the "P bit","R bit","A bit" and "Q bit", -- representing
 * supplementary service state information -- as defined in TS 3GPP TS 23.011 [22]
 *
 * -- bit 4: "Q bit"
 *
 * -- bit 3: "P bit"
 *
 * -- bit 2: "R bit"
 *
 * -- bit 1: "A bit"
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SSStatus extends Serializable {

    int getData();

    boolean getQBit();

    boolean getPBit();

    boolean getRBit();

    boolean getABit();

}