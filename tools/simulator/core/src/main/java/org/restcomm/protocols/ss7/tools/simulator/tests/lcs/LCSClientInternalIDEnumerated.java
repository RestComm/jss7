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

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.restcomm.protocols.ss7.tools.simulator.common.EnumeratedBase;

import java.util.Hashtable;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 */
public class LCSClientInternalIDEnumerated extends EnumeratedBase {

    private static Hashtable<String, Integer> stringMap = new Hashtable<String, Integer>();
    private static Hashtable<Integer, String> intMap = new Hashtable<Integer, String>();

    static {
        intMap.put(LCSClientInternalID.broadcastService.getId(), LCSClientInternalID.broadcastService.toString());
        intMap.put(LCSClientInternalID.oandMHPLMN.getId(), LCSClientInternalID.oandMHPLMN.toString());
        intMap.put(LCSClientInternalID.oandMVPLMN.getId(), LCSClientInternalID.oandMVPLMN.toString());
        intMap.put(LCSClientInternalID.anonymousLocation.getId(), LCSClientInternalID.anonymousLocation.toString());
        intMap.put(LCSClientInternalID.targetMSsubscribedService.getId(), LCSClientInternalID.targetMSsubscribedService.toString());

        stringMap.put(LCSClientInternalID.broadcastService.toString(), LCSClientInternalID.broadcastService.getId());
        stringMap.put(LCSClientInternalID.oandMHPLMN.toString(), LCSClientInternalID.oandMHPLMN.getId());
        stringMap.put(LCSClientInternalID.oandMVPLMN.toString(), LCSClientInternalID.oandMVPLMN.getId());
        stringMap.put(LCSClientInternalID.anonymousLocation.toString(), LCSClientInternalID.anonymousLocation.getId());
        stringMap.put(LCSClientInternalID.targetMSsubscribedService.toString(), LCSClientInternalID.targetMSsubscribedService.getId());
    }

    public LCSClientInternalIDEnumerated() {
    }

    public LCSClientInternalIDEnumerated(int val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public LCSClientInternalIDEnumerated(Integer val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public LCSClientInternalIDEnumerated(String val) throws java.lang.IllegalArgumentException {
        super(val);
    }

    public static LCSClientInternalIDEnumerated createInstance(String s) {
        Integer instance = doCreateInstance(s, stringMap, intMap);
        if (instance == null)
            return new LCSClientInternalIDEnumerated(LCSClientInternalID.broadcastService.getId());
        else
            return new LCSClientInternalIDEnumerated(instance);
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
