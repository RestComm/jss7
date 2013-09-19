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

package org.mobicents.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

/**
*
Carrier {PARAMETERS-BOUND : bound} ::= OCTET STRING(SIZE(
bound.&minCarrierLength ..bound.&maxCarrierLength))
-- This parameter contains the carrier selection followed by Transit Network Selection.
-- The Carrier selection is one octet and is encoded as:
-- 00000000 No indication
-- 00000001 Selected carrier code pre-subscribed and not input by calling party
-- 00000010 Selected carrier identification code pre-subscribed and input by calling party
-- 00000011 Selected carrier identification code pre-subscribed,
-- no indication of whether input by calling party.
-- 00000100 Selected carrier identification code not pre-subscribed and input by calling party
-- 00000101 - to 11111110 - spare
-- 11111111 reserved
-- Refer to ITU-T Recommendation Q.763 for encoding of Transit Network Selection.

*
* @author sergey vetyutnev
*
*/
public interface Carrier extends Serializable {

    byte[] getData();

    // TODO: internal structure is not covered

}
