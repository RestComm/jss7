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

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

/**
 * @author mnowa
 *
 */
public class TestCheckImeiServerStandardManMBean extends StandardMBean {

    public TestCheckImeiServerStandardManMBean(TestCheckImeiServerMan impl, Class<TestCheckImeiServerManMBean> intf) throws NotCompliantMBeanException {
        super(impl, intf);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {

                new MBeanAttributeInfo("AutoEquipmentStatus", EquipmentStatusType.class.getName(), "EquipmentStatus parameter to be automatically send in CheckImeiResponse", true, true, false),
                new MBeanAttributeInfo("AutoEquipmentStatus_Value", String.class.getName(), "EquipmentStatus parameter to be automatically send in CheckImeiResponse", true, false, false),
                new MBeanAttributeInfo("OneNotificationFor100Dialogs", boolean.class.getName(),
                        "If true there will be only one notification per every 100 sent dialogs", true, true, true),
                new MBeanAttributeInfo("CurrentRequestDef", String.class.getName(), "Definition of the current request Dialog",
                        true, false, false),

        };

        MBeanParameterInfo[] putAutoEquipmentStatusParam = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(), "EquipmentStatus value") };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
                new MBeanOperationInfo(
                        "putAutoEquipmentStatus",
                        "EquipmentStatus parameter to be automatically send in CheckImeiResponse: "
                                + "0:whiteListed,1:blackListed,2:greyListed",
                        putAutoEquipmentStatusParam, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("closeCurrentDialog", "Closing the current dialog", null, String.class.getName(), MBeanOperationInfo.ACTION)
        };
        return new MBeanInfo(TestCheckImeiServerMan.class.getName(), "CheckImeiServer test parameters management", attributes, null, operations, null);
    }
}
