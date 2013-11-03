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
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;

/**
 * Start time:17:36:23 2009-03-29<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author Oleg Kulikoff
 */
public class GenericNumberImpl extends AbstractNAINumber implements GenericNumber {

    private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private static final String NUMBERING_PLAN_INDICATOR = "numberingPlanIndicator";
    private static final String NUMBER_INCOMPLETE = "numberIncomplete";
    private static final String ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR = "addressRepresentationRestrictedIndicator";
    private static final String SCREENING_INDICATOR = "screeningIndicator";
    private static final String NUMBER_QUALIFIER_INDICATOR = "numberQualifierIndicator";

    private static final int DEFAULT_NUMBERING_PLAN_INDICATOR = 0;
    private static final boolean DEFAULT_NUMBER_INCOMPLETE = false;
    private static final int DEFAULT_ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR = 0;
    private static final int DEFAULT_SCREENING_INDICATOR = 0;
    private static final int DEFAULT_NUMBER_QUALIFIER_INDICATOR = 0;

    protected int numberQualifierIndicator;
    protected int numberingPlanIndicator;

    protected int addressRepresentationRestrictedIndicator;
    protected boolean numberIncomplete;
    protected int screeningIndicator;

    public GenericNumberImpl(int natureOfAddresIndicator, String address, int numberQualifierIndicator,
            int numberingPlanIndicator, int addressRepresentationREstrictedIndicator, boolean numberIncomplete,
            int screeningIndicator) {
        super(natureOfAddresIndicator, address);
        this.numberQualifierIndicator = numberQualifierIndicator;
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
        this.numberIncomplete = numberIncomplete;
        this.screeningIndicator = screeningIndicator;
    }

    public GenericNumberImpl(byte[] representation) throws ParameterException {
        super(representation);

    }

    public GenericNumberImpl(ByteArrayInputStream bis) throws ParameterException {
        super(bis);

    }

    public GenericNumberImpl() {
        super();

    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#decodeBody(java.io. ByteArrayInputStream)
     */

    public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {
        int b = bis.read() & 0xff;

        this.numberIncomplete = ((b & 0x80) >> 7) == _TURN_ON;
        this.numberingPlanIndicator = (b & 0x70) >> 4;
        this.addressRepresentationRestrictedIndicator = (b & 0x0c) >> 2;
        this.screeningIndicator = (b & 0x03);
        return 1;
    }

    /**
     * makes checks on APRI - see NOTE to APRI in Q.763, p 23
     */
    protected void doAddressPresentationRestricted() {

        if (this.addressRepresentationRestrictedIndicator != _APRI_NOT_AVAILABLE)
            return;
        // NOTE 1 If the parameter is included and the address presentation
        // restricted indicator indicates
        // address not available, octets 3 to n( this are digits.) are omitted,
        // the subfields in items a - odd/evem, b -nai , c - ni and d -npi, are
        // coded with
        // 0's, and the subfield f - filler, is coded with 11.
        this.oddFlag = 0;
        this.natureOfAddresIndicator = 0;
        this.numberingPlanIndicator = 0;
        this.numberIncomplete = _NI_COMPLETE;
        // 11
        this.screeningIndicator = _SI_NETWORK_PROVIDED;
        this.setAddress("");
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#encodeBody(java.io. ByteArrayOutputStream)
     */

    public int encodeBody(ByteArrayOutputStream bos) {

        int c = this.screeningIndicator;
        c |= (this.addressRepresentationRestrictedIndicator << 2);
        c |= (this.numberingPlanIndicator << 4);
        c |= ((this.numberIncomplete ? _TURN_ON : _TURN_OFF) << 7);

        bos.write(c);

        return 1;
    }

    public int decodeHeader(ByteArrayInputStream bis) throws ParameterException {
        this.numberQualifierIndicator = bis.read() & 0xff;
        return super.decodeHeader(bis) + 1;
    }

    public int encodeHeader(ByteArrayOutputStream bos) {
        doAddressPresentationRestricted();
        bos.write(this.numberQualifierIndicator);
        return super.encodeHeader(bos) + 1;
    }

    public int decodeDigits(ByteArrayInputStream bis) throws ParameterException {

        if (bis.available() != 0) {
            return super.decodeDigits(bis);
        } else {
            this.setAddress("");
            return 0;
        }
    }

    public int getNumberQualifierIndicator() {
        return numberQualifierIndicator;
    }

    public void setNumberQualifierIndicator(int numberQualifierIndicator) {
        this.numberQualifierIndicator = numberQualifierIndicator;
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator & 0x07;
    }

    public int getAddressRepresentationRestrictedIndicator() {
        return addressRepresentationRestrictedIndicator;
    }

    public void setAddressRepresentationRestrictedIndicator(int addressRepresentationREstrictedIndicator) {
        this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator & 0x03;
    }

    public boolean isNumberIncomplete() {
        return numberIncomplete;
    }

    public void setNumberIncompleter(boolean numberIncomplete) {
        this.numberIncomplete = numberIncomplete;
    }

    public int getScreeningIndicator() {
        return screeningIndicator;
    }

    public void setScreeningIndicator(int screeningIndicator) {
        this.screeningIndicator = screeningIndicator & 0x03;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    public String toString() {
        return "GenericNumber [numberingPlanIndicator=" + numberingPlanIndicator + ", numberIncomplete=" + numberIncomplete
                + ", addressRepresentationREstrictedIndicator=" + addressRepresentationRestrictedIndicator
                + ", screeningIndicator=" + screeningIndicator + ", numberQualifierIndicator=" + numberQualifierIndicator
                + ", natureOfAddresIndicator=" + natureOfAddresIndicator + ", oddFlag=" + oddFlag + ", address=" + address
                + "]";
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<GenericNumberImpl> ISUP_GENERIC_NUMBER_XML = new XMLFormat<GenericNumberImpl>(
            GenericNumberImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, GenericNumberImpl genericNumber) throws XMLStreamException {
            ISUP_ABSTRACT_NAI_NUMBER_XML.read(xml, genericNumber);

            genericNumber.numberingPlanIndicator = xml.getAttribute(NUMBERING_PLAN_INDICATOR, DEFAULT_NUMBERING_PLAN_INDICATOR);
            genericNumber.numberIncomplete = xml.getAttribute(NUMBER_INCOMPLETE, DEFAULT_NUMBER_INCOMPLETE);
            genericNumber.addressRepresentationRestrictedIndicator = xml.getAttribute(
                    ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR, DEFAULT_ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR);
            genericNumber.screeningIndicator = xml.getAttribute(SCREENING_INDICATOR, DEFAULT_SCREENING_INDICATOR);
            genericNumber.numberQualifierIndicator = xml.getAttribute(NUMBER_QUALIFIER_INDICATOR,
                    DEFAULT_NUMBER_QUALIFIER_INDICATOR);
        }

        @Override
        public void write(GenericNumberImpl genericNumber, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            ISUP_ABSTRACT_NAI_NUMBER_XML.write(genericNumber, xml);

            xml.setAttribute(NUMBERING_PLAN_INDICATOR, genericNumber.numberingPlanIndicator);
            xml.setAttribute(NUMBER_INCOMPLETE, genericNumber.numberIncomplete);
            xml.setAttribute(ADDRESS_REPRESENTATION_RESTRICTED_INDICATOR,
                    genericNumber.addressRepresentationRestrictedIndicator);
            xml.setAttribute(SCREENING_INDICATOR, genericNumber.screeningIndicator);
            xml.setAttribute(NUMBER_QUALIFIER_INDICATOR, genericNumber.numberQualifierIndicator);
        }
    };
}
