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

package org.mobicents.protocols.ss7.map.service.callhandling;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.mobicents.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/*
 *
 * @author cristian veliscu
 *
 */
public class RoutingInfoImpl implements RoutingInfo, MAPAsnPrimitive {
    private ISDNAddressString roamingNumber = null;
    private ForwardingData forwardingData = null;

    private static final String _PrimitiveName = "RoutingInfo";

    public RoutingInfoImpl() {
    }

    public RoutingInfoImpl(ISDNAddressString roamingNumber) {
        this.roamingNumber = roamingNumber;
    }

    public RoutingInfoImpl(ForwardingData forwardingData) {
        this.forwardingData = forwardingData;
    }

    @Override
    public ISDNAddressString getRoamingNumber() {
        return this.roamingNumber;
    }

    @Override
    public ForwardingData getForwardingData() {
        return this.forwardingData;
    }

    @Override
    public int getTag() throws MAPException {
        if (roamingNumber != null)
            return Tag.STRING_OCTET;
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        if (roamingNumber != null)
            return Tag.CLASS_UNIVERSAL;
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        if (roamingNumber != null)
            return true;
        return false;
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

    private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.roamingNumber = null;
        this.forwardingData = null;

        int tag = ais.getTag();
        if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            switch (tag) {
                case Tag.STRING_OCTET:
                    this.roamingNumber = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.roamingNumber).decodeData(ais, length);
                    break;
                case Tag.SEQUENCE:
                    this.forwardingData = new ForwardingDataImpl();
                    ((ForwardingDataImpl) this.forwardingData).decodeData(ais, length);
                    break;
                default:
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagNumber",
                            MAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        }
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
        if (this.roamingNumber == null && this.forwardingData == null)
            throw new MAPException("Both roamingNumber and forwardingData must not be null");
        if (this.roamingNumber != null && this.forwardingData != null)
            throw new MAPException("Both roamingNumber and forwardingData must not be not null");

        if (this.roamingNumber != null) {
            ((ISDNAddressStringImpl) this.roamingNumber).encodeData(asnOs);
        } else {
            ((ForwardingDataImpl) this.forwardingData).encodeData(asnOs);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.roamingNumber != null) {
            sb.append(this.roamingNumber.toString());
        } else if (this.forwardingData != null) {
            sb.append(this.forwardingData.toString());
        }

        sb.append("]");
        return sb.toString();
    }
}