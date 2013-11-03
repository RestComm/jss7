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
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class RejectImpl implements Reject {

    protected Long correlationId;
    private RejectProblem rejectProblem;
    private boolean localOriginated = false;


    @Override
    public RejectProblem getProblem() {
        return rejectProblem;
    }

    @Override
    public void setProblem(RejectProblem p) {
        rejectProblem = p;
    }

    @Override
    public Long getCorrelationId() {
        return correlationId;
    }

    @Override
    public void setCorrelationId(Long i) {
        if (i != null && (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Correlation ID our of range: <-128,127>: " + i);
        }
        this.correlationId = i;
    }

    public ComponentType getType() {
        return ComponentType.Reject;
    }

    @Override
    public boolean isLocalOriginated() {
        return localOriginated;
    }

    @Override
    public void setLocalOriginated(boolean p) {
        localOriginated = p;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        this.correlationId = null;
        this.rejectProblem = null;
        this.localOriginated = false;

        try {
            AsnInputStream localAis = ais.readSequenceStream();

            // correlationId
            byte[] buf = TcapFactory.readComponentId(localAis, 0, 1);
            if (buf.length > 0)
                this.setCorrelationId((long) buf[0]);

            // rejectProblem
            int tag = localAis.readTag();
            if (tag != Reject._TAG_REJECT_PROBLEM || localAis.getTagClass() != Tag.CLASS_PRIVATE
                    || !localAis.isTagPrimitive()) {
                throw new ParseException(RejectProblem.generalIncorrectComponentPortion,
                        "RejectProblem in Reject has bad tag or tag class or is not primitive: tag=" + tag + ", tagClass=" + localAis.getTagClass());
            }
            long i1 = localAis.readInteger();
            this.rejectProblem = RejectProblem.getFromInt(i1);

            // Empty parameter
            // we do not parse an empty parameter because it is useless

        } catch (IOException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "IOException while decoding Reject: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "AsnException while decoding Reject: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (this.getProblem() == null)
            throw new EncodeException("Error encoding Reject: RejectProblem must not be null");

        try {
            // tag
            aos.writeTag(Tag.CLASS_PRIVATE, false, Reject._TAG_REJECT);
            int pos = aos.StartContentDefiniteLength();

            // correlationId
            byte[] buf;
            if (this.correlationId != null) {
                buf = new byte[1];
                buf[0] = (byte) (long) this.correlationId;
            } else {
                buf = new byte[0];
            }
            aos.writeOctetString(Tag.CLASS_PRIVATE, Component._TAG_INVOKE_ID, buf);

            // rejectProblem
            aos.writeInteger(Tag.CLASS_PRIVATE, Reject._TAG_REJECT_PROBLEM, this.rejectProblem.getType());

            // Empty parameter
            aos.writeTag(Tag.CLASS_PRIVATE, false, Parameter._TAG_SEQUENCE);
            aos.writeLength(0);

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding Reject: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding Reject: " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reject[");
        sb.append("localOriginated=");
        sb.append(this.localOriginated);
        sb.append(", ");
        if (this.getCorrelationId() != null) {
            sb.append("CorrelationId=");
            sb.append(this.getCorrelationId());
            sb.append(", ");
        }
        if (this.getProblem() != null) {
            sb.append("Problem=");
            sb.append(this.getProblem());
            sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }
}
