/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, encode to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.protocols.ss7.m3ua.impl.message;

import org.mobicents.protocols.ss7.m3ua.message.*;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;

/**
 * 
 * @author kulikov
 */
public abstract class M3UAMessageImpl implements M3UAMessage {
    // header part
    private int messageClass;
    private int messageType;

    protected HashMap<Short, Parameter> parameters = new HashMap();

    private ParameterFactoryImpl factory = new ParameterFactoryImpl();

    public M3UAMessageImpl() {

    }

    protected M3UAMessageImpl(int messageClass, int messageType) {
        this.messageClass = messageClass;
        this.messageType = messageType;
    }

    protected abstract void encodeParams(ByteBuffer buffer);

    public void encode(ByteBuffer buffer) {
        buffer.position(8);

        encodeParams(buffer);

        int length = buffer.position();
        buffer.rewind();

        buffer.put((byte) 1);
        buffer.put((byte) 0);
        buffer.put((byte) messageClass);
        buffer.put((byte) messageType);
        buffer.putInt(length);

        buffer.position(length);
    }

    protected void decode(byte[] data) {
        int pos = 0;
        while (pos < data.length) {
            short tag = (short) ((data[pos] & 0xff) << 8 | (data[pos + 1] & 0xff));
            short len = (short) ((data[pos + 2] & 0xff) << 8 | (data[pos + 3] & 0xff));

            byte[] value = new byte[len - 4];

            System.arraycopy(data, pos + 4, value, 0, value.length);
            pos += len;
            parameters.put(tag, factory.createParameter(tag, value));

            // The Parameter Length does not include any padding octets. We have
            // to consider padding here
            pos += (pos % 4);
        }
    }

    public int getMessageClass() {
        return messageClass;
    }

    public int getMessageType() {
        return messageType;
    }
}
