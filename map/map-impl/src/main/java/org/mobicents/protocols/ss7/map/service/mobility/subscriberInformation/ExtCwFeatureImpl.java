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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;

import java.io.IOException;

/**
 * Created by vsubbotin on 26/05/16.
 */
public class ExtCwFeatureImpl extends SequenceBase implements ExtCwFeature {
    private static final int _TAG_EXT_BASIC_SERVICE_CODE = 1;
    private static final int _TAG_EXT_SS_STATUS = 2;

    private ExtBasicServiceCode basicService;
    private ExtSSStatus ssStatus;

    public ExtCwFeatureImpl() {
        super("ExtCwFeature");
    }

    public ExtCwFeatureImpl(ExtBasicServiceCode basicService, ExtSSStatus ssStatus) {
        super("ExtCwFeature");
        this.basicService = basicService;
        this.ssStatus = ssStatus;
    }

    public ExtBasicServiceCode getBasicService() {
        return this.basicService;
    }

    public ExtSSStatus getSsStatus() {
        return this.ssStatus;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.basicService = null;
        this.ssStatus = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_EXT_BASIC_SERVICE_CODE:
                        this.basicService = new ExtBasicServiceCodeImpl();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        ((ExtBasicServiceCodeImpl) this.basicService).decodeAll(ais2);
                        break;
                    case _TAG_EXT_SS_STATUS:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter ssStatus is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.ssStatus = new ExtSSStatusImpl();
                        ((ExtSSStatusImpl)this.ssStatus).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.basicService == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + "basicService is mandatory but it is absent",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (this.ssStatus == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + "ssStatus is mandatory but it is absent",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.basicService == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter basicService is not defined");
        }

        if (this.ssStatus == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter ssStatus is not defined");
        }

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_EXT_BASIC_SERVICE_CODE);
            int pos = asnOs.StartContentDefiniteLength();
            ((ExtBasicServiceCodeImpl) this.basicService).encodeAll(asnOs);
            asnOs.FinalizeContent(pos);

            ((ExtSSStatusImpl)this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXT_SS_STATUS);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.basicService != null) {
            sb.append("basicService=");
            sb.append(this.basicService);
        }
        if (this.ssStatus != null) {
            sb.append(", ssStatus=");
            sb.append(this.ssStatus);
        }

        sb.append("]");
        return sb.toString();
    }
}
