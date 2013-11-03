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

package org.mobicents.protocols.ss7.tools.simulator.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.SortedSet;
import java.util.TreeSet;

import com.sun.jdmk.Enumerated;

/**
 *
 * @author sergey vetyutnev
 *
 */
public abstract class EnumeratedBase extends Enumerated {

    private static final long serialVersionUID = 8688613658976075363L;

    public EnumeratedBase() {
    }

    public EnumeratedBase(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public EnumeratedBase(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public EnumeratedBase(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    protected static Integer doCreateInstance(String s, Hashtable<String, Integer> stringMap, Hashtable<Integer, String> intMap) {
        if (s == null)
            return null;

        String sx = s.toLowerCase();
        Enumeration<String> enS = stringMap.keys();
        while (enS.hasMoreElements()) {
            String s1 = enS.nextElement();
            if (s1.toLowerCase().equals(sx)) {
                return stringMap.get(s1);
            }
        }

        try {
            int i2 = Integer.parseInt(s);
            Enumeration<Integer> enI = intMap.keys();
            while (enI.hasMoreElements()) {
                int i1 = enI.nextElement();
                if (i1 == i2) {
                    return i1;
                }
            }
        } catch (Exception e) {

        }

        return null;
    }

    public EnumeratedBase[] getList() {
        Method[] mm = this.getClass().getMethods();
        Method mt = null;
        for (Method m : mm) {
            if (m.getName() == "createInstance") {
                mt = m;
                break;
            }
        }
        if (mt == null)
            return new EnumeratedBase[0];

        Hashtable<Integer, String> intMap = this.getIntTable();
        Enumeration<Integer> enI = intMap.keys();
        ArrayList<EnumeratedBase> res = new ArrayList<EnumeratedBase>();

        SortedSet<Integer> sl = new TreeSet<Integer>();
        while (enI.hasMoreElements()) {
            Integer i1 = enI.nextElement();
            sl.add(i1);
        }
        for (Integer i1 : sl) {
            try {
                Object rs = mt.invoke(null, i1.toString());
                EnumeratedBase rs2 = (EnumeratedBase) rs;
                res.add(rs2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        EnumeratedBase[] res2 = new EnumeratedBase[res.size()];
        res.toArray(res2);

        return res2;
    }

}
