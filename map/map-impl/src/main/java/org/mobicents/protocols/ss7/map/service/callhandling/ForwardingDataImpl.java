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

package org.mobicents.protocols.ss7.map.service.callhandling;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.FTNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingOptions;
import org.mobicents.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ForwardingOptionsImpl;

/*
 *
 * @author cristian veliscu
 *
 */
public class ForwardingDataImpl implements ForwardingData, MAPAsnPrimitive {
    private ISDNAddressString forwardedToNumber;
    private ISDNSubaddressString forwardedToSubaddress;
    private ForwardingOptions forwardingOptions;
    private MAPExtensionContainer extensionContainer;
    private FTNAddressString longForwardedToNumber;

    private static final int TAG_forwardedToNumber = 5;
    private static final int TAG_forwardedToSubaddress = 4;
    private static final int TAG_forwardingOptions = 6;
    private static final int TAG_extensionContainer = 7;
    private static final int TAG_longForwardedToNumber = 8;

    private static final String _PrimitiveName = "ForwardingData";

    public ForwardingDataImpl() {
    }

    public ForwardingDataImpl(ISDNAddressString forwardedToNumber, ISDNSubaddressString forwardedToSubaddress,
            ForwardingOptions forwardingOptions, MAPExtensionContainer extensionContainer,
            FTNAddressString longForwardedToNumber) {
        this.forwardedToNumber = forwardedToNumber;
        this.forwardedToSubaddress = forwardedToSubaddress;
        this.forwardingOptions = forwardingOptions;
        this.extensionContainer = extensionContainer;
        this.longForwardedToNumber = longForwardedToNumber;
    }

    @Override
    public ISDNAddressString getForwardedToNumber() {
        return this.forwardedToNumber;
    }

    @Override
    public ISDNSubaddressString getForwardedToSubaddress() {
        return this.forwardedToSubaddress;
    }

    @Override
    public ForwardingOptions getForwardingOptions() {
        return this.forwardingOptions;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public FTNAddressString getLongForwardedToNumber() {
        return this.longForwardedToNumber;
    }

    @Override
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.forwardedToNumber = null;
        this.forwardedToSubaddress = null;
        this.longForwardedToNumber = null;
        this.forwardingOptions = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case TAG_forwardedToNumber:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".forwardedToNumber: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.forwardedToNumber = new ISDNAddressStringImpl();
                        ((ISDNAddressStringImpl) this.forwardedToNumber).decodeAll(ais);
                        break;
                    case TAG_forwardedToSubaddress: // TODO:
                        break;
                    case TAG_forwardingOptions:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".forwardingOptions: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.forwardingOptions = new ForwardingOptionsImpl();
                        ((ForwardingOptionsImpl) this.forwardingOptions).decodeAll(ais);
                        break;
                    case TAG_extensionContainer:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".extensionContainer: Parameter extensionContainer is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;
                    case TAG_longForwardedToNumber:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".longForwardedToNumber: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.longForwardedToNumber = new FTNAddressStringImpl();
                        ((FTNAddressStringImpl) this.longForwardedToNumber).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.forwardedToNumber != null) {
            ((ISDNAddressStringImpl) this.forwardedToNumber)
                    .encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_forwardedToNumber);
        }

        if (this.forwardedToSubaddress != null) {
            // TODO:
        }

        if (this.forwardingOptions != null) {
            ((ForwardingOptionsImpl) this.forwardingOptions)
                    .encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_forwardingOptions);
        }

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_extensionContainer);
        }

        if (this.longForwardedToNumber != null) {
            ((FTNAddressStringImpl) this.longForwardedToNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_longForwardedToNumber);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.forwardedToNumber != null) {
            sb.append("forwardedToNumber=[");
            sb.append(this.forwardedToNumber);
            sb.append("], ");
        }

        if (this.forwardedToSubaddress != null) {
            sb.append("forwardedToSubaddress=[");
            sb.append(this.forwardedToSubaddress);
            sb.append("], ");
        }

        if (this.forwardingOptions != null) {
            sb.append("forwardingOptions=[");
            sb.append(this.forwardingOptions);
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=[");
            sb.append(this.extensionContainer);
            sb.append("], ");
        }

        if (this.longForwardedToNumber != null) {
            sb.append("longForwardedToNumber=[");
            sb.append(this.longForwardedToNumber);
            sb.append("]");
        }

        sb.append("]");
        return sb.toString();
    }
}