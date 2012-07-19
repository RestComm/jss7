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

package org.mobicents.protocols.ss7.map.api.service.oam;

/**
 * 

PGW-InterfaceList ::= BIT STRING {
	s2a (0),
	s2b (1),
	s2c (2),
	s5 (3),
	s6b (4),
	gx (5),
	s8b (6),
	sgi (7)} (SIZE (8..16))
-- Other bits than listed above shall be discarded.

 * 
 * @author sergey vetyutnev
 * 
 */
public interface PGWInterfaceList {

	public boolean getS2a();

	public boolean getS2b();

	public boolean getS2c();

	public boolean getS5();

	public boolean getS6b();

	public boolean getGx();

	public boolean getS8b();

	public boolean getSgi();

}
