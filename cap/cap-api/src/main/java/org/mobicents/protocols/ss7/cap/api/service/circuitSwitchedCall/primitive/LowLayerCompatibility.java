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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

/**
 *
 LowLayerCompatibility {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE ( bound.&minLowLayerCompatibilityLength ..
 * bound.&maxLowLayerCompatibilityLength)) -- indicates the LowLayerCompatibility for the calling party. -- Refer to 3GPP TS
 * 24.008 [9] for encoding. -- It shall be coded as in the value part defined in 3GPP TS 24.008. -- i.e. the 3GPP TS 24.008 IEI
 * and 3GPP TS 24.008 octet length indicator -- shall not be included.
 *
 * minLowLayerCompatibilityLength ::= 1 maxLowLayerCompatibilityLength ::= 16
 *
 * }
 *
 * @author sergey vetyutnev
 *
 */
public interface LowLayerCompatibility extends Serializable {

    byte[] getData();

}