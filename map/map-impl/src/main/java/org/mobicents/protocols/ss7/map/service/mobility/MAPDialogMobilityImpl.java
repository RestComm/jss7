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

package org.mobicents.protocols.ss7.map.service.mobility;

import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.mobicents.protocols.ss7.map.api.primitives.NetworkResource;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.TMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AccessType;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContext;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.FailureCause;
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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriority;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.mobicents.protocols.ss7.map.api.service.oam.MDTConfiguration;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceDepthList;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceEventList;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceInterfaceList;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceNETypeList;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceReference;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceReference2;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceType;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.AuthenticationFailureReportRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.AuthenticationFailureReportResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.faultRecovery.ForwardCheckSSIndicationRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.faultRecovery.ResetRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.faultRecovery.RestoreDataRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.faultRecovery.RestoreDataResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.imei.CheckImeiRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.imei.CheckImeiResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.CancelLocationRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.CancelLocationResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.PurgeMSRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.PurgeMSResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.UpdateGprsLocationRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.UpdateGprsLocationResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.UpdateLocationRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.UpdateLocationResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.oam.ActivateTraceModeRequestImpl_Mobility;
import org.mobicents.protocols.ss7.map.service.mobility.oam.ActivateTraceModeResponseImpl_Mobility;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.ProvideSubscriberInfoRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.ProvideSubscriberInfoResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.DeleteSubscriberDataRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.DeleteSubscriberDataResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataResponseImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPDialogMobilityImpl extends MAPDialogImpl implements MAPDialogMobility {

    protected MAPDialogMobilityImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
            MAPServiceMobility mapService, AddressString origReference, AddressString destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }

    public Long addSendAuthenticationInfoRequest(IMSI imsi, int numberOfRequestedVectors, boolean segmentationProhibited,
            boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo,
            MAPExtensionContainer extensionContainer, RequestingNodeType requestingNodeType, PlmnId requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException {
        return this.addSendAuthenticationInfoRequest(_Timer_Default, imsi, numberOfRequestedVectors, segmentationProhibited,
                immediateResponsePreferred, reSynchronisationInfo, extensionContainer, requestingNodeType, requestingPlmnId,
                numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS);
    }

    public Long addSendAuthenticationInfoRequest(int customInvokeTimeout, IMSI imsi, int numberOfRequestedVectors,
            boolean segmentationProhibited, boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo,
            MAPExtensionContainer extensionContainer, RequestingNodeType requestingNodeType, PlmnId requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.infoRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for sendAuthenticationInfoRequest: must be infoRetrievalContext_V2 or V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.sendAuthenticationInfo);
        invoke.setOperationCode(oc);

        if (imsi != null) {
            // parameter is optional: is no imsi is included we will not add a parameter
            SendAuthenticationInfoRequestImpl req = new SendAuthenticationInfoRequestImpl(this.appCntx
                    .getApplicationContextVersion().getVersion(), imsi, numberOfRequestedVectors, segmentationProhibited,
                    immediateResponsePreferred, reSynchronisationInfo, extensionContainer, requestingNodeType,
                    requestingPlmnId, numberOfRequestedAdditionalVectors, additionalVectorsAreForEPS);
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

    public void addSendAuthenticationInfoResponse(long invokeId, AuthenticationSetList authenticationSetList,
            MAPExtensionContainer extensionContainer, EpsAuthenticationSetList epsAuthenticationSetList) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.infoRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addSendAuthenticationInfoResponse: must be infoRetrievalContext_V2 or V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        if (authenticationSetList != null || extensionContainer != null || epsAuthenticationSetList != null) {
            // Operation Code
            OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
            oc.setLocalOperationCode((long) MAPOperationCode.sendAuthenticationInfo);
            resultLast.setOperationCode(oc);

            SendAuthenticationInfoResponseImpl req = new SendAuthenticationInfoResponseImpl(this.appCntx
                    .getApplicationContextVersion().getVersion(), authenticationSetList, extensionContainer,
                    epsAuthenticationSetList);
            AsnOutputStream aos = new AsnOutputStream();
            req.encodeData(aos);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(req.getTagClass());
            p.setPrimitive(req.getIsPrimitive());
            p.setTag(req.getTag());
            p.setData(aos.toByteArray());
            resultLast.setParameter(p);
        }

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addAuthenticationFailureReportRequest(IMSI imsi, FailureCause failureCause, MAPExtensionContainer extensionContainer, Boolean reAttempt,
            AccessType accessType, byte[] rand, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber) throws MAPException {
        return this.addAuthenticationFailureReportRequest(_Timer_Default, imsi, failureCause, extensionContainer, reAttempt, accessType, rand, vlrNumber,
                sgsnNumber);
    }

    @Override
    public Long addAuthenticationFailureReportRequest(int customInvokeTimeout, IMSI imsi, FailureCause failureCause, MAPExtensionContainer extensionContainer,
            Boolean reAttempt, AccessType accessType, byte[] rand, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.authenticationFailureReportContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException("Bad application context name for authenticationFailureReportRequest: must be authenticationFailureReportContext_V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.authenticationFailureReport);
        invoke.setOperationCode(oc);

        AuthenticationFailureReportRequestImpl req = new AuthenticationFailureReportRequestImpl(imsi, failureCause, extensionContainer, reAttempt, accessType,
                rand, vlrNumber, sgsnNumber);
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
    public void addAuthenticationFailureReportResponse(long invokeId, MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.authenticationFailureReportContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException("Bad application context name for authenticationFailureReportResponse: must be authenticationFailureReportContext_V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        if (extensionContainer != null) {
            // Operation Code
            OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
            oc.setLocalOperationCode((long) MAPOperationCode.authenticationFailureReport);
            resultLast.setOperationCode(oc);

            AuthenticationFailureReportResponseImpl req = new AuthenticationFailureReportResponseImpl(extensionContainer);
            AsnOutputStream aos = new AsnOutputStream();
            req.encodeData(aos);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(req.getTagClass());
            p.setPrimitive(req.getIsPrimitive());
            p.setTag(req.getTag());
            p.setData(aos.toByteArray());
            resultLast.setParameter(p);
        }

        this.sendReturnResultLastComponent(resultLast);
    }

    public Long addUpdateLocationRequest(IMSI imsi, ISDNAddressString mscNumber, ISDNAddressString roamingNumber,
            ISDNAddressString vlrNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, VLRCapability vlrCapability,
            boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo,
            PagingArea pagingArea, boolean skipSubscriberDataUpdate, boolean restorationIndicator) throws MAPException {
        return addUpdateLocationRequest(_Timer_Default, imsi, mscNumber, roamingNumber, vlrNumber, lmsi, extensionContainer,
                vlrCapability, informPreviousNetworkEntity, csLCSNotSupportedByUE, vGmlcAddress, addInfo, pagingArea,
                skipSubscriberDataUpdate, restorationIndicator);
    }

    public Long addUpdateLocationRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString mscNumber,
            ISDNAddressString roamingNumber, ISDNAddressString vlrNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
            VLRCapability vlrCapability, boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE,
            GSNAddress vGmlcAddress, ADDInfo addInfo, PagingArea pagingArea, boolean skipSubscriberDataUpdate,
            boolean restorationIndicator) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for UpdateLocationRequest: must be networkLocUpContext_V1, V2 or V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.updateLocation);
        invoke.setOperationCode(oc);

        UpdateLocationRequestImpl req = new UpdateLocationRequestImpl(this.appCntx.getApplicationContextVersion().getVersion(),
                imsi, mscNumber, roamingNumber, vlrNumber, lmsi, extensionContainer, vlrCapability,
                informPreviousNetworkEntity, csLCSNotSupportedByUE, vGmlcAddress, addInfo, pagingArea,
                skipSubscriberDataUpdate, restorationIndicator);
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

    public void addUpdateLocationResponse(long invokeId, ISDNAddressString hlrNumber, MAPExtensionContainer extensionContainer,
            boolean addCapability, boolean pagingAreaCapability) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for UpdateLocationResponse: must be networkLocUpContext_V1, V2 or V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.updateLocation);
        resultLast.setOperationCode(oc);

        UpdateLocationResponseImpl req = new UpdateLocationResponseImpl(this.appCntx.getApplicationContextVersion()
                .getVersion(), hlrNumber, extensionContainer, addCapability, pagingAreaCapability);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. MAPDialogSubscriberInformation
     * #addAnyTimeInterrogationRequest(org.mobicents .protocols.ss7.map.api.primitives.SubscriberIdentity,
     * org.mobicents.protocols .ss7.map.api.service.subscriberInformation.RequestedInfo,
     * org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    public long addAnyTimeInterrogationRequest(SubscriberIdentity subscriberIdentity, RequestedInfo requestedInfo,
            ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer) throws MAPException {

        return this.addAnyTimeInterrogationRequest(_Timer_Default, subscriberIdentity, requestedInfo, gsmSCFAddress,
                extensionContainer);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * MAPDialogSubscriberInformation#addAnyTimeInterrogationRequest(long,
     * org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity, org.mobicents
     * .protocols.ss7.map.api.service.subscriberInformation.RequestedInfo,
     * org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    public long addAnyTimeInterrogationRequest(long customInvokeTimeout, SubscriberIdentity subscriberIdentity,
            RequestedInfo requestedInfo, ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer)
            throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.anyTimeEnquiryContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for AnyTimeInterrogationRequest: must be networkLocUpContext_V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        // Operation Code
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.anyTimeInterrogation);
        invoke.setOperationCode(oc);

        AnyTimeInterrogationRequestImpl req = new AnyTimeInterrogationRequestImpl(subscriberIdentity, requestedInfo,
                gsmSCFAddress, extensionContainer);

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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * MAPDialogSubscriberInformation#addAnyTimeInterrogationResponse(long)
     */
    public void addAnyTimeInterrogationResponse(long invokeId, SubscriberInfo subscriberInfo,
            MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.anyTimeEnquiryContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for AnyTimeInterrogationRequest: must be networkLocUpContext_V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.anyTimeInterrogation);
        resultLast.setOperationCode(oc);

        AnyTimeInterrogationResponseImpl req = new AnyTimeInterrogationResponseImpl(subscriberInfo, extensionContainer);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    public long addAnyTimeSubscriptionInterrogationRequest(SubscriberIdentity subscriberIdentity,
            RequestedSubscriptionInfo requestedSubscriptionInfo, ISDNAddressString gsmSCFAddress,
            MAPExtensionContainer extensionContainer, boolean isLongFTNSupported) throws MAPException {
        return this.addAnyTimeSubscriptionInterrogationRequest(_Timer_Default, subscriberIdentity, requestedSubscriptionInfo,
                gsmSCFAddress, extensionContainer, isLongFTNSupported);
    }

    public long addAnyTimeSubscriptionInterrogationRequest(int customTimeout, SubscriberIdentity subscriberIdentity,
            RequestedSubscriptionInfo requestedSubscriptionInfo, ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer,
            boolean isLongFTNSupported) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.anyTimeInfoHandlingContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for AnyTimeSubscriptionInterrogationRequest: must be anyTimeInfoHandlingContext_V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customTimeout);

        // Operation Code
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.anyTimeSubscriptionInterrogation);
        invoke.setOperationCode(oc);

        AnyTimeSubscriptionInterrogationRequestImpl req = new AnyTimeSubscriptionInterrogationRequestImpl(subscriberIdentity, requestedSubscriptionInfo, gsmSCFAddress, extensionContainer, isLongFTNSupported);
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

    public void addAnyTimeSubscriptionInterrogationResponse(long invokeId, CallForwardingData callForwardingData,
            CallBarringData callBarringData, ODBInfo odbInfo, CAMELSubscriptionInfo camelSubscriptionInfo,
            SupportedCamelPhases supportedVlrCamelPhases, SupportedCamelPhases supportedSgsnCamelPhases, MAPExtensionContainer extensionContainer,
            OfferedCamel4CSIs offeredCamel4CSIsInVlr, OfferedCamel4CSIs offeredCamel4CSIsInSgsn, ArrayList<MSISDNBS> msisdnBsList,
            ArrayList<CSGSubscriptionData> csgSubscriptionDataList, CallWaitingData callWaitingData, CallHoldData callHoldData, ClipData clipData,
            ClirData clirData, EctData ectData) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.anyTimeInfoHandlingContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for AnyTimeSubscriptionInterrogationRequest: must be anyTimeInfoHandlingContext_V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.anyTimeSubscriptionInterrogation);
        resultLast.setOperationCode(oc);

        AnyTimeSubscriptionInterrogationResponseImpl req = new AnyTimeSubscriptionInterrogationResponseImpl(callForwardingData, callBarringData, odbInfo,
                camelSubscriptionInfo, supportedVlrCamelPhases, supportedSgsnCamelPhases, extensionContainer, offeredCamel4CSIsInVlr, offeredCamel4CSIsInSgsn,
                msisdnBsList, csgSubscriptionDataList, callWaitingData, callHoldData, clipData, clirData, ectData);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public long addProvideSubscriberInfoRequest(IMSI imsi, LMSI lmsi, RequestedInfo requestedInfo, MAPExtensionContainer extensionContainer,
            EMLPPPriority callPriority) throws MAPException {
        return this.addProvideSubscriberInfoRequest(_Timer_Default, imsi, lmsi, requestedInfo, extensionContainer, callPriority);
    }

    @Override
    public long addProvideSubscriberInfoRequest(long customInvokeTimeout, IMSI imsi, LMSI lmsi, RequestedInfo requestedInfo,
            MAPExtensionContainer extensionContainer, EMLPPPriority callPriority) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.subscriberInfoEnquiryContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException("Bad application context name for ProvideSubscriberInfoRequest: must be subscriberInfoEnquiryContext_V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        // Operation Code
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.provideSubscriberInfo);
        invoke.setOperationCode(oc);

        ProvideSubscriberInfoRequestImpl req = new ProvideSubscriberInfoRequestImpl(imsi, lmsi, requestedInfo, extensionContainer, callPriority);

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
    public void addProvideSubscriberInfoResponse(long invokeId, SubscriberInfo subscriberInfo, MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.subscriberInfoEnquiryContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for ProvideSubscriberInfoResponse: must be subscriberInfoEnquiryContext_V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.provideSubscriberInfo);
        resultLast.setOperationCode(oc);

        ProvideSubscriberInfoResponseImpl req = new ProvideSubscriberInfoResponseImpl(subscriberInfo, extensionContainer);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addCheckImeiRequest(IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
            MAPExtensionContainer extensionContainer) throws MAPException {

        return this.addCheckImeiRequest(_Timer_Default, imei, requestedEquipmentInfo, extensionContainer);
    }

    @Override
    public Long addCheckImeiRequest(long customInvokeTimeout, IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
            MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.equipmentMngtContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3)) {
            throw new MAPException(
                    "Bad application context name for CheckImeiRequest: must be equipmentMngtContext_V1, V2 or V3");
        }

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default) {
            invoke.setTimeout(_Timer_m);
        } else {
            invoke.setTimeout(customInvokeTimeout);
        }

        // Operation Code
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.checkIMEI);
        invoke.setOperationCode(oc);

        CheckImeiRequestImpl req = new CheckImeiRequestImpl(this.appCntx.getApplicationContextVersion().getVersion(), imei,
                requestedEquipmentInfo, extensionContainer);

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
    public void addCheckImeiResponse(long invokeId, EquipmentStatus equipmentStatus, UESBIIu bmuef,
            MAPExtensionContainer extensionContainer) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.equipmentMngtContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3)) {
            throw new MAPException(
                    "Bad application context name for CheckImeiResponse: must be equipmentMngtContext_V1, V2 or V3");
        }

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();
        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.checkIMEI);
        resultLast.setOperationCode(oc);

        CheckImeiResponseImpl resp = new CheckImeiResponseImpl(this.appCntx.getApplicationContextVersion().getVersion(),
                equipmentStatus, bmuef, extensionContainer);
        AsnOutputStream aos = new AsnOutputStream();
        resp.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(resp.getTagClass());
        p.setPrimitive(resp.getIsPrimitive());
        p.setTag(resp.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addCheckImeiRequest_Huawei(IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
            MAPExtensionContainer extensionContainer, IMSI imsi) throws MAPException {

        return this.addCheckImeiRequest_Huawei(_Timer_Default, imei, requestedEquipmentInfo, extensionContainer, imsi);
    }

    @Override
    public Long addCheckImeiRequest_Huawei(long customInvokeTimeout, IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
            MAPExtensionContainer extensionContainer, IMSI imsi) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.equipmentMngtContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3)) {
            throw new MAPException(
                    "Bad application context name for CheckImeiRequest: must be equipmentMngtContext_V1, V2 or V3");
        }

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default) {
            invoke.setTimeout(_Timer_m);
        } else {
            invoke.setTimeout(customInvokeTimeout);
        }

        // Operation Code
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.checkIMEI);
        invoke.setOperationCode(oc);

        CheckImeiRequestImpl req = new CheckImeiRequestImpl(this.appCntx.getApplicationContextVersion().getVersion(), imei,
                requestedEquipmentInfo, extensionContainer);
        req.setIMSI(imsi);

        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        p.setEncodingLength(req.getEncodedLength());
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
    public Long addInsertSubscriberDataRequest(IMSI imsi, ISDNAddressString msisdn, Category category,
            SubscriberStatus subscriberStatus, ArrayList<ExtBearerServiceCode> bearerServiceList,
            ArrayList<ExtTeleserviceCode> teleserviceList, ArrayList<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, ArrayList<ZoneCode> regionalSubscriptionData,
            ArrayList<VoiceBroadcastData> vbsSubscriptionData, ArrayList<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo) throws MAPException {

        return this.addInsertSubscriberDataRequest(_Timer_Default, imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData,
                vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo);
    }

    @Override
    public Long addInsertSubscriberDataRequest(long customInvokeTimeout, IMSI imsi, ISDNAddressString msisdn,
            Category category, SubscriberStatus subscriberStatus, ArrayList<ExtBearerServiceCode> bearerServiceList,
            ArrayList<ExtTeleserviceCode> teleserviceList, ArrayList<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, ArrayList<ZoneCode> regionalSubscriptionData,
            ArrayList<VoiceBroadcastData> vbsSubscriptionData, ArrayList<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo) throws MAPException {

        return this.addInsertSubscriberDataRequest(customInvokeTimeout, imsi, msisdn, category, subscriberStatus,
                bearerServiceList, teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature,
                regionalSubscriptionData, vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo, null, null,
                null, false, null, null, false, null, null, null, null, null, null, null, null, false, null, null, false, null,
                null, null, false, false, null);
    }

    @Override
    public Long addInsertSubscriberDataRequest(IMSI imsi, ISDNAddressString msisdn, Category category,
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
            Long subscribedPeriodicLAUtimer) throws MAPException {

        return this.addInsertSubscriberDataRequest(_Timer_Default, imsi, msisdn, category, subscriberStatus, bearerServiceList,
                teleserviceList, provisionedSS, odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData,
                vbsSubscriptionData, vgcsSubscriptionData, vlrCamelSubscriptionInfo, extensionContainer, naeaPreferredCI,
                gprsSubscriptionData, roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode, lsaInformation,
                lmuIndicator, lcsInformation, istAlertTimer, superChargerSupportedInHLR, mcSsInfo,
                csAllocationRetentionPriority, sgsnCamelSubscriptionInfo, chargingCharacteristics, accessRestrictionData,
                icsIndicator, epsSubscriptionData, csgSubscriptionDataList, ueReachabilityRequestIndicator, sgsnNumber,
                mmeName, subscribedPeriodicRAUTAUtimer, vplmnLIPAAllowed, mdtUserConsent, subscribedPeriodicLAUtimer);
    }

    @Override
    public Long addInsertSubscriberDataRequest(long customInvokeTimeout, IMSI imsi, ISDNAddressString msisdn,
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
            Long subscribedPeriodicLAUtimer) throws MAPException {

        boolean isSubscriberDataMngtContext = false;
        boolean isNetworkLocUpContext = false;
        boolean isGprsLocationUpdateContext = false;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.subscriberDataMngtContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx
                        .getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isSubscriberDataMngtContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.networkLocUpContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx
                        .getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isNetworkLocUpContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.gprsLocationUpdateContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isGprsLocationUpdateContext = true;
        if (isSubscriberDataMngtContext == false && isNetworkLocUpContext == false && isGprsLocationUpdateContext == false)
            throw new MAPException(
                    "Bad application context name for InsertSubscriberDataRequest: must be networkLocUpContext_V1, V2 or V3 or "
                            + "subscriberDataMngtContext_V1, V2 or V3 or gprsLocationUpdateContext_V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default) {
            invoke.setTimeout(_Timer_m);
        } else {
            invoke.setTimeout(customInvokeTimeout);
        }

        // Operation Code
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.insertSubscriberData);
        invoke.setOperationCode(oc);

        InsertSubscriberDataRequestImpl req = new InsertSubscriberDataRequestImpl(this.appCntx.getApplicationContextVersion()
                .getVersion(), imsi, msisdn, category, subscriberStatus, bearerServiceList, teleserviceList, provisionedSS,
                odbData, roamingRestrictionDueToUnsupportedFeature, regionalSubscriptionData, vbsSubscriptionData,
                vgcsSubscriptionData, vlrCamelSubscriptionInfo, extensionContainer, naeaPreferredCI, gprsSubscriptionData,
                roamingRestrictedInSgsnDueToUnsupportedFeature, networkAccessMode, lsaInformation, lmuIndicator,
                lcsInformation, istAlertTimer, superChargerSupportedInHLR, mcSsInfo, csAllocationRetentionPriority,
                sgsnCamelSubscriptionInfo, chargingCharacteristics, accessRestrictionData, icsIndicator, epsSubscriptionData,
                csgSubscriptionDataList, ueReachabilityRequestIndicator, sgsnNumber, mmeName, subscribedPeriodicRAUTAUtimer,
                vplmnLIPAAllowed, mdtUserConsent, subscribedPeriodicLAUtimer);

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
    public void addInsertSubscriberDataResponse(long invokeId, ArrayList<ExtTeleserviceCode> teleserviceList,
            ArrayList<ExtBearerServiceCode> bearerServiceList, ArrayList<SSCode> ssList, ODBGeneralData odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse) throws MAPException {

        this.addInsertSubscriberDataResponse(invokeId, teleserviceList, bearerServiceList, ssList, odbGeneralData,
                regionalSubscriptionResponse, null, null, null, null);
    }

    @Override
    public void addInsertSubscriberDataResponse(long invokeId, ArrayList<ExtTeleserviceCode> teleserviceList,
            ArrayList<ExtBearerServiceCode> bearerServiceList, ArrayList<SSCode> ssList, ODBGeneralData odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse, SupportedCamelPhases supportedCamelPhases,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIs, SupportedFeatures supportedFeatures)
            throws MAPException {

        boolean isSubscriberDataMngtContext = false;
        boolean isNetworkLocUpContext = false;
        boolean isGprsLocationUpdateContext = false;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.subscriberDataMngtContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx
                        .getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isSubscriberDataMngtContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.networkLocUpContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx
                        .getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isNetworkLocUpContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.gprsLocationUpdateContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isGprsLocationUpdateContext = true;
        if (isSubscriberDataMngtContext == false && isNetworkLocUpContext == false && isGprsLocationUpdateContext == false)
            throw new MAPException(
                    "Bad application context name for InsertSubscriberDataResponse: must be networkLocUpContext_V1, V2 or V3 or "
                            + "subscriberDataMngtContext_V1, V2 or V3 or gprsLocationUpdateContext_V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();
        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.insertSubscriberData);
        resultLast.setOperationCode(oc);

        if ((teleserviceList != null || bearerServiceList != null || ssList != null || odbGeneralData != null || regionalSubscriptionResponse != null
                || supportedCamelPhases != null || extensionContainer != null || offeredCamel4CSIs != null || supportedFeatures != null)
                && this.appCntx.getApplicationContextVersion().getVersion() != 1) {
            InsertSubscriberDataResponseImpl resp = new InsertSubscriberDataResponseImpl(this.appCntx.getApplicationContextVersion().getVersion(),
                    teleserviceList, bearerServiceList, ssList, odbGeneralData, regionalSubscriptionResponse, supportedCamelPhases, extensionContainer,
                    offeredCamel4CSIs, supportedFeatures);
            AsnOutputStream aos = new AsnOutputStream();
            resp.encodeData(aos);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(resp.getTagClass());
            p.setPrimitive(resp.getIsPrimitive());
            p.setTag(resp.getTag());
            p.setData(aos.toByteArray());
            resultLast.setParameter(p);
        }

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addDeleteSubscriberDataRequest(IMSI imsi, ArrayList<ExtBasicServiceCode> basicServiceList, ArrayList<SSCode> ssList,
            boolean roamingRestrictionDueToUnsupportedFeature, ZoneCode regionalSubscriptionIdentifier, boolean vbsGroupIndication,
            boolean vgcsGroupIndication, boolean camelSubscriptionInfoWithdraw, MAPExtensionContainer extensionContainer,
            GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw, boolean roamingRestrictedInSgsnDueToUnsuppportedFeature,
            LSAInformationWithdraw lsaInformationWithdraw, boolean gmlcListWithdraw, boolean istInformationWithdraw, SpecificCSIWithdraw specificCSIWithdraw,
            boolean chargingCharacteristicsWithdraw, boolean stnSrWithdraw, EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw,
            boolean apnOiReplacementWithdraw, boolean csgSubscriptionDeleted) throws MAPException {

        return this.addDeleteSubscriberDataRequest(_Timer_Default, imsi, basicServiceList, ssList, roamingRestrictionDueToUnsupportedFeature,
                regionalSubscriptionIdentifier, vbsGroupIndication, vgcsGroupIndication, camelSubscriptionInfoWithdraw, extensionContainer,
                gprsSubscriptionDataWithdraw, roamingRestrictedInSgsnDueToUnsuppportedFeature, lsaInformationWithdraw, gmlcListWithdraw,
                istInformationWithdraw, specificCSIWithdraw, chargingCharacteristicsWithdraw, stnSrWithdraw, epsSubscriptionDataWithdraw,
                apnOiReplacementWithdraw, csgSubscriptionDeleted);
    }

    @Override
    public Long addDeleteSubscriberDataRequest(long customInvokeTimeout, IMSI imsi, ArrayList<ExtBasicServiceCode> basicServiceList, ArrayList<SSCode> ssList,
            boolean roamingRestrictionDueToUnsupportedFeature, ZoneCode regionalSubscriptionIdentifier, boolean vbsGroupIndication,
            boolean vgcsGroupIndication, boolean camelSubscriptionInfoWithdraw, MAPExtensionContainer extensionContainer,
            GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw, boolean roamingRestrictedInSgsnDueToUnsuppportedFeature,
            LSAInformationWithdraw lsaInformationWithdraw, boolean gmlcListWithdraw, boolean istInformationWithdraw, SpecificCSIWithdraw specificCSIWithdraw,
            boolean chargingCharacteristicsWithdraw, boolean stnSrWithdraw, EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw,
            boolean apnOiReplacementWithdraw, boolean csgSubscriptionDeleted) throws MAPException {

        boolean isSubscriberDataMngtContext = false;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.subscriberDataMngtContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isSubscriberDataMngtContext = true;
        if (isSubscriberDataMngtContext == false)
            throw new MAPException("Bad application context name for DeleteSubscriberDataRequest: must be subscriberDataMngtContext_V1, V2 or V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default) {
            invoke.setTimeout(_Timer_m);
        } else {
            invoke.setTimeout(customInvokeTimeout);
        }

        // Operation Code
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.deleteSubscriberData);
        invoke.setOperationCode(oc);

        DeleteSubscriberDataRequestImpl req = new DeleteSubscriberDataRequestImpl(imsi, basicServiceList, ssList, roamingRestrictionDueToUnsupportedFeature,
                regionalSubscriptionIdentifier, vbsGroupIndication, vgcsGroupIndication, camelSubscriptionInfoWithdraw, extensionContainer,
                gprsSubscriptionDataWithdraw, roamingRestrictedInSgsnDueToUnsuppportedFeature, lsaInformationWithdraw, gmlcListWithdraw,
                istInformationWithdraw, specificCSIWithdraw, chargingCharacteristicsWithdraw, stnSrWithdraw, epsSubscriptionDataWithdraw,
                apnOiReplacementWithdraw, csgSubscriptionDeleted);

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
    public void addDeleteSubscriberDataResponse(long invokeId, RegionalSubscriptionResponse regionalSubscriptionResponse,
            MAPExtensionContainer extensionContainer) throws MAPException {

        boolean isSubscriberDataMngtContext = false;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.subscriberDataMngtContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isSubscriberDataMngtContext = true;
        if (isSubscriberDataMngtContext == false)
            throw new MAPException("Bad application context name for DeleteSubscriberDataResponse: must be subscriberDataMngtContext_V1, V2 or V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();
        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.deleteSubscriberData);
        resultLast.setOperationCode(oc);

        if ((regionalSubscriptionResponse != null || extensionContainer != null) && this.appCntx.getApplicationContextVersion().getVersion() != 1) {
            DeleteSubscriberDataResponseImpl resp = new DeleteSubscriberDataResponseImpl(regionalSubscriptionResponse, extensionContainer);
            AsnOutputStream aos = new AsnOutputStream();
            resp.encodeData(aos);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(resp.getTagClass());
            p.setPrimitive(resp.getIsPrimitive());
            p.setTag(resp.getTag());
            p.setData(aos.toByteArray());
            resultLast.setParameter(p);
        }

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addCancelLocationRequest(IMSI imsi, IMSIWithLMSI imsiWithLmsi, CancellationType cancellationType,
            MAPExtensionContainer extensionContainer, TypeOfUpdate typeOfUpdate, boolean mtrfSupportedAndAuthorized,
            boolean mtrfSupportedAndNotAuthorized, ISDNAddressString newMSCNumber, ISDNAddressString newVLRNumber, LMSI newLmsi)
            throws MAPException {

        return this.addCancelLocationRequest(_Timer_Default, imsi, imsiWithLmsi, cancellationType, extensionContainer,
                typeOfUpdate, mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi);
    }

    @Override
    public Long addCancelLocationRequest(int customInvokeTimeout, IMSI imsi, IMSIWithLMSI imsiWithLmsi,
            CancellationType cancellationType, MAPExtensionContainer extensionContainer, TypeOfUpdate typeOfUpdate,
            boolean mtrfSupportedAndAuthorized, boolean mtrfSupportedAndNotAuthorized, ISDNAddressString newMSCNumber,
            ISDNAddressString newVLRNumber, LMSI newLmsi) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationCancellationContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for CancelLocationRequest: must be locationCancellationContext_V1, V2 or V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.cancelLocation);
        invoke.setOperationCode(oc);

        CancelLocationRequestImpl req = new CancelLocationRequestImpl(imsi, imsiWithLmsi, cancellationType, extensionContainer,
                typeOfUpdate, mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi,
                this.appCntx.getApplicationContextVersion().getVersion());

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
    public void addCancelLocationResponse(long invokeId, MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationCancellationContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1
                        && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for CancelLocationResponse: must be locationCancellationContext_V1, V2 or V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.cancelLocation);
        resultLast.setOperationCode(oc);

        if (extensionContainer != null) {
            CancelLocationResponseImpl req = new CancelLocationResponseImpl(extensionContainer);

            AsnOutputStream aos = new AsnOutputStream();
            req.encodeData(aos);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(req.getTagClass());
            p.setPrimitive(req.getIsPrimitive());
            p.setTag(req.getTag());
            p.setData(aos.toByteArray());
            resultLast.setParameter(p);
        }

        this.sendReturnResultLastComponent(resultLast);

    }

    @Override
    public Long addSendIdentificationRequest(TMSI tmsi, Integer numberOfRequestedVectors, boolean segmentationProhibited,
            MAPExtensionContainer extensionContainer, ISDNAddressString mscNumber, LAIFixedLength previousLAI,
            Integer hopCounter, boolean mtRoamingForwardingSupported, ISDNAddressString newVLRNumber, LMSI lmsi)
            throws MAPException {
        return this.addSendIdentificationRequest(_Timer_Default, tmsi, numberOfRequestedVectors, segmentationProhibited,
                extensionContainer, mscNumber, previousLAI, hopCounter, mtRoamingForwardingSupported, newVLRNumber, lmsi);
    }

    @Override
    public Long addSendIdentificationRequest(int customInvokeTimeout, TMSI tmsi, Integer numberOfRequestedVectors,
            boolean segmentationProhibited, MAPExtensionContainer extensionContainer, ISDNAddressString mscNumber,
            LAIFixedLength previousLAI, Integer hopCounter, boolean mtRoamingForwardingSupported,
            ISDNAddressString newVLRNumber, LMSI lmsi) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.interVlrInfoRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for SendIdentificationRequest: must be interVlrInfoRetrievalContext_V2 or V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_s);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.sendIdentification);
        invoke.setOperationCode(oc);

        SendIdentificationRequestImpl req = new SendIdentificationRequestImpl(tmsi, numberOfRequestedVectors,
                segmentationProhibited, extensionContainer, mscNumber, previousLAI, hopCounter, mtRoamingForwardingSupported,
                newVLRNumber, lmsi, this.appCntx.getApplicationContextVersion().getVersion());

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
    public void addSendIdentificationResponse(long invokeId, IMSI imsi, AuthenticationSetList authenticationSetList,
            CurrentSecurityContext currentSecurityContext, MAPExtensionContainer extensionContainer) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.interVlrInfoRetrievalContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for CancelLocationResponse: must be interVlrInfoRetrievalContext_V2 or V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.sendIdentification);
        resultLast.setOperationCode(oc);

        SendIdentificationResponseImpl req = new SendIdentificationResponseImpl(imsi, authenticationSetList,
                currentSecurityContext, extensionContainer, this.appCntx.getApplicationContextVersion().getVersion());

        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addUpdateGprsLocationRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString sgsnNumber,
            GSNAddress sgsnAddress, MAPExtensionContainer extensionContainer, SGSNCapability sgsnCapability,
            boolean informPreviousNetworkEntity, boolean psLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo,
            EPSInfo epsInfo, boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
            boolean gprsSubscriptionDataNotNeeded, boolean nodeTypeIndicator, boolean areaRestricted,
            boolean ueReachableIndicator, boolean epsSubscriptionDataNotNeeded, UESRVCCCapability uesrvccCapability)
            throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.gprsLocationUpdateContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for UpdateGprsLocationRequest: must be gprsLocationUpdateContext_V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.updateGprsLocation);
        invoke.setOperationCode(oc);

        UpdateGprsLocationRequestImpl req = new UpdateGprsLocationRequestImpl(imsi, sgsnNumber, sgsnAddress,
                extensionContainer, sgsnCapability, informPreviousNetworkEntity, psLCSNotSupportedByUE, vGmlcAddress, addInfo,
                epsInfo, servingNodeTypeIndicator, skipSubscriberDataUpdate, usedRATType, gprsSubscriptionDataNotNeeded,
                nodeTypeIndicator, areaRestricted, ueReachableIndicator, epsSubscriptionDataNotNeeded, uesrvccCapability,
                this.appCntx.getApplicationContextVersion().getVersion());

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
    public Long addUpdateGprsLocationRequest(IMSI imsi, ISDNAddressString sgsnNumber, GSNAddress sgsnAddress,
            MAPExtensionContainer extensionContainer, SGSNCapability sgsnCapability, boolean informPreviousNetworkEntity,
            boolean psLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo, EPSInfo epsInfo,
            boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
            boolean gprsSubscriptionDataNotNeeded, boolean nodeTypeIndicator, boolean areaRestricted,
            boolean ueReachableIndicator, boolean epsSubscriptionDataNotNeeded, UESRVCCCapability uesrvccCapability)
            throws MAPException {
        return addUpdateGprsLocationRequest(_Timer_Default, imsi, sgsnNumber, sgsnAddress, extensionContainer, sgsnCapability,
                informPreviousNetworkEntity, psLCSNotSupportedByUE, vGmlcAddress, addInfo, epsInfo, servingNodeTypeIndicator,
                skipSubscriberDataUpdate, usedRATType, gprsSubscriptionDataNotNeeded, nodeTypeIndicator, areaRestricted,
                ueReachableIndicator, epsSubscriptionDataNotNeeded, uesrvccCapability);
    }

    @Override
    public void addUpdateGprsLocationResponse(long invokeId, ISDNAddressString hlrNumber,
            MAPExtensionContainer extensionContainer, boolean addCapability, boolean sgsnMmeSeparationSupported)
            throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.gprsLocationUpdateContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for UpdateGprsLocationResponse: must be gprsLocationUpdateContext_V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.updateGprsLocation);
        resultLast.setOperationCode(oc);

        UpdateGprsLocationResponseImpl req = new UpdateGprsLocationResponseImpl(hlrNumber, extensionContainer, addCapability,
                sgsnMmeSeparationSupported);

        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);

    }

    @Override
    public Long addPurgeMSRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString vlrNumber,
            ISDNAddressString sgsnNumber, MAPExtensionContainer extensionContainer) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.msPurgingContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
                && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)))
            throw new MAPException(
                    "Bad application context name for PurgeMSRequest: must be msPurgingContext_V2 or msPurgingContext_V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.purgeMS);
        invoke.setOperationCode(oc);

        PurgeMSRequestImpl req = new PurgeMSRequestImpl(imsi, vlrNumber, sgsnNumber, extensionContainer, this.appCntx
                .getApplicationContextVersion().getVersion());

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
    public Long addPurgeMSRequest(IMSI imsi, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber,
            MAPExtensionContainer extensionContainer) throws MAPException {
        return addPurgeMSRequest(_Timer_Default, imsi, vlrNumber, sgsnNumber, extensionContainer);
    }

    @Override
    public void addPurgeMSResponse(long invokeId, boolean freezeTMSI, boolean freezePTMSI,
            MAPExtensionContainer extensionContainer, boolean freezeMTMSI) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.msPurgingContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3) && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)))
            throw new MAPException("Bad application context name for PurgeMSResponse: must be msPurgingContext_V3 OR  msPurgingContext_V2");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.purgeMS);
        resultLast.setOperationCode(oc);

        PurgeMSResponseImpl resp = new PurgeMSResponseImpl(freezeTMSI, freezePTMSI, extensionContainer, freezeMTMSI);

        if (this.appCntx.getApplicationContextVersion().getVersion() >= 3 && (freezeTMSI || freezePTMSI || extensionContainer != null || freezeMTMSI)) {
            AsnOutputStream aos = new AsnOutputStream();
            resp.encodeData(aos);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(resp.getTagClass());
            p.setPrimitive(resp.getIsPrimitive());
            p.setTag(resp.getTag());
            p.setData(aos.toByteArray());
            resultLast.setParameter(p);
        }

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addResetRequest(NetworkResource networkResource, ISDNAddressString hlrNumber, ArrayList<IMSI> hlrList) throws MAPException {
        return addResetRequest(_Timer_Default, networkResource, hlrNumber, hlrList);
    }

    @Override
    public Long addResetRequest(int customInvokeTimeout, NetworkResource networkResource, ISDNAddressString hlrNumber, ArrayList<IMSI> hlrList)
            throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.resetContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2) && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1)))
            throw new MAPException("Bad application context name for ResetRequest: must be resetContext_V1 or V2");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest(InvokeClass.Class4);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.reset);
        invoke.setOperationCode(oc);

        int version = this.appCntx.getApplicationContextVersion().getVersion();
        ResetRequestImpl req = new ResetRequestImpl(networkResource, hlrNumber, hlrList, version);

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
    public Long addForwardCheckSSIndicationRequest() throws MAPException {
        return addForwardCheckSSIndicationRequest(_Timer_Default);
    }

    @Override
    public Long addForwardCheckSSIndicationRequest(int customInvokeTimeout) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
                        && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2) && (this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version1)))
            throw new MAPException("Bad application context name for ForwardCheckSSIndicationRequest: must be networkLocUpContext_V1, V2 or V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest(InvokeClass.Class4);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_s);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.forwardCheckSsIndication);
        invoke.setOperationCode(oc);

        ForwardCheckSSIndicationRequestImpl req = new ForwardCheckSSIndicationRequestImpl();

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
    public Long addRestoreDataRequest(IMSI imsi, LMSI lmsi, VLRCapability vlrCapability, MAPExtensionContainer extensionContainer, boolean restorationIndicator)
            throws MAPException {
        return addRestoreDataRequest(_Timer_Default, imsi, lmsi, vlrCapability, extensionContainer, restorationIndicator);
    }

    @Override
    public Long addRestoreDataRequest(int customInvokeTimeout, IMSI imsi, LMSI lmsi, VLRCapability vlrCapability, MAPExtensionContainer extensionContainer,
            boolean restorationIndicator) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3) && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)))
            throw new MAPException("Bad application context name for RestoreDataRequest: must be networkLocUpContext_V2 or networkLocUpContext_V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.restoreData);
        invoke.setOperationCode(oc);

        RestoreDataRequestImpl req = new RestoreDataRequestImpl(imsi, lmsi, vlrCapability, extensionContainer, restorationIndicator);

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
    public void addRestoreDataResponse(long invokeId, ISDNAddressString hlrNumber, boolean msNotReachable, MAPExtensionContainer extensionContainer)
            throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkLocUpContext)
                || ((this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3) && (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)))
            throw new MAPException("Bad application context name for RestoreDataResponse: must be networkLocUpContext_V2 or networkLocUpContext_V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.restoreData);
        resultLast.setOperationCode(oc);

        RestoreDataResponseImpl resp = new RestoreDataResponseImpl(hlrNumber, msNotReachable, extensionContainer);

        AsnOutputStream aos = new AsnOutputStream();
        resp.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(resp.getTagClass());
        p.setPrimitive(resp.getIsPrimitive());
        p.setTag(resp.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addActivateTraceModeRequest(IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
            MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
            TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration)
            throws MAPException {
        return this.addActivateTraceModeRequest(_Timer_Default, imsi, traceReference, traceType, omcId, extensionContainer, traceReference2, traceDepthList,
                traceNeTypeList, traceInterfaceList, traceEventList, traceCollectionEntity, mdtConfiguration);
    }

    @Override
    public Long addActivateTraceModeRequest(int customInvokeTimeout, IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
            MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
            TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration)
            throws MAPException {

        boolean isTracingContext = false;
        boolean isNetworkLocUpContext = false;
        boolean isGprsLocationUpdateContext = false;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.tracingContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isTracingContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.networkLocUpContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isNetworkLocUpContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.gprsLocationUpdateContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isGprsLocationUpdateContext = true;

        if (!isTracingContext && !isNetworkLocUpContext && !isGprsLocationUpdateContext)
            throw new MAPException(
                    "Bad application context name for activateTraceModeRequest: must be tracingContext_V1, V2 or V3, networkLocUpContext_V1, V2 or V3 or gprsLocationUpdateContext_V3");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.activateTraceMode);
        invoke.setOperationCode(oc);

        ActivateTraceModeRequestImpl_Mobility req = new ActivateTraceModeRequestImpl_Mobility(imsi, traceReference, traceType, omcId, extensionContainer, traceReference2,
                traceDepthList, traceNeTypeList, traceInterfaceList, traceEventList, traceCollectionEntity, mdtConfiguration);
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
    public void addActivateTraceModeResponse(long invokeId, MAPExtensionContainer extensionContainer, boolean traceSupportIndicator) throws MAPException {
        boolean isTracingContext = false;
        boolean isNetworkLocUpContext = false;
        boolean isGprsLocationUpdateContext = false;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.tracingContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isTracingContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.networkLocUpContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1
                        || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version2 || this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isNetworkLocUpContext = true;
        if ((this.appCntx.getApplicationContextName() == MAPApplicationContextName.gprsLocationUpdateContext)
                && (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version3))
            isGprsLocationUpdateContext = true;

        if (!isTracingContext && !isNetworkLocUpContext && !isGprsLocationUpdateContext)
            throw new MAPException(
                    "Bad application context name for activateTraceModeResponse: must be tracingContext_V1, V2 or V3, networkLocUpContext_V1, V2 or V3 or gprsLocationUpdateContext_V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        if ((traceSupportIndicator || extensionContainer != null) && this.appCntx.getApplicationContextVersion().getVersion() >= 3) {
            // Operation Code
            OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
            oc.setLocalOperationCode((long) MAPOperationCode.activateTraceMode);
            resultLast.setOperationCode(oc);

            ActivateTraceModeResponseImpl_Mobility req = new ActivateTraceModeResponseImpl_Mobility(extensionContainer, traceSupportIndicator);
            AsnOutputStream aos = new AsnOutputStream();
            req.encodeData(aos);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(req.getTagClass());
            p.setPrimitive(req.getIsPrimitive());
            p.setTag(req.getTag());
            p.setData(aos.toByteArray());
            resultLast.setParameter(p);
        }

        this.sendReturnResultLastComponent(resultLast);
    }

}
