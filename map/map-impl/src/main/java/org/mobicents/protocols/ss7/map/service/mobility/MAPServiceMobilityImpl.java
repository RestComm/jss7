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

package org.mobicents.protocols.ss7.map.service.mobility;

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
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoResponseImpl;
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
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataRequestImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataResponseImpl;
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
public class MAPServiceMobilityImpl extends MAPServiceBaseImpl implements MAPServiceMobility {

    protected Logger loger = Logger.getLogger(MAPServiceMobilityImpl.class);

    public MAPServiceMobilityImpl(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    /*
     * Creating a new outgoing MAP Mobility dialog and adding it to the MAPProvider.dialog collection
     */
    public MAPDialogMobility createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
            AddressString destReference) throws MAPException {
        return this.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference, null);
    }

    public MAPDialogMobility createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
            AddressString destReference, Long localTrId) throws MAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new MAPException("Cannot create MAPDialogMobility because MAPServiceMobility is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        MAPDialogMobilityImpl dialog = new MAPDialogMobilityImpl(appCntx, tcapDialog, this.mapProviderImpl, this,
                origReference, destReference);

        this.putMAPDialogIntoCollection(dialog);

        return dialog;
    }

    @Override
    protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
        return new MAPDialogMobilityImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
    }

    public void addMAPServiceListener(MAPServiceMobilityListener mapServiceListener) {
        super.addMAPServiceListener(mapServiceListener);
    }

    public void removeMAPServiceListener(MAPServiceMobilityListener mapServiceListener) {
        super.removeMAPServiceListener(mapServiceListener);
    }

    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
        int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

        switch (ctx) {

        // -- Authentication management services
            case infoRetrievalContext:
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

                // -- Location management services
            case networkLocUpContext:
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
            case locationCancellationContext:
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
            case interVlrInfoRetrievalContext:
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
            case gprsLocationUpdateContext:
                if (vers == 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else if (vers > 3) {
                    long[] altOid = dialogApplicationContext.getOID();
                    altOid[7] = 3;
                    ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }
            case msPurgingContext:
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

                // -- Fault recovery

                // -- International mobile equipment identities management services
            case equipmentMngtContext:
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

                // -- Subscriber Information services
            case anyTimeEnquiryContext:
                if (vers >= 3 && vers <= 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else if (vers > 3) {
                    long[] altOid = dialogApplicationContext.getOID();
                    altOid[7] = 3;
                    ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }

                // -- Subscriber Management services
            case subscriberDataMngtContext:
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

        // -- Location management services
            case MAPOperationCode.updateLocation:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext,
                        MAPApplicationContextVersion.version1);

            case MAPOperationCode.cancelLocation:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext,
                        MAPApplicationContextVersion.version1);

                // -- Authentication management services
            case MAPOperationCode.sendParameters:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.infoRetrievalContext,
                        MAPApplicationContextVersion.version1);

                // -- IMEI services
            case MAPOperationCode.checkIMEI:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext,
                        MAPApplicationContextVersion.version1);

                // -- Subscriber Management services
            case MAPOperationCode.insertSubscriberData:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext,
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
        if (compType == ComponentType.Invoke && this.mapProviderImpl.isCongested()) {
            // we agree mobility services when congestion
        }

        MAPDialogMobilityImpl mapDialogMobilityImpl = (MAPDialogMobilityImpl) mapDialog;

        Long ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
        MAPApplicationContextName acn = mapDialog.getApplicationContext().getApplicationContextName();
        int vers = mapDialog.getApplicationContext().getApplicationContextVersion().getVersion();
        int ocValueInt = (int) (long) ocValue;

        switch (ocValueInt) {

        // -- Location management services
            case MAPOperationCode.updateLocation:
                if (acn == MAPApplicationContextName.networkLocUpContext) {
                    if (compType == ComponentType.Invoke)
                        this.updateLocationRequest(parameter, mapDialogMobilityImpl, invokeId);
                    else
                        this.updateLocationResponse(parameter, mapDialogMobilityImpl, invokeId);
                }
                break;
            case MAPOperationCode.cancelLocation:
                if (acn == MAPApplicationContextName.locationCancellationContext) {
                    if (compType == ComponentType.Invoke)
                        this.cancelLocationRequest(parameter, mapDialogMobilityImpl, invokeId);
                    else
                        this.cancelLocationResponse(parameter, mapDialogMobilityImpl, invokeId);
                }
                break;
            case MAPOperationCode.sendIdentification:
                if (acn == MAPApplicationContextName.interVlrInfoRetrievalContext) {
                    if (compType == ComponentType.Invoke)
                        this.SendIdentificationRequest(parameter, mapDialogMobilityImpl, invokeId);
                    else
                        this.SendIdentificationResponse(parameter, mapDialogMobilityImpl, invokeId);
                }
                break;
            case MAPOperationCode.updateGprsLocation:
                if (acn == MAPApplicationContextName.gprsLocationUpdateContext) {
                    if (compType == ComponentType.Invoke)
                        this.updateGprsLocationRequest(parameter, mapDialogMobilityImpl, invokeId);
                    else
                        this.updateGprsLocationResponse(parameter, mapDialogMobilityImpl, invokeId);
                }
                break;
            case MAPOperationCode.purgeMS:
                if (acn == MAPApplicationContextName.msPurgingContext) {
                    if (compType == ComponentType.Invoke)
                        this.purgeMSRequest(parameter, mapDialogMobilityImpl, invokeId);
                    else
                        this.purgeMSResponse(parameter, mapDialogMobilityImpl, invokeId);
                }
                break;
            // -- Authentication management services
            case MAPOperationCode.sendAuthenticationInfo:
                if (acn == MAPApplicationContextName.infoRetrievalContext && vers >= 2) {
                    if (compType == ComponentType.Invoke)
                        this.sendAuthenticationInfoRequest(parameter, mapDialogMobilityImpl, invokeId);
                    else
                        this.sendAuthenticationInfoResponse(parameter, mapDialogMobilityImpl, invokeId);
                }
                break;

            // -- Subscriber Information services
            case MAPOperationCode.anyTimeInterrogation:
                if (acn == MAPApplicationContextName.anyTimeEnquiryContext) {
                    if (compType == ComponentType.Invoke)
                        this.processAnyTimeInterrogationRequest(parameter, mapDialogMobilityImpl, invokeId);
                    else
                        this.processAnyTimeInterrogationResponse(parameter, mapDialogMobilityImpl, invokeId);
                }
                break;

            // -- IMEI services
            case MAPOperationCode.checkIMEI:
                if (acn == MAPApplicationContextName.equipmentMngtContext) {
                    if (compType == ComponentType.Invoke)
                        this.processCheckImeiRequest(parameter, mapDialogMobilityImpl, invokeId);
                    else
                        this.processCheckImeiResponse(parameter, mapDialogMobilityImpl, invokeId);
                }
                break;

            // -- Subscriber management services
            case MAPOperationCode.insertSubscriberData:
            if (acn == MAPApplicationContextName.subscriberDataMngtContext || acn == MAPApplicationContextName.networkLocUpContext
                    || acn == MAPApplicationContextName.gprsLocationUpdateContext) {
                    if (compType == ComponentType.Invoke)
                        this.processInsertSubscriberDataRequest(parameter, mapDialogMobilityImpl, invokeId);
                    else
                        this.processInsertSubscriberDataResponse(parameter, mapDialogMobilityImpl, invokeId);
                }
                break;

            default:
                throw new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
        }
    }

    // -- Location management services
    private void updateLocationRequest(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding updateLocationRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding updateLocationRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        UpdateLocationRequestImpl ind = new UpdateLocationRequestImpl(version);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceMobilityListener) serLis).onUpdateLocationRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing updateLocationRequest: " + e.getMessage(), e);
            }
        }
    }

    private void updateLocationResponse(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding updateLocationResponse: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (version >= 2) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding updateLocationResponse V2_3: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        } else {
            if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                    || !parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding updateLocationResponse V1: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);
        UpdateLocationResponseImpl ind = new UpdateLocationResponseImpl(version);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceMobilityListener) serLis).onUpdateLocationResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing updateLocationResponse: " + e.getMessage(), e);
            }
        }
    }

    private void cancelLocationRequest(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding cancelLocationRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (version == 3) {
            if (parameter.getTag() != CancelLocationRequestImpl.TAG_cancelLocationRequest
                    || parameter.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding cancelLocationRequest: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        } else {
            if ((!(parameter.getTag() == Tag.SEQUENCE || parameter.getTag() == Tag.STRING_OCTET))
                    || parameter.getTagClass() != Tag.CLASS_UNIVERSAL)
                throw new MAPParsingComponentException(
                        "Error while decoding cancelLocationRequest: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());
        CancelLocationRequestImpl ind = new CancelLocationRequestImpl(version);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceMobilityListener) serLis).onCancelLocationRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing cancelLocationRequest: " + e.getMessage(), e);
            }
        }
    }

    private void cancelLocationResponse(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {
        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();

        CancelLocationResponseImpl ind = new CancelLocationResponseImpl();

        if (parameter != null) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding cancelLocationResponse V2_3: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

            byte[] buf = parameter.getData();
            AsnInputStream ais = new AsnInputStream(buf);

            ind.decodeData(ais, buf.length);
        }

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceMobilityListener) serLis).onCancelLocationResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing cancelLocationResponse: " + e.getMessage(), e);
            }
        }
    }

    private void SendIdentificationRequest(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding sendIdentificationRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (version == 3) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding sendIdentificationRequest: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        } else {
            if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                    || !parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding sendIdentificationRequest: Bad tag or tagClass or parameter is not primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());
        SendIdentificationRequestImpl ind = new SendIdentificationRequestImpl(version);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceMobilityListener) serLis).onSendIdentificationRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing sendIdentificationRequest: " + e.getMessage(), e);
            }
        }
    }

    private void SendIdentificationResponse(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {
        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();

        SendIdentificationResponseImpl ind = new SendIdentificationResponseImpl(version);

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding SendIdentificationResponse: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (version == 3) {
            if (parameter.getTag() != SendIdentificationResponseImpl._TAG_SendIdentificationResponse
                    || parameter.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding sendIdentificationResponse: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        } else {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding sendIdentificationResponse: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);

        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceMobilityListener) serLis).onSendIdentificationResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing sendIdentificationResponse: " + e.getMessage(), e);
            }
        }
    }

    private void updateGprsLocationRequest(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding updateGprsLocationRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (version == 3) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding updateGprsLocationRequest: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());
        UpdateGprsLocationRequestImpl ind = new UpdateGprsLocationRequestImpl();
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceMobilityListener) serLis).onUpdateGprsLocationRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing UpdateGprsLocationRequest: " + e.getMessage(), e);
            }
        }
    }

    private void updateGprsLocationResponse(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {
        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();

        UpdateGprsLocationResponseImpl ind = new UpdateGprsLocationResponseImpl();

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding updateGprsLocationResponse: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding updateGprsLocationResponse V3: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);

        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceMobilityListener) serLis).onUpdateGprsLocationResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing UpdateGprsLocationResponse: " + e.getMessage(), e);
            }
        }
    }

    private void purgeMSRequest(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        if (parameter == null)
            throw new MAPParsingComponentException("Error while decoding PurgeMSRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (version == 3) {
            if (parameter.getTag() != PurgeMSRequestImpl._TAG_PurgeMSRequest
                    || parameter.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding PurgeMSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (version == 2) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding PurgeMSRequest: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());
        PurgeMSRequestImpl ind = new PurgeMSRequestImpl(version);
        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceMobilityListener) serLis).onPurgeMSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing PurgeMSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void purgeMSResponse(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {
        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();

        PurgeMSResponseImpl ind = new PurgeMSResponseImpl();

        if (parameter != null) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException("Error while decoding PurgeMSResponse V3: Bad tag or tagClass or parameter is primitive, received tag="
                        + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

            byte[] buf = parameter.getData();
            AsnInputStream ais = new AsnInputStream(buf);

            ind.decodeData(ais, buf.length);
        }

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceMobilityListener) serLis).onPurgeMSResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing PurgeMSResponse: " + e.getMessage(), e);
            }
        }
    }

    // -- Authentication management services
    private void sendAuthenticationInfoRequest(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        SendAuthenticationInfoRequestImpl ind = new SendAuthenticationInfoRequestImpl(version);
        if (version >= 3) {
            if (parameter != null) {
                if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                        || parameter.isPrimitive())
                    throw new MAPParsingComponentException(
                            "Error while decoding sendAuthenticationInfoRequest V3: Bad tag or tagClass or parameter is primitive, received tag="
                                    + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

                byte[] buf = parameter.getData();
                AsnInputStream ais = new AsnInputStream(buf);
                ind.decodeData(ais, buf.length);
            }
        } else {
            if (parameter == null)
                throw new MAPParsingComponentException(
                        "Error while decoding sendAuthenticationInfoRequest V2: Parameter is mandatory but not found",
                        MAPParsingComponentExceptionReason.MistypedParameter);

            if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                    || !parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding sendAuthenticationInfoRequest V2: Bad tag or tagClass or parameter is not primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

            byte[] buf = parameter.getData();
            AsnInputStream ais = new AsnInputStream(buf);
            ind.decodeData(ais, buf.length);
        }

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceMobilityListener) serLis).onSendAuthenticationInfoRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing sendAuthenticationInfoRequest: " + e.getMessage(), e);
            }
        }
    }

    private void sendAuthenticationInfoResponse(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        SendAuthenticationInfoResponseImpl ind = new SendAuthenticationInfoResponseImpl(version);
        if (version >= 3) {
            if (parameter != null) {
                if (parameter.getTag() != SendAuthenticationInfoResponseImpl._TAG_General
                        || parameter.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || parameter.isPrimitive())
                    throw new MAPParsingComponentException(
                            "Error while decoding sendAuthenticationInfoResponse: Bad tag or tagClass or parameter is primitive, received tag="
                                    + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

                byte[] buf = parameter.getData();
                AsnInputStream ais = new AsnInputStream(buf);
                ind.decodeData(ais, buf.length);
            }
        } else {
            if (parameter != null) {
                if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                        || parameter.isPrimitive())
                    throw new MAPParsingComponentException(
                            "Error while decoding sendAuthenticationInfoResponse: Bad tag or tagClass or parameter is primitive, received tag="
                                    + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

                byte[] buf = parameter.getData();
                AsnInputStream ais = new AsnInputStream(buf);
                ind.decodeData(ais, buf.length);
            }
        }

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceMobilityListener) serLis).onSendAuthenticationInfoResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing sendAuthenticationInfoResponse: " + e.getMessage(), e);
            }
        }
    }

    // -- Subscriber Information services
    private void processAnyTimeInterrogationRequest(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding AnyTimeInterrogationRequestIndication: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding AnyTimeInterrogationRequestIndication: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);

        AnyTimeInterrogationRequestImpl ind = new AnyTimeInterrogationRequestImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                ((MAPServiceMobilityListener) serLis).onAnyTimeInterrogationRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing ProcessUnstructuredSSRequestIndication: " + e.getMessage(), e);
            }
        }

    }

    private void processAnyTimeInterrogationResponse(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding AnyTimeInterrogationResponseIndication: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding AnyTimeInterrogationResponseIndication: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);

        AnyTimeInterrogationResponseImpl ind = new AnyTimeInterrogationResponseImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                ((MAPServiceMobilityListener) serLis).onAnyTimeInterrogationResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing ProcessUnstructuredSSRequestIndication: " + e.getMessage(), e);
            }
        }

    }

    // - IMEI services
    private void processCheckImeiRequest(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding CheckImeiRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();

        if (version >= 3) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding CheckImeiRequest: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        } else {
            if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                    || !parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding CheckImeiRequest V1 or V2: Bad tag or tagClass or parameter is not primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);

        CheckImeiRequestImpl ind = new CheckImeiRequestImpl(version);
        ind.decodeData(ais, parameter.getEncodingLength());

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                ((MAPServiceMobilityListener) serLis).onCheckImeiRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing processCheckImeiRequest: " + e.getMessage(), e);
            }
        }
    }

    private void processCheckImeiResponse(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding CheckImeiResponse: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();

        if (version >= 3) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding CheckImeiResponse: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        } else {
            if (parameter.getTag() != Tag.ENUMERATED || parameter.getTagClass() != Tag.CLASS_UNIVERSAL
                    || !parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding CheckImeiResponse: Bad tag or tagClass or parameter is not primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);

        CheckImeiResponseImpl ind = new CheckImeiResponseImpl(version);
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                ((MAPServiceMobilityListener) serLis).onCheckImeiResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing processCheckImeiResponse: " + e.getMessage(), e);
            }
        }
    }

    // - Subscriber management services
    private void processInsertSubscriberDataRequest(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding InsertSubscriberDataRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding InsertSubscriberDataRequest: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf);

        InsertSubscriberDataRequestImpl ind = new InsertSubscriberDataRequestImpl(version);
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                ((MAPServiceMobilityListener) serLis).onInsertSubscriberDataRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing processInsertSubscriberDataRequest: " + e.getMessage(), e);
            }
        }
    }

    private void processInsertSubscriberDataResponse(Parameter parameter, MAPDialogMobilityImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        long version = mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion();
        InsertSubscriberDataResponseImpl ind = new InsertSubscriberDataResponseImpl(version);

        if (parameter != null) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding InsertSubscriberDataResponse: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

            byte[] buf = parameter.getData();
            AsnInputStream ais = new AsnInputStream(buf);
            ind.decodeData(ais, buf.length);
        }

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                ((MAPServiceMobilityListener) serLis).onInsertSubscriberDataResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing processInsertSubscriberDataResponse: " + e.getMessage(), e);
            }
        }
    }

}
