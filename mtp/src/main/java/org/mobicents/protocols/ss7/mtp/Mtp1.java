/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */
package org.mobicents.protocols.ss7.mtp;

import java.io.IOException;

/**
 * @author baranowb
 * @author kulikov
 */
public interface Mtp1 {
    /**
     * Reads upto buffer.length bytes from layer 1.
     * 
     * @param buffer reader buffer
     * @return the number of actualy read bytes.
     */
    public int read(byte[] buffer) throws IOException;
    
    /**
     * Writes data to layer 1.
     * 
     * @param buffer the buffer containing data to write.
     * @param bytesRead 
     */
    public void write(byte[] buffer, int bytesRead) throws IOException;
    
    /**
     * Open message tranfer part layer 1.
     */
    public void open() throws IOException;
    
    /**
     * Close message tranfer part layer 1.
     */
    public void close();

    
}
