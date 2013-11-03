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

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;

/**
 *
 * @author kulikov
 */
public class HopCounterImpl extends AbstractParameter implements HopCounter {

    private int value;

    public HopCounterImpl(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void decode(InputStream in) throws IOException {
        if (in.read() != 1) {
            throw new IOException();
        }
        this.value = in.read() & 0x0F; // ?

    }

    public void encode(OutputStream os) throws IOException {
        os.write(1);
        os.write(this.value & 0x0F);

    }

    public void decode(byte[] b) throws IOException {
        this.value = b[0] & 0x0F; // ?

    }

    public byte[] encode() throws IOException {
        return new byte[] { (byte) (this.value & 0x0F) };
    }
}
