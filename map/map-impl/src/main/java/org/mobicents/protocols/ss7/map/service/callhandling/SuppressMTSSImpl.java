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

package org.mobicents.protocols.ss7.map.service.callhandling;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SuppressMTSS;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/*
 *
 * @author cristian veliscu
 *
 */
public class SuppressMTSSImpl implements SuppressMTSS, MAPAsnPrimitive {
    private static final int _INDEX_SuppressCUG = 0;
    private static final int _INDEX_SuppressCCBS = 1;

    private static final String _PrimitiveName = "SuppressMTSS";

    private BitSetStrictLength bitString = new BitSetStrictLength(2);

    /**
     *
     */
    public SuppressMTSSImpl() {
    }

    public SuppressMTSSImpl(boolean suppressCUG, boolean suppressCCBS) {
        if (suppressCUG)
            this.bitString.set(_INDEX_SuppressCUG);
        if (suppressCCBS)
            this.bitString.set(_INDEX_SuppressCCBS);
    }

    @Override
    public boolean getSuppressCUG() {
        return this.bitString.get(_INDEX_SuppressCUG);
    }

    @Override
    public boolean getSuppressCCBS() {
        return this.bitString.get(_INDEX_SuppressCCBS);
    }

    @Override
    public int getTag() throws MAPException {
        return Tag.STRING_BIT;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
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

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        if ((length < 2) || (length > 3))
            throw new MAPParsingComponentException("Error decoding " + _PrimitiveName + ": "
                    + "the SuppressMTSS field must contain from 2 or 3 octets. Contains: " + length,
                    MAPParsingComponentExceptionReason.MistypedParameter);

        this.bitString = ansIS.readBitStringData(length);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_BIT);
    }

    @Override
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

    @Override
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (getSuppressCUG())
            sb.append("SuppressCUG, ");
        if (getSuppressCCBS())
            sb.append("SuppressCCBS, ");

        sb.append("]");
        return sb.toString();
    }
}
