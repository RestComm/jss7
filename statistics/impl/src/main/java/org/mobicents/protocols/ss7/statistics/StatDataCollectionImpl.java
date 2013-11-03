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

import java.util.Date;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.statistics.api.StatCounterCollection;
import org.mobicents.protocols.ss7.statistics.api.StatDataCollection;
import org.mobicents.protocols.ss7.statistics.api.StatDataCollectorType;
import org.mobicents.protocols.ss7.statistics.api.StatResult;

/**
*
* Collection of data for all StatDataCollector-style counters
* Contains StatCounterCollection data depending on a counter name
*
* @author sergey vetyutnev
*
*/
public class StatDataCollectionImpl implements StatDataCollection {

    private FastMap<String, StatCounterCollection> coll = new FastMap<String, StatCounterCollection>();

    public StatCounterCollection registerStatCounterCollector(String counterName, StatDataCollectorType type) {
        synchronized (this) {
            StatCounterCollectionImpl c = new StatCounterCollectionImpl(counterName, type);
            coll.put(counterName, c);
            return c;
        }
    }

    public StatCounterCollection unregisterStatCounterCollector(String counterName) {
        synchronized (this) {
            StatCounterCollection c = coll.remove(counterName);
            return c;
        }
    }

    public StatCounterCollection getStatCounterCollector(String counterName) {
        synchronized (this) {
            StatCounterCollection c = coll.get(counterName);
            return c;
        }
    }

    public void clearDeadCampaignes(Date lastTime) {
        synchronized (this) {
            for (String s : coll.keySet()) {
                StatCounterCollection d = coll.get(s);
                d.clearDeadCampaignes(lastTime);
            }
        }
    }

    public StatResult restartAndGet(String counterName, String campaignName) {
        StatCounterCollection scc;
        synchronized (this) {
            scc = this.coll.get(counterName);
        }
        if (scc != null) {
            return scc.restartAndGet(campaignName);
        } else {
            return null;
        }
    }

    public void updateData(String counterName, long newVal) {
        StatCounterCollection scc;
        synchronized (this) {
            scc = this.coll.get(counterName);
        }
        if (scc != null) {
            scc.updateData(newVal);
        }
    }

    public void updateData(String counterName, String newVal) {
        StatCounterCollection scc;
        synchronized (this) {
            scc = this.coll.get(counterName);
        }
        if (scc != null) {
            scc.updateData(newVal);
        }
    }
}
