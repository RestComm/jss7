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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalSubscriptions;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GroupId;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LongGroupId;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class VoiceGroupCallDataImpl extends SequenceBase implements VoiceGroupCallData {

    private static final int _TAG_additionalInfo = 0;
    private static final int _TAG_longGroupId = 1;

    private GroupId groupId;
    private MAPExtensionContainer extensionContainer;
    private AdditionalSubscriptions additionalSubscriptions;
    private AdditionalInfo additionalInfo;
    private LongGroupId longGroupId;

    public VoiceGroupCallDataImpl() {
        super("VoiceGroupCallData");
    }

    public VoiceGroupCallDataImpl(GroupId groupId, MAPExtensionContainer extensionContainer,
            AdditionalSubscriptions additionalSubscriptions, AdditionalInfo additionalInfo, LongGroupId longGroupId) {
        super("VoiceGroupCallData");
        this.groupId = groupId;
        this.extensionContainer = extensionContainer;
        this.additionalSubscriptions = additionalSubscriptions;
        this.additionalInfo = additionalInfo;
        this.longGroupId = longGroupId;
    }

    @Override
    public GroupId getGroupId() {
        return this.groupId;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public AdditionalSubscriptions getAdditionalSubscriptions() {
        return this.additionalSubscriptions;
    }

    @Override
    public AdditionalInfo getAdditionalInfo() {
        return this.additionalInfo;
    }

    @Override
    public LongGroupId getLongGroupId() {
        return this.longGroupId;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.groupId = null;
        this.extensionContainer = null;
        this.additionalSubscriptions = null;
        this.additionalInfo = null;
        this.longGroupId = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    if (!ais.isTagPrimitive() || tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".groupId:  Bad tab or tag class or Parameter not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.groupId = new GroupIdImpl();
                    ((GroupIdImpl) this.groupId).decodeAll(ais);
                    break;
                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_UNIVERSAL: {
                            switch (tag) {
                                case Tag.SEQUENCE:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".extensionContainer: Parameter extensionContainer is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.extensionContainer = new MAPExtensionContainerImpl();
                                    ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                                    break;
                                case Tag.STRING_BIT:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".additionalSubscriptions: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.additionalSubscriptions = new AdditionalSubscriptionsImpl();
                                    ((AdditionalSubscriptionsImpl) this.additionalSubscriptions).decodeAll(ais);
                                    break;
                                default:
                                    ais.advanceElement();
                                    break;
                            }
                        }
                            break;
                        case Tag.CLASS_CONTEXT_SPECIFIC: {
                            switch (tag) {
                                case _TAG_additionalInfo:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".additionalInfo: Parameter not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.additionalInfo = new AdditionalInfoImpl();
                                    ((AdditionalInfoImpl) this.additionalInfo).decodeAll(ais);
                                    break;
                                case _TAG_longGroupId:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".longGroupId: Parameter not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.longGroupId = new LongGroupIdImpl();
                                    ((LongGroupIdImpl) this.longGroupId).decodeAll(ais);
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
                    break;
            }
            num++;

        }

        if (this.groupId == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament groupId is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.groupId == null && this.longGroupId == null)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": groupId must not be null");

        if (this.longGroupId != null) {
            (new GroupIdImpl("")).encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET);
        } else {
            ((GroupIdImpl) this.groupId).encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET);
        }

        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);

        if (this.additionalSubscriptions != null)
            ((AdditionalSubscriptionsImpl) this.additionalSubscriptions).encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_BIT);

        if (this.additionalInfo != null)
            ((AdditionalInfoImpl) this.additionalInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_additionalInfo);

        if (this.longGroupId != null)
            ((LongGroupIdImpl) this.longGroupId).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_longGroupId);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.groupId != null) {
            sb.append("groupId=");
            sb.append(this.groupId.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.additionalSubscriptions != null) {
            sb.append("additionalSubscriptions=");
            sb.append(this.additionalSubscriptions.toString());
            sb.append(", ");
        }

        if (this.additionalInfo != null) {
            sb.append("additionalInfo=");
            sb.append(this.additionalInfo.toString());
            sb.append(", ");
        }

        if (this.longGroupId != null) {
            sb.append("longGroupId=");
            sb.append(this.longGroupId.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
