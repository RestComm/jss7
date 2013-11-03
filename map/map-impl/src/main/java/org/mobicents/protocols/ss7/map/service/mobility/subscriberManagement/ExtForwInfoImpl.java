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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;

/**
 * @author daniel bichara
 *
 */
public class ExtForwInfoImpl extends SequenceBase implements ExtForwInfo {

    private static final int _TAG_extensionContainer = 0;

    private SSCode ssCode = null;
    private ArrayList<ExtForwFeature> forwardingFeatureList = null;
    private MAPExtensionContainer extensionContainer = null;

    public ExtForwInfoImpl() {
        super("ExtForwInfo");
    }

    /**
     *
     */
    public ExtForwInfoImpl(SSCode ssCode, ArrayList<ExtForwFeature> forwardingFeatureList,
            MAPExtensionContainer extensionContainer) {
        super("ExtForwInfo");

        this.ssCode = ssCode;
        this.forwardingFeatureList = forwardingFeatureList;
        this.extensionContainer = extensionContainer;
    }

    public SSCode getSsCode() {
        return this.ssCode;
    }

    public ArrayList<ExtForwFeature> getForwardingFeatureList() {
        return this.forwardingFeatureList;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        ExtForwFeature featureItem = null;
        this.ssCode = null;
        this.forwardingFeatureList = null;
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

                case 1: // forwardingFeatureList
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || tag != Tag.SEQUENCE || ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".forwardingFeatureList: Parameter is primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);

                    AsnInputStream ais2 = ais.readSequenceStream();
                    this.forwardingFeatureList = new ArrayList<ExtForwFeature>();
                    while (true) {
                        if (ais2.available() == 0)
                            break;

                        int tag2 = ais2.readTag();
                        if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": bad forwardingFeature tag or tagClass or is primitive ",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        featureItem = new ExtForwFeatureImpl();
                        ((ExtForwFeatureImpl) featureItem).decodeAll(ais2);
                        this.forwardingFeatureList.add(featureItem);
                    }

                    if (this.forwardingFeatureList.size() < 1 || this.forwardingFeatureList.size() > 32) {
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter forwardingFeatureList size must be from 1 to 32, found: "
                                + this.forwardingFeatureList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                    }
                    break;

                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_CONTEXT_SPECIFIC:
                            switch (tag) {
                                case _TAG_extensionContainer:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".extensionContainer: is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.extensionContainer = new MAPExtensionContainerImpl();
                                    ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
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

        if (num < 2)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": ssCode, forwardingFeatureList are required but not found.",
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

        if (this.forwardingFeatureList == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": forwardingFeatureList required.");

        if (this.forwardingFeatureList.size() < 1 || this.forwardingFeatureList.size() > 32) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter forwardingFeatureList size must be from 1 to 32, found: "
                    + this.forwardingFeatureList.size());
        }

        try {

            ((SSCodeImpl) this.ssCode).encodeAll(asnOs);

            asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int pos = asnOs.StartContentDefiniteLength();
            for (ExtForwFeature featureItem : this.forwardingFeatureList) {
                ((ExtForwFeatureImpl) featureItem).encodeAll(asnOs);
            }
            asnOs.FinalizeContent(pos);

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

        if (this.forwardingFeatureList != null) {
            sb.append("forwardingFeatureList=[");
            boolean firstItem = true;
            for (ExtForwFeature be : this.forwardingFeatureList) {
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
