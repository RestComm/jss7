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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentException;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ChangeOfLocationAlt;
import org.restcomm.protocols.ss7.cap.primitives.SequenceBase;
import org.restcomm.protocols.ss7.inap.api.INAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;

/**
*
* @author sergey vetyutnev
*
*/
public class ChangeOfLocationAltImpl extends SequenceBase implements ChangeOfLocationAlt {

//    private static final String O_SERVICE_CHANGE_SPECIFIC_INFO = "oServiceChangeSpecificInfo";
//
//    public static final int _ID_oServiceChangeSpecificInfo = 0;

    public ChangeOfLocationAltImpl() {
        super("ChangeOfLocationAlt");
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException, MAPParsingComponentException,
            INAPParsingComponentException {

//        this.oServiceChangeSpecificInfo = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
//                case _ID_oServiceChangeSpecificInfo:
//                    this.oServiceChangeSpecificInfo = new OServiceChangeSpecificInfoImpl();
//                    ((OServiceChangeSpecificInfoImpl) this.oServiceChangeSpecificInfo).decodeAll(ais);
//                    break;

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
//        if (this.oServiceChangeSpecificInfo != null) {
//            ((OServiceChangeSpecificInfoImpl) this.oServiceChangeSpecificInfo).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_oServiceChangeSpecificInfo);
//        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

//        if (this.oServiceChangeSpecificInfo != null) {
//            sb.append("oServiceChangeSpecificInfo=");
//            sb.append(oServiceChangeSpecificInfo);
//            sb.append(", ");
//        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ChangeOfLocationAltImpl> CHANGE_OF_LOCATION_ALT_XML = new XMLFormat<ChangeOfLocationAltImpl>(ChangeOfLocationAltImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ChangeOfLocationAltImpl changeOfLocationAlt) throws XMLStreamException {
//            dpSpecificInfoAlt.oServiceChangeSpecificInfo = xml.get(O_SERVICE_CHANGE_SPECIFIC_INFO, OServiceChangeSpecificInfoImpl.class);
        }

        @Override
        public void write(ChangeOfLocationAltImpl changeOfLocationAlt, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
//            if (dpSpecificInfoAlt.oServiceChangeSpecificInfo != null)
//                xml.add((OServiceChangeSpecificInfoImpl) dpSpecificInfoAlt.oServiceChangeSpecificInfo, O_SERVICE_CHANGE_SPECIFIC_INFO,
//                        OServiceChangeSpecificInfoImpl.class);
        }
    };

}
