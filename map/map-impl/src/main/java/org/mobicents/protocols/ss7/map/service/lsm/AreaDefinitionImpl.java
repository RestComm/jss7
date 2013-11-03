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
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.Area;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class AreaDefinitionImpl extends SequenceBase implements AreaDefinition {

    private static final int _TAG_areaList = 0;

    private ArrayList<Area> areaList = null;

    /**
     *
     */
    public AreaDefinitionImpl() {
        super("AreaDefinition");
    }

    /**
     * @param areaList
     */
    public AreaDefinitionImpl(ArrayList<Area> areaList) {
        super("AreaDefinition");
        this.areaList = areaList;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition#getAreaList ()
     */
    public ArrayList<Area> getAreaList() {
        return this.areaList;
    }

    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.areaList = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_areaList:
                        if (ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter areaList is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        }

                        this.areaList = new ArrayList<Area>();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            int tag2 = ais2.readTag();
                            if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": bad Area tag or tagClass or is primitive ",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            AreaImpl el = new AreaImpl();
                            el.decodeAll(ais2);
                            this.areaList.add(el);
                        }
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (areaList == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament areaList is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (this.areaList.size() < 1 || this.areaList.size() > 10) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament areaList size must be from 1 to 10, found: " + this.areaList.size(),
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.areaList == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter[areaList [0] AreaList] is not defined");
        }
        if (this.areaList.size() < 1 || this.areaList.size() > 10) {
            throw new MAPException("Error while encoding " + _PrimitiveName + " areaList size must be from 1 to 10, found: "
                    + this.areaList.size());
        }

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_areaList);
            int pos = asnOs.StartContentDefiniteLength();
            for (Area ri : this.areaList) {
                if (ri != null) {
                    AreaImpl rii = (AreaImpl) ri;
                    rii.encodeAll(asnOs);
                }
            }
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (this.areaList != null) {
            for (int i = 0; i < areaList.size(); i++) {
                Area a1 = areaList.get(i);
                result = prime * result + ((a1 == null) ? 0 : a1.hashCode());
            }
        }
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
        AreaDefinitionImpl other = (AreaDefinitionImpl) obj;
        if (areaList == null) {
            if (other.areaList != null)
                return false;
        } else {
            if (areaList.size() != other.areaList.size())
                return false;
            for (int i = 0; i < areaList.size(); i++) {
                Area a1 = areaList.get(i);
                Area a2 = other.areaList.get(i);
                if (a1 == null) {
                    if (a2 != null)
                        return false;
                } else {
                    if (!a1.equals(a2))
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.areaList != null) {
            sb.append("areaList [");
            for (int i = 0; i < areaList.size(); i++) {
                Area a1 = areaList.get(i);
                sb.append("area=");
                sb.append(a1);
                sb.append(", ");
            }
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }
}
