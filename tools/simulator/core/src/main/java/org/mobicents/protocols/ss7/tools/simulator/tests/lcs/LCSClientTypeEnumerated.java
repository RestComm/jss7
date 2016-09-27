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

package org.mobicents.protocols.ss7.tools.simulator.tests.lcs;

import java.util.Hashtable;

import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LCSClientTypeEnumerated extends EnumeratedBase {

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(LCSClientType.emergencyServices.getType(),LCSClientType.emergencyServices.toString());
        intMap.put(LCSClientType.valueAddedServices.getType(),LCSClientType.valueAddedServices.toString());
        intMap.put(LCSClientType.plmnOperatorServices.getType(),LCSClientType.plmnOperatorServices.toString());
        intMap.put(LCSClientType.lawfulInterceptServices.getType(),LCSClientType.lawfulInterceptServices.toString());
        stringMap.put(LCSClientType.emergencyServices.toString(),LCSClientType.emergencyServices.getType());
        stringMap.put(LCSClientType.valueAddedServices.toString(),LCSClientType.valueAddedServices.getType());
        stringMap.put(LCSClientType.plmnOperatorServices.toString(),LCSClientType.plmnOperatorServices.getType());
        stringMap.put(LCSClientType.lawfulInterceptServices.toString(),LCSClientType.lawfulInterceptServices.getType());
    }

    public LCSClientTypeEnumerated() {
    }

    public LCSClientTypeEnumerated(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public LCSClientTypeEnumerated(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public LCSClientTypeEnumerated(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static LCSClientTypeEnumerated createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new LCSClientTypeEnumerated(LCSClientType.emergencyServices.getType());
        else
            return new LCSClientTypeEnumerated(i1);
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
