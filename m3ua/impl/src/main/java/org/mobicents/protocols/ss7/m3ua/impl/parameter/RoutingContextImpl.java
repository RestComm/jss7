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
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 *
 * @author amit bhayani
 *
 */
public class RoutingContextImpl extends ParameterImpl implements RoutingContext, XMLSerializable {

    private static final String ARRAY_SIZE = "size";
    private static final String ROUTING_CONTEXT = "rc";

    private long[] rcs = null;
    private byte[] value;

    public RoutingContextImpl() {
        this.tag = Parameter.Routing_Context;
    }

    protected RoutingContextImpl(byte[] value) {
        this.tag = Parameter.Routing_Context;

        int count = 0;
        int arrSize = 0;
        rcs = new long[(value.length / 4)];

        while (count < value.length) {
            rcs[arrSize] = 0;
            rcs[arrSize] |= value[count++] & 0xFF;
            rcs[arrSize] <<= 8;
            rcs[arrSize] |= value[count++] & 0xFF;
            rcs[arrSize] <<= 8;
            rcs[arrSize] |= value[count++] & 0xFF;
            rcs[arrSize] <<= 8;
            rcs[arrSize++] |= value[count++] & 0xFF;
        }

        this.value = value;
    }

    protected RoutingContextImpl(long[] routingcontexts) {
        this.tag = Parameter.Routing_Context;
        rcs = routingcontexts;
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = new byte[(rcs.length * 4)];
        int count = 0;
        int arrSize = 0;
        // encode routing context
        while (count < value.length) {
            value[count++] = (byte) (rcs[arrSize] >>> 24);
            value[count++] = (byte) (rcs[arrSize] >>> 16);
            value[count++] = (byte) (rcs[arrSize] >>> 8);
            value[count++] = (byte) (rcs[arrSize++]);
        }
    }

    public long[] getRoutingContexts() {
        return this.rcs;
    }

    @Override
    protected byte[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("RoutingContext rc=%s", Arrays.toString(rcs));
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<RoutingContextImpl> RC_XML = new XMLFormat<RoutingContextImpl>(RoutingContextImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, RoutingContextImpl rc) throws XMLStreamException {
            int size = xml.getAttribute(ARRAY_SIZE).toInt();
            rc.rcs = new long[size];
            size = 0;
            while (xml.hasNext()) {
                rc.rcs[size++] = xml.get(ROUTING_CONTEXT, Long.class);
            }
            rc.encode();
        }

        @Override
        public void write(RoutingContextImpl rc, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(ARRAY_SIZE, rc.rcs.length);
            for (Long l : rc.rcs) {
                xml.add(l, ROUTING_CONTEXT, Long.class);
            }
        }
    };
}
