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
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Return;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultNotLast;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public abstract class ReturnImpl implements Return {

    protected Long correlationId;
    protected Parameter parameter;
    private OperationCode operationCode;

    @Override
    public OperationCode getOperationCode() {
        return this.operationCode;
    }

    @Override
    public void setOperationCode(OperationCode i) {
        this.operationCode = i;

    }

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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        this.correlationId = null;
        this.parameter = null;

        try {
            AsnInputStream localAis = ais.readSequenceStream();

            // correlationId
            byte[] buf = TcapFactory.readComponentId(localAis, 1, 1);
            this.setCorrelationId((long) buf[0]);

            // Parameter
            this.parameter = TcapFactory.readParameter(localAis);

        } catch (IOException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "IOException while decoding ReturnResult: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "AsnException while decoding ReturnResult: " + e.getMessage(), e);
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

        if (this.correlationId == null)
            throw new EncodeException("Error encoding ReturnResult: correlationId is mandatory but is not set");

        try {
            // tag
            if (this.getType() == ComponentType.ReturnResultNotLast)
                aos.writeTag(Tag.CLASS_PRIVATE, false, ReturnResultNotLast._TAG_RETURN_RESULT_NOT_LAST);
            else
                aos.writeTag(Tag.CLASS_PRIVATE, false, ReturnResultLast._TAG_RETURN_RESULT_LAST);
            int pos = aos.StartContentDefiniteLength();

            // correlationId
            byte[] buf = new byte[1];
            buf[0] = (byte) (long) this.correlationId;
            aos.writeOctetString(Tag.CLASS_PRIVATE, Component._TAG_INVOKE_ID, buf);

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.getType() == ComponentType.ReturnResultNotLast)
            sb.append("ReturnResultNotLast[");
        else
            sb.append("ReturnResultLast[");
        if (this.getCorrelationId() != null) {
            sb.append("CorrelationId=");
            sb.append(this.getCorrelationId());
            sb.append(", ");
        }
        if (this.getOperationCode() != null) {
            sb.append("OperationCode=");
            sb.append(this.getOperationCode());
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
