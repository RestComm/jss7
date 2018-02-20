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
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MidCallEvents;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TMidCallSpecificInfo;
import org.restcomm.protocols.ss7.cap.primitives.SequenceBase;
import org.restcomm.protocols.ss7.inap.api.INAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;

/**
*
* @author sergey vetyutnev
*
*/
public class TMidCallSpecificInfoImpl extends SequenceBase implements TMidCallSpecificInfo {

    public static final String MID_CALL_EVENTS = "midCallEvents";

    public static final int _ID_midCallEvents = 1;

    private MidCallEvents midCallEvents;

    public TMidCallSpecificInfoImpl() {
        super("TMidCallSpecificInfo");
    }

    public TMidCallSpecificInfoImpl(MidCallEvents midCallEvents) {
        super("TMidCallSpecificInfo");

        this.midCallEvents = midCallEvents;
    }

    @Override
    public MidCallEvents getMidCallEvents() {
        return midCallEvents;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException, MAPParsingComponentException,
            INAPParsingComponentException {

        this.midCallEvents = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_midCallEvents:
                    AsnInputStream ais2 = ais.readSequenceStream();
                    ais2.readTag();
                    this.midCallEvents = new MidCallEventsImpl();
                    ((MidCallEventsImpl) this.midCallEvents).decodeAll(ais2);
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
    public void encodeData(AsnOutputStream aos) throws CAPException {
        try {
            if (this.midCallEvents != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_midCallEvents);
                int pos = aos.StartContentDefiniteLength();
                ((MidCallEventsImpl) this.midCallEvents).encodeAll(aos);
                aos.FinalizeContent(pos);
            }
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.midCallEvents != null) {
            sb.append("midCallEvents=");
            sb.append(midCallEvents.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<TMidCallSpecificInfoImpl> T_MID_CALL_SPECIFIC_INFO_XML = new XMLFormat<TMidCallSpecificInfoImpl>(
            TMidCallSpecificInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, TMidCallSpecificInfoImpl tMidCallSpecificInfo)
                throws XMLStreamException {
            tMidCallSpecificInfo.midCallEvents = xml.get(MID_CALL_EVENTS, MidCallEventsImpl.class);
        }

        @Override
        public void write(TMidCallSpecificInfoImpl tMidCallSpecificInfo, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (tMidCallSpecificInfo.midCallEvents != null) {
                xml.add(((MidCallEventsImpl) tMidCallSpecificInfo.midCallEvents), MID_CALL_EVENTS, MidCallEventsImpl.class);
            }
        }
    };

}
