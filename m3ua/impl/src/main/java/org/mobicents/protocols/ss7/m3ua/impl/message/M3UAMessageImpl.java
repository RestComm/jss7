/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.m3ua.impl.message;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;
import javolution.util.FastMap;

import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * @author amit bhayani
 * @author kulikov
 * @author sergey vetyutnev
 */
public abstract class M3UAMessageImpl implements M3UAMessage {
    // header part
    private int messageClass;
    private int messageType;

    private String message;
    protected FastMap<Short, Parameter> parameters = new FastMap<Short, Parameter>();

    private ParameterFactoryImpl factory = new ParameterFactoryImpl();

    int initialPosition = 0;

    public M3UAMessageImpl(String message) {
        this.message = message;
    }

    protected M3UAMessageImpl(int messageClass, int messageType, String message) {
        this(message);
        this.messageClass = messageClass;
        this.messageType = messageType;
    }

    protected abstract void encodeParams(ByteBuffer buffer);

    public void encode(ByteBuf byteBuf) {
        byteBuf.writeByte(1);
        byteBuf.writeByte(0);
        byteBuf.writeByte(messageClass);
        byteBuf.writeByte(messageType);

        ByteBuffer buffer = ByteBuffer.allocate(4096);
        encodeParams(buffer);
        int length = buffer.position();
        byte[] data = new byte[length];
        buffer.flip();
        buffer.get(data);

        byteBuf.writeInt(length + 8);
        byteBuf.writeBytes(data);
    }

    protected void decode(ByteBuf data) {
        while (data.readableBytes() >= 4) {
            short tag = (short) ((data.readUnsignedByte() << 8) | (data.readUnsignedByte()));
            short len = (short) ((data.readUnsignedByte() << 8) | (data.readUnsignedByte()));

            if (data.readableBytes() < len - 4) {
                return;
            }

            byte[] value = new byte[len - 4];
            data.readBytes(value);
            parameters.put(tag, factory.createParameter(tag, value));

            // The Parameter Length does not include any padding octets. We have
            // to consider padding here
            int padding = 4 - (len % 4);
            if (padding < 4) {
                if (data.readableBytes() < padding)
                    return;
                else
                    data.skipBytes(padding);
            }
        }
    }

    public int getMessageClass() {
        return messageClass;
    }

    public int getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        TextBuilder tb = new TextBuilder();
        tb.append(this.message).append(" Params(");
        for (FastMap.Entry<Short, Parameter> e = parameters.head(), end = parameters.tail(); (e = e.getNext()) != end;) {
            Parameter value = e.getValue();
            tb.append(value.toString());
            tb.append(", ");
        }
        tb.append(")");
        return tb.toString();
    }
}
