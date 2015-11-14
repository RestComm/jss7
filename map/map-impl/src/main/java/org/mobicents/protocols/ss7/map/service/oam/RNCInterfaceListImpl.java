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

import org.mobicents.protocols.ss7.map.api.service.oam.RNCInterfaceList;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class RNCInterfaceListImpl extends BitStringBase implements RNCInterfaceList {
    static final int _ID_iu = 0;
    static final int _ID_iur = 1;
    static final int _ID_iub = 2;
    static final int _ID_uu = 3;

    public static final String _PrimitiveName = "RNCInterfaceList";

    public RNCInterfaceListImpl() {
        super(4, 8, 4, _PrimitiveName);
    }

    public RNCInterfaceListImpl(boolean iu, boolean iur, boolean iub, boolean uu) {
        super(4, 8, 4, _PrimitiveName);

        if (iu)
            this.bitString.set(_ID_iu);
        if (iur)
            this.bitString.set(_ID_iur);
        if (iub)
            this.bitString.set(_ID_iub);
        if (uu)
            this.bitString.set(_ID_uu);
    }

    @Override
    public boolean getIu() {
        return this.bitString.get(_ID_iu);
    }

    @Override
    public boolean getIur() {
        return this.bitString.get(_ID_iur);
    }

    @Override
    public boolean getIub() {
        return this.bitString.get(_ID_iub);
    }

    @Override
    public boolean getUu() {
        return this.bitString.get(_ID_uu);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getIu()) {
            sb.append("iu, ");
        }
        if (this.getIur()) {
            sb.append("iur, ");
        }
        if (this.getIub()) {
            sb.append("iub, ");
        }
        if (this.getUu()) {
            sb.append("uu, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
