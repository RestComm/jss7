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

package org.mobicents.protocols.ss7.mtp;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class Mtp3StatusPrimitive extends Mtp3Primitive {

    private Mtp3StatusCause cause;
    /**
     * Dialogic: Congestion status (if status = 0x02).<br/>
     * This field is set to the current congestion level in the range 0 to 3, <br/>
     * where 0 means no congestion and 3 means maximum congestion. <br/>
     * Many networks use only a single level of congestion (that is, 1). <br/>
     */
    private int congestionLevel;

    public Mtp3StatusPrimitive(int affectedDpc, Mtp3StatusCause cause, int congestionLevel) {
        super(STATUS, affectedDpc);
        this.cause = cause;
        this.congestionLevel = congestionLevel;
    }

    public Mtp3StatusCause getCause() {
        return this.cause;
    }

    public int getCongestionLevel() {
        return this.congestionLevel;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("MTP-STATUS: AffectedDpc=");
        sb.append(this.affectedDpc);
        sb.append("Cause=");
        sb.append(this.cause.toString());
        sb.append("CongLevel=");
        sb.append(this.congestionLevel);

        return sb.toString();
    }
}
