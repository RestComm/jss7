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
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;

/**
 *
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class ApplicationContextImpl implements ApplicationContext {

    // object identifier value
    private long[] oidVal;
    private long integerVal;
    private ACType acType;

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {
        oidVal = null;
        integerVal = 0;
        acType = null;

        try {
            if (!ais.isTagPrimitive() || ais.getTagClass() != Tag.CLASS_PRIVATE)
                throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion,
                        "Error decoding ApplicationContext: bad tagClass or not primitive, found tagClass=" + ais.getTagClass());

            switch (ais.getTag()) {
            case ApplicationContext._TAG_OBJECT_ID:
                this.oidVal = ais.readObjectIdentifier();
                acType = ACType.OId;
                break;

            case ApplicationContext._TAG_INTEGER:
                this.integerVal = ais.readInteger();
                acType = ACType.Integer;
                break;

            default:
                throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion,
                        "Error decoding ApplicationContext: bad tag, found tag=" + ais.getTag());
            }

        } catch (IOException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion,
                    "IOException while decoding ApplicationContextName: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion,
                    "AsnException while decoding ApplicationContextName: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        try {
            if (this.isInteger()) {
                aos.writeInteger(Tag.CLASS_PRIVATE, ApplicationContext._TAG_INTEGER, integerVal);
            } else if (this.isObjectID()) {
                aos.writeObjectIdentifier(Tag.CLASS_PRIVATE, ApplicationContext._TAG_OBJECT_ID, this.oidVal);
            } else {
                throw new EncodeException("Error while encoding ApplicationContextName: Neither OId nor Integer value is set");
            }

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding ApplicationContextName: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding ApplicationContextName: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isInteger() {
        if (acType == ACType.Integer)
            return true;
        else
            return false;
    }

    @Override
    public boolean isObjectID() {
        if (acType == ACType.OId)
            return true;
        else
            return false;
    }

    /**
     * @return the oid
     */
    public long[] getOid() {
        return oidVal;
    }

    /**
     * @param oid the oid to set
     */
    public void setOid(long[] oid) {
        if (oid != null) {
            this.oidVal = oid;
            acType = ACType.OId;
        } else {
            acType = null;
        }
    }

    @Override
    public long getInteger() {
        return this.integerVal;
    }

    @Override
    public void setInteger(long val) {
        this.integerVal = val;
        acType = ACType.Integer;
    }

    public String getStringValue() {
        StringBuilder sb = new StringBuilder();
        if (this.isInteger()) {
            sb.append(this.integerVal);
        } else if (this.isObjectID()) {
            sb.append(Arrays.toString(oidVal));
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ApplicationContext[");
        if (this.isInteger()) {
            sb.append("integer=");
            sb.append(this.integerVal);
        } else if (this.isObjectID()) {
            sb.append("oid=");
            sb.append(Arrays.toString(oidVal));
        }
        sb.append("]");
        return sb.toString();
    }

    private enum ACType {
        OId, Integer,
    }
}
