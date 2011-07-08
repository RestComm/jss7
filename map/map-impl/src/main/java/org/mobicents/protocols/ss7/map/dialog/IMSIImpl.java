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

package org.mobicents.protocols.ss7.map.dialog;

import java.io.IOException;
import java.text.DecimalFormat;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.dialog.AddressNature;
import org.mobicents.protocols.ss7.map.api.dialog.IMSI;
import org.mobicents.protocols.ss7.map.api.dialog.NumberingPlan;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class IMSIImpl extends TbcdString implements IMSI {

	private Long MCC;
	private Long MNC;
	private String MSIN;

	@Override
	public Long getMCC() {
		return this.MCC;
	}

	public void setMCC(Long MCC) {
		this.MCC = MCC;
	}

	@Override
	public Long getMNC() {
		return this.MNC;
	}

	public void setMNC(Long MNC) {
		this.MNC = MNC;
	}

	@Override
	public String getMSIN() {
		return this.MSIN;
	}
	
	public void setMSIN(String MSIN) {
		this.MSIN = MSIN;
	}

	
	public void decode(AsnInputStream ansIS) throws MAPException, IOException {

		int digCnt = ansIS.available();
		if (digCnt < 3 || digCnt > 8)
			throw new MAPException("Error decoding IMSI: the IMSI field must contain from 3 to 8 octets. Contains: " + digCnt);

		String res = this.decodeString(ansIS);
		
		String sMcc = res.substring(0, 3); 
		String sMnc = res.substring(3, 5); 
		this.MSIN = res.substring(5); 

		this.MCC = (long)Integer.parseInt(sMcc);
		this.MNC = (long)Integer.parseInt(sMnc);
	}

	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.MCC == null || this.MNC == null || this.MSIN == null)
			throw new MAPException("Error while encoding the IMSI: MMC, MNC or MSIN is not defined");

		if (this.MCC < 0 || this.MCC > 999)
			throw new MAPException("Error while encoding the IMSI: Bad MCC value");
		if (this.MNC < 0 || this.MNC > 99)
			throw new MAPException("Error while encoding the IMSI: Bad MNC value");
		if (this.MSIN.length() < 1 || this.MSIN.length() > 11)
			throw new MAPException("Error while encoding the IMSI: Bad MSIN value");

		StringBuilder sb = new StringBuilder();
		if (this.MCC < 100)
			sb.append("0");
		if (this.MCC < 10)
			sb.append("0");
		sb.append(this.MCC);
		if (this.MNC < 10)
			sb.append("0");
		sb.append(this.MNC);
		sb.append(this.MSIN);

		this.encodeString(asnOs, sb.toString());
	}

	@Override
	public String toString() {
		return "IMSI [MCC=" + this.MCC + ", MNC=" + this.MNC + ", MSIN=" + this.MSIN + "]";
	}
}
