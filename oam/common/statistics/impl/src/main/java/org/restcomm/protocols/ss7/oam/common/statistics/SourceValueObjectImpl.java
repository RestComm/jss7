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

package org.restcomm.protocols.ss7.oam.common.statistics;

import java.util.Arrays;

import org.restcomm.protocols.ss7.oam.common.statistics.api.ComplexValue;
import org.restcomm.protocols.ss7.oam.common.statistics.api.SourceValueObject;

/**
*
* @author sergey vetyutnev
*
*/
public class SourceValueObjectImpl implements SourceValueObject {

    private String objectName;
    private long value;
    private double valueA;
    private double valueB;
    private ComplexValue[] complexValue;

    public SourceValueObjectImpl(String objectName, long value) {
        this.objectName = objectName;
        this.value = value;
    }

    public void setValueA(double val) {
        valueA = val;
    }

    public void setValueB(double val) {
        valueB = val;
    }

    public void setComplexValue(ComplexValue[] val) {
        complexValue = val;
    }

    @Override
    public String getObjectName() {
        return objectName;
    }

    @Override
    public long getValue() {
        return value;
    }

    @Override
    public double getValueA() {
        return valueA;
    }

    @Override
    public double getValueB() {
        return valueB;
    }

    @Override
    public ComplexValue[] getComplexValue() {
        return complexValue;
    }

    @Override
    public String toString() {
        return "SourceValueObjectImpl [objectName=" + objectName + ", value=" + value + ", valueA=" + valueA + ", valueB="
                + valueB + ", complexValue=" + Arrays.toString(complexValue) + "]";
    }

}
