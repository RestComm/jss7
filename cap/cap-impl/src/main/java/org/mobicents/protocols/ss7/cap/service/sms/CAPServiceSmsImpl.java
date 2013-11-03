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

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.CAPDialogImpl;
import org.mobicents.protocols.ss7.cap.CAPProviderImpl;
import org.mobicents.protocols.ss7.cap.CAPServiceBaseImpl;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.CAPServiceListener;
import org.mobicents.protocols.ss7.cap.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.cap.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.cap.api.service.sms.CAPDialogSms;
import org.mobicents.protocols.ss7.cap.api.service.sms.CAPServiceSms;
import org.mobicents.protocols.ss7.cap.api.service.sms.CAPServiceSmsListener;
import org.mobicents.protocols.ss7.cap.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPServiceSmsImpl extends CAPServiceBaseImpl implements CAPServiceSms {

    protected Logger loger = Logger.getLogger(CAPServiceSmsImpl.class);

    public CAPServiceSmsImpl(CAPProviderImpl capProviderImpl) {
        super(capProviderImpl);
    }

    @Override
    public CAPDialogSms createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress)
            throws CAPException {
        return this.createNewDialog(appCntx, origAddress, destAddress, null);
    }

    @Override
    public CAPDialogSms createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress,
            SccpAddress destAddress, Long localTrId) throws CAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new CAPException("Cannot create CAPDialogSms because CAPServiceSms is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        CAPDialogSmsImpl dialog = new CAPDialogSmsImpl(appCntx, tcapDialog, this.capProviderImpl, this);

        this.putCAPDialogIntoCollection(dialog);

        return dialog;
    }

    @Override
    public void addCAPServiceListener(CAPServiceSmsListener capServiceListener) {
        super.addCAPServiceListener(capServiceListener);
    }

    @Override
    public void removeCAPServiceListener(CAPServiceSmsListener capServiceListener) {
        super.removeCAPServiceListener(capServiceListener);
    }

    @Override
    protected CAPDialogImpl createNewDialogIncoming(CAPApplicationContext appCntx, Dialog tcapDialog) {
        return new CAPDialogSmsImpl(appCntx, tcapDialog, this.capProviderImpl, this);
    }

    @Override
    public ServingCheckData isServingService(CAPApplicationContext dialogApplicationContext) {
        switch (dialogApplicationContext) {
        case CapV3_cap3_sms:
        case CapV4_cap4_sms:
            return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
        }

        return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
    }

    @Override
    public void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, CAPDialog capDialog,
            Long invokeId, Long linkedId, Invoke linkedInvoke) throws CAPParsingComponentException {

        CAPDialogSmsImpl capDialogSmsImpl = (CAPDialogSmsImpl) capDialog;

        Long ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new CAPParsingComponentException("", CAPParsingComponentExceptionReason.UnrecognizedOperation);
        CAPApplicationContext acn = capDialog.getApplicationContext();
        int ocValueInt = (int) (long) ocValue;

        switch (ocValueInt) {
        case CAPOperationCode.connectSMS:
            if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
                if (compType == ComponentType.Invoke) {
                    this.connectSMSRequest(parameter, capDialogSmsImpl, invokeId);
                }
            }
            break;
        case CAPOperationCode.eventReportSMS:
            if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
                if (compType == ComponentType.Invoke) {
                    this.eventReportSMSRequest(parameter, capDialogSmsImpl, invokeId);
                }
            }
            break;
        case CAPOperationCode.furnishChargingInformationSMS:
            if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
                if (compType == ComponentType.Invoke) {
                    this.furnishChargingInformationSMSRequest(parameter, capDialogSmsImpl, invokeId);
                }
            }
            break;
        case CAPOperationCode.initialDPSMS:
            if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
                if (compType == ComponentType.Invoke) {
                    this.initialDPSMSRequest(parameter, capDialogSmsImpl, invokeId);
                }
            }
            break;
        case CAPOperationCode.releaseSMS:
            if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
                if (compType == ComponentType.Invoke) {
                    this.releaseSMSRequest(parameter, capDialogSmsImpl, invokeId);
                }
            }
            break;
        case CAPOperationCode.requestReportSMSEvent:
            if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
                if (compType == ComponentType.Invoke) {
                    this.requestReportSMSEventRequest(parameter, capDialogSmsImpl, invokeId);
                }
            }
            break;
        case CAPOperationCode.resetTimerSMS:
            if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
                if (compType == ComponentType.Invoke) {
                    this.resetTimerSMSRequest(parameter, capDialogSmsImpl, invokeId);
                }
            }
            break;
        case CAPOperationCode.continueSMS:
            if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
                if (compType == ComponentType.Invoke) {
                    this.continueSMSRequest(parameter, capDialogSmsImpl, invokeId);
                }
            }
            break;
        default:
            throw new CAPParsingComponentException("", CAPParsingComponentExceptionReason.UnrecognizedOperation);
        }
    }

    private void connectSMSRequest(Parameter parameter, CAPDialogSmsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding connectSMSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding connectSMSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ConnectSMSRequestImpl ind = new ConnectSMSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceSmsListener) serLis).onConnectSMSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void eventReportSMSRequest(Parameter parameter, CAPDialogSmsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding EventReportSMSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding EventReportSMSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        EventReportSMSRequestImpl ind = new EventReportSMSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceSmsListener) serLis).onEventReportSMSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing EventReportSMSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void furnishChargingInformationSMSRequest(Parameter parameter, CAPDialogSmsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding FurnishChargingInformationSMSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                || !parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding FurnishChargingInformationSMSRequest: Bad tag or tagClass or parameter is not a primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        FurnishChargingInformationSMSRequestImpl ind = new FurnishChargingInformationSMSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceSmsListener) serLis).onFurnishChargingInformationSMSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing FurnishChargingInformationSMSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void initialDPSMSRequest(Parameter parameter, CAPDialogSmsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding InitialDPSMSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding InitialDPSMSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        InitialDPSMSRequestImpl ind = new InitialDPSMSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceSmsListener) serLis).onInitialDPSMSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing InitialDPSMSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void releaseSMSRequest(Parameter parameter, CAPDialogSmsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding ReleaseSMSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                || !parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding ReleaseSMSRequest: Bad tag or tagClass or parameter is not primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ReleaseSMSRequestImpl ind = new ReleaseSMSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceSmsListener) serLis).onReleaseSMSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing ReleaseSMSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void requestReportSMSEventRequest(Parameter parameter, CAPDialogSmsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding RequestReportSMSEventRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding RequestReportSMSEventRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        RequestReportSMSEventRequestImpl ind = new RequestReportSMSEventRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceSmsListener) serLis).onRequestReportSMSEventRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing RequestReportSMSEventRequest: " + e.getMessage(), e);
            }
        }
    }

    private void resetTimerSMSRequest(Parameter parameter, CAPDialogSmsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding ResetTimerSMSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding ResetTimerSMSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ResetTimerSMSRequestImpl ind = new ResetTimerSMSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceSmsListener) serLis).onResetTimerSMSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing ResetTimerSMSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void continueSMSRequest(Parameter parameter, CAPDialogSmsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        ContinueSMSRequestImpl ind = new ContinueSMSRequestImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceSmsListener) serLis).onContinueSMSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing continueSMSRequest: " + e.getMessage(), e);
            }
        }
    }

}
