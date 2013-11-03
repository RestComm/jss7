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
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class ReturnResultImpl implements ReturnResult {

    // mandatory
    private Long invokeId;

    // This is sequence, both must be present
    // optional: this is sequence
    private OperationCode operationCode;
    // optional
    private Parameter parameter;

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Return#getInvokeId()
     */
    public Long getInvokeId() {

        return this.invokeId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Return#getOperationCode()
     */
    public OperationCode getOperationCode() {

        return this.operationCode;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Return#getParameter()
     */
    public Parameter getParameter() {
        return this.parameter;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Return#setInvokeId(java.lang .Long)
     */
    public void setInvokeId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.invokeId = i;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Return#setOperationCode(org
     * .mobicents.protocols.ss7.tcap.asn.comp.OperationCode[])
     */
    public void setOperationCode(OperationCode oc) {
        this.operationCode = oc;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Return#setParameter(org.mobicents .protocols.ss7.tcap.asn.comp.Parameter)
     */
    public void setParameter(Parameter p) {
        this.parameter = p;
    }

    public ComponentType getType() {

        return ComponentType.ReturnResult;
    }

    public String toString() {
        return "ReturnResult[invokeId=" + invokeId + ", operationCode=" + operationCode + ", parameter=" + parameter + "]";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        try {
            AsnInputStream localAis = ais.readSequenceStream();

            int tag = localAis.readTag();
            if (tag != _TAG_IID || localAis.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new ParseException(null, GeneralProblemType.MistypedComponent,
                        "Error while decoding ReturnLast: bad tag or tag class for InvokeID: tag=" + tag + ", tagClass = "
                                + localAis.getTagClass());
            }

            this.invokeId = localAis.readInteger();

            if (localAis.available() <= 0)
                return;

            tag = localAis.readTag();
            if (tag != Tag.SEQUENCE || localAis.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new ParseException(null, GeneralProblemType.MistypedComponent,
                        "Error while decoding ReturnLast: bad tag or tag class for sequence: tag=" + tag + ", tagClass = "
                                + localAis.getTagClass());
            }

            // sequence of OperationCode
            AsnInputStream sequenceStream = localAis.readSequenceStream();

            tag = sequenceStream.readTag();
            if (tag != OperationCode._TAG_GLOBAL && tag != OperationCode._TAG_LOCAL
                    || localAis.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new ParseException(null, GeneralProblemType.MistypedComponent,
                        "Error while decoding ReturnLast: bad tag or tag class for operationCode: tag=" + tag + ", tagClass = "
                                + localAis.getTagClass());
            }
            this.operationCode = TcapFactory.createOperationCode(tag, sequenceStream);

            tag = sequenceStream.readTag();
            this.parameter = TcapFactory.createParameter(tag, sequenceStream, true);

        } catch (IOException e) {
            throw new ParseException(null, GeneralProblemType.BadlyStructuredComponent,
                    "IOException while decoding ReturnResult: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(null, GeneralProblemType.BadlyStructuredComponent,
                    "AsnException while decoding ReturnResult: " + e.getMessage(), e);
        } catch (ParseException e) {
            e.setInvokeId(this.invokeId);
            throw e;
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols .asn.AsnOutputStream)
     */
    public void encode(AsnOutputStream aos) throws EncodeException {

        if (invokeId == null)
            throw new EncodeException("No Invoke ID set.");

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG);
            int pos = aos.StartContentDefiniteLength();

            aos.writeInteger(this.invokeId);

            if (this.operationCode != null && this.parameter != null) {
                aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
                int pos2 = aos.StartContentDefiniteLength();
                this.operationCode.encode(aos);
                this.parameter.encode(aos);
                aos.FinalizeContent(pos2);
            }

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding ReturnResult: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding ReturnResult: " + e.getMessage(), e);
        }
    }

}
