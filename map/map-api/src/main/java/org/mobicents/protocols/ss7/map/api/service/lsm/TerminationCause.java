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
 * TerminationCause ::= ENUMERATED { normal (0), errorundefined (1), internalTimeout (2), congestion (3), mt-lrRestart (4),
 * privacyViolation (5), ..., shapeOfLocationEstimateNotSupported (6) } -- mt-lrRestart shall be used to trigger the GMLC to
 * restart the location procedure, -- either because the sending node knows that the terminal has moved under coverage -- of
 * another MSC or SGSN (e.g. Send Identification received), or because the subscriber -- has been deregistered due to a Cancel
 * Location received from HLR. -- -- exception handling -- an unrecognized value shall be treated the same as value 1
 * (errorundefined)
 *
 * @author amit bhayani
 *
 */
public enum TerminationCause {

    normal(0), errorundefined(1), internalTimeout(2), congestion(3), mtlrRestart(4), privacyViolation(5);

    private final int cause;

    private TerminationCause(int cause) {
        this.cause = cause;
    }

    public int getCause() {
        return this.cause;
    }

    public static TerminationCause getTerminationCause(int cause) {
        switch (cause) {
            case 0:
                return normal;
            case 1:
                return errorundefined;
            case 2:
                return internalTimeout;
            case 3:
                return congestion;
            case 4:
                return mtlrRestart;
            case 5:
                return privacyViolation;
            default:
                return null;
        }
    }
}
