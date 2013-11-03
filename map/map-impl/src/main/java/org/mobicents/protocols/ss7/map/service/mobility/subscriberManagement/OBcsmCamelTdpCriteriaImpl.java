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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CallTypeCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DestinationNumberCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class OBcsmCamelTdpCriteriaImpl extends SequenceBase implements OBcsmCamelTdpCriteria {

    private static final int _TAG_destinationNumberCriteria = 0;
    private static final int _TAG_basicServiceCriteria = 1;
    private static final int _TAG_callTypeCriteria = 2;
    private static final int _TAG_oCauseValueCriteria = 3;
    private static final int _TAG_extensionContainer = 4;

    private OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint;
    private DestinationNumberCriteria destinationNumberCriteria;
    private ArrayList<ExtBasicServiceCode> basicServiceCriteria;
    private CallTypeCriteria callTypeCriteria;
    private ArrayList<CauseValue> oCauseValueCriteria;
    private MAPExtensionContainer extensionContainer;

    public OBcsmCamelTdpCriteriaImpl() {
        super("OBcsmCamelTdpCriteria");
    }

    public OBcsmCamelTdpCriteriaImpl(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint,
            DestinationNumberCriteria destinationNumberCriteria, ArrayList<ExtBasicServiceCode> basicServiceCriteria,
            CallTypeCriteria callTypeCriteria, ArrayList<CauseValue> oCauseValueCriteria,
            MAPExtensionContainer extensionContainer) {
        super("OBcsmCamelTdpCriteria");
        this.oBcsmTriggerDetectionPoint = oBcsmTriggerDetectionPoint;
        this.destinationNumberCriteria = destinationNumberCriteria;
        this.basicServiceCriteria = basicServiceCriteria;
        this.callTypeCriteria = callTypeCriteria;
        this.oCauseValueCriteria = oCauseValueCriteria;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public OBcsmTriggerDetectionPoint getOBcsmTriggerDetectionPoint() {
        return this.oBcsmTriggerDetectionPoint;
    }

    @Override
    public DestinationNumberCriteria getDestinationNumberCriteria() {
        return this.destinationNumberCriteria;
    }

    @Override
    public ArrayList<ExtBasicServiceCode> getBasicServiceCriteria() {
        return this.basicServiceCriteria;
    }

    @Override
    public CallTypeCriteria getCallTypeCriteria() {
        return this.callTypeCriteria;
    }

    @Override
    public ArrayList<CauseValue> getOCauseValueCriteria() {
        return this.oCauseValueCriteria;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.oBcsmTriggerDetectionPoint = null;
        this.destinationNumberCriteria = null;
        this.basicServiceCriteria = null;
        this.callTypeCriteria = null;
        this.oCauseValueCriteria = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_UNIVERSAL:
                    switch (tag) {
                        case Tag.ENUMERATED:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".oBcsmTriggerDetectionPoint: not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            int code = (int) ais.readInteger();
                            this.oBcsmTriggerDetectionPoint = OBcsmTriggerDetectionPoint.getInstance(code);
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                case Tag.CLASS_CONTEXT_SPECIFIC:

                    switch (tag) {
                        case _TAG_destinationNumberCriteria:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".destinationNumberCriteria: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.destinationNumberCriteria = new DestinationNumberCriteriaImpl();
                            ((DestinationNumberCriteriaImpl) this.destinationNumberCriteria).decodeAll(ais);
                            break;
                        case _TAG_basicServiceCriteria:
                            ExtBasicServiceCode extBasicServiceCode = null;
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".basicServiceCriteria: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.basicServiceCriteria = new ArrayList<ExtBasicServiceCode>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                ais2.readTag();
                                extBasicServiceCode = new ExtBasicServiceCodeImpl();
                                ((ExtBasicServiceCodeImpl) extBasicServiceCode).decodeAll(ais2);
                                this.basicServiceCriteria.add(extBasicServiceCode);
                            }

                            if (this.basicServiceCriteria.size() < 1 || this.basicServiceCriteria.size() > 5) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter basicServiceCriteria size must be from 1 to 5, found: "
                                        + this.basicServiceCriteria.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_callTypeCriteria:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".callTypeCriteria: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            int code = (int) ais.readInteger();
                            this.callTypeCriteria = CallTypeCriteria.getInstance(code);
                            break;
                        case _TAG_oCauseValueCriteria:
                            CauseValue causeValue = null;
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".oCauseValueCriteria: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais3 = ais.readSequenceStream();
                            this.oCauseValueCriteria = new ArrayList<CauseValue>();
                            while (true) {
                                if (ais3.available() == 0)
                                    break;

                                int tag2 = ais3.readTag();
                                if (tag2 != Tag.STRING_OCTET || ais3.getTagClass() != Tag.CLASS_UNIVERSAL
                                        || !ais3.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": causeValue tag or tagClass or is not primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                causeValue = new CauseValueImpl();
                                ((CauseValueImpl) causeValue).decodeAll(ais3);
                                this.oCauseValueCriteria.add(causeValue);
                            }

                            if (this.oCauseValueCriteria.size() < 1 || this.oCauseValueCriteria.size() > 5) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter oCauseValueCriteria size must be from 1 to 5, found: "
                                        + this.oCauseValueCriteria.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                            }
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

                default:
                    ais.advanceElement();
                    break;
            }

        }

        if (this.oBcsmTriggerDetectionPoint == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament oBcsmTriggerDetectionPoint is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.oBcsmTriggerDetectionPoint == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": oBcsmTriggerDetectionPoint required.");

        if (this.basicServiceCriteria != null && (this.basicServiceCriteria.size() < 1 || this.basicServiceCriteria.size() > 5)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter basicServiceCriteria size must be from 1 to 5, found: " + this.basicServiceCriteria.size());
        }

        if (this.oCauseValueCriteria != null && (this.oCauseValueCriteria.size() < 1 || this.oCauseValueCriteria.size() > 5)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter oCauseValueCriteria size must be from 1 to 5, found: " + this.oCauseValueCriteria.size());
        }

        try {

            asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.oBcsmTriggerDetectionPoint.getCode());

            if (this.destinationNumberCriteria != null)
                ((DestinationNumberCriteriaImpl) this.destinationNumberCriteria).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_destinationNumberCriteria);

            if (this.basicServiceCriteria != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_basicServiceCriteria);
                int pos = asnOs.StartContentDefiniteLength();
                for (ExtBasicServiceCode extBasicServiceCode : this.basicServiceCriteria) {
                    ((ExtBasicServiceCodeImpl) extBasicServiceCode).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.callTypeCriteria != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_callTypeCriteria, this.callTypeCriteria.getCode());

            if (this.oCauseValueCriteria != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_oCauseValueCriteria);
                int pos = asnOs.StartContentDefiniteLength();
                for (CauseValue causeValue : this.oCauseValueCriteria) {
                    ((CauseValueImpl) causeValue).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);

        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (IOException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.oBcsmTriggerDetectionPoint != null) {
            sb.append("oBcsmTriggerDetectionPoint=");
            sb.append(this.oBcsmTriggerDetectionPoint.toString());
            sb.append(", ");
        }

        if (this.destinationNumberCriteria != null) {
            sb.append("destinationNumberCriteria=");
            sb.append(this.destinationNumberCriteria.toString());
            sb.append(", ");
        }

        if (this.basicServiceCriteria != null) {
            sb.append("basicServiceCriteria=[");
            boolean firstItem = true;
            for (ExtBasicServiceCode be : this.basicServiceCriteria) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.callTypeCriteria != null) {
            sb.append("callTypeCriteria=");
            sb.append(this.callTypeCriteria.toString());
            sb.append(", ");
        }

        if (this.oCauseValueCriteria != null) {
            sb.append("oCauseValueCriteria=[");
            boolean firstItem = true;
            for (CauseValue be : this.oCauseValueCriteria) {
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
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

}
