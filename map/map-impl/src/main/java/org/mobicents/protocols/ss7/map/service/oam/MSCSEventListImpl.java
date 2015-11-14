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

import org.mobicents.protocols.ss7.map.api.service.oam.MSCSEventList;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class MSCSEventListImpl extends BitStringBase implements MSCSEventList {
    static final int _ID_moMtCall = 0;
    static final int _ID_moMtSms = 1;
    static final int _ID_luImsiAttachImsiDetach = 2;
    static final int _ID_handovers = 3;
    static final int _ID_ss = 4;

    public static final String _PrimitiveName = "MSCSEventList";

    public MSCSEventListImpl() {
        super(5, 16, 5, _PrimitiveName);
    }

    public MSCSEventListImpl(boolean moMtCall, boolean moMtSms, boolean luImsiAttachImsiDetach, boolean handovers, boolean ss) {
        super(5, 16, 5, _PrimitiveName);

        if (moMtCall)
            this.bitString.set(_ID_moMtCall);
        if (moMtSms)
            this.bitString.set(_ID_moMtSms);
        if (luImsiAttachImsiDetach)
            this.bitString.set(_ID_luImsiAttachImsiDetach);
        if (handovers)
            this.bitString.set(_ID_handovers);
        if (ss)
            this.bitString.set(_ID_ss);
    }

    @Override
    public boolean getMoMtCall() {
        return this.bitString.get(_ID_moMtCall);
    }

    @Override
    public boolean getMoMtSms() {
        return this.bitString.get(_ID_moMtSms);
    }

    @Override
    public boolean getLuImsiAttachImsiDetach() {
        return this.bitString.get(_ID_luImsiAttachImsiDetach);
    }

    @Override
    public boolean getHandovers() {
        return this.bitString.get(_ID_handovers);
    }

    @Override
    public boolean getSs() {
        return this.bitString.get(_ID_ss);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getMoMtCall()) {
            sb.append("moMtCall, ");
        }
        if (this.getMoMtSms()) {
            sb.append("moMtSms, ");
        }
        if (this.getLuImsiAttachImsiDetach()) {
            sb.append("luImsiAttachImsiDetach, ");
        }
        if (this.getHandovers()) {
            sb.append("handovers, ");
        }
        if (this.getSs()) {
            sb.append("ss, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
