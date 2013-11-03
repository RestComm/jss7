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

package org.mobicents.protocols.ss7.isup.impl;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.mobicents.protocols.ss7.isup.CircuitManager;

/**
 * @author baranowb
 *
 */
public class CircuitManagerImpl implements CircuitManager {

    // ansi allows 14 bits for cic and 24 bits for point code = 38 bits
    // itu allows 12 bits for cic and 14 bits for point code = 26 bits
    protected ConcurrentHashMap<Long, Integer> cicMap = new ConcurrentHashMap<Long, Integer>();

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.CircuitManager#addCircuit(int, int)
     */
    @Override
    public void addCircuit(int cic, int dpc) {
        cicMap.put(getChannelID(cic, dpc), dpc);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.CircuitManager#removeCircuit(int)
     */
    @Override
    public void removeCircuit(int cic, int dpc) {
        cicMap.remove(getChannelID(cic, dpc));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.CircuitManager#isCircuitPresent(int)
     */
    @Override
    public boolean isCircuitPresent(int cic, int dpc) {
        return this.cicMap.containsKey(getChannelID(cic, dpc));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.CircuitManager#getChannelIDs()
     */
    @Override
    public long[] getChannelIDs() {
        long[] x = new long[this.cicMap.size()];
        Iterator<Long> it = this.cicMap.keySet().iterator();
        int index = 0;
        while (it.hasNext())
            x[index++] = it.next();

        return x;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.CircuitManager#getCIC()
     */
    @Override
    public int getCIC(long channelID) {
        // get last 14 bits
        return (int) (channelID & 0x3FFF);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.CircuitManager#getDPC()
     */
    @Override
    public int getDPC(long channelID) {
        return (int) (channelID >> 14);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.isup.CircuitManager#getChannelID(int,int)
     */
    @Override
    public long getChannelID(int cic, int dpc) {
        long currValue = dpc;
        currValue = (currValue << 14) + (long) cic;
        return currValue;
    }
}
