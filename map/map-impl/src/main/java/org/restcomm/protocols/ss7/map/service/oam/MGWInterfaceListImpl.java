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

import org.restcomm.protocols.ss7.map.api.service.oam.MGWInterfaceList;
import org.restcomm.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class MGWInterfaceListImpl extends BitStringBase implements MGWInterfaceList {
    static final int _ID_mc = 0;
    static final int _ID_nbUp = 1;
    static final int _ID_iuUp = 2;

    public static final String _PrimitiveName = "MGWInterfaceList";

    public MGWInterfaceListImpl() {
        super(3, 8, 3, _PrimitiveName);
    }

    public MGWInterfaceListImpl(boolean mc, boolean nbUp, boolean iuUp) {
        super(3, 8, 3, _PrimitiveName);

        if (mc)
            this.bitString.set(_ID_mc);
        if (nbUp)
            this.bitString.set(_ID_nbUp);
        if (iuUp)
            this.bitString.set(_ID_iuUp);
    }

    @Override
    public boolean getMc() {
        return this.bitString.get(_ID_mc);
    }

    @Override
    public boolean getNbUp() {
        return this.bitString.get(_ID_nbUp);
    }

    @Override
    public boolean getIuUp() {
        return this.bitString.get(_ID_iuUp);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getMc()) {
            sb.append("mc, ");
        }
        if (this.getNbUp()) {
            sb.append("nbUp, ");
        }
        if (this.getIuUp()) {
            sb.append("iuUp, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
