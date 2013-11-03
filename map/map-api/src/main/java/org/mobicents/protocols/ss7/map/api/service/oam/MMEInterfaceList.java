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
 MME-InterfaceList ::= BIT STRING { s1-mme (0), s3 (1), s6a (2), s10 (3), s11 (4)} (SIZE (5..8)) -- Other bits than listed
 * above shall be discarded.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface MMEInterfaceList extends Serializable {

    boolean getS1Mme();

    boolean getS3();

    boolean getS6a();

    boolean getS10();

    boolean getS11();

}
