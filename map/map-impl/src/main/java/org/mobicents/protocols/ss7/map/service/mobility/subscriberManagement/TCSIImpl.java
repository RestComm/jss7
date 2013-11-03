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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TCSIImpl extends SequenceBase implements TCSI {

    public static final int _TAGcamelCapabilityHandling = 0;
    public static final int _TAGnotificationToCSE = 1;
    public static final int _TAGcsi_Active = 2;

    public ArrayList<TBcsmCamelTDPData> tBcsmCamelTDPDataList;
    public MAPExtensionContainer extensionContainer;
    public Integer camelCapabilityHandling;
    public boolean notificationToCSE;
    public boolean csiActive;

    public TCSIImpl() {
        super("TCSI");
    }

    public TCSIImpl(ArrayList<TBcsmCamelTDPData> tBcsmCamelTDPDataList, MAPExtensionContainer extensionContainer,
            Integer camelCapabilityHandling, boolean notificationToCSE, boolean csiActive) {
        super("TCSI");

        this.tBcsmCamelTDPDataList = tBcsmCamelTDPDataList;
        this.extensionContainer = extensionContainer;
        this.camelCapabilityHandling = camelCapabilityHandling;
        this.notificationToCSE = notificationToCSE;
        this.csiActive = csiActive;
    }

    @Override
    public ArrayList<TBcsmCamelTDPData> getTBcsmCamelTDPDataList() {
        return tBcsmCamelTDPDataList;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public Integer getCamelCapabilityHandling() {
        return camelCapabilityHandling;
    }

    @Override
    public boolean getNotificationToCSE() {
        return notificationToCSE;
    }

    @Override
    public boolean getCsiActive() {
        return csiActive;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.tBcsmCamelTDPDataList = null;
        this.extensionContainer = null;
        this.camelCapabilityHandling = null;
        this.notificationToCSE = false;
        this.csiActive = false;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    // t-BcsmCamelTDPDataList
                    if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL || ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter 0 bad tag, tag class or primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);

                    this.tBcsmCamelTDPDataList = new ArrayList<TBcsmCamelTDPData>();
                    AsnInputStream ais2 = ais.readSequenceStream();

                    while (true) {
                        if (ais2.available() == 0)
                            break;

                        int tag2 = ais2.readTag();

                        if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": bad tag or tagClass or is primitive when decoding tBcsmCamelTDPDataList",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        TBcsmCamelTDPDataImpl elem = new TBcsmCamelTDPDataImpl();
                        ((TBcsmCamelTDPDataImpl) elem).decodeAll(ais2);
                        this.tBcsmCamelTDPDataList.add(elem);
                    }

                    if (this.tBcsmCamelTDPDataList.size() < 1 || this.tBcsmCamelTDPDataList.size() > 10)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".tBcsmCamelTDPDataList: tBcsmCamelTDPData elements count must be from 1 to 10, found: "
                                + this.tBcsmCamelTDPDataList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                    break;

                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_UNIVERSAL: {
                            switch (tag) {
                                case Tag.SEQUENCE: // forwardingData
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".forwardingData: Parameter is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.extensionContainer = new MAPExtensionContainerImpl();
                                    ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                                    break;
                                default:
                                    ais.advanceElement();
                                    break;
                            }
                        }
                            break;
                        case Tag.CLASS_CONTEXT_SPECIFIC: {
                            switch (tag) {
                                case _TAGcamelCapabilityHandling:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".camelCapabilityHandling: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.camelCapabilityHandling = (int) ais.readInteger();
                                    break;
                                case _TAGnotificationToCSE:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".notificationToCSE: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.notificationToCSE = true;
                                    ais.readNull();
                                    break;
                                case _TAGcsi_Active:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".csiActive: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.csiActive = true;
                                    ais.readNull();
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

            num++;
        }

        if (num < 1) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament tBcsmCamelTDPDataList is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            if (this.tBcsmCamelTDPDataList == null)
                throw new MAPException("Error while encoding" + _PrimitiveName + ": tBcsmCamelTDPDataList must not be null");
            if (this.tBcsmCamelTDPDataList.size() < 1 || this.tBcsmCamelTDPDataList.size() > 10)
                throw new MAPException("Error while encoding" + _PrimitiveName
                        + ": tBcsmCamelTDPDataList size must be from 1 to 10, found: " + this.tBcsmCamelTDPDataList.size());

            asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int pos = asnOs.StartContentDefiniteLength();
            for (TBcsmCamelTDPData be : this.tBcsmCamelTDPDataList) {
                TBcsmCamelTDPDataImpl bee = (TBcsmCamelTDPDataImpl) be;
                bee.encodeAll(asnOs);
            }
            asnOs.FinalizeContent(pos);

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);

            if (this.camelCapabilityHandling != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAGcamelCapabilityHandling, this.camelCapabilityHandling);

            if (this.notificationToCSE)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAGnotificationToCSE);

            if (this.csiActive)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAGcsi_Active);

        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._PrimitiveName);
        sb.append(" [");

        if (this.tBcsmCamelTDPDataList != null) {
            sb.append("tBcsmCamelTDPDataList=[");
            for (TBcsmCamelTDPData be : this.tBcsmCamelTDPDataList) {
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.camelCapabilityHandling != null) {
            sb.append(", camelCapabilityHandling=");
            sb.append(this.camelCapabilityHandling);
        }
        if (this.notificationToCSE) {
            sb.append(", notificationToCSE");
        }
        if (this.csiActive) {
            sb.append(", csiActive");
        }

        sb.append("]");

        return sb.toString();
    }

}
