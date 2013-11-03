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
import org.mobicents.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CUGInterlockImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CUGCheckInfoImpl extends SequenceBase implements CUGCheckInfo {

    private CUGInterlock cugInterlock;
    private boolean cugOutgoingAccess;
    private MAPExtensionContainer extensionContainer;

    public CUGCheckInfoImpl() {
        super("CUGCheckInfo");
    }

    public CUGCheckInfoImpl(CUGInterlock cugInterlock, boolean cugOutgoingAccess, MAPExtensionContainer extensionContainer) {
        super("CUGCheckInfo");

        this.cugInterlock = cugInterlock;
        this.cugOutgoingAccess = cugOutgoingAccess;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public CUGInterlock getCUGInterlock() {
        return cugInterlock;
    }

    @Override
    public boolean getCUGOutgoingAccess() {
        return cugOutgoingAccess;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.cugInterlock = null;
        this.cugOutgoingAccess = false;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_UNIVERSAL: {
                    switch (tag) {
                        case Tag.STRING_OCTET: // cug-Interlock
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".cug-Interlock: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.cugInterlock = new CUGInterlockImpl();
                            ((CUGInterlockImpl) this.cugInterlock).decodeAll(ais);
                            break;
                        case Tag.NULL: // cug-OutgoingAccess
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".cug-OutgoingAccess: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            this.cugOutgoingAccess = true;
                            ais.readNull();
                            break;
                        case Tag.SEQUENCE: // extensionContainer
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
                case Tag.CLASS_CONTEXT_SPECIFIC: {
                    switch (tag) {
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

        if (cugInterlock == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament cugInterlock is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            if (this.cugInterlock == null)
                throw new MAPException("Error while encoding" + _PrimitiveName + ": cugInterlock must not be null");

            ((CUGInterlockImpl) this.cugInterlock).encodeAll(asnOs);

            if (this.cugOutgoingAccess)
                asnOs.writeNull();

            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
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

        if (this.cugInterlock != null) {
            sb.append("cugInterlock=");
            sb.append(this.cugInterlock.toString());
        }
        if (this.cugOutgoingAccess) {
            sb.append(", cugOutgoingAccess");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
