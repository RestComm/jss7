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
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class AreaEventInfoImpl extends SequenceBase implements AreaEventInfo {

    private static final int _TAG_areaDefinition = 0;
    private static final int _TAG_OCCURRENCE_INFO = 1;
    private static final int _TAG_INTERVAL_TIME = 2;

    private AreaDefinition areaDefinition;
    private OccurrenceInfo occurrenceInfo;
    private Integer intervalTime;

    /**
     *
     */
    public AreaEventInfoImpl() {
        super("AreaEventInfo");
    }

    /**
     * @param areaDefinition
     * @param occurrenceInfo
     * @param intervalTime
     */
    public AreaEventInfoImpl(AreaDefinition areaDefinition, OccurrenceInfo occurrenceInfo, Integer intervalTime) {
        super("AreaEventInfo");
        this.areaDefinition = areaDefinition;
        this.occurrenceInfo = occurrenceInfo;
        this.intervalTime = intervalTime;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo# getAreaDefinition()
     */
    public AreaDefinition getAreaDefinition() {
        return this.areaDefinition;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo# getOccurrenceInfo()
     */
    public OccurrenceInfo getOccurrenceInfo() {
        return this.occurrenceInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo#getIntervalTime ()
     */
    public Integer getIntervalTime() {
        return this.intervalTime;
    }

    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.areaDefinition = null;
        this.occurrenceInfo = null;
        this.intervalTime = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int tag = ais.readTag();

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive() || tag != _TAG_areaDefinition) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter 0 [areaDefinition [0] AreaDefinition] bad tag class, tag or not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        this.areaDefinition = new AreaDefinitionImpl();
        ((AreaDefinitionImpl) this.areaDefinition).decodeAll(ais);

        while (true) {
            if (ais.available() == 0)
                break;

            tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_OCCURRENCE_INFO:
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter 1 [occurrenceInfo [1] OccurrenceInfo] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }

                        int i1 = (int) ais.readInteger();
                        this.occurrenceInfo = OccurrenceInfo.getOccurrenceInfo(i1);
                        break;
                    case _TAG_INTERVAL_TIME:
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter 2 [intervalTime [2] IntervalTime] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.intervalTime = (int) ais.readInteger();
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

    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData(org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.areaDefinition == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter[areaDefinition [0] AreaDefinition] is not defined");
        }

        ((AreaDefinitionImpl) this.areaDefinition).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_areaDefinition);

        try {
            if (this.occurrenceInfo != null) {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_OCCURRENCE_INFO, this.occurrenceInfo.getInfo());
            }

            if (this.intervalTime != null) {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_INTERVAL_TIME, this.intervalTime);
            }

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
        result = prime * result + ((areaDefinition == null) ? 0 : areaDefinition.hashCode());
        result = prime * result + ((occurrenceInfo == null) ? 0 : occurrenceInfo.getInfo());
        result = prime * result + ((intervalTime == null) ? 0 : (int) intervalTime);
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
        AreaEventInfoImpl other = (AreaEventInfoImpl) obj;

        if (this.areaDefinition == null) {
            if (other.areaDefinition != null)
                return false;
        } else {
            if (!this.areaDefinition.equals(other.areaDefinition))
                return false;
        }
        if (this.occurrenceInfo == null) {
            if (other.occurrenceInfo != null)
                return false;
        } else {
            if (this.occurrenceInfo != other.occurrenceInfo)
                return false;
        }
        if (this.intervalTime == null) {
            if (other.intervalTime != null)
                return false;
        } else {
            if ((int) this.intervalTime != (int) other.intervalTime)
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.areaDefinition != null) {
            sb.append("areaDefinition=");
            sb.append(this.areaDefinition);
        }
        if (this.occurrenceInfo != null) {
            sb.append(", occurrenceInfo=");
            sb.append(this.occurrenceInfo.toString());
        }
        if (this.intervalTime != null) {
            sb.append(", intervalTime=");
            sb.append(this.intervalTime.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
