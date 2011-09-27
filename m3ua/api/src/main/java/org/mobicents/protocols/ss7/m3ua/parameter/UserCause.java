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
 * The Unavailability Cause and MTP3-User Identity fields, associated with the
 * Affected PC in the Affected Point Code parameter
 * 
 * @author amit bhayani
 * 
 */
public interface UserCause extends Parameter {

    /**
     * <p>
     * The Unavailability Cause parameter provides the reason for the
     * unavailability of the MTP3-User. The valid values for the Unavailability
     * Cause parameter are
     * </p>
     * <p>
     * <ul>
     * <li>0 Unknown</li>
     * <li>1 Unequipped Remote User</li>
     * <li>2 Inaccessible Remote User</li>
     * </ul>
     * </p>
     * 
     * @return
     */
    public int getCause();

    /**
     * <p>
     * The MTP3-User Identity describes the specific MTP3-User that is
     * unavailable (e.g., ISUP, SCCP, etc.). Some of the valid values for the
     * MTP3-User Identity are
     * </p>
     * <p>
     * <ul>
     * <li>0 to 2 Reserved</li>
     * <li>3 SCCP</li>
     * <li>4 TUP</li>
     * <li>5 ISUP</li>
     * <li>6 to 8 Reserved</li>
     * <li>9 Broadband ISUP</li>
     * <li>10 Satellite ISUP</li>
     * <li>11 Reserved</li>
     * <li>12 AAL type 2 Signalling</li>
     * <li>13 Bearer Independent Call Control (BICC)</li>
     * <li>14 Gateway Control Protocol</li>
     * <li>15 Reserved</li>
     * </ul>
     * </p>
     * 
     * @return
     */
    public int getUser();

}
