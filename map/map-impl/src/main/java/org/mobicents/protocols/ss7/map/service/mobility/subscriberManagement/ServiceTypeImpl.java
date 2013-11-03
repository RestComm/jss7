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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GMLCRestriction;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.NotificationToMSUser;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ServiceType;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ServiceTypeImpl extends SequenceBase implements ServiceType {

    private static final int _TAG_gmlcRestriction = 0;
    private static final int _TAG_notificationToMSUser = 1;
    private static final int _TAG_extensionContainer = 2;

    private int serviceTypeIdentity;
    private GMLCRestriction gmlcRestriction;
    private NotificationToMSUser notificationToMSUser;
    private MAPExtensionContainer extensionContainer;

    public ServiceTypeImpl() {
        super("ServiceType");
    }

    public ServiceTypeImpl(int serviceTypeIdentity, GMLCRestriction gmlcRestriction, NotificationToMSUser notificationToMSUser,
            MAPExtensionContainer extensionContainer) {
        super("ServiceType");
        this.serviceTypeIdentity = serviceTypeIdentity;
        this.gmlcRestriction = gmlcRestriction;
        this.notificationToMSUser = notificationToMSUser;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public int getServiceTypeIdentity() {
        return this.serviceTypeIdentity;
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

        this.serviceTypeIdentity = -1;
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
                    if (tag != Tag.INTEGER || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".serviceTypeIdentity: bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.serviceTypeIdentity = (int) ais.readInteger();
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
                                    int i1 = (int) ais.readInteger();
                                    this.gmlcRestriction = GMLCRestriction.getInstance(i1);
                                    break;
                                case _TAG_notificationToMSUser:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".notificationToMSUser: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    int i2 = (int) ais.readInteger();
                                    this.notificationToMSUser = NotificationToMSUser.getInstance(i2);
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

        if (this.serviceTypeIdentity == -1) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament serviceTypeIdentity is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        try {
            asnOs.writeInteger(this.serviceTypeIdentity);

            if (this.gmlcRestriction != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_gmlcRestriction, this.gmlcRestriction.getCode());

            if (this.notificationToMSUser != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_notificationToMSUser, this.notificationToMSUser.getCode());

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);

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

        sb.append("serviceTypeIdentity=");
        sb.append(this.serviceTypeIdentity);
        sb.append(", ");

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
