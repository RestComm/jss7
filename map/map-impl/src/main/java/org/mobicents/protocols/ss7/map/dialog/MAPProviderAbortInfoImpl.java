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

package org.mobicents.protocols.ss7.map.dialog;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 MAP-ProviderAbortInfo ::= SEQUENCE { map-ProviderAbortReason MAP-ProviderAbortReason, ..., extensionContainer
 * ExtensionContainer OPTIONAL -- extensionContainer must not be used in version 2 }
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPProviderAbortInfoImpl implements MAPAsnPrimitive {

    public static final int MAP_PROVIDER_ABORT_INFO_TAG = 0x05;

    protected static final int PROVIDER_ABORT_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    protected static final boolean PROVIDER_ABORT_TAG_PC_CONSTRUCTED = false;

    private MAPProviderAbortReason mapProviderAbortReason = null;
    private MAPExtensionContainer extensionContainer;

    public MAPProviderAbortInfoImpl() {
    }

    public MAPProviderAbortReason getMAPProviderAbortReason() {
        return this.mapProviderAbortReason;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setMAPProviderAbortReason(MAPProviderAbortReason mapProvAbrtReas) {
        this.mapProviderAbortReason = mapProvAbrtReas;
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public int getTag() throws MAPException {
        return MAP_PROVIDER_ABORT_INFO_TAG;
    }

    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPProviderAbortInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPProviderAbortInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPProviderAbortInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPProviderAbortInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.setMAPProviderAbortReason(null);
        this.setExtensionContainer(null);

        AsnInputStream localAis = ais.readSequenceStreamData(length);

        int tag = localAis.readTag();
        if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL && tag != Tag.ENUMERATED)
            throw new MAPParsingComponentException(
                    "Error decoding MAP-ProviderAbortInfo.map-ProviderAbortReason: bad tagClass or tag: tagClass="
                            + localAis.getTagClass() + ", tag=" + tag, MAPParsingComponentExceptionReason.MistypedParameter);
        int code = (int) localAis.readInteger();
        this.mapProviderAbortReason = MAPProviderAbortReason.getInstance(code);
        if (this.mapProviderAbortReason == null)
            throw new MAPParsingComponentException(
                    "Bad map-ProviderAbortReason code received when decoding MAP-ProviderAbortInfo" + code,
                    MAPParsingComponentExceptionReason.MistypedParameter);

        while (localAis.available() > 0) {
            tag = localAis.readTag();

            switch (localAis.getTagClass()) {
                case Tag.CLASS_UNIVERSAL:
                    switch (tag) {
                        case Tag.SEQUENCE:
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(localAis);
                            break;

                        default:
                            localAis.advanceElement();
                            break;
                    }
                    break;

                default:
                    localAis.advanceElement();
                    break;
            }
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, MAP_PROVIDER_ABORT_INFO_TAG);
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAP-ProviderAbortInfo: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOS) throws MAPException {

        if (this.mapProviderAbortReason == null)
            throw new MAPException("Error while encoding MAP-ProviderAbortInfo: MapProviderAbortReason parameter has not set");

        try {
            asnOS.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.mapProviderAbortReason.getCode());

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOS);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding MAPProviderAbortInfo: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPProviderAbortInfo: " + e.getMessage(), e);
        }
    }
}
