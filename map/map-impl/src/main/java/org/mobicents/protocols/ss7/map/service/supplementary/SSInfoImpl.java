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
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CallBarringInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSData;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
*
* @author sergey vetyutnev
*
*/
public class SSInfoImpl implements SSInfo, MAPAsnPrimitive {
    public static final int _TAG_forwardingInfo = 0;
    public static final int _TAG_callBarringInfo = 1;
    public static final int _TAG_ssData = 3;

    public static final String _PrimitiveName = "SSInfo";

    private ForwardingInfo forwardingInfo;
    private CallBarringInfo callBarringInfo;
    private SSData ssData;

    public SSInfoImpl() {
    }

    public SSInfoImpl(ForwardingInfo forwardingInfo) {
        this.forwardingInfo = forwardingInfo;
    }

    public SSInfoImpl(CallBarringInfo callBarringInfo) {
        this.callBarringInfo = callBarringInfo;
    }

    public SSInfoImpl(SSData ssData) {
        this.ssData = ssData;
    }


    @Override
    public ForwardingInfo getForwardingInfo() {
        return forwardingInfo;
    }

    @Override
    public CallBarringInfo getCallBarringInfo() {
        return callBarringInfo;
    }

    @Override
    public SSData getSsData() {
        return ssData;
    }

    @Override
    public int getTag() throws MAPException {
        if (forwardingInfo != null) {
            return _TAG_forwardingInfo;
        } else if (callBarringInfo != null) {
            return _TAG_callBarringInfo;
        } else if (ssData != null) {
            return _TAG_ssData;
        } else {
            throw new MAPException("No of choices are supplied");
        }
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
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
        this.forwardingInfo = null;
        this.callBarringInfo = null;
        this.ssData = null;

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive())
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tag class or is primitive: TagClass=" + ais.getTagClass(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        switch (ais.getTag()) {
        case _TAG_forwardingInfo:
            this.forwardingInfo = new ForwardingInfoImpl();
            ((ForwardingInfoImpl) this.forwardingInfo).decodeData(ais, length);
            break;
        case _TAG_callBarringInfo:
            this.callBarringInfo = new CallBarringInfoImpl();
            ((CallBarringInfoImpl) this.callBarringInfo).decodeData(ais, length);
            break;
        case _TAG_ssData:
            this.ssData = new SSDataImpl();
            ((SSDataImpl) this.ssData).decodeData(ais, length);
            break;

        default:
            throw new MAPParsingComponentException("Error while " + _PrimitiveName + ": bad tag: " + ais.getTag(),
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
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
        int cnt = 0;
        if (forwardingInfo != null)
            cnt++;
        if (callBarringInfo != null)
            cnt++;
        if (ssData != null)
            cnt++;

        if (cnt == 0)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": no option is set.");
        if (cnt > 1)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": more than 1 option is set.");

        if (this.forwardingInfo != null)
            ((ForwardingInfoImpl) this.forwardingInfo).encodeData(asnOs);
        if (this.callBarringInfo != null)
            ((CallBarringInfoImpl) this.callBarringInfo).encodeData(asnOs);
        if (this.ssData != null)
            ((SSDataImpl) this.ssData).encodeData(asnOs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.forwardingInfo != null) {
            sb.append("forwardingInfo=");
            sb.append(this.forwardingInfo.toString());
            sb.append(", ");
        }
        if (this.callBarringInfo != null) {
            sb.append("callBarringInfo=");
            sb.append(this.callBarringInfo.toString());
            sb.append(", ");
        }
        if (this.ssData != null) {
            sb.append("ssData=");
            sb.append(this.ssData.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
