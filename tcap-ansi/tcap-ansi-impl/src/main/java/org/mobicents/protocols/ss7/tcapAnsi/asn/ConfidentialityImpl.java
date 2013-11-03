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

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.Confidentiality;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;

/**
*
* @author sergey vetyutnev
*
*/
public class ConfidentialityImpl implements Confidentiality {

    private Long integerConfidentialityId;
    private long[] objectConfidentialityId;

    @Override
    public Long getIntegerConfidentialityId() {
        return integerConfidentialityId;
    }

    @Override
    public void setIntegerConfidentialityId(Long val) {
        integerConfidentialityId = val;
        objectConfidentialityId = null;
    }

    @Override
    public long[] getObjectConfidentialityId() {
        return objectConfidentialityId;
    }

    @Override
    public void setObjectConfidentialityId(long[] val) {
        integerConfidentialityId = null;
        objectConfidentialityId = val;
    }

    @Override
    public void decode(AsnInputStream aisA) throws ParseException {
        this.integerConfidentialityId = null;
        this.objectConfidentialityId = null;

        try {
            AsnInputStream ais = aisA.readSequenceStream();

            int tag = ais.readTag();
            if (!ais.isTagPrimitive() || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
                throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion,
                        "Error decoding Confidentiality: bad tagClass or not primitive, found tagClass=" + ais.getTagClass());

            switch (tag) {
            case Confidentiality._TAG_OBJECT_CONFIDENTIALITY_ID:
                this.objectConfidentialityId = ais.readObjectIdentifier();
                break;

            case Confidentiality._TAG_INTEGER_CONFIDENTIALITY_ID:
                this.integerConfidentialityId = ais.readInteger();
                break;

            default:
                throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion,
                        "Error decoding Confidentiality: bad tag, found tag=" + ais.getTag());
            }

        } catch (IOException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion,
                    "IOException while decoding Confidentiality: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion,
                    "AsnException while decoding Confidentiality: " + e.getMessage(), e);
        }
    }

    @Override
    public void encode(AsnOutputStream aos) throws EncodeException {

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, Confidentiality._TAG_CONFIDENTIALITY);
            int pos = aos.StartContentDefiniteLength();

            if (this.integerConfidentialityId != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, Confidentiality._TAG_INTEGER_CONFIDENTIALITY_ID, integerConfidentialityId);
            } else if (this.objectConfidentialityId != null) {
                aos.writeObjectIdentifier(Tag.CLASS_CONTEXT_SPECIFIC, Confidentiality._TAG_OBJECT_CONFIDENTIALITY_ID, this.objectConfidentialityId);
            } else {
                throw new EncodeException("Error while encoding Confidentiality: Neither integerConfidentialityId nor objectConfidentialityId value is set");
            }

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding Confidentiality: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding Confidentiality: " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Confidentiality[");
        if (this.integerConfidentialityId != null) {
            sb.append("integerConfidentialityId=");
            sb.append(this.integerConfidentialityId);
        } else if (this.objectConfidentialityId != null) {
            sb.append("objectConfidentialityId=");
            sb.append(Arrays.toString(objectConfidentialityId));
        }
        sb.append("]");
        return sb.toString();
    }

}
