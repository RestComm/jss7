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

/**
 *
 */
package org.mobicents.protocols.ss7.tcap.asn;

import org.mobicents.protocols.asn.Tag;

/**
 * @author baranowb
 *
 */
public interface AbortSource extends Encodable {

    // Q773 shows this is PRIMITIVE ..... but its coded in ASN the same way as Result.....
    // making it Primitive...., where is logic!
    int _TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    boolean _TAG_PC_PRIMITIVE = true; // constructed.... // specs show true
    int _TAG = 0x00;

    void setAbortSourceType(AbortSourceType t);

    AbortSourceType getAbortSourceType();
}
