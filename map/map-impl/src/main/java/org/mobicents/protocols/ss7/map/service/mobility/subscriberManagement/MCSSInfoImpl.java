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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class MCSSInfoImpl extends SequenceBase implements MCSSInfo {

    public static final int _TAG_ssCode = 0;
    public static final int _TAG_ssStatus = 1;
    public static final int _TAG_nbrSB = 2;
    public static final int _TAG_nbrUser = 3;
    public static final int _TAG_extensionContainer = 4;

    private SSCode ssCode;
    private ExtSSStatus ssStatus;
    private int nbrSB;
    private int nbrUser;
    private MAPExtensionContainer extensionContainer;

    public MCSSInfoImpl() {
        super("MCSSInfo");
    }

    public MCSSInfoImpl(SSCode ssCode, ExtSSStatus ssStatus, int nbrSB, int nbrUser, MAPExtensionContainer extensionContainer) {
        super("MCSSInfo");
        this.ssCode = ssCode;
        this.ssStatus = ssStatus;
        this.nbrSB = nbrSB;
        this.nbrUser = nbrUser;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public SSCode getSSCode() {
        return this.ssCode;
    }

    @Override
    public ExtSSStatus getSSStatus() {
        return this.ssStatus;
    }

    @Override
    public int getNbrSB() {
        return this.nbrSB;
    }

    @Override
    public int getNbrUser() {
        return this.nbrUser;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.ssCode = null;
        this.ssStatus = null;
        this.nbrSB = -1;
        this.nbrUser = -1;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    if (tag != _TAG_ssCode || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ssCode: bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.ssCode = new SSCodeImpl();
                    ((SSCodeImpl) this.ssCode).decodeAll(ais);
                    break;
                case 1:
                    if (tag != _TAG_ssStatus || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".ssStatus: bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.ssStatus = new ExtSSStatusImpl();
                    ((ExtSSStatusImpl) this.ssStatus).decodeAll(ais);
                    break;
                case 2:
                    if (tag != _TAG_nbrSB || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".nbrSB: bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.nbrSB = (int) ais.readInteger();
                    break;
                case 3:
                    if (tag != _TAG_nbrUser || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".nbrUser: bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.nbrUser = (int) ais.readInteger();
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

        if (this.ssCode == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament ssCode is mandatory but does not found", MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.ssStatus == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament ssStatus is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.nbrSB == -1) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament nbrSB is mandatory but does not found", MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.nbrUser == -1) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament nbrUser is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.ssCode == null)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": ssCode must not be null");

        if (this.ssStatus == null)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": ssStatus must not be null");

        if (this.nbrSB < 2 || this.nbrSB > 7)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": nbrSB value muct be between 2 and 7");

        if (this.nbrUser < 1 || this.nbrUser > 7)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": nbrUser value muct be between 1 and 7");

        try {

            ((SSCodeImpl) this.ssCode).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ssCode);

            ((ExtSSStatusImpl) this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ssStatus);

            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_nbrSB, nbrSB);

            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_nbrUser, nbrUser);

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

        if (this.ssCode != null) {
            sb.append("ssCode=");
            sb.append(this.ssCode.toString());
            sb.append(", ");
        }

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus.toString());
            sb.append(", ");
        }

        sb.append("nbrSB=");
        sb.append(this.nbrSB);
        sb.append(", ");

        sb.append("nbrUser=");
        sb.append(this.nbrUser);
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
