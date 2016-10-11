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

package org.mobicents.protocols.ss7.map.service.supplementary;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.MAPServiceBaseImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.map.service.sms.MAPServiceSmsImpl;
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
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class MAPServiceSupplementaryImpl extends MAPServiceBaseImpl implements MAPServiceSupplementary {

    private static final Logger loger = Logger.getLogger(MAPServiceSmsImpl.class);

    public MAPServiceSupplementaryImpl(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    /**
     * Creating a new outgoing MAP Supplementary dialog and adding it to the MAPProvider.dialog collection
     *
     */
    public MAPDialogSupplementary createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
            AddressString destReference) throws MAPException {
        return this.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference, null);
    }

    public MAPDialogSupplementary createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
            AddressString destReference, Long localTrId) throws MAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new MAPException("Cannot create MAPDialogSupplementary because MAPServiceSupplementary is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        MAPDialogSupplementaryImpl dialog = new MAPDialogSupplementaryImpl(appCntx, tcapDialog, this.mapProviderImpl, this,
                origReference, destReference);

        this.putMAPDialogIntoCollection(dialog);

        return dialog;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.MAPServiceBaseImpl#createNewDialogIncoming
     * (org.mobicents.protocols.ss7.map.api.MAPApplicationContext, org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog)
     */
    protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
        return new MAPDialogSupplementaryImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
    }

    public void addMAPServiceListener(MAPServiceSupplementaryListener mapServiceListener) {
        super.addMAPServiceListener(mapServiceListener);
    }

    public void removeMAPServiceListener(MAPServiceSupplementaryListener mapServiceListener) {
        super.removeMAPServiceListener(mapServiceListener);
    }

    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
        int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

        switch (ctx) {
        case networkUnstructuredSsContext:
            if (vers == 2) {
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
            } else if (vers > 2) {
                long[] altOid = dialogApplicationContext.getOID();
                altOid[7] = 2;
                ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
            } else {
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
            }
        case networkFunctionalSsContext:
            if (vers == 2) {
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
            } else if (vers > 2) {
                long[] altOid = dialogApplicationContext.getOID();
                altOid[7] = 2;
                ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
            } else {
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
            }
        }

        return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
    }

    public long[] getLinkedOperationList(long operCode) {
        if (operCode == MAPOperationCode.registerPassword) {
            return new long[] { MAPOperationCode.getPassword };
        }

        return null;
    }

    public void processComponent(ComponentType compType, OperationCode oc, Parameter parameter, MAPDialog mapDialog,
            Long invokeId, Long linkedId, Invoke linkedInvoke) throws MAPParsingComponentException {

        // if an application-context-name different from version 1 is
        // received in a syntactically correct TC-
        // BEGIN indication primitive but is not acceptable from a load
        // control point of view, the MAP PM
        // shall ignore this dialogue request. The MAP-user is not informed.
//        if (compType == ComponentType.Invoke && this.mapProviderImpl.isCongested()) {
//            // we reject all supplementary services when congestion
//            return;
//        }

        MAPDialogSupplementaryImpl mapDialogSupplementaryImpl = (MAPDialogSupplementaryImpl) mapDialog;

        Long ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);

        long ocValueInt = ocValue;
        int ocValueInt2 = (int) ocValueInt;
        MAPApplicationContextName acn = mapDialog.getApplicationContext().getApplicationContextName();
        switch (ocValueInt2) {
            case MAPOperationCode.registerSS:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                    if (compType == ComponentType.Invoke)
                        this.registerSSRequest(parameter, mapDialogSupplementaryImpl, invokeId);
                    else
                        this.registerSSResponse(parameter, mapDialogSupplementaryImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;
            case MAPOperationCode.eraseSS:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                    if (compType == ComponentType.Invoke)
                        this.eraseSSRequest(parameter, mapDialogSupplementaryImpl, invokeId);
                    else
                        this.eraseSSResponse(parameter, mapDialogSupplementaryImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;
            case MAPOperationCode.activateSS:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                    if (compType == ComponentType.Invoke)
                        this.activateSSRequest(parameter, mapDialogSupplementaryImpl, invokeId);
                    else
                        this.activateSSResponse(parameter, mapDialogSupplementaryImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;
            case MAPOperationCode.deactivateSS:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                    if (compType == ComponentType.Invoke)
                        this.deactivateSSRequest(parameter, mapDialogSupplementaryImpl, invokeId);
                    else
                        this.deactivateSSResponse(parameter, mapDialogSupplementaryImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;
            case MAPOperationCode.interrogateSS:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                    if (compType == ComponentType.Invoke)
                        this.interrogateSSRequest(parameter, mapDialogSupplementaryImpl, invokeId);
                    else
                        this.interrogateSSResponse(parameter, mapDialogSupplementaryImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;
            case MAPOperationCode.registerPassword:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                    if (compType == ComponentType.Invoke)
                        this.registerPasswordRequest(parameter, mapDialogSupplementaryImpl, invokeId);
                    else
                        this.registerPasswordResponse(parameter, mapDialogSupplementaryImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;
            case MAPOperationCode.getPassword:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                    if (compType == ComponentType.Invoke)
                        this.getPasswordRequest(parameter, mapDialogSupplementaryImpl, invokeId, linkedId, linkedInvoke);
                    else
                        this.getPasswordResponse(parameter, mapDialogSupplementaryImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;

            case MAPOperationCode.processUnstructuredSS_Request:
                if (acn == MAPApplicationContextName.networkUnstructuredSsContext) {
                    if (compType == ComponentType.Invoke)
                        this.processUnstructuredSSRequest(parameter, mapDialogSupplementaryImpl, invokeId);
                    else
                        this.processUnstructuredSSResponse(parameter, mapDialogSupplementaryImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;
            case MAPOperationCode.unstructuredSS_Request:
                if (acn == MAPApplicationContextName.networkUnstructuredSsContext) {
                    if (compType == ComponentType.Invoke)
                        this.unstructuredSSRequest(parameter, mapDialogSupplementaryImpl, invokeId);
                    else
                        this.unstructuredSSResponse(parameter, mapDialogSupplementaryImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;
            case MAPOperationCode.unstructuredSS_Notify:
                if (acn == MAPApplicationContextName.networkUnstructuredSsContext) {
                    if (compType == ComponentType.Invoke)
                        this.unstructuredSSNotifyRequest(parameter, mapDialogSupplementaryImpl, invokeId);
                    else
                        this.unstructuredSSNotifyResponse(parameter, mapDialogSupplementaryImpl, invokeId,
                                compType == ComponentType.ReturnResult);
                }
                break;
            default:
                throw new MAPParsingComponentException("MAPServiceSupplementary: unknown incoming operation code: "
                        + ocValueInt2, MAPParsingComponentExceptionReason.UnrecognizedOperation);
        }
    }

    private void registerSSRequest(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
        if (parameter == null)
            throw new MAPParsingComponentException("Error while decoding registerSSRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding registerSSRequest: Bad tag or tagClass or parameter is primitive, received tag=" + parameter.getTag(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        RegisterSSRequestImpl ind = new RegisterSSRequestImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onRegisterSSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing registerSSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void registerSSResponse(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {
        RegisterSSResponseImpl ind = new RegisterSSResponseImpl();

        if (parameter != null) {
            // we do not check tag / tagClass because it is a choice

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
                ((MAPServiceSupplementaryListener) serLis).onRegisterSSResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onRegisterSSResponse: " + e.getMessage(), e);
            }
        }
    }

    private void eraseSSRequest(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
        if (parameter == null)
            throw new MAPParsingComponentException("Error while decoding eraseSSRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding eraseSSRequest: Bad tag or tagClass or parameter is primitive, received tag=" + parameter.getTag(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        EraseSSRequestImpl ind = new EraseSSRequestImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onEraseSSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onEraseSSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void eraseSSResponse(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {
        EraseSSResponseImpl ind = new EraseSSResponseImpl();

        if (parameter != null) {
            // we do not check tag / tagClass because it is a choice

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
                ((MAPServiceSupplementaryListener) serLis).onEraseSSResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onEraseSSResponse: " + e.getMessage(), e);
            }
        }
    }

    private void activateSSRequest(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
        if (parameter == null)
            throw new MAPParsingComponentException("Error while decoding activateSSRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding activateSSRequest: Bad tag or tagClass or parameter is primitive, received tag=" + parameter.getTag(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        ActivateSSRequestImpl ind = new ActivateSSRequestImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onActivateSSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onActivateSSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void activateSSResponse(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {
        ActivateSSResponseImpl ind = new ActivateSSResponseImpl();

        if (parameter != null) {
            // we do not check tag / tagClass because it is a choice

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
                ((MAPServiceSupplementaryListener) serLis).onActivateSSResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onActivateSSResponse: " + e.getMessage(), e);
            }
        }
    }

    private void deactivateSSRequest(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
        if (parameter == null)
            throw new MAPParsingComponentException("Error while decoding deactivateSSRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding deactivateSSRequest: Bad tag or tagClass or parameter is primitive, received tag=" + parameter.getTag(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        DeactivateSSRequestImpl ind = new DeactivateSSRequestImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onDeactivateSSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onDeactivateSSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void deactivateSSResponse(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {
        DeactivateSSResponseImpl ind = new DeactivateSSResponseImpl();

        if (parameter != null) {
            // we do not check tag / tagClass because it is a choice

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
                ((MAPServiceSupplementaryListener) serLis).onDeactivateSSResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onDeactivateSSResponse: " + e.getMessage(), e);
            }
        }
    }

    private void interrogateSSRequest(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
        if (parameter == null)
            throw new MAPParsingComponentException("Error while decoding interrogateSSRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding interrogateSSRequest: Bad tag or tagClass or parameter is primitive, received tag=" + parameter.getTag(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        InterrogateSSRequestImpl ind = new InterrogateSSRequestImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onInterrogateSSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onInterrogateSSRequest: " + e.getMessage(), e);
            }
        }
    }

    private void interrogateSSResponse(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {
        InterrogateSSResponseImpl ind = new InterrogateSSResponseImpl();

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding interrogateSSResponse: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        // we do not check tag / tagClass because it is a choice

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onInterrogateSSResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onInterrogateSSResponse: " + e.getMessage(), e);
            }
        }
    }

    private void getPasswordRequest(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId, Long linkedId, Invoke linkedInvoke)
            throws MAPParsingComponentException {
        if (parameter == null)
            throw new MAPParsingComponentException("Error while decoding getPasswordRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.ENUMERATED || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || !parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding getPasswordRequest: Bad tag or tagClass or parameter is not primitive, received tag=" + parameter.getTag(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        GetPasswordRequestImpl ind = new GetPasswordRequestImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setLinkedId(linkedId);
        ind.setLinkedInvoke(linkedInvoke);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onGetPasswordRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onGetPasswordRequest: " + e.getMessage(), e);
            }
        }
    }

    private void getPasswordResponse(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {
        GetPasswordResponseImpl ind = new GetPasswordResponseImpl();

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding getPasswordResponse: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.STRING_NUMERIC || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || !parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding getPasswordResponse: Bad tag or tagClass or parameter is not primitive, received tag=" + parameter.getTag(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onGetPasswordResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onGetPasswordResponse: " + e.getMessage(), e);
            }
        }
    }

    private void registerPasswordRequest(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId) throws MAPParsingComponentException {
        if (parameter == null)
            throw new MAPParsingComponentException("Error while decoding registerPasswordRequest: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.STRING_OCTET || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || !parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding registerPasswordRequest: Bad tag or tagClass or parameter is not primitive, received tag=" + parameter.getTag(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        RegisterPasswordRequestImpl ind = new RegisterPasswordRequestImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onRegisterPasswordRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing onRegisterPasswordRequest: " + e.getMessage(), e);
            }
        }
    }

    private void registerPasswordResponse(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {
        RegisterPasswordResponseImpl ind = new RegisterPasswordResponseImpl();

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding registerPasswordResponse: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.STRING_NUMERIC || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || !parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding registerPasswordResponse: Bad tag or tagClass or parameter is not primitive, received tag=" + parameter.getTag(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        ind.decodeData(ais, buf.length);

        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onRegisterPasswordResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing onRegisterPasswordResponse: " + e.getMessage(), e);
            }
        }
    }


    private void unstructuredSSNotifyRequest(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {
        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding unstructuredSSNotifyIndication: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding unstructuredSSNotifyIndication: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        UnstructuredSSNotifyRequestImpl ind = new UnstructuredSSNotifyRequestImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onUnstructuredSSNotifyRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing unstructuredSSNotifyIndication: " + e.getMessage(), e);
            }
        }
    }

    private void unstructuredSSNotifyResponse(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {

        UnstructuredSSNotifyResponseImpl ind = new UnstructuredSSNotifyResponseImpl();
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onUnstructuredSSNotifyResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing unstructuredSSNotifyIndication: " + e.getMessage(), e);
            }
        }
    }

    private void unstructuredSSRequest(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {
        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding UnstructuredSSRequestIndication: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding UnstructuredSSRequestIndication: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        UnstructuredSSRequestImpl ind = new UnstructuredSSRequestImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onUnstructuredSSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing UnstructuredSSRequestIndication: " + e.getMessage(), e);
            }
        }
    }

    private void unstructuredSSResponse(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {

        UnstructuredSSResponseImpl ind = new UnstructuredSSResponseImpl();

        if (parameter != null) {
            if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
                throw new MAPParsingComponentException(
                        "Error while decoding UnstructuredSSResponseIndication: Bad tag or tagClass or parameter is primitive, received tag="
                                + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

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
                ((MAPServiceSupplementaryListener) serLis).onUnstructuredSSResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing UnstructuredSSResponseIndication: " + e.getMessage(), e);
            }
        }

    }

    private void processUnstructuredSSRequest(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId)
            throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding ProcessUnstructuredSSRequestIndication: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding ProcessUnstructuredSSRequestIndication: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        ProcessUnstructuredSSRequestImpl ind = new ProcessUnstructuredSSRequestImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onProcessUnstructuredSSRequest(ind);
            } catch (Exception e) {
                loger.error("Error processing ProcessUnstructuredSSRequestIndication: " + e.getMessage(), e);
            }
        }

    }

    private void processUnstructuredSSResponse(Parameter parameter, MAPDialogSupplementaryImpl mapDialogImpl, Long invokeId,
            boolean returnResultNotLast) throws MAPParsingComponentException {

        if (parameter == null)
            throw new MAPParsingComponentException(
                    "Error while decoding ProcessUnstructuredSSResponseIndication: Parameter is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        if (parameter.getTag() != Tag.SEQUENCE || parameter.getTagClass() != Tag.CLASS_UNIVERSAL || parameter.isPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding ProcessUnstructuredSSResponseIndication: Bad tag or tagClass or parameter is primitive, received tag="
                            + parameter.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);

        byte[] buf = parameter.getData();
        AsnInputStream ais = new AsnInputStream(buf, parameter.getTagClass(), parameter.isPrimitive(), parameter.getTag());

        ProcessUnstructuredSSResponseImpl ind = new ProcessUnstructuredSSResponseImpl();
        ind.decodeData(ais, buf.length);
        ind.setInvokeId(invokeId);
        ind.setMAPDialog(mapDialogImpl);
        ind.setReturnResultNotLast(returnResultNotLast);

        for (MAPServiceListener serLis : this.serviceListeners) {
            try {
                serLis.onMAPMessage(ind);
                ((MAPServiceSupplementaryListener) serLis).onProcessUnstructuredSSResponse(ind);
            } catch (Exception e) {
                loger.error("Error processing ProcessUnstructuredSSResponseIndication: " + e.getMessage(), e);
            }
        }

    }
}
