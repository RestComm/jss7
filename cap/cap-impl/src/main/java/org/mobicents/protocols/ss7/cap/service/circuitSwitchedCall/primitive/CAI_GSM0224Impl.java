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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAI_GSM0224;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAI_GSM0224Impl implements CAI_GSM0224, CAPAsnPrimitive {

    public static final int _ID_e1 = 0;
    public static final int _ID_e2 = 1;
    public static final int _ID_e3 = 2;
    public static final int _ID_e4 = 3;
    public static final int _ID_e5 = 4;
    public static final int _ID_e6 = 5;
    public static final int _ID_e7 = 6;

    public static final String _PrimitiveName = "CAI_GSM0224";

    private Integer e1;
    private Integer e2;
    private Integer e3;
    private Integer e4;
    private Integer e5;
    private Integer e6;
    private Integer e7;

    public CAI_GSM0224Impl() {
    }

    public CAI_GSM0224Impl(Integer e1, Integer e2, Integer e3, Integer e4, Integer e5, Integer e6, Integer e7) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
        this.e4 = e4;
        this.e5 = e5;
        this.e6 = e6;
        this.e7 = e7;
    }

    @Override
    public Integer getE1() {
        return e1;
    }

    @Override
    public Integer getE2() {
        return e2;
    }

    @Override
    public Integer getE3() {
        return e3;
    }

    @Override
    public Integer getE4() {
        return e4;
    }

    @Override
    public Integer getE5() {
        return e5;
    }

    @Override
    public Integer getE6() {
        return e6;
    }

    @Override
    public Integer getE7() {
        return e7;
    }

    @Override
    public int getTag() throws CAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.e1 = null;
        this.e2 = null;
        this.e3 = null;
        this.e4 = null;
        this.e5 = null;
        this.e6 = null;
        this.e7 = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_e1:
                        this.e1 = (int) ais.readInteger();
                        break;
                    case _ID_e2:
                        this.e2 = (int) ais.readInteger();
                        break;
                    case _ID_e3:
                        this.e3 = (int) ais.readInteger();
                        break;
                    case _ID_e4:
                        this.e4 = (int) ais.readInteger();
                        break;
                    case _ID_e5:
                        this.e5 = (int) ais.readInteger();
                        break;
                    case _ID_e6:
                        this.e6 = (int) ais.readInteger();
                        break;
                    case _ID_e7:
                        this.e7 = (int) ais.readInteger();
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());

    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream aos) throws CAPException {

        try {
            if (this.e1 != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_e1, this.e1);
            if (this.e2 != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_e2, this.e2);
            if (this.e3 != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_e3, this.e3);
            if (this.e4 != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_e4, this.e4);
            if (this.e5 != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_e5, this.e5);
            if (this.e6 != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_e6, this.e6);
            if (this.e7 != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_e7, this.e7);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.e1 != null) {
            sb.append("e1=");
            sb.append(this.e1);
        }
        if (this.e2 != null) {
            sb.append(", e2=");
            sb.append(this.e2);
        }
        if (this.e3 != null) {
            sb.append(", e3=");
            sb.append(this.e3);
        }
        if (this.e4 != null) {
            sb.append(", e4=");
            sb.append(this.e4);
        }
        if (this.e5 != null) {
            sb.append(", e5=");
            sb.append(this.e5);
        }
        if (this.e6 != null) {
            sb.append(", e6=");
            sb.append(this.e6);
        }
        if (this.e7 != null) {
            sb.append(", e7=");
            sb.append(this.e7);
        }

        sb.append("]");

        return sb.toString();
    }
}
