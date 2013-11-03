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

package org.mobicents.protocols.ss7.map.service.lsm;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityType;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VelocityEstimateImpl extends OctetStringBase implements VelocityEstimate {

    public VelocityEstimateImpl() {
        super(4, 7, "VelocityEstimate");
    }

    public VelocityEstimateImpl(byte[] data) {
        super(4, 7, "VelocityEstimate", data);
    }

    public VelocityEstimateImpl(VelocityType velocityType, int horizontalSpeed, int bearing, int verticalSpeed,
            int uncertaintyHorizontalSpeed, int uncertaintyVerticalSpeed) throws MAPException {
        super(4, 7, "VelocityEstimate");

        if (velocityType == null) {
            throw new MAPException("velocityType parameter is null");
        }
        switch (velocityType) {
            case HorizontalVelocity:
                this.initData(4, velocityType, horizontalSpeed, bearing, 0);
                break;

            case HorizontalWithVerticalVelocity:
                this.initData(5, velocityType, horizontalSpeed, bearing, verticalSpeed);
                if (verticalSpeed < 0)
                    verticalSpeed = -verticalSpeed;
                this.data[4] = (byte) (verticalSpeed & 0xFF);
                break;

            case HorizontalVelocityWithUncertainty:
                this.initData(5, velocityType, horizontalSpeed, bearing, 0);
                this.data[4] = (byte) (uncertaintyHorizontalSpeed & 0xFF);
                break;

            case HorizontalWithVerticalVelocityAndUncertainty:
                this.initData(7, velocityType, horizontalSpeed, bearing, verticalSpeed);
                this.data[4] = (byte) (verticalSpeed & 0xFF);
                this.data[5] = (byte) (uncertaintyHorizontalSpeed & 0xFF);
                this.data[6] = (byte) (uncertaintyVerticalSpeed & 0xFF);
                break;
        }
    }

    private void initData(int len, VelocityType velocityType, int horizontalSpeed, int bearing, int verticalSpeed) {
        this.data = new byte[len];

        this.data[0] = (byte) ((velocityType.getCode() << 4) | (verticalSpeed < 0 ? 0x02 : 0) | (bearing & 0x0100) >> 8);
        this.data[1] = (byte) (bearing & 0xFF);
        this.data[2] = (byte) ((horizontalSpeed & 0xFF00) >> 8);
        this.data[3] = (byte) (horizontalSpeed & 0xFF);
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public VelocityType getVelocityType() {
        if (this.data == null || this.data.length < 1)
            return null;

        return VelocityType.getInstance((this.data[0] & 0xF0) >> 4);
    }

    @Override
    public int getHorizontalSpeed() {
        if (this.data == null || this.data.length < 4)
            return 0;

        int res = ((data[2] & 0xFF) << 8) + (data[3] & 0xFF);
        return res;
    }

    @Override
    public int getBearing() {
        if (this.data == null || this.data.length < 4)
            return 0;

        int res = ((data[0] & 0x01) << 8) + (data[1] & 0xFF);
        return res;
    }

    @Override
    public int getVerticalSpeed() {
        VelocityType velocityType = this.getVelocityType();
        if (velocityType == null)
            return 0;

        switch (velocityType) {
            case HorizontalWithVerticalVelocity:
            case HorizontalWithVerticalVelocityAndUncertainty:
                int res = (data[4] & 0xFF);
                return res;
        }

        return 0;
    }

    @Override
    public int getUncertaintyHorizontalSpeed() {
        VelocityType velocityType = this.getVelocityType();
        if (velocityType == null)
            return 0;

        switch (velocityType) {
            case HorizontalVelocityWithUncertainty:
                int res = (data[4] & 0xFF);
                return res;
            case HorizontalWithVerticalVelocityAndUncertainty:
                res = (data[5] & 0xFF);
                return res;
        }

        return 0;
    }

    @Override
    public int getUncertaintyVerticalSpeed() {
        VelocityType velocityType = this.getVelocityType();
        if (velocityType == null)
            return 0;

        switch (velocityType) {
            case HorizontalWithVerticalVelocityAndUncertainty:
                int res = (data[6] & 0xFF);
                return res;
        }

        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("VelocityType=");
        sb.append(this.getVelocityType());

        sb.append(", HorizontalSpeed=");
        sb.append(this.getHorizontalSpeed());

        sb.append(", Bearing=");
        sb.append(this.getBearing());

        VelocityType velocityType = this.getVelocityType();
        if (velocityType == VelocityType.HorizontalWithVerticalVelocity
                || velocityType == VelocityType.HorizontalWithVerticalVelocityAndUncertainty) {
            sb.append(", VerticalSpeed=");
            sb.append(this.getVerticalSpeed());
        }

        if (velocityType == VelocityType.HorizontalVelocityWithUncertainty
                || velocityType == VelocityType.HorizontalWithVerticalVelocityAndUncertainty) {
            sb.append(", UncertaintyHorizontalSpeed=");
            sb.append(this.getUncertaintyHorizontalSpeed());
        }

        if (velocityType == VelocityType.HorizontalWithVerticalVelocityAndUncertainty) {
            sb.append(", UncertaintyVerticalSpeed=");
            sb.append(this.getUncertaintyVerticalSpeed());
        }

        sb.append("]");

        return sb.toString();
    }
}
