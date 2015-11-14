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

package org.mobicents.protocols.ss7.cap.api.EsiBcsm;

/**
 *
<code>
-- As specified in ITU-T Recommendation Q.763 as follows:
-- no indication 'xxxx xx00'B
-- no charge 'xxxx xx01'B
-- charge 'xxxx xx10'B
-- spare 'xxxx xx11'B
-- Sending entity shall fill the upper six bits with '0's.
-- Receiving entity shall ignore the upper six bits.
<code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum ChargeIndicatorValue {
    noIndication(0), noCharge(1), charge(2), spare(3);

    private int code;

    private ChargeIndicatorValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static ChargeIndicatorValue getInstance(int code) {
        switch (code) {
            case 0:
                return ChargeIndicatorValue.noIndication;
            case 1:
                return ChargeIndicatorValue.noCharge;
            case 2:
                return ChargeIndicatorValue.charge;
            case 3:
                return ChargeIndicatorValue.spare;
            default:
                return null;
        }
    }
}
