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

package org.restcomm.protocols.ss7.oam.common.alarm;

import java.util.List;
import javolution.util.FastList;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AlarmListenerCollection {

    private transient List<AlarmListener> alarmListeners = new FastList<AlarmListener>();
    private String alarmProviderObjectPath;
    private String alarmProviderObjectPathWithSlash;

    public void registerAlarmListener(AlarmListener listener) {
        synchronized (this) {
            this.alarmListeners.add(listener);
        }
    }

    public void unregisterAlarmListener(AlarmListener listener) {
        synchronized (this) {
            this.alarmListeners.remove(listener);
        }
    }

    public void prepareAlarm(AlarmMessage alarm) {
        if (alarmProviderObjectPath != null)
            alarm.addPrefixToAlarmSource(alarmProviderObjectPath);
    }

    public void onAlarm(AlarmMessage alarm) {
        this.prepareAlarm(alarm);

        synchronized (this) {
            for (AlarmListener listener : this.alarmListeners) {
                listener.onAlarm(alarm);
            }
        }
    }

    /**
     * Returns a prefix that AlarmProvider will be add at the begin of AlarmMessage.ObjectPath field (without "/")
     */
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

}
