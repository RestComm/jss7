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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EMLPPInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
public class ExtSSInfoImpl implements ExtSSInfo, MAPAsnPrimitive {

    public static final String _PrimitiveName = "ExtSSInfo";

    protected static final int _TAG_forwardingInfo = 0;
    protected static final int _TAG_callBarringInfo = 1;
    protected static final int _TAG_cugInfo = 2;
    protected static final int _TAG_ssData = 3;
    protected static final int _TAG_emlppInfo = 4;

    private ExtForwInfo forwardingInfo = null;
    private ExtCallBarInfo callBarringInfo = null;
    private CUGInfo cugInfo = null;
    private ExtSSData ssData = null;
    private EMLPPInfo emlppInfo = null;

    public ExtSSInfoImpl() {

    }

    public ExtSSInfoImpl(ExtForwInfo forwardingInfo) {

        this.forwardingInfo = forwardingInfo;
    }

    public ExtSSInfoImpl(ExtCallBarInfo callBarringInfo) {

        this.callBarringInfo = callBarringInfo;
    }

    public ExtSSInfoImpl(CUGInfo cugInfo) {

        this.cugInfo = cugInfo;
    }

    public ExtSSInfoImpl(ExtSSData ssData) {

        this.ssData = ssData;
    }

    public ExtSSInfoImpl(EMLPPInfo emlppInfo) {

        this.emlppInfo = emlppInfo;
    }

    public ExtForwInfo getForwardingInfo() {
        return this.forwardingInfo;
    }

    public ExtCallBarInfo getCallBarringInfo() {
        return this.callBarringInfo;
    }

    public CUGInfo getCugInfo() {
        return this.cugInfo;
    }

    public ExtSSData getSsData() {
        return this.ssData;
    }

    public EMLPPInfo getEmlppInfo() {
        return this.emlppInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        if (forwardingInfo != null) {
            return _TAG_forwardingInfo;
        } else if (callBarringInfo != null) {
            return _TAG_callBarringInfo;
        } else if (cugInfo != null) {
            return _TAG_cugInfo;
        } else if (ssData != null) {
            return _TAG_ssData;
        } else if (emlppInfo != null) {
            return _TAG_emlppInfo;
        } else {
            throw new MAPException("No of choices are supplied");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
     */
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll( org.mobicents.protocols.asn.AsnInputStream)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData (org.mobicents.protocols.asn.AsnInputStream,
     * int)
     */
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
        this.cugInfo = null;
        this.ssData = null;
        this.emlppInfo = null;

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive())
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": bad tag class or is primitive: TagClass=" + ais.getTagClass(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        switch (ais.getTag()) {
            case _TAG_forwardingInfo:
                this.forwardingInfo = new ExtForwInfoImpl();
                ((ExtForwInfoImpl) this.forwardingInfo).decodeData(ais, length);
                break;
            case _TAG_callBarringInfo:
                this.callBarringInfo = new ExtCallBarInfoImpl();
                ((ExtCallBarInfoImpl) this.callBarringInfo).decodeData(ais, length);
                break;
            case _TAG_cugInfo:
                this.cugInfo = new CUGInfoImpl();
                ((CUGInfoImpl) this.cugInfo).decodeData(ais, length);
                break;
            case _TAG_ssData:
                this.ssData = new ExtSSDataImpl();
                ((ExtSSDataImpl) this.ssData).decodeData(ais, length);
                break;
            case _TAG_emlppInfo:
                this.emlppInfo = new EMLPPInfoImpl();
                ((EMLPPInfoImpl) this.emlppInfo).decodeData(ais, length);
                break;

            default:
                throw new MAPParsingComponentException("Error while " + _PrimitiveName + ": bad tag: " + ais.getTag(),
                        MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream,
     * int, int)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        int cnt = 0;
        if (this.forwardingInfo != null)
            cnt++;
        if (this.callBarringInfo != null)
            cnt++;
        if (this.cugInfo != null)
            cnt++;
        if (this.ssData != null)
            cnt++;
        if (this.emlppInfo != null)
            cnt++;

        if (cnt != 1)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": one and only one choice is required.");

        if (this.forwardingInfo != null) {
            ((ExtForwInfoImpl) this.forwardingInfo).encodeData(asnOs);
            return;
        }

        if (this.callBarringInfo != null) {
            ((ExtCallBarInfoImpl) this.callBarringInfo).encodeData(asnOs);
            return;
        }

        if (this.cugInfo != null) {
            ((CUGInfoImpl) this.cugInfo).encodeData(asnOs);
            return;
        }

        if (this.ssData != null) {
            ((ExtSSDataImpl) this.ssData).encodeData(asnOs);
            return;
        }

        if (this.emlppInfo != null) {
            ((EMLPPInfoImpl) this.emlppInfo).encodeData(asnOs);
            return;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

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

        if (this.cugInfo != null) {
            sb.append("cugInfo=");
            sb.append(this.cugInfo.toString());
            sb.append(", ");
        }

        if (this.ssData != null) {
            sb.append("ssData=");
            sb.append(this.ssData.toString());
            sb.append(", ");
        }

        if (this.emlppInfo != null) {
            sb.append("emlppInfo=");
            sb.append(this.emlppInfo.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
