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
import org.mobicents.protocols.ss7.cap.api.service.gprs.CAPDialogGprs;
import org.mobicents.protocols.ss7.cap.api.service.gprs.CAPServiceGprs;
import org.mobicents.protocols.ss7.cap.api.service.gprs.CAPServiceGprsListener;
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
public class CAPServiceGprsImpl extends CAPServiceBaseImpl implements CAPServiceGprs {

    protected Logger loger = Logger.getLogger(CAPServiceGprsImpl.class);

    public CAPServiceGprsImpl(CAPProviderImpl capProviderImpl) {
        super(capProviderImpl);
    }

    @Override
    public CAPDialogGprs createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress) throws CAPException {
        return this.createNewDialog(appCntx, origAddress, destAddress, null);
    }

    @Override
    public CAPDialogGprs createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress, Long localTrId) throws CAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new CAPException("Cannot create CAPDialogGprs because CAPServiceGprsl is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        CAPDialogGprsImpl dialog = new CAPDialogGprsImpl(appCntx, tcapDialog, this.capProviderImpl, this);

        this.putCAPDialogIntoCollection(dialog);

        return dialog;
    }

    @Override
    public void addCAPServiceListener(CAPServiceGprsListener capServiceListener) {
        super.addCAPServiceListener(capServiceListener);
    }

    @Override
    public void removeCAPServiceListener(CAPServiceGprsListener capServiceListener) {
        super.removeCAPServiceListener(capServiceListener);
    }

    @Override
    protected CAPDialogImpl createNewDialogIncoming(CAPApplicationContext appCntx, Dialog tcapDialog) {
        return new CAPDialogGprsImpl(appCntx, tcapDialog, this.capProviderImpl, this);
    }

    @Override
    public ServingCheckData isServingService(CAPApplicationContext dialogApplicationContext) {
        switch (dialogApplicationContext) {
            case CapV3_gprsSSF_gsmSCF:
            case CapV3_gsmSCF_gprsSSF:
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
        }

        return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
    }

    @Override
    public void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, CAPDialog capDialog,
            Long invokeId, Long linkedId, Invoke linkedInvoke) throws CAPParsingComponentException {

        CAPDialogGprsImpl capDialogGprsImpl = (CAPDialogGprsImpl) capDialog;

        Long ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new CAPParsingComponentException("", CAPParsingComponentExceptionReason.UnrecognizedOperation);
        CAPApplicationContext acn = capDialog.getApplicationContext();
        int ocValueInt = (int) (long) ocValue;

        switch (ocValueInt) {
            case CAPOperationCode.initialDPGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF) {
                    if (compType == ComponentType.Invoke) {
                        this.initialDpGprsRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.requestReportGPRSEvent:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.requestReportGPRSEventRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.applyChargingGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.applyChargingGPRSRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.entityReleasedGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.entityReleasedGPRSRequest(parameter, capDialogGprsImpl, invokeId);
                    }

                    if (compType == ComponentType.ReturnResultLast) {
                        this.entityReleasedGPRSResponse(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.connectGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.connectGPRSRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.continueGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.continueGPRSRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.releaseGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.releaseGPRSRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.resetTimerGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.resetTimerGPRSRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.furnishChargingInformationGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.furnishChargingInformationGPRSRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.cancelGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.cancelGPRSRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.sendChargingInformationGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.sendChargingInformationGPRSRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.applyChargingReportGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.applyChargingReportGPRSRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                    if (compType == ComponentType.ReturnResultLast) {
                        this.applyChargingReportGPRSResponse(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.eventReportGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.eventReportGPRSRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                    if (compType == ComponentType.ReturnResultLast) {
                        this.eventReportGPRSResponse(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            case CAPOperationCode.activityTestGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                    if (compType == ComponentType.Invoke) {
                        this.activityTestRequest(parameter, capDialogGprsImpl, invokeId);
                    }
                    if (compType == ComponentType.ReturnResultLast) {
                        this.activityTestResponse(parameter, capDialogGprsImpl, invokeId);
                    }
                }
                break;
            default:
                throw new CAPParsingComponentException("", CAPParsingComponentExceptionReason.UnrecognizedOperation);
        }
    }

    private void initialDpGprsRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding initialDpGprsRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding initialDpGprsRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        InitialDpGprsRequestImpl ind = new InitialDpGprsRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onInitialDpGprsRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing initialDpRequest: " + e.getMessage(), e);
            }
        }
    }

    private void requestReportGPRSEventRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding requestReportGPRSEventRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding requestReportGPRSEventRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        RequestReportGPRSEventRequestImpl ind = new RequestReportGPRSEventRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onRequestReportGPRSEventRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing requestReportGPRSEventRequest: " + e.getMessage(), e);
            }
        }
    }

    private void applyChargingGPRSRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding applyChargingGPRSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding applyChargingGPRSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ApplyChargingGPRSRequestImpl ind = new ApplyChargingGPRSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onApplyChargingGPRSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing applyChargingGPRSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void entityReleasedGPRSRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding entityReleasedGPRSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding entityReleasedGPRSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        EntityReleasedGPRSRequestImpl ind = new EntityReleasedGPRSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onEntityReleasedGPRSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing entityReleasedGPRSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void entityReleasedGPRSResponse(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        EntityReleasedGPRSResponseImpl ind = new EntityReleasedGPRSResponseImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onEntityReleasedGPRSResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing entityReleasedGPRSResponse: " + e.getMessage(), e);
            }
        }
    }

    private void connectGPRSRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding connectGPRSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding connectGPRSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ConnectGPRSRequestImpl ind = new ConnectGPRSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onConnectGPRSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing connectGPRSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void continueGPRSRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding continueGPRSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding continueGPRSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ContinueGPRSRequestImpl ind = new ContinueGPRSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onContinueGPRSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing continueGPRSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void releaseGPRSRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding releaseGPRSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding releaseGPRSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ReleaseGPRSRequestImpl ind = new ReleaseGPRSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onReleaseGPRSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing releaseGPRSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void resetTimerGPRSRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding resetTimerGPRSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding resetTimerGPRSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ResetTimerGPRSRequestImpl ind = new ResetTimerGPRSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onResetTimerGPRSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing resetTimerGPRSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void furnishChargingInformationGPRSRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding furnishChargingInformationGPRSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                || !parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding furnishChargingInformationGPRSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        FurnishChargingInformationGPRSRequestImpl ind = new FurnishChargingInformationGPRSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onFurnishChargingInformationGPRSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing furnishChargingInformationGPRSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void cancelGPRSRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding cancelGPRSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding cancelGPRSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        CancelGPRSRequestImpl ind = new CancelGPRSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onCancelGPRSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing cancelGPRSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void sendChargingInformationGPRSRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding sendChargingInformationGPRSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding sendChargingInformationGPRSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        SendChargingInformationGPRSRequestImpl ind = new SendChargingInformationGPRSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onSendChargingInformationGPRSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing sendChargingInformationGPRSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void applyChargingReportGPRSRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding applyChargingReportGPRSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding applyChargingReportGPRSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ApplyChargingReportGPRSRequestImpl ind = new ApplyChargingReportGPRSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onApplyChargingReportGPRSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing applyChargingReportGPRSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void applyChargingReportGPRSResponse(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        ApplyChargingReportGPRSResponseImpl ind = new ApplyChargingReportGPRSResponseImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onApplyChargingReportGPRSResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing applyChargingReportGPRSResponse: " + e.getMessage(), e);
            }
        }
    }

    private void eventReportGPRSRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        if (parameter == null)
            throw new CAPParsingComponentException(
                    "Error while decoding eventReportGPRSRequest: Parameter is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new CAPParsingComponentException(
                    "Error while decoding eventReportGPRSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), CAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        EventReportGPRSRequestImpl ind = new EventReportGPRSRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onEventReportGPRSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing eventReportGPRSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void eventReportGPRSResponse(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        EventReportGPRSResponseImpl ind = new EventReportGPRSResponseImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onEventReportGPRSResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing eventReportGPRSResponse: " + e.getMessage(), e);
            }
        }
    }

    private void activityTestRequest(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        ActivityTestGPRSRequestImpl ind = new ActivityTestGPRSRequestImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onActivityTestGPRSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing activityTestGPRSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void activityTestResponse(Parameter parameter, CAPDialogGprsImpl capDialogImpl, Long invokeId)
            throws CAPParsingComponentException {

        ActivityTestGPRSResponseImpl ind = new ActivityTestGPRSResponseImpl();

        ind.setInvokeId(invokeId);
        ind.setCAPDialog(capDialogImpl);

        for (CAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onCAPMessage(ind);
                ((CAPServiceGprsListener) serLis).onActivityTestGPRSResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing activityTestGPRSResponse: " + e.getMessage(), e);
            }
        }
    }

}
