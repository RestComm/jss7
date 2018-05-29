/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2018, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.restcomm.protocols.ss7.tools.simulator.tests.lcs;

import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;

import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.IMEI;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;

import org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsm;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.restcomm.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientName;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSRequestorID;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;
import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.restcomm.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.lsm.AddGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.lsm.ServingNodeAddress;
import org.restcomm.protocols.ss7.map.api.service.lsm.PositioningDataInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.GeranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranPositioningDataInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.UtranGANSSpositioningData;
import org.restcomm.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.TerminationCause;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;
import org.restcomm.protocols.ss7.map.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;

import org.restcomm.protocols.ss7.map.service.lsm.DeferredLocationEventTypeImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientNameImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ExtGeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.service.lsm.VelocityEstimateImpl;
import org.restcomm.protocols.ss7.map.service.lsm.DeferredmtlrDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.AdditionalNumberImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientIDImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSCodewordImpl;
import org.restcomm.protocols.ss7.map.service.lsm.LCSPrivacyCheckImpl;
import org.restcomm.protocols.ss7.map.service.lsm.UtranPositioningDataInfoImpl;
import org.restcomm.protocols.ss7.map.service.lsm.GeranGANSSpositioningDataImpl;
import org.restcomm.protocols.ss7.map.service.lsm.UtranGANSSpositioningDataImpl;

import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SupportedLCSCapabilitySetsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.tools.simulator.Stoppable;
import org.restcomm.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.restcomm.protocols.ss7.tools.simulator.common.TesterBase;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.tools.simulator.level3.MapMan;
import org.restcomm.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.restcomm.protocols.ss7.tools.simulator.management.TesterHostInterface;

import java.nio.charset.Charset;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 * @author <a href="mailto:falonso@csc.om"> Fernando Alonso </a>
 */
public class TestLcsServerMan extends TesterBase implements TestLcsServerManMBean, Stoppable, MAPServiceLsmListener {

    private static Logger logger = Logger.getLogger(TestLcsServerMan.class);

    public static String SOURCE_NAME = "TestLcsServerMan";
    private final String name;
    private MapMan mapMan;
    private boolean isStarted = false;
    private int countMapLcsReq = 0;
    private int countMapLcsResp = 0;
    private String currentRequestDef = "";
    private MAPProvider mapProvider;
    private MAPServiceLsm mapServiceLsm;
    private MAPParameterFactory mapParameterFactory;

    public TestLcsServerMan(String name) {
        super(SOURCE_NAME);
        this.name = name;
        this.isStarted = false;
    }

    public boolean start() {

        this.mapProvider = this.mapMan.getMAPStack().getMAPProvider();
        this.mapServiceLsm = mapProvider.getMAPServiceLsm();
        this.mapParameterFactory = mapProvider.getMAPParameterFactory();

        mapServiceLsm.acivate();
        mapServiceLsm.addMAPServiceListener(this);
        mapProvider.addMAPDialogListener(this);

        isStarted = true;
        this.countMapLcsReq = 0;
        this.countMapLcsResp = 0;
        return true;
    }

    public void setTesterHost(TesterHostInterface testerHost) {
        this.testerHost = testerHost;
    }

    public void setMapMan(MapMan val) {
        this.mapMan = val;
    }

    @Override
    public String getState() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(SOURCE_NAME);
        sb.append(": ");
        sb.append("<br>Count: countMapLcsReq-");
        sb.append(countMapLcsReq);
        sb.append(", countMapLcsResp-");
        sb.append(countMapLcsResp);
        sb.append("</html>");
        return sb.toString();
    }

    @Override
    public void execute() {
    }

    @Override
    public void stop() {
        isStarted = false;
        mapProvider.getMAPServiceLsm().deactivate();
        mapProvider.getMAPServiceLsm().removeMAPServiceListener(this);
        mapProvider.removeMAPDialogListener(this);
        this.testerHost.sendNotif(SOURCE_NAME, "LCS Client has been stopped", "", Level.INFO);
    }

    //***************************//
    //**** SRIforLCS methods ***//
    //*************************//
    @Override
    public String performSendRoutingInfoForLCSResponse() {
        if (!isStarted) {
            return "The tester is not started";
        }

        return sendRoutingInfoForLCSResponse();
    }

    public String sendRoutingInfoForLCSResponse() {

        return "sendRoutingInfoForLCSResponse called automatically";
    }

    public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest sendRoutingInforForLCSRequest) {

        logger.debug("\nonSendRoutingInfoForLCSRequest");
        if (!isStarted)
            return;

        this.countMapLcsReq++;

        MAPDialogLsm curDialog = sendRoutingInforForLCSRequest.getMAPDialog();

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: ProvideSubscriberLocationRequest",
                createSRIforLCSReqData(curDialog.getLocalDialogId(),
                        sendRoutingInforForLCSRequest.getMLCNumber(),
                        sendRoutingInforForLCSRequest.getTargetMS()),
                Level.INFO
        );

        try {
            MAPParameterFactoryImpl mapFactory = new MAPParameterFactoryImpl();
            SubscriberIdentity msisdn = sendRoutingInforForLCSRequest.getTargetMS();
            ISDNAddressString mlcNumber = sendRoutingInforForLCSRequest.getMLCNumber();
            String mscAddress = "5982123007";
            String sgsnAddress = "5982123009";
            ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, mscAddress);
            ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, sgsnAddress);
            AdditionalNumber additionalNumber = new AdditionalNumberImpl(mscNumber, sgsnNumber);
            String lmsiStr = "4321";
            byte[] Lmsi = lmsiStr.getBytes();
            LMSI lmsi = mapFactory.createLMSI(Lmsi);
            Boolean gprsNodeIndicator = false;
            boolean lcsCapabilitySetRelease98_99 = true;
            boolean lcsCapabilitySetRelease4 = true;
            boolean lcsCapabilitySetRelease5 = true;
            boolean lcsCapabilitySetRelease6 = true;
            boolean lcsCapabilitySetRelease7 = false;
            SupportedLCSCapabilitySets supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                    lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
            lcsCapabilitySetRelease7 = true;
            SupportedLCSCapabilitySets additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                    lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
            MAPExtensionContainer mapExtensionContainer = null;
            byte[] mmeNom = new BigInteger("00112233445566778899", 16).toByteArray();
            DiameterIdentity mmeName = new DiameterIdentityImpl(mmeNom);
            byte[] aaaSN = new BigInteger("0011223344556677889900", 16).toByteArray();
            DiameterIdentity aaaServerName = new DiameterIdentityImpl(aaaSN);
            byte[] visitedGmlcAddress = new BigInteger("112233445500", 16).toByteArray();
            GSNAddress vGmlcAddress = new GSNAddressImpl(visitedGmlcAddress);
            byte[] homeGmlcAddress = new BigInteger("11223344556677", 16).toByteArray();
            GSNAddress hGmlcAddress = new GSNAddressImpl(homeGmlcAddress);
            byte[] pivacyProfileRegisterAddress = new BigInteger("112233445566", 16).toByteArray();
            GSNAddress pprAddress = new GSNAddressImpl(pivacyProfileRegisterAddress);
            byte[] addVGmlcAddress = new BigInteger("5522334455", 16).toByteArray();
            GSNAddress additionalVGmlcAddress = new GSNAddressImpl(addVGmlcAddress);

            LCSLocationInfo lcsLocationInfo = mapFactory.createLCSLocationInfo(mscNumber, lmsi, mapExtensionContainer, gprsNodeIndicator,
                    null, supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName);

            curDialog.addSendRoutingInfoForLCSResponse(sendRoutingInforForLCSRequest.getInvokeId(),
                    msisdn, lcsLocationInfo, mapExtensionContainer, vGmlcAddress, hGmlcAddress, pprAddress, additionalVGmlcAddress);

            logger.debug("\nset addSendRoutingForLCSResponse");
            curDialog.close(false);
            logger.debug("\naddSendRoutingForLCSResponse sent");
            this.countMapLcsResp++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SendRoutingForLCSResponse",
                    createSRIforLCSResData(curDialog.getLocalDialogId(), mscNumber), Level.INFO);

        } catch (MAPException me) {
            logger.debug("Failed building SendRoutingInfoForLCS response " + me.toString());
        }

    }

    private String createSRIforLCSReqData(long dialogId, ISDNAddressString mlcNumber, SubscriberIdentity msisdn) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", mlcNumber=\"");
        sb.append(mlcNumber.getAddress());
        sb.append(", MSISDN/IMSI=\"");
        sb.append(msisdn.getMSISDN().getAddress());
        sb.append("\"");
        return sb.toString();
    }


    private String createSRIforLCSResData(long dialogId, ISDNAddressString networkNodeNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", networkNodeNumber=\"");
        sb.append(networkNodeNumber.getAddress());
        sb.append("\"");
        return sb.toString();
    }

    public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse sendRoutingInforForLCSResponseIndication) {
        logger.debug("\nonSendRoutingInfoForLCSResponse");
        this.countMapLcsResp++;
        MAPDialogLsm curDialog = sendRoutingInforForLCSResponseIndication.getMAPDialog();
        this.testerHost.sendNotif(SOURCE_NAME,
                "Rcvd: SendRoutingInfoForLCSResponse", this
                        .createSRIforLCSResData(
                                curDialog.getLocalDialogId(),
                                sendRoutingInforForLCSResponseIndication.getLCSLocationInfo().getNetworkNodeNumber()),
                Level.INFO);

    }

    //*********************//
    //**** PSL methods ***//
    //*******************//
    @Override
    public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest provideSubscriberLocationRequest) {

        logger.debug("\nonProvideSubscriberLocationRequest");
        if (!isStarted)
            return;

        this.countMapLcsReq++;

        MAPDialogLsm curDialog = provideSubscriberLocationRequest.getMAPDialog();

        MAPParameterFactoryImpl mapFactory = new MAPParameterFactoryImpl();
        int cbsDataCodingSchemeCode = 15;
        CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(cbsDataCodingSchemeCode);
        String ussdLcsString = "*911#";
        Charset gsm8Charset = Charset.defaultCharset();
        ISDNAddressString externalAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "444567");
        ISDNAddressString msisdn;
        MAPExtensionContainer mapExtensionContainer = null;
        LCSClientExternalID lcsClientExternalID = new LCSClientExternalIDImpl(externalAddress, mapExtensionContainer);
        LCSClientInternalID lcsClientInternalID = LCSClientInternalID.oandMHPLMN;
        USSDString ussdString = null;
        try {
            ussdString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
        } catch (MAPException e) {
            e.printStackTrace();
        }
        LCSFormatIndicator lcsFormatIndicator = LCSFormatIndicator.url;
        PrivacyCheckRelatedAction callSessionUnrelated = PrivacyCheckRelatedAction.allowedWithNotification;
        PrivacyCheckRelatedAction callSessionRelated = PrivacyCheckRelatedAction.allowedIfNoResponse;

        LCSPrivacyCheck lcsPrivacyCheck;
        LCSClientID lcsClientID;
        LCSCodeword lcsCodeword;
        IMSI imsi;
        IMEI imei;

        if (provideSubscriberLocationRequest.getLCSClientID() == null) {
            String clientName = "219023";
            LCSClientName lcsClientName = new LCSClientNameImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
            AddressString lcsClientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, clientName);
            byte[] apn = new BigInteger("6763a20b0890", 16).toByteArray();
            APN lcsAPN = new APNImpl(apn);
            lcsClientID = new LCSClientIDImpl(LCSClientType.valueAddedServices, lcsClientExternalID, lcsClientInternalID, lcsClientName, lcsClientDialedByMS, lcsAPN, null);
        } else {
            lcsClientID = provideSubscriberLocationRequest.getLCSClientID();
        }

        if (provideSubscriberLocationRequest.getLCSCodeword() == null) {
            lcsCodeword = new LCSCodewordImpl(cbsDataCodingScheme, ussdString);
        } else {
            lcsCodeword = provideSubscriberLocationRequest.getLCSCodeword();
        }

        if (provideSubscriberLocationRequest.getIMSI() == null) {
            imsi = new IMSIImpl("124356871012345");
        } else {
            imsi = provideSubscriberLocationRequest.getIMSI();
        }

        if (provideSubscriberLocationRequest.getMSISDN() == null) {
            msisdn = new ISDNAddressStringImpl(AddressNature.international_number,
                    NumberingPlan.ISDN, "59899077937");
        } else {
            msisdn = provideSubscriberLocationRequest.getMSISDN();
        }

        if (provideSubscriberLocationRequest.getIMEI() == null) {
            imei = new IMEIImpl("01171400466105");
        } else {
            imei = provideSubscriberLocationRequest.getIMEI();
        }

        if (provideSubscriberLocationRequest.getLCSPrivacyCheck() == null) {
            lcsPrivacyCheck = new LCSPrivacyCheckImpl(callSessionUnrelated, callSessionRelated);
        } else {
            lcsPrivacyCheck = provideSubscriberLocationRequest.getLCSPrivacyCheck();
        }

        logger.info("\n\nDialog Id=" + curDialog);
        logger.info("\n\nLocal Dialog Id=" + curDialog.getLocalDialogId());
        logger.info("\n\nLocation Type=" + provideSubscriberLocationRequest.getLocationType());
        logger.info("\n\nMLC Number=" + provideSubscriberLocationRequest.getMlcNumber());
        logger.info("\n\nLCS Client ID=" + lcsClientID);
        logger.info("\n\nIMSI=" + imsi);
        logger.info("\n\nMSISDN=" + msisdn);
        logger.info("\n\nLMSI=" + provideSubscriberLocationRequest.getLMSI());
        logger.info("\n\nLCS Priority=" + provideSubscriberLocationRequest.getLCSPriority());
        logger.info("\n\nLCS QoS=" + provideSubscriberLocationRequest.getLCSQoS());
        logger.info("\n\nIMEI=" + imei);
        logger.info("\n\nLCS Reference Number=" + provideSubscriberLocationRequest.getLCSReferenceNumber());
        logger.info("\n\nLCS Service Type ID=" + provideSubscriberLocationRequest.getLCSServiceTypeID());
        logger.info("\n\nLCS Codeword=" + lcsCodeword);
        logger.info("\n\nLCS Privacy Check=" + lcsPrivacyCheck);
        logger.info("\n\nArea Event Info=" + provideSubscriberLocationRequest.getAreaEventInfo());
        logger.info("\n\nH-GMLC Address=" + provideSubscriberLocationRequest.getHGMLCAddress());
        logger.info("\n\nMO LR Short Circuit Indicator=" + provideSubscriberLocationRequest.getMoLrShortCircuitIndicator());
        logger.info("\n\nPeriodic LDR Info=" + provideSubscriberLocationRequest.getPeriodicLDRInfo());

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: ProvideSubscriberLocationRequest",
                createPSLRequestData(curDialog.getLocalDialogId(),
                        provideSubscriberLocationRequest.getLocationType(),
                        provideSubscriberLocationRequest.getMlcNumber(),
                        lcsClientID,
                        imsi,
                        msisdn,
                        provideSubscriberLocationRequest.getLMSI(),
                        provideSubscriberLocationRequest.getLCSPriority(),
                        provideSubscriberLocationRequest.getLCSQoS(),
                        imei,
                        provideSubscriberLocationRequest.getLCSReferenceNumber(),
                        provideSubscriberLocationRequest.getLCSServiceTypeID(),
                        lcsCodeword,
                        lcsPrivacyCheck,
                        provideSubscriberLocationRequest.getAreaEventInfo(),
                        provideSubscriberLocationRequest.getHGMLCAddress(),
                        provideSubscriberLocationRequest.getMoLrShortCircuitIndicator(),
                        provideSubscriberLocationRequest.getPeriodicLDRInfo()
                ), Level.INFO);

        PositioningDataInformation geranPositioningData = null;
        Integer ageOfLocationEstimate = 0;
        AddGeographicalInformation additionalLocationEstimate = null;
        MAPExtensionContainer extensionContainer = null;
        boolean deferredMTLRResponseIndicator = true;
        byte[] cidOrSaiFixedLength = new BigInteger("34970120704321", 16).toByteArray();
        CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(cidOrSaiFixedLength);
        CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
        boolean saiPresent = false;
        ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "598990192837");
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "598990192837");
        byte[] utranData = new BigInteger("43210987654321", 16).toByteArray();
        UtranPositioningDataInfo utranPositioningDataInfo = new UtranPositioningDataInfoImpl(utranData);
        AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.requestedAccuracyFulfilled;
        String velStr = "1234567";
        byte[] velEstimate = velStr.getBytes();
        VelocityEstimate velocityEstimate = new VelocityEstimateImpl(velEstimate);
        boolean moLrShortCircuitIndicator = true;
        byte[] gGanss = new BigInteger("666601019999", 16).toByteArray();
        GeranGANSSpositioningData geranGANSSpositioningData = new GeranGANSSpositioningDataImpl(gGanss);
        byte[] uGanss = new BigInteger("777701019898", 16).toByteArray();
        UtranGANSSpositioningData utranGANSSpositioningData = new UtranGANSSpositioningDataImpl(uGanss);
        ServingNodeAddress targetServingNodeForHandover = mapFactory.createServingNodeAddressMscNumber(mscNumber);

        try {
            ExtGeographicalInformation locationEstimate = mapParameterFactory.createExtGeographicalInformation_EllipsoidPoint(34.790000, -124.910000);

            curDialog.addProvideSubscriberLocationResponse(
                    provideSubscriberLocationRequest.getInvokeId(),
                    locationEstimate,
                    geranPositioningData,
                    utranPositioningDataInfo,
                    ageOfLocationEstimate,
                    additionalLocationEstimate,
                    extensionContainer,
                    deferredMTLRResponseIndicator,
                    cellGlobalIdOrServiceAreaIdOrLAI,
                    saiPresent,
                    accuracyFulfilmentIndicator,
                    velocityEstimate,
                    moLrShortCircuitIndicator,
                    geranGANSSpositioningData,
                    utranGANSSpositioningData,
                    targetServingNodeForHandover);

            logger.debug("\nset addProvideSubscriberLocationResponse");
            curDialog.close(false);
            logger.debug("\naddProvideSubscriberLocationResponse sent");
            this.countMapLcsResp++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: ProvideSubscriberLocationResponse",
                    createPSLResponse(curDialog.getLocalDialogId(),
                            locationEstimate), Level.INFO);

        } catch (MAPException me) {
            logger.debug("Failed building  ProvideSubscriberLocation response " + me.toString());
        }
    }

    private String createPSLResponse(
            long dialogId,
            ExtGeographicalInformation locationEstimate) {

        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId).append("\",\n ");
        sb.append("locationEstimate=\"");
        sb.append(locationEstimate).append("\"");
        sb.append(locationEstimate).append(", ");
        sb.append("\", latitude=\"");
        sb.append(locationEstimate.getLatitude()).append(", ");
        sb.append("\", longitude=\"");
        sb.append(locationEstimate.getLongitude()).append(", ");
        sb.append("\", altitude=\"");
        sb.append(locationEstimate.getAltitude()).append(", ");
        sb.append("\", locationEstimateConfidence=\"");
        sb.append(locationEstimate.getConfidence()).append(", ");
        sb.append("\", locationEstimateInnerRadius=\"");
        sb.append(locationEstimate.getInnerRadius()).append(", ");
        sb.append("\", locationEstimateTypeOfShape=\"");
        return sb.toString();
    }

    private String createPSLRequestData(long dialogId, LocationType locationType, ISDNAddressString mlcNumber, LCSClientID lcsClientID, IMSI imsi,
                                        ISDNAddressString msisdn, LMSI lmsi, LCSPriority lcsPriority, LCSQoS lcsQoS, IMEI imei, Integer lcsReferenceNumber,
                                        Integer lcsServiceTypeID, LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo,
                                        GSNAddress hgmlcAddress, boolean moLrShortCircuitIndicator, PeriodicLDRInfo periodicLDRInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId).append("\",\n ");
        sb.append("locationType=\"");
        sb.append(locationType).append("\",\n ");
        sb.append("locationEstimateType=\"");
        sb.append(locationType.getLocationEstimateType().getType()).append("\",\n ");
        sb.append("deferredLocationEventType=\"");
        sb.append(locationType.getDeferredLocationEventType()).append("\",\n ");
        sb.append("mlcNumber=\"");
        sb.append(mlcNumber.getAddress()).append("\",\n ");
        sb.append("lcsClientID=\"");
        sb.append(lcsClientID).append("\",\n ");
        sb.append("IMSI=\"");
        sb.append(imsi.getData()).append("\",\n ");
        sb.append("MSISDIN=\"");
        sb.append(msisdn.getAddress()).append("\",\n ");
        sb.append("LMSI=\"");
        sb.append(lmsi).append("\",\n ");
        sb.append("lcsPriority=\"");
        sb.append(lcsPriority).append("\",\n ");
        sb.append("lcsQos=\"");
        sb.append(lcsQoS).append("\",\n ");
        sb.append("lcsQosHorizontalAccuracy=\"");
        sb.append(lcsQoS.getHorizontalAccuracy().intValue()).append("\",\n ");
        sb.append("lcsQosVerticalAccuracy=\"");
        sb.append(lcsQoS.getVerticalAccuracy().intValue()).append("\",\n ");
        sb.append("lcsQosResponseTimeCategory=\"");
        sb.append(lcsQoS.getResponseTime().getResponseTimeCategory()).append("\",\n ");
        sb.append("lcsQosVerticalCoordinateRequest=\"");
        sb.append(lcsQoS.getVerticalCoordinateRequest()).append("\",\n ");
        sb.append("IMEI=\"");
        sb.append(imei).append("\",\n ");
        sb.append("lcsReferenceNumber=\"");
        sb.append(lcsReferenceNumber).append("\",\n ");
        sb.append("lcsServiceTypeID=\"");
        sb.append(lcsServiceTypeID).append("\",\n ");
        sb.append("lcsCodeword=\"");
        sb.append(lcsCodeword).append("\",\n ");
        sb.append("lcsPrivacyCheck=\"");
        sb.append(lcsPrivacyCheck).append("\",\n ");
        sb.append("areaEventInfo=\"");
        sb.append(areaEventInfo).append("\",\n ");
        sb.append("areaId=\"");
        sb.append(areaEventInfo.getAreaDefinition().getAreaList().get(0)).append("\",\n ");
        sb.append("occurrenceInfo=\"");
        sb.append(areaEventInfo.getOccurrenceInfo().getInfo()).append("\",\n ");
        sb.append("intervalTime=\"");
        sb.append(areaEventInfo.getIntervalTime()).append("\",\n ");
        sb.append("hgmlcAddress=\"");
        sb.append(hgmlcAddress).append("\",\n ");
        sb.append("moLrShortCircuitIndicator=\"");
        sb.append(moLrShortCircuitIndicator).append("\",\n ");
        sb.append("periodicLDRInfo=\"");
        sb.append(periodicLDRInfo);
        return sb.toString();
    }

    public void onProvideSubscriberLocationResponse(
            ProvideSubscriberLocationResponse provideSubscriberLocationResponse) {

        logger.debug("onProvideSubscriberLocationResponse");

        MAPDialogLsm curDialog = provideSubscriberLocationResponse.getMAPDialog();

        this.countMapLcsResp++;
        this.testerHost.sendNotif(SOURCE_NAME,
                "Rcvd: ProvideSubscriberLocationResponse", this
                        .createPSLResponse(
                                curDialog.getLocalDialogId(),
                                provideSubscriberLocationResponse.getLocationEstimate()), Level.INFO);
    }

    //*********************//
    //**** SLR methods ***//
    //*******************//
    @Override
    public String performSubscriberLocationReportRequest() {
        if (!isStarted) {
            return "The tester is not started";
        }

        return subscriberLocationReportRequest();
    }

    private String subscriberLocationReportRequest() {
        if (mapProvider == null) {
            return "mapProvider is null";
        }

        try {
            // LSM dialog creation
            MAPApplicationContext appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.locationSvcEnquiryContext,
                    MAPApplicationContextVersion.version3);
            AddressString origReference = null;
            AddressString destReference = null;
            MAPDialogLsm mapDialogLsm = mapServiceLsm.createNewDialog(appCnt, this.mapMan.createOrigAddress(), origReference,
                    this.mapMan.createDestAddress(), destReference);
            logger.debug("MAPDialogLsm Created");
            TestLcsServerConfigurationData configData = this.testerHost.getConfigurationData().getTestLcsServerConfigurationData();

            // SLR Mandatory parameters LCSEvent, LCSClientID & Network Node Number
            MAPParameterFactoryImpl mapFactory = new MAPParameterFactoryImpl();
            LCSEvent lcsEvent = this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLCSEvent();

            LCSClientExternalID lcsClientExternalID = null;
            LCSClientInternalID lcsClientInternalID = LCSClientInternalID.anonymousLocation;
            String clientName = "340012";
            int cbsDataCodingSchemeCode = 15;
            CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(cbsDataCodingSchemeCode);
            String ussdLcsString = "*911#";
            Charset gsm8Charset = Charset.defaultCharset();
            USSDString ussdString = new USSDStringImpl(ussdLcsString, cbsDataCodingScheme, gsm8Charset);
            LCSFormatIndicator lcsFormatIndicator = LCSFormatIndicator.url;
            LCSClientName lcsClientName = new LCSClientNameImpl(cbsDataCodingScheme, ussdString, lcsFormatIndicator);
            AddressString lcsClientDialedByMS = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, clientName);
            byte[] apn = new BigInteger("8877665544", 16).toByteArray();
            APN lcsAPN = new APNImpl(apn);
            LCSRequestorID lcsRequestorID = null;
            LCSClientID lcsClientID = mapParameterFactory.createLCSClientID(configData.getLcsClientType(), lcsClientExternalID, lcsClientInternalID,
                    lcsClientName, lcsClientDialedByMS, lcsAPN, lcsRequestorID);

            ISDNAddressString networkNodeNumber = mapParameterFactory.createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAddressNature(),
                    this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNumberingPlanType(),
                    getNetworkNodeNumber());

            // SLR TC-user optional parameters
            IMEI imei = mapParameterFactory.createIMEI(getIMEI());

            String lmsiStr = "4321";
            byte[] Lmsi = lmsiStr.getBytes();
            LMSI lmsi = mapFactory.createLMSI(Lmsi);

            // LSR Conditional parameters
            IMSI imsi = mapParameterFactory.createIMSI(getIMSI());

            ISDNAddressString msisdn = mapParameterFactory.createISDNAddressString(
                    this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAddressNature(),
                    this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNumberingPlanType(),
                    getMSISDN());

            TypeOfShape typeOfShape = TypeOfShape.EllipsoidArc;
            Double latitude = 34.790000;
            Double longitude = -124.910000;
            Double uncertainty = 100.0;
            Double uncertaintySemiMajorAxis = 50.0;
            Double uncertaintySemiMinorAxis = 30.0;
            Double angleOfMajorAxis = 10.0;
            int confidence = 5;
            int altitude = 1000;
            Double uncertaintyAltitude = 200.0;
            int innerRadius = 40;
            Double uncertaintyRadius = 5.00;
            Double offsetAngle = 10.0;
            Double includedAngle = 30.0;
            ExtGeographicalInformation locationEstimate = new ExtGeographicalInformationImpl(typeOfShape, latitude, longitude, uncertainty, uncertaintySemiMajorAxis,
                    uncertaintySemiMinorAxis, angleOfMajorAxis, confidence, altitude, uncertaintyAltitude, innerRadius, uncertaintyRadius, offsetAngle, includedAngle);

            Boolean gprsNodeIndicator = false;

            GSNAddress hgmlcAddress = createGSNAddress(getHGMLCAddress());

            AccuracyFulfilmentIndicator accuracyFulfilmentIndicator = AccuracyFulfilmentIndicator.requestedAccuracyFulfilled;

            ISDNAddressString naEsrd = null;
            ISDNAddressString naEsrk = null;
            SLRArgExtensionContainer slrArgExtensionContainer = null;

            CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength = mapParameterFactory.createCellGlobalIdOrServiceAreaIdFixedLength(this.getMCC(), this.getMNC(), this.getLAC(), this.getCellId());
            CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai = mapParameterFactory.createCellGlobalIdOrServiceAreaIdOrLAI(cellGlobalIdOrServiceAreaIdFixedLength);

            MAPExtensionContainer extensionContainer = null;
            AddGeographicalInformation additionalLocationEstimate = null;

            PositioningDataInformation geranPositioningData = null;
            UtranPositioningDataInfo utranPositioningDataInfo = null;
            Integer lcsServiceTypeID = 1;
            Boolean saiPresent = false;
            Boolean pseudonymIndicator = false;
            String velStr = "1234567";
            byte[] velEstimate = velStr.getBytes();
            VelocityEstimate velocityEstimate = new VelocityEstimateImpl(velEstimate);
            Integer sequenceNumber = 0;
            //PeriodicLDRInfo periodicLDRInfo = mapParameterFactory.createPeriodicLDRInfo(getReportingAmount(), getReportingInterval());
            PeriodicLDRInfo periodicLDRInfo = mapParameterFactory.createPeriodicLDRInfo(10, 60);
            Boolean moLrShortCircuitIndicator = false;
            GeranGANSSpositioningData geranGANSSpositioningData = null;
            UtranGANSSpositioningData utranGANSSpositioningData = null;
            ServingNodeAddress targetNodeForHandover = null;
            AdditionalNumber additionalNumber = null;
            boolean lcsCapabilitySetRelease98_99 = true;
            boolean lcsCapabilitySetRelease4 = true;
            boolean lcsCapabilitySetRelease5 = true;
            boolean lcsCapabilitySetRelease6 = true;
            boolean lcsCapabilitySetRelease7 = true;
            SupportedLCSCapabilitySets supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                    lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
            SupportedLCSCapabilitySets additionalLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(lcsCapabilitySetRelease98_99, lcsCapabilitySetRelease4,
                    lcsCapabilitySetRelease5, lcsCapabilitySetRelease6, lcsCapabilitySetRelease7);
            byte[] mme = new BigInteger("8720c00a30a1743401000a", 16).toByteArray();
            DiameterIdentity mmeName = new DiameterIdentityImpl(mme);
            byte[] aaa = new BigInteger("8720c00a30a1743401101112", 16).toByteArray();
            DiameterIdentity aaaServerName = new DiameterIdentityImpl(aaa);

            LCSLocationInfo lcsLocationInfo = mapParameterFactory.createLCSLocationInfo(networkNodeNumber, lmsi, extensionContainer, gprsNodeIndicator,
                    additionalNumber, supportedLCSCapabilitySets, additionalLCSCapabilitySets, mmeName, aaaServerName);

            boolean msAvailable = false;
            boolean enteringIntoArea = false;
            boolean leavingFromArea = false;
            boolean beingInsideArea = true;
            DeferredLocationEventType deferredLocationEventType = new DeferredLocationEventTypeImpl(msAvailable, enteringIntoArea, leavingFromArea, beingInsideArea);
            TerminationCause terminationCause = TerminationCause.congestion;
            DeferredmtlrData deferredmtlrData = new DeferredmtlrDataImpl(deferredLocationEventType, terminationCause, lcsLocationInfo);

            ReportingPLMNList reportingPLMNList = null;

            mapDialogLsm.addSubscriberLocationReportRequest(lcsEvent, lcsClientID, lcsLocationInfo,
                    msisdn, imsi, imei, naEsrd, naEsrk, locationEstimate, getAgeOfLocationEstimate(), slrArgExtensionContainer, additionalLocationEstimate, deferredmtlrData,
                    getLCSReferenceNumber(), geranPositioningData, utranPositioningDataInfo, cellIdOrSai, hgmlcAddress, lcsServiceTypeID, saiPresent, pseudonymIndicator,
                    accuracyFulfilmentIndicator, velocityEstimate, sequenceNumber, periodicLDRInfo, moLrShortCircuitIndicator, geranGANSSpositioningData,
                    utranGANSSpositioningData, targetNodeForHandover);
            logger.debug("Added SubscriberLocationReportRequest");

            mapDialogLsm.send();

            this.countMapLcsReq++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SubscriberLocationReportRequest", createSLRReqData(mapDialogLsm.getLocalDialogId(), lcsEvent,
                    this.getNetworkNodeNumber(), lcsClientID, msisdn, imsi, imei, locationEstimate, getAgeOfLocationEstimate(), lmsi, getLCSReferenceNumber(),
                    deferredmtlrData, cellIdOrSai, hgmlcAddress, accuracyFulfilmentIndicator, reportingPLMNList), Level.INFO);

            currentRequestDef += "Sent SLR Request;";

        } catch (MAPException e) {
            return "Exception " + e.toString();
        }

        return "subscriberLocationReportRequest sent";
    }

    private String createSLRReqData(long dialogId, LCSEvent lcsEvent, String networkNodeNumber, LCSClientID lcsClientID, ISDNAddressString msisdn, IMSI imsi,
                                    IMEI imei, ExtGeographicalInformation locationEstimate, Integer ageOfLocationEstimate, LMSI lmsi, Integer lcsReferenceNumber,
                                    DeferredmtlrData deferredmtlrData, CellGlobalIdOrServiceAreaIdOrLAI cellIdOrSai, GSNAddress hgmlcAddress,
                                    AccuracyFulfilmentIndicator accuracyFulfilmentIndicator, ReportingPLMNList reportingPLMNList) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", lcsEvent=\"");
        sb.append(lcsEvent.getEvent());
        sb.append("\", networkNodeNumber=\"");
        sb.append(networkNodeNumber).append(", ");
        sb.append("\", lcsClientID=\"");
        sb.append(lcsClientID).append(", ");
        sb.append("\", MSISDN=\"");
        sb.append(msisdn.getAddress()).append(", ");
        sb.append("\", IMSI=\"");
        sb.append(imsi.getData()).append(", ");
        sb.append("\", IMEI=\"");
        sb.append(imei).append(", ");
        sb.append("\", locationEstimate=\"");
        sb.append(locationEstimate).append(", ");
        sb.append("\", latitude=\"");
        sb.append(locationEstimate.getLatitude()).append(", ");
        sb.append("\", longitude=\"");
        sb.append(locationEstimate.getLongitude()).append(", ");
        sb.append("\", altitude=\"");
        sb.append(locationEstimate.getAltitude()).append(", ");
        sb.append("\", locationEstimateConfidence=\"");
        sb.append(locationEstimate.getConfidence()).append(", ");
        sb.append("\", locationEstimateInnerRadius=\"");
        sb.append(locationEstimate.getInnerRadius()).append(", ");
        sb.append("\", locationEstimateTypeOfShape=\"");
        sb.append(locationEstimate.getTypeOfShape()).append(", ");
        sb.append("\", ageOfLocationEstimate=\"");
        sb.append(ageOfLocationEstimate);
        sb.append("\", LMSI=\"");
        sb.append(lmsi.toString()).append(", ");
        sb.append("\", lcsReferenceNumber=\"");
        sb.append(lcsReferenceNumber).append("\", ");
        sb.append("\", deferredmtlrData=\"");
        sb.append(deferredmtlrData).append(", ");
        sb.append("\", MCC=\"");
        try {
            sb.append((cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC())).append(", ");
        } catch (MAPException e) {
            e.printStackTrace();
        }
        sb.append("\", MNC=\"");
        try {
            sb.append((cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC())).append(", ");
        } catch (MAPException e) {
            e.printStackTrace();
        }
        sb.append("\", LAC=\"");
        try {
            sb.append((cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getLac())).append(", ");
        } catch (MAPException e) {
            e.printStackTrace();
        }
        sb.append("\", LAC=\"");
        try {
            sb.append((cellIdOrSai.getCellGlobalIdOrServiceAreaIdFixedLength().getCellIdOrServiceAreaCode())).append(", ");
        } catch (MAPException e) {
            e.printStackTrace();
        }
        sb.append("\", H-GMLCAddress=\"");
        sb.append(hgmlcAddress);
        sb.append("\", accuracyFulfilmentIndicator=\"");
        sb.append(accuracyFulfilmentIndicator);
        sb.append("\", reportingPLMNList=\"");
        for (int i = 0; i < reportingPLMNList.getPlmnList().size(); i++) {
            sb.append(reportingPLMNList.getPlmnList().get(i)).append(", ");
        }

        return sb.toString();
    }

    private String createSLRResData(long dialogId, String address) {
        StringBuilder sb = new StringBuilder();
        sb.append("dialogId=");
        sb.append(dialogId);
        sb.append(", naESRD=\"");
        sb.append(address);
        sb.append("\"");
        return sb.toString();
    }

    public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest subscriberLocationReportRequestIndication) {
        logger.debug("onSubscriberLocationReportRequest");
        this.countMapLcsReq++;
        if (!isStarted)
            return;

        MAPDialogLsm curDialog = subscriberLocationReportRequestIndication.getMAPDialog();
        String networkNodeNumberAddress = subscriberLocationReportRequestIndication.getLCSLocationInfo().getNetworkNodeNumber().getAddress();

        this.testerHost.sendNotif(SOURCE_NAME, "Rcvd: SubscriberLocationReportRequest",
                createSLRReqData(curDialog.getLocalDialogId(), subscriberLocationReportRequestIndication.getLCSEvent(),
                        networkNodeNumberAddress,
                        subscriberLocationReportRequestIndication.getLCSClientID(),
                        subscriberLocationReportRequestIndication.getMSISDN(),
                        subscriberLocationReportRequestIndication.getIMSI(),
                        subscriberLocationReportRequestIndication.getIMEI(),
                        subscriberLocationReportRequestIndication.getLocationEstimate(),
                        subscriberLocationReportRequestIndication.getAgeOfLocationEstimate(),
                        subscriberLocationReportRequestIndication.getLMSI(),
                        subscriberLocationReportRequestIndication.getLCSReferenceNumber(),
                        subscriberLocationReportRequestIndication.getDeferredmtlrData(),
                        subscriberLocationReportRequestIndication.getCellGlobalIdOrServiceAreaIdOrLAI(),
                        subscriberLocationReportRequestIndication.getHGMLCAddress(),
                        subscriberLocationReportRequestIndication.getAccuracyFulfilmentIndicator(),
                        subscriberLocationReportRequestIndication.getReportingPLMNList()), Level.INFO);

        MAPParameterFactory mapParameterFactory = this.mapProvider.getMAPParameterFactory();
        ISDNAddressString naEsrd = mapParameterFactory.createISDNAddressString(
                AddressNature.getInstance(getAddressNature().intValue()),
                NumberingPlan.getInstance(getNumberingPlanType().intValue()),
                getNaESRDAddress());

        try {
            curDialog.addSubscriberLocationReportResponse(subscriberLocationReportRequestIndication.getInvokeId(), naEsrd, null, null);
            logger.debug("\nset addSubscriberLocationReportResponse");
            curDialog.send();
            logger.debug("\naddSubscriberLocationReportResponse sent");
            this.countMapLcsResp++;

            this.testerHost.sendNotif(SOURCE_NAME, "Sent: SubscriberLocationReportResponse",
                    createSLRResData(curDialog.getLocalDialogId(), getNaESRDAddress()), Level.INFO);

        } catch (MAPException e) {
            logger.debug("Failed building response " + e.toString());
        }
    }

    public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse subscriberLocationReportResponseIndication) {
        logger.debug("onSubscriberLocationReportResponse");
        this.countMapLcsResp++;
        this.testerHost.sendNotif(SOURCE_NAME,
                "Rcvd: SubscriberLocationReportResponse", this
                        .createSLRResData(
                                subscriberLocationReportResponseIndication
                                        .getInvokeId(),
                                subscriberLocationReportResponseIndication
                                        .getNaESRD().getAddress()), Level.INFO);
    }

    //**********************************************************//
    //*** Common methods for MAP LSM operations' attributes ***//
    //********************************************************//
    @Override
    public String getMlcNumber() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getMlcNumber();
    }

    @Override
    public void setMlcNumber(String mlcNumber) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setMlcNumber(mlcNumber);
    }

    @Override
    public String getNetworkNodeNumber() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNetworkNodeNumber();
    }

    @Override
    public void setNetworkNodeNumber(String data) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setNetworkNodeNumber(data);
        this.testerHost.markStore();
    }

    // PSL Request
    @Override
    public Double getLocationEstimateLatitude() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLocationEstimate().getLatitude();
    }

    @Override
    public void setLocationEstimateLatitude(Double locationEstimateLatitude) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLatitude(locationEstimateLatitude);
        this.testerHost.markStore();
    }

    @Override
    public Double getLocationEstimateLongitude() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLocationEstimate().getLongitude();
    }

    @Override
    public void setLocationEstimateLongitude(Double locationEstimateLongitude) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLongitude(locationEstimateLongitude);
        this.testerHost.markStore();
    }

    @Override
    public LocationEstimateTypeEnumerated getLocEstimateType() {
        return new LocationEstimateTypeEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLocationEstimateType().getType());
    }

    @Override
    public void setLocEstimateType(LocationEstimateTypeEnumerated locEstimate) {
        this.testerHost.getConfigurationData().
                getTestLcsServerConfigurationData().setLocationEstimateType(LocationEstimateType.getLocationEstimateType(locEstimate.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public TypeOfShapeEnumerated getTypeOfShape() {
        return new TypeOfShapeEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getTypeOfShape().getCode());
    }

    @Override
    public void setTypeOfShapeEnumerated(TypeOfShapeEnumerated typeOfShape) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setTypeOfShape(TypeOfShape.valueOf(typeOfShape.toString()));
        this.testerHost.markStore();
    }

    @Override
    public Integer getLcsServiceTypeID() {
        return this.testerHost.getConfigurationData().
                getTestLcsServerConfigurationData().getLcsServiceTypeID();
    }

    @Override
    public void setLcsServiceTypeID(Integer lcsServiceTypeID) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLcsServiceTypeID(lcsServiceTypeID);
        this.testerHost.markStore();
    }

    @Override
    public LCSClientTypeEnumerated getLcsClientTypeEnumerated() {
        return new LCSClientTypeEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLcsClientType().getType());
    }

    @Override
    public void setLcsClientTypeEnumerated(LCSClientTypeEnumerated val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLcsClientType(LCSClientType.getLCSClientType(val.intValue()));
        this.testerHost.markStore();
    }


    @Override
    public void setCodeWordUSSDString(String codeWordUSSDString) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setCodeWordUSSDString(codeWordUSSDString);
        this.testerHost.markStore();
    }

    @Override
    public String getCodeWordUSSDString() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getCodeWordUSSDString();
    }

    @Override
    public void setCallSessionUnrelated(PrivacyCheckRelatedActionEnumerated val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setCallSessionUnrelated(PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public PrivacyCheckRelatedActionEnumerated getCallSessionUnrelated() {
        return new PrivacyCheckRelatedActionEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getCallSessionUnrelated().getAction());
    }

    @Override
    public void setCallSessionRelated(PrivacyCheckRelatedActionEnumerated val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setCallSessionRelated(PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public PrivacyCheckRelatedActionEnumerated getCallSessionRelated() {
        return new PrivacyCheckRelatedActionEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getCallSessionRelated().getAction());
    }

    @Override
    public boolean getMoLrShortCircuitIndicator() {
        return this.testerHost.getConfigurationData().
                getTestLcsServerConfigurationData().getMoLrShortCircuitIndicator();
    }

    @Override
    public void setMoLrShortCircuitIndicator(boolean moLrShortCircuitIndicator) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setMoLrShortCircuitIndicator(moLrShortCircuitIndicator);
        this.testerHost.markStore();
    }

    @Override
    public LCSEventType getLCSEventType() {
        return new LCSEventType(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLCSEvent().getEvent());
    }

    @Override
    public void setLCSEventType(LCSEventType val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLCSEvent(LCSEvent.getLCSEvent(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public LCSEvent getLCSEvent() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLCSEvent();
    }

    @Override
    public void setLCSEvent(LCSEvent lcsEvent) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLCSEvent(lcsEvent);
        this.testerHost.markStore();
    }

    @Override
    public Integer getCellId() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getCellId();
    }

    @Override
    public void setCellId(Integer cellId) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setCellId(cellId);
        this.testerHost.markStore();
    }

    @Override
    public Integer getLAC() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLAC();
    }

    @Override
    public void setLAC(Integer lac) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLAC(lac);
        this.testerHost.markStore();
    }

    @Override
    public Integer getMNC() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getMNC();
    }

    @Override
    public void setMNC(Integer mnc) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setMNC(mnc);
        this.testerHost.markStore();
    }

    @Override
    public Integer getMCC() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getMCC();
    }

    @Override
    public void setMCC(Integer mcc) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setMCC(mcc);
        this.testerHost.markStore();
    }

    @Override
    public Integer getAgeOfLocationEstimate() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAgeOfLocationEstimate();
    }

    @Override
    public void setAgeOfLocationEstimate(Integer ageLocationEstimate) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setAgeOfLocationEstimate(ageLocationEstimate);
        this.testerHost.markStore();
    }

    @Override
    public String getHGMLCAddress() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getHGMLCAddress();
    }

    @Override
    public void setHGMLCAddress(String hgmlcAddress) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setHGMLCAddress(hgmlcAddress);
        this.testerHost.markStore();
    }

    @Override
    public Integer getLCSReferenceNumber() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLCSReferenceNumber();
    }

    @Override
    public void setLCSReferenceNumber(Integer lcsReferenceNumber) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLCSReferenceNumber(lcsReferenceNumber);
        this.testerHost.markStore();
    }

    @Override
    public String getMSISDN() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getMSISDN();
    }

    @Override
    public void setMSISDN(String msisdn) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setMSISDN(msisdn);
        this.testerHost.markStore();
    }

    @Override
    public String getLMSI() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getLMSI();
    }

    @Override
    public void setLMSI(String lmsi) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setLMSI(lmsi);
        this.testerHost.markStore();
    }

    @Override
    public String getIMEI() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getIMEI();
    }

    @Override
    public void setIMEI(String imei) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setIMEI(imei);
        this.testerHost.markStore();
    }

    @Override
    public String getIMSI() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getIMSI();
    }

    @Override
    public void setIMSI(String imsi) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setIMSI(imsi);
        this.testerHost.markStore();
    }

    @Override
    public AddressNatureType getAddressNature() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAddressNature().getIndicator());
    }

    @Override
    public void setAddressNature(AddressNatureType val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setAddressNature(AddressNature.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public String getNumberingPlan() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNumberingPlan();
    }

    @Override
    public void setNumberingPlan(String numPlan) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setNumberingPlan(numPlan);
        this.testerHost.markStore();
    }

    @Override
    public NumberingPlanMapType getNumberingPlanType() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getNumberingPlanType().getIndicator());
    }

    @Override
    public void setNumberingPlanType(NumberingPlanMapType val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setNumberingPlanType(NumberingPlan.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void setAreaType(AreaTypeEnumerated val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setAreaType(AreaType.getAreaType(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public AreaTypeEnumerated getAreaType() {
        return new AreaTypeEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getAreaType().getType());
    }

    @Override
    public OccurrenceInfoEnumerated getOccurrenceInfo() {
        return new OccurrenceInfoEnumerated(this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getOccurrenceInfo().getInfo());
    }

    @Override
    public void setOccurrenceInfo(OccurrenceInfoEnumerated occurrenceInfo) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setOccurrenceInfo(OccurrenceInfo.getOccurrenceInfo(occurrenceInfo.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public Integer getIntervalTime() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getIntervalTime();
    }

    @Override
    public void setIntervalTime(Integer intervalTime) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setIntervalTime(intervalTime);
        this.testerHost.markStore();
    }

    @Override
    public void setReportingAmount(Integer val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setReportingAmount(val);
        this.testerHost.markStore();
    }

    @Override
    public Integer getReportingAmount() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getReportingAmount();
    }

    @Override
    public void setReportingInterval(Integer val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setReportingInterval(val);
        this.testerHost.markStore();
    }

    @Override
    public Integer getReportingInterval() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getReportingInterval();
    }

    @Override
    public void setDataCodingScheme(Integer val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setDataCodingScheme(val);
        this.testerHost.markStore();
    }

    @Override
    public Integer getDataCodingScheme() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getDataCodingScheme();
    }

    @Override
    public String getCurrentRequestDef() {
        return "LastDialog: " + currentRequestDef;
    }

    @Override
    public void putAddressNature(String val) {
        AddressNatureType x = AddressNatureType.createInstance(val);
        if (x != null)
            this.setAddressNature(x);
    }

    @Override
    public void putNumberingPlanType(String val) {
        NumberingPlanMapType x = NumberingPlanMapType.createInstance(val);
        if (x != null)
            this.setNumberingPlanType(x);
    }

    @Override
    public void putLCSEventType(String val) {
        LCSEventType x = LCSEventType.createInstance(val);
        if (x != null)
            this.setLCSEventType(x);
    }

    @Override
    public String getNaESRDAddress() {
        return this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().getNaESRDAddress();
    }

    @Override
    public void setNaESRDAddress(String address) {
        this.testerHost.getConfigurationData().getTestLcsClientConfigurationData().setNaESRDAddress(address);
        this.testerHost.markStore();
    }

    //TODO move this helper method to constructor type...
    private GSNAddress createGSNAddress(String gsnAddress) throws MAPException {
        try {
            //From InetAddress javadoc "the host name can either be a machine name, such as "java.sun.com", or a textual representation of its IP address.
            //If a literal IP address is supplied, only the validity of the address format is checked".
            InetAddress address = InetAddress.getByName(gsnAddress);
            GSNAddressAddressType addressType = null;
            if (address instanceof Inet4Address) {
                addressType = GSNAddressAddressType.IPv4;
            } else if (address instanceof Inet6Address) {
                addressType = GSNAddressAddressType.IPv6;
            }
            byte[] addressData = address.getAddress();
            return this.mapParameterFactory.createGSNAddress(addressType, addressData);

        } catch (UnknownHostException e) {
            throw new MAPException("Invalid GSNAddress", e);
        }
    }

    //*********************************************************//
    //*** Tester Host MAP LSM operations' reaction methods ***//
    //*******************************************************//

    @Override
    public SRIforLCSReaction getSRIforLCSReaction() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getSriForLCSReaction();
    }

    @Override
    public String getSRIforLCSReaction_Value() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getSriForLCSReaction().toString();
    }

    @Override
    public void setSRIforLCSReaction(SRIforLCSReaction val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setSriForLCSReaction(val);
        this.testerHost.markStore();
    }

    @Override
    public void putSRIforLCSReaction(String val) {
        SRIforLCSReaction x = SRIforLCSReaction.createInstance(val);
        if (x != null)
            this.setSRIforLCSReaction(x);
    }

    @Override
    public PSLReaction getPSLReaction() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getPslReaction();
    }

    @Override
    public String getPSLReaction_Value() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getPslReaction().toString();
    }

    @Override
    public void setPSLReaction(PSLReaction val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setPslReaction(val);
        this.testerHost.markStore();
    }

    @Override
    public void putPSLReaction(String val) {
        PSLReaction x = PSLReaction.createInstance(val);
        if (x != null)
            this.setPSLReaction(x);
    }

    @Override
    public SLRReaction getSLRReaction() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getSlrReaction();
    }

    @Override
    public String getSLRReaction_Value() {
        return this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().getSlrReaction().toString();
    }

    @Override
    public void setSLRReaction(SLRReaction val) {
        this.testerHost.getConfigurationData().getTestLcsServerConfigurationData().setSlrReaction(val);
        this.testerHost.markStore();
    }

    @Override
    public void putSLRReaction(String val) {
        SLRReaction x = SLRReaction.createInstance(val);
        if (x != null)
            this.setSLRReaction(x);
    }


}
