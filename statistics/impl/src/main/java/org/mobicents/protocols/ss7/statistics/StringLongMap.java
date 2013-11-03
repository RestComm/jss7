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

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.statistics.api.LongValue;
import org.mobicents.protocols.ss7.statistics.api.StatDataCollectorType;
import org.mobicents.protocols.ss7.statistics.api.StatResult;

/**
*
* @author sergey vetyutnev
*
*/
public class StringLongMap extends StatDataCollectorAbstractImpl {

    private FastMap<String, LongValue> data = new FastMap<String, LongValue>();

    public StringLongMap(String campaignName) {
        super(campaignName);
    }

    public StatResult restartAndGet() {
        synchronized (this) {
            StatResultStringLongMap res = new StatResultStringLongMap(this.data);
            this.data = new FastMap<String, LongValue>();
            this.reset();
            return res;
        }
    }

    protected void reset() {
        synchronized (this) {
            this.data.clear();
        }
    }

    @Override
    public void updateData(long newVal) {
    }

    @Override
    public void updateData(String name) {
        synchronized (this) {
            LongValue val = data.get(name);
            if (val == null) {
                val = new LongValue();
                data.put(name, val);
            }
            val.updateValue();
        }
    }

    @Override
    public StatDataCollectorType getStatDataCollectorType() {
        return StatDataCollectorType.StringLongMap;
    }

    public class StatResultStringLongMap implements StatResult {

        private FastMap<String, LongValue> data;

        public StatResultStringLongMap(FastMap<String, LongValue> data) {
            this.data = data;
        }

        @Override
        public long getLongValue() {
            return 0;
        }

        @Override
        public FastMap<String, LongValue> getStringLongValue() {
            return data;
        }

    }
}
