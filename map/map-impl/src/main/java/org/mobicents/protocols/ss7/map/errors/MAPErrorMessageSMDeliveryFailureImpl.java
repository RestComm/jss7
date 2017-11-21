/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.mobicents.protocols.ss7.map.errors;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ByteArrayContainer;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.mobicents.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.smstpdu.SmsTpduImpl;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
public class MAPErrorMessageSMDeliveryFailureImpl extends MAPErrorMessageImpl implements MAPErrorMessageSMDeliveryFailure {

    private static final String MAP_PROTOCOL_VERSION = "mapProtocolVersion";
    private static final String SM_ENUMERATE_DEL_FAIL_CAUSE = "sMEnumeratedDeliveryFailureCause";
    private static final String SIGNAL_INFO = "signalInfo";
    private static final String MAP_EXTENSION_CONTAINER = "mapExtensionContainer";

    private long mapProtocolVersion;
    private SMEnumeratedDeliveryFailureCause sMEnumeratedDeliveryFailureCause;
    private byte[] signalInfo;
    private MAPExtensionContainer extensionContainer;

    public MAPErrorMessageSMDeliveryFailureImpl(long mapProtocolVersion,
            SMEnumeratedDeliveryFailureCause smEnumeratedDeliveryFailureCause, byte[] signalInfo,
            MAPExtensionContainer extensionContainer) {
        super((long) MAPErrorCode.smDeliveryFailure);

        this.mapProtocolVersion = mapProtocolVersion;
        this.sMEnumeratedDeliveryFailureCause = smEnumeratedDeliveryFailureCause;
        this.signalInfo = signalInfo;
        this.extensionContainer = extensionContainer;
    }

    public MAPErrorMessageSMDeliveryFailureImpl() {
        super((long) MAPErrorCode.smDeliveryFailure);
    }

    public SMEnumeratedDeliveryFailureCause getSMEnumeratedDeliveryFailureCause() {
        return this.sMEnumeratedDeliveryFailureCause;
    }

    public byte[] getSignalInfo() {
        return this.signalInfo;
    }

    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public void setSMEnumeratedDeliveryFailureCause(SMEnumeratedDeliveryFailureCause sMEnumeratedDeliveryFailureCause) {
        this.sMEnumeratedDeliveryFailureCause = sMEnumeratedDeliveryFailureCause;
    }

    public void setSignalInfo(byte[] signalInfo) {
        this.signalInfo = signalInfo;
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setMapProtocolVersion(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public boolean isEmSMDeliveryFailure() {
        return true;
    }

    public MAPErrorMessageSMDeliveryFailure getEmSMDeliveryFailure() {
        return this;
    }

    public int getTag() throws MAPException {
        if (this.mapProtocolVersion == 1)
            return Tag.ENUMERATED;
        else
            return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        if (this.mapProtocolVersion == 1)
            return true;
        else
            return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPErrorMessageSMDeliveryFailure: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageSMDeliveryFailure: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPErrorMessageSMDeliveryFailure: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageSMDeliveryFailure: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.sMEnumeratedDeliveryFailureCause = null;
        this.signalInfo = null;
        this.extensionContainer = null;

        if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL)
            throw new MAPParsingComponentException("Error decoding MAPErrorMessageSMDeliveryFailure: bad tag class",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        switch (localAis.getTag()) {
            case Tag.ENUMERATED:
                if (!localAis.isTagPrimitive())
                    throw new MAPParsingComponentException(
                            "Error decoding MAPErrorMessageSMDeliveryFailure: ENUMERATED tag but data is not primitive",
                            MAPParsingComponentExceptionReason.MistypedParameter);
                int code = (int) localAis.readIntegerData(length);
                this.sMEnumeratedDeliveryFailureCause = SMEnumeratedDeliveryFailureCause.getInstance(code);
                if (this.sMEnumeratedDeliveryFailureCause == null)
                    throw new MAPParsingComponentException(
                            "Error decoding MAPErrorMessageSMDeliveryFailure.sMEnumeratedDeliveryFailureCause: bad code value",
                            MAPParsingComponentExceptionReason.MistypedParameter);
                this.mapProtocolVersion = 1;
                break;

            case Tag.SEQUENCE:
                if (localAis.isTagPrimitive())
                    throw new MAPParsingComponentException(
                            "Error decoding MAPErrorMessageCallBarred: SEQUENCE tag but data is primitive",
                            MAPParsingComponentExceptionReason.MistypedParameter);
                AsnInputStream ais = localAis.readSequenceStreamData(length);

                int tag = ais.readTag();
                if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || tag != Tag.ENUMERATED)
                    throw new MAPParsingComponentException(
                            "Error decoding MAPErrorMessageSMDeliveryFailure.sMEnumeratedDeliveryFailureCause: bad tag class or tag",
                            MAPParsingComponentExceptionReason.MistypedParameter);
                code = (int) ais.readInteger();
                this.sMEnumeratedDeliveryFailureCause = SMEnumeratedDeliveryFailureCause.getInstance(code);
                if (this.sMEnumeratedDeliveryFailureCause == null)
                    throw new MAPParsingComponentException(
                            "Error decoding MAPErrorMessageSMDeliveryFailure.sMEnumeratedDeliveryFailureCause: bad value",
                            MAPParsingComponentExceptionReason.MistypedParameter);

                while (true) {
                    if (ais.available() == 0)
                        break;

                    tag = ais.readTag();

                    switch (ais.getTagClass()) {
                        case Tag.CLASS_UNIVERSAL:
                            switch (tag) {
                                case Tag.STRING_OCTET:
                                    this.signalInfo = ais.readOctetString();
                                    break;

                                case Tag.SEQUENCE:
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

                this.mapProtocolVersion = 3;
                break;
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPErrorMessageSMDeliveryFailure: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream aos) throws MAPException {

        if (this.sMEnumeratedDeliveryFailureCause == null)
            throw new MAPException(
                    "Error encoding MAPErrorMessageSMDeliveryFailure: parameter sMEnumeratedDeliveryFailureCause must not be null");

        try {
            if (this.mapProtocolVersion < 3) {
                aos.writeIntegerData(this.sMEnumeratedDeliveryFailureCause.getCode());
            } else {
                aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, this.sMEnumeratedDeliveryFailureCause.getCode());

                if (this.signalInfo != null)
                    aos.writeOctetString(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, this.signalInfo);
                if (this.extensionContainer != null)
                    ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(aos);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding MAPErrorMessageSMDeliveryFailure: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPErrorMessageSMDeliveryFailure: " + e.getMessage(), e);
        }
    }

    public SmsDeliverReportTpdu getSmsDeliverReportTpdu() throws MAPException {
        if (this.signalInfo != null) {
            SmsTpdu smsTpdu = SmsTpduImpl.createInstance(this.signalInfo, true, null);
            if (smsTpdu.getSmsTpduType() == SmsTpduType.SMS_DELIVER_REPORT) {
                SmsDeliverReportTpdu drTpdu = (SmsDeliverReportTpdu) smsTpdu;
                return drTpdu;
            }
        }
        return null;
    }

    public void setSmsDeliverReportTpdu(SmsDeliverReportTpdu tpdu) throws MAPException {
        this.signalInfo = tpdu.encodeData();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageSMDeliveryFailure [");
        if (this.sMEnumeratedDeliveryFailureCause != null)
            sb.append("sMEnumeratedDeliveryFailureCause=" + this.sMEnumeratedDeliveryFailureCause.toString());
        if (this.signalInfo != null)
            sb.append(", signalInfo=" + this.printDataArr(this.signalInfo));
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        sb.append("]");

        return sb.toString();
    }

    private String printDataArr(byte[] data) {
        StringBuilder sb = new StringBuilder();
        if (data != null) {
            for (int b : data) {
                sb.append(b);
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MAPErrorMessageSMDeliveryFailureImpl> MAP_ERROR_MESSAGE_SM_DEL_FAILURE_XML = new XMLFormat<MAPErrorMessageSMDeliveryFailureImpl>(
            MAPErrorMessageSMDeliveryFailureImpl.class) {

        // TODO: we need to think of parsing of SignallingInfo into XML components (now we just write a byte array)

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MAPErrorMessageSMDeliveryFailureImpl errorMessage)
                throws XMLStreamException {
            MAP_ERROR_MESSAGE_XML.read(xml, errorMessage);
            errorMessage.mapProtocolVersion = xml.get(MAP_PROTOCOL_VERSION, Long.class);

            String str = xml.get(SM_ENUMERATE_DEL_FAIL_CAUSE, String.class);
            if (str != null)
                errorMessage.sMEnumeratedDeliveryFailureCause = Enum.valueOf(SMEnumeratedDeliveryFailureCause.class, str);

            ByteArrayContainer bc = xml.get(SIGNAL_INFO, ByteArrayContainer.class);
            if (bc != null) {
                errorMessage.signalInfo = bc.getData();
            }

            errorMessage.extensionContainer = xml.get(MAP_EXTENSION_CONTAINER, MAPExtensionContainerImpl.class);
        }

        @Override
        public void write(MAPErrorMessageSMDeliveryFailureImpl errorMessage, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            MAP_ERROR_MESSAGE_XML.write(errorMessage, xml);
            xml.add(errorMessage.getMapProtocolVersion(), MAP_PROTOCOL_VERSION, Long.class);

            if (errorMessage.getSMEnumeratedDeliveryFailureCause() != null)
                xml.add((String) errorMessage.getSMEnumeratedDeliveryFailureCause().toString(), SM_ENUMERATE_DEL_FAIL_CAUSE,
                        String.class);

            if (errorMessage.signalInfo != null) {
                ByteArrayContainer bac = new ByteArrayContainer(errorMessage.signalInfo);
                xml.add(bac, SIGNAL_INFO, ByteArrayContainer.class);
            }

            xml.add((MAPExtensionContainerImpl) errorMessage.extensionContainer, MAP_EXTENSION_CONTAINER,
                    MAPExtensionContainerImpl.class);
        }
    };
}
