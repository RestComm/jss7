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
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * Below is Ericsson MAP-OpenInfo
 *
 * MAP−OpenInfo ::= SEQUENCE ( imsi (0) IMSI OPTIONAL, originationReference (1) AddressString OPTIONAL, msisdn (2)
 * AddressString, ... vlrNo (3) AddressString OPTIONAL )
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPOpenInfoImpl implements MAPAsnPrimitive {

    public static final int MAP_OPEN_INFO_TAG = 0x00;

    protected static final int DESTINATION_REF_TAG = 0x00;
    protected static final int ORIGINATION_REF_TAG = 0x01;
    protected static final int ERI_MSISDN_TAG = 0x02;
    protected static final int ERI_NLR_NO_TAG = 0x03;

    protected static final int OPEN_INFO_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
    protected static final boolean OPEN_INFO_TAG_PC_PRIMITIVE = true;
    protected static final boolean OPEN_INFO_TAG_PC_CONSTRUCTED = false;

    private AddressString destReference;
    private AddressString origReference;
    private MAPExtensionContainer extensionContainer;

    private boolean eriStyle;
    private IMSI eriImsi;
    private AddressString eriVlrNo;

    public AddressString getDestReference() {
        return this.destReference;
    }

    public AddressString getOrigReference() {
        return this.origReference;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public boolean getEriStyle() {
        return this.eriStyle;
    }

    public IMSI getEriImsi() {
        return eriImsi;
    }

    public AddressString getEriVlrNo() {
        return eriVlrNo;
    }

    public void setDestReference(AddressString destReference) {
        this.destReference = destReference;
    }

    public void setOrigReference(AddressString origReference) {
        this.origReference = origReference;
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setEriStyle(boolean eriStyle) {
        this.eriStyle = eriStyle;
    }

    public void setEriImsi(IMSI eriImsi) {
        this.eriImsi = eriImsi;
    }

    public void setEriVlrNo(AddressString eriVlrNo) {
        this.eriVlrNo = eriVlrNo;
    }

    public int getTag() throws MAPException {
        return MAP_OPEN_INFO_TAG;
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
            throw new MAPParsingComponentException("IOException when decoding MAPOpenInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPOpenInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPOpenInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPOpenInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {

        // Definitioon from GSM 09.02 version 5.15.1 Page 690
        // map-open [0] IMPLICIT SEQUENCE {
        // destinationReference [0] IMPLICIT OCTET STRING ( SIZE( 1 .. 20 ) )
        // OPTIONAL,
        // originationReference [1] IMPLICIT OCTET STRING ( SIZE( 1 .. 20 ) )
        // OPTIONAL,
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
        // ... } OPTIONAL},

        this.destReference = null;
        this.origReference = null;
        this.extensionContainer = null;
        this.eriStyle = false;
        this.eriImsi = null;
        this.eriVlrNo = null;

        AsnInputStream localAis = ais.readSequenceStreamData(length);

        // checking for Ericsson-style
        int startPos = localAis.position();
        while (localAis.available() > 0) {
            int tag = localAis.readTag();
            if (localAis.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && tag == ERI_MSISDN_TAG) {
                this.eriStyle = true;
                break;
            }
            localAis.advanceElement();
        }

        // parsing
        localAis.position(startPos);
        while (localAis.available() > 0) {
            int tag = localAis.readTag();

            switch (localAis.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case DESTINATION_REF_TAG:
                            if (this.eriStyle) {
                                this.eriImsi = new IMSIImpl();
                                ((IMSIImpl) this.eriImsi).decodeAll(localAis);
                            } else {
                                this.destReference = new AddressStringImpl();
                                ((AddressStringImpl) this.destReference).decodeAll(localAis);
                            }
                            break;

                        case ORIGINATION_REF_TAG:
                            this.origReference = new AddressStringImpl();
                            ((AddressStringImpl) this.origReference).decodeAll(localAis);
                            break;

                        case ERI_MSISDN_TAG:
                            this.destReference = new AddressStringImpl();
                            ((AddressStringImpl) this.destReference).decodeAll(localAis);
                            break;

                        case ERI_NLR_NO_TAG:
                            this.eriVlrNo = new AddressStringImpl();
                            ((AddressStringImpl) this.eriVlrNo).decodeAll(localAis);
                            break;

                        default:
                            localAis.advanceElement();
                            break;
                    }
                    break;

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

        this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, MAP_OPEN_INFO_TAG);
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPOpenInfo: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOS) throws MAPException {

        if (this.eriStyle) {

            if (this.destReference == null)
                throw new MAPException(
                        "Error when encoding MAPOpenInf Ericsson style: destReference parameter must not be null");

            if (this.eriImsi != null)
                ((IMSIImpl) this.eriImsi).encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, DESTINATION_REF_TAG);
            if (this.origReference != null)
                ((AddressStringImpl) this.origReference).encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, ORIGINATION_REF_TAG);
            ((AddressStringImpl) this.destReference).encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, ERI_MSISDN_TAG);
            if (this.eriVlrNo != null)
                ((AddressStringImpl) this.eriVlrNo).encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, ERI_NLR_NO_TAG);
        } else {

            if (this.destReference != null)
                ((AddressStringImpl) this.destReference).encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, DESTINATION_REF_TAG);

            if (this.origReference != null)
                ((AddressStringImpl) this.origReference).encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, ORIGINATION_REF_TAG);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOS);
        }
    }
}
