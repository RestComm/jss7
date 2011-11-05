/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.inap.api.primitives;

/**
*
LegID::= CHOICE {
sendingSideID[0] LegType,  -- used in operations sent from SCF to SSF
receivingSideID[1] LegType   -- used in operations sent from SSF to SCF
}
--  Indicates a reference to a specific party in a call. OPTIONAL denotes network operator specific use with
--  unilateral ID assignment. OPTIONAL for LegID also denotes the following:
--   - when only one party exists in the call, this parameter is not needed (as no ambiguity exists).
--   - when more than one party exists in the call, one of the following alternatives applies:
--     1) LegID is present and indicates which party is concerned.
--     2) LegID is not present and a default value is assumed (e.g., calling party in the case of the
--        ApplyCharging operation).
LegType::= OCTET STRING (SIZE(1))
leg1 LegType::= '01'H
leg2 LegType::= '02'H

Other simple primitives:
Duration::= INTEGER (-2..86400)
--  Values are seconds. Negative values denote a special value, refer to the procedure description of the
--  relevant operations for further information.

Integer4::= INTEGER (0..2147483647)

Interval::= INTEGER (-1..60000)
--  Units are milliseconds. A -1 value denotes infinite.

ServiceKey::= Integer4
--  Information that allows the SCF to choose the appropriate service logic.

* 
* @author sergey vetyutnev
* 
*/
public interface LegID {

	public LegType getSendingSideID();

	public LegType getReceivingSideID();

}
