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

import org.mobicents.protocols.ss7.statistics.api.LongValue;
import org.mobicents.protocols.ss7.statistics.api.StatResult;

/**
*
* @author sergey vetyutnev
*
*/
public abstract class StatDataCollectorLongImpl extends StatDataCollectorAbstractImpl {

    protected long val;

    public StatDataCollectorLongImpl(String campaignName) {
        super(campaignName);
    }

    public StatResult restartAndGet() {
        StatResultLong res = new StatResultLong(val);
        this.sessionStartTime = new Date();
        this.reset();
        return res;
    }

    public class StatResultLong implements StatResult {

        private long val;

        public StatResultLong(long val) {
            this.val = val;
        }

        @Override
        public long getLongValue() {
            return val;
        }

        @Override
        public FastMap<String, LongValue> getStringLongValue() {
            return null;
        }

    }
}
