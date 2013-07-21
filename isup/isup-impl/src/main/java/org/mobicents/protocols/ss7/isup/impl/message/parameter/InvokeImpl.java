/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.Invoke;
import org.mobicents.protocols.ss7.isup.message.parameter.OperationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.OperationCodeType;
import org.mobicents.protocols.ss7.isup.message.parameter.Parameter;



/**
 * @author baranowb
 *
 */
public class InvokeImpl extends AbstractRemoteOperation implements Invoke {
    // mandatory
    private Long invokeId;
    // optional
    private Long linkedId;
    // mandatory
    private OperationCode operationCode;

    // optional
    private Parameter parameter;

    public InvokeImpl() {
        super(OperationType.Invoke);
    }

    @Override
    public void setInvokeId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.invokeId = i;

    }

    @Override
    public Long getInvokeId() {
        return this.invokeId;
    }

    @Override
    public void setLinkedId(Long i) {
        if ((i == null) || (i < -128 || i > 127)) {
            throw new IllegalArgumentException("Invoke ID our of range: <-128,127>: " + i);
        }
        this.linkedId = i;
    }

    @Override
    public Long getLinkedId() {
        return this.linkedId;
    }

    @Override
    public void setOperationCode(OperationCode i) {
        this.operationCode = i;
    }

    @Override
    public OperationCode getOperationCode() {
        return this.operationCode;
    }

    @Override
    public void setParameter(Parameter p) {
        this.parameter = p;
    }

    @Override
    public Parameter getParameter() {
        return this.parameter;
    }
    @Override
    public void decode(AsnInputStream ais) throws ParameterException {

        try {
            AsnInputStream localAis = ais.readSequenceStream();

            // invokeId
            int tag = localAis.readTag();
            if (tag != _TAG_IID || localAis.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new ParameterException(
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
                throw new ParameterException(
                        "Error while decoding Invoke: bad tag or tag class for operationCode: tag=" + tag + ", tagClass = "
                                + localAis.getTagClass());
            }
            this.operationCode = new OperationCodeImpl();
            ((OperationCodeImpl)this.operationCode).setOperationType(OperationCode._TAG_GLOBAL == tag ? OperationCodeType.Global : OperationCodeType.Local);
            ((AbstractAsnEncodable)this.operationCode).decode(localAis);
            // It could be PARAMETER
            if (localAis.available() == 0)
                return;
            tag = localAis.readTag();
            this.parameter = new ParameterImpl(tag,localAis, true);//TcapFactory.createParameter(tag, localAis, true);

        } catch (IOException e) {
            throw new ParameterException("IOException while decoding Invoke: "
                    + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParameterException("AsnException while decoding Invoke: "
                    + e.getMessage(), e);
        }
    }

    @Override
    public void encode(AsnOutputStream aos) throws ParameterException {
        if (this.invokeId == null)
            throw new ParameterException("Invoke ID not set!");
        if (this.operationCode == null)
            throw new ParameterException("Operation Code not set!");

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG);
            int pos = aos.StartContentDefiniteLength();

            aos.writeInteger(this.invokeId);
            if (this.linkedId != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LID, this.linkedId);
            ((AbstractAsnEncodable)this.operationCode).encode(aos);

            if (this.parameter != null)
                ((AbstractAsnEncodable)this.parameter).encode(aos);

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new ParameterException("IOException while encoding Invoke: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParameterException("AsnException while encoding Invoke: " + e.getMessage(), e);
        }
    }

}
