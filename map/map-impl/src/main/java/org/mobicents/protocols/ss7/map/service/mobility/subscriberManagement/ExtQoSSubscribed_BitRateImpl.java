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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRate;

/**
*
* @author sergey vetyutnev
*
*/
public class ExtQoSSubscribed_BitRateImpl implements ExtQoSSubscribed_BitRate {

    private int data;

    public ExtQoSSubscribed_BitRateImpl(int data, boolean isSourceData) {
        if (isSourceData)
            this.data = data;
        else
            this.setData(data);
    }

    protected void setData(int val) {
        if (val <= 0) {
            this.data = 0;
        } else if (val > 0 && val < 64) {
            this.data = val;
        } else if (val >= 64 && val <= 576) {
            this.data = (val - 64) / 8 + 64;
        } else {
            this.data = (val - 576) / 64 + 128;
            if (this.data > 254)
                this.data = 0;
        }
    }

    @Override
    public int getSourceData() {
        return data;
    }

    @Override
    public int getBitRate() {
        if (this.data > 0 && this.data < 64) {
            return this.data;
        } else if (this.data >= 64 && this.data < 128) {
            return 64 + (this.data - 64) * 8;
        } else if (this.data >= 128 && this.data < 255) {
            return 576 + (this.data - 128) * 64;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BitRate(kbit/s)=");
        int v = this.getBitRate();
        if (data == 255)
            sb.append("reserved");
        else if (v == 0)
            sb.append("Subscribed maximum bit rate for uplink / reserved");
        else {
            sb.append(v);
        }
        return sb.toString();
    }

}
