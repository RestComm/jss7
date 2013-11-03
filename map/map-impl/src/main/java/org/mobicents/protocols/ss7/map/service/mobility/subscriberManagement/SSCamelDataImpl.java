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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCamelData;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SSCamelDataImpl extends SequenceBase implements SSCamelData {

    private static final int _TAG_extensionContainer = 0;

    private ArrayList<SSCode> ssEventList;
    private ISDNAddressString gsmSCFAddress;
    private MAPExtensionContainer extensionContainer;

    public SSCamelDataImpl() {
        super("SSCamelData");
    }

    public SSCamelDataImpl(ArrayList<SSCode> ssEventList, ISDNAddressString gsmSCFAddress,
            MAPExtensionContainer extensionContainer) {
        super("SSCamelData");
        this.ssEventList = ssEventList;
        this.gsmSCFAddress = gsmSCFAddress;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public ArrayList<SSCode> getSsEventList() {
        return this.ssEventList;
    }

    @Override
    public ISDNAddressString getGsmSCFAddress() {
        return this.gsmSCFAddress;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        SSCode sSCode = null;
        this.ssEventList = null;
        this.gsmSCFAddress = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
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

                case Tag.CLASS_UNIVERSAL:
                    switch (tag) {
                        case Tag.SEQUENCE:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ssEventList: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.ssEventList = new ArrayList<SSCode>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL
                                        || !ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad tag or tagClass or is not primitive when decoding ssEventList",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                sSCode = new SSCodeImpl();
                                ((SSCodeImpl) sSCode).decodeAll(ais2);
                                this.ssEventList.add(sSCode);
                            }
                            if (this.ssEventList.size() < 1 && this.ssEventList.size() > 10) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter ssEventList size must be from 1 to 10, found: "
                                        + this.ssEventList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case Tag.STRING_OCTET:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gsmSCFAddress: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.gsmSCFAddress = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.gsmSCFAddress).decodeAll(ais);
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

        if (this.ssEventList == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": ssEventList required.",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (this.gsmSCFAddress == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": gsmSCFAddress required.",
                    MAPParsingComponentExceptionReason.MistypedParameter);

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.ssEventList == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": ssEventList required.");

        if (this.gsmSCFAddress == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + ": gsmSCFAddress required.");

        if (this.ssEventList != null && (this.ssEventList.size() < 1 || this.ssEventList.size() > 10)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter ssEventList size must be from 1 to 10, found: " + this.ssEventList.size());
        }

        try {
            asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int pos = asnOs.StartContentDefiniteLength();
            for (SSCode sSCode : this.ssEventList) {
                ((SSCodeImpl) sSCode).encodeAll(asnOs);
            }
            asnOs.FinalizeContent(pos);

            ((ISDNAddressStringImpl) this.gsmSCFAddress).encodeAll(asnOs);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);

        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.ssEventList != null) {
            sb.append("ssEventList=[");
            boolean firstItem = true;
            for (SSCode be : this.ssEventList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.gsmSCFAddress != null) {
            sb.append("gsmSCFAddress=");
            sb.append(this.gsmSCFAddress.toString());
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
