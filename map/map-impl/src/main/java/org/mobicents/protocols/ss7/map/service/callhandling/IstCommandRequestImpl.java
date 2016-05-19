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

package org.mobicents.protocols.ss7.map.service.callhandling;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.IstCommandRequest;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

import java.io.IOException;

/**
 *
 * @author eva ogallar
 *
 */
public class IstCommandRequestImpl extends CallHandlingMessageImpl implements IstCommandRequest {

    public static final String _PrimitiveName = "IstCommandRequest";

    private static final int TAG_imsi = 0;
    private static final int TAG_extensionContainer = 1;


    private IMSI imsi;

    private MAPExtensionContainer extensionContainer;

    public IstCommandRequestImpl(IMSI imsi, MAPExtensionContainer extensionContainer) {
        this.imsi = imsi;
        this.extensionContainer = extensionContainer;
    }

    public IstCommandRequestImpl() {
    }

    @Override
    public IMSI getImsi() {
        return imsi;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.istCommand_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.istCommand;
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

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.extensionContainer = null;
        this.imsi = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case TAG_imsi:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".imsi: is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.imsi = new IMSIImpl();
                            ((IMSIImpl) this.imsi).decodeAll(ais);
                            break;
                        case TAG_extensionContainer:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
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

        if (this.imsi == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament imsi is mandatory but does not found", MAPParsingComponentExceptionReason.MistypedParameter);
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
        if (this.imsi == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + " the mandatory parameter IMSI is not defined");

        ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_imsi);

        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_extensionContainer);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
