/*
 * TeleStax, Open Source Cloud Communications  
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.map.primitives;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class IMSIImpl extends TbcdString implements IMSI {

	private static final String NUMBER = "number";

	public IMSIImpl() {
		super(3, 8, "IMSI");
	}

	public IMSIImpl(String data) {
		super(3, 8, "IMSI", data);
	}

	public String getData() {
		return this.data;
	}
	
	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<IMSIImpl> IMSI_XML = new XMLFormat<IMSIImpl>(IMSIImpl.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, IMSIImpl imsi) throws XMLStreamException {
			imsi.data = xml.getAttribute(NUMBER, "");
		}

		@Override
		public void write(IMSIImpl imsi, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			xml.setAttribute(NUMBER, imsi.data);
		}
	};
}
