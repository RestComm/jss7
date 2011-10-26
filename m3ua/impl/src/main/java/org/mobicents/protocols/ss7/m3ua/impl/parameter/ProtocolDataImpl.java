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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;

/**
 * Implements Protocol Data parameter.
 * 
 * @author amit bhayani
 * @author kulikov
 */
public class ProtocolDataImpl extends ParameterImpl implements ProtocolData {

	private int opc;
	private int dpc;
	private int si;
	private int ni;
	private int mp;
	private int sls;
	private byte[] data;

	protected ProtocolDataImpl() {
		this.tag = ParameterImpl.Protocol_Data;
	}

	protected ProtocolDataImpl(Mtp3TransferPrimitive mtp3TransferPrimitive) {
		this();
		this.opc = mtp3TransferPrimitive.getOpc();
		this.dpc = mtp3TransferPrimitive.getDpc();
		this.si = mtp3TransferPrimitive.getSi();
		this.ni = mtp3TransferPrimitive.getNi();
		this.mp = mtp3TransferPrimitive.getMp();
		this.sls = mtp3TransferPrimitive.getSls();
		this.data = mtp3TransferPrimitive.getData();
	}

	protected ProtocolDataImpl(int opc, int dpc, int si, int ni, int mp, int sls, byte[] data) {
		this();
		this.opc = opc;
		this.dpc = dpc;
		this.si = si;
		this.ni = ni;
		this.mp = mp;
		this.sls = sls;
		this.data = data;
		encode();
	}

	/**
	 * Creates new parameter with specified value.
	 * 
	 * @param valueData
	 *            the value of this parameter
	 */
	protected ProtocolDataImpl(byte[] valueData) {
		this();

		this.opc = ((valueData[0] & 0xff) << 24) | ((valueData[1] & 0xff) << 16) | ((valueData[2] & 0xff) << 8)
				| (valueData[3] & 0xff);
		this.dpc = ((valueData[4] & 0xff) << 24) | ((valueData[5] & 0xff) << 16) | ((valueData[6] & 0xff) << 8)
				| (valueData[7] & 0xff);

		this.si = valueData[8] & 0xff;
		this.ni = valueData[9] & 0xff;
		this.mp = valueData[10] & 0xff;
		this.sls = valueData[11] & 0xff;

		this.data = new byte[valueData.length - 12];
		System.arraycopy(valueData, 12, data, 0, valueData.length - 12);
	}

	private byte[] encode() {
		// create byte array taking into account data, point codes and
		// indicators;
		byte[] value = new byte[data.length + 12];
		// insert data
		System.arraycopy(data, 0, value, 12, data.length);

		// encode originated point codes
		value[0] = (byte) (opc >> 24);
		value[1] = (byte) (opc >> 16);
		value[2] = (byte) (opc >> 8);
		value[3] = (byte) (opc);

		// encode destination point code
		value[4] = (byte) (dpc >> 24);
		value[5] = (byte) (dpc >> 16);
		value[6] = (byte) (dpc >> 8);
		value[7] = (byte) (dpc);

		// encode indicators
		value[8] = (byte) (si);
		value[9] = (byte) (ni);
		value[10] = (byte) (mp);
		value[11] = (byte) (sls);

		return value;
	}

	public int getOpc() {
		return opc;
	}

	public int getDpc() {
		return dpc;
	}

	public int getSI() {
		return si;
	}

	public int getNI() {
		return ni;
	}

	public int getMP() {
		return mp;
	}

	public int getSLS() {
		return sls;
	}

	public byte[] getData() {
		return data;
	}

	@Override
	protected byte[] getValue() {
		return this.encode();
	}

	@Override
	public String toString() {
		return String.format("Protocol opc=%d dpc=%d si=%d ni=%d sls=%d", opc, dpc, si, ni, sls);
	}

}
