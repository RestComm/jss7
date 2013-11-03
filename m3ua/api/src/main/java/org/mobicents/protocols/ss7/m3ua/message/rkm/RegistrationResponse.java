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

package org.mobicents.protocols.ss7.m3ua.message.rkm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationResult;

/**
 * The Registration Response (REG RSP) message is used as a response to the REG REQ message from a remote M3UA peer. It contains
 * indications of success/failure for registration requests and returns a unique Routing Context value for successful
 * registration requests, to be used in subsequent M3UA Traffic Management protocol.
 *
 * @author amit bhayani
 *
 */
public interface RegistrationResponse extends M3UAMessage {
    RegistrationResult getRegistrationResult();

    void setRegistrationResult(RegistrationResult rslt);
}
