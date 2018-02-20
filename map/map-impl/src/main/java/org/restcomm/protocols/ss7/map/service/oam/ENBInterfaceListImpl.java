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

import org.restcomm.protocols.ss7.map.api.service.oam.ENBInterfaceList;
import org.restcomm.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class ENBInterfaceListImpl extends BitStringBase implements ENBInterfaceList {
    static final int _ID_s1Mme = 0;
    static final int _ID_x2 = 1;
    static final int _ID_uu = 2;

    public static final String _PrimitiveName = "ENBInterfaceList";

    public ENBInterfaceListImpl() {
        super(3, 8, 3, _PrimitiveName);
    }

    public ENBInterfaceListImpl(boolean s1Mme, boolean x2, boolean uu) {
        super(3, 8, 3, _PrimitiveName);

        if (s1Mme)
            this.bitString.set(_ID_s1Mme);
        if (x2)
            this.bitString.set(_ID_x2);
        if (uu)
            this.bitString.set(_ID_uu);
    }

    @Override
    public boolean getS1Mme() {
        return this.bitString.get(_ID_s1Mme);
    }

    @Override
    public boolean getX2() {
        return this.bitString.get(_ID_x2);
    }

    @Override
    public boolean getUu() {
        return this.bitString.get(_ID_uu);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getS1Mme()) {
            sb.append("s1Mme, ");
        }
        if (this.getX2()) {
            sb.append("x2, ");
        }
        if (this.getUu()) {
            sb.append("uu, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
