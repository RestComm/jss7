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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ODBDataImpl extends SequenceBase implements ODBData {

    private static final long serialVersionUID = 1L;

    private ODBGeneralData oDBGeneralData;
    private ODBHPLMNData odbHplmnData;
    private MAPExtensionContainer extensionContainer;

    public ODBDataImpl() {
        super("ODBData");
    }

    public ODBDataImpl(ODBGeneralData oDBGeneralData, ODBHPLMNData odbHplmnData, MAPExtensionContainer extensionContainer) {
        super("ODBData");
        this.oDBGeneralData = oDBGeneralData;
        this.odbHplmnData = odbHplmnData;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public ODBGeneralData getODBGeneralData() {
        return this.oDBGeneralData;
    }

    @Override
    public ODBHPLMNData getOdbHplmnData() {
        return this.odbHplmnData;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.oDBGeneralData = null;
        this.odbHplmnData = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    if (!ais.isTagPrimitive() || tag != Tag.STRING_BIT)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".oDBGeneralData: bad tag or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.oDBGeneralData = new ODBGeneralDataImpl();
                    ((ODBGeneralDataImpl) this.oDBGeneralData).decodeAll(ais);
                    break;
                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_UNIVERSAL: {
                            switch (tag) {
                                case Tag.STRING_BIT:
                                    if (!ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".odbHplmnData: Parameter is not primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    this.odbHplmnData = new ODBHPLMNDataImpl();
                                    ((ODBHPLMNDataImpl) this.odbHplmnData).decodeAll(ais);
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
                        }
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }

            }

            num++;
        }

        if (this.oDBGeneralData == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament oDBGeneralData is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.oDBGeneralData == null)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": oDBGeneralData must not be null");

        ((ODBGeneralDataImpl) this.oDBGeneralData).encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_BIT);

        if (this.odbHplmnData != null)
            ((ODBHPLMNDataImpl) this.odbHplmnData).encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_BIT);

        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.oDBGeneralData != null) {
            sb.append("oDBGeneralData=");
            sb.append(this.oDBGeneralData.toString());
            sb.append(", ");
        }

        if (this.odbHplmnData != null) {
            sb.append("odbHplmnData=");
            sb.append(this.odbHplmnData.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

}
