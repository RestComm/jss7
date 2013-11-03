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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriterium;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class DPAnalysedInfoCriteriumImpl extends SequenceBase implements DPAnalysedInfoCriterium {

    private ISDNAddressString dialledNumber;
    private long serviceKey;
    private ISDNAddressString gsmSCFAddress;
    private DefaultCallHandling defaultCallHandling;
    private MAPExtensionContainer extensionContainer;

    public DPAnalysedInfoCriteriumImpl() {
        super("DPAnalysedInfoCriterium");
    }

    public DPAnalysedInfoCriteriumImpl(ISDNAddressString dialledNumber, long serviceKey, ISDNAddressString gsmSCFAddress,
            DefaultCallHandling defaultCallHandling, MAPExtensionContainer extensionContainer) {
        super("DPAnalysedInfoCriterium");
        this.dialledNumber = dialledNumber;
        this.serviceKey = serviceKey;
        this.gsmSCFAddress = gsmSCFAddress;
        this.defaultCallHandling = defaultCallHandling;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public ISDNAddressString getDialledNumber() {
        return this.dialledNumber;
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
    public DefaultCallHandling getDefaultCallHandling() {
        return this.defaultCallHandling;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.dialledNumber = null;
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

                    if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".dialledNumber: Parameter is bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.dialledNumber = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.dialledNumber).decodeAll(ais);
                    break;

                case 1:
                    if (tag != Tag.INTEGER || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".serviceKey :Parameter is bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.serviceKey = (long) ais.readInteger();
                    break;
                case 2:
                    if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".gsmSCFAddress: Parameter is bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.gsmSCFAddress = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.gsmSCFAddress).decodeAll(ais);
                    break;
                case 3:
                    if (tag != Tag.INTEGER || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".defaultCallHandling: Parameter is bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    int i1 = (int) ais.readInteger();
                    this.defaultCallHandling = DefaultCallHandling.getInstance(i1);
                    break;

                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_UNIVERSAL: {
                            switch (tag) {
                                case Tag.SEQUENCE:
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

        if (this.dialledNumber == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament dialledNumber is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.gsmSCFAddress == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament gsmSCFAddress is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.defaultCallHandling == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament defaultCallHandling is mandatory but does not found",
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

            if (this.dialledNumber == null)
                throw new MAPException("Error while encoding" + _PrimitiveName + ": dialledNumber must not be null");

            if (this.gsmSCFAddress == null)
                throw new MAPException("Error while encoding" + _PrimitiveName + ": gsmSCFAddress must not be null");

            if (this.defaultCallHandling == null)
                throw new MAPException("Error while encoding" + _PrimitiveName + ": defaultCallHandling must not be null");

            ((ISDNAddressStringImpl) this.dialledNumber).encodeAll(asnOs);

            asnOs.writeInteger(this.serviceKey);

            ((ISDNAddressStringImpl) this.gsmSCFAddress).encodeAll(asnOs);

            asnOs.writeInteger(this.defaultCallHandling.getCode());

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);

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

        if (this.dialledNumber != null) {
            sb.append("dialledNumber=");
            sb.append(this.dialledNumber.toString());
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

        if (this.defaultCallHandling != null) {
            sb.append("defaultCallHandling=");
            sb.append(this.defaultCallHandling.toString());
            sb.append(", ");
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
