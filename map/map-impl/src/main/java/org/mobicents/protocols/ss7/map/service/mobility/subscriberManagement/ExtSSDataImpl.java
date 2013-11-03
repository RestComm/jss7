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
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSSubscriptionOptionImpl;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
public class ExtSSDataImpl extends SequenceBase implements ExtSSData {

    private static final int _TAG_ss_Status = 4;
    private static final int _TAG_extensionContainer = 5;

    private SSCode ssCode = null;
    private ExtSSStatus ssStatus = null;
    private SSSubscriptionOption ssSubscriptionOption = null;
    private ArrayList<ExtBasicServiceCode> basicServiceGroupList = null;
    private MAPExtensionContainer extensionContainer = null;

    public ExtSSDataImpl() {
        super("ExtSSData");
    }

    /**
     *
     */
    public ExtSSDataImpl(SSCode ssCode, ExtSSStatus ssStatus, SSSubscriptionOption ssSubscriptionOption,
            ArrayList<ExtBasicServiceCode> basicServiceGroupList, MAPExtensionContainer extensionContainer) {
        super("ExtSSData");

        this.ssCode = ssCode;
        this.ssStatus = ssStatus;
        this.ssSubscriptionOption = ssSubscriptionOption;
        this.basicServiceGroupList = basicServiceGroupList;
        this.extensionContainer = extensionContainer;
    }

    public SSCode getSsCode() {
        return this.ssCode;
    }

    public ExtSSStatus getSsStatus() {
        return this.ssStatus;
    }

    public SSSubscriptionOption getSSSubscriptionOption() {
        return this.ssSubscriptionOption;
    }

    public ArrayList<ExtBasicServiceCode> getBasicServiceGroupList() {
        return this.basicServiceGroupList;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        ExtBasicServiceCode serviceItem = null;
        this.ssCode = null;
        this.ssStatus = null;
        this.ssSubscriptionOption = null;
        this.basicServiceGroupList = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0: // ssCode
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || tag != Tag.STRING_OCTET || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ssCode: bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.ssCode = new SSCodeImpl();
                    ((SSCodeImpl) this.ssCode).decodeAll(ais);
                    break;

                case 1: // ss-Status
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || tag != _TAG_ss_Status || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + "._TAG_ss_Status: bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.ssStatus = new ExtSSStatusImpl();
                    ((ExtSSStatusImpl) this.ssStatus).decodeAll(ais);
                    break;

                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_CONTEXT_SPECIFIC:
                            switch (tag) {
                                case SSSubscriptionOptionImpl._TAG_overrideCategory:
                                case SSSubscriptionOptionImpl._TAG_cliRestrictionOption:
                                    this.ssSubscriptionOption = new SSSubscriptionOptionImpl();
                                    ((SSSubscriptionOptionImpl) this.ssSubscriptionOption).decodeAll(ais);
                                    break;

                                case _TAG_extensionContainer:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".extensionContainer: is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.extensionContainer = new MAPExtensionContainerImpl();
                                    ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                                    break;

                                default:
                                    ais.advanceElement();
                                    break;
                            }
                            break;

                        case Tag.CLASS_UNIVERSAL: // basicServiceGroupList
                            switch (tag) {
                                case Tag.SEQUENCE:
                                    AsnInputStream ais2 = ais.readSequenceStream();
                                    this.basicServiceGroupList = new ArrayList<ExtBasicServiceCode>();
                                    while (true) {
                                        if (ais2.available() == 0)
                                            break;

                                        ais2.readTag();
                                        serviceItem = new ExtBasicServiceCodeImpl();
                                        ((ExtBasicServiceCodeImpl) serviceItem).decodeAll(ais2);
                                        this.basicServiceGroupList.add(serviceItem);
                                    }
                                    if (this.basicServiceGroupList.size() < 1 && this.basicServiceGroupList.size() > 32) {
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ": Parameter basicServiceGroupList size must be from 1 to 32, found: "
                                                + this.basicServiceGroupList.size(),
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    }
                                    break;
                                default:
                                    ais.advanceElement();
                                    break;
                            }
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;
            }

            num++;
        }

        if (this.ssCode == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": ssCode required.",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (this.ssStatus == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": ssStatus required.",
                    MAPParsingComponentExceptionReason.MistypedParameter);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.ssCode == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": ssCode required.");

        if (this.ssStatus == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": ssStatus required.");

        if (this.basicServiceGroupList != null
                && (this.basicServiceGroupList.size() < 1 || this.basicServiceGroupList.size() > 32)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter basicServiceGroupList size must be from 1 to 32, found: "
                    + this.basicServiceGroupList.size());
        }

        try {

            ((SSCodeImpl) this.ssCode).encodeAll(asnOs);

            ((ExtSSStatusImpl) this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ss_Status);

            if (this.ssSubscriptionOption != null) {
                ((SSSubscriptionOptionImpl) this.ssSubscriptionOption).encodeAll(asnOs);
            }

            if (this.basicServiceGroupList != null) {
                asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
                int pos = asnOs.StartContentDefiniteLength();
                for (ExtBasicServiceCode serviceItem : this.basicServiceGroupList) {
                    ((ExtBasicServiceCodeImpl) serviceItem).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode.toString());
            sb.append(", ");
        }

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus.toString());
            sb.append(", ");
        }

        if (this.ssSubscriptionOption != null) {
            sb.append("ssSubscriptionOption=");
            sb.append(this.ssSubscriptionOption.toString());
            sb.append(", ");
        }

        if (this.basicServiceGroupList != null) {
            sb.append("basicServiceGroupList=[");
            boolean firstItem = true;
            for (ExtBasicServiceCode be : this.basicServiceGroupList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
