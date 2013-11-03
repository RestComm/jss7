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

package org.mobicents.protocols.ss7.cap.api.service.gprs.primitive;

import java.io.Serializable;

/**
 *
 GPRSCause {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE (1 .. 1)) -- Shall only include the cause value. -- 00000000
 * Unspecified -- All other values shall be interpreted as 'Unspecified'. -- -- This parameter indicates the cause for CAP
 * interface related information. -- The GPRSCause mapping to/from GTP cause values specified in the 3GPP TS 29.060 [12] and --
 * to/from 3GPP TS 24.008 [9] GMM cause and SM cause values are outside scope of this document.
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface GPRSCause extends Serializable {

    int getData();

}
