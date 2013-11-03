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

    public String getStringValue() {
        StringBuilder sb = new StringBuilder();
        if (this.getNationalErrorCode() != null) {
            sb.append("NEC=");
            sb.append(this.getNationalErrorCode());
        } else if (this.getPrivateErrorCode() != null) {
            sb.append("PEC=");
            sb.append(this.getPrivateErrorCode());
        }
        return sb.toString();
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
