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
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;

/**
 * @author baranowb
 *
 */
public class ApplicationContextNameImpl implements ApplicationContextName {

    // object identifier value
    private long[] oid;

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {
        try {
            AsnInputStream localAis = ais.readSequenceStream();
            int tag = localAis.readTag();
            if (tag != Tag.OBJECT_IDENTIFIER || localAis.getTagClass() != Tag.CLASS_UNIVERSAL)
                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                        "Error decoding ApplicationContextName: bad tag or tagClass, found tag=" + tag + ", tagClass="
                                + localAis.getTagClass());
            this.oid = localAis.readObjectIdentifier();

        } catch (IOException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null,
                    "IOException while decoding ApplicationContextName: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null,
                    "AsnException while decoding ApplicationContextName: " + e.getMessage(), e);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (this.oid == null)
            throw new EncodeException("Error while decoding ApplicationContextName: No OID value set");

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG);
            int pos = aos.StartContentDefiniteLength();

            aos.writeObjectIdentifier(this.oid);

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding ApplicationContextName: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("IOException while encoding ApplicationContextName: " + e.getMessage(), e);
        }

    }

    /**
     * @return the oid
     */
    public long[] getOid() {
        return oid;
    }

    /**
     * @param oid the oid to set
     */
    public void setOid(long[] oid) {
        this.oid = oid;
    }

    public String getStringValue() {
        return Arrays.toString(oid);
    }

    public String toString() {
        return "ApplicationContextName[oid=" + Arrays.toString(oid) + "]";
    }

}
