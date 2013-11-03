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
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CamelInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SuppressMTSS;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.primitives.ExtExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;

/*
 *
 * @author cristian veliscu
 *
 */
public class SendRoutingInformationRequestImpl extends CallHandlingMessageImpl implements SendRoutingInformationRequest {
    private ISDNAddressString msisdn;
    private CUGCheckInfo cugCheckInfo;
    private Integer numberOfForwarding;
    private InterrogationType interrogationType;
    private boolean orInterrogation;
    private Integer orCapability;
    private ISDNAddressString gmscAddress;
    private CallReferenceNumber callReferenceNumber;
    private ForwardingReason forwardingReason;
    private ExtBasicServiceCode basicServiceGroup;
    private ExternalSignalInfo networkSignalInfo;
    private CamelInfo camelInfo;
    private boolean suppressionOfAnnouncement;
    private MAPExtensionContainer extensionContainer;
    private AlertingPattern alertingPattern;
    private boolean ccbsCall;
    private Integer supportedCCBSPhase;
    private ExtExternalSignalInfo additionalSignalInfo;
    private ISTSupportIndicator istSupportIndicator;
    private boolean prePagingSupported;
    private CallDiversionTreatmentIndicator callDiversionTreatmentIndicator;
    private boolean longFTNSupported;
    private boolean suppressVtCSI;
    private boolean suppressIncomingCallBarring;
    private boolean gsmSCFInitiatedCall;
    private ExtBasicServiceCode basicServiceGroup2;
    private ExternalSignalInfo networkSignalInfo2;
    private SuppressMTSS suppressMTSS;
    private boolean mtRoamingRetrySupported;
    private EMLPPPriority callPriority;
    private long mapProtocolVersion;

    private static final int TAG_msisdn = 0;
    private static final int TAG_cugCheckInfo = 1;
    private static final int TAG_numberOfForwarding = 2;
    private static final int TAG_interrogationType = 3;
    private static final int TAG_orInterrogation = 4;
    private static final int TAG_orCapability = 5;
    private static final int TAG_gmscOrGsmSCFAddress = 6;
    private static final int TAG_callReferenceNumber = 7;
    private static final int TAG_forwardingReason = 8;
    private static final int TAG_basicServiceGroup = 9;
    private static final int TAG_networkSignalInfo = 10;
    private static final int TAG_camelInfo = 11;
    private static final int TAG_suppressionOfAnnouncement = 12;
    private static final int TAG_extensionContainer = 13;
    private static final int TAG_alertingPattern = 14;
    private static final int TAG_ccbsCall = 15;
    private static final int TAG_supportedCCBSPhase = 16;
    private static final int TAG_additionalSignalInfo = 17;
    private static final int TAG_istSupportIndicator = 18;
    private static final int TAG_prePagingSupported = 19;
    private static final int TAG_callDiversionTreatmentIndicator = 20;
    private static final int TAG_longFTNSupported = 21;
    private static final int TAG_suppress_VT_CSI = 22;
    private static final int TAG_suppressIncomingCallBarring = 23;
    private static final int TAG_gsmSCFInitiatedCall = 24;
    private static final int TAG_basicServiceGroup2 = 25;
    private static final int TAG_networkSignalInfo2 = 26;
    private static final int TAG_suppressMTSS = 27;
    private static final int TAG_mtRoamingRetrySupported = 28;
    private static final int TAG_callPriority = 29;

    private static final String _PrimitiveName = "SendRoutingInformationRequest";

    public SendRoutingInformationRequestImpl() {
        this(3);
    }

    public SendRoutingInformationRequestImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendRoutingInformationRequestImpl(ISDNAddressString msisdn, ISDNAddressString gmscAddress,
            InterrogationType interrogationType, MAPExtensionContainer extensionContainer) {
        this(3, msisdn, gmscAddress, interrogationType, extensionContainer);
    }

    public SendRoutingInformationRequestImpl(long mapProtocolVersion, ISDNAddressString msisdn, ISDNAddressString gmscAddress,
            InterrogationType interrogationType, MAPExtensionContainer extensionContainer) {
        this.msisdn = msisdn;
        this.gmscAddress = gmscAddress;
        this.interrogationType = interrogationType;
        this.extensionContainer = extensionContainer;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendRoutingInformationRequestImpl(long mapProtocolVersion, ISDNAddressString msisdn, CUGCheckInfo cugCheckInfo,
            Integer numberOfForwarding, InterrogationType interrogationType, boolean orInterrogation, Integer orCapability,
            ISDNAddressString gmscAddress, CallReferenceNumber callReferenceNumber, ForwardingReason forwardingReason,
            ExtBasicServiceCode basicServiceGroup, ExternalSignalInfo networkSignalInfo, CamelInfo camelInfo,
            boolean suppressionOfAnnouncement, MAPExtensionContainer extensionContainer, AlertingPattern alertingPattern,
            boolean ccbsCall, Integer supportedCCBSPhase, ExtExternalSignalInfo additionalSignalInfo,
            ISTSupportIndicator istSupportIndicator, boolean prePagingSupported,
            CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, boolean longFTNSupported, boolean suppressVtCSI,
            boolean suppressIncomingCallBarring, boolean gsmSCFInitiatedCall, ExtBasicServiceCode basicServiceGroup2,
            ExternalSignalInfo networkSignalInfo2, SuppressMTSS suppressMTSS, boolean mtRoamingRetrySupported,
            EMLPPPriority callPriority) {

        if (mapProtocolVersion >= 3) {

            this.orInterrogation = orInterrogation;
            this.orCapability = orCapability;
            this.callReferenceNumber = callReferenceNumber;
            this.forwardingReason = forwardingReason;
            this.basicServiceGroup = basicServiceGroup;
            this.camelInfo = camelInfo;
            this.suppressionOfAnnouncement = suppressionOfAnnouncement;
            this.alertingPattern = alertingPattern;
            this.ccbsCall = ccbsCall;
            this.supportedCCBSPhase = supportedCCBSPhase;
            this.additionalSignalInfo = additionalSignalInfo;
            this.istSupportIndicator = istSupportIndicator;
            this.prePagingSupported = prePagingSupported;
            this.callDiversionTreatmentIndicator = callDiversionTreatmentIndicator;
            this.longFTNSupported = longFTNSupported;
            this.suppressVtCSI = suppressVtCSI;
            this.suppressIncomingCallBarring = suppressIncomingCallBarring;
            this.gsmSCFInitiatedCall = gsmSCFInitiatedCall;
            this.basicServiceGroup2 = basicServiceGroup2;
            this.networkSignalInfo2 = networkSignalInfo2;
            this.suppressMTSS = suppressMTSS;
            this.mtRoamingRetrySupported = mtRoamingRetrySupported;
            this.callPriority = callPriority;
            this.interrogationType = interrogationType;
            this.gmscAddress = gmscAddress;
            this.extensionContainer = extensionContainer;
        }

        this.msisdn = msisdn;
        this.cugCheckInfo = cugCheckInfo;
        this.numberOfForwarding = numberOfForwarding;
        this.networkSignalInfo = networkSignalInfo;
        this.mapProtocolVersion = mapProtocolVersion;

    }

    public long getMapProtocolVersion() {
        return mapProtocolVersion;
    }

    @Override
    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    @Override
    public CUGCheckInfo getCUGCheckInfo() {
        return this.cugCheckInfo;
    }

    @Override
    public Integer getNumberOfForwarding() {
        return this.numberOfForwarding;
    }

    @Override
    public InterrogationType getInterogationType() {
        return this.interrogationType;
    }

    @Override
    public boolean getORInterrogation() {
        return this.orInterrogation;
    }

    @Override
    public Integer getORCapability() {
        return this.orCapability;
    }

    @Override
    public ISDNAddressString getGmscOrGsmSCFAddress() {
        return this.gmscAddress;
    }

    @Override
    public CallReferenceNumber getCallReferenceNumber() {
        return this.callReferenceNumber;
    }

    @Override
    public ForwardingReason getForwardingReason() {
        return this.forwardingReason;
    }

    @Override
    public ExtBasicServiceCode getBasicServiceGroup() {
        return this.basicServiceGroup;
    }

    @Override
    public ExternalSignalInfo getNetworkSignalInfo() {
        return this.networkSignalInfo;
    }

    @Override
    public CamelInfo getCamelInfo() {
        return this.camelInfo;
    }

    @Override
    public boolean getSuppressionOfAnnouncement() {
        return this.suppressionOfAnnouncement;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public AlertingPattern getAlertingPattern() {
        return this.alertingPattern;
    }

    @Override
    public boolean getCCBSCall() {
        return this.ccbsCall;
    }

    @Override
    public Integer getSupportedCCBSPhase() {
        return this.supportedCCBSPhase;
    }

    @Override
    public ExtExternalSignalInfo getAdditionalSignalInfo() {
        return this.additionalSignalInfo;
    }

    @Override
    public ISTSupportIndicator getIstSupportIndicator() {
        return this.istSupportIndicator;
    }

    @Override
    public boolean getPrePagingSupported() {
        return this.prePagingSupported;
    }

    @Override
    public CallDiversionTreatmentIndicator getCallDiversionTreatmentIndicator() {
        return this.callDiversionTreatmentIndicator;
    }

    @Override
    public boolean getLongFTNSupported() {
        return this.longFTNSupported;
    }

    @Override
    public boolean getSuppressVtCSI() {
        return this.suppressVtCSI;
    }

    @Override
    public boolean getSuppressIncomingCallBarring() {
        return this.suppressIncomingCallBarring;
    }

    @Override
    public boolean getGsmSCFInitiatedCall() {
        return this.gsmSCFInitiatedCall;
    }

    @Override
    public ExtBasicServiceCode getBasicServiceGroup2() {
        return this.basicServiceGroup2;
    }

    @Override
    public ExternalSignalInfo getNetworkSignalInfo2() {
        return this.networkSignalInfo2;
    }

    @Override
    public SuppressMTSS getSuppressMTSS() {
        return this.suppressMTSS;
    }

    @Override
    public boolean getMTRoamingRetrySupported() {
        return this.mtRoamingRetrySupported;
    }

    @Override
    public EMLPPPriority getCallPriority() {
        return this.callPriority;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfo_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfo;
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
            throw new MAPParsingComponentException("IOException when decoding SendRoutingInformationRequest: ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding SendRoutingInformationRequest: ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding SendRoutingInformationRequest: ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding SendRoutingInformationRequest: ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.msisdn = null;
        this.cugCheckInfo = null;
        this.numberOfForwarding = null;
        this.interrogationType = null;
        this.orInterrogation = false;
        this.orCapability = null;
        this.gmscAddress = null;
        this.callReferenceNumber = null;
        this.forwardingReason = null;
        this.basicServiceGroup = null;
        this.networkSignalInfo = null;
        this.camelInfo = null;
        this.suppressionOfAnnouncement = false;
        this.extensionContainer = null;
        this.alertingPattern = null;
        this.ccbsCall = false;
        this.supportedCCBSPhase = null;
        this.additionalSignalInfo = null;
        this.istSupportIndicator = null;
        this.prePagingSupported = false;
        this.callDiversionTreatmentIndicator = null;
        this.longFTNSupported = false;
        this.suppressVtCSI = false;
        this.suppressIncomingCallBarring = false;
        this.gsmSCFInitiatedCall = false;
        this.basicServiceGroup2 = null;
        this.networkSignalInfo2 = null;
        this.suppressMTSS = null;
        this.mtRoamingRetrySupported = false;
        this.callPriority = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case TAG_msisdn:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".msisdn: is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.msisdn = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.msisdn).decodeAll(ais);
                            break;
                        case TAG_cugCheckInfo:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".cugCheckInfo: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.cugCheckInfo = new CUGCheckInfoImpl();
                            ((CUGCheckInfoImpl) this.cugCheckInfo).decodeAll(ais);
                            break;
                        case TAG_numberOfForwarding:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".numberOfForwarding: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.numberOfForwarding = (int) ais.readInteger();
                            break;
                        case TAG_interrogationType:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".interrogationType: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            int code = (int) ais.readInteger();
                            this.interrogationType = InterrogationType.getInterrogationType(code);
                            break;
                        case TAG_orInterrogation:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".orInterrogation: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            ais.readNull();
                            this.orInterrogation = true;
                            break;
                        case TAG_gmscOrGsmSCFAddress:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gmscAddress: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.gmscAddress = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.gmscAddress).decodeAll(ais);
                            break;
                        case TAG_orCapability:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".orCapability: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.orCapability = (int) ais.readInteger();
                            break;
                        case TAG_callReferenceNumber:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".callReferenceNumber: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.callReferenceNumber = new CallReferenceNumberImpl();
                            ((CallReferenceNumberImpl) this.callReferenceNumber).decodeAll(ais);
                            break;
                        case TAG_forwardingReason:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".forwardingReason: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            int i = (int) ais.readInteger();
                            this.forwardingReason = ForwardingReason.getForwardingReason(i);
                            break;
                        case TAG_basicServiceGroup: // explicit tag encoding
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".basicServiceGroup: is  primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            AsnInputStream ais1 = ais.readSequenceStream();
                            ais1.readTag();
                            this.basicServiceGroup = new ExtBasicServiceCodeImpl();
                            ((ExtBasicServiceCodeImpl) this.basicServiceGroup).decodeAll(ais1);
                            break;
                        case TAG_networkSignalInfo:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".networkSignalInfo: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.networkSignalInfo = new ExternalSignalInfoImpl();
                            ((ExternalSignalInfoImpl) this.networkSignalInfo).decodeAll(ais);
                            break;
                        case TAG_camelInfo:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".camelInfo: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.camelInfo = new CamelInfoImpl();
                            ((CamelInfoImpl) this.camelInfo).decodeAll(ais);
                            break;
                        case TAG_suppressionOfAnnouncement:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".suppressionOfAnnouncement: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            ais.readNull();
                            this.suppressionOfAnnouncement = true;
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
                        case TAG_alertingPattern:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".alertingPattern: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.alertingPattern = new AlertingPatternImpl();
                            ((AlertingPatternImpl) this.alertingPattern).decodeAll(ais);
                            break;
                        case TAG_ccbsCall:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".ccbsCall: is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            ais.readNull();
                            this.ccbsCall = true;
                            break;
                        case TAG_supportedCCBSPhase:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".supportedCCBSPhase: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.supportedCCBSPhase = (int) ais.readInteger();
                            break;
                        case TAG_additionalSignalInfo:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".additionalSignalInfo: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.additionalSignalInfo = new ExtExternalSignalInfoImpl();
                            ((ExtExternalSignalInfoImpl) this.additionalSignalInfo).decodeAll(ais);
                            break;
                        case TAG_istSupportIndicator:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".istSupportIndicator: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            int j = (int) ais.readInteger();
                            this.istSupportIndicator = ISTSupportIndicator.getInstance(j);
                            break;
                        case TAG_prePagingSupported:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".prePagingSupported: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            ais.readNull();
                            this.prePagingSupported = true;
                            break;
                        case TAG_callDiversionTreatmentIndicator:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".callDiversionTreatmentIndicator: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.callDiversionTreatmentIndicator = new CallDiversionTreatmentIndicatorImpl();
                            ((CallDiversionTreatmentIndicatorImpl) this.callDiversionTreatmentIndicator).decodeAll(ais);
                            break;
                        case TAG_longFTNSupported:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".longFTNSupported: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            ais.readNull();
                            this.longFTNSupported = true;
                            break;
                        case TAG_suppress_VT_CSI:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".suppressVtCSI: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            ais.readNull();
                            this.suppressVtCSI = true;
                            break;
                        case TAG_suppressIncomingCallBarring:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".suppressIncomingCallBarring: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            ais.readNull();
                            this.suppressIncomingCallBarring = true;
                            break;
                        case TAG_gsmSCFInitiatedCall:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gsmSCFInitiatedCall: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            ais.readNull();
                            this.gsmSCFInitiatedCall = true;
                            break;
                        case TAG_basicServiceGroup2: // explicit tag encoding
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".basicServiceGroup2: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            AsnInputStream ais2 = ais.readSequenceStream();
                            ais2.readTag();
                            this.basicServiceGroup2 = new ExtBasicServiceCodeImpl();
                            ((ExtBasicServiceCodeImpl) this.basicServiceGroup2).decodeAll(ais2);
                            break;
                        case TAG_networkSignalInfo2:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".networkSignalInfo2: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.networkSignalInfo2 = new ExternalSignalInfoImpl();
                            ((ExternalSignalInfoImpl) this.networkSignalInfo2).decodeAll(ais);
                            break;
                        case TAG_suppressMTSS:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".suppressMTSS: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.suppressMTSS = new SuppressMTSSImpl();
                            ((SuppressMTSSImpl) this.suppressMTSS).decodeAll(ais);
                            break;
                        case TAG_mtRoamingRetrySupported:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".mtRoamingRetrySupported: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            ais.readNull();
                            this.mtRoamingRetrySupported = true;
                            break;
                        case TAG_callPriority:
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".callPriority: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.callPriority = EMLPPPriority.getEMLPPPriority((int) ais.readInteger());
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

        if (this.msisdn == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament msisdn is mandatory but does not found", MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.mapProtocolVersion >= 3 && this.interrogationType == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament interrogationType is mandatory (V3) but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.mapProtocolVersion >= 3 && this.gmscAddress == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament gmscAddress is mandatory (V3) but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
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
            throw new MAPException("AsnException when encoding SendRoutingInformationRequest: " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.msisdn == null)
            throw new MAPException("Error while encoding " + _PrimitiveName + " the mandatory parameter MSISDN is not defined");

        if (this.mapProtocolVersion >= 3 && this.interrogationType == null)
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter (V3) interrogationType is not defined");

        if (this.mapProtocolVersion >= 3 && this.gmscAddress == null)
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter (V3) gmsc-OrGsmSCF-Address is not defined");

        try {

            ((ISDNAddressStringImpl) this.msisdn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_msisdn);

            if (this.mapProtocolVersion >= 2 && this.cugCheckInfo != null) {
                ((CUGCheckInfoImpl) this.cugCheckInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_cugCheckInfo);
            }

            if (this.numberOfForwarding != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_numberOfForwarding, this.numberOfForwarding);

            if (this.mapProtocolVersion >= 3) {
                if (this.interrogationType != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_interrogationType, this.interrogationType.getCode());

                if (this.orInterrogation)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_orInterrogation);

                if (this.orCapability != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_orCapability, this.orCapability);

                if (this.gmscAddress != null)
                    ((ISDNAddressStringImpl) this.gmscAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            TAG_gmscOrGsmSCFAddress);

                if (this.callReferenceNumber != null)
                    ((CallReferenceNumberImpl) this.callReferenceNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            TAG_callReferenceNumber);

                if (this.forwardingReason != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_forwardingReason, this.forwardingReason.getCode());

                if (this.basicServiceGroup != null) { // explicit tag encoding
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_basicServiceGroup);
                    int pos = asnOs.StartContentDefiniteLength();
                    ((ExtBasicServiceCodeImpl) this.basicServiceGroup).encodeAll(asnOs);
                    asnOs.FinalizeContent(pos);
                }
            }

            if (this.networkSignalInfo != null)
                ((ExternalSignalInfoImpl) this.networkSignalInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        TAG_networkSignalInfo);

            if (this.mapProtocolVersion >= 3) {
                if (this.camelInfo != null) {
                    ((CamelInfoImpl) this.camelInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_camelInfo);
                }

                if (this.suppressionOfAnnouncement)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_suppressionOfAnnouncement);

                if (this.extensionContainer != null)
                    ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            TAG_extensionContainer);

                if (this.alertingPattern != null)
                    ((AlertingPatternImpl) this.alertingPattern).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            TAG_alertingPattern);

                if (this.ccbsCall)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_ccbsCall);

                if (this.supportedCCBSPhase != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_supportedCCBSPhase, this.supportedCCBSPhase);

                if (this.additionalSignalInfo != null)
                    ((ExtExternalSignalInfoImpl) this.additionalSignalInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            TAG_additionalSignalInfo);

                if (this.istSupportIndicator != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_istSupportIndicator, this.istSupportIndicator.getCode());

                if (this.prePagingSupported)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_prePagingSupported);

                if (this.callDiversionTreatmentIndicator != null) {
                    ((CallDiversionTreatmentIndicatorImpl) this.callDiversionTreatmentIndicator).encodeAll(asnOs,
                            Tag.CLASS_CONTEXT_SPECIFIC, TAG_callDiversionTreatmentIndicator);
                }

                if (this.longFTNSupported)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_longFTNSupported);

                if (this.suppressVtCSI)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_suppress_VT_CSI);

                if (this.suppressIncomingCallBarring)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_suppressIncomingCallBarring);

                if (this.gsmSCFInitiatedCall)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_gsmSCFInitiatedCall);

                if (this.basicServiceGroup2 != null) { // explicit tag encoding
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_basicServiceGroup2);
                    int pos = asnOs.StartContentDefiniteLength();
                    ((ExtBasicServiceCodeImpl) this.basicServiceGroup2).encodeAll(asnOs);
                    asnOs.FinalizeContent(pos);
                }

                if (this.networkSignalInfo2 != null)
                    ((ExternalSignalInfoImpl) this.networkSignalInfo2).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            TAG_networkSignalInfo2);

                if (this.suppressMTSS != null)
                    ((SuppressMTSSImpl) this.suppressMTSS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_suppressMTSS);

                if (this.mtRoamingRetrySupported)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_mtRoamingRetrySupported);

                if (this.callPriority != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_callPriority, this.callPriority.getCode());
            }

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(this.msisdn);
        }

        if (this.cugCheckInfo != null) {
            sb.append(", cugCheckInfo=");
            sb.append(this.cugCheckInfo);
        }

        if (this.numberOfForwarding != null) {
            sb.append(", numberOfForwarding=");
            sb.append(this.numberOfForwarding);
        }

        if (this.interrogationType != null) {
            sb.append(", interrogationType=");
            sb.append(this.interrogationType);
        }

        if (this.orInterrogation != false)
            sb.append(", orInterrogation=TRUE");

        if (this.orCapability != null) {
            sb.append(", orCapability=");
            sb.append(this.orCapability);
        }

        if (this.gmscAddress != null) {
            sb.append(", gmscAddress=");
            sb.append(this.gmscAddress);
        }

        if (this.callReferenceNumber != null) {
            sb.append(", callReferenceNumber=");
            sb.append(this.callReferenceNumber);
        }

        if (this.forwardingReason != null) {
            sb.append(", forwardingReason=");
            sb.append(this.forwardingReason);
        }

        if (this.basicServiceGroup != null) {
            sb.append(", basicServiceGroup=");
            sb.append(this.basicServiceGroup);
        }

        if (this.networkSignalInfo != null) {
            sb.append(", networkSignalInfo=");
            sb.append(this.networkSignalInfo);
        }

        if (this.camelInfo != null) {
            sb.append(", camelInfo=");
            sb.append(this.camelInfo);
        }

        if (this.camelInfo != null) {
            sb.append(", camelInfo=");
            sb.append(this.camelInfo);
        }

        if (this.suppressionOfAnnouncement != false)
            sb.append(", suppressionOfAnnouncement=TRUE");

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        if (this.alertingPattern != null) {
            sb.append(", alertingPattern=");
            sb.append(this.alertingPattern);
        }

        if (this.ccbsCall != false)
            sb.append(", ccbsCall=TRUE");

        if (this.supportedCCBSPhase != null) {
            sb.append(", supportedCCBSPhase=");
            sb.append(this.supportedCCBSPhase);
        }

        if (this.additionalSignalInfo != null) {
            sb.append(", additionalSignalInfo=");
            sb.append(this.additionalSignalInfo);
        }

        if (this.istSupportIndicator != null) {
            sb.append(", istSupportIndicator=");
            sb.append(this.istSupportIndicator);
        }

        if (this.prePagingSupported != false)
            sb.append(", prePagingSupportedr=TRUE");

        if (this.callDiversionTreatmentIndicator != null) {
            sb.append(", callDiversionTreatmentIndicator=");
            sb.append(this.callDiversionTreatmentIndicator);
        }

        if (this.longFTNSupported != false)
            sb.append(", longFTNSupported=TRUE");

        if (this.suppressVtCSI != false)
            sb.append(", suppressVtCSI=TRUE");

        if (this.suppressIncomingCallBarring != false)
            sb.append(", suppressIncomingCallBarring=TRUE");

        if (this.gsmSCFInitiatedCall != false)
            sb.append(", gsmSCFInitiatedCall=TRUE");

        if (this.basicServiceGroup2 != null) {
            sb.append(", basicServiceGroup2=");
            sb.append(this.basicServiceGroup2);
        }

        if (this.networkSignalInfo2 != null) {
            sb.append(", networkSignalInfo2=");
            sb.append(this.networkSignalInfo2);
        }

        if (this.suppressMTSS != null) {
            sb.append(", suppressMTSS=");
            sb.append(this.suppressMTSS);
        }

        if (this.mtRoamingRetrySupported != false)
            sb.append(", mtRoamingRetrySupported=TRUE");

        if (this.callPriority != null) {
            sb.append(", callPriority=");
            sb.append(this.callPriority);
        }

        sb.append("]");
        return sb.toString();
    }
}