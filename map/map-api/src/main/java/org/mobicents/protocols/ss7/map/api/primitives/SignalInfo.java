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

package org.mobicents.protocols.ss7.map.api.primitives;

/**
 * SignalInfo ::= OCTET STRING (SIZE (1..maxSignalInfoLength))
 * maxSignalInfoLength INTEGER ::= 200
 * -- This NamedValue represents the theoretical maximum number of octets which is
 * -- available to carry a single instance of the SignalInfo data type,
 * -- without requiring segmentation to cope with the network layer service.
 * -- However, the actual maximum size available for an instance of the data
 * -- type may be lower, especially when other information elements
 * -- have to be included in the same component
 * 
 * @author cristian veliscu
 * 
 */
public interface SignalInfo {
	public byte[] getData();
}