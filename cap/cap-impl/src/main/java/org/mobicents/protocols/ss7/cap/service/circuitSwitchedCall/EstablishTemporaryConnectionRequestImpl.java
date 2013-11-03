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
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.ScfID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.primitives.ScfIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class EstablishTemporaryConnectionRequestImpl extends CircuitSwitchedCallMessageImpl implements
        EstablishTemporaryConnectionRequest {

    public static final int _ID_assistingSSPIPRoutingAddress = 0;
    public static final int _ID_correlationID = 1;
    public static final int _ID_scfID = 3;
    public static final int _ID_extensions = 4;
    public static final int _ID_carrier = 5;
    public static final int _ID_serviceInteractionIndicatorsTwo = 6;
    public static final int _ID_callSegmentID = 7;
    public static final int _ID_naOliInfo = 50;
    public static final int _ID_chargeNumber = 51;
    public static final int _ID_originalCalledPartyID = 52;
    public static final int _ID_callingPartyNumber = 53;

    private static final String _PrimitiveName = "EstablishTemporaryConnectionIndication";

    private Digits assistingSSPIPRoutingAddress;
    private Digits correlationID;
    private ScfID scfID;
    private CAPExtensions extensions;
    private Carrier carrier;
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
    private Integer callSegmentID;
    private NAOliInfo naOliInfo;
    private LocationNumberCap chargeNumber;
    private OriginalCalledNumberCap originalCalledPartyID;
    private CallingPartyNumberCap callingPartyNumber;

    private boolean isCAPVersion3orLater;

    public EstablishTemporaryConnectionRequestImpl(boolean isCAPVersion3orLater) {
        this.isCAPVersion3orLater = isCAPVersion3orLater;
    }

    public EstablishTemporaryConnectionRequestImpl(Digits assistingSSPIPRoutingAddress, Digits correlationID, ScfID scfID,
            CAPExtensions extensions, Carrier carrier, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            Integer callSegmentID, NAOliInfo naOliInfo, LocationNumberCap chargeNumber,
            OriginalCalledNumberCap originalCalledPartyID, CallingPartyNumberCap callingPartyNumber,
            boolean isCAPVersion3orLater) {
        this.assistingSSPIPRoutingAddress = assistingSSPIPRoutingAddress;
        this.correlationID = correlationID;
        this.scfID = scfID;
        this.extensions = extensions;
        this.carrier = carrier;
        this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
        this.callSegmentID = callSegmentID;
        this.naOliInfo = naOliInfo;
        this.chargeNumber = chargeNumber;
        this.originalCalledPartyID = originalCalledPartyID;
        this.callingPartyNumber = callingPartyNumber;
        this.isCAPVersion3orLater = isCAPVersion3orLater;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.establishTemporaryConnection_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.establishTemporaryConnection;
    }

    @Override
    public Digits getAssistingSSPIPRoutingAddress() {
        return assistingSSPIPRoutingAddress;
    }

    @Override
    public Digits getCorrelationID() {
        return correlationID;
    }

    @Override
    public ScfID getScfID() {
        return scfID;
    }

    @Override
    public CAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public Carrier getCarrier() {
        return carrier;
    }

    @Override
    public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
        return serviceInteractionIndicatorsTwo;
    }

    @Override
    public Integer getCallSegmentID() {
        return callSegmentID;
    }

    @Override
    public NAOliInfo getNAOliInfo() {
        return naOliInfo;
    }

    @Override
    public LocationNumberCap getChargeNumber() {
        return chargeNumber;
    }

    @Override
    public OriginalCalledNumberCap getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

    @Override
    public CallingPartyNumberCap getCallingPartyNumber() {
        return callingPartyNumber;
    }

    @Override
    public int getTag() throws CAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.assistingSSPIPRoutingAddress = null;
        this.correlationID = null;
        this.scfID = null;
        this.extensions = null;
        this.carrier = null;
        this.serviceInteractionIndicatorsTwo = null;
        this.callSegmentID = null;
        this.naOliInfo = null;
        this.chargeNumber = null;
        this.originalCalledPartyID = null;
        this.callingPartyNumber = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_assistingSSPIPRoutingAddress:
                        this.assistingSSPIPRoutingAddress = new DigitsImpl();
                        ((DigitsImpl) this.assistingSSPIPRoutingAddress).decodeAll(ais);
                        this.assistingSSPIPRoutingAddress.setIsGenericNumber();
                        break;
                    case _ID_correlationID:
                        this.correlationID = new DigitsImpl();
                        ((DigitsImpl) this.correlationID).decodeAll(ais);
                        this.correlationID.setIsGenericDigits();
                        break;
                    case _ID_scfID:
                        this.scfID = new ScfIDImpl();
                        ((ScfIDImpl) this.scfID).decodeAll(ais);
                        break;
                    case _ID_extensions:
                        this.extensions = new CAPExtensionsImpl();
                        ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
                        break;
                    case _ID_carrier:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_serviceInteractionIndicatorsTwo:
                        if (this.isCAPVersion3orLater) {
                            this.serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl();
                            ((ServiceInteractionIndicatorsTwoImpl) this.serviceInteractionIndicatorsTwo).decodeAll(ais);
                        }
                        break;
                    case _ID_callSegmentID:
                        if (this.isCAPVersion3orLater) {
                            this.callSegmentID = (int) ais.readInteger();
                        } else {
                            this.serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl();
                            ((ServiceInteractionIndicatorsTwoImpl) this.serviceInteractionIndicatorsTwo).decodeAll(ais);
                        }
                        break;
                    case _ID_naOliInfo:
                        this.naOliInfo = new NAOliInfoImpl();
                        ((NAOliInfoImpl) this.naOliInfo).decodeAll(ais);
                        break;
                    case _ID_chargeNumber:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_originalCalledPartyID:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_callingPartyNumber:
                        ais.advanceElement(); // TODO: implement it
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.assistingSSPIPRoutingAddress == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter assistingSSPIPRoutingAddress is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
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

    @Override
    public void encodeData(AsnOutputStream aos) throws CAPException {

        if (this.assistingSSPIPRoutingAddress == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": assistingSSPIPRoutingAddress must not be null");

        try {
            ((DigitsImpl) this.assistingSSPIPRoutingAddress).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                    _ID_assistingSSPIPRoutingAddress);

            if (this.correlationID != null)
                ((DigitsImpl) this.correlationID).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_correlationID);
            if (this.scfID != null)
                ((ScfIDImpl) this.scfID).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_scfID);
            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);
            if (this.carrier != null) {
                // TODO: implement it - _ID_carrier
            }

            if (this.serviceInteractionIndicatorsTwo != null) {
                if (this.isCAPVersion3orLater) {
                    ((ServiceInteractionIndicatorsTwoImpl) this.serviceInteractionIndicatorsTwo).encodeAll(aos,
                            Tag.CLASS_CONTEXT_SPECIFIC, _ID_serviceInteractionIndicatorsTwo);
                } else {
                    ((ServiceInteractionIndicatorsTwoImpl) this.serviceInteractionIndicatorsTwo).encodeAll(aos,
                            Tag.CLASS_CONTEXT_SPECIFIC, _ID_callSegmentID);
                }
            }
            if (this.callSegmentID != null) {
                if (this.isCAPVersion3orLater) {
                    aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_callSegmentID, this.callSegmentID);
                }
            }

            if (this.naOliInfo != null)
                ((NAOliInfoImpl) this.naOliInfo).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_naOliInfo);
            if (this.chargeNumber != null) {
                // TODO: implement it - _ID_chargeNumber
            }
            if (this.originalCalledPartyID != null) {
                // TODO: implement it - _ID_originalCalledPartyID
            }
            if (this.callingPartyNumber != null) {
                // TODO: implement it - _ID_callingPartyNumber
            }

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.assistingSSPIPRoutingAddress != null) {
            sb.append("assistingSSPIPRoutingAddress=");
            sb.append(this.assistingSSPIPRoutingAddress.toString());
        }
        if (this.correlationID != null) {
            sb.append(", correlationID=");
            sb.append(correlationID.toString());
        }
        if (this.scfID != null) {
            sb.append(", scfID=");
            sb.append(scfID.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier.toString());
        }
        if (this.serviceInteractionIndicatorsTwo != null) {
            sb.append(", serviceInteractionIndicatorsTwo=");
            sb.append(serviceInteractionIndicatorsTwo.toString());
        }
        if (this.callSegmentID != null) {
            sb.append(", callSegmentID=");
            sb.append(callSegmentID.toString());
        }
        if (this.naOliInfo != null) {
            sb.append(", naOliInfo=");
            sb.append(naOliInfo.toString());
        }
        if (this.chargeNumber != null) {
            sb.append(", chargeNumber=");
            sb.append(chargeNumber.toString());
        }
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
