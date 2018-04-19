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

package org.restcomm.protocols.ss7.map.primitives;

import javax.xml.bind.DatatypeConverter;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class GSNAddressImpl extends OctetStringBase implements GSNAddress {

    private static final String GSN_ADDRESS_ADDRESS_TYPE = "gsnAddressAddressType";
    private static final String DATA = "data";

    private static final String DEFAULT_VALUE = null;

    public GSNAddressImpl() {
        super(5, 17, "GSNAddress");
    }

    public GSNAddressImpl(byte[] data) {
        super(5, 17, "GSNAddress", data);
    }

    public GSNAddressImpl(GSNAddressAddressType addressType, byte[] addressData) throws MAPException {
        super(5, 17, "GSNAddress", null);

        if (addressType == null)
            throw new MAPException("addressType argument must not be null");
        if (addressData == null)
            throw new MAPException("addressData argument must not be null");

        fillData(addressType, addressData);
    }

    private void fillData(GSNAddressAddressType addressType, byte[] addressData) throws MAPException {
        switch (addressType) {
        case IPv4:
            if (addressData.length != 4)
                throw new MAPException("addressData argument must have length=4 for IPv4");
            break;
        case IPv6:
            if (addressData.length != 16)
                throw new MAPException("addressData argument must have length=4 for IPv6");
            break;
        }

        this.data = new byte[addressData.length + 1];
        this.data[0] = (byte) addressType.createGSNAddressFirstByte();
        System.arraycopy(addressData, 0, this.data, 1, addressData.length);
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public GSNAddressAddressType getGSNAddressAddressType() {
        if (data == null || data.length == 0)
            return null;
        int val = data[0] & 0xFF;
        return GSNAddressAddressType.getFromGSNAddressFirstByte(val);
    }

    @Override
    public byte[] getGSNAddressData() {
        GSNAddressAddressType type = getGSNAddressAddressType();
        if (type == null)
            return null;

        switch (type) {
        case IPv4:
            if (data.length >= 5) {
                byte[] res = new byte[4];
                System.arraycopy(this.data, 1, res, 0, 4);
                return res;
            }
            break;
        case IPv6:
            if (data.length >= 17) {
                byte[] res = new byte[16];
                System.arraycopy(this.data, 1, res, 0, 16);
                return res;
            }
            break;
        }

        return null;
    }

    @Override
    public String toString() {
        GSNAddressAddressType type = getGSNAddressAddressType();
        byte[] val = getGSNAddressData();

        if (type != null && val != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(_PrimitiveName);
            sb.append(" [");

            sb.append("type=");
            sb.append(type);
            sb.append(", data=[");
            sb.append(printDataArr(val));
            sb.append("]");

            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }

    protected String printDataArr(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        if (arr != null) {
            for (byte b : arr) {
                if (first)
                    first = false;
                else
                    sb.append(", ");
                sb.append(b & 0xFF);
            }
        }

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<GSNAddressImpl> GSN_ADDRESS_XML = new XMLFormat<GSNAddressImpl>(GSNAddressImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, GSNAddressImpl gsnAddress) throws XMLStreamException {
            String s0 = xml.getAttribute(GSN_ADDRESS_ADDRESS_TYPE, DEFAULT_VALUE);
            String s = xml.getAttribute(DATA, DEFAULT_VALUE);
            if (s0 != null && s != null) {
                try {
                    GSNAddressAddressType addressType = Enum.valueOf(GSNAddressAddressType.class, s0);
                    byte[] addressData = DatatypeConverter.parseHexBinary(s);
                    gsnAddress.fillData(addressType, addressData);
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void write(GSNAddressImpl gsnAddress, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (gsnAddress.data != null) {
                xml.setAttribute(GSN_ADDRESS_ADDRESS_TYPE, gsnAddress.getGSNAddressAddressType().toString());
                xml.setAttribute(DATA, DatatypeConverter.printHexBinary(gsnAddress.getGSNAddressData()));
            }
        }
    };
}
