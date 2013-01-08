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
LSAIdentity ::= OCTET STRING (SIZE (3))
	-- Octets are coded according to TS 3GPP TS 23.003 [17]

-- Cells may be grouped into specific localised service areas. Each localised service area is identified by a localised
-- service area identity (LSA ID). No restrictions are placed on what cells may be grouped into a given localised service
-- area.
-- The LSA ID can either be a PLMN significant number or a universal identity. This shall be known both in the networks
-- and in the SIM.
-- The LSA ID consists of 24 bits, numbered from 0 to 23, with bit 0 being the LSB. Bit 0 indicates whether the LSA is a
-- PLMN significant number or a universal LSA. If the bit is set to 0 the LSA is a PLMN significant number; if it is set to
-- 1 it is a universal LSA.

--MSB                                          LSB
   ________________________________________________
--|    23 Bits                            | 1 bit  |
--|_______________________________________|________|
  <----------------- LSA ID ----------------------->

* 
* @author sergey vetyutnev
* 
*/
public interface LSAIdentity {

	public byte[] getData();
	
	public boolean isPlmnSignificantLSA();

}
