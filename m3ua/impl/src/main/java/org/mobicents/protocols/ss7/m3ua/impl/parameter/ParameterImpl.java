/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */ 
package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import java.nio.ByteBuffer;

import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * @author amit bhayani
 * @author kulikov
 */
public abstract class ParameterImpl implements Parameter {

    protected short tag;
    protected short length;

    public short getTag() {
        return tag;
    }

    protected abstract byte[] getValue();

//    public void encode(OutputStream out) throws IOException {
//        // obtain encoded value
//        byte[] value = getValue();
//
//        // encode tag
//        out.write((byte) (tag >> 8));
//        out.write((byte) (tag));
//
//        // encode length including value, tag and length field itself
//        length = (short) (value.length + 4);
//
//        out.write((byte) (length >> 8));
//        out.write((byte) (length));
//
//        // encode value
//        out.write(value);
//    }

    public void write(ByteBuffer buffer) {
        // obtain encoded value
        byte[] value = getValue();

        // encode tag
        buffer.put((byte) (tag >> 8));
        buffer.put((byte) (tag));

        // encode length including value, tag and length field itself
        length = (short) (value.length + 4);

        buffer.put((byte) (length >> 8));
        buffer.put((byte) (length));

        // encode value
        buffer.put(value);


        /*
         * The total length of a parameter (including Tag, Parameter Length, and
         * Value fields) MUST be a multiple of 4 octets. If the length of the
         * parameter is not a multiple of 4 octets, the sender pads the
         * Parameter at the end (i.e., after the Parameter Value field) with all
         * zero octets. The length of the padding is NOT included in the
         * parameter length field. A sender MUST NOT pad with more than 3
         * octets. The receiver MUST ignore the padding octets.
         */
        int remainder = length % 4;
        while (remainder > 0) {
            buffer.put((byte) 0x00);
            remainder--;
        }
    }

}
