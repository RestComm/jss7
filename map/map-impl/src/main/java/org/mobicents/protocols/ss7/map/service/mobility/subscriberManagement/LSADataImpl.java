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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAAttributes;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class LSADataImpl extends SequenceBase implements LSAData {

    public static final int _TAG_lsaIdentity = 0;
    public static final int _TAG_lsaAttributes = 1;
    public static final int _TAG_lsaActiveModeIndicator = 2;
    public static final int _TAG_extensionContainer = 3;

    private LSAIdentity lsaIdentity;
    private LSAAttributes lsaAttributes;
    private boolean lsaActiveModeIndicator;
    private MAPExtensionContainer extensionContainer;

    public LSADataImpl() {
        super("LSAData");
    }

    public LSADataImpl(LSAIdentity lsaIdentity, LSAAttributes lsaAttributes, boolean lsaActiveModeIndicator,
            MAPExtensionContainer extensionContainer) {
        super("LSAData");
        this.lsaIdentity = lsaIdentity;
        this.lsaAttributes = lsaAttributes;
        this.lsaActiveModeIndicator = lsaActiveModeIndicator;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public LSAIdentity getLSAIdentity() {
        return this.lsaIdentity;
    }

    @Override
    public LSAAttributes getLSAAttributes() {
        return this.lsaAttributes;
    }

    @Override
    public boolean getLsaActiveModeIndicator() {
        return this.lsaActiveModeIndicator;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.lsaIdentity = null;
        this.lsaAttributes = null;
        this.lsaActiveModeIndicator = false;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    if (tag != _TAG_lsaIdentity || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter lsaIdentity bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.lsaIdentity = new LSAIdentityImpl();
                    ((LSAIdentityImpl) this.lsaIdentity).decodeAll(ais);
                    break;
                case 1:
                    if (tag != _TAG_lsaAttributes || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter lsaAttributes  bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.lsaAttributes = new LSAAttributesImpl();
                    ((LSAAttributesImpl) this.lsaAttributes).decodeAll(ais);

                    break;
                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_CONTEXT_SPECIFIC: {
                            switch (tag) {
                                case _TAG_lsaActiveModeIndicator:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".lsaActiveModeIndicator: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    ais.readNull();
                                    this.lsaActiveModeIndicator = true;
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

        if (this.lsaIdentity == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament lsaIdentity is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.lsaAttributes == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament lsaAttributes is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.lsaIdentity == null)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": lsaIdentity must not be null");

        if (this.lsaAttributes == null)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": lsaAttributes must not be null");

        try {
            ((LSAIdentityImpl) this.lsaIdentity).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_lsaIdentity);

            ((LSAAttributesImpl) this.lsaAttributes).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_lsaAttributes);

            if (this.lsaActiveModeIndicator)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_lsaActiveModeIndicator);

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

        if (this.lsaIdentity != null) {
            sb.append("lsaIdentity=");
            sb.append(this.lsaIdentity.toString());
            sb.append(", ");
        }

        if (this.lsaAttributes != null) {
            sb.append("lsaAttributes=");
            sb.append(this.lsaAttributes.toString());
            sb.append(", ");
        }

        if (this.lsaActiveModeIndicator) {
            sb.append("lsaActiveModeIndicator, ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());

        }

        sb.append("]");

        return sb.toString();
    }
}
