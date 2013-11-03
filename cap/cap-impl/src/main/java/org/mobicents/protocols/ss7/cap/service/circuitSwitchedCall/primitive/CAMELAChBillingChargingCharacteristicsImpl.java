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
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AudibleIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class CAMELAChBillingChargingCharacteristicsImpl implements CAMELAChBillingChargingCharacteristics, CAPAsnPrimitive {

    private static final String MAX_CALL_PERIOD_DURATION = "maxCallPeriodDuration";
    private static final String RELEASED_IF_DURATION_EXCEEDED = "releaseIfdurationExceeded";
    private static final String TARIFF_SWITCH_INTERVAL = "tariffSwitchInterval";
    private static final String AUDIBLE_INDICATOR = "audibleIndicator";
    private static final String EXTENSIONS = "extensions";

    public static final int _ID_timeDurationCharging = 0;

    public static final int _ID_maxCallPeriodDuration = 0;
    public static final int _ID_releaseIfdurationExceeded = 1;
    public static final int _ID_tariffSwitchInterval = 2;
    public static final int _ID_audibleIndicator = 3;
    public static final int _ID_extensions = 4;
    public static final int _ID_extensions_In_ReleaseIfDurationExceeded = 10;

    public static final String _PrimitiveName = "CAMELAChBillingChargingCharacteristics";

    private byte[] data;
    private long maxCallPeriodDuration;
    private boolean releaseIfdurationExceeded;
    private Long tariffSwitchInterval;
    private AudibleIndicator audibleIndicator;
    private CAPExtensions extensions;

    private boolean isCAPVersion3orLater;

    public CAMELAChBillingChargingCharacteristicsImpl() {
    }

    public CAMELAChBillingChargingCharacteristicsImpl(byte[] data) {
        this.data = data;
    }

    public CAMELAChBillingChargingCharacteristicsImpl(long maxCallPeriodDuration, boolean releaseIfdurationExceeded,
            Long tariffSwitchInterval, AudibleIndicator audibleIndicator, CAPExtensions extensions, boolean isCAPVersion3orLater) {
        this.maxCallPeriodDuration = maxCallPeriodDuration;
        this.releaseIfdurationExceeded = releaseIfdurationExceeded;
        this.tariffSwitchInterval = tariffSwitchInterval;
        this.audibleIndicator = audibleIndicator;
        this.extensions = extensions;
        this.isCAPVersion3orLater = isCAPVersion3orLater;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public long getMaxCallPeriodDuration() {
        return maxCallPeriodDuration;
    }

    @Override
    public boolean getReleaseIfdurationExceeded() {
        return releaseIfdurationExceeded;
    }

    @Override
    public Long getTariffSwitchInterval() {
        return tariffSwitchInterval;
    }

    @Override
    public AudibleIndicator getAudibleIndicator() {
        return audibleIndicator;
    }

    @Override
    public CAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public int getTag() throws CAPException {
        return Tag.STRING_OCTET;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return true;
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
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
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
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException {

        this.data = null;
        this.maxCallPeriodDuration = -1;
        this.releaseIfdurationExceeded = false;
        this.tariffSwitchInterval = null;
        this.audibleIndicator = null; // TODO: DEFAULT tone: FALSE
        this.extensions = null;

        this.data = ansIS.readOctetStringData(length);

        AsnInputStream aiss = new AsnInputStream(this.data);
        int tag = aiss.readTag();
        if (tag != _ID_timeDurationCharging || aiss.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || aiss.isTagPrimitive())
            throw new CAPParsingComponentException("Error when decoding " + _PrimitiveName
                    + ": CAMEL-AChBillingChargingCharacteristics choice has bad tag oe tagClass or is primitive, tag=" + tag
                    + ", tagClass=" + aiss.getTagClass(), CAPParsingComponentExceptionReason.MistypedParameter);

        AsnInputStream ais = aiss.readSequenceStream();
        while (true) {
            if (ais.available() == 0)
                break;

            tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_maxCallPeriodDuration:
                        this.maxCallPeriodDuration = ais.readInteger();
                        break;
                    case _ID_releaseIfdurationExceeded:
                        int ln = ais.readLength();
                        if (ln == 1) { // IMPLICIT - IN CAP V3 and later

                            this.releaseIfdurationExceeded = ais.readBooleanData(ln);
                        } else { // EXPLICIT - from trace - IN CAP V2

                            AsnInputStream ais2 = ais.readSequenceStreamData(ln);
                            int num = 0;
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                boolean parsed = false;
                                if (num == 0) {
                                    if (tag2 == Tag.BOOLEAN && ais2.getTagClass() == Tag.CLASS_UNIVERSAL) {
                                        this.releaseIfdurationExceeded = ais2.readBoolean();
                                        parsed = true;
                                    }
                                }
                                if (ais2.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC
                                        && tag2 == _ID_extensions_In_ReleaseIfDurationExceeded) {
                                    this.extensions = new CAPExtensionsImpl();
                                    ((CAPExtensionsImpl) this.extensions).decodeAll(ais2);
                                    parsed = true;
                                }
                                if (!parsed)
                                    ais2.advanceElement();

                                num++;
                            }
                        }
                        break;
                    case _ID_tariffSwitchInterval:
                        this.tariffSwitchInterval = ais.readInteger();
                        break;
                    case _ID_audibleIndicator:
                        ais.advanceElement(); // TODO: implement it
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

        if (this.maxCallPeriodDuration == -1)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": maxCallPeriodDuration is mandatory but not found",
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
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.data == null) {
            // encoding internal octet string
            if (this.maxCallPeriodDuration < 1 || this.maxCallPeriodDuration > 864000)
                throw new CAPException("Error while encoding " + _PrimitiveName
                        + ": maxCallPeriodDuration must be from 1 to 864000");
            if (this.tariffSwitchInterval != null && (this.tariffSwitchInterval < 1 || this.tariffSwitchInterval > 86400))
                throw new CAPException("Error while encoding " + _PrimitiveName
                        + ": tariffSwitchInterval must be from 1 to 86400");

            try {
                AsnOutputStream aos = new AsnOutputStream();
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_timeDurationCharging);
                int pos = aos.StartContentDefiniteLength();

                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_maxCallPeriodDuration, this.maxCallPeriodDuration);

                if (this.isCAPVersion3orLater) {
                    if (this.releaseIfdurationExceeded)
                        aos.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, _ID_releaseIfdurationExceeded, true);
                } else {
                    if (this.releaseIfdurationExceeded || this.extensions != null) {
                        aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_releaseIfdurationExceeded);
                        int pos2 = aos.StartContentDefiniteLength();
                        aos.writeBoolean(true);
                        if (this.extensions != null) {
                            ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                                    _ID_extensions_In_ReleaseIfDurationExceeded);
                        }
                        aos.FinalizeContent(pos2);
                    }
                }

                if (this.tariffSwitchInterval != null)
                    aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_tariffSwitchInterval, this.tariffSwitchInterval);

                if (this.audibleIndicator != null) {
                    // TODO: implement it
                }

                if (this.extensions != null && this.isCAPVersion3orLater)
                    ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);

                aos.FinalizeContent(pos);
                this.data = aos.toByteArray();
            } catch (IOException e) {
                throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
            } catch (AsnException e) {
                throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
            }
        }

        asnOs.writeOctetStringData(data);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [timeDurationCharging [");

        sb.append("maxCallPeriodDuration=");
        sb.append(this.maxCallPeriodDuration);
        if (this.releaseIfdurationExceeded) {
            sb.append(", releaseIfdurationExceeded");
        }
        if (this.tariffSwitchInterval != null) {
            sb.append(", tariffSwitchInterval=");
            sb.append(tariffSwitchInterval);
        }
        if (this.audibleIndicator != null) {
            sb.append(", audibleIndicator=");
            sb.append(audibleIndicator.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CAMELAChBillingChargingCharacteristicsImpl> CAMEL_ACH_BILLING_CHARGING_CHARACTERISTIC_XML = new XMLFormat<CAMELAChBillingChargingCharacteristicsImpl>(
            CAMELAChBillingChargingCharacteristicsImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                CAMELAChBillingChargingCharacteristicsImpl camelAChBillingChargingCharacteristics) throws XMLStreamException {
            camelAChBillingChargingCharacteristics.maxCallPeriodDuration = xml.get(MAX_CALL_PERIOD_DURATION, Long.class);
            camelAChBillingChargingCharacteristics.releaseIfdurationExceeded = xml.get(RELEASED_IF_DURATION_EXCEEDED,
                    Boolean.class);
            camelAChBillingChargingCharacteristics.tariffSwitchInterval = xml.get(TARIFF_SWITCH_INTERVAL, Long.class);
            camelAChBillingChargingCharacteristics.extensions = xml.get(EXTENSIONS, CAPExtensionsImpl.class);
            // TODO AudibleIndicatorImpl is not yet implemented
            // oAnswerSpecificInfo.audibleIndicator = xml.get(AUDIBLE_INDICATOR,
            // AudibleIndicatorImpl.class);

        }

        @Override
        public void write(CAMELAChBillingChargingCharacteristicsImpl camelAChBillingChargingCharacteristics,
                javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {

            xml.add(camelAChBillingChargingCharacteristics.maxCallPeriodDuration, MAX_CALL_PERIOD_DURATION, Long.class);
            xml.add(camelAChBillingChargingCharacteristics.releaseIfdurationExceeded, RELEASED_IF_DURATION_EXCEEDED,
                    Boolean.class);

            if (camelAChBillingChargingCharacteristics.tariffSwitchInterval != null) {
                xml.add(camelAChBillingChargingCharacteristics.tariffSwitchInterval, TARIFF_SWITCH_INTERVAL, Long.class);
            }
            if (camelAChBillingChargingCharacteristics.extensions != null) {
                xml.add((CAPExtensionsImpl) camelAChBillingChargingCharacteristics.extensions, EXTENSIONS,
                        CAPExtensionsImpl.class);
            }

            // TODO AudibleIndicatorImpl is not yet implemented
            // if(oAnswerSpecificInfo.audibleIndicator != null){
            // xml.add(((AudibleIndicatorImpl)oAnswerSpecificInfo.audibleIndicator),
            // AUDIBLE_INDICATOR, AudibleIndicatorImpl.class);
            // }

        }
    };
}
