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

package org.mobicents.protocols.ss7.map.service.callhandling;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPServiceBase;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.mobicents.protocols.ss7.map.api.service.callhandling.AllowedServices;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CCBSIndicators;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CamelInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPDialogCallHandling;
import org.mobicents.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SuppressMTSS;
import org.mobicents.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

import java.util.ArrayList;

/*
 *
 * @author cristian veliscu
 * @author eva ogallar
 *
 */
public class MAPDialogCallHandlingImpl extends MAPDialogImpl implements MAPDialogCallHandling {
    // Include these constants in MAPApplicationContextName and MAPOperationCode
    // sendRoutingInfo_Request: add constant to MAPMessageType
    // sendRoutingInfo_Response: add constant to MAPMessageType
    protected static final int locationInfoRetrievalContext = 5;
    protected static final int sendRoutingInfo = 22;
    protected static final int version = 3;

    protected MAPDialogCallHandlingImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
            MAPServiceBase mapService, AddressString origReference, AddressString destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }

    public Long addSendRoutingInformationRequest(ISDNAddressString msisdn, CUGCheckInfo cugCheckInfo,
            Integer numberOfForwarding, ExternalSignalInfo networkSignalInfo) throws MAPException {
        return this.addSendRoutingInformationRequest(_Timer_Default, msisdn, cugCheckInfo, numberOfForwarding, null, false,
                null, null, null, null, null, networkSignalInfo, null, false, null, null, false, null, null, null, false, null,
                false, false, false, false, null, null, null, false, null);
    }

    public Long addSendRoutingInformationRequest(int customInvokeTimeout, ISDNAddressString msisdn, CUGCheckInfo cugCheckInfo,
            Integer numberOfForwarding, ExternalSignalInfo networkSignalInfo) throws MAPException {

        return this.addSendRoutingInformationRequest(customInvokeTimeout, msisdn, cugCheckInfo, numberOfForwarding, null,
                false, null, null, null, null, null, networkSignalInfo, null, false, null, null, false, null, null, null,
                false, null, false, false, false, false, null, null, null, false, null);

    }

    @Override
    public Long addSendRoutingInformationRequest(ISDNAddressString msisdn, CUGCheckInfo cugCheckInfo,
            Integer numberOfForwarding, InterrogationType interrogationType, boolean orInterrogation, Integer orCapability,
            ISDNAddressString gmscAddress, CallReferenceNumber callReferenceNumber, ForwardingReason forwardingReason,
            ExtBasicServiceCode basicServiceGroup, ExternalSignalInfo networkSignalInfo, CamelInfo camelInfo,
            boolean suppressionOfAnnouncement, MAPExtensionContainer extensionContainer, AlertingPattern alertingPattern,
            boolean ccbsCall, Integer supportedCCBSPhase, ExtExternalSignalInfo additionalSignalInfo,
            ISTSupportIndicator istSupportIndicator, boolean prePagingSupported,
            CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, boolean longFTNSupported, boolean suppressVtCSI,
            boolean suppressIncomingCallBarring, boolean gsmSCFInitiatedCall, ExtBasicServiceCode basicServiceGroup2,
            ExternalSignalInfo networkSignalInfo2, SuppressMTSS supressMTSS, boolean mtRoamingRetrySupported,
            EMLPPPriority callPriority) throws MAPException {

        return this
                .addSendRoutingInformationRequest(_Timer_Default, msisdn, cugCheckInfo, numberOfForwarding, interrogationType,
                        orInterrogation, orCapability, gmscAddress, callReferenceNumber, forwardingReason, basicServiceGroup,
                        networkSignalInfo, camelInfo, suppressionOfAnnouncement, extensionContainer, alertingPattern, ccbsCall,
                        supportedCCBSPhase, additionalSignalInfo, istSupportIndicator, prePagingSupported,
                        callDiversionTreatmentIndicator, longFTNSupported, suppressVtCSI, suppressIncomingCallBarring,
                        gsmSCFInitiatedCall, basicServiceGroup2, networkSignalInfo2, supressMTSS, mtRoamingRetrySupported,
                        callPriority);
    }

    @Override
    public Long addSendRoutingInformationRequest(int customInvokeTimeout, ISDNAddressString msisdn, CUGCheckInfo cugCheckInfo,
            Integer numberOfForwarding, InterrogationType interrogationType, boolean orInterrogation, Integer orCapability,
            ISDNAddressString gmscAddress, CallReferenceNumber callReferenceNumber, ForwardingReason forwardingReason,
            ExtBasicServiceCode basicServiceGroup, ExternalSignalInfo networkSignalInfo, CamelInfo camelInfo,
            boolean suppressionOfAnnouncement, MAPExtensionContainer extensionContainer, AlertingPattern alertingPattern,
            boolean ccbsCall, Integer supportedCCBSPhase, ExtExternalSignalInfo additionalSignalInfo,
            ISTSupportIndicator istSupportIndicator, boolean prePagingSupported,
            CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, boolean longFTNSupported, boolean suppressVtCSI,
            boolean suppressIncomingCallBarring, boolean gsmSCFInitiatedCall, ExtBasicServiceCode basicServiceGroup2,
            ExternalSignalInfo networkSignalInfo2, SuppressMTSS supressMTSS, boolean mtRoamingRetrySupported,
            EMLPPPriority callPriority) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationInfoRetrievalContext)
                || (vers != MAPApplicationContextVersion.version1 && vers != MAPApplicationContextVersion.version2 && vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addSendRoutingInformationRequest: must be locationInfoRetrievalContext_V1, V2 or V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.sendRoutingInfo);
        invoke.setOperationCode(oc);

        if (true) { // validate parameters here...
            SendRoutingInformationRequestImpl req = new SendRoutingInformationRequestImpl(this.appCntx
                    .getApplicationContextVersion().getVersion(), msisdn, cugCheckInfo, numberOfForwarding, interrogationType,
                    orInterrogation, orCapability, gmscAddress, callReferenceNumber, forwardingReason, basicServiceGroup,
                    networkSignalInfo, camelInfo, suppressionOfAnnouncement, extensionContainer, alertingPattern, ccbsCall,
                    supportedCCBSPhase, additionalSignalInfo, istSupportIndicator, prePagingSupported,
                    callDiversionTreatmentIndicator, longFTNSupported, suppressVtCSI, suppressIncomingCallBarring,
                    gsmSCFInitiatedCall, basicServiceGroup2, networkSignalInfo2, supressMTSS, mtRoamingRetrySupported,
                    callPriority);
            AsnOutputStream aos = new AsnOutputStream();
            req.encodeData(aos);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(req.getTagClass());
            p.setPrimitive(req.getIsPrimitive());
            p.setTag(req.getTag());
            p.setData(aos.toByteArray());
            invoke.setParameter(p);
        }

        Long invokeId;
        try {
            invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);
        } catch (TCAPException e) {
            throw new MAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);
        return invokeId;
    }

    public void addSendRoutingInformationResponse(long invokeId, IMSI imsi, CUGCheckInfo cugCheckInfo, RoutingInfo routingInfo2)
            throws MAPException {
        this.addSendRoutingInformationResponse(invokeId, imsi, null, cugCheckInfo, false, null, null, null, false, null, null,
                null, null, null, null, null, null, null, routingInfo2, null, null, null, null, false, null);
    }

    @Override
    public void addSendRoutingInformationResponse(long invokeId, IMSI imsi, ExtendedRoutingInfo extRoutingInfo,
            CUGCheckInfo cugCheckInfo, boolean cugSubscriptionFlag, SubscriberInfo subscriberInfo, ArrayList<SSCode> ssList,
            ExtBasicServiceCode basicService, boolean forwardingInterrogationRequired, ISDNAddressString vmscAddress,
            MAPExtensionContainer extensionContainer, NAEAPreferredCI naeaPreferredCI, CCBSIndicators ccbsIndicators,
            ISDNAddressString msisdn, NumberPortabilityStatus nrPortabilityStatus, Integer istAlertTimer,
            SupportedCamelPhases supportedCamelPhases, OfferedCamel4CSIs offeredCamel4CSIs, RoutingInfo routingInfo2,
            ArrayList<SSCode> ssList2, ExtBasicServiceCode basicService2, AllowedServices allowedServices,
            UnavailabilityCause unavailabilityCause, boolean releaseResourcesSupported, ExternalSignalInfo gsmBearerCapability)
            throws MAPException {
        doAddSendRoutingInformationResponse(false, invokeId, imsi, extRoutingInfo, cugCheckInfo, cugSubscriptionFlag,
                subscriberInfo, ssList, basicService, forwardingInterrogationRequired, vmscAddress, extensionContainer,
                naeaPreferredCI, ccbsIndicators, msisdn, nrPortabilityStatus, istAlertTimer, supportedCamelPhases,
                offeredCamel4CSIs, routingInfo2, ssList2, basicService2, allowedServices, unavailabilityCause,
                releaseResourcesSupported, gsmBearerCapability);
    }

    @Override
    public void addSendRoutingInformationResponse_NonLast(long invokeId, IMSI imsi, ExtendedRoutingInfo extRoutingInfo,
            CUGCheckInfo cugCheckInfo, boolean cugSubscriptionFlag, SubscriberInfo subscriberInfo, ArrayList<SSCode> ssList,
            ExtBasicServiceCode basicService, boolean forwardingInterrogationRequired, ISDNAddressString vmscAddress,
            MAPExtensionContainer extensionContainer, NAEAPreferredCI naeaPreferredCI, CCBSIndicators ccbsIndicators,
            ISDNAddressString msisdn, NumberPortabilityStatus nrPortabilityStatus, Integer istAlertTimer,
            SupportedCamelPhases supportedCamelPhases, OfferedCamel4CSIs offeredCamel4CSIs, RoutingInfo routingInfo2,
            ArrayList<SSCode> ssList2, ExtBasicServiceCode basicService2, AllowedServices allowedServices,
            UnavailabilityCause unavailabilityCause, boolean releaseResourcesSupported, ExternalSignalInfo gsmBearerCapability)
            throws MAPException {
        doAddSendRoutingInformationResponse(true, invokeId, imsi, extRoutingInfo, cugCheckInfo, cugSubscriptionFlag,
                subscriberInfo, ssList, basicService, forwardingInterrogationRequired, vmscAddress, extensionContainer,
                naeaPreferredCI, ccbsIndicators, msisdn, nrPortabilityStatus, istAlertTimer, supportedCamelPhases,
                offeredCamel4CSIs, routingInfo2, ssList2, basicService2, allowedServices, unavailabilityCause,
                releaseResourcesSupported, gsmBearerCapability);
    }

    protected void doAddSendRoutingInformationResponse(boolean nonLast, long invokeId, IMSI imsi,
            ExtendedRoutingInfo extRoutingInfo, CUGCheckInfo cugCheckInfo, boolean cugSubscriptionFlag,
            SubscriberInfo subscriberInfo, ArrayList<SSCode> ssList, ExtBasicServiceCode basicService,
            boolean forwardingInterrogationRequired, ISDNAddressString vmscAddress, MAPExtensionContainer extensionContainer,
            NAEAPreferredCI naeaPreferredCI, CCBSIndicators ccbsIndicators, ISDNAddressString msisdn,
            NumberPortabilityStatus nrPortabilityStatus, Integer istAlertTimer, SupportedCamelPhases supportedCamelPhases,
            OfferedCamel4CSIs offeredCamel4CSIs, RoutingInfo routingInfo2, ArrayList<SSCode> ssList2,
            ExtBasicServiceCode basicService2, AllowedServices allowedServices, UnavailabilityCause unavailabilityCause,
            boolean releaseResourcesSupported, ExternalSignalInfo gsmBearerCapability) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationInfoRetrievalContext)
                || (vers != MAPApplicationContextVersion.version1 && vers != MAPApplicationContextVersion.version2 && vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addSendRoutingInformationResponse: must be locationInfoRetrievalContext_V1, V2 or V3");
        if (vers != MAPApplicationContextVersion.version3 && nonLast)
            throw new MAPException(
                    "Bad application context name for addSendRoutingInformationResponse - nonLast: must be locationInfoRetrievalContext_V3");

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.sendRoutingInfo);

        Parameter p = null;
        if (true) { // validate parameters here...
            SendRoutingInformationResponseImpl res = new SendRoutingInformationResponseImpl(this.appCntx
                    .getApplicationContextVersion().getVersion(), imsi, extRoutingInfo, cugCheckInfo, cugSubscriptionFlag,
                    subscriberInfo, ssList, basicService, forwardingInterrogationRequired, vmscAddress, extensionContainer,
                    naeaPreferredCI, ccbsIndicators, msisdn, nrPortabilityStatus, istAlertTimer, supportedCamelPhases,
                    offeredCamel4CSIs, routingInfo2, ssList2, basicService2, allowedServices, unavailabilityCause,
                    releaseResourcesSupported, gsmBearerCapability);
            AsnOutputStream aos = new AsnOutputStream();
            res.encodeData(aos);

            p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(res.getTagClass());
            p.setPrimitive(res.getIsPrimitive());
            p.setTag(res.getTag());
            p.setData(aos.toByteArray());
        }

        if (nonLast) {
            ReturnResult resultLastNonLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                    .createTCResultRequest();
            resultLastNonLast.setInvokeId(invokeId);

            resultLastNonLast.setOperationCode(oc);

            if (p != null)
                resultLastNonLast.setParameter(p);

            this.sendReturnResultComponent(resultLastNonLast);
        } else {
            ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                    .createTCResultLastRequest();
            resultLast.setInvokeId(invokeId);

            resultLast.setOperationCode(oc);

            if (p != null)
                resultLast.setParameter(p);

            this.sendReturnResultLastComponent(resultLast);
        }
    }

    @Override
    public Long addProvideRoamingNumberRequest(IMSI imsi, ISDNAddressString mscNumber, ISDNAddressString msisdn, LMSI lmsi,
            ExternalSignalInfo gsmBearerCapability, ExternalSignalInfo networkSignalInfo, boolean suppressionOfAnnouncement,
            ISDNAddressString gmscAddress, CallReferenceNumber callReferenceNumber, boolean orInterrogation,
            MAPExtensionContainer extensionContainer, AlertingPattern alertingPattern, boolean ccbsCall,
            SupportedCamelPhases supportedCamelPhasesInInterrogatingNode, ExtExternalSignalInfo additionalSignalInfo,
            boolean orNotSupportedInGMSC, boolean prePagingSupported, boolean longFTNSupported, boolean suppressVtCsi,
            OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode, boolean mtRoamingRetrySupported, PagingArea pagingArea,
            EMLPPPriority callPriority, boolean mtrfIndicator, ISDNAddressString oldMSCNumber) throws MAPException {
        return this.addProvideRoamingNumberRequest(_Timer_Default, imsi, mscNumber, msisdn, lmsi, gsmBearerCapability,
                networkSignalInfo, suppressionOfAnnouncement, gmscAddress, callReferenceNumber, orInterrogation,
                extensionContainer, alertingPattern, ccbsCall, supportedCamelPhasesInInterrogatingNode, additionalSignalInfo,
                orNotSupportedInGMSC, prePagingSupported, longFTNSupported, suppressVtCsi,
                offeredCamel4CSIsInInterrogatingNode, mtRoamingRetrySupported, pagingArea, callPriority, mtrfIndicator,
                oldMSCNumber);
    }

    @Override
    public Long addProvideRoamingNumberRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString mscNumber,
            ISDNAddressString msisdn, LMSI lmsi, ExternalSignalInfo gsmBearerCapability, ExternalSignalInfo networkSignalInfo,
            boolean suppressionOfAnnouncement, ISDNAddressString gmscAddress, CallReferenceNumber callReferenceNumber,
            boolean orInterrogation, MAPExtensionContainer extensionContainer, AlertingPattern alertingPattern,
            boolean ccbsCall, SupportedCamelPhases supportedCamelPhasesInInterrogatingNode,
            ExtExternalSignalInfo additionalSignalInfo, boolean orNotSupportedInGMSC, boolean prePagingSupported,
            boolean longFTNSupported, boolean suppressVtCsi, OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode,
            boolean mtRoamingRetrySupported, PagingArea pagingArea, EMLPPPriority callPriority, boolean mtrfIndicator,
            ISDNAddressString oldMSCNumber) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.roamingNumberEnquiryContext)
                || (vers != MAPApplicationContextVersion.version1 && vers != MAPApplicationContextVersion.version2 && vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addProvideRoamingNumberRequest: must be roamingNumberEnquiryContext _V1, V2 or V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.provideRoamingNumber);
        invoke.setOperationCode(oc);

        ProvideRoamingNumberRequestImpl req = new ProvideRoamingNumberRequestImpl(imsi, mscNumber, msisdn, lmsi,
                gsmBearerCapability, networkSignalInfo, suppressionOfAnnouncement, gmscAddress, callReferenceNumber,
                orInterrogation, extensionContainer, alertingPattern, ccbsCall, supportedCamelPhasesInInterrogatingNode,
                additionalSignalInfo, orNotSupportedInGMSC, prePagingSupported, longFTNSupported, suppressVtCsi,
                offeredCamel4CSIsInInterrogatingNode, mtRoamingRetrySupported, pagingArea, callPriority, mtrfIndicator,
                oldMSCNumber, this.appCntx.getApplicationContextVersion().getVersion());
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        invoke.setParameter(p);

        Long invokeId;
        try {
            invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);
        } catch (TCAPException e) {
            throw new MAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);
        return invokeId;

    }

    @Override
    public void addProvideRoamingNumberResponse(long invokeId, ISDNAddressString roamingNumber,
            MAPExtensionContainer extensionContainer, boolean releaseResourcesSupported, ISDNAddressString vmscAddress)
            throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.roamingNumberEnquiryContext)
                || (vers != MAPApplicationContextVersion.version1 && vers != MAPApplicationContextVersion.version2 && vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addProvideRoamingNumberResponse: must be roamingNumberEnquiryContext_V1, V2 or V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();
        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.provideRoamingNumber);
        resultLast.setOperationCode(oc);

        ProvideRoamingNumberResponseImpl res = new ProvideRoamingNumberResponseImpl(roamingNumber, extensionContainer,
                releaseResourcesSupported, vmscAddress, this.appCntx.getApplicationContextVersion().getVersion());
        AsnOutputStream aos = new AsnOutputStream();
        res.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(res.getTagClass());
        p.setPrimitive(res.getIsPrimitive());
        p.setTag(res.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);

    }

    @Override
    public Long addIstCommandRequest(IMSI imsi, MAPExtensionContainer extensionContainer) throws MAPException {
        return this.addIstCommandRequest(_Timer_Default, imsi, extensionContainer);
    }

    @Override
    public Long addIstCommandRequest(int customInvokeTimeout, IMSI imsi, MAPExtensionContainer extensionContainer) throws MAPException {
        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.ServiceTerminationContext)
                || (vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addIstCommandRequest: must be ServiceTerminationContext_V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.istCommand);
        invoke.setOperationCode(oc);

        IstCommandRequestImpl req = new IstCommandRequestImpl(imsi, extensionContainer);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        invoke.setParameter(p);

        Long invokeId;
        try {
            invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);
        } catch (TCAPException e) {
            throw new MAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);
        return invokeId;

    }

    @Override
    public void addIstCommandResponse(long invokeId, MAPExtensionContainer extensionContainer) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.ServiceTerminationContext)
                || (vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addIstCommandResponse: must be ServiceTerminationContext_V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();
        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.istCommand);
        resultLast.setOperationCode(oc);

        if (extensionContainer!=null) {
            IstCommandResponseImpl res = new IstCommandResponseImpl(extensionContainer);
            AsnOutputStream aos = new AsnOutputStream();
            res.encodeData(aos);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(res.getTagClass());
            p.setPrimitive(res.getIsPrimitive());
            p.setTag(res.getTag());
            p.setData(aos.toByteArray());
            resultLast.setParameter(p);
        }
        this.sendReturnResultLastComponent(resultLast);
    }
}