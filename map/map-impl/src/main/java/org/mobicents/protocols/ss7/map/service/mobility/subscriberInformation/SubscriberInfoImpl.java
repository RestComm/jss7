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
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MNPInfoRes;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class SubscriberInfoImpl implements SubscriberInfo, MAPAsnPrimitive {

    public static final String _PrimitiveName = "SubscriberInfo";

    public static final int _ID_locationInformation = 0;
    public static final int _ID_subscriberState = 1;
    public static final int _ID_extensionContainer = 2;
    public static final int _ID_locationInformationGPRS = 3;
    public static final int _ID_psSubscriberState = 4;
    public static final int _ID_imei = 5;
    public static final int _ID_msclassmark2 = 6;
    public static final int _ID_gprsMSClass = 7;
    public static final int _ID_mnpInfoRes = 8;

    private LocationInformation locationInformation = null;
    private SubscriberState subscriberState = null;
    private MAPExtensionContainer extensionContainer = null;
    private LocationInformationGPRS locationInformationGPRS = null;
    private PSSubscriberState psSubscriberState = null;
    private IMEI imei = null;
    private MSClassmark2 msClassmark2 = null;
    private GPRSMSClass gprsMSClass = null;
    private MNPInfoRes mnpInfoRes = null;

    public SubscriberInfoImpl() {
    }

    public SubscriberInfoImpl(LocationInformation locationInformation, SubscriberState subscriberState,
            MAPExtensionContainer extensionContainer, LocationInformationGPRS locationInformationGPRS,
            PSSubscriberState psSubscriberState, IMEI imei, MSClassmark2 msClassmark2, GPRSMSClass gprsMSClass,
            MNPInfoRes mnpInfoRes) {
        this.locationInformation = locationInformation;
        this.subscriberState = subscriberState;
        this.extensionContainer = extensionContainer;
        this.locationInformationGPRS = locationInformationGPRS;
        this.psSubscriberState = psSubscriberState;
        this.imei = imei;
        this.msClassmark2 = msClassmark2;
        this.gprsMSClass = gprsMSClass;
        this.mnpInfoRes = mnpInfoRes;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. SubscriberInfo#getLocationInformation()
     */
    public LocationInformation getLocationInformation() {
        return this.locationInformation;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. SubscriberInfo#getSubscriberState()
     */
    public SubscriberState getSubscriberState() {
        return this.subscriberState;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. SubscriberInfo#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. SubscriberInfo#getLocationInformationGPRS()
     */
    public LocationInformationGPRS getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. SubscriberInfo#getPSSubscriberState()
     */
    public PSSubscriberState getPSSubscriberState() {
        return this.psSubscriberState;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. SubscriberInfo#getIMEI()
     */
    public IMEI getIMEI() {
        return this.imei;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. SubscriberInfo#getMSClassmark2()
     */
    public MSClassmark2 getMSClassmark2() {
        return this.msClassmark2;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. SubscriberInfo#getGPRSMSClass()
     */
    public GPRSMSClass getGPRSMSClass() {
        return this.gprsMSClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation. SubscriberInfo#getMNPInfoRes()
     */
    public MNPInfoRes getMNPInfoRes() {
        return this.mnpInfoRes;
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
        this.locationInformation = null;
        this.subscriberState = null;
        this.extensionContainer = null;
        this.locationInformationGPRS = null;
        this.psSubscriberState = null;
        this.imei = null;
        this.msClassmark2 = null;
        this.gprsMSClass = null;
        this.mnpInfoRes = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_locationInformation:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding locationInformation: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.locationInformation = new LocationInformationImpl();
                        ((LocationInformationImpl) this.locationInformation).decodeAll(ais);
                        break;
                    case _ID_subscriberState:
                        this.subscriberState = new SubscriberStateImpl();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        ((SubscriberStateImpl) this.subscriberState).decodeAll(ais2);
                        break;
                    case _ID_extensionContainer:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding MAPExtensionContainer: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) extensionContainer).decodeAll(ais);
                        break;

                    case _ID_locationInformationGPRS:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding locationInformationGPRS: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        locationInformationGPRS = new LocationInformationGPRSImpl();
                        ((LocationInformationGPRSImpl) locationInformationGPRS).decodeAll(ais);
                        break;
                    case _ID_psSubscriberState:
                        this.psSubscriberState = new PSSubscriberStateImpl();
                        ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        ((PSSubscriberStateImpl) this.psSubscriberState).decodeAll(ais2);
                        break;
                    case _ID_imei:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding IMEI: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        imei = new IMEIImpl();
                        ((IMEIImpl) imei).decodeAll(ais);
                        break;
                    case _ID_msclassmark2:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding msClassmark2is: Parameter not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        msClassmark2 = new MSClassmark2Impl();
                        ((MSClassmark2Impl) msClassmark2).decodeAll(ais);
                        break;
                    case _ID_gprsMSClass:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding gprsMSClass: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        gprsMSClass = new GPRSMSClassImpl();
                        ((GPRSMSClassImpl) gprsMSClass).decodeAll(ais);
                        break;
                    case _ID_mnpInfoRes:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding mnpInfoRes: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        mnpInfoRes = new MNPInfoResImpl();
                        ((MNPInfoResImpl) mnpInfoRes).decodeAll(ais);
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

            if (this.locationInformation != null)
                ((LocationInformationImpl) this.locationInformation).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_locationInformation);

            if (this.subscriberState != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_subscriberState);
                int pos = asnOs.StartContentDefiniteLength();
                ((SubscriberStateImpl) this.subscriberState).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            }

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_extensionContainer);

            if (this.locationInformationGPRS != null)
                ((LocationInformationGPRSImpl) this.locationInformationGPRS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_locationInformationGPRS);

            if (this.psSubscriberState != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_psSubscriberState);
                int pos = asnOs.StartContentDefiniteLength();
                ((PSSubscriberStateImpl) this.psSubscriberState).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            }

            if (this.imei != null)
                ((IMEIImpl) this.imei).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_imei);

            if (this.msClassmark2 != null)
                ((MSClassmark2Impl) this.msClassmark2).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_msclassmark2);

            if (this.gprsMSClass != null)
                ((GPRSMSClassImpl) this.gprsMSClass).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_gprsMSClass);

            if (this.mnpInfoRes != null)
                ((MNPInfoResImpl) this.mnpInfoRes).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_mnpInfoRes);

        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.locationInformation != null) {
            sb.append(", locationInformation=");
            sb.append(this.locationInformation);
        }
        if (this.subscriberState != null) {
            sb.append(", subscriberState=");
            sb.append(this.subscriberState);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.locationInformationGPRS != null) {
            sb.append(", locationInformationGPRS=");
            sb.append(this.locationInformationGPRS);
        }
        if (this.psSubscriberState != null) {
            sb.append(", psSubscriberState=");
            sb.append(this.psSubscriberState);
        }
        if (this.imei != null) {
            sb.append(", imei=");
            sb.append(this.imei);
        }
        if (this.msClassmark2 != null) {
            sb.append(", msClassmark2=");
            sb.append(this.msClassmark2);
        }
        if (this.gprsMSClass != null) {
            sb.append(", gprsMSClass=");
            sb.append(this.gprsMSClass);
        }
        if (this.mnpInfoRes != null) {
            sb.append(", mnpInfoRes=");
            sb.append(this.mnpInfoRes);
        }

        sb.append("]");
        return sb.toString();
    }
}
