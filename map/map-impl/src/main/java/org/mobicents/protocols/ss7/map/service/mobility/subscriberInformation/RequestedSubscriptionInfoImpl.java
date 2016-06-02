/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSForBSCode;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.supplementary.SSForBSCodeImpl;

import java.io.IOException;

/**
 * Created by vsubbotin on 24/05/16.
 */
public class RequestedSubscriptionInfoImpl extends SequenceBase implements RequestedSubscriptionInfo {
    public static final int _ID_requested_SS_info = 1;
    public static final int _ID_odb = 2;
    public static final int _ID_requested_CAMEL_subscription_info = 3;
    public static final int _ID_supported_VLR_CAMEL_phases = 4;
    public static final int _ID_supported_SGSN_CAMEL_phases = 5;
    public static final int _ID_extension_container = 6;
    public static final int _ID_additional_requested_CAMEL_subscription_info = 7;
    public static final int _ID_msisdn_BS_list = 8;
    public static final int _ID_csg_subscription_data_requested = 9;
    public static final int _ID_cw_info = 10;
    public static final int _ID_clip_info = 11;
    public static final int _ID_clir_info = 12;
    public static final int _ID_hold_info = 13;
    public static final int _ID_ect_info = 14;

    private SSForBSCode ssForBSCode;
    private boolean isOdb;
    private RequestedCAMELSubscriptionInfo requestedCAMELSubscriptionInfo;
    private boolean isSupportedVlrCamelPhases;
    private boolean isSupportedSgsnCamelPhases;
    private MAPExtensionContainer extensionContainer;
    private AdditionalRequestedCAMELSubscriptionInfo additionalRequestedCAMELSubscriptionInfo;
    private boolean isMsisdnBsList;
    private boolean isCsgSubscriptionDataRequested;
    private boolean isCwInfo;
    private boolean isClipInfo;
    private boolean isClirInfo;
    private boolean isHoldInfo;
    private boolean isEctInfo;

    public RequestedSubscriptionInfoImpl() {
        super("RequestedSubscriptionInfo");
    }

    public RequestedSubscriptionInfoImpl(SSForBSCode ssForBSCode, boolean isOdb, RequestedCAMELSubscriptionInfo requestedCAMELSubscriptionInfo,
                                         boolean isSupportedVlrCamelPhases, boolean isSupportedSgsnCamelPhases, MAPExtensionContainer extensionContainer,
                                         AdditionalRequestedCAMELSubscriptionInfo additionalRequestedCAMELSubscriptionInfo, boolean isMsisdnBsList,
                                         boolean isCsgSubscriptionDataRequested, boolean isCwInfo, boolean isClipInfo, boolean isClirInfo, boolean isHoldInfo,
                                         boolean isEctInfo) {
        super("RequestedSubscriptionInfo");
        this.ssForBSCode = ssForBSCode;
        this.isOdb = isOdb;
        this.requestedCAMELSubscriptionInfo = requestedCAMELSubscriptionInfo;
        this.isSupportedVlrCamelPhases = isSupportedVlrCamelPhases;
        this.isSupportedSgsnCamelPhases = isSupportedSgsnCamelPhases;
        this.extensionContainer = extensionContainer;
        this.additionalRequestedCAMELSubscriptionInfo = additionalRequestedCAMELSubscriptionInfo;
        this.isMsisdnBsList = isMsisdnBsList;
        this.isCsgSubscriptionDataRequested = isCsgSubscriptionDataRequested;
        this.isCwInfo = isCwInfo;
        this.isClipInfo = isClipInfo;
        this.isClirInfo = isClirInfo;
        this.isHoldInfo = isHoldInfo;
        this.isEctInfo = isEctInfo;
    }

    public SSForBSCode getRequestedSSInfo() {
        return this.ssForBSCode;
    }

    public boolean getOdb() {
        return this.isOdb;
    }

    public RequestedCAMELSubscriptionInfo getRequestedCAMELSubscriptionInfo() {
        return this.requestedCAMELSubscriptionInfo;
    }

    public boolean getSupportedVlrCamelPhases() {
        return this.isSupportedVlrCamelPhases;
    }

    public boolean getSupportedSgsnCamelPhases() {
        return this.isSupportedSgsnCamelPhases;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public AdditionalRequestedCAMELSubscriptionInfo getAdditionalRequestedCamelSubscriptionInfo() {
        return this.additionalRequestedCAMELSubscriptionInfo;
    }

    public boolean getMsisdnBsList() {
        return this.isMsisdnBsList;
    }

    public boolean getCsgSubscriptionDataRequested() {
        return this.isCsgSubscriptionDataRequested;
    }

    public boolean getCwInfo() {
        return this.isCwInfo;
    }

    public boolean getClipInfo() {
        return this.isClipInfo;
    }

    public boolean getClirInfo() {
        return this.isClirInfo;
    }

    public boolean getHoldInfo() {
        return this.isHoldInfo;
    }

    public boolean getEctInfo() {
        return this.isEctInfo;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        ssForBSCode = null;
        isOdb = false;
        requestedCAMELSubscriptionInfo = null;
        isSupportedVlrCamelPhases = false;
        isSupportedSgsnCamelPhases = false;
        extensionContainer = null;
        additionalRequestedCAMELSubscriptionInfo = null;
        isMsisdnBsList = false;
        isCsgSubscriptionDataRequested = false;
        isCwInfo = false;
        isClipInfo = false;
        isClirInfo = false;
        isHoldInfo = false;
        isEctInfo = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_requested_SS_info:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter ssForBSCode is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.ssForBSCode = new SSForBSCodeImpl();
                        ((SSForBSCodeImpl)this.ssForBSCode).decodeAll(ais);
                        break;
                    case _ID_odb:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter isOdb is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.isOdb = Boolean.TRUE;
                        break;
                    case _ID_requested_CAMEL_subscription_info:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter requestedCAMELSubscriptionInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        int requestedInfo = (int)ais.readInteger();
                        this.requestedCAMELSubscriptionInfo = RequestedCAMELSubscriptionInfo.getInstance(requestedInfo);
                        break;
                    case _ID_supported_VLR_CAMEL_phases:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter isSupportedVlrCamelPhases is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.isSupportedVlrCamelPhases = Boolean.TRUE;
                        break;
                    case _ID_supported_SGSN_CAMEL_phases:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter isSupportedSgsnCamelPhases is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.isSupportedSgsnCamelPhases = Boolean.TRUE;
                        break;
                    case _ID_extension_container:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter extensionContainer is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) extensionContainer).decodeAll(ais);
                        break;
                    case _ID_additional_requested_CAMEL_subscription_info:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter additionalRequestedCAMELSubscriptionInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        int requestedAdditionalInfo = (int)ais.readInteger();
                        this.additionalRequestedCAMELSubscriptionInfo = AdditionalRequestedCAMELSubscriptionInfo.getInstance(requestedAdditionalInfo);
                        break;
                    case _ID_msisdn_BS_list:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter isMsisdnBsList is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.isMsisdnBsList = Boolean.TRUE;
                        break;
                    case _ID_csg_subscription_data_requested:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter isCsgSubscriptionDataRequested is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.isCsgSubscriptionDataRequested = Boolean.TRUE;
                        break;
                    case _ID_cw_info:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter isCwInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.isCwInfo = Boolean.TRUE;
                        break;
                    case _ID_clip_info:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter isClipInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.isClipInfo = Boolean.TRUE;
                        break;
                    case _ID_clir_info:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter isClirInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.isClirInfo = Boolean.TRUE;
                        break;
                    case _ID_hold_info:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter isHoldInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.isHoldInfo = Boolean.TRUE;
                        break;
                    case _ID_ect_info:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter isEctInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.isEctInfo = Boolean.TRUE;
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

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.ssForBSCode != null) {
            ((SSForBSCodeImpl)this.ssForBSCode).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_requested_SS_info);
        }

        try {
            if (this.isOdb) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_odb);
            }
        }  catch (IOException e) {
            throw new MAPException("IOException when encoding parameter odb: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter odb: ", e);
        }

        try {
            if (this.requestedCAMELSubscriptionInfo != null) {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_requested_CAMEL_subscription_info, requestedCAMELSubscriptionInfo.getCode());
            }
        }  catch (IOException e) {
            throw new MAPException("IOException when encoding parameter requestedCAMELSubscriptionInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter requestedCAMELSubscriptionInfo: ", e);
        }

        try {
            if (this.isSupportedVlrCamelPhases) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_supported_VLR_CAMEL_phases);
            }
        }  catch (IOException e) {
            throw new MAPException("IOException when encoding parameter isSupportedVlrCamelPhases: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter isSupportedVlrCamelPhases: ", e);
        }

        try {
            if (this.isSupportedSgsnCamelPhases) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_supported_SGSN_CAMEL_phases);
            }
        }  catch (IOException e) {
            throw new MAPException("IOException when encoding parameter isSupportedSgsnCamelPhases: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter isSupportedSgsnCamelPhases: ", e);
        }

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extension_container);
        }

        try {
            if (this.additionalRequestedCAMELSubscriptionInfo != null) {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_additional_requested_CAMEL_subscription_info, additionalRequestedCAMELSubscriptionInfo.getCode());
            }
        }  catch (IOException e) {
            throw new MAPException("IOException when encoding parameter additionalRequestedCAMELSubscriptionInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter additionalRequestedCAMELSubscriptionInfo: ", e);
        }

        try {
            if (this.isMsisdnBsList) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_msisdn_BS_list);
            }
        }  catch (IOException e) {
            throw new MAPException("IOException when encoding parameter isMsisdnBsList: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter isMsisdnBsList: ", e);
        }

        try {
            if (this.isCsgSubscriptionDataRequested) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_csg_subscription_data_requested);
            }
        }  catch (IOException e) {
            throw new MAPException("IOException when encoding parameter isCsgSubscriptionDataRequested: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter isCsgSubscriptionDataRequested: ", e);
        }

        try {
            if (this.isCwInfo) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_cw_info);
            }
        }  catch (IOException e) {
            throw new MAPException("IOException when encoding parameter isCwInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter isCwInfo: ", e);
        }

        try {
            if (this.isClipInfo) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_clip_info);
            }
        }  catch (IOException e) {
            throw new MAPException("IOException when encoding parameter isClipInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter isClipInfo: ", e);
        }

        try {
            if (this.isClirInfo) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_clir_info);
            }
        }  catch (IOException e) {
            throw new MAPException("IOException when encoding parameter isClirInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter isClirInfo: ", e);
        }

        try {
            if (this.isHoldInfo) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_hold_info);
            }
        }  catch (IOException e) {
            throw new MAPException("IOException when encoding parameter isHoldInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter isHoldInfo: ", e);
        }

        try {
            if (this.isEctInfo) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_ect_info);
            }
        }  catch (IOException e) {
            throw new MAPException("IOException when encoding parameter isEctInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter isEctInfo: ", e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.ssForBSCode != null) {
            sb.append("ssForBSCode=");
            sb.append(this.ssForBSCode);
        }
        if (this.isOdb) {
            sb.append(", isOdb");
        }
        if (this.requestedCAMELSubscriptionInfo != null) {
            sb.append(", requestedCAMELSubscriptionInfo=");
            sb.append(this.requestedCAMELSubscriptionInfo);
        }
        if (this.isSupportedVlrCamelPhases) {
            sb.append(", isSupportedVlrCamelPhases");
        }
        if (this.isSupportedSgsnCamelPhases) {
            sb.append(", isSupportedSgsnCamelPhases");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.additionalRequestedCAMELSubscriptionInfo != null) {
            sb.append(", additionalRequestedCAMELSubscriptionInfo=");
            sb.append(this.additionalRequestedCAMELSubscriptionInfo);
        }
        if (this.isMsisdnBsList) {
            sb.append(", isMsisdnBsList");
        }
        if (this.isCsgSubscriptionDataRequested) {
            sb.append(", isCsgSubscriptionDataRequested");
        }
        if (this.isCwInfo) {
            sb.append(",isCwInfo");
        }
        if (this.isClipInfo) {
            sb.append(", isClipInfo");
        }
        if (this.isClirInfo) {
            sb.append(", isClirInfo");
        }
        if (this.isHoldInfo) {
            sb.append(", isHoldInfo");
        }
        if (this.isEctInfo) {
            sb.append(", isEctInfo");
        }

        sb.append("]");
        return sb.toString();
    }
}
