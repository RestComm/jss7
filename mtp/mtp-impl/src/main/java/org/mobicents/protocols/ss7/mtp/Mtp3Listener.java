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

package org.mobicents.protocols.ss7.mtp;

public interface Mtp3Listener {

    /**
     * Callback method from lower layers MTP3-. This is called once MTP3 determines that link is stable and is able to
     * send/receive messages properly. This method should be called only once. Every linkup event.
     */
    void linkUp();

    /**
     * Callback method from MTP3 layer, informs upper layers that link is not operable.
     */
    void linkDown();

    /**
     * Mtp3 invokes this method once proper MSU is detected. Argument contains full Mtp3 MSU.
     *
     * @param msgBuff
     */
    void receive(byte[] msgBuff); // http://pt.com/page/tutorials/ss7-tutorial/mtp

}
