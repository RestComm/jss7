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
import org.mobicents.protocols.ss7.isup.message.parameter.ErrorCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ErrorCodeType;
import org.mobicents.protocols.ss7.isup.message.parameter.Parameter;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnError;

/**
 * @author baranowb
 *
 */
public class ReturnErrorImpl extends AbstractRemoteOperation implements ReturnError {
    // mandatory
    private Long invokeId;
    // optional
    private ErrorCode errorCode;
    // optional
    private Parameter parameter;

    public ReturnErrorImpl() {
        super(OperationType.ReturnError);
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
    public void setErrorCode(ErrorCode ec) {
        this.errorCode = ec;
    }

    @Override
    public ErrorCode getErrorCode() {
        return this.errorCode;
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
            if (tag != _TAG_IID || localAis.getTagClass() != _TAG_IID_CLASS) {
                throw new ParameterException("Error while decoding Invoke: bad tag or tag class for InvokeID: tag=" + tag
                        + ", tagClass = " + localAis.getTagClass());
            }
            this.invokeId = localAis.readInteger();
            if (localAis.available() == 0) {
                return;
            }
            while (localAis.available() > 0) {

                tag = localAis.readTag();
                ErrorCodeType type = null;
                switch (tag) {
                    case ErrorCode._TAG_GLOBAL:
                        type = ErrorCodeType.Global;
                    case ErrorCode._TAG_LOCAL:
                        if (this.errorCode != null) {
                            throw new ParameterException();
                        }
                        if (localAis.getTagClass() != _TAG_EC_CLASS) {
                            throw new ParameterException();
                        }
                        if (type == null)
                            type = ErrorCodeType.Local;
                        ErrorCodeImpl ec = new ErrorCodeImpl();
                        ec.setErrorCodeType(type);
                        ec.decode(localAis);
                        this.errorCode = ec;
                    default:
                        if (this.parameter != null) {
                            throw new ParameterException();
                        }
                        this.parameter = new ParameterImpl(tag, localAis, true);// TcapFactory.createParameter(tag, localAis,
                                                                                // true);
                }
            }
        } catch (IOException e) {
            throw new ParameterException("IOException while decoding Invoke: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParameterException("AsnException while decoding Invoke: " + e.getMessage(), e);
        }

    }

    @Override
    public void encode(AsnOutputStream aos) throws ParameterException {
        if (this.invokeId == null)
            throw new ParameterException("Invoke ID not set!");
        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG);
            int pos = aos.StartContentDefiniteLength();
            aos.writeInteger(this.invokeId);
            if (this.errorCode != null)
                ((AbstractAsnEncodable) this.errorCode).encode(aos);

            if (this.parameter != null)
                ((AbstractAsnEncodable) this.parameter).encode(aos);

            aos.FinalizeContent(pos);

        } catch (IOException e) {
            throw new ParameterException("IOException while encoding Invoke: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParameterException("AsnException while encoding Invoke: " + e.getMessage(), e);
        }
    }

}
