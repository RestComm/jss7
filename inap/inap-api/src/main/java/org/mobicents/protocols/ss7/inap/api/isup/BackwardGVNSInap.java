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

package org.mobicents.protocols.ss7.inap.api.isup;

import java.io.Serializable;

import org.mobicents.protocols.ss7.isup.message.parameter.BackwardGVNS;

/**
*

BackwardGVNS {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE(
bound.&minBackwardGVNSLength..bound.&maxBackwardGVNSLength))
-- Indicats the GVNS Backward information. Refer to Q.735, �6 for encoding.

*
* @author sergey vetyutnev
*
*/
public interface BackwardGVNSInap extends Serializable {

    byte[] getData();

    BackwardGVNS getBackwardGVNS();

    // TODO: Spec refers to "Q.735, �6", we refer to ISUP, what is correct ?

}
