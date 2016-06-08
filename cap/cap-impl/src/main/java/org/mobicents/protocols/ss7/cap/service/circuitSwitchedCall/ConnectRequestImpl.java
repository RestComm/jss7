    /*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.RedirectingPartyIDCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCap;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddress;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.isup.GenericNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.LocationNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.OriginalCalledNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.RedirectingPartyIDCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.isup.CallingPartysCategoryInapImpl;
import org.mobicents.protocols.ss7.inap.isup.RedirectionInformationInapImpl;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.primitives.ArrayListSerializingBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CUGInterlockImpl;

/**
 *
 * @author sergey vetyutnev
 * @author tamas gyorgyey
 */
public class ConnectRequestImpl extends CircuitSwitchedCallMessageImpl implements ConnectRequest {

    public static final int _ID_destinationRoutingAddress = 0;
    public static final int _ID_alertingPattern = 1;
    public static final int _ID_originalCalledPartyID = 6;
    public static final int _ID_extensions = 10;
    public static final int _ID_carrier = 11;
    public static final int _ID_callingPartysCategory = 28;
    public static final int _ID_redirectingPartyID = 29;
    public static final int _ID_redirectionInformation = 30;
    public static final int _ID_genericNumbers = 14;
    public static final int _ID_serviceInteractionIndicatorsTwo = 15;
    public static final int _ID_chargeNumber = 19;
    public static final int _ID_legToBeConnected = 21;
    public static final int _ID_cug_Interlock = 31;
    public static final int _ID_cug_OutgoingAccess = 32;
    public static final int _ID_suppressionOfAnnouncement = 55;
    public static final int _ID_oCSIApplicable = 56;
    public static final int _ID_naOliInfo = 57;
    public static final int _ID_bor_InterrogationRequested = 58;
    public static final int _ID_suppressNCSI = 59;

    private static final String DESTINATION_ROUTING_ADDRESS = "destinationRoutingAddress";
    private static final String ALERTING_PATTERN = "alertingPattern";
    private static final String ORIGINAL_CALLED_PARTY_ID = "originalCalledPartyID";
    private static final String EXTENSIONS = "extensions";
    private static final String CARRIER = "carrier";
    private static final String CALLING_PARTYS_CATEGORY = "callingPartysCategory";
    private static final String REDIRECTING_PARTY_ID = "redirectingPartyID";
    private static final String REDIRECTION_INFORMATION = "redirectionInformation";
    private static final String GENERIC_NUMBER = "genericNumber";
    private static final String GENERIC_NUMBER_LIST = "genericNumbersList";
    private static final String SERVICE_INTERACTION_INDICATORS_TWO = "serviceInteractionIndicatorsTwo";
    private static final String CHARGE_NUMBER = "chargeNumber";
    private static final String LEG_TO_BE_CONNECTED = "legToBeConnected";
    private static final String CUG_INTERLOCK = "cugInterlock";
    private static final String CUG_OUTGOING_ACCESS = "cugOutgoingAccess";
    private static final String SUPPRESSION_OF_ANNOUNCEMENT = "suppressionOfAnnouncement";
    private static final String OCSI_APPLICABLE = "OCSIApplicable";
    private static final String NA_OLI_INFO = "NAOliInfo";
    private static final String BOR_INTERROGATION_REQUESTED = "borInterrogationRequested";
    private static final String SUPPRESS_N_CSI = "suppressNCSI";

    public static final String _PrimitiveName = "ConnectRequestIndication";

    private DestinationRoutingAddress destinationRoutingAddress;
    private AlertingPatternCap alertingPattern;
    private OriginalCalledNumberCap originalCalledPartyID;
    private CAPExtensions extensions;
    private Carrier carrier;
    private CallingPartysCategoryInap callingPartysCategory;
    private RedirectingPartyIDCap redirectingPartyID;
    private RedirectionInformationInap redirectionInformation;
    private ArrayList<GenericNumberCap> genericNumbers;
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
    private LocationNumberCap chargeNumber;
    private LegID legToBeConnected;
    private CUGInterlock cugInterlock;
    private boolean cugOutgoingAccess;
    private boolean suppressionOfAnnouncement;
    private boolean ocsIApplicable;
    private NAOliInfo naoliInfo;
    private boolean borInterrogationRequested;
    private boolean suppressNCSI;

    public ConnectRequestImpl() {
    }

    public ConnectRequestImpl(DestinationRoutingAddress destinationRoutingAddress, AlertingPatternCap alertingPattern,
            OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions, Carrier carrier,
            CallingPartysCategoryInap callingPartysCategory, RedirectingPartyIDCap redirectingPartyID,
            RedirectionInformationInap redirectionInformation, ArrayList<GenericNumberCap> genericNumbers,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberCap chargeNumber,
            LegID legToBeConnected, CUGInterlock cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) {
        this.destinationRoutingAddress = destinationRoutingAddress;
        this.alertingPattern = alertingPattern;
        this.originalCalledPartyID = originalCalledPartyID;
        this.extensions = extensions;
        this.carrier = carrier;
        this.callingPartysCategory = callingPartysCategory;
        this.redirectingPartyID = redirectingPartyID;
        this.redirectionInformation = redirectionInformation;
        this.genericNumbers = genericNumbers;
        this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
        this.chargeNumber = chargeNumber;
        this.legToBeConnected = legToBeConnected;
        this.cugInterlock = cugInterlock;
        this.cugOutgoingAccess = cugOutgoingAccess;
        this.suppressionOfAnnouncement = suppressionOfAnnouncement;
        this.ocsIApplicable = ocsIApplicable;
        this.naoliInfo = naoliInfo;
        this.borInterrogationRequested = borInterrogationRequested;
        this.suppressNCSI = suppressNCSI;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.connect_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.connect;
    }

    @Override
    public DestinationRoutingAddress getDestinationRoutingAddress() {
        return destinationRoutingAddress;
    }

    @Override
    public AlertingPatternCap getAlertingPattern() {
        return alertingPattern;
    }

    @Override
    public OriginalCalledNumberCap getOriginalCalledPartyID() {
        return originalCalledPartyID;
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
    public CallingPartysCategoryInap getCallingPartysCategory() {
        return callingPartysCategory;
    }

    @Override
    public RedirectingPartyIDCap getRedirectingPartyID() {
        return redirectingPartyID;
    }

    @Override
    public RedirectionInformationInap getRedirectionInformation() {
        return redirectionInformation;
    }

    @Override
    public ArrayList<GenericNumberCap> getGenericNumbers() {
        return genericNumbers;
    }

    @Override
    public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
        return serviceInteractionIndicatorsTwo;
    }

    @Override
    public LocationNumberCap getChargeNumber() {
        return chargeNumber;
    }

    @Override
    public LegID getLegToBeConnected() {
        return legToBeConnected;
    }

    @Override
    public CUGInterlock getCUGInterlock() {
        return cugInterlock;
    }

    @Override
    public boolean getCugOutgoingAccess() {
        return cugOutgoingAccess;
    }

    @Override
    public boolean getSuppressionOfAnnouncement() {
        return suppressionOfAnnouncement;
    }

    @Override
    public boolean getOCSIApplicable() {
        return ocsIApplicable;
    }

    @Override
    public NAOliInfo getNAOliInfo() {
        return naoliInfo;
    }

    @Override
    public boolean getBorInterrogationRequested() {
        return borInterrogationRequested;
    }

    @Override
    public boolean getSuppressNCSI() {
        return suppressNCSI;
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
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName + ": "
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
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            INAPParsingComponentException, IOException, AsnException {

        this.destinationRoutingAddress = null;
        this.alertingPattern = null;
        this.originalCalledPartyID = null;
        this.extensions = null;
        this.carrier = null;
        this.callingPartysCategory = null;
        this.redirectingPartyID = null;
        this.redirectionInformation = null;
        this.genericNumbers = null;
        this.serviceInteractionIndicatorsTwo = null;
        this.chargeNumber = null;
        this.legToBeConnected = null;
        this.cugInterlock = null;
        this.cugOutgoingAccess = false;
        this.suppressionOfAnnouncement = false;
        this.ocsIApplicable = false;
        this.naoliInfo = null;
        this.borInterrogationRequested = false;
        this.suppressNCSI = false;

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
                case _ID_alertingPattern:
                    this.alertingPattern = new AlertingPatternCapImpl();
                    ((AlertingPatternCapImpl) this.alertingPattern).decodeAll(ais);
                    break;
                case _ID_originalCalledPartyID:
                    this.originalCalledPartyID = new OriginalCalledNumberCapImpl();
                    ((OriginalCalledNumberCapImpl) this.originalCalledPartyID).decodeAll(ais);
                    break;
                case _ID_extensions:
                    this.extensions = new CAPExtensionsImpl();
                    ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
                    break;
                case _ID_carrier:
                    this.carrier = new CarrierImpl();
                    ((CarrierImpl) this.carrier).decodeAll(ais);
                    break;
                case _ID_callingPartysCategory:
                    this.callingPartysCategory = new CallingPartysCategoryInapImpl();
                    ((CallingPartysCategoryInapImpl) this.callingPartysCategory).decodeAll(ais);
                    break;
                case _ID_redirectingPartyID:
                    this.redirectingPartyID = new RedirectingPartyIDCapImpl();
                    ((RedirectingPartyIDCapImpl) this.redirectingPartyID).decodeAll(ais);
                    break;
                case _ID_redirectionInformation:
                    this.redirectionInformation = new RedirectionInformationInapImpl();
                    ((RedirectionInformationInapImpl) this.redirectionInformation).decodeAll(ais);
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
                case _ID_serviceInteractionIndicatorsTwo:
                    this.serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl();
                    ((ServiceInteractionIndicatorsTwoImpl) this.serviceInteractionIndicatorsTwo).decodeAll(ais);
                    break;
                case _ID_chargeNumber:
                    this.chargeNumber = new LocationNumberCapImpl();
                    ((LocationNumberCapImpl) this.chargeNumber).decodeAll(ais);
                    break;
                case _ID_legToBeConnected:
                    ais2 = ais.readSequenceStream();
                    ais2.readTag();
                    this.legToBeConnected = new LegIDImpl();
                    ((LegIDImpl) this.legToBeConnected).decodeAll(ais2);
                    break;
                case _ID_cug_Interlock:
                    this.cugInterlock = new CUGInterlockImpl();
                    ((CUGInterlockImpl) this.cugInterlock).decodeAll(ais);
                    break;
                case _ID_cug_OutgoingAccess:
                    ais.readNull();
                    this.cugOutgoingAccess = true;
                    break;
                case _ID_suppressionOfAnnouncement:
                    ais.readNull();
                    this.suppressionOfAnnouncement = true;
                    break;
                case _ID_oCSIApplicable:
                    ais.readNull();
                    this.ocsIApplicable = true;
                    break;
                case _ID_naOliInfo:
                    this.naoliInfo = new NAOliInfoImpl();
                    ((NAOliInfoImpl) this.naoliInfo).decodeAll(ais);
                    break;
                case _ID_bor_InterrogationRequested:
                    ais.readNull();
                    this.borInterrogationRequested = true;
                    break;
                case _ID_suppressNCSI:
                    ais.readNull();
                    this.suppressNCSI = true;
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

        if (this.destinationRoutingAddress == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": destinationRoutingAddress must not be null");

        try {
            ((DestinationRoutingAddressImpl) this.destinationRoutingAddress).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                    _ID_destinationRoutingAddress);

            if (this.alertingPattern != null)
                ((AlertingPatternCapImpl) this.alertingPattern).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_alertingPattern);
            if (this.originalCalledPartyID != null)
                ((OriginalCalledNumberCapImpl) this.originalCalledPartyID).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_originalCalledPartyID);
            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);
            if (this.carrier != null) {
                ((CarrierImpl) this.carrier).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_carrier);
            }
            if (this.callingPartysCategory != null)
                ((CallingPartysCategoryInapImpl) this.callingPartysCategory).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_callingPartysCategory);
            if (this.redirectingPartyID != null)
                ((RedirectingPartyIDCapImpl) this.redirectingPartyID).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_redirectingPartyID);
            if (this.redirectionInformation != null)
                ((RedirectionInformationInapImpl) this.redirectionInformation).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_redirectionInformation);

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
            if (this.serviceInteractionIndicatorsTwo != null) {
                ((ServiceInteractionIndicatorsTwoImpl) this.serviceInteractionIndicatorsTwo).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_serviceInteractionIndicatorsTwo);
            }
            if (this.chargeNumber != null) {
                ((LocationNumberCapImpl) this.chargeNumber).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_chargeNumber);
            }
            if (this.legToBeConnected != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_legToBeConnected);
                int pos = aos.StartContentDefiniteLength();
                ((LegIDImpl) this.legToBeConnected).encodeAll(aos);
                aos.FinalizeContent(pos);
            }
            if (this.cugInterlock != null) {
                ((CUGInterlockImpl) this.cugInterlock).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_cug_Interlock);
            }
            if (this.cugOutgoingAccess) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_cug_OutgoingAccess);
            }
            if (this.suppressionOfAnnouncement)
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_suppressionOfAnnouncement);
            if (this.ocsIApplicable)
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_oCSIApplicable);
            if (this.naoliInfo != null)
                ((NAOliInfoImpl) this.naoliInfo).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_naOliInfo);
            if (this.borInterrogationRequested) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_bor_InterrogationRequested);
            }
            if (this.suppressNCSI) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_suppressNCSI);
            }

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

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        this.addInvokeIdInfo(sb);

        if (this.destinationRoutingAddress != null) {
            sb.append(", destinationRoutingAddress=");
            sb.append(destinationRoutingAddress.toString());
        }
        if (this.alertingPattern != null) {
            sb.append(", alertingPattern=");
            sb.append(alertingPattern.toString());
        }
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier.toString());
        }
        if (this.callingPartysCategory != null) {
            sb.append(", callingPartysCategory=");
            sb.append(callingPartysCategory.toString());
        }
        if (this.redirectingPartyID != null) {
            sb.append(", redirectingPartyID=");
            sb.append(redirectingPartyID.toString());
        }
        if (this.redirectionInformation != null) {
            sb.append(", redirectionInformation=");
            sb.append(redirectionInformation.toString());
        }
        if (this.genericNumbers != null) {
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
        if (this.serviceInteractionIndicatorsTwo != null) {
            sb.append(", serviceInteractionIndicatorsTwo=");
            sb.append(serviceInteractionIndicatorsTwo.toString());
        }
        if (this.chargeNumber != null) {
            sb.append(", chargeNumber=");
            sb.append(chargeNumber.toString());
        }
        if (this.legToBeConnected != null) {
            sb.append(", legToBeConnected=");
            sb.append(legToBeConnected.toString());
        }
        if (this.cugInterlock != null) {
            sb.append(", cugInterlock=");
            sb.append(cugInterlock.toString());
        }
        if (this.cugOutgoingAccess) {
            sb.append(", cugOutgoingAccess");
        }
        if (suppressionOfAnnouncement) {
            sb.append(", suppressionOfAnnouncement");
        }
        if (ocsIApplicable) {
            sb.append(", ocsIApplicable");
        }
        if (this.naoliInfo != null) {
            sb.append(", naoliInfo=");
            sb.append(naoliInfo.toString());
        }
        if (this.borInterrogationRequested) {
            sb.append(", borInterrogationRequested");
        }
        if (this.suppressNCSI) {
            sb.append(", suppressNCSI");
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ConnectRequestImpl> CONNECT_REQUEST_XML = new XMLFormat<ConnectRequestImpl>(
            ConnectRequestImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ConnectRequestImpl connectRequest)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, connectRequest);

            connectRequest.destinationRoutingAddress = xml
                    .get(DESTINATION_ROUTING_ADDRESS, DestinationRoutingAddressImpl.class);
            connectRequest.alertingPattern = xml.get(ALERTING_PATTERN, AlertingPatternCapImpl.class);
            connectRequest.originalCalledPartyID = xml.get(ORIGINAL_CALLED_PARTY_ID, OriginalCalledNumberCapImpl.class);
            connectRequest.extensions = xml.get(EXTENSIONS, CAPExtensionsImpl.class);
            connectRequest.carrier = xml.get(CARRIER, CarrierImpl.class);
            connectRequest.callingPartysCategory = xml.get(CALLING_PARTYS_CATEGORY, CallingPartysCategoryInapImpl.class);

            connectRequest.redirectingPartyID = xml.get(REDIRECTING_PARTY_ID, RedirectingPartyIDCapImpl.class);
            connectRequest.redirectionInformation = xml.get(REDIRECTION_INFORMATION, RedirectionInformationInapImpl.class);

            ConnectRequest_GenericNumbers al = xml.get(GENERIC_NUMBER_LIST, ConnectRequest_GenericNumbers.class);
            if (al != null) {
                connectRequest.genericNumbers = al.getData();
            }

            connectRequest.serviceInteractionIndicatorsTwo = xml.get(SERVICE_INTERACTION_INDICATORS_TWO, ServiceInteractionIndicatorsTwoImpl.class);
            connectRequest.chargeNumber = xml.get(CHARGE_NUMBER, LocationNumberCapImpl.class);
            connectRequest.legToBeConnected = xml.get(LEG_TO_BE_CONNECTED, LegIDImpl.class);
            connectRequest.cugInterlock = xml.get(CUG_INTERLOCK, CUGInterlockImpl.class);

            Boolean bval = xml.get(CUG_OUTGOING_ACCESS, Boolean.class);
            if (bval != null)
                connectRequest.cugOutgoingAccess = bval;
            bval = xml.get(SUPPRESSION_OF_ANNOUNCEMENT, Boolean.class);
            if (bval != null)
                connectRequest.suppressionOfAnnouncement = bval;
            bval = xml.get(OCSI_APPLICABLE, Boolean.class);
            if (bval != null)
                connectRequest.ocsIApplicable = bval;

            connectRequest.naoliInfo = xml.get(NA_OLI_INFO, NAOliInfoImpl.class);

            bval = xml.get(BOR_INTERROGATION_REQUESTED, Boolean.class);
            if (bval != null)
                connectRequest.borInterrogationRequested = bval;
            bval = xml.get(SUPPRESS_N_CSI, Boolean.class);
            if (bval != null)
                connectRequest.suppressNCSI = bval;

        }

        @Override
        public void write(ConnectRequestImpl connectRequest, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(connectRequest, xml);

            if (connectRequest.getDestinationRoutingAddress() != null)
                xml.add((DestinationRoutingAddressImpl) connectRequest.getDestinationRoutingAddress(),
                        DESTINATION_ROUTING_ADDRESS, DestinationRoutingAddressImpl.class);
            if (connectRequest.getAlertingPattern() != null)
                xml.add((AlertingPatternCapImpl) connectRequest.getAlertingPattern(), ALERTING_PATTERN,
                        AlertingPatternCapImpl.class);
            if (connectRequest.getOriginalCalledPartyID() != null)
                xml.add((OriginalCalledNumberCapImpl) connectRequest.getOriginalCalledPartyID(), ORIGINAL_CALLED_PARTY_ID,
                        OriginalCalledNumberCapImpl.class);
            if (connectRequest.getExtensions() != null)
                xml.add((CAPExtensionsImpl) connectRequest.getExtensions(), EXTENSIONS, CAPExtensionsImpl.class);
            if (connectRequest.carrier != null)
                xml.add((CarrierImpl) connectRequest.carrier, CARRIER, CarrierImpl.class);
            if (connectRequest.getCallingPartysCategory() != null)
                xml.add((CallingPartysCategoryInapImpl) connectRequest.getCallingPartysCategory(), CALLING_PARTYS_CATEGORY,
                        CallingPartysCategoryInapImpl.class);

            if (connectRequest.getRedirectingPartyID() != null)
                xml.add((RedirectingPartyIDCapImpl) connectRequest.getRedirectingPartyID(), REDIRECTING_PARTY_ID,
                        RedirectingPartyIDCapImpl.class);
            if (connectRequest.getRedirectionInformation() != null)
                xml.add((RedirectionInformationInapImpl) connectRequest.getRedirectionInformation(), REDIRECTION_INFORMATION,
                        RedirectionInformationInapImpl.class);

            if (connectRequest.getGenericNumbers() != null) {
                ConnectRequest_GenericNumbers al = new ConnectRequest_GenericNumbers(connectRequest.getGenericNumbers());
                xml.add(al, GENERIC_NUMBER_LIST, ConnectRequest_GenericNumbers.class);
            }

            if (connectRequest.serviceInteractionIndicatorsTwo != null)
                xml.add((ServiceInteractionIndicatorsTwoImpl) connectRequest.serviceInteractionIndicatorsTwo, SERVICE_INTERACTION_INDICATORS_TWO,
                        ServiceInteractionIndicatorsTwoImpl.class);
            if (connectRequest.chargeNumber != null)
                xml.add((LocationNumberCapImpl) connectRequest.chargeNumber, CHARGE_NUMBER, LocationNumberCapImpl.class);
            if (connectRequest.legToBeConnected != null)
                xml.add((LegIDImpl) connectRequest.legToBeConnected, LEG_TO_BE_CONNECTED, LegIDImpl.class);
            if (connectRequest.cugInterlock != null)
                xml.add((CUGInterlockImpl) connectRequest.cugInterlock, CUG_INTERLOCK, CUGInterlockImpl.class);

            if (connectRequest.cugOutgoingAccess)
                xml.add(true, CUG_OUTGOING_ACCESS, Boolean.class);
            if (connectRequest.getSuppressionOfAnnouncement())
                xml.add(true, SUPPRESSION_OF_ANNOUNCEMENT, Boolean.class);
            if (connectRequest.getOCSIApplicable())
                xml.add(true, OCSI_APPLICABLE, Boolean.class);

            if (connectRequest.getNAOliInfo() != null)
                xml.add((NAOliInfoImpl) connectRequest.getNAOliInfo(), NA_OLI_INFO, NAOliInfoImpl.class);

            if (connectRequest.borInterrogationRequested)
                xml.add(true, BOR_INTERROGATION_REQUESTED, Boolean.class);
            if (connectRequest.suppressNCSI)
                xml.add(true, SUPPRESS_N_CSI, Boolean.class);
        }
    };

    public static class ConnectRequest_GenericNumbers extends ArrayListSerializingBase<GenericNumberCap> {

        public ConnectRequest_GenericNumbers() {
            super(GENERIC_NUMBER, GenericNumberCapImpl.class);
        }

        public ConnectRequest_GenericNumbers(ArrayList<GenericNumberCap> data) {
            super(GENERIC_NUMBER, GenericNumberCapImpl.class, data);
        }

    }
}
