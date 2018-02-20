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

package org.restcomm.protocols.ss7.map.api.service.mobility.handover;

/**
 *
 KeyStatus ::= ENUMERATED { old (0), new (1), ...} -- exception handling: -- received values in range 2-31 shall be treated as
 * "old" -- received values greater than 31 shall be treated as "new"
 *
 *
 * @author sergey vetyutnev
 *
 */
public enum KeyStatus {
    _old(0), _new(1);

    private int code;

    private KeyStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static KeyStatus getInstance(int code) {

        switch (code) {
            case 0:
                return KeyStatus._old;
            case 1:
                return KeyStatus._new;
            default:
                if (code >= 2 && code <= 31)
                    return KeyStatus._old;
                else
                    return KeyStatus._new;
        }
    }
}
