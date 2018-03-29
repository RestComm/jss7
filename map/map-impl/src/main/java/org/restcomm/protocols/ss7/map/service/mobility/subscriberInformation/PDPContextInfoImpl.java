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
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TEID;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TransactionId;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPAddressImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPTypeImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PDPContextInfoImpl implements PDPContextInfo, MAPAsnPrimitive {

    public static final int _ID_pdpContextIdentifier = 0;
    public static final int _ID_pdpContextActive = 1;
    public static final int _ID_pdpType = 2;
    public static final int _ID_pdpAddress = 3;
    public static final int _ID_apnSubscribed = 4;
    public static final int _ID_apnInUse = 5;
    public static final int _ID_nsapi = 6;
    public static final int _ID_transactionId = 7;
    public static final int _ID_teidForGnAndGp = 8;
    public static final int _ID_teidForIu = 9;
    public static final int _ID_ggsnAddress = 10;
    public static final int _ID_qosSubscribed = 11;
    public static final int _ID_qosRequested = 12;
    public static final int _ID_qosNegotiated = 13;
    public static final int _ID_chargingId = 14;
    public static final int _ID_chargingCharacteristics = 15;
    public static final int _ID_rncAddress = 16;
    public static final int _ID_extensionContainer = 17;
    public static final int _ID_qos2Subscribed = 18;
    public static final int _ID_qos2Requested = 19;
    public static final int _ID_qos2Negotiated = 20;
    public static final int _ID_qos3Subscribed = 21;
    public static final int _ID_qos3Requested = 22;
    public static final int _ID_qos3Negotiated = 23;
    public static final int _ID_qos4Subscribed = 25;
    public static final int _ID_qos4Requested = 26;
    public static final int _ID_qos4Negotiated = 27;
    public static final int _ID_extPdpType = 28;
    public static final int _ID_extPdpAddress = 29;

    public static final String _PrimitiveName = "PDPContextInfo";

    private static final String PDP_CONTEXT_IDENTIFIER = "pdpContextIdentifier";
    private static final String PDP_CONTEXT_ACTIVE = "pdpContextActive";
    private static final String PDP_TYPE = "pdpType";
    private static final String PDP_ADDRESS = "pdpAddress";
    private static final String APN_SUBSCRIBED = "apnSubscribed";
    private static final String APN_IN_USE = "apnInUse";
    private static final String NSAPI = "nsapi";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String TEID_FOR_GN_AND_GP = "teidForGnAndGp";
    private static final String TEID_FOR_IU = "teidForIu";
    private static final String GGSN_ADDRESS = "ggsnAddress";
    private static final String QOS_SUBSCRIBED = "qosSubscribed";
    private static final String QOS_REQUESTED = "qosRequested";
    private static final String QOS_NEGOTIATED = "qosNegotiated";
    private static final String CHARGING_ID = "chargingId";
    private static final String CHARGING_CHARACTERISTICS = "chargingCharacteristics";
    private static final String RNC_ADDRESS = "rncAddress";
    private static final String EXTENTSION_CONTAINER = "extensionContainer";
    private static final String QOS2_SUBSCRIBED = "qos2Subscribed";
    private static final String QOS2_REQUESTED = "qos2Requested";
    private static final String QOS2_NEGOTIATED = "qos2Negotiated";
    private static final String QOS3_SUBSCRIBED = "qos3Subscribed";
    private static final String QOS3_REQUESTED = "qos3Requested";
    private static final String QOS3_NEGOTIATED = "qos3Negotiated";
    private static final String QOS4_SUBSCRIBED = "qos4Subscribed";
    private static final String QOS4_REQUESTED = "qos4Requested";
    private static final String QOS4_NEGOTIATED = "qos4Negotiated";
    private static final String EXT_PDP_TYPE = "extPdpType";
    private static final String EXT_PDP_ADDRESS = "extPdpAddress";

    private int pdpContextIdentifier;
    private boolean pdpContextActive;
    private PDPType pdpType;
    private PDPAddress pdpAddress;
    private APN apnSubscribed;
    private APN apnInUse;
    private Integer nsapi;
    private TransactionId transactionId;
    private TEID teidForGnAndGp;
    private TEID teidForIu;
    private GSNAddress ggsnAddress;
    private ExtQoSSubscribed qosSubscribed;
    private ExtQoSSubscribed qosRequested;
    private ExtQoSSubscribed qosNegotiated;
    private GPRSChargingID chargingId;
    private ChargingCharacteristics chargingCharacteristics;
    private GSNAddress rncAddress;
    private MAPExtensionContainer extensionContainer;
    private Ext2QoSSubscribed qos2Subscribed;
    private Ext2QoSSubscribed qos2Requested;
    private Ext2QoSSubscribed qos2Negotiated;
    private Ext3QoSSubscribed qos3Subscribed;
    private Ext3QoSSubscribed qos3Requested;
    private Ext3QoSSubscribed qos3Negotiated;
    private Ext4QoSSubscribed qos4Subscribed;
    private Ext4QoSSubscribed qos4Requested;
    private Ext4QoSSubscribed qos4Negotiated;
    private ExtPDPType extPdpType;
    private PDPAddress extPdpAddress;

    public PDPContextInfoImpl() {
    }

    public PDPContextInfoImpl(int pdpContextIdentifier, boolean pdpContextActive, PDPType pdpType, PDPAddress pdpAddress,
            APN apnSubscribed, APN apnInUse, Integer asapi, TransactionId transactionId, TEID teidForGnAndGp, TEID teidForIu,
            GSNAddress ggsnAddress, ExtQoSSubscribed qosSubscribed, ExtQoSSubscribed qosRequested,
            ExtQoSSubscribed qosNegotiated, GPRSChargingID chargingId, ChargingCharacteristics chargingCharacteristics,
            GSNAddress rncAddress, MAPExtensionContainer extensionContainer, Ext2QoSSubscribed qos2Subscribed,
            Ext2QoSSubscribed qos2Requested, Ext2QoSSubscribed qos2Negotiated, Ext3QoSSubscribed qos3Subscribed,
            Ext3QoSSubscribed qos3Requested, Ext3QoSSubscribed qos3Negotiated, Ext4QoSSubscribed qos4Subscribed,
            Ext4QoSSubscribed qos4Requested, Ext4QoSSubscribed qos4Negotiated, ExtPDPType extPdpType, PDPAddress extPdpAddress) {
        this.pdpContextIdentifier = pdpContextIdentifier;
        this.pdpContextActive = pdpContextActive;
        this.pdpType = pdpType;
        this.pdpAddress = pdpAddress;
        this.apnSubscribed = apnSubscribed;
        this.apnInUse = apnInUse;
        this.nsapi = asapi;
        this.transactionId = transactionId;
        this.teidForGnAndGp = teidForGnAndGp;
        this.teidForIu = teidForIu;
        this.ggsnAddress = ggsnAddress;
        this.qosSubscribed = qosSubscribed;
        this.qosRequested = qosRequested;
        this.qosNegotiated = qosNegotiated;
        this.chargingId = chargingId;
        this.chargingCharacteristics = chargingCharacteristics;
        this.rncAddress = rncAddress;
        this.extensionContainer = extensionContainer;
        this.qos2Subscribed = qos2Subscribed;
        this.qos2Requested = qos2Requested;
        this.qos2Negotiated = qos2Negotiated;
        this.qos3Subscribed = qos3Subscribed;
        this.qos3Requested = qos3Requested;
        this.qos3Negotiated = qos3Negotiated;
        this.qos4Subscribed = qos4Subscribed;
        this.qos4Requested = qos4Requested;
        this.qos4Negotiated = qos4Negotiated;
        this.extPdpType = extPdpType;
        this.extPdpAddress = extPdpAddress;
    }

    @Override
    public int getPdpContextIdentifier() {
        return pdpContextIdentifier;
    }

    @Override
    public boolean getPdpContextActive() {
        return pdpContextActive;
    }

    @Override
    public PDPType getPdpType() {
        return pdpType;
    }

    @Override
    public PDPAddress getPdpAddress() {
        return pdpAddress;
    }

    @Override
    public APN getApnSubscribed() {
        return apnSubscribed;
    }

    @Override
    public APN getApnInUse() {
        return apnInUse;
    }

    @Override
    public Integer getNsapi() {
        return nsapi;
    }

    @Override
    public TransactionId getTransactionId() {
        return transactionId;
    }

    @Override
    public TEID getTeidForGnAndGp() {
        return teidForGnAndGp;
    }

    @Override
    public TEID getTeidForIu() {
        return teidForIu;
    }

    @Override
    public GSNAddress getGgsnAddress() {
        return ggsnAddress;
    }

    @Override
    public ExtQoSSubscribed getQosSubscribed() {
        return qosSubscribed;
    }

    @Override
    public ExtQoSSubscribed getQosRequested() {
        return qosRequested;
    }

    @Override
    public ExtQoSSubscribed getQosNegotiated() {
        return qosNegotiated;
    }

    @Override
    public GPRSChargingID getChargingId() {
        return chargingId;
    }

    @Override
    public ChargingCharacteristics getChargingCharacteristics() {
        return chargingCharacteristics;
    }

    @Override
    public GSNAddress getRncAddress() {
        return rncAddress;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public Ext2QoSSubscribed getQos2Subscribed() {
        return qos2Subscribed;
    }

    @Override
    public Ext2QoSSubscribed getQos2Requested() {
        return qos2Requested;
    }

    @Override
    public Ext2QoSSubscribed getQos2Negotiated() {
        return qos2Negotiated;
    }

    @Override
    public Ext3QoSSubscribed getQos3Subscribed() {
        return qos3Subscribed;
    }

    @Override
    public Ext3QoSSubscribed getQos3Requested() {
        return qos3Requested;
    }

    @Override
    public Ext3QoSSubscribed getQos3Negotiated() {
        return qos3Negotiated;
    }

    @Override
    public Ext4QoSSubscribed getQos4Subscribed() {
        return qos4Subscribed;
    }

    @Override
    public Ext4QoSSubscribed getQos4Requested() {
        return qos4Requested;
    }

    @Override
    public Ext4QoSSubscribed getQos4Negotiated() {
        return qos4Negotiated;
    }

    @Override
    public ExtPDPType getExtPdpType() {
        return extPdpType;
    }

    @Override
    public PDPAddress getExtPdpAddress() {
        return extPdpAddress;
    }

    @Override
    public int getTag() throws MAPException {
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

        this.pdpContextIdentifier = 0;
        this.pdpContextActive = false;
        this.pdpType = null;
        this.pdpAddress = null;
        this.apnSubscribed = null;
        this.apnInUse = null;
        this.nsapi = null;
        this.transactionId = null;
        this.teidForGnAndGp = null;
        this.teidForIu = null;
        this.ggsnAddress = null;
        this.qosSubscribed = null;
        this.qosRequested = null;
        this.qosNegotiated = null;
        this.chargingId = null;
        this.chargingCharacteristics = null;
        this.rncAddress = null;
        this.extensionContainer = null;
        this.qos2Subscribed = null;
        this.qos2Requested = null;
        this.qos2Negotiated = null;
        this.qos3Subscribed = null;
        this.qos3Requested = null;
        this.qos3Negotiated = null;
        this.qos4Subscribed = null;
        this.qos4Requested = null;
        this.qos4Negotiated = null;
        this.extPdpType = null;
        this.extPdpAddress = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        boolean pdpContextIdentifierIsRead = false;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                switch (tag) {
                    case _ID_pdpContextIdentifier:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " pdpContextIdentifier: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.pdpContextIdentifier = (int) ais.readInteger();
                        pdpContextIdentifierIsRead = true;
                        break;
                    case _ID_pdpContextActive:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " pdpContextActive: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.pdpContextActive = true;
                        break;
                    case _ID_pdpType:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " pdpType: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.pdpType = new PDPTypeImpl();
                        ((PDPTypeImpl) this.pdpType).decodeAll(ais);
                        break;
                    case _ID_pdpAddress:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " pdpAddress: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.pdpAddress = new PDPAddressImpl();
                        ((PDPAddressImpl) this.pdpAddress).decodeAll(ais);
                        break;
                    case _ID_apnSubscribed:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " apnSubscribed: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.apnSubscribed = new APNImpl();
                        ((APNImpl) this.apnSubscribed).decodeAll(ais);
                        break;
                    case _ID_apnInUse:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " apnInUse: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.apnInUse = new APNImpl();
                        ((APNImpl) this.apnInUse).decodeAll(ais);
                        break;
                    case _ID_nsapi:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " nsapi: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.nsapi = (int) ais.readInteger();
                        break;
                    case _ID_transactionId:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " transactionId: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.transactionId = new TransactionIdImpl();
                        ((TransactionIdImpl) this.transactionId).decodeAll(ais);
                        break;
                    case _ID_teidForGnAndGp:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " teidForGnAndGp: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.teidForGnAndGp = new TEIDImpl();
                        ((TEIDImpl) this.teidForGnAndGp).decodeAll(ais);
                        break;
                    case _ID_teidForIu:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " teidForIu: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.teidForIu = new TEIDImpl();
                        ((TEIDImpl) this.teidForIu).decodeAll(ais);
                        break;
                    case _ID_ggsnAddress:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " ggsnAddress: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.ggsnAddress = new GSNAddressImpl();
                        ((GSNAddressImpl) this.ggsnAddress).decodeAll(ais);
                        break;
                    case _ID_qosSubscribed:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " qosSubscribed: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.qosSubscribed = new ExtQoSSubscribedImpl();
                        ((ExtQoSSubscribedImpl) this.qosSubscribed).decodeAll(ais);
                        break;
                    case _ID_qosRequested:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " qosRequested: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.qosRequested = new ExtQoSSubscribedImpl();
                        ((ExtQoSSubscribedImpl) this.qosRequested).decodeAll(ais);
                        break;
                    case _ID_qosNegotiated:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " qosNegotiated: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.qosNegotiated = new ExtQoSSubscribedImpl();
                        ((ExtQoSSubscribedImpl) this.qosNegotiated).decodeAll(ais);
                        break;
                    case _ID_chargingId:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " chargingId: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.chargingId = new GPRSChargingIDImpl();
                        ((GPRSChargingIDImpl) this.chargingId).decodeAll(ais);
                        break;
                    case _ID_chargingCharacteristics:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " chargingCharacteristics: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.chargingCharacteristics = new ChargingCharacteristicsImpl();
                        ((ChargingCharacteristicsImpl) this.chargingCharacteristics).decodeAll(ais);
                        break;
                    case _ID_rncAddress:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " rncAddress: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.rncAddress = new GSNAddressImpl();
                        ((GSNAddressImpl) this.rncAddress).decodeAll(ais);
                        break;
                    case _ID_extensionContainer:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " extensionContainer: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;
                    case _ID_qos2Subscribed:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " qos2Subscribed: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.qos2Subscribed = new Ext2QoSSubscribedImpl();
                        ((Ext2QoSSubscribedImpl) this.qos2Subscribed).decodeAll(ais);
                        break;
                    case _ID_qos2Requested:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " qos2Requested: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.qos2Requested = new Ext2QoSSubscribedImpl();
                        ((Ext2QoSSubscribedImpl) this.qos2Requested).decodeAll(ais);
                        break;
                    case _ID_qos2Negotiated:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " qos2Negotiated: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.qos2Negotiated = new Ext2QoSSubscribedImpl();
                        ((Ext2QoSSubscribedImpl) this.qos2Negotiated).decodeAll(ais);
                        break;
                    case _ID_qos3Subscribed:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " qos3Subscribed: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.qos3Subscribed = new Ext3QoSSubscribedImpl();
                        ((Ext3QoSSubscribedImpl) this.qos3Subscribed).decodeAll(ais);
                        break;
                    case _ID_qos3Requested:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " qos3Requested: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.qos3Requested = new Ext3QoSSubscribedImpl();
                        ((Ext3QoSSubscribedImpl) this.qos3Requested).decodeAll(ais);
                        break;
                    case _ID_qos3Negotiated:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " qos3Negotiated: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.qos3Negotiated = new Ext3QoSSubscribedImpl();
                        ((Ext3QoSSubscribedImpl) this.qos3Negotiated).decodeAll(ais);
                        break;
                    case _ID_qos4Subscribed:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " qos4Subscribed: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.qos4Subscribed = new Ext4QoSSubscribedImpl();
                        ((Ext4QoSSubscribedImpl) this.qos4Subscribed).decodeAll(ais);
                        break;
                    case _ID_qos4Requested:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " qos4Requested: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.qos4Requested = new Ext4QoSSubscribedImpl();
                        ((Ext4QoSSubscribedImpl) this.qos4Requested).decodeAll(ais);
                        break;
                    case _ID_qos4Negotiated:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " qos4Negotiated: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.qos4Negotiated = new Ext4QoSSubscribedImpl();
                        ((Ext4QoSSubscribedImpl) this.qos4Negotiated).decodeAll(ais);
                        break;
                    case _ID_extPdpType:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " extPdpType: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extPdpType = new ExtPDPTypeImpl();
                        ((ExtPDPTypeImpl) this.extPdpType).decodeAll(ais);
                        break;
                    case _ID_extPdpAddress:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + " extPdpAddress: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extPdpAddress = new PDPAddressImpl();
                        ((PDPAddressImpl) this.extPdpAddress).decodeAll(ais);
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (!pdpContextIdentifierIsRead) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": pdpContextIdentifier paramater is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
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

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {

            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdpContextIdentifier, this.pdpContextIdentifier);

            if (this.pdpContextActive)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdpContextActive);

            if (this.pdpType != null)
                ((PDPTypeImpl) this.pdpType).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdpType);

            if (this.pdpAddress != null)
                ((PDPAddressImpl) this.pdpAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdpAddress);

            if (this.apnSubscribed != null)
                ((APNImpl) this.apnSubscribed).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_apnSubscribed);

            if (this.apnInUse != null)
                ((APNImpl) this.apnInUse).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_apnInUse);

            if (this.nsapi != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_nsapi, this.nsapi);

            if (this.transactionId != null)
                ((TransactionIdImpl) this.transactionId).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_transactionId);

            if (this.teidForGnAndGp != null)
                ((TEIDImpl) this.teidForGnAndGp).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_teidForGnAndGp);

            if (this.teidForIu != null)
                ((TEIDImpl) this.teidForIu).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_teidForIu);

            if (this.ggsnAddress != null)
                ((GSNAddressImpl) this.ggsnAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_ggsnAddress);

            if (this.qosSubscribed != null)
                ((ExtQoSSubscribedImpl) this.qosSubscribed).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_qosSubscribed);

            if (this.qosRequested != null)
                ((ExtQoSSubscribedImpl) this.qosRequested).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_qosRequested);

            if (this.qosNegotiated != null)
                ((ExtQoSSubscribedImpl) this.qosNegotiated).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_qosNegotiated);

            if (this.chargingId != null)
                ((GPRSChargingIDImpl) this.chargingId).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_chargingId);

            if (this.chargingCharacteristics != null)
                ((ChargingCharacteristicsImpl) this.chargingCharacteristics).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_chargingCharacteristics);

            if (this.rncAddress != null)
                ((GSNAddressImpl) this.rncAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_rncAddress);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_extensionContainer);

            if (this.qos2Subscribed != null)
                ((Ext2QoSSubscribedImpl) this.qos2Subscribed).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_qos2Subscribed);

            if (this.qos2Requested != null)
                ((Ext2QoSSubscribedImpl) this.qos2Requested).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_qos2Requested);

            if (this.qos2Negotiated != null)
                ((Ext2QoSSubscribedImpl) this.qos2Negotiated).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_qos2Negotiated);

            if (this.qos3Subscribed != null)
                ((Ext3QoSSubscribedImpl) this.qos3Subscribed).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_qos3Subscribed);

            if (this.qos3Requested != null)
                ((Ext3QoSSubscribedImpl) this.qos3Requested).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_qos3Requested);

            if (this.qos3Negotiated != null)
                ((Ext3QoSSubscribedImpl) this.qos3Negotiated).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_qos3Negotiated);

            if (this.qos4Subscribed != null)
                ((Ext4QoSSubscribedImpl) this.qos4Subscribed).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_qos4Subscribed);

            if (this.qos4Requested != null)
                ((Ext4QoSSubscribedImpl) this.qos4Requested).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_qos4Requested);

            if (this.qos4Negotiated != null)
                ((Ext4QoSSubscribedImpl) this.qos4Negotiated).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_qos4Negotiated);

            if (this.extPdpType != null)
                ((ExtPDPTypeImpl) this.extPdpType).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extPdpType);

            if (this.extPdpAddress != null)
                ((PDPAddressImpl) this.extPdpAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extPdpAddress);

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

        sb.append("pdpContextIdentifier=");
        sb.append(this.pdpContextIdentifier);

        if (this.pdpContextActive) {
            sb.append(", pdpContextActive");
        }
        if (this.pdpType != null) {
            sb.append(", pdpType=");
            sb.append(this.pdpType);
        }
        if (this.pdpAddress != null) {
            sb.append(", pdpAddress=");
            sb.append(this.pdpAddress);
        }
        if (this.apnSubscribed != null) {
            sb.append(", apnSubscribed=");
            sb.append(this.apnSubscribed);
        }
        if (this.apnInUse != null) {
            sb.append(", apnInUse=");
            sb.append(this.apnInUse);
        }
        if (this.nsapi != null) {
            sb.append(", nsapi=");
            sb.append(this.nsapi);
        }
        if (this.transactionId != null) {
            sb.append(", transactionId=");
            sb.append(this.transactionId);
        }
        if (this.teidForGnAndGp != null) {
            sb.append(", teidForGnAndGp=");
            sb.append(this.teidForGnAndGp);
        }
        if (this.teidForIu != null) {
            sb.append(", teidForIu=");
            sb.append(this.teidForIu);
        }
        if (this.ggsnAddress != null) {
            sb.append(", ggsnAddress=");
            sb.append(this.ggsnAddress);
        }
        if (this.qosSubscribed != null) {
            sb.append(", qosSubscribed=");
            sb.append(this.qosSubscribed);
        }
        if (this.qosRequested != null) {
            sb.append(", qosRequested=");
            sb.append(this.qosRequested);
        }
        if (this.qosNegotiated != null) {
            sb.append(", qosNegotiated=");
            sb.append(this.qosNegotiated);
        }
        if (this.chargingId != null) {
            sb.append(", chargingId=");
            sb.append(this.chargingId);
        }
        if (this.chargingCharacteristics != null) {
            sb.append(", chargingCharacteristics=");
            sb.append(this.chargingCharacteristics);
        }
        if (this.rncAddress != null) {
            sb.append(", rncAddress=");
            sb.append(this.rncAddress);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.qos2Subscribed != null) {
            sb.append(", qos2Subscribed=");
            sb.append(this.qos2Subscribed);
        }
        if (this.qos2Requested != null) {
            sb.append(", qos2Requested=");
            sb.append(this.qos2Requested);
        }
        if (this.qos2Negotiated != null) {
            sb.append(", qos2Negotiated=");
            sb.append(this.qos2Negotiated);
        }
        if (this.qos3Subscribed != null) {
            sb.append(", qos3Subscribed=");
            sb.append(this.qos3Subscribed);
        }
        if (this.qos3Requested != null) {
            sb.append(", qos3Requested=");
            sb.append(this.qos3Requested);
        }
        if (this.qos3Negotiated != null) {
            sb.append(", qos3Negotiated=");
            sb.append(this.qos3Negotiated);
        }
        if (this.qos4Subscribed != null) {
            sb.append(", qos4Subscribed=");
            sb.append(this.qos4Subscribed);
        }
        if (this.qos4Requested != null) {
            sb.append(", qos4Requested=");
            sb.append(this.qos4Requested);
        }
        if (this.qos4Negotiated != null) {
            sb.append(", qos4Negotiated=");
            sb.append(this.qos4Negotiated);
        }
        if (this.extPdpType != null) {
            sb.append(", extPdpType=");
            sb.append(this.extPdpType);
        }
        if (this.extPdpAddress != null) {
            sb.append(", extPdpAddress=");
            sb.append(this.extPdpAddress);
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<PDPContextInfoImpl> PDP_CONTEXT_INFO_XML = new XMLFormat<PDPContextInfoImpl>(
            PDPContextInfoImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, PDPContextInfoImpl pdpContextInfo)
                throws XMLStreamException {

            pdpContextInfo.pdpContextIdentifier = xml.get(PDP_CONTEXT_IDENTIFIER, Integer.class);
            pdpContextInfo.pdpContextActive = xml.get(PDP_CONTEXT_ACTIVE, Boolean.class);
            pdpContextInfo.pdpType = xml.get(PDP_TYPE, PDPTypeImpl.class);
            pdpContextInfo.pdpAddress = xml.get(PDP_ADDRESS, PDPAddressImpl.class);
            pdpContextInfo.apnSubscribed = xml.get(APN_SUBSCRIBED, APNImpl.class);
            pdpContextInfo.apnInUse = xml.get(APN_IN_USE, APNImpl.class);
            pdpContextInfo.nsapi = xml.get(NSAPI, Integer.class);
            pdpContextInfo.transactionId = xml.get(TRANSACTION_ID, TransactionIdImpl.class);
            pdpContextInfo.teidForGnAndGp = xml.get(TEID_FOR_GN_AND_GP, TEIDImpl.class);
            pdpContextInfo.teidForIu = xml.get(TEID_FOR_IU, TEIDImpl.class);
            pdpContextInfo.ggsnAddress = xml.get(GGSN_ADDRESS, GSNAddressImpl.class);
            pdpContextInfo.qosSubscribed = xml.get(QOS_SUBSCRIBED, ExtQoSSubscribedImpl.class);
            pdpContextInfo.qosRequested = xml.get(QOS_REQUESTED, ExtQoSSubscribedImpl.class);
            pdpContextInfo.qosNegotiated = xml.get(QOS_NEGOTIATED, ExtQoSSubscribedImpl.class);
            pdpContextInfo.chargingId = xml.get(CHARGING_ID, GPRSChargingIDImpl.class);
            pdpContextInfo.chargingCharacteristics = xml.get(CHARGING_CHARACTERISTICS, ChargingCharacteristicsImpl.class);
            pdpContextInfo.rncAddress = xml.get(RNC_ADDRESS, GSNAddressImpl.class);
            pdpContextInfo.extensionContainer = xml.get(EXTENTSION_CONTAINER, MAPExtensionContainerImpl.class);
            pdpContextInfo.qos2Subscribed = xml.get(QOS2_SUBSCRIBED, Ext2QoSSubscribedImpl.class);
            pdpContextInfo.qos2Requested = xml.get(QOS2_REQUESTED, Ext2QoSSubscribedImpl.class);
            pdpContextInfo.qos2Negotiated = xml.get(QOS2_NEGOTIATED, Ext2QoSSubscribedImpl.class);
            pdpContextInfo.qos3Subscribed = xml.get(QOS3_SUBSCRIBED, Ext3QoSSubscribedImpl.class);
            pdpContextInfo.qos3Requested = xml.get(QOS3_REQUESTED, Ext3QoSSubscribedImpl.class);
            pdpContextInfo.qos3Negotiated = xml.get(QOS3_NEGOTIATED, Ext3QoSSubscribedImpl.class);
            pdpContextInfo.qos4Subscribed = xml.get(QOS4_SUBSCRIBED, Ext4QoSSubscribedImpl.class);
            pdpContextInfo.qos4Requested = xml.get(QOS4_REQUESTED, Ext4QoSSubscribedImpl.class);
            pdpContextInfo.qos4Negotiated = xml.get(QOS4_NEGOTIATED, Ext4QoSSubscribedImpl.class);
            pdpContextInfo.extPdpType = xml.get(EXT_PDP_TYPE, ExtPDPTypeImpl.class);
            pdpContextInfo.extPdpAddress = xml.get(EXT_PDP_ADDRESS, PDPAddressImpl.class);
        }

        @Override
        public void write(PDPContextInfoImpl pdpContextInfo, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
              xml.add((Integer) pdpContextInfo.getPdpContextIdentifier(), PDP_CONTEXT_IDENTIFIER, Integer.class);
              xml.add((Boolean) pdpContextInfo.getPdpContextActive(), PDP_CONTEXT_ACTIVE, Boolean.class);
              if (pdpContextInfo.getPdpType() != null)
                  xml.add((PDPTypeImpl) pdpContextInfo.getPdpType(), PDP_TYPE, PDPTypeImpl.class);
              if (pdpContextInfo.getPdpAddress() != null)
                  xml.add((PDPAddressImpl) pdpContextInfo.getPdpAddress(), PDP_ADDRESS, PDPAddressImpl.class);
              if (pdpContextInfo.getApnSubscribed() != null)
                  xml.add((APNImpl) pdpContextInfo.getApnSubscribed(), APN_SUBSCRIBED, APNImpl.class);
              if (pdpContextInfo.getApnInUse() != null)
                  xml.add((APNImpl) pdpContextInfo.getApnInUse(), APN_IN_USE, APNImpl.class);
              xml.add((Integer) pdpContextInfo.getNsapi(), NSAPI, Integer.class);
              if (pdpContextInfo.getTransactionId() != null)
                  xml.add((TransactionIdImpl) pdpContextInfo.getTransactionId(), TRANSACTION_ID, TransactionIdImpl.class);
              if (pdpContextInfo.getTeidForGnAndGp() != null)
                  xml.add((TEIDImpl) pdpContextInfo.getTeidForGnAndGp(), TEID_FOR_GN_AND_GP, TEIDImpl.class);
              if (pdpContextInfo.getTeidForIu() != null)
                  xml.add((TEIDImpl) pdpContextInfo.getTeidForIu(), TEID_FOR_IU, TEIDImpl.class);
              if (pdpContextInfo.getGgsnAddress() != null)
                  xml.add((GSNAddressImpl) pdpContextInfo.getGgsnAddress(), GGSN_ADDRESS, GSNAddressImpl.class);
              if (pdpContextInfo.getQosSubscribed() != null)
                  xml.add((ExtQoSSubscribedImpl) pdpContextInfo.getQosSubscribed(), QOS_SUBSCRIBED, ExtQoSSubscribedImpl.class);
              if (pdpContextInfo.getQosRequested() != null)
                  xml.add((ExtQoSSubscribedImpl) pdpContextInfo.getQosRequested(), QOS_REQUESTED, ExtQoSSubscribedImpl.class);
              if (pdpContextInfo.getQosNegotiated() != null)
                  xml.add((ExtQoSSubscribedImpl) pdpContextInfo.getQosNegotiated(), QOS_NEGOTIATED, ExtQoSSubscribedImpl.class);
              if (pdpContextInfo.getChargingId() != null)
                  xml.add((GPRSChargingIDImpl) pdpContextInfo.getChargingId(), CHARGING_ID, GPRSChargingIDImpl.class);
              if (pdpContextInfo.getChargingCharacteristics() != null)
                  xml.add((ChargingCharacteristicsImpl) pdpContextInfo.getChargingCharacteristics(), CHARGING_CHARACTERISTICS, ChargingCharacteristicsImpl.class);
              if (pdpContextInfo.getRncAddress() != null)
                  xml.add((GSNAddressImpl) pdpContextInfo.getRncAddress(), RNC_ADDRESS, GSNAddressImpl.class);
              if (pdpContextInfo.getExtensionContainer() != null)
                  xml.add((MAPExtensionContainerImpl) pdpContextInfo.getExtensionContainer(), EXTENTSION_CONTAINER, MAPExtensionContainerImpl.class);
              if (pdpContextInfo.getQos2Subscribed() != null)
                  xml.add((Ext2QoSSubscribedImpl) pdpContextInfo.getQos2Subscribed(), QOS2_SUBSCRIBED, Ext2QoSSubscribedImpl.class);
              if (pdpContextInfo.getQos2Requested() != null)
                  xml.add((Ext2QoSSubscribedImpl) pdpContextInfo.getQos2Requested(), QOS2_REQUESTED, Ext2QoSSubscribedImpl.class);
              if (pdpContextInfo.getQos2Negotiated() != null)
                  xml.add((Ext2QoSSubscribedImpl) pdpContextInfo.getQos2Negotiated(), QOS2_NEGOTIATED, Ext2QoSSubscribedImpl.class);
              if (pdpContextInfo.getQos3Subscribed() != null)
                  xml.add((Ext3QoSSubscribedImpl) pdpContextInfo.getQos3Subscribed(), QOS3_SUBSCRIBED, Ext3QoSSubscribedImpl.class);
              if (pdpContextInfo.getQos3Requested() != null)
                  xml.add((Ext3QoSSubscribedImpl) pdpContextInfo.getQos3Requested(), QOS3_REQUESTED, Ext3QoSSubscribedImpl.class);
              if (pdpContextInfo.getQos3Negotiated() != null)
                  xml.add((Ext3QoSSubscribedImpl) pdpContextInfo.getQos3Negotiated(), QOS2_NEGOTIATED, Ext3QoSSubscribedImpl.class);
              if (pdpContextInfo.getQos4Subscribed() != null)
                  xml.add((Ext4QoSSubscribedImpl) pdpContextInfo.getQos4Subscribed(), QOS4_SUBSCRIBED, Ext4QoSSubscribedImpl.class);
              if (pdpContextInfo.getQos4Requested() != null)
                  xml.add((Ext4QoSSubscribedImpl) pdpContextInfo.getQos4Requested(), QOS4_REQUESTED, Ext4QoSSubscribedImpl.class);
              if (pdpContextInfo.getQos4Negotiated() != null)
                  xml.add((Ext4QoSSubscribedImpl) pdpContextInfo.getQos4Negotiated(), QOS4_NEGOTIATED, Ext4QoSSubscribedImpl.class);
              if (pdpContextInfo.getExtPdpType() != null)
                  xml.add((ExtPDPTypeImpl) pdpContextInfo.getExtPdpType(), EXT_PDP_TYPE, ExtPDPTypeImpl.class);
              if (pdpContextInfo.getExtPdpAddress() != null)
                  xml.add((PDPAddressImpl) pdpContextInfo.getExtPdpAddress(), EXT_PDP_ADDRESS, PDPAddressImpl.class);
        }
    };
}
