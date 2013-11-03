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

package org.mobicents.protocols.ss7.cap.service.sms;

import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.CAPDialogImpl;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPServiceBase;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.primitives.TimerID;
import org.mobicents.protocols.ss7.cap.api.service.sms.CAPDialogSms;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELsequence1SMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.RPCause;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSEvent;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingScheme;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifier;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriod;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPDialogSmsImpl extends CAPDialogImpl implements CAPDialogSms {

    protected CAPDialogSmsImpl(CAPApplicationContext appCntx, Dialog tcapDialog, CAPProviderImpl capProviderImpl,
            CAPServiceBase capService) {
        super(appCntx, tcapDialog, capProviderImpl, capService);
    }

    @Override
    public Long addConnectSMSRequest(SMSAddressString callingPartysNumber,
            CalledPartyBCDNumber destinationSubscriberNumber, ISDNAddressString smscAddress, CAPExtensions extensions)
            throws CAPException {
        return addConnectSMSRequest(_Timer_Default, callingPartysNumber, destinationSubscriberNumber, smscAddress,
                extensions);

    }

    @Override
    public Long addConnectSMSRequest(int customInvokeTimeout, SMSAddressString callingPartysNumber,
            CalledPartyBCDNumber destinationSubscriberNumber, ISDNAddressString smscAddress, CAPExtensions extensions)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Sms_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.connectSMS);
        invoke.setOperationCode(oc);

        ConnectSMSRequestImpl req = new ConnectSMSRequestImpl(callingPartysNumber, destinationSubscriberNumber,
                smscAddress, extensions);

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
    public Long addEventReportSMSRequest(EventTypeSMS eventTypeSMS,
            EventSpecificInformationSMS eventSpecificInformationSMS, MiscCallInfo miscCallInfo, CAPExtensions extensions)
            throws CAPException {
        return this.addEventReportSMSRequest(_Timer_Default, eventTypeSMS, eventSpecificInformationSMS, miscCallInfo,
                extensions);
    }

    @Override
    public Long addEventReportSMSRequest(int customInvokeTimeout, EventTypeSMS eventTypeSMS,
            EventSpecificInformationSMS eventSpecificInformationSMS, MiscCallInfo miscCallInfo, CAPExtensions extensions)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class4);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Sms_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.eventReportSMS);
        invoke.setOperationCode(oc);

        EventReportSMSRequestImpl req = new EventReportSMSRequestImpl(eventTypeSMS, eventSpecificInformationSMS,
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
    public Long addFurnishChargingInformationSMSRequest(FCIBCCCAMELsequence1SMS fciBCCCAMELsequence1) throws CAPException {
        return this.addFurnishChargingInformationSMSRequest(_Timer_Default, fciBCCCAMELsequence1);
    }

    @Override
    public Long addFurnishChargingInformationSMSRequest(int customInvokeTimeout, FCIBCCCAMELsequence1SMS fciBCCCAMELsequence1) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Sms_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.furnishChargingInformationSMS);
        invoke.setOperationCode(oc);

        FurnishChargingInformationSMSRequestImpl req = new FurnishChargingInformationSMSRequestImpl(fciBCCCAMELsequence1);
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
    public Long addInitialDPSMSRequest(int serviceKey, CalledPartyBCDNumber destinationSubscriberNumber,
            SMSAddressString callingPartyNumber, EventTypeSMS eventTypeSMS, IMSI imsi,
            LocationInformation locationInformationMSC, LocationInformationGPRS locationInformationGPRS,
            ISDNAddressString smscCAddress, TimeAndTimezone timeAndTimezone,
            TPShortMessageSpecificInfo tPShortMessageSpecificInfo, TPProtocolIdentifier tPProtocolIdentifier,
            TPDataCodingScheme tPDataCodingScheme, TPValidityPeriod tPValidityPeriod, CAPExtensions extensions,
            CallReferenceNumber smsReferenceNumber, ISDNAddressString mscAddress, ISDNAddressString sgsnNumber,
            MSClassmark2 mSClassmark2, GPRSMSClass gprsMSClass, IMEI imei, ISDNAddressString calledPartyNumber)
            throws CAPException {
        return this.addInitialDPSMSRequest(_Timer_Default, serviceKey, destinationSubscriberNumber, callingPartyNumber,
                eventTypeSMS, imsi, locationInformationMSC, locationInformationGPRS, smscCAddress, timeAndTimezone,
                tPShortMessageSpecificInfo, tPProtocolIdentifier, tPDataCodingScheme, tPValidityPeriod, extensions,
                smsReferenceNumber, mscAddress, sgsnNumber, mSClassmark2, gprsMSClass, imei, calledPartyNumber);
    }

    @Override
    public Long addInitialDPSMSRequest(int customInvokeTimeout, int serviceKey,
            CalledPartyBCDNumber destinationSubscriberNumber, SMSAddressString callingPartyNumber,
            EventTypeSMS eventTypeSMS, IMSI imsi, LocationInformation locationInformationMSC,
            LocationInformationGPRS locationInformationGPRS, ISDNAddressString smscCAddress,
            TimeAndTimezone timeAndTimezone, TPShortMessageSpecificInfo tPShortMessageSpecificInfo,
            TPProtocolIdentifier tPProtocolIdentifier, TPDataCodingScheme tPDataCodingScheme,
            TPValidityPeriod tPValidityPeriod, CAPExtensions extensions, CallReferenceNumber smsReferenceNumber,
            ISDNAddressString mscAddress, ISDNAddressString sgsnNumber, MSClassmark2 mSClassmark2,
            GPRSMSClass gprsMSClass, IMEI imei, ISDNAddressString calledPartyNumber) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Sms_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.initialDPSMS);
        invoke.setOperationCode(oc);

        InitialDPSMSRequestImpl req = new InitialDPSMSRequestImpl(serviceKey, destinationSubscriberNumber,
                callingPartyNumber, eventTypeSMS, imsi, locationInformationMSC, locationInformationGPRS, smscCAddress,
                timeAndTimezone, tPShortMessageSpecificInfo, tPProtocolIdentifier, tPDataCodingScheme,
                tPValidityPeriod, extensions, smsReferenceNumber, mscAddress, sgsnNumber, mSClassmark2, gprsMSClass,
                imei, calledPartyNumber);

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
    public Long addReleaseSMSRequest(RPCause rpCause) throws CAPException {
        return this.addReleaseSMSRequest(_Timer_Default, rpCause);
    }

    @Override
    public Long addReleaseSMSRequest(int customInvokeTimeout, RPCause rpCause) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class4);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Sms_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.releaseSMS);
        invoke.setOperationCode(oc);

        ReleaseSMSRequestImpl req = new ReleaseSMSRequestImpl(rpCause);

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
    public Long addRequestReportSMSEventRequest(ArrayList<SMSEvent> smsEvents, CAPExtensions extensions)
            throws CAPException {
        return this.addRequestReportSMSEventRequest(_Timer_Default, smsEvents, extensions);
    }

    @Override
    public Long addRequestReportSMSEventRequest(int customInvokeTimeout, ArrayList<SMSEvent> smsEvents,
            CAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Sms_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.requestReportSMSEvent);
        invoke.setOperationCode(oc);

        RequestReportSMSEventRequestImpl req = new RequestReportSMSEventRequestImpl(smsEvents, extensions);

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
    public Long addResetTimerSMSRequest(TimerID timerID, int timerValue, CAPExtensions extensions) throws CAPException {
        return this.addResetTimerSMSRequest(_Timer_Default, timerID, timerValue, extensions);
    }

    @Override
    public Long addResetTimerSMSRequest(int customInvokeTimeout, TimerID timerID, int timerValue,
            CAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCInvokeRequest(InvokeClass.Class2);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Sms_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.resetTimerSMS);
        invoke.setOperationCode(oc);

        ResetTimerSMSRequestImpl req = new ResetTimerSMSRequestImpl(timerID, timerValue, extensions);

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
    public Long addContinueSMSRequest() throws CAPException {

        return addContinueSMSRequest(_Timer_Default);
    }

    @Override
    public Long addContinueSMSRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Invoke invoke = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest(InvokeClass.Class4);
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_Sms_Short);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) CAPOperationCode.continueSMS);
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

}
