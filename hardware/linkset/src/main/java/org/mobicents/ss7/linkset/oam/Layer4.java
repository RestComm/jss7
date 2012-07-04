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
 * Listener interface for receiving
 * {@link org.mobicents.ss7.linkset.oam.Linkset} event. The class that is
 * interested in linkset for it's operation implements this interface.
 * </p>
 * <p>
 * {@linkset LinksetManager} calls {@link #add add} method when it adds new
 * Linkset
 * <p>
 * 
 * <p>
 * {@linkset LinksetManager} calls {@link #remove remove} method when it remove
 * existing Linkset
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
    public void add(Linkset linkset);

    /**
     * Existing linkset removed
     * 
     * @param linkset
     */
    public void remove(Linkset linkset);

}
