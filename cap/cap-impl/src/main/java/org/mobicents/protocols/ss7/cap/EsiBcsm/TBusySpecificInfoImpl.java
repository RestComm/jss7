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

package org.mobicents.protocols.ss7.cap.EsiBcsm;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TBusySpecificInfo;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TBusySpecificInfoImpl extends SequenceBase implements TBusySpecificInfo {

    private static final String BUSY_CAUSE = "busyCause";
    private static final String CALL_FORWARDED = "callForwarded";
    private static final String ROUTE_NOT_PERMITTED = "routeNotPermitted";
    private static final String FORWARDING_DESTINATION_NUMBER = "forwardingDestinationNumber";

    public static final int _ID_busyCause = 0;
    public static final int _ID_callForwarded = 50;
    public static final int _ID_routeNotPermitted = 51;
    public static final int _ID_forwardingDestinationNumber = 52;

    private CauseCap busyCause;
    private boolean callForwarded;
    private boolean routeNotPermitted;
    private CalledPartyNumberCap forwardingDestinationNumber;

    public TBusySpecificInfoImpl() {
        super("TBusySpecificInfo");
    }

    public TBusySpecificInfoImpl(CauseCap busyCause, boolean callForwarded, boolean routeNotPermitted,
            CalledPartyNumberCap forwardingDestinationNumber) {
        super("TBusySpecificInfo");
        this.busyCause = busyCause;
        this.callForwarded = callForwarded;
        this.routeNotPermitted = routeNotPermitted;
        this.forwardingDestinationNumber = forwardingDestinationNumber;
    }

    @Override
    public CauseCap getBusyCause() {
        return busyCause;
    }

    @Override
    public boolean getCallForwarded() {
        return callForwarded;
    }

    @Override
    public boolean getRouteNotPermitted() {
        return routeNotPermitted;
    }

    @Override
    public CalledPartyNumberCap getForwardingDestinationNumber() {
        return forwardingDestinationNumber;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException {

        this.busyCause = null;
        this.callForwarded = false;
        this.routeNotPermitted = false;
        this.forwardingDestinationNumber = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_busyCause:
                        this.busyCause = new CauseCapImpl();
                        ((CauseCapImpl) this.busyCause).decodeAll(ais);
                        break;
                    case _ID_callForwarded:
                        ais.readNull();
                        this.callForwarded = true;
                        break;
                    case _ID_routeNotPermitted:
                        ais.readNull();
                        this.routeNotPermitted = true;
                        break;
                    case _ID_forwardingDestinationNumber:
                        this.forwardingDestinationNumber = new CalledPartyNumberCapImpl();
                        ((CalledPartyNumberCapImpl) this.forwardingDestinationNumber).decodeAll(ais);
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
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        try {
            if (this.busyCause != null)
                ((CauseCapImpl) this.busyCause).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_busyCause);
            if (this.callForwarded)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_callForwarded);
            if (this.routeNotPermitted)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_routeNotPermitted);
            if (this.forwardingDestinationNumber != null)
                ((CalledPartyNumberCapImpl) this.forwardingDestinationNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_forwardingDestinationNumber);
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

        if (this.busyCause != null) {
            sb.append("busyCause= [");
            sb.append(busyCause);
            sb.append("]");
        }
        if (this.callForwarded) {
            sb.append(", callForwarded");
        }
        if (this.routeNotPermitted) {
            sb.append(", routeNotPermitted");
        }
        if (this.forwardingDestinationNumber != null) {
            sb.append(", forwardingDestinationNumber= [");
            sb.append(forwardingDestinationNumber);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<TBusySpecificInfoImpl> T_BUSY_SPECIFIC_INFO = new XMLFormat<TBusySpecificInfoImpl>(
            TBusySpecificInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, TBusySpecificInfoImpl tBusySpecificInfo)
                throws XMLStreamException {
            tBusySpecificInfo.busyCause = xml.get(BUSY_CAUSE, CauseCapImpl.class);

            Boolean bval = xml.get(CALL_FORWARDED, Boolean.class);
            if (bval != null)
                tBusySpecificInfo.callForwarded = bval;
            bval = xml.get(ROUTE_NOT_PERMITTED, Boolean.class);
            if (bval != null)
                tBusySpecificInfo.routeNotPermitted = bval;

            tBusySpecificInfo.forwardingDestinationNumber = xml.get(FORWARDING_DESTINATION_NUMBER,
                    CalledPartyNumberCapImpl.class);
        }

        @Override
        public void write(TBusySpecificInfoImpl tBusySpecificInfo, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (tBusySpecificInfo.busyCause != null) {
                xml.add((CauseCapImpl) tBusySpecificInfo.busyCause, BUSY_CAUSE, CauseCapImpl.class);
            }

            if (tBusySpecificInfo.callForwarded)
                xml.add(tBusySpecificInfo.callForwarded, CALL_FORWARDED, Boolean.class);
            if (tBusySpecificInfo.routeNotPermitted)
                xml.add(tBusySpecificInfo.routeNotPermitted, ROUTE_NOT_PERMITTED, Boolean.class);

            if (tBusySpecificInfo.forwardingDestinationNumber != null) {
                xml.add((CalledPartyNumberCapImpl) tBusySpecificInfo.forwardingDestinationNumber,
                        FORWARDING_DESTINATION_NUMBER, CalledPartyNumberCapImpl.class);
            }
        }
    };
}
