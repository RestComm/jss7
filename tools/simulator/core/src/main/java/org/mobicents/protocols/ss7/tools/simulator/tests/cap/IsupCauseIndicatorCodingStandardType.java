/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.tools.simulator.tests.cap;

import java.util.Hashtable;

import org.mobicents.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author mnowa
 *
 */
public class IsupCauseIndicatorCodingStandardType extends EnumeratedBase {

    private static final long serialVersionUID = 1173666813197137562L;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(IsupCauseIndicatorCodingStandard.ITUT.getCode(), IsupCauseIndicatorCodingStandard.ITUT.toString());
        intMap.put(IsupCauseIndicatorCodingStandard.IOS_IEC.getCode(), IsupCauseIndicatorCodingStandard.IOS_IEC.toString());
        intMap.put(IsupCauseIndicatorCodingStandard.national.getCode(), IsupCauseIndicatorCodingStandard.national.toString());
        intMap.put(IsupCauseIndicatorCodingStandard.specific.getCode(), IsupCauseIndicatorCodingStandard.specific.toString());

        stringMap.put(IsupCauseIndicatorCodingStandard.ITUT.toString(), IsupCauseIndicatorCodingStandard.ITUT.getCode());
        stringMap.put(IsupCauseIndicatorCodingStandard.IOS_IEC.toString(), IsupCauseIndicatorCodingStandard.IOS_IEC.getCode());
        stringMap.put(IsupCauseIndicatorCodingStandard.national.toString(), IsupCauseIndicatorCodingStandard.national.getCode());
        stringMap.put(IsupCauseIndicatorCodingStandard.specific.toString(), IsupCauseIndicatorCodingStandard.specific.getCode());
    }

    public IsupCauseIndicatorCodingStandardType() {
    }

    public IsupCauseIndicatorCodingStandardType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public IsupCauseIndicatorCodingStandardType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public IsupCauseIndicatorCodingStandardType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static IsupCauseIndicatorCodingStandardType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new IsupCauseIndicatorCodingStandardType(EquipmentStatus.whiteListed.getCode());
        else
            return new IsupCauseIndicatorCodingStandardType(i1);
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
