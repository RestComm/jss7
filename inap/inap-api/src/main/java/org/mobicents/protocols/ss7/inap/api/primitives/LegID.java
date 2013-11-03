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

package org.mobicents.protocols.ss7.inap.api.primitives;

import java.io.Serializable;

/**
 *
 LegID::= CHOICE { sendingSideID[0] LegType, -- used in operations sent from SCF to SSF receivingSideID[1] LegType -- used in
 * operations sent from SSF to SCF } -- Indicates a reference to a specific party in a call. OPTIONAL denotes network operator
 * specific use with -- unilateral ID assignment. OPTIONAL for LegID also denotes the following: -- - when only one party exists
 * in the call, this parameter is not needed (as no ambiguity exists). -- - when more than one party exists in the call, one of
 * the following alternatives applies: -- 1) LegID is present and indicates which party is concerned. -- 2) LegID is not present
 * and a default value is assumed (e.g., calling party in the case of the -- ApplyCharging operation). LegType::= OCTET STRING
 * (SIZE(1)) leg1 LegType::= '01'H leg2 LegType::= '02'H
 *
 * Other simple primitives: Duration::= INTEGER (-2..86400) -- Values are seconds. Negative values denote a special value, refer
 * to the procedure description of the -- relevant operations for further information.
 *
 * Integer4::= INTEGER (0..2147483647)
 *
 * Interval::= INTEGER (-1..60000) -- Units are milliseconds. A -1 value denotes infinite.
 *
 * ServiceKey::= Integer4 -- Information that allows the SCF to choose the appropriate service logic.
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface LegID extends Serializable {

    LegType getSendingSideID();

    LegType getReceivingSideID();

}