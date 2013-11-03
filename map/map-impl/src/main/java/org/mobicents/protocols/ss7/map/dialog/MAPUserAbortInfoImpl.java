/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * MAP-UserAbortInfo ::= SEQUENCE { map-UserAbortChoice MAP-UserAbortChoice, ... extensionContainer ExtensionContainer OPTIONAL
 * }
 *
 * MAP-UserAbortChoice ::= CHOICE { userSpecificReason [0] NULL, userResourceLimitation [1] NULL, resourceUnavailable [2]
 * ResourceUnavailableReason, applicationProcedureCancellation [3] ProcedureCancellationReason}
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPUserAbortInfoImpl implements MAPAsnPrimitive {

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

    public int getTag() throws MAPException {
        return MAP_USER_ABORT_INFO_TAG;
    }

    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPUserAbortInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPUserAbortInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding MAPUserAbortInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPUserAbortInfo: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {

        // MAP-UserAbortInfo ::= SEQUENCE {
        // map-UserAbortChoice CHOICE {
        // userSpecificReason [0] IMPLICIT NULL,
        // userResourceLimitation [1] IMPLICIT NULL,
        // resourceUnavailable [2] IMPLICIT ENUMERATED {
        // shortTermResourceLimitation ( 0 ),
        // longTermResourceLimitation ( 1 ) },
        // applicationProcedureCancellation [3] IMPLICIT ENUMERATED {
        // handoverCancellation ( 0 ),
        // radioChannelRelease ( 1 ),
        // networkPathRelease ( 2 ),
        // callRelease ( 3 ),
        // associatedProcedureFailure ( 4 ),
        // tandemDialogueRelease ( 5 ),
        // remoteOperationsFailure ( 6 ) }},
        // ... ,
        // extensionContainer SEQUENCE {
        // privateExtensionList [0] IMPLICIT SEQUENCE ( SIZE( 1 .. 10 ) ) OF
        // SEQUENCE {
        // extId MAP-EXTENSION .&extensionId ( {
        // ,
        // ...} ) ,
        // extType MAP-EXTENSION .&ExtensionType ( {
        // ,
        // ...} { @extId } ) OPTIONAL} OPTIONAL,
        // pcs-Extensions [1] IMPLICIT SEQUENCE {
        // ... } OPTIONAL,
        // ... } OPTIONAL}

        this.mapUserAbortChoice = null;
        this.extensionContainer = null;

        AsnInputStream localAis = ais.readSequenceStreamData(length);

        int tag = localAis.readTag();

        if (localAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !localAis.isTagPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding MAPUserAbortInfo.map-UserAbortChoice: bad tag class or not primitive element",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        MAPUserAbortChoiceImpl usAbrtChoice = new MAPUserAbortChoiceImpl();
        switch (tag) {
            case MAPUserAbortChoiceImpl.USER_SPECIFIC_REASON_TAG:
                try {
                    localAis.readNull();
                } catch (Exception e) {
                    // we ignore ASN bad syntax
                }
                usAbrtChoice.setUserSpecificReason();
                this.setMAPUserAbortChoice(usAbrtChoice);
                break;

            case MAPUserAbortChoiceImpl.USER_RESOURCE_LIMITATION_TAG:
                try {
                    localAis.readNull();
                } catch (Exception e) {
                    // we ignore ASN bad syntax
                }
                usAbrtChoice.setUserResourceLimitation();
                this.setMAPUserAbortChoice(usAbrtChoice);
                break;

            case MAPUserAbortChoiceImpl.RESOURCE_UNAVAILABLE:
                int code = (int) localAis.readInteger();
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
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(localAis);
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
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, MAP_USER_ABORT_INFO_TAG);
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPUserAbortInfo: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOS) throws MAPException {

        if (this.mapUserAbortChoice == null)
            throw new MAPException("Error encoding MAPUserAbortInfo: UserSpecificReason must not be null");

        try {
            if (this.mapUserAbortChoice.isUserSpecificReason()) {
                asnOS.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, MAPUserAbortChoiceImpl.USER_SPECIFIC_REASON_TAG);
            } else if (this.mapUserAbortChoice.isUserResourceLimitation()) {
                asnOS.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, MAPUserAbortChoiceImpl.USER_RESOURCE_LIMITATION_TAG);
            } else if (this.mapUserAbortChoice.isResourceUnavailableReason()) {
                ResourceUnavailableReason rur = this.mapUserAbortChoice.getResourceUnavailableReason();
                if (rur == null)
                    throw new MAPException("Error encoding MAPUserAbortInfo: ResourceUnavailableReason value must not be null");
                asnOS.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, MAPUserAbortChoiceImpl.RESOURCE_UNAVAILABLE, rur.getCode());
            } else if (this.mapUserAbortChoice.isProcedureCancellationReason()) {
                ProcedureCancellationReason pcr = this.mapUserAbortChoice.getProcedureCancellationReason();
                if (pcr == null)
                    throw new MAPException(
                            "Error encoding MAPUserAbortInfo: ProcedureCancellationReason value must not be null");
                asnOS.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, MAPUserAbortChoiceImpl.APPLICATION_PROCEDURE_CANCELLATION,
                        pcr.getCode());
            }

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOS);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding MAPUserAbortInfo: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPUserAbortInfo: " + e.getMessage(), e);
        }
    }
}
