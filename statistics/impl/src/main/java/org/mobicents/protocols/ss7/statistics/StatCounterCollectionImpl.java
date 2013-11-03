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

package org.mobicents.protocols.ss7.statistics;

import java.util.ArrayList;
import java.util.Date;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.statistics.api.StatCounterCollection;
import org.mobicents.protocols.ss7.statistics.api.StatDataCollectorType;
import org.mobicents.protocols.ss7.statistics.api.StatResult;

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
