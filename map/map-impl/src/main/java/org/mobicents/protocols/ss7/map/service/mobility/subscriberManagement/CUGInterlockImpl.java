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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 * @author alerant appngin
 *
 */
@SuppressWarnings("serial")
public class CUGInterlockImpl extends OctetStringBase implements CUGInterlock {
    private static final String DATA = "data";

    public CUGInterlockImpl() {
        super(4, 4, "CUGInterlock");
    }

    public CUGInterlockImpl(byte[] data) {
        super(4, 4, "CUGInterlock", data);
    }

    public byte[] getData() {
        return data;
    }

    /**
     * XML serialization
     */
    protected static final XMLFormat<CUGInterlockImpl> CUG_INTERLOCK_XML = new XMLFormat<CUGInterlockImpl>(
            CUGInterlockImpl.class) {

        @Override
        public void write(CUGInterlockImpl obj,
                javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(DATA, OctetStringBase.bytesToHex(obj.data));
        }

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                CUGInterlockImpl obj) throws XMLStreamException {
            obj.data = OctetStringBase.hexToBytes(xml.getAttribute(DATA, (String) null));
        }

    };
}
