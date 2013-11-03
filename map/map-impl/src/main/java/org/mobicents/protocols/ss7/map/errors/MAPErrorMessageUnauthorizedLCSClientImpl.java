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
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageUnauthorizedLCSClient;
import org.mobicents.protocols.ss7.map.api.errors.UnauthorizedLCSClientDiagnostic;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPErrorMessageUnauthorizedLCSClientImpl extends MAPErrorMessageImpl implements
        MAPErrorMessageUnauthorizedLCSClient {

    public static final int UnauthorizedLCSClientDiagnostic_TAG = 0;
    public static final int ExtensionContainer_TAG = 1;

    private UnauthorizedLCSClientDiagnostic unauthorizedLCSClientDiagnostic;
    private MAPExtensionContainer extensionContainer;

    public MAPErrorMessageUnauthorizedLCSClientImpl(UnauthorizedLCSClientDiagnostic unauthorizedLCSClientDiagnostic,
            MAPExtensionContainer extensionContainer) {
        super((long) MAPErrorCode.unauthorizedLCSClient);

        this.unauthorizedLCSClientDiagnostic = unauthorizedLCSClientDiagnostic;
        this.extensionContainer = extensionContainer;
    }

    protected MAPErrorMessageUnauthorizedLCSClientImpl() {
        super((long) MAPErrorCode.unauthorizedLCSClient);
    }

    public boolean isEmUnauthorizedLCSClient() {
        return true;
    }

    public MAPErrorMessageUnauthorizedLCSClient getEmUnauthorizedLCSClient() {
        return this;
    }

    public UnauthorizedLCSClientDiagnostic getUnauthorizedLCSClientDiagnostic() {
        return this.unauthorizedLCSClientDiagnostic;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public void setUnauthorizedLCSClientDiagnostic(UnauthorizedLCSClientDiagnostic unauthorizedLCSClientDiagnostic) {
        this.unauthorizedLCSClientDiagnostic = unauthorizedLCSClientDiagnostic;
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
            throw new MAPParsingComponentException("IOException when decoding MAPErrorMessageUnauthorizedLCSClient: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageUnauthorizedLCSClient: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPErrorMessageUnauthorizedLCSClient: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageUnauthorizedLCSClient: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.extensionContainer = null;
        this.unauthorizedLCSClientDiagnostic = null;

        if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || localAis.getTag() != Tag.SEQUENCE || localAis.isTagPrimitive())
            throw new MAPParsingComponentException(
                    "Error decoding MAPErrorMessageUnauthorizedLCSClient: bad tag class or tag or parameter is primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        AsnInputStream ais = localAis.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case UnauthorizedLCSClientDiagnostic_TAG:
                            int code = (int) ais.readInteger();
                            this.unauthorizedLCSClientDiagnostic = UnauthorizedLCSClientDiagnostic.getInstance(code);
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
            throw new MAPException("AsnException when encoding MAPErrorMessageUnauthorizedLCSClient: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream aos) throws MAPException {

        if (this.unauthorizedLCSClientDiagnostic == null && this.extensionContainer == null)
            return;

        try {
            if (this.unauthorizedLCSClientDiagnostic != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, UnauthorizedLCSClientDiagnostic_TAG,
                        this.unauthorizedLCSClientDiagnostic.getCode());
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        ExtensionContainer_TAG);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding MAPErrorMessageUnauthorizedLCSClient: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPErrorMessageUnauthorizedLCSClient: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageUnauthorizedLCSClient [");
        if (this.unauthorizedLCSClientDiagnostic != null)
            sb.append("unauthorizedLCSClientDiagnostic=" + this.unauthorizedLCSClientDiagnostic.toString());
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        sb.append("]");

        return sb.toString();
    }
}
