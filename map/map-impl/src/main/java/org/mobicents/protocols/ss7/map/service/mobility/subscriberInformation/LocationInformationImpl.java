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
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class LocationInformationImpl extends SequenceBase implements LocationInformation {

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

    private static final String AGE_OF_LOCATION_INFORMATION = "ageOfLocationInformation";
    private static final String GEOGRAPHICAL_INFORMATION = "geographicalInformation";
    private static final String VLR_NUMBER = "vlrNumber";
    private static final String LOCATION_NUMBER = "locationNumber";
    private static final String CELL_GLOBAL_ID_OR_SERVICE_AREA_ID_OR_LAI = "cellGlobalIdOrServiceAreaIdOrLAI";
    private static final String EXTENSION_CONTAINER = "extensionContainer";
    private static final String SELECTED_LSA_ID = "selectedLSAId";
    private static final String MSC_NUMBER = "mscNumber";
    private static final String GEODETIC_INFORMATION = "geodeticInformation";
    private static final String CURRENT_LOCATION_RETRIEVED = "currentLocationRetrieved";
    private static final String SAI_PRESENT = "saiPresent";
    private static final String LOCATION_INFORMATION_EPS = "locationInformationEPS";
    private static final String USER_CSG_INFORMATION = "userCSGInformation";

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
        super("LocationInformation");
    }

    public LocationInformationImpl(Integer ageOfLocationInformation, GeographicalInformation geographicalInformation,
            ISDNAddressString vlrNumber, LocationNumberMap locationNumber,
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, MAPExtensionContainer extensionContainer,
            LSAIdentity selectedLSAId, ISDNAddressString mscNumber, GeodeticInformation geodeticInformation,
            boolean currentLocationRetrieved, boolean saiPresent, LocationInformationEPS locationInformationEPS,
            UserCSGInformation userCSGInformation) {
        super("LocationInformation");

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

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

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

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<LocationInformationImpl> LOCATION_INFORMATION_XML = new XMLFormat<LocationInformationImpl>(
            LocationInformationImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, LocationInformationImpl locationInformation)
                throws XMLStreamException {
            locationInformation.ageOfLocationInformation = xml.get(AGE_OF_LOCATION_INFORMATION, Integer.class);
            locationInformation.geographicalInformation = xml.get(GEOGRAPHICAL_INFORMATION, GeographicalInformationImpl.class);
            locationInformation.vlrNumber = xml.get(VLR_NUMBER, ISDNAddressStringImpl.class);
            locationInformation.locationNumber = xml.get(LOCATION_NUMBER, LocationNumberMapImpl.class);
            locationInformation.cellGlobalIdOrServiceAreaIdOrLAI = xml.get(CELL_GLOBAL_ID_OR_SERVICE_AREA_ID_OR_LAI,
                    CellGlobalIdOrServiceAreaIdOrLAIImpl.class);

            locationInformation.extensionContainer = xml.get(EXTENSION_CONTAINER, MAPExtensionContainerImpl.class);
            locationInformation.selectedLSAId = xml.get(SELECTED_LSA_ID, LSAIdentityImpl.class);
            locationInformation.mscNumber = xml.get(MSC_NUMBER, ISDNAddressStringImpl.class);
            locationInformation.geodeticInformation = xml.get(GEODETIC_INFORMATION, GeodeticInformationImpl.class);

            Boolean bval = xml.get(CURRENT_LOCATION_RETRIEVED, Boolean.class);
            if (bval != null)
                locationInformation.currentLocationRetrieved = bval;
            bval = xml.get(SAI_PRESENT, Boolean.class);
            if (bval != null)
                locationInformation.saiPresent = bval;
            locationInformation.locationInformationEPS = xml.get(LOCATION_INFORMATION_EPS, LocationInformationEPSImpl.class);
            locationInformation.userCSGInformation = xml.get(USER_CSG_INFORMATION, UserCSGInformationImpl.class);
        }

        @Override
        public void write(LocationInformationImpl locationInformation, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (locationInformation.ageOfLocationInformation != null) {
                xml.add((Integer) locationInformation.ageOfLocationInformation, AGE_OF_LOCATION_INFORMATION, Integer.class);
            }
            if (locationInformation.geographicalInformation != null) {
                xml.add((GeographicalInformationImpl) locationInformation.geographicalInformation, GEOGRAPHICAL_INFORMATION,
                        GeographicalInformationImpl.class);
            }
            if (locationInformation.vlrNumber != null) {
                xml.add((ISDNAddressStringImpl) locationInformation.vlrNumber, VLR_NUMBER, ISDNAddressStringImpl.class);
            }
            if (locationInformation.locationNumber != null) {
                xml.add((LocationNumberMapImpl) locationInformation.locationNumber, LOCATION_NUMBER,
                        LocationNumberMapImpl.class);
            }
            if (locationInformation.cellGlobalIdOrServiceAreaIdOrLAI != null) {
                xml.add((CellGlobalIdOrServiceAreaIdOrLAIImpl) locationInformation.cellGlobalIdOrServiceAreaIdOrLAI,
                        CELL_GLOBAL_ID_OR_SERVICE_AREA_ID_OR_LAI, CellGlobalIdOrServiceAreaIdOrLAIImpl.class);
            }

            if (locationInformation.extensionContainer != null) {
                xml.add((MAPExtensionContainerImpl) locationInformation.extensionContainer, EXTENSION_CONTAINER,
                        MAPExtensionContainerImpl.class);
            }
            if (locationInformation.selectedLSAId != null) {
                xml.add((LSAIdentityImpl) locationInformation.selectedLSAId, SELECTED_LSA_ID, LSAIdentityImpl.class);
            }
            if (locationInformation.mscNumber != null) {
                xml.add((ISDNAddressStringImpl) locationInformation.mscNumber, MSC_NUMBER, ISDNAddressStringImpl.class);
            }
            if (locationInformation.geodeticInformation != null) {
                xml.add((GeodeticInformationImpl) locationInformation.geodeticInformation, GEODETIC_INFORMATION,
                        GeodeticInformationImpl.class);
            }

            if (locationInformation.currentLocationRetrieved) {
                xml.add((Boolean) locationInformation.currentLocationRetrieved, CURRENT_LOCATION_RETRIEVED, Boolean.class);
            }
            if (locationInformation.saiPresent) {
                xml.add((Boolean) locationInformation.saiPresent, SAI_PRESENT, Boolean.class);
            }
            if (locationInformation.locationInformationEPS != null) {
                xml.add((LocationInformationEPSImpl) locationInformation.locationInformationEPS, LOCATION_INFORMATION_EPS,
                        LocationInformationEPSImpl.class);
            }
            if (locationInformation.userCSGInformation != null) {
                xml.add((UserCSGInformationImpl) locationInformation.userCSGInformation, USER_CSG_INFORMATION,
                        UserCSGInformationImpl.class);
            }
        }
    };
}
