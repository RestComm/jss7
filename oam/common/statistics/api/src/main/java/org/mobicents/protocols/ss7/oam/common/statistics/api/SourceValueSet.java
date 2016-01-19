/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.oam.common.statistics.api;

import java.io.Serializable;
import java.util.UUID;


import javolution.util.FastMap;

/**
*
* This value is supplied by CounterMediator and consumed by CounterProvider.
* This is a set of counters results for the whole counterSet (all counters and objects)
*
* @author sergey vetyutnev
*
*/
public interface SourceValueSet extends Serializable {

    /**
     * This value means Id of session of the source why counters were collected.
     * Before counters calculating CounterProvider must compare SessionId of current
     * and previous values. If they are not equal, CounterProvider will not process
     * data.
     *
     * @return
     */
    UUID getSessionId();

    FastMap<String, SourceValueCounter> getCounters();

}
