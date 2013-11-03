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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * map-accept [1] IMPLICIT SEQUENCE { ... , extensionContainer SEQUENCE { privateExtensionList [0] IMPLICIT SEQUENCE SIZE (1 ..
 * SEQUENCE { extId MAP-EXTENSION .&extensionId ( { , ...} ) , extType MAP-EXTENSION .&ExtensionType ( { , ...} { @extId } )
 * OPTIONAL} OPTIONAL, pcs-Extensions [1] IMPLICIT SEQUENCE { ... } OPTIONAL, ... } OPTIONAL},
 *
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPAcceptInfoImpl implements MAPAsnPrimitive {

    public static final int MAP_ACCEPT_INFO_TAG = 0x01;

    protected static final int ACCEPT_INFO_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    protected static final boolean ACCEPT_INFO_TAG_PC_PRIMITIVE = true;
    protected static final boolean ACCEPT_INFO_TAG_PC_CONSTRUCTED = false;

    private MAPExtensionContainer extensionContainer;

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public int getTag() throws MAPException {
        return MAP_ACCEPT_INFO_TAG;
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
            throw new MAPParsingComponentException("IOException when decoding MAPAcceptInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPAcceptInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPAcceptInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPAcceptInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {
        // MAP-AcceptInfo ::= SEQUENCE {
        // ... ,
        // extensionContainer SEQUENCE {
        // privateExtensionList [0] IMPLICIT SEQUENCE SIZE (1 .. 10 ) OF
        // SEQUENCE {
        // extId MAP-EXTENSION .&extensionId ( {
        // ,
        // ...} ) ,
        // extType MAP-EXTENSION .&ExtensionType ( {
        // ,
        // ...} { @extId } ) OPTIONAL} OPTIONAL,
        // pcs-Extensions [1] IMPLICIT SEQUENCE {
        // ... } OPTIONAL,
        // ... } OPTIONAL}

        this.setExtensionContainer(null);

        AsnInputStream localAis = ais.readSequenceStreamData(length);

        while (localAis.available() > 0) {
            int tag = localAis.readTag();

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

        this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, MAP_ACCEPT_INFO_TAG);
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPAcceptInfo: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOS) throws MAPException {
        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOS);
    }
}
