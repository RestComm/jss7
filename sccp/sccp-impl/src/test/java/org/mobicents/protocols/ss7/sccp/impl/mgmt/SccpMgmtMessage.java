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

package org.mobicents.protocols.ss7.sccp.impl.mgmt;

import java.io.Serializable;

/**
 * Simple class to represent mgmt message
 *
 * @author baranowb
 *
 */
public class SccpMgmtMessage implements Serializable {
    private SccpMgmtMessageType type;
    private int affectedSsn;
    private int affectedPc;
    private int subsystemMultiplicity;
    private long tstamp = System.currentTimeMillis();
    private int seq;

    /**
     * @param messgType
     * @param affectedSsn
     * @param affectedPc
     * @param subsystemMultiplicity
     */
    public SccpMgmtMessage(int seq, int messgType, int affectedSsn, int affectedPc, int subsystemMultiplicity) {
        this.type = SccpMgmtMessageType.fromInt(messgType);
        this.affectedPc = affectedPc;
        this.affectedSsn = affectedSsn;
        this.subsystemMultiplicity = subsystemMultiplicity;
        this.seq = seq;
    }

    public SccpMgmtMessageType getType() {
        return type;
    }

    public int getAffectedSsn() {
        return affectedSsn;
    }

    public int getAffectedPc() {
        return affectedPc;
    }

    public int getSubsystemMultiplicity() {
        return subsystemMultiplicity;
    }

    public long getTstamp() {
        return tstamp;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + affectedPc;
        result = prime * result + affectedSsn;
        result = prime * result + seq;
        result = prime * result + subsystemMultiplicity;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SccpMgmtMessage other = (SccpMgmtMessage) obj;
        if (affectedPc != other.affectedPc)
            return false;
        if (affectedSsn != other.affectedSsn)
            return false;
        if (seq != other.seq)
            return false;
        if (subsystemMultiplicity != other.subsystemMultiplicity)
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    public String toString() {
        return "SccpMgmtMessage [type=" + type + ", affectedSsn=" + affectedSsn + ", affectedPc=" + affectedPc
                + ", subsystemMultiplicity=" + subsystemMultiplicity + ", tstamp=" + tstamp + ", seq=" + seq + "]";
    }

}
