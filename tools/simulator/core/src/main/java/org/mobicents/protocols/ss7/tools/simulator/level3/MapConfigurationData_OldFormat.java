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

package org.mobicents.protocols.ss7.tools.simulator.level3;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MapConfigurationData_OldFormat extends MapConfigurationData {

    protected static final XMLFormat<MapConfigurationData_OldFormat> XML = new XMLFormat<MapConfigurationData_OldFormat>(
            MapConfigurationData_OldFormat.class) {

        public void write(MapConfigurationData_OldFormat map, OutputElement xml) throws XMLStreamException {
        }

        public void read(InputElement xml, MapConfigurationData_OldFormat map) throws XMLStreamException {
            int localSsn = xml.getAttribute(LOCAL_SSN).toInt();
            int remoteSsn = xml.getAttribute(REMOTE_SSN).toInt();

            map.setRemoteAddressDigits((String) xml.get(REMOTE_ADDRESS_DIGITS, String.class));
            map.setOrigReference((String) xml.get(ORIG_REFERENCE, String.class));
            map.setDestReference((String) xml.get(DEST_REFERENCE, String.class));

            String an = (String) xml.get(ORIG_REFERENCE_ADDRESS_NATURE, String.class);
            map.setOrigReferenceAddressNature(AddressNature.valueOf(an));
            String np = (String) xml.get(ORIG_REFERENCE_NUMBERING_PLAN, String.class);
            map.setOrigReferenceNumberingPlan(NumberingPlan.valueOf(np));
            an = (String) xml.get(DEST_REFERENCE_ADDRESS_NATURE, String.class);
            map.setDestReferenceAddressNature(AddressNature.valueOf(an));
            np = (String) xml.get(DEST_REFERENCE_NUMBERING_PLAN, String.class);
            map.setDestReferenceNumberingPlan(NumberingPlan.valueOf(np));
        }
    };

}
