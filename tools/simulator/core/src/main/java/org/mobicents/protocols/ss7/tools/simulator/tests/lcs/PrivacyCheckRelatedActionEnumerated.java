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

import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;
import org.mobicents.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PrivacyCheckRelatedActionEnumerated extends EnumeratedBase {

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(PrivacyCheckRelatedAction.allowedWithoutNotification.getAction(),PrivacyCheckRelatedAction.allowedWithoutNotification.toString());
        intMap.put(PrivacyCheckRelatedAction.allowedWithNotification.getAction(),PrivacyCheckRelatedAction.allowedWithNotification.toString());
        intMap.put(PrivacyCheckRelatedAction.allowedIfNoResponse.getAction(),PrivacyCheckRelatedAction.allowedIfNoResponse.toString());
        intMap.put(PrivacyCheckRelatedAction.restrictedIfNoResponse.getAction(),PrivacyCheckRelatedAction.restrictedIfNoResponse.toString());
        intMap.put(PrivacyCheckRelatedAction.notAllowed.getAction(),PrivacyCheckRelatedAction.notAllowed.toString());
        stringMap.put(PrivacyCheckRelatedAction.allowedWithoutNotification.toString(),PrivacyCheckRelatedAction.allowedWithoutNotification.getAction());
        stringMap.put(PrivacyCheckRelatedAction.allowedWithNotification.toString(),PrivacyCheckRelatedAction.allowedWithNotification.getAction());
        stringMap.put(PrivacyCheckRelatedAction.allowedIfNoResponse.toString(),PrivacyCheckRelatedAction.allowedIfNoResponse.getAction());
        stringMap.put(PrivacyCheckRelatedAction.restrictedIfNoResponse.toString(),PrivacyCheckRelatedAction.restrictedIfNoResponse.getAction());
        stringMap.put(PrivacyCheckRelatedAction.notAllowed.toString(),PrivacyCheckRelatedAction.notAllowed.getAction());
    }

    public PrivacyCheckRelatedActionEnumerated() {
    }

    public PrivacyCheckRelatedActionEnumerated(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public PrivacyCheckRelatedActionEnumerated(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public PrivacyCheckRelatedActionEnumerated(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static PrivacyCheckRelatedActionEnumerated createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new PrivacyCheckRelatedActionEnumerated(PrivacyCheckRelatedAction.allowedWithoutNotification.getAction());
        else
            return new PrivacyCheckRelatedActionEnumerated(i1);
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
