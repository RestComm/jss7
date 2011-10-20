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

package org.mobicents.protocols.ss7.mtp;

/**
 * @author sergey vetyutnev
 * 
 */
public class Mtp3TransferPrimitive {

	protected int si; // service indicator
	protected int ni; // network indicator
	protected int mp; // message priority
	protected int opc;
	protected int dpc;
	protected int sls;
	protected byte[] data;

	public Mtp3TransferPrimitive() {
	}

	public Mtp3TransferPrimitive(int si, int ni, int mp, int opc, int dpc, int sls, byte[] data) {
		this.si = si;
		this.ni = ni;
		this.mp = mp;
		this.opc = opc;
		this.dpc = dpc;
		this.sls = sls;
		this.data = data;
	}
	
	public int getSi() {
		return this.si;
	}

	public int getNi() {
		return this.ni;
	}

	public int getMp() {
		return this.mp;
	}

	public int getOpc() {
		return this.opc;
	}

	public int getDpc() {
		return this.dpc;
	}

	public int getSls() {
		return this.sls;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setSi(int si) {
		this.si = si;
	}

	public void setNi(int ni) {
		this.ni = ni;
	}

	public void setMp(int mp) {
		this.mp = mp;
	}

	public void setOpc( int opc ) {
		this.opc = opc;
	}

	public void setDpc( int dpc ) {
		this.dpc = dpc;
	}

	public void setSls( int sls ) {
		this.sls = sls;
	}

	public void setData(byte[] data) {
		this.data = data;
	}


	public void decodeMtp3(byte[] msg) {

		// sio
		int sio = msg[0];
		this.si = sio & 0x0F;
		int ssi = (sio & 0xF0) >> 4;
		this.ni = ssi >> 2;
		this.mp = ssi & 0x03;

		// routing label
		byte b1 = msg[1];
		byte b2 = msg[2];
		byte b3 = msg[3];
		byte b4 = msg[4];
		this.dpc = ((b2 & 0x3f) << 8) | (b1 & 0xff);
		this.opc = ((b4 & 0x0f) << 10) | ((b3 & 0xff) << 2) | ((b2 & 0xc0) >> 6);
		this.sls = ((b4 & 0xf0) >> 4);

		// msu data
		this.data = new byte[msg.length - 5];
		System.arraycopy(msg, 5, this.data, 0, this.data.length);
	}

	public byte[] encodeMtp3() {

		byte[] res = new byte[this.data.length + 5];

		// sio
		int ssi = (this.ni & 0x03) << 2 | (this.mp & 0x03);
		res[0] = (byte) (((ssi & 0x0F) << 4) | (this.si & 0x0F));
		
		// routing label
		res[1] = (byte) dpc;
		res[2] = (byte) (((dpc >> 8) & 0x3F) | ((this.opc & 0x03) << 6));
		res[3] = (byte) (this.opc >> 2);
		res[4] = (byte) (((this.opc >> 10) & 0x0F) | ((sls & 0x0F) << 4));

		// msu data
		System.arraycopy(this.data, 0, res, 5, this.data.length);

		return res;
	}
	
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("MTP-TRANSFER: OPC=");
		sb.append(this.opc);
		sb.append(", DPC=");
		sb.append(this.dpc);
		sb.append(", SLS=");
		sb.append(this.sls);

		if (this.data != null) {
			sb.append(", MsgLen=");
			sb.append(this.data.length);
		}
		
		sb.append(", NI=");
		switch (this.ni) {
		case 0:
			sb.append("National");
			break;
		case 1:
			sb.append("NationalSpare");
			break;
		case 2:
			sb.append("International");
			break;
		case 3:
			sb.append("InternationalSpare");
			break;
		default:
			sb.append(this.ni);
			break;
		}

		sb.append(", SI=");
		switch (this.si) {
		case 0:
			sb.append("SNMM");
			break;
		case 1:
			sb.append("SNTMM");
			break;
		case 2:
			sb.append("SNTMM Special");
			break;
		case 3:
			sb.append("SCCP");
			break;
		case 4:
			sb.append("TUP");
			break;
		case 5:
			sb.append("ISDN");
			break;
		case 6:
			sb.append("DUP-1");
			break;
		case 7:
			sb.append("DUP-1");
			break;
		case 8:
			sb.append("MTP Testing");
			break;
		case 9:
			sb.append("Broadband ISDN");
			break;
		case 10:
			sb.append("Satellite ISDN");
			break;
		default:
			sb.append(this.si);
			break;
		}

		sb.append(", MP=");
		sb.append(this.mp);

		return sb.toString();
	}
}


