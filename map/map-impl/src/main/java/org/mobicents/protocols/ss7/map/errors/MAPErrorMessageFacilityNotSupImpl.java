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

package org.mobicents.protocols.ss7.map.errors;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorCode;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageFacilityNotSup;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPErrorMessageFacilityNotSupImpl extends MAPErrorMessageImpl implements MAPErrorMessageFacilityNotSup {

    public static final int shapeOfLocationEstimateNotSupported_TAG = 0;
    public static final int neededLcsCapabilityNotSupportedInServingNode_TAG = 1;

    private MAPExtensionContainer extensionContainer;
    private Boolean shapeOfLocationEstimateNotSupported;
    private Boolean neededLcsCapabilityNotSupportedInServingNode;

    public MAPErrorMessageFacilityNotSupImpl(MAPExtensionContainer extensionContainer,
            Boolean shapeOfLocationEstimateNotSupported, Boolean neededLcsCapabilityNotSupportedInServingNode) {
        super((long) MAPErrorCode.facilityNotSupported);

        this.extensionContainer = extensionContainer;
        this.shapeOfLocationEstimateNotSupported = shapeOfLocationEstimateNotSupported;
        this.neededLcsCapabilityNotSupportedInServingNode = neededLcsCapabilityNotSupportedInServingNode;
    }

    protected MAPErrorMessageFacilityNotSupImpl() {
        super((long) MAPErrorCode.facilityNotSupported);
    }

    public boolean isEmFacilityNotSup() {
        return true;
    }

    public MAPErrorMessageFacilityNotSup getEmFacilityNotSup() {
        return this;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public Boolean getShapeOfLocationEstimateNotSupported() {
        return this.shapeOfLocationEstimateNotSupported;
    }

    public Boolean getNeededLcsCapabilityNotSupportedInServingNode() {
        return this.neededLcsCapabilityNotSupportedInServingNode;
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setShapeOfLocationEstimateNotSupported(Boolean shapeOfLocationEstimateNotSupported) {
        this.shapeOfLocationEstimateNotSupported = shapeOfLocationEstimateNotSupported;
    }

    public void getNeededLcsCapabilityNotSupportedInServingNode(Boolean neededLcsCapabilityNotSupportedInServingNode) {
        this.neededLcsCapabilityNotSupportedInServingNode = neededLcsCapabilityNotSupportedInServingNode;
    }

    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException(
                    "IOException when decoding MAPErrorMessageFacilityNotSup: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageFacilityNotSup: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException(
                    "IOException when decoding MAPErrorMessageFacilityNotSup: " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding MAPErrorMessageFacilityNotSup: "
                    + e.getMessage(), e, MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream localAis, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.extensionContainer = null;
        this.shapeOfLocationEstimateNotSupported = null;
        this.neededLcsCapabilityNotSupportedInServingNode = null;

        if (localAis.getTagClass() != Tag.CLASS_UNIVERSAL || localAis.getTag() != Tag.SEQUENCE || localAis.isTagPrimitive())
            throw new MAPParsingComponentException(
                    "Error decoding MAPErrorMessageFacilityNotSup: bad tag class or tag or parameter is primitive or no parameter data",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        AsnInputStream ais = localAis.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_UNIVERSAL:
                    switch (tag) {
                        case Tag.SEQUENCE:
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case shapeOfLocationEstimateNotSupported_TAG:
                            ais.readNull();
                            this.shapeOfLocationEstimateNotSupported = true;
                            break;

                        case neededLcsCapabilityNotSupportedInServingNode_TAG:
                            ais.readNull();
                            this.neededLcsCapabilityNotSupportedInServingNode = true;
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                default:
                    ais.advanceElement();
                    break;
            }
        }

        if (this.shapeOfLocationEstimateNotSupported == null)
            this.shapeOfLocationEstimateNotSupported = false;
        if (this.neededLcsCapabilityNotSupportedInServingNode == null)
            this.neededLcsCapabilityNotSupportedInServingNode = false;
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, false, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPErrorMessageFacilityNotSup: " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream aos) throws MAPException {

        if (this.extensionContainer == null
                && (this.shapeOfLocationEstimateNotSupported == null || this.shapeOfLocationEstimateNotSupported == false)
                && (this.neededLcsCapabilityNotSupportedInServingNode == null || this.neededLcsCapabilityNotSupportedInServingNode == false))
            return;

        try {
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(aos);

            if (this.shapeOfLocationEstimateNotSupported != null && this.shapeOfLocationEstimateNotSupported == true)
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, shapeOfLocationEstimateNotSupported_TAG);
            if (this.neededLcsCapabilityNotSupportedInServingNode != null
                    && this.neededLcsCapabilityNotSupportedInServingNode == true)
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, neededLcsCapabilityNotSupportedInServingNode_TAG);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding MAPErrorMessageFacilityNotSup: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding MAPErrorMessageFacilityNotSup: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageFacilityNotSup [");
        if (this.extensionContainer != null)
            sb.append("extensionContainer=" + this.extensionContainer.toString());
        if (this.shapeOfLocationEstimateNotSupported != null && this.shapeOfLocationEstimateNotSupported == true)
            sb.append(", shapeOfLocationEstimateNotSupported=true");
        if (this.neededLcsCapabilityNotSupportedInServingNode != null
                && this.neededLcsCapabilityNotSupportedInServingNode == true)
            sb.append(", neededLcsCapabilityNotSupportedInServingNode=true");
        sb.append("]");

        return sb.toString();
    }
}
