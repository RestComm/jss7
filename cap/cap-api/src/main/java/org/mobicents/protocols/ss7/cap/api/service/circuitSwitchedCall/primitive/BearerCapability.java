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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

import org.mobicents.protocols.ss7.cap.api.isup.BearerCap;

/**
 *
BearerCapability {PARAMETERS-BOUND : bound} ::= CHOICE {
     bearerCap [0] OCTET STRING (SIZE(2..bound.&maxBearerCapabilityLength))
}
-- Indicates the type of bearer capability connection to the user. For bearerCap, the ISUP User
-- Service Information, ETSI EN 300 356-1 [23] -- encoding shall be used.
 *
 * MAXIMUM-FOR-BEARER-CAPABILITY ::= 11
 *
 * @author sergey vetyutnev
 *
 */
public interface BearerCapability extends Serializable {

    BearerCap getBearerCap();

}