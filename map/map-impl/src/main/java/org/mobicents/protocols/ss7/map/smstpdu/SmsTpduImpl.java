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

import java.nio.charset.Charset;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpdu;
import org.mobicents.protocols.ss7.map.api.smstpdu.SmsTpduType;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public abstract class SmsTpduImpl implements SmsTpdu {
	
	protected static int _MASK_TP_MTI = 0x03;
	protected static int _MASK_TP_MMS = 0x04;
	protected static int _MASK_TP_RD = 0x04;
	protected static int _MASK_TP_LP = 0x08;
	protected static int _MASK_TP_SRI = 0x20;
	protected static int _MASK_TP_SRR = 0x20;
	protected static int _MASK_TP_SRQ = 0x20;
	protected static int _MASK_TP_UDHI = 0x40;
	protected static int _MASK_TP_RP = 0x80;
	protected static int _MASK_TP_VPF = 0x18;

	protected boolean mobileOriginatedMessage;
	protected SmsTpduType tpduType;

	protected SmsTpduImpl() {
	}

	public static SmsTpduImpl createInstance(byte[] data, boolean mobileOriginatedMessage, Charset gsm8Charset) throws MAPException {
		
		if (data == null)
			throw new MAPException("Error creating a new SmsTpduImpl instance: data is empty");
		if (data.length < 1)
			throw new MAPException("Error creating a new SmsTpduImpl instance: data length is equal zero");
		
		int tpMti = data[0] & _MASK_TP_MTI;
		if (mobileOriginatedMessage) {
			SmsTpduType type = SmsTpduType.getMobileOriginatedInstance(tpMti);
			switch (type) {
			case SMS_DELIVER_REPORT:
				return new SmsDeliverReportTpduImpl(data, gsm8Charset);
			case SMS_SUBMIT:
				return new SmsSubmitTpduImpl(data, gsm8Charset);
			case SMS_COMMAND:
				return new SmsCommandTpduImpl(data);
			}
		} else {
			SmsTpduType type = SmsTpduType.getMobileTerminatedInstance(tpMti);
			switch (type) {
			case SMS_DELIVER:
				return new SmsDeliverTpduImpl(data, gsm8Charset);
			case SMS_SUBMIT_REPORT:
				return new SmsSubmitReportTpduImpl(data, gsm8Charset);
			case SMS_STATUS_REPORT:
				return new SmsStatusReportTpduImpl(data, gsm8Charset);
			}
		}
		
		throw new MAPException("Error creating a new SmsTpduImpl instance: unsupported Sms Tpdu type");
	}

	@Override
	public SmsTpduType getSmsTpduType() {
		return tpduType;
	}

	
//	private String printDataArr(byte[] arr) {
//		StringBuilder sb = new StringBuilder();
//		for (int b : arr) {
//			sb.append(b);
//			sb.append(", ");
//		}
//
//		return sb.toString();
//	}
}
