/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationNumberMap;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.UserCSGInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class LocationInformationImpl implements LocationInformation, MAPAsnPrimitive {

    public static final int _ID_geographicalInformation = 0;
    public static final int _ID_vlr_number = 1;
    public static final int _ID_locationNumber = 2;
    public static final int _ID_cellGlobalIdOrServiceAreaIdOrLAI = 3;
    public static final int _ID_extensionContainer = 4;
    public static final int _ID_selectedLSA_Id = 5;
    public static final int _ID_msc_Number = 6;
    public static final int _ID_geodeticInformation = 7;
    public static final int _ID_currentLocationRetrieved = 8;
    public static final int _ID_sai_Present = 9;
    public static final int _ID_locationInformationEPS = 10;
    public static final int _ID_userCSGInformation = 11;

    public static final String _PrimitiveName = "LocationInformation";

    private Integer ageOfLocationInformation;
    private GeographicalInformation geographicalInformation;
    private ISDNAddressString vlrNumber;
    private LocationNumberMap locationNumber;
    private CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
    private MAPExtensionContainer extensionContainer;
    private LSAIdentity selectedLSAId;
    private ISDNAddressString mscNumber;
    private GeodeticInformation geodeticInformation;
    private boolean currentLocationRetrieved;
    private boolean saiPresent;
    private LocationInformationEPS locationInformationEPS;
    private UserCSGInformation userCSGInformation;

    public LocationInformationImpl() {
    }

    public LocationInformationImpl(Integer ageOfLocationInformation, GeographicalInformation geographicalInformation,
            ISDNAddressString vlrNumber, LocationNumberMap locationNumber,
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, MAPExtensionContainer extensionContainer,
            LSAIdentity selectedLSAId, ISDNAddressString mscNumber, GeodeticInformation geodeticInformation,
            boolean currentLocationRetrieved, boolean saiPresent, LocationInformationEPS locationInformationEPS,
            UserCSGInformation userCSGInformation) {
        this.ageOfLocationInformation = ageOfLocationInformation;
        this.geographicalInformation = geographicalInformation;
        this.vlrNumber = vlrNumber;
        this.locationNumber = locationNumber;
        this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
        this.extensionContainer = extensionContainer;
        this.selectedLSAId = selectedLSAId;
        this.mscNumber = mscNumber;
        this.geodeticInformation = geodeticInformation;
        this.currentLocationRetrieved = currentLocationRetrieved;
        this.saiPresent = saiPresent;
        this.locationInformationEPS = locationInformationEPS;
        this.userCSGInformation = userCSGInformation;
    }

    public Integer getAgeOfLocationInformation() {
        return this.ageOfLocationInformation;
    }

    public GeographicalInformation getGeographicalInformation() {
        return this.geographicalInformation;
    }

    public ISDNAddressString getVlrNumber() {
        return this.vlrNumber;
    }

    public LocationNumberMap getLocationNumber() {
        return locationNumber;
    }

    public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
        return cellGlobalIdOrServiceAreaIdOrLAI;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public LSAIdentity getSelectedLSAId() {
        return selectedLSAId;
    }

    public ISDNAddressString getMscNumber() {
        return mscNumber;
    }

    public GeodeticInformation getGeodeticInformation() {
        return geodeticInformation;
    }

    public boolean getCurrentLocationRetrieved() {
        return currentLocationRetrieved;
    }

    public boolean getSaiPresent() {
        return saiPresent;
    }

    public LocationInformationEPS getLocationInformationEPS() {
        return locationInformationEPS;
    }

    public UserCSGInformation getUserCSGInformation() {
        return userCSGInformation;
    }

    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
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

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.ageOfLocationInformation = null;
        this.geographicalInformation = null;
        this.vlrNumber = null;
        this.locationNumber = null;
        this.cellGlobalIdOrServiceAreaIdOrLAI = null;
        this.extensionContainer = null;
        this.selectedLSAId = null;
        this.mscNumber = null;
        this.geodeticInformation = null;
        this.currentLocationRetrieved = false;
        this.saiPresent = false;
        this.locationInformationEPS = null;
        this.userCSGInformation = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            // optional parameters
            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

                switch (tag) {
                    case Tag.INTEGER: // AgeOfLocationInformation
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " AgeOfLocationInformation: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.ageOfLocationInformation = (int) ais.readInteger();
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                switch (tag) {
                    case _ID_geographicalInformation:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " geographicalInformation: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.geographicalInformation = new GeographicalInformationImpl();
                        ((GeographicalInformationImpl) this.geographicalInformation).decodeAll(ais);
                        break;
                    case _ID_vlr_number:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " vlrNumber: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.vlrNumber = new ISDNAddressStringImpl();
                        ((ISDNAddressStringImpl) this.vlrNumber).decodeAll(ais);
                        break;
                    case _ID_locationNumber:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " locationNumber: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.locationNumber = new LocationNumberMapImpl();
                        ((LocationNumberMapImpl) this.locationNumber).decodeAll(ais);
                        break;
                    case _ID_cellGlobalIdOrServiceAreaIdOrLAI:
                        this.cellGlobalIdOrServiceAreaIdOrLAI = LocationInformationGPRSImpl
                                .decodeCellGlobalIdOrServiceAreaIdOrLAI(ais, _PrimitiveName);
                        // if (ais.isTagPrimitive())
                        // throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                        // + " cellGlobalIdOrServiceAreaIdOrLAI: Parameter is primitive",
                        // MAPParsingComponentExceptionReason.MistypedParameter);
                        // this.cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl();
                        // AsnInputStream ais2 = ais.readSequenceStream();
                        // ais2.readTag();
                        // ((CellGlobalIdOrServiceAreaIdOrLAIImpl) this.cellGlobalIdOrServiceAreaIdOrLAI).decodeAll(ais2);
                        break;
                    case _ID_extensionContainer:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " extensionContainer: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;
                    case _ID_selectedLSA_Id:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " selectedLSAId: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.selectedLSAId = new LSAIdentityImpl();
                        ((LSAIdentityImpl) this.selectedLSAId).decodeAll(ais);
                        break;
                    case _ID_msc_Number:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " mscNumber: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.mscNumber = new ISDNAddressStringImpl();
                        ((ISDNAddressStringImpl) this.mscNumber).decodeAll(ais);
                        break;
                    case _ID_geodeticInformation:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " geodeticInformation: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.geodeticInformation = new GeodeticInformationImpl();
                        ((GeodeticInformationImpl) this.geodeticInformation).decodeAll(ais);
                        break;
                    case _ID_currentLocationRetrieved:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " currentLocationRetrieved: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.currentLocationRetrieved = true;
                        break;
                    case _ID_sai_Present:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " saiPresent: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.saiPresent = true;
                        break;
                    case _ID_locationInformationEPS:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " locationInformationEPS: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.locationInformationEPS = new LocationInformationEPSImpl();
                        ((LocationInformationEPSImpl) this.locationInformationEPS).decodeAll(ais);
                        break;
                    case _ID_userCSGInformation:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " userCSGInformation: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.userCSGInformation = new UserCSGInformationImpl();
                        ((UserCSGInformationImpl) this.userCSGInformation).decodeAll(ais);
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

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {

        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        try {
            if (ageOfLocationInformation != null)
                asnOs.writeInteger((int) ageOfLocationInformation);

            if (this.geographicalInformation != null)
                ((GeographicalInformationImpl) this.geographicalInformation).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_geographicalInformation);

            if (vlrNumber != null)
                ((ISDNAddressStringImpl) vlrNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_vlr_number);

            if (locationNumber != null)
                ((LocationNumberMapImpl) locationNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_locationNumber);

            if (cellGlobalIdOrServiceAreaIdOrLAI != null) {
                try {
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_cellGlobalIdOrServiceAreaIdOrLAI);
                    int pos = asnOs.StartContentDefiniteLength();
                    ((CellGlobalIdOrServiceAreaIdOrLAIImpl) this.cellGlobalIdOrServiceAreaIdOrLAI).encodeAll(asnOs);
                    asnOs.FinalizeContent(pos);
                } catch (AsnException e) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " the optional parameter cellGlobalIdOrServiceAreaIdOrLAI encoding failed ", e);
                }
            }

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_extensionContainer);

            if (this.selectedLSAId != null)
                ((LSAIdentityImpl) this.selectedLSAId).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_selectedLSA_Id);

            if (this.mscNumber != null) {
                ((ISDNAddressStringImpl) this.mscNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_msc_Number);
            }

            if (this.geodeticInformation != null)
                ((GeodeticInformationImpl) this.geodeticInformation).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_geodeticInformation);

            if (this.currentLocationRetrieved) {
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_currentLocationRetrieved);
                } catch (IOException e) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " the optional parameter currentLocationRetrieved encoding failed ", e);
                } catch (AsnException e) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " the optional parameter currentLocationRetrieved encoding failed ", e);
                }
            }

            if (this.saiPresent) {
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_sai_Present);
                } catch (IOException e) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " the optional parameter sai-Present encoding failed ", e);
                } catch (AsnException e) {
                    throw new MAPException("Error while encoding " + _PrimitiveName
                            + " the optional parameter sai-Present encoding failed ", e);
                }
            }

            if (this.locationInformationEPS != null) {
                ((LocationInformationEPSImpl) this.locationInformationEPS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_locationInformationEPS);
            }

            if (this.userCSGInformation != null) {
                ((UserCSGInformationImpl) this.userCSGInformation).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_userCSGInformation);
            }

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationInformation [");

        if (this.ageOfLocationInformation != null) {
            sb.append(", ageOfLocationInformation=");
            sb.append(this.ageOfLocationInformation);
        }
        if (this.geographicalInformation != null) {
            sb.append(", geographicalInformation=");
            sb.append(this.geographicalInformation);
        }
        if (this.vlrNumber != null) {
            sb.append(", vlrNumber=");
            sb.append(this.vlrNumber.toString());
        }
        if (this.locationNumber != null) {
            sb.append(", locationNumber=");
            sb.append(this.locationNumber.toString());
        }
        if (this.cellGlobalIdOrServiceAreaIdOrLAI != null) {
            sb.append(", cellGlobalIdOrServiceAreaIdOrLAI=[");
            sb.append(this.cellGlobalIdOrServiceAreaIdOrLAI.toString());
            sb.append("]");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.selectedLSAId != null) {
            sb.append(", selectedLSAId=");
            sb.append(this.selectedLSAId.toString());
        }
        if (this.mscNumber != null) {
            sb.append(", mscNumber=");
            sb.append(this.mscNumber.toString());
        }
        if (this.geodeticInformation != null) {
            sb.append(", geodeticInformation=");
            sb.append(this.geodeticInformation.toString());
        }
        if (this.currentLocationRetrieved) {
            sb.append(", currentLocationRetrieved");
        }
        if (this.saiPresent) {
            sb.append(", saiPresent");
        }
        if (this.locationInformationEPS != null) {
            sb.append(", locationInformationEPS=");
            sb.append(this.locationInformationEPS.toString());
        }
        if (this.userCSGInformation != null) {
            sb.append(", userCSGInformation=");
            sb.append(this.userCSGInformation.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
