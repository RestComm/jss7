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

package org.mobicents.protocols.ss7.map.errors;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageParameterless;

/**
 * The MAP ReturnError message without any parameters
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
public class MAPErrorMessageParameterlessImpl extends MAPErrorMessageImpl implements MAPErrorMessageParameterless {

    public MAPErrorMessageParameterlessImpl() {
    }

    public MAPErrorMessageParameterlessImpl(Long errorCode) {
        super(errorCode);
    }

    public boolean isEmParameterless() {
        return true;
    }

    public MAPErrorMessageParameterless getEmParameterless() {
        return this;
    }

    public int getTag() throws MAPException {
        throw new MAPException("MAPErrorMessageParameterless does not support encoding");
    }

    public int getTagClass() {
        return 0;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
    }

    @Override
    public String toString() {
        return "MAPErrorMessageParameterless [errorCode=" + this.errorCode + "]";
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MAPErrorMessageParameterlessImpl> MAP_ERROR_MESSAGE_PARAMETERLESS_XML = new XMLFormat<MAPErrorMessageParameterlessImpl>(
            MAPErrorMessageParameterlessImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MAPErrorMessageParameterlessImpl errorMessage)
                throws XMLStreamException {
            MAP_ERROR_MESSAGE_XML.read(xml, errorMessage);
        }

        @Override
        public void write(MAPErrorMessageParameterlessImpl errorMessage, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            MAP_ERROR_MESSAGE_XML.write(errorMessage, xml);
        }
    };
}
