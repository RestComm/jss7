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

package org.mobicents.protocols.ss7.map.service.mobility.authentication;

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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.PlmnIdImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SendAuthenticationInfoRequestImpl extends MobilityMessageImpl implements SendAuthenticationInfoRequest {

    protected static final int _TAG_imsi = 0;
    protected static final int _TAG_immediateResponsePreferred = 1;
    protected static final int _TAG_extensionContainer = 2;
    protected static final int _TAG_requestingNodeType = 3;
    protected static final int _TAG_requestingPLMNId = 4;
    protected static final int _TAG_numberOfRequestedAdditionalVectors = 5;
    protected static final int _TAG_additionalVectorsAreForEPS = 6;

    public static final String _PrimitiveName = "SendAuthenticationInfoRequest";

    private IMSI imsi;
    private int numberOfRequestedVectors;
    private boolean segmentationProhibited;
    private boolean immediateResponsePreferred;
    private ReSynchronisationInfo reSynchronisationInfo;
    private MAPExtensionContainer extensionContainer;
    private RequestingNodeType requestingNodeType;
    private PlmnId requestingPlmnId;
    private Integer numberOfRequestedAdditionalVectors;
    private boolean additionalVectorsAreForEPS;
    private long mapProtocolVersion;

    public SendAuthenticationInfoRequestImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendAuthenticationInfoRequestImpl(long mapProtocolVersion, IMSI imsi, int numberOfRequestedVectors,
            boolean segmentationProhibited, boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo,
            MAPExtensionContainer extensionContainer, RequestingNodeType requestingNodeType, PlmnId requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) {
        this.mapProtocolVersion = mapProtocolVersion;
        this.imsi = imsi;
        this.numberOfRequestedVectors = numberOfRequestedVectors;
        this.segmentationProhibited = segmentationProhibited;
        this.immediateResponsePreferred = immediateResponsePreferred;
        this.reSynchronisationInfo = reSynchronisationInfo;
        this.extensionContainer = extensionContainer;
        this.requestingNodeType = requestingNodeType;
        this.requestingPlmnId = requestingPlmnId;
        this.numberOfRequestedAdditionalVectors = numberOfRequestedAdditionalVectors;
        this.additionalVectorsAreForEPS = additionalVectorsAreForEPS;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.sendAuthenticationInfo_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.sendAuthenticationInfo;
    }

    public IMSI getImsi() {
        return imsi;
    }

    public int getNumberOfRequestedVectors() {
        return numberOfRequestedVectors;
    }

    public boolean getSegmentationProhibited() {
        return segmentationProhibited;
    }

    public boolean getImmediateResponsePreferred() {
        return immediateResponsePreferred;
    }

    public ReSynchronisationInfo getReSynchronisationInfo() {
        return reSynchronisationInfo;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public RequestingNodeType getRequestingNodeType() {
        return requestingNodeType;
    }

    public PlmnId getRequestingPlmnId() {
        return requestingPlmnId;
    }

    public Integer getNumberOfRequestedAdditionalVectors() {
        return numberOfRequestedAdditionalVectors;
    }

    public boolean getAdditionalVectorsAreForEPS() {
        return additionalVectorsAreForEPS;
    }

    public long getMapProtocolVersion() {
        return mapProtocolVersion;
    }

    public int getTag() throws MAPException {
        if (this.mapProtocolVersion >= 3) {
            return Tag.SEQUENCE;
        } else {
            return Tag.STRING_OCTET;
        }
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        if (this.mapProtocolVersion >= 3) {
            return false;
        } else {
            return true;
        }
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        imsi = null;
        numberOfRequestedVectors = 0;
        segmentationProhibited = false;
        immediateResponsePreferred = false;
        reSynchronisationInfo = null;
        extensionContainer = null;
        requestingNodeType = null;
        requestingPlmnId = null;
        numberOfRequestedAdditionalVectors = null;
        additionalVectorsAreForEPS = false;

        if (mapProtocolVersion >= 3) {
            AsnInputStream ais = ansIS.readSequenceStreamData(length);
            int num = 0;
            while (true) {
                if (ais.available() == 0)
                    break;

                int tag = ais.readTag();

                switch (num) {
                    case 0:
                        // imsi
                        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_imsi)
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".imsi: Parameter 0 bad tag or tag class or not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.imsi = new IMSIImpl();
                        ((IMSIImpl) this.imsi).decodeAll(ais);
                        break;

                    case 1:
                        // numberOfRequestedVectors
                        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.INTEGER)
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".numberOfRequestedVectors: Parameter 1 bad tag class or tag or not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.numberOfRequestedVectors = (int) ais.readInteger();
                        break;

                    default:
                        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                            switch (tag) {
                                case _TAG_immediateResponsePreferred:
                                    // immediateResponsePreferred
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".immediateResponsePreferred: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    ais.readNull();
                                    this.immediateResponsePreferred = true;
                                    break;
                                case _TAG_extensionContainer:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".extensionContainer: Parameter extensionContainer is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.extensionContainer = new MAPExtensionContainerImpl();
                                    ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                                    break;
                                case _TAG_requestingNodeType:
                                    // requestingNodeType
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".requestingNodeType: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    int i1 = (int) ais.readInteger();
                                    this.requestingNodeType = RequestingNodeType.getInstance(i1);
                                    break;
                                case _TAG_requestingPLMNId:
                                    // requestingPlmnId
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".requestingPlmnId: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.requestingPlmnId = new PlmnIdImpl();
                                    ((PlmnIdImpl) this.requestingPlmnId).decodeAll(ais);
                                    break;
                                case _TAG_numberOfRequestedAdditionalVectors:
                                    // numberOfRequestedAdditionalVectors
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".numberOfRequestedAdditionalVectors: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.numberOfRequestedAdditionalVectors = (int) ais.readInteger();
                                    break;
                                case _TAG_additionalVectorsAreForEPS:
                                    // additionalVectorsAreForEPS
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".additionalVectorsAreForEPS: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    ais.readNull();
                                    this.additionalVectorsAreForEPS = true;
                                    break;

                                default:
                                    ais.advanceElement();
                                    break;
                            }
                        } else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

                            switch (tag) {
                                case Tag.NULL:
                                    // segmentationProhibited
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".segmentationProhibited: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    ais.readNull();
                                    this.segmentationProhibited = true;
                                    break;
                                case Tag.SEQUENCE:
                                    // re-synchronisationInfo
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".re-synchronisationInfo: Parameter is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.reSynchronisationInfo = new ReSynchronisationInfoImpl();
                                    ((ReSynchronisationInfoImpl) this.reSynchronisationInfo).decodeAll(ais);
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

            if (num < 2)
                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                        + ": Needs at least 2 mandatory parameters, found " + num,
                        MAPParsingComponentExceptionReason.MistypedParameter);
        } else {

            this.imsi = new IMSIImpl();
            ((IMSIImpl) this.imsi).decodeData(ansIS, length);
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            // if (this.mapProtocolVersion >= 3)
            // asnOs.writeTag(tagClass, false, tag);
            // else
            // asnOs.writeTag(tagClass, true, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.mapProtocolVersion <= 2) {
            if (this.imsi == null)
                throw new MAPException("Imsi must not be null for MAP Version2");
            ((IMSIImpl) this.imsi).encodeData(asnOs);

        } else {
            try {
                if (this.imsi == null)
                    throw new MAPException("IMSI parameter must not be null for MAP Version3");

                ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_imsi);
                asnOs.writeInteger(numberOfRequestedVectors);

                if (segmentationProhibited)
                    asnOs.writeNull();
                if (immediateResponsePreferred)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_immediateResponsePreferred);
                if (reSynchronisationInfo != null) {
                    ((ReSynchronisationInfoImpl) this.reSynchronisationInfo).encodeAll(asnOs);
                }
                if (this.extensionContainer != null)
                    ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
                if (requestingNodeType != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_requestingNodeType, requestingNodeType.getCode());
                if (this.requestingPlmnId != null)
                    ((PlmnIdImpl) this.requestingPlmnId).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_requestingPLMNId);
                if (numberOfRequestedAdditionalVectors != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_numberOfRequestedAdditionalVectors,
                            numberOfRequestedAdditionalVectors);
                if (additionalVectorsAreForEPS)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_additionalVectorsAreForEPS);

            } catch (IOException e) {
                throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi.toString());
            sb.append(", ");
        }
        sb.append("numberOfRequestedVectors=");
        sb.append(numberOfRequestedVectors);
        sb.append(", ");
        if (this.segmentationProhibited) {
            sb.append("segmentationProhibited, ");
        }
        if (this.immediateResponsePreferred) {
            sb.append("immediateResponsePreferred, ");
        }
        if (this.reSynchronisationInfo != null) {
            sb.append("reSynchronisationInfo=");
            sb.append(reSynchronisationInfo.toString());
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }
        if (this.requestingNodeType != null) {
            sb.append("requestingNodeType=");
            sb.append(requestingNodeType.toString());
            sb.append(", ");
        }
        if (this.requestingPlmnId != null) {
            sb.append("requestingPlmnId=");
            sb.append(requestingPlmnId.toString());
            sb.append(", ");
        }
        if (this.numberOfRequestedAdditionalVectors != null) {
            sb.append("numberOfRequestedAdditionalVectors=");
            sb.append(numberOfRequestedAdditionalVectors.toString());
            sb.append(", ");
        }
        if (this.additionalVectorsAreForEPS) {
            sb.append("additionalVectorsAreForEPS, ");
        }
        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }
}
