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

import javolution.util.FastMap;

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
