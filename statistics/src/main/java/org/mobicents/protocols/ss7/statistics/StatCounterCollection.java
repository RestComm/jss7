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
public class StatCounterCollection {

    private String name;
    private StatDataCollectorType type;
    private FastMap<String, StatDataCollector> coll = new FastMap<String, StatDataCollector>();

    public StatCounterCollection(String name, StatDataCollectorType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void clearDeadCampaignes(Date lastTime) {
        ArrayList<String> toDel = new ArrayList<String>();
        for (String s : coll.keySet()) {
            StatDataCollector d = coll.get(s);
            if (d.getSessionStartTime().before(lastTime)) {
                toDel.add(s);
            }
        }
        for (String s : toDel) {
            coll.remove(s);
        }
    }

    public Long restartAndGet(String campaignName, Long newVal) {
        StatDataCollector sdc = coll.get(campaignName);
        if (sdc != null) {
            return sdc.restartAndGet();
        } else {
            switch (type) {
            case MIN:
                sdc = new StatDataCollectorMin(campaignName);
                break;
            case MAX:
                sdc = new StatDataCollectorMax(campaignName);
                break;
            }
            if (sdc != null) {
                coll.put(campaignName, sdc);
                if (newVal != null)
                    sdc.updateData(newVal);
            }
            return null;
        }
    }

    public void updateData(long newVal) {
        for (String s : coll.keySet()) {
            StatDataCollector d = coll.get(s);
            d.updateData(newVal);
        }
    }

}
