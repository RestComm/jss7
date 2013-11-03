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

package org.mobicents.protocols.ss7.map.api.primitives;

import java.io.Serializable;

/**
 * SignalInfo ::= OCTET STRING (SIZE (1..maxSignalInfoLength)) maxSignalInfoLength INTEGER ::= 200 -- This NamedValue represents
 * the theoretical maximum number of octets which is -- available to carry a single instance of the SignalInfo data type, --
 * without requiring segmentation to cope with the network layer service. -- However, the actual maximum size available for an
 * instance of the data -- type may be lower, especially when other information elements -- have to be included in the same
 * component
 *
 * @author cristian veliscu
 *
 */
public interface SignalInfo extends Serializable {
    byte[] getData();
}