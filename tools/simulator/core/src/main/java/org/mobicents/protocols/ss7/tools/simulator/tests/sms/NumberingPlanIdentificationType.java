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

import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class NumberingPlanIdentificationType extends EnumeratedBase {

    private static final long serialVersionUID = 4016746411943985346L;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(NumberingPlanIdentification.Unknown.getCode(), NumberingPlanIdentification.Unknown.toString());
        intMap.put(NumberingPlanIdentification.ISDNTelephoneNumberingPlan.getCode(),
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan.toString());
        intMap.put(NumberingPlanIdentification.DataNumberingPlan.getCode(),
                NumberingPlanIdentification.DataNumberingPlan.toString());
        intMap.put(NumberingPlanIdentification.TelexNumberingPlan.getCode(),
                NumberingPlanIdentification.TelexNumberingPlan.toString());
        intMap.put(NumberingPlanIdentification.ServiceCentreSpecificPlan1.getCode(),
                NumberingPlanIdentification.ServiceCentreSpecificPlan1.toString());
        intMap.put(NumberingPlanIdentification.ServiceCentreSpecificPlan2.getCode(),
                NumberingPlanIdentification.ServiceCentreSpecificPlan2.toString());
        intMap.put(NumberingPlanIdentification.NationalNumberingPlan.getCode(),
                NumberingPlanIdentification.NationalNumberingPlan.toString());
        intMap.put(NumberingPlanIdentification.PrivateNumberingPlan.getCode(),
                NumberingPlanIdentification.PrivateNumberingPlan.toString());
        intMap.put(NumberingPlanIdentification.ERMESNumberingPlan.getCode(),
                NumberingPlanIdentification.ERMESNumberingPlan.toString());
        intMap.put(NumberingPlanIdentification.Reserved.getCode(), NumberingPlanIdentification.Reserved.toString());

        stringMap.put(NumberingPlanIdentification.Unknown.toString(), NumberingPlanIdentification.Unknown.getCode());
        stringMap.put(NumberingPlanIdentification.ISDNTelephoneNumberingPlan.toString(),
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan.getCode());
        stringMap.put(NumberingPlanIdentification.DataNumberingPlan.toString(),
                NumberingPlanIdentification.DataNumberingPlan.getCode());
        stringMap.put(NumberingPlanIdentification.TelexNumberingPlan.toString(),
                NumberingPlanIdentification.TelexNumberingPlan.getCode());
        stringMap.put(NumberingPlanIdentification.ServiceCentreSpecificPlan1.toString(),
                NumberingPlanIdentification.ServiceCentreSpecificPlan1.getCode());
        stringMap.put(NumberingPlanIdentification.ServiceCentreSpecificPlan2.toString(),
                NumberingPlanIdentification.ServiceCentreSpecificPlan2.getCode());
        stringMap.put(NumberingPlanIdentification.NationalNumberingPlan.toString(),
                NumberingPlanIdentification.NationalNumberingPlan.getCode());
        stringMap.put(NumberingPlanIdentification.PrivateNumberingPlan.toString(),
                NumberingPlanIdentification.PrivateNumberingPlan.getCode());
        stringMap.put(NumberingPlanIdentification.ERMESNumberingPlan.toString(),
                NumberingPlanIdentification.ERMESNumberingPlan.getCode());
        stringMap.put(NumberingPlanIdentification.Reserved.toString(), NumberingPlanIdentification.Reserved.getCode());
    }

    public NumberingPlanIdentificationType() {
    }

    public NumberingPlanIdentificationType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public NumberingPlanIdentificationType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public NumberingPlanIdentificationType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static NumberingPlanIdentificationType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new NumberingPlanIdentificationType(NumberingPlanIdentification.ISDNTelephoneNumberingPlan.getCode());
        else
            return new NumberingPlanIdentificationType(i1);
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
