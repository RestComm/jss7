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

package org.mobicents.protocols.ss7.map.api.service.lsm;

import java.io.Serializable;

/**
 * LocationType ::= SEQUENCE { locationEstimateType [0] LocationEstimateType, ..., deferredLocationEventType [1]
 * DeferredLocationEventType OPTIONAL }
 *
 * @author amit bhayani
 *
 */
public interface LocationType extends Serializable {
    LocationEstimateType getLocationEstimateType();

    /**
     * DeferredLocationEventType ::= BIT STRING { msAvailable (0) , enteringIntoArea (1), leavingFromArea (2), beingInsideArea
     * (3) } (SIZE (1..16)) -- beingInsideArea is always treated as oneTimeEvent regardless of the possible value -- of
     * occurrenceInfo inside areaEventInfo. -- exception handling: -- a ProvideSubscriberLocation-Arg containing other values
     * than listed above in -- DeferredLocationEventType shall be rejected by the receiver with a return error cause of --
     * unexpected data value.
     */
    DeferredLocationEventType getDeferredLocationEventType();
}
