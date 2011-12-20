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
import org.mobicents.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.mobicents.protocols.ss7.map.api.smstpdu.FailureCause;
import org.mobicents.protocols.ss7.map.api.smstpdu.ParameterIndicator;
import org.mobicents.protocols.ss7.map.api.smstpdu.ProtocolIdentifier;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsSubmitReportTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpduType;
import org.mobicents.protocols.ss7.map.api.smstpdu.UserData;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SmsSubmitReportTpduImpl extends SmsTpduImpl implements SmsSubmitReportTpdu {

	private boolean userDataHeaderIndicator;
	private FailureCause failureCause;
	private ParameterIndicator parameterIndicator;
	private AbsoluteTimeStampImpl serviceCentreTimeStamp;
	private ProtocolIdentifierImpl protocolIdentifier;
	private DataCodingSchemeImpl dataCodingScheme;
	private int userDataLength;
	private UserDataImpl smsUserData;

	private SmsSubmitReportTpduImpl() {
		this.tpduType = SmsTpduType.SMS_SUBMIT_REPORT;
		this.mobileOriginatedMessage = false;
	}

	public SmsSubmitReportTpduImpl(boolean userDataHeaderIndicator) {
		this();
		
		this.userDataHeaderIndicator = userDataHeaderIndicator;
	}

	public SmsSubmitReportTpduImpl(byte[] data, Charset gsm8Charset) throws MAPException {
		this();

		if (data == null)
			throw new MAPException("Error creating a new SmsSubmitReportTpdu instance: data is empty");
		if (data.length < 1)
			throw new MAPException("Error creating a new SmsSubmitReportTpdu instance: data length is equal zero");

		ByteArrayInputStream stm = new ByteArrayInputStream(data);

		int bt = stm.read();
		if ((bt & _MASK_TP_UDHI) != 0)
			this.userDataHeaderIndicator = true;

		bt = stm.read();
		if (bt == -1)
			throw new MAPException("Error creating a new SmsDeliverReportTpdu instance: Failure-Cause and Parameter-Indicator fields have not been found");
		if ((bt & 0x80) != 0) {
			// Failure-Cause exists
			this.failureCause = new FailureCauseImpl(bt);
			
			bt = stm.read();
			if (bt == -1)
				throw new MAPException("Error creating a new SmsDeliverReportTpdu instance: Parameter-Indicator field has not been found");
		}
		
		this.parameterIndicator = new ParameterIndicatorImpl(bt);

		this.serviceCentreTimeStamp = AbsoluteTimeStampImpl.createMessage(stm);

		if (this.parameterIndicator.getTP_PIDPresence()) {
			bt = stm.read();
			if (bt == -1)
				throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: protocolIdentifier field has not been found");
			this.protocolIdentifier = new ProtocolIdentifierImpl(bt);
		}

		if (this.parameterIndicator.getTP_DCSPresence()) {
			bt = stm.read();
			if (bt == -1)
				throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: dataCodingScheme field has not been found");
			this.dataCodingScheme = new DataCodingSchemeImpl(bt);
		}

		if (this.parameterIndicator.getTP_UDLPresence()) {
			this.userDataLength = stm.read();
			if (this.userDataLength == -1)
				throw new MAPException("Error creating a new SmsDeliverTpduImpl instance: userDataLength field has not been found");

			int avail = stm.available();
			byte[] buf = new byte[avail];
			try {
				stm.read(buf);
			} catch (IOException e) {
				throw new MAPException("IOException while creating a new SmsDeliverTpduImpl instance: " + e.getMessage(), e);
			}
			smsUserData = new UserDataImpl(buf, dataCodingScheme, userDataLength, userDataHeaderIndicator, gsm8Charset);
		}
	}

	@Override
	public boolean getUserDataHeaderIndicator() {
		return this.userDataHeaderIndicator;
	}

	@Override
	public FailureCause getFailureCause() {
		return failureCause;
	}

	@Override
	public ParameterIndicator getParameterIndicator() {
		return parameterIndicator;
	}

	@Override
	public AbsoluteTimeStamp getServiceCentreTimeStamp() {
		return serviceCentreTimeStamp;
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

		AsnOutputStream res = new AsnOutputStream();

		// byte 0
		res.write(SmsTpduType.SMS_COMMAND.getCode() | (this.userDataHeaderIndicator ? _MASK_TP_UDHI : 0));

		// TODO: implement encoding

		return res.toByteArray();
	}
}
