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
 * The Congestion Ticket interface contains info for the source of congestion and the congestion level
 *
 * @author sergey vetyutnev
 *
 */
public interface CongestionTicket {

    /**
     * @return the name of the source of the congestion
     */
    String getSource();

    /**
     * @return the level of the congestion. 0 means no congestion, 3 means max congestion level. Level 1 is warning level
     *         without of message processing affect, Level 2 will prevent from new dialog creating, Level 3 will prevent from
     *         any message processing.
     */
    int getLevel();

    /**
     * @return the attribute for the ticket (like networkID, dpc values, etc)
     */
    Object getAttribute(String key);

}
