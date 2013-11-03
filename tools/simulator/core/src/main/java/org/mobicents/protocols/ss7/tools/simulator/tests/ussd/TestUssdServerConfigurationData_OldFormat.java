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

package org.mobicents.protocols.ss7.tools.simulator.tests.ussd;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestUssdServerConfigurationData_OldFormat extends TestUssdServerConfigurationData {

    protected static final XMLFormat<TestUssdServerConfigurationData_OldFormat> XML = new XMLFormat<TestUssdServerConfigurationData_OldFormat>(
            TestUssdServerConfigurationData_OldFormat.class) {

        public void write(TestUssdServerConfigurationData_OldFormat srv, OutputElement xml) throws XMLStreamException {
        }

        public void read(InputElement xml, TestUssdServerConfigurationData_OldFormat srv) throws XMLStreamException {
            srv.dataCodingScheme = xml.getAttribute(DATA_CODING_SCHEME).toInt();
            srv.alertingPattern = xml.getAttribute(ALERTING_PATTERN).toInt();
            srv.oneNotificationFor100Dialogs = xml.getAttribute(ONE_NOTIFICATION_FOR_100_DIALOGS).toBoolean();

            srv.msisdnAddress = (String) xml.get(MSISDN_ADDRESS, String.class);
            srv.autoResponseString = (String) xml.get(AUTO_RESPONSE_STRING, String.class);
            srv.autoUnstructured_SS_RequestString = (String) xml.get(AUTO_UNSTRUCTURED_SS_REQUEST_STRING, String.class);

            String an = (String) xml.get(MSISDN_ADDRESS_NATURE, String.class);
            srv.msisdnAddressNature = AddressNature.valueOf(an);
            String np = (String) xml.get(MSISDN_NUMBERING_PLAN, String.class);
            srv.msisdnNumberingPlan = NumberingPlan.valueOf(np);
            String ss_act = (String) xml.get(PROCESS_SS_REQUEST_ACTION, String.class);
            srv.processSsRequestAction = ProcessSsRequestAction.createInstance(ss_act);
        }
    };

}
