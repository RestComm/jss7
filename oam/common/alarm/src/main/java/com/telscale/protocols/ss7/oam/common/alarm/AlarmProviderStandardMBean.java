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

package com.telscale.protocols.ss7.oam.common.alarm;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationEmitter;
import javax.management.StandardEmitterMBean;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AlarmProviderStandardMBean extends StandardEmitterMBean {

    public AlarmProviderStandardMBean(AlarmProvider impl, Class<AlarmProviderMBean> intf, NotificationEmitter emitter)
            throws NotCompliantMBeanException {
        super(impl, intf, emitter);
    }

    @Override
    public MBeanInfo getMBeanInfo() {

        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {
                new MBeanAttributeInfo("CurrentAlarmList", CurrentAlarmList.class.getName(),
                        "A list of alarms that are currently active", true, false, false),
                new MBeanAttributeInfo("AlarmProviderObjectPath", String.class.getName(),
                        "Returns a prefix that AlarmProvider will be add at the begin of AlarmMessage.ObjectPath field", true,
                        false, false), };

        // MBeanParameterInfo[] signString = new MBeanParameterInfo[] { new MBeanParameterInfo("val", String.class.getName(),
        // "Index number or value") };
        // MBeanParameterInfo[] signEmpty = new MBeanParameterInfo[] { };

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
        // new MBeanOperationInfo("putInstance_L1Value", "Tester Level 1 agent: 0:NO,1:M3UA,2:DialogicCard", signString,
        // Void.TYPE.getName(), MBeanOperationInfo.ACTION),
        // new MBeanOperationInfo("putInstance_L2Value", "Tester Level 2 agent: 0:NO,1:SCCP,2:ISUP", signString,
        // Void.TYPE.getName(), MBeanOperationInfo.ACTION),
        // new MBeanOperationInfo("putInstance_L3Value", "Tester Level 3 agent: 0:NO,1:MAP,2:CAP,3:INAP", signString,
        // Void.TYPE.getName(), MBeanOperationInfo.ACTION),
        // new MBeanOperationInfo("putInstance_TestTaskValue",
        // "Tester task: 0:NO,1:USSD_TEST_CLIENT,2:USSD_TEST_SERVER,3:SMSC_SMS_DELIVER,4:DIALOGIC_MTU_INTERCONNECT,5:DIALOGIC_MTR_INTERCONNECT",
        // signString, Void.TYPE.getName(), MBeanOperationInfo.ACTION),
        // new MBeanOperationInfo("start", "Starting a tester process", signEmpty, Void.TYPE.getName(),
        // MBeanOperationInfo.ACTION),
        // new MBeanOperationInfo("stop", "Stopping a tester process", signEmpty, Void.TYPE.getName(),
        // MBeanOperationInfo.ACTION),
        // new MBeanOperationInfo("quit", "Stop a tester process and terminate an application", signEmpty, Void.TYPE.getName(),
        // MBeanOperationInfo.ACTION),
        };

        MBeanNotificationInfo[] notifications = new MBeanNotificationInfo[] { new MBeanNotificationInfo(
                new String[] { AlarmProvider.ALARM_EVENT }, AlarmNotification.class.getName(), "Alarm notification"), };

        return new MBeanInfo(AlarmProvider.class.getName(), "AlarmProvider", attributes, null, operations, notifications);
    }

}
