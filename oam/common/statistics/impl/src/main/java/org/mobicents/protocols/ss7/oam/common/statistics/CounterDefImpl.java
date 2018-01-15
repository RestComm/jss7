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

package org.mobicents.protocols.ss7.oam.common.statistics;

import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterDef;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterType;

/**
*
* @author sergey vetyutnev
*
*/
public class CounterDefImpl implements CounterDef {

    public static final String OBJECT_NAME_SEPARATOR = "-";

    public CounterType counterType;
    public String counterName;
    public String groupName;
    public String objectName;
    public String description;

    public CounterDefImpl(CounterType counterType, String counterName, String description) {
        this.counterType = counterType;
        this.counterName = counterName;
        this.description = description;
    }

    public CounterDefImpl(CounterType counterType, String groupName, String objectName, String description) {
        this.counterType = counterType;
        this.groupName = groupName;
        this.objectName = objectName;
        this.counterName = groupName + OBJECT_NAME_SEPARATOR + objectName;
        this.description = description;
    }

    @Override
    public String getCounterName() {
        return counterName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getObjectName() {
        return objectName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public CounterType getCounterType() {
        return counterType;
    }

    @Override
    public String toString() {
        return "CounterDefImpl [counterType=" + counterType + ", counterName=" + counterName + ", description=" + description
                + "]";
    }

}
