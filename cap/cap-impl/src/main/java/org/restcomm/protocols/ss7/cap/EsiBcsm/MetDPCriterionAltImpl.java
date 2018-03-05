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
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterionAlt;
import org.restcomm.protocols.ss7.cap.primitives.SequenceBase;
import org.restcomm.protocols.ss7.inap.api.INAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;

/**
*
* @author sergey vetyutnev
*
*/
public class MetDPCriterionAltImpl extends SequenceBase implements MetDPCriterionAlt {

    public MetDPCriterionAltImpl() {
        super("MetDPCriterionAlt");
    }

    @Override
    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException, MAPParsingComponentException,
            INAPParsingComponentException {
        // TODO Auto-generated method stub

//        this.routeNotPermitted = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
//                    case _ID_routeNotPermitted:
//                        ais.readNull();
//                        this.routeNotPermitted = true;
//                        break;

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
//        try {
//            if (this.routeNotPermitted)
//                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_routeNotPermitted);
//        } catch (IOException e) {
//            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
//        } catch (AsnException e) {
//            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
//        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

//        if (this.routeNotPermitted) {
//            sb.append("routeNotPermitted");
//        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MetDPCriterionAltImpl> MET_DP_CRITERION_ALT_INFO = new XMLFormat<MetDPCriterionAltImpl>(
            MetDPCriterionAltImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MetDPCriterionAltImpl metDPCriterionAlt)
                throws XMLStreamException {
//            Boolean bval = xml.get(ROUTE_NOT_PERMITTED, Boolean.class);
//            if (bval != null)
//                metDPCriterionAlt.routeNotPermitted = bval;
        }

        @Override
        public void write(MetDPCriterionAltImpl metDPCriterionAlt, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
//            if (metDPCriterionAlt.routeNotPermitted)
//                xml.add(metDPCriterionAlt.routeNotPermitted, ROUTE_NOT_PERMITTED, Boolean.class);
        }
    };

}
