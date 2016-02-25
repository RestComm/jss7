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


package org.mobicents.protocols.ss7.tools.simulator.tests.checkimei;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.mobicents.protocols.ss7.tools.simulator.level3.MapProtocolVersion;

/**
 * @author mnowa
 *
 */
public class TestCheckImeiClientStandardManMBean extends StandardMBean {

    public TestCheckImeiClientStandardManMBean(TestCheckImeiClientMan impl, Class<TestCheckImeiClientManMBean> intf)
            throws NotCompliantMBeanException {
        super(impl, intf);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {
                // TODO mnowa: add/correct attributes here
                new MBeanAttributeInfo("Imei", String.class.getName(), "IMEI's digits string", true, true, false),
                new MBeanAttributeInfo("MapProtocolVersion", MapProtocolVersion.class.getName(), "MAP protocol version", true, true, false),
                new MBeanAttributeInfo("MapProtocolVersion_Value", String.class.getName(), "MAP protocol version", true, false, false),
                new MBeanAttributeInfo(
                        "CheckImeiClientAction",
                        CheckImeiClientAction.class.getName(),
                        "The mode of CheckImeiClient work. When manual response user can manually send CheckImei request, when VAL_AUTO_SendCheckImeiRequest the tester sends CheckImei requests without dealay (load test)",
                        true, true, false),
                new MBeanAttributeInfo(
                        "CheckImeiClientAction_Value",
                        String.class.getName(),
                        "The mode of CheckImeiClient work. When manual response user can manually send CheckImei request, when VAL_AUTO_SendCheckImeiRequest the tester sends CheckImei requests without dealay (load test)",
                        true, false, false),
                new MBeanAttributeInfo("MaxConcurrentDialogs", int.class.getName(), "The count of maximum active MAP dialogs when the auto sending mode", true,
                        true, false),
                new MBeanAttributeInfo("OneNotificationFor100Dialogs", boolean.class.getName(),
                        "If true there will be only one notification per every 100 sent dialogs (recommended for the auto sending mode)", true, true, true),

        };

        MBeanParameterInfo[] performCheckImeiParam = new MBeanParameterInfo[] { new MBeanParameterInfo("imei", String.class.getName(), "IMEI's digits string")};
        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(), "Index number or value") };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
                new MBeanOperationInfo("performCheckImeiRequest", "Send CheckIMEI request for provided IMEI", performCheckImeiParam/*signString*/ , String.class.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putMapProtocolVersion", "MAP protocol version: " + "1, 2 or 3", signString, Void.TYPE.getName(),
                        MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("closeCurrentDialog", "Closing the current dialog", null, String.class.getName(), MBeanOperationInfo.ACTION),

                new MBeanOperationInfo("putCheckImeiClientAction",
                        "The mode of CheckImeiClient work. 1:VAL_MANUAL_OPERATION,2:VAL_AUTO_SendCheckImeiRequest", signString, Void.TYPE.getName(),
                        MBeanOperationInfo.ACTION),
        };
        return new MBeanInfo(TestCheckImeiClientMan.class.getName(), "CheckImeiClient test parameters management", attributes, null, operations, null);
    }
}
