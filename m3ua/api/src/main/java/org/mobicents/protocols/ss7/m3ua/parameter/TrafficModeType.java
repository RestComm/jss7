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
 * The optional Traffic Mode Type parameter identifies the traffic mode of operation of the ASP(s) within an Application Server.
 * </p>
 * <p>
 * The valid values for Traffic Mode Type are shown in the following table:
 * <ul>
 * <li>1 Override</li>
 * <li>2 Loadshare</li>
 * <li>3 Broadcast</li>
 * </ul>
 * </p>
 *
 * @author amit bhayani
 *
 */
public interface TrafficModeType extends Parameter {

    int Override = 1;
    int Loadshare = 2;
    int Broadcast = 3;

    int getMode();

}
