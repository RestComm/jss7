/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtForwFeatureImpl;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by vsubbotin on 24/05/16.
 */
public class CallForwardingDataImpl extends SequenceBase implements CallForwardingData {
    private static final int _TAG_extensionContainer = 0;

    private ArrayList<ExtForwFeature> forwardingFeatureList;
    private boolean isNotificationToCSE;
    private MAPExtensionContainer extensionContainer;

    public CallForwardingDataImpl() {
        super("CallForwardingData");
    }

    public CallForwardingDataImpl(ArrayList<ExtForwFeature> forwardingFeatureList, boolean isNotificationToCSE,
            MAPExtensionContainer extensionContainer) {
        super("CallForwardingData");
        this.forwardingFeatureList = forwardingFeatureList;
        this.isNotificationToCSE = isNotificationToCSE;
        this.extensionContainer = extensionContainer;
    }

    public ArrayList<ExtForwFeature> getForwardingFeatureList() {
        return this.forwardingFeatureList;
    }

    public boolean getNotificationToCSE() {
        return this.isNotificationToCSE;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.forwardingFeatureList = null;
        this.isNotificationToCSE = false;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            switch (ais.getTagClass()) {
                case Tag.CLASS_UNIVERSAL:
                    switch (tag) {
                        case Tag.SEQUENCE:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".forwardingFeatureList: Parameter forwardingFeatureList is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ExtForwFeature extForwFeature;
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.forwardingFeatureList = new ArrayList<ExtForwFeature>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                if (ais2.readTag() != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".extForwFeature: Parameter extForwFeature is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                extForwFeature = new ExtForwFeatureImpl();
                                ((ExtForwFeatureImpl)extForwFeature).decodeAll(ais2);
                                this.forwardingFeatureList.add(extForwFeature);
                            }

                            if (this.forwardingFeatureList.size() < 1 || this.forwardingFeatureList.size() > 32) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter forwardingFeatureList size must be from 1 to 32, found: "
                                        + this.forwardingFeatureList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case Tag.NULL:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".isNotificationToCSE: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            ais.readNull();
                            this.isNotificationToCSE = Boolean.TRUE;
                            break;
                    }
                    break;
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case _TAG_extensionContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter is primitive",
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
        }

        if (this.forwardingFeatureList == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + "forwardingFeatureList is mandatory but it is absent",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.forwardingFeatureList == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter forwardingFeatureList is not defined");
        }

        if (this.forwardingFeatureList.isEmpty() || this.forwardingFeatureList.size() > 32) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " size forwardingFeatureList is out of range (1..32)");
        }

        try {
            asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int pos = asnOs.StartContentDefiniteLength();
            for (ExtForwFeature extForwFeature: forwardingFeatureList) {
                ((ExtForwFeatureImpl)extForwFeature).encodeAll(asnOs);
            }
            asnOs.FinalizeContent(pos);

            if (this.isNotificationToCSE) {
                asnOs.writeNull();
            }

            if (this.extensionContainer != null) {
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_extensionContainer);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.forwardingFeatureList != null) {
            sb.append("forwardingFeatureList=[");
            boolean firstItem = true;
            for (ExtForwFeature extForwFeature: forwardingFeatureList) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(extForwFeature);
            }
            sb.append("], ");
        }
        if (isNotificationToCSE) {
            sb.append("isNotificationToCSE, ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer);
            sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }
}
