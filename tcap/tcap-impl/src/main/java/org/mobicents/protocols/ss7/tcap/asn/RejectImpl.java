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

package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;

/**
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class RejectImpl implements Reject {

    // all are mandatory

    // this can actaully be null in this case.
    private Long invokeId;
    private boolean localOriginated = false;

    private Problem problem;

    public RejectImpl() {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Reject#getInvokeId()
     */
    public Long getInvokeId() {

        return this.invokeId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Reject#getProblem()
     */
    public Problem getProblem() {

        return this.problem;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Reject#setInvokeId(java.lang .Long)
     */
    public void setInvokeId(Long i) {
        if (i != null && (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.invokeId = i;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Reject#setProblem(org.mobicents .protocols.ss7.tcap.asn.comp.Problem)
     */
    public void setProblem(Problem p) {

        this.problem = p;

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

    public String toString() {
        return "Reject[invokeId=" + invokeId + (this.isLocalOriginated() ? ", localOriginated" : ", remoteOriginated")
                + ", problem=" + problem + "]";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        try {
            this.setLocalOriginated(false);

            AsnInputStream localAis = ais.readSequenceStream();

            int tag = localAis.readTag();
            if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new ParseException(null, GeneralProblemType.MistypedComponent,
                        "Error while decoding Reject: bad tag class for InvokeID or NULL: tagClass = " + localAis.getTagClass());
            }
            switch (tag) {
                case _TAG_IID:
                    this.invokeId = localAis.readInteger();
                    break;
                case Tag.NULL:
                    localAis.readNull();
                    break;
            }

            tag = localAis.readTag();
            if (localAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new ParseException(null, GeneralProblemType.MistypedComponent,
                        "Error while decoding Reject: bad tag class for a problem: tagClass = " + localAis.getTagClass());
            }
            ProblemType pt = ProblemType.getFromInt(tag);
            if (pt == null) {
                throw new ParseException(null, GeneralProblemType.MistypedComponent,
                        "Error while decoding Reject: ProblemType not found");
            }
            this.problem = TcapFactory.createProblem(pt, localAis);

        } catch (IOException e) {
            throw new ParseException(null, GeneralProblemType.BadlyStructuredComponent, "IOException while decoding Reject: "
                    + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(null, GeneralProblemType.BadlyStructuredComponent, "AsnException while decoding Reject: "
                    + e.getMessage(), e);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (this.problem == null) {
            throw new EncodeException("Problem not set!");
        }
        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG);
            int pos = aos.StartContentDefiniteLength();

            if (this.invokeId == null)
                aos.writeNull();
            else
                aos.writeInteger(this.invokeId);
            this.problem.encode(aos);

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding Reject: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding Reject: " + e.getMessage(), e);
        }

    }

}
