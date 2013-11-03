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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ByteArrayContainer;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class IPSSPCapabilitiesImpl implements IPSSPCapabilities, CAPAsnPrimitive {

    public static int _Mask_IPRoutingAddress = 0x01;
    public static int _Mask_VoiceBack = 0x02;
    public static int _Mask_VoiceInformation_SpeechRecognition = 0x04;
    public static int _Mask_VoiceInformation_VoiceRecognition = 0x08;
    public static int _Mask_GenerationOfVoiceAnnouncementsFromTextSupported = 0x10;

    private static final String IP_ROUTING_ADDRESS_SUPPORTED = "ipRoutingAddressSupported";
    private static final String VOICE_BACK_SUPPORTED = "voiceBackSupported";
    private static final String VOICE_INFORMATION_SUPPORTED_VIA_SPEECH_RECOGNITION = "voiceInformationSupportedViaSpeechRecognition";
    private static final String VOICE_INFORMATION_SUPPORTED_VIA_VOICE_RECOGNITION = "voiceInformationSupportedViaVoiceRecognition";
    private static final String GENERATION_OF_VOICE_ANNOUNCEMENTS_FROM_TEXT_SUPPORTED = "generationOfVoiceAnnouncementsFromTextSupported";
    private static final String EXTRA_DATA = "extraData";

    public static final String _PrimitiveName = "IPSSPCapabilities";

    private byte[] data;

    public IPSSPCapabilitiesImpl() {
    }

    public IPSSPCapabilitiesImpl(byte[] data) {
        this.data = data;
    }

    public IPSSPCapabilitiesImpl(boolean IPRoutingAddressSupported, boolean VoiceBackSupported,
            boolean VoiceInformationSupportedViaSpeechRecognition, boolean VoiceInformationSupportedViaVoiceRecognition,
            boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[] extraData) {
        setData(IPRoutingAddressSupported, VoiceBackSupported, VoiceInformationSupportedViaSpeechRecognition,
                VoiceInformationSupportedViaVoiceRecognition, GenerationOfVoiceAnnouncementsFromTextSupported, extraData);
    }

    public void setData(boolean IPRoutingAddressSupported, boolean VoiceBackSupported,
            boolean VoiceInformationSupportedViaSpeechRecognition, boolean VoiceInformationSupportedViaVoiceRecognition,
            boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[] extraData) {
        int firstByte = (IPRoutingAddressSupported ? _Mask_IPRoutingAddress : 0) | (VoiceBackSupported ? _Mask_VoiceBack : 0)
                | (VoiceInformationSupportedViaSpeechRecognition ? _Mask_VoiceInformation_SpeechRecognition : 0)
                | (VoiceInformationSupportedViaVoiceRecognition ? _Mask_VoiceInformation_VoiceRecognition : 0)
                | (GenerationOfVoiceAnnouncementsFromTextSupported ? _Mask_GenerationOfVoiceAnnouncementsFromTextSupported : 0);
        int extraCnt = 0;
        if (extraData != null)
            extraCnt = extraData.length;
        if (extraCnt > 3)
            extraCnt = 3;

        this.data = new byte[1 + extraCnt];
        this.data[0] = (byte) firstByte;
        if (extraCnt > 0)
            System.arraycopy(extraData, 0, this.data, 1, extraCnt);
    }

    @Override
    public byte[] getData() {
        return this.data;
    }

    @Override
    public boolean getIPRoutingAddressSupported() {

        if (this.data == null || this.data.length == 0)
            return false;

        return (((int) this.data[0]) & _Mask_IPRoutingAddress) != 0;
    }

    @Override
    public boolean getVoiceBackSupported() {

        if (this.data == null || this.data.length == 0)
            return false;

        return (((int) this.data[0]) & _Mask_VoiceBack) != 0;
    }

    @Override
    public boolean getVoiceInformationSupportedViaSpeechRecognition() {

        if (this.data == null || this.data.length == 0)
            return false;

        return (((int) this.data[0]) & _Mask_VoiceInformation_SpeechRecognition) != 0;
    }

    @Override
    public boolean getVoiceInformationSupportedViaVoiceRecognition() {

        if (this.data == null || this.data.length == 0)
            return false;

        return (((int) this.data[0]) & _Mask_VoiceInformation_VoiceRecognition) != 0;
    }

    @Override
    public boolean getGenerationOfVoiceAnnouncementsFromTextSupported() {

        if (this.data == null || this.data.length == 0)
            return false;

        return (((int) this.data[0]) & _Mask_GenerationOfVoiceAnnouncementsFromTextSupported) != 0;
    }

    @Override
    public byte[] getExtraData() {

        if (this.data == null || this.data.length < 2)
            return null;

        int extraCount = this.data.length;
        if (extraCount > 3)
            extraCount = 3;
        byte[] res = new byte[extraCount];
        System.arraycopy(this.data, 1, res, 0, extraCount);
        return res;
    }

    @Override
    public int getTag() throws CAPException {
        return Tag.STRING_OCTET;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.data = ansIS.readOctetStringData(length);
        if (this.data.length < 1 || this.data.length > 4)
            throw new CAPParsingComponentException("Error decoding " + _PrimitiveName
                    + ": length must be from 1 to 4, real length = " + length,
                    CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.data == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": data field must not be null");
        if (this.data.length < 1 || this.data.length > 4)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": data field length must be from 1 to 4");

        asnOs.writeOctetStringData(data);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (data != null) {
            if (this.getIPRoutingAddressSupported())
                sb.append("IPRoutingAddressSupported, ");
            if (this.getVoiceBackSupported())
                sb.append("VoiceBackSupported, ");
            if (this.getVoiceInformationSupportedViaSpeechRecognition())
                sb.append("VoiceInformationSupportedViaSpeechRecognition, ");
            if (this.getVoiceInformationSupportedViaVoiceRecognition())
                sb.append("VoiceInformationSupportedViaVoiceRecognition, ");
            if (this.getGenerationOfVoiceAnnouncementsFromTextSupported())
                sb.append("GenerationOfVoiceAnnouncementsFromTextSupported, ");
            byte[] eArr = this.getExtraData();
            if (eArr != null) {
                sb.append("ExtraData=");
                for (int i1 = 0; i1 < eArr.length; i1++) {
                    sb.append(eArr[i1]);
                    sb.append(", ");
                }
            }
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<IPSSPCapabilitiesImpl> IPSSP_CAPABILITIES_XML = new XMLFormat<IPSSPCapabilitiesImpl>(
            IPSSPCapabilitiesImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, IPSSPCapabilitiesImpl ipsspCapabilities)
                throws XMLStreamException {
            boolean IPRoutingAddressSupported = xml.getAttribute(IP_ROUTING_ADDRESS_SUPPORTED, false);
            boolean voiceBackSupported = xml.getAttribute(VOICE_BACK_SUPPORTED, false);
            boolean voiceInformationSupportedViaSpeechRecognition = xml.getAttribute(
                    VOICE_INFORMATION_SUPPORTED_VIA_SPEECH_RECOGNITION, false);
            boolean voiceInformationSupportedViaVoiceRecognition = xml.getAttribute(
                    VOICE_INFORMATION_SUPPORTED_VIA_VOICE_RECOGNITION, false);
            boolean generationOfVoiceAnnouncementsFromTextSupported = xml.getAttribute(
                    GENERATION_OF_VOICE_ANNOUNCEMENTS_FROM_TEXT_SUPPORTED, false);

            ByteArrayContainer bc = xml.get(EXTRA_DATA, ByteArrayContainer.class);
            byte[] extraData = null;
            if (bc != null) {
                extraData = bc.getData();
            }

            ipsspCapabilities.setData(IPRoutingAddressSupported, voiceBackSupported,
                    voiceInformationSupportedViaSpeechRecognition, voiceInformationSupportedViaVoiceRecognition,
                    generationOfVoiceAnnouncementsFromTextSupported, extraData);
        }

        @Override
        public void write(IPSSPCapabilitiesImpl ipsspCapabilities, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (ipsspCapabilities.getIPRoutingAddressSupported())
                xml.setAttribute(IP_ROUTING_ADDRESS_SUPPORTED, ipsspCapabilities.getIPRoutingAddressSupported());
            if (ipsspCapabilities.getVoiceBackSupported())
                xml.setAttribute(VOICE_BACK_SUPPORTED, ipsspCapabilities.getVoiceBackSupported());
            if (ipsspCapabilities.getVoiceInformationSupportedViaSpeechRecognition())
                xml.setAttribute(VOICE_INFORMATION_SUPPORTED_VIA_SPEECH_RECOGNITION,
                        ipsspCapabilities.getVoiceInformationSupportedViaSpeechRecognition());
            if (ipsspCapabilities.getVoiceInformationSupportedViaVoiceRecognition())
                xml.setAttribute(VOICE_INFORMATION_SUPPORTED_VIA_VOICE_RECOGNITION,
                        ipsspCapabilities.getVoiceInformationSupportedViaVoiceRecognition());
            if (ipsspCapabilities.getGenerationOfVoiceAnnouncementsFromTextSupported())
                xml.setAttribute(GENERATION_OF_VOICE_ANNOUNCEMENTS_FROM_TEXT_SUPPORTED,
                        ipsspCapabilities.getGenerationOfVoiceAnnouncementsFromTextSupported());

            if (ipsspCapabilities.getExtraData() != null && ipsspCapabilities.getExtraData().length > 0) {
                ByteArrayContainer bac = new ByteArrayContainer(ipsspCapabilities.getExtraData());
                xml.add(bac, EXTRA_DATA, ByteArrayContainer.class);
            }
        }
    };
}
