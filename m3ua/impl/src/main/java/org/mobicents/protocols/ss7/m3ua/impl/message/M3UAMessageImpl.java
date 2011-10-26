/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;
import javolution.util.FastMap;

import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * @author amit bhayani
 * @author kulikov
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

	public void encode(ByteBuffer buffer) {

		initialPosition = buffer.position();

		buffer.position(initialPosition + 8);

		encodeParams(buffer);

		int length = buffer.position() - initialPosition;
		// buffer.rewind();

		buffer.put(initialPosition++, (byte) 1);
		buffer.put(initialPosition++, (byte) 0);
		buffer.put(initialPosition++, (byte) messageClass);
		buffer.put(initialPosition++, (byte) messageType);
		buffer.putInt(initialPosition++, length);

		// buffer.position(length);
	}

	protected void decode(byte[] data) {
		this.decode(data, 0);
	}

	protected void decode(byte[] data, int initialPos) {
		int pos = initialPos;
		while (pos < data.length) {
			short tag = (short) ((data[pos] & 0xff) << 8 | (data[pos + 1] & 0xff));
			short len = (short) ((data[pos + 2] & 0xff) << 8 | (data[pos + 3] & 0xff));

			byte[] value = new byte[len - 4];

			System.arraycopy(data, pos + 4, value, 0, value.length);
			pos += len;
			parameters.put(tag, factory.createParameter(tag, value));

			// The Parameter Length does not include any padding octets. We have
			// to consider padding here
			int padding = 4 - (pos % 4);
			if (padding < 4) {
				pos += padding;
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
