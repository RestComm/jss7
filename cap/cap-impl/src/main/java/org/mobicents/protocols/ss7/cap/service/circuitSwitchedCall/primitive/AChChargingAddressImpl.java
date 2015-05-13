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
package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.primitives.AChChargingAddress;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;

/**
 * AChChargingAddress implementation class.
 *
 * @author alerant appngin
 */
@SuppressWarnings("serial")
public class AChChargingAddressImpl implements AChChargingAddress,
        CAPAsnPrimitive {

    private static final String LEG_ID = "legID";
    private static final String SRF_CONNECTION = "srfConnection";

    public static final int _ID_legID = 2;
    public static final int _ID_srfConnection = 50;

    public static final String _PrimitiveName = "AChChargingAddress";

    private LegID legID;
    private Integer srfConnection;

    public AChChargingAddressImpl() {
    }

    public AChChargingAddressImpl(LegID legID) {
        this.legID = legID;
    }

    public AChChargingAddressImpl(int srfConnection) {
        this.srfConnection = srfConnection;
    }

    public LegID getLegID() {
        return legID;
    }

    public int getSrfConnection() {
        return srfConnection == null ? -1 : srfConnection;
    }

    public int getTag() throws CAPException {
        if (legID != null)
            return _ID_legID;
        else
            return _ID_srfConnection;
    }

    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS)
            throws CAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding "
                    + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException(
                    "AsnException when decoding " + _PrimitiveName + ": "
                            + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException(
                    "INAPParsingComponentException when decoding "
                            + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length)
            throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding "
                    + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException(
                    "AsnException when decoding " + _PrimitiveName + ": "
                            + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException(
                    "INAPParsingComponentException when decoding "
                            + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    private void _decode(AsnInputStream ais, int length)
            throws CAPParsingComponentException, IOException, AsnException,
            INAPParsingComponentException {

        this.legID = null;
        this.srfConnection = null;

        int tag = ais.readTag();

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
            throw new CAPParsingComponentException("Error while decoding "
                    + _PrimitiveName + ": bad choice tagClass "
                    + ais.getTagClass(),
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }

        switch (tag) {
        case _ID_legID:
            if (ais.isTagPrimitive())
                throw new CAPParsingComponentException("Error while decoding "
                        + _PrimitiveName + ".legID: Parameter is primitive",
                        CAPParsingComponentExceptionReason.MistypedParameter);
            LegIDImpl legID = new LegIDImpl();
            // LegIDImpl expects wrapping length/tag to be read already
            AsnInputStream inner = ais.readSequenceStream();
            inner.readTag();
            legID.decodeAll(inner);
            this.legID = legID;
            break;
        case _ID_srfConnection:
            if (!ais.isTagPrimitive())
                throw new CAPParsingComponentException("Error while decoding "
                        + _PrimitiveName
                        + ".srfConnection: Parameter is not primitive",
                        CAPParsingComponentExceptionReason.MistypedParameter);
            // CallSegmentID implicit Integer4
            this.srfConnection = (int) ais.readInteger();
            break;

        default:
            throw new CAPParsingComponentException("Error while decoding "
                    + _PrimitiveName + ": bad choice tag " + tag,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag)
            throws CAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding "
                    + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if ((this.legID == null) == (this.srfConnection == null)) {
            throw new CAPException("Error while encoding " + _PrimitiveName
                    + ": One and only one choice must be selected");
        }

        try {
            if (this.legID != null) {
                ((LegIDImpl) this.legID).encodeAll(asnOs,
                        Tag.CLASS_CONTEXT_SPECIFIC, _ID_legID);
            } else if (this.srfConnection != null) {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_srfConnection, this.srfConnection);
            }
        } catch (IOException e) {
            throw new CAPException("IOException when encoding "
                    + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (INAPException e) {
            throw new CAPException("INAPException when encoding "
                    + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding "
                    + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        boolean first = true;

        if (this.legID != null) {
            sb.append("legID=").append(this.legID);
            first = false;
        }

        if (this.srfConnection != null) {
            if (!first)
                sb.append(", ");
            sb.append("srfConnection=").append(this.srfConnection);
            first = false;
        }

        sb.append("]");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((legID == null) ? 0 : legID.hashCode());
        result = prime * result
                + ((srfConnection == null) ? 0 : srfConnection.hashCode());
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
        AChChargingAddressImpl other = (AChChargingAddressImpl) obj;
        if (legID == null) {
            if (other.legID != null)
                return false;
        } else if (!legID.equals(other.legID))
            return false;
        if (srfConnection == null) {
            if (other.srfConnection != null)
                return false;
        } else if (!srfConnection.equals(other.srfConnection))
            return false;
        return true;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<AChChargingAddressImpl> ACH_CHARGING_ADDRESS_XML = new XMLFormat<AChChargingAddressImpl>(
            AChChargingAddressImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                AChChargingAddressImpl achChargingAddress)
                throws XMLStreamException {

            achChargingAddress.legID = xml.get(LEG_ID, LegIDImpl.class);
            achChargingAddress.srfConnection = xml.get(SRF_CONNECTION,
                    Integer.class);

        }

        @Override
        public void write(AChChargingAddressImpl achChargingAddress,
                javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            xml.add((LegIDImpl) achChargingAddress.legID, LEG_ID,
                    LegIDImpl.class);

            xml.add(achChargingAddress.srfConnection, SRF_CONNECTION,
                    Integer.class);

        }
    };

}
