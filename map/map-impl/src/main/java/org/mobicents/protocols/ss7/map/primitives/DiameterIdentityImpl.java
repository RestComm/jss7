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

package org.mobicents.protocols.ss7.map.primitives;

import javax.xml.bind.DatatypeConverter;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;

/**
 * @author abhayani
 * @author sergey vetyutnev
 *
 */
public class DiameterIdentityImpl extends OctetStringBase implements DiameterIdentity {

	private static final String DATA = "data";

	private static final String DEFAULT_VALUE = null;

	public DiameterIdentityImpl() {
		super(9, 55, "DiameterIdentity");
	}

	public DiameterIdentityImpl(byte[] data) {
		super(9, 55, "DiameterIdentity", data);
	}

	public byte[] getData() {
		return data;
	}	

	// TODO: add implementing of internal structure (?)

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<DiameterIdentityImpl> DIAMETER_IDENTITY_XML = new XMLFormat<DiameterIdentityImpl>(DiameterIdentityImpl.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, DiameterIdentityImpl diameterIdentity) throws XMLStreamException {
			String s = xml.getAttribute(DATA, DEFAULT_VALUE);
			if (s != null) {
				diameterIdentity.data = DatatypeConverter.parseHexBinary(s);
			}
		}

		@Override
		public void write(DiameterIdentityImpl diameterIdentity, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			if (diameterIdentity.data != null) {
				xml.setAttribute(DATA, DatatypeConverter.printHexBinary(diameterIdentity.data));
			}
		}
	};
}
