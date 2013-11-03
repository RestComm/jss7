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
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ChargeIndicator;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TAnswerSpecificInfoImpl extends SequenceBase implements TAnswerSpecificInfo {

    private static final String DESTINATION_ADDRESS = "destinationAddress";
    private static final String OR_CALL = "orCall";
    private static final String FORWARDED_CALL = "forwardedCall";
    private static final String CHARGE_INDICATOR = "chargeIndicator";
    private static final String EXT_BASIC_SERVICE_CODE = "extBasicServiceCode";
    private static final String EXT_BASIC_SERVICE_CODE2 = "extBasicServiceCode2";

    public static final int _ID_destinationAddress = 50;
    public static final int _ID_orCall = 51;
    public static final int _ID_forwardedCall = 52;
    public static final int _ID_chargeIndicator = 53;
    public static final int _ID_extbasicServiceCode = 54;
    public static final int _ID_extbasicServiceCode2 = 55;

    private CalledPartyNumberCap destinationAddress;
    private boolean orCall;
    private boolean forwardedCall;
    private ChargeIndicator chargeIndicator;
    private ExtBasicServiceCode extBasicServiceCode;
    private ExtBasicServiceCode extBasicServiceCode2;

    public TAnswerSpecificInfoImpl() {
        super("TAnswerSpecificInfo");
    }

    public TAnswerSpecificInfoImpl(CalledPartyNumberCap destinationAddress, boolean orCall, boolean forwardedCall,
            ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode, ExtBasicServiceCode extBasicServiceCode2) {
        super("TAnswerSpecificInfo");
        this.destinationAddress = destinationAddress;
        this.orCall = orCall;
        this.forwardedCall = forwardedCall;
        this.chargeIndicator = chargeIndicator;
        this.extBasicServiceCode = extBasicServiceCode;
        this.extBasicServiceCode2 = extBasicServiceCode2;
    }

    @Override
    public CalledPartyNumberCap getDestinationAddress() {
        return destinationAddress;
    }

    @Override
    public boolean getOrCall() {
        return orCall;
    }

    @Override
    public boolean getForwardedCall() {
        return forwardedCall;
    }

    @Override
    public ChargeIndicator getChargeIndicator() {
        return chargeIndicator;
    }

    @Override
    public ExtBasicServiceCode getExtBasicServiceCode() {
        return extBasicServiceCode;
    }

    @Override
    public ExtBasicServiceCode getExtBasicServiceCode2() {
        return extBasicServiceCode2;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException {

        this.destinationAddress = null;
        this.orCall = false;
        this.forwardedCall = false;
        this.chargeIndicator = null;
        this.extBasicServiceCode = null;
        this.extBasicServiceCode2 = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_destinationAddress:
                        ais.advanceElement();
                        // TODO: implement it
                        break;
                    case _ID_orCall:
                        ais.advanceElement();
                        // TODO: implement it
                        break;
                    case _ID_forwardedCall:
                        ais.advanceElement();
                        // TODO: implement it
                        break;
                    case _ID_chargeIndicator:
                        ais.advanceElement();
                        // TODO: implement it
                        break;
                    case _ID_extbasicServiceCode:
                        ais.advanceElement();
                        // TODO: implement it
                        break;
                    case _ID_extbasicServiceCode2:
                        ais.advanceElement();
                        // TODO: implement it
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

        if (this.destinationAddress != null) {
            // TODO: implement it
        }
        if (this.orCall) {
            // TODO: implement it
        }
        if (this.forwardedCall) {
            // TODO: implement it
        }
        if (this.chargeIndicator != null) {
            // TODO: implement it
        }
        if (this.extBasicServiceCode != null) {
            // TODO: implement it
        }
        if (this.extBasicServiceCode2 != null) {
            // TODO: implement it
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.destinationAddress != null) {
            sb.append("destinationAddress= [");
            sb.append(destinationAddress.toString());
            sb.append("]");
        }
        if (this.orCall) {
            sb.append(", orCall");
        }
        if (this.forwardedCall) {
            sb.append(", forwardedCall");
        }
        if (this.chargeIndicator != null) {
            sb.append(", chargeIndicator= [");
            sb.append(chargeIndicator.toString());
            sb.append("]");
        }
        if (this.extBasicServiceCode != null) {
            sb.append(", extBasicServiceCode= [");
            sb.append(extBasicServiceCode.toString());
            sb.append("]");
        }
        if (this.extBasicServiceCode2 != null) {
            sb.append(", extBasicServiceCode2= [");
            sb.append(extBasicServiceCode2.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<TAnswerSpecificInfoImpl> O_ANSWER_SPECIFIC_INFO_XML = new XMLFormat<TAnswerSpecificInfoImpl>(
            TAnswerSpecificInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, TAnswerSpecificInfoImpl oAnswerSpecificInfo)
                throws XMLStreamException {
            oAnswerSpecificInfo.destinationAddress = xml.get(DESTINATION_ADDRESS, CalledPartyNumberCapImpl.class);

            Boolean bval = xml.get(OR_CALL, Boolean.class);
            if (bval != null)
                oAnswerSpecificInfo.orCall = bval;
            bval = xml.get(FORWARDED_CALL, Boolean.class);
            if (bval != null)
                oAnswerSpecificInfo.forwardedCall = bval;

            // oAnswerSpecificInfo.chargeIndicator = xml.get(CHARGE_INDICATOR,
            // ChargeIndicatorImpl.class);

            oAnswerSpecificInfo.extBasicServiceCode = xml.get(EXT_BASIC_SERVICE_CODE, ExtBasicServiceCodeImpl.class);
            oAnswerSpecificInfo.extBasicServiceCode2 = xml.get(EXT_BASIC_SERVICE_CODE2, ExtBasicServiceCodeImpl.class);
        }

        @Override
        public void write(TAnswerSpecificInfoImpl oAnswerSpecificInfo, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            if (oAnswerSpecificInfo.destinationAddress != null) {
                xml.add(((CalledPartyNumberCapImpl) oAnswerSpecificInfo.destinationAddress), DESTINATION_ADDRESS,
                        CalledPartyNumberCapImpl.class);
            }

            if (oAnswerSpecificInfo.orCall)
                xml.add(oAnswerSpecificInfo.orCall, OR_CALL, Boolean.class);
            if (oAnswerSpecificInfo.forwardedCall)
                xml.add(oAnswerSpecificInfo.forwardedCall, FORWARDED_CALL, Boolean.class);

            if (oAnswerSpecificInfo.chargeIndicator != null) {
                // TODO ChargeIndicatorImpl not yet implemented
                // xml.add(((ChargeIndicatorImpl)
                // oAnswerSpecificInfo.chargeIndicator), CHARGE_INDICATOR,
                // ChargeIndicatorImpl.class);
            }

            if (oAnswerSpecificInfo.extBasicServiceCode != null) {
                xml.add(((ExtBasicServiceCodeImpl) oAnswerSpecificInfo.extBasicServiceCode), EXT_BASIC_SERVICE_CODE,
                        ExtBasicServiceCodeImpl.class);
            }

            if (oAnswerSpecificInfo.extBasicServiceCode2 != null) {
                xml.add(((ExtBasicServiceCodeImpl) oAnswerSpecificInfo.extBasicServiceCode2), EXT_BASIC_SERVICE_CODE2,
                        ExtBasicServiceCodeImpl.class);
            }
        }
    };
}
