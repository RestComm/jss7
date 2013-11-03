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
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.LowLayerCompatibility;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.UUData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4Functionalities;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class InitialDPArgExtensionImpl implements InitialDPArgExtension, CAPAsnPrimitive {

    public static final int _ID_gmscAddress = 0;
    public static final int _ID_forwardingDestinationNumber = 1;
    public static final int _ID_ms_Classmark2 = 2;
    public static final int _ID_iMEI = 3;
    public static final int _ID_supportedCamelPhases = 4;
    public static final int _ID_offeredCamel4Functionalities = 5;
    public static final int _ID_bearerCapability2 = 6;
    public static final int _ID_ext_basicServiceCode2 = 7;
    public static final int _ID_highLayerCompatibility2 = 8;
    public static final int _ID_lowLayerCompatibility = 9;
    public static final int _ID_lowLayerCompatibility2 = 10;
    public static final int _ID_enhancedDialledServicesAllowed = 11;
    public static final int _ID_uu_Data = 12;

    private static final String IS_CAP_VERSION_3_OR_LATER = "isCAPVersion3orLater";
    private static final String GMSC_ADDRESS = "gmscAddress";
    private static final String FORWARDING_DESTINATION_NUMBER = "forwardingDestinationNumber";

    public static final String _PrimitiveName = "InitialDPArgExtension";

    private ISDNAddressString gmscAddress;
    private CalledPartyNumberCap forwardingDestinationNumber;
    private MSClassmark2 msClassmark2;
    private IMEI imei;
    private SupportedCamelPhases supportedCamelPhases;
    private OfferedCamel4Functionalities offeredCamel4Functionalities;
    private BearerCapability bearerCapability2;
    private ExtBasicServiceCode extBasicServiceCode2;
    private HighLayerCompatibilityInap highLayerCompatibility2;
    private LowLayerCompatibility lowLayerCompatibility;
    private LowLayerCompatibility lowLayerCompatibility2;
    private boolean enhancedDialledServicesAllowed;
    private UUData uuData;

    protected boolean isCAPVersion3orLater;

    /**
     * This constructor is for deserializing purposes
     */
    public InitialDPArgExtensionImpl() {
    }

    public InitialDPArgExtensionImpl(boolean isCAPVersion3orLater) {
        this.isCAPVersion3orLater = isCAPVersion3orLater;
    }

    public InitialDPArgExtensionImpl(ISDNAddressString gmscAddress, CalledPartyNumberCap forwardingDestinationNumber,
            MSClassmark2 msClassmark2, IMEI imei, SupportedCamelPhases supportedCamelPhases,
            OfferedCamel4Functionalities offeredCamel4Functionalities, BearerCapability bearerCapability2,
            ExtBasicServiceCode extBasicServiceCode2, HighLayerCompatibilityInap highLayerCompatibility2,
            LowLayerCompatibility lowLayerCompatibility, LowLayerCompatibility lowLayerCompatibility2,
            boolean enhancedDialledServicesAllowed, UUData uuData, boolean isCAPVersion3orLater) {
        this.gmscAddress = gmscAddress;
        this.forwardingDestinationNumber = forwardingDestinationNumber;
        this.msClassmark2 = msClassmark2;
        this.imei = imei;
        this.supportedCamelPhases = supportedCamelPhases;
        this.offeredCamel4Functionalities = offeredCamel4Functionalities;
        this.bearerCapability2 = bearerCapability2;
        this.extBasicServiceCode2 = extBasicServiceCode2;
        this.highLayerCompatibility2 = highLayerCompatibility2;
        this.lowLayerCompatibility = lowLayerCompatibility;
        this.lowLayerCompatibility2 = lowLayerCompatibility2;
        this.enhancedDialledServicesAllowed = enhancedDialledServicesAllowed;
        this.uuData = uuData;
        this.isCAPVersion3orLater = isCAPVersion3orLater;
    }

    @Override
    public ISDNAddressString getGmscAddress() {
        return gmscAddress;
    }

    @Override
    public CalledPartyNumberCap getForwardingDestinationNumber() {
        return forwardingDestinationNumber;
    }

    @Override
    public MSClassmark2 getMSClassmark2() {
        return msClassmark2;
    }

    @Override
    public IMEI getIMEI() {
        return imei;
    }

    @Override
    public SupportedCamelPhases getSupportedCamelPhases() {
        return supportedCamelPhases;
    }

    @Override
    public OfferedCamel4Functionalities getOfferedCamel4Functionalities() {
        return offeredCamel4Functionalities;
    }

    @Override
    public BearerCapability getBearerCapability2() {
        return bearerCapability2;
    }

    @Override
    public ExtBasicServiceCode getExtBasicServiceCode2() {
        return extBasicServiceCode2;
    }

    @Override
    public HighLayerCompatibilityInap getHighLayerCompatibility2() {
        return highLayerCompatibility2;
    }

    @Override
    public LowLayerCompatibility getLowLayerCompatibility() {
        return lowLayerCompatibility;
    }

    @Override
    public LowLayerCompatibility getLowLayerCompatibility2() {
        return lowLayerCompatibility2;
    }

    @Override
    public boolean getEnhancedDialledServicesAllowed() {
        return enhancedDialledServicesAllowed;
    }

    @Override
    public UUData getUUData() {
        return uuData;
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

        this.gmscAddress = null;
        this.forwardingDestinationNumber = null;
        this.msClassmark2 = null;
        this.imei = null;
        this.supportedCamelPhases = null;
        this.offeredCamel4Functionalities = null;
        this.bearerCapability2 = null;
        this.extBasicServiceCode2 = null;
        this.highLayerCompatibility2 = null;
        this.lowLayerCompatibility = null;
        this.lowLayerCompatibility2 = null;
        this.enhancedDialledServicesAllowed = false;
        this.uuData = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_gmscAddress:
                        if (isCAPVersion3orLater) {
                            this.gmscAddress = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.gmscAddress).decodeAll(ais);
                        } else {
                            // in CAP V2 naCarrierInformation parameter - we do not implement it
                            ais.advanceElement();
                        }
                        break;
                    case _ID_forwardingDestinationNumber:
                        if (isCAPVersion3orLater) {
                            this.forwardingDestinationNumber = new CalledPartyNumberCapImpl();
                            ((CalledPartyNumberCapImpl) this.forwardingDestinationNumber).decodeAll(ais);
                        } else {
                            // in CAP V2 gmscAddress parameter
                            this.gmscAddress = new ISDNAddressStringImpl();
                            ((ISDNAddressStringImpl) this.gmscAddress).decodeAll(ais);
                        }
                        break;
                    case _ID_ms_Classmark2:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_iMEI:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_supportedCamelPhases:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_offeredCamel4Functionalities:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_bearerCapability2:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_ext_basicServiceCode2:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_highLayerCompatibility2:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_lowLayerCompatibility:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_lowLayerCompatibility2:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_enhancedDialledServicesAllowed:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_uu_Data:
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
            if (isCAPVersion3orLater) {
                if (this.gmscAddress != null)
                    ((ISDNAddressStringImpl) this.gmscAddress).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_gmscAddress);
                if (this.forwardingDestinationNumber != null)
                    ((CalledPartyNumberCapImpl) this.forwardingDestinationNumber).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                            _ID_forwardingDestinationNumber);
            } else {
                if (this.gmscAddress != null)
                    ((ISDNAddressStringImpl) this.gmscAddress).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                            _ID_forwardingDestinationNumber);
            }

            if (msClassmark2 != null) {
                // TODO: implement it
            }
            if (imei != null) {
                // TODO: implement it
            }
            if (supportedCamelPhases != null) {
                // TODO: implement it
            }
            if (offeredCamel4Functionalities != null) {
                // TODO: implement it
            }
            if (bearerCapability2 != null) {
                // TODO: implement it
            }
            if (extBasicServiceCode2 != null) {
                // TODO: implement it
            }
            if (highLayerCompatibility2 != null) {
                // TODO: implement it
            }
            if (lowLayerCompatibility != null) {
                // TODO: implement it
            }
            if (lowLayerCompatibility2 != null) {
                // TODO: implement it
            }
            if (enhancedDialledServicesAllowed) {
                // TODO: implement it
            }
            if (uuData != null) {
                // TODO: implement it
            }
        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.gmscAddress != null) {
            sb.append(", gmscAddress=");
            sb.append(gmscAddress);
        }
        if (this.forwardingDestinationNumber != null) {
            sb.append(", forwardingDestinationNumber=");
            sb.append(forwardingDestinationNumber);
        }
        if (this.msClassmark2 != null) {
            sb.append(", msClassmark2=");
            sb.append(msClassmark2.toString());
        }
        if (this.imei != null) {
            sb.append(", imei=");
            sb.append(imei.toString());
        }
        if (this.supportedCamelPhases != null) {
            sb.append(", supportedCamelPhases=");
            sb.append(supportedCamelPhases.toString());
        }
        if (this.offeredCamel4Functionalities != null) {
            sb.append(", offeredCamel4Functionalities=");
            sb.append(offeredCamel4Functionalities.toString());
        }
        if (this.bearerCapability2 != null) {
            sb.append(", bearerCapability2=");
            sb.append(bearerCapability2.toString());
        }
        if (this.extBasicServiceCode2 != null) {
            sb.append(", extBasicServiceCode2=");
            sb.append(extBasicServiceCode2.toString());
        }
        if (this.highLayerCompatibility2 != null) {
            sb.append(", highLayerCompatibility2=");
            sb.append(highLayerCompatibility2.toString());
        }
        if (this.lowLayerCompatibility != null) {
            sb.append(", lowLayerCompatibility=");
            sb.append(lowLayerCompatibility.toString());
        }
        if (this.lowLayerCompatibility2 != null) {
            sb.append(", lowLayerCompatibility2=");
            sb.append(lowLayerCompatibility2.toString());
        }
        if (this.enhancedDialledServicesAllowed) {
            sb.append(", enhancedDialledServicesAllowed");
        }
        if (this.uuData != null) {
            sb.append(", uuData=");
            sb.append(uuData.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<InitialDPArgExtensionImpl> INITIAL_DP_ARG_EXTENSION_XML = new XMLFormat<InitialDPArgExtensionImpl>(
            InitialDPArgExtensionImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, InitialDPArgExtensionImpl initialDPArgExtension)
                throws XMLStreamException {
            initialDPArgExtension.isCAPVersion3orLater = xml.getAttribute(IS_CAP_VERSION_3_OR_LATER, false);

            initialDPArgExtension.gmscAddress = xml.get(GMSC_ADDRESS, ISDNAddressStringImpl.class);
            initialDPArgExtension.forwardingDestinationNumber = xml.get(FORWARDING_DESTINATION_NUMBER,
                    CalledPartyNumberCapImpl.class);
        }

        @Override
        public void write(InitialDPArgExtensionImpl initialDPArgExtension, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            xml.setAttribute(IS_CAP_VERSION_3_OR_LATER, initialDPArgExtension.isCAPVersion3orLater);

            if (initialDPArgExtension.getGmscAddress() != null)
                xml.add((ISDNAddressStringImpl) initialDPArgExtension.getGmscAddress(), GMSC_ADDRESS,
                        ISDNAddressStringImpl.class);
            if (initialDPArgExtension.getForwardingDestinationNumber() != null)
                xml.add((CalledPartyNumberCapImpl) initialDPArgExtension.getForwardingDestinationNumber(),
                        FORWARDING_DESTINATION_NUMBER, CalledPartyNumberCapImpl.class);
        }
    };
}
