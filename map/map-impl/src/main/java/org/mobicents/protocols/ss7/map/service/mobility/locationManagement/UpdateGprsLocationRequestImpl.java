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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class UpdateGprsLocationRequestImpl extends MobilityMessageImpl implements UpdateGprsLocationRequest {

    private static final int TAG_sgsnCapability = 0;
    private static final int TAG_informPreviousNetworkEntity = 1;
    private static final int TAG_psLCSNotSupportedByUE = 2;
    private static final int TAG_vGmlcAddress = 3;
    private static final int TAG_addInfo = 4;
    private static final int TAG_epsInfo = 5;
    private static final int TAG_servingNodeTypeIndicator = 6;
    private static final int TAG_skipSubscriberDataUpdate = 7;
    private static final int TAG_usedRATType = 8;
    private static final int TAG_gprsSubscriptionDataNotNeeded = 9;
    private static final int TAG_nodeTypeIndicator = 10;
    private static final int TAG_areaRestricted = 11;
    private static final int TAG_ueReachableIndicator = 12;
    private static final int TAG_epsSubscriptionDataNotNeeded = 13;
    private static final int TAG_uesrvccCapability = 14;

    public static final String _PrimitiveName = "UpdateGprsLocationRequest";

    private IMSI imsi;
    private ISDNAddressString sgsnNumber;
    private GSNAddress sgsnAddress;
    private MAPExtensionContainer extensionContainer;
    private SGSNCapability sgsnCapability;
    private boolean informPreviousNetworkEntity;
    private boolean psLCSNotSupportedByUE;
    private GSNAddress vGmlcAddress;
    private ADDInfo addInfo;
    private EPSInfo epsInfo;
    private boolean servingNodeTypeIndicator;
    private boolean skipSubscriberDataUpdate;
    private UsedRATType usedRATType;
    private boolean gprsSubscriptionDataNotNeeded;
    private boolean nodeTypeIndicator;
    private boolean areaRestricted;
    private boolean ueReachableIndicator;
    private boolean epsSubscriptionDataNotNeeded;
    private UESRVCCCapability uesrvccCapability;

    public UpdateGprsLocationRequestImpl() {
        super();
    }

    public UpdateGprsLocationRequestImpl(IMSI imsi, ISDNAddressString sgsnNumber, GSNAddress sgsnAddress,
            MAPExtensionContainer extensionContainer, SGSNCapability sgsnCapability, boolean informPreviousNetworkEntity,
            boolean psLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo, EPSInfo epsInfo,
            boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
            boolean gprsSubscriptionDataNotNeeded, boolean nodeTypeIndicator, boolean areaRestricted,
            boolean ueReachableIndicator, boolean epsSubscriptionDataNotNeeded, UESRVCCCapability uesrvccCapability,
            long mapProtocolVersion) {
        super();
        this.imsi = imsi;
        this.sgsnNumber = sgsnNumber;
        this.sgsnAddress = sgsnAddress;
        this.extensionContainer = extensionContainer;
        this.sgsnCapability = sgsnCapability;
        this.informPreviousNetworkEntity = informPreviousNetworkEntity;
        this.psLCSNotSupportedByUE = psLCSNotSupportedByUE;
        this.vGmlcAddress = vGmlcAddress;
        this.addInfo = addInfo;
        this.epsInfo = epsInfo;
        this.servingNodeTypeIndicator = servingNodeTypeIndicator;
        this.skipSubscriberDataUpdate = skipSubscriberDataUpdate;
        this.usedRATType = usedRATType;
        this.gprsSubscriptionDataNotNeeded = gprsSubscriptionDataNotNeeded;
        this.nodeTypeIndicator = nodeTypeIndicator;
        this.areaRestricted = areaRestricted;
        this.ueReachableIndicator = ueReachableIndicator;
        this.epsSubscriptionDataNotNeeded = epsSubscriptionDataNotNeeded;
        this.uesrvccCapability = uesrvccCapability;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.updateGprsLocation_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.updateGprsLocation;
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public ISDNAddressString getSgsnNumber() {
        return this.sgsnNumber;
    }

    @Override
    public GSNAddress getSgsnAddress() {
        return this.sgsnAddress;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public SGSNCapability getSGSNCapability() {
        return this.sgsnCapability;
    }

    @Override
    public boolean getInformPreviousNetworkEntity() {
        return this.informPreviousNetworkEntity;
    }

    @Override
    public boolean getPsLCSNotSupportedByUE() {
        return this.psLCSNotSupportedByUE;
    }

    @Override
    public GSNAddress getVGmlcAddress() {
        return this.vGmlcAddress;
    }

    @Override
    public ADDInfo getADDInfo() {
        return this.addInfo;
    }

    @Override
    public EPSInfo getEPSInfo() {
        return this.epsInfo;
    }

    @Override
    public boolean getServingNodeTypeIndicator() {
        return this.servingNodeTypeIndicator;
    }

    @Override
    public boolean getSkipSubscriberDataUpdate() {
        return this.skipSubscriberDataUpdate;
    }

    @Override
    public UsedRATType getUsedRATType() {
        return this.usedRATType;
    }

    @Override
    public boolean getGprsSubscriptionDataNotNeeded() {
        return this.gprsSubscriptionDataNotNeeded;
    }

    @Override
    public boolean getNodeTypeIndicator() {
        return this.nodeTypeIndicator;
    }

    @Override
    public boolean getAreaRestricted() {
        return this.areaRestricted;
    }

    @Override
    public boolean getUeReachableIndicator() {
        return this.ueReachableIndicator;
    }

    @Override
    public boolean getEpsSubscriptionDataNotNeeded() {
        return this.epsSubscriptionDataNotNeeded;
    }

    @Override
    public UESRVCCCapability getUESRVCCCapability() {
        return this.uesrvccCapability;
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
        this.sgsnNumber = null;
        this.sgsnAddress = null;
        this.extensionContainer = null;
        this.sgsnCapability = null;
        this.informPreviousNetworkEntity = false;
        this.psLCSNotSupportedByUE = false;
        this.vGmlcAddress = null;
        this.addInfo = null;
        this.epsInfo = null;
        this.servingNodeTypeIndicator = false;
        this.skipSubscriberDataUpdate = false;
        this.usedRATType = null;
        this.gprsSubscriptionDataNotNeeded = false;
        this.nodeTypeIndicator = false;
        this.areaRestricted = false;
        this.ueReachableIndicator = false;
        this.epsSubscriptionDataNotNeeded = false;
        this.uesrvccCapability = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    // imsi
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".imsi: Parameter 0 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.imsi = new IMSIImpl();
                    ((IMSIImpl) this.imsi).decodeAll(ais);
                    break;

                case 1:
                    // sgsnNumber
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".imsi: Parameter 0 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.sgsnNumber = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.sgsnNumber).decodeAll(ais);
                    break;
                case 2:
                    // sgsnAddress
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".vlrNumber: Parameter 2 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.sgsnAddress = new GSNAddressImpl();
                    ((GSNAddressImpl) this.sgsnAddress).decodeAll(ais);
                    break;

                default:
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        switch (tag) {
                            case Tag.SEQUENCE:
                                // extensionContainer
                                if (ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".extensionContainer: Parameter extensionContainer is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                this.extensionContainer = new MAPExtensionContainerImpl();
                                ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                                break;
                            default:
                                ais.advanceElement();
                                break;
                        }
                    } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                        switch (tag) {
                            case TAG_sgsnCapability: // sgsnCapability
                                if (ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".sgsnCapability: Parameter is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                this.sgsnCapability = new SGSNCapabilityImpl();
                                ((SGSNCapabilityImpl) this.sgsnCapability).decodeAll(ais);
                                break;
                            case TAG_informPreviousNetworkEntity: // informPreviousNetworkEntity
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".informPreviousNetworkEntity: Parameter is  not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                ais.readNull();
                                this.informPreviousNetworkEntity = true;
                                break;
                            case TAG_psLCSNotSupportedByUE:
                                // tpsLCSNotSupportedByUE
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".tpsLCSNotSupportedByUE: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                ais.readNull();
                                this.psLCSNotSupportedByUE = true;
                                break;
                            case TAG_vGmlcAddress:
                                // vmlcAddress
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".vGmlcAddress: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                this.vGmlcAddress = new GSNAddressImpl();
                                ((GSNAddressImpl) this.vGmlcAddress).decodeAll(ais);
                                break;
                            case TAG_addInfo: // addInfo
                                if (ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".addInfo: Parameter is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                this.addInfo = new ADDInfoImpl();
                                ((ADDInfoImpl) this.addInfo).decodeAll(ais);
                                break;
                            case TAG_epsInfo: // epsInfo
                                if (ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".epsInfo: Parameter is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                AsnInputStream ais2 = ais.readSequenceStream();
                                ais2.readTag();
                                this.epsInfo = new EPSInfoImpl();
                                ((EPSInfoImpl) this.epsInfo).decodeAll(ais2);
                                break;
                            case TAG_servingNodeTypeIndicator:
                                // servingNodeTypeIndicator
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".servingNodeTypeIndicator: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                ais.readNull();
                                this.servingNodeTypeIndicator = true;
                                break;
                            case TAG_skipSubscriberDataUpdate:
                                // skipSubscriberDataUpdate
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".skipSubscriberDataUpdate: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                ais.readNull();
                                this.skipSubscriberDataUpdate = true;
                                break;
                            case TAG_usedRATType: // sgsnCapability
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".usedRATType: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                int raType = (int) ais.readInteger();
                                this.usedRATType = UsedRATType.getInstance(raType);
                                break;
                            case TAG_gprsSubscriptionDataNotNeeded:
                                // gprsSubscriptionDataNotNeeded
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".gprsSubscriptionDataNotNeeded: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                ais.readNull();
                                this.gprsSubscriptionDataNotNeeded = true;
                                break;
                            case TAG_nodeTypeIndicator:
                                // nodeTypeIndicator
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".nodeTypeIndicator: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                ais.readNull();
                                this.nodeTypeIndicator = true;
                                break;
                            case TAG_areaRestricted:
                                // areaRestricted
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".areaRestricted: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                ais.readNull();
                                this.areaRestricted = true;
                                break;
                            case TAG_ueReachableIndicator:
                                // ueReachableIndicator
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".ueReachableIndicator: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                ais.readNull();
                                this.ueReachableIndicator = true;
                                break;
                            case TAG_epsSubscriptionDataNotNeeded:
                                // epsSubscriptionDataNotNeeded
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".epsSubscriptionDataNotNeeded: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                ais.readNull();
                                this.epsSubscriptionDataNotNeeded = true;
                                break;
                            case TAG_uesrvccCapability: // uesrvccCapability
                                if (!ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".uesrvccCapability: Parameter is not primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                int vccCapability = (int) ais.readInteger();
                                this.uesrvccCapability = UESRVCCCapability.getInstance(vccCapability);
                                break;
                            default:
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

        if (this.imsi == null || this.sgsnNumber == null || this.sgsnAddress == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": imsi or sgsnNumber or sgsnAddress is null ", MAPParsingComponentExceptionReason.MistypedParameter);

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

        try {
            if (this.imsi == null || this.sgsnNumber == null || this.sgsnAddress == null)
                throw new MAPException("imsi, sgsnNumber and sgsnAddress parameter must not be null");

            ((IMSIImpl) this.imsi).encodeAll(asnOs);

            ((ISDNAddressStringImpl) this.sgsnNumber).encodeAll(asnOs);

            ((GSNAddressImpl) this.sgsnAddress).encodeAll(asnOs);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);

            if (this.sgsnCapability != null)
                ((SGSNCapabilityImpl) this.sgsnCapability).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_sgsnCapability);

            if (informPreviousNetworkEntity)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_informPreviousNetworkEntity);

            if (psLCSNotSupportedByUE)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_psLCSNotSupportedByUE);

            if (this.vGmlcAddress != null)
                ((GSNAddressImpl) this.vGmlcAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_vGmlcAddress);

            if (this.addInfo != null)
                ((ADDInfoImpl) this.addInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_addInfo);

            if (this.epsInfo != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_epsInfo);
                int pos = asnOs.StartContentDefiniteLength();
                ((EPSInfoImpl) this.epsInfo).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            }

            if (servingNodeTypeIndicator)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_servingNodeTypeIndicator);

            if (skipSubscriberDataUpdate)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_skipSubscriberDataUpdate);

            if (this.usedRATType != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_usedRATType, this.usedRATType.getCode());

            if (gprsSubscriptionDataNotNeeded)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_gprsSubscriptionDataNotNeeded);

            if (nodeTypeIndicator)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_nodeTypeIndicator);

            if (areaRestricted)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_areaRestricted);

            if (ueReachableIndicator)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_ueReachableIndicator);

            if (epsSubscriptionDataNotNeeded)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_epsSubscriptionDataNotNeeded);

            if (this.uesrvccCapability != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_uesrvccCapability, this.uesrvccCapability.getCode());

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(this.imsi.toString());
            sb.append(", ");
        }

        if (this.sgsnNumber != null) {
            sb.append("sgsnNumber=");
            sb.append(this.sgsnNumber.toString());
            sb.append(", ");
        }

        if (this.sgsnAddress != null) {
            sb.append("sgsnAddress=");
            sb.append(this.sgsnAddress.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.sgsnCapability != null) {
            sb.append("sgsnCapability=");
            sb.append(this.sgsnCapability.toString());
            sb.append(", ");
        }

        if (this.informPreviousNetworkEntity) {
            sb.append("informPreviousNetworkEntity, ");
        }

        if (this.psLCSNotSupportedByUE) {
            sb.append("psLCSNotSupportedByUE, ");
        }

        if (this.vGmlcAddress != null) {
            sb.append("vGmlcAddress=");
            sb.append(this.vGmlcAddress.toString());
            sb.append(", ");
        }

        if (this.addInfo != null) {
            sb.append("addInfo=");
            sb.append(this.addInfo.toString());
            sb.append(", ");
        }

        if (this.epsInfo != null) {
            sb.append("epsInfo=");
            sb.append(this.epsInfo.toString());
            sb.append(", ");
        }

        if (this.servingNodeTypeIndicator) {
            sb.append("servingNodeTypeIndicator, ");
        }

        if (this.skipSubscriberDataUpdate) {
            sb.append("skipSubscriberDataUpdate, ");
        }

        if (this.usedRATType != null) {
            sb.append("usedRATType=");
            sb.append(this.usedRATType.toString());
            sb.append(", ");
        }

        if (this.gprsSubscriptionDataNotNeeded) {
            sb.append("gprsSubscriptionDataNotNeeded, ");
        }

        if (this.nodeTypeIndicator) {
            sb.append("nodeTypeIndicator, ");
        }

        if (this.areaRestricted) {
            sb.append("areaRestricted, ");
        }

        if (this.ueReachableIndicator) {
            sb.append("ueReachableIndicator, ");
        }

        if (this.epsSubscriptionDataNotNeeded) {
            sb.append("epsSubscriptionDataNotNeeded, ");
        }

        if (this.uesrvccCapability != null) {
            sb.append("uesrvccCapability=");
            sb.append(this.uesrvccCapability.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
