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
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
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
    protected Parameter[] parameters;
    private boolean parameterIsSETStyle;

    @Override
    public Parameter[] getParameters() {
        return this.parameters;
    }

    @Override
    public void setParameters(Parameter[] p) {
        this.parameters = p;
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

    @Override
    public boolean getParameterIsSETStyle() {
        return parameterIsSETStyle;
    }

    @Override
    public void setParameterIsSETStyle(boolean val) {
        parameterIsSETStyle = val;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        this.correlationId = null;
        this.parameters = null;
        this.parameterIsSETStyle = false;

        try {
            AsnInputStream localAis = ais.readSequenceStream();

            // correlationId
            int tag = localAis.readTag();
            if (tag != Component._TAG_INVOKE_ID || localAis.getTagClass() != Tag.CLASS_PRIVATE || !localAis.isTagPrimitive()) {
                throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, RejectProblem.generalIncorrectComponentPortion,
                        "InvokeID in ReturnResult has bad tag or tag class or is not primitive: tag=" + tag + ", tagClass=" + localAis.getTagClass());
            }
            byte[] buf = localAis.readOctetString();
            if (buf.length > 1)
                throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, RejectProblem.generalBadlyStructuredCompPortion,
                        "InvokeID in ReturnResult must be 0 or 1 bytes length, found bytes=" + buf.length);
            if (buf.length >= 1)
                this.setCorrelationId((long) buf[0]);

            // Parameters
            tag = localAis.readTag();
            if ((tag != Parameter._TAG_SEQUENCE && tag != Parameter._TAG_SET) || localAis.getTagClass() != Tag.CLASS_PRIVATE || localAis.isTagPrimitive()) {
                throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, RejectProblem.generalIncorrectComponentPortion,
                        "Parameters in ReturnResult has bad tag or tag class or is primitive: tag=" + tag + ", tagClass=" + localAis.getTagClass());
            }
            if (tag == Parameter._TAG_SEQUENCE)
                parameterIsSETStyle = false;
            else
                parameterIsSETStyle = true;

            AsnInputStream ais2 = localAis.readSequenceStream();
            ArrayList<Parameter> pars = new ArrayList<Parameter>();
            while (true) {
                if (ais2.available() == 0)
                    break;

                ais2.readTag();
                Parameter par = TcapFactory.createParameter(ais2);
                pars.add(par);
            }
            Parameter[] res = new Parameter[pars.size()];
            pars.toArray(res);
            this.setParameters(res);

        } catch (IOException e) {
            throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, RejectProblem.generalBadlyStructuredCompPortion,
                    "IOException while decoding ReturnResult: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, RejectProblem.generalBadlyStructuredCompPortion,
                    "AsnException while decoding ReturnResult: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (this.getParameterIsSETStyle() && (this.parameters == null || this.parameters.length == 0))
            throw new EncodeException("Error encoding ReturnResult: for Paramaters SET we have to have at least one parameter");

        try {
            // tag
            if (this.getType() == ComponentType.ReturnResultNotLast)
                aos.writeTag(Tag.CLASS_PRIVATE, false, ReturnResultNotLast._TAG_RETURN_RESULT_NOT_LAST);
            else
                aos.writeTag(Tag.CLASS_PRIVATE, false, ReturnResultLast._TAG_RETURN_RESULT_LAST);
            int pos = aos.StartContentDefiniteLength();

            // correlationId
            byte[] buf;
            if (this.correlationId != null) {
                buf = new byte[2];
                buf[0] = (byte) (long) this.correlationId;
            } else {
                buf = new byte[0];
            }
            aos.writeOctetString(Tag.CLASS_PRIVATE, Component._TAG_INVOKE_ID, buf);

            // parameters
            if (this.getParameterIsSETStyle()) {
                aos.writeTag(Tag.CLASS_PRIVATE, false, Parameter._TAG_SET);
            } else {
                aos.writeTag(Tag.CLASS_PRIVATE, false, Parameter._TAG_SEQUENCE);
            }
            int pos2 = aos.StartContentDefiniteLength();
            if (this.parameters != null && this.parameters.length > 0) {
                for (Parameter par : this.parameters) {
                    par.encode(aos);
                }
            }
            aos.FinalizeContent(pos2);

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
        if (this.getParameters() != null && this.getParameters().length > 0) {
            sb.append("Parameters=[");
            for (Parameter par : this.getParameters()) {
                sb.append("Parameter=[");
                sb.append(par);
                sb.append("], ");
            }
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }
}
