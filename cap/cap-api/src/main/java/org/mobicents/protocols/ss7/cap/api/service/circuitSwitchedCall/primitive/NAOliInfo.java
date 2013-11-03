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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

/**
 *
 NAOliInfo ::= OCTET STRING (SIZE (1))
 -- NA Oli information takes the same value as defined in ANSI T1.113-1995 [92]
 -- e.g. '3D'H Decimal value 61 - Cellular Service (Type 1)
 -- '3E'H Decimal value 62 - Cellular Service (Type 2)
 -- '3F'H Decimal value 63 - Cellular Service (roaming)
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface NAOliInfo extends Serializable {

    int getData();

    // TODO: implement getting info according to ANSI T1.113-1995

}