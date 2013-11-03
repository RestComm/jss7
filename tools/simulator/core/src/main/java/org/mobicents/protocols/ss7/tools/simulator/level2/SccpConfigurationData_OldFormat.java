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

package org.mobicents.protocols.ss7.tools.simulator.level2;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SccpConfigurationData_OldFormat extends SccpConfigurationData {

    protected static final String EXTRA_LOCAL_ADDRESS_DIGITS = "extraLocalAddressDigits";

    // private String extraLocalAddressDigits = "";

    protected static final XMLFormat<SccpConfigurationData_OldFormat> XML = new XMLFormat<SccpConfigurationData_OldFormat>(
            SccpConfigurationData_OldFormat.class) {

        public void write(SccpConfigurationData_OldFormat sccp, OutputElement xml) throws XMLStreamException {
        }

        public void read(InputElement xml, SccpConfigurationData_OldFormat sccp) throws XMLStreamException {
            sccp.setRouteOnGtMode(xml.getAttribute(REMOTE_ON_GT_MODE).toBoolean());
            sccp.setRemoteSpc(xml.getAttribute(REMOTE_SPC).toInt());
            sccp.setLocalSpc(xml.getAttribute(LOCAL_SPC).toInt());
            sccp.setNi(xml.getAttribute(NI).toInt());
            sccp.setRemoteSsn(xml.getAttribute(REMOTE_SSN).toInt());
            sccp.setLocalSsn(xml.getAttribute(LOCAL_SSN).toInt());
            sccp.setTranslationType(xml.getAttribute(TRANSLATION_TYTE).toInt());

            String gtt = (String) xml.get(GLOBAL_TITLE_TYPE, String.class);
            sccp.setGlobalTitleType(GlobalTitleType.createInstance(gtt));
            String an = (String) xml.get(ADDRESS_NATURE, String.class);
            sccp.setNatureOfAddress(NatureOfAddress.valueOf(an));
            String np = (String) xml.get(NUMBERING_PLAN, String.class);
            sccp.setNumberingPlan(NumberingPlan.valueOf(np));
            sccp.setCallingPartyAddressDigits((String) xml.get(CALLING_PARTY_ADDRESS_DIGITS, String.class));
            // for skipping previous data
            String extraLocalAddressDigits = (String) xml.get(EXTRA_LOCAL_ADDRESS_DIGITS, String.class);
        }
    };

}
