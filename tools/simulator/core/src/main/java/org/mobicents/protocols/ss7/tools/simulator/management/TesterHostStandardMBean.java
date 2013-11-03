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

package org.mobicents.protocols.ss7.tools.simulator.management;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.StandardEmitterMBean;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TesterHostStandardMBean extends StandardEmitterMBean {

    public TesterHostStandardMBean(TesterHost impl, Class<TesterHostMBean> intf, NotificationEmitter emitter)
            throws NotCompliantMBeanException {
        // super(intf, impl);
        super(impl, intf, emitter);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {
                new MBeanAttributeInfo("Started", boolean.class.getName(), "Tester host is started now", true, false, true),
                new MBeanAttributeInfo("Instance_L1", Instance_L1.class.getName(), "Tester Level 1 agent", true, true, false),
                new MBeanAttributeInfo("Instance_L2", Instance_L2.class.getName(), "Tester Level 2 agent", true, true, false),
                new MBeanAttributeInfo("Instance_L3", Instance_L3.class.getName(), "Tester Level 3 agent", true, true, false),
                new MBeanAttributeInfo("Instance_TestTask", Instance_TestTask.class.getName(), "Tester task", true, true, false),
                new MBeanAttributeInfo("Instance_L1_Value", String.class.getName(), "Tester Level 1 agent", true, false, false),
                new MBeanAttributeInfo("Instance_L2_Value", String.class.getName(), "Tester Level 2 agent", true, false, false),
                new MBeanAttributeInfo("Instance_L3_Value", String.class.getName(), "Tester Level 3 agent", true, false, false),
                new MBeanAttributeInfo("Instance_TestTask_Value", String.class.getName(), "Tester task", true, false, false),
                new MBeanAttributeInfo("L1State", String.class.getName(), "Tester Level 1 state", true, false, false),
                new MBeanAttributeInfo("L2State", String.class.getName(), "Tester Level 2 state", true, false, false),
                new MBeanAttributeInfo("L3State", String.class.getName(), "Tester Level 3 state", true, false, false),
                new MBeanAttributeInfo("TestTaskState", String.class.getName(), "Tester TestTask state", true, false, false), };

        MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(),
                "Index number or value") };
        MBeanParameterInfo[] signEmpty = new MBeanParameterInfo[] {};

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
                new MBeanOperationInfo("putInstance_L1Value", "Tester Level 1 agent: 0:NO,1:M3UA,2:DialogicCard", signString,
                        Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putInstance_L2Value", "Tester Level 2 agent: 0:NO,1:SCCP,2:ISUP", signString,
                        Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("putInstance_L3Value", "Tester Level 3 agent: 0:NO,1:MAP,2:CAP,3:INAP", signString,
                        Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo(
                        "putInstance_TestTaskValue",
                        "Tester task: 0:NO,1:USSD_TEST_CLIENT,2:USSD_TEST_SERVER,3:SMSC_SMS_DELIVER,4:DIALOGIC_MTU_INTERCONNECT,5:DIALOGIC_MTR_INTERCONNECT",
                        signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("start", "Starting a tester process", signEmpty, Void.TYPE.getName(),
                        MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("stop", "Stopping a tester process", signEmpty, Void.TYPE.getName(),
                        MBeanOperationInfo.ACTION),
                new MBeanOperationInfo("quit", "Stop a tester process and terminate an application", signEmpty,
                        Void.TYPE.getName(), MBeanOperationInfo.ACTION), };

        MBeanNotificationInfo[] notifications = new MBeanNotificationInfo[] { new MBeanNotificationInfo(
                new String[] { TesterHost.SS7_EVENT }, Notification.class.getName(), "SS7 events notification"), };

        return new MBeanInfo(TesterHost.class.getName(), "Tester host", attributes, null, operations, notifications);
    }
}
