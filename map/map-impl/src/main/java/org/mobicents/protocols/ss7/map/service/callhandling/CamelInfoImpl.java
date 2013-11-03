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

package org.mobicents.protocols.ss7.map.service.callhandling;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CamelInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CamelInfoImpl extends SequenceBase implements CamelInfo {

    private static final int _TAG_offeredCamel4CSIs = 0;

    public SupportedCamelPhases supportedCamelPhases;
    public boolean suppressTCSI;
    public MAPExtensionContainer extensionContainer;
    public OfferedCamel4CSIs offeredCamel4CSIs;

    public CamelInfoImpl() {
        super("CamelInfo");
    }

    public CamelInfoImpl(SupportedCamelPhases supportedCamelPhases, boolean suppressTCSI,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIs) {
        super("CamelInfo");

        this.supportedCamelPhases = supportedCamelPhases;
        this.suppressTCSI = suppressTCSI;
        this.extensionContainer = extensionContainer;
        this.offeredCamel4CSIs = offeredCamel4CSIs;
    }

    @Override
    public SupportedCamelPhases getSupportedCamelPhases() {
        return supportedCamelPhases;
    }

    @Override
    public boolean getSuppressTCSI() {
        return suppressTCSI;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public OfferedCamel4CSIs getOfferedCamel4CSIs() {
        return offeredCamel4CSIs;
    }

    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.supportedCamelPhases = null;
        this.suppressTCSI = false;
        this.extensionContainer = null;
        this.offeredCamel4CSIs = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_UNIVERSAL: {
                    switch (tag) {
                        case Tag.STRING_BIT:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".supportedCamelPhases: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.supportedCamelPhases = new SupportedCamelPhasesImpl();
                            ((SupportedCamelPhasesImpl) this.supportedCamelPhases).decodeAll(ais);
                            break;
                        case Tag.NULL:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".suppressTCSI: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.suppressTCSI = true;
                            break;
                        case Tag.SEQUENCE:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter extensionContainer is primitive",
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
                        case _TAG_offeredCamel4CSIs:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".offeredCamel4CSIs: Parameter not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.offeredCamel4CSIs = new OfferedCamel4CSIsImpl();
                            ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).decodeAll(ais);
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

        if (supportedCamelPhases == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament supportedCamelPhases is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.supportedCamelPhases == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter supportedCamelPhases is not defined");
        }

        try {
            ((SupportedCamelPhasesImpl) this.supportedCamelPhases).encodeAll(asnOs);

            if (suppressTCSI)
                asnOs.writeNull();

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);

            if (this.offeredCamel4CSIs != null)
                ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIs).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_offeredCamel4CSIs);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((supportedCamelPhases == null) ? 0 : supportedCamelPhases.hashCode());
        result = prime * result + ((suppressTCSI == false) ? 0 : 1);
        result = prime * result + ((extensionContainer == null) ? 0 : extensionContainer.hashCode());
        result = prime * result + ((offeredCamel4CSIs == null) ? 0 : offeredCamel4CSIs.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CamelInfoImpl other = (CamelInfoImpl) obj;

        if (this.supportedCamelPhases == null) {
            if (other.supportedCamelPhases != null)
                return false;
        } else {
            if (!this.supportedCamelPhases.equals(other.supportedCamelPhases))
                return false;
        }

        if (this.suppressTCSI != other.suppressTCSI)
            return false;

        if (this.extensionContainer == null) {
            if (other.extensionContainer != null)
                return false;
        } else {
            if (!this.extensionContainer.equals(other.extensionContainer))
                return false;
        }

        if (this.offeredCamel4CSIs == null) {
            if (other.offeredCamel4CSIs != null)
                return false;
        } else {
            if (!this.offeredCamel4CSIs.equals(other.offeredCamel4CSIs))
                return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.supportedCamelPhases != null) {
            sb.append("supportedCamelPhases=");
            sb.append(this.supportedCamelPhases);
        }
        if (this.suppressTCSI) {
            sb.append(", suppressTCSI");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.offeredCamel4CSIs != null) {
            sb.append(", offeredCamel4CSIs=");
            sb.append(this.offeredCamel4CSIs);
        }

        return sb.toString();
    }
}
