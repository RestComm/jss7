/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.service.supplementary;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.RegisterSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSInfo;

/**
*
* @author sergey vetyutnev
*
*/
public class RegisterSSResponseImpl extends SupplementaryMessageImpl implements RegisterSSResponse {

    public static final String _PrimitiveName = "RegisterSSResponse";

    private SSInfo ssInfo;

    public RegisterSSResponseImpl() {
    }

    public RegisterSSResponseImpl(SSInfo ssInfo) {
        this.ssInfo = ssInfo;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.registerSS_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.registerSS;
    }

    @Override
    public SSInfo getSsInfo() {
        return ssInfo;
    }

    @Override
    public int getTag() throws MAPException {
        if (ssInfo != null)
            return ((SSInfoImpl) ssInfo).getTag();

        throw new MAPException("ssInfo is not defined");
    }

    @Override
    public int getTagClass() {
        if (ssInfo != null)
            return ((SSInfoImpl) ssInfo).getTagClass();

        return 0;
    }

    @Override
    public boolean getIsPrimitive() {
        if (ssInfo != null)
            return ((SSInfoImpl) ssInfo).getIsPrimitive();

        return false;
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

    private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.ssInfo = null;

        this.ssInfo = new SSInfoImpl();
        ((SSInfoImpl) this.ssInfo).decodeData(ais, length);
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
        if (ssInfo == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": ssInfo parameter is not defined.");

        if (this.ssInfo != null)
            ((SSInfoImpl) this.ssInfo).encodeData(asnOs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.ssInfo != null) {
            sb.append("ssInfo=");
            sb.append(this.ssInfo);
        }

        sb.append("]");

        return sb.toString();
    }

}
