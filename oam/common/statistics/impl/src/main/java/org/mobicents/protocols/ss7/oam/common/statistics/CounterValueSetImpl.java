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

import java.util.ArrayList;
import java.util.Date;

import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterValue;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterValueSet;

/**
*
* @author sergey vetyutnev
*
*/
public class CounterValueSetImpl implements CounterValueSet {

    public Date startTime;
    public Date endTime;
    public int duration;
    public int durationSeconds;
    public ArrayList<CounterValue> counterValues = new ArrayList<CounterValue>();

    public CounterValueSetImpl(Date startTime, Date endTime, int duration, int durationSeconds) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.durationSeconds = durationSeconds;
    }

    public void addCounterValue(CounterValue val) {
        counterValues.add(val);
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public int getDurationSeconds() {
        return durationSeconds;
    }

    @Override
    public CounterValue[] getCounterValues() {
        CounterValue[] res = new CounterValue[counterValues.size()];
        counterValues.toArray(res);
        return res;
    }

}
