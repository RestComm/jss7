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
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortInfo;
import org.mobicents.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.mobicents.protocols.ss7.map.api.dialog.ResourceUnavailableReason;

/**
 * MAP-UserAbortInfo ::= SEQUENCE { map-UserAbortChoice MAP-UserAbortChoice, ...
 * extensionContainer ExtensionContainer OPTIONAL }
 * 
 * MAP-UserAbortChoice ::= CHOICE { userSpecificReason [0] NULL,
 * userResourceLimitation [1] NULL, resourceUnavailable [2]
 * ResourceUnavailableReason, applicationProcedureCancellation [3]
 * ProcedureCancellationReason}
 * 
 * @author amit bhayani
 * 
 */
public class MAPUserAbortInfoImpl implements MAPUserAbortInfo {

	protected static final int MAP_USER_ABORT_INFO_TAG = 0x04;

	protected static final int USER_ABORT_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	protected static final boolean USER_ABORT_TAG_PC_CONSTRUCTED = false;

	private MAPDialog mapDialog = null;
	private MAPUserAbortChoice mapUserAbortChoice = null;

	public MAPDialog getMAPDialog() {
		return this.mapDialog;
	}

	public MAPUserAbortChoice getMAPUserAbortChoice() {
		return this.mapUserAbortChoice;
	}

	public void setMAPDialog(MAPDialog mapDialog) {
		this.mapDialog = mapDialog;
	}

	public void setMAPUserAbortChoice(MAPUserAbortChoice mapUsrAbrtChoice) {
		this.mapUserAbortChoice = mapUsrAbrtChoice;
	}

	public void decode(AsnInputStream ais) throws AsnException, IOException,
			MAPException {

		byte[] seqData = ais.readSequence();

		AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(
				seqData));

		int tag;
		int length;
		while (localAis.available() > 0) {
			tag = localAis.readTag();
			MAPUserAbortChoiceImpl usAbrtChoice = new MAPUserAbortChoiceImpl();
			switch (tag) {
			case MAPUserAbortChoiceImpl.USER_SPECIFIC_REASON_TAG:
				length = localAis.readLength();
				if (length != 0) {
					throw new AsnException("Null length should be 0 but is "
							+ length);
				}
				usAbrtChoice.setUserSpecificReason();
				break;
			case MAPUserAbortChoiceImpl.USER_RESOURCE_LIMITATION_TAG:
				length = localAis.readLength();
				if (length != 0) {
					throw new AsnException("Null length should be 0 but is "
							+ length);
				}
				usAbrtChoice.setUserResourceLimitation();
				break;
			case MAPUserAbortChoiceImpl.RESOURCE_UNAVAILABLE:
				
				length = localAis.readLength();
				
				tag = localAis.readTag();
				if (tag != Tag.ENUMERATED) {
					throw new AsnException(
							"Expected ENUMERATED TAG for ResourceUnavailableReason but received "
									+ tag);
				}

				length = localAis.readLength();
				if (length != 1) {
					throw new MAPException(
							"Expected length of MAPUserAbortChoiceImpl.RESOURCE_UNAVAILABLE to be 1 but found "
									+ length);
				}

				int code = localAis.read();
				ResourceUnavailableReason resUnaReas = ResourceUnavailableReason
						.getInstance(code);
				usAbrtChoice.setResourceUnavailableReason(resUnaReas);
				break;
			case MAPUserAbortChoiceImpl.APPLICATION_PROCEDURE_CANCELLATION:
				
				length = localAis.readLength();
				
				tag = localAis.readTag();
				if (tag != Tag.ENUMERATED) {
					throw new AsnException(
							"Expected ENUMERATED TAG for ResourceUnavailableReason but received "
									+ tag);
				}

				length = localAis.readLength();
				if (length != 1) {
					throw new MAPException(
							"Expected length of MAPUserAbortChoiceImpl.APPLICATION_PROCEDURE_CANCELLATION to be 1 but found "
									+ length);
				}

				int code1 = localAis.read();
				ProcedureCancellationReason procCanReasn = ProcedureCancellationReason
						.getInstance(code1);
				usAbrtChoice.setProcedureCancellationReason(procCanReasn);

				break;
			}
			
			this.setMAPUserAbortChoice(usAbrtChoice);
		}

	}

	public void encode(AsnOutputStream asnOS) throws IOException, MAPException {
		byte[] data = null;
		if (this.mapUserAbortChoice.isUserSpecificReason()) {
			data = new byte[2];
			data[0] = MAPUserAbortChoiceImpl.USER_SPECIFIC_REASON_TAG;
			data[1] = 0x00;

		} else if (this.mapUserAbortChoice.isUserResourceLimitation()) {
			data = new byte[2];
			data[0] = MAPUserAbortChoiceImpl.USER_RESOURCE_LIMITATION_TAG;
			data[1] = 0x00;

		} else if (this.mapUserAbortChoice.isResourceUnavailableReason()) {
			data = new byte[5];
			data[0] = MAPUserAbortChoiceImpl.RESOURCE_UNAVAILABLE;
			data[1] = 0x03;

			// Enumerated
			data[2] = 0x0A;
			data[3] = 0x01;
			data[4] = (byte) this.mapUserAbortChoice
					.getResourceUnavailableReason().getCode();

		} else if (this.mapUserAbortChoice.isProcedureCancellationReason()) {
			data = new byte[5];
			data[0] = MAPUserAbortChoiceImpl.APPLICATION_PROCEDURE_CANCELLATION;
			data[1] = 0x03;

			// Enumerated
			data[2] = 0x0A;
			data[3] = 0x01;
			data[4] = (byte) this.mapUserAbortChoice
					.getProcedureCancellationReason().getCode();

		}

		// Now let us write the MAP OPEN-INFO Tags
		asnOS.writeTag(USER_ABORT_TAG_CLASS, USER_ABORT_TAG_PC_CONSTRUCTED,
				MAP_USER_ABORT_INFO_TAG);
		asnOS.writeLength(data.length);
		asnOS.write(data);
	}

}
