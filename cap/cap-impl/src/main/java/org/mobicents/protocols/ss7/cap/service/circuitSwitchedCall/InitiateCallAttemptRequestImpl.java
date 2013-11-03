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
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.isup.CallingPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;

/**
 *
 * @author Povilas Jurna
 *
 */
public class InitiateCallAttemptRequestImpl extends CircuitSwitchedCallMessageImpl implements
        InitiateCallAttemptRequest {

    public static final int _ID_destinationRoutingAddress = 0;
    public static final int _ID_extensions = 4;
    public static final int _ID_legToBeCreated = 5;
    public static final int _ID_newCallSegment = 6;
    public static final int _ID_callingPartyNumber = 30;
    public static final int _ID_callReferenceNumber = 51;
    public static final int _ID_gsmSCFAddress = 52;
    public static final int _ID_suppressTCsi = 53;

    private static final String DESTINATION_ROUTING_ADDRESS = "destinationRoutingAddress";
    private static final String EXTENSIONS = "extensions";
    private static final String LEG_TO_BECREATED = "legToBeCreated";
    private static final String NEW_CALL_SEGMENT = "newCallSegment";
    private static final String CALLING_PARTY_NUMBER = "callingPartyNumber";
    private static final String CALL_REFERENCE_NUMBER = "callReferenceNumber";
    private static final String GSM_SCF_ADDRESS = "gsmSCFAddress";
    private static final String SUPPRESS_TCSI = "suppressTCsi";

    public static final String _PrimitiveName = "InitiateCallAttemptIndication";

    private DestinationRoutingAddress destinationRoutingAddress;
    private CAPExtensions extensions;
    private LegID legToBeCreated;
    private Integer newCallSegment;
    private CallingPartyNumberCap callingPartyNumber;
    private CallReferenceNumber callReferenceNumber;
    private ISDNAddressString gsmSCFAddress;
    private boolean suppressTCsi;

    public InitiateCallAttemptRequestImpl() {
    }

    public InitiateCallAttemptRequestImpl(DestinationRoutingAddress destinationRoutingAddress,
            CAPExtensions extensions, LegID legToBeCreated, Integer newCallSegment,
            CallingPartyNumberCap callingPartyNumber, CallReferenceNumber callReferenceNumber,
            ISDNAddressString gsmSCFAddress, boolean suppressTCsi) {
        this.destinationRoutingAddress = destinationRoutingAddress;
        this.extensions = extensions;
        this.legToBeCreated = legToBeCreated;
        this.newCallSegment = newCallSegment;
        this.callingPartyNumber = callingPartyNumber;
        this.callReferenceNumber = callReferenceNumber;
        this.gsmSCFAddress = gsmSCFAddress;
        this.suppressTCsi = suppressTCsi;
    }

    public CAPMessageType getMessageType() {
        return CAPMessageType.initiateCallAttempt_Request;
    }

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

        this.destinationRoutingAddress = null;
        this.extensions = null;
        this.legToBeCreated = null;
        this.newCallSegment = null;
        this.callingPartyNumber = null;
        this.callReferenceNumber = null;
        this.gsmSCFAddress = null;
        this.suppressTCsi = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_destinationRoutingAddress:
                    this.destinationRoutingAddress = new DestinationRoutingAddressImpl();
                    ((DestinationRoutingAddressImpl) this.destinationRoutingAddress).decodeAll(ais);
                    break;
                case _ID_extensions:
                    this.extensions = new CAPExtensionsImpl();
                    ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
                    break;
                case _ID_legToBeCreated:
                    this.legToBeCreated = new LegIDImpl();
                    AsnInputStream ais2 = ais.readSequenceStream();
                    ais2.readTag();
                    ((LegIDImpl) this.legToBeCreated).decodeAll(ais2);
                    break;
                case _ID_newCallSegment:
                    this.newCallSegment = (int) ais.readInteger();
                    break;
                case _ID_callingPartyNumber:
                    this.callingPartyNumber = new CallingPartyNumberCapImpl();
                    ((CallingPartyNumberCapImpl) this.callingPartyNumber).decodeAll(ais);
                    break;
                case _ID_callReferenceNumber:
                    this.callReferenceNumber = new CallReferenceNumberImpl();
                    ((CallReferenceNumberImpl) this.callReferenceNumber).decodeAll(ais);
                    break;
                case _ID_gsmSCFAddress:
                    this.gsmSCFAddress = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.gsmSCFAddress).decodeAll(ais);
                    break;
                case _ID_suppressTCsi:
                    ais.readNull();
                    this.suppressTCsi = true;
                    break;
                default:
                    ais.advanceElement();
                    break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.destinationRoutingAddress == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": destinationRoutingAddress is mandatory but not found ",
                    CAPParsingComponentExceptionReason.MistypedParameter);

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

        if (this.destinationRoutingAddress == null)
            throw new CAPException("Error while encoding " + _PrimitiveName
                    + ": destinationRoutingAddress must not be null");

        try {
            ((DestinationRoutingAddressImpl) this.destinationRoutingAddress).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_destinationRoutingAddress);

            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);
            if (this.legToBeCreated != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, this.getIsPrimitive(), _ID_legToBeCreated);
                int pos = aos.StartContentDefiniteLength();
                ((LegIDImpl) this.legToBeCreated).encodeAll(aos);
                aos.FinalizeContent(pos);
            }
            if (this.newCallSegment != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_newCallSegment, this.newCallSegment);
            if (this.callingPartyNumber != null)
                ((CallingPartyNumberCapImpl) this.callingPartyNumber).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_callingPartyNumber);
            if (this.callReferenceNumber != null)
                ((CallReferenceNumberImpl) this.callReferenceNumber).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_callReferenceNumber);
            if (this.gsmSCFAddress != null)
                ((ISDNAddressStringImpl) this.gsmSCFAddress).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_gsmSCFAddress);
            if (this.suppressTCsi)
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_suppressTCsi);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (INAPException e) {
            throw new CAPException("INAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.destinationRoutingAddress != null) {
            sb.append("destinationRoutingAddress=");
            sb.append(destinationRoutingAddress.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.legToBeCreated != null) {
            sb.append("legToBeCreated=");
            sb.append(legToBeCreated.toString());
        }
        if (this.newCallSegment != null) {
            sb.append(", newCallSegment=");
            sb.append(newCallSegment);
        }
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber);
        }
        if (this.callReferenceNumber != null) {
            sb.append(", callReferenceNumber=");
            sb.append(callReferenceNumber);
        }
        if (this.gsmSCFAddress != null) {
            sb.append(", gsmSCFAddress=");
            sb.append(gsmSCFAddress);
        }
        if (this.suppressTCsi) {
            sb.append(", suppressTCsi=");
            sb.append(suppressTCsi);
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<InitiateCallAttemptRequestImpl> CONNECT_REQUEST_XML = new XMLFormat<InitiateCallAttemptRequestImpl>(
            InitiateCallAttemptRequestImpl.class) {

        public void read(javolution.xml.XMLFormat.InputElement xml,
                InitiateCallAttemptRequestImpl initiateCallAttemptRequest) throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, initiateCallAttemptRequest);

            initiateCallAttemptRequest.destinationRoutingAddress = xml.get(DESTINATION_ROUTING_ADDRESS,
                    DestinationRoutingAddressImpl.class);
            initiateCallAttemptRequest.extensions = xml.get(EXTENSIONS, CAPExtensionsImpl.class);
            initiateCallAttemptRequest.legToBeCreated = xml.get(LEG_TO_BECREATED, LegIDImpl.class);
            initiateCallAttemptRequest.newCallSegment = xml.get(NEW_CALL_SEGMENT, Integer.class);
            initiateCallAttemptRequest.callingPartyNumber = xml.get(CALLING_PARTY_NUMBER,
                    CallingPartyNumberCapImpl.class);
            initiateCallAttemptRequest.callReferenceNumber = xml.get(CALL_REFERENCE_NUMBER,
                    CallReferenceNumberImpl.class);
            initiateCallAttemptRequest.gsmSCFAddress = xml.get(GSM_SCF_ADDRESS, ISDNAddressStringImpl.class);
            Boolean bval = xml.get(SUPPRESS_TCSI, Boolean.class);
            if (bval != null)
                initiateCallAttemptRequest.suppressTCsi = bval;
        }

        public void write(InitiateCallAttemptRequestImpl initiateCallAttemptRequest,
                javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(initiateCallAttemptRequest, xml);

            if (initiateCallAttemptRequest.getDestinationRoutingAddress() != null)
                xml.add((DestinationRoutingAddressImpl) initiateCallAttemptRequest.getDestinationRoutingAddress(),
                        DESTINATION_ROUTING_ADDRESS, DestinationRoutingAddressImpl.class);
            if (initiateCallAttemptRequest.getExtensions() != null)
                xml.add((CAPExtensionsImpl) initiateCallAttemptRequest.getExtensions(), EXTENSIONS,
                        CAPExtensionsImpl.class);
            if (initiateCallAttemptRequest.getLegToBeCreated() != null)
                xml.add((LegIDImpl) initiateCallAttemptRequest.getLegToBeCreated(), LEG_TO_BECREATED, LegIDImpl.class);
            if (initiateCallAttemptRequest.getNewCallSegment() != null)
                xml.add((Integer) initiateCallAttemptRequest.getNewCallSegment(), NEW_CALL_SEGMENT, Integer.class);
            if (initiateCallAttemptRequest.getCallingPartyNumber() != null)
                xml.add((CallingPartyNumberCapImpl) initiateCallAttemptRequest.getCallingPartyNumber(),
                        CALLING_PARTY_NUMBER, CallingPartyNumberCapImpl.class);
            if (initiateCallAttemptRequest.getCallReferenceNumber() != null)
                xml.add((CallReferenceNumberImpl) initiateCallAttemptRequest.getCallReferenceNumber(),
                        CALL_REFERENCE_NUMBER, CallReferenceNumberImpl.class);
            if (initiateCallAttemptRequest.getGsmSCFAddress() != null)
                xml.add((ISDNAddressStringImpl) initiateCallAttemptRequest.getGsmSCFAddress(), GSM_SCF_ADDRESS,
                        ISDNAddressStringImpl.class);
            if (initiateCallAttemptRequest.getSuppressTCsi())
                xml.add(true, SUPPRESS_TCSI, Boolean.class);
        }
    };

    @Override
    public DestinationRoutingAddress getDestinationRoutingAddress() {
        return destinationRoutingAddress;
    }

    @Override
    public CAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public LegID getLegToBeCreated() {
        return legToBeCreated;
    }

    @Override
    public Integer getNewCallSegment() {
        return newCallSegment;
    }

    @Override
    public CallingPartyNumberCap getCallingPartyNumber() {
        return callingPartyNumber;
    }

    @Override
    public CallReferenceNumber getCallReferenceNumber() {
        return callReferenceNumber;
    }

    @Override
    public ISDNAddressString getGsmSCFAddress() {
        return gsmSCFAddress;
    }

    @Override
    public boolean getSuppressTCsi() {
        return suppressTCsi;
    }

}
