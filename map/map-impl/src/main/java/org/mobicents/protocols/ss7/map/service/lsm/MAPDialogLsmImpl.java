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

package org.mobicents.protocols.ss7.map.service.lsm;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPDialogImpl;
import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextName;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPServiceBase;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.mobicents.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.mobicents.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.mobicents.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * @author amit bhayani
 *
 */
public class MAPDialogLsmImpl extends MAPDialogImpl implements MAPDialogLsm {

    /**
     * @param appCntx
     * @param tcapDialog
     * @param mapProviderImpl
     * @param mapService
     * @param origReference
     * @param destReference
     */
    protected MAPDialogLsmImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
            MAPServiceBase mapService, AddressString origReference, AddressString destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addProvideSubscriberLocationRequest
     * (org.mobicents.protocols.ss7.map.api.service.lsm.LocationType,
     * org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString,
     * org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID, java.lang.Boolean,
     * org.mobicents.protocols.ss7.map.api.primitives.IMSI, org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString,
     * org.mobicents.protocols.ss7.map.api.primitives.LMSI, org.mobicents.protocols.ss7.map.api.primitives.IMEI,
     * java.lang.Integer, org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer, java.util.BitSet, java.lang.Byte,
     * java.lang.Integer, org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword,
     * org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck,
     * org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo, byte[])
     */
    public Long addProvideSubscriberLocationRequest(LocationType locationType, ISDNAddressString mlcNumber,
            LCSClientID lcsClientID, boolean privacyOverride, IMSI imsi, ISDNAddressString msisdn, LMSI lmsi, IMEI imei,
            LCSPriority lcsPriority, LCSQoS lcsQoS, MAPExtensionContainer extensionContainer,
            SupportedGADShapes supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
            LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo, GSNAddress hgmlcAddress,
            boolean moLrShortCircuitIndicator, PeriodicLDRInfo periodicLDRInfo, ReportingPLMNList reportingPLMNList)
            throws MAPException {
        return this.addProvideSubscriberLocationRequest(_Timer_Default, locationType, mlcNumber, lcsClientID, privacyOverride,
                imsi, msisdn, lmsi, imei, lcsPriority, lcsQoS, extensionContainer, supportedGADShapes, lcsReferenceNumber,
                lcsServiceTypeID, lcsCodeword, lcsPrivacyCheck, areaEventInfo, hgmlcAddress, moLrShortCircuitIndicator,
                periodicLDRInfo, reportingPLMNList);
    }

    public Long addProvideSubscriberLocationRequest(int customInvokeTimeout, LocationType locationType,
            ISDNAddressString mlcNumber, LCSClientID lcsClientID, boolean privacyOverride, IMSI imsi, ISDNAddressString msisdn,
            LMSI lmsi, IMEI imei, LCSPriority lcsPriority, LCSQoS lcsQoS, MAPExtensionContainer extensionContainer,
            SupportedGADShapes supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
            LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo, GSNAddress hgmlcAddress,
            boolean moLrShortCircuitIndicator, PeriodicLDRInfo periodicLDRInfo, ReportingPLMNList reportingPLMNList)
            throws MAPException {

        if (locationType == null || mlcNumber == null) {
            throw new MAPException(
                    "addProvideSubscriberLocationRequest: Mandatroy parameters locationType or mlcNumber cannot be null");
        }

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationSvcEnquiryContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "addProvideSubscriberLocationRequest: Bad application context name for addProvideSubscriberLocationRequest: must be locationSvcEnquiryContext_V3");

        try {
            Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
            if (customInvokeTimeout == _Timer_Default)
                invoke.setTimeout(_Timer_ml);
            else
                invoke.setTimeout(customInvokeTimeout);

            // Operation Code
            OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
            oc.setLocalOperationCode((long) MAPOperationCode.provideSubscriberLocation);
            invoke.setOperationCode(oc);

            ProvideSubscriberLocationRequestImpl req = new ProvideSubscriberLocationRequestImpl(locationType, mlcNumber,
                    lcsClientID, privacyOverride, imsi, msisdn, lmsi, imei, lcsPriority, lcsQoS, extensionContainer,
                    supportedGADShapes, lcsReferenceNumber, lcsServiceTypeID, lcsCodeword, lcsPrivacyCheck, areaEventInfo,
                    hgmlcAddress, moLrShortCircuitIndicator, periodicLDRInfo, reportingPLMNList);

            AsnOutputStream asnOs = new AsnOutputStream();
            req.encodeData(asnOs);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(req.getTagClass());
            p.setPrimitive(req.getIsPrimitive());
            p.setTag(req.getTag());
            p.setData(asnOs.toByteArray());

            invoke.setParameter(p);

            Long invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);

            this.sendInvokeComponent(invoke);

            return invokeId;
        } catch (TCAPException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addProvideSubscriberLocationResponse(long, byte[],
     * byte[], byte[], java.lang.Integer, byte[], org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer,
     * java.lang.Boolean, org.mobicents.protocols.ss7.map.api.service.lsm. CellGlobalIdOrServiceAreaIdOrLAI, java.lang.Boolean,
     * org.mobicents.protocols .ss7.map.api.service.lsm.AccuracyFulfilmentIndicator)
     */
    public void addProvideSubscriberLocationResponse(long invokeId, ExtGeographicalInformation locationEstimate,
            PositioningDataInformation geranPositioningData, UtranPositioningDataInfo utranPositioningData,
            Integer ageOfLocationEstimate, AddGeographicalInformation additionalLocationEstimate,
            MAPExtensionContainer extensionContainer, boolean deferredMTLRResponseIndicator,
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, boolean saiPresent,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate,
            boolean moLrShortCircuitIndicator, GeranGANSSpositioningData geranGANSSpositioningData,
            UtranGANSSpositioningData utranGANSSpositioningData, ServingNodeAddress targetServingNodeForHandover)
            throws MAPException {

        if (locationEstimate == null) {
            throw new MAPException("addProvideSubscriberLocationResponse: Mandatroy parameters locationEstimate cannot be null");
        }

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationSvcEnquiryContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addProvideSubscriberLocationResponse: must be locationSvcEnquiryContext_V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();
        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.provideSubscriberLocation);
        resultLast.setOperationCode(oc);

        ProvideSubscriberLocationResponseImpl resInd = new ProvideSubscriberLocationResponseImpl(locationEstimate,
                geranPositioningData, utranPositioningData, ageOfLocationEstimate, additionalLocationEstimate,
                extensionContainer, deferredMTLRResponseIndicator, cellGlobalIdOrServiceAreaIdOrLAI, saiPresent,
                accuracyFulfilmentIndicator, velocityEstimate, moLrShortCircuitIndicator, geranGANSSpositioningData,
                utranGANSSpositioningData, targetServingNodeForHandover);

        AsnOutputStream asnOs = new AsnOutputStream();
        resInd.encodeData(asnOs);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(resInd.getTagClass());
        p.setPrimitive(resInd.getIsPrimitive());
        p.setTag(resInd.getTag());
        p.setData(asnOs.toByteArray());

        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addSubscriberLocationReportRequestIndication
     * (org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent, org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID,
     * org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo, org.mobicents.protocols.ss7.map.api.dialog.IMSI,
     * org.mobicents.protocols.ss7.map.api.dialog.AddressString, org.mobicents.protocols.ss7.map.api.dialog.AddressString,
     * org.mobicents.protocols.ss7.map.api.dialog.AddressString, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, int, java.lang.String, org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData,
     * java.lang.String, org.mobicents.protocols.ss7.map.api.service.lsm. CellGlobalIdOrServiceAreaIdOrLAI, java.lang.String,
     * int, boolean, org.mobicents .protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator)
     */
    public Long addSubscriberLocationReportRequest(LCSEvent lcsEvent, LCSClientID lcsClientID, LCSLocationInfo lcsLocationInfo,
            ISDNAddressString msisdn, IMSI imsi, IMEI imei, ISDNAddressString naEsrd, ISDNAddressString naEsrk,
            ExtGeographicalInformation locationEstimate, Integer ageOfLocationEstimate,
            SLRArgExtensionContainer slrArgExtensionContainer, AddGeographicalInformation addLocationEstimate,
            DeferredmtlrData deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformation geranPositioningData,
            UtranPositioningDataInfo utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai,
            GSNAddress hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate, Integer sequenceNumber,
            PeriodicLDRInfo periodicLDRInfo, boolean moLrShortCircuitIndicator,
            GeranGANSSpositioningData geranGANSSpositioningData, UtranGANSSpositioningData utranGANSSpositioningData,
            ServingNodeAddress targetServingNodeForHandover) throws MAPException {
        return this.addSubscriberLocationReportRequest(_Timer_Default, lcsEvent, lcsClientID, lcsLocationInfo, msisdn, imsi,
                imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate, slrArgExtensionContainer, addLocationEstimate,
                deferredmtlrData, lcsReferenceNumber, geranPositioningData, utranPositioningData, cellIdOrSai, hgmlcAddress,
                lcsServiceTypeID, saiPresent, pseudonymIndicator, accuracyFulfilmentIndicator, velocityEstimate,
                sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGANSSpositioningData,
                utranGANSSpositioningData, targetServingNodeForHandover);
    }

    public Long addSubscriberLocationReportRequest(int customInvokeTimeout, LCSEvent lcsEvent, LCSClientID lcsClientID,
            LCSLocationInfo lcsLocationInfo, ISDNAddressString msisdn, IMSI imsi, IMEI imei, ISDNAddressString naEsrd,
            ISDNAddressString naEsrk, ExtGeographicalInformation locationEstimate, Integer ageOfLocationEstimate,
            SLRArgExtensionContainer slrArgExtensionContainer, AddGeographicalInformation addLocationEstimate,
            DeferredmtlrData deferredmtlrData, Integer lcsReferenceNumber, PositioningDataInformation geranPositioningData,
            UtranPositioningDataInfo utranPositioningData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai,
            GSNAddress hgmlcAddress, Integer lcsServiceTypeID, boolean saiPresent, boolean pseudonymIndicator,
            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, VelocityEstimate velocityEstimate, Integer sequenceNumber,
            PeriodicLDRInfo periodicLDRInfo, boolean moLrShortCircuitIndicator,
            GeranGANSSpositioningData geranGANSSpositioningData, UtranGANSSpositioningData utranGANSSpositioningData,
            ServingNodeAddress targetServingNodeForHandover) throws MAPException {

        if (lcsEvent == null || lcsClientID == null || lcsLocationInfo == null) {
            throw new MAPException("Mandatroy parameters lCSEvent, lCSClientID or lCSLocationInfo cannot be null");
        }
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationSvcEnquiryContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addSubscriberLocationReportRequest: must be locationSvcEnquiryContext_V3");

        try {
            Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
            if (customInvokeTimeout == _Timer_Default)
                invoke.setTimeout(_Timer_m);
            else
                invoke.setTimeout(customInvokeTimeout);

            // Operation Code
            OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
            oc.setLocalOperationCode((long) MAPOperationCode.subscriberLocationReport);
            invoke.setOperationCode(oc);

            SubscriberLocationReportRequestImpl req = new SubscriberLocationReportRequestImpl(lcsEvent, lcsClientID,
                    lcsLocationInfo, msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, ageOfLocationEstimate,
                    slrArgExtensionContainer, addLocationEstimate, deferredmtlrData, lcsReferenceNumber, geranPositioningData,
                    utranPositioningData, cellIdOrSai, hgmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator,
                    accuracyFulfilmentIndicator, velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator,
                    geranGANSSpositioningData, utranGANSSpositioningData, targetServingNodeForHandover);

            AsnOutputStream asnOs = new AsnOutputStream();
            req.encodeData(asnOs);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(req.getTagClass());
            p.setPrimitive(req.getIsPrimitive());
            p.setTag(req.getTag());
            p.setData(asnOs.toByteArray());

            invoke.setParameter(p);

            Long invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);

            this.sendInvokeComponent(invoke);

            return invokeId;
        } catch (TCAPException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addSubscriberLocationReportResponseIndication(long,
     * org.mobicents.protocols.ss7.map.api.dialog.MAPExtensionContainer,
     * org.mobicents.protocols.ss7.map.api.dialog.AddressString, org.mobicents.protocols.ss7.map.api.dialog.AddressString)
     */
    public void addSubscriberLocationReportResponse(long invokeId, ISDNAddressString naEsrd, ISDNAddressString naEsrk,
            MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationSvcEnquiryContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addSubscriberLocationReportResponse: must be locationSvcEnquiryContext_V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();
        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.subscriberLocationReport);
        resultLast.setOperationCode(oc);

        SubscriberLocationReportResponseImpl resInd = new SubscriberLocationReportResponseImpl(naEsrd, naEsrk,
                extensionContainer);

        AsnOutputStream asnOs = new AsnOutputStream();
        resInd.encodeData(asnOs);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(resInd.getTagClass());
        p.setPrimitive(resInd.getIsPrimitive());
        p.setTag(resInd.getTag());
        p.setData(asnOs.toByteArray());

        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addSendRoutingInforForLCSRequestIndication
     * (org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString,
     * org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer)
     */
    public Long addSendRoutingInfoForLCSRequest(ISDNAddressString mlcNumber, SubscriberIdentity targetMS,
            MAPExtensionContainer extensionContainer) throws MAPException {
        return this.addSendRoutingInfoForLCSRequest(_Timer_Default, mlcNumber, targetMS, extensionContainer);
    }

    public Long addSendRoutingInfoForLCSRequest(int customInvokeTimeout, ISDNAddressString mlcNumber,
            SubscriberIdentity targetMS, MAPExtensionContainer extensionContainer) throws MAPException {

        if (mlcNumber == null || targetMS == null) {
            throw new MAPException("Mandatroy parameters mlcNumber or targetMS cannot be null");
        }
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationSvcGatewayContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addSendRoutingInfoForLCSRequest: must be locationSvcGatewayContext_V3");

        try {
            Invoke invoke = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCInvokeRequest();
            if (customInvokeTimeout == _Timer_Default)
                invoke.setTimeout(_Timer_m);
            else
                invoke.setTimeout(customInvokeTimeout);

            // Operation Code
            OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
            oc.setLocalOperationCode((long) MAPOperationCode.sendRoutingInfoForLCS);
            invoke.setOperationCode(oc);

            SendRoutingInfoForLCSRequestImpl req = new SendRoutingInfoForLCSRequestImpl(mlcNumber, targetMS, extensionContainer);

            AsnOutputStream asnOs = new AsnOutputStream();
            req.encodeData(asnOs);

            Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
            p.setTagClass(req.getTagClass());
            p.setPrimitive(req.getIsPrimitive());
            p.setTag(req.getTag());
            p.setData(asnOs.toByteArray());

            invoke.setParameter(p);

            Long invokeId = this.tcapDialog.getNewInvokeId();
            invoke.setInvokeId(invokeId);

            this.sendInvokeComponent(invoke);

            return invokeId;
        } catch (TCAPException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm# addSendRoutingInforForLCSResponseIndication
     * (org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity,
     * org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo,
     * org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer, byte[], byte[], byte[], byte[])
     */
    public void addSendRoutingInfoForLCSResponse(long invokeId, SubscriberIdentity targetMS, LCSLocationInfo lcsLocationInfo,
            MAPExtensionContainer extensionContainer, GSNAddress vgmlcAddress, GSNAddress hGmlcAddress, GSNAddress pprAddress,
            GSNAddress additionalVGmlcAddress) throws MAPException {

        if (targetMS == null || lcsLocationInfo == null) {
            throw new MAPException("Mandatroy parameters targetMS or lcsLocationInfo cannot be null");
        }
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationSvcGatewayContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addSendRoutingInfoForLCSResponse: must be locationSvcGatewayContext_V3");

        ReturnResultLast resultLast = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCResultLastRequest();
        resultLast.setInvokeId(invokeId);

        // Operation Code
        OperationCode oc = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createOperationCode();
        oc.setLocalOperationCode((long) MAPOperationCode.sendRoutingInfoForLCS);
        resultLast.setOperationCode(oc);

        SendRoutingInfoForLCSResponseImpl resInd = new SendRoutingInfoForLCSResponseImpl(targetMS, lcsLocationInfo,
                extensionContainer, vgmlcAddress, hGmlcAddress, pprAddress, additionalVGmlcAddress);

        AsnOutputStream asnOs = new AsnOutputStream();
        resInd.encodeData(asnOs);

        Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
        p.setTagClass(resInd.getTagClass());
        p.setPrimitive(resInd.getIsPrimitive());
        p.setTag(resInd.getTag());
        p.setData(asnOs.toByteArray());

        resultLast.setParameter(p);

        this.sendReturnResultLastComponent(resultLast);

    }
}
