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
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.CallAcceptedSpecificInfo;
import org.restcomm.protocols.ss7.cap.primitives.SequenceBase;
import org.restcomm.protocols.ss7.inap.api.INAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;

/**
*
* @author sergey vetyutnev
*
*/
public class CallAcceptedSpecificInfoImpl extends SequenceBase implements CallAcceptedSpecificInfo {

    private static final String LOCATION_INFORMATION = "locationInformation";

    public static final int _ID_locationInformation = 50;

    private LocationInformation locationInformation;

    public CallAcceptedSpecificInfoImpl() {
        super("CallAcceptedSpecificInfo");
    }

    public CallAcceptedSpecificInfoImpl(LocationInformation locationInformation) {
        super("CallAcceptedSpecificInfo");

        this.locationInformation = locationInformation;
    }

    @Override
    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException, MAPParsingComponentException,
            INAPParsingComponentException {

        this.locationInformation = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_locationInformation:
                        this.locationInformation = new LocationInformationImpl();
                        ((LocationInformationImpl) this.locationInformation).decodeAll(ais);
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
        try {
            if (this.locationInformation != null) {
                ((LocationInformationImpl) this.locationInformation).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_locationInformation);
            }
        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.locationInformation != null) {
            sb.append("locationInformation=");
            sb.append(locationInformation);
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CallAcceptedSpecificInfoImpl> CALL_ACCEPTED_SPECIFIC_INFO_XML = new XMLFormat<CallAcceptedSpecificInfoImpl>(
            CallAcceptedSpecificInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CallAcceptedSpecificInfoImpl callAcceptedSpecificInfo)
                throws XMLStreamException {
            callAcceptedSpecificInfo.locationInformation = xml.get(LOCATION_INFORMATION, LocationInformationImpl.class);
        }

        @Override
        public void write(CallAcceptedSpecificInfoImpl callAcceptedSpecificInfo, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            if (callAcceptedSpecificInfo.locationInformation != null) {
                xml.add((LocationInformationImpl) callAcceptedSpecificInfo.locationInformation, LOCATION_INFORMATION, LocationInformationImpl.class);
            }
        }
    };

}
