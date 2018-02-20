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

import org.restcomm.protocols.ss7.map.api.service.oam.SGWInterfaceList;
import org.restcomm.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class SGWInterfaceListImpl extends BitStringBase implements SGWInterfaceList {
    static final int _ID_s4 = 0;
    static final int _ID_s5 = 1;
    static final int _ID_s8b = 2;
    static final int _ID_s11 = 3;
    static final int _ID_gxc = 4;

    public static final String _PrimitiveName = "SGWInterfaceList";

    public SGWInterfaceListImpl() {
        super(5, 8, 5, _PrimitiveName);
    }

    public SGWInterfaceListImpl(boolean s4, boolean s5, boolean s8b, boolean s11, boolean gxc) {
        super(5, 8, 5, _PrimitiveName);

        if (s4)
            this.bitString.set(_ID_s4);
        if (s5)
            this.bitString.set(_ID_s5);
        if (s8b)
            this.bitString.set(_ID_s8b);
        if (s11)
            this.bitString.set(_ID_s11);
        if (gxc)
            this.bitString.set(_ID_gxc);
    }

    @Override
    public boolean getS4() {
        return this.bitString.get(_ID_s4);
    }

    @Override
    public boolean getS5() {
        return this.bitString.get(_ID_s5);
    }

    @Override
    public boolean getS8b() {
        return this.bitString.get(_ID_s8b);
    }

    @Override
    public boolean getS11() {
        return this.bitString.get(_ID_s11);
    }

    @Override
    public boolean getGxc() {
        return this.bitString.get(_ID_gxc);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getS4()) {
            sb.append("s4, ");
        }
        if (this.getS5()) {
            sb.append("s5, ");
        }
        if (this.getS8b()) {
            sb.append("s8b, ");
        }
        if (this.getS11()) {
            sb.append("s11, ");
        }
        if (this.getGxc()) {
            sb.append("gxc, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
