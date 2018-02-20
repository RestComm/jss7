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

package org.restcomm.protocols.ss7.map.service.supplementary;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CCBSFeature;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.SequenceBase;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;

/**
*
* @author sergey vetyutnev
*
*/
public class CCBSFeatureImpl extends SequenceBase implements CCBSFeature {
    public static final int _TAG_ccbsIndex = 0;
    public static final int _TAG_bSubscriberNumber = 1;
    public static final int _TAG_bSubscriberSubaddress = 2;
    public static final int _TAG_basicServiceGroup = 3;

    private Integer ccbsIndex;
    private ISDNAddressString bSubscriberNumber;
    private ISDNAddressString bSubscriberSubaddress;
    private BasicServiceCode basicServiceCode;

    public CCBSFeatureImpl() {
        super("CCBSFeature");
    }

    public CCBSFeatureImpl(Integer ccbsIndex, ISDNAddressString bSubscriberNumber, ISDNAddressString bSubscriberSubaddress, BasicServiceCode basicServiceCode) {
        super("CCBSFeature");
        this.ccbsIndex = ccbsIndex;
        this.bSubscriberNumber = bSubscriberNumber;
        this.bSubscriberSubaddress = bSubscriberSubaddress;
        this.basicServiceCode = basicServiceCode;
    }

    @Override
    public Integer getCcbsIndex() {
        return ccbsIndex;
    }

    @Override
    public ISDNAddressString getBSubscriberNumber() {
        return bSubscriberNumber;
    }

    @Override
    public ISDNAddressString getBSubscriberSubaddress() {
        return bSubscriberSubaddress;
    }

    @Override
    public BasicServiceCode getBasicServiceCode() {
        return basicServiceCode;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.ccbsIndex = null;
        this.bSubscriberNumber = null;
        this.bSubscriberSubaddress = null;
        this.basicServiceCode = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                switch (tag) {
                case _TAG_ccbsIndex:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " ccbsIndex: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.ccbsIndex = (int) ais.readInteger();
                    break;
                case _TAG_bSubscriberNumber:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " bSubscriberNumber: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.bSubscriberNumber = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.bSubscriberNumber).decodeAll(ais);
                    break;
                case _TAG_bSubscriberSubaddress:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " bSubscriberSubaddress: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.bSubscriberSubaddress = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.bSubscriberSubaddress).decodeAll(ais);
                    break;
                case _TAG_basicServiceGroup:
                    AsnInputStream ais2 = ais.readSequenceStream();
                    ais2.readTag();
                    this.basicServiceCode = new BasicServiceCodeImpl();
                    ((BasicServiceCodeImpl) this.basicServiceCode).decodeAll(ais2);
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
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            if (this.ccbsIndex != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ccbsIndex, this.ccbsIndex);
            if (this.bSubscriberNumber != null)
                ((ISDNAddressStringImpl) this.bSubscriberNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_bSubscriberNumber);
            if (this.bSubscriberSubaddress != null)
                ((ISDNAddressStringImpl) this.bSubscriberSubaddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_bSubscriberSubaddress);
            if (this.basicServiceCode != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_basicServiceGroup);
                int pos = asnOs.StartContentDefiniteLength();
                ((BasicServiceCodeImpl) this.basicServiceCode).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.ccbsIndex != null) {
            sb.append("ccbsIndex=");
            sb.append(this.ccbsIndex);
            sb.append(", ");
        }
        if (this.bSubscriberNumber != null) {
            sb.append("bSubscriberNumber=");
            sb.append(this.bSubscriberNumber);
            sb.append(", ");
        }
        if (this.bSubscriberSubaddress != null) {
            sb.append("bSubscriberSubaddress=");
            sb.append(this.bSubscriberSubaddress);
            sb.append(", ");
        }
        if (this.basicServiceCode != null) {
            sb.append("basicServiceCode=");
            sb.append(this.basicServiceCode);
            sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }

}
