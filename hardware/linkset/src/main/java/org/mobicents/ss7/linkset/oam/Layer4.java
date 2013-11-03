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

package org.mobicents.ss7.linkset.oam;

/**
 * <p>
 * Listener interface for receiving {@link org.mobicents.ss7.linkset.oam.Linkset} event. The class that is interested in linkset
 * for it's operation implements this interface.
 * </p>
 * <p>
 * {@linkset LinksetManager} calls {@link #add add} method when it adds new Linkset
 * <p>
 *
 * <p>
 * {@linkset LinksetManager} calls {@link #remove remove} method when it remove existing Linkset
 * <p>
 *
 * @author amit bhayani
 *
 */
public interface Layer4 {

    /**
     * New linkset added
     *
     * @param linkset
     */
    void add(Linkset linkset);

    /**
     * Existing linkset removed
     *
     * @param linkset
     */
    void remove(Linkset linkset);

}
