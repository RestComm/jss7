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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCBeforeAnswer;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCSubsequent;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELSCIBillingChargingCharacteristicsAlt;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SCIBillingChargingCharacteristicsImpl implements SCIBillingChargingCharacteristics, CAPAsnPrimitive {

    public static final int _ID_aOCBeforeAnswer = 0;
    public static final int _ID_aOCAfterAnswer = 1;
    public static final int _ID_aOC_extension = 2;

    public static final String _PrimitiveName = "SCIBillingChargingCharacteristics";

    private AOCBeforeAnswer aocBeforeAnswer;
    private AOCSubsequent aocSubsequent;
    private CAMELSCIBillingChargingCharacteristicsAlt aocExtension;

    public SCIBillingChargingCharacteristicsImpl() {
    }

    public SCIBillingChargingCharacteristicsImpl(AOCBeforeAnswer aocBeforeAnswer) {
        this.aocBeforeAnswer = aocBeforeAnswer;
    }

    public SCIBillingChargingCharacteristicsImpl(AOCSubsequent aocSubsequent) {
        this.aocSubsequent = aocSubsequent;
    }

    public SCIBillingChargingCharacteristicsImpl(CAMELSCIBillingChargingCharacteristicsAlt aocExtension) {
        this.aocExtension = aocExtension;
    }

    @Override
    public AOCBeforeAnswer getAOCBeforeAnswer() {
        return aocBeforeAnswer;
    }

    @Override
    public AOCSubsequent getAOCSubsequent() {
        return aocSubsequent;
    }

    @Override
    public CAMELSCIBillingChargingCharacteristicsAlt getAOCExtension() {
        return aocExtension;
    }

    @Override
    public int getTag() throws CAPException {
        return Tag.STRING_OCTET;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
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

        this.aocBeforeAnswer = null;
        this.aocSubsequent = null;
        this.aocExtension = null;

        byte[] buf = ansIS.readOctetStringData(length);
        if (buf.length < 2 || buf.length > 255)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": data length must be from 2 to 255, found: " + buf.length,
                    CAPParsingComponentExceptionReason.MistypedParameter);

        AsnInputStream ais = new AsnInputStream(buf);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_aOCBeforeAnswer:
                        this.aocBeforeAnswer = new AOCBeforeAnswerImpl();
                        ((AOCBeforeAnswerImpl) this.aocBeforeAnswer).decodeAll(ais);
                        break;
                    case _ID_aOCAfterAnswer:
                        this.aocSubsequent = new AOCSubsequentImpl();
                        ((AOCSubsequentImpl) this.aocSubsequent).decodeAll(ais);
                        break;
                    case _ID_aOC_extension:
                        this.aocExtension = new CAMELSCIBillingChargingCharacteristicsAltImpl();
                        ((CAMELSCIBillingChargingCharacteristicsAltImpl) this.aocExtension).decodeAll(ais);
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        int cnt = 0;
        if (this.aocBeforeAnswer != null)
            cnt++;
        if (this.aocSubsequent != null)
            cnt++;
        if (this.aocExtension != null)
            cnt++;
        if (cnt != 1)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": only one choice must be present, found choices: " + cnt,
                    CAPParsingComponentExceptionReason.MistypedParameter);
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
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        int cnt = 0;
        if (this.aocBeforeAnswer != null)
            cnt++;
        if (this.aocSubsequent != null)
            cnt++;
        if (this.aocExtension != null)
            cnt++;
        if (cnt != 1)
            throw new CAPException("Error while encoding " + _PrimitiveName
                    + ": only one choice must be present, found choices: " + cnt);

        AsnOutputStream aos = new AsnOutputStream();
        if (this.aocBeforeAnswer != null)
            ((AOCBeforeAnswerImpl) this.aocBeforeAnswer).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_aOCBeforeAnswer);
        if (this.aocSubsequent != null)
            ((AOCSubsequentImpl) this.aocSubsequent).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_aOCAfterAnswer);
        if (this.aocExtension != null)
            ((CAMELSCIBillingChargingCharacteristicsAltImpl) this.aocExtension).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                    _ID_aOC_extension);

        byte[] buf = aos.toByteArray();
        if (buf.length < 2 || buf.length > 255)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": data length must be from 2 to 255, encoded: "
                    + buf.length);

        asnOs.writeOctetStringData(buf);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.aocBeforeAnswer != null) {
            sb.append("aocBeforeAnswer=");
            sb.append(aocBeforeAnswer.toString());
        }
        if (this.aocSubsequent != null) {
            sb.append(" aocSubsequent=");
            sb.append(aocSubsequent.toString());
        }
        if (this.aocExtension != null) {
            sb.append("aocExtension=");
            sb.append(aocExtension.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
