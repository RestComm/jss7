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

package org.mobicents.protocols.ss7.sccp.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;

/**
 * @author abhayani
 * @author baranowb
 */
public class Mtp3UserPartImpl implements Mtp3UserPart {

	protected ConcurrentLinkedQueue<byte[]> readFrom;
	protected ConcurrentLinkedQueue<byte[]> writeTo;

	Mtp3UserPartImpl(ConcurrentLinkedQueue<byte[]> readFrom, ConcurrentLinkedQueue<byte[]> writeTo) {
		this.readFrom = readFrom;
		this.writeTo = writeTo;
	}

	
	public int read(ByteBuffer b) throws IOException {
		int dataRead = 0;
		while (!this.readFrom.isEmpty()) {
			byte[] rxData = this.readFrom.poll();
			System.out.println("Read " + Arrays.toString(rxData));
			dataRead = dataRead + rxData.length;
			b.put(rxData);
		}
		return dataRead;
	}

	
	public int write(ByteBuffer b) throws IOException {
		int dataAdded = 0;
		if (b.hasRemaining()) {
			byte[] txData = new byte[b.limit() - b.position()];
			b.get(txData);
			dataAdded = dataAdded + txData.length;
			System.out.println("Write " + Arrays.toString(txData));
			this.writeTo.add(txData);
		}
		return dataAdded;
	}


	@Override
	public void execute() throws IOException {
		//We don't use this
		
	}

}
