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

package org.mobicents.protocols.ss7.statistics;

import java.util.ArrayList;
import java.util.Date;

import javolution.util.FastMap;

/**
*
* @author sergey vetyutnev
*
*/
public class StatCounterCollectionImpl implements StatCounterCollection {

    private String counterName;
    private StatDataCollectorType type;
    private FastMap<String, StatDataCollectorAbstractImpl> coll = new FastMap<String, StatDataCollectorAbstractImpl>();

    public StatCounterCollectionImpl(String counterName, StatDataCollectorType type) {
        this.counterName = counterName;
        this.type = type;
    }

    @Override
    public String getCounterName() {
        return counterName;
    }

    @Override
    public void clearDeadCampaignes(Date lastTime) {
        synchronized (this) {
            ArrayList<String> toDel = new ArrayList<String>();
            for (String s : coll.keySet()) {
                StatDataCollectorAbstractImpl d = coll.get(s);
                if (d.getSessionStartTime().before(lastTime)) {
                    toDel.add(s);
                }
            }
            for (String s : toDel) {
                coll.remove(s);
            }
        }
    }

    @Override
    public StatResult restartAndGet(String campaignName) {
        synchronized (this) {
            StatDataCollectorAbstractImpl sdc = coll.get(campaignName);
            if (sdc != null) {
                return sdc.restartAndGet();
            } else {
                switch (type) {
                case MIN:
                    sdc = new StatDataCollectorMin(campaignName);
                    sdc.reset();
                    break;
                case MAX:
                    sdc = new StatDataCollectorMax(campaignName);
                    sdc.reset();
                    break;
                case StringLongMap:
                    sdc = new StringLongMap(campaignName);
                    sdc.reset();
                    break;
                }
                if (sdc != null) {
                    coll.put(campaignName, sdc);
                }
                return null;
            }
        }
    }

    @Override
    public void updateData(long newVal) {
        synchronized (this) {
            for (String s : coll.keySet()) {
                StatDataCollectorAbstractImpl d = coll.get(s);
                d.updateData(newVal);
            }
        }
    }

    @Override
    public void updateData(String newVal) {
        synchronized (this) {
            for (String s : coll.keySet()) {
                StatDataCollectorAbstractImpl d = coll.get(s);
                d.updateData(newVal);
            }
        }
    }

}
