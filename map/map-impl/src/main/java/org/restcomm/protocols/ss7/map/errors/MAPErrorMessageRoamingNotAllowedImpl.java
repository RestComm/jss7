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

package org.restcomm.protocols.ss7.map.errors;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.errors.AdditionalRoamingNotAllowedCause;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageRoamingNotAllowed;
import org.restcomm.protocols.ss7.map.api.errors.RoamingNotAllowedCause;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
public class MAPErrorMessageRoamingNotAllowedImpl extends MAPErrorMessageImpl implements MAPErrorMessageRoamingNotAllowed {

    private static final String ROAMING_NOT_ALLOWED_CAUSE = "roamingNotAllowedCause";
    private static final String ADDITIONAL_ROAMING_NOT_ALLOWED_CAUSE = "additionalRoamingNotAllowedCause";
    private static final String MAP_EXTENSION_CONTAINER = "mapExtensionContainer";

    public static final int _tag_additionalRoamingNotAllowedCause = 0;

    private RoamingNotAllowedCause roamingNotAllowedCause;
    private MAPExtensionContainer extensionContainer;
    private AdditionalRoamingNotAllowedCause additionalRoamingNotAllowedCause;

    protected String _PrimitiveName = "MAPErrorMessageRoamingNotAllowed";

    public MAPErrorMessageRoamingNotAllowedImpl(RoamingNotAllowedCause roamingNotAllowedCause,
            MAPExtensionContainer extensionContainer, AdditionalRoamingNotAllowedCause additionalRoamingNotAllowedCause) {
        super((long) MAPErrorCode.roamingNotAllowed);

        this.roamingNotAllowedCause = roamingNotAllowedCause;
        this.extensionContainer = extensionContainer;
        this.additionalRoamingNotAllowedCause = additionalRoamingNotAllowedCause;
    }

    public MAPErrorMessageRoamingNotAllowedImpl() {
        super((long) MAPErrorCode.roamingNotAllowed);
    }

    public boolean isEmRoamingNotAllowed() {
        return true;
    }

    public MAPErrorMessageRoamingNotAllowed getEmRoamingNotAllowed() {
        return this;
    }

    @Override
    public RoamingNotAllowedCause getRoamingNotAllowedCause() {
        return roamingNotAllowedCause;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public AdditionalRoamingNotAllowedCause getAdditionalRoamingNotAllowedCause() {
        return additionalRoamingNotAllowedCause;
    }

    @Override
    public void setRoamingNotAllowedCause(RoamingNotAllowedCause val) {
        roamingNotAllowedCause = val;
    }

    @Override
    public void setExtensionContainer(MAPExtensionContainer val) {
        extensionContainer = val;
    }

    @Override
    public void setAdditionalRoamingNotAllowedCause(AdditionalRoamingNotAllowedCause val) {
        additionalRoamingNotAllowedCause = val;
    }

    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
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

        this.roamingNotAllowedCause = null;
        this.extensionContainer = null;
        this.additionalRoamingNotAllowedCause = null;

        if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || localAis.getTag() != Tag.SEQUENCE || localAis.isTagPrimitive())
            throw new MAPParsingComponentException("Error decoding " + _PrimitiveName
                    + ": bad tag class or tag or parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);

        AsnInputStream ais = localAis.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_UNIVERSAL:
                    switch (tag) {
                        case Tag.ENUMERATED:
                            int i1 = (int) ais.readInteger();
                            this.roamingNotAllowedCause = RoamingNotAllowedCause.getInstance(i1);
                            break;
                        case Tag.SEQUENCE:
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case _tag_additionalRoamingNotAllowedCause:
                            int i1 = (int) ais.readInteger();
                            this.additionalRoamingNotAllowedCause = AdditionalRoamingNotAllowedCause.getInstance(i1);
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                default:
                    ais.advanceElement();
                    break;
            }
        }

        if (this.roamingNotAllowedCause == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter roamingNotAllowedCause is mandatory but has not found.",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
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

        if (this.roamingNotAllowedCause == null) {
            throw new MAPException("Parameter roamingNotAllowedCause must not be null");
        }

        try {
            asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.roamingNotAllowedCause.getCode());
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
            if (this.additionalRoamingNotAllowedCause != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _tag_additionalRoamingNotAllowedCause,
                        this.additionalRoamingNotAllowedCause.getCode());

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.roamingNotAllowedCause != null) {
            sb.append("roamingNotAllowedCause = ");
            sb.append(roamingNotAllowedCause);
        }
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        if (this.additionalRoamingNotAllowedCause != null) {
            sb.append(", additionalRoamingNotAllowedCause = ");
            sb.append(additionalRoamingNotAllowedCause);
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MAPErrorMessageRoamingNotAllowedImpl> MAP_ERROR_MESSAGE_ROAMING_NOT_ALLOWED_XML = new XMLFormat<MAPErrorMessageRoamingNotAllowedImpl>(
            MAPErrorMessageRoamingNotAllowedImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MAPErrorMessageRoamingNotAllowedImpl errorMessage)
                throws XMLStreamException {
            MAP_ERROR_MESSAGE_XML.read(xml, errorMessage);
            String str = xml.get(ROAMING_NOT_ALLOWED_CAUSE, String.class);
            if (str != null)
                errorMessage.roamingNotAllowedCause = Enum.valueOf(RoamingNotAllowedCause.class, str);

            str = xml.get(ADDITIONAL_ROAMING_NOT_ALLOWED_CAUSE, String.class);
            if (str != null)
                errorMessage.additionalRoamingNotAllowedCause = Enum.valueOf(AdditionalRoamingNotAllowedCause.class, str);

            errorMessage.extensionContainer = xml.get(MAP_EXTENSION_CONTAINER, MAPExtensionContainerImpl.class);
        }

        @Override
        public void write(MAPErrorMessageRoamingNotAllowedImpl errorMessage, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            MAP_ERROR_MESSAGE_XML.write(errorMessage, xml);
            if (errorMessage.getRoamingNotAllowedCause() != null)
                xml.add((String) errorMessage.getRoamingNotAllowedCause().toString(), ROAMING_NOT_ALLOWED_CAUSE, String.class);
            if (errorMessage.getAdditionalRoamingNotAllowedCause() != null)
                xml.add((String) errorMessage.getAdditionalRoamingNotAllowedCause().toString(),
                        ADDITIONAL_ROAMING_NOT_ALLOWED_CAUSE, String.class);

            xml.add((MAPExtensionContainerImpl) errorMessage.extensionContainer, MAP_EXTENSION_CONTAINER,
                    MAPExtensionContainerImpl.class);
        }
    };

}
