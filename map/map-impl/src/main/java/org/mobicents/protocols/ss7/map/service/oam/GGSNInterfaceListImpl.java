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

package org.mobicents.protocols.ss7.map.service.oam;

import org.mobicents.protocols.ss7.map.api.service.oam.GGSNInterfaceList;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class GGSNInterfaceListImpl extends BitStringBase implements GGSNInterfaceList {
    static final int _ID_gn = 0;
    static final int _ID_gi = 1;
    static final int _ID_gmb = 2;

    public static final String _PrimitiveName = "GGSNInterfaceList";

    public GGSNInterfaceListImpl() {
        super(3, 8, 3, _PrimitiveName);
    }

    public GGSNInterfaceListImpl(boolean gn, boolean gi, boolean gmb) {
        super(3, 8, 3, _PrimitiveName);

        if (gn)
            this.bitString.set(_ID_gn);
        if (gi)
            this.bitString.set(_ID_gi);
        if (gmb)
            this.bitString.set(_ID_gmb);
    }

    @Override
    public boolean getGn() {
        return this.bitString.get(_ID_gn);
    }

    @Override
    public boolean getGi() {
        return this.bitString.get(_ID_gi);
    }

    @Override
    public boolean getGmb() {
        return this.bitString.get(_ID_gmb);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getGn()) {
            sb.append("gn, ");
        }
        if (this.getGi()) {
            sb.append("gi, ");
        }
        if (this.getGmb()) {
            sb.append("gmb, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
