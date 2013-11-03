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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.BitStringBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class SupportedCamelPhasesImpl extends BitStringBase implements SupportedCamelPhases {

    private static final int _INDEX_Phase1 = 0;
    private static final int _INDEX_Phase2 = 1;
    private static final int _INDEX_Phase3 = 2;
    private static final int _INDEX_Phase4 = 3;

    public SupportedCamelPhasesImpl() {
        super(1, 16, 4, "SupportedCamelPhases");
    }

    public SupportedCamelPhasesImpl(boolean phase1, boolean phase2, boolean phase3, boolean phase4) {
        super(1, 16, 4, "SupportedCamelPhases");

        if (phase1)
            this.bitString.set(_INDEX_Phase1);
        if (phase2)
            this.bitString.set(_INDEX_Phase2);
        if (phase3)
            this.bitString.set(_INDEX_Phase3);
        if (phase4)
            this.bitString.set(_INDEX_Phase4);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement. SupportedCamelPhases#getPhase1Supported()
     */
    public boolean getPhase1Supported() {
        return this.bitString.get(_INDEX_Phase1);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement. SupportedCamelPhases#getPhase2Supported()
     */
    public boolean getPhase2Supported() {
        return this.bitString.get(_INDEX_Phase2);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement. SupportedCamelPhases#getPhase3Supported()
     */
    public boolean getPhase3Supported() {
        return this.bitString.get(_INDEX_Phase3);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberManagement. SupportedCamelPhases#getPhase4Supported()
     */
    public boolean getPhase4Supported() {
        return this.bitString.get(_INDEX_Phase4);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SupportedCamelPhases [");

        if (getPhase1Supported())
            sb.append("Phase1Supported, ");
        if (getPhase2Supported())
            sb.append("Phase2Supported, ");
        if (getPhase3Supported())
            sb.append("Phase3Supported, ");
        if (getPhase4Supported())
            sb.append("Phase4Supported, ");

        sb.append("]");

        return sb.toString();
    }
}
