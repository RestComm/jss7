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

import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingFeature;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GenericServiceInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.GuidanceInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.Password;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSForBSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Return;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 *
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class MAPDialogSupplementaryImpl extends MAPDialogImpl implements MAPDialogSupplementary {

    protected MAPDialogSupplementaryImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
            MAPServiceSupplementary mapService, AddressString origReference, AddressString destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }

    public Long addProcessUnstructuredSSRequest(CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString,
            AlertingPattern alertingPatter, ISDNAddressString msisdn) throws MAPException {
        return this.addProcessUnstructuredSSRequest(_Timer_Default, ussdDataCodingScheme, ussdString, alertingPatter, msisdn);
    }


    @Override
    public Long addRegisterSSRequest(SSCode ssCode, BasicServiceCode basicService, AddressString forwardedToNumber, ISDNAddressString forwardedToSubaddress,
            Integer noReplyConditionTime, EMLPPPriority defaultPriority, Integer nbrUser, ISDNAddressString longFTNSupported) throws MAPException {
        return this.addRegisterSSRequest(_Timer_Default, ssCode, basicService, forwardedToNumber, forwardedToSubaddress, noReplyConditionTime, defaultPriority,
                nbrUser, longFTNSupported);
    }

    @Override
    public Long addRegisterSSRequest(int customInvokeTimeout, SSCode ssCode, BasicServiceCode basicService, AddressString forwardedToNumber,
            ISDNAddressString forwardedToSubaddress, Integer noReplyConditionTime, EMLPPPriority defaultPriority, Integer nbrUser,
            ISDNAddressString longFTNSupported) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addRegisterSSRequest: must be networkFunctionalSsContext_V2");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.registerSS);
        invoke.setOperationCode(oc);

        RegisterSSRequestImpl req = new RegisterSSRequestImpl(ssCode, basicService, forwardedToNumber, forwardedToSubaddress, noReplyConditionTime,
                defaultPriority, nbrUser, longFTNSupported);
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
    public void addRegisterSSResponse(long invokeId, SSInfo ssInfo) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addRegisterSSResponse: must be networkFunctionalSsContext_V2");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.registerSS);
        resultLast.setOperationCode(oc);

        if (ssInfo != null) {
            RegisterSSResponseImpl req = new RegisterSSResponseImpl(ssInfo);
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
    public Long addEraseSSRequest(SSForBSCode ssForBSCode) throws MAPException {
        return this.addEraseSSRequest(_Timer_Default, ssForBSCode);
    }

    @Override
    public Long addEraseSSRequest(int customInvokeTimeout, SSForBSCode ssForBSCode) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addEraseSSRequest: must be networkFunctionalSsContext_V2");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.eraseSS);
        invoke.setOperationCode(oc);

        EraseSSRequestImpl req = new EraseSSRequestImpl(ssForBSCode);
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
    public void addEraseSSResponse(long invokeId, SSInfo ssInfo) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addEraseSSResponse: must be networkFunctionalSsContext_V2");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.eraseSS);
        resultLast.setOperationCode(oc);

        if (ssInfo != null) {
            EraseSSResponseImpl req = new EraseSSResponseImpl(ssInfo);
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
    public Long addActivateSSRequest(SSForBSCode ssForBSCode) throws MAPException {
        return this.addActivateSSRequest(_Timer_Default, ssForBSCode);
    }

    @Override
    public Long addActivateSSRequest(int customInvokeTimeout, SSForBSCode ssForBSCode) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addActivateSSRequest: must be networkFunctionalSsContext_V2");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.activateSS);
        invoke.setOperationCode(oc);

        ActivateSSRequestImpl req = new ActivateSSRequestImpl(ssForBSCode);
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
    public void addActivateSSResponse(long invokeId, SSInfo ssInfo) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addActivateSSResponse: must be networkFunctionalSsContext_V2");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.activateSS);
        resultLast.setOperationCode(oc);

        if (ssInfo != null) {
            ActivateSSResponseImpl req = new ActivateSSResponseImpl(ssInfo);
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
    public Long addDeactivateSSRequest(SSForBSCode ssForBSCode) throws MAPException {
        return this.addDeactivateSSRequest(_Timer_Default, ssForBSCode);
    }

    @Override
    public Long addDeactivateSSRequest(int customInvokeTimeout, SSForBSCode ssForBSCode) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addDeactivateSSRequest: must be networkFunctionalSsContext_V2");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.deactivateSS);
        invoke.setOperationCode(oc);

        DeactivateSSRequestImpl req = new DeactivateSSRequestImpl(ssForBSCode);
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
    public void addDeactivateSSResponse(long invokeId, SSInfo ssInfo) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addDeactivateSSResponse: must be networkFunctionalSsContext_V2");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.deactivateSS);
        resultLast.setOperationCode(oc);

        if (ssInfo != null) {
            DeactivateSSResponseImpl req = new DeactivateSSResponseImpl(ssInfo);
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
    public Long addInterrogateSSRequest(SSForBSCode ssForBSCode) throws MAPException {
        return this.addInterrogateSSRequest(_Timer_Default, ssForBSCode);
    }

    @Override
    public Long addInterrogateSSRequest(int customInvokeTimeout, SSForBSCode ssForBSCode) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addInterrogateSSRequest: must be networkFunctionalSsContext_V2");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.interrogateSS);
        invoke.setOperationCode(oc);

        InterrogateSSRequestImpl req = new InterrogateSSRequestImpl(ssForBSCode);
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
    public void addInterrogateSSResponse_SSStatus(long invokeId, SSStatus ssStatus) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addInterrogateSSResponse: must be networkFunctionalSsContext_V2");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.interrogateSS);
        resultLast.setOperationCode(oc);

        InterrogateSSResponseImpl req = new InterrogateSSResponseImpl(ssStatus);
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
    public void addInterrogateSSResponse_BasicServiceGroupList(long invokeId, ArrayList<BasicServiceCode> basicServiceGroupList) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addInterrogateSSResponse: must be networkFunctionalSsContext_V2");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.interrogateSS);
        resultLast.setOperationCode(oc);

        InterrogateSSResponseImpl req = new InterrogateSSResponseImpl(basicServiceGroupList, false);
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
    public void addInterrogateSSResponse_ForwardingFeatureList(long invokeId, ArrayList<ForwardingFeature> forwardingFeatureList) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addInterrogateSSResponse: must be networkFunctionalSsContext_V2");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.interrogateSS);
        resultLast.setOperationCode(oc);

        InterrogateSSResponseImpl req = new InterrogateSSResponseImpl(forwardingFeatureList);
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
    public void addInterrogateSSResponse_GenericServiceInfo(long invokeId, GenericServiceInfo genericServiceInfo) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addInterrogateSSResponse: must be networkFunctionalSsContext_V2");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.interrogateSS);
        resultLast.setOperationCode(oc);

        InterrogateSSResponseImpl req = new InterrogateSSResponseImpl(genericServiceInfo);
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
    public Long addGetPasswordRequest(Long linkedId, GuidanceInfo guidanceInfo) throws MAPException {
        return this.addGetPasswordRequest(_Timer_Default, linkedId, guidanceInfo);
    }

    @Override
    public Long addGetPasswordRequest(int customInvokeTimeout, Long linkedId, GuidanceInfo guidanceInfo) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addGetPasswordRequest: must be networkFunctionalSsContext_V2");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_ml);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.getPassword);
        invoke.setOperationCode(oc);

        GetPasswordRequestImpl req = new GetPasswordRequestImpl(guidanceInfo);
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
            invoke.setLinkedId(linkedId);
        } catch (TCAPException e) {
            throw new MAPException(e.getMessage(), e);
        }

        this.sendInvokeComponent(invoke);

        return invokeId;
    }

    @Override
    public void addGetPasswordResponse(long invokeId, Password password) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addGetPasswordResponse: must be networkFunctionalSsContext_V2");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.getPassword);
        resultLast.setOperationCode(oc);

        GetPasswordResponseImpl req = new GetPasswordResponseImpl(password);
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
    public Long addRegisterPasswordRequest(SSCode ssCode) throws MAPException {
        return this.addRegisterPasswordRequest(_Timer_Default, ssCode);
    }

    @Override
    public Long addRegisterPasswordRequest(int customInvokeTimeout, SSCode ssCode) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addRegisterPasswordRequest: must be networkFunctionalSsContext_V2");

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_m);
        else
            invoke.setTimeout(customInvokeTimeout);

        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.registerPassword);
        invoke.setOperationCode(oc);

        RegisterPasswordRequestImpl req = new RegisterPasswordRequestImpl(ssCode);
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
    public void addRegisterPasswordResponse(long invokeId, Password password) throws MAPException {
        if ((this.getAppCntx().getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.getAppCntx().getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addRegisterPasswordResponse: must be networkFunctionalSsContext_V2");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.registerPassword);
        resultLast.setOperationCode(oc);

        RegisterPasswordResponseImpl req = new RegisterPasswordResponseImpl(password);
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


    public Long addProcessUnstructuredSSRequest(int customInvokeTimeout, CBSDataCodingScheme ussdDataCodingScheme,
            USSDString ussdString, AlertingPattern alertingPatter, ISDNAddressString msisdn) throws MAPException {

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(600000); // 10 minutes
        else
            invoke.setTimeout(customInvokeTimeout);

        // Operation Code
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.processUnstructuredSS_Request);
        invoke.setOperationCode(oc);

        ProcessUnstructuredSSRequestImpl req = new ProcessUnstructuredSSRequestImpl(ussdDataCodingScheme, ussdString,
                alertingPatter, msisdn);
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

    public void addProcessUnstructuredSSResponse(long invokeId, CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString)
            throws MAPException {
        Return returnResult = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

        returnResult.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.processUnstructuredSS_Request);
        returnResult.setOperationCode(oc);

        ProcessUnstructuredSSResponseImpl req = new ProcessUnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
        AsnOutputStream aos = new AsnOutputStream();
        req.encodeData(aos);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(req.getTagClass());
        p.setPrimitive(req.getIsPrimitive());
        p.setTag(req.getTag());
        p.setData(aos.toByteArray());
        returnResult.setParameter(p);

        this.sendReturnResultLastComponent((ReturnResultLast) returnResult);
    }

    public Long addUnstructuredSSRequest(CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString,
            AlertingPattern alertingPatter, ISDNAddressString msisdn) throws MAPException {
        return this.addUnstructuredSSRequest(_Timer_Default, ussdDataCodingScheme, ussdString, alertingPatter, msisdn);
    }

    public Long addUnstructuredSSRequest(int customInvokeTimeout, CBSDataCodingScheme ussdDataCodingScheme,
            USSDString ussdString, AlertingPattern alertingPatter, ISDNAddressString msisdn) throws MAPException {

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_ml);
        else
            invoke.setTimeout(customInvokeTimeout);

        // Operation Code
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.unstructuredSS_Request);
        invoke.setOperationCode(oc);

        UnstructuredSSRequestImpl req = new UnstructuredSSRequestImpl(ussdDataCodingScheme, ussdString, alertingPatter, msisdn);
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

    public Long addUnstructuredSSNotifyRequest(CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString,
            AlertingPattern alertingPatter, ISDNAddressString msisdn) throws MAPException {
        return this.addUnstructuredSSNotifyRequest(_Timer_Default, ussdDataCodingScheme, ussdString, alertingPatter, msisdn);
    }

    public Long addUnstructuredSSNotifyRequest(int customInvokeTimeout, CBSDataCodingScheme ussdDataCodingScheme,
            USSDString ussdString, AlertingPattern alertingPatter, ISDNAddressString msisdn) throws MAPException {

        Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
        if (customInvokeTimeout == _Timer_Default)
            invoke.setTimeout(_Timer_ml);
        else
            invoke.setTimeout(customInvokeTimeout);

        // Operation Code
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.unstructuredSS_Notify);
        invoke.setOperationCode(oc);

        if (ussdString != null) {
            UnstructuredSSRequestImpl req = new UnstructuredSSRequestImpl(ussdDataCodingScheme, ussdString, alertingPatter, msisdn);
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

    public void addUnstructuredSSResponse(long invokeId, CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString)
            throws MAPException {

        Return returnResult = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCResultLastRequest();

        returnResult.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = TcapFactory.createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.unstructuredSS_Request);
        returnResult.setOperationCode(oc);

        if (ussdString != null) {
            UnstructuredSSResponseImpl req = new UnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
            AsnOutputStream aos = new AsnOutputStream();
            req.encodeData(aos);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(req.getTagClass());
            p.setPrimitive(req.getIsPrimitive());
            p.setTag(req.getTag());
            p.setData(aos.toByteArray());
            returnResult.setParameter(p);
        }

        this.sendReturnResultLastComponent((ReturnResultLast) returnResult);
    }

    public void addUnstructuredSSNotifyResponse(long invokeId) throws MAPException {

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();

        resultLast.setInvokeId(invokeId);

        // we need not Operation Code because no answer

        this.sendReturnResultLastComponent(resultLast);
    }

}
