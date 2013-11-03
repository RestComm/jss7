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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class LCSPrivacyCheckImpl extends SequenceBase implements LCSPrivacyCheck {

    private static final int _TAG_CALL_SESSION_UNRELATED = 0;
    private static final int _TAG_CALL_SESSION_RELATED = 1;

    private PrivacyCheckRelatedAction callSessionUnrelated;
    private PrivacyCheckRelatedAction callSessionRelated;

    /**
     *
     */
    public LCSPrivacyCheckImpl() {
        super("LCSPrivacyCheck");
    }

    /**
     * @param callSessionUnrelated
     * @param callSessionRelated
     */
    public LCSPrivacyCheckImpl(PrivacyCheckRelatedAction callSessionUnrelated, PrivacyCheckRelatedAction callSessionRelated) {
        super("LCSPrivacyCheck");

        this.callSessionUnrelated = callSessionUnrelated;
        this.callSessionRelated = callSessionRelated;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck# getCallSessionUnrelated()
     */
    public PrivacyCheckRelatedAction getCallSessionUnrelated() {
        return this.callSessionUnrelated;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck# getCallSessionRelated()
     */
    public PrivacyCheckRelatedAction getCallSessionRelated() {
        return this.callSessionRelated;
    }

    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.callSessionUnrelated = null;
        this.callSessionRelated = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int tag = ais.readTag();

        // Decode callSessionUnrelated [0] PrivacyCheckRelatedAction
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() || tag != _TAG_CALL_SESSION_UNRELATED) {
            throw new MAPParsingComponentException(
                    "Error while decoding LCSPrivacyCheck: Parameter 0 [callSessionUnrelated [0] PrivacyCheckRelatedAction] bad tag class, tag or not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        int action = (int) ais.readInteger();
        this.callSessionUnrelated = PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(action);

        while (true) {
            if (ais.available() == 0)
                break;

            tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_CALL_SESSION_RELATED:
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException(
                                    "Error while decoding LCSPrivacyCheck: Parameter 1 [callSessionRelated [1] PrivacyCheckRelatedAction] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        action = (int) ais.readInteger();
                        this.callSessionRelated = PrivacyCheckRelatedAction.getPrivacyCheckRelatedAction(action);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.callSessionUnrelated == null) {
            throw new MAPException(
                    "Error while encoding LCSPrivacyCheck the mandatory parameter callSessionUnrelated is not defined");
        }

        try {
            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_CALL_SESSION_UNRELATED, this.callSessionUnrelated.getAction());
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter LCSPrivacyCheck.callSessionUnrelated", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter LCSPrivacyCheck.callSessionUnrelated", e);
        }

        if (this.callSessionRelated != null) {
            try {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_CALL_SESSION_RELATED, this.callSessionRelated.getAction());
            } catch (IOException e) {
                throw new MAPException("IOException when encoding parameter LCSPrivacyCheck.callSessionRelated", e);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding parameter LCSPrivacyCheck.callSessionRelated", e);
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((callSessionRelated == null) ? 0 : callSessionRelated.hashCode());
        result = prime * result + ((callSessionUnrelated == null) ? 0 : callSessionUnrelated.hashCode());
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
        LCSPrivacyCheckImpl other = (LCSPrivacyCheckImpl) obj;
        if (callSessionRelated != other.callSessionRelated)
            return false;
        if (callSessionUnrelated != other.callSessionUnrelated)
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.callSessionUnrelated != null) {
            sb.append("callSessionUnrelated=");
            sb.append(this.callSessionUnrelated);
        }
        if (this.callSessionRelated != null) {
            sb.append(", callSessionRelated=");
            sb.append(this.callSessionRelated.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
