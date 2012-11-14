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

package org.mobicents.protocols.ss7.map.service.lsm;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityEstimate;
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
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

	public VelocityEstimateImpl(VelocityType velocityType, int horizontalSpeed, int bearing, int verticalSpeed, int uncertaintyHorizontalSpeed,
			int uncertaintyVerticalSpeed) throws MAPException {
		super(4, 7, "VelocityEstimate");

		if (velocityType == null) {
			throw new MAPException("velocityType parameter is null");
		}
		switch(velocityType){
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

		this.data[0] = (byte) ((velocityType.getCode() << 4) + (verticalSpeed < 0 ? 0x02 : 0) + (bearing & 0x0100) >> 8);
		this.data[1] = (byte) (bearing & 0xFF);
		this.data[2] = (byte) ((horizontalSpeed & 0xFF00) >> 8);
		this.data[3] = (byte) (horizontalSpeed & 0xFF);
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public VelocityType getVelocityType() {
		if (this.data == null || this.data.length > 0)
			return null;

		return VelocityType.getInstance((this.data[0] & 0xF0) >> 4);
	}

	@Override
	public int getHorizontalSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBearing() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVerticalSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUncertaintyHorizontalSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUncertaintyVerticalSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

}
