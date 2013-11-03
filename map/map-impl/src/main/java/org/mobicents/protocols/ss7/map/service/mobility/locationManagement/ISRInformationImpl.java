/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ISRInformation;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ISRInformationImpl extends BitStringBase implements ISRInformation {

    private static final int _INDEX_updateMME = 0;
    private static final int _INDEX_cancelSGSN = 1;
    private static final int _INDEX_initialAttachIndicator = 2;

    public ISRInformationImpl(boolean updateMME, boolean cancelSGSN, boolean initialAttachIndicator) {
        super(3, 8, 3, "ISRInformation");
        if (updateMME)
            this.bitString.set(_INDEX_updateMME);
        if (cancelSGSN)
            this.bitString.set(_INDEX_cancelSGSN);
        if (initialAttachIndicator)
            this.bitString.set(_INDEX_initialAttachIndicator);
    }

    public ISRInformationImpl() {
        super(3, 8, 3, "ISRInformation");
    }

    @Override
    public boolean getUpdateMME() {
        return this.bitString.get(_INDEX_updateMME);
    }

    @Override
    public boolean getCancelSGSN() {
        return this.bitString.get(_INDEX_cancelSGSN);
    }

    @Override
    public boolean getInitialAttachIndicator() {
        return this.bitString.get(_INDEX_initialAttachIndicator);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.getUpdateMME())
            sb.append("UpdateMME, ");
        if (this.getCancelSGSN())
            sb.append("CancelSGSN, ");
        if (this.getInitialAttachIndicator())
            sb.append("InitialAttachIndicator ");
        sb.append("]");
        return sb.toString();
    }

}
