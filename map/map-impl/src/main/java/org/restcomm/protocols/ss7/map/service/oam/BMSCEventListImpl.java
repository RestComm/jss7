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

package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.BMSCEventList;
import org.restcomm.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class BMSCEventListImpl extends BitStringBase implements BMSCEventList {
    static final int _ID_mbmsMulticastServiceActivation = 0;

    public static final String _PrimitiveName = "BMSCEventList";

    public BMSCEventListImpl() {
        super(1, 8, 1, _PrimitiveName);
    }

    public BMSCEventListImpl(boolean mbmsMulticastServiceActivation) {
        super(1, 8, 1, _PrimitiveName);

        if (mbmsMulticastServiceActivation)
            this.bitString.set(_ID_mbmsMulticastServiceActivation);
    }

    @Override
    public boolean getMbmsMulticastServiceActivation() {
        return this.bitString.get(_ID_mbmsMulticastServiceActivation);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getMbmsMulticastServiceActivation()) {
            sb.append("mbmsMulticastServiceActivation, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
