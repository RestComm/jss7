/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
