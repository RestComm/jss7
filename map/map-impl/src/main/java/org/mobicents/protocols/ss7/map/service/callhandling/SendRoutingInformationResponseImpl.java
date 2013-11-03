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
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.mobicents.protocols.ss7.map.api.service.callhandling.AllowedServices;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CCBSIndicators;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.mobicents.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.NAEAPreferredCIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;

/*
 *
 * @author cristian veliscu
 *
 */
public class SendRoutingInformationResponseImpl extends CallHandlingMessageImpl implements SendRoutingInformationResponse {
    private IMSI imsi;
    private ExtendedRoutingInfo extRoutingInfo;
    private CUGCheckInfo cugCheckInfo;
    private boolean cugSubscriptionFlag;
    private SubscriberInfo subscriberInfo;
    private ArrayList<SSCode> ssList;
    private ExtBasicServiceCode basicService;
    private boolean forwardingInterrogationRequired;
    private ISDNAddressString vmscAddress;
    private MAPExtensionContainer extensionContainer;
    private NAEAPreferredCI naeaPreferredCI;
    private CCBSIndicators ccbsIndicators;
    private ISDNAddressString msisdn;
    private NumberPortabilityStatus nrPortabilityStatus;
    private Integer istAlertTimer;
    private SupportedCamelPhases supportedCamelPhases;
    private OfferedCamel4CSIs offeredCamel4CSIs;
    private RoutingInfo routingInfo2;
    private ArrayList<SSCode> ssList2;
    private ExtBasicServiceCode basicService2;
    private AllowedServices allowedServices;
    private UnavailabilityCause unavailabilityCause;
    private boolean releaseResourcesSupported;
    private ExternalSignalInfo gsmBearerCapability;
    private long mapProtocolVersion;

    public static final int TAG_sendRoutingInfoRes = 3;
    private static final int TAG_imsi = 9;
    private static final int TAG_cugCheckInfo = 3;
    private static final int TAG_cugSubscriptionFlag = 6;
    private static final int TAG_subscriberInfo = 7;
    private static final int TAG_ssList = 1;
    private static final int TAG_basicService = 5;
    private static final int TAG_forwardingInterrogationRequired = 4;
    private static final int TAG_vmscAddress = 2;
    private static final int TAG_extensionContainer = 0;
    private static final int TAG_naeaPreferredCI = 10;
    private static final int TAG_ccbsIndicators = 11;
    private static final int TAG_msisdn = 12;
    private static final int TAG_numberPortabilityStatus = 13;
    private static final int TAG_istAlertTimer = 14;
    private static final int TAG_supportedCamelPhasesInVMSC = 15;
    private static final int TAG_offeredCamel4CSIsInVMSC = 16;
    private static final int TAG_routingInfo2 = 17;
    private static final int TAG_ssList2 = 18;
    private static final int TAG_basicService2 = 19;
    private static final int TAG_allowedServices = 20;
    private static final int TAG_unavailabilityCause = 21;
    private static final int TAG_releaseResourcesSupported = 22;
    private static final int TAG_gsmBearerCapability = 23;

    // private static final int TAG_camelRoutingInfo = 8;

    private static final String _PrimitiveName = "SendRoutingInformationResponse";

    public SendRoutingInformationResponseImpl() {
        this(3);
    }

    public SendRoutingInformationResponseImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendRoutingInformationResponseImpl(IMSI imsi, ExtendedRoutingInfo extRoutingInfo, SubscriberInfo subscriberInfo,
            MAPExtensionContainer extensionContainer) {
        this(3, imsi, extRoutingInfo, subscriberInfo, extensionContainer);
    }

    public SendRoutingInformationResponseImpl(long mapProtocolVersion, IMSI imsi, ExtendedRoutingInfo extRoutingInfo,
            SubscriberInfo subscriberInfo, MAPExtensionContainer extensionContainer) {
        this.imsi = imsi;
        this.extRoutingInfo = extRoutingInfo;
        this.subscriberInfo = subscriberInfo;
        this.extensionContainer = extensionContainer;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendRoutingInformationResponseImpl(long mapProtocolVersion, IMSI imsi, ExtendedRoutingInfo extRoutingInfo,
            CUGCheckInfo cugCheckInfo, boolean cugSubscriptionFlag, SubscriberInfo subscriberInfo, ArrayList<SSCode> ssList,
            ExtBasicServiceCode basicService, boolean forwardingInterrogationRequired, ISDNAddressString vmscAddress,
            MAPExtensionContainer extensionContainer, NAEAPreferredCI naeaPreferredCI, CCBSIndicators ccbsIndicators,
            ISDNAddressString msisdn, NumberPortabilityStatus nrPortabilityStatus, Integer istAlertTimer,
            SupportedCamelPhases supportedCamelPhases, OfferedCamel4CSIs offeredCamel4CSIs, RoutingInfo routingInfo2,
            ArrayList<SSCode> ssList2, ExtBasicServiceCode basicService2, AllowedServices allowedServices,
            UnavailabilityCause unavailabilityCause, boolean releaseResourcesSupported, ExternalSignalInfo gsmBearerCapability) {

        this.imsi = imsi;
        this.extRoutingInfo = extRoutingInfo;
        this.cugCheckInfo = cugCheckInfo;
        this.cugSubscriptionFlag = cugSubscriptionFlag;
        this.subscriberInfo = subscriberInfo;
        this.ssList = ssList;
        this.basicService = basicService;
        this.forwardingInterrogationRequired = forwardingInterrogationRequired;
        this.vmscAddress = vmscAddress;
        this.extensionContainer = extensionContainer;
        this.naeaPreferredCI = naeaPreferredCI;
        this.ccbsIndicators = ccbsIndicators;
        this.msisdn = msisdn;
        this.nrPortabilityStatus = nrPortabilityStatus;
        this.istAlertTimer = istAlertTimer;
        this.supportedCamelPhases = supportedCamelPhases;
        this.offeredCamel4CSIs = offeredCamel4CSIs;
        this.routingInfo2 = routingInfo2;
        this.ssList2 = ssList2;
        this.basicService2 = basicService2;
        this.allowedServices = allowedServices;
        this.unavailabilityCause = unavailabilityCause;
        this.releaseResourcesSupported = releaseResourcesSupported;
        this.gsmBearerCapability = gsmBearerCapability;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public long getMapProtocolVersion() {
        return mapProtocolVersion;
    }

    @Override
    public IMSI getIMSI() {
        return this.imsi;
    }

    @Override
    public ExtendedRoutingInfo getExtendedRoutingInfo() {
        return this.extRoutingInfo;
    }

    @Override
    public CUGCheckInfo getCUGCheckInfo() {
        return this.cugCheckInfo;
    }

    @Override
    public boolean getCUGSubscriptionFlag() {
        return this.cugSubscriptionFlag;
    }

    @Override
    public SubscriberInfo getSubscriberInfo() {
        return this.subscriberInfo;
    }

    @Override
    public ArrayList<SSCode> getSSList() {
        return this.ssList;
    }

    @Override
    public ExtBasicServiceCode getBasicService() {
        return this.basicService;
    }

    @Override
    public boolean getForwardingInterrogationRequired() {
        return this.forwardingInterrogationRequired;
    }

    @Override
    public ISDNAddressString getVmscAddress() {
        return this.vmscAddress;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public NAEAPreferredCI getNaeaPreferredCI() {
        return this.naeaPreferredCI;
    }

    @Override
    public CCBSIndicators getCCBSIndicators() {
        return this.ccbsIndicators;
    }

    @Override
    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    @Override
    public NumberPortabilityStatus getNumberPortabilityStatus() {
        return this.nrPortabilityStatus;
    }

    @Override
    public Integer getISTAlertTimer() {
        return this.istAlertTimer;
    }

    @Override
    public SupportedCamelPhases getSupportedCamelPhasesInVMSC() {
        return this.supportedCamelPhases;
    }

    @Override
    public OfferedCamel4CSIs getOfferedCamel4CSIsInVMSC() {
        return this.offeredCamel4CSIs;
    }

    @Override
    public RoutingInfo getRoutingInfo2() {
        return this.routingInfo2;
    }

    @Override
    public ArrayList<SSCode> getSSList2() {
        return this.ssList2;
    }

    @Override
    public ExtBasicServiceCode getBasicService2() {
        return this.basicService2;
    }

    @Override
    public AllowedServices getAllowedServices() {
        return this.allowedServices;
    }

    @Override
    public UnavailabilityCause getUnavailabilityCause() {
        return this.unavailabilityCause;
    }

    @Override
    public boolean getReleaseResourcesSupported() {
        return this.releaseResourcesSupported;
    }

    @Override
    public ExternalSignalInfo getGsmBearerCapability() {
        return this.gsmBearerCapability;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfo_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfo;
    }

    @Override
    public int getTag() throws MAPException {
        if (this.mapProtocolVersion >= 3) {
            return TAG_sendRoutingInfoRes;
        } else {
            return Tag.SEQUENCE;
        }
    }

    @Override
    public int getTagClass() {
        if (this.mapProtocolVersion >= 3) {
            return Tag.CLASS_CONTEXT_SPECIFIC;
        } else {
            return Tag.CLASS_UNIVERSAL;
        }
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
            throw new MAPParsingComponentException("IOException when decoding SendRoutingInformationResponse: ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding SendRoutingInformationResponse: ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding SendRoutingInformationResponse: ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding SendRoutingInformationResponse: ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.imsi = null;
        this.extRoutingInfo = null;
        this.cugCheckInfo = null;
        this.cugSubscriptionFlag = false;
        this.subscriberInfo = null;
        this.ssList = null;
        this.basicService = null;
        this.forwardingInterrogationRequired = false;
        this.vmscAddress = null;
        this.extensionContainer = null;
        this.naeaPreferredCI = null;
        this.ccbsIndicators = null;
        this.msisdn = null;
        this.nrPortabilityStatus = null;
        this.istAlertTimer = null;
        this.supportedCamelPhases = null;
        this.offeredCamel4CSIs = null;
        this.routingInfo2 = null;
        this.ssList2 = null;
        this.basicService2 = null;
        this.allowedServices = null;
        this.unavailabilityCause = null;
        this.releaseResourcesSupported = false;
        this.gsmBearerCapability = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        if (this.mapProtocolVersion < 3) {
            while (true) {
                if (ais.available() == 0)
                    break;

                int tag = ais.readTag();

                switch (num) {
                    case 0:
                        // imsi
                        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || tag != Tag.STRING_OCTET || !ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter 0 bad tag, tag class or not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.imsi = new IMSIImpl();
                        ((IMSIImpl) this.imsi).decodeAll(ais);
                        break;

                    case 1:
                        // RoutingInfo
                        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || (tag != Tag.STRING_OCTET && tag != Tag.SEQUENCE))
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter 1 bad tag or tag class",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.routingInfo2 = new RoutingInfoImpl();
                        ((RoutingInfoImpl) this.routingInfo2).decodeAll(ais);
                        break;

                    default:
                        if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL && !ais.isTagPrimitive()) {
                            this.cugCheckInfo = new CUGCheckInfoImpl();
                            ((CUGCheckInfoImpl) this.cugCheckInfo).decodeAll(ais);
                            break;
                        } else {
                            ais.advanceElement();
                        }
                        break;
                }
                num++;
            }

            if (num < 2)
                throw new MAPParsingComponentException(
                        "Error while decoding forwardShortMessageRequest: Needs at least 2 mandatory parameters, found " + num,
                        MAPParsingComponentExceptionReason.MistypedParameter);
        } else {
            while (true) {
                if (ais.available() == 0)
                    break;

                int tag = ais.readTag();
                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                    switch (tag) {
                        case Tag.STRING_OCTET:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extRoutingInfo: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.extRoutingInfo = new ExtendedRoutingInfoImpl();
                            ((ExtendedRoutingInfoImpl) this.extRoutingInfo).decodeAll(ais);
                            break;
                        case Tag.SEQUENCE:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extRoutingInfo: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.extRoutingInfo = new ExtendedRoutingInfoImpl();
                            ((ExtendedRoutingInfoImpl) this.extRoutingInfo).decodeAll(ais);
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    switch (tag) {
                        case TAG_imsi:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".imsi: is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.imsi = new IMSIImpl();
                            ((IMSIImpl) this.imsi).decodeAll(ais);
                            break;
                        case ExtendedRoutingInfoImpl.TAG_camel:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extRoutingInfo: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.extRoutingInfo = new ExtendedRoutingInfoImpl();
                            ((ExtendedRoutingInfoImpl) this.extRoutingInfo).decodeAll(ais);
                            break;
                        case TAG_cugCheckInfo:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".cugCheckInfo: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.cugCheckInfo = new CUGCheckInfoImpl();
                            ((CUGCheckInfoImpl) this.cugCheckInfo).decodeAll(ais);
                            break;
                        case TAG_cugSubscriptionFlag:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".cugSubscriptionFlag: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            ais.readNull();
                            this.cugSubscriptionFlag = true;
                            break;
                        case TAG_subscriberInfo:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".subscriberInfo: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.subscriberInfo = new SubscriberInfoImpl();
                            ((SubscriberInfoImpl) this.subscriberInfo).decodeAll(ais);
                            break;
                        case TAG_ssList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ssList: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream aissSCd = ais.readSequenceStream();
                            SSCode sSCd = null;
                            this.ssList = new ArrayList<SSCode>();
                            while (true) {
                                if (aissSCd.available() == 0)
                                    break;

                                int tag2 = aissSCd.readTag();
                                if (tag2 != Tag.STRING_OCTET || aissSCd.getTagClass() != Tag.CLASS_UNIVERSAL
                                        || !aissSCd.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad tag or tagClass or is not primitive when decoding ssList2",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                sSCd = new SSCodeImpl();
                                ((SSCodeImpl) sSCd).decodeAll(aissSCd);
                                this.ssList.add(sSCd);
                            }
                            if (this.ssList.size() < 1 && this.ssList.size() > 10) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter ssList size must be from 1 to 10, found: " + this.ssList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case TAG_basicService: // explicit tag encoding
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".basicService: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            AsnInputStream ais1 = ais.readSequenceStream();
                            ais1.readTag();
                            this.basicService = new ExtBasicServiceCodeImpl();
                            ((ExtBasicServiceCodeImpl) this.basicService).decodeAll(ais1);
                            break;
                        case TAG_forwardingInterrogationRequired:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".forwardingInterrogationRequired: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            ais.readNull();
                            this.forwardingInterrogationRequired = true;
                            break;
                        case TAG_vmscAddress:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".vmscAddress: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.vmscAddress = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.vmscAddress).decodeAll(ais);
                            break;
                        case TAG_extensionContainer:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;
                        case TAG_naeaPreferredCI:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".naeaPreferredCI: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.naeaPreferredCI = new NAEAPreferredCIImpl();
                            ((NAEAPreferredCIImpl) this.naeaPreferredCI).decodeAll(ais);
                            break;
                        case TAG_ccbsIndicators:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ccbsIndicators: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.ccbsIndicators = new CCBSIndicatorsImpl();
                            ((CCBSIndicatorsImpl) this.ccbsIndicators).decodeAll(ais);
                            break;
                        case TAG_msisdn:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".msisdn: is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.msisdn = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.msisdn).decodeAll(ais);
                            break;
                        case TAG_numberPortabilityStatus:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".nrPortabilityStatus: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            int type = (int) ais.readInteger();
                            this.nrPortabilityStatus = NumberPortabilityStatus.getInstance(type);
                            break;
                        case TAG_istAlertTimer:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".istAlertTimer: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.istAlertTimer = (int) ais.readInteger();
                            break;
                        case TAG_supportedCamelPhasesInVMSC:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".supportedCamelPhases: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.supportedCamelPhases = new SupportedCamelPhasesImpl();
                            ((SupportedCamelPhasesImpl) this.supportedCamelPhases).decodeAll(ais);
                            break;
                        case TAG_offeredCamel4CSIsInVMSC:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".offeredCamel4CSIs: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.offeredCamel4CSIs = new OfferedCamel4CSIsImpl();
                            ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).decodeAll(ais);
                            break;
                        case TAG_routingInfo2:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".routingInfo2: primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais0 = ais.readSequenceStream();
                            ais0.readTag();
                            this.routingInfo2 = new RoutingInfoImpl();
                            ((RoutingInfoImpl) this.routingInfo2).decodeAll(ais0);
                            break;
                        case TAG_ssList2:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ssEventList: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream aissSCode = ais.readSequenceStream();
                            SSCode sSCode = null;
                            this.ssList2 = new ArrayList<SSCode>();
                            while (true) {
                                if (aissSCode.available() == 0)
                                    break;

                                int tag2 = aissSCode.readTag();
                                if (tag2 != Tag.STRING_OCTET || aissSCode.getTagClass() != Tag.CLASS_UNIVERSAL
                                        || !aissSCode.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad tag or tagClass or is not primitive when decoding ssList2",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                sSCode = new SSCodeImpl();
                                ((SSCodeImpl) sSCode).decodeAll(aissSCode);
                                this.ssList2.add(sSCode);
                            }
                            if (this.ssList2.size() < 1 && this.ssList2.size() > 10) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter ssList size must be from 1 to 10, found: " + this.ssList2.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case TAG_basicService2: // explicit tag encoding
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".basicService2: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            AsnInputStream ais2 = ais.readSequenceStream();
                            ais2.readTag();
                            this.basicService2 = new ExtBasicServiceCodeImpl();
                            ((ExtBasicServiceCodeImpl) this.basicService2).decodeAll(ais2);
                            break;
                        case TAG_allowedServices:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".allowedServices: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.allowedServices = new AllowedServicesImpl();
                            ((AllowedServicesImpl) this.allowedServices).decodeAll(ais);
                            break;
                        case TAG_unavailabilityCause:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".unavailabilityCause: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            int code = (int) ais.readInteger();
                            this.unavailabilityCause = UnavailabilityCause.getUnavailabilityCause(code);
                            break;
                        case TAG_releaseResourcesSupported:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".releaseResourcesSupported: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            ais.readNull();
                            this.releaseResourcesSupported = true;
                            break;
                        case TAG_gsmBearerCapability:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gsmBearerCapability: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.gsmBearerCapability = new ExternalSignalInfoImpl();
                            ((ExternalSignalInfoImpl) this.gsmBearerCapability).decodeAll(ais);
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
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding SendRoutingInformationResponse: " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.mapProtocolVersion < 3) {

            if (this.imsi == null)
                throw new MAPException("Error while encoding " + _PrimitiveName
                        + " the mandatory parameter imsi is not defined");

            if (this.routingInfo2 == null)
                throw new MAPException("Error while encoding " + _PrimitiveName
                        + " the mandatory parameter routingInfo2 is not defined");

            ((IMSIImpl) this.imsi).encodeAll(asnOs);

            ((RoutingInfoImpl) this.routingInfo2).encodeAll(asnOs);

            if (this.mapProtocolVersion >= 2) {
                if (this.cugCheckInfo != null) {
                    ((CUGCheckInfoImpl) this.cugCheckInfo).encodeAll(asnOs);
                }
            }

        } else {
            try {
                if (this.imsi != null)
                    ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_imsi);

                if (this.extRoutingInfo != null) { // Universal TAG class here
                    ((ExtendedRoutingInfoImpl) this.extRoutingInfo).encodeAll(asnOs);
                }

                if (this.cugCheckInfo != null) {
                    ((CUGCheckInfoImpl) this.cugCheckInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_cugCheckInfo);
                }

                if (this.cugSubscriptionFlag)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_cugSubscriptionFlag);

                if (this.subscriberInfo != null)
                    ((SubscriberInfoImpl) this.subscriberInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_subscriberInfo);

                if (this.ssList != null) {
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_ssList);
                    int pos = asnOs.StartContentDefiniteLength();
                    for (SSCode sSCode : this.ssList) {
                        ((SSCodeImpl) sSCode).encodeAll(asnOs);
                    }
                    asnOs.FinalizeContent(pos);
                }

                if (this.basicService != null) { // explicit tag encoding
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_basicService);
                    int pos = asnOs.StartContentDefiniteLength();
                    ((ExtBasicServiceCodeImpl) this.basicService).encodeAll(asnOs);
                    asnOs.FinalizeContent(pos);
                }

                if (this.forwardingInterrogationRequired)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_forwardingInterrogationRequired);

                if (this.vmscAddress != null)
                    ((ISDNAddressStringImpl) this.vmscAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_vmscAddress);

                if (this.extensionContainer != null)
                    ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            TAG_extensionContainer);

                if (this.naeaPreferredCI != null) {
                    ((NAEAPreferredCIImpl) this.naeaPreferredCI).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            TAG_naeaPreferredCI);
                }

                if (this.ccbsIndicators != null) {
                    ((CCBSIndicatorsImpl) this.ccbsIndicators).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_ccbsIndicators);
                }

                if (this.msisdn != null)
                    ((ISDNAddressStringImpl) this.msisdn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_msisdn);

                if (this.nrPortabilityStatus != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_numberPortabilityStatus,
                            this.nrPortabilityStatus.getType());

                if (this.istAlertTimer != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_istAlertTimer, this.istAlertTimer);

                if (this.supportedCamelPhases != null)
                    ((SupportedCamelPhasesImpl) this.supportedCamelPhases).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            TAG_supportedCamelPhasesInVMSC);

                if (this.offeredCamel4CSIs != null)
                    ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            TAG_offeredCamel4CSIsInVMSC);

                if (this.routingInfo2 != null) { // explicit tag encoding
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_routingInfo2);
                    int pos = asnOs.StartContentDefiniteLength();
                    ((RoutingInfoImpl) this.routingInfo2).encodeAll(asnOs);
                    asnOs.FinalizeContent(pos);
                }

                if (this.ssList2 != null) {
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_ssList2);
                    int pos = asnOs.StartContentDefiniteLength();
                    for (SSCode sSCode : this.ssList2) {
                        ((SSCodeImpl) sSCode).encodeAll(asnOs);
                    }
                    asnOs.FinalizeContent(pos);
                }

                if (this.basicService2 != null) {
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_basicService2);
                    int pos = asnOs.StartContentDefiniteLength();
                    ((ExtBasicServiceCodeImpl) this.basicService2).encodeAll(asnOs);
                    asnOs.FinalizeContent(pos);
                }

                if (this.allowedServices != null)
                    ((AllowedServicesImpl) this.allowedServices).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            TAG_allowedServices);

                if (this.unavailabilityCause != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_unavailabilityCause, this.unavailabilityCause.getCode());

                if (this.releaseResourcesSupported)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_releaseResourcesSupported);

                if (this.gsmBearerCapability != null)
                    ((ExternalSignalInfoImpl) this.gsmBearerCapability).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            TAG_gsmBearerCapability);

            } catch (IOException e) {
                throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(this.imsi);
        }

        if (this.extRoutingInfo != null) {
            sb.append(", extRoutingInfo=");
            sb.append(this.extRoutingInfo);
        }

        if (this.cugCheckInfo != null) {
            sb.append(", cugCheckInfo=");
            sb.append(this.cugCheckInfo);
        }

        if (this.cugSubscriptionFlag != false)
            sb.append(", cugSubscriptionFlag=TRUE");

        if (this.subscriberInfo != null) {
            sb.append(", subscriberInfo=");
            sb.append(this.subscriberInfo);
        }

        if (this.ssList != null) {
            sb.append(", ssList=");
            sb.append(this.ssList);
        }

        if (this.basicService != null) {
            sb.append(", basicService=");
            sb.append(this.basicService);
        }

        if (this.forwardingInterrogationRequired != false)
            sb.append(", forwardingInterrogationRequired=TRUE");

        if (this.vmscAddress != null) {
            sb.append(", vmscAddress=");
            sb.append(this.vmscAddress);
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        if (this.naeaPreferredCI != null) {
            sb.append(", naeaPreferredCI=");
            sb.append(this.naeaPreferredCI);
        }

        if (this.ccbsIndicators != null) {
            sb.append(", ccbsIndicators=");
            sb.append(this.ccbsIndicators);
        }

        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(this.msisdn);
        }

        if (this.nrPortabilityStatus != null) {
            sb.append(", nrPortabilityStatus=");
            sb.append(this.nrPortabilityStatus);
        }

        if (this.istAlertTimer != null) {
            sb.append(", istAlertTimer=");
            sb.append(this.istAlertTimer);
        }

        if (this.supportedCamelPhases != null) {
            sb.append(", supportedCamelPhases=");
            sb.append(this.supportedCamelPhases);
        }

        if (this.offeredCamel4CSIs != null) {
            sb.append(", offeredCamel4CSIs=");
            sb.append(this.offeredCamel4CSIs);
        }

        if (this.routingInfo2 != null) {
            sb.append(", routingInfo2=");
            sb.append(this.routingInfo2);
        }

        if (this.ssList2 != null) {
            sb.append(", ssList2=");
            sb.append(this.ssList2);
        }

        if (this.basicService2 != null) {
            sb.append(", basicService2=");
            sb.append(this.basicService2);
        }

        if (this.allowedServices != null) {
            sb.append(", allowedServices=");
            sb.append(this.allowedServices);
        }

        if (this.unavailabilityCause != null) {
            sb.append(", unavailabilityCause=");
            sb.append(this.unavailabilityCause);
        }

        if (this.releaseResourcesSupported != false)
            sb.append(", releaseResourcesSupported=TRUE");

        if (this.gsmBearerCapability != null) {
            sb.append(", gsmBearerCapability=");
            sb.append(this.gsmBearerCapability);
        }

        sb.append("]");
        return sb.toString();
    }
}