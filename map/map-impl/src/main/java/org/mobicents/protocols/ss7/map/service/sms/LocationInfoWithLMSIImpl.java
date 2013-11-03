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

package org.mobicents.protocols.ss7.map.service.sms;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocationInfoWithLMSIImpl implements LocationInfoWithLMSI, MAPAsnPrimitive {

    private static final int _TAG_NetworkNodeNumber = 1;
    private static final int _TAG_GprsNodeIndicator = 5;
    private static final int _TAG_AdditionalNumber = 6;

    private ISDNAddressString networkNodeNumber;
    private LMSI lmsi;
    private MAPExtensionContainer extensionContainer;
    private AdditionalNumberType additionalNumberType;
    private ISDNAddressString additionalNumber;

    public LocationInfoWithLMSIImpl() {
    }

    public LocationInfoWithLMSIImpl(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
            AdditionalNumberType additionalNumberType, ISDNAddressString additionalNumber) {
        this.networkNodeNumber = networkNodeNumber;
        this.lmsi = lmsi;
        this.extensionContainer = extensionContainer;
        this.additionalNumberType = additionalNumberType;
        this.additionalNumber = additionalNumber;
    }

    public ISDNAddressString getNetworkNodeNumber() {
        return this.networkNodeNumber;
    }

    public LMSI getLMSI() {
        return this.lmsi;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public AdditionalNumberType getAdditionalNumberType() {
        return this.additionalNumberType;
    }

    public ISDNAddressString getAdditionalNumber() {
        return this.additionalNumber;
    }

    public int getTag() {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding LocationInfoWithLMSI: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding LocationInfoWithLMSI: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding LocationInfoWithLMSI: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding LocationInfoWithLMSI: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.networkNodeNumber = null;
        this.lmsi = null;
        this.extensionContainer = null;
        this.additionalNumberType = null;
        this.additionalNumber = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (num == 0) {
                // first parameter is mandatory - networkNode-Number
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_NetworkNodeNumber)
                    throw new MAPParsingComponentException(
                            "Error when decoding LocationInfoWithLMSI: networkNode-Number: tagClass or tag is bad or element is not primitive: tagClass="
                                    + ais.getTagClass() + ", Tag=" + tag, MAPParsingComponentExceptionReason.MistypedParameter);
                this.networkNodeNumber = new ISDNAddressStringImpl();
                ((ISDNAddressStringImpl) this.networkNodeNumber).decodeAll(ais);
            } else {
                // optional parameters
                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

                    switch (tag) {
                        case Tag.STRING_OCTET:
                            if (!ais.isTagPrimitive() || this.lmsi != null)
                                throw new MAPParsingComponentException(
                                        "Error when decoding LocationInfoWithLMSI: lmsi: double element or element is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.lmsi = new LMSIImpl();
                            ((LMSIImpl) this.lmsi).decodeAll(ais);
                            break;

                        case Tag.SEQUENCE:
                            if (ais.isTagPrimitive() || this.extensionContainer != null)
                                throw new MAPParsingComponentException(
                                        "Error when decoding LocationInfoWithLMSI: extensionContainer: double element or element is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                    switch (tag) {
                        case _TAG_GprsNodeIndicator:
                            if (!ais.isTagPrimitive() || this.additionalNumberType != null)
                                throw new MAPParsingComponentException(
                                        "Error when decoding LocationInfoWithLMSI: gprsNodeIndicator: double element or element is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.additionalNumberType = AdditionalNumberType.sgsn;
                            break;

                        case _TAG_AdditionalNumber:
                            if (!ais.isTagPrimitive() || this.additionalNumber != null)
                                throw new MAPParsingComponentException(
                                        "Error when decoding LocationInfoWithLMSI: additionalNumber: double element or element is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.additionalNumber = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.additionalNumber).decodeAll(ais);
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                } else {
                    ais.advanceElement();
                }
            }

            num++;
        }

        if (this.networkNodeNumber == null)
            throw new MAPParsingComponentException(
                    "Error while decoding LocationInfoWithLMSI: 1 parameter is mandatory, found " + num,
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (this.additionalNumberType == null && this.additionalNumber != null)
            this.additionalNumberType = AdditionalNumberType.msc;
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding LocationInfoWithLMSI: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        try {
            if (this.networkNodeNumber == null)
                throw new MAPException("Error while encoding LocationInfoWithLMSI: networkNodeNumber must not be null");

            ((ISDNAddressStringImpl) this.networkNodeNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_NetworkNodeNumber);

            if (this.lmsi != null)
                ((LMSIImpl) this.lmsi).encodeAll(asnOs);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);

            if (this.additionalNumber != null) {
                if (this.additionalNumberType == AdditionalNumberType.sgsn)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_GprsNodeIndicator);

                ((ISDNAddressStringImpl) this.additionalNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_AdditionalNumber);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding LocationInfoWithLMSI: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding LocationInfoWithLMSI: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationInfoWithLMSIImpl [");

        if (this.networkNodeNumber != null) {
            sb.append("networkNodeNumber=");
            sb.append(this.networkNodeNumber.toString());
        }
        if (this.lmsi != null) {
            sb.append(", lmsi=");
            sb.append(this.lmsi.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.additionalNumberType != null) {
            sb.append(", additionalNumberType=");
            sb.append(this.additionalNumberType.toString());
        }
        if (this.additionalNumber != null) {
            sb.append(", additionalNumber=");
            sb.append(this.additionalNumber.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
