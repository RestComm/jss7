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
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AuthenticationTripletImpl implements AuthenticationTriplet, MAPAsnPrimitive {

    public static final String _PrimitiveName = "AuthenticationTriplet";

    private byte[] rand;
    private byte[] sres;
    private byte[] kc;

    public AuthenticationTripletImpl() {
    }

    public AuthenticationTripletImpl(byte[] rand, byte[] sres, byte[] kc) {
        this.rand = rand;
        this.sres = sres;
        this.kc = kc;
    }

    public byte[] getRand() {
        return rand;
    }

    public byte[] getSres() {
        return sres;
    }

    public byte[] getKc() {
        return kc;
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
        this.sres = null;
        this.kc = null;

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
                    // sres
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".sres: Parameter 1 bad tag or tag class or is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.sres = ais.readOctetString();
                    if (this.sres.length != 4)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".sres: Bad field length: 4 is needed, found: " + this.sres.length,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    break;

                case 2:
                    // kc
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".kc: Parameter 2 bad tag or tag class or is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.kc = ais.readOctetString();
                    if (this.kc.length != 8)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".kc: Bad field length: 8 is needed, found: " + this.kc.length,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    break;
            }

            num++;
        }

        if (num < 3)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Needs at least 3 mandatory parameters, found " + num,
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

        if (this.rand == null || this.sres == null || this.kc == null) {
            throw new MAPException("rand, sres and kc fields must not be null");
        }
        if (this.rand.length != 16)
            throw new MAPException("Wrong rand field length: must be 16, found " + this.rand.length);
        if (this.sres.length != 4)
            throw new MAPException("Wrong sres field length: must be 4, found " + this.rand.length);
        if (this.kc.length != 8)
            throw new MAPException("Wrong kc field length: must be 8, found " + this.rand.length);

        try {
            asnOs.writeOctetString(this.rand);
            asnOs.writeOctetString(this.sres);
            asnOs.writeOctetString(this.kc);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AuthenticationTriplet [");

        if (this.rand != null) {
            sb.append("rand=[");
            sb.append(printDataArr(this.rand));
            sb.append("], ");
        }
        if (this.sres != null) {
            sb.append("sres=[");
            sb.append(printDataArr(this.sres));
            sb.append("], ");
        }
        if (this.kc != null) {
            sb.append("kc=[");
            sb.append(printDataArr(this.kc));
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
