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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
*
Ext-TeleserviceCode ::= OCTET STRING (SIZE (1..5))
	-- This type is used to represent the code identifying a single
	-- teleservice, a group of teleservices, or all teleservices. The
	-- services are defined in TS GSM 22.003 [4].
	-- The internal structure is defined as follows:

	-- OCTET 1:
	-- bits 87654321: group (bits 8765) and specific service
	-- (bits 4321)

	-- OCTETS 2-5: reserved for future use. If received the
    -- Ext-TeleserviceCode shall be
 	-- treated according to the exception handling defined for the
	-- operation that uses this type.

	-- Ext-TeleserviceCode includes all values defined for TeleserviceCode.

* 
* @author sergey vetyutnev
* 
*/
public interface ExtTeleserviceCode {

	public byte[] getData();

}
