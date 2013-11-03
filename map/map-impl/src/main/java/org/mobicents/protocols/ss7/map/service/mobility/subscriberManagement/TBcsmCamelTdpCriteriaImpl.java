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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class TBcsmCamelTdpCriteriaImpl extends SequenceBase implements TBcsmCamelTdpCriteria {

    private static final int _TAG_basicServiceCriteria = 0;
    private static final int _TAG_tCauseValueCriteria = 1;

    private TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint;
    private ArrayList<ExtBasicServiceCode> basicServiceCriteria;
    private ArrayList<CauseValue> tCauseValueCriteria;

    public TBcsmCamelTdpCriteriaImpl() {
        super("TBcsmCamelTdpCriteria");
    }

    public TBcsmCamelTdpCriteriaImpl(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint,
            ArrayList<ExtBasicServiceCode> basicServiceCriteria, ArrayList<CauseValue> tCauseValueCriteria) {
        super("TBcsmCamelTdpCriteria");
        this.tBcsmTriggerDetectionPoint = tBcsmTriggerDetectionPoint;
        this.basicServiceCriteria = basicServiceCriteria;
        this.tCauseValueCriteria = tCauseValueCriteria;
    }

    @Override
    public TBcsmTriggerDetectionPoint getTBcsmTriggerDetectionPoint() {
        return this.tBcsmTriggerDetectionPoint;
    }

    @Override
    public ArrayList<ExtBasicServiceCode> getBasicServiceCriteria() {
        return this.basicServiceCriteria;
    }

    @Override
    public ArrayList<CauseValue> getTCauseValueCriteria() {
        return this.tCauseValueCriteria;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.tBcsmTriggerDetectionPoint = null;
        this.basicServiceCriteria = null;
        this.tCauseValueCriteria = null;

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
                                        + ".tBcsmTriggerDetectionPoint:  not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            int code = (int) ais.readInteger();
                            this.tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.getInstance(code);
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case _TAG_basicServiceCriteria:
                            ExtBasicServiceCode extBasicServiceCode = null;
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".basicServiceCriteria: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.basicServiceCriteria = new ArrayList<ExtBasicServiceCode>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
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

                        case _TAG_tCauseValueCriteria:
                            CauseValue causeValue = null;
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".tCauseValueCriteria: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais3 = ais.readSequenceStream();
                            this.tCauseValueCriteria = new ArrayList<CauseValue>();
                            while (true) {
                                if (ais3.available() == 0)
                                    break;

                                int tag2 = ais3.readTag();
                                if (tag2 != Tag.STRING_OCTET || ais3.getTagClass() != Tag.CLASS_UNIVERSAL
                                        || !ais3.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": causeValue tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                causeValue = new CauseValueImpl();
                                ((CauseValueImpl) causeValue).decodeAll(ais3);
                                this.tCauseValueCriteria.add(causeValue);
                            }

                            if (this.tCauseValueCriteria.size() < 1 || this.tCauseValueCriteria.size() > 5) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter tCauseValueCriteria size must be from 1 to 5, found: "
                                        + this.tCauseValueCriteria.size(), MAPParsingComponentExceptionReason.MistypedParameter);
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

        }

        if (this.tBcsmTriggerDetectionPoint == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament tBcsmTriggerDetectionPoint is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.tBcsmTriggerDetectionPoint == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": tBcsmTriggerDetectionPoint required.");

        if (this.basicServiceCriteria != null && (this.basicServiceCriteria.size() < 1 || this.basicServiceCriteria.size() > 5)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter basicServiceCriteria size must be from 1 to 5, found: " + this.basicServiceCriteria.size());
        }

        if (this.tCauseValueCriteria != null && (this.tCauseValueCriteria.size() < 1 || this.tCauseValueCriteria.size() > 5)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter tCauseValueCriteria size must be from 1 to 5, found: " + this.tCauseValueCriteria.size());
        }

        try {
            asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.tBcsmTriggerDetectionPoint.getCode());

            if (this.basicServiceCriteria != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_basicServiceCriteria);
                int pos = asnOs.StartContentDefiniteLength();
                for (ExtBasicServiceCode extBasicServiceCode : this.basicServiceCriteria) {
                    ((ExtBasicServiceCodeImpl) extBasicServiceCode).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.tCauseValueCriteria != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_tCauseValueCriteria);
                int pos = asnOs.StartContentDefiniteLength();
                for (CauseValue causeValue : this.tCauseValueCriteria) {
                    ((CauseValueImpl) causeValue).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

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

        if (this.tBcsmTriggerDetectionPoint != null) {
            sb.append("tBcsmTriggerDetectionPoint=");
            sb.append(this.tBcsmTriggerDetectionPoint.toString());
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

        if (this.tCauseValueCriteria != null) {
            sb.append("tCauseValueCriteria=[");
            boolean firstItem = true;
            for (CauseValue be : this.tCauseValueCriteria) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("] ");
        }

        sb.append("]");

        return sb.toString();
    }

}
