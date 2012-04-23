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

package org.mobicents.protocols.ss7.map.primitives;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ISDNAddressStringImpl extends AddressStringImpl implements ISDNAddressString {

	public ISDNAddressStringImpl() {
	}

	public ISDNAddressStringImpl(AddressNature addressNature, NumberingPlan numberingPlan, String address) {
		super(addressNature, numberingPlan, address);
	}

	@Override
	protected void _testLengthDecode(int length) throws MAPParsingComponentException {
		if (length > 9)
			throw new MAPParsingComponentException("Error when decoding FTNAddressString: mesage length must not exceed 9",
					MAPParsingComponentExceptionReason.MistypedParameter);
	}

	@Override
	protected void _testLengthEncode() throws MAPException {

		if (this.address == null && this.address.length() > 16)
			throw new MAPException("Error when encoding ISDNAddressString: address length must not exceed 16 digits");
	}

	@Override
	public String toString() {
		return "ISDNAddressString[AddressNature=" + this.addressNature + ", NumberingPlan=" + this.numberingPlan + ", Address="
				+ this.address + "]";
	}
	
	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<ISDNAddressStringImpl> ISDN_ADDRESS_STRING_XML = new XMLFormat<ISDNAddressStringImpl>(
			ISDNAddressStringImpl.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, ISDNAddressStringImpl isdnAddressStringImpl)
				throws XMLStreamException {
			ADDRESS_STRING_XML.read(xml, isdnAddressStringImpl);
		}

		@Override
		public void write(ISDNAddressStringImpl isdnAddressStringImpl, javolution.xml.XMLFormat.OutputElement xml)
				throws XMLStreamException {
			ADDRESS_STRING_XML.write(isdnAddressStringImpl, xml);
		}
	};
}
