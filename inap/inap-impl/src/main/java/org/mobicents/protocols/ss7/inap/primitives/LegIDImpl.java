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

package org.mobicents.protocols.ss7.inap.primitives;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;

/**
* 
* @author sergey vetyutnev
* 
*/
public class LegIDImpl implements LegID, INAPAsnPrimitive {

	public static final int _ID_sendingSideID = 0;
	public static final int _ID_receivingSideID = 1;

	public static final String _PrimitiveName = "LegID";

	private byte[] sendingSideID;
	private byte[] receivingSideID;	
	
	
	public LegIDImpl() {
	}
	
	public LegIDImpl(boolean isSendingSideID, byte[] legID) {
		if (isSendingSideID)
			this.sendingSideID = legID;
		else
			this.receivingSideID = legID;
	}
	
	
	@Override
	public byte[] getSendingSideID() {
		return sendingSideID;
	}

	@Override
	public byte[] getReceivingSideID() {
		return receivingSideID;
	}

	
	@Override
	public int getTag() throws INAPException {
		if (this.sendingSideID != null) {
			return _ID_sendingSideID;
		} else {
			return _ID_receivingSideID;
		}
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_CONTEXT_SPECIFIC;
	}

	@Override
	public boolean getIsPrimitive() {
		return true;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws INAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new INAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					INAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new INAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					INAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws INAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new INAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					INAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new INAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					INAPParsingComponentExceptionReason.MistypedParameter);
		}		
	}

	private void _decode(AsnInputStream asnIS, int length) throws INAPParsingComponentException, IOException, AsnException {
		
		if (asnIS.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !asnIS.isTagPrimitive())
			throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tag class or is not primitive: TagClass=" + asnIS.getTagClass(),
					INAPParsingComponentExceptionReason.MistypedParameter);

		switch (asnIS.getTag()) {
		case _ID_sendingSideID:
			this.sendingSideID = asnIS.readOctetStringData(length);
			if (this.sendingSideID.length != 1)
				throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName + ": sendingSideID length must be 1 but it equals "
						+ this.sendingSideID.length, INAPParsingComponentExceptionReason.MistypedParameter);
			break;
		case _ID_receivingSideID:
			this.receivingSideID = asnIS.readOctetStringData(length);
			if (this.receivingSideID.length != 1)
				throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName + ": receivingSideID length must be 1 but it equals "
						+ this.receivingSideID.length, INAPParsingComponentExceptionReason.MistypedParameter);
			break;
		default:
			throw new INAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tag : tag=" + asnIS.getTag(),
					INAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws INAPException {
		
		this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws INAPException {
		
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new INAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws INAPException {

		if (this.sendingSideID == null && this.receivingSideID == null || this.sendingSideID != null && this.receivingSideID != null)
			throw new INAPException("Error while encoding the " + _PrimitiveName + ": one of sendingSideID or receivingSideID (not both) nust not be empty");

		if (this.sendingSideID != null && this.sendingSideID.length != 1 || this.receivingSideID != null && this.receivingSideID.length != 1)
			throw new INAPException("Error while encoding the " + _PrimitiveName + ": data field length must equal 4");

		if (this.sendingSideID != null)
			asnOs.writeOctetStringData(sendingSideID);
		else
			asnOs.writeOctetStringData(receivingSideID);
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("LegID [");
		if (this.sendingSideID != null && this.sendingSideID.length > 0) {
			sb.append("sendingSideID=");
			sb.append(sendingSideID[0]);
		}
		if (this.receivingSideID != null && this.receivingSideID.length > 0) {
			sb.append("receivingSideID=");
			sb.append(receivingSideID[0]);
		}
		sb.append("]");
		
		return sb.toString();
	}
}
