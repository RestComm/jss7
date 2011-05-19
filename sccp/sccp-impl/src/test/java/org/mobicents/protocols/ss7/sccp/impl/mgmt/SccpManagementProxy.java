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

package org.mobicents.protocols.ss7.sccp.impl.mgmt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.ss7.sccp.impl.SccpManagement;
import org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpRoutingControl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.message.UnitData;

/**
 * @author baranowb
 *
 */
public class SccpManagementProxy extends SccpManagement {

	private int seq = 0; //seq, to mark messages, so we get them correctly
	//separate lists, thats better
	private List<SccpMgmtMessage> mgmtMessages = new ArrayList<SccpMgmtMessage>();
	private List<Mtp3PrimitiveMessage> mtp3Messages = new ArrayList<Mtp3PrimitiveMessage>();
	private boolean encounteredError = false;
	/**
	 * @param sccpProviderImpl
	 * @param sccpStackImpl
	 */
	public SccpManagementProxy(SccpProviderImpl sccpProviderImpl, SccpStackImpl sccpStackImpl) {
		super(sccpProviderImpl, sccpStackImpl);
		// TODO Auto-generated constructor stub
	}
	
	// =----------------= some getters

	public int getSeq() {
		return seq;
	}

	public boolean isEncounteredError() {
		return encounteredError;
	}

	public List<SccpMgmtMessage> getMgmtMessages() {
		return mgmtMessages;
	}

	public List<Mtp3PrimitiveMessage> getMtp3Messages() {
		return mtp3Messages;
	}

	// =----------------= deletage to intercept.
	
	public SccpRoutingControl getSccpRoutingControl() {
		
		return super.getSccpRoutingControl();
	}

	public void setSccpRoutingControl(SccpRoutingControl sccpRoutingControl) {
		
		super.setSccpRoutingControl(sccpRoutingControl);
	}

	public void onMessage(SccpMessage message, int seqControl) {
		byte[] data = ((UnitData) message).getData();
		int messgType = data[0];
		int affectedSsn = data[1];
		int affectedPc = (data[2] & 0x00FF) | (data[3] & 0x00FF << 8);
		int subsystemMultiplicity = data[3];
		SccpMgmtMessage mgmtMessage = new SccpMgmtMessage(seq++,messgType,affectedSsn,affectedPc,subsystemMultiplicity);
		mgmtMessages.add(mgmtMessage);
		super.onMessage(message, seqControl);
	}

	protected void recdMsgForProhibitedSsn(SccpMessage msg, int ssn) {
		
		super.recdMsgForProhibitedSsn(msg, ssn);
	}

	public void start() {
		
		super.start();
	}

	public void stop() {
		
		super.stop();
	}

	protected void handleMtp3Primitive(DataInputStream in) {
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(boas);
		try {
			
			int mtpParam = in.readUnsignedByte();
			dos.writeByte(mtpParam & 0xFF);
			
			switch (mtpParam) {
			case MTP3_PAUSE:
			case MTP3_RESUME:
				int affectedPc = in.readInt();
				dos.writeInt(affectedPc);
				Mtp3PrimitiveMessage prim = new Mtp3PrimitiveMessage(seq++,mtpParam,affectedPc);
				mtp3Messages.add(prim);
				break;
			case MTP3_STATUS:
				//here we have more :)
				int status = in.readUnsignedByte();
				dos.writeByte(status & 0xFF);
				affectedPc = in.readInt();
				dos.writeInt(affectedPc);
				int congStatus = in.readShort();
				dos.writeShort(congStatus);
				int unavailabiltyCause = in.readShort();
				dos.writeShort(unavailabiltyCause);
				prim = new Mtp3PrimitiveMessage(seq++,mtpParam,affectedPc,status,congStatus,unavailabiltyCause);
				mtp3Messages.add(prim);
				break;
			default:
				encounteredError = true;
				break;
			}
		} catch (IOException e) {
			encounteredError = true;
			e.printStackTrace();
		}
		super.handleMtp3Primitive(new DataInputStream(new ByteArrayInputStream(boas.toByteArray())));
	}

}