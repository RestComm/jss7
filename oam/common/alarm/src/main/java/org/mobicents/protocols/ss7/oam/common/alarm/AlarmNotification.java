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

import javax.management.Notification;

/**
 *
 * Notifications about alarms that AlarmProvider sends as JMX notifications
 *
 * @author sergey vetyutnev
 *
 */
public class AlarmNotification extends Notification {

    private static final long serialVersionUID = -5746460310998997260L;

    private AlarmMessage alarmMessage;

    public AlarmNotification(String type, Object source, long sequenceNumber, long timeStamp, String msg,
            AlarmMessage alarmMessage) {
        super(type, source, sequenceNumber, timeStamp, msg);

        this.alarmMessage = alarmMessage;
    }

    public AlarmMessage getAlarmMessage() {
        return this.alarmMessage;
    }
}
