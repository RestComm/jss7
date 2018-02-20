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
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.CollectedInfoSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.restcomm.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.primitives.SequenceBase;
import org.restcomm.protocols.ss7.inap.api.INAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;

/**
*
* @author sergey vetyutnev
*
*/
public class CollectedInfoSpecificInfoImpl extends SequenceBase implements CollectedInfoSpecificInfo {

    private static final String CALLED_PARTY_NUMBER = "calledPartyNumber";

    public static final int _ID_calledPartyNumber = 0;

    private CalledPartyNumberCap calledPartyNumber;

    public CollectedInfoSpecificInfoImpl() {
        super("CollectedInfoSpecificInfo");
    }

    public CollectedInfoSpecificInfoImpl(CalledPartyNumberCap calledPartyNumber) {
        super("CollectedInfoSpecificInfo");

        this.calledPartyNumber = calledPartyNumber;
    }

    @Override
    public CalledPartyNumberCap getCalledPartyNumber() {
        return calledPartyNumber;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException, MAPParsingComponentException,
            INAPParsingComponentException {

        this.calledPartyNumber = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_calledPartyNumber:
                    this.calledPartyNumber = new CalledPartyNumberCapImpl();
                    ((CalledPartyNumberCapImpl) this.calledPartyNumber).decodeAll(ais);
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
        if (this.calledPartyNumber != null) {
            ((CalledPartyNumberCapImpl) this.calledPartyNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_calledPartyNumber);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.calledPartyNumber != null) {
            sb.append("calledPartyNumber= [");
            sb.append(calledPartyNumber);
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CollectedInfoSpecificInfoImpl> COLLECTED_INFO_SPECIFIC_INFO_XML = new XMLFormat<CollectedInfoSpecificInfoImpl>(
            CollectedInfoSpecificInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CollectedInfoSpecificInfoImpl collectedInfoSpecificInfo) throws XMLStreamException {
            collectedInfoSpecificInfo.calledPartyNumber = xml.get(CALLED_PARTY_NUMBER, CalledPartyNumberCapImpl.class);
        }

        @Override
        public void write(CollectedInfoSpecificInfoImpl collectedInfoSpecificInfo, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {

            if (collectedInfoSpecificInfo.calledPartyNumber != null) {
                xml.add(((CalledPartyNumberCapImpl) collectedInfoSpecificInfo.calledPartyNumber), CALLED_PARTY_NUMBER, CalledPartyNumberCapImpl.class);
            }
        }
    };

}
