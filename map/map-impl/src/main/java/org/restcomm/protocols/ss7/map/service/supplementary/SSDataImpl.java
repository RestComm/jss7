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
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSData;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.restcomm.protocols.ss7.map.primitives.SequenceBase;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;

/**
*
* @author sergey vetyutnev
*
*/
public class SSDataImpl extends SequenceBase implements SSData {
    public static final int _TAG_ss_Status = 4;
    public static final int _TAG_nbrUser = 5;

    private SSCode ssCode;
    private SSStatus ssStatus;
    private SSSubscriptionOption ssSubscriptionOption;
    private ArrayList<BasicServiceCode> basicServiceGroupList;
    private EMLPPPriority defaultPriority;
    private Integer nbrUser;

    public SSDataImpl() {
        super("SSData");
    }

    public SSDataImpl(SSCode ssCode, SSStatus ssStatus, SSSubscriptionOption ssSubscriptionOption, ArrayList<BasicServiceCode> basicServiceGroupList,
            EMLPPPriority defaultPriority, Integer nbrUser) {
        super("SSData");

        this.ssCode = ssCode;
        this.ssStatus = ssStatus;
        this.ssSubscriptionOption = ssSubscriptionOption;
        this.basicServiceGroupList = basicServiceGroupList;
        this.defaultPriority = defaultPriority;
        this.nbrUser = nbrUser;
    }


    @Override
    public SSCode getSsCode() {
        return ssCode;
    }

    @Override
    public SSStatus getSsStatus() {
        return ssStatus;
    }

    @Override
    public SSSubscriptionOption getSsSubscriptionOption() {
        return ssSubscriptionOption;
    }

    @Override
    public ArrayList<BasicServiceCode> getBasicServiceGroupList() {
        return basicServiceGroupList;
    }

    @Override
    public EMLPPPriority getDefaultPriority() {
        return defaultPriority;
    }

    @Override
    public Integer getNbrUser() {
        return nbrUser;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.ssCode = null;
        this.ssStatus = null;
        this.ssSubscriptionOption = null;
        this.basicServiceGroupList = null;
        this.defaultPriority = null;
        this.nbrUser = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

                switch (tag) {
                case Tag.STRING_OCTET:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " ssCode: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.ssCode = new SSCodeImpl();
                    ((SSCodeImpl) this.ssCode).decodeAll(ais);
                    break;

                case Tag.SEQUENCE:
                    if (ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".basicServiceGroupList: Parameter is primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);

                    AsnInputStream ais2 = ais.readSequenceStream();
                    this.basicServiceGroupList = new ArrayList<BasicServiceCode>();
                    while (true) {
                        if (ais2.available() == 0)
                            break;

                        ais2.readTag();

                        BasicServiceCode basicServiceCode = new BasicServiceCodeImpl();
                        ((BasicServiceCodeImpl) basicServiceCode).decodeAll(ais2);
                        this.basicServiceGroupList.add(basicServiceCode);
                    }
                    if (this.basicServiceGroupList.size() < 1 || this.basicServiceGroupList.size() > 13) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter forwardingFeatureList size must be from 1 to 13, found: " + this.basicServiceGroupList.size(),
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    }
                    break;

                case Tag.INTEGER:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " defaultPriority: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    int i1 = (int) ais.readInteger();
                    this.defaultPriority = EMLPPPriority.getEMLPPPriority(i1);
                    break;

                default:
                    ais.advanceElement();
                    break;
                }

            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                switch (tag) {
                case _TAG_ss_Status:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " ssStatus: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.ssStatus = new SSStatusImpl();
                    ((SSStatusImpl) this.ssStatus).decodeAll(ais);
                    break;
                case SSSubscriptionOptionImpl._TAG_cliRestrictionOption:
                case SSSubscriptionOptionImpl._TAG_overrideCategory:
                    this.ssSubscriptionOption = new SSSubscriptionOptionImpl();
                    ((SSSubscriptionOptionImpl) this.ssSubscriptionOption).decodeAll(ais);
                    break;

                case _TAG_nbrUser:
                    if (!ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " nbrUser: Parameter is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.nbrUser = (int) ais.readInteger();
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
            if (this.ssCode != null)
                ((SSCodeImpl) this.ssCode).encodeAll(asnOs);
            if (this.ssStatus != null)
                ((SSStatusImpl) this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ss_Status);
            if (this.ssSubscriptionOption != null)
                ((SSSubscriptionOptionImpl) this.ssSubscriptionOption).encodeAll(asnOs);

            if (this.basicServiceGroupList != null) {
                try {
                    asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
                    int pos = asnOs.StartContentDefiniteLength();
                    for (BasicServiceCode item : this.basicServiceGroupList) {
                        ((BasicServiceCodeImpl) item).encodeAll(asnOs);
                    }
                    asnOs.FinalizeContent(pos);
                } catch (AsnException e) {
                    throw new MAPException("AsnException when encoding " + _PrimitiveName + ".callBarringFeatureList: " + e.getMessage(), e);
                }
            }

            if (this.defaultPriority != null)
                asnOs.writeInteger(this.defaultPriority.getCode());

            if (this.nbrUser != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_nbrUser, nbrUser);
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

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode);
            sb.append(", ");
        }
        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus);
            sb.append(", ");
        }
        if (this.ssSubscriptionOption != null) {
            sb.append("ssSubscriptionOption=");
            sb.append(this.ssSubscriptionOption);
            sb.append(", ");
        }

        if (this.basicServiceGroupList != null) {
            sb.append("basicServiceGroupList=[");
            boolean firstItem = true;
            for (BasicServiceCode be : this.basicServiceGroupList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }
        if (this.defaultPriority != null) {
            sb.append("defaultPriority=");
            sb.append(this.defaultPriority);
            sb.append(", ");
        }
        if (this.nbrUser != null) {
            sb.append("nbrUser=");
            sb.append(this.nbrUser);
            sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }

}
