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
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ONoAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class ONoAnswerSpecificInfoImpl extends SequenceBase implements ONoAnswerSpecificInfo {

    public ONoAnswerSpecificInfoImpl() {
        super("ONoAnswerSpecificInfo");
    }

    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException {

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            ais.advanceElement();
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ONoAnswerSpecificInfoImpl> O_NO_ANSWER_SPECIFIC_INFO_XML = new XMLFormat<ONoAnswerSpecificInfoImpl>(
            ONoAnswerSpecificInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ONoAnswerSpecificInfoImpl oNoAnswerSpecificInfo)
                throws XMLStreamException {
        }

        @Override
        public void write(ONoAnswerSpecificInfoImpl oNoAnswerSpecificInfo, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

        }
    };
}
