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

import org.restcomm.protocols.ss7.map.api.service.oam.SGSNEventList;
import org.restcomm.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class SGSNEventListImpl extends BitStringBase implements SGSNEventList {
    static final int _ID_pdpContext = 0;
    static final int _ID_moMtSms = 1;
    static final int _ID_rauGprsAttachGprsDetach = 2;
    static final int _ID_mbmsContext = 3;

    public static final String _PrimitiveName = "SGSNEventList";

    public SGSNEventListImpl() {
        super(4, 16, 4, _PrimitiveName);
    }

    public SGSNEventListImpl(boolean pdpContext, boolean moMtSms, boolean rauGprsAttachGprsDetach, boolean mbmsContext) {
        super(4, 16, 4, _PrimitiveName);

        if (pdpContext)
            this.bitString.set(_ID_pdpContext);
        if (moMtSms)
            this.bitString.set(_ID_moMtSms);
        if (rauGprsAttachGprsDetach)
            this.bitString.set(_ID_rauGprsAttachGprsDetach);
        if (mbmsContext)
            this.bitString.set(_ID_mbmsContext);
    }

    @Override
    public boolean getPdpContext() {
        return this.bitString.get(_ID_pdpContext);
    }

    @Override
    public boolean getMoMtSms() {
        return this.bitString.get(_ID_moMtSms);
    }

    @Override
    public boolean getRauGprsAttachGprsDetach() {
        return this.bitString.get(_ID_rauGprsAttachGprsDetach);
    }

    @Override
    public boolean getMbmsContext() {
        return this.bitString.get(_ID_mbmsContext);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getPdpContext()) {
            sb.append("pdpContext, ");
        }
        if (this.getMoMtSms()) {
            sb.append("moMtSms, ");
        }
        if (this.getRauGprsAttachGprsDetach()) {
            sb.append("rauGprsAttachGprsDetach, ");
        }
        if (this.getMbmsContext()) {
            sb.append("mbmsContext, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
