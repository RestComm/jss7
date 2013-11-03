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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessageType;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptResponse;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4Functionalities;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4FunctionalitiesImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

/**
 *
 * @author Povilas Jurna
 *
 */
public class InitiateCallAttemptResponseImpl extends CircuitSwitchedCallMessageImpl implements
        InitiateCallAttemptResponse {

    public static final int _ID_supportedCamelPhases = 0;
    public static final int _ID_offeredCamel4Functionalities = 1;
    public static final int _ID_extensions = 2;
    public static final int _ID_releaseCallArgExtensionAllowed = 3;

    private static final String SUPPORTED_CAMEL_PHASES = "supportedCamelPhases";
    private static final String OFFERED_CAMEL4_FUNCTIONALITIES = "offeredCamel4Functionalities";
    private static final String EXTENSIONS = "extensions";
    private static final String RELEASE_CALL_ARG_EXTENSION_ALLOWED = "releaseCallArgExtensionAllowed";

    public static final String _PrimitiveName = "InitiateCallAttemptResponse";

    private SupportedCamelPhases supportedCamelPhases;
    private OfferedCamel4Functionalities offeredCamel4Functionalities;
    private CAPExtensions extensions;
    private boolean releaseCallArgExtensionAllowed;

    public InitiateCallAttemptResponseImpl() {
    }

    public InitiateCallAttemptResponseImpl(SupportedCamelPhases supportedCamelPhases,
            OfferedCamel4Functionalities offeredCamel4Functionalities, CAPExtensions extensions,
            boolean releaseCallArgExtensionAllowed) {
        this.supportedCamelPhases = supportedCamelPhases;
        this.offeredCamel4Functionalities = offeredCamel4Functionalities;
        this.extensions = extensions;
        this.releaseCallArgExtensionAllowed = releaseCallArgExtensionAllowed;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.initiateCallAttempt_Response;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.initiateCallAttempt;
    }

    public int getTag() throws CAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException,
            MAPParsingComponentException, INAPParsingComponentException, IOException, AsnException {

        this.supportedCamelPhases = null;
        this.offeredCamel4Functionalities = null;
        this.extensions = null;
        this.releaseCallArgExtensionAllowed = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_supportedCamelPhases:
                    this.supportedCamelPhases = new SupportedCamelPhasesImpl();
                    ((SupportedCamelPhasesImpl) this.supportedCamelPhases).decodeAll(ais);
                    break;
                case _ID_extensions:
                    this.extensions = new CAPExtensionsImpl();
                    ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
                    break;
                case _ID_offeredCamel4Functionalities:
                    this.offeredCamel4Functionalities = new OfferedCamel4FunctionalitiesImpl();
                    ((OfferedCamel4FunctionalitiesImpl) this.offeredCamel4Functionalities).decodeAll(ais);
                    break;
                case _ID_releaseCallArgExtensionAllowed:
                    ais.readNull();
                    this.releaseCallArgExtensionAllowed = true;
                    break;
                default:
                    ais.advanceElement();
                    break;
                }
            } else {
                ais.advanceElement();
            }
        }

    }

    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream aos) throws CAPException {

        try {

            if (this.supportedCamelPhases != null)
                ((SupportedCamelPhasesImpl) this.supportedCamelPhases).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_supportedCamelPhases);
            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);
            if (this.offeredCamel4Functionalities != null)
                ((OfferedCamel4FunctionalitiesImpl) this.offeredCamel4Functionalities).encodeAll(aos,
                        Tag.CLASS_CONTEXT_SPECIFIC, _ID_offeredCamel4Functionalities);
            if (this.releaseCallArgExtensionAllowed)
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_releaseCallArgExtensionAllowed);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.supportedCamelPhases != null) {
            sb.append("supportedCamelPhases=");
            sb.append(supportedCamelPhases.toString());
            sb.append(", ");
        }
        if (this.offeredCamel4Functionalities != null) {
            sb.append("offeredCamel4Functionalities=");
            sb.append(offeredCamel4Functionalities.toString());
            sb.append(", ");
        }
        if (this.extensions != null) {
            sb.append("extensions=");
            sb.append(extensions.toString());
            sb.append(", ");
        }
        if (releaseCallArgExtensionAllowed) {
            sb.append("releaseCallArgExtensionAllowed=");
            sb.append(releaseCallArgExtensionAllowed);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<InitiateCallAttemptResponseImpl> CONNECT_REQUEST_XML = new XMLFormat<InitiateCallAttemptResponseImpl>(
            InitiateCallAttemptResponseImpl.class) {

        public void read(javolution.xml.XMLFormat.InputElement xml,
                InitiateCallAttemptResponseImpl initiateCallAttemptResponse) throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, initiateCallAttemptResponse);

            initiateCallAttemptResponse.supportedCamelPhases = xml.get(SUPPORTED_CAMEL_PHASES,
                    SupportedCamelPhasesImpl.class);
            initiateCallAttemptResponse.extensions = xml.get(EXTENSIONS, CAPExtensionsImpl.class);
            initiateCallAttemptResponse.offeredCamel4Functionalities = xml.get(OFFERED_CAMEL4_FUNCTIONALITIES,
                    OfferedCamel4FunctionalitiesImpl.class);
            Boolean bval = xml.get(RELEASE_CALL_ARG_EXTENSION_ALLOWED, Boolean.class);
            if (bval != null)
                initiateCallAttemptResponse.releaseCallArgExtensionAllowed = bval;
        }

        public void write(InitiateCallAttemptResponseImpl initiateCallAttemptResponse,
                javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(initiateCallAttemptResponse, xml);

            if (initiateCallAttemptResponse.getSupportedCamelPhases() != null)
                xml.add((SupportedCamelPhasesImpl) initiateCallAttemptResponse.getSupportedCamelPhases(),
                        SUPPORTED_CAMEL_PHASES, SupportedCamelPhasesImpl.class);
            if (initiateCallAttemptResponse.getExtensions() != null)
                xml.add((CAPExtensionsImpl) initiateCallAttemptResponse.getExtensions(), EXTENSIONS,
                        CAPExtensionsImpl.class);
            if (initiateCallAttemptResponse.getOfferedCamel4Functionalities() != null)
                xml.add((OfferedCamel4FunctionalitiesImpl) initiateCallAttemptResponse.getOfferedCamel4Functionalities(), OFFERED_CAMEL4_FUNCTIONALITIES,
                        OfferedCamel4FunctionalitiesImpl.class);
            if (initiateCallAttemptResponse.getReleaseCallArgExtensionAllowed())
                xml.add(true, RELEASE_CALL_ARG_EXTENSION_ALLOWED, Boolean.class);
        }
    };

    @Override
    public SupportedCamelPhases getSupportedCamelPhases() {
        return supportedCamelPhases;
    }

    @Override
    public OfferedCamel4Functionalities getOfferedCamel4Functionalities() {
        return offeredCamel4Functionalities;
    }

    @Override
    public CAPExtensions getExtensions() {
        return extensions;
    }

    public boolean getReleaseCallArgExtensionAllowed() {
        return releaseCallArgExtensionAllowed;
    }
}
