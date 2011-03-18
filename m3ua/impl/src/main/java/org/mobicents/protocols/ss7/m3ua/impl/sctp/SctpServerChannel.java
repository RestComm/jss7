/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.mobicents.protocols.ss7.m3ua.impl.sctp;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.spi.AbstractSelectableChannel;

import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAServerChannelImpl;

/**
 * @author amit bhayani
 * 
 */
public class SctpServerChannel extends M3UAServerChannelImpl {

	private SctpProvider provider;

	/**
	 * @param channel
	 * @throws IOException
	 */
	public SctpServerChannel(SctpProvider provider,
			AbstractSelectableChannel channel) throws IOException {
		super(channel);
		this.provider = provider;
	}

	/**
	 * Opens new M3UA channel.
	 * 
	 * @return the new M3UA channel
	 * @throws java.io.IOException
	 */
	public static SctpServerChannel open(SctpProvider provider)
			throws IOException {
		return new SctpServerChannel(provider,
				com.sun.nio.sctp.SctpServerChannel.open());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.m3ua.M3UAServerChannel#accept()
	 */
	@Override
	public M3UAChannel accept() throws IOException {
		return new SctpChannel(provider,
				((com.sun.nio.sctp.SctpServerChannel) channel).accept());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.m3ua.M3UAServerChannel#bind(java.net.
	 * SocketAddress)
	 */
	@Override
	public void bind(SocketAddress address) throws IOException {
		((com.sun.nio.sctp.SctpServerChannel) channel).bind(address);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.m3ua.M3UAServerChannel#close()
	 */
	@Override
	public void close() throws IOException {
		((com.sun.nio.sctp.SctpServerChannel) channel).close();
	}

}
