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

import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LCSEventType extends EnumeratedBase {

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(LCSEvent.deferredmtlrResponse.getEvent(), LCSEvent.deferredmtlrResponse.toString());
        intMap.put(LCSEvent.emergencyCallOrigination.getEvent(), LCSEvent.emergencyCallOrigination.toString());
        intMap.put(LCSEvent.emergencyCallRelease.getEvent(), LCSEvent.emergencyCallRelease.toString());
        intMap.put(LCSEvent.molr.getEvent(), LCSEvent.molr.toString());
        stringMap.put(LCSEvent.deferredmtlrResponse.toString(), LCSEvent.deferredmtlrResponse.getEvent());
        stringMap.put(LCSEvent.emergencyCallOrigination.toString(), LCSEvent.emergencyCallOrigination.getEvent());
        stringMap.put(LCSEvent.emergencyCallRelease.toString(), LCSEvent.emergencyCallRelease.getEvent());
        stringMap.put(LCSEvent.molr.toString(), LCSEvent.molr.getEvent());
    }

    public LCSEventType() {
    }

    public LCSEventType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public LCSEventType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public LCSEventType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static LCSEventType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new LCSEventType(LCSEvent.deferredmtlrResponse.getEvent());
        else
            return new LCSEventType(i1);
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
