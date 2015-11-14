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

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCodeType;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;

/**
 * @author baranowb
 *
 */
public class OperationCodeImpl implements OperationCode {

    private Long nationalOperationCode;
    private Long privateOperationCode;
    private OperationCodeType type;

    public OperationCodeType getOperationType() {
        return type;
    }

    @Override
    public void setNationalOperationCode(Long nationalOperationCode) {
        this.nationalOperationCode = nationalOperationCode;
        this.privateOperationCode = null;
        this.type = OperationCodeType.National;
    }

    @Override
    public void setPrivateOperationCode(Long privateOperationCode) {
        this.nationalOperationCode = null;
        this.privateOperationCode = privateOperationCode;
        this.type = OperationCodeType.Private;
    }

    @Override
    public Long getNationalOperationCode() {
        return nationalOperationCode;
    }

    @Override
    public Long getPrivateOperationCode() {
        return privateOperationCode;
    }

    public void setOperationType(OperationCodeType t) {
        this.type = t;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        try {
            long val = ais.readInteger();
            if (ais.getTag() == OperationCode._TAG_NATIONAL) {
                this.setNationalOperationCode(val);
            } else {
                this.setPrivateOperationCode(val);
            }
        } catch (IOException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "IOException while decoding OperationCode: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "AsnException while decoding OperationCode: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (this.nationalOperationCode == null && this.privateOperationCode == null)
            throw new EncodeException("Operation code: No Operation code set!");

        try {
            if (this.type == OperationCodeType.National && this.nationalOperationCode != null) {
                aos.writeInteger(Tag.CLASS_PRIVATE, OperationCode._TAG_NATIONAL, this.nationalOperationCode);
            } else if (this.type == OperationCodeType.Private && this.privateOperationCode != null) {
                aos.writeInteger(Tag.CLASS_PRIVATE, OperationCode._TAG_PRIVATE, this.privateOperationCode);
            } else {
                throw new EncodeException("Operation code: No Operation code set!");
            }

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding OperationCode: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding OperationCode: " + e.getMessage(), e);
        }
    }

    public String getStringValue() {
        StringBuilder sb = new StringBuilder();
        if (this.getNationalOperationCode() != null) {
            sb.append("NOC=");
            sb.append(this.getNationalOperationCode());
        } else if (this.getPrivateOperationCode() != null) {
            sb.append("POC=");
            sb.append(this.getPrivateOperationCode());
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OperationCode[");
        if (this.getNationalOperationCode() != null) {
            sb.append("NationalOperationCode=");
            sb.append(this.getNationalOperationCode());
        } else if (this.getPrivateOperationCode() != null) {
            sb.append("PrivateOperationCode=");
            sb.append(this.getPrivateOperationCode());
        }
        sb.append("]");
        return sb.toString();
    }
}
