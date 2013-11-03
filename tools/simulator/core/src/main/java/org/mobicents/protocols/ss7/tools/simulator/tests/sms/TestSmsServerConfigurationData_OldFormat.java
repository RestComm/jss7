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

package org.mobicents.protocols.ss7.tools.simulator.tests.sms;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapProtocolVersion;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestSmsServerConfigurationData_OldFormat extends TestSmsServerConfigurationData {

    protected static final XMLFormat<TestSmsServerConfigurationData_OldFormat> XML = new XMLFormat<TestSmsServerConfigurationData_OldFormat>(
            TestSmsServerConfigurationData_OldFormat.class) {

        public void write(TestSmsServerConfigurationData_OldFormat srv, OutputElement xml) throws XMLStreamException {
        }

        public void read(InputElement xml, TestSmsServerConfigurationData_OldFormat srv) throws XMLStreamException {
            srv.hlrSsn = xml.getAttribute(HLR_SSN).toInt();
            srv.vlrSsn = xml.getAttribute(VLR_SSN).toInt();

            srv.serviceCenterAddress = (String) xml.get(SERVICE_CENTER_ADDRESS, String.class);

            String an = (String) xml.get(ADDRESS_NATURE, String.class);
            srv.addressNature = AddressNature.valueOf(an);
            String np = (String) xml.get(NUMBERING_PLAN, String.class);
            srv.numberingPlan = NumberingPlan.valueOf(np);
            String mpv = (String) xml.get(MAP_PROTOCOL_VERSION, String.class);
            srv.mapProtocolVersion = MapProtocolVersion.createInstance(mpv);
            String ton = (String) xml.get(TYPE_OF_NUMBER, String.class);
            srv.typeOfNumber = TypeOfNumber.valueOf(ton);
            String npi = (String) xml.get(NUMBERING_PLAN_IDENTIFICATION, String.class);
            srv.numberingPlanIdentification = NumberingPlanIdentification.valueOf(npi);
            String sct = (String) xml.get(SMS_CODING_TYPE, String.class);
            srv.smsCodingType = SmsCodingType.createInstance(sct);
        }
    };

}
