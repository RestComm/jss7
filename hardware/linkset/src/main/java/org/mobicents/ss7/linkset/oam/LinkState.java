/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
