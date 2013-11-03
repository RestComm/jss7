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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

/**
 * NumberPortabilityStatus ::= ENUMERATED { notKnownToBePorted (0), ownNumberPortedOut (1), foreignNumberPortedToForeignNetwork
 * (2), ..., ownNumberNotPortedOut (4), foreignNumberPortedIn (5) } -- exception handling: -- reception of other values than the
 * ones listed the receiver shall ignore the -- whole NumberPortabilityStatus; -- ownNumberNotPortedOut or foreignNumberPortedIn
 * may only be included in Any Time -- Interrogation message.
 *
 * @author amit bhayani
 *
 */
public enum NumberPortabilityStatus {
    notKnownToBePorted(0), ownNumberPortedOut(1), foreignNumberPortedToForeignNetwork(2), ownNumberNotPortedOut(4), foreignNumberPortedIn(
            5);

    private final int type;

    private NumberPortabilityStatus(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public static NumberPortabilityStatus getInstance(int type) {
        switch (type) {
            case 0:
                return notKnownToBePorted;
            case 1:
                return ownNumberPortedOut;
            case 2:
                return foreignNumberPortedToForeignNetwork;
            case 4:
                return ownNumberNotPortedOut;
            case 5:
                return foreignNumberPortedIn;
            default:
                return null;
        }
    }
}
