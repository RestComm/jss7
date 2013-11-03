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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

import java.io.Serializable;

/**
 *
 SS-Code ::= OCTET STRING (SIZE (1)) -- This type is used to represent the code identifying a single -- supplementary service,
 * a group of supplementary services, or -- all supplementary services. The services and abbreviations -- used are defined in TS
 * 3GPP TS 22.004 [5]. The internal structure is -- defined as follows: -- -- bits 87654321: group (bits 8765), and specific
 * service -- (bits 4321)
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SSCode extends Serializable {

    int getData();

    SupplementaryCodeValue getSupplementaryCodeValue();

}