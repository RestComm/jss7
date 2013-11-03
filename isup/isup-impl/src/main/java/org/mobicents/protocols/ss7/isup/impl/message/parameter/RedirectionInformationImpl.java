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
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;

/**
 * Start time:15:18:18 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author sergey vetyutnev
 */
public class RedirectionInformationImpl extends AbstractISUPParameter implements RedirectionInformation {

    private static final String REDIRECTING_INDICATOR = "redirectingIndicator";
    private static final String ORIGINAL_REDIRECTION_REASON = "originalRedirectionReason";
    private static final String REDIRECTION_COUNTER = "redirectionCounter";
    private static final String REDIRECTION_REASON = "redirectionReason";

    private static final int DEFAULT_INT_VALUE = 0;

    private int redirectingIndicator;
    private int originalRedirectionReason;
    private int redirectionCounter;
    private int redirectionReason;

    public RedirectionInformationImpl(byte[] b) throws IllegalArgumentException, ParameterException {
        super();
        decode(b);
    }

    public RedirectionInformationImpl(int redirectingIndicator, int originalRedirectionReason, int redirectionCounter,
            int redirectionReason) throws IllegalArgumentException {
        super();
        this.setRedirectingIndicator(redirectingIndicator);
        this.setOriginalRedirectionReason(originalRedirectionReason);
        this.setRedirectionCounter(redirectionCounter);
        this.setRedirectionReason(redirectionReason);
    }

    public RedirectionInformationImpl() {
        super();
    }

    public int decode(byte[] b) throws ParameterException {
        if (b == null || b.length != 2) {
            throw new ParameterException("byte[] must  not be null and length must  be 2");
        }
        try {
            this.setRedirectingIndicator((b[0] & 0x07));
            this.setOriginalRedirectionReason(((b[0] >> 4) & 0x0F));
            this.setRedirectionCounter((b[1] & 0x07));
            this.setRedirectionReason(((b[1] >> 4) & 0x0F));
        } catch (Exception e) {
            throw new ParameterException(e);
        }
        return 2;
    }

    public byte[] encode() throws ParameterException {
        int b0 = redirectingIndicator & 0x07;
        b0 |= (this.originalRedirectionReason & 0x0F) << 4;

        int b1 = redirectionCounter & 0x07;
        b1 |= (this.redirectionReason & 0x0F) << 4;
        return new byte[] { (byte) b0, (byte) b1 };
    }

    public int getRedirectingIndicator() {
        return redirectingIndicator;
    }

    public void setRedirectingIndicator(int redirectingIndicator) {
        this.redirectingIndicator = redirectingIndicator & 0x07;
    }

    public int getOriginalRedirectionReason() {
        return originalRedirectionReason;
    }

    public void setOriginalRedirectionReason(int originalRedirectionReason) {
        this.originalRedirectionReason = originalRedirectionReason & 0x0F;
    }

    public int getRedirectionCounter() {
        return redirectionCounter;
    }

    public void setRedirectionCounter(int redirectionCounter) throws IllegalArgumentException {
        if (redirectionCounter < 1 || redirectionCounter > 5) {
            throw new IllegalArgumentException("Out of range - must be between 1 and 5");
        }
        this.redirectionCounter = redirectionCounter & 0x07;
    }

    public int getRedirectionReason() {
        return redirectionReason;
    }

    public void setRedirectionReason(int redirectionReason) {
        this.redirectionReason = redirectionReason & 0x0F;
    }

    public String toString() {
        return "RedirectionInformation [redirectingIndicator=" + redirectingIndicator + ", originalRedirectionReason="
                + originalRedirectionReason + ", redirectionCounter=" + redirectionCounter + ", redirectionReason="
                + redirectionReason + "]";
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<RedirectionInformationImpl> ISUP_REDIRECTION_INFORMATION_XML = new XMLFormat<RedirectionInformationImpl>(
            RedirectionInformationImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, RedirectionInformationImpl redirectionInformation)
                throws XMLStreamException {
            redirectionInformation.redirectingIndicator = xml.getAttribute(REDIRECTING_INDICATOR, DEFAULT_INT_VALUE);
            redirectionInformation.originalRedirectionReason = xml.getAttribute(ORIGINAL_REDIRECTION_REASON, DEFAULT_INT_VALUE);
            redirectionInformation.redirectionCounter = xml.getAttribute(REDIRECTION_COUNTER, DEFAULT_INT_VALUE);
            redirectionInformation.redirectionReason = xml.getAttribute(REDIRECTION_REASON, DEFAULT_INT_VALUE);
        }

        @Override
        public void write(RedirectionInformationImpl redirectionInformation, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(REDIRECTING_INDICATOR, redirectionInformation.redirectingIndicator);
            xml.setAttribute(ORIGINAL_REDIRECTION_REASON, redirectionInformation.originalRedirectionReason);
            xml.setAttribute(REDIRECTION_COUNTER, redirectionInformation.redirectionCounter);
            xml.setAttribute(REDIRECTION_REASON, redirectionInformation.redirectionReason);
        }
    };
}
