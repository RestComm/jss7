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
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnError;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class ReturnErrorImpl implements ReturnError {

    protected Long correlationId;
    private ErrorCode errorCode;
    protected Parameter parameter;


    @Override
    public Parameter getParameter() {
        return this.parameter;
    }

    @Override
    public void setParameter(Parameter p) {
        this.parameter = p;
    }

    @Override
    public Long getCorrelationId() {
        return correlationId;
    }

    @Override
    public void setCorrelationId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.correlationId = i;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(ErrorCode ec) {
        this.errorCode = ec;
    }

    public ComponentType getType() {
        return ComponentType.ReturnError;
    }


    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        this.correlationId = null;
        this.errorCode = null;
        this.parameter = null;

        try {
            AsnInputStream localAis = ais.readSequenceStream();

            // correlationId
            byte[] buf = TcapFactory.readComponentId(localAis, 1, 1);
            this.setCorrelationId((long) buf[0]);

            // errorCode
            int tag = localAis.readTag();
            if ((tag != ErrorCode._TAG_NATIONAL && tag != ErrorCode._TAG_PRIVATE) || localAis.getTagClass() != Tag.CLASS_PRIVATE
                    || !localAis.isTagPrimitive()) {
                throw new ParseException(RejectProblem.generalIncorrectComponentPortion,
                        "ErrorCode in ReturnError has bad tag or tag class or is not primitive: tag=" + tag + ", tagClass=" + localAis.getTagClass());
            }
            this.errorCode = TcapFactory.createErrorCode(localAis);

            // Parameter
            this.parameter = TcapFactory.readParameter(localAis);

        } catch (IOException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "IOException while decoding ReturnError: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "AsnException while decoding ReturnError: " + e.getMessage(), e);
        } catch (ParseException e) {
            e.setInvokeId(this.correlationId);
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (this.errorCode == null)
            throw new EncodeException("Error encoding ReturnError: ErrorCode must not be null");
        if (this.correlationId == null)
            throw new EncodeException("Error encoding ReturnError: correlationId is mandatory but is not set");

        try {
            // tag
            aos.writeTag(Tag.CLASS_PRIVATE, false, ReturnError._TAG_RETURN_ERROR);
            int pos = aos.StartContentDefiniteLength();

            // correlationId
            byte[] buf = new byte[1];
            buf[0] = (byte) (long) this.correlationId;
            aos.writeOctetString(Tag.CLASS_PRIVATE, Component._TAG_INVOKE_ID, buf);

            // error code
            this.errorCode.encode(aos);

            // parameters
            if (this.parameter != null)
                this.parameter.encode(aos);
            else
                ParameterImpl.encodeEmptyParameter(aos);

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding ReturnResult: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding ReturnResult: " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReturnError[");
        if (this.getCorrelationId() != null) {
            sb.append("CorrelationId=");
            sb.append(this.getCorrelationId());
            sb.append(", ");
        }
        if (this.getErrorCode() != null) {
            sb.append("ErrorCode=");
            sb.append(this.getErrorCode());
            sb.append(", ");
        }
        if (this.getParameter() != null) {
            sb.append("Parameter=[");
            sb.append(this.getParameter());
            sb.append("], ");
        }
        sb.append("]");

        return sb.toString();
    }
}
