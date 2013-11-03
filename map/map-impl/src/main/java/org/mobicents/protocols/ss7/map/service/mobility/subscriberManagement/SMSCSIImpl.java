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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SMSCSIImpl extends SequenceBase implements SMSCSI {

    public static final int _TAG_smsCamelTdpDataList = 0;
    public static final int _TAG_camelCapabilityHandling = 1;
    public static final int _TAG_extensionContainer = 2;
    public static final int _TAG_notificationToCSE = 3;
    public static final int _TAG_csiActive = 4;

    private ArrayList<SMSCAMELTDPData> smsCamelTdpDataList;
    private Integer camelCapabilityHandling;
    private MAPExtensionContainer extensionContainer;
    private boolean notificationToCSE;
    private boolean csiActive;

    public SMSCSIImpl() {
        super("SMSCSI");
    }

    public SMSCSIImpl(ArrayList<SMSCAMELTDPData> smsCamelTdpDataList, Integer camelCapabilityHandling,
            MAPExtensionContainer extensionContainer, boolean notificationToCSE, boolean csiActive) {
        super("SMSCSI");
        this.smsCamelTdpDataList = smsCamelTdpDataList;
        this.camelCapabilityHandling = camelCapabilityHandling;
        this.extensionContainer = extensionContainer;
        this.notificationToCSE = notificationToCSE;
        this.csiActive = csiActive;
    }

    @Override
    public ArrayList<SMSCAMELTDPData> getSmsCamelTdpDataList() {
        return this.smsCamelTdpDataList;
    }

    @Override
    public Integer getCamelCapabilityHandling() {
        return this.camelCapabilityHandling;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public boolean getNotificationToCSE() {
        return this.notificationToCSE;
    }

    @Override
    public boolean getCsiActive() {
        return this.csiActive;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.smsCamelTdpDataList = null;
        this.camelCapabilityHandling = null;
        this.extensionContainer = null;
        this.notificationToCSE = false;
        this.csiActive = false;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
                        case _TAG_smsCamelTdpDataList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".smsCamelTdpDataList: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            SMSCAMELTDPData smsCAMELTDPData = null;
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.smsCamelTdpDataList = new ArrayList<SMSCAMELTDPData>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad tag or tagClass or is primitive when decoding SMSCAMELTDPData",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                smsCAMELTDPData = new SMSCAMELTDPDataImpl();
                                ((SMSCAMELTDPDataImpl) smsCAMELTDPData).decodeAll(ais2);
                                this.smsCamelTdpDataList.add(smsCAMELTDPData);
                            }

                            if (this.smsCamelTdpDataList.size() < 1 || this.smsCamelTdpDataList.size() > 1) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter smsCamelTdpDataList size must be from 1 to 1, found: "
                                        + this.smsCamelTdpDataList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_camelCapabilityHandling:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".camelCapabilityHandling: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.camelCapabilityHandling = (int) ais.readInteger();
                            break;
                        case _TAG_extensionContainer:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;
                        case _TAG_notificationToCSE:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".notificationToCSE: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.notificationToCSE = true;
                            break;
                        case _TAG_csiActive:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".csiActive: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.csiActive = true;
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                }
                    break;
                default:
                    ais.advanceElement();
                    break;
            }
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.smsCamelTdpDataList != null && (this.smsCamelTdpDataList.size() < 1 || this.smsCamelTdpDataList.size() > 10)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter smsCamelTdpDataList size must be from 1 to 10, found: " + this.smsCamelTdpDataList.size());
        }

        try {

            if (this.smsCamelTdpDataList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_smsCamelTdpDataList);
                int pos = asnOs.StartContentDefiniteLength();
                for (SMSCAMELTDPData smsCAMELTDPData : this.smsCamelTdpDataList) {
                    ((SMSCAMELTDPDataImpl) smsCAMELTDPData).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }

            if (this.camelCapabilityHandling != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_camelCapabilityHandling, this.camelCapabilityHandling);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);

            if (notificationToCSE)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_notificationToCSE);

            if (csiActive)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_csiActive);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.smsCamelTdpDataList != null) {
            sb.append("smsCamelTdpDataList=[");
            boolean firstItem = true;
            for (SMSCAMELTDPData be : this.smsCamelTdpDataList) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.camelCapabilityHandling != null) {
            sb.append("camelCapabilityHandling=");
            sb.append(this.camelCapabilityHandling.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.notificationToCSE) {
            sb.append("notificationToCSE ");
            sb.append(", ");
        }

        if (this.csiActive) {
            sb.append("csiActive ");
        }

        sb.append("]");

        return sb.toString();
    }

}
