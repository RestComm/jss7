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
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.mobicents.protocols.ss7.map.api.dialog.ResourceUnavailableReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

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
 * @author sergey vetyutnev
 * 
 */
public class MAPUserAbortInfoImpl {

	public static final int MAP_USER_ABORT_INFO_TAG = 0x04;

	protected static final int USER_ABORT_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	protected static final boolean USER_ABORT_TAG_PC_CONSTRUCTED = false;

	private MAPUserAbortChoice mapUserAbortChoice = null;
	private MAPExtensionContainer extensionContainer;

	public MAPUserAbortChoice getMAPUserAbortChoice() {
		return this.mapUserAbortChoice;
	}

	public MAPExtensionContainer getExtensionContainer() {
		return extensionContainer;
	}

	public void setMAPUserAbortChoice(MAPUserAbortChoice mapUsrAbrtChoice) {
		this.mapUserAbortChoice = mapUsrAbrtChoice;
	}

	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
		this.extensionContainer = extensionContainer;
	}

	public void decode(AsnInputStream ais) throws MAPParsingComponentException {

		// MAP-UserAbortInfo ::= SEQUENCE {
		//   map-UserAbortChoice   CHOICE {
		//	      userSpecificReason                   [0] IMPLICIT NULL, 
		//	      userResourceLimitation               [1] IMPLICIT NULL, 
		//	      resourceUnavailable                  [2] IMPLICIT ENUMERATED {
		//	         shortTermResourceLimitation    ( 0 ), 
		//	         longTermResourceLimitation     ( 1 ) }, 
		//	      applicationProcedureCancellation     [3] IMPLICIT ENUMERATED {
		//	         handoverCancellation          ( 0 ), 
		//	         radioChannelRelease           ( 1 ), 
		//	         networkPathRelease            ( 2 ), 
		//	         callRelease                   ( 3 ), 
		//	         associatedProcedureFailure    ( 4 ), 
		//	         tandemDialogueRelease         ( 5 ), 
		//	         remoteOperationsFailure       ( 6 ) }}, 
		//	   ... , 
		//	   extensionContainer    SEQUENCE {
		//	      privateExtensionList   [0] IMPLICIT SEQUENCE  ( SIZE( 1 .. 10 ) ) OF
		//	         SEQUENCE {
		//	            extId      MAP-EXTENSION .&extensionId  ( {
		//	               , 
		//	               ...} ) , 
		//	            extType    MAP-EXTENSION .&ExtensionType  ( {
		//	               , 
		//	               ...} { @extId   }  )  OPTIONAL} OPTIONAL, 
		//	      pcs-Extensions         [1] IMPLICIT SEQUENCE {
		//	         ... } OPTIONAL, 
		//	      ... } OPTIONAL}

		this.setMAPUserAbortChoice(null);
		this.setExtensionContainer(null);

		try {
			byte[] seqData = ais.readSequence();

			AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(seqData));

			int tag;
			int length;
			int seqz = 0;
			while (localAis.available() > 0) {

				tag = localAis.readTag();
				if (seqz == 0) {
					// first element must be map-ProviderAbortReason
					MAPUserAbortChoiceImpl usAbrtChoice = new MAPUserAbortChoiceImpl();
					switch (tag) {
					case MAPUserAbortChoiceImpl.USER_SPECIFIC_REASON_TAG:
						length = localAis.readLength();
						if (length != 0) {
							throw new MAPParsingComponentException("Null length should be 0 but is " + length,
									MAPParsingComponentExceptionReason.MistypedParameter);
						}
						usAbrtChoice.setUserSpecificReason();
						this.setMAPUserAbortChoice(usAbrtChoice);
						break;

					case MAPUserAbortChoiceImpl.USER_RESOURCE_LIMITATION_TAG:
						length = localAis.readLength();
						if (length != 0) {
							throw new MAPParsingComponentException("Null length should be 0 but is " + length,
									MAPParsingComponentExceptionReason.MistypedParameter);
						}
						usAbrtChoice.setUserResourceLimitation();
						this.setMAPUserAbortChoice(usAbrtChoice);
						break;

					case MAPUserAbortChoiceImpl.RESOURCE_UNAVAILABLE:

						length = localAis.readLength();

						tag = localAis.readTag();
						if (tag != Tag.ENUMERATED) {
							throw new MAPParsingComponentException("Expected ENUMERATED TAG for ResourceUnavailableReason but received " + tag,
									MAPParsingComponentExceptionReason.MistypedParameter);
						}

						length = localAis.readLength();
						if (length != 1) {
							throw new MAPParsingComponentException(
									"Expected length of MAPUserAbortChoiceImpl.RESOURCE_UNAVAILABLE to be 1 but found " + length,
									MAPParsingComponentExceptionReason.MistypedParameter);
						}

						int code = localAis.read();
						ResourceUnavailableReason resUnaReas = ResourceUnavailableReason.getInstance(code);
						usAbrtChoice.setResourceUnavailableReason(resUnaReas);
						this.setMAPUserAbortChoice(usAbrtChoice);
						break;

					case MAPUserAbortChoiceImpl.APPLICATION_PROCEDURE_CANCELLATION:

						length = localAis.readLength();

						tag = localAis.readTag();
						if (tag != Tag.ENUMERATED) {
							throw new MAPParsingComponentException("Expected ENUMERATED TAG for ResourceUnavailableReason but received " + tag,
									MAPParsingComponentExceptionReason.MistypedParameter);
						}

						length = localAis.readLength();
						if (length != 1) {
							throw new MAPParsingComponentException(
									"Expected length of MAPUserAbortChoiceImpl.APPLICATION_PROCEDURE_CANCELLATION to be 1 but found " + length,
									MAPParsingComponentExceptionReason.MistypedParameter);
						}

						int code1 = localAis.read();
						ProcedureCancellationReason procCanReasn = ProcedureCancellationReason.getInstance(code1);
						usAbrtChoice.setProcedureCancellationReason(procCanReasn);
						this.setMAPUserAbortChoice(usAbrtChoice);
						break;

					default:
						throw new MAPParsingComponentException(
								"The first element of MAP-UserAbortInfo must be the MAP-UserAbortChoice when decoding MAP-UserAbortInfo",
								MAPParsingComponentExceptionReason.MistypedParameter);
					}
				} else {

					if (tag == Tag.SEQUENCE) {
						this.extensionContainer = new MAPExtensionContainerImpl();
						byte[] buf = localAis.readSequence();
						AsnInputStream lis = new AsnInputStream(new ByteArrayInputStream(buf));
						((MAPExtensionContainerImpl) this.extensionContainer).decode(lis, localAis.getTagClass(), localAis.isTagPrimitive(), tag, buf.length);
					} else
						break;
				}

				seqz++;
			}

			if (this.getMAPUserAbortChoice() == null)
				throw new MAPParsingComponentException("The first element of MAP-UserAbortInfo must be MAP-UserAbortChoice when decoding MAP-UserAbortInfo",
						MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding MAPUserAbortInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MAPUserAbortInfo: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	public void encode(AsnOutputStream asnOS) throws MAPException {
		
		if (this.mapUserAbortChoice == null)
			throw new MAPException("MAPUserAbortInfo must contains the field UserSpecificReason - when encoding MAP-ProviderAbortInfo");

		try {
			byte[] choice = null;
			byte[] extContData = null;

			if (this.mapUserAbortChoice.isUserSpecificReason()) {
				choice = new byte[2];
				choice[0] = MAPUserAbortChoiceImpl.USER_SPECIFIC_REASON_TAG;
				choice[1] = 0x00;

			} else if (this.mapUserAbortChoice.isUserResourceLimitation()) {
				choice = new byte[2];
				choice[0] = MAPUserAbortChoiceImpl.USER_RESOURCE_LIMITATION_TAG;
				choice[1] = 0x00;

			} else if (this.mapUserAbortChoice.isResourceUnavailableReason()) {
				choice = new byte[5];
				choice[0] = MAPUserAbortChoiceImpl.RESOURCE_UNAVAILABLE;
				choice[1] = 0x03;

				// Enumerated
				choice[2] = 0x0A;
				choice[3] = 0x01;
				choice[4] = (byte) this.mapUserAbortChoice.getResourceUnavailableReason().getCode();

			} else if (this.mapUserAbortChoice.isProcedureCancellationReason()) {
				choice = new byte[5];
				choice[0] = MAPUserAbortChoiceImpl.APPLICATION_PROCEDURE_CANCELLATION;
				choice[1] = 0x03;

				// Enumerated
				choice[2] = 0x0A;
				choice[3] = 0x01;
				choice[4] = (byte) this.mapUserAbortChoice.getProcedureCancellationReason().getCode();

			} else {
				throw new MAPException("UserSpecificReason in the MAPUserAbortInfo is not filled");
			}

			AsnOutputStream localAos = new AsnOutputStream();
			if (this.extensionContainer != null) {
				((MAPExtensionContainerImpl) this.extensionContainer).encode(localAos);
				extContData = localAos.toByteArray();
			}

			localAos.reset();
			localAos.write(choice);

			if (extContData != null) {
				localAos.writeTag(Tag.CLASS_UNIVERSAL, USER_ABORT_TAG_PC_CONSTRUCTED, Tag.SEQUENCE);
				localAos.writeLength(extContData.length);
				localAos.write(extContData);
			}
			byte[] data = localAos.toByteArray();

			// Now let us write the MAP OPEN-INFO Tags
			asnOS.writeTag(USER_ABORT_TAG_CLASS, USER_ABORT_TAG_PC_CONSTRUCTED, MAP_USER_ABORT_INFO_TAG);
			asnOS.writeLength(data.length);
			asnOS.write(data);
		} catch (IOException e) {
			throw new MAPException("IOException when encoding MAPUserAbortInfo: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding MAPUserAbortInfo: " + e.getMessage(), e);
		}
	}

}
