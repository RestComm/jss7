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

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.ReportingPLMNList;
import org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class ProvideSubscriberLocationRequestImpl extends LsmMessageImpl implements ProvideSubscriberLocationRequest {

    private static final int _TAG_LCS_CLIENT_ID = 0;
    private static final int _TAG_PRIVACY_OVERRIDE = 1;
    private static final int _TAG_IMSI = 2;
    private static final int _TAG_MSISDN = 3;
    private static final int _TAG_LMSI = 4;
    private static final int _TAG_IMEI = 5;
    private static final int _TAG_LCS_PRIORITY = 6;
    private static final int _TAG_LCS_QOS = 7;
    private static final int _TAG_EXTENSION_CONTAINER = 8;
    private static final int _TAG_SUPPORTED_GAD_SHAPES = 9;
    private static final int _TAG_LCS_REFERENCE_NUMBER = 10;
    private static final int _TAG_LCS_SERVICE_TYPE_ID = 11;
    private static final int _TAG_LCS_CODEWORD = 12;
    private static final int _TAG_LCS_PRIVACY_CHECK = 13;
    private static final int _TAG_AREA_EVENT_INFO = 14;
    private static final int _TAG_H_GMLC_ADDRESS = 15;
    private static final int _TAG_mo_lrShortCircuitIndicator = 16;
    private static final int _TAG_periodicLDRInfo = 17;
    private static final int _TAG_reportingPLMNList = 18;

    public static final String _PrimitiveName = "ProvideSubscriberLocationRequest";

    private LocationType locationType;
    private ISDNAddressString mlcNumber;
    private LCSClientID lcsClientID;
    private boolean privacyOverride;
    private IMSI imsi;
    private ISDNAddressString msisdn;
    private LMSI lmsi;
    private IMEI imei;
    private LCSPriority lcsPriority;
    private LCSQoS lcsQoS;
    private MAPExtensionContainer extensionContainer;
    private SupportedGADShapes supportedGADShapes;
    private Integer lcsReferenceNumber;
    private Integer lcsServiceTypeID;
    private LCSCodeword lcsCodeword;
    private LCSPrivacyCheck lcsPrivacyCheck;
    private AreaEventInfo areaEventInfo;
    private GSNAddress hgmlcAddress;
    private boolean moLrShortCircuitIndicator;
    private PeriodicLDRInfo periodicLDRInfo;
    private ReportingPLMNList reportingPLMNList;

    /**
     *
     */
    public ProvideSubscriberLocationRequestImpl() {
        super();
    }

    /**
     * @param locationType
     * @param mlcNumber
     * @param lcsClientID
     * @param privacyOverride
     * @param imsi
     * @param msisdn
     * @param lmsi
     * @param imei
     * @param lcsPriority
     * @param lcsQoS
     * @param extensionContainer
     * @param supportedGADShapes
     * @param lcsReferenceNumber
     * @param lcsServiceTypeID
     * @param lcsCodeword
     * @param lcsPrivacyCheck
     * @param areaEventInfo
     * @param hgmlcAddress
     */
    public ProvideSubscriberLocationRequestImpl(LocationType locationType, ISDNAddressString mlcNumber,
            LCSClientID lcsClientID, boolean privacyOverride, IMSI imsi, ISDNAddressString msisdn, LMSI lmsi, IMEI imei,
            LCSPriority lcsPriority, LCSQoS lcsQoS, MAPExtensionContainer extensionContainer,
            SupportedGADShapes supportedGADShapes, Integer lcsReferenceNumber, Integer lcsServiceTypeID,
            LCSCodeword lcsCodeword, LCSPrivacyCheck lcsPrivacyCheck, AreaEventInfo areaEventInfo, GSNAddress hgmlcAddress,
            boolean moLrShortCircuitIndicator, PeriodicLDRInfo periodicLDRInfo, ReportingPLMNList reportingPLMNList) {
        super();
        this.locationType = locationType;
        this.mlcNumber = mlcNumber;
        this.lcsClientID = lcsClientID;
        this.privacyOverride = privacyOverride;
        this.imsi = imsi;
        this.msisdn = msisdn;
        this.lmsi = lmsi;
        this.imei = imei;
        this.lcsPriority = lcsPriority;
        this.lcsQoS = lcsQoS;
        this.extensionContainer = extensionContainer;
        this.supportedGADShapes = supportedGADShapes;
        this.lcsReferenceNumber = lcsReferenceNumber;
        this.lcsServiceTypeID = lcsServiceTypeID;
        this.lcsCodeword = lcsCodeword;
        this.lcsPrivacyCheck = lcsPrivacyCheck;
        this.areaEventInfo = areaEventInfo;
        this.hgmlcAddress = hgmlcAddress;
        this.moLrShortCircuitIndicator = moLrShortCircuitIndicator;
        this.periodicLDRInfo = periodicLDRInfo;
        this.reportingPLMNList = reportingPLMNList;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.provideSubscriberLocation_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.provideSubscriberLocation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLocationType()
     */
    public LocationType getLocationType() {
        return this.locationType;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getMlcNumber()
     */
    public ISDNAddressString getMlcNumber() {
        return this.mlcNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSClientID()
     */
    public LCSClientID getLCSClientID() {
        return this.lcsClientID;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getPrivacyOverride()
     */
    public boolean getPrivacyOverride() {
        return this.privacyOverride;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getIMSI()
     */
    public IMSI getIMSI() {
        return this.imsi;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getMSISDN()
     */
    public ISDNAddressString getMSISDN() {
        return this.msisdn;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLMSI()
     */
    public LMSI getLMSI() {
        return this.lmsi;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSPriority()
     */
    public LCSPriority getLCSPriority() {
        return this.lcsPriority;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSQoS()
     */
    public LCSQoS getLCSQoS() {
        return this.lcsQoS;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getIMEI()
     */
    public IMEI getIMEI() {
        return this.imei;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getSupportedGADShapes()
     */
    public SupportedGADShapes getSupportedGADShapes() {
        return this.supportedGADShapes;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSReferenceNumber()
     */
    public Integer getLCSReferenceNumber() {
        return this.lcsReferenceNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSCodeword()
     */
    public LCSCodeword getLCSCodeword() {
        return this.lcsCodeword;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSServiceTypeID()
     */
    public Integer getLCSServiceTypeID() {
        return this.lcsServiceTypeID;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getLCSPrivacyCheck()
     */
    public LCSPrivacyCheck getLCSPrivacyCheck() {
        return this.lcsPrivacyCheck;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getAreaEventInfo()
     */
    public AreaEventInfo getAreaEventInfo() {
        return this.areaEventInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm. ProvideSubscriberLocationRequestIndication#getHGMLCAddress()
     */
    public GSNAddress getHGMLCAddress() {
        return this.hgmlcAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass ()
     */
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return false;
    }

    public boolean getMoLrShortCircuitIndicator() {
        return moLrShortCircuitIndicator;
    }

    public PeriodicLDRInfo getPeriodicLDRInfo() {
        return periodicLDRInfo;
    }

    public ReportingPLMNList getReportingPLMNList() {
        return reportingPLMNList;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll
     * (org.mobicents.protocols.asn.AsnInputStream)
     */
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData
     * (org.mobicents.protocols.asn.AsnInputStream, int)
     */
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.locationType = null;
        this.mlcNumber = null;
        this.lcsClientID = null;
        this.privacyOverride = false;
        this.imsi = null;
        this.msisdn = null;
        this.lmsi = null;
        this.imei = null;
        this.lcsPriority = null;
        this.lcsQoS = null;
        this.extensionContainer = null;
        this.supportedGADShapes = null;
        this.lcsReferenceNumber = null;
        this.lcsServiceTypeID = null;
        this.lcsCodeword = null;
        this.lcsPrivacyCheck = null;
        this.areaEventInfo = null;
        this.hgmlcAddress = null;
        this.moLrShortCircuitIndicator = false;
        this.periodicLDRInfo = null;
        this.reportingPLMNList = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    // locationType
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || ais.isTagPrimitive() || tag != Tag.SEQUENCE)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".locationType: Parameter has bad tag or tag class or is primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.locationType = new LocationTypeImpl();
                    ((LocationTypeImpl) this.locationType).decodeAll(ais);
                    break;

                case 1:
                    // mlc-Number
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".mlcNumber: Parameter has bad tag or tag class or is not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.mlcNumber = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.mlcNumber).decodeAll(ais);
                    break;

                default:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        switch (tag) {
                            case _TAG_LCS_CLIENT_ID:
                                // lcs-ClientID [0] LCS-ClientID OPTIONAL,
                                if (ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [lcs-ClientID [0] LCS-ClientID] is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.lcsClientID = new LCSClientIDImpl();
                                ((LCSClientIDImpl) this.lcsClientID).decodeAll(ais);
                                break;
                            case _TAG_PRIVACY_OVERRIDE:
                                // privacyOverride [1] NULL OPTIONAL,
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [privacyOverride [1] NULL ] is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                ais.readNull();
                                this.privacyOverride = true;
                                break;
                            case _TAG_IMSI:
                                // imsi [2] IMSI OPTIONAL,
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [imsi [2] IMSI ] is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.imsi = new IMSIImpl();
                                ((IMSIImpl) this.imsi).decodeAll(ais);
                                break;
                            case _TAG_MSISDN:
                                // msisdn [3] ISDN-AddressString OPTIONAL,
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [msisdn [3] ISDN-AddressString ] is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.msisdn = new ISDNAddressStringImpl();
                                ((ISDNAddressStringImpl) this.msisdn).decodeAll(ais);
                                break;
                            case _TAG_LMSI:
                                // lmsi [4] LMSI OPTIONAL,
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [lmsi [4] LMSI ] is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.lmsi = new LMSIImpl();
                                ((LMSIImpl) this.lmsi).decodeAll(ais);
                                break;
                            case _TAG_IMEI:
                                // imei [5] IMEI OPTIONAL
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [imei [5] IMEI ] is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.imei = new IMEIImpl();
                                ((IMEIImpl) this.imei).decodeAll(ais);
                                break;
                            case _TAG_LCS_PRIORITY:
                                // lcs-Priority [6] LCS-Priority OPTIONAL,
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [lcs-Priority [6] LCS-Priority ] is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.lcsPriority = LCSPriority.getInstance((int) ais.readOctetString()[0]);
                                break;
                            case _TAG_LCS_QOS:
                                // lcs-QoS [7] LCS-QoS OPTIONAL,
                                if (ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [lcs-QoS [7] LCS-QoS ] is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.lcsQoS = new LCSQoSImpl();
                                ((LCSQoSImpl) this.lcsQoS).decodeAll(ais);
                                break;
                            case _TAG_EXTENSION_CONTAINER:
                                // extensionContainer [8] ExtensionContainer OPTIONAL,
                                if (ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter extensionContainer is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.extensionContainer = new MAPExtensionContainerImpl();
                                ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                                break;
                            case _TAG_SUPPORTED_GAD_SHAPES:
                                // supportedGADShapes [9] SupportedGADShapes OPTIONAL,
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [supportedGADShapes [9] SupportedGADShapes] is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.supportedGADShapes = new SupportedGADShapesImpl();
                                ((SupportedGADShapesImpl) this.supportedGADShapes).decodeAll(ais);
                                break;
                            case _TAG_LCS_REFERENCE_NUMBER:
                                // lcs-ReferenceNumber [10] LCS-ReferenceNumber OPTIONAL,
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [lcs-ReferenceNumber [10] LCS-ReferenceNumber] is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.lcsReferenceNumber = (int) ais.readOctetString()[0];
                                break;
                            case _TAG_LCS_SERVICE_TYPE_ID:
                                // lcsServiceTypeID [11] LCSServiceTypeID OPTIONAL,
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [lcsServiceTypeID [11] LCSServiceTypeID] is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.lcsServiceTypeID = (int) ais.readInteger();
                                break;
                            case _TAG_LCS_CODEWORD:
                                // lcsCodeword [12] LCSCodeword OPTIONAL,
                                if (ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [lcsCodeword [12] LCSCodeword] is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.lcsCodeword = new LCSCodewordImpl();
                                ((LCSCodewordImpl) this.lcsCodeword).decodeAll(ais);
                                break;
                            case _TAG_LCS_PRIVACY_CHECK:
                                // lcs-PrivacyCheck [13] LCS-PrivacyCheck OPTIONAL,
                                if (ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [lcs-PrivacyCheck [13] LCS-PrivacyCheck] is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.lcsPrivacyCheck = new LCSPrivacyCheckImpl();
                                ((LCSPrivacyCheckImpl) this.lcsPrivacyCheck).decodeAll(ais);
                                break;
                            case _TAG_AREA_EVENT_INFO:
                                // areaEventInfo [14] AreaEventInfo OPTIONAL,
                                if (ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [areaEventInfo [14] AreaEventInfo] is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.areaEventInfo = new AreaEventInfoImpl();
                                ((AreaEventInfoImpl) this.areaEventInfo).decodeAll(ais);
                                break;
                            case _TAG_H_GMLC_ADDRESS:
                                // h-gmlc-Address [15] GSN-Address OPTIONAL
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter [h-gmlc-Address [15] GSN-Address] is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.hgmlcAddress = new GSNAddressImpl();
                                ((GSNAddressImpl) this.hgmlcAddress).decodeAll(ais);
                                break;
                            case _TAG_mo_lrShortCircuitIndicator:
                                // mo_lrShortCircuitIndicator
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter mo_lrShortCircuitIndicator is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                ais.readNull();
                                this.moLrShortCircuitIndicator = true;
                                break;
                            case _TAG_periodicLDRInfo:
                                // periodicLDRInfo
                                if (ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter periodicLDRInfo is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.periodicLDRInfo = new PeriodicLDRInfoImpl();
                                ((PeriodicLDRInfoImpl) this.periodicLDRInfo).decodeAll(ais);
                                break;
                            case _TAG_reportingPLMNList:
                                // reportingPLMNList
                                if (ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter reportingPLMNList is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.reportingPLMNList = new ReportingPLMNListImpl();
                                ((ReportingPLMNListImpl) this.reportingPLMNList).decodeAll(ais);
                                break;

                            default:
                                // Do we care?
                                ais.advanceElement();
                                break;
                        }
                    } else {
                        ais.advanceElement();
                    }
                    break;
            }

            num++;
        }

        if (num < 2)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Needs at least 2 mandatory parameters, found " + num,
                    MAPParsingComponentExceptionReason.MistypedParameter);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
     * (org.mobicents.protocols.asn.AsnOutputStream, int, int)
     */
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.locationType == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter locationType is not defined");
        }

        if (this.mlcNumber == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter mlc-Number is not defined");
        }

        ((LocationTypeImpl) this.locationType).encodeAll(asnOs);

        ((ISDNAddressStringImpl) this.mlcNumber).encodeAll(asnOs);

        if (this.lcsClientID != null) {
            // lcs-ClientID [0] LCS-ClientID OPTIONAL
            ((LCSClientIDImpl) this.lcsClientID).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CLIENT_ID);
        }

        if (this.privacyOverride) {
            // privacyOverride [1] NULL OPTIONAL
            try {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_PRIVACY_OVERRIDE);
            } catch (IOException e) {
                throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter privacyOverride", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException while encoding " + _PrimitiveName + " parameter privacyOverride", e);
            }
        }

        if (this.imsi != null) {
            // imsi [2] IMSI OPTIONAL
            ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_IMSI);
        }

        if (this.msisdn != null) {
            // msisdn [3] ISDN-AddressString OPTIONAL,
            ((ISDNAddressStringImpl) this.msisdn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_MSISDN);
        }

        if (this.lmsi != null) {
            // lmsi [4] LMSI OPTIONAL,
            ((LMSIImpl) this.lmsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LMSI);
        }

        if (this.imei != null) {
            // imei [5] IMEI OPTIONAL,
            ((IMEIImpl) this.imei).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_IMEI);
        }

        if (this.lcsPriority != null) {
            // lcs-Priority [6] LCS-Priority OPTIONAL,
            try {
                asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_PRIORITY,
                        new byte[] { (byte) this.lcsPriority.getCode() });
            } catch (IOException e) {
                throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter lcsPriority", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException while encoding " + _PrimitiveName + " parameter lcsPriority", e);
            }
        }

        if (this.lcsQoS != null) {
            // lcs-QoS [7] LCS-QoS OPTIONAL
            ((LCSQoSImpl) this.lcsQoS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_QOS);
        }

        if (this.extensionContainer != null) {
            // extensionContainer [8] ExtensionContainer OPTIONAL,
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_EXTENSION_CONTAINER);
        }

        if (this.supportedGADShapes != null) {
            // supportedGADShapes [9] SupportedGADShapes OPTIONAL
            ((SupportedGADShapesImpl) this.supportedGADShapes).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_SUPPORTED_GAD_SHAPES);
        }

        if (this.lcsReferenceNumber != null) {
            // lcs-ReferenceNumber [10] LCS-ReferenceNumber OPTIONAL,
            try {
                asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_REFERENCE_NUMBER,
                        new byte[] { this.lcsReferenceNumber.byteValue() });
            } catch (IOException e) {
                throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter lcsReferenceNumber", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException while encoding " + _PrimitiveName + " parameter lcsReferenceNumber", e);
            }
        }

        if (this.lcsServiceTypeID != null) {
            // lcsServiceTypeID [11] LCSServiceTypeID OPTIONAL,
            try {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_SERVICE_TYPE_ID, this.lcsServiceTypeID);
            } catch (IOException e) {
                throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter lcsReferenceNumber", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException while encoding " + _PrimitiveName + " parameter lcsReferenceNumber", e);
            }
        }

        if (this.lcsCodeword != null) {
            // lcsCodeword [12] LCSCodeword OPTIONAL,
            ((LCSCodewordImpl) this.lcsCodeword).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_CODEWORD);
        }

        if (this.lcsPrivacyCheck != null) {
            // lcs-PrivacyCheck [13] LCS-PrivacyCheck OPTIONAL,
            ((LCSPrivacyCheckImpl) this.lcsPrivacyCheck).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LCS_PRIVACY_CHECK);
        }

        if (this.areaEventInfo != null) {
            // areaEventInfo [14] AreaEventInfo OPTIONAL,
            ((AreaEventInfoImpl) this.areaEventInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_AREA_EVENT_INFO);
        }

        if (this.hgmlcAddress != null) {
            ((GSNAddressImpl) this.hgmlcAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_H_GMLC_ADDRESS);
        }

        if (this.moLrShortCircuitIndicator) {
            // moLrShortCircuitIndicator
            try {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_mo_lrShortCircuitIndicator);
            } catch (IOException e) {
                throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter moLrShortCircuitIndicator",
                        e);
            } catch (AsnException e) {
                throw new MAPException(
                        "AsnException while encoding " + _PrimitiveName + " parameter moLrShortCircuitIndicator", e);
            }
        }

        if (this.periodicLDRInfo != null) {
            ((PeriodicLDRInfoImpl) this.periodicLDRInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_periodicLDRInfo);
        }

        if (this.reportingPLMNList != null) {
            ((ReportingPLMNListImpl) this.reportingPLMNList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_reportingPLMNList);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.locationType != null) {
            sb.append("locationType=");
            sb.append(locationType.toString());
        }
        if (this.mlcNumber != null) {
            sb.append(", mlcNumber=");
            sb.append(mlcNumber.toString());
        }
        if (this.lcsClientID != null) {
            sb.append(", lcsClientID=");
            sb.append(lcsClientID.toString());
        }
        if (this.privacyOverride) {
            sb.append(", privacyOverride");
        }
        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(imsi.toString());
        }
        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(msisdn.toString());
        }
        if (this.lmsi != null) {
            sb.append(", lmsi=");
            sb.append(lmsi.toString());
        }
        if (this.imei != null) {
            sb.append(", imei=");
            sb.append(imei.toString());
        }
        if (this.lcsPriority != null) {
            sb.append(", lcsPriority=");
            sb.append(lcsPriority.toString());
        }
        if (this.lcsQoS != null) {
            sb.append(", lcsQoS=");
            sb.append(lcsQoS.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(extensionContainer.toString());
        }
        if (this.supportedGADShapes != null) {
            sb.append(", supportedGADShapes=");
            sb.append(supportedGADShapes.toString());
        }
        if (this.lcsReferenceNumber != null) {
            sb.append(", lcsReferenceNumber=");
            sb.append(lcsReferenceNumber.toString());
        }
        if (this.lcsServiceTypeID != null) {
            sb.append(", lcsServiceTypeID=");
            sb.append(lcsServiceTypeID.toString());
        }
        if (this.lcsCodeword != null) {
            sb.append(", lcsCodeword=");
            sb.append(lcsCodeword.toString());
        }
        if (this.lcsPrivacyCheck != null) {
            sb.append(", lcsPrivacyCheck=");
            sb.append(lcsPrivacyCheck.toString());
        }
        if (this.areaEventInfo != null) {
            sb.append(", areaEventInfo=");
            sb.append(areaEventInfo.toString());
        }
        if (this.hgmlcAddress != null) {
            sb.append(", hgmlcAddress=");
            sb.append(hgmlcAddress.toString());
        }
        if (this.moLrShortCircuitIndicator) {
            sb.append(", moLrShortCircuitIndicator");
        }
        if (this.periodicLDRInfo != null) {
            sb.append(", periodicLDRInfo=");
            sb.append(periodicLDRInfo.toString());
        }
        if (this.reportingPLMNList != null) {
            sb.append(", reportingPLMNList=");
            sb.append(reportingPLMNList.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
