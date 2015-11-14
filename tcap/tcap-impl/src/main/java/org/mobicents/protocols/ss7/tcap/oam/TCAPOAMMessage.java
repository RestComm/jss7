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

package org.mobicents.protocols.ss7.tcap.oam;

/**
 * @author Amit Bhayani
 *
 */
public interface TCAPOAMMessage {

    String INVALID_COMMAND = "Invalid Command";

    String SERVER_ERROR = "Server Error";

    String PARAMETER_SUCCESSFULLY_SET = "Parameter has been successfully set for stack=%s";

    String NO_TCAP_STCAK_CONFIGURE = "No TCAP stack configured for name %s";
}
