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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;

/**
 * Start time:12:43:13 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author sergey vetyutnev
 */
public class UserTeleserviceInformationImpl extends AbstractISUPParameter implements UserTeleserviceInformation {

    private static final String CODING_STANDARD = "codingStandard";
    private static final String INTERPRETATION = "interpretation";
    private static final String PRESENTATION_METHOD = "presentationMethod";
    private static final String HIGHLAYER_CHAR_IDENTIFICATION = "highLayerCharIdentification";
    private static final String E_HIGH_LAYER_CHAR_IDENTIFICATION = "eHighLayerCharIdentification";
    private static final String E_VIDEO_TELEPHONY_CHAR_IDENTIFICATION = "eVideoTelephonyCharIdentification";

    private static final int DEFAULT_INT_VALUE = 0;

    private int codingStandard;
    private int interpretation;
    private int presentationMethod;
    private int highLayerCharIdentification;

    // I hate this, its soo jsr 17 style...
    private boolean eHighLayerCharIdentificationPresent;
    private boolean eVideoTelephonyCharIdentificationPresent;

    private int eHighLayerCharIdentification;
    private int eVideoTelephonyCharIdentification;

    public UserTeleserviceInformationImpl() {
        super();

    }

    public UserTeleserviceInformationImpl(int codingStandard, int interpretation, int presentationMethod,
            int highLayerCharIdentification) {
        super();
        this.setCodingStandard(codingStandard);
        this.setInterpretation(interpretation);
        this.setPresentationMethod(presentationMethod);
        this.setHighLayerCharIdentification(highLayerCharIdentification);
    }

    public UserTeleserviceInformationImpl(int codingStandard, int interpretation, int presentationMethod,
            int highLayerCharIdentification, int eVideoTelephonyCharIdentification, boolean notImportantIgnoredParameter) {
        super();
        this.setCodingStandard(codingStandard);
        this.setInterpretation(interpretation);
        this.setPresentationMethod(presentationMethod);
        this.setHighLayerCharIdentification(highLayerCharIdentification);
        setEVideoTelephonyCharIdentification(eVideoTelephonyCharIdentification);
    }

    public UserTeleserviceInformationImpl(int codingStandard, int interpretation, int presentationMethod,
            int highLayerCharIdentification, int eHighLayerCharIdentification) {
        super();
        this.setCodingStandard(codingStandard);
        this.setInterpretation(interpretation);
        this.setPresentationMethod(presentationMethod);
        this.setHighLayerCharIdentification(highLayerCharIdentification);
        this.setEHighLayerCharIdentification(eHighLayerCharIdentification);
    }

    public UserTeleserviceInformationImpl(byte[] b) throws ParameterException {
        super();
        // FIXME: this is only elementID

        decode(b);
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length < 2) {
            throw new ParameterException("byte[] must not be null and length must be greater than  1");
        }

        try {
            this.setPresentationMethod(b[0]);
            this.setInterpretation((b[0] >> 2));
            this.setCodingStandard((b[0] >> 5));
            this.setHighLayerCharIdentification(b[1]);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
        boolean ext = ((b[1] >> 7) & 0x01) == 0;
        if (ext && b.length < 3) {
            throw new ParameterException(
                    "byte[] indicates extension to high layer cahracteristic indicator, however there isnt enough bytes");
        }
        if (!ext) {
            return b.length;
        }

        // FIXME: add check for excesive byte?, it should not happen though?
        if (this.highLayerCharIdentification == _HLCI_MAINTAINENCE || this.highLayerCharIdentification == _HLCI_MANAGEMENT) {
            this.setEHighLayerCharIdentification(b[2] & 0x7F);
            // } else if ((this.highLayerCharIdentification >= _HLCI_AUDIO_VID_LOW_RANGE && this.highLayerCharIdentification <=
            // _HLCI_AUDIO_VID_HIGH_RANGE)
            // || (this.highLayerCharIdentification >= _HLCI_AUDIO_VID_LOW_RANGE2 && this.highLayerCharIdentification <=
            // _HLCI_AUDIO_VID_HIGH_RANGE2)) {
        } else if ((this.highLayerCharIdentification >= _HLCI_VIDEOTELEPHONY && this.highLayerCharIdentification <= _HLCI_AUDIO_VID_HIGH_RANGE2)) {
            this.setEVideoTelephonyCharIdentification(b[2] & 0x7F);
        } else {
            // logger.warning("HLCI indicates value which does not allow for extension, but its present....");
        }
        return b.length;
    }

    public byte[] encode() throws ParameterException {
        byte[] b = null;
        if (this.eHighLayerCharIdentificationPresent || this.eVideoTelephonyCharIdentificationPresent) {
            b = new byte[3];
        } else {
            b = new byte[2];
        }

        int v = 0;
        v = this.presentationMethod & 0x03;
        v |= (this.interpretation & 0x07) << 2;
        v |= (this.codingStandard & 0x03) << 5;
        v |= 0x80;

        b[0] = (byte) v;
        b[1] = (byte) (this.highLayerCharIdentification & 0x7F);
        if (this.eHighLayerCharIdentificationPresent || this.eVideoTelephonyCharIdentificationPresent) {

            if (this.eHighLayerCharIdentificationPresent) {
                b[2] = (byte) (0x80 | (this.eHighLayerCharIdentification & 0x7F));
            } else {
                b[2] = (byte) (0x80 | (this.eVideoTelephonyCharIdentification & 0x7F));
            }
            return b;
        } else {
            b[1] |= 0x80;
            return b;
        }
    }

    public int getCodingStandard() {
        return codingStandard;
    }

    public void setCodingStandard(int codingStandard) {
        this.codingStandard = codingStandard & 0x03;
    }

    public int getInterpretation() {
        return interpretation;
    }

    public void setInterpretation(int interpretation) {
        this.interpretation = interpretation & 0x07;
    }

    public int getPresentationMethod() {
        return presentationMethod;
    }

    public void setPresentationMethod(int presentationMethod) {
        this.presentationMethod = presentationMethod & 0x03;
    }

    public int getHighLayerCharIdentification() {
        return highLayerCharIdentification;
    }

    public void setHighLayerCharIdentification(int highLayerCharIdentification) {
        this.highLayerCharIdentification = highLayerCharIdentification & 0x7F;
    }

    public int getEHighLayerCharIdentification() {
        return eHighLayerCharIdentification;
    }

    public void setEHighLayerCharIdentification(int highLayerCharIdentification) {

        if (this.eVideoTelephonyCharIdentificationPresent) {
            throw new IllegalStateException(
                    "Either Extended VideoTlephony or Extended HighLayer octet is set. ExtendedVideoTlephony is already present");
        }
        if (this.highLayerCharIdentification == _HLCI_MAINTAINENCE || this.highLayerCharIdentification == _HLCI_MANAGEMENT) {
            this.eHighLayerCharIdentificationPresent = true;
            this.eHighLayerCharIdentification = highLayerCharIdentification & 0x7F;
        } else {
            throw new IllegalArgumentException("Can not set this octet - HLCI is of value: " + this.highLayerCharIdentification);
        }
    }

    public int getEVideoTelephonyCharIdentification() {
        return eVideoTelephonyCharIdentification;
    }

    public void setEVideoTelephonyCharIdentification(int eVidedoTelephonyCharIdentification) {
        if (this.eHighLayerCharIdentificationPresent) {
            throw new IllegalStateException(
                    "Either Extended VideoTlephony or Extended HighLayer octet is set. ExtendedHighLayer is already present");
        }
        // if ((this.highLayerCharIdentification >= _HLCI_AUDIO_VID_LOW_RANGE && this.highLayerCharIdentification <=
        // _HLCI_AUDIO_VID_HIGH_RANGE)
        // || (this.highLayerCharIdentification >= _HLCI_AUDIO_VID_LOW_RANGE2 && this.highLayerCharIdentification <=
        // _HLCI_AUDIO_VID_HIGH_RANGE2)) {
        if ((this.highLayerCharIdentification >= _HLCI_VIDEOTELEPHONY && this.highLayerCharIdentification <= _HLCI_AUDIO_VID_HIGH_RANGE2)) {
            this.eVideoTelephonyCharIdentificationPresent = true;
            this.eVideoTelephonyCharIdentification = eVidedoTelephonyCharIdentification & 0x7F;
        } else {
            throw new IllegalArgumentException("Can not set this octet - HLCI is of value: " + this.highLayerCharIdentification);
        }
    }

    public boolean isEHighLayerCharIdentificationPresent() {
        return eHighLayerCharIdentificationPresent;
    }

    public boolean isEVideoTelephonyCharIdentificationPresent() {
        return eVideoTelephonyCharIdentificationPresent;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("UserTeleserviceInformation [codingStandard=");
        sb.append(codingStandard);
        sb.append(", interpretation=");
        sb.append(interpretation);
        sb.append(", presentationMethod=");
        sb.append(presentationMethod);
        sb.append(", highLayerCharIdentification=");
        sb.append(highLayerCharIdentification);

        if (this.eHighLayerCharIdentificationPresent) {
            sb.append(", eHighLayerCharIdentification=");
            sb.append(eHighLayerCharIdentification);
        }

        if (this.eVideoTelephonyCharIdentificationPresent) {
            sb.append(", eVideoTelephonyCharIdentification=");
            sb.append(this.eVideoTelephonyCharIdentification);
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<UserTeleserviceInformationImpl> ISUP_USER_TELESERVICE_INFORMATION_XML = new XMLFormat<UserTeleserviceInformationImpl>(
            UserTeleserviceInformationImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, UserTeleserviceInformationImpl userTeleserviceInformation)
                throws XMLStreamException {
            userTeleserviceInformation.codingStandard = xml.getAttribute(CODING_STANDARD, DEFAULT_INT_VALUE);
            userTeleserviceInformation.interpretation = xml.getAttribute(INTERPRETATION, DEFAULT_INT_VALUE);
            userTeleserviceInformation.presentationMethod = xml.getAttribute(PRESENTATION_METHOD, DEFAULT_INT_VALUE);
            userTeleserviceInformation.highLayerCharIdentification = xml.getAttribute(HIGHLAYER_CHAR_IDENTIFICATION,
                    DEFAULT_INT_VALUE);

            int val = xml.getAttribute(E_HIGH_LAYER_CHAR_IDENTIFICATION, Integer.MIN_VALUE);
            if (val != Integer.MIN_VALUE)
                userTeleserviceInformation.setEHighLayerCharIdentification(val);

            val = xml.getAttribute(E_VIDEO_TELEPHONY_CHAR_IDENTIFICATION, Integer.MIN_VALUE);
            if (val != Integer.MIN_VALUE)
                userTeleserviceInformation.setEVideoTelephonyCharIdentification(val);
        }

        @Override
        public void write(UserTeleserviceInformationImpl userTeleserviceInformation, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(CODING_STANDARD, userTeleserviceInformation.codingStandard);
            xml.setAttribute(INTERPRETATION, userTeleserviceInformation.interpretation);
            xml.setAttribute(PRESENTATION_METHOD, userTeleserviceInformation.presentationMethod);
            xml.setAttribute(HIGHLAYER_CHAR_IDENTIFICATION, userTeleserviceInformation.highLayerCharIdentification);

            if (userTeleserviceInformation.eHighLayerCharIdentificationPresent)
                xml.setAttribute(E_HIGH_LAYER_CHAR_IDENTIFICATION, userTeleserviceInformation.eHighLayerCharIdentification);

            if (userTeleserviceInformation.eVideoTelephonyCharIdentificationPresent)
                xml.setAttribute(E_VIDEO_TELEPHONY_CHAR_IDENTIFICATION,
                        userTeleserviceInformation.eVideoTelephonyCharIdentification);
        }
    };
}
