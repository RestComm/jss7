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
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationQuintuplet;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AuthenticationQuintupletImpl implements AuthenticationQuintuplet, MAPAsnPrimitive {

    public static final String _PrimitiveName = "AuthenticationQuintuplet";

    private byte[] rand;
    private byte[] xres;
    private byte[] ck;
    private byte[] ik;
    private byte[] autn;

    public AuthenticationQuintupletImpl() {
    }

    public AuthenticationQuintupletImpl(byte[] rand, byte[] xres, byte[] ck, byte[] ik, byte[] autn) {
        this.rand = rand;
        this.xres = xres;
        this.ck = ck;
        this.ik = ik;
        this.autn = autn;
    }

    public byte[] getRand() {
        return rand;
    }

    public byte[] getXres() {
        return xres;
    }

    public byte[] getCk() {
        return ck;
    }

    public byte[] getIk() {
        return ik;
    }

    public byte[] getAutn() {
        return autn;
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
        this.ck = null;
        this.ik = null;
        this.autn = null;

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
                                + ".xres: Parameter 1 bad tag or tag class or is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.xres = ais.readOctetString();
                    if (this.xres.length < 4 || this.xres.length > 16)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".xres: Bad field length: 4-16 is needed, found: " + this.xres.length,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    break;

                case 2:
                    // ck
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ck: Parameter 2 bad tag or tag class or is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.ck = ais.readOctetString();
                    if (this.ck.length != 16)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ck: Bad field length: 16 is needed, found: " + this.ck.length,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    break;

                case 3:
                    // ik
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ik: Parameter 3 bad tag or tag class or is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.ik = ais.readOctetString();
                    if (this.ik.length != 16)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ik: Bad field length: 16 is needed, found: " + this.ik.length,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    break;

                case 4:
                    // autn
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".autn: Parameter 4 bad tag or tag class or is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.autn = ais.readOctetString();
                    if (this.autn.length != 16)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".autn: Bad field length: 16 is needed, found: " + this.autn.length,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    break;
            }

            num++;
        }

        if (num < 5)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Needs at least 5 mandatory parameters, found " + num,
                    MAPParsingComponentExceptionReason.MistypedParameter);
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.rand == null || this.xres == null || this.ck == null || this.ik == null || this.autn == null) {
            throw new MAPException("rand, xres, ck, ik and autn fields must not be null");
        }
        if (this.rand.length != 16)
            throw new MAPException("Wrong rand field length: must be 16, found " + this.rand.length);
        if (this.xres.length < 4 || this.xres.length > 16)
            throw new MAPException("Wrong xres field length: must be from 4 to 16, found " + this.xres.length);
        if (this.ck.length != 16)
            throw new MAPException("Wrong ck field length: must be 16, found " + this.ck.length);
        if (this.ik.length != 16)
            throw new MAPException("Wrong ik field length: must be 16, found " + this.ik.length);
        if (this.autn.length != 16)
            throw new MAPException("Wrong autn field length: must be 16, found " + this.autn.length);

        try {
            asnOs.writeOctetString(this.rand);
            asnOs.writeOctetString(this.xres);
            asnOs.writeOctetString(this.ck);
            asnOs.writeOctetString(this.ik);
            asnOs.writeOctetString(this.autn);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AuthenticationQuintuplet [");

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
        if (this.ck != null) {
            sb.append("ck=[");
            sb.append(printDataArr(this.ck));
            sb.append("]");
        }
        if (this.ik != null) {
            sb.append("ik=[");
            sb.append(printDataArr(this.ik));
            sb.append("]");
        }
        if (this.autn != null) {
            sb.append("autn=[");
            sb.append(printDataArr(this.autn));
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
