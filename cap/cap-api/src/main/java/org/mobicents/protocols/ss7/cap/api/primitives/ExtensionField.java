/*
 * TeleStax, Open Source Cloud Communications  
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.cap.api.primitives;

/**
*
ExtensionField ::= SEQUENCE {
	type EXTENSION.&id ({SupportedExtensions}),
	-- shall identify the value of an EXTENSION type
	criticality CriticalityType DEFAULT ignore,
	value [1] EXTENSION.&ExtensionType ({SupportedExtensions}{@type}),
	...
}
-- This parameter indicates an extension of an argument data type.
-- Its content is network operator specific
 
CriticalityType  ::= ENUMERATED { 
        ignore    (0), 
        abort     (1) 
        } 
Code ::= CHOICE {local   INTEGER, 
                 global  OBJECT IDENTIFIER} 
* 
* @author sergey vetyutnev
* 
*/
public interface ExtensionField {

	public Integer getLocalCode();

	public long[] getGlobalCode();

	public CriticalityType getCriticalityType();

	/**
	 * 
	 * @return Encoded field parameter without tag and length fields
	 */
	public byte[] getData();


	public void setLocalCode(Integer localCode);

	public void setGlobalCode(long[] globalCode);

	public void setCriticalityType(CriticalityType criticalityType);

	/**
	 * @param data
	 *            Encoded field parameter without tag and length fields
	 */
	public void setData(byte[] data);
}

