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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificAPNInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SpecificAPNInfoImpl extends SequenceBase implements SpecificAPNInfo {

    private static final int _TAG_apn = 0;
    private static final int _TAG_pdnGwIdentity = 1;
    private static final int _TAG_extensionContainer = 2;

    private APN apn;
    private PDNGWIdentity pdnGwIdentity;
    private MAPExtensionContainer extensionContainer;

    public SpecificAPNInfoImpl() {
        super("SpecificAPNInfo");
    }

    public SpecificAPNInfoImpl(APN apn, PDNGWIdentity pdnGwIdentity, MAPExtensionContainer extensionContainer) {
        super("SpecificAPNInfo");
        this.apn = apn;
        this.pdnGwIdentity = pdnGwIdentity;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public APN getAPN() {
        return this.apn;
    }

    @Override
    public PDNGWIdentity getPdnGwIdentity() {
        return this.pdnGwIdentity;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.apn = null;
        this.pdnGwIdentity = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
                        case _TAG_apn:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".apn: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.apn = new APNImpl();
                            ((APNImpl) this.apn).decodeAll(ais);
                            break;
                        case _TAG_pdnGwIdentity:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".pdnGwIdentity: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.pdnGwIdentity = new PDNGWIdentityImpl();
                            ((PDNGWIdentityImpl) this.pdnGwIdentity).decodeAll(ais);
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

        if (this.apn == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament apn is mandatory but does not found", MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.pdnGwIdentity == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament pdnGwIdentity is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.apn == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName + " the mandatory parameter apn is not defined");
        }
        if (this.pdnGwIdentity == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter pdnGwIdentity is not defined");
        }

        ((APNImpl) this.apn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_apn);

        ((PDNGWIdentityImpl) this.pdnGwIdentity).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_pdnGwIdentity);

        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_extensionContainer);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.apn != null) {
            sb.append("apn=");
            sb.append(this.apn.toString());
            sb.append(", ");
        }

        if (this.pdnGwIdentity != null) {
            sb.append("pdnGwIdentity=");
            sb.append(this.pdnGwIdentity.toString());
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
