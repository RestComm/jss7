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
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCodeType;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;

/**
 * @author baranowb
 * @author sergey netyutnev
 *
 */
public class ErrorCodeImpl implements ErrorCode {
    private ErrorCodeType type;
    private Long nationalErrorCode;
    private Long privateErrorCode;

    public void setErrorCodeType(ErrorCodeType type) {
        this.type = type;
    }


    @Override
    public void setNationalErrorCode(Long nationalErrorCode) {
        this.nationalErrorCode = nationalErrorCode;
        this.privateErrorCode = null;
        this.type = ErrorCodeType.National;
    }

    @Override
    public void setPrivateErrorCode(Long privateErrorCode) {
        this.nationalErrorCode = null;
        this.privateErrorCode = privateErrorCode;
        this.type = ErrorCodeType.Private;
    }

    @Override
    public Long getNationalErrorCode() {
        return nationalErrorCode;
    }

    @Override
    public Long getPrivateErrorCode() {
        return privateErrorCode;
    }

    @Override
    public ErrorCodeType getErrorType() {
        return type;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        try {
            long val = ais.readInteger();
            if (ais.getTag() == ErrorCode._TAG_NATIONAL) {
                this.setNationalErrorCode(val);
            } else {
                this.setPrivateErrorCode(val);
            }
        } catch (IOException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "IOException while decoding ErrorCode: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "AsnException while decoding ErrorCode: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (this.nationalErrorCode == null && this.privateErrorCode == null)
            throw new EncodeException("Error code: No error code set!");

        try {
            if (this.type == ErrorCodeType.National && this.nationalErrorCode != null) {
                aos.writeInteger(Tag.CLASS_PRIVATE, ErrorCode._TAG_NATIONAL, this.nationalErrorCode);
            } else if (this.type == ErrorCodeType.Private && this.privateErrorCode != null) {
                aos.writeInteger(Tag.CLASS_PRIVATE, ErrorCode._TAG_PRIVATE, this.privateErrorCode);
            } else {
                throw new EncodeException("Error code: No error code set!");
            }

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding ErrorCode: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding ErrorCode: " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ErrorCode[");
        if (this.getNationalErrorCode() != null) {
            sb.append("NationalErrorCode=");
            sb.append(this.getNationalErrorCode());
        } else if (this.getPrivateErrorCode() != null) {
            sb.append("PrivateErrorCode=");
            sb.append(this.getPrivateErrorCode());
        }
        sb.append("]");
        return sb.toString();
    }

}
