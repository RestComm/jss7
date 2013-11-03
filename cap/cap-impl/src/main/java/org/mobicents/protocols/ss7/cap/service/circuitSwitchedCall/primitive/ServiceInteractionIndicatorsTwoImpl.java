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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BackwardServiceInteractionInd;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ConnectedNumberTreatmentInd;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CwTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EctTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ForwardServiceInteractionInd;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.HoldTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.inap.api.primitives.BothwayThroughConnectionInd;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ServiceInteractionIndicatorsTwoImpl implements ServiceInteractionIndicatorsTwo, CAPAsnPrimitive {

    public static final int _ID_forwardServiceInteractionInd = 0;
    public static final int _ID_backwardServiceInteractionInd = 1;
    public static final int _ID_bothwayThroughConnectionInd = 2;
    public static final int _ID_connectedNumberTreatmentInd = 4;
    public static final int _ID_nonCUGCall = 13;
    public static final int _ID_holdTreatmentIndicator = 50;
    public static final int _ID_cwTreatmentIndicator = 51;
    public static final int _ID_ectTreatmentIndicator = 52;

    private static final String FORWARD_SERVICE_INTERACTION_IND = "forwardServiceInteractionInd";
    private static final String BACKWARD_SERVICE_INTERACTION_IND = "backwardServiceInteractionInd";
    private static final String BOTHWAY_THROUGH_CONNECTION_IND = "bothwayThroughConnectionInd";

    public static final String _PrimitiveName = "ServiceInteractionIndicatorsTwo";

    private ForwardServiceInteractionInd forwardServiceInteractionInd;
    private BackwardServiceInteractionInd backwardServiceInteractionInd;
    private BothwayThroughConnectionInd bothwayThroughConnectionInd;
    private ConnectedNumberTreatmentInd connectedNumberTreatmentInd;
    private boolean nonCUGCall;
    private HoldTreatmentIndicator holdTreatmentIndicator;
    private CwTreatmentIndicator cwTreatmentIndicator;
    private EctTreatmentIndicator ectTreatmentIndicator;

    public ServiceInteractionIndicatorsTwoImpl() {
    }

    public ServiceInteractionIndicatorsTwoImpl(ForwardServiceInteractionInd forwardServiceInteractionInd,
            BackwardServiceInteractionInd backwardServiceInteractionInd,
            BothwayThroughConnectionInd bothwayThroughConnectionInd, ConnectedNumberTreatmentInd connectedNumberTreatmentInd,
            boolean nonCUGCall, HoldTreatmentIndicator holdTreatmentIndicator, CwTreatmentIndicator cwTreatmentIndicator,
            EctTreatmentIndicator ectTreatmentIndicator) {
        this.forwardServiceInteractionInd = forwardServiceInteractionInd;
        this.backwardServiceInteractionInd = backwardServiceInteractionInd;
        this.bothwayThroughConnectionInd = bothwayThroughConnectionInd;
        this.connectedNumberTreatmentInd = connectedNumberTreatmentInd;
        this.nonCUGCall = nonCUGCall;
        this.holdTreatmentIndicator = holdTreatmentIndicator;
        this.cwTreatmentIndicator = cwTreatmentIndicator;
        this.ectTreatmentIndicator = ectTreatmentIndicator;
    }

    @Override
    public ForwardServiceInteractionInd getForwardServiceInteractionInd() {
        return forwardServiceInteractionInd;
    }

    @Override
    public BackwardServiceInteractionInd getBackwardServiceInteractionInd() {
        return backwardServiceInteractionInd;
    }

    @Override
    public BothwayThroughConnectionInd getBothwayThroughConnectionInd() {
        return bothwayThroughConnectionInd;
    }

    @Override
    public ConnectedNumberTreatmentInd getConnectedNumberTreatmentInd() {
        return connectedNumberTreatmentInd;
    }

    @Override
    public boolean getNonCUGCall() {
        return nonCUGCall;
    }

    @Override
    public HoldTreatmentIndicator getHoldTreatmentIndicator() {
        return holdTreatmentIndicator;
    }

    @Override
    public CwTreatmentIndicator getCwTreatmentIndicator() {
        return cwTreatmentIndicator;
    }

    @Override
    public EctTreatmentIndicator getEctTreatmentIndicator() {
        return ectTreatmentIndicator;
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

        this.forwardServiceInteractionInd = null;
        this.backwardServiceInteractionInd = null;
        this.bothwayThroughConnectionInd = null;
        this.connectedNumberTreatmentInd = null;
        this.nonCUGCall = false;
        this.holdTreatmentIndicator = null;
        this.cwTreatmentIndicator = null;
        this.ectTreatmentIndicator = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_forwardServiceInteractionInd:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_backwardServiceInteractionInd:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_bothwayThroughConnectionInd:
                        int i1 = (int) ais.readInteger();
                        this.bothwayThroughConnectionInd = BothwayThroughConnectionInd.getInstance(i1);
                        break;
                    case _ID_connectedNumberTreatmentInd:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_nonCUGCall:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_holdTreatmentIndicator:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_cwTreatmentIndicator:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_ectTreatmentIndicator:
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

        try {

            if (this.forwardServiceInteractionInd != null) {
                // TODO: implement it
            }
            if (this.backwardServiceInteractionInd != null) {
                // TODO: implement it
            }
            if (this.bothwayThroughConnectionInd != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_bothwayThroughConnectionInd,
                        this.bothwayThroughConnectionInd.getCode());
            }
            if (this.connectedNumberTreatmentInd != null) {
                // TODO: implement it
            }
            if (this.nonCUGCall) {
                // TODO: implement it
            }
            if (this.holdTreatmentIndicator != null) {
                // TODO: implement it
            }
            if (this.cwTreatmentIndicator != null) {
                // TODO: implement it
            }
            if (this.ectTreatmentIndicator != null) {
                // TODO: implement it
            }
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ServiceInteractionIndicatorsTwoImpl> SERVICE_INTERACTION_INDICATORS_TWO_XML = new XMLFormat<ServiceInteractionIndicatorsTwoImpl>(
            ServiceInteractionIndicatorsTwoImpl.class) {

        public void read(javolution.xml.XMLFormat.InputElement xml,
                ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo) throws XMLStreamException {

            String vals = xml.get(BOTHWAY_THROUGH_CONNECTION_IND, String.class);
            if (vals != null) {
                try {
                    serviceInteractionIndicatorsTwo.bothwayThroughConnectionInd = Enum.valueOf(BothwayThroughConnectionInd.class, vals);
                } catch (Exception e) {
                }
            }

            // TODO: implement all methods
        }

        public void write(ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo,
                javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {

            if (serviceInteractionIndicatorsTwo.bothwayThroughConnectionInd != null)
                xml.add(serviceInteractionIndicatorsTwo.bothwayThroughConnectionInd.toString(), BOTHWAY_THROUGH_CONNECTION_IND, String.class);

            // TODO: implement all methods
        }
    };

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.forwardServiceInteractionInd != null) {
            sb.append("forwardServiceInteractionInd=");
            sb.append(forwardServiceInteractionInd.toString());
        }
        if (this.backwardServiceInteractionInd != null) {
            sb.append(", backwardServiceInteractionInd=");
            sb.append(backwardServiceInteractionInd.toString());
        }
        if (this.bothwayThroughConnectionInd != null) {
            sb.append(", bothwayThroughConnectionInd=");
            sb.append(bothwayThroughConnectionInd.toString());
        }
        if (this.connectedNumberTreatmentInd != null) {
            sb.append(", connectedNumberTreatmentInd=");
            sb.append(connectedNumberTreatmentInd.toString());
        }
        if (this.nonCUGCall) {
            sb.append(", nonCUGCall");
        }
        if (this.holdTreatmentIndicator != null) {
            sb.append(", holdTreatmentIndicator=");
            sb.append(holdTreatmentIndicator.toString());
        }
        if (this.cwTreatmentIndicator != null) {
            sb.append(", cwTreatmentIndicator=");
            sb.append(cwTreatmentIndicator.toString());
        }
        if (this.ectTreatmentIndicator != null) {
            sb.append(", ectTreatmentIndicator=");
            sb.append(ectTreatmentIndicator.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
