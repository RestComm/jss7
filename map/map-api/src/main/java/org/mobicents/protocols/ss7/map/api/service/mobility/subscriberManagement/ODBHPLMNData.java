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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
 * 

ODB-HPLMN-Data ::= BIT STRING {
	plmn-SpecificBarringType1  (0),
	plmn-SpecificBarringType2  (1),
	plmn-SpecificBarringType3  (2),
	plmn-SpecificBarringType4  (3)} (SIZE (4..32))
	-- exception handling: reception of unknown bit assignments in the
	-- ODB-HPLMN-Data type shall be treated like unsupported ODB-HPLMN-Data 
	-- When the ODB-HPLMN-Data type is removed from the HLR for a given subscriber, 
	-- in NoteSubscriberDataModified operation sent toward the gsmSCF
	-- all bits shall be set to O.

 * 
 * 
 * @author sergey vetyutnev
 * 
 */
public interface ODBHPLMNData {

	public boolean getPlmnSpecificBarringType1();

	public boolean getPlmnSpecificBarringType2();

	public boolean getPlmnSpecificBarringType3();

	public boolean getPlmnSpecificBarringType4();

}
