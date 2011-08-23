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

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
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

		this.mapUserAbortChoice = null;
		this.extensionContainer = null;

		try {
			AsnInputStream localAis = ais.readSequenceStream();

			int tag = localAis.readTag();
			
			if (localAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !localAis.isTagPrimitive())
				throw new MAPParsingComponentException("Error while decoding MAPUserAbortInfo.map-UserAbortChoice: bad tag class or not primitive element",
						MAPParsingComponentExceptionReason.MistypedParameter);
			
			MAPUserAbortChoiceImpl usAbrtChoice = new MAPUserAbortChoiceImpl();
			switch( tag ) {
			case MAPUserAbortChoiceImpl.USER_SPECIFIC_REASON_TAG:
				localAis.readNull();
				usAbrtChoice.setUserSpecificReason();
				this.setMAPUserAbortChoice(usAbrtChoice);
				break;

			case MAPUserAbortChoiceImpl.USER_RESOURCE_LIMITATION_TAG:
				localAis.readNull();
				usAbrtChoice.setUserResourceLimitation();
				this.setMAPUserAbortChoice(usAbrtChoice);
				break;

			case MAPUserAbortChoiceImpl.RESOURCE_UNAVAILABLE:
				int code = (int)localAis.readInteger();
				ResourceUnavailableReason resUnaReas = ResourceUnavailableReason.getInstance(code);
				usAbrtChoice.setResourceUnavailableReason(resUnaReas);
				this.setMAPUserAbortChoice(usAbrtChoice);
				break;

			case MAPUserAbortChoiceImpl.APPLICATION_PROCEDURE_CANCELLATION:
				code = (int) localAis.readInteger();
				ProcedureCancellationReason procCanReasn = ProcedureCancellationReason.getInstance(code);
				usAbrtChoice.setProcedureCancellationReason(procCanReasn);
				this.setMAPUserAbortChoice(usAbrtChoice);
				break;

			default:
				throw new MAPParsingComponentException("Error while decoding MAPUserAbortInfo.map-UserAbortChoice: bad tag",
						MAPParsingComponentExceptionReason.MistypedParameter);
			}

			while (localAis.available() > 0) {
				tag = localAis.readTag();

				switch (localAis.getTagClass()) {
				case Tag.CLASS_UNIVERSAL:
					switch (tag) {
					case Tag.SEQUENCE:
						this.extensionContainer = new MAPExtensionContainerImpl();
						this.extensionContainer.decodeAll(localAis);
						break;

					default:
						localAis.advanceElement();
						break;
					}
					break;
					
				default:
					localAis.advanceElement();
					break;
				}
			}
			
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
			throw new MAPException("Error encoding MAPUserAbortInfo: UserSpecificReason must not be null");

		try {
			asnOS.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, MAP_USER_ABORT_INFO_TAG);
			int pos = asnOS.StartContentDefiniteLength();
			
			if(this.mapUserAbortChoice.isUserSpecificReason()) {
				asnOS.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, MAPUserAbortChoiceImpl.USER_SPECIFIC_REASON_TAG);
			} else if(this.mapUserAbortChoice.isUserResourceLimitation()) {
				asnOS.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, MAPUserAbortChoiceImpl.USER_RESOURCE_LIMITATION_TAG);
			} else if(this.mapUserAbortChoice.isResourceUnavailableReason()) {
				ResourceUnavailableReason rur = this.mapUserAbortChoice.getResourceUnavailableReason();
				if (rur == null)
					throw new MAPException("Error encoding MAPUserAbortInfo: ResourceUnavailableReason value must not be null");
				asnOS.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, MAPUserAbortChoiceImpl.RESOURCE_UNAVAILABLE, rur.getCode());
			} else if(this.mapUserAbortChoice.isProcedureCancellationReason()) {
				ProcedureCancellationReason pcr = this.mapUserAbortChoice.getProcedureCancellationReason();
				if (pcr == null)
					throw new MAPException("Error encoding MAPUserAbortInfo: ProcedureCancellationReason value must not be null");
				asnOS.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, MAPUserAbortChoiceImpl.APPLICATION_PROCEDURE_CANCELLATION, pcr.getCode());
			}

			if (this.extensionContainer != null)
				this.extensionContainer.encodeAll(asnOS);
			
			asnOS.FinalizeContent(pos);
			
		} catch (IOException e) {
			throw new MAPException("IOException when encoding MAPUserAbortInfo: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding MAPUserAbortInfo: " + e.getMessage(), e);
		}
	}

}
