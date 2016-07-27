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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;

/**
 * Start time:16:14:51 2009-03-29<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author Oleg Kulikoff
 */
public class CallingPartyNumberImpl extends AbstractNAINumber implements CallingPartyNumber {

    private static final String NUMBERING_PLAN_INDICATOR = "numberingPlanIndicator";
    private static final String NUMBER_NUMBER_INCOMPLETE_INDICATOR = "numberIncompleteIndicator";
    private static final String ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR = "addressRepresentationRestrictedIndicator";
    private static final String SCREENING_INDICATOR = "screeningIndicator";

    private static final int DEFAULT_NUMBERING_PLAN_INDICATOR = 0;
    private static final int DEFAULT_NUMBER_NUMBER_INCOMPLETE_INDICATOR = 0;
    private static final int DEFAULT_ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR = 0;
    private static final int DEFAULT_SCREENING_INDICATOR = 0;

    protected int numberingPlanIndicator;
    protected int numberIncompleteIndicator;
    protected int addressRepresentationRestrictedIndicator;
    protected int screeningIndicator;

    /**
     *
     * @param representation
     * @throws ParameterException
     */
    public CallingPartyNumberImpl(byte[] representation) throws ParameterException {
        super(representation);

    }

    public CallingPartyNumberImpl() {
        super();

    }

    /**
     *
     * @param bis
     * @throws ParameterException
     */
    public CallingPartyNumberImpl(ByteArrayInputStream bis) throws ParameterException {
        super(bis);

    }

    public CallingPartyNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator,
            int numberIncompleteIndicator, int addressRepresentationREstrictedIndicator, int screeningIndicator) {
        super(natureOfAddresIndicator, address);
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.numberIncompleteIndicator = numberIncompleteIndicator;
        this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
        this.screeningIndicator = screeningIndicator;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#decodeBody(java.io. ByteArrayInputStream)
     */

    public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {
        int b = bis.read() & 0xff;

        this.numberIncompleteIndicator = (b & 0x80) >> 7;
        this.numberingPlanIndicator = (b & 0x70) >> 4;
        this.addressRepresentationRestrictedIndicator = (b & 0x0c) >> 2;
        this.screeningIndicator = (b & 0x03);

        return 1;
    }

    public int encodeHeader(ByteArrayOutputStream bos) {
        doAddressPresentationRestricted();
        return super.encodeHeader(bos);
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#encodeBody(java.io. ByteArrayOutputStream)
     */

    public int encodeBody(ByteArrayOutputStream bos) {

        int c = this.numberingPlanIndicator << 4;
        c |= (this.numberIncompleteIndicator << 7);
        c |= (this.addressRepresentationRestrictedIndicator << 2);
        c |= (this.screeningIndicator);
        bos.write(c);
        return 1;
    }

    /**
     * makes checks on APRI - see NOTE to APRI in Q.763, p 23
     */
    protected void doAddressPresentationRestricted() {

        if (this.addressRepresentationRestrictedIndicator == _APRI_NOT_AVAILABLE) {

            // NOTE 1 If the parameter is included and the address
            // presentation
            // restricted indicator indicates
            // address not available, octets 3 to n( this are digits.) are
            // omitted,
            // the subfields in items a - odd/evem, b -nai , c - ni and d -npi,
            // are
            // coded with
            // FIXME: add this filler
            // 0's, and the subfield f - filler, is coded with 11.
            this.oddFlag = 0;
            this.natureOfAddresIndicator = 0;
            this.numberIncompleteIndicator = 0;
            this.numberingPlanIndicator = 0;
            this.screeningIndicator = 3; // !!!
            this.setAddress("");
        }
    }

    public int decodeDigits(ByteArrayInputStream bis) throws ParameterException {

        if (this.addressRepresentationRestrictedIndicator == _APRI_NOT_AVAILABLE) {
            this.setAddress("");
            return 0;
        } else {
            return super.decodeDigits(bis);
        }
    }

    public int encodeDigits(ByteArrayOutputStream bos) {

        if (this.addressRepresentationRestrictedIndicator == _APRI_NOT_AVAILABLE) {
            return 0;
        } else {
            return super.encodeDigits(bos);
        }
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    public int getNumberIncompleteIndicator() {
        return numberIncompleteIndicator;
    }

    public void setNumberIncompleteIndicator(int numberIncompleteIndicator) {
        this.numberIncompleteIndicator = numberIncompleteIndicator;
    }

    public int getAddressRepresentationRestrictedIndicator() {
        return addressRepresentationRestrictedIndicator;
    }

    public void setAddressRepresentationREstrictedIndicator(int addressRepresentationREstrictedIndicator) {
        this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
    }

    public int getScreeningIndicator() {
        return screeningIndicator;
    }

    public void setScreeningIndicator(int screeningIndicator) {
        this.screeningIndicator = screeningIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    public String toString() {
        return "CallingPartyNumber [numberingPlanIndicator=" + numberingPlanIndicator + ", numberIncompleteIndicator="
                + numberIncompleteIndicator + ", addressRepresentationRestrictedIndicator="
                + addressRepresentationRestrictedIndicator + ", screeningIndicator=" + screeningIndicator
                + ", natureOfAddresIndicator=" + natureOfAddresIndicator + ", oddFlag=" + oddFlag + ", address=" + address
                + "]";
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CallingPartyNumberImpl> ISUP_CALLING_PARTY_NUMBER_XML = new XMLFormat<CallingPartyNumberImpl>(
            CallingPartyNumberImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CallingPartyNumberImpl callingPartyNumber)
                throws XMLStreamException {
            ISUP_ABSTRACT_NAI_NUMBER_XML.read(xml, callingPartyNumber);

            callingPartyNumber.numberingPlanIndicator = xml.getAttribute(NUMBERING_PLAN_INDICATOR,
                    DEFAULT_NUMBERING_PLAN_INDICATOR);
            callingPartyNumber.numberIncompleteIndicator = xml.getAttribute(NUMBER_NUMBER_INCOMPLETE_INDICATOR,
                    DEFAULT_NUMBER_NUMBER_INCOMPLETE_INDICATOR);
            callingPartyNumber.addressRepresentationRestrictedIndicator = xml.getAttribute(
                    ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR, DEFAULT_ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR);
            callingPartyNumber.screeningIndicator = xml.getAttribute(SCREENING_INDICATOR, DEFAULT_SCREENING_INDICATOR);
        }

        @Override
        public void write(CallingPartyNumberImpl callingPartyNumber, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            ISUP_ABSTRACT_NAI_NUMBER_XML.write(callingPartyNumber, xml);

            xml.setAttribute(NUMBERING_PLAN_INDICATOR, callingPartyNumber.numberingPlanIndicator);
            xml.setAttribute(NUMBER_NUMBER_INCOMPLETE_INDICATOR, callingPartyNumber.numberIncompleteIndicator);
            xml.setAttribute(ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR,
                    callingPartyNumber.addressRepresentationRestrictedIndicator);
            xml.setAttribute(SCREENING_INDICATOR, callingPartyNumber.screeningIndicator);
        }
    };
}
