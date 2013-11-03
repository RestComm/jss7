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

package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;

/**
 * @author baranowb
 *
 */
public class AbortSourceImpl implements AbortSource {

    private AbortSourceType type;

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.AbortSource#getAbortSourceType()
     */
    public AbortSourceType getAbortSourceType() {
        return this.type;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.tcap.asn.AbortSource#setAbortSourceType(org.mobicents.protocols.ss7.tcap.asn.AbortSourceType)
     */
    public void setAbortSourceType(AbortSourceType t) {
        this.type = t;

    }

    public String toString() {
        return "AbortSource[type=" + type + "]";
    }

    public void decode(AsnInputStream ais) throws ParseException {

        try {
            long t = ais.readInteger();
            this.type = AbortSourceType.getFromInt(t);

        } catch (IOException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null, "IOException while decoding AbortSource: "
                    + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null, "AsnException while decoding AbortSource: "
                    + e.getMessage(), e);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (type == null)
            throw new EncodeException("Error encoding AbortSource: No type set");

        try {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG, type.getType());

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding AbortSource: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding AbortSource: " + e.getMessage(), e);
        }
    }

}
