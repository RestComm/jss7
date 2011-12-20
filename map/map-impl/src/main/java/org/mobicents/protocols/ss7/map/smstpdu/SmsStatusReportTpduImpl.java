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
import java.nio.charset.Charset;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.smstpdu.AbsoluteTimeStamp;
import org.mobicents.protocols.ss7.map.api.smstpdu.AddressField;
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.mobicents.protocols.ss7.map.api.smstpdu.ParameterIndicator;
import org.mobicents.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsStatusReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.mobicents.protocols.ss7.map.api.smstpdu.Status;
import org.mobicents.protocols.ss7.map.api.smstpdu.StatusReportQualifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SmsStatusReportTpduImpl extends SmsTpduImpl implements SmsStatusReportTpdu {

	private boolean userDataHeaderIndicator;
	private boolean moreMessagesToSend;
	private StatusReportQualifier statusReportQualifier;
	private int messageReference;
	private AddressField recipientAddress;
	private AbsoluteTimeStampImpl serviceCentreTimeStamp;
	private AbsoluteTimeStamp dischargeTime;
	private Status status;
	private ParameterIndicator parameterIndicator;
	private ProtocolIdentifierImpl protocolIdentifier;
	private DataCodingSchemeImpl dataCodingScheme;
	private int userDataLength;
	private UserDataImpl smsUserData;

	private SmsStatusReportTpduImpl() {
		this.tpduType = SmsTpduType.SMS_STATUS_REPORT;
		this.mobileOriginatedMessage = false;
	}

	public SmsStatusReportTpduImpl(boolean userDataHeaderIndicator, boolean moreMessagesToSend, StatusReportQualifier statusReportQualifier) {
		this();

		this.userDataHeaderIndicator = userDataHeaderIndicator;
		this.moreMessagesToSend = moreMessagesToSend;
		this.statusReportQualifier = statusReportQualifier;
	}
	
	public SmsStatusReportTpduImpl(byte[] data, Charset gsm8Charset) throws MAPException {
		this();

		if (data == null)
			throw new MAPException("Error creating a new SmsStatusReport instance: data is empty");
		if (data.length < 1)
			throw new MAPException("Error creating a new SmsStatusReport instance: data length is equal zero");

		ByteArrayInputStream stm = new ByteArrayInputStream(data);

		int bt = stm.read();
		if ((bt & _MASK_TP_UDHI) != 0)
			this.userDataHeaderIndicator = true;
		if ((bt & _MASK_TP_MMS) == 0)
			this.moreMessagesToSend = true;
		int code = (bt & _MASK_TP_SRQ) >> 5;
		this.statusReportQualifier = StatusReportQualifier.getInstance(code);

		this.messageReference = stm.read();
		if (this.messageReference == -1)
			throw new MAPException("Error creating a new SmsStatusReport instance: messageReference field has not been found");

		this.recipientAddress = AddressFieldImpl.createMessage(stm);
		this.serviceCentreTimeStamp = AbsoluteTimeStampImpl.createMessage(stm);
		this.dischargeTime = AbsoluteTimeStampImpl.createMessage(stm);

		bt = stm.read();
		if (bt == -1)
			throw new MAPException("Error creating a new SmsStatusReport instance: Status field has not been found");
		this.status = new StatusImpl(bt);

		bt = stm.read();
		if (bt == -1)
			this.parameterIndicator = new ParameterIndicatorImpl(0);
		else
			this.parameterIndicator = new ParameterIndicatorImpl(bt);

		if (this.parameterIndicator.getTP_PIDPresence()) {
			bt = stm.read();
			if (bt == -1)
				throw new MAPException("Error creating a new SmsStatusReport instance: protocolIdentifier field has not been found");
			this.protocolIdentifier = new ProtocolIdentifierImpl(bt);
		}

		if (this.parameterIndicator.getTP_DCSPresence()) {
			bt = stm.read();
			if (bt == -1)
				throw new MAPException("Error creating a new SmsStatusReport instance: dataCodingScheme field has not been found");
			this.dataCodingScheme = new DataCodingSchemeImpl(bt);
		}

		if (this.parameterIndicator.getTP_UDLPresence()) {
			this.userDataLength = stm.read();
			if (this.userDataLength == -1)
				throw new MAPException("Error creating a new SmsStatusReport instance: userDataLength field has not been found");

			int avail = stm.available();
			byte[] buf = new byte[avail];
			try {
				stm.read(buf);
			} catch (IOException e) {
				throw new MAPException("IOException while creating a new SmsStatusReport instance: " + e.getMessage(), e);
			}
			smsUserData = new UserDataImpl(buf, dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
		}
	}

	@Override
	public boolean getUserDataHeaderIndicator() {
		return this.userDataHeaderIndicator;
	}

	@Override
	public boolean getMoreMessagesToSend() {
		return this.moreMessagesToSend;
	}

	@Override
	public StatusReportQualifier getStatusReportQualifier() {
		return this.statusReportQualifier;
	}

	@Override
	public int getMessageReference() {
		return messageReference;
	}

	@Override
	public AddressField getRecipientAddress() {
		return recipientAddress;
	}

	@Override
	public AbsoluteTimeStamp getServiceCentreTimeStamp() {
		return serviceCentreTimeStamp;
	}

	@Override
	public AbsoluteTimeStamp getDischargeTime() {
		return dischargeTime;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public ParameterIndicator getParameterIndicator() {
		return parameterIndicator;
	}

	@Override
	public ProtocolIdentifier getProtocolIdentifier() {
		return protocolIdentifier;
	}

	@Override
	public DataCodingScheme getDataCodingScheme() {
		return dataCodingScheme;
	}

	@Override
	public int getUserDataLength() {
		return userDataLength;
	}

	@Override
	public UserData getUserData() {
		return smsUserData;
	}

	@Override
	public byte[] encodeData() throws MAPException {

		if (statusReportQualifier == null)
			throw new MAPException("Error encoding a SmsStatusReportTpdu: statusReportQualifier is null");

		AsnOutputStream res = new AsnOutputStream();

		// byte 0
		res.write(SmsTpduType.SMS_COMMAND.getCode() | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0) | (!this.moreMessagesToSend ? _MASK_TP_MMS : 0)
				| (this.statusReportQualifier.getCode() << 5));

		// TODO: implement encoding

		return res.toByteArray();
	}
}
