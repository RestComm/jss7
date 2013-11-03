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

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class RequestedInfoImpl implements RequestedInfo, MAPAsnPrimitive {

    public static final int _ID_locationInformation = 0;
    public static final int _ID_subscriberState = 1;
    public static final int _ID_extensionContainer = 2;
    public static final int _ID_currentLocation = 3;
    public static final int _ID_requestedDomain = 4;
    public static final int _ID_imei = 6;
    public static final int _ID_msclassmark = 5;
    public static final int _ID_mnpRequestedInfo = 7;

    public static final String _PrimitiveName = "RequestedInfo";

    private boolean locationInformation;
    private boolean subscriberState;
    private MAPExtensionContainer extensionContainer;
    private boolean currentLocation;
    private DomainType requestedDomain;
    private boolean imei;
    private boolean msClassmark;
    private boolean mnpRequestedInfo;

    /**
     *
     */
    public RequestedInfoImpl() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param locationInformation
     * @param subscriberState
     * @param extensionContainer
     * @param currentLocation
     * @param requestedDomain
     * @param imei
     * @param msClassmark
     * @param mnpRequestedInfo
     */
    public RequestedInfoImpl(boolean locationInformation, boolean subscriberState, MAPExtensionContainer extensionContainer,
            boolean currentLocation, DomainType requestedDomain, boolean imei, boolean msClassmark, boolean mnpRequestedInfo) {
        super();
        this.locationInformation = locationInformation;
        this.subscriberState = subscriberState;
        this.extensionContainer = extensionContainer;
        this.currentLocation = currentLocation;
        this.requestedDomain = requestedDomain;
        this.imei = imei;
        this.msClassmark = msClassmark;
        this.mnpRequestedInfo = mnpRequestedInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
     */
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll( org.mobicents.protocols.asn.AsnInputStream)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData (org.mobicents.protocols.asn.AsnInputStream,
     * int)
     */
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
        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        locationInformation = false;
        subscriberState = false;
        extensionContainer = null;
        currentLocation = false;
        requestedDomain = null;
        imei = false;
        msClassmark = false;
        mnpRequestedInfo = false;

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_locationInformation:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.locationInformation = Boolean.TRUE;
                        break;
                    case _ID_subscriberState:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.subscriberState = Boolean.TRUE;
                        break;
                    case _ID_extensionContainer:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) extensionContainer).decodeAll(ais);
                        break;
                    case _ID_currentLocation:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.currentLocation = Boolean.TRUE;
                        break;
                    case _ID_requestedDomain:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        int i1 = (int) ais.readInteger();
                        this.requestedDomain = DomainType.getInstance(i1);
                        break;
                    case _ID_msclassmark:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.msClassmark = Boolean.TRUE;
                        break;
                    case _ID_imei:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.imei = Boolean.TRUE;
                        break;
                    case _ID_mnpRequestedInfo:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedInfo: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.mnpRequestedInfo = Boolean.TRUE;
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
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream,
     * int, int)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            if (this.locationInformation) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_locationInformation);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter locationInformation: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter locationInformation: ", e);
        }

        try {
            if (this.subscriberState) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_subscriberState);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter subscriberState: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter subscriberState: ", e);
        }

        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _ID_extensionContainer);

        try {
            if (this.currentLocation) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_currentLocation);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter currentLocation: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter currentLocation: ", e);
        }

        try {
            if (this.requestedDomain != null) {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_requestedDomain, this.requestedDomain.getType());
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter requestedDomain: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter requestedDomain: ", e);
        }

        try {
            if (this.imei) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_imei);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter imei: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter imei: ", e);
        }

        try {
            if (this.msClassmark) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_msclassmark);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter msClassmark: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter msClassmark: ", e);
        }

        try {
            if (this.mnpRequestedInfo) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_mnpRequestedInfo);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter mnpRequestedInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter mnpRequestedInfo: ", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getLocationInformation()
     */
    public boolean getLocationInformation() {
        return this.locationInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getSubscriberState()
     */
    public boolean getSubscriberState() {
        return this.subscriberState;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getCurrentLocation()
     */
    public boolean getCurrentLocation() {
        return this.currentLocation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getRequestedDomain()
     */
    public DomainType getRequestedDomain() {
        return this.requestedDomain;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getImei()
     */
    public boolean getImei() {
        return this.imei;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getMsClassmark()
     */
    public boolean getMsClassmark() {
        return this.msClassmark;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. RequestedInfo#getMnpRequestedInfo()
     */
    public boolean getMnpRequestedInfo() {
        return this.mnpRequestedInfo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (locationInformation) {
            sb.append(", locationInformation");
        }

        if (subscriberState) {
            sb.append(", subscriberState");
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        if (currentLocation) {
            sb.append(", currentLocation");
        }

        if (this.requestedDomain != null) {
            sb.append("requestedDomain=");
            sb.append(this.requestedDomain);
        }

        if (imei) {
            sb.append(", imei");
        }

        if (msClassmark) {
            sb.append(", msClassmark");
        }

        if (mnpRequestedInfo) {
            sb.append(", mnpRequestedInfo");
        }

        sb.append("]");
        return sb.toString();
    }
}
