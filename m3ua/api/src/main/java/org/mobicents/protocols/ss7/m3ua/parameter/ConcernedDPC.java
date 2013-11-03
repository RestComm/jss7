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
 * The optional Concerned Destination parameter is only used if the SCON message is sent from an ASP to the SGP. It contains the
 * point code of the originator of the message that triggered the SCON message. Any resulting Transfer Controlled (TFC) message
 * from the SG is sent to the Concerned Point Code using the single Affected DPC contained in the SCON message to populate the
 * (affected) Destination field of the TFC message
 *
 * @author abhayani
 *
 */
public interface ConcernedDPC extends Parameter {

    int getPointCode();

}
