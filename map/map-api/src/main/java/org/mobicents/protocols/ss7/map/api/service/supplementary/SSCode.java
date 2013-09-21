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