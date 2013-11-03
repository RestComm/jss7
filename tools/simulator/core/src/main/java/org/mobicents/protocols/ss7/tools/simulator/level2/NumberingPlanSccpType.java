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

package org.mobicents.protocols.ss7.tools.simulator.level2;

import java.util.Hashtable;

import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class NumberingPlanSccpType extends EnumeratedBase {

    private static final long serialVersionUID = 6312337607873455146L;
    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(NumberingPlan.UNKNOWN.getValue(), NumberingPlan.UNKNOWN.toString());
        intMap.put(NumberingPlan.ISDN_TELEPHONY.getValue(), NumberingPlan.ISDN_TELEPHONY.toString());
        intMap.put(NumberingPlan.GENERIC.getValue(), NumberingPlan.GENERIC.toString());
        intMap.put(NumberingPlan.DATA.getValue(), NumberingPlan.DATA.toString());
        intMap.put(NumberingPlan.TELEX.getValue(), NumberingPlan.TELEX.toString());
        intMap.put(NumberingPlan.MERITIME_MOBILE.getValue(), NumberingPlan.MERITIME_MOBILE.toString());
        intMap.put(NumberingPlan.LAND_MOBILE.getValue(), NumberingPlan.LAND_MOBILE.toString());
        intMap.put(NumberingPlan.ISDN_MOBILE.getValue(), NumberingPlan.ISDN_MOBILE.toString());
        intMap.put(NumberingPlan.PRIVATE.getValue(), NumberingPlan.PRIVATE.toString());
        intMap.put(NumberingPlan.RESERVED.getValue(), NumberingPlan.RESERVED.toString());

        stringMap.put(NumberingPlan.UNKNOWN.toString(), NumberingPlan.UNKNOWN.getValue());
        stringMap.put(NumberingPlan.ISDN_TELEPHONY.toString(), NumberingPlan.ISDN_TELEPHONY.getValue());
        stringMap.put(NumberingPlan.GENERIC.toString(), NumberingPlan.GENERIC.getValue());
        stringMap.put(NumberingPlan.DATA.toString(), NumberingPlan.DATA.getValue());
        stringMap.put(NumberingPlan.TELEX.toString(), NumberingPlan.TELEX.getValue());
        stringMap.put(NumberingPlan.MERITIME_MOBILE.toString(), NumberingPlan.MERITIME_MOBILE.getValue());
        stringMap.put(NumberingPlan.LAND_MOBILE.toString(), NumberingPlan.LAND_MOBILE.getValue());
        stringMap.put(NumberingPlan.ISDN_MOBILE.toString(), NumberingPlan.ISDN_MOBILE.getValue());
        stringMap.put(NumberingPlan.PRIVATE.toString(), NumberingPlan.PRIVATE.getValue());
        stringMap.put(NumberingPlan.RESERVED.toString(), NumberingPlan.RESERVED.getValue());
    }

    public NumberingPlanSccpType() {
    }

    public NumberingPlanSccpType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public NumberingPlanSccpType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public NumberingPlanSccpType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static NumberingPlanSccpType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new NumberingPlanSccpType(NumberingPlan.UNKNOWN.getValue());
        else
            return new NumberingPlanSccpType(i1);
    }

    @Override
    protected Hashtable<Integer, String> getIntTable() {
        return intMap;
    }

    @Override
    protected Hashtable<String, Integer> getStringTable() {
        return stringMap;
    }

}
