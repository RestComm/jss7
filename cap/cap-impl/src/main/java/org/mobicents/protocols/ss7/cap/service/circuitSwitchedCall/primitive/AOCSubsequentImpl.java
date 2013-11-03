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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCSubsequent;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAI_GSM0224;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class AOCSubsequentImpl implements AOCSubsequent, CAPAsnPrimitive {

    public static final int _ID_cAI_GSM0224 = 0;
    public static final int _ID_tariffSwitchInterval = 1;

    public static final String _PrimitiveName = "AOCSubsequent";

    private CAI_GSM0224 cai_GSM0224;
    private Integer tariffSwitchInterval;

    public AOCSubsequentImpl() {
    }

    public AOCSubsequentImpl(CAI_GSM0224 cai_GSM0224, Integer tariffSwitchInterval) {
        this.cai_GSM0224 = cai_GSM0224;
        this.tariffSwitchInterval = tariffSwitchInterval;
    }

    @Override
    public CAI_GSM0224 getCAI_GSM0224() {
        return cai_GSM0224;
    }

    @Override
    public Integer getTariffSwitchInterval() {
        return tariffSwitchInterval;
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

        this.cai_GSM0224 = null;
        this.tariffSwitchInterval = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_cAI_GSM0224:
                        this.cai_GSM0224 = new CAI_GSM0224Impl();
                        ((CAI_GSM0224Impl) this.cai_GSM0224).decodeAll(ais);
                        break;
                    case _ID_tariffSwitchInterval:
                        this.tariffSwitchInterval = (int) ais.readInteger();
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.cai_GSM0224 == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": cai_GSM0224 is mandatory but not found", CAPParsingComponentExceptionReason.MistypedParameter);
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

        if (this.cai_GSM0224 == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": cai_GSM0224 must not be null");

        try {
            ((CAI_GSM0224Impl) this.cai_GSM0224).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_cAI_GSM0224);

            if (this.tariffSwitchInterval != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_tariffSwitchInterval, this.tariffSwitchInterval);
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

        if (this.cai_GSM0224 != null) {
            sb.append("cai_GSM0224=");
            sb.append(cai_GSM0224.toString());
        }
        if (this.tariffSwitchInterval != null) {
            sb.append(", tariffSwitchInterval=");
            sb.append(tariffSwitchInterval);
        }

        sb.append("]");

        return sb.toString();
    }
}
