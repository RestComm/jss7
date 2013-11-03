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
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class SupportedRATTypesImpl implements SupportedRATTypes, MAPAsnPrimitive {

    private static final int _INDEX_utran = 0;
    private static final int _INDEX_geran = 1;
    private static final int _INDEX_gan = 2;
    private static final int _INDEX_i_hspa_evolution = 3;
    private static final int _INDEX_e_utran = 4;

    public static final String _PrimitiveName = "SupportedRATTypes";

    private BitSetStrictLength bitString = new BitSetStrictLength(5);

    public SupportedRATTypesImpl() {
    }

    public SupportedRATTypesImpl(boolean utran, boolean geran, boolean gan, boolean i_hspa_evolution, boolean e_utran) {
        if (utran)
            this.bitString.set(_INDEX_utran);
        if (geran)
            this.bitString.set(_INDEX_geran);
        if (gan)
            this.bitString.set(_INDEX_gan);
        if (i_hspa_evolution)
            this.bitString.set(_INDEX_i_hspa_evolution);
        if (e_utran)
            this.bitString.set(_INDEX_e_utran);
    }

    public int getTag() throws MAPException {
        return Tag.STRING_BIT;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return true;
    }

    public boolean getUtran() {
        return this.bitString.get(_INDEX_utran);
    }

    public boolean getGeran() {
        return this.bitString.get(_INDEX_geran);
    }

    public boolean getGan() {
        return this.bitString.get(_INDEX_gan);
    }

    public boolean getIHspaEvolution() {
        return this.bitString.get(_INDEX_i_hspa_evolution);
    }

    public boolean getEUtran() {
        return this.bitString.get(_INDEX_e_utran);
    }

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

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        if (length < 1 || length > 2)
            throw new MAPParsingComponentException("Error decoding " + _PrimitiveName
                    + ": the field must contain from 1 or 2 octets. Contains: " + length,
                    MAPParsingComponentExceptionReason.MistypedParameter);

        this.bitString = ansIS.readBitStringData(length);
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, true, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            asnOs.writeBitStringData(this.bitString);
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
        result = prime * result + ((bitString == null) ? 0 : bitString.hashCode());
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
        SupportedRATTypesImpl other = (SupportedRATTypesImpl) obj;
        if (bitString == null) {
            if (other.bitString != null)
                return false;
        } else if (!bitString.equals(other.bitString))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SupportedRATTypes [");

        if (getUtran())
            sb.append("utran, ");
        if (getGeran())
            sb.append("geran, ");
        if (getGan())
            sb.append("gan, ");
        if (getIHspaEvolution())
            sb.append("i_hspa_evolution, ");
        if (getEUtran())
            sb.append("e_utran, ");

        sb.append("]");

        return sb.toString();
    }
}
