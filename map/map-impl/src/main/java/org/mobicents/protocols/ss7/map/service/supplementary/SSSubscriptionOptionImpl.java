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

package org.mobicents.protocols.ss7.map.service.supplementary;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.mobicents.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author daniel bichara
 *
 */
public class SSSubscriptionOptionImpl implements SSSubscriptionOption, MAPAsnPrimitive {

    public static final String _PrimitiveName = "SSSubscriptionOption";

    public static final int _TAG_cliRestrictionOption = 2;
    public static final int _TAG_overrideCategory = 1;

    private CliRestrictionOption cliRestrictionOption = null;
    private OverrideCategory overrideCategory = null;

    public SSSubscriptionOptionImpl() {

    }

    public SSSubscriptionOptionImpl(CliRestrictionOption cliRestrictionOption) {

        this.cliRestrictionOption = cliRestrictionOption;
    }

    public SSSubscriptionOptionImpl(OverrideCategory overrideCategory) {

        this.overrideCategory = overrideCategory;
    }

    public CliRestrictionOption getCliRestrictionOption() {
        return this.cliRestrictionOption;
    }

    public OverrideCategory getOverrideCategory() {
        return this.overrideCategory;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        if (cliRestrictionOption != null) {
            return _TAG_cliRestrictionOption;
        } else if (overrideCategory != null) {
            return _TAG_overrideCategory;
        } else {
            throw new MAPException("No of choices are supplied");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
     */
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll( org.mobicents.protocols.asn.AsnInputStream)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData (org.mobicents.protocols.asn.AsnInputStream,
     * int)
     */
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
        this.cliRestrictionOption = null;
        this.overrideCategory = null;

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": bad tag class or is not primitive: TagClass=" + ais.getTagClass(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        switch (ais.getTag()) {
            case _TAG_cliRestrictionOption:
                this.cliRestrictionOption = CliRestrictionOption.getInstance((int) ais.readIntegerData(length));
                break;
            case _TAG_overrideCategory:
                this.overrideCategory = OverrideCategory.getInstance((int) ais.readIntegerData(length));
                break;

            default:
                throw new MAPParsingComponentException("Error while " + _PrimitiveName + ": bad tag: " + ais.getTag(),
                        MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream,
     * int, int)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.cliRestrictionOption == null && this.overrideCategory == null)
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": missing cliRestrictionOption and overrideCategory.");

        if (this.cliRestrictionOption != null && this.overrideCategory != null)
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": both cliRestrictionOption and overrideCategory are defined.");

        try {

            if (this.cliRestrictionOption != null) {
                asnOs.writeIntegerData(this.cliRestrictionOption.getCode());
            } else {
                asnOs.writeIntegerData(this.overrideCategory.getCode());
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.cliRestrictionOption != null) {
            sb.append("cliRestrictionOption=");
            sb.append(this.cliRestrictionOption.toString());
            sb.append(", ");
        }

        if (this.overrideCategory != null) {
            sb.append("overrideCategory=");
            sb.append(this.overrideCategory.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
