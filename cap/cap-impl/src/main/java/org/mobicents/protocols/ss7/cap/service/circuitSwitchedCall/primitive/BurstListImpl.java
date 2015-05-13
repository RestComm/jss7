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
package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.primitives.Burst;
import org.mobicents.protocols.ss7.cap.api.primitives.BurstList;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;

/**
 * BurstList implementation class.
 *
 * @author alerant appngin
 */
@SuppressWarnings("serial")
public class BurstListImpl extends SequenceBase implements BurstList {

    private static final String WARNING_PERIOD = "warningPeriod";
    private static final String BURSTS = "bursts";

    public static final int _ID_warningPeriod = 0;
    public static final int _ID_bursts = 1;

    public static final String _PrimitiveName = "BurstList";

    private int warningPeriod;
    private Burst bursts;

    public BurstListImpl() {
        super(_PrimitiveName);
    }

    public Burst getBurst() {
        return bursts;
    }

    public int getWarningPeriod() {
        return warningPeriod;
    }

    protected void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, IOException,
            AsnException, INAPParsingComponentException {

        this.warningPeriod = 30;
        this.bursts = null;

        AsnInputStream data = ais.readSequenceStreamData(length);
        // TODO: parse data

    }

    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (bursts == null) {
            throw new CAPException("Missing mandatory bursts parameter from " + _PrimitiveName);
        }

        try {
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_warningPeriod, warningPeriod);

            ((CAPAsnPrimitive) bursts).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_bursts);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        sb.append("warningPeriod=").append(this.warningPeriod);

        sb.append(", bursts=").append(this.bursts);

        sb.append("]");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bursts == null) ? 0 : bursts.hashCode());
        result = prime * result + warningPeriod;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BurstListImpl other = (BurstListImpl) obj;
        if (bursts == null) {
            if (other.bursts != null)
                return false;
        } else if (!bursts.equals(other.bursts))
            return false;
        if (warningPeriod != other.warningPeriod)
            return false;
        return true;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<BurstListImpl> BURST_LIST_XML = new XMLFormat<BurstListImpl>(
            BurstListImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, BurstListImpl burstList)
                throws XMLStreamException {

            burstList.warningPeriod = xml.get(WARNING_PERIOD, Integer.class);
            // TODO: Burst not implemented yet
            // burstList.bursts = xml.get(BURSTS, BurstImpl.class);

        }

        @Override
        public void write(BurstListImpl burstList, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            xml.add(burstList.warningPeriod, WARNING_PERIOD, Integer.class);

            // TODO Burst not implemented yet
            // xml.add((BurstImpl)burstList.bursts, BURSTS, BurstImpl.class);

        }
    };

}
