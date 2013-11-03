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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContext;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.AuthenticationSetListImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.CurrentSecurityContextImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SendIdentificationResponseImpl extends MobilityMessageImpl implements SendIdentificationResponse {

    protected static final int _TAG_currentSecurityContext = 2;
    protected static final int _TAG_extensionContainer = 3;

    public static final int _TAG_SendIdentificationResponse = 3;

    public static final String _PrimitiveName = "SendIdentificationResponse";

    private IMSI imsi;
    private AuthenticationSetList authenticationSetList;
    private CurrentSecurityContext currentSecurityContext;
    private MAPExtensionContainer extensionContainer;
    private long mapProtocolVersion;

    public SendIdentificationResponseImpl(long mapProtocolVersion) {
        super();
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendIdentificationResponseImpl(IMSI imsi, AuthenticationSetList authenticationSetList,
            CurrentSecurityContext currentSecurityContext, MAPExtensionContainer extensionContainer, long mapProtocolVersion) {
        super();
        this.imsi = imsi;
        this.authenticationSetList = authenticationSetList;
        this.currentSecurityContext = currentSecurityContext;
        this.extensionContainer = extensionContainer;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.sendIdentification_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.sendIdentification;
    }

    @Override
    public int getTag() throws MAPException {
        if (this.mapProtocolVersion >= 3) {
            return SendIdentificationResponseImpl._TAG_SendIdentificationResponse;
        } else {
            return Tag.SEQUENCE;
        }
    }

    @Override
    public int getTagClass() {
        if (this.mapProtocolVersion >= 3) {
            return Tag.CLASS_CONTEXT_SPECIFIC;
        } else {
            return Tag.CLASS_UNIVERSAL;
        }
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public AuthenticationSetList getAuthenticationSetList() {
        return this.authenticationSetList;
    }

    @Override
    public CurrentSecurityContext getCurrentSecurityContext() {
        return this.currentSecurityContext;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public long getMapProtocolVersion() {
        return mapProtocolVersion;
    }

    @Override
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

    @Override
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

        this.imsi = null;
        this.authenticationSetList = null;
        this.currentSecurityContext = null;
        this.extensionContainer = null;

        if (this.mapProtocolVersion >= 3) {
            AsnInputStream ais = ansIS.readSequenceStreamData(length);

            while (true) {
                if (ais.available() == 0)
                    break;

                int tag = ais.readTag();

                switch (ais.getTagClass()) {
                    case Tag.CLASS_UNIVERSAL:
                        switch (tag) {
                            case Tag.STRING_OCTET:
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".imsi: is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.imsi = new IMSIImpl();
                                ((IMSIImpl) this.imsi).decodeAll(ais);
                                break;

                            default:
                                ais.advanceElement();
                                break;
                        }
                        break;

                    case Tag.CLASS_CONTEXT_SPECIFIC:
                        switch (tag) {
                            case AuthenticationSetListImpl._TAG_tripletList:
                            case AuthenticationSetListImpl._TAG_quintupletList:
                                this.authenticationSetList = new AuthenticationSetListImpl();
                                ((AuthenticationSetListImpl) this.authenticationSetList).decodeAll(ais);
                                break;
                            case _TAG_currentSecurityContext:
                                AsnInputStream ais2 = ais.readSequenceStream();
                                ais2.readTag();
                                this.currentSecurityContext = new CurrentSecurityContextImpl();
                                ((CurrentSecurityContextImpl) this.currentSecurityContext).decodeAll(ais2);
                                break;
                            case _TAG_extensionContainer:
                                if (ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".extensionContainer: is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.extensionContainer = new MAPExtensionContainerImpl();
                                ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                                break;
                            default:
                                ais.advanceElement();
                                break;
                        }
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            }
        } else {
            AsnInputStream ais = ansIS.readSequenceStreamData(length);

            while (true) {
                if (ais.available() == 0)
                    break;

                int tag = ais.readTag();

                switch (ais.getTagClass()) {
                    case Tag.CLASS_UNIVERSAL:
                        switch (tag) {
                            case Tag.STRING_OCTET:
                                if (!ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".imsi: is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.imsi = new IMSIImpl();
                                ((IMSIImpl) this.imsi).decodeAll(ais);
                                break;
                            case Tag.SEQUENCE:
                                if (ais.isTagPrimitive()) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".authenticationSetList: is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                }
                                this.authenticationSetList = new AuthenticationSetListImpl();
                                ((AuthenticationSetListImpl) this.authenticationSetList).decodeAll(ais);
                                break;

                            default:
                                ais.advanceElement();
                                break;
                        }
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            }

            if (this.imsi == null) {
                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                        + ": imsi is mandatory for MAP V2 but not found ", MAPParsingComponentExceptionReason.MistypedParameter);
            }
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (mapProtocolVersion >= 3) {
            try {
                if (this.imsi != null)
                    ((IMSIImpl) this.imsi).encodeAll(asnOs);

                if (this.authenticationSetList != null)
                    ((AuthenticationSetListImpl) this.authenticationSetList).encodeAll(asnOs);

                if (this.currentSecurityContext != null) {
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_currentSecurityContext);
                    int pos = asnOs.StartContentDefiniteLength();
                    ((CurrentSecurityContextImpl) this.currentSecurityContext).encodeAll(asnOs);
                    asnOs.FinalizeContent(pos);

                }
                if (this.extensionContainer != null)
                    ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            _TAG_extensionContainer);

            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
            }
        } else {
            if (this.imsi == null) {
                throw new MAPException("Error while encoding " + _PrimitiveName
                        + " the mandatory parameter imsi is not defined");
            }

            ((IMSIImpl) this.imsi).encodeAll(asnOs);

            if (this.authenticationSetList != null)
                ((AuthenticationSetListImpl) this.authenticationSetList).encodeAll(asnOs);
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

        if (this.authenticationSetList != null) {
            sb.append("authenticationSetList=");
            sb.append(this.authenticationSetList.toString());
            sb.append(", ");
        }

        if (this.currentSecurityContext != null) {
            sb.append("currentSecurityContext=");
            sb.append(this.currentSecurityContext.toString());
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}
