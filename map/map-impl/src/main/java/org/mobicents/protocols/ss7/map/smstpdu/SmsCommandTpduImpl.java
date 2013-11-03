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

package org.mobicents.protocols.ss7.map.smstpdu;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.smstpdu.AddressField;
import org.mobicents.protocols.ss7.map.api.smstpdu.CommandData;
import org.mobicents.protocols.ss7.map.api.smstpdu.CommandType;
import org.mobicents.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsCommandTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpduType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SmsCommandTpduImpl extends SmsTpduImpl implements SmsCommandTpdu {

    private boolean userDataHeaderIndicator;
    private boolean statusReportRequest;
    private int messageReference;
    private ProtocolIdentifier protocolIdentifier;
    private CommandType commandType;
    private int messageNumber;
    private AddressField destinationAddress;
    private int commandDataLength;
    private CommandData commandData;

    private SmsCommandTpduImpl() {
        this.tpduType = SmsTpduType.SMS_COMMAND;
        this.mobileOriginatedMessage = true;
    }

    public SmsCommandTpduImpl(boolean statusReportRequest, int messageReference, ProtocolIdentifier protocolIdentifier,
            CommandType commandType, int messageNumber, AddressField destinationAddress, CommandData commandData) {
        this();

        this.statusReportRequest = statusReportRequest;
        this.messageReference = messageReference;
        this.protocolIdentifier = protocolIdentifier;
        this.commandType = commandType;
        this.messageNumber = messageNumber;
        this.destinationAddress = destinationAddress;
        this.commandData = commandData;
    }

    public SmsCommandTpduImpl(byte[] data) throws MAPException {
        this();

        if (data == null)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: data is empty");
        if (data.length < 1)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: data length is equal zero");

        ByteArrayInputStream stm = new ByteArrayInputStream(data);

        int bt = stm.read();
        if ((bt & _MASK_TP_UDHI) != 0)
            this.userDataHeaderIndicator = true;
        if ((bt & _MASK_TP_SRR) != 0)
            this.statusReportRequest = true;

        this.messageReference = stm.read();
        if (this.messageReference == -1)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: messageReference field has not been found");

        bt = stm.read();
        if (bt == -1)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: protocolIdentifier field has not been found");
        this.protocolIdentifier = new ProtocolIdentifierImpl(bt);

        bt = stm.read();
        if (bt == -1)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: commandType field has not been found");
        this.commandType = new CommandTypeImpl(bt);

        this.messageNumber = stm.read();
        if (this.messageNumber == -1)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: messageNumber field has not been found");

        this.destinationAddress = AddressFieldImpl.createMessage(stm);

        this.commandDataLength = stm.read();
        if (this.commandDataLength == -1)
            throw new MAPException("Error creating a new SmsCommandTpdu instance: commandDataLength field has not been found");

        int avail = this.commandDataLength;
        byte[] buf = new byte[avail];
        try {
            stm.read(buf);
        } catch (IOException e) {
            throw new MAPException("IOException while creating a new SmsCommandTpdu instance: " + e.getMessage(), e);
        }
        commandData = new CommandDataImpl(buf);
    }

    public boolean getUserDataHeaderIndicator() {
        return this.userDataHeaderIndicator;
    }

    public boolean getStatusReportRequest() {
        return this.statusReportRequest;
    }

    public int getMessageReference() {
        return messageReference;
    }

    public ProtocolIdentifier getProtocolIdentifier() {
        return protocolIdentifier;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public AddressField getDestinationAddress() {
        return destinationAddress;
    }

    public int getCommandDataLength() {
        return commandDataLength;
    }

    public CommandData getCommandData() {
        return commandData;
    }

    public byte[] encodeData() throws MAPException {

        if (this.protocolIdentifier == null || this.commandData == null || commandType == null || destinationAddress == null)
            throw new MAPException(
                    "Error encoding a SmsCommandTpdu: protocolIdentifier, messageNumber, destinationAddress and commandData must not be null");

        AsnOutputStream res = new AsnOutputStream();

        // byte 0
        res.write(SmsTpduType.SMS_COMMAND.getEncodedValue() | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0)
                | (this.statusReportRequest ? _MASK_TP_SRR : 0));

        this.commandData.encode();
        this.commandDataLength = this.commandData.getEncodedData().length;

        if (this.commandDataLength > _CommandDataLimit)
            throw new MAPException("Command data field length may not increase " + _CommandDataLimit);

        res.write(this.messageReference);
        res.write(this.protocolIdentifier.getCode());
        res.write(this.commandType.getCode());
        res.write(this.messageNumber);
        this.destinationAddress.encodeData(res);

        res.write(this.commandDataLength);
        res.write(this.commandData.getEncodedData());

        return res.toByteArray();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SMS-COMMAND tpdu [");

        boolean started = false;
        if (this.userDataHeaderIndicator) {
            sb.append("userDataHeaderIndicator");
            started = true;
        }
        if (this.statusReportRequest) {
            if (started)
                sb.append(", ");
            sb.append("statusReportRequest");
            started = true;
        }

        if (started)
            sb.append(", ");
        sb.append("messageReference=");
        sb.append(this.messageReference);
        if (this.protocolIdentifier != null) {
            sb.append(", ");
            sb.append(this.protocolIdentifier.toString());
        }
        if (this.commandType != null) {
            sb.append(", ");
            sb.append(this.commandType.toString());
        }
        sb.append(", messageNumber=");
        sb.append(this.messageNumber);
        if (this.destinationAddress != null) {
            sb.append(", destinationAddress [");
            sb.append(this.destinationAddress.toString());
            sb.append("]");
        }
        if (this.commandData != null) {
            sb.append("\nCOMMAND [");
            sb.append(this.commandData.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
