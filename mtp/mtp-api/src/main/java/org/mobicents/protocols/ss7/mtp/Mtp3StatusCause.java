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
 * @author sergey vetyutnev
 *
 */
public enum Mtp3StatusCause {

    SignallingNetworkCongested(0), UserPartUnavailability_Unknown(1), UserPartUnavailability_UnequippedRemoteUser(2), UserPartUnavailability_InaccessibleRemoteUser(
            3);

    private int code;

    private Mtp3StatusCause(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static Mtp3StatusCause getMtp3StatusCause(int cause) {
        switch (cause) {
            case 0:
                return SignallingNetworkCongested;
            case 1:
                return UserPartUnavailability_Unknown;
            case 2:
                return UserPartUnavailability_UnequippedRemoteUser;
            case 3:
                return UserPartUnavailability_InaccessibleRemoteUser;
            default:
                return null;
        }
    }
}
