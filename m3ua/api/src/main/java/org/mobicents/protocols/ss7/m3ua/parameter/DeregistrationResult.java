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

package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The Deregistration Result parameter contains the deregistration status for a single Routing Context in a DEREG REQ message.
 * The number of results in a single DEREG RSP message MAY be anywhere from one to the total number of number of Routing Context
 * values found in the corresponding DEREG REQ message.
 *
 * Where multiple DEREG RSP messages are used in reply to DEREG REQ message, a specific result SHOULD be in only one DEREG RSP
 * message.
 *
 * @author amit bhayani
 *
 */
public interface DeregistrationResult extends Parameter {

    RoutingContext getRoutingContext();

    DeregistrationStatus getDeregistrationStatus();
}
