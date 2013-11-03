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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class OBcsmCamelTDPDataImpl extends SequenceBase implements OBcsmCamelTDPData {

    public static final int _TAG_gsmSCF_Address = 0;
    public static final int _TAG_defaultCallHandling = 1;
    public static final int _TAG_extensionContainer = 2;

    private OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint;
    private long serviceKey;
    private ISDNAddressString gsmSCFAddress;
    private DefaultCallHandling defaultCallHandling;
    private MAPExtensionContainer extensionContainer;

    public OBcsmCamelTDPDataImpl() {
        super("OBcsmCamelTDPData");
    }

    public OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint, long serviceKey,
            ISDNAddressString gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainer extensionContainer) {
        super("OBcsmCamelTDPData");

        this.oBcsmTriggerDetectionPoint = oBcsmTriggerDetectionPoint;
        this.serviceKey = serviceKey;
        this.gsmSCFAddress = gsmSCFAddress;
        this.defaultCallHandling = defaultCallHandling;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public OBcsmTriggerDetectionPoint getOBcsmTriggerDetectionPoint() {
        return oBcsmTriggerDetectionPoint;
    }

    @Override
    public long getServiceKey() {
        return serviceKey;
    }

    @Override
    public ISDNAddressString getGsmSCFAddress() {
        return gsmSCFAddress;
    }

    @Override
    public DefaultCallHandling getDefaultCallHandling() {
        return defaultCallHandling;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.oBcsmTriggerDetectionPoint = null;
        this.serviceKey = -1;
        this.gsmSCFAddress = null;
        this.defaultCallHandling = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    // o-BcsmTriggerDetectionPoint
                    if (tag != Tag.ENUMERATED || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter 0 bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    int i1 = (int) ais.readInteger();
                    this.oBcsmTriggerDetectionPoint = OBcsmTriggerDetectionPoint.getInstance(i1);
                    break;

                case 1:
                    // serviceKey
                    if (tag != Tag.INTEGER || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter 1 bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.serviceKey = (int) ais.readInteger();
                    break;

                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_CONTEXT_SPECIFIC: {
                            switch (tag) {
                                case _TAG_gsmSCF_Address:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".gsmSCF_Address: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.gsmSCFAddress = new ISDNAddressStringImpl();
                                    ((ISDNAddressStringImpl) this.gsmSCFAddress).decodeAll(ais);
                                    break;
                                case _TAG_defaultCallHandling:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".defaultCallHandling: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    i1 = (int) ais.readInteger();
                                    this.defaultCallHandling = DefaultCallHandling.getInstance(i1);
                                    break;
                                case _TAG_extensionContainer:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".extensionContainer: Parameter extensionContainer is primitive",
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

            num++;
        }

        if (this.oBcsmTriggerDetectionPoint == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament oBcsmTriggerDetectionPoint is mandatory but does not found",
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
        try {
            if (this.oBcsmTriggerDetectionPoint == null)
                throw new MAPException("Error while encoding" + _PrimitiveName
                        + ": oBcsmTriggerDetectionPoint must not be null");

            asnOs.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.oBcsmTriggerDetectionPoint.getCode());
            asnOs.writeInteger(this.serviceKey);

            if (this.gsmSCFAddress != null)
                ((ISDNAddressStringImpl) this.gsmSCFAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_gsmSCF_Address);

            if (this.defaultCallHandling != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_defaultCallHandling, this.defaultCallHandling.getCode());

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
        sb.append(this._PrimitiveName);
        sb.append(" [");

        if (this.oBcsmTriggerDetectionPoint != null) {
            sb.append("oBcsmTriggerDetectionPoint=");
            sb.append(this.oBcsmTriggerDetectionPoint.toString());
        }
        sb.append(", serviceKey=");
        sb.append(this.serviceKey);
        if (this.gsmSCFAddress != null) {
            sb.append(", gsmSCFAddress=");
            sb.append(this.gsmSCFAddress.toString());
        }
        if (this.defaultCallHandling != null) {
            sb.append(", defaultCallHandling=");
            sb.append(this.defaultCallHandling.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
