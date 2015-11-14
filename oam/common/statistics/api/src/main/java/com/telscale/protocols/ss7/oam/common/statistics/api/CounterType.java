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

/**
*
* Counter values type
*
* @author sergey vetyutnev
*
*/
public enum CounterType {
    /**
     * A simple constantly increasing long value : CounterValue.getLongValue() =
     * SourceValueObject.getValue() - SourceValueObject_prev.getValue()
     */
    Summary,
    /**
     * A simple constantly increasing long value - cumulative counter:
     * CounterValue.getLongValue() = SourceValueObject.getValue()
     */
    Summary_Cumulative,
    /**
     * A simple constantly increasing double value : CounterValue.getDoubleValue() =
     * SourceValueObject.getValueA() - SourceValueObject_prev.getValueA()
     */
    SummaryDouble,
    /**
     * A minimal long value for time duration : CounterValue.getLongValue() =
     * SourceValueObject.getValue()
     */
    Minimal,
    /**
     * A maximal long value for time duration : CounterValue.getLongValue() =
     * SourceValueObject.getValue()
     */
    Maximal,
    /**
     * An average double value for time duration : CounterValue.getDoubleValue()
     * = (SourceValueObject.getValueA() - SourceValueObject_prev.getValueA()) /
     * (SourceValueObject.getValueB() - SourceValueObject_prev.getValueB())
     */
    Average,
    /**
     * An array of pairs String-Long values: CounterValue.getComplexValue() =
     * SourceValueObject.getComplexValue()
     */
    ComplexValue,
}
