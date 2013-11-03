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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 *
 */
public class CongestedIndicationImpl extends ParameterImpl implements CongestedIndication {

    private CongestionLevel level;

    protected CongestedIndicationImpl(CongestionLevel level) {
        this.level = level;
        this.tag = Parameter.Congestion_Indications;
    }

    protected CongestedIndicationImpl(byte[] data) {
        // data[0], data[1] and data[2] are reserved
        this.level = CongestionLevel.getCongestionLevel(data[3]);
        this.tag = Parameter.Congestion_Indications;
    }

    @Override
    protected byte[] getValue() {
        byte[] data = new byte[4];
        data[0] = 0;// Reserved
        data[1] = 0; // Reserved
        data[2] = 0;// Reserved
        data[3] = (byte) level.getLevel();

        return data;
    }

    public CongestionLevel getCongestionLevel() {
        return this.level;
    }

    @Override
    public String toString() {
        return String.format("CongestedIndication level=%s", level);
    }

}
