/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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


package org.restcomm.protocols.ss7.tools.simulator.tests.checkimei;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;

/**
 * @author mnowa
 *
 */
public class TestCheckImeiServerConfigurationData {

    protected static final String AUTO_EQUIPMENT_STATUS = "autoEquipmentStatus";
    protected static final String ONE_NOTIFICATION_FOR_100_DIALOGS = "oneNotificationFor100Dialogs";

    protected EquipmentStatus autoEquipmentStatus = EquipmentStatus.whiteListed;

    protected boolean oneNotificationFor100Dialogs = false;

    public EquipmentStatus getAutoEquipmentStatus() {
        return autoEquipmentStatus;
    }

    public void setAutoEquipmentStatus(EquipmentStatus autoEquipmentStatus) {
        this.autoEquipmentStatus = autoEquipmentStatus;
    }

    public boolean isOneNotificationFor100Dialogs() {
        return oneNotificationFor100Dialogs;
    }

    public void setOneNotificationFor100Dialogs(boolean oneNotificationFor100Dialogs) {
        this.oneNotificationFor100Dialogs = oneNotificationFor100Dialogs;
    }

    protected static final XMLFormat<TestCheckImeiServerConfigurationData> XML = new XMLFormat<TestCheckImeiServerConfigurationData>(
            TestCheckImeiServerConfigurationData.class) {

        public void write(TestCheckImeiServerConfigurationData clt, OutputElement xml) throws XMLStreamException {

            xml.setAttribute(ONE_NOTIFICATION_FOR_100_DIALOGS, clt.oneNotificationFor100Dialogs);

            xml.add(clt.autoEquipmentStatus.toString(), AUTO_EQUIPMENT_STATUS, String.class);
        }

        public void read(InputElement xml, TestCheckImeiServerConfigurationData clt) throws XMLStreamException {

            clt.oneNotificationFor100Dialogs = xml.getAttribute(ONE_NOTIFICATION_FOR_100_DIALOGS).toBoolean();

            String aqs = (String) xml.get(AUTO_EQUIPMENT_STATUS, String.class);
            clt.autoEquipmentStatus = EquipmentStatus.valueOf(aqs);
        }
    };

}
