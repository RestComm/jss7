/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ODBDataImpl;

import java.io.IOException;

/**
 * Created by vsubbotin on 25/05/16.
 */
public class ODBInfoImpl extends SequenceBase implements ODBInfo {
    public static final String _PrimitiveName = "ODBInfo";

    private ODBData odbData;
    private boolean notificationToCSE;
    private MAPExtensionContainer extensionContainer;

    public ODBInfoImpl() {
        super(_PrimitiveName);
    }

    public ODBInfoImpl(ODBData odbData, boolean notificationToCSE, MAPExtensionContainer extensionContainer) {
        super(_PrimitiveName);
        this.odbData = odbData;
        this.notificationToCSE = notificationToCSE;
        this.extensionContainer = extensionContainer;
    }

    public ODBData getOdbData() {
        return this.odbData;
    }

    public boolean getNotificationToCSE() {
        return this.notificationToCSE;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.odbData = null;
        this.notificationToCSE = false;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int tag = ais.readTag();
        //decode odbData
        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || tag != Tag.SEQUENCE || ais.isTagPrimitive()) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parameter odbData has bad tag or tag class or not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        this.odbData = new ODBDataImpl();
        ((ODBDataImpl)this.odbData).decodeAll(ais);

        while (true) {
            if (ais.available() == 0)
                break;

            tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                switch (tag) {
                    case Tag.NULL:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".notificationToCSE: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.notificationToCSE = Boolean.TRUE;
                        break;
                    case Tag.SEQUENCE:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".extensionContainer: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
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
        if (odbData == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter odbData is not defined");
        }

        try {
            ((ODBDataImpl)this.odbData).encodeAll(asnOs);

            if (this.notificationToCSE) {
                asnOs.writeNull();
            }

            if (this.extensionContainer != null) {
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.odbData != null) {
            sb.append("odbData=");
            sb.append(this.odbData);
        }
        if (this.notificationToCSE) {
            sb.append(", notificationToCSE");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        sb.append("]");
        return sb.toString();
    }
}
