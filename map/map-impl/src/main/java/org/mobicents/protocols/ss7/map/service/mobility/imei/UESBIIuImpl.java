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

package org.mobicents.protocols.ss7.map.service.mobility.imei;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIuA;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIuB;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author normandes
 *
 */
public class UESBIIuImpl extends SequenceBase implements UESBIIu {

    private static final int _TAG_UESBI_IuA = 0;
    private static final int _TAG_UESBI_IuB = 1;

    private UESBIIuA uesbiIuA;
    private UESBIIuB uesbiIuB;

    public UESBIIuImpl() {
        super("UESBIIu");
    }

    public UESBIIuImpl(UESBIIuA uesbiIuA, UESBIIuB uesbiIuB) {
        super("UESBIIu");
        this.uesbiIuA = uesbiIuA;
        this.uesbiIuB = uesbiIuB;
    }

    @Override
    public UESBIIuA getUESBI_IuA() {
        return this.uesbiIuA;
    }

    @Override
    public UESBIIuB getUESBI_IuB() {
        return this.uesbiIuB;
    }

    public void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.uesbiIuA = null;
        this.uesbiIuB = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_UESBI_IuA:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter uesbiIuA is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.uesbiIuA = new UESBIIuAImpl();
                        ((UESBIIuAImpl) this.uesbiIuA).decodeAll(ais);
                        break;

                    case _TAG_UESBI_IuB:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter uesbiIuB is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.uesbiIuB = new UESBIIuBImpl();
                        ((UESBIIuBImpl) this.uesbiIuB).decodeAll(ais);
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.uesbiIuA != null) {
            ((UESBIIuAImpl) this.uesbiIuA).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_UESBI_IuA);
        }
        if (this.uesbiIuB != null) {
            ((UESBIIuBImpl) this.uesbiIuB).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_UESBI_IuB);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.uesbiIuA != null) {
            sb.append("uesbiIuA=");
            sb.append(this.uesbiIuA);
            sb.append(", ");
        }

        if (this.uesbiIuB != null) {
            sb.append("uesbiIuB=");
            sb.append(this.uesbiIuB);
        }
        sb.append("]");

        return sb.toString();
    }

}
