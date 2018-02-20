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

package org.restcomm.protocols.ss7.tools.simulator.tests.ati;

import java.util.Hashtable;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.restcomm.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
*
* @author sergey vetyutnev
*
*/
public class AtiDomainType extends EnumeratedBase {

    private static final long serialVersionUID = 8544451513960148816L;

    public static final int NO_VALUE = -1;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(NO_VALUE, "No value");
        intMap.put(DomainType.csDomain.getType(), "csDomain");
        intMap.put(DomainType.psDomain.getType(), "psDomain");

        stringMap.put("No value", NO_VALUE);
        stringMap.put("csDomain", DomainType.csDomain.getType());
        stringMap.put("psDomain", DomainType.psDomain.getType());
    }

    public AtiDomainType() {
    }

    public AtiDomainType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public AtiDomainType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public AtiDomainType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static AtiDomainType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new AtiDomainType(DomainType.csDomain.getType());
        else
            return new AtiDomainType(i1);
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
