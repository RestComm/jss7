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

package org.mobicents.protocols.ss7.map.errors;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessagePositionMethodFailure;
import org.mobicents.protocols.ss7.map.api.errors.PositionMethodFailureDiagnostic;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPErrorMessagePositionMethodFailureImpl extends MAPErrorMessageImpl implements
        MAPErrorMessagePositionMethodFailure {

    public static final int PositionMethodFailureDiagnostic_TAG = 0;
    public static final int ExtensionContainer_TAG = 1;

    private PositionMethodFailureDiagnostic positionMethodFailureDiagnostic;
    private MAPExtensionContainer extensionContainer;

    public MAPErrorMessagePositionMethodFailureImpl(PositionMethodFailureDiagnostic positionMethodFailureDiagnostic,
            MAPExtensionContainer extensionContainer) {
        super((long) MAPErrorCode.positionMethodFailure);

        this.positionMethodFailureDiagnostic = positionMethodFailureDiagnostic;
        this.extensionContainer = extensionContainer;
    }

    protected MAPErrorMessagePositionMethodFailureImpl() {
        super((long) MAPErrorCode.positionMethodFailure);
    }

    public boolean isEmPositionMethodFailure() {
        return true;
    }

    public MAPErrorMessagePositionMethodFailure getEmPositionMethodFailure() {
        return this;
    }

    public PositionMethodFailureDiagnostic getPositionMethodFailureDiagnostic() {
        return this.positionMethodFailureDiagnostic;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public void setPositionMethodFailureDiagnostic(PositionMethodFailureDiagnostic positionMethodFailureDiagnostic) {
        this.positionMethodFailureDiagnostic = positionMethodFailureDiagnostic;
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
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

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPErrorMessagePositionMethodFailure: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessagePositionMethodFailure: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPErrorMessagePositionMethodFailure: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessagePositionMethodFailure: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.extensionContainer = null;
        this.positionMethodFailureDiagnostic = null;

        if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || localAis.getTag() != Tag.SEQUENCE || localAis.isTagPrimitive())
            throw new MAPParsingComponentException(
                    "Error decoding MAPErrorMessagePositionMethodFailure: bad tag class or tag or parameter is primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        AsnInputStream ais = localAis.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case PositionMethodFailureDiagnostic_TAG:
                            int code = (int) ais.readInteger();
                            this.positionMethodFailureDiagnostic = PositionMethodFailureDiagnostic.getInstance(code);
                            break;

                        case ExtensionContainer_TAG:
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
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPErrorMessagePositionMethodFailure: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream aos) throws MAPException {

        if (this.positionMethodFailureDiagnostic == null && this.extensionContainer == null)
            return;

        try {
            if (this.positionMethodFailureDiagnostic != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, PositionMethodFailureDiagnostic_TAG,
                        this.positionMethodFailureDiagnostic.getCode());
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        ExtensionContainer_TAG);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding MAPErrorMessagePositionMethodFailure: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPErrorMessagePositionMethodFailure: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessagePositionMethodFailure [");
        if (this.positionMethodFailureDiagnostic != null)
            sb.append("positionMethodFailureDiagnostic=" + this.positionMethodFailureDiagnostic.toString());
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        sb.append("]");

        return sb.toString();
    }

}
