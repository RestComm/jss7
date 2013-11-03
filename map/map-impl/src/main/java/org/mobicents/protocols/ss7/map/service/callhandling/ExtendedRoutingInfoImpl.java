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
import org.mobicents.protocols.ss7.map.api.service.callhandling.CamelRoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/*
 *
 * @author cristian veliscu
 * @author sergey vetyutnev
 *
 */
public class ExtendedRoutingInfoImpl implements ExtendedRoutingInfo, MAPAsnPrimitive {
    private RoutingInfo routingInfo = null;
    private CamelRoutingInfo camelRoutingInfo = null;

    public static final int TAG_camel = 8;
    private static final String _PrimitiveName = "ExtendedRoutingInfo";

    public ExtendedRoutingInfoImpl() {
    }

    public ExtendedRoutingInfoImpl(RoutingInfo routingInfo) {
        this.routingInfo = routingInfo;
    }

    public ExtendedRoutingInfoImpl(CamelRoutingInfo camelRoutingInfo) {
        this.camelRoutingInfo = camelRoutingInfo;
    }

    @Override
    public RoutingInfo getRoutingInfo() {
        return this.routingInfo;
    }

    @Override
    public CamelRoutingInfo getCamelRoutingInfo() {
        return this.camelRoutingInfo;
    }

    @Override
    public int getTag() throws MAPException {
        if (routingInfo != null)
            return ((RoutingInfoImpl) routingInfo).getTag();
        return TAG_camel;
    }

    @Override
    public int getTagClass() {
        if (routingInfo != null)
            return ((RoutingInfoImpl) routingInfo).getTagClass();
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        if (routingInfo != null)
            return ((RoutingInfoImpl) routingInfo).getIsPrimitive();
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
        this.routingInfo = null;
        this.camelRoutingInfo = null;

        int tag = ais.getTag();
        if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            switch (tag) {
                case Tag.SEQUENCE:
                case Tag.STRING_OCTET:
                    this.routingInfo = new RoutingInfoImpl();
                    ((RoutingInfoImpl) this.routingInfo).decodeData(ais, length);
                    break;
                default:
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagNumber",
                            MAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
                case TAG_camel:
                    this.camelRoutingInfo = new CamelRoutingInfoImpl();
                    ((CamelRoutingInfoImpl) this.camelRoutingInfo).decodeData(ais, length);
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
        if (this.routingInfo == null && this.camelRoutingInfo == null)
            throw new MAPException("Both routingInfo and camelRoutingInfo must not be null");
        if (this.routingInfo != null && this.camelRoutingInfo != null)
            throw new MAPException("Both routingInfo and camelRoutingInfo must not be not null");

        if (this.routingInfo != null) {
            ((RoutingInfoImpl) this.routingInfo).encodeData(asnOs);
        } else {
            ((CamelRoutingInfoImpl) this.camelRoutingInfo).encodeData(asnOs);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.routingInfo != null) {
            sb.append(this.routingInfo.toString());
        } else if (this.camelRoutingInfo != null) {
            sb.append(this.camelRoutingInfo.toString());
        }

        sb.append("]");
        return sb.toString();
    }
}