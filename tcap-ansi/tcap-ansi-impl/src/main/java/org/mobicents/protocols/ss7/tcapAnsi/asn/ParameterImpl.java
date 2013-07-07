/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class ParameterImpl implements Parameter {

    private byte[] data;
    private boolean primitive = true;
    private int tag;
    private int tagClass;

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Parameter#getData()
     */
    public byte[] getData() {

        return data;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Parameter#isPrimitive()
     */
    public boolean isPrimitive() {

        return primitive;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Parameter#setData(byte[])
     */
    public void setData(byte[] b) {
        this.data = b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Parameter#setPrimitive(boolean)
     */
    public void setPrimitive(boolean b) {
        this.primitive = b;
    }

    /**
     * @return the tag
     */
    public int getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(int tag) {
        this.tag = tag;
    }

    /**
     * @return the tagClass
     */
    public int getTagClass() {
        return tagClass;
    }

    /**
     * @param tagClass the tagClass to set
     */
    public void setTagClass(int tagClass) {
        this.tagClass = tagClass;
    }

    private boolean singleParameterInAsn = false;

    public void setSingleParameterInAsn() {
        singleParameterInAsn = true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {
        try {
            primitive = ais.isTagPrimitive();
            tagClass = ais.getTagClass();
            data = ais.readSequence();

        } catch (IOException e) {
            throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, RejectProblem.generalBadlyStructuredCompPortion,
                    "IOException while decoding the parameter: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, RejectProblem.generalBadlyStructuredCompPortion,
                    "AsnException while decoding the parameter: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {
        if (data == null) {
            throw new EncodeException("Parameter data not set.");
        }

        try {
            aos.writeTag(tagClass, primitive, tag);
            aos.writeLength(data.length);
            aos.write(data);
        } catch (IOException e) {
            throw new EncodeException("IOException while encoding the parameter: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding the parameter: " + e.getMessage(), e);
        }
    }

    public String toString() {
        return "Parameter[data=" + Arrays.toString(data) + ", primitive=" + primitive + ", tag=" + tag + ", tagClass=" + tagClass + "]";
    }
}
