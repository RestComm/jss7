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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

/**
 * CUG-Interlock ::= OCTET STRING (SIZE (4))
 *
 * CUG interlock code: this is a means of identifying closed user group membership within the network. At the calling side, if a
 * closed user group match exists, the CUG index identifying a closed user group maps to the closed user group interlock code
 * for that closed user group. If a closed user group match exists at the called side the closed user group interlock code
 * identifying a closed user group maps to the CUG index representing that closed user group. Closed user group interlock code
 * is not an access concept, but is used for clarity during the descriptions of signalling procedures and flows.
 *
 *
 * @author cristian veliscu
 *
 */
public interface CUGInterlock extends Serializable {
    byte[] getData();
}