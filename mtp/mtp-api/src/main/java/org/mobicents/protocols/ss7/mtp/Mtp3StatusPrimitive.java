/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
    /**
     * The User Part identity (3==SCCP, 4==TUP, 5==ISUP, 6==DUP, 8==MTP Testing User Part, 0==unknown)
     */
    private int userPartIdentity;

    public Mtp3StatusPrimitive(int affectedDpc, Mtp3StatusCause cause, int congestionLevel, int userPartIdentity) {
        super(STATUS, affectedDpc);
        this.cause = cause;
        this.congestionLevel = congestionLevel;
        this.userPartIdentity = userPartIdentity;
    }

    public Mtp3StatusCause getCause() {
        return this.cause;
    }

    public int getCongestionLevel() {
        return this.congestionLevel;
    }

    public int getUserPartIdentity() {
        return this.userPartIdentity;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("MTP-STATUS: AffectedDpc=");
        sb.append(this.affectedDpc);
        sb.append(", Cause=");
        sb.append(this.cause.toString());
        sb.append(", CongLevel=");
        sb.append(this.congestionLevel);
        sb.append(", userPartIdentity=");
        sb.append(this.userPartIdentity);

        return sb.toString();
    }
}
