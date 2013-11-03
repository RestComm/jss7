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

package org.mobicents.protocols.ss7.cap.EsiBcsm;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ODisconnectSpecificInfoImpl extends SequenceBase implements ODisconnectSpecificInfo {

    private static final String RELEASE_CAUSE = "releaseCause";

    public static final int _ID_releaseCause = 0;

    private CauseCap releaseCause;

    public ODisconnectSpecificInfoImpl() {
        super("ODisconnectSpecificInfo");
    }

    public ODisconnectSpecificInfoImpl(CauseCap releaseCause) {
        super("ODisconnectSpecificInfo");
        this.releaseCause = releaseCause;
    }

    @Override
    public CauseCap getReleaseCause() {
        return releaseCause;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException {

        this.releaseCause = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_releaseCause:
                        this.releaseCause = new CauseCapImpl();
                        ((CauseCapImpl) this.releaseCause).decodeAll(ais);
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
        if (this.releaseCause != null) {
            ((CauseCapImpl) this.releaseCause).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_releaseCause);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.releaseCause != null) {
            sb.append("releaseCause= [");
            sb.append(releaseCause);
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ODisconnectSpecificInfoImpl> O_DISCONNECT_SPECIFIC_INFO_XML = new XMLFormat<ODisconnectSpecificInfoImpl>(
            ODisconnectSpecificInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ODisconnectSpecificInfoImpl oDisconnectSpecificInfo)
                throws XMLStreamException {
            oDisconnectSpecificInfo.releaseCause = xml.get(RELEASE_CAUSE, CauseCapImpl.class);
        }

        @Override
        public void write(ODisconnectSpecificInfoImpl oDisconnectSpecificInfo, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            if (oDisconnectSpecificInfo.releaseCause != null) {
                xml.add((CauseCapImpl) oDisconnectSpecificInfo.releaseCause, RELEASE_CAUSE, CauseCapImpl.class);
            }
        }
    };
}
