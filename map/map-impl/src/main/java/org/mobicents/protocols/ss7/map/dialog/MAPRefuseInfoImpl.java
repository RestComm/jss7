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
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextNameImpl;

/**
 * MAP-RefuseInfo ::= SEQUENCE { reason Reason, ..., extensionContainer ExtensionContainer -- extensionContainer must not be
 * used in version 2 }
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPRefuseInfoImpl implements MAPAsnPrimitive {

    public static final int MAP_REFUSE_INFO_TAG = 0x03;

    protected static final int REFUSE_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    protected static final boolean REFUSE_TAG_PC_CONSTRUCTED = false;
    protected static final boolean REFUSE_TAG_PC_PRIMITIVE = true;

    private Reason reason;
    private MAPExtensionContainer extensionContainer;
    private ApplicationContextName alternativeAcn;

    public Reason getReason() {
        return this.reason;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public ApplicationContextName getAlternativeAcn() {
        return this.alternativeAcn;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setAlternativeAcn(ApplicationContextName alternativeAcn) {
        this.alternativeAcn = alternativeAcn;
    }

    public int getTag() throws MAPException {
        return MAP_REFUSE_INFO_TAG;
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
            throw new MAPParsingComponentException("IOException when decoding MAP-RefuseInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAP-RefuseInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAP-RefuseInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAP-RefuseInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {
        // MAP-RefuseInfo ::= SEQUENCE {
        // reason ENUMERATED {
        // noReasonGiven ( 0 ),
        // invalidDestinationReference ( 1 ),
        // invalidOriginatingReference ( 2 ) },
        // ... ,
        // extensionContainer SEQUENCE {
        // privateExtensionList [0] IMPLICIT SEQUENCE ( SIZE( 1 .. 10 ) ) OF
        // SEQUENCE {
        // extId MAP-EXTENSION .&extensionId ( {
        // ,
        // ...} ) ,
        // extType MAP-EXTENSION .&ExtensionType ( {
        // ,
        // ...} { @extId } ) OPTIONAL} OPTIONAL,
        // pcs-Extensions [1] IMPLICIT SEQUENCE {
        // ... } OPTIONAL,
        // ... } OPTIONAL,
        // alternativeApplicationContext OBJECT IDENTIFIER OPTIONAL}

        this.reason = null;
        this.alternativeAcn = null;
        this.extensionContainer = null;

        AsnInputStream localAis = ais.readSequenceStreamData(length);

        int tag = localAis.readTag();
        if (tag != Tag.ENUMERATED || localAis.getTagClass() != Tag.CLASS_UNIVERSAL)
            throw new MAPParsingComponentException("Error decoding MAP-RefuseInfo.Reason: bad tag or tagClass",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        int reasonCode = (int) localAis.readInteger();
        this.reason = Reason.getReason(reasonCode);

        while (localAis.available() > 0) {
            tag = localAis.readTag();

            switch (localAis.getTagClass()) {
                case Tag.CLASS_UNIVERSAL:
                    switch (tag) {
                        case Tag.SEQUENCE:
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(localAis);
                            break;

                        case Tag.OBJECT_IDENTIFIER:
                            this.alternativeAcn = new ApplicationContextNameImpl();
                            long[] oid = localAis.readObjectIdentifier();
                            this.alternativeAcn.setOid(oid);
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

        this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, MAP_REFUSE_INFO_TAG);
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPRefuseInfo: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOS) throws MAPException {

        if (this.reason == null)
            throw new MAPException("Error decoding MAP-RefuseInfo: Reason field must not be empty");

        try {
            asnOS.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.reason.getCode());

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOS);
            if (this.alternativeAcn != null)
                asnOS.writeObjectIdentifier(this.alternativeAcn.getOid());

        } catch (IOException e) {
            throw new MAPException("IOException when encoding MAPRefuseInfo: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPRefuseInfo: " + e.getMessage(), e);
        }
    }
}
