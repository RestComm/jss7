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

package org.mobicents.protocols.ss7.tools.simulator.level1;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class DialogicConfigurationData {

	protected static final String SOURCE_MODULE_ID = "SourceModuleId";
	protected static final String DESTINATION_MODULE_ID = "DestinationModuleId";

	private int sourceModuleId;
	private int destinationModuleId;

	public int getSourceModuleId() {
		return sourceModuleId;
	}

	public void setSourceModuleId(int val) {
		sourceModuleId = val;
	}

	public int getDestinationModuleId() {
		return destinationModuleId;
	}

	public void setDestinationModuleId(int val) {
		destinationModuleId = val;
	}

	protected static final XMLFormat<DialogicConfigurationData> XML = new XMLFormat<DialogicConfigurationData>(DialogicConfigurationData.class) {

		public void write(DialogicConfigurationData dial, OutputElement xml) throws XMLStreamException {
			xml.setAttribute(SOURCE_MODULE_ID, dial.sourceModuleId);
			xml.setAttribute(DESTINATION_MODULE_ID, dial.destinationModuleId);
		}

		public void read(InputElement xml, DialogicConfigurationData dial) throws XMLStreamException {
			dial.sourceModuleId = xml.getAttribute(SOURCE_MODULE_ID).toInt();
			dial.destinationModuleId = xml.getAttribute(DESTINATION_MODULE_ID).toInt();
		}
	};

}
