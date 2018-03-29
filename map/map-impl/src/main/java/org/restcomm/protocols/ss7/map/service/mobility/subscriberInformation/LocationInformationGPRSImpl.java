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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.primitives.SequenceBase;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class LocationInformationGPRSImpl extends SequenceBase implements LocationInformationGPRS {

    public static final int _ID_cellGlobalIdOrServiceAreaIdOrLAI = 0;
    private static final int _ID_routeingAreaIdentity = 1;
    private static final int _ID_geographicalInformation = 2;
    private static final int _ID_sgsnNumber = 3;
    private static final int _ID_selectedLSAIdentity = 4;
    private static final int _ID_extensionContainer = 5;
    private static final int _ID_sai_Present = 6;
    private static final int _ID_geodeticInformation = 7;
    private static final int _ID_currentLocationRetrieved = 8;
    private static final int _ID_ageOfLocationInformation = 9;

    private static final String CELL_GLOBAL_ID_OR_SERVICE_AREA_ID_OR_LAI = "cellGlobalIdOrServiceAreaIdOrLAI";
    private static final String ROUTEING_AREA_ID = "routeingAreaIdentity";
    private static final String GEOGRAPHICAL_INFORMATION = "geographicalInformation";
    private static final String SGSN_NUMBER = "sgsnNumber";
    private static final String SELECTED_LSA_ID = "selectedLSAIdentity";
    private static final String EXTENSION_CONTAINER = "extensionContainer";
    private static final String SAI_PRESENT = "saiPresent";
    private static final String GEODETIC_INFORMATION = "geodeticInformation";
    private static final String CURRENT_LOCATION_RETRIEVED = "currentLocationRetrieved";
    private static final String AGE_OF_LOCATION_INFORMATION = "ageOfLocationInformation";

    private CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = null;
    private RAIdentity routeingAreaIdentity = null;
    private GeographicalInformation geographicalInformation = null;
    private ISDNAddressString sgsnNumber = null;
    private LSAIdentity selectedLSAIdentity = null;
    private MAPExtensionContainer extensionContainer = null;
    private boolean saiPresent = false;
    private GeodeticInformation geodeticInformation = null;
    private boolean currentLocationRetrieved = false;
    private Integer ageOfLocationInformation = null;

    /**
     *
     */
    public LocationInformationGPRSImpl() {
        super("LocationInformationGPRS");
    }

    /**
     * @param cellGlobalIdOrServiceAreaIdOrLAI
     * @param routeingAreaIdentity
     * @param geographicalInformation
     * @param sgsnNumber
     * @param selectedLSAIdentity
     * @param extensionContainer
     * @param saiPresent
     * @param geodeticInformation
     * @param currentLocationRetrieved
     * @param ageOfLocationInformation
     */
    public LocationInformationGPRSImpl(CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI,
            RAIdentity routeingAreaIdentity, GeographicalInformation geographicalInformation, ISDNAddressString sgsnNumber,
            LSAIdentity selectedLSAIdentity, MAPExtensionContainer extensionContainer, boolean saiPresent,
            GeodeticInformation geodeticInformation, boolean currentLocationRetrieved, Integer ageOfLocationInformation) {
        super("LocationInformationGPRS");

        this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
        this.routeingAreaIdentity = routeingAreaIdentity;
        this.geographicalInformation = geographicalInformation;
        this.sgsnNumber = sgsnNumber;
        this.selectedLSAIdentity = selectedLSAIdentity;
        this.extensionContainer = extensionContainer;
        this.saiPresent = saiPresent;
        this.geodeticInformation = geodeticInformation;
        this.currentLocationRetrieved = currentLocationRetrieved;
        this.ageOfLocationInformation = ageOfLocationInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationGPRS#getCellGlobalIdOrServiceAreaIdOrLAI()
     */
    public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
        return this.cellGlobalIdOrServiceAreaIdOrLAI;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationGPRS#getRouteingAreaIdentity()
     */
    public RAIdentity getRouteingAreaIdentity() {
        return this.routeingAreaIdentity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationGPRS#getGeographicalInformation()
     */
    public GeographicalInformation getGeographicalInformation() {
        return this.geographicalInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationGPRS#getSGSNNumber()
     */
    public ISDNAddressString getSGSNNumber() {
        return this.sgsnNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationGPRS#getLSAIdentity()
     */
    public LSAIdentity getLSAIdentity() {
        return this.selectedLSAIdentity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationGPRS#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationGPRS#isSaiPresent()
     */
    public boolean isSaiPresent() {
        return this.saiPresent;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation. LocationInformationGPRS#getGeodeticInformation()
     */
    public GeodeticInformation getGeodeticInformation() {
        return this.geodeticInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationGPRS#isCurrentLocationRetrieved()
     */
    public boolean isCurrentLocationRetrieved() {
        return this.currentLocationRetrieved;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationGPRS#getAgeOfLocationInformation()
     */
    public Integer getAgeOfLocationInformation() {
        return this.ageOfLocationInformation;
    }

    public static CellGlobalIdOrServiceAreaIdOrLAI decodeCellGlobalIdOrServiceAreaIdOrLAI(AsnInputStream ais,
            String primitiveName) throws MAPParsingComponentException, AsnException, IOException {
        if (ais.isTagPrimitive()) {
            // nonstandard case when there is no external container
            int len = ais.readLength();
            if (len == 7) {
                CellGlobalIdOrServiceAreaIdFixedLengthImpl val = new CellGlobalIdOrServiceAreaIdFixedLengthImpl();
                val.decodeData(ais, len);
                CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(
                        val);
                return cellGlobalIdOrServiceAreaIdOrLAI;
            } else if (len == 5) {
                LAIFixedLengthImpl val = new LAIFixedLengthImpl();
                val.decodeData(ais, len);
                CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(
                        val);
                return cellGlobalIdOrServiceAreaIdOrLAI;
            } else {
                throw new MAPParsingComponentException("Error while decoding " + primitiveName
                        + " cellGlobalIdOrServiceAreaIdOrLAI: Parameter length must be 5 or 7",
                        MAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else {
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl();
            AsnInputStream ais2 = ais.readSequenceStream();
            ais2.readTag();
            ((CellGlobalIdOrServiceAreaIdOrLAIImpl) cellGlobalIdOrServiceAreaIdOrLAI).decodeAll(ais2);
            return cellGlobalIdOrServiceAreaIdOrLAI;
        }
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.cellGlobalIdOrServiceAreaIdOrLAI = null;
        this.routeingAreaIdentity = null;
        this.geographicalInformation = null;
        this.sgsnNumber = null;
        this.selectedLSAIdentity = null;
        this.extensionContainer = null;
        this.saiPresent = false;
        this.geodeticInformation = null;
        this.currentLocationRetrieved = false;
        this.ageOfLocationInformation = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            // optional parameters
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                switch (tag) {
                    case _ID_cellGlobalIdOrServiceAreaIdOrLAI:
                        this.cellGlobalIdOrServiceAreaIdOrLAI = decodeCellGlobalIdOrServiceAreaIdOrLAI(ais, _PrimitiveName);
                        break;
                    case _ID_routeingAreaIdentity:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " routeingAreaIdentity: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.routeingAreaIdentity = new RAIdentityImpl();
                        ((RAIdentityImpl) this.routeingAreaIdentity).decodeAll(ais);
                        break;
                    case _ID_geographicalInformation:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " geographicalInformation: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.geographicalInformation = new GeographicalInformationImpl();
                        ((GeographicalInformationImpl) this.geographicalInformation).decodeAll(ais);
                        break;
                    case _ID_sgsnNumber:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " sgsnNumber: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.sgsnNumber = new ISDNAddressStringImpl();
                        ((ISDNAddressStringImpl) this.sgsnNumber).decodeAll(ais);
                        break;

                    case _ID_selectedLSAIdentity:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " selectedLSAIdentity: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.selectedLSAIdentity = new LSAIdentityImpl();
                        ((LSAIdentityImpl) this.selectedLSAIdentity).decodeAll(ais);
                        break;

                    case _ID_extensionContainer:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " extensionContainer: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;
                    case _ID_sai_Present:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " saiPresent: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.saiPresent = true;
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
                    case _ID_ageOfLocationInformation:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " ageOfLocationInformation: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.ageOfLocationInformation = (int) ais.readInteger();
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

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {

            if (this.cellGlobalIdOrServiceAreaIdOrLAI != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_cellGlobalIdOrServiceAreaIdOrLAI);
                int pos = asnOs.StartContentDefiniteLength();
                ((CellGlobalIdOrServiceAreaIdOrLAIImpl) this.cellGlobalIdOrServiceAreaIdOrLAI).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            }

            if (this.routeingAreaIdentity != null) {
                ((RAIdentityImpl) this.routeingAreaIdentity).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_routeingAreaIdentity);
            }

            if (this.geographicalInformation != null)
                ((GeographicalInformationImpl) this.geographicalInformation).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_geographicalInformation);

            if (this.sgsnNumber != null)
                ((ISDNAddressStringImpl) this.sgsnNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_sgsnNumber);

            if (this.selectedLSAIdentity != null)
                ((LSAIdentityImpl) this.selectedLSAIdentity).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_selectedLSAIdentity);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_extensionContainer);

            if (this.saiPresent) {
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_sai_Present);
                } catch (IOException e) {
                    throw new MAPException(
                            "Error while encoding LocationInformation the optional parameter sai-Present encoding failed ", e);
                } catch (AsnException e) {
                    throw new MAPException(
                            "Error while encoding LocationInformation the optional parameter sai-Present encoding failed ", e);
                }
            }

            if (this.geodeticInformation != null)
                ((GeodeticInformationImpl) this.geodeticInformation).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_geodeticInformation);

            if (this.currentLocationRetrieved) {
                try {
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_currentLocationRetrieved);
                } catch (IOException e) {
                    throw new MAPException(
                            "Error while encoding LocationInformation the optional parameter currentLocationRetrieved encoding failed ",
                            e);
                } catch (AsnException e) {
                    throw new MAPException(
                            "Error while encoding LocationInformation the optional parameter currentLocationRetrieved encoding failed ",
                            e);
                }
            }

            if (ageOfLocationInformation != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_ageOfLocationInformation, (int) ageOfLocationInformation);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.cellGlobalIdOrServiceAreaIdOrLAI != null) {
            sb.append("cellGlobalIdOrServiceAreaIdOrLAI=");
            sb.append(this.cellGlobalIdOrServiceAreaIdOrLAI);
        }

        if (this.routeingAreaIdentity != null) {
            sb.append(", routeingAreaIdentity=");
            sb.append(this.routeingAreaIdentity);
        }

        if (this.geographicalInformation != null) {
            sb.append(", geographicalInformation=");
            sb.append(this.geographicalInformation);
        }

        if (this.sgsnNumber != null) {
            sb.append(", sgsnNumber=");
            sb.append(this.sgsnNumber);
        }

        if (this.selectedLSAIdentity != null) {
            sb.append(", selectedLSAIdentity=");
            sb.append(this.selectedLSAIdentity);
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        if (saiPresent) {
            sb.append(", saiPresent");
        }

        if (this.geodeticInformation != null) {
            sb.append(", geodeticInformation=");
            sb.append(this.geodeticInformation);
        }

        if (currentLocationRetrieved) {
            sb.append(", currentLocationRetrieved");
        }

        if (this.ageOfLocationInformation != null) {
            sb.append(", ageOfLocationInformation=");
            sb.append(this.ageOfLocationInformation);
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<LocationInformationGPRSImpl> LOCATION_INFORMATION_GPRS_XML = new XMLFormat<LocationInformationGPRSImpl>(
            LocationInformationGPRSImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, LocationInformationGPRSImpl locationInformation)
                throws XMLStreamException {
            locationInformation.ageOfLocationInformation = xml.get(AGE_OF_LOCATION_INFORMATION, Integer.class);
            locationInformation.geographicalInformation = xml.get(GEOGRAPHICAL_INFORMATION, GeographicalInformationImpl.class);
            locationInformation.routeingAreaIdentity = xml.get(ROUTEING_AREA_ID, RAIdentityImpl.class);
            locationInformation.sgsnNumber = xml.get(SGSN_NUMBER, ISDNAddressStringImpl.class);
            locationInformation.cellGlobalIdOrServiceAreaIdOrLAI = xml.get(CELL_GLOBAL_ID_OR_SERVICE_AREA_ID_OR_LAI,
                    CellGlobalIdOrServiceAreaIdOrLAIImpl.class);

            locationInformation.extensionContainer = xml.get(EXTENSION_CONTAINER, MAPExtensionContainerImpl.class);
            locationInformation.geodeticInformation = xml.get(GEODETIC_INFORMATION, GeodeticInformationImpl.class);
            locationInformation.selectedLSAIdentity = xml.get(SELECTED_LSA_ID, LSAIdentityImpl.class);

            Boolean bval = xml.get(CURRENT_LOCATION_RETRIEVED, Boolean.class);
            if (bval != null)
                locationInformation.currentLocationRetrieved = bval;
            bval = xml.get(SAI_PRESENT, Boolean.class);
            if (bval != null)
                locationInformation.saiPresent = bval;
        }

        @Override
        public void write(LocationInformationGPRSImpl locationInformation, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (locationInformation.ageOfLocationInformation != null) {
                xml.add((Integer) locationInformation.ageOfLocationInformation, AGE_OF_LOCATION_INFORMATION, Integer.class);
            }
            if (locationInformation.geographicalInformation != null) {
                xml.add((GeographicalInformationImpl) locationInformation.geographicalInformation, GEOGRAPHICAL_INFORMATION,
                        GeographicalInformationImpl.class);
            }
            if (locationInformation.routeingAreaIdentity != null) {
                xml.add((RAIdentityImpl) locationInformation.routeingAreaIdentity, ROUTEING_AREA_ID, RAIdentityImpl.class);
            }
            if (locationInformation.sgsnNumber != null) {
                xml.add((ISDNAddressStringImpl) locationInformation.sgsnNumber, SGSN_NUMBER,
                        ISDNAddressStringImpl.class);
            }
            if (locationInformation.cellGlobalIdOrServiceAreaIdOrLAI != null) {
                xml.add((CellGlobalIdOrServiceAreaIdOrLAIImpl) locationInformation.cellGlobalIdOrServiceAreaIdOrLAI,
                        CELL_GLOBAL_ID_OR_SERVICE_AREA_ID_OR_LAI, CellGlobalIdOrServiceAreaIdOrLAIImpl.class);
            }

            if (locationInformation.extensionContainer != null) {
                xml.add((MAPExtensionContainerImpl) locationInformation.extensionContainer, EXTENSION_CONTAINER,
                        MAPExtensionContainerImpl.class);
            }
            if (locationInformation.selectedLSAIdentity != null) {
                xml.add((LSAIdentityImpl) locationInformation.selectedLSAIdentity, SELECTED_LSA_ID, LSAIdentityImpl.class);
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
        }
    };
}
