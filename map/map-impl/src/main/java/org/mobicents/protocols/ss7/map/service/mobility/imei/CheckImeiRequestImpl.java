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

package org.mobicents.protocols.ss7.map.service.mobility.imei;

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
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

/**
 * @author normandes
 *
 */
public class CheckImeiRequestImpl extends MobilityMessageImpl implements CheckImeiRequest {

    public static final String _PrimitiveName = "CheckImeiRequest";

    private IMEI imei = null;
    private RequestedEquipmentInfo requestedEquipmentInfo = null;
    private MAPExtensionContainer extensionContainer = null;

    private IMSI imsi = null;

    private long mapProtocolVersion;
    private int encodedLength;

    // For incoming messages
    public CheckImeiRequestImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    // For outgoing messages
    public CheckImeiRequestImpl(long mapProtocolVersion, IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
            MAPExtensionContainer extensionContainer) {
        this.mapProtocolVersion = mapProtocolVersion;
        this.imei = imei;
        this.requestedEquipmentInfo = requestedEquipmentInfo;
        this.extensionContainer = extensionContainer;
    }

    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    @Override
    public int getTag() throws MAPException {
        if (this.mapProtocolVersion >= 3) {
            return Tag.SEQUENCE;
        } else {
            return Tag.STRING_OCTET;
        }
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        if (this.mapProtocolVersion >= 3) {
            return false;
        } else {
            return true;
        }
    }

    public int getEncodedLength() {
        return encodedLength;
    }

    public IMSI getIMSI() {
        return imsi;
    }

    public void setIMSI(IMSI imsi) {
        this.imsi = imsi;
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
        this.imei = null;
        this.requestedEquipmentInfo = null;
        this.extensionContainer = null;
        this.imsi = null;

        if (mapProtocolVersion >= 3) {
            AsnInputStream ais = ansIS.readSequenceStreamData(length);
            int num = 0;
            while (true) {
                if (ais.available() == 0) {
                    break;
                }

                int tag = ais.readTag();

                switch (num) {
                    case 0:
                        // imei
                        if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".imei: bad tag or tag class or is not primitive: TagClass=" + ais.getTagClass()
                                    + ", tag=" + tag, MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.imei = new IMEIImpl();
                        ((IMEIImpl) this.imei).decodeAll(ais);
                        break;
                    case 1:
                        // requestedEquipmentInfo
                        if (tag != Tag.STRING_BIT || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".requestedEquipmentInfo: bad tag or tag class or is not primitive: TagClass="
                                    + ais.getTagClass() + ", tag=" + tag, MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.requestedEquipmentInfo = new RequestedEquipmentInfoImpl();
                        ((RequestedEquipmentInfoImpl) this.requestedEquipmentInfo).decodeAll(ais);
                        break;
                    default:
                        if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter extensionContainer is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
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
            this.imei = new IMEIImpl();
            ((IMEIImpl) this.imei).decodeData(ansIS, length);

            // To decode IMSI in Huawei package
            if (ansIS.available() != 0) {
                int tag = ansIS.readTag();
                length = ansIS.readLength();
                if (tag != 0 && ansIS.getTagClass() != Tag.CLASS_UNIVERSAL && !ansIS.isTagPrimitive()) {
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ".imsi: bad tag or tag class or is not primitive: TagClass=" + ansIS.getTagClass() + ", tag="
                            + tag, MAPParsingComponentExceptionReason.MistypedParameter);
                }

                this.imsi = new IMSIImpl();
                ((IMSIImpl) this.imsi).decodeData(ansIS, length);
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
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            if (this.imsi == null) {
                int pos = asnOs.StartContentDefiniteLength();
                this.encodeData(asnOs);
                asnOs.FinalizeContent(pos);
            } else {
                AsnOutputStream tempAos = new AsnOutputStream();
                this.encodeData(tempAos);
                asnOs.writeLength(this.getEncodedLength());
                this.encodeData(asnOs);
            }
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.imei == null) {
            throw new MAPException("imei parameter must not be null");
        }

        if (mapProtocolVersion >= 3) {
            if (this.requestedEquipmentInfo == null) {
                throw new MAPException("requestedEquipmentInfo parameter must not be null");
            }
            ((IMEIImpl) this.imei).encodeAll(asnOs);
            ((RequestedEquipmentInfoImpl) this.requestedEquipmentInfo).encodeAll(asnOs);
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
        } else {
            ((IMEIImpl) this.imei).encodeData(asnOs);
            encodedLength = asnOs.size();

            if (imsi != null) {
                ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_UNIVERSAL, 0);
            }
        }
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.checkIMEI_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.checkIMEI;
    }

    @Override
    public IMEI getIMEI() {
        return this.imei;
    }

    @Override
    public RequestedEquipmentInfo getRequestedEquipmentInfo() {
        return this.requestedEquipmentInfo;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.imei != null) {
            sb.append("imei=");
            sb.append(imei.toString());
            sb.append(", ");
        }

        if (this.requestedEquipmentInfo != null) {
            sb.append("requestedEquipmentInfo=");
            sb.append(requestedEquipmentInfo.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }

}
