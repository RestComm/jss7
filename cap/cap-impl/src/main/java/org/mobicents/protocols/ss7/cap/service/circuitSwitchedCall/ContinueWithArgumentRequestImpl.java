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
import java.util.ArrayList;

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
import org.mobicents.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueWithArgumentRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCap;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ContinueWithArgumentArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.isup.GenericNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.LocationNumberCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ContinueWithArgumentArgExtensionImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.isup.CallingPartysCategoryInapImpl;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.primitives.ArrayListSerializingBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CUGInterlockImpl;

/**
 *
 * @author Povilas Jurna
 *
 */
public class ContinueWithArgumentRequestImpl extends CircuitSwitchedCallMessageImpl implements
        ContinueWithArgumentRequest {

    private static final int _ID_alertingPattern = 1;
    private static final int _ID_extensions = 6;
    private static final int _ID_serviceInteractionIndicatorsTwo = 7;
    private static final int _ID_callingPartysCategory = 12;
    private static final int _ID_genericNumbers = 16;
    private static final int _ID_cugInterlock = 17;
    private static final int _ID_cugOutgoingAccess = 18;
    private static final int _ID_chargeNumber = 50;
    private static final int _ID_carrier = 52;
    private static final int _ID_suppressionOfAnnouncement = 55;
    private static final int _ID_naOliInfo = 56;
    private static final int _ID_borInterrogationRequested = 57;
    private static final int _ID_suppressOCSI = 58;
    private static final int _ID_continueWithArgumentArgExtension = 59;

    private static final String ALERTING_PATTERN = "alertingPattern";
    private static final String EXTENSIONS = "extensions";
    private static final String SERVICE_INTERACTION_INDICATORS_TWO = "serviceInteractionIndicatorsTwo";
    private static final String CALLING_PARTYS_CATEGORY = "callingPartysCategory";
    private static final String GENERIC_NUMBER = "genericNumber";
    private static final String GENERIC_NUMBER_LIST = "genericNumbersList";
    private static final String CUG_INTER_LOCK = "cugInterlock";
    private static final String CUG_OUTGOING_ACCESS = "cugOutgoingAccess";
    private static final String CHARGE_NUMBER = "chargeNumber";
    private static final String CARRIER = "carrier";
    private static final String SUPPRESSION_OF_ANNOUNCEMENT = "suppressionOfAnnouncement";
    private static final String NA_OLI_INFO = "naOliInfo";
    private static final String BOR_INTERROGATION_REQUESTED = "borInterrogationRequested";
    private static final String SUPPRESS_OCSI = "suppressOCSI";
    private static final String CONTINUE_WITH_ARGUMENT_ARG_EXTENSION = "continueWithArgumentArgExtension";

    public static final String _PrimitiveName = "ContinueWithArgumentRequestIndication";

    private AlertingPatternCap alertingPattern;
    private CAPExtensions extensions;
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
    private CallingPartysCategoryInap callingPartysCategory;
    private ArrayList<GenericNumberCap> genericNumbers;
    private CUGInterlock cugInterlock;
    private boolean cugOutgoingAccess;
    private LocationNumberCap chargeNumber;
    private Carrier carrier;
    private boolean suppressionOfAnnouncement;
    private NAOliInfo naOliInfo;
    private boolean borInterrogationRequested;
    private boolean suppressOCsi;
    private ContinueWithArgumentArgExtension continueWithArgumentArgExtension;

    public ContinueWithArgumentRequestImpl() {
    }

    public ContinueWithArgumentRequestImpl(AlertingPatternCap alertingPattern, CAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            CallingPartysCategoryInap callingPartysCategory, ArrayList<GenericNumberCap> genericNumbers,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, LocationNumberCap chargeNumber, Carrier carrier,
            boolean suppressionOfAnnouncement, NAOliInfo naOliInfo, boolean borInterrogationRequested,
            boolean suppressOCsi, ContinueWithArgumentArgExtension continueWithArgumentArgExtension) {
        super();
        this.alertingPattern = alertingPattern;
        this.extensions = extensions;
        this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
        this.callingPartysCategory = callingPartysCategory;
        this.genericNumbers = genericNumbers;
        this.cugInterlock = cugInterlock;
        this.cugOutgoingAccess = cugOutgoingAccess;
        this.chargeNumber = chargeNumber;
        this.carrier = carrier;
        this.suppressionOfAnnouncement = suppressionOfAnnouncement;
        this.naOliInfo = naOliInfo;
        this.borInterrogationRequested = borInterrogationRequested;
        this.suppressOCsi = suppressOCsi;
        this.continueWithArgumentArgExtension = continueWithArgumentArgExtension;
    }

    public CAPMessageType getMessageType() {
        return CAPMessageType.continueWithArgument_Request;
    }

    public int getOperationCode() {
        return CAPOperationCode.continueWithArgument;
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

        this.alertingPattern = null;
        this.extensions = null;
        this.serviceInteractionIndicatorsTwo = null;
        this.callingPartysCategory = null;
        this.genericNumbers = null;
        this.cugInterlock = null;
        this.cugOutgoingAccess = false;
        this.chargeNumber = null;
        this.carrier = null;
        this.suppressionOfAnnouncement = false;
        this.naOliInfo = null;
        this.borInterrogationRequested = false;
        this.suppressOCsi = false;
        this.continueWithArgumentArgExtension = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _ID_alertingPattern:
                    this.alertingPattern = new AlertingPatternCapImpl();
                    ((AlertingPatternCapImpl) this.alertingPattern).decodeAll(ais);
                    break;
                case _ID_extensions:
                    this.extensions = new CAPExtensionsImpl();
                    ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
                    break;
                case _ID_serviceInteractionIndicatorsTwo:
                    this.serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl();
                    ((ServiceInteractionIndicatorsTwoImpl) this.serviceInteractionIndicatorsTwo).decodeAll(ais);
                    break;
                case _ID_callingPartysCategory:
                    this.callingPartysCategory = new CallingPartysCategoryInapImpl();
                    ((CallingPartysCategoryInapImpl) this.callingPartysCategory).decodeAll(ais);
                    break;
                case _ID_genericNumbers:
                    this.genericNumbers = new ArrayList<GenericNumberCap>();
                    AsnInputStream ais2 = ais.readSequenceStream();
                    while (true) {
                        if (ais2.available() == 0)
                            break;

                        int tag2 = ais2.readTag();
                        if (ais2.getTagClass() != Tag.CLASS_UNIVERSAL || tag2 != Tag.STRING_OCTET)
                            throw new CAPParsingComponentException("Error when decoding " + _PrimitiveName
                                    + " genericNumbers parameter SET must consist of OCTET_STRING elements",
                                    CAPParsingComponentExceptionReason.MistypedParameter);

                        GenericNumberCapImpl elem = new GenericNumberCapImpl();
                        elem.decodeAll(ais2);
                        this.genericNumbers.add(elem);
                    }
                    break;
                case _ID_cugInterlock:
                    this.cugInterlock = new CUGInterlockImpl();
                    ((CUGInterlockImpl) this.cugInterlock).decodeAll(ais);
                    break;
                case _ID_cugOutgoingAccess:
                    ais.readNull();
                    this.cugOutgoingAccess = true;
                    break;
                case _ID_chargeNumber:
                    this.chargeNumber = new LocationNumberCapImpl();
                    ((LocationNumberCapImpl) this.chargeNumber).decodeAll(ais);
                    break;
                case _ID_carrier:
                    this.carrier = new CarrierImpl();
                    ((CarrierImpl) this.carrier).decodeAll(ais);
                    break;
                case _ID_suppressionOfAnnouncement:
                    ais.readNull();
                    this.suppressionOfAnnouncement = true;
                    break;
                case _ID_naOliInfo:
                    this.naOliInfo = new NAOliInfoImpl();
                    ((NAOliInfoImpl) this.naOliInfo).decodeAll(ais);
                    break;
                case _ID_borInterrogationRequested:
                    ais.readNull();
                    this.borInterrogationRequested = true;
                    break;
                case _ID_suppressOCSI:
                    ais.readNull();
                    this.suppressOCsi = true;
                    break;
                case _ID_continueWithArgumentArgExtension:
                    this.continueWithArgumentArgExtension = new ContinueWithArgumentArgExtensionImpl();
                    ((ContinueWithArgumentArgExtensionImpl) this.continueWithArgumentArgExtension).decodeAll(ais);
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
            if (alertingPattern != null)
                ((AlertingPatternCapImpl) this.alertingPattern).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_alertingPattern);

            if (extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);

            if (serviceInteractionIndicatorsTwo != null)
                ((ServiceInteractionIndicatorsTwoImpl) this.serviceInteractionIndicatorsTwo).encodeAll(aos,
                        Tag.CLASS_CONTEXT_SPECIFIC, _ID_serviceInteractionIndicatorsTwo);

            if (callingPartysCategory != null)
                ((CallingPartysCategoryInapImpl) this.callingPartysCategory).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_callingPartysCategory);

            if (this.genericNumbers != null) {
                if (this.genericNumbers.size() < 1 || this.genericNumbers.size() > 5)
                    throw new CAPException("Error while encoding " + _PrimitiveName
                            + ": genericNumbers size must be from 1 to 5");

                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_genericNumbers);
                int pos = aos.StartContentDefiniteLength();
                for (GenericNumberCap gnc : this.genericNumbers) {
                    GenericNumberCapImpl gncc = (GenericNumberCapImpl) gnc;
                    gncc.encodeAll(aos);
                }
                aos.FinalizeContent(pos);
            }

            if (cugInterlock != null)
                ((CUGInterlockImpl) this.cugInterlock).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_cugInterlock);

            if (cugOutgoingAccess)
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_cugOutgoingAccess);

            if (chargeNumber != null)
                ((LocationNumberCapImpl) this.chargeNumber)
                        .encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_chargeNumber);

            if (this.carrier != null)
                ((CarrierImpl) this.carrier).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_carrier);

            if (suppressionOfAnnouncement)
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_suppressionOfAnnouncement);

            if (naOliInfo != null)
                ((NAOliInfoImpl) this.naOliInfo).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_naOliInfo);

            if (borInterrogationRequested)
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_borInterrogationRequested);

            if (suppressOCsi)
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_suppressOCSI);

            if (continueWithArgumentArgExtension != null)
                ((ContinueWithArgumentArgExtensionImpl) this.continueWithArgumentArgExtension).encodeAll(aos,
                        Tag.CLASS_CONTEXT_SPECIFIC, _ID_continueWithArgumentArgExtension);

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

        if (alertingPattern != null) {
            sb.append("alertingPattern=");
            sb.append(alertingPattern);
        }
        if (extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions);
        }
        if (serviceInteractionIndicatorsTwo != null) {
            sb.append(", serviceInteractionIndicatorsTwo=");
            sb.append(serviceInteractionIndicatorsTwo);
        }
        if (callingPartysCategory != null) {
            sb.append(", callingPartysCategory=");
            sb.append(callingPartysCategory);
        }
        if (genericNumbers != null) {
            sb.append(", genericNumbers=[");
            boolean isFirst = true;
            for (GenericNumberCap gnc : this.genericNumbers) {
                if (isFirst)
                    isFirst = false;
                else
                    sb.append(", ");
                sb.append(gnc.toString());
            }
            sb.append("]");
        }
        if (cugInterlock != null) {
            sb.append(", cugInterlock=");
            sb.append(cugInterlock);
        }
        if (cugOutgoingAccess) {
            sb.append(", cugOutgoingAccess=");
            sb.append(cugOutgoingAccess);
        }
        if (chargeNumber != null) {
            sb.append(", chargeNumber=");
            sb.append(chargeNumber);
        }
        if (carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier);
        }
        if (suppressionOfAnnouncement) {
            sb.append(", suppressionOfAnnouncement=");
            sb.append(suppressionOfAnnouncement);
        }
        if (naOliInfo != null) {
            sb.append(", naOliInfo=");
            sb.append(naOliInfo);
        }
        if (borInterrogationRequested) {
            sb.append(", borInterrogationRequested=");
            sb.append(borInterrogationRequested);
        }
        if (suppressOCsi) {
            sb.append(", suppressOCsi=");
            sb.append(suppressOCsi);
        }
        if (continueWithArgumentArgExtension != null) {
            sb.append(", continueWithArgumentArgExtension=");
            sb.append(continueWithArgumentArgExtension);
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ContinueWithArgumentRequestImpl> CONNECT_REQUEST_XML = new XMLFormat<ContinueWithArgumentRequestImpl>(
            ContinueWithArgumentRequestImpl.class) {

        public void read(javolution.xml.XMLFormat.InputElement xml,
                ContinueWithArgumentRequestImpl continueWithArgumentRequest) throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, continueWithArgumentRequest);

            continueWithArgumentRequest.alertingPattern = xml.get(ALERTING_PATTERN, AlertingPatternCapImpl.class);
            continueWithArgumentRequest.extensions = xml.get(EXTENSIONS, CAPExtensionsImpl.class);
            continueWithArgumentRequest.serviceInteractionIndicatorsTwo = xml.get(SERVICE_INTERACTION_INDICATORS_TWO,
                    ServiceInteractionIndicatorsTwoImpl.class);
            continueWithArgumentRequest.callingPartysCategory = xml.get(CALLING_PARTYS_CATEGORY,
                    CallingPartysCategoryInapImpl.class);
            ContinueWithArgument_GenericNumbers al = xml.get(GENERIC_NUMBER_LIST,
                    ContinueWithArgument_GenericNumbers.class);
            if (al != null) {
                continueWithArgumentRequest.genericNumbers = al.getData();
            }
            continueWithArgumentRequest.cugInterlock = xml.get(CUG_INTER_LOCK, CUGInterlockImpl.class);
            Boolean bval = xml.get(CUG_OUTGOING_ACCESS, Boolean.class);
            if (bval != null)
                continueWithArgumentRequest.cugOutgoingAccess = bval;
            continueWithArgumentRequest.chargeNumber = xml.get(CHARGE_NUMBER, LocationNumberCapImpl.class);
            bval = continueWithArgumentRequest.suppressionOfAnnouncement = xml.get(SUPPRESSION_OF_ANNOUNCEMENT,
                    Boolean.class);
            if (bval != null)
                continueWithArgumentRequest.naOliInfo = xml.get(NA_OLI_INFO, NAOliInfoImpl.class);
            bval = xml.get(BOR_INTERROGATION_REQUESTED, Boolean.class);
            if (bval != null)
                continueWithArgumentRequest.borInterrogationRequested = bval;
            bval = xml.get(SUPPRESS_OCSI, Boolean.class);
            if (bval != null)
                continueWithArgumentRequest.suppressOCsi = bval;
            continueWithArgumentRequest.continueWithArgumentArgExtension = xml.get(CONTINUE_WITH_ARGUMENT_ARG_EXTENSION,
                    ContinueWithArgumentArgExtensionImpl.class);
        }

        public void write(ContinueWithArgumentRequestImpl continueWithArgumentRequest,
                javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {

            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(continueWithArgumentRequest, xml);

            if (continueWithArgumentRequest.getAlertingPattern() != null)
                xml.add((AlertingPatternCapImpl) continueWithArgumentRequest.getAlertingPattern(), ALERTING_PATTERN,
                        AlertingPatternCapImpl.class);

            if (continueWithArgumentRequest.getExtensions() != null)
                xml.add((CAPExtensionsImpl) continueWithArgumentRequest.getExtensions(), EXTENSIONS,
                        CAPExtensionsImpl.class);

            if (continueWithArgumentRequest.getServiceInteractionIndicatorsTwo() != null)
                xml.add((ServiceInteractionIndicatorsTwoImpl) continueWithArgumentRequest
                        .getServiceInteractionIndicatorsTwo(), SERVICE_INTERACTION_INDICATORS_TWO,
                        ServiceInteractionIndicatorsTwoImpl.class);

            if (continueWithArgumentRequest.getCallingPartysCategory() != null)
                xml.add((CallingPartysCategoryInapImpl) continueWithArgumentRequest.getCallingPartysCategory(),
                        CALLING_PARTYS_CATEGORY, CallingPartysCategoryInapImpl.class);

            if (continueWithArgumentRequest.getGenericNumbers() != null) {
                ContinueWithArgument_GenericNumbers al = new ContinueWithArgument_GenericNumbers(
                        continueWithArgumentRequest.getGenericNumbers());
                xml.add(al, GENERIC_NUMBER_LIST, ContinueWithArgument_GenericNumbers.class);
            }

            if (continueWithArgumentRequest.getCugInterlock() != null)
                xml.add((CUGInterlockImpl) continueWithArgumentRequest.getCugInterlock(), CUG_INTER_LOCK,
                        CUGInterlockImpl.class);

            if (continueWithArgumentRequest.getCugOutgoingAccess())
                xml.add((Boolean) continueWithArgumentRequest.getCugOutgoingAccess(), CUG_OUTGOING_ACCESS,
                        Boolean.class);

            if (continueWithArgumentRequest.getChargeNumber() != null)
                xml.add((LocationNumberCapImpl) continueWithArgumentRequest.getChargeNumber(), CHARGE_NUMBER,
                        LocationNumberCapImpl.class);

            if (continueWithArgumentRequest.getSuppressionOfAnnouncement())
                xml.add((Boolean) continueWithArgumentRequest.getSuppressionOfAnnouncement(),
                        SUPPRESSION_OF_ANNOUNCEMENT, Boolean.class);

            if (continueWithArgumentRequest.getNaOliInfo() != null)
                xml.add((NAOliInfoImpl) continueWithArgumentRequest.getNaOliInfo(), NA_OLI_INFO, NAOliInfoImpl.class);

            if (continueWithArgumentRequest.getBorInterrogationRequested())
                xml.add((Boolean) continueWithArgumentRequest.getBorInterrogationRequested(),
                        BOR_INTERROGATION_REQUESTED, Boolean.class);

            if (continueWithArgumentRequest.getSuppressOCsi())
                xml.add((Boolean) continueWithArgumentRequest.getSuppressOCsi(), SUPPRESS_OCSI, Boolean.class);

            if (continueWithArgumentRequest.getContinueWithArgumentArgExtension() != null)
                xml.add((ContinueWithArgumentArgExtensionImpl) continueWithArgumentRequest
                        .getContinueWithArgumentArgExtension(), CONTINUE_WITH_ARGUMENT_ARG_EXTENSION,
                        ContinueWithArgumentArgExtensionImpl.class);
        }
    };

    public static class ContinueWithArgument_GenericNumbers extends ArrayListSerializingBase<GenericNumberCap> {

        public ContinueWithArgument_GenericNumbers() {
            super(GENERIC_NUMBER, GenericNumberCapImpl.class);
        }

        public ContinueWithArgument_GenericNumbers(ArrayList<GenericNumberCap> data) {
            super(GENERIC_NUMBER, GenericNumberCapImpl.class, data);
        }

    }

    @Override
    public AlertingPatternCap getAlertingPattern() {
        return this.alertingPattern;
    }

    @Override
    public CAPExtensions getExtensions() {
        return this.extensions;
    }

    @Override
    public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
        return this.serviceInteractionIndicatorsTwo;
    }

    @Override
    public CallingPartysCategoryInap getCallingPartysCategory() {
        return this.callingPartysCategory;
    }

    @Override
    public ArrayList<GenericNumberCap> getGenericNumbers() {
        return this.genericNumbers;
    }

    @Override
    public CUGInterlock getCugInterlock() {
        return this.cugInterlock;
    }

    @Override
    public boolean getCugOutgoingAccess() {
        return this.cugOutgoingAccess;
    }

    @Override
    public LocationNumberCap getChargeNumber() {
        return this.chargeNumber;
    }

    @Override
    public Carrier getCarrier() {
        return this.carrier;
    }

    @Override
    public boolean getSuppressionOfAnnouncement() {
        return this.suppressionOfAnnouncement;
    }

    @Override
    public NAOliInfo getNaOliInfo() {
        return this.naOliInfo;
    }

    @Override
    public boolean getBorInterrogationRequested() {
        return this.borInterrogationRequested;
    }

    @Override
    public boolean getSuppressOCsi() {
        return this.suppressOCsi;
    }

    @Override
    public ContinueWithArgumentArgExtension getContinueWithArgumentArgExtension() {
        return this.continueWithArgumentArgExtension;
    }

}
