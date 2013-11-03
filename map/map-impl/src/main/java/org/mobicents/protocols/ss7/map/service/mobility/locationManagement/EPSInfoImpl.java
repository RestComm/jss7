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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ISRInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PDNGWUpdate;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class EPSInfoImpl implements EPSInfo, MAPAsnPrimitive {

    public static final int _TAG_pndGwUpdate = 0;
    public static final int _TAG_isrInformation = 1;

    public static final String _PrimitiveName = "EPSInfo";

    private PDNGWUpdate pndGwUpdate;
    private ISRInformation isrInformation;

    public EPSInfoImpl() {
        super();
    }

    public EPSInfoImpl(PDNGWUpdate pndGwUpdate) {
        super();
        this.pndGwUpdate = pndGwUpdate;
        this.isrInformation = null;
    }

    public EPSInfoImpl(ISRInformation isrInformation) {
        super();
        this.pndGwUpdate = null;
        this.isrInformation = isrInformation;
    }

    @Override
    public PDNGWUpdate getPndGwUpdate() {
        return this.pndGwUpdate;
    }

    @Override
    public ISRInformation getIsrInformation() {
        return this.isrInformation;
    }

    @Override
    public int getTag() throws MAPException {
        if (this.pndGwUpdate != null)
            return _TAG_pndGwUpdate;
        else
            return _TAG_isrInformation;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        if (this.pndGwUpdate != null)
            return false;
        else
            return true;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.pndGwUpdate = null;
        this.isrInformation = null;

        int tag = ais.getTag();

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Primitive has bad tag class",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        switch (tag) {
            case _TAG_pndGwUpdate:
                if (ais.isTagPrimitive())
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ": Parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                this.pndGwUpdate = new PDNGWUpdateImpl();
                ((PDNGWUpdateImpl) this.pndGwUpdate).decodeData(ais, length);
                break;
            case _TAG_isrInformation:
                if (!ais.isTagPrimitive())
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ": Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                this.isrInformation = new ISRInformationImpl();
                ((ISRInformationImpl) this.isrInformation).decodeData(ais, length);
                break;

            default:
                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
                        MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.pndGwUpdate == null && this.isrInformation == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": all choices must not be null");
        if (this.pndGwUpdate != null && this.isrInformation != null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": all choices must not be not null");

        if (this.pndGwUpdate != null) {
            ((PDNGWUpdateImpl) this.pndGwUpdate).encodeData(asnOs);
        } else {
            ((ISRInformationImpl) this.isrInformation).encodeData(asnOs);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.pndGwUpdate != null) {
            sb.append("pndGwUpdate=");
            sb.append(this.pndGwUpdate.toString());
        }

        if (this.isrInformation != null) {
            sb.append("isrInformation=");
            sb.append(this.isrInformation.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
