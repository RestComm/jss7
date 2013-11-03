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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberResponse;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ProvideRoamingNumberResponseImpl extends CallHandlingMessageImpl implements ProvideRoamingNumberResponse {

    public ISDNAddressString roamingNumber;
    public MAPExtensionContainer extensionContainer;
    public boolean releaseResourcesSupported;
    public ISDNAddressString vmscAddress;
    private long mapProtocolVersion;
    public static final String _PrimitiveName = "ProvideRoamingNumberResponse";

    public ProvideRoamingNumberResponseImpl(ISDNAddressString roamingNumber, MAPExtensionContainer extensionContainer,
            boolean releaseResourcesSupported, ISDNAddressString vmscAddress, long mapProtocolVersion) {
        super();
        this.roamingNumber = roamingNumber;
        this.extensionContainer = extensionContainer;
        this.releaseResourcesSupported = releaseResourcesSupported;
        this.vmscAddress = vmscAddress;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public ProvideRoamingNumberResponseImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.privideRoamingNumber_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.provideRoamingNumber;
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

    @Override
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            e.printStackTrace();
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            e.printStackTrace();
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.roamingNumber = null;
        this.extensionContainer = null;
        this.releaseResourcesSupported = false;
        this.vmscAddress = null;
        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        if (this.mapProtocolVersion >= 3) {

            int num = 0;

            while (true) {
                if (ais.available() == 0)
                    break;

                int tag = ais.readTag();

                if (num == 0) {
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL && tag != Tag.STRING_OCTET && !ais.isTagPrimitive()) {
                        throw new MAPParsingComponentException("Error while decoding  " + _PrimitiveName + " "
                                + ".roamingNumber: Parameter 0 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    }

                    this.roamingNumber = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.roamingNumber).decodeAll(ais);
                    num++;
                } else {
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        if (tag == Tag.STRING_OCTET) {
                            this.vmscAddress = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.vmscAddress).decodeAll(ais);
                        } else if (tag == Tag.SEQUENCE) {
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        } else if (tag == Tag.NULL) {
                            ais.readNull();
                            this.releaseResourcesSupported = true;
                        } else {
                            ais.advanceElement();
                        }

                    } else {
                        ais.advanceElement();
                    }
                }

            }
        } else {
            this.roamingNumber = new ISDNAddressStringImpl();
            ((ISDNAddressStringImpl) this.roamingNumber).decodeData(ais, length);
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
            throw new MAPException("AsnException when encoding " + _PrimitiveName + " : " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.roamingNumber == null)
            throw new MAPException("roamingNumber parameter must not be null");

        try {

            if (this.mapProtocolVersion >= 3) {

                ((ISDNAddressStringImpl) this.roamingNumber).encodeAll(asnOs);

                if (this.extensionContainer != null) {
                    ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
                }
                if (this.releaseResourcesSupported) {
                    asnOs.writeNull();
                }
                if (this.vmscAddress != null) {
                    ((ISDNAddressStringImpl) this.vmscAddress).encodeAll(asnOs);
                }

            } else {
                ((ISDNAddressStringImpl) this.roamingNumber).encodeData(asnOs);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + " " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + " " + e.getMessage(), e);
        }

    }

    @Override
    public ISDNAddressString getRoamingNumber() {
        return this.roamingNumber;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public boolean getReleaseResourcesSupported() {
        return this.releaseResourcesSupported;
    }

    @Override
    public ISDNAddressString getVmscAddress() {
        return this.vmscAddress;
    }

    @Override
    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.roamingNumber != null) {
            sb.append("roamingNumber=");
            sb.append(roamingNumber.toString());
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }
        if (this.releaseResourcesSupported) {
            sb.append("releaseResourcesSupported, ");
        }
        if (this.vmscAddress != null) {
            sb.append("vmscAddress=");
            sb.append(vmscAddress.toString());
            sb.append(", ");
        }

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        sb.append("]");

        return sb.toString();
    }
}
