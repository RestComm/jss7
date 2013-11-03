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

package org.mobicents.protocols.ss7.map.api.service.callhandling;

import java.io.Serializable;

/**
 *
 CipheringAlgorithm ::= OCTET STRING (SIZE (1)) -- Refers to 'permitted algorithms' in 'encryption information' -- coded
 * according to 3GPP TS 48.008 [49]:
 *
 * -- Bits 8-1 -- 8765 4321 -- 0000 0001 No encryption -- 0000 0010 GSM A5/1 -- 0000 0100 GSM A5/2 -- 0000 1000 GSM A5/3 -- 0001
 * 0000 GSM A5/4 -- 0010 0000 GSM A5/5 -- 0100 0000 GSM A5/6 -- 1000 0000 GSM A5/7
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface CipheringAlgorithm extends Serializable {

    int getData();

}
