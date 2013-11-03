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
 * <p>
 * The Deregistration Result Status field indicates the success or the reason for failure of the deregistration.
 * </p>
 *
 * <p>
 * Its values may be:
 * <ul>
 * <li>0 Successfully Deregistered</li>
 * <li>1 Error - Unknown</li>
 * <li>2 Error - Invalid Routing Context</li>
 * <li>3 Error - Permission Denied</li>
 * <li>4 Error - Not Registered</li>
 * <li>5 Error - ASP Currently Active for Routing Context</li>
 * </ul>
 * </p>
 *
 * @author amit bhayani
 *
 */
public interface DeregistrationStatus extends Parameter {
    int getStatus();
}
