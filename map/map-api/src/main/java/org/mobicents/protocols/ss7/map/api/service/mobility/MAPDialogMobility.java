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

package org.mobicents.protocols.ss7.map.api.service.mobility;

import java.util.ArrayList;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.TMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContext;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriority;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPDialogMobility extends MAPDialog {

    // -- Location Management Service
    Long addUpdateLocationRequest(IMSI imsi, ISDNAddressString mscNumber, ISDNAddressString roamingNumber,
            ISDNAddressString vlrNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, VLRCapability vlrCapability,
            boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo,
            PagingArea pagingArea, boolean skipSubscriberDataUpdate, boolean restorationIndicator) throws MAPException;

    Long addUpdateLocationRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString mscNumber,
            ISDNAddressString roamingNumber, ISDNAddressString vlrNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
            VLRCapability vlrCapability, boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE,
            GSNAddress vGmlcAddress, ADDInfo addInfo, PagingArea pagingArea, boolean skipSubscriberDataUpdate,
            boolean restorationIndicator) throws MAPException;

    void addUpdateLocationResponse(long invokeId, ISDNAddressString hlrNumber, MAPExtensionContainer extensionContainer,
            boolean addCapability, boolean pagingAreaCapability) throws MAPException;

    Long addCancelLocationRequest(int customInvokeTimeout, IMSI imsi, IMSIWithLMSI imsiWithLmsi,
            CancellationType cancellationType, MAPExtensionContainer extensionContainer, TypeOfUpdate typeOfUpdate,
            boolean mtrfSupportedAndAuthorized, boolean mtrfSupportedAndNotAuthorized, ISDNAddressString newMSCNumber,
            ISDNAddressString newVLRNumber, LMSI newLmsi) throws MAPException;

    Long addCancelLocationRequest(IMSI imsi, IMSIWithLMSI imsiWithLmsi, CancellationType cancellationType,
            MAPExtensionContainer extensionContainer, TypeOfUpdate typeOfUpdate, boolean mtrfSupportedAndAuthorized,
            boolean mtrfSupportedAndNotAuthorized, ISDNAddressString newMSCNumber, ISDNAddressString newVLRNumber, LMSI newLmsi)
            throws MAPException;

    void addCancelLocationResponse(long invokeId, MAPExtensionContainer extensionContainer) throws MAPException;

    Long addSendIdentificationRequest(int customInvokeTimeout, TMSI tmsi, Integer numberOfRequestedVectors,
            boolean segmentationProhibited, MAPExtensionContainer extensionContainer, ISDNAddressString mscNumber,
            LAIFixedLength previousLAI, Integer hopCounter, boolean mtRoamingForwardingSupported,
            ISDNAddressString newVLRNumber, LMSI lmsi) throws MAPException;

    Long addSendIdentificationRequest(TMSI tmsi, Integer numberOfRequestedVectors, boolean segmentationProhibited,
            MAPExtensionContainer extensionContainer, ISDNAddressString mscNumber, LAIFixedLength previousLAI,
            Integer hopCounter, boolean mtRoamingForwardingSupported, ISDNAddressString newVLRNumber, LMSI lmsi)
            throws MAPException;

    void addSendIdentificationResponse(long invokeId, IMSI imsi, AuthenticationSetList authenticationSetList,
            CurrentSecurityContext currentSecurityContext, MAPExtensionContainer extensionContainer) throws MAPException;

    Long addUpdateGprsLocationRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString sgsnNumber,
            GSNAddress sgsnAddress, MAPExtensionContainer extensionContainer, SGSNCapability sgsnCapability,
            boolean informPreviousNetworkEntity, boolean psLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo,
            EPSInfo epsInfo, boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
            boolean gprsSubscriptionDataNotNeeded, boolean nodeTypeIndicator, boolean areaRestricted,
            boolean ueReachableIndicator, boolean epsSubscriptionDataNotNeeded, UESRVCCCapability uesrvccCapability)
            throws MAPException;

    Long addUpdateGprsLocationRequest(IMSI imsi, ISDNAddressString sgsnNumber, GSNAddress sgsnAddress,
            MAPExtensionContainer extensionContainer, SGSNCapability sgsnCapability, boolean informPreviousNetworkEntity,
            boolean psLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo, EPSInfo epsInfo,
            boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
            boolean gprsSubscriptionDataNotNeeded, boolean nodeTypeIndicator, boolean areaRestricted,
            boolean ueReachableIndicator, boolean epsSubscriptionDataNotNeeded, UESRVCCCapability uesrvccCapability)
            throws MAPException;

    void addUpdateGprsLocationResponse(long invokeId, ISDNAddressString hlrNumber, MAPExtensionContainer extensionContainer,
            boolean addCapability, boolean sgsnMmeSeparationSupported) throws MAPException;

    Long addPurgeMSRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber,
            MAPExtensionContainer extensionContainer) throws MAPException;

    Long addPurgeMSRequest(IMSI imsi, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber,
            MAPExtensionContainer extensionContainer) throws MAPException;

    void addPurgeMSResponse(long invokeId, boolean freezeTMSI, boolean freezePTMSI, MAPExtensionContainer extensionContainer,
            boolean freezeMTMSI) throws MAPException;

    // -- Authentication management services
    Long addSendAuthenticationInfoRequest(IMSI imsi, int numberOfRequestedVectors, boolean segmentationProhibited,
            boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo,
            MAPExtensionContainer extensionContainer, RequestingNodeType requestingNodeType, PlmnId requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException;

    Long addSendAuthenticationInfoRequest(int customInvokeTimeout, IMSI imsi, int numberOfRequestedVectors,
            boolean segmentationProhibited, boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo,
            MAPExtensionContainer extensionContainer, RequestingNodeType requestingNodeType, PlmnId requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException;

    void addSendAuthenticationInfoResponse(long invokeId, AuthenticationSetList authenticationSetList,
            MAPExtensionContainer extensionContainer, EpsAuthenticationSetList epsAuthenticationSetList) throws MAPException;

    // -- Subscriber Information services
    long addAnyTimeInterrogationRequest(SubscriberIdentity subscriberIdentity, RequestedInfo requestedInfo,
            ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer) throws MAPException;

    long addAnyTimeInterrogationRequest(long customInvokeTimeout, SubscriberIdentity subscriberIdentity,
            RequestedInfo requestedInfo, ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer)
            throws MAPException;

    void addAnyTimeInterrogationResponse(long invokeId, SubscriberInfo subscriberInfo,
            MAPExtensionContainer extensionContainer) throws MAPException;

    // -- Subscriber Management services
    Long addInsertSubscriberDataRequest(IMSI imsi, ISDNAddressString msisdn, Category category,
            SubscriberStatus subscriberStatus, ArrayList<ExtBearerServiceCode> bearerServiceList,
            ArrayList<ExtTeleserviceCode> teleserviceList, ArrayList<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, ArrayList<ZoneCode> regionalSubscriptionData,
            ArrayList<VoiceBroadcastData> vbsSubscriptionData, ArrayList<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo) throws MAPException;

    Long addInsertSubscriberDataRequest(long customInvokeTimeout, IMSI imsi, ISDNAddressString msisdn,
            Category category, SubscriberStatus subscriberStatus, ArrayList<ExtBearerServiceCode> bearerServiceList,
            ArrayList<ExtTeleserviceCode> teleserviceList, ArrayList<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, ArrayList<ZoneCode> regionalSubscriptionData,
            ArrayList<VoiceBroadcastData> vbsSubscriptionData, ArrayList<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo) throws MAPException;

    Long addInsertSubscriberDataRequest(IMSI imsi, ISDNAddressString msisdn, Category category,
            SubscriberStatus subscriberStatus, ArrayList<ExtBearerServiceCode> bearerServiceList,
            ArrayList<ExtTeleserviceCode> teleserviceList, ArrayList<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, ArrayList<ZoneCode> regionalSubscriptionData,
            ArrayList<VoiceBroadcastData> vbsSubscriptionData, ArrayList<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo, MAPExtensionContainer extensionContainer,
            NAEAPreferredCI naeaPreferredCI, GPRSSubscriptionData gprsSubscriptionData,
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
            LSAInformation lsaInformation, boolean lmuIndicator, LCSInformation lcsInformation, Integer istAlertTimer,
            AgeIndicator superChargerSupportedInHLR, MCSSInfo mcSsInfo,
            CSAllocationRetentionPriority csAllocationRetentionPriority, SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo,
            ChargingCharacteristics chargingCharacteristics, AccessRestrictionData accessRestrictionData, Boolean icsIndicator,
            EPSSubscriptionData epsSubscriptionData, ArrayList<CSGSubscriptionData> csgSubscriptionDataList,
            boolean ueReachabilityRequestIndicator, ISDNAddressString sgsnNumber, DiameterIdentity mmeName,
            Long subscribedPeriodicRAUTAUtimer, boolean vplmnLIPAAllowed, Boolean mdtUserConsent,
            Long subscribedPeriodicLAUtimer) throws MAPException;

    Long addInsertSubscriberDataRequest(long customInvokeTimeout, IMSI imsi, ISDNAddressString msisdn,
            Category category, SubscriberStatus subscriberStatus, ArrayList<ExtBearerServiceCode> bearerServiceList,
            ArrayList<ExtTeleserviceCode> teleserviceList, ArrayList<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, ArrayList<ZoneCode> regionalSubscriptionData,
            ArrayList<VoiceBroadcastData> vbsSubscriptionData, ArrayList<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo, MAPExtensionContainer extensionContainer,
            NAEAPreferredCI naeaPreferredCI, GPRSSubscriptionData gprsSubscriptionData,
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
            LSAInformation lsaInformation, boolean lmuIndicator, LCSInformation lcsInformation, Integer istAlertTimer,
            AgeIndicator superChargerSupportedInHLR, MCSSInfo mcSsInfo,
            CSAllocationRetentionPriority csAllocationRetentionPriority, SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo,
            ChargingCharacteristics chargingCharacteristics, AccessRestrictionData accessRestrictionData, Boolean icsIndicator,
            EPSSubscriptionData epsSubscriptionData, ArrayList<CSGSubscriptionData> csgSubscriptionDataList,
            boolean ueReachabilityRequestIndicator, ISDNAddressString sgsnNumber, DiameterIdentity mmeName,
            Long subscribedPeriodicRAUTAUtimer, boolean vplmnLIPAAllowed, Boolean mdtUserConsent,
            Long subscribedPeriodicLAUtimer) throws MAPException;

    void addInsertSubscriberDataResponse(long invokeId, ArrayList<ExtTeleserviceCode> teleserviceList,
            ArrayList<ExtBearerServiceCode> bearerServiceList, ArrayList<SSCode> ssList, ODBGeneralData odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse) throws MAPException;

    void addInsertSubscriberDataResponse(long invokeId, ArrayList<ExtTeleserviceCode> teleserviceList,
            ArrayList<ExtBearerServiceCode> bearerServiceList, ArrayList<SSCode> ssList, ODBGeneralData odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse, SupportedCamelPhases supportedCamelPhases,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIs, SupportedFeatures supportedFeatures)
            throws MAPException;

    // -- International mobile equipment identities management services
    Long addCheckImeiRequest(IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
            MAPExtensionContainer extensionContainer) throws MAPException;

    Long addCheckImeiRequest(long customInvokeTimeout, IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
            MAPExtensionContainer extensionContainer) throws MAPException;

    void addCheckImeiResponse(long invokeId, EquipmentStatus equipmentStatus, UESBIIu bmuef,
            MAPExtensionContainer extensionContainer) throws MAPException;

    Long addCheckImeiRequest_Huawei(IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
            MAPExtensionContainer extensionContainer, IMSI imsi) throws MAPException;

    Long addCheckImeiRequest_Huawei(long customInvokeTimeout, IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
            MAPExtensionContainer extensionContainer, IMSI imsi) throws MAPException;

    // TODO: add service component adders
}
