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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGId;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class CSGIdImpl extends BitStringBase implements CSGId {

	private static final int BIT_STRING_LENGTH = 27;

	public CSGIdImpl() {
		super(BIT_STRING_LENGTH, BIT_STRING_LENGTH, BIT_STRING_LENGTH, "CSGId");
	}

	public CSGIdImpl(BitSetStrictLength data) {
		super(BIT_STRING_LENGTH, BIT_STRING_LENGTH, BIT_STRING_LENGTH, "CSGId", data);
	}

	public BitSetStrictLength getData() {
		return bitString;
	}	

	// TODO: add implementing of internal structure (?)

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<CSGIdImpl> CSG_ID_XML = new XMLFormat<CSGIdImpl>(CSGIdImpl.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, CSGIdImpl csgId) throws XMLStreamException {
			BIT_STRING_BASE_XML.read(xml, csgId);
		}

		@Override
		public void write(CSGIdImpl csgId, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			BIT_STRING_BASE_XML.write(csgId, xml);
		}
	};

}
