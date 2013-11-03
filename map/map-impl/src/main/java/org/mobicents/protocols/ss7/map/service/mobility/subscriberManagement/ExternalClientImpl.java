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

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExternalClient;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GMLCRestriction;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.NotificationToMSUser;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ExternalClientImpl extends SequenceBase implements ExternalClient {

    private static final int _TAG_gmlcRestriction = 0;
    private static final int _TAG_notificationToMSUser = 1;
    private static final int _TAG_extensionContainer = 2;

    private LCSClientExternalID clientIdentity;
    private GMLCRestriction gmlcRestriction;
    private NotificationToMSUser notificationToMSUser;
    private MAPExtensionContainer extensionContainer;

    public ExternalClientImpl() {
        super("ExternalClient");
    }

    public ExternalClientImpl(LCSClientExternalID clientIdentity, GMLCRestriction gmlcRestriction,
            NotificationToMSUser notificationToMSUser, MAPExtensionContainer extensionContainer) {
        super("ExternalClient");
        this.clientIdentity = clientIdentity;
        this.gmlcRestriction = gmlcRestriction;
        this.notificationToMSUser = notificationToMSUser;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public LCSClientExternalID getClientIdentity() {
        return this.clientIdentity;
    }

    @Override
    public GMLCRestriction getGMLCRestriction() {
        return this.gmlcRestriction;
    }

    @Override
    public NotificationToMSUser getNotificationToMSUser() {
        return this.notificationToMSUser;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.clientIdentity = null;
        this.gmlcRestriction = null;
        this.notificationToMSUser = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL || ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".clientIdentity: Parameter bad tag, tag class or primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.clientIdentity = new LCSClientExternalIDImpl();
                    ((LCSClientExternalIDImpl) this.clientIdentity).decodeAll(ais);
                    break;
                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_CONTEXT_SPECIFIC: {
                            switch (tag) {
                                case _TAG_gmlcRestriction:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".gmlcRestriction: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    int code = (int) ais.readInteger();
                                    this.gmlcRestriction = GMLCRestriction.getInstance(code);
                                    break;
                                case _TAG_notificationToMSUser:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".notificationToMSUser: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    int code1 = (int) ais.readInteger();
                                    this.notificationToMSUser = NotificationToMSUser.getInstance(code1);
                                    break;
                                case _TAG_extensionContainer:
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
                        }
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
            }

            num++;
        }

        if (this.clientIdentity == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament clientIdentity is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        try {
            if (this.clientIdentity == null)
                throw new MAPException("Error while encoding" + _PrimitiveName + ": clientIdentity must not be null");

            try {

                ((LCSClientExternalIDImpl) this.clientIdentity).encodeAll(asnOs);

                if (this.gmlcRestriction != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_gmlcRestriction, gmlcRestriction.getCode());

                if (this.notificationToMSUser != null)
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_notificationToMSUser, notificationToMSUser.getCode());

                if (this.extensionContainer != null)
                    ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            _TAG_extensionContainer);

            } catch (IOException e) {
                throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.clientIdentity != null) {
            sb.append("clientIdentity=");
            sb.append(this.clientIdentity.toString());
            sb.append(", ");
        }

        if (this.gmlcRestriction != null) {
            sb.append("gmlcRestriction=");
            sb.append(this.gmlcRestriction.toString());
            sb.append(", ");
        }

        if (this.notificationToMSUser != null) {
            sb.append("notificationToMSUser=");
            sb.append(this.notificationToMSUser.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
