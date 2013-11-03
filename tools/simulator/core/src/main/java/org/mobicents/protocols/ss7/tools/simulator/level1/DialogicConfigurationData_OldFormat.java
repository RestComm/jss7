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

package org.mobicents.protocols.ss7.tools.simulator.level1;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class DialogicConfigurationData_OldFormat extends DialogicConfigurationData {

    protected static final XMLFormat<DialogicConfigurationData_OldFormat> XML = new XMLFormat<DialogicConfigurationData_OldFormat>(
            DialogicConfigurationData_OldFormat.class) {

        public void write(DialogicConfigurationData_OldFormat dial, OutputElement xml) throws XMLStreamException {
        }

        public void read(InputElement xml, DialogicConfigurationData_OldFormat dial) throws XMLStreamException {
            dial.setSourceModuleId(xml.getAttribute(SOURCE_MODULE_ID).toInt());
            dial.setDestinationModuleId(xml.getAttribute(DESTINATION_MODULE_ID).toInt());
        }
    };

}
