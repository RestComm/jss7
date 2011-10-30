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

import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusCause;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartBaseImpl;

/**
 * @author abhayani
 * @author baranowb
 * @author sergey vetyutnev
 */
public class Mtp3UserPartImpl extends Mtp3UserPartBaseImpl {

//	protected ConcurrentLinkedQueue<byte[]> readFrom;
//	protected ConcurrentLinkedQueue<byte[]> writeTo;
	
	private Mtp3UserPartImpl otherPart;

	Mtp3UserPartImpl() {
		try {
			this.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setOtherPart(Mtp3UserPartImpl otherPart) {
		this.otherPart = otherPart;
	}

	@Override
	public void sendMessage(Mtp3TransferPrimitive msg) throws IOException {
		this.otherPart.sendTransferMessageToLocalUser(msg, msg.getSls());
	}

	public void sendPauseMessageToLocalUser(int affectedDpc) {
		Mtp3PausePrimitive msg = new Mtp3PausePrimitive(affectedDpc);
		this.sendPauseMessageToLocalUser(msg);
	}	

	public void sendResumeMessageToLocalUser(int affectedDpc) {
		Mtp3ResumePrimitive msg = new Mtp3ResumePrimitive(affectedDpc);
		this.sendResumeMessageToLocalUser(msg);
	}	

	public void sendStatusMessageToLocalUser(int affectedDpc, Mtp3StatusCause cause, int congestionLevel) {
		Mtp3StatusPrimitive msg = new Mtp3StatusPrimitive(affectedDpc, cause, congestionLevel);
		this.sendStatusMessageToLocalUser(msg);
	}
	
//	public int read(ByteBuffer b) throws IOException {
//		int dataRead = 0;
//		while (!this.readFrom.isEmpty()) {
//			byte[] rxData = this.readFrom.poll();
//			System.out.println("Read " + Arrays.toString(rxData));
//			dataRead = dataRead + rxData.length;
//			b.put(rxData);
//		}
//		return dataRead;
//	}
//
//	
//	public int write(ByteBuffer b) throws IOException {
//		int dataAdded = 0;
//		if (b.hasRemaining()) {
//			byte[] txData = new byte[b.limit() - b.position()];
//			b.get(txData);
//			dataAdded = dataAdded + txData.length;
//			System.out.println("Write " + Arrays.toString(txData));
//			this.writeTo.add(txData);
//		}
//		return dataAdded;
//	}

}
