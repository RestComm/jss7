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

package org.mobicents.protocols.ss7.m3ua;

import java.io.IOException;

import javolution.util.FastList;

/**
 * A multiplexor of M3UA channels.
 * 
 * @author abhayani
 * @author kulikov
 */
public interface M3UASelector {
    /**
     * Selects a set of keys whose corresponding channels are ready for I/O operations.
     * 
     * @return the collection keys.
     * @throws java.io.IOException
     */
    public FastList<M3UASelectionKey> selectNow() throws IOException;
    
    /**
     * Closes this selector.
     * 
     * @throws java.io.IOException
     */
    public void close() throws IOException;
}

