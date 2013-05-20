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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;
import java.util.ArrayList;

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
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.SupportedFeaturesImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
public class InsertSubscriberDataResponseImpl extends MobilityMessageImpl implements InsertSubscriberDataResponse {

    public static final String _PrimitiveName = "InsertSubscriberDataResponse";

    // MAP V1 & V2 & V3:
    protected static final int _TAG_teleserviceList = 1;
    protected static final int _TAG_bearerServiceList = 2;
    protected static final int _TAG_SS_List = 3;
    protected static final int _TAG_odb_GeneralData = 4;

    // MAP V2 & V3:
    protected static final int _TAG_regionalSubscriptionResponse = 5;

    // MAP V3:
    protected static final int _TAG_supportedCamelPhases = 6;
    protected static final int _TAG_extContainer = 7;
    protected static final int _TAG_offeredCamel4CSIs = 8;
    protected static final int _TAG_supportedFeatures = 9;

    private ArrayList<ExtTeleserviceCode> teleserviceList = null;
    private ArrayList<ExtBearerServiceCode> bearerServiceList = null;
    private ArrayList<SSCode> ssList = null;
    private ODBGeneralData odbGeneralData = null;
    private RegionalSubscriptionResponse regionalSubscriptionResponse = null;
    private SupportedCamelPhases supportedCamelPhases = null;
    private MAPExtensionContainer extensionContainer = null;
    private OfferedCamel4CSIs offeredCamel4CSIs = null;
    private SupportedFeatures supportedFeatures = null;

    private long mapProtocolVersion;

    // For incoming messages
    public InsertSubscriberDataResponseImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    // For outgoing messages - MAP V2
    public InsertSubscriberDataResponseImpl(long mapProtocolVersion, ArrayList<ExtTeleserviceCode> teleserviceList,
            ArrayList<ExtBearerServiceCode> bearerServiceList, ArrayList<SSCode> ssList, ODBGeneralData odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse) {
        this.mapProtocolVersion = mapProtocolVersion;
        this.teleserviceList = teleserviceList;
        this.bearerServiceList = bearerServiceList;
        this.ssList = ssList;
        this.odbGeneralData = odbGeneralData;
        this.regionalSubscriptionResponse = regionalSubscriptionResponse;
    }

    // For outgoing messages - MAP V3
    public InsertSubscriberDataResponseImpl(long mapProtocolVersion, ArrayList<ExtTeleserviceCode> teleserviceList,
            ArrayList<ExtBearerServiceCode> bearerServiceList, ArrayList<SSCode> ssList, ODBGeneralData odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse, SupportedCamelPhases supportedCamelPhases,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIs, SupportedFeatures supportedFeatures) {

        this.mapProtocolVersion = mapProtocolVersion;
        this.teleserviceList = teleserviceList;
        this.bearerServiceList = bearerServiceList;
        this.ssList = ssList;
        this.odbGeneralData = odbGeneralData;
        this.regionalSubscriptionResponse = regionalSubscriptionResponse;

        if (mapProtocolVersion >= 3) {
            this.supportedCamelPhases = supportedCamelPhases;
            this.extensionContainer = extensionContainer;
            this.offeredCamel4CSIs = offeredCamel4CSIs;
            this.supportedFeatures = supportedFeatures;
        }
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.insertSubscriberData_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.insertSubscriberData;
    }

    @Override
    public ArrayList<ExtTeleserviceCode> getTeleserviceList() {
        return this.teleserviceList;
    }

    @Override
    public ArrayList<ExtBearerServiceCode> getBearerServiceList() {
        return this.bearerServiceList;
    }

    @Override
    public ArrayList<SSCode> getSSList() {
        return this.ssList;
    }

    @Override
    public ODBGeneralData getODBGeneralData() {
        return this.odbGeneralData;
    }

    @Override
    public RegionalSubscriptionResponse getRegionalSubscriptionResponse() {
        return this.regionalSubscriptionResponse;
    }

    @Override
    public SupportedCamelPhases getSupportedCamelPhases() {
        return this.supportedCamelPhases;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public OfferedCamel4CSIs getOfferedCamel4CSIs() {
        return this.offeredCamel4CSIs;
    }

    @Override
    public SupportedFeatures getSupportedFeatures() {
        return this.supportedFeatures;
    }

    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
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
        ExtTeleserviceCode teleserviceItem = null;

        this.teleserviceList = null;
        this.extensionContainer = null;
        this.bearerServiceList = null;
        this.ssList = null;
        this.odbGeneralData = null;
        this.regionalSubscriptionResponse = null;
        this.supportedCamelPhases = null;
        this.offeredCamel4CSIs = null;
        this.supportedFeatures = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case _TAG_teleserviceList: // teleserviceList
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".teleserviceList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.teleserviceList = new ArrayList<ExtTeleserviceCode>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL
                                        || !ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad teleserviceCode element tag or tagClass or is not primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                teleserviceItem = new ExtTeleserviceCodeImpl();
                                ((ExtTeleserviceCodeImpl) teleserviceItem).decodeAll(ais2);
                                this.teleserviceList.add(teleserviceItem);
                            }
                            if (this.teleserviceList.size() < 1 || this.teleserviceList.size() > 20) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter teleserviceList size must be from 1 to 20, found: "
                                        + this.teleserviceList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_bearerServiceList: // bearerServiceList
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".bearerServiceList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            ais2 = ais.readSequenceStream();
                            this.bearerServiceList = new ArrayList<ExtBearerServiceCode>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL
                                        || !ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad bearerServiceList element tag or tagClass or is not primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                ExtBearerServiceCode extBearerServiceCode = new ExtBearerServiceCodeImpl();
                                ((ExtBearerServiceCodeImpl) extBearerServiceCode).decodeAll(ais2);
                                this.bearerServiceList.add(extBearerServiceCode);
                            }
                            if (this.bearerServiceList.size() < 1 || this.bearerServiceList.size() > 50) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter bearerServiceList size must be from 1 to 50, found: "
                                        + this.bearerServiceList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_SS_List:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ssList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            ais2 = ais.readSequenceStream();
                            this.ssList = new ArrayList<SSCode>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                if (tag2 != Tag.STRING_OCTET || ais2.getTagClass() != Tag.CLASS_UNIVERSAL
                                        || !ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad ssListList element tag or tagClass or is not primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                SSCode ssCode = new SSCodeImpl();
                                ((SSCodeImpl) ssCode).decodeAll(ais2);
                                this.ssList.add(ssCode);
                            }
                            if (this.ssList.size() < 1 || this.ssList.size() > 30) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter ssList size must be from 1 to 30, found: " + this.ssList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;

                        case _TAG_odb_GeneralData:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".odbGeneralData: Parameter odbGeneralData is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.odbGeneralData = new ODBGeneralDataImpl();
                            ((ODBGeneralDataImpl) this.odbGeneralData).decodeAll(ais);
                            break;
                        case _TAG_regionalSubscriptionResponse:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".regionalSubscriptionResponse: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.regionalSubscriptionResponse = RegionalSubscriptionResponse.getInstance((int) ais
                                    .readInteger());
                            break;

                        case _TAG_supportedCamelPhases:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".supportedCamelPhases: Parameter supportedCamelPhases is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.supportedCamelPhases = new SupportedCamelPhasesImpl();
                            ((SupportedCamelPhasesImpl) this.supportedCamelPhases).decodeAll(ais);
                            break;

                        case _TAG_extContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter extensionContainer is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;

                        case _TAG_offeredCamel4CSIs:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".offeredCamel4CSIs: Parameter offeredCamel4CSIs is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.offeredCamel4CSIs = new OfferedCamel4CSIsImpl();
                            ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).decodeAll(ais);
                            break;

                        case _TAG_supportedFeatures:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".supportedFeatures: Parameter supportedFeatures is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.supportedFeatures = new SupportedFeaturesImpl();
                            ((SupportedFeaturesImpl) this.supportedFeatures).decodeAll(ais);
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

            num++;
        }

        if (num == 0)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Needs at least 1 parameter, found " + num, MAPParsingComponentExceptionReason.MistypedParameter);
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
        if (this.teleserviceList != null && (this.teleserviceList.size() < 1 || this.teleserviceList.size() > 20)) {
            throw new MAPException("teleserviceList size must be from 1 to 20, found: " + this.teleserviceList.size());
        }
        if (this.bearerServiceList != null && (this.bearerServiceList.size() < 1 || this.bearerServiceList.size() > 50)) {
            throw new MAPException("bearerServiceList size must be from 1 to 50, found: " + this.bearerServiceList.size());
        }
        if (this.ssList != null && (this.ssList.size() < 1 || this.ssList.size() > 30)) {
            throw new MAPException("ssList size must be from 1 to 30, found: " + this.ssList.size());
        }

        if (this.teleserviceList != null) {
            try {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_teleserviceList);
                int pos = asnOs.StartContentDefiniteLength();
                for (ExtTeleserviceCode teleserviceItem : this.teleserviceList) {
                    ((ExtTeleserviceCodeImpl) teleserviceItem).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding " + _PrimitiveName + ".teleserviceList: " + e.getMessage(),
                        e);
            }
        }
        if (this.bearerServiceList != null) {
            try {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_bearerServiceList);
                int pos = asnOs.StartContentDefiniteLength();
                for (ExtBearerServiceCode item : this.bearerServiceList) {
                    ((ExtBearerServiceCodeImpl) item).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            } catch (AsnException e) {
                throw new MAPException(
                        "AsnException when encoding " + _PrimitiveName + ".bearerServiceList: " + e.getMessage(), e);
            }
        }
        if (this.ssList != null) {
            try {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_SS_List);
                int pos = asnOs.StartContentDefiniteLength();
                for (SSCode item : this.ssList) {
                    ((SSCodeImpl) item).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding " + _PrimitiveName + ".ssList: " + e.getMessage(), e);
            }
        }
        if (this.odbGeneralData != null) {
            ((ODBGeneralDataImpl) this.odbGeneralData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_odb_GeneralData);
        }

        if (mapProtocolVersion >= 2) {
            if (this.regionalSubscriptionResponse != null) {
                try {
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_regionalSubscriptionResponse,
                            this.regionalSubscriptionResponse.getCode());
                } catch (IOException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName
                            + " parameter regionalSubscriptionResponse", e);
                } catch (AsnException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName
                            + " parameter regionalSubscriptionResponse", e);
                }
            }
        }

        if (mapProtocolVersion >= 3) {
            if (this.supportedCamelPhases != null) {
                ((SupportedCamelPhasesImpl) this.supportedCamelPhases).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_supportedCamelPhases);
            }
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extContainer);
            if (this.offeredCamel4CSIs != null) {
                ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_offeredCamel4CSIs);
            }
            if (this.supportedFeatures != null) {
                ((SupportedFeaturesImpl) this.supportedFeatures).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_supportedFeatures);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.teleserviceList != null) {
            sb.append("teleserviceList=[");
            boolean firstItem = true;
            for (ExtTeleserviceCode be : this.teleserviceList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.bearerServiceList != null) {
            sb.append("bearerServiceList=[");
            boolean firstItem = true;
            for (ExtBearerServiceCode be : this.bearerServiceList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.ssList != null) {
            sb.append("ssList=[");
            boolean firstItem = true;
            for (SSCode be : this.ssList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.odbGeneralData != null) {
            sb.append("odbGeneralData=");
            sb.append(odbGeneralData.toString());
            sb.append(", ");
        }

        if (this.regionalSubscriptionResponse != null) {
            sb.append("regionalSubscriptionResponse=");
            sb.append(regionalSubscriptionResponse.toString());
            sb.append(", ");
        }

        if (this.supportedCamelPhases != null) {
            sb.append("supportedCamelPhases=");
            sb.append(supportedCamelPhases.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }

        if (this.offeredCamel4CSIs != null) {
            sb.append("offeredCamel4CSIs=");
            sb.append(offeredCamel4CSIs.toString());
            sb.append(", ");
        }

        if (this.supportedFeatures != null) {
            sb.append("supportedFeatures=");
            sb.append(supportedFeatures.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}
