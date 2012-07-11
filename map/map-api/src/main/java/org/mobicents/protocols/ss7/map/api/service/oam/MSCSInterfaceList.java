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

MSC-S-InterfaceList ::= BIT STRING {
	a (0),
	iu (1),
	mc (2),
	map-g (3),
	map-b (4),
	map-e (5),
	map-f (6),
	cap (7),
	map-d (8),
	map-c (9)} (SIZE (10..16))
-- Other bits than listed above shall be discarded.

 * 
 * @author sergey vetyutnev
 * 
 */
public interface MSCSInterfaceList {

	public boolean getA();

	public boolean getIu();

	public boolean getMc();

	public boolean getMapG();

	public boolean getMapB();

	public boolean getMapE();

	public boolean getMapF();

	public boolean getCap();

	public boolean getMapD();

	public boolean getMapC();

}
