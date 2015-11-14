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

package org.mobicents.protocols.ss7.map.errors;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessagePwRegistrationFailure;
import org.mobicents.protocols.ss7.map.api.errors.PWRegistrationFailureCause;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
public class MAPErrorMessagePwRegistrationFailureImpl extends MAPErrorMessageImpl implements
        MAPErrorMessagePwRegistrationFailure {

    private static final String PW_REGIS_FAILURE_CAUSE = "pwRegistrationFailureCause";

    private PWRegistrationFailureCause pwRegistrationFailureCause;

    protected String _PrimitiveName = "MAPErrorMessagePwRegistrationFailure";

    public MAPErrorMessagePwRegistrationFailureImpl(PWRegistrationFailureCause pwRegistrationFailureCause) {
        super((long) MAPErrorCode.pwRegistrationFailure);

        this.pwRegistrationFailureCause = pwRegistrationFailureCause;
    }

    public MAPErrorMessagePwRegistrationFailureImpl() {
        super((long) MAPErrorCode.pwRegistrationFailure);
    }

    public boolean isEmPwRegistrationFailure() {
        return true;
    }

    public MAPErrorMessagePwRegistrationFailure getEmPwRegistrationFailure() {
        return this;
    }

    @Override
    public PWRegistrationFailureCause getPWRegistrationFailureCause() {
        return pwRegistrationFailureCause;
    }

    @Override
    public void setPWRegistrationFailureCause(PWRegistrationFailureCause val) {
        pwRegistrationFailureCause = val;
    }

    @Override
    public int getTag() throws MAPException {
        return Tag.ENUMERATED;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {

        if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || localAis.getTag() != Tag.ENUMERATED || !localAis.isTagPrimitive())
            throw new MAPParsingComponentException("Error decoding " + _PrimitiveName
                    + ": bad tag class or tag or parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);

        int i1 = (int) localAis.readIntegerData(length);
        this.pwRegistrationFailureCause = PWRegistrationFailureCause.getInstance(i1);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.pwRegistrationFailureCause == null)
            throw new MAPException("Parameter pwRegistrationFailureCause must not be null");

        try {
            asnOs.writeIntegerData(this.pwRegistrationFailureCause.getCode());
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.pwRegistrationFailureCause != null)
            sb.append("pwRegistrationFailureCause=" + this.pwRegistrationFailureCause.toString());
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MAPErrorMessagePwRegistrationFailureImpl> MAP_ERROR_MESSAGE_PW_REGIS_FAILURE_XML = new XMLFormat<MAPErrorMessagePwRegistrationFailureImpl>(
            MAPErrorMessagePwRegistrationFailureImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MAPErrorMessagePwRegistrationFailureImpl errorMessage)
                throws XMLStreamException {
            MAP_ERROR_MESSAGE_XML.read(xml, errorMessage);
            String str = xml.get(PW_REGIS_FAILURE_CAUSE, String.class);
            if (str != null)
                errorMessage.pwRegistrationFailureCause = Enum.valueOf(PWRegistrationFailureCause.class, str);
        }

        @Override
        public void write(MAPErrorMessagePwRegistrationFailureImpl errorMessage, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            MAP_ERROR_MESSAGE_XML.write(errorMessage, xml);
            if (errorMessage.getPWRegistrationFailureCause() != null)
                xml.add((String) errorMessage.getPWRegistrationFailureCause().toString(), PW_REGIS_FAILURE_CAUSE, String.class);
        }
    };

}
