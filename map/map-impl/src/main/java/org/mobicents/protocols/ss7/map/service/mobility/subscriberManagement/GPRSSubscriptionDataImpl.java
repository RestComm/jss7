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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.PDPContextImpl;

/**
 *
 * @author daniel bichara
 *
 */
public class GPRSSubscriptionDataImpl extends SequenceBase implements GPRSSubscriptionData {

    protected static final int _TAG_gprsDataList = 1;
    protected static final int _TAG_extContainer = 2;
    protected static final int _TAG_apnOiReplacement = 3;

    private boolean completeDataListIncluded = false;
    private ArrayList<PDPContext> gprsDataList = null;
    private MAPExtensionContainer extensionContainer = null;
    private APNOIReplacement apnOiReplacement = null;

    public GPRSSubscriptionDataImpl() {
        super("GPRSSubscriptionData");
    }

    public GPRSSubscriptionDataImpl(boolean completeDataListIncluded, ArrayList<PDPContext> gprsDataList,
            MAPExtensionContainer extensionContainer, APNOIReplacement apnOiReplacement) {
        super("GPRSSubscriptionData");

        this.completeDataListIncluded = completeDataListIncluded;
        this.gprsDataList = gprsDataList;
        this.extensionContainer = extensionContainer;
        this.apnOiReplacement = apnOiReplacement;
    }

    @Override
    public boolean getCompleteDataListIncluded() {
        return this.completeDataListIncluded;
    }

    @Override
    public ArrayList<PDPContext> getGPRSDataList() {
        return this.gprsDataList;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public APNOIReplacement getApnOiReplacement() {
        return this.apnOiReplacement;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        PDPContext pdpContext = null;
        this.completeDataListIncluded = false;
        this.gprsDataList = null;
        this.extensionContainer = null;
        this.apnOiReplacement = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case _TAG_gprsDataList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gprsDataList: parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.gprsDataList = new ArrayList<PDPContext>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad gprsDataList element tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                pdpContext = new PDPContextImpl();
                                ((PDPContextImpl) pdpContext).decodeAll(ais2);
                                this.gprsDataList.add(pdpContext);
                            }
                            if (this.gprsDataList.size() > 50 || this.gprsDataList.size() < 1) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter gprsDataList size must be from 1 to 50, found: "
                                        + this.gprsDataList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_extContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter extensionContainer is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;
                        case _TAG_apnOiReplacement:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".apnOiReplacement: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.apnOiReplacement = new APNOIReplacementImpl();
                            ((APNOIReplacementImpl) this.apnOiReplacement).decodeAll(ais);
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                case Tag.CLASS_UNIVERSAL:
                    switch (tag) {
                        case Tag.NULL:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".completeDataListIncluded: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.completeDataListIncluded = true;
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
        }

        if (this.gprsDataList == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": gprsDataList parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.gprsDataList == null || this.gprsDataList.size() > 50 || this.gprsDataList.size() < 1)
            throw new MAPException("gprsDataList size must not be null and must be from 1 to 50, found: "
                    + this.gprsDataList.size());

        if (this.completeDataListIncluded) {
            try {
                asnOs.writeNull();
            } catch (IOException e) {
                throw new MAPException("IOException when encoding " + _PrimitiveName + ".completeDataListIncluded: "
                        + e.getMessage(), e);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding " + _PrimitiveName + ".completeDataListIncluded: "
                        + e.getMessage(), e);
            }
        }

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_gprsDataList);
            int pos = asnOs.StartContentDefiniteLength();
            for (PDPContext pdpContext : this.gprsDataList) {
                ((PDPContextImpl) pdpContext).encodeAll(asnOs);
            }
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_extContainer);

        if (this.apnOiReplacement != null)
            ((APNOIReplacementImpl) this.apnOiReplacement).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_apnOiReplacement);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GPRSSubscriptionData [");

        if (this.getCompleteDataListIncluded()) {
            sb.append("completeDataListIncluded, ");
        }

        if (this.gprsDataList != null) {
            sb.append("gprsDataList=[");
            boolean firstItem = true;
            for (PDPContext be : this.gprsDataList) {
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
            sb.append(", ");
        }

        if (this.apnOiReplacement != null) {
            sb.append("apnOiReplacement=");
            sb.append(this.apnOiReplacement.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
}
