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
 * The affected point code parameter identifies which point codes are affected, depending on the message type
 * </p>
 *
 * @author amit bhayani
 *
 */
public interface AffectedPointCode extends Parameter {

    /**
     * <p>
     * To make it easier to identify multiple point codes, ranges can be used as well. The mask field is used to identify ranges
     * within the point code. For example, if the mask contains a value of 2, this would indicate that the last two digits of
     * the point code are a “wild card.”
     * </p>
     *
     * @return returns the mask
     */
    short[] getMasks();

    /**
     * returns the affected point code.
     *
     * @return
     */
    int[] getPointCodes();

}
