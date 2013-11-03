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

package org.mobicents.protocols.ss7.cap.service.sms;

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
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.sms.InitialDPSMSRequest;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingScheme;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifier;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriod;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.primitives.CalledPartyBCDNumberImpl;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.SMSAddressStringImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPProtocolIdentifierImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPShortMessageSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPValidityPeriodImpl;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GPRSMSClassImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.MSClassmark2Impl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class InitialDPSMSRequestImpl extends SmsMessageImpl implements InitialDPSMSRequest {

    public static final String _PrimitiveName = "InitialDPSMSRequest";

    public static final int _ID_serviceKey = 0;
    public static final int _ID_destinationSubscriberNumber = 1;
    public static final int _ID_callingPartyNumber = 2;
    public static final int _ID_eventTypeSMS = 3;
    public static final int _ID_imsi = 4;
    public static final int _ID_locationInformationMSC = 5;
    public static final int _ID_locationInformationGPRS = 6;
    public static final int _ID_smscCAddress = 7;
    public static final int _ID_timeAndTimezone = 8;
    public static final int _ID_tPShortMessageSpecificInfo = 9;
    public static final int _ID_tPProtocolIdentifier = 10;
    public static final int _ID_tPDataCodingScheme = 11;
    public static final int _ID_tPValidityPeriod = 12;
    public static final int _ID_extensions = 13;
    public static final int _ID_smsReferenceNumber = 14;
    public static final int _ID_mscAddress = 15;
    public static final int _ID_sgsnNumber = 16;
    public static final int _ID_mSClassmark2 = 17;
    public static final int _ID_gprsMSClass = 18;
    public static final int _ID_imei = 19;
    public static final int _ID_calledPartyNumber = 20;

    private int serviceKey;
    private CalledPartyBCDNumber destinationSubscriberNumber;
    private SMSAddressString callingPartyNumber;
    private EventTypeSMS eventTypeSMS;
    private IMSI imsi;
    private LocationInformation locationInformationMSC;
    private LocationInformationGPRS locationInformationGPRS;
    private ISDNAddressString smscCAddress;
    private TimeAndTimezone timeAndTimezone;
    private TPShortMessageSpecificInfo tPShortMessageSpecificInfo;
    private TPProtocolIdentifier tPProtocolIdentifier;
    private TPDataCodingScheme tPDataCodingScheme;
    private TPValidityPeriod tPValidityPeriod;
    private CAPExtensions extensions;
    private CallReferenceNumber smsReferenceNumber;
    private ISDNAddressString mscAddress;
    private ISDNAddressString sgsnNumber;
    private MSClassmark2 mSClassmark2;
    private GPRSMSClass gprsMSClass;
    private IMEI imei;
    private ISDNAddressString calledPartyNumber;

    public InitialDPSMSRequestImpl() {
        super();
        this.serviceKey = -1;
    }

    public InitialDPSMSRequestImpl(int serviceKey, CalledPartyBCDNumber destinationSubscriberNumber,
            SMSAddressString callingPartyNumber, EventTypeSMS eventTypeSMS, IMSI imsi,
            LocationInformation locationInformationMSC, LocationInformationGPRS locationInformationGPRS,
            ISDNAddressString smscCAddress, TimeAndTimezone timeAndTimezone,
            TPShortMessageSpecificInfo tPShortMessageSpecificInfo, TPProtocolIdentifier tPProtocolIdentifier,
            TPDataCodingScheme tPDataCodingScheme, TPValidityPeriod tPValidityPeriod, CAPExtensions extensions,
            CallReferenceNumber smsReferenceNumber, ISDNAddressString mscAddress, ISDNAddressString sgsnNumber,
            MSClassmark2 mSClassmark2, GPRSMSClass gprsMSClass, IMEI imei, ISDNAddressString calledPartyNumber) {
        super();
        this.serviceKey = serviceKey;
        this.destinationSubscriberNumber = destinationSubscriberNumber;
        this.callingPartyNumber = callingPartyNumber;
        this.eventTypeSMS = eventTypeSMS;
        this.imsi = imsi;
        this.locationInformationMSC = locationInformationMSC;
        this.locationInformationGPRS = locationInformationGPRS;
        this.smscCAddress = smscCAddress;
        this.timeAndTimezone = timeAndTimezone;
        this.tPShortMessageSpecificInfo = tPShortMessageSpecificInfo;
        this.tPProtocolIdentifier = tPProtocolIdentifier;
        this.tPDataCodingScheme = tPDataCodingScheme;
        this.tPValidityPeriod = tPValidityPeriod;
        this.extensions = extensions;
        this.smsReferenceNumber = smsReferenceNumber;
        this.mscAddress = mscAddress;
        this.sgsnNumber = sgsnNumber;
        this.mSClassmark2 = mSClassmark2;
        this.gprsMSClass = gprsMSClass;
        this.imei = imei;
        this.calledPartyNumber = calledPartyNumber;
    }

    @Override
    public int getServiceKey() {
        return this.serviceKey;
    }

    @Override
    public CalledPartyBCDNumber getDestinationSubscriberNumber() {
        return this.destinationSubscriberNumber;
    }

    @Override
    public SMSAddressString getCallingPartyNumber() {
        return this.callingPartyNumber;
    }

    @Override
    public EventTypeSMS getEventTypeSMS() {
        return this.eventTypeSMS;
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public LocationInformation getLocationInformationMSC() {
        return this.locationInformationMSC;
    }

    @Override
    public LocationInformationGPRS getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    @Override
    public ISDNAddressString getSMSCAddress() {
        return this.smscCAddress;
    }

    @Override
    public TimeAndTimezone getTimeAndTimezone() {
        return this.timeAndTimezone;
    }

    @Override
    public TPShortMessageSpecificInfo getTPShortMessageSpecificInfo() {
        return this.tPShortMessageSpecificInfo;
    }

    @Override
    public TPProtocolIdentifier getTPProtocolIdentifier() {
        return this.tPProtocolIdentifier;
    }

    @Override
    public TPDataCodingScheme getTPDataCodingScheme() {
        return this.tPDataCodingScheme;
    }

    @Override
    public TPValidityPeriod getTPValidityPeriod() {
        return this.tPValidityPeriod;
    }

    @Override
    public CAPExtensions getExtensions() {
        return this.extensions;
    }

    @Override
    public CallReferenceNumber getSmsReferenceNumber() {
        return this.smsReferenceNumber;
    }

    @Override
    public ISDNAddressString getMscAddress() {
        return this.mscAddress;
    }

    @Override
    public ISDNAddressString getSgsnNumber() {
        return this.sgsnNumber;
    }

    @Override
    public MSClassmark2 getMSClassmark2() {
        return this.mSClassmark2;
    }

    @Override
    public GPRSMSClass getGPRSMSClass() {
        return this.gprsMSClass;
    }

    @Override
    public IMEI getImei() {
        return this.imei;
    }

    @Override
    public ISDNAddressString getCalledPartyNumber() {
        return this.calledPartyNumber;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.initialDPSMS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.initialDPSMS;
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
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName
                    + ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
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
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException,
            AsnException, MAPParsingComponentException {

        this.serviceKey = -1;
        this.destinationSubscriberNumber = null;
        this.callingPartyNumber = null;
        this.eventTypeSMS = null;
        this.imsi = null;
        this.locationInformationMSC = null;
        this.locationInformationGPRS = null;
        this.smscCAddress = null;
        this.timeAndTimezone = null;
        this.tPShortMessageSpecificInfo = null;
        this.tPProtocolIdentifier = null;
        this.tPDataCodingScheme = null;
        this.tPValidityPeriod = null;
        this.extensions = null;
        this.smsReferenceNumber = null;
        this.mscAddress = null;
        this.sgsnNumber = null;
        this.mSClassmark2 = null;
        this.gprsMSClass = null;
        this.imei = null;
        this.calledPartyNumber = null;
        boolean isServiceKeyFound = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_serviceKey:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".serviceKey: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.serviceKey = (int) ais.readInteger();
                    isServiceKeyFound = true;
                    break;

                case _ID_destinationSubscriberNumber:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".destinationSubscriberNumber: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.destinationSubscriberNumber = new CalledPartyBCDNumberImpl();
                    ((CalledPartyBCDNumberImpl) this.destinationSubscriberNumber).decodeAll(ais);
                    break;
                case _ID_callingPartyNumber:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".callingPartyNumber: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.callingPartyNumber = new SMSAddressStringImpl();
                    ((SMSAddressStringImpl) this.callingPartyNumber).decodeAll(ais);
                    break;
                case _ID_eventTypeSMS:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".eventTypeSMS: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    int i1 = (int) ais.readInteger();
                    this.eventTypeSMS = EventTypeSMS.getInstance(i1);
                    break;
                case _ID_imsi:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".imsi: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.imsi = new IMSIImpl();
                    ((IMSIImpl) this.imsi).decodeAll(ais);
                    break;
                case _ID_locationInformationMSC:
                    if (ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".locationInformationMSC: Parameter is primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.locationInformationMSC = new LocationInformationImpl();
                    ((LocationInformationImpl) this.locationInformationMSC).decodeAll(ais);
                    break;
                case _ID_locationInformationGPRS:
                    if (ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".locationInformationGPRS: Parameter is primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.locationInformationGPRS = new LocationInformationGPRSImpl();
                    ((LocationInformationGPRSImpl) this.locationInformationGPRS).decodeAll(ais);
                    break;
                case _ID_smscCAddress:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".smscCAddress: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.smscCAddress = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.smscCAddress).decodeAll(ais);
                    break;
                case _ID_timeAndTimezone:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".timeAndTimezone: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.timeAndTimezone = new TimeAndTimezoneImpl();
                    ((TimeAndTimezoneImpl) this.timeAndTimezone).decodeAll(ais);
                    break;
                case _ID_tPShortMessageSpecificInfo:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".tPShortMessageSpecificInfo: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.tPShortMessageSpecificInfo = new TPShortMessageSpecificInfoImpl();
                    ((TPShortMessageSpecificInfoImpl) this.tPShortMessageSpecificInfo).decodeAll(ais);
                    break;
                case _ID_tPProtocolIdentifier:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".tPProtocolIdentifier: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.tPProtocolIdentifier = new TPProtocolIdentifierImpl();
                    ((TPProtocolIdentifierImpl) this.tPProtocolIdentifier).decodeAll(ais);
                    break;
                case _ID_tPDataCodingScheme:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".tPDataCodingScheme: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.tPDataCodingScheme = new TPDataCodingSchemeImpl();
                    ((TPDataCodingSchemeImpl) this.tPDataCodingScheme).decodeAll(ais);
                    break;
                case _ID_tPValidityPeriod:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".tPValidityPeriod: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.tPValidityPeriod = new TPValidityPeriodImpl();
                    ((TPValidityPeriodImpl) this.tPValidityPeriod).decodeAll(ais);
                    break;
                case _ID_extensions:
                    if (ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".extensions: Parameter is primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.extensions = new CAPExtensionsImpl();
                    ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
                    break;
                case _ID_smsReferenceNumber:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".smsReferenceNumber: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.smsReferenceNumber = new CallReferenceNumberImpl();
                    ((CallReferenceNumberImpl) this.smsReferenceNumber).decodeAll(ais);
                    break;
                case _ID_mscAddress:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".mscAddress: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.mscAddress = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.mscAddress).decodeAll(ais);
                    break;
                case _ID_sgsnNumber:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".sgsnNumber: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.sgsnNumber = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.sgsnNumber).decodeAll(ais);
                    break;
                case _ID_mSClassmark2:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".mSClassmark2: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.mSClassmark2 = new MSClassmark2Impl();
                    ((MSClassmark2Impl) this.mSClassmark2).decodeAll(ais);
                    break;
                case _ID_gprsMSClass:
                    if (ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".gprsMSClass: Parameter is primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.gprsMSClass = new GPRSMSClassImpl();
                    ((GPRSMSClassImpl) this.gprsMSClass).decodeAll(ais);
                    break;
                case _ID_imei:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".imei: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.imei = new IMEIImpl();
                    ((IMEIImpl) this.imei).decodeAll(ais);
                    break;

                case _ID_calledPartyNumber:
                    if (!ais.isTagPrimitive())
                        throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".calledPartyNumber: Parameter is not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.calledPartyNumber = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.calledPartyNumber).decodeAll(ais);
                    break;

                default:
                    ais.advanceElement();
                    break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (!isServiceKeyFound)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter ServiceKey is mandatory but not found",
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

        try {

            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_serviceKey, this.serviceKey);

            if (this.destinationSubscriberNumber != null)
                ((CalledPartyBCDNumberImpl) this.destinationSubscriberNumber).encodeAll(asnOs,
                        Tag.CLASS_CONTEXT_SPECIFIC, _ID_destinationSubscriberNumber);

            if (this.callingPartyNumber != null)
                ((SMSAddressStringImpl) this.callingPartyNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_callingPartyNumber);

            if (this.eventTypeSMS != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_eventTypeSMS, this.eventTypeSMS.getCode());

            if (this.imsi != null)
                ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_imsi);

            if (this.locationInformationMSC != null)
                ((LocationInformationImpl) this.locationInformationMSC).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_locationInformationMSC);

            if (this.locationInformationGPRS != null)
                ((LocationInformationGPRSImpl) this.locationInformationGPRS).encodeAll(asnOs,
                        Tag.CLASS_CONTEXT_SPECIFIC, _ID_locationInformationGPRS);

            if (this.smscCAddress != null)
                ((ISDNAddressStringImpl) this.smscCAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_smscCAddress);

            if (this.timeAndTimezone != null)
                ((TimeAndTimezoneImpl) this.timeAndTimezone).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_timeAndTimezone);

            if (this.tPShortMessageSpecificInfo != null)
                ((TPShortMessageSpecificInfoImpl) this.tPShortMessageSpecificInfo).encodeAll(asnOs,
                        Tag.CLASS_CONTEXT_SPECIFIC, _ID_tPShortMessageSpecificInfo);

            if (this.tPProtocolIdentifier != null)
                ((TPProtocolIdentifierImpl) this.tPProtocolIdentifier).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_tPProtocolIdentifier);

            if (this.tPDataCodingScheme != null)
                ((TPDataCodingSchemeImpl) this.tPDataCodingScheme).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_tPDataCodingScheme);

            if (this.tPValidityPeriod != null)
                ((TPValidityPeriodImpl) this.tPValidityPeriod).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_tPValidityPeriod);

            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);

            if (this.smsReferenceNumber != null)
                ((CallReferenceNumberImpl) this.smsReferenceNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_smsReferenceNumber);

            if (this.mscAddress != null)
                ((ISDNAddressStringImpl) this.mscAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_mscAddress);

            if (this.sgsnNumber != null)
                ((ISDNAddressStringImpl) this.sgsnNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_sgsnNumber);

            if (this.mSClassmark2 != null)
                ((MSClassmark2Impl) this.mSClassmark2).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_mSClassmark2);

            if (this.locationInformationGPRS != null)
                ((LocationInformationGPRSImpl) this.locationInformationGPRS).encodeAll(asnOs,
                        Tag.CLASS_CONTEXT_SPECIFIC, _ID_locationInformationGPRS);

            if (this.gprsMSClass != null)
                ((GPRSMSClassImpl) this.gprsMSClass).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_gprsMSClass);

            if (this.imei != null)
                ((IMEIImpl) this.imei).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_imei);

            if (this.calledPartyNumber != null)
                ((ISDNAddressStringImpl) this.calledPartyNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_calledPartyNumber);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("serviceKey=");
        sb.append(serviceKey);
        if (this.destinationSubscriberNumber != null) {
            sb.append(", destinationSubscriberNumber=");
            sb.append(destinationSubscriberNumber.toString());
        }
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber.toString());
        }
        if (this.eventTypeSMS != null) {
            sb.append(", eventTypeSMS=");
            sb.append(eventTypeSMS.toString());
        }
        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(imsi.toString());
        }
        if (this.locationInformationMSC != null) {
            sb.append(", locationInformationMSC=");
            sb.append(locationInformationMSC.toString());
        }
        if (this.locationInformationGPRS != null) {
            sb.append(", locationInformationGPRS=");
            sb.append(locationInformationGPRS.toString());
        }
        if (this.smscCAddress != null) {
            sb.append(", smscCAddress=");
            sb.append(smscCAddress.toString());
        }
        if (this.timeAndTimezone != null) {
            sb.append(", timeAndTimezone=");
            sb.append(timeAndTimezone.toString());
        }
        if (this.tPShortMessageSpecificInfo != null) {
            sb.append(", tPShortMessageSpecificInfo=");
            sb.append(tPShortMessageSpecificInfo.toString());
        }
        if (this.tPProtocolIdentifier != null) {
            sb.append(", tPProtocolIdentifier=");
            sb.append(tPProtocolIdentifier.toString());
        }
        if (this.tPDataCodingScheme != null) {
            sb.append(", tPDataCodingScheme=");
            sb.append(tPDataCodingScheme.toString());
        }
        if (this.tPValidityPeriod != null) {
            sb.append(", tPValidityPeriod=");
            sb.append(tPValidityPeriod.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.smsReferenceNumber != null) {
            sb.append(", smsReferenceNumber=");
            sb.append(smsReferenceNumber.toString());
        }
        if (this.mscAddress != null) {
            sb.append(", mscAddress=");
            sb.append(mscAddress.toString());
        }
        if (this.sgsnNumber != null) {
            sb.append(", sgsnNumber=");
            sb.append(sgsnNumber.toString());
        }
        if (this.mSClassmark2 != null) {
            sb.append(", mSClassmark2=");
            sb.append(mSClassmark2.toString());
        }
        if (this.gprsMSClass != null) {
            sb.append(", gprsMSClass=");
            sb.append(gprsMSClass.toString());
        }
        if (this.imei != null) {
            sb.append(", imei=");
            sb.append(imei.toString());
        }
        if (this.calledPartyNumber != null) {
            sb.append(", calledPartyNumber=");
            sb.append(calledPartyNumber.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
