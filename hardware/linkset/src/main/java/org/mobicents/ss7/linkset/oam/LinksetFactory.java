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
 * The abstract class represents a factory for creating {@link Linkset}
 * </p>
 * <p>
 * When {@link LinksetManager} receives command to create new {@link Linkset}, it uses linkset factory to create new linkset
 * depending on options passed
 * </p>
 *
 * @author amit bhayani
 *
 */
public abstract class LinksetFactory {

    public LinksetFactory() {
    }

    /**
     * Create a new linkset analyzing the options passed
     *
     * @param options
     * @return
     * @throws Exception
     */
    public abstract Linkset createLinkset(String[] options) throws Exception;

    /**
     * Get name of this factory
     *
     * @return
     */
    public abstract String getName();

    /**
     * Get linkset name
     *
     * @return
     */
    public abstract String getLinksetName();

    /**
     * Get linkset class
     *
     * @return
     */
    public abstract Class getLinksetClass();

    /**
     * Get link name
     *
     * @return
     */
    public abstract String getLinkName();

    /**
     * Get link class
     *
     * @return
     */
    public abstract Class getLinkClass();

}