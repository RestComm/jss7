/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.map.api.service.oam;

import java.io.Serializable;

/**
 *
 PGW-InterfaceList ::= BIT STRING { s2a (0), s2b (1), s2c (2), s5 (3), s6b (4), gx (5), s8b (6), sgi (7)} (SIZE (8..16)) --
 * Other bits than listed above shall be discarded.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface PGWInterfaceList extends Serializable {

    boolean getS2a();

    boolean getS2b();

    boolean getS2c();

    boolean getS5();

    boolean getS6b();

    boolean getGx();

    boolean getS8b();

    boolean getSgi();

}
