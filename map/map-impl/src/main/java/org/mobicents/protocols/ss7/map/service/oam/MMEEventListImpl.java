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

import org.mobicents.protocols.ss7.map.api.service.oam.MMEEventList;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
*
* @author sergey vetyutnev
*
*/
public class MMEEventListImpl extends BitStringBase implements MMEEventList {
    static final int _ID_ueInitiatedPDNconectivityRequest = 0;
    static final int _ID_serviceRequestts = 1;
    static final int _ID_initialAttachTrackingAreaUpdateDetach = 2;
    static final int _ID_ueInitiatedPDNdisconnection = 3;
    static final int _ID_bearerActivationModificationDeletion = 4;
    static final int _ID_handover = 5;

    public static final String _PrimitiveName = "MMEEventList";

    public MMEEventListImpl() {
        super(6, 8, 6, _PrimitiveName);
    }

    public MMEEventListImpl(boolean ueInitiatedPDNconectivityRequest, boolean serviceRequestts, boolean initialAttachTrackingAreaUpdateDetach,
            boolean ueInitiatedPDNdisconnection, boolean bearerActivationModificationDeletion, boolean handover) {
        super(6, 8, 6, _PrimitiveName);

        if (ueInitiatedPDNconectivityRequest)
            this.bitString.set(_ID_ueInitiatedPDNconectivityRequest);
        if (serviceRequestts)
            this.bitString.set(_ID_serviceRequestts);
        if (initialAttachTrackingAreaUpdateDetach)
            this.bitString.set(_ID_initialAttachTrackingAreaUpdateDetach);
        if (ueInitiatedPDNdisconnection)
            this.bitString.set(_ID_ueInitiatedPDNdisconnection);
        if (bearerActivationModificationDeletion)
            this.bitString.set(_ID_bearerActivationModificationDeletion);
        if (handover)
            this.bitString.set(_ID_handover);
    }

    @Override
    public boolean getUeInitiatedPDNconectivityRequest() {
        return this.bitString.get(_ID_ueInitiatedPDNconectivityRequest);
    }

    @Override
    public boolean getServiceRequestts() {
        return this.bitString.get(_ID_serviceRequestts);
    }

    @Override
    public boolean getInitialAttachTrackingAreaUpdateDetach() {
        return this.bitString.get(_ID_initialAttachTrackingAreaUpdateDetach);
    }

    @Override
    public boolean getUeInitiatedPDNdisconnection() {
        return this.bitString.get(_ID_ueInitiatedPDNdisconnection);
    }

    @Override
    public boolean getBearerActivationModificationDeletion() {
        return this.bitString.get(_ID_bearerActivationModificationDeletion);
    }

    @Override
    public boolean getHandover() {
        return this.bitString.get(_ID_handover);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.getUeInitiatedPDNconectivityRequest()) {
            sb.append("ueInitiatedPDNconectivityRequest, ");
        }
        if (this.getServiceRequestts()) {
            sb.append("serviceRequestts, ");
        }
        if (this.getInitialAttachTrackingAreaUpdateDetach()) {
            sb.append("initialAttachTrackingAreaUpdateDetach, ");
        }
        if (this.getUeInitiatedPDNdisconnection()) {
            sb.append("ueInitiatedPDNdisconnection, ");
        }
        if (this.getBearerActivationModificationDeletion()) {
            sb.append("bearerActivationModificationDeletion, ");
        }
        if (this.getHandover()) {
            sb.append("handover, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
