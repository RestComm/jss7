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
import org.mobicents.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsSubmitTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityEnhancedFormatData;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityPeriod;
import org.mobicents.protocols.ss7.map.api.smstpdu.ValidityPeriodFormat;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SmsSubmitTpduImpl extends SmsTpduImpl implements SmsSubmitTpdu {

	private boolean rejectDuplicates;
	private ValidityPeriodFormat validityPeriodFormat;
	private boolean replyPathExists;
	private boolean userDataHeaderIndicator;
	private boolean statusReportRequest;
	private int messageReference;
	private AddressFieldImpl destinationAddress;
	private ProtocolIdentifierImpl protocolIdentifier;
	private DataCodingSchemeImpl dataCodingScheme;
	private ValidityPeriod validityPeriod;
	private int userDataLength;
	private UserDataImpl smsUserData;

	private SmsSubmitTpduImpl() {
		this.tpduType = SmsTpduType.SMS_SUBMIT;
		this.mobileOriginatedMessage = true;
	}

	public SmsSubmitTpduImpl(boolean rejectDuplicates, ValidityPeriodFormat validityPeriodFormat, boolean replyPathExists, boolean userDataHeaderIndicator,
			boolean statusReportRequest) {
		this();

		this.rejectDuplicates = rejectDuplicates;
		this.validityPeriodFormat = validityPeriodFormat;
		this.replyPathExists = replyPathExists;
		this.userDataHeaderIndicator = userDataHeaderIndicator;
		this.statusReportRequest = statusReportRequest;
	}

	public SmsSubmitTpduImpl(byte[] data, Charset gsm8Charset) throws MAPException {
		this();

		if (data == null)
			throw new MAPException("Error creating a new SmsSubmitTpdu instance: data is empty");
		if (data.length < 1)
			throw new MAPException("Error creating a new SmsSubmitTpdu instance: data length is equal zero");

		ByteArrayInputStream stm = new ByteArrayInputStream(data);

		int bt = stm.read();
		if ((bt & _MASK_TP_RD) != 0)
			this.rejectDuplicates = true;
		int code = (bt & _MASK_TP_VPF) >> 3;
		this.validityPeriodFormat = ValidityPeriodFormat.getInstance(code);
		if ((bt & _MASK_TP_RP) != 0)
			this.replyPathExists = true;
		if ((bt & _MASK_TP_UDHI) != 0)
			this.userDataHeaderIndicator = true;
		if ((bt & _MASK_TP_SRR) != 0)
			this.statusReportRequest = true;

		this.messageReference = stm.read();
		if (this.messageReference == -1)
			throw new MAPException("Error creating a new SmsSubmitTpdu instance: messageReference field has not been found");

		this.destinationAddress = AddressFieldImpl.createMessage(stm);

		bt = stm.read();
		if (bt == -1)
			throw new MAPException("Error creating a new SmsSubmitTpdu instance: protocolIdentifier field has not been found");
		this.protocolIdentifier = new ProtocolIdentifierImpl(bt);

		bt = stm.read();
		if (bt == -1)
			throw new MAPException("Error creating a new SmsSubmitTpdu instance: dataCodingScheme field has not been found");
		this.dataCodingScheme = new DataCodingSchemeImpl(bt);
		
		switch (this.validityPeriodFormat) {
		case fieldPresentRelativeFormat:
			bt = stm.read();
			if (bt == -1)
				throw new MAPException("Error creating a new SmsSubmitTpdu instance: validityPeriodFormat-fieldPresentEnhancedFormat field has not been found");
			this.validityPeriod = new ValidityPeriodImpl(bt);
			break;
		case fieldPresentAbsoluteFormat:
			AbsoluteTimeStamp ats = AbsoluteTimeStampImpl.createMessage(stm);
			this.validityPeriod = new ValidityPeriodImpl(ats);
			break;
		case fieldPresentEnhancedFormat:
			byte[] buf = new byte[7];
			try {
				stm.read(buf);
			} catch (IOException e) {
				throw new MAPException("IOException while creating a new SmsSubmitTpdu instance - decoding validityPeriodFormat-fieldPresentAbsoluteFormat: "
						+ e.getMessage(), e);
			}
			ValidityEnhancedFormatData vef = new ValidityEnhancedFormatDataImpl(buf);
			this.validityPeriod = new ValidityPeriodImpl(vef);
			break;
		}

		this.userDataLength = stm.read();
		if (this.userDataLength == -1)
			throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: userDataLength field has not been found");

		int avail = stm.available();
		byte[] buf = new byte[avail];
		try {
			stm.read(buf);
		} catch (IOException e) {
			throw new MAPException("IOException while creating a new SmsDeliverTpdu instance: " + e.getMessage(), e);
		}
		smsUserData = new UserDataImpl(buf, dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
	}

	@Override
	public boolean getRejectDuplicates() {
		return this.rejectDuplicates;
	}

	@Override
	public ValidityPeriodFormat getValidityPeriodFormat() {
		return this.validityPeriodFormat;
	}

	@Override
	public boolean getReplyPathExists() {
		return this.replyPathExists;
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
	public AddressField getDestinationAddress() {
		return destinationAddress;
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
	public ValidityPeriod getValidityPeriod() {
		return validityPeriod;
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

		if (validityPeriodFormat == null)
			throw new MAPException("Error encoding a SmsSubmitTpdu: validityPeriodFormat is null");

		AsnOutputStream res = new AsnOutputStream();

		// byte 0
		res.write(SmsTpduType.SMS_SUBMIT.getCode() | (this.rejectDuplicates ? _MASK_TP_RD : 0) | (this.validityPeriodFormat.getCode() << 3)
				| (this.replyPathExists ? _MASK_TP_RP : 0) | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0) | (this.statusReportRequest ? _MASK_TP_SRR : 0));

		// TODO: implement encoding

		return res.toByteArray();
	}
}
