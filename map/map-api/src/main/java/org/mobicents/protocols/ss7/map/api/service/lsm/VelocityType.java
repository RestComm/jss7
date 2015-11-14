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
