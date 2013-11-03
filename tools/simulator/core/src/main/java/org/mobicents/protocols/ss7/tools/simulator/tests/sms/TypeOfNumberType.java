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

package org.mobicents.protocols.ss7.tools.simulator.tests.sms;

import java.util.Hashtable;

import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TypeOfNumberType extends EnumeratedBase {

    private static final long serialVersionUID = 3669919751669763439L;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(TypeOfNumber.Unknown.getCode(), TypeOfNumber.Unknown.toString());
        intMap.put(TypeOfNumber.InternationalNumber.getCode(), TypeOfNumber.InternationalNumber.toString());
        intMap.put(TypeOfNumber.NationalNumber.getCode(), TypeOfNumber.NationalNumber.toString());
        intMap.put(TypeOfNumber.NetworkSpecificNumber.getCode(), TypeOfNumber.NetworkSpecificNumber.toString());
        intMap.put(TypeOfNumber.SubscriberNumber.getCode(), TypeOfNumber.SubscriberNumber.toString());
        intMap.put(TypeOfNumber.Alphanumeric.getCode(), TypeOfNumber.Alphanumeric.toString());
        intMap.put(TypeOfNumber.AbbreviatedNumber.getCode(), TypeOfNumber.AbbreviatedNumber.toString());
        intMap.put(TypeOfNumber.Reserved.getCode(), TypeOfNumber.Reserved.toString());

        stringMap.put(TypeOfNumber.Unknown.toString(), TypeOfNumber.Unknown.getCode());
        stringMap.put(TypeOfNumber.InternationalNumber.toString(), TypeOfNumber.InternationalNumber.getCode());
        stringMap.put(TypeOfNumber.NationalNumber.toString(), TypeOfNumber.NationalNumber.getCode());
        stringMap.put(TypeOfNumber.NetworkSpecificNumber.toString(), TypeOfNumber.NetworkSpecificNumber.getCode());
        stringMap.put(TypeOfNumber.SubscriberNumber.toString(), TypeOfNumber.SubscriberNumber.getCode());
        stringMap.put(TypeOfNumber.Alphanumeric.toString(), TypeOfNumber.Alphanumeric.getCode());
        stringMap.put(TypeOfNumber.AbbreviatedNumber.toString(), TypeOfNumber.AbbreviatedNumber.getCode());
        stringMap.put(TypeOfNumber.Reserved.toString(), TypeOfNumber.Reserved.getCode());
    }

    public TypeOfNumberType() {
    }

    public TypeOfNumberType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public TypeOfNumberType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public TypeOfNumberType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static TypeOfNumberType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new TypeOfNumberType(TypeOfNumber.InternationalNumber.getCode());
        else
            return new TypeOfNumberType(i1);
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
