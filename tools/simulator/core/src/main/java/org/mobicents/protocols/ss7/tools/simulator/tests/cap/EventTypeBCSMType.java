/*
 * TeleStax, Open Source Cloud Communications  Copyright 2017.
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

package org.mobicents.protocols.ss7.tools.simulator.tests.cap;

import java.util.Hashtable;

import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;

/**
 *
 * @author marek nowacki
 *
 */
public class EventTypeBCSMType extends EnumeratedBase {

    private static final long serialVersionUID = -8667527386628252814L;

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(EventTypeBCSM.collectedInfo.getCode(), EventTypeBCSM.collectedInfo.toString());
        intMap.put(EventTypeBCSM.analyzedInformation.getCode(), EventTypeBCSM.analyzedInformation.toString());
        intMap.put(EventTypeBCSM.routeSelectFailure.getCode(), EventTypeBCSM.routeSelectFailure.toString());
        intMap.put(EventTypeBCSM.oCalledPartyBusy.getCode(), EventTypeBCSM.oCalledPartyBusy.toString());
        intMap.put(EventTypeBCSM.oNoAnswer.getCode(), EventTypeBCSM.oNoAnswer.toString());
        intMap.put(EventTypeBCSM.oAnswer.getCode(), EventTypeBCSM.oAnswer.toString());
        intMap.put(EventTypeBCSM.oMidCall.getCode(), EventTypeBCSM.oMidCall.toString());
        intMap.put(EventTypeBCSM.oDisconnect.getCode(), EventTypeBCSM.oDisconnect.toString());
        intMap.put(EventTypeBCSM.oAbandon.getCode(), EventTypeBCSM.oAbandon.toString());
        intMap.put(EventTypeBCSM.termAttemptAuthorized.getCode(), EventTypeBCSM.termAttemptAuthorized.toString());
        intMap.put(EventTypeBCSM.tBusy.getCode(), EventTypeBCSM.tBusy.toString());
        intMap.put(EventTypeBCSM.tNoAnswer.getCode(), EventTypeBCSM.tNoAnswer.toString());
        intMap.put(EventTypeBCSM.tAnswer.getCode(), EventTypeBCSM.tAnswer.toString());
        intMap.put(EventTypeBCSM.tMidCall.getCode(), EventTypeBCSM.tMidCall.toString());
        intMap.put(EventTypeBCSM.tDisconnect.getCode(), EventTypeBCSM.tDisconnect.toString());
        intMap.put(EventTypeBCSM.tAbandon.getCode(), EventTypeBCSM.tAbandon.toString());
        intMap.put(EventTypeBCSM.oTermSeized.getCode(), EventTypeBCSM.oTermSeized.toString());
        intMap.put(EventTypeBCSM.callAccepted.getCode(), EventTypeBCSM.callAccepted.toString());
        intMap.put(EventTypeBCSM.oChangeOfPosition.getCode(), EventTypeBCSM.oChangeOfPosition.toString());
        intMap.put(EventTypeBCSM.tChangeOfPosition.getCode(), EventTypeBCSM.tChangeOfPosition.toString());
        intMap.put(EventTypeBCSM.oServiceChange.getCode(), EventTypeBCSM.oServiceChange.toString());
        intMap.put(EventTypeBCSM.tServiceChange.getCode(), EventTypeBCSM.tServiceChange.toString());

        stringMap.put(EventTypeBCSM.collectedInfo.toString(), EventTypeBCSM.collectedInfo.getCode());
        stringMap.put(EventTypeBCSM.analyzedInformation.toString(), EventTypeBCSM.analyzedInformation.getCode());
        stringMap.put(EventTypeBCSM.routeSelectFailure.toString(), EventTypeBCSM.routeSelectFailure.getCode());
        stringMap.put(EventTypeBCSM.oCalledPartyBusy.toString(), EventTypeBCSM.oCalledPartyBusy.getCode());
        stringMap.put(EventTypeBCSM.oNoAnswer.toString(), EventTypeBCSM.oNoAnswer.getCode());
        stringMap.put(EventTypeBCSM.oAnswer.toString(), EventTypeBCSM.oAnswer.getCode());
        stringMap.put(EventTypeBCSM.oMidCall.toString(), EventTypeBCSM.oMidCall.getCode());
        stringMap.put(EventTypeBCSM.oDisconnect.toString(), EventTypeBCSM.oDisconnect.getCode());
        stringMap.put(EventTypeBCSM.oAbandon.toString(), EventTypeBCSM.oAbandon.getCode());
        stringMap.put(EventTypeBCSM.termAttemptAuthorized.toString(), EventTypeBCSM.termAttemptAuthorized.getCode());
        stringMap.put(EventTypeBCSM.tBusy.toString(), EventTypeBCSM.tBusy.getCode());
        stringMap.put(EventTypeBCSM.tNoAnswer.toString(), EventTypeBCSM.tNoAnswer.getCode());
        stringMap.put(EventTypeBCSM.tAnswer.toString(), EventTypeBCSM.tAnswer.getCode());
        stringMap.put(EventTypeBCSM.tMidCall.toString(), EventTypeBCSM.tMidCall.getCode());
        stringMap.put(EventTypeBCSM.tDisconnect.toString(), EventTypeBCSM.tDisconnect.getCode());
        stringMap.put(EventTypeBCSM.tAbandon.toString(), EventTypeBCSM.tAbandon.getCode());
        stringMap.put(EventTypeBCSM.oTermSeized.toString(), EventTypeBCSM.oTermSeized.getCode());
        stringMap.put(EventTypeBCSM.callAccepted.toString(), EventTypeBCSM.callAccepted.getCode());
        stringMap.put(EventTypeBCSM.oChangeOfPosition.toString(), EventTypeBCSM.oChangeOfPosition.getCode());
        stringMap.put(EventTypeBCSM.tChangeOfPosition.toString(), EventTypeBCSM.tChangeOfPosition.getCode());
        stringMap.put(EventTypeBCSM.oServiceChange.toString(), EventTypeBCSM.oServiceChange.getCode());
        stringMap.put(EventTypeBCSM.tServiceChange.toString(), EventTypeBCSM.tServiceChange.getCode());
    }


    public EventTypeBCSMType() {
    }

    public EventTypeBCSMType(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public EventTypeBCSMType(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public EventTypeBCSMType(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static EventTypeBCSMType createInstance(String s) {
        Integer i1 = doCreateInstance(s, stringMap, intMap);
        if (i1 == null)
            return new EventTypeBCSMType(EventTypeBCSM.collectedInfo.getCode());
        else
            return new EventTypeBCSMType(i1);
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
