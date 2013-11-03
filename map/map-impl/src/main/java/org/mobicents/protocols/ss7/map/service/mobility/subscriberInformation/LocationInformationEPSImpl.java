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
import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgi;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationEPS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;
import org.mobicents.protocols.ss7.map.primitives.DiameterIdentityImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class LocationInformationEPSImpl extends SequenceBase implements LocationInformationEPS {

    public static final int _ID_eUtranCellGlobalIdentity = 0;
    public static final int _ID_trackingAreaIdentity = 1;
    public static final int _ID_extensionContainer = 2;
    public static final int _ID_geographicalInformation = 3;
    public static final int _ID_geodeticInformation = 4;
    public static final int _ID_currentLocationRetrieved = 5;
    public static final int _ID_ageOfLocationInformation = 6;
    public static final int _ID_mme_Name = 7;

    private static final String E_UTRAN_CELL_GLOBAL_IDENTITY = "eUtranCellGlobalIdentity";
    private static final String TRACKING_AREA_IDENTITY = "trackingAreaIdentity";
    private static final String EXTENSION_CONTAINER = "extensionContainer";
    private static final String GEOGRAPHICAL_INFORMATION = "geographicalInformation";
    private static final String GEODETIC_INFORMATION = "geodeticInformation";
    private static final String CURRENT_LOCATION_RETRIEVED = "currentLocationRetrieved";
    private static final String AGE_OF_LOCATION_INFORMATION = "ageOfLocationInformation";
    private static final String MME_NAME = "mmeName";

    private EUtranCgi eUtranCellGlobalIdentity = null;
    private TAId trackingAreaIdentity = null;
    private MAPExtensionContainer extensionContainer = null;
    private GeographicalInformation geographicalInformation = null;
    private GeodeticInformation geodeticInformation = null;
    private boolean currentLocationRetrieved = false;
    private Integer ageOfLocationInformation = null;
    private DiameterIdentity mmeName = null;

    /**
     *
     */
    public LocationInformationEPSImpl() {
        super("LocationInformationEPS");
    }

    /**
     * @param eUtranCellGlobalIdentity
     * @param trackingAreaIdentity
     * @param extensionContainer
     * @param geographicalInformation
     * @param geodeticInformation
     * @param currentLocationRetrieved
     * @param ageOfLocationInformation
     * @param mmeName
     */
    public LocationInformationEPSImpl(EUtranCgi eUtranCellGlobalIdentity, TAId trackingAreaIdentity,
            MAPExtensionContainer extensionContainer, GeographicalInformation geographicalInformation,
            GeodeticInformation geodeticInformation, boolean currentLocationRetrieved, Integer ageOfLocationInformation,
            DiameterIdentity mmeName) {
        super("LocationInformationEPS");

        this.eUtranCellGlobalIdentity = eUtranCellGlobalIdentity;
        this.trackingAreaIdentity = trackingAreaIdentity;
        this.extensionContainer = extensionContainer;
        this.geographicalInformation = geographicalInformation;
        this.geodeticInformation = geodeticInformation;
        this.currentLocationRetrieved = currentLocationRetrieved;
        this.ageOfLocationInformation = ageOfLocationInformation;
        this.mmeName = mmeName;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationEPS#getEUtranCellGlobalIdentity()
     */
    public EUtranCgi getEUtranCellGlobalIdentity() {
        return this.eUtranCellGlobalIdentity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. LocationInformationEPS#getTrackingAreaIdentity()
     */
    public TAId getTrackingAreaIdentity() {
        return this.trackingAreaIdentity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. LocationInformationEPS#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationEPS#getGeographicalInformation()
     */
    public GeographicalInformation getGeographicalInformation() {
        return this.geographicalInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. LocationInformationEPS#getGeodeticInformation()
     */
    public GeodeticInformation getGeodeticInformation() {
        return this.geodeticInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationEPS#getCurrentLocationRetrieved()
     */
    public boolean getCurrentLocationRetrieved() {
        return this.currentLocationRetrieved;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * LocationInformationEPS#getAgeOfLocationInformation()
     */
    public Integer getAgeOfLocationInformation() {
        return this.ageOfLocationInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. LocationInformationEPS#getMmeName()
     */
    public DiameterIdentity getMmeName() {
        return this.mmeName;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.eUtranCellGlobalIdentity = null;
        this.trackingAreaIdentity = null;
        this.extensionContainer = null;
        this.geographicalInformation = null;
        this.geodeticInformation = null;
        this.currentLocationRetrieved = false;
        this.ageOfLocationInformation = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                switch (tag) {
                    case _ID_eUtranCellGlobalIdentity:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " eUtranCellGlobalIdentity: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.eUtranCellGlobalIdentity = new EUtranCgiImpl();
                        ((EUtranCgiImpl) this.eUtranCellGlobalIdentity).decodeAll(ais);
                        break;
                    case _ID_trackingAreaIdentity:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " trackingAreaIdentity: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.trackingAreaIdentity = new TAIdImpl();
                        ((TAIdImpl) this.trackingAreaIdentity).decodeAll(ais);
                        break;
                    case _ID_extensionContainer:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " extensionContainer: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;
                    case _ID_geographicalInformation:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " geographicalInformation: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.geographicalInformation = new GeographicalInformationImpl();
                        ((GeographicalInformationImpl) this.geographicalInformation).decodeAll(ais);
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
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException(
                                    "Error while decoding LocationInformation: Parameter [currentLocationRetrieved    [8] NULL ] not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
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
                    case _ID_mme_Name:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " mmeName: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.mmeName = new DiameterIdentityImpl();
                        ((DiameterIdentityImpl) this.mmeName).decodeAll(ais);
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
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {

            if (this.eUtranCellGlobalIdentity != null)
                ((EUtranCgiImpl) this.eUtranCellGlobalIdentity).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_eUtranCellGlobalIdentity);

            if (this.trackingAreaIdentity != null) {
                ((TAIdImpl) this.trackingAreaIdentity).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_trackingAreaIdentity);
            }

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_extensionContainer);

            if (this.geographicalInformation != null)
                ((GeographicalInformationImpl) this.geographicalInformation).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_geographicalInformation);

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

            if (this.mmeName != null) {
                ((DiameterIdentityImpl) this.mmeName).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_mme_Name);
            }

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

        if (this.eUtranCellGlobalIdentity != null) {
            sb.append("eUtranCellGlobalIdentity=");
            sb.append(this.eUtranCellGlobalIdentity);
        }

        if (this.trackingAreaIdentity != null) {
            sb.append(", trackingAreaIdentity=");
            sb.append(this.trackingAreaIdentity);
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        if (this.geographicalInformation != null) {
            sb.append(", geographicalInformation=");
            sb.append(this.geographicalInformation);
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

        if (this.mmeName != null) {
            sb.append(", mmeName=");
            sb.append(this.mmeName);
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<LocationInformationEPSImpl> LOCATION_INFORMATION_EPS_XML = new XMLFormat<LocationInformationEPSImpl>(
            LocationInformationEPSImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, LocationInformationEPSImpl locationInformationEPS)
                throws XMLStreamException {
            locationInformationEPS.eUtranCellGlobalIdentity = xml.get(E_UTRAN_CELL_GLOBAL_IDENTITY, EUtranCgiImpl.class);
            locationInformationEPS.trackingAreaIdentity = xml.get(TRACKING_AREA_IDENTITY, TAIdImpl.class);
            locationInformationEPS.extensionContainer = xml.get(EXTENSION_CONTAINER, MAPExtensionContainerImpl.class);
            locationInformationEPS.geographicalInformation = xml.get(GEOGRAPHICAL_INFORMATION,
                    GeographicalInformationImpl.class);

            locationInformationEPS.geodeticInformation = xml.get(GEODETIC_INFORMATION, GeodeticInformationImpl.class);
            Boolean bval = xml.get(CURRENT_LOCATION_RETRIEVED, Boolean.class);
            if (bval != null)
                locationInformationEPS.currentLocationRetrieved = bval;
            locationInformationEPS.ageOfLocationInformation = xml.get(AGE_OF_LOCATION_INFORMATION, Integer.class);
            locationInformationEPS.mmeName = xml.get(MME_NAME, DiameterIdentityImpl.class);
        }

        @Override
        public void write(LocationInformationEPSImpl locationInformationEPS, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            if (locationInformationEPS.eUtranCellGlobalIdentity != null) {
                xml.add((EUtranCgiImpl) locationInformationEPS.eUtranCellGlobalIdentity, E_UTRAN_CELL_GLOBAL_IDENTITY,
                        EUtranCgiImpl.class);
            }
            if (locationInformationEPS.trackingAreaIdentity != null) {
                xml.add((TAIdImpl) locationInformationEPS.trackingAreaIdentity, TRACKING_AREA_IDENTITY, TAIdImpl.class);
            }
            if (locationInformationEPS.extensionContainer != null) {
                xml.add((MAPExtensionContainerImpl) locationInformationEPS.extensionContainer, EXTENSION_CONTAINER,
                        MAPExtensionContainerImpl.class);
            }
            if (locationInformationEPS.geographicalInformation != null) {
                xml.add((GeographicalInformationImpl) locationInformationEPS.geographicalInformation, GEOGRAPHICAL_INFORMATION,
                        GeographicalInformationImpl.class);
            }

            if (locationInformationEPS.geodeticInformation != null) {
                xml.add((GeodeticInformationImpl) locationInformationEPS.geodeticInformation, GEODETIC_INFORMATION,
                        GeodeticInformationImpl.class);
            }
            if (locationInformationEPS.currentLocationRetrieved) {
                xml.add((Boolean) locationInformationEPS.currentLocationRetrieved, CURRENT_LOCATION_RETRIEVED, Boolean.class);
            }
            if (locationInformationEPS.ageOfLocationInformation != null) {
                xml.add((Integer) locationInformationEPS.ageOfLocationInformation, AGE_OF_LOCATION_INFORMATION, Integer.class);
            }
            if (locationInformationEPS.mmeName != null) {
                xml.add((DiameterIdentityImpl) locationInformationEPS.mmeName, MME_NAME, DiameterIdentityImpl.class);
            }
        }
    };

}
