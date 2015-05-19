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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author alerant appngin
 *
 */
@SuppressWarnings("serial")
public class MSClassmark2Impl extends OctetStringBase implements MSClassmark2 {

    private static final String DATA = "data";

    public MSClassmark2Impl() {
        super(3, 3, "MSClassmark2");
    }

    public MSClassmark2Impl(byte[] data) {
        super(3, 3, "MSClassmark2", data);
    }

    public byte[] getData() {
        return data;
    }

    /** XML serialization */
    protected static final XMLFormat<MSClassmark2Impl> MS_CLASSMARK2_XML = new XMLFormat<MSClassmark2Impl>(
            MSClassmark2Impl.class) {

        @Override
        public void write(MSClassmark2Impl obj,
                javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            xml.setAttribute(DATA, OctetStringBase.bytesToHex(obj.data));
        }

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                MSClassmark2Impl obj) throws XMLStreamException {
            obj.data = OctetStringBase.hexToBytes(xml.getAttribute(DATA,
                    (String) null));
        }

    };

}
