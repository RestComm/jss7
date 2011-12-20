/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
	private ProtocolIdentifierImpl protocolIdentifier;
	private CommandType commandType;
	private int messageNumber;
	private AddressFieldImpl destinationAddress;
	private int commandDataLength;
	private CommandData commandData;

	private SmsCommandTpduImpl() {
		this.tpduType = SmsTpduType.SMS_COMMAND;
		this.mobileOriginatedMessage = true;
	}

	public SmsCommandTpduImpl(boolean userDataHeaderIndicator, boolean statusReportRequest) {
		this();
		
		this.userDataHeaderIndicator = userDataHeaderIndicator;
		this.statusReportRequest = statusReportRequest;
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

		int avail = stm.available();
		byte[] buf = new byte[avail];
		try {
			stm.read(buf);
		} catch (IOException e) {
			throw new MAPException("IOException while creating a new SmsCommandTpdu instance: " + e.getMessage(), e);
		}
		commandData = new CommandDataImpl(buf);
	}

	@Override
	public boolean getUserDataHeaderIndicator() {
		return this.userDataHeaderIndicator;
	}

	@Override
	public boolean getStatusReportRequest() {
		return this.statusReportRequest;
	}

	@Override
	public int getMessageReference() {
		return messageReference;
	}

	@Override
	public ProtocolIdentifier getProtocolIdentifier() {
		return protocolIdentifier;
	}

	@Override
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public int getMessageNumber() {
		return messageNumber;
	}

	@Override
	public AddressField getDestinationAddress() {
		return destinationAddress;
	}

	@Override
	public int getCommandDataLength() {
		return commandDataLength;
	}

	@Override
	public CommandData getCommandData() {
		return commandData;
	}

	@Override
	public byte[] encodeData() throws MAPException {
		
		AsnOutputStream res = new AsnOutputStream();

		// byte 0
		res.write(SmsTpduType.SMS_COMMAND.getCode() | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0) | (this.statusReportRequest ? _MASK_TP_SRR : 0));

		// TODO: implement encoding

		return res.toByteArray();
	}
}
