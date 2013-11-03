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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 *
 */
public class InfoStringImpl extends ParameterImpl implements InfoString {

    private String string;

    protected InfoStringImpl(byte[] value) {
        this.tag = Parameter.INFO_String;
        this.string = new String(value);
    }

    protected InfoStringImpl(String string) {
        this.tag = Parameter.INFO_String;
        this.string = string;
    }

    public String getString() {
        return this.string;
    }

    @Override
    protected byte[] getValue() {
        return this.string.getBytes();
    }

    @Override
    public String toString() {
        return String.format("InfoString : string = %s ", this.string);
    }

}
