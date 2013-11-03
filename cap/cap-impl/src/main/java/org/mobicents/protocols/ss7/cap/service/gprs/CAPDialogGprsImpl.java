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

package org.mobicents.protocols.ss7.cap.service.gprs;

import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.CAPDialogImpl;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPServiceBase;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.primitives.TimerID;
import org.mobicents.protocols.ss7.cap.api.service.gprs.CAPDialogGprs;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOver;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSCause;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilities;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;
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
 *
 */
public class CAPDialogGprsImpl extends CAPDialogImpl implements CAPDialogGprs {

    protected CAPDialogGprsImpl(CAPApplicationContext appCntx, Dialog tcapDialog, CAPProviderImpl capProviderImpl,
            CAPServiceBase capService) {
        super(appCntx, tcapDialog, capProviderImpl, capService);
    }

    @Override
    public Long addInitialDpGprsRequest(int serviceKey, GPRSEventType gprsEventType, ISDNAddressString msisdn, IMSI imsi,
            TimeAndTimezone timeAndTimezone, GPRSMSClass gprsMSClass, EndUserAddress endUserAddress,
            QualityOfService qualityOfService, AccessPointName accessPointName, RAIdentity routeingAreaIdentity,
            GPRSChargingID chargingID, SGSNCapabilities sgsnCapabilities, LocationInformationGPRS locationInformationGPRS,
            PDPInitiationType pdpInitiationType, CAPExtensions extensions, GSNAddress gsnAddress, boolean secondaryPDPContext,
            IMEI imei) throws CAPException {
        return addInitialDpGprsRequest(_Timer_Default, serviceKey, gprsEventType, msisdn, imsi, timeAndTimezone, gprsMSClass,
                endUserAddress, qualityOfService, accessPointName, routeingAreaIdentity, chargingID, sgsnCapabilities,
                locationInformationGPRS, pdpInitiationType, extensions, gsnAddress, secondaryPDPContext, imei);
    }

    @Override
    public Long addInitialDpGprsRequest(int customInvokeTimeout, int serviceKey, GPRSEventType gprsEventType,
            ISDNAddressString msisdn, IMSI imsi, TimeAndTimezone timeAndTimezone, GPRSMSClass gprsMSClass,
            EndUserAddress endUserAddress, QualityOfService qualityOfService, AccessPointName accessPointName,
            RAIdentity routeingAreaIdentity, GPRSChargingID chargingID, SGSNCapabilities sgsnCapabilities,
            LocationInformationGPRS locationInformationGPRS, PDPInitiationType pdpInitiationType, CAPExtensions extensions,
            GSNAddress gsnAddress, boolean secondaryPDPContext, IMEI imei) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF)
            throw new CAPException("Bad application context name for InitialDpGprsRequest: must be CapV3_gprsSSF_gsmSCF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.initialDPGPRS);
        invoke.setOperationCode(oc);

        InitialDpGprsRequestImpl req = new InitialDpGprsRequestImpl(serviceKey, gprsEventType, msisdn, imsi, timeAndTimezone,
                gprsMSClass, endUserAddress, qualityOfService, accessPointName, routeingAreaIdentity, chargingID,
                sgsnCapabilities, locationInformationGPRS, pdpInitiationType, extensions, gsnAddress, secondaryPDPContext, imei);

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
    public Long addRequestReportGPRSEventRequest(ArrayList<GPRSEvent> gprsEvent, PDPID pdpID) throws CAPException {
        return addRequestReportGPRSEventRequest(_Timer_Default, gprsEvent, pdpID);
    }

    @Override
    public Long addRequestReportGPRSEventRequest(int customInvokeTimeout, ArrayList<GPRSEvent> gprsEvent, PDPID pdpID)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for RequestReportGPRSEventRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.requestReportGPRSEvent);
        invoke.setOperationCode(oc);

        RequestReportGPRSEventRequestImpl req = new RequestReportGPRSEventRequestImpl(gprsEvent, pdpID);

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
    public Long addApplyChargingGPRSRequest(ChargingCharacteristics chargingCharacteristics, Integer tariffSwitchInterval,
            PDPID pdpID) throws CAPException {

        return addApplyChargingGPRSRequest(_Timer_Default, chargingCharacteristics, tariffSwitchInterval, pdpID);
    }

    @Override
    public Long addApplyChargingGPRSRequest(int customInvokeTimeout, ChargingCharacteristics chargingCharacteristics,
            Integer tariffSwitchInterval, PDPID pdpID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ApplyChargingGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.applyChargingGPRS);
        invoke.setOperationCode(oc);

        ApplyChargingGPRSRequestImpl req = new ApplyChargingGPRSRequestImpl(chargingCharacteristics, tariffSwitchInterval,
                pdpID);

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
    public Long addEntityReleasedGPRSRequest(GPRSCause gprsCause, PDPID pdpID) throws CAPException {

        return addEntityReleasedGPRSRequest(_Timer_Default, gprsCause, pdpID);
    }

    @Override
    public Long addEntityReleasedGPRSRequest(int customInvokeTimeout, GPRSCause gprsCause, PDPID pdpID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for EntityReleasedGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class1);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.entityReleasedGPRS);
        invoke.setOperationCode(oc);

        EntityReleasedGPRSRequestImpl req = new EntityReleasedGPRSRequestImpl(gprsCause, pdpID);

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
    public void addEntityReleasedGPRSResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for EntityReleasedGPRSResponse: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        ReturnResultLast resultLast = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // we need not Operation Code because no answer

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addConnectGPRSRequest(AccessPointName accessPointName, PDPID pdpID) throws CAPException {

        return addConnectGPRSRequest(_Timer_Default, accessPointName, pdpID);
    }

    @Override
    public Long addConnectGPRSRequest(int customInvokeTimeout, AccessPointName accessPointName, PDPID pdpID)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ConnectGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.connectGPRS);
        invoke.setOperationCode(oc);

        ConnectGPRSRequestImpl req = new ConnectGPRSRequestImpl(accessPointName, pdpID);

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
    public Long addContinueGPRSRequest(PDPID pdpID) throws CAPException {

        return addContinueGPRSRequest(_Timer_Default, pdpID);
    }

    @Override
    public Long addContinueGPRSRequest(int customInvokeTimeout, PDPID pdpID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ContinueGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.continueGPRS);
        invoke.setOperationCode(oc);

        ContinueGPRSRequestImpl req = new ContinueGPRSRequestImpl(pdpID);

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
    public Long addReleaseGPRSRequest(GPRSCause gprsCause, PDPID pdpID) throws CAPException {

        return addReleaseGPRSRequest(_Timer_Default, gprsCause, pdpID);
    }

    @Override
    public Long addReleaseGPRSRequest(int customInvokeTimeout, GPRSCause gprsCause, PDPID pdpID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ReleaseGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.releaseGPRS);
        invoke.setOperationCode(oc);

        ReleaseGPRSRequestImpl req = new ReleaseGPRSRequestImpl(gprsCause, pdpID);

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
    public Long addResetTimerGPRSRequest(TimerID timerID, int timerValue) throws CAPException {

        return addResetTimerGPRSRequest(_Timer_Default, timerID, timerValue);
    }

    @Override
    public Long addResetTimerGPRSRequest(int customInvokeTimeout, TimerID timerID, int timerValue) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ResetTimerGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.resetTimerGPRS);
        invoke.setOperationCode(oc);

        ResetTimerGPRSRequestImpl req = new ResetTimerGPRSRequestImpl(timerID, timerValue);

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
    public Long addFurnishChargingInformationGPRSRequest(
            CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics) throws CAPException {

        return addFurnishChargingInformationGPRSRequest(_Timer_Default, fciGPRSBillingChargingCharacteristics);
    }

    @Override
    public Long addFurnishChargingInformationGPRSRequest(int customInvokeTimeout,
            CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for FurnishChargingInformationGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.furnishChargingInformationGPRS);
        invoke.setOperationCode(oc);

        FurnishChargingInformationGPRSRequestImpl req = new FurnishChargingInformationGPRSRequestImpl(
                fciGPRSBillingChargingCharacteristics);

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
    public Long addCancelGPRSRequest(PDPID pdpID) throws CAPException {

        return addCancelGPRSRequest(_Timer_Default, pdpID);
    }

    @Override
    public Long addCancelGPRSRequest(int customInvokeTimeout, PDPID pdpID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for CancelGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.cancelGPRS);
        invoke.setOperationCode(oc);

        CancelGPRSRequestImpl req = new CancelGPRSRequestImpl(pdpID);

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
    public Long addSendChargingInformationGPRSRequest(
            CAMELSCIGPRSBillingChargingCharacteristics sciGPRSBillingChargingCharacteristics) throws CAPException {

        return addSendChargingInformationGPRSRequest(_Timer_Default, sciGPRSBillingChargingCharacteristics);
    }

    @Override
    public Long addSendChargingInformationGPRSRequest(int customInvokeTimeout,
            CAMELSCIGPRSBillingChargingCharacteristics sciGPRSBillingChargingCharacteristics) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for SendChargingInformationGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.sendChargingInformationGPRS);
        invoke.setOperationCode(oc);

        SendChargingInformationGPRSRequestImpl req = new SendChargingInformationGPRSRequestImpl(
                sciGPRSBillingChargingCharacteristics);

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
    public Long addApplyChargingReportGPRSRequest(ChargingResult chargingResult, QualityOfService qualityOfService,
            boolean active, PDPID pdpID, ChargingRollOver chargingRollOver) throws CAPException {

        return addApplyChargingReportGPRSRequest(_Timer_Default, chargingResult, qualityOfService, active, pdpID,
                chargingRollOver);
    }

    @Override
    public Long addApplyChargingReportGPRSRequest(int customInvokeTimeout, ChargingResult chargingResult,
            QualityOfService qualityOfService, boolean active, PDPID pdpID, ChargingRollOver chargingRollOver)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ApplyChargingReportGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class1);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.applyChargingReportGPRS);
        invoke.setOperationCode(oc);

        ApplyChargingReportGPRSRequestImpl req = new ApplyChargingReportGPRSRequestImpl(chargingResult, qualityOfService,
                active, pdpID, chargingRollOver);

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
    public void addApplyChargingReportGPRSResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ApplyChargingReportGPRSResponse: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        ReturnResultLast resultLast = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // we need not Operation Code because no answer

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addEventReportGPRSRequest(GPRSEventType gprsEventType, MiscCallInfo miscGPRSInfo,
            GPRSEventSpecificInformation gprsEventSpecificInformation, PDPID pdpID) throws CAPException {

        return addEventReportGPRSRequest(_Timer_Default, gprsEventType, miscGPRSInfo, gprsEventSpecificInformation, pdpID);
    }

    @Override
    public Long addEventReportGPRSRequest(int customInvokeTimeout, GPRSEventType gprsEventType, MiscCallInfo miscGPRSInfo,
            GPRSEventSpecificInformation gprsEventSpecificInformation, PDPID pdpID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for EventReportGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class1);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.eventReportGPRS);
        invoke.setOperationCode(oc);

        EventReportGPRSRequestImpl req = new EventReportGPRSRequestImpl(gprsEventType, miscGPRSInfo,
                gprsEventSpecificInformation, pdpID);

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
    public void addEventReportGPRSResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for RequestReportGPRSEventRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        ReturnResultLast resultLast = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // we need not Operation Code because no answer

        this.sendReturnResultLastComponent(resultLast);
    }

    @Override
    public Long addActivityTestGPRSRequest() throws CAPException {
        return addActivityTestGPRSRequest(_Timer_Default);
    }

    @Override
    public Long addActivityTestGPRSRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ActivityTestGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class3);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Gprs_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.activityTestGPRS);
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
    public void addActivityTestGPRSResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ActivityTestGPRSResponse: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        ReturnResultLast resultLast = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // we need not Operation Code because no answer

        this.sendReturnResultLastComponent(resultLast);
    }

}
