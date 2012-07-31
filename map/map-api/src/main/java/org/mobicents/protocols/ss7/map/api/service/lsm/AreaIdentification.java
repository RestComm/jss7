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

package org.mobicents.protocols.ss7.map.api.service.lsm;

import org.mobicents.protocols.ss7.map.api.MAPException;

/**
 * 

AreaIdentification ::= OCTET STRING (SIZE (2..7))
	-- The internal structure is defined as follows:
	-- octet 1 bits 4321	Mobile Country Code 1st digit
	--         bits 8765	Mobile Country Code 2nd digit
	-- octet 2 bits 4321	Mobile Country Code 3rd digit
	--         bits 8765	Mobile Network Code 3rd digit if 3 digit MNC included
	--			or filler (1111)
	-- octet 3 bits 4321	Mobile Network Code 1st digit
	--         bits 8765	Mobile Network Code 2nd digit
	-- octets 4 and 5	Location Area Code (LAC) for Local Area Id,
	--			Routing Area Id and Cell Global Id
	-- octet 6	Routing Area Code (RAC) for Routing Area Id
	-- octets 6 and 7	Cell Identity (CI) for Cell Global Id
	-- octets 4 until 7	Utran Cell Identity (UC-Id) for Utran Cell Id

 * 
 * @author sergey vetyutnev
 * 
 */
public interface AreaIdentification {

	public byte[] getData();

	public int getMCC() throws MAPException;

	public int getMNC() throws MAPException;

	public int getLac() throws MAPException;

	public int getRac() throws MAPException;

	public int getCellId() throws MAPException;

	public int getUtranCellId() throws MAPException;

}
