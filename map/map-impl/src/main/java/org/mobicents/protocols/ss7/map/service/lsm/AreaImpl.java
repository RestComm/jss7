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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.Area;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaIdentification;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class AreaImpl extends SequenceBase implements Area {

    private static final int _TAG_areaType = 0;
    private static final int _TAG_areaIdentification = 1;

    private AreaType areaType;
    private AreaIdentification areaIdentification;

    /**
     *
     */
    public AreaImpl() {
        super("Area");
    }

    /**
     * @param areaType
     * @param areaIdentification
     */
    public AreaImpl(AreaType areaType, AreaIdentification areaIdentification) {
        super("Area");
        this.areaType = areaType;
        this.areaIdentification = areaIdentification;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.Area#getAreaType()
     */
    public AreaType getAreaType() {
        return this.areaType;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.Area#getAreaIdentification ()
     */
    public AreaIdentification getAreaIdentification() {
        return this.areaIdentification;
    }

    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.areaType = null;
        this.areaIdentification = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_areaType) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter 0 [areaType [0] AreaType] bad tag class, tag or not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        int i1 = (int) ais.readInteger();
        this.areaType = AreaType.getAreaType(i1);

        tag = ais.readTag();
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_areaIdentification) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter 1 [areaIdentification] bad tag class, tag or not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        this.areaIdentification = new AreaIdentificationImpl();
        ((AreaIdentificationImpl) this.areaIdentification).decodeAll(ais);

        while (true) {
            if (ais.available() == 0)
                break;
            switch (ais.readTag()) {
                default:
                    ais.advanceElement();
                    break;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.areaType == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter[areaType [0] AreaType] is not defined");
        }

        if (this.areaIdentification == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter[areaIdentification [1] AreaIdentification] is not defined");
        }

        try {
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_areaType, this.areaType.getType());

            ((AreaIdentificationImpl) this.areaIdentification).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_areaIdentification);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((areaIdentification == null) ? 0 : areaIdentification.hashCode());
        result = prime * result + ((areaType == null) ? 0 : areaType.hashCode());
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
        AreaImpl other = (AreaImpl) obj;
        if (areaIdentification == null) {
            if (other.areaIdentification != null)
                return false;
        } else if (!areaIdentification.equals(other.areaIdentification))
            return false;

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.areaType != null) {
            sb.append("areaType=");
            sb.append(this.areaType);
        }
        if (this.areaIdentification != null) {
            sb.append(", areaIdentification=");
            sb.append(this.areaIdentification.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
