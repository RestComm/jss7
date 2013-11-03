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

/**
 *
 0 0 0 0 Horizontal Velocity 0 0 0 1 Horizontal with Vertical Velocity 0 0 1 0 Horizontal Velocity with Uncertainty 0 0 1 1
 * Horizontal with Vertical Velocity and Uncertainty
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum VelocityType {
    HorizontalVelocity(0), HorizontalWithVerticalVelocity(1), HorizontalVelocityWithUncertainty(2), HorizontalWithVerticalVelocityAndUncertainty(
            3);

    private final int type;

    private VelocityType(int type) {
        this.type = type;
    }

    public int getCode() {
        return this.type;
    }

    public static VelocityType getInstance(int type) {
        switch (type) {
            case 0:
                return HorizontalVelocity;
            case 1:
                return HorizontalWithVerticalVelocity;
            case 2:
                return HorizontalVelocityWithUncertainty;
            case 3:
                return HorizontalWithVerticalVelocityAndUncertainty;

            default:
                return null;
        }
    }
}
