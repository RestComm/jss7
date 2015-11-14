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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

/**
*
<code>
Source Statistics Descriptor, octet 14 (see 3GPP TS 23.107 [81])
Bits
4 3 2 1
In MS to network direction
0 0 0 0     unknown
0 0 0 1     speech
The network shall consider all other values as unknown.
In network to MS direction
Bits 4 to 1 of octet 14 are spare and shall be coded all 0.
The Source Statistics Descriptor value is ignored if the Traffic Class is Interactive class or Background class.
</code>
*
* @author sergey vetyutnev
*
*/
public enum Ext2QoSSubscribed_SourceStatisticsDescriptor {
    unknown(0), speech(1);

    private int code;

    private Ext2QoSSubscribed_SourceStatisticsDescriptor(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static Ext2QoSSubscribed_SourceStatisticsDescriptor getInstance(int code) {
        switch (code) {
        case 0:
            return Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown;
        case 1:
            return Ext2QoSSubscribed_SourceStatisticsDescriptor.speech;
        default:
            return Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown;
        }
    }

}
