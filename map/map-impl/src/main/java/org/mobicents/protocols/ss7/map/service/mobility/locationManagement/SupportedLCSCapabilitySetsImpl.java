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
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author amit bhayani
 *
 */
public class SupportedLCSCapabilitySetsImpl implements SupportedLCSCapabilitySets, MAPAsnPrimitive {

    private static final int _INDEX_LCS_CAPABILITY_SET1 = 0;
    private static final int _INDEX_LCS_CAPABILITY_SET2 = 1;
    private static final int _INDEX_LCS_CAPABILITY_SET3 = 2;
    private static final int _INDEX_LCS_CAPABILITY_SET4 = 3;
    private static final int _INDEX_LCS_CAPABILITY_SET5 = 4;

    private BitSetStrictLength bitString = new BitSetStrictLength(5);

    /**
     *
     */
    public SupportedLCSCapabilitySetsImpl() {
        super();
    }

    public SupportedLCSCapabilitySetsImpl(boolean lcsCapabilitySetRelease98_99, boolean lcsCapabilitySetRelease4,
            boolean lcsCapabilitySetRelease5, boolean lcsCapabilitySetRelease6, boolean lcsCapabilitySetRelease7) {
        if (lcsCapabilitySetRelease98_99)
            this.bitString.set(_INDEX_LCS_CAPABILITY_SET1);
        if (lcsCapabilitySetRelease4)
            this.bitString.set(_INDEX_LCS_CAPABILITY_SET2);
        if (lcsCapabilitySetRelease5)
            this.bitString.set(_INDEX_LCS_CAPABILITY_SET3);
        if (lcsCapabilitySetRelease6)
            this.bitString.set(_INDEX_LCS_CAPABILITY_SET4);
        if (lcsCapabilitySetRelease7)
            this.bitString.set(_INDEX_LCS_CAPABILITY_SET5);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        return Tag.STRING_BIT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass()
     */
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive()
     */
    public boolean getIsPrimitive() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll(org.mobicents.protocols.asn.AsnInputStream)
     */
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MWStatus: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MWStatus: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData(org.mobicents.protocols.asn.AsnInputStream,
     * int)
     */
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MWStatus: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MWStatus: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        if (length < 1 || length > 3)
            throw new MAPParsingComponentException(
                    "Error decoding SupportedLCSCapabilitySets: the SupportedLCSCapabilitySets field must contain from 1 or 3 octets. Contains: "
                            + length, MAPParsingComponentExceptionReason.MistypedParameter);

        this.bitString = ansIS.readBitStringData(length);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_BIT);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream,
     * int, int)
     */
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, true, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding SupportedLCSCapabilitySets: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData(org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            asnOs.writeBitStringData(this.bitString);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding SupportedLCSCapabilitySets: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding SupportedLCSCapabilitySets: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SupportedLCSCapabilitySets#getLcsCapabilitySet1()
     */
    public boolean getCapabilitySetRelease98_99() {
        return this.bitString.get(_INDEX_LCS_CAPABILITY_SET1);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SupportedLCSCapabilitySets#getLcsCapabilitySet2()
     */
    public boolean getCapabilitySetRelease4() {
        return this.bitString.get(_INDEX_LCS_CAPABILITY_SET2);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SupportedLCSCapabilitySets#getLcsCapabilitySet3()
     */
    public boolean getCapabilitySetRelease5() {
        return this.bitString.get(_INDEX_LCS_CAPABILITY_SET3);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.SupportedLCSCapabilitySets#getLcsCapabilitySet4()
     */
    public boolean getCapabilitySetRelease6() {
        return this.bitString.get(_INDEX_LCS_CAPABILITY_SET4);
    }

    public boolean getCapabilitySetRelease7() {
        return this.bitString.get(_INDEX_LCS_CAPABILITY_SET5);
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
        SupportedLCSCapabilitySetsImpl other = (SupportedLCSCapabilitySetsImpl) obj;
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
        sb.append("SupportedLCSCapabilitySets [");

        if (getCapabilitySetRelease98_99())
            sb.append("CapabilitySetRelease98_99, ");
        if (getCapabilitySetRelease4())
            sb.append("CapabilitySetRelease4, ");
        if (getCapabilitySetRelease5())
            sb.append("CapabilitySetRelease5, ");
        if (getCapabilitySetRelease6())
            sb.append("CapabilitySetRelease6, ");
        if (getCapabilitySetRelease7())
            sb.append("CapabilitySetRelease7, ");

        sb.append("]");

        return sb.toString();
    }
}
