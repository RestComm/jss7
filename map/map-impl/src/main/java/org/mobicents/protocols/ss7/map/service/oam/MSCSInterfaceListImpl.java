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

import org.mobicents.protocols.ss7.map.api.service.oam.MSCSInterfaceList;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class MSCSInterfaceListImpl extends BitStringBase implements MSCSInterfaceList {
    static final int _ID_a = 0;
    static final int _ID_iu = 1;
    static final int _ID_mc = 2;
    static final int _ID_mapG = 3;
    static final int _ID_mapB = 4;
    static final int _ID_mapE = 5;
    static final int _ID_mapF = 6;
    static final int _ID_cap = 7;
    static final int _ID_mapD = 8;
    static final int _ID_mapC = 9;

    public static final String _PrimitiveName = "MSCSInterfaceList";

    public MSCSInterfaceListImpl() {
        super(10, 16, 10, _PrimitiveName);
    }

    public MSCSInterfaceListImpl(boolean a, boolean iu, boolean mc, boolean mapG, boolean mapB, boolean mapE, boolean mapF, boolean cap, boolean mapD,
            boolean mapC) {
        super(10, 16, 10, _PrimitiveName);

        if (a)
            this.bitString.set(_ID_a);
        if (iu)
            this.bitString.set(_ID_iu);
        if (mc)
            this.bitString.set(_ID_mc);
        if (mapG)
            this.bitString.set(_ID_mapG);
        if (mapB)
            this.bitString.set(_ID_mapB);
        if (mapE)
            this.bitString.set(_ID_mapE);
        if (mapF)
            this.bitString.set(_ID_mapF);
        if (cap)
            this.bitString.set(_ID_cap);
        if (mapD)
            this.bitString.set(_ID_mapD);
        if (mapC)
            this.bitString.set(_ID_mapC);
    }

    @Override
    public boolean getA() {
        return this.bitString.get(_ID_a);
    }

    @Override
    public boolean getIu() {
        return this.bitString.get(_ID_iu);
    }

    @Override
    public boolean getMc() {
        return this.bitString.get(_ID_mc);
    }

    @Override
    public boolean getMapG() {
        return this.bitString.get(_ID_mapG);
    }

    @Override
    public boolean getMapB() {
        return this.bitString.get(_ID_mapB);
    }

    @Override
    public boolean getMapE() {
        return this.bitString.get(_ID_mapE);
    }

    @Override
    public boolean getMapF() {
        return this.bitString.get(_ID_mapF);
    }

    @Override
    public boolean getCap() {
        return this.bitString.get(_ID_cap);
    }

    @Override
    public boolean getMapD() {
        return this.bitString.get(_ID_mapD);
    }

    @Override
    public boolean getMapC() {
        return this.bitString.get(_ID_mapC);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getA()) {
            sb.append("a, ");
        }
        if (this.getIu()) {
            sb.append("iu, ");
        }
        if (this.getMc()) {
            sb.append("mc, ");
        }
        if (this.getMapG()) {
            sb.append("mapG, ");
        }
        if (this.getMapB()) {
            sb.append("mapB, ");
        }
        if (this.getMapE()) {
            sb.append("mapE, ");
        }
        if (this.getMapF()) {
            sb.append("mapF, ");
        }
        if (this.getCap()) {
            sb.append("cap, ");
        }
        if (this.getMapD()) {
            sb.append("mapD, ");
        }
        if (this.getMapC()) {
            sb.append("mapC, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
