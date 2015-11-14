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

package org.mobicents.protocols.ss7.cap.primitives;

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
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
*
* @author sergey vetyutnev
*
*/
public class AChChargingAddressImpl implements AChChargingAddress, CAPAsnPrimitive {
    private static final String LEG_ID = "legID";
    private static final String SRF_CONNECTION = "srfConnection";

    public static final int _ID_legID = 2;
    public static final int _ID_srfConnection = 50;

    public static final String _PrimitiveName = "AChChargingAddress";

    private LegID legID;
    private int srfConnection;

    public AChChargingAddressImpl() {
    }

    public AChChargingAddressImpl(LegID legID) {
        this.legID = legID;
    }

    public AChChargingAddressImpl(int srfConnection) {
        this.srfConnection = srfConnection;
    }


    @Override
    public LegID getLegID() {
        return legID;
    }

    @Override
    public int getSrfConnection() {
        return srfConnection;
    }

    @Override
    public int getTag() throws CAPException {
        if (legID != null) {
            return _ID_legID;
        } else if (srfConnection != 0) {
            return _ID_srfConnection;
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        if (legID != null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, MAPParsingComponentException, INAPParsingComponentException,
            IOException, AsnException {

        this.legID = null;
        this.srfConnection = 0;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
            case _ID_legID:
                if (ais.isTagPrimitive())
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + "-legID: parameter is primitive",
                            CAPParsingComponentExceptionReason.MistypedParameter);
                AsnInputStream ais2 = ais.readSequenceStreamData(length);
                ais2.readTag();
                this.legID = new LegIDImpl();
                ((LegIDImpl) this.legID).decodeAll(ais2);
                break;
            case _ID_srfConnection:
                this.srfConnection = (int) ais.readIntegerData(length);
                if (this.srfConnection < 1 || this.srfConnection > 127)
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + "-srfConnection: possible value 1..127, received="
                            + srfConnection, CAPParsingComponentExceptionReason.MistypedParameter);
                break;

                default:
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
                            CAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else {
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {
        try {
            if (legID != null) {
                ((LegIDImpl) legID).encodeAll(asnOs);
                return;
            } else if (srfConnection != 0) {
                if (srfConnection < 1 || srfConnection > 127)
                    throw new CAPException("Error when encoding " + _PrimitiveName + ": srfConnection must be 1..127, supplied=" + srfConnection);

                asnOs.writeIntegerData(srfConnection);
                return;
            }
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (INAPException e) {
            throw new CAPException("INAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (legID != null) {
            sb.append("legID=[");
            sb.append(legID);
            sb.append("]");
        } else if (srfConnection != 0) {
            sb.append("srfConnection=[");
            sb.append(srfConnection);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }

    protected static final XMLFormat<AChChargingAddressImpl> A_CH_CHARGING_ADDRESS_XML = new XMLFormat<AChChargingAddressImpl>(AChChargingAddressImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, AChChargingAddressImpl aChChargingAddress) throws XMLStreamException {
            aChChargingAddress.legID = xml.get(LEG_ID, LegIDImpl.class);
            Integer i1 = xml.get(SRF_CONNECTION, Integer.class);
            if (i1 != null)
                aChChargingAddress.srfConnection = i1;
        }

        @Override
        public void write(AChChargingAddressImpl aChChargingAddress, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (aChChargingAddress.legID != null) {
                xml.add((LegIDImpl) aChChargingAddress.legID, LEG_ID, LegIDImpl.class);
            }
            if (aChChargingAddress.srfConnection != 0) {
                xml.add((Integer) aChChargingAddress.srfConnection, SRF_CONNECTION, Integer.class);
            }
        }
    };

}
