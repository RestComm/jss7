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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;

/**
 * Start time:15:59:02 2009-03-29<br>
 * Project: mobicents-isup-stack<br>
 * This represent called party number - Q.763 - 3.9
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author Oleg Kulikoff
 */
public class CalledPartyNumberImpl extends AbstractNAINumber implements CalledPartyNumber {

    private static final String NUMBERING_PLAN_INDICATOR = "numberingPlanIndicator";
    private static final String INTERNAL_NETWORK_NUMBER_INDICATOR = "internalNetworkNumberIndicator";

    private static final int DEFAULT_NUMBERING_PLAN_INDICATOR = 0;
    private static final int DEFAULT_INTERNAL_NETWORK_NUMBER_INDICATOR = 0;

    protected int numberingPlanIndicator;
    protected int internalNetworkNumberIndicator;

    /**
     *
     *
     * @param representation
     * @throws ParameterException
     */
    public CalledPartyNumberImpl(byte[] representation) throws ParameterException {
        super(representation);

    }

    /**
     *
     *
     * @param bis
     * @throws ParameterException
     */
    public CalledPartyNumberImpl(ByteArrayInputStream bis) throws ParameterException {
        super(bis);

    }

    public CalledPartyNumberImpl() {
        super();

    }

    /**
     *
     * @param natureOfAddresIndicator
     * @param address
     */
    public CalledPartyNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator,
            int internalNetworkNumberIndicator) {
        super(natureOfAddresIndicator, address);
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.internalNetworkNumberIndicator = internalNetworkNumberIndicator;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#decodeBody(java.io. ByteArrayInputStream)
     */

    public int decodeBody(ByteArrayInputStream bis) throws IllegalArgumentException {
        int b = bis.read() & 0xff;

        this.internalNetworkNumberIndicator = (b & 0x80) >> 7;
        this.numberingPlanIndicator = (b & 0x70) >> 4;
        return 1;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#encodeBody(java.io. ByteArrayOutputStream)
     */

    public int encodeBody(ByteArrayOutputStream bos) {
        int c = (this.numberingPlanIndicator & 0x07) << 4;
        c |= ((this.internalNetworkNumberIndicator & 0x01) << 7);
        bos.write(c);
        return 1;
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    public int getInternalNetworkNumberIndicator() {
        return internalNetworkNumberIndicator;
    }

    public void setInternalNetworkNumberIndicator(int internalNetworkNumberIndicator) {
        this.internalNetworkNumberIndicator = internalNetworkNumberIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    public String toString() {
        return "CalledPartyNumber [numberingPlanIndicator=" + numberingPlanIndicator + ", internalNetworkNumberIndicator="
                + internalNetworkNumberIndicator + ", natureOfAddresIndicator=" + natureOfAddresIndicator + ", oddFlag="
                + oddFlag + ", address=" + address + "]";
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CalledPartyNumberImpl> ISUP_CALLED_PARTY_NUMBER_XML = new XMLFormat<CalledPartyNumberImpl>(
            CalledPartyNumberImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CalledPartyNumberImpl calledPartyNumber)
                throws XMLStreamException {
            ISUP_ABSTRACT_NAI_NUMBER_XML.read(xml, calledPartyNumber);

            calledPartyNumber.numberingPlanIndicator = xml.getAttribute(NUMBERING_PLAN_INDICATOR,
                    DEFAULT_NUMBERING_PLAN_INDICATOR);
            calledPartyNumber.internalNetworkNumberIndicator = xml.getAttribute(INTERNAL_NETWORK_NUMBER_INDICATOR,
                    DEFAULT_INTERNAL_NETWORK_NUMBER_INDICATOR);
        }

        @Override
        public void write(CalledPartyNumberImpl calledPartyNumber, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            ISUP_ABSTRACT_NAI_NUMBER_XML.write(calledPartyNumber, xml);

            xml.setAttribute(NUMBERING_PLAN_INDICATOR, calledPartyNumber.numberingPlanIndicator);
            xml.setAttribute(INTERNAL_NETWORK_NUMBER_INDICATOR, calledPartyNumber.internalNetworkNumberIndicator);
        }
    };

}
