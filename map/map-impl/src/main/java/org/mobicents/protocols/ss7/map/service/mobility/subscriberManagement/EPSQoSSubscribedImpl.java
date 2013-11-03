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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AllocationRetentionPriority;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSClassIdentifier;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class EPSQoSSubscribedImpl extends SequenceBase implements EPSQoSSubscribed {

    private static final int _TAG_qoSClassIdentifier = 0;
    private static final int _TAG_allocationRetentionPriority = 1;
    private static final int _TAG_extensionContainer = 2;

    private QoSClassIdentifier qoSClassIdentifier;
    private AllocationRetentionPriority allocationRetentionPriority;
    private MAPExtensionContainer extensionContainer;

    public EPSQoSSubscribedImpl() {
        super("EPSQoSSubscribed");
    }

    public EPSQoSSubscribedImpl(QoSClassIdentifier qoSClassIdentifier, AllocationRetentionPriority allocationRetentionPriority,
            MAPExtensionContainer extensionContainer) {
        super("EPSQoSSubscribed");
        this.qoSClassIdentifier = qoSClassIdentifier;
        this.allocationRetentionPriority = allocationRetentionPriority;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public QoSClassIdentifier getQoSClassIdentifier() {
        return this.qoSClassIdentifier;
    }

    @Override
    public AllocationRetentionPriority getAllocationRetentionPriority() {
        return this.allocationRetentionPriority;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.qoSClassIdentifier = null;
        this.allocationRetentionPriority = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    if (tag != _TAG_qoSClassIdentifier || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC
                            || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".qoSClassIdentifier: Parameter bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    int qos = (int) ais.readInteger();
                    this.qoSClassIdentifier = QoSClassIdentifier.getInstance(qos);
                    break;
                case 1:
                    if (tag != _TAG_allocationRetentionPriority || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC
                            || ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".allocationRetentionPriority: Parameter bad tag, tag class or primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.allocationRetentionPriority = new AllocationRetentionPriorityImpl();
                    ((AllocationRetentionPriorityImpl) this.allocationRetentionPriority).decodeAll(ais);
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

        if (this.qoSClassIdentifier == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament qoSClassIdentifier is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.allocationRetentionPriority == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament allocationRetentionPriority is mandatory but does not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.qoSClassIdentifier == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter qoSClassIdentifier is not defined");
        }

        if (this.allocationRetentionPriority == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter allocationRetentionPriority is not defined");
        }

        try {

            if (this.qoSClassIdentifier != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_qoSClassIdentifier, this.qoSClassIdentifier.getCode());

            ((AllocationRetentionPriorityImpl) this.allocationRetentionPriority).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_allocationRetentionPriority);

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

        if (this.qoSClassIdentifier != null) {
            sb.append("qoSClassIdentifier=");
            sb.append(this.qoSClassIdentifier.toString());
            sb.append(", ");
        }

        if (this.allocationRetentionPriority != null) {
            sb.append("allocationRetentionPriority=");
            sb.append(this.allocationRetentionPriority.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
