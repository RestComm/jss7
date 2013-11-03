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

package org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;
import java.util.ArrayList;

/**
*
RouteList {PARAMETERS-BOUND : bound} ::= SEQUENCE SIZE(1..3) OF OCTET STRING (
SIZE (bound.&minRouteListLength..bound.&maxRouteListLength))
-- Indicates a list of trunk groups or a route index. See Q.1224 for additional information on this item.

*
* @author sergey vetyutnev
*
*/
public interface RouteList extends Serializable {

    ArrayList<byte[]> getDataList();

}
