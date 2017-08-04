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

package org.mobicents.protocols.ss7.tcap.asn;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

import java.io.IOException;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class InvokeImpl implements Invoke {
    private transient Dialog dialog;
    // local to stack
    private InvokeClass invokeClass = InvokeClass.Class1;
    private long invokeTimeout = TCAPStackImpl._EMPTY_INVOKE_TIMEOUT;
    private transient TCAPProviderImpl provider;

    public InvokeImpl() {
        // Set Default Class
        this.invokeClass = InvokeClass.Class1;
    }

    public InvokeImpl(InvokeClass invokeClass) {
        if (invokeClass == null) {
            this.invokeClass = InvokeClass.Class1;
        } else {
            this.invokeClass = invokeClass;
        }
    }

    // mandatory
    private Long invokeId;

    // optional
    private Long linkedId;
    private transient Invoke linkedInvoke;

    // mandatory
    private OperationCode operationCode;

    // optional
    private Parameter parameter;

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#getInvokeId()
     */
    public Long getInvokeId() {

        return this.invokeId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#getLinkedId()
     */
    public Long getLinkedId() {

        return this.linkedId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#getLinkedInvoke()
     */
    public Invoke getLinkedInvoke() {
        return linkedInvoke;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#getOperationCode()
     */
    public OperationCode getOperationCode() {

        return this.operationCode;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#getParameteR()
     */
    public Parameter getParameter() {

        return this.parameter;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#setInvokeId(java.lang .Integer)
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
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#setLinkedId(java.lang .Integer)
     */
    public void setLinkedId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.linkedId = i;
    }

    public void setLinkedInvoke(Invoke val) {
        this.linkedInvoke = val;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#setOperationCode(org
     * .mobicents.protocols.ss7.tcap.asn.comp.OperationCode)
     */
    public void setOperationCode(OperationCode i) {
        this.operationCode = i;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.comp.Invoke#setParameter(org.mobicents .protocols.ss7.tcap.asn.comp.Parameter)
     */
    public void setParameter(Parameter p) {
        this.parameter = p;

    }

    public ComponentType getType() {

        return ComponentType.Invoke;
    }

    @Override
    public String toString() {
        return "Invoke[invokeId=" + invokeId + ", linkedId=" + linkedId + ", operationCode=" + operationCode + ", parameter="
                + parameter + ", invokeClass=" + invokeClass + "]";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols .asn.AsnInputStream)
     */
    public void decode(AsnInputStream ais) throws ParseException {

        try {
            AsnInputStream localAis = ais.readSequenceStream();

            // invokeId
            int tag = localAis.readTag();
            if (tag != _TAG_IID || localAis.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new ParseException(null, GeneralProblemType.MistypedComponent,
                        "Error while decoding Invoke: bad tag or tag class for InvokeID: tag=" + tag + ", tagClass = "
                                + localAis.getTagClass());
            }
            this.invokeId = localAis.readInteger();

            tag = localAis.readTag();
            if (tag == _TAG_LID && localAis.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                // linkedId - optional
                this.linkedId = localAis.readInteger();
                tag = localAis.readTag();
            }

            // operationCode
            if (tag != OperationCode._TAG_GLOBAL && tag != OperationCode._TAG_LOCAL
                    || localAis.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new ParseException(null, GeneralProblemType.MistypedComponent,
                        "Error while decoding Invoke: bad tag or tag class for operationCode: tag=" + tag + ", tagClass = "
                                + localAis.getTagClass());
            }
            this.operationCode = TcapFactory.createOperationCode(tag, localAis);

            // It could be PARAMETER
            if (localAis.available() == 0)
                return;
            tag = localAis.readTag();
            this.parameter = TcapFactory.createParameter(tag, localAis, true);

        } catch (IOException e) {
            throw new ParseException(null, GeneralProblemType.BadlyStructuredComponent, "IOException while decoding Invoke: "
                    + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(null, GeneralProblemType.BadlyStructuredComponent, "AsnException while decoding Invoke: "
                    + e.getMessage(), e);
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
        if (this.invokeId == null)
            throw new EncodeException("Invoke ID not set!");
        if (this.operationCode == null)
            throw new EncodeException("Operation Code not set!");

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG);
            int pos = aos.StartContentDefiniteLength();

            aos.writeInteger(this.invokeId);
            if (this.linkedId != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LID, this.linkedId);
            this.operationCode.encode(aos);

            if (this.parameter != null)
                this.parameter.encode(aos);

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new EncodeException("IOException while encoding Invoke: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new EncodeException("AsnException while encoding Invoke: " + e.getMessage(), e);
        }
    }

    /**
     * @return the invokeClass
     */
    public InvokeClass getInvokeClass() {
        return this.invokeClass;
    }

    /**
     * @return the invokeTimeout
     */
    public long getTimeout() {
        return invokeTimeout;
    }

    /**
     * @param invokeTimeout the invokeTimeout to set
     */
    public void setTimeout(long invokeTimeout) {
        this.invokeTimeout = invokeTimeout;
    }

    public void setProvider(TCAPProviderImpl provider) {
        this.provider = provider;
    }

    public void setDialog(Dialog d) { this.dialog=d;}
    public Dialog getDialog() { return dialog;}
}
