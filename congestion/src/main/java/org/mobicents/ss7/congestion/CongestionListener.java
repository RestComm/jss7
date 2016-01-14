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

/**
 * The listener interface for receiving congestion state. The class that is interested in listening for congestion state should
 * implement this interface and register itself to {@link CongestionMonitor}
 *
 * @author amit bhayani
 *
 */
public interface CongestionListener {

    /**
     * As soon as congestion starts or its level increases in the underlying source, it calls this method to notify about it.
     * Notification is only one-time till the congestion abates in which case
     * {@link CongestionListener#onCongestionFinish(String)} is called
     *
     * @param ticket Ticket content
     */
    void onCongestionStart(CongestionTicket ticket);

    /**
     * As soon as congestion abates or its level decreases in the underlying source, it calls this method to notify about it.
     * Notification is only one-time till the congestion starts agaain in which case
     * {@link CongestionListener#onCongestionStart(String)} is called
     *
     * @param ticket Ticket content
     */
    void onCongestionFinish(CongestionTicket ticket);

}
