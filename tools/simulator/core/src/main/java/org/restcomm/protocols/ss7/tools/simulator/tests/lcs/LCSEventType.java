/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2018, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.restcomm.protocols.ss7.tools.simulator.tests.lcs;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.tools.simulator.common.EnumeratedBase;

import java.util.Hashtable;

/**
 * @author <a href="mailto:serg.vetyutnev@gmail.com"> Sergey Vetyutnev </a>
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
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
        Integer instance = doCreateInstance(s, stringMap, intMap);
        if (instance == null)
            return new LCSEventType(LCSEvent.deferredmtlrResponse.getEvent());
        else
            return new LCSEventType(instance);
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

