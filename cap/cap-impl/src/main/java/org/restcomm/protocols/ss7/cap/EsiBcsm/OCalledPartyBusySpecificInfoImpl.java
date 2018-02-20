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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentException;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OCalledPartyBusySpecificInfo;
import org.restcomm.protocols.ss7.cap.api.isup.CauseCap;
import org.restcomm.protocols.ss7.cap.isup.CauseCapImpl;
import org.restcomm.protocols.ss7.cap.primitives.SequenceBase;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class OCalledPartyBusySpecificInfoImpl extends SequenceBase implements OCalledPartyBusySpecificInfo {

    private static final String BUSY_CAUSE = "busyCause";

    public static final int _ID_busyCause = 0;

    private CauseCap busyCause;

    public OCalledPartyBusySpecificInfoImpl() {
        super("OCalledPartyBusySpecificInfo");
    }

    public OCalledPartyBusySpecificInfoImpl(CauseCap busyCause) {
        super("OCalledPartyBusySpecificInfo");
        this.busyCause = busyCause;
    }

    @Override
    public CauseCap getBusyCause() {
        return busyCause;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException {

        this.busyCause = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_busyCause:
                        this.busyCause = new CauseCapImpl();
                        ((CauseCapImpl) this.busyCause).decodeAll(ais);
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
    public void encodeData(AsnOutputStream asnOs) throws CAPException {
        if (this.busyCause != null) {
            ((CauseCapImpl) this.busyCause).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_busyCause);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.busyCause != null) {
            sb.append("busyCause= [");
            sb.append(busyCause);
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<OCalledPartyBusySpecificInfoImpl> ROUTE_SELECT_FAILURE_SPECIFIC_INFO_XML = new XMLFormat<OCalledPartyBusySpecificInfoImpl>(
            OCalledPartyBusySpecificInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                OCalledPartyBusySpecificInfoImpl oCalledPartyBusySpecificInfo) throws XMLStreamException {
            oCalledPartyBusySpecificInfo.busyCause = xml.get(BUSY_CAUSE, CauseCapImpl.class);
        }

        @Override
        public void write(OCalledPartyBusySpecificInfoImpl oCalledPartyBusySpecificInfo,
                javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {

            if (oCalledPartyBusySpecificInfo.busyCause != null) {
                xml.add(((CauseCapImpl) oCalledPartyBusySpecificInfo.busyCause), BUSY_CAUSE, CauseCapImpl.class);
            }
        }
    };
}
