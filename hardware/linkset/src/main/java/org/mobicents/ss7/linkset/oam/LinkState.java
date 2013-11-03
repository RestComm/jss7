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

package org.mobicents.ss7.linkset.oam;

/**
 * Defines various states for Link
 *
 * @author amit bhayani
 *
 */
public interface LinkState {

    /**
     * Indicates the link is not available to carry traffic. This can occur if the link is remotely or locally inhibited by a
     * user. It can also be unavailable if MTP2 has not been able to successfully activate the link connection or the link test
     * messages sent by MTP3 are not being acknowledged.
     */
    int UNAVAILABLE = 1;

    /**
     * Indicates the link has been shutdown in the configuration. A link is shutdown when it is shutdown at the MTP3 layer.
     */
    int SHUTDOWN = 2;

    /**
     * Indicates the link is active and able to transport traffic.
     */
    int AVAILABLE = 3;

    /**
     * A link is FAILED when the link is not shutdown but is unavailable at layer2 for some reason. For example Initial
     * Alignment failed?
     */
    int FAILED = 4;

}
