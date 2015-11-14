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

import org.mobicents.protocols.ss7.map.api.service.oam.MMEInterfaceList;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class MMEInterfaceListImpl extends BitStringBase implements MMEInterfaceList {
    static final int _ID_s1Mme = 0;
    static final int _ID_s3 = 1;
    static final int _ID_s6a = 2;
    static final int _ID_s10 = 3;
    static final int _ID_s11 = 4;

    public static final String _PrimitiveName = "MMEInterfaceList";

    public MMEInterfaceListImpl() {
        super(5, 8, 5, _PrimitiveName);
    }

    public MMEInterfaceListImpl(boolean s1Mme, boolean s3, boolean s6a, boolean s10, boolean s11) {
        super(5, 8, 5, _PrimitiveName);

        if (s1Mme)
            this.bitString.set(_ID_s1Mme);
        if (s3)
            this.bitString.set(_ID_s3);
        if (s6a)
            this.bitString.set(_ID_s6a);
        if (s10)
            this.bitString.set(_ID_s10);
        if (s11)
            this.bitString.set(_ID_s11);
    }

    @Override
    public boolean getS1Mme() {
        return this.bitString.get(_ID_s1Mme);
    }

    @Override
    public boolean getS3() {
        return this.bitString.get(_ID_s3);
    }

    @Override
    public boolean getS6a() {
        return this.bitString.get(_ID_s6a);
    }

    @Override
    public boolean getS10() {
        return this.bitString.get(_ID_s10);
    }

    @Override
    public boolean getS11() {
        return this.bitString.get(_ID_s11);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getS1Mme()) {
            sb.append("s1Mme, ");
        }
        if (this.getS3()) {
            sb.append("s3, ");
        }
        if (this.getS6a()) {
            sb.append("s6a, ");
        }
        if (this.getS10()) {
            sb.append("s10, ");
        }
        if (this.getS11()) {
            sb.append("s11, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
