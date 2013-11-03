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
public abstract class Mtp3Primitive {

    // SI flag is 0
    // public static final int SERVICE_INDICATOR = 0;

    // Type definition
    public static final int PAUSE = 3;
    public static final int RESUME = 4;
    public static final int STATUS = 5;

    protected int type;
    protected int affectedDpc;

    public Mtp3Primitive() {

    }

    /**
     *
     */
    public Mtp3Primitive(int type, int affectedDpc) {
        this.type = type;
        this.affectedDpc = affectedDpc;
    }

    public int getAffectedDpc() {
        return affectedDpc;
    }

    public int getType() {
        return type;
    }

}
