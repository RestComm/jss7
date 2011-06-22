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

package org.mobicents.protocols.ss7.map.dialog;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;

/**
 * MAP-RefuseInfo ::= SEQUENCE {
 *   reason Reason,
 *   ...,
 *   extensionContainer               ExtensionContainer
 *   -- extensionContainer must not be used in version 2
 *   }
 * @author amit bhayani
 * 
 */
public class MAPRefuseInfoImpl implements MAPRefuseInfo {

	public static final int MAP_REFUSE_INFO_TAG = 0x03;

	protected static final int REFUSE_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	protected static final boolean REFUSE_TAG_PC_CONSTRUCTED = false;

	private MAPDialog mapDialog = null;
	private Reason reason;

	public MAPDialog getMAPDialog() {
		return this.mapDialog;
	}

	public Reason getReason() {
		return this.reason;
	}

	public void setMAPDialog(MAPDialog mapDialog) {
		this.mapDialog = mapDialog;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}

	public void decode(AsnInputStream ais) throws AsnException, IOException,
			MAPException {

		byte[] seqData = ais.readSequence();

		AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(
				seqData));

		int tag;

		while (localAis.available() > 0) {
			tag = localAis.readTag();

			if (tag == Tag.ENUMERATED) {
				int length = localAis.readLength();
				if (length != 1) {
					throw new MAPException(
							"Expected length of MAP-RefuseInfo.Reason to be 1 but found "
									+ length);
				}

				int reasonCode = localAis.read();

				this.reason = Reason.getReason(reasonCode);
			}

		}
	}

	public void encode(AsnOutputStream asnOS) throws IOException, MAPException {

		byte[] data = new byte[3];
		data[0] = Tag.ENUMERATED;
		data[1] = 0x01;
		data[2] = (byte) this.reason.getCode();

		// Now let us write the MAP OPEN-INFO Tags
		asnOS.writeTag(REFUSE_TAG_CLASS, REFUSE_TAG_PC_CONSTRUCTED,
				MAP_REFUSE_INFO_TAG);
		asnOS.writeLength(data.length);
		asnOS.write(data);
	}

}
