/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.CAPDialogImpl;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPServiceBase;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.RedirectingPartyIDCap;
import org.mobicents.protocols.ss7.cap.api.primitives.AChChargingAddress;
import org.mobicents.protocols.ss7.cap.api.primitives.BCSMEvent;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.ScfID;
import org.mobicents.protocols.ss7.cap.api.primitives.SendingSideID;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.primitives.TimerID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCap;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentToCancel;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ContinueWithArgumentArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FCIBCCCAMELsequence1;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSend;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResult;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGIndex;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4Functionalities;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 *
 * @author sergey vetyutnev
 * @author alerant appngin
 *
 */
public class CAPDialogCircuitSwitchedCallImpl extends CAPDialogImpl implements CAPDialogCircuitSwitchedCall {

    protected CAPDialogCircuitSwitchedCallImpl(CAPApplicationContext appCntx, Dialog tcapDialog,
            CAPProviderImpl capProviderImpl, CAPServiceBase capService) {
        super(appCntx, tcapDialog, capProviderImpl, capService);
    }

    @Override
    public Long addInitialDPRequest(int serviceKey, CalledPartyNumberCap calledPartyNumber,
            CallingPartyNumberCap callingPartyNumber, CallingPartysCategoryInap callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities, LocationNumberCap locationNumber,
            OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
            HighLayerCompatibilityInap highLayerCompatibility, Digits additionalCallingPartyNumber,
            BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID,
            RedirectionInformationInap redirectionInformation, CauseCap cause,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState,
            LocationInformation locationInformation, ExtBasicServiceCode extBasicServiceCode,
            CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress, CalledPartyBCDNumber calledPartyBCDNumber,
            TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtension initialDPArgExtension)
            throws CAPException {

        return addInitialDPRequest(_Timer_Default, serviceKey, calledPartyNumber, callingPartyNumber, callingPartysCategory,
                CGEncountered, IPSSPCapabilities, locationNumber, originalCalledPartyID, extensions, highLayerCompatibility,
                additionalCallingPartyNumber, bearerCapability, eventTypeBCSM, redirectingPartyID, redirectionInformation,
                cause, serviceInteractionIndicatorsTwo, carrier, cugIndex, cugInterlock, cugOutgoingAccess, imsi,
                subscriberState, locationInformation, extBasicServiceCode, callReferenceNumber, mscAddress,
                calledPartyBCDNumber, timeAndTimezone, callForwardingSSPending, initialDPArgExtension);
    }

    @Override
    public Long addInitialDPRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberCap calledPartyNumber,
            CallingPartyNumberCap callingPartyNumber, CallingPartysCategoryInap callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities, LocationNumberCap locationNumber,
            OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
            HighLayerCompatibilityInap highLayerCompatibility, Digits additionalCallingPartyNumber,
            BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID,
            RedirectionInformationInap redirectionInformation, CauseCap cause,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState,
            LocationInformation locationInformation, ExtBasicServiceCode extBasicServiceCode,
            CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress, CalledPartyBCDNumber calledPartyBCDNumber,
            TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtension initialDPArgExtension)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric)
            throw new CAPException(
                    "Bad application context name for addInitialDPRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric or CapV4_gsmSSF_scfGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.initialDP);
        invoke.setOperationCode(oc);

        InitialDPRequestImpl req = new InitialDPRequestImpl(serviceKey, calledPartyNumber, callingPartyNumber,
                callingPartysCategory, CGEncountered, IPSSPCapabilities, locationNumber, originalCalledPartyID, extensions,
                highLayerCompatibility, additionalCallingPartyNumber, bearerCapability, eventTypeBCSM, redirectingPartyID,
                redirectionInformation, cause, serviceInteractionIndicatorsTwo, carrier, cugIndex, cugInterlock,
                cugOutgoingAccess, imsi, subscriberState, locationInformation, extBasicServiceCode, callReferenceNumber,
                mscAddress, calledPartyBCDNumber, timeAndTimezone, callForwardingSSPending, initialDPArgExtension, this.appCntx
                        .getVersion().getVersion() >= 3);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addApplyChargingReportRequest(TimeDurationChargingResult timeDurationChargingResult) throws CAPException {
        return addApplyChargingReportRequest(_Timer_Default, timeDurationChargingResult);
    }

    @Override
    public Long addApplyChargingReportRequest(int customInvokeTimeout, TimeDurationChargingResult timeDurationChargingResult)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addApplyChargingReportRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_gsmSSF_scfGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.applyChargingReport);
        invoke.setOperationCode(oc);

        ApplyChargingReportRequestImpl req = new ApplyChargingReportRequestImpl(timeDurationChargingResult);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addApplyChargingRequest(CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics,
            SendingSideID partyToCharge, CAPExtensions extensions, AChChargingAddress aChChargingAddress) throws CAPException {

        return addApplyChargingRequest(_Timer_Default, aChBillingChargingCharacteristics, partyToCharge, extensions,
                aChChargingAddress);
    }

    @Override
    public Long addApplyChargingRequest(int customInvokeTimeout,
            CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics, SendingSideID partyToCharge,
            CAPExtensions extensions, AChChargingAddress aChChargingAddress) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addApplyChargingRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.applyCharging);
        invoke.setOperationCode(oc);

        ApplyChargingRequestImpl req = new ApplyChargingRequestImpl(aChBillingChargingCharacteristics, partyToCharge,
                extensions, aChChargingAddress);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addCallInformationReportRequest(ArrayList<RequestedInformation> requestedInformationList,
            CAPExtensions extensions, ReceivingSideID legID) throws CAPException {

        return addCallInformationReportRequest(_Timer_Default, requestedInformationList, extensions, legID);
    }

    @Override
    public Long addCallInformationReportRequest(int customInvokeTimeout,
            ArrayList<RequestedInformation> requestedInformationList, CAPExtensions extensions, ReceivingSideID legID)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addCallInformationReportRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_gsmSSF_scfGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class4);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.callInformationReport);
        invoke.setOperationCode(oc);

        CallInformationReportRequestImpl req = new CallInformationReportRequestImpl(requestedInformationList, extensions, legID);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addCallInformationRequestRequest(ArrayList<RequestedInformationType> requestedInformationTypeList,
            CAPExtensions extensions, SendingSideID legID) throws CAPException {

        return addCallInformationRequestRequest(_Timer_Default, requestedInformationTypeList, extensions, legID);
    }

    @Override
    public Long addCallInformationRequestRequest(int customInvokeTimeout,
            ArrayList<RequestedInformationType> requestedInformationTypeList, CAPExtensions extensions, SendingSideID legID)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addCallInformationRequestRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_gsmSSF_scfGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.callInformationRequest);
        invoke.setOperationCode(oc);

        CallInformationRequestRequestImpl req = new CallInformationRequestRequestImpl(requestedInformationTypeList, extensions,
                legID);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addConnectRequest(DestinationRoutingAddress destinationRoutingAddress, AlertingPatternCap alertingPattern,
            OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions, Carrier carrier,
            CallingPartysCategoryInap callingPartysCategory, RedirectingPartyIDCap redirectingPartyID,
            RedirectionInformationInap redirectionInformation, ArrayList<GenericNumberCap> genericNumbers,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberCap chargeNumber,
            LegID legToBeConnected, CUGInterlock cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws CAPException {

        return addConnectRequest(_Timer_Default, destinationRoutingAddress, alertingPattern, originalCalledPartyID, extensions,
                carrier, callingPartysCategory, redirectingPartyID, redirectionInformation, genericNumbers,
                serviceInteractionIndicatorsTwo, chargeNumber, legToBeConnected, cugInterlock, cugOutgoingAccess,
                suppressionOfAnnouncement, ocsIApplicable, naoliInfo, borInterrogationRequested, suppressNCSI);
    }

    @Override
    public Long addConnectRequest(int customInvokeTimeout, DestinationRoutingAddress destinationRoutingAddress,
            AlertingPatternCap alertingPattern, OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
            Carrier carrier, CallingPartysCategoryInap callingPartysCategory, RedirectingPartyIDCap redirectingPartyID,
            RedirectionInformationInap redirectionInformation, ArrayList<GenericNumberCap> genericNumbers,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberCap chargeNumber,
            LegID legToBeConnected, CUGInterlock cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addConnectRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.connect);
        invoke.setOperationCode(oc);

        ConnectRequestImpl req = new ConnectRequestImpl(destinationRoutingAddress, alertingPattern, originalCalledPartyID,
                extensions, carrier, callingPartysCategory, redirectingPartyID, redirectionInformation, genericNumbers,
                serviceInteractionIndicatorsTwo, chargeNumber, legToBeConnected, cugInterlock, cugOutgoingAccess,
                suppressionOfAnnouncement, ocsIApplicable, naoliInfo, borInterrogationRequested, suppressNCSI);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addContinueRequest() throws CAPException {

        return addContinueRequest(_Timer_Default);
    }

    @Override
    public Long addContinueRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addContinueRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class4);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.continueCode);
        invoke.setOperationCode(oc);

        Long invokeId;
        try {
            invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);
        } catch (TCAPException e) {
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addContinueWithArgumentRequest(AlertingPatternCap alertingPattern, CAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            CallingPartysCategoryInap callingPartysCategory, ArrayList<GenericNumberCap> genericNumbers,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, LocationNumberCap chargeNumber, Carrier carrier,
            boolean suppressionOfAnnouncement, NAOliInfo naOliInfo, boolean borInterrogationRequested,
            boolean suppressOCsi, ContinueWithArgumentArgExtension continueWithArgumentArgExtension)
            throws CAPException {

        return addContinueWithArgumentRequest(_Timer_Default, alertingPattern, extensions,
                serviceInteractionIndicatorsTwo, callingPartysCategory, genericNumbers, cugInterlock,
                cugOutgoingAccess, chargeNumber, carrier, suppressionOfAnnouncement, naOliInfo,
                borInterrogationRequested, suppressOCsi, continueWithArgumentArgExtension);
    }

    @Override
    public Long addContinueWithArgumentRequest(int customInvokeTimeout, AlertingPatternCap alertingPattern,
            CAPExtensions extensions, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            CallingPartysCategoryInap callingPartysCategory, ArrayList<GenericNumberCap> genericNumbers,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, LocationNumberCap chargeNumber, Carrier carrier,
            boolean suppressionOfAnnouncement, NAOliInfo naOliInfo, boolean borInterrogationRequested,
            boolean suppressOCsi, ContinueWithArgumentArgExtension continueWithArgumentArgExtension)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addContinueWithArgumentRequest: must be CapV3_gsmSSF_scfGeneric or CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.continueWithArgument);
        invoke.setOperationCode(oc);

        ContinueWithArgumentRequestImpl req = new ContinueWithArgumentRequestImpl(alertingPattern, extensions,
                serviceInteractionIndicatorsTwo, callingPartysCategory, genericNumbers, cugInterlock,
                cugOutgoingAccess, chargeNumber, carrier, suppressionOfAnnouncement, naOliInfo,
                borInterrogationRequested, suppressOCsi, continueWithArgumentArgExtension);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addEventReportBCSMRequest(EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, ReceivingSideID legID, MiscCallInfo miscCallInfo,
            CAPExtensions extensions) throws CAPException {

        return addEventReportBCSMRequest(_Timer_Default, eventTypeBCSM, eventSpecificInformationBCSM, legID, miscCallInfo,
                extensions);
    }

    @Override
    public Long addEventReportBCSMRequest(int customInvokeTimeout, EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, ReceivingSideID legID, MiscCallInfo miscCallInfo,
            CAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addEventReportBCSMRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class4);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.eventReportBCSM);
        invoke.setOperationCode(oc);

        EventReportBCSMRequestImpl req = new EventReportBCSMRequestImpl(eventTypeBCSM, eventSpecificInformationBCSM, legID,
                miscCallInfo, extensions);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addRequestReportBCSMEventRequest(ArrayList<BCSMEvent> bcsmEventList, CAPExtensions extensions)
            throws CAPException {

        return addRequestReportBCSMEventRequest(_Timer_Default, bcsmEventList, extensions);
    }

    @Override
    public Long addRequestReportBCSMEventRequest(int customInvokeTimeout, ArrayList<BCSMEvent> bcsmEventList,
            CAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addRequestReportBCSMEventRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.requestReportBCSMEvent);
        invoke.setOperationCode(oc);

        RequestReportBCSMEventRequestImpl req = new RequestReportBCSMEventRequestImpl(bcsmEventList, extensions);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addReleaseCallRequest(CauseCap cause) throws CAPException {

        return addReleaseCallRequest(_Timer_Default, cause);
    }

    @Override
    public Long addReleaseCallRequest(int customInvokeTimeout, CauseCap cause) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addReleaseCallRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class4);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.releaseCall);
        invoke.setOperationCode(oc);

        ReleaseCallRequestImpl req = new ReleaseCallRequestImpl(cause);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addActivityTestRequest() throws CAPException {

        return addActivityTestRequest(_Timer_Default);
    }

    @Override
    public Long addActivityTestRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addActivityTestRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF, CapV4_gsmSRF_gsmSCF "
                            + "or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class3);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.activityTest);
        invoke.setOperationCode(oc);

        Long invokeId;
        try {
            invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);
        } catch (TCAPException e) {
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    public void addActivityTestResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addActivityTestResponse: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF, CapV4_gsmSRF_gsmSCF "
                            + "or CapV4_scf_gsmSSFGeneric");

        ReturnResultLast resultLast = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // we need not Operation Code because no answer

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addAssistRequestInstructionsRequest(Digits correlationID, IPSSPCapabilities ipSSPCapabilities,
            CAPExtensions extensions) throws CAPException {

        return addAssistRequestInstructionsRequest(_Timer_Default, correlationID, ipSSPCapabilities, extensions);
    }

    @Override
    public Long addAssistRequestInstructionsRequest(int customInvokeTimeout, Digits correlationID,
            IPSSPCapabilities ipSSPCapabilities, CAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addAssistRequestInstructionsRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF  "
                            + "or CapV4_gsmSRF_gsmSCF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.assistRequestInstructions);
        invoke.setOperationCode(oc);

        AssistRequestInstructionsRequestImpl req = new AssistRequestInstructionsRequestImpl(correlationID, ipSSPCapabilities,
                extensions);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addEstablishTemporaryConnectionRequest(Digits assistingSSPIPRoutingAddress, Digits correlationID, ScfID scfID,
            CAPExtensions extensions, Carrier carrier, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            Integer callSegmentID, NAOliInfo naOliInfo, LocationNumberCap chargeNumber,
            OriginalCalledNumberCap originalCalledPartyID, CallingPartyNumberCap callingPartyNumber) throws CAPException {

        return addEstablishTemporaryConnectionRequest(_Timer_Default, assistingSSPIPRoutingAddress, correlationID, scfID,
                extensions, carrier, serviceInteractionIndicatorsTwo, callSegmentID, naOliInfo, chargeNumber,
                originalCalledPartyID, callingPartyNumber);
    }

    @Override
    public Long addEstablishTemporaryConnectionRequest(int customInvokeTimeout, Digits assistingSSPIPRoutingAddress,
            Digits correlationID, ScfID scfID, CAPExtensions extensions, Carrier carrier,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID, NAOliInfo naOliInfo,
            LocationNumberCap chargeNumber, OriginalCalledNumberCap originalCalledPartyID,
            CallingPartyNumberCap callingPartyNumber) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addEstablishTemporaryConnectionRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Medium);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.establishTemporaryConnection);
        invoke.setOperationCode(oc);

        EstablishTemporaryConnectionRequestImpl req = new EstablishTemporaryConnectionRequestImpl(assistingSSPIPRoutingAddress,
                correlationID, scfID, extensions, carrier, serviceInteractionIndicatorsTwo, callSegmentID, naOliInfo,
                chargeNumber, originalCalledPartyID, callingPartyNumber, this.appCntx.getVersion().getVersion() >= 3);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addDisconnectForwardConnectionRequest() throws CAPException {

        return addDisconnectForwardConnectionRequest(_Timer_Default);
    }

    @Override
    public Long addDisconnectForwardConnectionRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addDisconnectForwardConnectionRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.disconnectForwardConnection);
        invoke.setOperationCode(oc);

        Long invokeId;
        try {
            invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);
        } catch (TCAPException e) {
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addDisconnectForwardConnectionWithArgumentRequest(Integer callSegmentID, CAPExtensions extensions)
            throws CAPException {
        return addDisconnectForwardConnectionWithArgumentRequest(_Timer_Default, callSegmentID, extensions);
    }

    @Override
    public Long addDisconnectForwardConnectionWithArgumentRequest(int customInvokeTimeout, Integer callSegmentID,
            CAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addDisconnectForwardConnectionWithArgumentRequest: must be CapV4_gsmSSF_scfGeneric, " +
                    "CapV4_gsmSSF_scfAssistHandoff or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.dFCWithArgument);
        invoke.setOperationCode(oc);

        DisconnectForwardConnectionWithArgumentRequestImpl req = new DisconnectForwardConnectionWithArgumentRequestImpl(
                callSegmentID, extensions);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addConnectToResourceRequest(CalledPartyNumberCap resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException {

        return addConnectToResourceRequest(_Timer_Default, resourceAddress_IPRoutingAddress, resourceAddress_Null, extensions,
                serviceInteractionIndicatorsTwo, callSegmentID);
    }

    @Override
    public Long addConnectToResourceRequest(int customInvokeTimeout, CalledPartyNumberCap resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addConnectToResourceRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.connectToResource);
        invoke.setOperationCode(oc);

        ConnectToResourceRequestImpl req = new ConnectToResourceRequestImpl(resourceAddress_IPRoutingAddress,
                resourceAddress_Null, extensions, serviceInteractionIndicatorsTwo, callSegmentID);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addResetTimerRequest(TimerID timerID, int timerValue, CAPExtensions extensions, Integer callSegmentID)
            throws CAPException {

        return addResetTimerRequest(_Timer_Default, timerID, timerValue, extensions, callSegmentID);
    }

    @Override
    public Long addResetTimerRequest(int customInvokeTimeout, TimerID timerID, int timerValue, CAPExtensions extensions,
            Integer callSegmentID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addResetTimerRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.resetTimer);
        invoke.setOperationCode(oc);

        ResetTimerRequestImpl req = new ResetTimerRequestImpl(timerID, timerValue, extensions, callSegmentID);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addFurnishChargingInformationRequest(FCIBCCCAMELsequence1 FCIBCCCAMELsequence1) throws CAPException {

        return addFurnishChargingInformationRequest(_Timer_Default, FCIBCCCAMELsequence1);
    }

    @Override
    public Long addFurnishChargingInformationRequest(int customInvokeTimeout, FCIBCCCAMELsequence1 FCIBCCCAMELsequence1)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addFurnishChargingInformationRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.furnishChargingInformation);
        invoke.setOperationCode(oc);

        FurnishChargingInformationRequestImpl req = new FurnishChargingInformationRequestImpl(FCIBCCCAMELsequence1);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addSendChargingInformationRequest(SCIBillingChargingCharacteristics sciBillingChargingCharacteristics,
            SendingSideID partyToCharge, CAPExtensions extensions) throws CAPException {

        return addSendChargingInformationRequest(_Timer_Default, sciBillingChargingCharacteristics, partyToCharge, extensions);
    }

    @Override
    public Long addSendChargingInformationRequest(int customInvokeTimeout,
            SCIBillingChargingCharacteristics sciBillingChargingCharacteristics, SendingSideID partyToCharge,
            CAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric)
            throw new CAPException(
                    "Bad application context name for addSendChargingInformationRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.sendChargingInformation);
        invoke.setOperationCode(oc);

        SendChargingInformationRequestImpl req = new SendChargingInformationRequestImpl(sciBillingChargingCharacteristics,
                partyToCharge, extensions);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addSpecializedResourceReportRequest_CapV23(Long linkedId) throws CAPException {

        return addSpecializedResourceReportRequest_CapV23(linkedId, _Timer_Default);
    }

    @Override
    public Long addSpecializedResourceReportRequest_CapV4(Long linkedId, boolean isAllAnnouncementsComplete,
            boolean isFirstAnnouncementStarted) throws CAPException {

        return addSpecializedResourceReportRequest_CapV4(linkedId, _Timer_Default, isAllAnnouncementsComplete,
                isFirstAnnouncementStarted);
    }

    @Override
    public Long addSpecializedResourceReportRequest_CapV23(Long linkedId, int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addSpecializedResourceReportRequest_CapV23: must be "
                            + "CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV2_gsmSRF_to_gsmSCF or CapV3_gsmSRF_gsmSCF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class4);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.specializedResourceReport);
        invoke.setOperationCode(oc);

        SpecializedResourceReportRequestImpl req = new SpecializedResourceReportRequestImpl(false);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        invoke.setLinkedId(linkedId);

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addSpecializedResourceReportRequest_CapV4(Long linkedId, int customInvokeTimeout,
            boolean isAllAnnouncementsComplete, boolean isFirstAnnouncementStarted) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addSpecializedResourceReportRequest_CapV4: "
                            + "must be CapV4_gsmSSF_scfGeneric, CapV4_gsmSSF_scfAssistHandoff, CapV4_scf_gsmSSFGeneric or CapV4_gsmSRF_gsmSCF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class4);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.specializedResourceReport);
        invoke.setOperationCode(oc);

        SpecializedResourceReportRequestImpl req = new SpecializedResourceReportRequestImpl(isAllAnnouncementsComplete,
                isFirstAnnouncementStarted, true);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        invoke.setLinkedId(linkedId);

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addPlayAnnouncementRequest(InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
            Boolean requestAnnouncementCompleteNotification, CAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws CAPException {

        return addPlayAnnouncementRequest(_Timer_Default, informationToSend, disconnectFromIPForbidden,
                requestAnnouncementCompleteNotification, extensions, callSegmentID, requestAnnouncementStartedNotification);
    }

    @Override
    public Long addPlayAnnouncementRequest(int customInvokeTimeout, InformationToSend informationToSend,
            Boolean disconnectFromIPForbidden, Boolean requestAnnouncementCompleteNotification, CAPExtensions extensions,
            Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addPlayAnnouncementRequest: must be "
                            + "CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV4_scf_gsmSSFGeneric"
                            + ", CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF or CapV4_gsmSRF_gsmSCF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Long);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.playAnnouncement);
        invoke.setOperationCode(oc);

        PlayAnnouncementRequestImpl req = new PlayAnnouncementRequestImpl(informationToSend, disconnectFromIPForbidden,
                requestAnnouncementCompleteNotification, extensions, callSegmentID, requestAnnouncementStartedNotification);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addPromptAndCollectUserInformationRequest(CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSend informationToSend, CAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws CAPException {

        return addPromptAndCollectUserInformationRequest(_Timer_Default, collectedInfo, disconnectFromIPForbidden,
                informationToSend, extensions, callSegmentID, requestAnnouncementStartedNotification);
    }

    @Override
    public Long addPromptAndCollectUserInformationRequest(int customInvokeTimeout, CollectedInfo collectedInfo,
            Boolean disconnectFromIPForbidden, InformationToSend informationToSend, CAPExtensions extensions,
            Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addPromptAndCollectUserInformationRequest: must be "
                            + "CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV4_scf_gsmSSFGeneric"
                            + ", CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF or CapV4_gsmSRF_gsmSCF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class1);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Long);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.promptAndCollectUserInformation);
        invoke.setOperationCode(oc);

        PromptAndCollectUserInformationRequestImpl req = new PromptAndCollectUserInformationRequestImpl(collectedInfo,
                disconnectFromIPForbidden, informationToSend, extensions, callSegmentID, requestAnnouncementStartedNotification);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public void addPromptAndCollectUserInformationResponse_DigitsResponse(long invokeId, Digits digitsResponse)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addPromptAndCollectUserInformationResponse: must be "
                            + "CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV4_scf_gsmSSFGeneric"
                            + ", CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF or CapV4_gsmSRF_gsmSCF");

        ReturnResultLast resultLast = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.promptAndCollectUserInformation);
        resultLast.setOperationCode(oc);

        PromptAndCollectUserInformationResponseImpl req = new PromptAndCollectUserInformationResponseImpl(digitsResponse);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addCancelRequest_InvokeId(Integer invokeID) throws CAPException {

        return addCancelRequest_InvokeId(_Timer_Default, invokeID);
    }

    @Override
    public Long addCancelRequest_AllRequests() throws CAPException {

        return addCancelRequest_AllRequests(_Timer_Default);
    }

    @Override
    public Long addCancelRequest_CallSegmentToCancel(CallSegmentToCancel callSegmentToCancel) throws CAPException {

        return addCancelRequest_CallSegmentToCancel(_Timer_Default, callSegmentToCancel);
    }

    @Override
    public Long addCancelRequest_InvokeId(int customInvokeTimeout, Integer invokeID) throws CAPException {

        CancelRequestImpl req = new CancelRequestImpl(invokeID);
        return addCancelRequest(customInvokeTimeout, req);
    }

    @Override
    public Long addCancelRequest_AllRequests(int customInvokeTimeout) throws CAPException {

        CancelRequestImpl req = new CancelRequestImpl(true);
        return addCancelRequest(customInvokeTimeout, req);
    }

    @Override
    public Long addCancelRequest_CallSegmentToCancel(int customInvokeTimeout, CallSegmentToCancel callSegmentToCancel)
            throws CAPException {

        CancelRequestImpl req = new CancelRequestImpl(callSegmentToCancel);
        return addCancelRequest(customInvokeTimeout, req);
    }

    private Long addCancelRequest(int customInvokeTimeout, CancelRequestImpl req) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addCancelRequest: must be "
                            + "CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV4_scf_gsmSSFGeneric"
                            + ", CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF or CapV4_gsmSRF_gsmSCF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.cancelCode);
        invoke.setOperationCode(oc);

        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public Long addDisconnectLegRequest(LegID logToBeReleased, CauseCap releaseCause, CAPExtensions extensions)
            throws CAPException {
        return addDisconnectLegRequest(_Timer_Default, logToBeReleased, releaseCause, extensions);
    }

    @Override
    public Long addDisconnectLegRequest(int customInvokeTimeout, LegID logToBeReleased, CauseCap releaseCause,
            CAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addDisconnectLegRequest: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class1);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.disconnectLeg);
        invoke.setOperationCode(oc);

        DisconnectLegRequestImpl req = new DisconnectLegRequestImpl(logToBeReleased, releaseCause, extensions);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public void addDisconnectLegResponse(long invokeId) throws CAPException {
        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException("Bad application context name for addDisconnectLegResponse: must be " + "CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        ReturnResultLast resultLast = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.disconnectLeg);
        resultLast.setOperationCode(oc);

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addInitiateCallAttemptRequest(DestinationRoutingAddress destinationRoutingAddress,
            CAPExtensions extensions, LegID legToBeCreated, Integer newCallSegment,
            CallingPartyNumberCap callingPartyNumber, CallReferenceNumber callReferenceNumber,
            ISDNAddressString gsmSCFAddress, boolean suppressTCsi) throws CAPException {
        return addInitiateCallAttemptRequest(_Timer_Default, destinationRoutingAddress, extensions, legToBeCreated,
                newCallSegment, callingPartyNumber, callReferenceNumber, gsmSCFAddress, suppressTCsi);
    }

    @Override
    public Long addInitiateCallAttemptRequest(int customInvokeTimeout,
            DestinationRoutingAddress destinationRoutingAddress, CAPExtensions extensions, LegID legToBeCreated,
            Integer newCallSegment, CallingPartyNumberCap callingPartyNumber, CallReferenceNumber callReferenceNumber,
            ISDNAddressString gsmSCFAddress, boolean suppressTCsi) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addInitiateCallAttemptRequest: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class1);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.initiateCallAttempt);
        invoke.setOperationCode(oc);

        InitiateCallAttemptRequestImpl req = new InitiateCallAttemptRequestImpl(destinationRoutingAddress, extensions,
                legToBeCreated, newCallSegment, callingPartyNumber, callReferenceNumber, gsmSCFAddress, suppressTCsi);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public void addInitiateCallAttemptResponse(long invokeId, SupportedCamelPhases supportedCamelPhases,
            OfferedCamel4Functionalities offeredCamel4Functionalities, CAPExtensions extensions,
            boolean releaseCallArgExtensionAllowed) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addInitiateCallAttemptResponse: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        ReturnResultLast resultLast = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.initiateCallAttempt);
        resultLast.setOperationCode(oc);

        InitiateCallAttemptResponseImpl res = new InitiateCallAttemptResponseImpl(supportedCamelPhases,
                offeredCamel4Functionalities, extensions, releaseCallArgExtensionAllowed);
        AsnOutputStream aos = new AsnOutputStream();
        res.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(res.getTagClass());
        p.setPrimitive(res.getIsPrimitive());
        p.setTag(res.getTag());
        p.setData(aos.toByteArray());
        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addMoveLegRequest(LegID logIDToMove, CAPExtensions extensions) throws CAPException {
        return addMoveLegRequest(_Timer_Default, logIDToMove, extensions);
    }

    @Override
    public Long addMoveLegRequest(int customInvokeTimeout, LegID logIDToMove, CAPExtensions extensions)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addMoveLegRequest: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class1);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.moveLeg);
        invoke.setOperationCode(oc);

        MoveLegRequestImpl req = new MoveLegRequestImpl(logIDToMove, extensions);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public void addMoveLegResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addMoveLegResponse: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        ReturnResultLast resultLast = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.moveLeg);
        resultLast.setOperationCode(oc);

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addCollectInformationRequest() throws CAPException {

        return addCollectInformationRequest(_Timer_Default);
    }

    @Override
    public Long addCollectInformationRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addContinueRequest: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class4);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.collectInformation);
        invoke.setOperationCode(oc);

        Long invokeId;
        try {
            invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);
        } catch (TCAPException e) {
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    public Long addSplitLegRequest(LegID legIDToSplit, Integer newCallSegmentId, CAPExtensions extensions)
            throws CAPException {
        return addSplitLegRequest(_Timer_Default, legIDToSplit, newCallSegmentId, extensions);
    }

    public Long addSplitLegRequest(int customInvokeTimeout, LegID legIDToSplit, Integer newCallSegmentId,
            CAPExtensions extensions) throws CAPException {
        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context for addSplitLegRequest: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class1);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_CircuitSwitchedCallControl_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.splitLeg);
        invoke.setOperationCode(oc);

        SplitLegRequestImpl req = new SplitLegRequestImpl(legIDToSplit, newCallSegmentId, extensions);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
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
            throw new CAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    public void addSplitLegResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context for addSplitLegResponse: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        ReturnResultLast resultLast = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.splitLeg);
        resultLast.setOperationCode(oc);

        this.sendReturnResultLastComponent(resultLast);
    }
}
