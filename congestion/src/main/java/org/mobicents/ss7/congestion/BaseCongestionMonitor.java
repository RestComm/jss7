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

package org.mobicents.ss7.congestion;

import javolution.util.FastList;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public abstract class BaseCongestionMonitor implements CongestionMonitor {

    private final FastList<CongestionListener> listeners = new FastList<CongestionListener>();

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.congestion.CongestionMonitor#addCongestionListener(
     * org.mobicents.ss7.congestion.CongestionListener)
     */
    @Override
    public void addCongestionListener(CongestionListener listener) {
        this.listeners.add(listener);

        CongestionTicket[] ticketsList = getCongestionTicketsList();
        if (ticketsList != null) {
            for (CongestionTicket ticket : ticketsList) {
                listener.onCongestionStart(ticket);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.congestion.CongestionMonitor#removeCongestionListener
     * (org.mobicents.ss7.congestion.CongestionListener)
     */
    @Override
    public void removeCongestionListener(CongestionListener listener) {
        this.listeners.remove(listener);
    }

    protected abstract CongestionTicketImpl generateTicket();

    protected void applyNewValue(int currentAlarmLevel, double newValue, double[] alarmThreshold, double[] clearThreshold) {
        int newAlarmLevel = currentAlarmLevel;
        for (int i1 = currentAlarmLevel - 1; i1 >= 0; i1--) {
            if (newValue <= clearThreshold[i1]) {
                newAlarmLevel = i1;
            }
        }
        for (int i1 = currentAlarmLevel; i1 < 3; i1++) {
            if (newValue >= alarmThreshold[i1]) {
                newAlarmLevel = i1 + 1;
            }
        }

        if (newAlarmLevel != currentAlarmLevel)
            this.setAlarmLevel(newAlarmLevel);

        if (newAlarmLevel < currentAlarmLevel) {
            // Lets notify the listeners
            CongestionTicketImpl ticket = generateTicket();
            for (FastList.Node<CongestionListener> n = listeners.head(), end = listeners.tail(); (n = n.getNext()) != end;) {
                CongestionListener listener = n.getValue();
                listener.onCongestionFinish(ticket);
            }
        }
        if (newAlarmLevel > currentAlarmLevel) {
            // Lets notify the listeners
            CongestionTicketImpl ticket = generateTicket();
            for (FastList.Node<CongestionListener> n = listeners.head(), end = listeners.tail(); (n = n.getNext()) != end;) {
                CongestionListener listener = n.getValue();
                listener.onCongestionStart(ticket);
            }
        }
    }

    protected abstract int getAlarmLevel();

    protected abstract void setAlarmLevel(int val);

    @Override
    public CongestionTicket[] getCongestionTicketsList() {
        if (getAlarmLevel() > 0)
            return new CongestionTicket[] { generateTicket() };
        else
            return null;
    }
}
