/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.restcomm.protocols.ss7.map.api.service.lsm;

import java.io.Serializable;

/**
 * DeferredLocationEventType ::= BIT STRING { msAvailable (0) , enteringIntoArea (1), leavingFromArea (2), beingInsideArea (3) }
 * (SIZE (1..16)) -- beingInsideArea is always treated as oneTimeEvent regardless of the possible value -- of occurrenceInfo
 * inside areaEventInfo. -- exception handling: -- a ProvideSubscriberLocation-Arg containing other values than listed above in
 * -- DeferredLocationEventType shall be rejected by the receiver with a return error cause of -- unexpected data value.
 *
 * @author amit bhayani
 *
 */
public interface DeferredLocationEventType extends Serializable {

    boolean getMsAvailable();

    boolean getEnteringIntoArea();

    boolean getLeavingFromArea();

    boolean getBeingInsideArea();

}
