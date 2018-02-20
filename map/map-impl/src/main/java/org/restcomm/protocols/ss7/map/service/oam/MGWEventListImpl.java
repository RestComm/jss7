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

import org.restcomm.protocols.ss7.map.api.service.oam.MGWEventList;
import org.restcomm.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class MGWEventListImpl extends BitStringBase implements MGWEventList {
    static final int _ID_context = 0;

    public static final String _PrimitiveName = "MGWEventList";

    public MGWEventListImpl() {
        super(1, 8, 1, _PrimitiveName);
    }

    public MGWEventListImpl(boolean context) {
        super(1, 8, 1, _PrimitiveName);

        if (context)
            this.bitString.set(_ID_context);
    }

    @Override
    public boolean getContext() {
        return this.bitString.get(_ID_context);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getContext()) {
            sb.append("context, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
