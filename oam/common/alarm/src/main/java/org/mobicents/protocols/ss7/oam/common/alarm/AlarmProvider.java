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

package org.mobicents.protocols.ss7.oam.common.alarm;

import java.util.List;

import javax.management.NotCompliantMBeanException;
import javax.management.NotificationBroadcasterSupport;

import org.mobicents.protocols.ss7.oam.common.jmx.MBeanHost;
import org.mobicents.protocols.ss7.oam.common.jmx.MBeanType;

import javolution.util.FastList;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AlarmProvider extends NotificationBroadcasterSupport implements AlarmProviderMBean, AlarmListener {

    public static String ALARM_EVENT = "Alarm_Event";

    private final MBeanHost beanHost;
    private final AlarmMediator alarmMediator;

    private List<AlarmMediator> alarmMediators = new FastList<AlarmMediator>();
    private long sequenceNumber = 0;
    private String alarmProviderObjectPath;
    private String alarmProviderObjectPathWithSlash;
    private String name = "AlarmHost";

    public AlarmProvider(MBeanHost beanHost, AlarmMediator alarmMediator) {
        super();
        this.beanHost = beanHost;
        this.alarmMediator = alarmMediator;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String val) {
        this.name = val;
    }

    public void start() {
        synchronized (this) {
            try {
                AlarmProviderStandardMBean sbean = new AlarmProviderStandardMBean(this, AlarmProviderMBean.class, this);
                this.beanHost.registerMBean(AlarmLayer.ALARM, AlarmManagementType.MANAGEMENT, this.name, sbean);
            } catch (NotCompliantMBeanException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            alarmMediator.registerAlarmListener(this);
            this.registerAlarmMediator(alarmMediator);
        }
    }

    public void stop() {
        synchronized (this) {
            alarmMediator.unregisterAlarmListener(this);
            this.unregisterAlarmMediator(alarmMediator);
        }
    }

    public void registerAlarmMediator(AlarmMediator am) {
        synchronized (this.alarmMediators) {
            this.alarmMediators.add(am);
        }
    }

    public void unregisterAlarmMediator(AlarmMediator am) {
        synchronized (this.alarmMediators) {
            this.alarmMediators.remove(am);
        }
    }

    @Override
    public String getAlarmProviderObjectPath() {
        return alarmProviderObjectPath;
    }

    /**
     * Set a prefix that AlarmProvider will be add at the begin of AlarmMessage.ObjectPath field (without "/")
     */
    public void setAlarmProviderObjectPath(String value) {
        alarmProviderObjectPath = value;
        if (alarmProviderObjectPath != null && !alarmProviderObjectPath.equals(""))
            alarmProviderObjectPathWithSlash = "/" + alarmProviderObjectPath;
        else
            alarmProviderObjectPathWithSlash = null;
    }

    @Override
    public CurrentAlarmList getCurrentAlarmList() {

        CurrentAlarmListImpl al = new CurrentAlarmListImpl();
        synchronized (this.alarmMediators) {
            for (AlarmMediator amr : this.alarmMediators) {
                CurrentAlarmList temp = amr.getCurrentAlarmList();
                for (AlarmMessage alm : temp.getCurrentAlarmList()) {
                    if (alarmProviderObjectPath != null)
                        alm.addPrefixToAlarmSource(alarmProviderObjectPath);
                    al.addAlarm(alm);
                }
            }
        }

        al.sortAlarms();
        return al;
    }

    @Override
    public void onAlarm(AlarmMessage alarm) {
        if (alarmProviderObjectPath != null)
            alarm.addPrefixToAlarmSource(alarmProviderObjectPath);
        this.doSendNotif(alarm);
    }

    private synchronized void doSendNotif(AlarmMessage alarm) {
        AlarmNotification notif = new AlarmNotification(ALARM_EVENT + "-" + alarmProviderObjectPath, "AlarmProvider",
                ++sequenceNumber, System.currentTimeMillis(), null, alarm);
        // String type, Object source, long sequenceNumber, long timeStamp, String msg, AlarmMessage alarmMessage
        // notif.setUserData(userData);
        this.sendNotification(notif);
    }

    public enum AlarmManagementType implements MBeanType {
        MANAGEMENT("Management");

        private final String name;

        public static final String NAME_MANAGEMENT = "Management";

        private AlarmManagementType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static AlarmManagementType getInstance(String name) {
            if (NAME_MANAGEMENT.equals(name)) {
                return MANAGEMENT;
            }

            return null;
        }
    }
}
