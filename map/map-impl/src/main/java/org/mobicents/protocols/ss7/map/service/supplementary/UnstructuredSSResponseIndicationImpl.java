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

package org.mobicents.protocols.ss7.map.service.supplementary;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponseIndication;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;

/**
 * 
 * @author amit bhayani
 * 
 */
public class UnstructuredSSResponseIndicationImpl extends USSDMessageImpl implements UnstructuredSSResponseIndication {

	/**
	 * 
	 */
	public UnstructuredSSResponseIndicationImpl() {
		super();
	}

	public UnstructuredSSResponseIndicationImpl(byte ussdDataCodingSch, USSDString ussdString) {
		super(ussdDataCodingSch, ussdString);
	}

	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding UnstructuredSSResponseIndication: "
					+ e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding UnstructuredSSResponseIndication: "
					+ e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding UnstructuredSSResponseIndication: "
					+ e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding UnstructuredSSResponseIndication: "
					+ e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException,
			AsnException {

		AsnInputStream ais = ansIS.readSequenceStreamData(length);

		int tag = ais.readTag();

		// ussd-DataCodingScheme USSD-DataCodingScheme
		if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
			throw new MAPParsingComponentException(
					"Error while decoding UnstructuredSSResponseIndication: Parameter ussd-DataCodingScheme bad tag class or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);

		int length1 = ais.readLength();
		this.ussdDataCodingSch = ais.readOctetStringData(length1)[0];

		tag = ais.readTag();

		// ussd-String USSD-String
		if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
			throw new MAPParsingComponentException(
					"Error while decoding UnstructuredSSResponseIndication: Parameter ussd-String bad tag class or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);

		this.ussdString = new USSDStringImpl();
		((USSDStringImpl) this.ussdString).decodeAll(ais);

	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {

		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding UnstructuredSSResponseIndication", e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.ussdString == null)
			throw new MAPException("ussdString must not be null");

		try {
			asnOs.writeOctetString(new byte[] { this.ussdDataCodingSch });

			((USSDStringImpl) this.ussdString).encodeAll(asnOs);

		} catch (IOException e) {
			throw new MAPException("IOException when encoding UnstructuredSSResponseIndication", e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding UnstructuredSSResponseIndication", e);
		}
	}

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<UnstructuredSSResponseIndicationImpl> UNSTRUCTURED_SS_RESPONSE_XML = new XMLFormat<UnstructuredSSResponseIndicationImpl>(
			UnstructuredSSResponseIndicationImpl.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, UnstructuredSSResponseIndicationImpl ussdMessage)
				throws XMLStreamException {
			USSD_MESSAGE_XML.read(xml, ussdMessage);

		}

		@Override
		public void write(UnstructuredSSResponseIndicationImpl ussdMessage, javolution.xml.XMLFormat.OutputElement xml)
				throws XMLStreamException {
			USSD_MESSAGE_XML.write(ussdMessage, xml);
		}
	};

}
