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

package org.mobicents.protocols.ss7.map.service.sms;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;

public class AlertServiceCentreResponseImpl extends SmsMessageImpl implements AlertServiceCentreResponse {

    public int getTag() throws MAPException {

        throw new MAPException("AlertServiceCentreResponse has no MAP message primitive");
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.alertServiceCentre_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.alertServiceCentre;
    }

    public int getTagClass() {

        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        throw new MAPParsingComponentException("AlertServiceCentreResponse has no MAP message primitive",
                MAPParsingComponentExceptionReason.MistypedParameter);
    }

    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        throw new MAPParsingComponentException("AlertServiceCentreResponse has no MAP message primitive",
                MAPParsingComponentExceptionReason.MistypedParameter);
    }

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        throw new MAPException("AlertServiceCentreResponse has no MAP message primitive");
    }

    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        throw new MAPException("AlertServiceCentreResponse has no MAP message primitive");
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        throw new MAPException("AlertServiceCentreResponse has no MAP message primitive");
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AlertServiceCentreResponse [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        sb.append("]");

        return sb.toString();
    }
}
