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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
public class ExtCallBarringFeatureImpl extends SequenceBase implements ExtCallBarringFeature {

    private static final int _TAG_ss_Status = 4;

    private ExtBasicServiceCode basicService = null;
    private ExtSSStatus ssStatus = null;
    private MAPExtensionContainer extensionContainer = null;

    public ExtCallBarringFeatureImpl() {
        super("ExtCallBarringFeature");
    }

    /**
     *
     */
    public ExtCallBarringFeatureImpl(ExtBasicServiceCode basicService, ExtSSStatus ssStatus,
            MAPExtensionContainer extensionContainer) {
        super("ExtCallBarringFeature");

        this.basicService = basicService;
        this.ssStatus = ssStatus;
        this.extensionContainer = extensionContainer;
    }

    public ExtBasicServiceCode getBasicService() {
        return this.basicService;
    }

    public ExtSSStatus getSsStatus() {
        return this.ssStatus;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.basicService = null;
        this.ssStatus = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case ExtBasicServiceCodeImpl._ID_ext_BearerService:
                        case ExtBasicServiceCodeImpl._ID_ext_Teleservice:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".basicService: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.basicService = new ExtBasicServiceCodeImpl();
                            ((ExtBasicServiceCodeImpl) this.basicService).decodeAll(ais);
                            break;
                        case _TAG_ss_Status:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ssStatus: is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.ssStatus = new ExtSSStatusImpl();
                            ((ExtSSStatusImpl) this.ssStatus).decodeAll(ais);
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                case Tag.CLASS_UNIVERSAL:
                    switch (tag) {
                        case Tag.SEQUENCE:
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

        if (this.ssStatus == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": ssStatus required.");

        if (this.basicService != null)
            ((ExtBasicServiceCodeImpl) this.basicService).encodeAll(asnOs);

        ((ExtSSStatusImpl) this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ss_Status);

        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.basicService != null) {
            sb.append("basicService=");
            sb.append(this.basicService.toString());
            sb.append(", ");
        }

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
