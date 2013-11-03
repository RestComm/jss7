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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NotReachableReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SubscriberStateImpl implements SubscriberState, MAPAsnPrimitive {

    public static final int _ID_assumedIdle = 0;
    public static final int _ID_camelBusy = 1;
    public static final int _ID_notProvidedFromVLR = 2;

    private static final String SUBSCRIBER_STATE_CHOICE = "subscriberStateChoice";
    private static final String NOT_REACHABLE_REASON = "notReachableReason";

    private static final String DEFAULT_STRING_VALUE = null;

    public static final String _PrimitiveName = "SubscriberState";

    private SubscriberStateChoice subscriberStateChoice;
    private NotReachableReason notReachableReason;

    public SubscriberStateImpl() {
    }

    public SubscriberStateImpl(SubscriberStateChoice subscriberStateChoice, NotReachableReason notReachableReason) {
        setData(subscriberStateChoice, notReachableReason);
    }

    public void setData(SubscriberStateChoice subscriberStateChoice, NotReachableReason notReachableReason) {
        this.subscriberStateChoice = subscriberStateChoice;
        this.notReachableReason = notReachableReason;
    }

    public SubscriberStateChoice getSubscriberStateChoice() {
        return subscriberStateChoice;
    }

    public NotReachableReason getNotReachableReason() {
        return notReachableReason;
    }

    public int getTag() throws MAPException {

        if (this.subscriberStateChoice == null)
            throw new MAPException("Error encoding " + _PrimitiveName + ": No subscriberStateChoice value");

        switch (this.subscriberStateChoice) {
            case assumedIdle:
                return _ID_assumedIdle;
            case camelBusy:
                return _ID_camelBusy;
            case netDetNotReachable:
                return Tag.ENUMERATED;
            case notProvidedFromVLR:
                return _ID_notProvidedFromVLR;
        }

        throw new MAPException("Error encoding " + _PrimitiveName + ": Bad subscriberStateChoice value");
    }

    public int getTagClass() {
        if (this.subscriberStateChoice != null && this.subscriberStateChoice == SubscriberStateChoice.netDetNotReachable)
            return Tag.CLASS_UNIVERSAL;
        else
            return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    public boolean getIsPrimitive() {
        return true;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.subscriberStateChoice = null;
        this.notReachableReason = null;

        int tag = ais.getTag();

        switch (ais.getTagClass()) {
            case Tag.CLASS_UNIVERSAL:
                if (tag == Tag.ENUMERATED) {
                    this.subscriberStateChoice = SubscriberStateChoice.netDetNotReachable;
                    int i1 = (int) ais.readIntegerData(length);
                    this.notReachableReason = NotReachableReason.getInstance(i1);
                } else {
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                            + ": bad choice tag for universal tag class: " + tag,
                            MAPParsingComponentExceptionReason.MistypedParameter);
                }
                break;
            case Tag.CLASS_CONTEXT_SPECIFIC:
                switch (tag) {
                    case _ID_assumedIdle:
                        this.subscriberStateChoice = SubscriberStateChoice.assumedIdle;
                        break;
                    case _ID_camelBusy:
                        this.subscriberStateChoice = SubscriberStateChoice.camelBusy;
                        break;
                    case _ID_notProvidedFromVLR:
                        this.subscriberStateChoice = SubscriberStateChoice.notProvidedFromVLR;
                        break;
                    default:
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": bad choice tag for contextSpecific tag class: " + tag,
                                MAPParsingComponentExceptionReason.MistypedParameter);
                }
                ais.readNullData(length);
                break;
            default:
                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass: "
                        + ais.getTagClass(), MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, true, tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.subscriberStateChoice == null)
            throw new MAPException("subscriberStateChoice must not be null");
        if (this.subscriberStateChoice == SubscriberStateChoice.netDetNotReachable) {
            if (this.notReachableReason == null)
                throw new MAPException("notReachableReason must not be null when subscriberStateChoice is netDetNotReachable");

            try {
                asnOs.writeIntegerData(this.notReachableReason.getCode());
            } catch (IOException e) {
                throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
            }
        } else {
            asnOs.writeNullData();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SubscriberState [");

        if (this.subscriberStateChoice != null) {
            sb.append("subscriberStateChoice=");
            sb.append(this.subscriberStateChoice);
        }
        if (this.notReachableReason != null) {
            sb.append(", notReachableReason=");
            sb.append(this.notReachableReason);
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<SubscriberStateImpl> SUBSCRIBER_STATE_XML = new XMLFormat<SubscriberStateImpl>(
            SubscriberStateImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, SubscriberStateImpl subscriberState)
                throws XMLStreamException {
            String choice = xml.getAttribute(SUBSCRIBER_STATE_CHOICE, DEFAULT_STRING_VALUE);
            SubscriberStateChoice subscriberStateChoice = null;
            if (choice != null) {
                subscriberStateChoice = Enum.valueOf(SubscriberStateChoice.class, choice);
            }
            String nrr = xml.get(NOT_REACHABLE_REASON, String.class);
            NotReachableReason notReachableReason = null;
            if (nrr != null) {
                notReachableReason = Enum.valueOf(NotReachableReason.class, nrr);
            }

            subscriberState.setData(subscriberStateChoice, notReachableReason);
        }

        @Override
        public void write(SubscriberStateImpl subscriberState, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (subscriberState.subscriberStateChoice != null)
                xml.setAttribute(SUBSCRIBER_STATE_CHOICE, subscriberState.subscriberStateChoice.toString());
            if (subscriberState.notReachableReason != null)
                xml.add(subscriberState.notReachableReason.toString(), NOT_REACHABLE_REASON, String.class);
        }
    };
}
