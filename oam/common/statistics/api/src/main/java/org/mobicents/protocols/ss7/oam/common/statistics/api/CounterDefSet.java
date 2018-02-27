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

/**
 *
 * Description of a set of counters. CounterMediator can supply one ore more CounterDefSet.
 *
 * @author sergey vetyutnev
 *
 */
public interface CounterDefSet extends Serializable {

    /**
     * The name of CounterDefSet
     * 
     * @return
     */
    String getName();

    /**
     * allows to add counters on a runtime
     * 
     * @param val - counter to add
     */
    void addCounterDef(CounterDef val);

    /**
     * allows to delete counters on a runtime
     * 
     * @param val - counter to add
     */
    void delCounterDef(String counterName);

    /**
     * Returns a list of CounterDef which are contained in CounterDefSet
     * 
     * @return
     */
    CounterDef[] getCounterDefs();

}