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

package org.mobicents.protocols.ss7.inap.api.primitives;

import java.io.Serializable;

/**
*
ExtensionField {PARAMETERS-BOUND : bound} ::= SEQUENCE{
    type EXTENSION.&id ({SupportedExtensions {bound}}),
    -- shall identify the value of an EXTENSION type
    criticality CriticalityType DEFAULT ignore,
    value [1] EXTENSION.&ExtensionType ({SupportedExtensions {bound}}{@type}),
...
}
--This parameter indicates an extension of an argument data type.
-- Its content is network operator specific

CriticalityType ::= ENUMERATED {ignore (0),abort (1)}
*
* @author sergey vetyutnev
*
*/
public interface ExtensionField extends Serializable {

    Integer getLocalCode();

    long[] getGlobalCode();

    CriticalityType getCriticalityType();

    /**
     *
     * @return Encoded field parameter without tag and length fields
     */
    byte[] getData();

    void setLocalCode(Integer localCode);

    void setGlobalCode(long[] globalCode);

    void setCriticalityType(CriticalityType criticalityType);

    /**
     * @param data Encoded field parameter without tag and length fields
     */
    void setData(byte[] data);

}
