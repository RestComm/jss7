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

package com.telscale.protocols.ss7.oam.common.statistics;

import java.util.ArrayList;

import com.telscale.protocols.ss7.oam.common.statistics.api.CounterDef;
import com.telscale.protocols.ss7.oam.common.statistics.api.CounterDefSet;

/**
*
* @author sergey vetyutnev
*
*/
public class CounterDefSetImpl implements CounterDefSet {

    private String name;
    private ArrayList<CounterDef> counterDefs = new ArrayList<CounterDef>();

    public CounterDefSetImpl(String name) {
        this.name = name;
    }

    public void addCounterDef(CounterDef val) {
        counterDefs.add(val);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CounterDef[] getCounterDefs() {
        CounterDef[] res = new CounterDef[counterDefs.size()];
        counterDefs.toArray(res);
        return res;
    }

}
