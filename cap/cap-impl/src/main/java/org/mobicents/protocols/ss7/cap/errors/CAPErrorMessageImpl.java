/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.cap.errors;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageCancelFailed;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageParameterless;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageRequestedInfoError;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageSystemFailure;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageTaskRefused;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 * Base class of CAP ReturnError messages
 *
 * @author sergey vetyutnev
 *
 */
public abstract class CAPErrorMessageImpl implements CAPErrorMessage, CAPAsnPrimitive {

    private static final String ERROR_CODE = "errorCode";

    protected Long errorCode;

    protected CAPErrorMessageImpl(Long errorCode) {
        this.errorCode = errorCode;
    }

    public CAPErrorMessageImpl() {
    }

    @Override
    public Long getErrorCode() {
        return errorCode;
    }

    @Override
    public boolean isEmParameterless() {
        return false;
    }

    @Override
    public boolean isEmCancelFailed() {
        return false;
    }

    @Override
    public boolean isEmRequestedInfoError() {
        return false;
    }

    @Override
    public boolean isEmSystemFailure() {
        return false;
    }

    @Override
    public boolean isEmTaskRefused() {
        return false;
    }

    @Override
    public CAPErrorMessageParameterless getEmParameterless() {
        return null;
    }

    @Override
    public CAPErrorMessageCancelFailed getEmCancelFailed() {
        return null;
    }

    @Override
    public CAPErrorMessageRequestedInfoError getEmRequestedInfoError() {
        return null;
    }

    @Override
    public CAPErrorMessageSystemFailure getEmSystemFailure() {
        return null;
    }

    @Override
    public CAPErrorMessageTaskRefused getEmTaskRefused() {
        return null;
    }

    /**
     * XML Serialization/Deserialization
     */
    public static final XMLFormat<CAPErrorMessageImpl> CAP_ERROR_MESSAGE_XML = new XMLFormat<CAPErrorMessageImpl>(
            CAPErrorMessageImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CAPErrorMessageImpl message) throws XMLStreamException {
            message.errorCode = xml.getAttribute(ERROR_CODE, -1L);
        }

        @Override
        public void write(CAPErrorMessageImpl message, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(ERROR_CODE, message.errorCode);
        }
    };
}
