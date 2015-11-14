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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;
import java.util.ArrayList;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ChangeOfLocation;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DpSpecificCriteriaAlt;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.primitives.ArrayListSerializingBase;

/**
*
* @author sergey vetyutnev
*
*/
public class DpSpecificCriteriaAltImpl extends SequenceBase implements DpSpecificCriteriaAlt {

    private static final String CHANGE_OF_POSITION_CONTROL_INFO = "changeOfPositionControlInfo";
    private static final String CHANGE_OF_POSITION_CONTROL_INFO_LIST = "changeOfPositionControlInfoList";
    private static final String NUMBER_OF_DIGITS = "numberOfDigits";

    public static final int _ID_changeOfPositionControlInfo = 0;
    public static final int _ID_numberOfDigits = 1;

    private ArrayList<ChangeOfLocation> changeOfPositionControlInfo;
    private Integer numberOfDigits;

    public DpSpecificCriteriaAltImpl() {
        super("DpSpecificCriteriaAlt");
    }

    public DpSpecificCriteriaAltImpl(ArrayList<ChangeOfLocation> changeOfPositionControlInfo, Integer numberOfDigits) {
        super("DpSpecificCriteriaAlt");

        this.changeOfPositionControlInfo = changeOfPositionControlInfo;
        this.numberOfDigits = numberOfDigits;
    }

    @Override
    public ArrayList<ChangeOfLocation> getChangeOfPositionControlInfo() {
        return changeOfPositionControlInfo;
    }

    @Override
    public Integer getNumberOfDigits() {
        return numberOfDigits;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException, MAPParsingComponentException,
            INAPParsingComponentException {

        this.changeOfPositionControlInfo = null;
        this.numberOfDigits = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_changeOfPositionControlInfo:
                    this.changeOfPositionControlInfo = new ArrayList<ChangeOfLocation>();
                    AsnInputStream ais2 = ais.readSequenceStream();

                    while (true) {
                        if (ais2.available() == 0)
                            break;

                        ais2.readTag();

                        ChangeOfLocationImpl elem = new ChangeOfLocationImpl();
                        elem.decodeAll(ais2);
                        this.changeOfPositionControlInfo.add(elem);
                    }
                    if (this.changeOfPositionControlInfo.size() < 1 || this.changeOfPositionControlInfo.size() > 10)
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": changeOfPositionControlInfo list must be from 1 to 10 elements, found: " + this.changeOfPositionControlInfo.size(),
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    break;
                case _ID_numberOfDigits:
                    this.numberOfDigits = (int) ais.readInteger();
                    break;

                default:
                    ais.advanceElement();
                    break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.changeOfPositionControlInfo == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": changeOfPositionControlInfo parameter is mandatory but does not found", CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeData(AsnOutputStream aos) throws CAPException {

        if (this.changeOfPositionControlInfo == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": changeOfPositionControlInfo must not be null");
        if (this.changeOfPositionControlInfo.size() < 1 || this.changeOfPositionControlInfo.size() > 10)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": changeOfPositionControlInfo list length must be from 1 to 30");
        if (this.numberOfDigits != null && (this.numberOfDigits < 1 || this.numberOfDigits > 255))
            throw new CAPException("Error while encoding " + _PrimitiveName + ": numberOfDigits must has value from 1 to 255");

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_changeOfPositionControlInfo);
            int pos = aos.StartContentDefiniteLength();
            for (ChangeOfLocation be : this.changeOfPositionControlInfo) {
                ChangeOfLocationImpl bee = (ChangeOfLocationImpl) be;
                bee.encodeAll(aos);
            }
            aos.FinalizeContent(pos);

            if (this.numberOfDigits != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_numberOfDigits, this.numberOfDigits);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.changeOfPositionControlInfo != null) {
            sb.append("changeOfPositionControlInfo=[");
            boolean firstItem = true;
            for (ChangeOfLocation be : this.changeOfPositionControlInfo) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }
        if (this.numberOfDigits != null) {
            sb.append("numberOfDigits=");
            sb.append(numberOfDigits);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<DpSpecificCriteriaAltImpl> DP_SPECIFIC_CRITERIA_ALT_XML = new XMLFormat<DpSpecificCriteriaAltImpl>(
            DpSpecificCriteriaAltImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, DpSpecificCriteriaAltImpl dpSpecificCriteriaAlt) throws XMLStreamException {
            DpSpecificCriteriaAlt_ChangeOfLocation al = xml.get(CHANGE_OF_POSITION_CONTROL_INFO_LIST, DpSpecificCriteriaAlt_ChangeOfLocation.class);
            if (al != null) {
                dpSpecificCriteriaAlt.changeOfPositionControlInfo = al.getData();
            }

            dpSpecificCriteriaAlt.numberOfDigits = xml.get(NUMBER_OF_DIGITS, Integer.class);
        }

        @Override
        public void write(DpSpecificCriteriaAltImpl dpSpecificCriteriaAlt, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (dpSpecificCriteriaAlt.changeOfPositionControlInfo != null) {
                DpSpecificCriteriaAlt_ChangeOfLocation al = new DpSpecificCriteriaAlt_ChangeOfLocation(dpSpecificCriteriaAlt.changeOfPositionControlInfo);
                xml.add(al, CHANGE_OF_POSITION_CONTROL_INFO_LIST, DpSpecificCriteriaAlt_ChangeOfLocation.class);
            }

            if (dpSpecificCriteriaAlt.numberOfDigits != null)
                xml.add((Integer) dpSpecificCriteriaAlt.numberOfDigits, NUMBER_OF_DIGITS, Integer.class);
        }
    };

    public static class DpSpecificCriteriaAlt_ChangeOfLocation extends ArrayListSerializingBase<ChangeOfLocation> {

        public DpSpecificCriteriaAlt_ChangeOfLocation() {
            super(CHANGE_OF_POSITION_CONTROL_INFO, ChangeOfLocationImpl.class);
        }

        public DpSpecificCriteriaAlt_ChangeOfLocation(ArrayList<ChangeOfLocation> data) {
            super(CHANGE_OF_POSITION_CONTROL_INFO, ChangeOfLocationImpl.class, data);
        }

    }

}
