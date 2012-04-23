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

package org.mobicents.protocols.ss7.map.smstpdu;

import org.mobicents.protocols.ss7.map.api.smstpdu.ConcatenatedShortMessagesIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserDataHeader;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ConcatenatedShortMessagesIdentifierImpl implements ConcatenatedShortMessagesIdentifier {

	private boolean referenceIs16bit;
	private int reference;
	private int mesageSegmentCount;
	private int mesageSegmentNumber;

	public ConcatenatedShortMessagesIdentifierImpl(boolean referenceIs16bit, int reference, int mesageSegmentCount, int mesageSegmentNumber) {
		this.referenceIs16bit = referenceIs16bit;
		this.reference = reference;
		this.mesageSegmentCount = mesageSegmentCount;
		this.mesageSegmentNumber = mesageSegmentNumber;
	}

	public ConcatenatedShortMessagesIdentifierImpl(byte[] encodedInformationElementData) {

		if (encodedInformationElementData == null || encodedInformationElementData.length < 3 || encodedInformationElementData.length > 4)
			return;

		if (encodedInformationElementData.length == 3) {
			this.referenceIs16bit = false;
			this.reference = encodedInformationElementData[0] & 0xFF;
			this.mesageSegmentCount = encodedInformationElementData[1] & 0xFF;
			this.mesageSegmentNumber = encodedInformationElementData[2] & 0xFF;
		} else {
			this.referenceIs16bit = true;
			this.reference = (encodedInformationElementData[0] & 0xFF) + (encodedInformationElementData[1] & 0xFF) << 8;
			this.mesageSegmentCount = encodedInformationElementData[2] & 0xFF;
			this.mesageSegmentNumber = encodedInformationElementData[3] & 0xFF;
		}
	}
	
	@Override
	public boolean getReferenceIs16bit() {
		return referenceIs16bit;
	}

	@Override
	public int getReference() {
		return reference;
	}

	@Override
	public int getMesageSegmentCount() {
		return mesageSegmentCount;
	}

	@Override
	public int getMesageSegmentNumber() {
		return mesageSegmentNumber;
	}

	@Override
	public int getEncodedInformationElementIdentifier() {
		if (this.getReferenceIs16bit())
			return UserDataHeader._InformationElementIdentifier_ConcatenatedShortMessages16bit;
		else
			return UserDataHeader._InformationElementIdentifier_ConcatenatedShortMessages8bit;
	}

	@Override
	public byte[] getEncodedInformationElementData() {
		byte[] res;
		if (this.referenceIs16bit) {
			res = new byte[4];
			res[0] = (byte) (this.reference & 0x00FF);
			res[1] = (byte) ((this.reference & 0xFF00) >> 8);
			res[2] = (byte) (this.mesageSegmentCount & 0xFF);
			res[3] = (byte) (this.mesageSegmentNumber & 0xFF);
		} else {
			res = new byte[3];
			res[0] = (byte) (this.reference & 0x00FF);
			res[1] = (byte) (this.mesageSegmentCount & 0xFF);
			res[2] = (byte) (this.mesageSegmentNumber & 0xFF);
		}
		return res;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ConcatenatedShortMessagesIdentifier [");
		if (this.referenceIs16bit)
			sb.append("Reference=16bit");
		else
			sb.append("Reference=8bit");
		sb.append(", reference=");
		sb.append(this.reference);
		sb.append(", mesagePartsCount=");
		sb.append(this.mesageSegmentCount);
		sb.append(", mesagePartNumber=");
		sb.append(this.mesageSegmentNumber);
		sb.append("]");

		return sb.toString();
	}
}
