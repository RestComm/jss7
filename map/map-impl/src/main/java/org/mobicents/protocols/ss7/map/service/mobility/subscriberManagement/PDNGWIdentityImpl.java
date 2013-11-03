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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.FQDN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class PDNGWIdentityImpl extends SequenceBase implements PDNGWIdentity {

    private static final int _TAG_pdnGwIpv4Address = 0;
    private static final int _TAG_pdnGwIpv6Address = 1;
    private static final int _TAG_pdnGwName = 2;
    private static final int _TAG_extensionContainer = 3;

    private PDPAddress pdnGwIpv4Address;
    private PDPAddress pdnGwIpv6Address;
    private FQDN pdnGwName;
    private MAPExtensionContainer extensionContainer;

    public PDNGWIdentityImpl() {
        super("PDNGWIdentity");
    }

    public PDNGWIdentityImpl(PDPAddress pdnGwIpv4Address, PDPAddress pdnGwIpv6Address, FQDN pdnGwName,
            MAPExtensionContainer extensionContainer) {
        super("PDNGWIdentity");
        this.pdnGwIpv4Address = pdnGwIpv4Address;
        this.pdnGwIpv6Address = pdnGwIpv6Address;
        this.pdnGwName = pdnGwName;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public PDPAddress getPdnGwIpv4Address() {
        return this.pdnGwIpv4Address;
    }

    @Override
    public PDPAddress getPdnGwIpv6Address() {
        return this.pdnGwIpv6Address;
    }

    @Override
    public FQDN getPdnGwName() {
        return this.pdnGwName;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.pdnGwIpv4Address = null;
        this.pdnGwIpv6Address = null;
        this.pdnGwName = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
                        case _TAG_pdnGwIpv4Address:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".pdnGwIpv4Address: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.pdnGwIpv4Address = new PDPAddressImpl();
                            ((PDPAddressImpl) this.pdnGwIpv4Address).decodeAll(ais);
                            break;
                        case _TAG_pdnGwIpv6Address:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".pdnGwIpv6Address: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.pdnGwIpv6Address = new PDPAddressImpl();
                            ((PDPAddressImpl) this.pdnGwIpv6Address).decodeAll(ais);
                            break;
                        case _TAG_pdnGwName:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".pdnGwName: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.pdnGwName = new FQDNImpl();
                            ((FQDNImpl) this.pdnGwName).decodeAll(ais);
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

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.pdnGwIpv4Address != null)
            ((PDPAddressImpl) this.pdnGwIpv4Address).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_pdnGwIpv4Address);

        if (this.pdnGwIpv6Address != null)
            ((PDPAddressImpl) this.pdnGwIpv6Address).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_pdnGwIpv6Address);

        if (this.pdnGwName != null)
            ((FQDNImpl) this.pdnGwName).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_pdnGwName);

        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_extensionContainer);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.pdnGwIpv4Address != null) {
            sb.append("pdnGwIpv4Address=");
            sb.append(this.pdnGwIpv4Address.toString());
            sb.append(", ");
        }

        if (this.pdnGwIpv6Address != null) {
            sb.append("pdnGwIpv6Address=");
            sb.append(this.pdnGwIpv6Address.toString());
            sb.append(", ");
        }

        if (this.pdnGwName != null) {
            sb.append("pdnGwName=");
            sb.append(this.pdnGwName.toString());
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
