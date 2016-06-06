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
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.EctData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CSGSubscriptionDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by vsubbotin on 24/05/16.
 */
public class AnyTimeSubscriptionInterrogationResponseImpl extends MobilityMessageImpl implements AnyTimeSubscriptionInterrogationResponse, MAPAsnPrimitive {
    private static final int _TAG_CALL_FORWARDING_DATA = 1;
    private static final int _TAG_CALL_BARRING_DATA = 2;
    private static final int _TAG_ODB_INFO = 3;
    private static final int _TAG_CAMEL_SUBSCRIPTION_INFO = 4;
    private static final int _TAG_SUPPORTED_VLR_CAMEL_PHASES = 5;
    private static final int _TAG_SUPPORTED_SGSN_CAMEL_PHASES = 6;
    private static final int _TAG_EXTENSION_CONTAINER = 7;
    private static final int _TAG_OFFERED_CAMEL4_CSI_IS_IN_VLR = 8;
    private static final int _TAG_OFFERED_CAMEL4_CSI_IS_IS_SGSN = 9;
    private static final int _TAG_MSISDN_BS_LIST = 10;
    private static final int _TAG_CSG_SUBSCRIPTION_DATA_LIST = 11;
    private static final int _TAG_CW_DATA = 12;
    private static final int _TAG_CH_DATA = 13;
    private static final int _TAG_CLIP_DATA = 14;
    private static final int _TAG_CLIR_DATA = 15;
    private static final int _TAG_ECT_DATA = 16;

    public static final String _PrimitiveName = "AnyTimeSubscriptionInterrogationResponse";

    private CallForwardingData callForwardingData;
    private CallBarringData callBarringData;
    private ODBInfo odbInfo;
    private CAMELSubscriptionInfo camelSubscriptionInfo;
    private SupportedCamelPhases supportedVlrCamelPhases;
    private SupportedCamelPhases supportedSgsnCamelPhases;
    private MAPExtensionContainer extensionContainer;
    private OfferedCamel4CSIs offeredCamel4CSIsInVlr;
    private OfferedCamel4CSIs offeredCamel4CSIsInSgsn;
    private ArrayList<MSISDNBS> msisdnBsList;
    private ArrayList<CSGSubscriptionData> csgSubscriptionDataList;
    private CallWaitingData cwData;
    private CallHoldData chData;
    private ClipData clipData;
    private ClirData clirData;
    private EctData ectData;

    public AnyTimeSubscriptionInterrogationResponseImpl() {
        super();
    }

    public AnyTimeSubscriptionInterrogationResponseImpl(CallForwardingData callForwardingData, CallBarringData callBarringData, ODBInfo odbInfo,
            CAMELSubscriptionInfo camelSubscriptionInfo, SupportedCamelPhases supportedVlrCamelPhases, SupportedCamelPhases supportedSgsnCamelPhases,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIsInVlr, OfferedCamel4CSIs offeredCamel4CSIsInSgsn,
            ArrayList<MSISDNBS> msisdnBsList, ArrayList<CSGSubscriptionData> csgSubscriptionDataList, CallWaitingData cwData, CallHoldData chData,
            ClipData clipData, ClirData clirData, EctData ectData) {
        super();
        this.callForwardingData = callForwardingData;
        this.callBarringData = callBarringData;
        this.odbInfo = odbInfo;
        this.camelSubscriptionInfo = camelSubscriptionInfo;
        this.supportedVlrCamelPhases = supportedVlrCamelPhases;
        this.supportedSgsnCamelPhases = supportedSgsnCamelPhases;
        this.extensionContainer = extensionContainer;
        this.offeredCamel4CSIsInVlr = offeredCamel4CSIsInVlr;
        this.offeredCamel4CSIsInSgsn = offeredCamel4CSIsInSgsn;
        this.msisdnBsList = msisdnBsList;
        this.csgSubscriptionDataList = csgSubscriptionDataList;
        this.cwData = cwData;
        this.chData = chData;
        this.clipData = clipData;
        this.clirData = clirData;
        this.ectData = ectData;
    }

    public CallForwardingData getCallForwardingData() {
        return this.callForwardingData;
    }

    public CallBarringData getCallBarringData() {
        return this.callBarringData;
    }

    public ODBInfo getOdbInfo() {
        return this.odbInfo;
    }

    public CAMELSubscriptionInfo getCamelSubscriptionInfo() {
        return this.camelSubscriptionInfo;
    }

    public SupportedCamelPhases getsupportedVlrCamelPhases() {
        return this.supportedVlrCamelPhases;
    }

    public SupportedCamelPhases getsupportedSgsnCamelPhases() {
        return this.supportedSgsnCamelPhases;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public OfferedCamel4CSIs getOfferedCamel4CSIsInVlr() {
        return this.offeredCamel4CSIsInVlr;
    }

    public OfferedCamel4CSIs getOfferedCamel4CSIsInSgsn() {
        return this.offeredCamel4CSIsInSgsn;
    }

    public ArrayList<MSISDNBS> getMsisdnBsList() {
        return this.msisdnBsList;
    }

    public ArrayList<CSGSubscriptionData> getCsgSubscriptionDataList() {
        return this.csgSubscriptionDataList;
    }

    public CallWaitingData getCwData() {
        return this.cwData;
    }

    public CallHoldData getChData() {
        return this.chData;
    }

    public ClipData getClipData() {
        return this.clipData;
    }

    public ClirData getClirData() {
        return this.clirData;
    }

    public EctData getEctData() {
        return this.ectData;
    }

    public int getTag() throws MAPException {
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
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

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
        this.callForwardingData = null;
        this.callBarringData = null;
        this.odbInfo = null;
        this.camelSubscriptionInfo = null;
        this.supportedVlrCamelPhases = null;
        this.supportedSgsnCamelPhases = null;
        this.extensionContainer = null;
        this.offeredCamel4CSIsInVlr = null;
        this.offeredCamel4CSIsInSgsn = null;
        this.msisdnBsList = null;
        this.csgSubscriptionDataList = null;
        this.cwData = null;
        this.chData = null;
        this.clipData = null;
        this.clirData = null;
        this.ectData = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_CALL_FORWARDING_DATA:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter callForwardingData is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.callForwardingData = new CallForwardingDataImpl();
                        ((CallForwardingDataImpl)this.callForwardingData).decodeAll(ais);
                        break;
                    case _TAG_CALL_BARRING_DATA:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter callBarringData is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.callBarringData = new CallBarringDataImpl();
                        ((CallBarringDataImpl)this.callBarringData).decodeAll(ais);
                        break;
                    case _TAG_ODB_INFO:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter odbInfo is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.odbInfo = new ODBInfoImpl();
                        ((ODBInfoImpl)this.odbInfo).decodeAll(ais);
                        break;
                    case _TAG_CAMEL_SUBSCRIPTION_INFO:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter camelSubscriptionInfo is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.camelSubscriptionInfo = new CAMELSubscriptionInfoImpl();
                        ((CAMELSubscriptionInfoImpl)this.camelSubscriptionInfo).decodeAll(ais);
                        break;
                    case _TAG_SUPPORTED_VLR_CAMEL_PHASES:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter supportedVlrCamelPhases is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.supportedVlrCamelPhases = new SupportedCamelPhasesImpl();
                        ((SupportedCamelPhasesImpl)this.supportedVlrCamelPhases).decodeAll(ais);
                        break;
                    case _TAG_SUPPORTED_SGSN_CAMEL_PHASES:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter supportedSgsnCamelPhases is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.supportedSgsnCamelPhases = new SupportedCamelPhasesImpl();
                        ((SupportedCamelPhasesImpl)this.supportedSgsnCamelPhases).decodeAll(ais);
                        break;
                    case _TAG_EXTENSION_CONTAINER:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter extensionContainer is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
                        break;
                    case _TAG_OFFERED_CAMEL4_CSI_IS_IN_VLR:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter offeredCamel4CSIsInVlr is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.offeredCamel4CSIsInVlr = new OfferedCamel4CSIsImpl();
                        ((OfferedCamel4CSIsImpl)this.offeredCamel4CSIsInVlr).decodeAll(ais);
                        break;
                    case _TAG_OFFERED_CAMEL4_CSI_IS_IS_SGSN:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter offeredCamel4CSIsInSgsn is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.offeredCamel4CSIsInSgsn = new OfferedCamel4CSIsImpl();
                        ((OfferedCamel4CSIsImpl)this.offeredCamel4CSIsInSgsn).decodeAll(ais);
                        break;
                    case _TAG_MSISDN_BS_LIST:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter msisdnBsList is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        MSISDNBS msisdnbs;
                        this.msisdnBsList = new ArrayList<MSISDNBS>();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            if (ais2.readTag() != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".msisdnbs: Parameter msisdnbs is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            msisdnbs = new MSISDNBSImpl();
                            ((MSISDNBSImpl)msisdnbs).decodeAll(ais2);
                            this.msisdnBsList.add(msisdnbs);
                        }

                        if (this.msisdnBsList.size() < 1 || this.msisdnBsList.size() > 50) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter msisdnBsList size must be from 1 to 50, found: "
                                    + this.msisdnBsList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;
                    case _TAG_CSG_SUBSCRIPTION_DATA_LIST:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter csgSubscriptionDataList is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        CSGSubscriptionData subscriptionData;
                        this.csgSubscriptionDataList = new ArrayList<CSGSubscriptionData>();
                        ais2 = ais.readSequenceStream();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            if (ais2.readTag() != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".subscriptionData: Parameter subscriptionData is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            subscriptionData = new CSGSubscriptionDataImpl();
                            ((CSGSubscriptionDataImpl)subscriptionData).decodeAll(ais2);
                            this.csgSubscriptionDataList.add(subscriptionData);
                        }

                        if (this.csgSubscriptionDataList.size() < 1 || this.csgSubscriptionDataList.size() > 50) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter csgSubscriptionDataList size must be from 1 to 50, found: "
                                    + this.csgSubscriptionDataList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;
                    case _TAG_CW_DATA:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter cwData is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.cwData = new CallWaitingDataImpl();
                        ((CallWaitingDataImpl)this.cwData).decodeAll(ais);
                        break;
                    case _TAG_CH_DATA:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter chData is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.chData = new CallHoldDataImpl();
                        ((CallHoldDataImpl)this.chData).decodeAll(ais);
                        break;
                    case _TAG_CLIP_DATA:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter clipData is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.clipData = new ClipDataImpl();
                        ((ClipDataImpl)this.clipData).decodeAll(ais);
                        break;
                    case _TAG_CLIR_DATA:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter clirData is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.clirData = new ClirDataImpl();
                        ((ClirDataImpl)this.clirData).decodeAll(ais);
                        break;
                    case _TAG_ECT_DATA:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter imsi is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.ectData = new EctDataImpl();
                        ((EctDataImpl)this.ectData).decodeAll(ais);
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

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + " : " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.callForwardingData != null) {
            ((CallForwardingDataImpl)this.callForwardingData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_CALL_FORWARDING_DATA);
        }

        if (callBarringData != null) {
            ((CallBarringDataImpl)this.callBarringData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_CALL_BARRING_DATA);
        }

        if (this.odbInfo != null) {
            ((ODBInfoImpl)this.odbInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ODB_INFO);
        }

        if (this.camelSubscriptionInfo != null) {
            ((CAMELSubscriptionInfoImpl)this.camelSubscriptionInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_CAMEL_SUBSCRIPTION_INFO);
        }

        if (this.supportedVlrCamelPhases != null) {
            ((SupportedCamelPhasesImpl)this.supportedVlrCamelPhases).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SUPPORTED_VLR_CAMEL_PHASES);
        }

        if (this.supportedSgsnCamelPhases != null) {
            ((SupportedCamelPhasesImpl)this.supportedSgsnCamelPhases).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SUPPORTED_SGSN_CAMEL_PHASES);
        }

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl)this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSION_CONTAINER);
        }

        if (this.offeredCamel4CSIsInVlr != null) {
            ((OfferedCamel4CSIsImpl)this.offeredCamel4CSIsInVlr).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_OFFERED_CAMEL4_CSI_IS_IN_VLR);
        }

        if (this.offeredCamel4CSIsInSgsn != null) {
            ((OfferedCamel4CSIsImpl)this.offeredCamel4CSIsInSgsn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_OFFERED_CAMEL4_CSI_IS_IS_SGSN);
        }

        try {
            if (this.msisdnBsList != null) {
                if (this.msisdnBsList.size() < 1 || this.msisdnBsList.size() > 50) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " size msisdnBsList is out of range (1..50). Actual size: " + this.msisdnBsList.size());
                }

                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_MSISDN_BS_LIST);
                int pos = asnOs.StartContentDefiniteLength();
                for (MSISDNBS msisdnbs: this.msisdnBsList) {
                    ((MSISDNBSImpl)msisdnbs).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }

        try {
            if (this.csgSubscriptionDataList != null) {
                if (this.csgSubscriptionDataList.size() < 1 || this.csgSubscriptionDataList.size() > 50) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " size csgSubscriptionDataList is out of range (1..50). Actual size: " + this.csgSubscriptionDataList.size());
                }

                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_CSG_SUBSCRIPTION_DATA_LIST);
                int pos = asnOs.StartContentDefiniteLength();
                for (CSGSubscriptionData subscriptionData: this.csgSubscriptionDataList) {
                    ((CSGSubscriptionDataImpl)subscriptionData).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }

        if (this.cwData != null) {
            ((CallWaitingDataImpl)this.cwData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_CW_DATA);
        }

        if (this.chData != null) {
            ((CallHoldDataImpl)this.chData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_CH_DATA);
        }

        if (this.clipData != null) {
            ((ClipDataImpl)this.clipData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_CLIP_DATA);
        }

        if (this.clirData != null) {
            ((ClirDataImpl)this.clirData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_CLIR_DATA);
        }

        if (this.ectData != null) {
            ((EctDataImpl)this.ectData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ECT_DATA);
        }
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.anyTimeSubscriptionInterrogation_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.anyTimeSubscriptionInterrogation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.callForwardingData != null) {
            sb.append("callForwardingData=");
            sb.append(this.callForwardingData);
        }
        if (this.callBarringData != null) {
            sb.append(", callBarringData=");
            sb.append(this.callBarringData);
        }
        if (this.odbInfo != null) {
            sb.append(", odbInfo=");
            sb.append(this.odbInfo);
        }
        if (this.camelSubscriptionInfo != null) {
            sb.append(", camelSubscriptionInfo=");
            sb.append(this.camelSubscriptionInfo);
        }
        if (this.supportedVlrCamelPhases != null) {
            sb.append(", supportedVlrCamelPhases=");
            sb.append(this.supportedVlrCamelPhases);
        }
        if (this.supportedSgsnCamelPhases != null) {
            sb.append(", supportedSgsnCamelPhases=");
            sb.append(this.supportedSgsnCamelPhases);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.offeredCamel4CSIsInVlr != null) {
            sb.append(", offeredCamel4CSIsInVlr=");
            sb.append(this.offeredCamel4CSIsInVlr);
        }
        if (this.offeredCamel4CSIsInSgsn != null) {
            sb.append(", offeredCamel4CSIsInSgsn=");
            sb.append(this.offeredCamel4CSIsInSgsn);
        }
        if (this.msisdnBsList != null) {
            sb.append(", msisdnBsList=[");
            boolean firstItem = true;
            for (MSISDNBS msisdnbs: msisdnBsList) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(msisdnbs);
            }
            sb.append("], ");
        }
        if (this.csgSubscriptionDataList != null) {
            sb.append(", csgSubscriptionDataList=[");
            boolean firstItem = true;
            for (CSGSubscriptionData csgSubscriptionData: csgSubscriptionDataList) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(csgSubscriptionData);
            }
            sb.append("], ");
        }
        if (this.cwData != null) {
            sb.append(", cwData=");
            sb.append(this.cwData);
        }
        if (this.chData != null) {
            sb.append(", chData=");
            sb.append(this.chData);
        }
        if (this.clipData != null) {
            sb.append(", clipData=");
            sb.append(this.clipData);
        }
        if (this.clirData != null) {
            sb.append(", clirData=");
            sb.append(this.clirData);
        }
        if (this.ectData != null) {
            sb.append(", imsi=");
            sb.append(this.ectData);
        }

        sb.append("]");
        return sb.toString();
    }
}
