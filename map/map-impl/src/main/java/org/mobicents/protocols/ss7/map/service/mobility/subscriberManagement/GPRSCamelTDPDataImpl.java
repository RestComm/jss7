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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultGPRSHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class GPRSCamelTDPDataImpl extends SequenceBase implements GPRSCamelTDPData {

    private static final int _TAG_gprsTriggerDetectionPoint = 0;
    private static final int _TAG_serviceKey = 1;
    private static final int _TAG_gsmSCFAddress = 2;
    private static final int _TAG_defaultSessionHandling = 3;
    private static final int _TAG_extensionContainer = 4;

    private GPRSTriggerDetectionPoint gprsTriggerDetectionPoint;
    private long serviceKey;
    private ISDNAddressString gsmSCFAddress;
    private DefaultGPRSHandling defaultSessionHandling;
    private MAPExtensionContainer extensionContainer;

    public GPRSCamelTDPDataImpl() {
        super("GPRSCamelTDPData");
    }

    public GPRSCamelTDPDataImpl(GPRSTriggerDetectionPoint gprsTriggerDetectionPoint, long serviceKey,
            ISDNAddressString gsmSCFAddress, DefaultGPRSHandling defaultSessionHandling,
            MAPExtensionContainer extensionContainer) {
        super("GPRSCamelTDPData");
        this.gprsTriggerDetectionPoint = gprsTriggerDetectionPoint;
        this.serviceKey = serviceKey;
        this.gsmSCFAddress = gsmSCFAddress;
        this.defaultSessionHandling = defaultSessionHandling;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public GPRSTriggerDetectionPoint getGPRSTriggerDetectionPoint() {
        return this.gprsTriggerDetectionPoint;
    }

    @Override
    public long getServiceKey() {
        return this.serviceKey;
    }

    @Override
    public ISDNAddressString getGsmSCFAddress() {
        return this.gsmSCFAddress;
    }

    @Override
    public DefaultGPRSHandling getDefaultSessionHandling() {
        return this.defaultSessionHandling;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.gprsTriggerDetectionPoint = null;
        this.serviceKey = -1;
        this.gsmSCFAddress = null;
        this.defaultSessionHandling = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
                        case _TAG_gprsTriggerDetectionPoint:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gprsTriggerDetectionPoint: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            int i1 = (int) ais.readInteger();
                            this.gprsTriggerDetectionPoint = GPRSTriggerDetectionPoint.getInstance(i1);
                            break;
                        case _TAG_serviceKey:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".serviceKey: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.serviceKey = (long) ais.readInteger();
                            break;
                        case _TAG_gsmSCFAddress:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gsmSCFAddress: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.gsmSCFAddress = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.gsmSCFAddress).decodeAll(ais);
                            break;
                        case _TAG_defaultSessionHandling:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".defaultSessionHandling: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            int i2 = (int) ais.readInteger();
                            this.defaultSessionHandling = DefaultGPRSHandling.getInstance(i2);
                            break;
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
                }
                    break;
                default:
                    ais.advanceElement();
                    break;
            }
        }

        if (this.gprsTriggerDetectionPoint == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament gprsTriggerDetectionPoint is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.gsmSCFAddress == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament gsmSCFAddress is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.defaultSessionHandling == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament defaultSessionHandling is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.serviceKey == -1) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament serviceKey is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.gprsTriggerDetectionPoint == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter gprsTriggerDetectionPoint is not defined");
        }

        if (this.gsmSCFAddress == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter gsmSCFAddress is not defined");
        }

        if (this.defaultSessionHandling == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter defaultSessionHandling is not defined");
        }

        try {
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_gprsTriggerDetectionPoint,
                    this.gprsTriggerDetectionPoint.getCode());

            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_serviceKey, this.serviceKey);

            ((ISDNAddressStringImpl) this.gsmSCFAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_gsmSCFAddress);

            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_defaultSessionHandling, this.defaultSessionHandling.getCode());

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.gprsTriggerDetectionPoint != null) {
            sb.append("gprsTriggerDetectionPoint=");
            sb.append(this.gprsTriggerDetectionPoint.toString());
            sb.append(", ");
        }

        sb.append("serviceKey=");
        sb.append(this.serviceKey);
        sb.append(", ");

        if (this.gsmSCFAddress != null) {
            sb.append("gsmSCFAddress=");
            sb.append(this.gsmSCFAddress.toString());
            sb.append(", ");
        }

        if (this.defaultSessionHandling != null) {
            sb.append("defaultSessionHandling=");
            sb.append(this.defaultSessionHandling.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());

        }

        sb.append("]");

        return sb.toString();
    }
}
