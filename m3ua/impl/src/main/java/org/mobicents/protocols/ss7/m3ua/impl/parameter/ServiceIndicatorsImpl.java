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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import java.util.Arrays;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;

/**
 *
 * @author amit bhayani
 *
 */
public class ServiceIndicatorsImpl extends ParameterImpl implements ServiceIndicators, XMLSerializable {

    private static final String INDICATOR = "indicator";
    private static final String ARRAY_SIZE = "size";

    private short[] indicators;
    private byte[] value = null;

    public ServiceIndicatorsImpl() {
        this.tag = Parameter.Service_Indicators;
    }

    protected ServiceIndicatorsImpl(short[] inds) {
        this.tag = Parameter.Service_Indicators;
        this.indicators = inds;
        this.encode();
    }

    protected ServiceIndicatorsImpl(byte[] value) {
        this.tag = Parameter.Service_Indicators;
        this.indicators = new short[value.length];
        for (int i = 0; i < value.length; i++) {
            this.indicators[i] = value[i];
        }
        this.value = value;
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[indicators.length];
        int count = 0;
        // encode routing context
        while (count < value.length) {
            value[count] = (byte) indicators[count++];
        }
    }

    @Override
    protected byte[] getValue() {
        return this.value;
    }

    public short[] getIndicators() {
        return this.indicators;
    }

    @Override
    public String toString() {
        return String.format("ServiceIndicators ids=%s", Arrays.toString(this.indicators));
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ServiceIndicatorsImpl> RC_XML = new XMLFormat<ServiceIndicatorsImpl>(
            ServiceIndicatorsImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ServiceIndicatorsImpl si) throws XMLStreamException {
            int size = xml.getAttribute(ARRAY_SIZE).toInt();
            si.indicators = new short[size];
            size = 0;
            while (xml.hasNext()) {
                si.indicators[size++] = xml.get(INDICATOR);
            }

            si.encode();
        }

        @Override
        public void write(ServiceIndicatorsImpl si, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(ARRAY_SIZE, si.indicators.length);
            for (Short s : si.indicators) {
                xml.add(s, INDICATOR);
            }
        }
    };

}
