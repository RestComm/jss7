/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.tcapAnsi.tc.dialog.events;

import org.mobicents.protocols.ss7.tcapAnsi.api.MessageType;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.DraftParsedMessage;

/**
 * @author sergey vetyutnev
 *
 */
public class DraftParsedMessageImpl implements DraftParsedMessage {

    private MessageType messageType = MessageType.Unknown;
    private Long originationDialogId;
    private Long destinationDialogId;
    private String parsingErrorReason;

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType val) {
        messageType = val;
    }

    @Override
    public Long getOriginationDialogId() {
        return originationDialogId;
    }

    public void setOriginationDialogId(Long val) {
        originationDialogId = val;
    }

    @Override
    public Long getDestinationDialogId() {
        return destinationDialogId;
    }

    public void setDestinationDialogId(Long val) {
        destinationDialogId = val;
    }

    @Override
    public String getParsingErrorReason() {
        return parsingErrorReason;
    }

    public void setParsingErrorReason(String val) {
        parsingErrorReason = val;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("DraftParsedMessage [");
        sb.append(messageType);
        if (originationDialogId != null) {
            sb.append(", originationDialogId=");
            sb.append(originationDialogId);
        }
        if (destinationDialogId != null) {
            sb.append(", destinationDialogId=");
            sb.append(destinationDialogId);
        }
        if (parsingErrorReason != null) {
            sb.append(", parsingErrorReason=");
            sb.append(parsingErrorReason);
        }
        sb.append("]");

        return sb.toString();
    }

}
