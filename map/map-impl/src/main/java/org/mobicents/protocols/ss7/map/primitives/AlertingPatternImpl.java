/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.primitives;

import java.io.IOException;
import java.util.Arrays;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingCategory;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingLevel;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;

/**
 * TODO : Add XML Serialization
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class AlertingPatternImpl implements AlertingPattern, MAPAsnPrimitive {

	public static final String _PrimitiveName = "AlertingPattern";

	private byte[] data;

	public AlertingPatternImpl() {
	}

	public AlertingPatternImpl(byte[] data) {
		this.data = data;
	}

	public AlertingPatternImpl(AlertingLevel alertingLevel) {
		this.data = new byte[] { alertingLevel.getLevel() };
	}

	public AlertingPatternImpl(AlertingCategory alertingCategory) {
		this.data = new byte[] { alertingCategory.getCategory() };
	}

	public byte[] getData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern#
	 * getAlertingLevel()
	 */
	public AlertingLevel getAlertingLevel() {

		if (this.data == null || this.data.length != 1)
			return null;
		else
			return AlertingLevel.getInstance(this.data[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern#
	 * getAlertingCategory()
	 */
	public AlertingCategory getAlertingCategory() {

		if (this.data == null || this.data.length != 1)
			return null;
		else
			return AlertingCategory.getInstance(this.data[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
	 */
	public int getTag() throws MAPException {
		return Tag.STRING_OCTET;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass
	 * ()
	 */
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive
	 * ()
	 */
	public boolean getIsPrimitive() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll
	 * (org.mobicents.protocols.asn.AsnInputStream)
	 */
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData
	 * (org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		if (length != 1)
			throw new MAPParsingComponentException("Error decoding AlertingPattern: the " + _PrimitiveName + " field must contain 1 octets. Contains: "
					+ length, MAPParsingComponentExceptionReason.MistypedParameter);

		this.data = ansIS.readOctetStringData(length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
	 * (org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if (this.data == null)
			throw new MAPException("Error when encoding " + _PrimitiveName + ": data must not be empty");
		if (this.data.length != 1)
			throw new MAPException("Error when encoding " + _PrimitiveName + ": data length must be equal 1");

		asnOs.writeOctetStringData(this.data);
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");

		AlertingLevel al = this.getAlertingLevel();
		if (al != null) {
			sb.append("AlertingLevel=");
			sb.append(al);
		}
		AlertingCategory ac = this.getAlertingCategory();
		if (ac != null) {
			sb.append(" AlertingCategory=");
			sb.append(ac);
		}
		sb.append("]");

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlertingPatternImpl other = (AlertingPatternImpl) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<AlertingPatternImpl> ADDRESS_STRING_XML = new XMLFormat<AlertingPatternImpl>(AlertingPatternImpl.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, AlertingPatternImpl alertingPatternImpl) throws XMLStreamException {
			int size = xml.getAttribute("size", 0);
			alertingPatternImpl.data = new byte[size];
			for(int i=0;i<size;i++){
				alertingPatternImpl.data[i] = xml.get("value", Byte.class);
			}
		}

		@Override
		public void write(AlertingPatternImpl alertingPatternImpl, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			
			byte[] tempdata = alertingPatternImpl.data;
			xml.setAttribute("size",tempdata  == null ? 0 : tempdata.length);
			for (int i = 0; i < tempdata.length; i++) {
				xml.add(tempdata[i], "value", Byte.class);
			}

		}
	};

}
