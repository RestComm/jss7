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

package org.mobicents.protocols.ss7.map.service.callhandling;

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
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.primitives.ExtExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.PagingAreaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ProvideRoamingNumberRequestImpl extends CallHandlingMessageImpl implements ProvideRoamingNumberRequest {

    private static final int TAG_imsi = 0;
    private static final int TAG_mscNumber = 1;
    private static final int TAG_msisdn = 2;
    private static final int TAG_lmsi = 4;
    private static final int TAG_gsmBearerCapability = 5;
    private static final int TAG_networkSignalInfo = 6;
    private static final int TAG_suppressionOfAnnouncement = 7;
    private static final int TAG_gmscAddress = 8;
    private static final int TAG_callReferenceNumber = 9;
    private static final int TAG_orInterrogation = 10;
    private static final int TAG_extensionContainer = 11;
    private static final int TAG_alertingPattern = 12;
    private static final int TAG_ccbsCall = 13;
    private static final int TAG_supportedCamelPhasesInInterrogatingNode = 15;
    private static final int TAG_additionalSignalInfo = 14;
    private static final int TAG_orNotSupportedInGMSC = 16;
    private static final int TAG_prePagingSupported = 17;
    private static final int TAG_longFTNSupported = 18;
    private static final int TAG_suppressVtCsi = 19;
    private static final int TAG_offeredCamel4CSIsInInterrogatingNode = 20;
    private static final int TAG_mtRoamingRetrySupported = 21;
    private static final int TAG_pagingArea = 22;
    private static final int TAG_callPriority = 23;
    private static final int TAG_mtrfIndicator = 24;
    private static final int TAG_oldMSCNumber = 25;
    public static final String _PrimitiveName = "ProvideRoamingNumberRequest";

    private IMSI imsi;
    private ISDNAddressString mscNumber;
    private ISDNAddressString msisdn;
    private LMSI lmsi;
    private ExternalSignalInfo gsmBearerCapability;
    private ExternalSignalInfo networkSignalInfo;
    private boolean suppressionOfAnnouncement;
    private ISDNAddressString gmscAddress;
    private CallReferenceNumber callReferenceNumber;
    private boolean orInterrogation;
    private MAPExtensionContainer extensionContainer;
    private AlertingPattern alertingPattern;
    private boolean ccbsCall;
    private SupportedCamelPhases supportedCamelPhasesInInterrogatingNode;
    private ExtExternalSignalInfo additionalSignalInfo;
    private boolean orNotSupportedInGMSC;
    private boolean prePagingSupported;
    private boolean longFTNSupported;
    private boolean suppressVtCsi;
    private OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode;
    private boolean mtRoamingRetrySupported;
    private PagingArea pagingArea;
    private EMLPPPriority callPriority;
    private boolean mtrfIndicator;
    private ISDNAddressString oldMSCNumber;
    private long mapProtocolVersion;

    public ProvideRoamingNumberRequestImpl(IMSI imsi, ISDNAddressString mscNumber, ISDNAddressString msisdn, LMSI lmsi,
            ExternalSignalInfo gsmBearerCapability, ExternalSignalInfo networkSignalInfo, boolean suppressionOfAnnouncement,
            ISDNAddressString gmscAddress, CallReferenceNumber callReferenceNumber, boolean orInterrogation,
            MAPExtensionContainer extensionContainer, AlertingPattern alertingPattern, boolean ccbsCall,
            SupportedCamelPhases supportedCamelPhasesInInterrogatingNode, ExtExternalSignalInfo additionalSignalInfo,
            boolean orNotSupportedInGMSC, boolean prePagingSupported, boolean longFTNSupported, boolean suppressVtCsi,
            OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode, boolean mtRoamingRetrySupported, PagingArea pagingArea,
            EMLPPPriority callPriority, boolean mtrfIndicator, ISDNAddressString oldMSCNumber, long mapProtocolVersion) {
        super();
        this.imsi = imsi;
        this.mscNumber = mscNumber;
        this.msisdn = msisdn;
        this.lmsi = lmsi;
        this.gsmBearerCapability = gsmBearerCapability;
        this.networkSignalInfo = networkSignalInfo;
        this.suppressionOfAnnouncement = suppressionOfAnnouncement;
        this.gmscAddress = gmscAddress;
        this.callReferenceNumber = callReferenceNumber;
        this.orInterrogation = orInterrogation;
        this.extensionContainer = extensionContainer;
        this.alertingPattern = alertingPattern;
        this.ccbsCall = ccbsCall;
        this.supportedCamelPhasesInInterrogatingNode = supportedCamelPhasesInInterrogatingNode;
        this.additionalSignalInfo = additionalSignalInfo;
        this.orNotSupportedInGMSC = orNotSupportedInGMSC;
        this.prePagingSupported = prePagingSupported;
        this.longFTNSupported = longFTNSupported;
        this.suppressVtCsi = suppressVtCsi;
        this.offeredCamel4CSIsInInterrogatingNode = offeredCamel4CSIsInInterrogatingNode;
        this.mtRoamingRetrySupported = mtRoamingRetrySupported;
        this.pagingArea = pagingArea;
        this.callPriority = callPriority;
        this.mtrfIndicator = mtrfIndicator;
        this.oldMSCNumber = oldMSCNumber;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public ProvideRoamingNumberRequestImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.provideRoamingNumber_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.provideRoamingNumber;
    }

    @Override
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
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

    @Override
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
        this.imsi = null;
        this.mscNumber = null;
        this.msisdn = null;
        this.lmsi = null;
        this.gsmBearerCapability = null;
        this.networkSignalInfo = null;
        this.suppressionOfAnnouncement = false;
        this.gmscAddress = null;
        this.callReferenceNumber = null;
        this.orInterrogation = false;
        this.extensionContainer = null;
        this.alertingPattern = null;
        this.ccbsCall = false;
        this.supportedCamelPhasesInInterrogatingNode = null;
        this.additionalSignalInfo = null;
        this.orNotSupportedInGMSC = false;
        this.prePagingSupported = false;
        this.longFTNSupported = false;
        this.suppressVtCsi = false;
        this.offeredCamel4CSIsInInterrogatingNode = null;
        this.mtRoamingRetrySupported = false;
        this.pagingArea = null;
        this.callPriority = null;
        this.mtrfIndicator = false;
        this.oldMSCNumber = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        if (this.mapProtocolVersion <= 2) {

            if (ais.available() > 0) {
                while (true) {
                    if (ais.available() == 0)
                        break;

                    int tag = ais.readTag();

                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        switch (tag) {
                            case TAG_imsi:
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".imsi: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.imsi = new IMSIImpl();
                                ((IMSIImpl) this.imsi).decodeAll(ais);
                                break;
                            case TAG_mscNumber:
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".mscNumber: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.mscNumber = new ISDNAddressStringImpl();
                                ((ISDNAddressStringImpl) this.mscNumber).decodeAll(ais);
                                break;
                            case TAG_msisdn:
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".msisdn: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.msisdn = new ISDNAddressStringImpl();
                                ((ISDNAddressStringImpl) this.msisdn).decodeAll(ais);
                                break;
                            case TAG_lmsi:
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".lmsi: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.lmsi = new LMSIImpl();
                                ((LMSIImpl) this.lmsi).decodeAll(ais);
                                break;
                            case TAG_gsmBearerCapability:
                                if (ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".gsmBearerCapability: is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.gsmBearerCapability = new ExternalSignalInfoImpl();
                                ((ExternalSignalInfoImpl) this.gsmBearerCapability).decodeAll(ais);
                                break;
                            case TAG_networkSignalInfo:
                                if (ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".networkSignalInfo: is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.networkSignalInfo = new ExternalSignalInfoImpl();
                                ((ExternalSignalInfoImpl) this.networkSignalInfo).decodeAll(ais);
                                break;
                            default:
                                ais.advanceElement();
                                break;
                        }
                    } else {
                        ais.advanceElement();
                        // break;
                    }
                }
            }

            if (this.mapProtocolVersion == 2) {
                if (this.imsi == null || this.mscNumber == null)
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " V2"
                            + ": imsi  or mscNumber must not be null", MAPParsingComponentExceptionReason.MistypedParameter);
            } else if (this.mapProtocolVersion == 1) {
                if (this.imsi == null)
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + " V1"
                            + ": imsi  or mscNumber must not be null", MAPParsingComponentExceptionReason.MistypedParameter);
            }

        } else {
            while (true) {
                if (ais.available() == 0)
                    break;

                int tag = ais.readTag();
                if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                    switch (tag) {
                        case TAG_imsi:// 1
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".imsi: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.imsi = new IMSIImpl();
                            ((IMSIImpl) this.imsi).decodeAll(ais);
                            break;
                        case TAG_mscNumber:// 2
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".mscNumber: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.mscNumber = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.mscNumber).decodeAll(ais);
                            break;
                        case TAG_msisdn:// 3
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".msisdn: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.msisdn = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.msisdn).decodeAll(ais);
                            break;
                        case TAG_lmsi:// 4
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".lmsi: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.lmsi = new LMSIImpl();
                            ((LMSIImpl) this.lmsi).decodeAll(ais);
                            break;
                        case TAG_gsmBearerCapability:// 5
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gsmBearerCapability: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.gsmBearerCapability = new ExternalSignalInfoImpl();
                            ((ExternalSignalInfoImpl) this.gsmBearerCapability).decodeAll(ais);
                            break;
                        case TAG_networkSignalInfo:// 6
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".networkSignalInfo: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.networkSignalInfo = new ExternalSignalInfoImpl();
                            ((ExternalSignalInfoImpl) this.networkSignalInfo).decodeAll(ais);
                            break;
                        case TAG_suppressionOfAnnouncement:// 7
                            ais.readNull();
                            this.suppressionOfAnnouncement = true;
                            break;
                        case TAG_gmscAddress:// 8
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".gmscAddress: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.gmscAddress = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.gmscAddress).decodeAll(ais);
                            break;
                        case TAG_callReferenceNumber:// 9
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".callReferenceNumber: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.callReferenceNumber = new CallReferenceNumberImpl();
                            ((CallReferenceNumberImpl) this.callReferenceNumber).decodeAll(ais);
                            break;
                        case TAG_orInterrogation:// 10
                            ais.readNull();
                            this.orInterrogation = true;
                            break;
                        case TAG_extensionContainer:// 11
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;
                        case TAG_alertingPattern:// 12
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".alertingPattern: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.alertingPattern = new AlertingPatternImpl();
                            ((AlertingPatternImpl) this.alertingPattern).decodeAll(ais);
                            break;
                        case TAG_ccbsCall:// 13
                            ais.readNull();
                            this.ccbsCall = true;
                            break;
                        case TAG_supportedCamelPhasesInInterrogatingNode:// 14
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".supportedCamelPhasesInInterrogatingNode: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.supportedCamelPhasesInInterrogatingNode = new SupportedCamelPhasesImpl();
                            ((SupportedCamelPhasesImpl) this.supportedCamelPhasesInInterrogatingNode).decodeAll(ais);
                            break;
                        case TAG_additionalSignalInfo:// 15
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".additionalSignalInfo: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.additionalSignalInfo = new ExtExternalSignalInfoImpl();
                            ((ExtExternalSignalInfoImpl) this.additionalSignalInfo).decodeAll(ais);
                            break;
                        case TAG_orNotSupportedInGMSC:// 16
                            ais.readNull();
                            this.orNotSupportedInGMSC = true;
                            break;
                        case TAG_prePagingSupported:// 17
                            ais.readNull();
                            this.prePagingSupported = true;
                            break;
                        case TAG_longFTNSupported:// 18
                            ais.readNull();
                            this.longFTNSupported = true;
                            break;
                        case TAG_suppressVtCsi:// 19
                            ais.readNull();
                            this.suppressVtCsi = true;
                            break;
                        case TAG_offeredCamel4CSIsInInterrogatingNode:// 20
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".offeredCamel4CSIsInInterrogatingNode: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.offeredCamel4CSIsInInterrogatingNode = new OfferedCamel4CSIsImpl();
                            ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIsInInterrogatingNode).decodeAll(ais);
                            break;
                        case TAG_mtRoamingRetrySupported:// 21
                            ais.readNull();
                            this.mtRoamingRetrySupported = true;
                            break;
                        case TAG_pagingArea:// 22
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".pagingArea: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.pagingArea = new PagingAreaImpl();
                            ((PagingAreaImpl) this.pagingArea).decodeAll(ais);
                            break;
                        case TAG_callPriority:// 23
                            this.callPriority = EMLPPPriority.getEMLPPPriority((int) ais.readInteger());
                            break;
                        case TAG_mtrfIndicator:// 24
                            ais.readNull();
                            this.mtrfIndicator = true;
                            break;
                        case TAG_oldMSCNumber:// 25
                            if (!ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".oldMSCNumber: is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.oldMSCNumber = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.oldMSCNumber).decodeAll(ais);
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                } else {
                    ais.advanceElement();
                    // break;
                }
            }

            if (this.imsi == null || this.mscNumber == null)
                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + "  V3"
                        + ": imsi,  and mscNumber must not be null", MAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        try {
            this.encodeAll(asnOs, this.getTagClass(), this.getTag());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MAPException(e);
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);

        } catch (AsnException e) {
            e.printStackTrace();
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.imsi == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName + " the mandatory parameter imsi is not defined");
        }

        if (this.mscNumber == null && this.mapProtocolVersion >= 2) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter mscNumber is not defined");
        }

        // 1
        ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_imsi);

        // 2
        if (this.mscNumber != null) {
            ((ISDNAddressStringImpl) this.mscNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_mscNumber);
        }

        // 3
        if (this.msisdn != null) {
            ((ISDNAddressStringImpl) this.msisdn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_msisdn);
        }

        // 4
        if (this.lmsi != null) {
            ((LMSIImpl) this.lmsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_lmsi);
        }

        // 5
        if (this.gsmBearerCapability != null) {
            ((ExternalSignalInfoImpl) this.gsmBearerCapability).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_gsmBearerCapability);
        }

        // 6
        if (this.networkSignalInfo != null) {
            ((ExternalSignalInfoImpl) this.networkSignalInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_networkSignalInfo);
        }

        if (mapProtocolVersion >= 3) {
            // 7
            if (this.suppressionOfAnnouncement) {
                // suppressionOfAnnouncement
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_suppressionOfAnnouncement);
                } catch (IOException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName
                            + " parameter suppressionOfAnnouncement", e);
                } catch (AsnException e) {
                    throw new MAPException("AsnException while encoding " + _PrimitiveName
                            + " parameter suppressionOfAnnouncement", e);
                }
            }

            // 8
            if (this.gmscAddress != null) {
                ((ISDNAddressStringImpl) this.gmscAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_gmscAddress);
            }

            // 9
            if (this.callReferenceNumber != null) {
                ((CallReferenceNumberImpl) this.callReferenceNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        TAG_callReferenceNumber);
            }
            // 10
            if (this.orInterrogation) {
                // orInterrogation
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_orInterrogation);
                } catch (IOException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter orInterrogation", e);
                } catch (AsnException e) {
                    throw new MAPException("AsnException while encoding " + _PrimitiveName + " parameter orInterrogation", e);
                }
            }

            // 11
            if (this.extensionContainer != null) {
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        TAG_extensionContainer);
            }

            // 12
            if (this.alertingPattern != null) {
                ((AlertingPatternImpl) this.alertingPattern).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_alertingPattern);
            }

            // 13
            if (this.ccbsCall) {
                // ccbsCall
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_ccbsCall);
                } catch (IOException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter ccbsCall", e);
                } catch (AsnException e) {
                    throw new MAPException("AsnException while encoding " + _PrimitiveName + " parameter ccbsCall", e);
                }
            }

            // 14
            if (this.supportedCamelPhasesInInterrogatingNode != null) {
                ((SupportedCamelPhasesImpl) this.supportedCamelPhasesInInterrogatingNode).encodeAll(asnOs,
                        Tag.CLASS_CONTEXT_SPECIFIC, TAG_supportedCamelPhasesInInterrogatingNode);
            }

            // 15
            if (this.additionalSignalInfo != null) {
                ((ExtExternalSignalInfoImpl) this.additionalSignalInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        TAG_additionalSignalInfo);
            }

            // 16
            if (this.orNotSupportedInGMSC) {
                // orNotSupportedInGMSC
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_orNotSupportedInGMSC);
                } catch (IOException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter orNotSupportedInGMSC",
                            e);
                } catch (AsnException e) {
                    throw new MAPException("AsnException while encoding " + _PrimitiveName + " parameter orNotSupportedInGMSC",
                            e);
                }
            }

            // 17
            if (this.prePagingSupported) {
                // prePagingSupported
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_prePagingSupported);
                } catch (IOException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter prePagingSupported", e);
                } catch (AsnException e) {
                    throw new MAPException("AsnException while encoding " + _PrimitiveName + " parameter prePagingSupported", e);
                }
            }

            // 18
            if (this.longFTNSupported) {
                // prePagingSupported
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_longFTNSupported);
                } catch (IOException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter longFTNSupported", e);
                } catch (AsnException e) {
                    throw new MAPException("AsnException while encoding " + _PrimitiveName + " parameter longFTNSupported", e);
                }
            }

            // 19
            if (this.suppressVtCsi) {
                // suppressVtCsi
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_suppressVtCsi);
                } catch (IOException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter suppressVtCsi", e);
                } catch (AsnException e) {
                    throw new MAPException("AsnException while encoding " + _PrimitiveName + " parameter suppressVtCsi", e);
                }
            }

            // 20
            if (this.offeredCamel4CSIsInInterrogatingNode != null) {
                ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIsInInterrogatingNode).encodeAll(asnOs,
                        Tag.CLASS_CONTEXT_SPECIFIC, TAG_offeredCamel4CSIsInInterrogatingNode);
            }

            // 21
            if (this.mtRoamingRetrySupported) {
                // mtRoamingRetrySupported
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_mtRoamingRetrySupported);
                } catch (IOException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName
                            + " parameter mtRoamingRetrySupported", e);
                } catch (AsnException e) {
                    throw new MAPException("AsnException while encoding " + _PrimitiveName
                            + " parameter mtRoamingRetrySupported", e);
                }
            }

            // 22
            if (this.pagingArea != null) {
                ((PagingAreaImpl) this.pagingArea).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_pagingArea);
            }

            // 23
            if (this.callPriority != null) {
                try {
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_callPriority, this.callPriority.getCode());
                } catch (IOException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter callPriority", e);
                } catch (AsnException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter callPriority", e);
                }
            }

            // 24
            if (this.mtrfIndicator) {
                // mtrfIndicator
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_mtrfIndicator);
                } catch (IOException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter mtrfIndicator", e);
                } catch (AsnException e) {
                    throw new MAPException("AsnException while encoding " + _PrimitiveName + " parameter mtrfIndicator", e);
                }
            }

            // 25
            if (this.oldMSCNumber != null) {
                ((ISDNAddressStringImpl) this.oldMSCNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_oldMSCNumber);
            }
        }

    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public ISDNAddressString getMscNumber() {
        return this.mscNumber;
    }

    @Override
    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    @Override
    public LMSI getLmsi() {
        return this.lmsi;
    }

    @Override
    public ExternalSignalInfo getGsmBearerCapability() {
        return this.gsmBearerCapability;
    }

    @Override
    public ExternalSignalInfo getNetworkSignalInfo() {
        return this.networkSignalInfo;
    }

    @Override
    public boolean getSuppressionOfAnnouncement() {
        return this.suppressionOfAnnouncement;
    }

    @Override
    public ISDNAddressString getGmscAddress() {
        return this.gmscAddress;
    }

    @Override
    public CallReferenceNumber getCallReferenceNumber() {
        return this.callReferenceNumber;
    }

    @Override
    public boolean getOrInterrogation() {
        return this.orInterrogation;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public AlertingPattern getAlertingPattern() {
        return this.alertingPattern;
    }

    @Override
    public boolean getCcbsCall() {
        return this.ccbsCall;
    }

    @Override
    public SupportedCamelPhases getSupportedCamelPhasesInInterrogatingNode() {
        return this.supportedCamelPhasesInInterrogatingNode;
    }

    @Override
    public ExtExternalSignalInfo getAdditionalSignalInfo() {
        return this.additionalSignalInfo;
    }

    @Override
    public boolean getOrNotSupportedInGMSC() {
        return this.orNotSupportedInGMSC;
    }

    @Override
    public boolean getPrePagingSupported() {
        return this.prePagingSupported;
    }

    @Override
    public boolean getLongFTNSupported() {
        return this.longFTNSupported;
    }

    @Override
    public boolean getSuppressVtCsi() {
        return this.suppressVtCsi;
    }

    @Override
    public OfferedCamel4CSIs getOfferedCamel4CSIsInInterrogatingNode() {
        return this.offeredCamel4CSIsInInterrogatingNode;
    }

    @Override
    public boolean getMtRoamingRetrySupported() {
        return this.mtRoamingRetrySupported;
    }

    @Override
    public PagingArea getPagingArea() {
        return this.pagingArea;
    }

    @Override
    public EMLPPPriority getCallPriority() {
        return this.callPriority;
    }

    @Override
    public boolean getMtrfIndicator() {
        return this.mtrfIndicator;
    }

    @Override
    public ISDNAddressString getOldMSCNumber() {
        return this.oldMSCNumber;
    }

    @Override
    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._PrimitiveName);
        sb.append(" [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi.toString());
            sb.append(", ");
        }
        if (this.mscNumber != null) {
            sb.append("mscNumber=");
            sb.append(mscNumber.toString());
            sb.append(", ");
        }
        if (this.msisdn != null) {
            sb.append("msisdn=");
            sb.append(msisdn.toString());
            sb.append(", ");
        }
        if (this.lmsi != null) {
            sb.append("lmsi=");
            sb.append(lmsi.toString());
            sb.append(", ");
        }
        if (this.gsmBearerCapability != null) {
            sb.append("gsmBearerCapability=");
            sb.append(gsmBearerCapability.toString());
            sb.append(", ");
        }
        if (this.networkSignalInfo != null) {
            sb.append("networkSignalInfo=");
            sb.append(networkSignalInfo.toString());
            sb.append(", ");
        }
        if (this.suppressionOfAnnouncement) {
            sb.append("suppressionOfAnnouncement, ");
        }
        if (this.gmscAddress != null) {
            sb.append("gmscAddress=");
            sb.append(gmscAddress.toString());
            sb.append(", ");
        }
        if (this.callReferenceNumber != null) {
            sb.append("callReferenceNumber=");
            sb.append(callReferenceNumber.toString());
            sb.append(", ");
        }
        if (this.orInterrogation) {
            sb.append("orInterrogation, ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }
        if (this.alertingPattern != null) {
            sb.append("alertingPattern=");
            sb.append(alertingPattern.toString());
            sb.append(", ");
        }
        if (this.ccbsCall) {
            sb.append("ccbsCall, ");
        }
        if (this.supportedCamelPhasesInInterrogatingNode != null) {
            sb.append("supportedCamelPhasesInInterrogatingNode=");
            sb.append(supportedCamelPhasesInInterrogatingNode.toString());
            sb.append(", ");
        }
        if (this.additionalSignalInfo != null) {
            sb.append("additionalSignalInfo=");
            sb.append(additionalSignalInfo.toString());
            sb.append(", ");
        }
        if (this.orNotSupportedInGMSC) {
            sb.append("orNotSupportedInGMSC, ");
        }
        if (this.prePagingSupported) {
            sb.append("prePagingSupported, ");
        }
        if (this.longFTNSupported) {
            sb.append("longFTNSupported, ");
        }
        if (this.suppressVtCsi) {
            sb.append("suppressVtCsi, ");
        }
        if (this.offeredCamel4CSIsInInterrogatingNode != null) {
            sb.append("offeredCamel4CSIsInInterrogatingNode=");
            sb.append(offeredCamel4CSIsInInterrogatingNode.toString());
            sb.append(", ");
        }
        if (this.mtRoamingRetrySupported) {
            sb.append("mtRoamingRetrySupported, ");
        }
        if (this.pagingArea != null) {
            sb.append("pagingArea=");
            sb.append(pagingArea.toString());
            sb.append(", ");
        }
        if (this.callPriority != null) {
            sb.append("callPriority=");
            sb.append(callPriority.toString());
            sb.append(", ");
        }
        if (this.mtrfIndicator) {
            sb.append("mtrfIndicator, ");
        }
        if (this.oldMSCNumber != null) {
            sb.append("oldMSCNumber=");
            sb.append(oldMSCNumber.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }
}
