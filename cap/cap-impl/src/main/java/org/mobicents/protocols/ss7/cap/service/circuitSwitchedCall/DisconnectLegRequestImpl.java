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
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegRequest;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Povilas Jurna
 *
 */
public class DisconnectLegRequestImpl extends CircuitSwitchedCallMessageImpl implements DisconnectLegRequest {

    public static final int _ID_legToBeReleased = 0;
    public static final int _ID_releaseCause = 1;
    public static final int _ID_extensions = 2;

    private static final String LEG_TO_BE_RELEASED = "legToBeReleased";
    private static final String RELEASE_CAUSE = "releaseCause";
    private static final String EXTENSIONS = "extensions";

    public static final String _PrimitiveName = "DisconnectlegRequestIndication";

    private LegID legToBeReleased;
    private CauseCap releaseCause;
    private CAPExtensions extensions;

    public DisconnectLegRequestImpl() {
    }

    public DisconnectLegRequestImpl(LegID legToBeReleased, CauseCap releaseCause, CAPExtensions extensions) {
        this.legToBeReleased = legToBeReleased;
        this.releaseCause = releaseCause;
        this.extensions = extensions;
    }

    public CAPMessageType getMessageType() {
        return CAPMessageType.disconnectLeg_Request;
    }

    public int getOperationCode() {
        return CAPOperationCode.disconnectLeg;
    }

    public CAPExtensions getExtensions() {
        return extensions;
    }

    public LegID getLegToBeReleased() {
        return legToBeReleased;
    }

    public CauseCap getReleaseCause() {
        return releaseCause;
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

        this.legToBeReleased = null;
        this.releaseCause = null;
        this.extensions = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_legToBeReleased:
                    this.legToBeReleased = new LegIDImpl();
                    AsnInputStream ais2 = ais.readSequenceStream();
                    ais2.readTag();
                    ((LegIDImpl) this.legToBeReleased).decodeAll(ais2);
                    break;
                case _ID_releaseCause:
                    this.releaseCause = new CauseCapImpl();
                    ((CauseCapImpl) this.releaseCause).decodeAll(ais);
                    break;
                case _ID_extensions:
                    this.extensions = new CAPExtensionsImpl();
                    ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
                    break;
                default:
                    ais.advanceElement();
                    break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.legToBeReleased == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": legToBeReleased is mandatory but not found ",
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

        if (this.legToBeReleased == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": legToBeReleased must not be null");

        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_legToBeReleased);
            int pos = aos.StartContentDefiniteLength();
            ((LegIDImpl) this.legToBeReleased).encodeAll(aos);
            aos.FinalizeContent(pos);

            if (this.releaseCause != null)
                ((CauseCapImpl) this.releaseCause).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_releaseCause);

            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);

        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (INAPException e) {
            throw new CAPException("INAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.legToBeReleased != null) {
            sb.append("legToBeReleased=");
            sb.append(legToBeReleased.toString());
        }
        if (this.releaseCause != null) {
            sb.append(", releaseCause=");
            sb.append(releaseCause.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<DisconnectLegRequestImpl> CONNECT_REQUEST_XML = new XMLFormat<DisconnectLegRequestImpl>(
            DisconnectLegRequestImpl.class) {

        public void read(javolution.xml.XMLFormat.InputElement xml, DisconnectLegRequestImpl disconnectLegRequest)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, disconnectLegRequest);

            disconnectLegRequest.legToBeReleased = xml.get(LEG_TO_BE_RELEASED, LegIDImpl.class);
            disconnectLegRequest.releaseCause = xml.get(RELEASE_CAUSE, CauseCapImpl.class);
            disconnectLegRequest.extensions = xml.get(EXTENSIONS, CAPExtensionsImpl.class);

        }

        public void write(DisconnectLegRequestImpl disconnectLegRequest, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(disconnectLegRequest, xml);

            if (disconnectLegRequest.getLegToBeReleased() != null)
                xml.add((LegIDImpl) disconnectLegRequest.getLegToBeReleased(), LEG_TO_BE_RELEASED, LegIDImpl.class);
            if (disconnectLegRequest.getReleaseCause() != null)
                xml.add((CauseCapImpl) disconnectLegRequest.getReleaseCause(), RELEASE_CAUSE, CauseCapImpl.class);
            if (disconnectLegRequest.getExtensions() != null)
                xml.add((CAPExtensionsImpl) disconnectLegRequest.getExtensions(), EXTENSIONS, CAPExtensionsImpl.class);
        }
    };

}
