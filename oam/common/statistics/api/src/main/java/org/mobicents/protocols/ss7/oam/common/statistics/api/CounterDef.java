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
* A definition of one counter for collecting
*
* @author sergey vetyutnev
*
*/
public interface CounterDef extends Serializable {

    /**
     * CounterName is a short abbreviation, some sort of counter Id. Avoid to keep spaces there
     * CounterName (and CounterType) must not be changed till the meaning of a counter has not changed
     * CounterName must be changed when the meaning of a counter has changed
     * @return
     */
    String getCounterName();

    /**
     * GroupName is a name of group which counter belongs to
     * @return group name
     */
    String getGroupName();

    /**
     * ObjectName is a name of an object that counter is elated to
     * @return group name
     */
    String getObjectName();

    /**
     * Description may be a long verbal description and may be updated
     * @return
     */
    String getDescription();

    /**
     * A type of a counter (for example summory, minimal or maximal value or may be complex value)
     * @return
     */
    CounterType getCounterType();

}
