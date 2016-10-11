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

package org.mobicents.protocols.ss7.map.service.sms;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.MAPServiceBaseImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPServiceSmsImpl extends MAPServiceBaseImpl implements MAPServiceSms {

    protected Logger loger = Logger.getLogger(MAPServiceSmsImpl.class);

    public MAPServiceSmsImpl(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    /*
     * Creating a new outgoing MAP SMS dialog and adding it to the MAPProvider.dialog collection
     */
    public MAPDialogSms createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
            AddressString destReference) throws MAPException {
        return this.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference, null);
    }

    public MAPDialogSms createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
            AddressString destReference, Long localTrId) throws MAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new MAPException("Cannot create MAPDialogSms because MAPServiceSms is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        MAPDialogSmsImpl dialog = new MAPDialogSmsImpl(appCntx, tcapDialog, this.mapProviderImpl, this, origReference,
                destReference);

        this.putMAPDialogIntoCollection(dialog);

        return dialog;
    }

    @Override
    protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
        return new MAPDialogSmsImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
    }

    public void addMAPServiceListener(MAPServiceSmsListener mapServiceListener) {
        super.addMAPServiceListener(mapServiceListener);
    }

    public void removeMAPServiceListener(MAPServiceSmsListener mapServiceListener) {
        super.removeMAPServiceListener(mapServiceListener);
    }

    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
        int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

        switch (ctx) {
            case shortMsgAlertContext:
                if (vers >= 1 && vers <= 2) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else if (vers > 2) {
                    long[] altOid = dialogApplicationContext.getOID();
                    altOid[7] = 2;
                    ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }

            case shortMsgMORelayContext:
            case shortMsgGatewayContext:
                if (vers >= 1 && vers <= 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else if (vers > 3) {
                    long[] altOid = dialogApplicationContext.getOID();
                    altOid[7] = 3;
                    ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }

            case shortMsgMTRelayContext:
                if (vers >= 2 && vers <= 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else if (vers > 3) {
                    long[] altOid = dialogApplicationContext.getOID();
                    altOid[7] = 3;
                    ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }

            case mwdMngtContext:
                if (vers >= 1 && vers <= 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else if (vers > 3) {
                    long[] altOid = dialogApplicationContext.getOID();
                    altOid[7] = 3;
                    ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }
        }

        return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
    }

    @Override
    public MAPApplicationContext getMAPv1ApplicationContext(int operationCode, Invoke invoke) {

        switch (operationCode) {
            case MAPOperationCode.mo_forwardSM:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
                        MAPApplicationContextVersion.version1);
            case MAPOperationCode.alertServiceCentreWithoutResult:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                        MAPApplicationContextVersion.version1);
            case MAPOperationCode.sendRoutingInfoForSM:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                        MAPApplicationContextVersion.version1);
            case MAPOperationCode.reportSM_DeliveryStatus:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                        MAPApplicationContextVersion.version1);
            case MAPOperationCode.noteSubscriberPresent:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.mwdMngtContext,
                        MAPApplicationContextVersion.version1);
        }

        return null;
    }

    @Override
    public void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, MAPDialog mapDialog,
            Long invokeId, Long linkedId, Invoke linkedInvoke) throws MAPParsingComponentException {

        // if an application-context-name different from version 1 is
        // received in a syntactically correct TC-
        // BEGIN indication primitive but is not acceptable from a load
        // control point of view, the MAP PM
        // shall ignore this dialogue request. The MAP-user is not informed.
//        if (compType == ComponentType.Invoke && this.mapProviderImpl.isCongested()) {
//            // we agree all sms services when congestion
//        }

        MAPDialogSmsImpl mapDialogSmsImpl = (MAPDialogSmsImpl) mapDialog;

        Long ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
        MAPApplicationContextName acn = mapDialog.getApplicationContext().getApplicationContextName();
        int vers = mapDialog.getApplicationContext().getApplicationContextVersion().getVersion();
        int ocValueInt = (int) (long) ocValue;

        switch (ocValueInt) {
            case MAPOperationCode.mo_forwardSM:
                if (acn == MAPApplicationContextName.shortMsgMORelayContext
                        || acn == MAPApplicationContextName.shortMsgMTRelayContext && vers == 2) {
                    if (vers >= 3) {
                        if (compType == ComponentType.Invoke)
                            this.moForwardShortMessageRequest(parameter, mapDialogSmsImpl, invokeId);
                        else
                            this.moForwardShortMessageResponse(parameter, mapDialogSmsImpl, invokeId,
                                    compType == ComponentType.ReturnResult);
                    } else {
                        if (compType == ComponentType.Invoke)
                            this.forwardShortMessageRequest(parameter, mapDialogSmsImpl, invokeId);
                        else
                            this.forwardShortMessageResponse(parameter, mapDialogSmsImpl, invokeId,
                                    compType == ComponentType.ReturnResult);
                    }
                }
                break;

            case MAPOperationCode.mt_forwardSM:
                if (acn == MAPApplicationContextName.shortMsgMTRelayContext && vers >= 3) {
                    if (compType == ComponentType.Invoke)
                        this.mtForwardShortMessageRequest(parameter, mapDialogSmsImpl, invokeId);
                    else
                        this.mtForwardShortMessageResponse(parameter, mapDialogSmsImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;

            case MAPOperationCode.sendRoutingInfoForSM:
                if (acn == MAPApplicationContextName.shortMsgGatewayContext) {
                    if (compType == ComponentType.Invoke)
                        this.sendRoutingInfoForSMRequest(parameter, mapDialogSmsImpl, invokeId);
                    else
                        this.sendRoutingInfoForSMResponse(parameter, mapDialogSmsImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;

            case MAPOperationCode.reportSM_DeliveryStatus:
                if (acn == MAPApplicationContextName.shortMsgGatewayContext) {
                    if (compType == ComponentType.Invoke)
                        this.reportSMDeliveryStatusRequest(parameter, mapDialogSmsImpl, invokeId);
                    else
                        this.reportSMDeliveryStatusResponse(parameter, mapDialogSmsImpl, invokeId, vers,
                                compType == ComponentType.ReturnResult);
                }
                break;

            case MAPOperationCode.informServiceCentre:
                if (acn == MAPApplicationContextName.shortMsgGatewayContext && vers >= 2) {
                    if (compType == ComponentType.Invoke)
                        this.informServiceCentreRequest(parameter, mapDialogSmsImpl, invokeId);
                }
                break;

            case MAPOperationCode.alertServiceCentre:
                if (acn == MAPApplicationContextName.shortMsgAlertContext && vers >= 2) {
                    if (compType == ComponentType.Invoke)
                        this.alertServiceCentreRequest(parameter, mapDialogSmsImpl, invokeId, ocValueInt);
                    else
                        this.alertServiceCentreResponse(parameter, mapDialogSmsImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;

            case MAPOperationCode.alertServiceCentreWithoutResult:
                if (acn == MAPApplicationContextName.shortMsgAlertContext && vers == 1) {
                    if (compType == ComponentType.Invoke)
                        this.alertServiceCentreRequest(parameter, mapDialogSmsImpl, invokeId, ocValueInt);
                }
                break;

            case MAPOperationCode.readyForSM:
                if (acn == MAPApplicationContextName.mwdMngtContext && vers >= 2) {
                    if (compType == ComponentType.Invoke)
                        this.readyForSMRequest(parameter, mapDialogSmsImpl, invokeId, ocValueInt);
                    else
                        this.readyForSMResponse(parameter, mapDialogSmsImpl, invokeId, compType == ComponentType.ReturnResult);
                }
                break;

            case MAPOperationCode.noteSubscriberPresent:
                if (acn == MAPApplicationContextName.mwdMngtContext && vers == 1) {
                    if (compType == ComponentType.Invoke)
                        this.noteSubscriberPresentRequest(parameter, mapDialogSmsImpl, invokeId, ocValueInt);
                }
                break;

            default:
                throw new MAPParsingComponentException("MAPServiceSms: unknown incoming operation code: " + ocValueInt,
                        MAPParsingComponentExceptionReason.UnrecognizedOperation);
        }
    }

    private void forwardShortMessageRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding forwardShortMessageRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding moForwardShortMessageRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ForwardShortMessageRequestImpl ind = new ForwardShortMessageRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onForwardShortMessageRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing forwardShortMessageRequest: " + e.getMessage(), e);
            }
        }
    }

    private void forwardShortMessageResponse(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {

        ForwardShortMessageResponseImpl ind = new ForwardShortMessageResponseImpl();

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {

            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onForwardShortMessageResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing forwardShortMessageResponse: " + e.getMessage(), e);
            }
        }
    }

    private void moForwardShortMessageRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding moForwardShortMessageRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding moForwardShortMessageRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        MoForwardShortMessageRequestImpl ind = new MoForwardShortMessageRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onMoForwardShortMessageRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onMoForwardShortMessageIndication: " + e.getMessage(), e);
            }
        }
    }

    private void moForwardShortMessageResponse(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {

        MoForwardShortMessageResponseImpl ind = new MoForwardShortMessageResponseImpl();

        if (parameter != null) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding moForwardShortMessageResponse: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

            byte[] buf = parameter.getData();
            AsnInputStream ais = new AsnInputStream(buf);
            ind.decodeData(ais, buf.length);
        }

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onMoForwardShortMessageResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onMoForwardShortMessageRespIndication: " + e.getMessage(), e);
            }
        }
    }

    private void mtForwardShortMessageRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding mtForwardShortMessageRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding mtForwardShortMessageRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        MtForwardShortMessageRequestImpl ind = new MtForwardShortMessageRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onMtForwardShortMessageRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onMtForwardShortMessageIndication: " + e.getMessage(), e);
            }
        }
    }

    private void mtForwardShortMessageResponse(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {

        MtForwardShortMessageResponseImpl ind = new MtForwardShortMessageResponseImpl();

        if (parameter != null) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding mtForwardShortMessageResponse: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

            byte[] buf = parameter.getData();
            AsnInputStream ais = new AsnInputStream(buf);
            ind.decodeData(ais, buf.length);
        }

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onMtForwardShortMessageResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onMtForwardShortMessageRespIndication: " + e.getMessage(), e);
            }
        }
    }

    private void sendRoutingInfoForSMRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding sendRoutingInfoForSMRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding sendRoutingInfoForSMRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        SendRoutingInfoForSMRequestImpl ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onSendRoutingInfoForSMRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onSendRoutingInfoForSMIndication: " + e.getMessage(), e);
            }
        }
    }

    private void sendRoutingInfoForSMResponse(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {

        SendRoutingInfoForSMResponseImpl ind = new SendRoutingInfoForSMResponseImpl();

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding sendRoutingInfoForSMResponse: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding sendRoutingInfoForSMResponse: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onSendRoutingInfoForSMResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onSendRoutingInfoForSMRespIndication: " + e.getMessage(), e);
            }
        }
    }

    private void reportSMDeliveryStatusRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding sendRoutingInfoForSMRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding sendRoutingInfoForSMRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ReportSMDeliveryStatusRequestImpl ind = new ReportSMDeliveryStatusRequestImpl(mapDialogImpl.getApplicationContext()
                .getApplicationContextVersion().getVersion());
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onReportSMDeliveryStatusRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onReportSMDeliveryStatusIndication: " + e.getMessage(), e);
            }
        }
    }

    private void reportSMDeliveryStatusResponse(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId,
            long mapProtocolVersion, boolean returnResultNotLast) throws MAPParsingComponentException {

        ReportSMDeliveryStatusResponseImpl ind = new ReportSMDeliveryStatusResponseImpl(mapProtocolVersion);

        if (parameter != null) {
            if (mapProtocolVersion >= 3) {
                if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                        || parameter.isPrimitive())
                    throw new MAPParsingComponentException(
                            "Error while decoding reportSMDeliveryStatusResponse: Bad tag or tagClass or parameter is primitive, received tag="
                                    + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
            } else {
                if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                        || !parameter.isPrimitive())
                    throw new MAPParsingComponentException(
                            "Error while decoding reportSMDeliveryStatusResponse: Bad tag or tagClass or parameter is primitive, received tag="
                                    + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
            }

            byte[] buf = parameter.getData();
            AsnInputStream ais = new AsnInputStream(buf);
            ind.decodeData(ais, buf.length);
        }

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onReportSMDeliveryStatusResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onReportSMDeliveryStatusRespIndication: " + e.getMessage(), e);
            }
        }
    }

    private void informServiceCentreRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding informServiceCentreRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding informServiceCentreRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        InformServiceCentreRequestImpl ind = new InformServiceCentreRequestImpl();
        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onInformServiceCentreRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onInformServiceCentreIndication: " + e.getMessage(), e);
            }
        }
    }

    private void alertServiceCentreRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId, int operationCode)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding alertServiceCentreRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding alertServiceCentreRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        AlertServiceCentreRequestImpl ind = new AlertServiceCentreRequestImpl(operationCode);
        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onAlertServiceCentreRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onAlertServiceCentreIndication: " + e.getMessage(), e);
            }
        }
    }

    private void alertServiceCentreResponse(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {

        AlertServiceCentreResponseImpl ind = new AlertServiceCentreResponseImpl();

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {

            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onAlertServiceCentreResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onAlertServiceCentreRespIndication: " + e.getMessage(), e);
            }

        }
    }

    private void readyForSMRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId, int operationCode)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding readyForSMRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding readyForSMRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        ReadyForSMRequestImpl ind = new ReadyForSMRequestImpl();
        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onReadyForSMRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onReadyForSMRequest: " + e.getMessage(), e);
            }
        }
    }

    private void readyForSMResponse(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {

        ReadyForSMResponseImpl ind = new ReadyForSMResponseImpl();

        if (parameter != null) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding readyForSMResponse: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

            byte[] buf = parameter.getData();
            AsnInputStream ais = new AsnInputStream(buf);
            ind.decodeData(ais, buf.length);
        }

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onReadyForSMResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onReadyForSMResponse: " + e.getMessage(), e);
            }
        }
    }

    private void noteSubscriberPresentRequest(Parameter parameter, MAPDialogSmsImpl mapDialogImpl, Long invokeId, int operationCode)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException("Error while decoding noteSubscriberPresentRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || !parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding noteSubscriberPresentRequest: Bad tag or tagClass or parameter is not primitive, received tag=" + parameter.getTag(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        NoteSubscriberPresentRequestImpl ind = new NoteSubscriberPresentRequestImpl();
        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSmsListener) serLis).onNoteSubscriberPresentRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onNoteSubscriberPresentRequest: " + e.getMessage(), e);
            }
        }
    }

}
