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

package org.mobicents.ss7.linkset.oam;

/**
 * <p>
 * The abstract class represents a factory for creating {@link Linkset}
 * </p>
 * <p>
 * When {@link LinksetManager} receives command to create new {@link Linkset},
 * it uses linkset factory to create new linkset depending on options passed
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

}
