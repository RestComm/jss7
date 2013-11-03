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

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class NatureOfAddressType extends EnumeratedBase {

    private static final long serialVersionUID = 7963736221782830909L;
    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(NatureOfAddress.UNKNOWN.getValue(), NatureOfAddress.UNKNOWN.toString());
        intMap.put(NatureOfAddress.SUBSCRIBER.getValue(), NatureOfAddress.SUBSCRIBER.toString());
        intMap.put(NatureOfAddress.RESERVED_NATIONAL_2.getValue(), NatureOfAddress.RESERVED_NATIONAL_2.toString());
        intMap.put(NatureOfAddress.NATIONAL.getValue(), NatureOfAddress.NATIONAL.toString());
        intMap.put(NatureOfAddress.INTERNATIONAL.getValue(), NatureOfAddress.INTERNATIONAL.toString());
        intMap.put(NatureOfAddress.RESERVED.getValue(), NatureOfAddress.RESERVED.toString());

        stringMap.put(NatureOfAddress.UNKNOWN.toString(), NatureOfAddress.UNKNOWN.getValue());
        stringMap.put(NatureOfAddress.SUBSCRIBER.toString(), NatureOfAddress.SUBSCRIBER.getValue());
        stringMap.put(NatureOfAddress.RESERVED_NATIONAL_2.toString(), NatureOfAddress.RESERVED_NATIONAL_2.getValue());
        stringMap.put(NatureOfAddress.NATIONAL.toString(), NatureOfAddress.NATIONAL.getValue());
        stringMap.put(NatureOfAddress.INTERNATIONAL.toString(), NatureOfAddress.INTERNATIONAL.getValue());
        stringMap.put(NatureOfAddress.RESERVED.toString(), NatureOfAddress.RESERVED.getValue());
    }

    public NatureOfAddressType() {
    }

    public NatureOfAddressType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public NatureOfAddressType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public NatureOfAddressType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static NatureOfAddressType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new NatureOfAddressType(NatureOfAddress.UNKNOWN.getValue());
        else
            return new NatureOfAddressType(i1);
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
