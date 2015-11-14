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

package org.mobicents.protocols.ss7.map.errors;

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
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSsErrorStatus;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
public class MAPErrorMessageSsErrorStatusImpl extends MAPErrorMessageImpl implements MAPErrorMessageSsErrorStatus {
    private static final String DATA = "data";
    private static final String Q_BIT = "qBit";
    private static final String P_BIT = "pBit";
    private static final String R_BIT = "rBit";
    private static final String A_BIT = "aBit";

    public static final int _mask_QBit = 0x08;
    public static final int _mask_PBit = 0x04;
    public static final int _mask_RBit = 0x02;
    public static final int _mask_ABit = 0x01;

    private int data;

    protected String _PrimitiveName = "MAPErrorMessageSsErrorStatus";

    public MAPErrorMessageSsErrorStatusImpl(int data) {
        super((long) MAPErrorCode.ssErrorStatus);

        this.data = data;
    }

    public MAPErrorMessageSsErrorStatusImpl(boolean qBit, boolean pBit, boolean rBit, boolean aBit) {
        super((long) MAPErrorCode.ssErrorStatus);

        this.data = (qBit ? _mask_QBit : 0) + (pBit ? _mask_PBit : 0) + (rBit ? _mask_RBit : 0) + (aBit ? _mask_ABit : 0);
    }

    public MAPErrorMessageSsErrorStatusImpl() {
        super((long) MAPErrorCode.ssErrorStatus);
    }

    public boolean isEmSsErrorStatus() {
        return true;
    }

    public MAPErrorMessageSsErrorStatus getEmSsErrorStatus() {
        return this;
    }

    @Override
    public int getData() {
        return data;
    }

    @Override
    public boolean getQBit() {
        return (this.data & _mask_QBit) != 0;
    }

    @Override
    public boolean getPBit() {
        return (this.data & _mask_PBit) != 0;
    }

    @Override
    public boolean getRBit() {
        return (this.data & _mask_RBit) != 0;
    }

    @Override
    public boolean getABit() {
        return (this.data & _mask_ABit) != 0;
    }

    @Override
    public void setData(int val) {
        this.data = val;
    }

    @Override
    public void setQBit(boolean val) {
        if (val) {
            this.data |= _mask_QBit;
        } else {
            this.data &= (_mask_QBit ^ 0xFF);
        }
    }

    @Override
    public void setPBit(boolean val) {
        if (val) {
            this.data |= _mask_PBit;
        } else {
            this.data &= (_mask_PBit ^ 0xFF);
        }
    }

    @Override
    public void setRBit(boolean val) {
        if (val) {
            this.data |= _mask_RBit;
        } else {
            this.data &= (_mask_RBit ^ 0xFF);
        }
    }

    @Override
    public void setABit(boolean val) {
        if (val) {
            this.data |= _mask_ABit;
        } else {
            this.data &= (_mask_ABit ^ 0xFF);
        }
    }

    @Override
    public int getTag() throws MAPException {
        return Tag.STRING_OCTET;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
    }

    @Override
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

    @Override
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

    private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {

        if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || localAis.getTag() != Tag.STRING_OCTET
                || !localAis.isTagPrimitive())
            throw new MAPParsingComponentException("Error decoding " + _PrimitiveName
                    + ": bad tag class or tag or parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);

        if (length != 1)
            throw new MAPParsingComponentException("Error decoding " + _PrimitiveName
                    + ": the field must contain 1 octet. Contains: " + length,
                    MAPParsingComponentExceptionReason.MistypedParameter);

        this.data = localAis.read();
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        asnOs.write(this.data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getQBit()) {
            sb.append("QBit");
            sb.append(", ");
        }
        if (this.getPBit()) {
            sb.append("PBit");
            sb.append(", ");
        }
        if (this.getRBit()) {
            sb.append("RBit");
            sb.append(", ");
        }
        if (this.getABit()) {
            sb.append("ABit");
            sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MAPErrorMessageSsErrorStatusImpl> MAP_ERROR_MESSAGE_SS_ERROR_STATUS_XML = new XMLFormat<MAPErrorMessageSsErrorStatusImpl>(
            MAPErrorMessageSsErrorStatusImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MAPErrorMessageSsErrorStatusImpl errorMessage)
                throws XMLStreamException {
            MAP_ERROR_MESSAGE_XML.read(xml, errorMessage);
            errorMessage.data = xml.get(DATA, Integer.class);
            xml.get(Q_BIT, Boolean.class);
            xml.get(P_BIT, Boolean.class);
            xml.get(R_BIT, Boolean.class);
            xml.get(A_BIT, Boolean.class);
        }

        @Override
        public void write(MAPErrorMessageSsErrorStatusImpl errorMessage, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            MAP_ERROR_MESSAGE_XML.write(errorMessage, xml);
            xml.add(errorMessage.data, DATA, Integer.class);
            xml.add(errorMessage.getQBit(), Q_BIT, Boolean.class);
            xml.add(errorMessage.getPBit(), P_BIT, Boolean.class);
            xml.add(errorMessage.getRBit(), R_BIT, Boolean.class);
            xml.add(errorMessage.getABit(), A_BIT, Boolean.class);
        }
    };

}
