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

package org.mobicents.protocols.ss7.map.errors;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessagePwRegistrationFailure;
import org.mobicents.protocols.ss7.map.api.errors.PWRegistrationFailureCause;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPErrorMessagePwRegistrationFailureImpl extends MAPErrorMessageImpl implements
        MAPErrorMessagePwRegistrationFailure {

    private PWRegistrationFailureCause pwRegistrationFailureCause;

    protected String _PrimitiveName = "MAPErrorMessagePwRegistrationFailure";

    public MAPErrorMessagePwRegistrationFailureImpl(PWRegistrationFailureCause pwRegistrationFailureCause) {
        super((long) MAPErrorCode.pwRegistrationFailure);

        this.pwRegistrationFailureCause = pwRegistrationFailureCause;
    }

    public MAPErrorMessagePwRegistrationFailureImpl() {
        super((long) MAPErrorCode.pwRegistrationFailure);
    }

    public boolean isEmPwRegistrationFailure() {
        return true;
    }

    public MAPErrorMessagePwRegistrationFailure getEmPwRegistrationFailure() {
        return this;
    }

    @Override
    public PWRegistrationFailureCause getPWRegistrationFailureCause() {
        return pwRegistrationFailureCause;
    }

    @Override
    public void setPWRegistrationFailureCause(PWRegistrationFailureCause val) {
        pwRegistrationFailureCause = val;
    }

    @Override
    public int getTag() throws MAPException {
        return Tag.ENUMERATED;
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

    @Override
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

    private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {

        if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || localAis.getTag() != Tag.ENUMERATED || !localAis.isTagPrimitive())
            throw new MAPParsingComponentException("Error decoding " + _PrimitiveName
                    + ": bad tag class or tag or parameter is primitive", MAPParsingComponentExceptionReason.MistypedParameter);

        int i1 = (int) localAis.readIntegerData(length);
        this.pwRegistrationFailureCause = PWRegistrationFailureCause.getInstance(i1);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
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

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.pwRegistrationFailureCause == null)
            throw new MAPException("Parameter pwRegistrationFailureCause must not be null");

        try {
            asnOs.writeIntegerData(this.pwRegistrationFailureCause.getCode());
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.pwRegistrationFailureCause != null)
            sb.append("pwRegistrationFailureCause=" + this.pwRegistrationFailureCause.toString());
        sb.append("]");

        return sb.toString();
    }

}
