/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.lsm.AdditionalNumberImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocationInfoWithLMSIImpl extends SequenceBase implements LocationInfoWithLMSI {

    private static final int _TAG_NetworkNodeNumber = 1;
    private static final int _TAG_GprsNodeIndicator = 5;
    private static final int _TAG_AdditionalNumber = 6;

    private ISDNAddressString networkNodeNumber;
    private LMSI lmsi;
    private MAPExtensionContainer extensionContainer;
    private boolean gprsNodeIndicator;
    private AdditionalNumber additionalNumber;

    public LocationInfoWithLMSIImpl() {
        super("LocationInfoWithLMSI");
    }

    public LocationInfoWithLMSIImpl(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, boolean gprsNodeIndicator,
            AdditionalNumber additionalNumber) {
        super("LocationInfoWithLMSI");

        this.networkNodeNumber = networkNodeNumber;
        this.lmsi = lmsi;
        this.extensionContainer = extensionContainer;
        this.gprsNodeIndicator = gprsNodeIndicator;
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

    @Override
    public boolean getGprsNodeIndicator() {
        return gprsNodeIndicator;
    }

    public AdditionalNumber getAdditionalNumber() {
        return this.additionalNumber;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.networkNodeNumber = null;
        this.lmsi = null;
        this.extensionContainer = null;
        this.gprsNodeIndicator = false;
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
                            throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                    + ": lmsi: double element or element is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        this.lmsi = new LMSIImpl();
                        ((LMSIImpl) this.lmsi).decodeAll(ais);
                        break;

                    case Tag.SEQUENCE:
                        if (ais.isTagPrimitive() || this.extensionContainer != null)
                            throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                    + ": extensionContainer: double element or element is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
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
                        if (!ais.isTagPrimitive() || this.gprsNodeIndicator)
                            throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                    + ": gprsNodeIndicator: double element or element is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.gprsNodeIndicator = true;
                        break;

                    case _TAG_AdditionalNumber:
                        if (ais.isTagPrimitive() || this.additionalNumber != null)
                            throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                    + ": additionalNumber: double element or element is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.additionalNumber = new AdditionalNumberImpl();
                        ((AdditionalNumberImpl) this.additionalNumber).decodeAll(ais2);
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
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": 1 parameter is mandatory, found " + num,
                    MAPParsingComponentExceptionReason.MistypedParameter);
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        try {
            if (this.networkNodeNumber == null)
                throw new MAPException("Error while encoding " + _PrimitiveName + ": networkNodeNumber must not be null");

            ((ISDNAddressStringImpl) this.networkNodeNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_NetworkNodeNumber);

            if (this.lmsi != null)
                ((LMSIImpl) this.lmsi).encodeAll(asnOs);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);

            if (gprsNodeIndicator)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_GprsNodeIndicator);

            if (this.additionalNumber != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_AdditionalNumber);
                int pos = asnOs.StartContentDefiniteLength();
                ((AdditionalNumberImpl) this.additionalNumber).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

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
        if (this.gprsNodeIndicator) {
            sb.append(", gprsNodeIndicator");
        }
        if (this.additionalNumber != null) {
            sb.append(", additionalNumber=");
            sb.append(this.additionalNumber.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
