/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.map.service.callhandling;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.map.api.service.callhandling.UUIndicator;
import org.restcomm.protocols.ss7.map.primitives.OctetStringLength1Base;

/**
*
* @author sergey vetyutnev
*
*/
public class UUIndicatorImpl extends OctetStringLength1Base implements UUIndicator {

    private static final String DATA = "data";

    private static final int DEFAULT_VALUE = 0;

    public UUIndicatorImpl() {
        super("UUIndicator");
    }

    public UUIndicatorImpl(int data) {
        super("UUIndicator", data);
    }

    public int getData() {
        return data;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<UUIndicatorImpl> UU_INDICATOR_XML = new XMLFormat<UUIndicatorImpl>(UUIndicatorImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, UUIndicatorImpl uuIndicator) throws XMLStreamException {
            Integer i1 = xml.getAttribute(DATA, DEFAULT_VALUE);
            if (i1 != null) {
                uuIndicator.data = i1;
            }
        }

        @Override
        public void write(UUIndicatorImpl uuIndicator, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(DATA, uuIndicator.data);
        }
    };

}
