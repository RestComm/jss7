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

package org.restcomm.protocols.ss7.oam.common.statistics;

import java.util.ArrayList;

import org.restcomm.protocols.ss7.oam.common.statistics.api.ComplexValue;
import org.restcomm.protocols.ss7.oam.common.statistics.api.CounterDef;
import org.restcomm.protocols.ss7.oam.common.statistics.api.CounterValue;

/**
*
* @author sergey vetyutnev
*
*/
public class CounterValueImpl implements CounterValue {

    private CounterDef counterDef;
    private String objectName;
    private long longValue;
    private double doubleValue;
    private ArrayList<ComplexValue> complexValue = new ArrayList<ComplexValue>();

    public CounterValueImpl(CounterDef counterDef, String objectName, long longValue) {
        this.counterDef = counterDef;
        this.objectName = objectName;
        this.longValue = longValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public void addComplexValue(ComplexValue val) {
        complexValue.add(val);
    }

    @Override
    public CounterDef getCounterDef() {
        return counterDef;
    }

    @Override
    public String getObjectName() {
        return objectName;
    }

    @Override
    public long getLongValue() {
        return longValue;
    }

    @Override
    public double getDoubleValue() {
        return doubleValue;
    }

    @Override
    public ComplexValue[] getComplexValue() {
        ComplexValue[] res = new ComplexValue[complexValue.size()];
        complexValue.toArray(res);
        return res;
    }

}
