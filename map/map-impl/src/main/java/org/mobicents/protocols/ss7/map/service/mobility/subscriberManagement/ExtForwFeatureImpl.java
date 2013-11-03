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
import org.mobicents.protocols.ss7.map.api.primitives.FTNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptions;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
public class ExtForwFeatureImpl extends SequenceBase implements ExtForwFeature {

    private static final int _TAG_ss_Status = 4;
    private static final int _TAG_forwardedToNumber = 5;
    private static final int _TAG_forwardedToSubaddress = 8;
    private static final int _TAG_forwardingOptions = 6;
    private static final int _TAG_noReplyConditionTime = 7;
    private static final int _TAG_extensionContainer = 9;
    private static final int _TAG_longForwardedToNumber = 10;

    private ExtBasicServiceCode basicService = null;
    private ExtSSStatus ssStatus = null;
    private ISDNAddressString forwardedToNumber = null;
    private ISDNSubaddressString forwardedToSubaddress = null;
    private ExtForwOptions forwardingOptions = null;
    private Integer noReplyConditionTime = null;
    private MAPExtensionContainer extensionContainer = null;
    private FTNAddressString longForwardedToNumber = null;

    public ExtForwFeatureImpl() {
        super("ExtForwFeature");
    }

    /**
     *
     */
    public ExtForwFeatureImpl(ExtBasicServiceCode basicService, ExtSSStatus ssStatus, ISDNAddressString forwardedToNumber,
            ISDNSubaddressString forwardedToSubaddress, ExtForwOptions forwardingOptions, Integer noReplyConditionTime,
            MAPExtensionContainer extensionContainer, FTNAddressString longForwardedToNumber) {
        super("ExtForwFeature");

        this.basicService = basicService;
        this.ssStatus = ssStatus;
        this.forwardedToNumber = forwardedToNumber;
        this.forwardedToSubaddress = forwardedToSubaddress;
        this.forwardingOptions = forwardingOptions;
        this.noReplyConditionTime = noReplyConditionTime;
        this.extensionContainer = extensionContainer;
        this.longForwardedToNumber = longForwardedToNumber;
    }

    public ExtBasicServiceCode getBasicService() {
        return this.basicService;
    }

    public ExtSSStatus getSsStatus() {
        return this.ssStatus;
    }

    public ISDNAddressString getForwardedToNumber() {
        return this.forwardedToNumber;
    }

    public ISDNSubaddressString getForwardedToSubaddress() {
        return this.forwardedToSubaddress;
    }

    public ExtForwOptions getForwardingOptions() {
        return this.forwardingOptions;
    }

    public Integer getNoReplyConditionTime() {
        return this.noReplyConditionTime;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public FTNAddressString getLongForwardedToNumber() {
        return this.longForwardedToNumber;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.basicService = null;
        this.ssStatus = null;
        this.forwardedToNumber = null;
        this.forwardedToSubaddress = null;
        this.forwardingOptions = null;
        this.noReplyConditionTime = null;
        this.extensionContainer = null;
        this.longForwardedToNumber = null;

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

                        case _TAG_forwardedToNumber:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".forwardedToNumber: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.forwardedToNumber = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.forwardedToNumber).decodeAll(ais);
                            break;

                        case _TAG_forwardedToSubaddress:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".forwardedToSubaddress: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.forwardedToSubaddress = new ISDNSubaddressStringImpl();
                            ((ISDNSubaddressStringImpl) this.forwardedToSubaddress).decodeAll(ais);
                            break;

                        case _TAG_forwardingOptions:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".forwardingOptions: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.forwardingOptions = new ExtForwOptionsImpl();
                            ((ExtForwOptionsImpl) this.forwardingOptions).decodeAll(ais);
                            break;

                        case _TAG_noReplyConditionTime:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".noReplyConditionTime: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.noReplyConditionTime = (int) ais.readInteger();
                            break;

                        case _TAG_extensionContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;

                        case _TAG_longForwardedToNumber:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".longForwardedToNumber: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.longForwardedToNumber = new FTNAddressStringImpl();
                            ((FTNAddressStringImpl) this.longForwardedToNumber).decodeAll(ais);
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

        try {

            if (this.basicService != null)
                ((ExtBasicServiceCodeImpl) this.basicService).encodeAll(asnOs);

            ((ExtSSStatusImpl) this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ss_Status);

            if (this.forwardedToNumber != null)
                ((ISDNAddressStringImpl) this.forwardedToNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_forwardedToNumber);

            if (this.forwardedToSubaddress != null)
                ((ISDNSubaddressStringImpl) this.forwardedToSubaddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_forwardedToSubaddress);

            if (this.forwardingOptions != null)
                ((ExtForwOptionsImpl) this.forwardingOptions).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_forwardingOptions);

            if (this.noReplyConditionTime != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_noReplyConditionTime, this.noReplyConditionTime);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);

            if (this.longForwardedToNumber != null)
                ((FTNAddressStringImpl) this.longForwardedToNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_longForwardedToNumber);
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

        if (this.forwardedToNumber != null) {
            sb.append("forwardedToNumber=");
            sb.append(this.forwardedToNumber.toString());
            sb.append(", ");
        }

        if (this.forwardedToSubaddress != null) {
            sb.append("forwardedToSubaddress=");
            sb.append(this.forwardedToSubaddress.toString());
            sb.append(", ");
        }

        if (this.forwardingOptions != null) {
            sb.append("forwardingOptions=");
            sb.append(this.forwardingOptions.toString());
            sb.append(", ");
        }

        if (this.noReplyConditionTime != null) {
            sb.append("noReplyConditionTime=");
            sb.append(this.noReplyConditionTime.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.longForwardedToNumber != null) {
            sb.append("longForwardedToNumber=");
            sb.append(this.longForwardedToNumber.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
