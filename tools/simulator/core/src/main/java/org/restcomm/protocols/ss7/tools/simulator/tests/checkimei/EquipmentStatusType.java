/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.tools.simulator.tests.checkimei;

import java.util.Hashtable;

import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.restcomm.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author mnowa
 *
 */
public class EquipmentStatusType extends EnumeratedBase {

    private static final long serialVersionUID = 3073666813197137562L;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(EquipmentStatus.whiteListed.getCode(), EquipmentStatus.whiteListed.toString());
        intMap.put(EquipmentStatus.blackListed.getCode(), EquipmentStatus.blackListed.toString());
        intMap.put(EquipmentStatus.greyListed.getCode(), EquipmentStatus.greyListed.toString());


        stringMap.put(EquipmentStatus.whiteListed.toString(), EquipmentStatus.whiteListed.getCode());
        stringMap.put(EquipmentStatus.blackListed.toString(), EquipmentStatus.blackListed.getCode());
        stringMap.put(EquipmentStatus.greyListed.toString(), EquipmentStatus.greyListed.getCode());
    }

    public EquipmentStatusType() {
    }

    public EquipmentStatusType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public EquipmentStatusType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public EquipmentStatusType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static EquipmentStatusType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new EquipmentStatusType(EquipmentStatus.whiteListed.getCode());
        else
            return new EquipmentStatusType(i1);
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
