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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class NetworkAppearanceImpl extends ParameterImpl implements NetworkAppearance, XMLSerializable {

	private static final String VALUE = "value";

	private static final long UNSIGNED_INT_MAX_VALUE = 0xFFFFFFFF;
	private long value;
	
	public NetworkAppearanceImpl(){
		this.tag = Parameter.Network_Appearance;
	}

	protected NetworkAppearanceImpl(long value) {
		this.value = value;
		this.tag = Parameter.Network_Appearance;
	}

	protected NetworkAppearanceImpl(byte[] data) {
		this.value = 0;
		this.value |= data[0] & 0xFF;
		this.value <<= 8;
		this.value |= data[1] & 0xFF;
		this.value <<= 8;
		this.value |= data[2] & 0xFF;
		this.value <<= 8;
		this.value |= data[3] & 0xFF;
		this.tag = Parameter.Network_Appearance;
	}

	public long getNetApp() {
		return value;
	}

	@Override
	protected byte[] getValue() {
		byte[] data = new byte[4];
		data[0] = (byte) (value >>> 24);
		data[1] = (byte) (value >>> 16);
		data[2] = (byte) (value >>> 8);
		data[3] = (byte) (value);

		return data;
	}

	@Override
	public String toString() {
		return String.format("NetworkAppearance value=%d", value);
	}

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<NetworkAppearanceImpl> RC_XML = new XMLFormat<NetworkAppearanceImpl>(
			NetworkAppearanceImpl.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, NetworkAppearanceImpl localRkId)
				throws XMLStreamException {
			localRkId.value = xml.getAttribute(VALUE).toLong();
		}

		@Override
		public void write(NetworkAppearanceImpl localRkId, javolution.xml.XMLFormat.OutputElement xml)
				throws XMLStreamException {
			xml.setAttribute(VALUE, localRkId.value);
		}
	};

}
