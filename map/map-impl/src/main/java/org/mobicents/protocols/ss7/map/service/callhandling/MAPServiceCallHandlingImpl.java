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
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPDialogCallHandling;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandling;
import org.mobicents.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandlingListener;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/*
 *
 * @author cristian veliscu
 * @author eva ogallar
 */
public class MAPServiceCallHandlingImpl extends MAPServiceBaseImpl implements MAPServiceCallHandling {
    private static final Logger loger = Logger.getLogger(MAPServiceCallHandlingImpl.class);

    // Include these constants in MAPApplicationContextName and MAPOperationCode
    // sendRoutingInfo_Request: add constant to MAPMessageType
    // sendRoutingInfo_Response: add constant to MAPMessageType
    protected static final int version = 3;

    public MAPServiceCallHandlingImpl(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    @Override
    public MAPDialogCallHandling createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
            AddressString destReference) throws MAPException {
        return this.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference, null);
    }

    @Override
    public MAPDialogCallHandling createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
            AddressString destReference, Long localTrId) throws MAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new MAPException(
                    "Cannot create MAPDialogRoutingInformation because MAPServiceRoutingInformation is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        MAPDialogCallHandlingImpl dialog = new MAPDialogCallHandlingImpl(appCntx, tcapDialog, this.mapProviderImpl, this,
                origReference, destReference);

        this.putMAPDialogIntoCollection(dialog);
        return dialog;
    }

    @Override
    protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
        return new MAPDialogCallHandlingImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
    }

    @Override
    public void addMAPServiceListener(MAPServiceCallHandlingListener mapServiceListener) {
        super.addMAPServiceListener(mapServiceListener);
    }

    @Override
    public void removeMAPServiceListener(MAPServiceCallHandlingListener mapServiceListener) {
        super.removeMAPServiceListener(mapServiceListener);
    }

    @Override
    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
        int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

        switch (ctx) {
            case locationInfoRetrievalContext:
                if (vers >= 1 && vers <= 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }
            case roamingNumberEnquiryContext:
                if (vers >= 1 && vers <= 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }
            case ServiceTerminationContext:
                if (vers == 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }
        }

        return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
    }

    @Override
    public MAPApplicationContext getMAPv1ApplicationContext(int operationCode, Invoke invoke) {
        switch (operationCode) {
            case MAPOperationCode.sendRoutingInfo:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.locationInfoRetrievalContext,
                        MAPApplicationContextVersion.version1);
            case MAPOperationCode.provideRoamingNumber:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.roamingNumberEnquiryContext,
                        MAPApplicationContextVersion.version1);
        }

        return null;
    }

    @Override
    public void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, MAPDialog mapDialog,
            Long invokeId, Long linkedId, Invoke linkedInvoke) throws MAPParsingComponentException {

//        if (compType == ComponentType.Invoke && this.mapProviderImpl.isCongested()) {
//            // we reject all supplementary services when congestion
//            return;
//        }

        MAPDialogCallHandlingImpl mapDialogImpl = (MAPDialogCallHandlingImpl) mapDialog;

        Long ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
        MAPApplicationContextName acn = mapDialog.getApplicationContext().getApplicationContextName();
        int vers = mapDialog.getApplicationContext().getApplicationContextVersion().getVersion();
        int ocValueInt = (int) (long) ocValue;

        switch (ocValueInt) {
            case MAPOperationCode.sendRoutingInfo:
                if (compType == ComponentType.Invoke)
                    this.sendRoutingInformationRequest(parameter, mapDialogImpl, invokeId);
                else if (compType == ComponentType.ReturnResult || compType == ComponentType.ReturnResultLast)
                    this.sendRoutingInformationResponse(parameter, mapDialogImpl, invokeId,
                            compType == ComponentType.ReturnResult);
                break;

            case MAPOperationCode.provideRoamingNumber:
                if (compType == ComponentType.Invoke)
                    this.provideRoamingNumberRequest(parameter, mapDialogImpl, invokeId);
                else if (compType == ComponentType.ReturnResult || compType == ComponentType.ReturnResultLast)
                    this.provideRoamingNumberResponse(parameter, mapDialogImpl, invokeId,
                            compType == ComponentType.ReturnResult);
                break;

            case MAPOperationCode.istCommand:
                if (compType == ComponentType.Invoke)
                    this.istCommandRequest(parameter, mapDialogImpl, invokeId);
                else if (compType == ComponentType.ReturnResult || compType == ComponentType.ReturnResultLast)
                    this.istCommandResponse(parameter, mapDialogImpl, invokeId, compType == ComponentType.ReturnResult);
                break;
            default:
            throw new MAPParsingComponentException("MAPServiceCallHandling: unknown incoming operation code: " + ocValueInt,
                    MAPParsingComponentExceptionReason.UnrecognizedOperation);
        }
    }

    private void sendRoutingInformationRequest(Parameter parameter, MAPDialogCallHandlingImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {
        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        SendRoutingInformationRequestImpl ind = new SendRoutingInformationRequestImpl(version);

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding SendRoutingInformationRequestIndication: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        // No matter what MAP version V1,V2,V3: tag=Tag.SEQUENCE and tagClass=Tag.CLASS_UNIVERSAL
        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding SendRoutingInformationRequestIndication: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);

        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceCallHandlingListener) serLis).onSendRoutingInformationRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing SendRoutingInformationRequestIndication: " + e.getMessage(), e);
            }
        }
    }

    private void sendRoutingInformationResponse(Parameter parameter, MAPDialogCallHandlingImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {
        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        SendRoutingInformationResponseImpl ind = new SendRoutingInformationResponseImpl(version);

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding SendRoutingInformationResponseIndication: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (version >= 3) { // tag=3 and tagClass=Tag.CLASS_CONTEXT_SPECIFIC
            if (parameter.getTag() != SendRoutingInformationResponseImpl.TAG_sendRoutingInfoRes
                    || parameter.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding SendRoutingInformationResponseIndication: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        } else {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding SendRoutingInformationResponseIndication: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);

        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceCallHandlingListener) serLis).onSendRoutingInformationResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing SendRoutingInformationResponseIndication: " + e.getMessage(), e);
            }
        }
    }

    private void provideRoamingNumberRequest(Parameter parameter, MAPDialogCallHandlingImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {
        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        ProvideRoamingNumberRequestImpl ind = new ProvideRoamingNumberRequestImpl(version);

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding ProvideRoamingNumberRequestIndication: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        // No matter what MAP version V1,V2,V3: tag=Tag.SEQUENCE and tagClass=Tag.CLASS_UNIVERSAL
        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding ProvideRoamingNumberRequestIndication: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);

        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceCallHandlingListener) serLis).onProvideRoamingNumberRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing ProvideRoamingNumberRequestIndication: " + e.getMessage(), e);
            }
        }
    }

    private void provideRoamingNumberResponse(Parameter parameter, MAPDialogCallHandlingImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {
        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        ProvideRoamingNumberResponseImpl ind = new ProvideRoamingNumberResponseImpl(version);

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding ProvideRoamingNumberResponseIndication: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (version >= 3) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding ProvideRoamingNumberResponseIndication: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        } else {
            if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                    || !parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding ProvideRoamingNumberResponseIndication: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceCallHandlingListener) serLis).onProvideRoamingNumberResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing ProvideRoamingNumberResponseIndication: " + e.getMessage(), e);
            }
        }
    }

    private void istCommandRequest(Parameter parameter, MAPDialogCallHandlingImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {
        IstCommandRequestImpl ind = new IstCommandRequestImpl();

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding IstCommandRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        // No matter what MAP version V1,V2,V3: tag=Tag.SEQUENCE and tagClass=Tag.CLASS_UNIVERSAL
        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding IstCommandRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);

        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceCallHandlingListener) serLis).onIstCommandRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing IstCommandRequest: " + e.getMessage(), e);
            }
        }
    }

    private void istCommandResponse(Parameter parameter, MAPDialogCallHandlingImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {
        IstCommandResponseImpl ind = new IstCommandResponseImpl();

        if (parameter != null) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive()) {
                throw new MAPParsingComponentException(
                        "Error while decoding IstCommandResponse: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
            }
            byte[] buf = parameter.getData();
            AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());
            ind.decodeData(ais, buf.length);
        }

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceCallHandlingListener) serLis).onIstCommandResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing IstCommandResponse: " + e.getMessage(), e);
            }
        }
    }
}
