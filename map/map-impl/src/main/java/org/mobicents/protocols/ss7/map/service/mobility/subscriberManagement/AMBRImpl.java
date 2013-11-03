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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AMBR;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class AMBRImpl extends SequenceBase implements AMBR {

    private static final int _TAG_maxRequestedBandwidthUL = 0;
    private static final int _TAG_maxRequestedBandwidthDL = 1;
    private static final int _TAG_extensionContainer = 2;

    private int maxRequestedBandwidthUL;
    private int maxRequestedBandwidthDL;
    private MAPExtensionContainer extensionContainer = null;

    public AMBRImpl() {
        super("AMBR");
    }

    public AMBRImpl(int maxRequestedBandwidthUL, int maxRequestedBandwidthDL, MAPExtensionContainer extensionContainer) {
        super("AMBR");
        this.maxRequestedBandwidthUL = maxRequestedBandwidthUL;
        this.maxRequestedBandwidthDL = maxRequestedBandwidthDL;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public int getMaxRequestedBandwidthUL() {
        return this.maxRequestedBandwidthUL;
    }

    @Override
    public int getMaxRequestedBandwidthDL() {
        return this.maxRequestedBandwidthDL;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.maxRequestedBandwidthUL = -1;
        this.maxRequestedBandwidthDL = -1;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    if (tag != _TAG_maxRequestedBandwidthUL || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC
                            || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".maxRequestedBandwidthUL: Parameter bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.maxRequestedBandwidthUL = (int) ais.readInteger();
                    break;
                case 1:
                    if (tag != _TAG_maxRequestedBandwidthDL || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC
                            || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".maxRequestedBandwidthDL: Parameter bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.maxRequestedBandwidthDL = (int) ais.readInteger();
                    break;
                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_CONTEXT_SPECIFIC: {
                            switch (tag) {
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

        if (this.maxRequestedBandwidthUL == -1) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament maxRequestedBandwidthUL is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.maxRequestedBandwidthDL == -1) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament maxRequestedBandwidthDL is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_maxRequestedBandwidthUL, this.maxRequestedBandwidthUL);

            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_maxRequestedBandwidthDL, this.maxRequestedBandwidthDL);

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

        sb.append("maxRequestedBandwidthUL=");
        sb.append(this.maxRequestedBandwidthUL);
        sb.append(", ");

        sb.append("maxRequestedBandwidthDL=");
        sb.append(this.maxRequestedBandwidthDL);
        sb.append(", ");

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }
}
