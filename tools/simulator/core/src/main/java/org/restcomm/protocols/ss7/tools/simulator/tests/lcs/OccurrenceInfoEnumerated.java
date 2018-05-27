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

import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.restcomm.protocols.ss7.tools.simulator.common.EnumeratedBase;

import java.util.Hashtable;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class OccurrenceInfoEnumerated extends EnumeratedBase {

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(OccurrenceInfo.oneTimeEvent.getInfo(), OccurrenceInfo.oneTimeEvent.toString());
        intMap.put(OccurrenceInfo.multipleTimeEvent.getInfo(), OccurrenceInfo.multipleTimeEvent.toString());

        stringMap.put(OccurrenceInfo.oneTimeEvent.toString(), OccurrenceInfo.oneTimeEvent.getInfo());
        stringMap.put(OccurrenceInfo.multipleTimeEvent.toString(), OccurrenceInfo.multipleTimeEvent.getInfo());
    }

    public OccurrenceInfoEnumerated() {
    }

    public OccurrenceInfoEnumerated(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public OccurrenceInfoEnumerated(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public OccurrenceInfoEnumerated(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static OccurrenceInfoEnumerated createInstance(String s) {
        Integer instance = doCreateInstance(s, stringMap, intMap);
        if (instance == null)
            return new OccurrenceInfoEnumerated(OccurrenceInfo.oneTimeEvent.getInfo());
        else
            return new OccurrenceInfoEnumerated(instance);
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
