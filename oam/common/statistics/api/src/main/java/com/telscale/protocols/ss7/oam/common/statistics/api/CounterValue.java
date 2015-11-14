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

package com.telscale.protocols.ss7.oam.common.statistics.api;

import java.io.Serializable;


/**
*
* Value for one counter of one object
*
* @author sergey vetyutnev
*
*/
public interface CounterValue extends Serializable {

    /**
     * Definition of a counter
     * @return
     */
    CounterDef getCounterDef();

    /**
     * ObjectName is very important - if there are more then one object that
     * supplies statistic we must provide unique ObjectName for each object. Not
     * good example: several TCAP stacks
     *
     * @return
     */
    String getObjectName();

    /**
     * @return Long value
     */
    long getLongValue();

    /**
     * @return Double value
     */
    double getDoubleValue();

    /**
     * A set of complex values (a set of pairs "String" - "Long", for example a count of different ACN's)
     * @return
     */
    ComplexValue[] getComplexValue();

}
