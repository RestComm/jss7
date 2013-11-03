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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class PurgeMSRequestImpl extends MobilityMessageImpl implements PurgeMSRequest {

    protected static final int _TAG_vlrNumber = 0;
    protected static final int _TAG_sgsnNumber = 1;

    public static final int _TAG_PurgeMSRequest = 3;

    public static final String _PrimitiveName = "PurgeMSRequest";

    private IMSI imsi;
    private ISDNAddressString vlrNumber;
    private ISDNAddressString sgsnNumber;
    private MAPExtensionContainer extensionContainer;
    private long mapProtocolVersion;

    public PurgeMSRequestImpl(long mapProtocolVersion) {
        super();
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public PurgeMSRequestImpl(IMSI imsi, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber,
            MAPExtensionContainer extensionContainer, long mapProtocolVersion) {
        super();
        this.imsi = imsi;
        this.vlrNumber = vlrNumber;
        this.sgsnNumber = sgsnNumber;
        this.extensionContainer = extensionContainer;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.purgeMS_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.purgeMS;
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public ISDNAddressString getVlrNumber() {
        return this.vlrNumber;
    }

    @Override
    public ISDNAddressString getSgsnNumber() {
        return this.sgsnNumber;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public int getTag() throws MAPException {
        if (this.mapProtocolVersion >= 3) {
            return _TAG_PurgeMSRequest;
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
        this.vlrNumber = null;
        this.sgsnNumber = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (num == 0) {
                if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive()) {
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ".imsi: Parameter 0 bad tag or tag class or not primitive",
                            MAPParsingComponentExceptionReason.MistypedParameter);

                }
                this.imsi = new IMSIImpl();
                ((IMSIImpl) this.imsi).decodeAll(ais);

            } else if (num == 1 && this.mapProtocolVersion < 3) {
                if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive()) {
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ".imsi: Parameter 0 bad tag or tag class or not primitive",
                            MAPParsingComponentExceptionReason.MistypedParameter);

                }
                this.vlrNumber = new ISDNAddressStringImpl();
                ((ISDNAddressStringImpl) this.vlrNumber).decodeAll(ais);

            } else {

                if (this.mapProtocolVersion >= 3) {

                    switch (ais.getTagClass()) {
                        case Tag.CLASS_UNIVERSAL:
                            switch (tag) {
                                case Tag.SEQUENCE:
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

                        case Tag.CLASS_CONTEXT_SPECIFIC:
                            switch (tag) {
                                case PurgeMSRequestImpl._TAG_vlrNumber:
                                    if (!ais.isTagPrimitive()) {
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".vlrNumber: is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    }
                                    this.vlrNumber = new ISDNAddressStringImpl();
                                    ((ISDNAddressStringImpl) this.vlrNumber).decodeAll(ais);
                                    break;
                                case PurgeMSRequestImpl._TAG_sgsnNumber:
                                    if (!ais.isTagPrimitive()) {
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".sgsnNumber: is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    }
                                    this.sgsnNumber = new ISDNAddressStringImpl();
                                    ((ISDNAddressStringImpl) this.sgsnNumber).decodeAll(ais);
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
                } else {

                }
            }

            num++;
        }

        if (this.mapProtocolVersion < 3) {
            if (this.vlrNumber == null) {
                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                        + ": vlrNumber is mandatory for MAP V2 but not found ",
                        MAPParsingComponentExceptionReason.MistypedParameter);
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

        if (this.imsi == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName + " the mandatory parameter imsi is not defined");
        }

        if (mapProtocolVersion >= 3) {

            ((IMSIImpl) this.imsi).encodeAll(asnOs);

            if (this.vlrNumber != null)
                ((ISDNAddressStringImpl) this.vlrNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_vlrNumber);

            if (this.sgsnNumber != null)
                ((ISDNAddressStringImpl) this.sgsnNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_sgsnNumber);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);

        } else {

            if (this.vlrNumber == null) {
                throw new MAPException("Error while encoding " + _PrimitiveName
                        + " the mandatory parameter vlrNumber is not defined");
            }

            ((IMSIImpl) this.imsi).encodeAll(asnOs);

            ((ISDNAddressStringImpl) this.vlrNumber).encodeAll(asnOs);

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

        if (this.vlrNumber != null) {
            sb.append("vlrNumber=");
            sb.append(vlrNumber.toString());
            sb.append(", ");
        }

        if (this.sgsnNumber != null) {
            sb.append("sgsnNumber=");
            sb.append(sgsnNumber.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}
