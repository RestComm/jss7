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

package org.mobicents.protocols.ss7.cap.api.isup;

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;

/**
 *
 ISUP GenericNumber wrapper
 *
 * GenericNumber {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE( bound.&minGenericNumberLength ..
 * bound.&maxGenericNumberLength)) -- Indicates a generic number. Refer to ETSI EN 300 356-1 [23] Generic number for encoding.
 * minGenericNumberLength ::= 3 maxGenericNumberLength ::= 11
 *
 * GenericNumbers {PARAMETERS-BOUND : bound} ::= SET SIZE(1..bound.&numOfGenericNumbers) OF GenericNumber {bound}
 * numOfGenericNumbers ::= 5
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface GenericNumberCap extends Serializable {

    byte[] getData();

    GenericNumber getGenericNumber() throws CAPException;

}