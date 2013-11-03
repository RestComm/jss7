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

package org.mobicents.protocols.ss7.map.api.service.lsm;

/**
 * PrivacyCheckRelatedAction ::= ENUMERATED { allowedWithoutNotification (0), allowedWithNotification (1), allowedIfNoResponse
 * (2), restrictedIfNoResponse (3), notAllowed (4), ...} -- exception handling: -- a ProvideSubscriberLocation-Arg containing an
 * unrecognized PrivacyCheckRelatedAction -- shall be rejected by the receiver with a return error cause of unexpected data
 * value
 *
 * @author amit bhayani
 *
 */
public enum PrivacyCheckRelatedAction {

    allowedWithoutNotification(0), allowedWithNotification(1), allowedIfNoResponse(2), restrictedIfNoResponse(3), notAllowed(4);

    private final int action;

    private PrivacyCheckRelatedAction(int action) {
        this.action = action;
    }

    public int getAction() {
        return this.action;
    }

    public static PrivacyCheckRelatedAction getPrivacyCheckRelatedAction(int action) {
        switch (action) {
            case 0:
                return allowedWithoutNotification;
            case 1:
                return allowedWithNotification;
            case 2:
                return allowedIfNoResponse;
            case 3:
                return restrictedIfNoResponse;
            case 4:
                return notAllowed;
            default:
                return null;
        }
    }
}
