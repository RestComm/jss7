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

package org.mobicents.protocols.ss7.map.service.mobility.authentication;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.EpcAv;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class EpcAvImpl implements EpcAv, MAPAsnPrimitive {

    public static final String _PrimitiveName = "EpcAv";

    private byte[] rand;
    private byte[] xres;
    private byte[] autn;
    private byte[] kasme;
    private MAPExtensionContainer extensionContainer;

    public EpcAvImpl() {
    }

    public EpcAvImpl(byte[] rand, byte[] xres, byte[] autn, byte[] kasme, MAPExtensionContainer extensionContainer) {
        this.rand = rand;
        this.xres = xres;
        this.autn = autn;
        this.kasme = kasme;
        this.extensionContainer = extensionContainer;
    }

    public byte[] getRand() {
        return rand;
    }

    public byte[] getXres() {
        return xres;
    }

    public byte[] getAutn() {
        return autn;
    }

    public byte[] getKasme() {
        return kasme;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
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
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

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

        this.rand = null;
        this.xres = null;
        this.autn = null;
        this.kasme = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    // rand
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".rand: Parameter 0 bad tag or tag class or is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.rand = ais.readOctetString();
                    if (this.rand.length != 16)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".rand: Bad field length: 16 is needed, found: " + this.rand.length,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    break;

                case 1:
                    // xres
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".sres: Parameter 1 bad tag or tag class or is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.xres = ais.readOctetString();
                    if (this.xres.length < 4 || this.xres.length > 16)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".xres: Bad field length: from 4 to 16 is needed, found: " + this.xres.length,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    break;

                case 2:
                    // autn
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".autn: Parameter 2 bad tag or tag class or is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.autn = ais.readOctetString();
                    if (this.autn.length != 16)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".autn: Bad field length: 16 is needed, found: " + this.autn.length,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    break;

                case 3:
                    // kasme
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".kasme: Parameter 2 bad tag or tag class or is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.kasme = ais.readOctetString();
                    if (this.kasme.length != 32)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".kasme: Bad field length: 32 is needed, found: " + this.kasme.length,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    break;

                default:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        ais.advanceElement();
                    } else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

                        switch (tag) {
                            case Tag.SEQUENCE:
                                // extensionContainer
                                if (ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".extensionContainer: Parameter extensionContainer is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                this.extensionContainer = new MAPExtensionContainerImpl();
                                ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                                break;

                            default:
                                ais.advanceElement();
                                break;
                        }
                    } else {

                        ais.advanceElement();
                    }
                    break;
            }

            num++;
        }

        if (num < 4)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Needs at least 4 mandatory parameters, found " + num,
                    MAPParsingComponentExceptionReason.MistypedParameter);
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

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

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.rand == null || this.xres == null || this.autn == null || this.kasme == null) {
            throw new MAPException("rand, xres, autn and kasme fields must not be null");
        }
        if (this.rand.length != 16)
            throw new MAPException("Wrong rand field length: must be 16, found " + this.rand.length);
        if (this.xres.length < 4 || this.xres.length > 16)
            throw new MAPException("Wrong xres field length: must be from 4 to 16, found " + this.xres.length);
        if (this.autn.length != 16)
            throw new MAPException("Wrong autn field length: must be 16, found " + this.autn.length);
        if (this.kasme.length != 32)
            throw new MAPException("Wrong kasme field length: must be 32, found " + this.kasme.length);

        try {
            asnOs.writeOctetString(this.rand);
            asnOs.writeOctetString(this.xres);
            asnOs.writeOctetString(this.autn);
            asnOs.writeOctetString(this.kasme);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
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

        if (this.rand != null) {
            sb.append("rand=[");
            sb.append(printDataArr(this.rand));
            sb.append("], ");
        }
        if (this.xres != null) {
            sb.append("xres=[");
            sb.append(printDataArr(this.xres));
            sb.append("], ");
        }
        if (this.autn != null) {
            sb.append("autn=[");
            sb.append(printDataArr(this.autn));
            sb.append("]");
        }
        if (this.kasme != null) {
            sb.append("kasme=[");
            sb.append(printDataArr(this.kasme));
            sb.append("]");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=[");
            sb.append(this.extensionContainer);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }

    private String printDataArr(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int b : arr) {
            sb.append(b);
            sb.append(", ");
        }

        return sb.toString();
    }
}
