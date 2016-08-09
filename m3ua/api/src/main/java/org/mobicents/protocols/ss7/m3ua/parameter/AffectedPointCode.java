/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
     * the point code are a "wild card."
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
