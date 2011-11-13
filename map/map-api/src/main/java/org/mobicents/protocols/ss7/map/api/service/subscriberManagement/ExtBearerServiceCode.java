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

package org.mobicents.protocols.ss7.map.api.service.subscriberManagement;

/**
*
Ext-BearerServiceCode ::= OCTET STRING (SIZE (1..5))
	-- This type is used to represent the code identifying a single
	-- bearer service, a group of bearer services, or all bearer
	-- services. The services are defined in TS 3GPP TS 22.002 [3].
	-- The internal structure is defined as follows:
	--
	-- OCTET 1:
	-- plmn-specific bearer services:
	-- bits 87654321: defined by the HPLMN operator
	--
	-- rest of bearer services:
	-- bit 8: 0 (unused)
	-- bits 7654321: group (bits 7654), and rate, if applicable
	-- (bits 321)

	-- OCTETS 2-5: reserved for future use. If received the
    -- Ext-TeleserviceCode shall be
	-- treated according to the exception handling defined for the
	-- operation that uses this type. 


	-- Ext-BearerServiceCode includes all values defined for BearerServiceCode.

* 
* @author sergey vetyutnev
* 
*/
public interface ExtBearerServiceCode {

	public byte[] getData();

}
