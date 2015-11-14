/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.inap.api.isup;

import java.io.Serializable;

import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;

/**
*
<code>
Cause {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE (minCauseLength..
bound.&maxCauseLength))
-- Indicates the cause for interface related information.
-- Refer to the ITU-T Recommendation Q.763 Cause parameter for encoding.
-- For the use of cause and location values refer to ITU-T Recommendation Q.850
</code>
*
*
* @author sergey vetyutnev
*
*/
public interface CauseInap extends Serializable {

    byte[] getData();

    CauseIndicators getCauseIndicators() throws INAPException;

}
