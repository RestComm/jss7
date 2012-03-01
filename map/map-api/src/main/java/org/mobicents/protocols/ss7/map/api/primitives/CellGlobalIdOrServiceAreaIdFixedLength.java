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

package org.mobicents.protocols.ss7.map.api.primitives;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.MAPException;

/**
 *
CellGlobalIdOrServiceAreaIdFixedLength ::= OCTET STRING (SIZE (7))
	-- Refers to Cell Global Identification or Service Are Identification
	-- defined in 3GPP TS 23.003.
	-- The internal structure is defined as follows:
	-- octet 1 bits 4321	Mobile Country Code 1st digit
	--         bits 8765	Mobile Country Code 2nd digit
	-- octet 2 bits 4321	Mobile Country Code 3rd digit
	--         bits 8765	Mobile Network Code 3rd digit
	--			or filler (1111) for 2 digit MNCs
	-- octet 3 bits 4321	Mobile Network Code 1st digit
	--         bits 8765	Mobile Network Code 2nd digit
	-- octets 4 and 5	Location Area Code according to 3GPP TS 24.008
	-- octets 6 and 7	Cell Identity (CI) value or 
	-- 			Service Area Code (SAC) value 
	--			according to 3GPP TS 23.003

 * 
 * @author sergey vetyutnev
 * 
 */
public interface CellGlobalIdOrServiceAreaIdFixedLength extends Serializable {

	public byte[] getData();

	public int getMCC() throws MAPException;

	public int getMNC() throws MAPException;

	public int getLac() throws MAPException;

	/**
	 * Cell Identity (CI) value or Service Area Code (SAC) value
	 * 
	 * @return
	 */
	public int getCellId() throws MAPException;

}
