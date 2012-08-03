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

SGSN-InterfaceList ::= BIT STRING {
	gb (0),
	iu (1),
	gn (2),
	map-gr (3),
	map-gd (4),
	map-gf (5),
	gs (6),
	ge (7),
	s3 (8),
	s4 (9),
	s6d (10)} (SIZE (8..16))
-- Other bits than listed above shall be discarded.

 * 
 * @author sergey vetyutnev
 * 
 */
public interface SGSNInterfaceList {

	public boolean getGb();

	public boolean getIu();

	public boolean getGn();

	public boolean getMapGr();

	public boolean getMapGd();

	public boolean getMapGf();

	public boolean getGs();

	public boolean getGe();

	public boolean getS3();

	public boolean getS4();

	public boolean getS6d();

}
