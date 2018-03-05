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

package org.restcomm.protocols.ss7.map.api.smstpdu;

/**
 * This facility allows short messages to be routed to one of multiple
 * applications, using a method similar to TCP/UDP ports in a TCP/IP network. An
 * application entity is uniquely identified by the pair of TP-DA/TP-OA and the
 * port address. The port addressing is transparent to the transport, and also
 * useful in Status Reports.
 *
 * @author sergey vetyutnev
 *
 */
public interface ApplicationPortAddressing16BitAddress extends UserDataHeaderElement {

    int getDestinationPort();

    int getOriginatorPort();

}
