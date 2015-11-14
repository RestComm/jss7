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

import org.mobicents.protocols.ss7.map.api.service.oam.SGWEventList;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class SGWEventListImpl extends BitStringBase implements SGWEventList {
    static final int _ID_pdnConnectionCreation = 0;
    static final int _ID_pdnConnectionTermination = 1;
    static final int _ID_bearerActivationModificationDeletion = 2;

    public static final String _PrimitiveName = "MMEEventList";

    public SGWEventListImpl() {
        super(3, 8, 3, _PrimitiveName);
    }

    public SGWEventListImpl(boolean pdnConnectionCreation, boolean pdnConnectionTermination, boolean bearerActivationModificationDeletion) {
        super(3, 8, 3, _PrimitiveName);

        if (pdnConnectionCreation)
            this.bitString.set(_ID_pdnConnectionCreation);
        if (pdnConnectionTermination)
            this.bitString.set(_ID_pdnConnectionTermination);
        if (bearerActivationModificationDeletion)
            this.bitString.set(_ID_bearerActivationModificationDeletion);
    }

    @Override
    public boolean getPdnConnectionCreation() {
        return this.bitString.get(_ID_pdnConnectionCreation);
    }

    @Override
    public boolean getPdnConnectionTermination() {
        return this.bitString.get(_ID_pdnConnectionTermination);
    }

    @Override
    public boolean getBearerActivationModificationDeletion() {
        return this.bitString.get(_ID_bearerActivationModificationDeletion);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getPdnConnectionCreation()) {
            sb.append("pdnConnectionCreation, ");
        }
        if (this.getPdnConnectionTermination()) {
            sb.append("pdnConnectionTermination, ");
        }
        if (this.getBearerActivationModificationDeletion()) {
            sb.append("bearerActivationModificationDeletion, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
