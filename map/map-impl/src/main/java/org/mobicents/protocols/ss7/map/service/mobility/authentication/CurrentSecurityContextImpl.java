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

package org.mobicents.protocols.ss7.map.service.mobility.authentication;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContext;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.GSMSecurityContextData;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.UMTSSecurityContextData;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class CurrentSecurityContextImpl implements CurrentSecurityContext, MAPAsnPrimitive {

    public static final int _TAG_gsmSecurityContextData = 0;
    public static final int _TAG_umtsSecurityContextData = 1;

    public static final String _PrimitiveName = "CurrentSecurityContext";

    private GSMSecurityContextData gsmSecurityContextData;
    private UMTSSecurityContextData umtsSecurityContextData;

    public CurrentSecurityContextImpl() {
        super();
        this.gsmSecurityContextData = null;
        this.umtsSecurityContextData = null;
    }

    public CurrentSecurityContextImpl(GSMSecurityContextData gsmSecurityContextData) {
        super();
        this.gsmSecurityContextData = gsmSecurityContextData;
    }

    public CurrentSecurityContextImpl(UMTSSecurityContextData umtsSecurityContextData) {
        super();
        this.umtsSecurityContextData = umtsSecurityContextData;
    }

    @Override
    public GSMSecurityContextData getGSMSecurityContextData() {
        return this.gsmSecurityContextData;
    }

    @Override
    public UMTSSecurityContextData getUMTSSecurityContextData() {
        return this.umtsSecurityContextData;
    }

    @Override
    public int getTag() throws MAPException {
        if (this.gsmSecurityContextData != null)
            return _TAG_gsmSecurityContextData;
        else
            return _TAG_umtsSecurityContextData;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
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

        this.gsmSecurityContextData = null;
        this.umtsSecurityContextData = null;

        int tag = ais.getTag();

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive())
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Primitive has bad tag class or is primitive", MAPParsingComponentExceptionReason.MistypedParameter);

        switch (tag) {
            case _TAG_gsmSecurityContextData:
                this.gsmSecurityContextData = new GSMSecurityContextDataImpl();
                ((GSMSecurityContextDataImpl) this.gsmSecurityContextData).decodeData(ais, length);
                break;
            case _TAG_umtsSecurityContextData:
                this.umtsSecurityContextData = new UMTSSecurityContextDataImpl();
                ((UMTSSecurityContextDataImpl) this.umtsSecurityContextData).decodeData(ais, length);
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
        if (this.gsmSecurityContextData == null && this.umtsSecurityContextData == null || this.gsmSecurityContextData != null
                && this.umtsSecurityContextData != null) {
            throw new MAPException("Error while decoding " + _PrimitiveName + ": One and only one choice must be selected");
        }

        if (this.gsmSecurityContextData != null) {
            ((GSMSecurityContextDataImpl) this.gsmSecurityContextData).encodeData(asnOs);
        } else {
            ((UMTSSecurityContextDataImpl) this.umtsSecurityContextData).encodeData(asnOs);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.gsmSecurityContextData != null) {
            sb.append(this.gsmSecurityContextData.toString());
            sb.append(", ");
        }

        if (this.umtsSecurityContextData != null) {
            sb.append(this.umtsSecurityContextData.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
}
