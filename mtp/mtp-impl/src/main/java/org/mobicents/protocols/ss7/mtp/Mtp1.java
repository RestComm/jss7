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
import org.mobicents.protocols.stream.api.Stream;

/**
 * @author baranowb
 * @author kulikov
 */
public interface Mtp1 extends Stream {
	// FIXME: Oleg what's that?
	/**
	 * Gets the code of this channel.
	 * 
	 * @return the code of this channel.
	 */
	public int getCode();

	/**
	 * Set MTP2 layer serving this MTP1
	 * 
	 * @param link
	 */
	public void setLink(Object link);

	/**
	 * Get MTP2 layer serving this MTP1
	 * 
	 * @return
	 */
	public Object getLink();

	/**
	 * Fetches implementation dependent IO Buffer size which should be used
	 * 
	 * @return integer number, Mtp2 implementation should assign buffers of this
	 *         size to interact with Mtp1
	 */
	public int getIOBufferSize();

	/**
	 * Reads up to buffer.length bytes from layer 1.
	 * 
	 * @param buffer
	 *            reader buffer
	 * @return the number of actually read bytes.
	 */
	public int read(byte[] buffer) throws IOException;

	/**
	 * Writes data to layer 1.
	 * 
	 * @param buffer
	 *            the buffer containing data to write.
	 * @param bytesToWrite
	 */
	public void write(byte[] buffer, int bytesToWrite) throws IOException;

	/**
	 * Open message transfer part layer 1.
	 */
	public void open() throws IOException;

	/**
	 * Close message transfer part layer 1.
	 */
	public void close();

}
