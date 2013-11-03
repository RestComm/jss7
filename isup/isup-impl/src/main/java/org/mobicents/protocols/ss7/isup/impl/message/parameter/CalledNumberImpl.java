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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledNumber;

/**
 * Start time:13:05:28 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 *
 */
public abstract class CalledNumberImpl extends AbstractNAINumber implements CalledNumber {

    private static final String NUMBERING_PLAN_INDICATOR = "numberingPlanIndicator";
    private static final String ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR = "addressRepresentationRestrictedIndicator";

    private static final int DEFAULT_NUMBERING_PLAN_INDICATOR = 0;
    private static final int DEFAULT_ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR = 0;

    protected int numberingPlanIndicator;
    protected int addressRepresentationRestrictedIndicator;

    public CalledNumberImpl(byte[] representation) throws ParameterException {
        super(representation);

    }

    public CalledNumberImpl() {
        super();

    }

    public CalledNumberImpl(ByteArrayInputStream bis) throws ParameterException {
        super(bis);

    }

    public CalledNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator,
            int addressRepresentationREstrictedIndicator) {
        super(natureOfAddresIndicator, address);
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
    }

    public int encodeHeader(ByteArrayOutputStream bos) {
        doAddressPresentationRestricted();
        return super.encodeHeader(bos);
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#decodeBody(java.io. ByteArrayInputStream)
     */
    public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {
        int b = bis.read() & 0xff;

        this.numberingPlanIndicator = (b & 0x70) >> 4;
        this.addressRepresentationRestrictedIndicator = (b & 0x0c) >> 2;

        return 1;
    }

    protected void doAddressPresentationRestricted() {
        if (this.addressRepresentationRestrictedIndicator == _APRI_NOT_AVAILABLE) {
            this.oddFlag = 0;
            this.natureOfAddresIndicator = 0;
            this.numberingPlanIndicator = 0;
            this.setAddress("");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#encodeBody(java.io. ByteArrayOutputStream)
     */

    public int encodeBody(ByteArrayOutputStream bos) {
        int c = (this.numberingPlanIndicator & 0x07) << 4;
        c |= ((this.addressRepresentationRestrictedIndicator & 0x03) << 2);

        bos.write(c);
        return 1;
    }

    public int decodeDigits(ByteArrayInputStream bis) throws ParameterException {

        if (this.addressRepresentationRestrictedIndicator == _APRI_NOT_AVAILABLE) {
            this.setAddress("");
            return 0;
        } else {
            return super.decodeDigits(bis);
        }
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    public int getAddressRepresentationRestrictedIndicator() {
        return addressRepresentationRestrictedIndicator;
    }

    public void setAddressRepresentationRestrictedIndicator(int addressRepresentationREstrictedIndicator) {
        this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
    }

    protected abstract String getPrimitiveName();

    public String toString() {
        return getPrimitiveName() + " [numberingPlanIndicator=" + numberingPlanIndicator
                + ", addressRepresentationREstrictedIndicator=" + addressRepresentationRestrictedIndicator
                + ", natureOfAddresIndicator=" + natureOfAddresIndicator + ", oddFlag=" + oddFlag + ", address=" + address
                + "]";
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CalledNumberImpl> ISUP_CALLED_NUMBER_XML = new XMLFormat<CalledNumberImpl>(
            CalledNumberImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CalledNumberImpl calledNumber) throws XMLStreamException {
            ISUP_ABSTRACT_NAI_NUMBER_XML.read(xml, calledNumber);

            calledNumber.numberingPlanIndicator = xml.getAttribute(NUMBERING_PLAN_INDICATOR, DEFAULT_NUMBERING_PLAN_INDICATOR);
            calledNumber.addressRepresentationRestrictedIndicator = xml.getAttribute(
                    ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR, DEFAULT_ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR);
        }

        @Override
        public void write(CalledNumberImpl calledNumber, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            ISUP_ABSTRACT_NAI_NUMBER_XML.write(calledNumber, xml);

            xml.setAttribute(NUMBERING_PLAN_INDICATOR, calledNumber.numberingPlanIndicator);
            xml.setAttribute(ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR, calledNumber.addressRepresentationRestrictedIndicator);
        }
    };

}
