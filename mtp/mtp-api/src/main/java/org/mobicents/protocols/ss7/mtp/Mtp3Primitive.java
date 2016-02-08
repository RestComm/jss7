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
    public static final int END_CONGESTION = 11;

    protected int type;

    //At some point we should change this is long as its 4 byte unsigned
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
