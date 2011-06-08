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

package org.mobicents.protocols.ss7.sccp.impl.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mobicents.protocols.ss7.sccp.impl.parameter.AbstractParameter;
import org.mobicents.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressCodec;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SegmentationImpl;
import org.mobicents.protocols.ss7.sccp.message.XUnitData;
import org.mobicents.protocols.ss7.sccp.parameter.HopCounter;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.parameter.Segmentation;

/**
 * See Q.713 4.18
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public class XUnitDataImpl extends SccpMessageImpl implements XUnitData {

	// //////////////////
	// Fixed parts //
	// //////////////////
	/**
	 * See Q.713 3.18
	 */

	private HopCounter hopCounter;
	private byte[] data;
	private SegmentationImpl segmentation;
	private ImportanceImpl importance;

	private SccpAddressCodec addressCodec = new SccpAddressCodec();

	protected XUnitDataImpl() {
		super(MESSAGE_TYPE);
	}

	protected XUnitDataImpl(HopCounter hopCounter, ProtocolClass pClass, SccpAddress calledParty,
			SccpAddress callingParty) {
		super(MESSAGE_TYPE);
		this.hopCounter = hopCounter;
		this.protocolClass = (ProtocolClassImpl) pClass;
		this.calledParty = calledParty;
		this.callingParty = (SccpAddress) callingParty;
	}

	public HopCounter getHopCounter() {
		return hopCounter;
	}

	public void setHopCounter(HopCounter hopCounter) {
		this.hopCounter = hopCounter;
	}

	public Segmentation getSegmentation() {
		return segmentation;
	}

	public void setSegmentation(SegmentationImpl segmentation) {
		this.segmentation = segmentation;
	}

	public Importance getImportance() {
		return (Importance) importance;
	}

	public void setImportance(ImportanceImpl importance) {
		this.importance = importance;
	}

	public void encode(OutputStream out) throws IOException {
		out.write(this.getType());

		out.write(((AbstractParameter) protocolClass).encode());
		if (this.hopCounter == null) {
			throw new IOException("Failed parsing, hop counter is not set.");
		}
		out.write(this.hopCounter.getValue());

		byte[] cdp = addressCodec.encode(calledParty);
		byte[] cnp = addressCodec.encode(callingParty);

		// we have 4 pointers, cdp,cnp,data and optionalm, cdp starts after 4
		// octests than
		int len = 4;
		out.write(len);

		len = (cdp.length + len);
		out.write(len);

		len += (cnp.length);
		out.write(len);
		boolean optionalPresent = false;
		if (segmentation != null || importance != null) {
			len += (data.length);
			out.write(len);
			optionalPresent = true;
		} else {
			// in case there is no optional
			out.write(0);
		}

		out.write((byte) cdp.length);
		out.write(cdp);

		out.write((byte) cnp.length);
		out.write(cnp);

		out.write((byte) data.length);
		out.write(data);

		if (segmentation != null) {
			optionalPresent = true;
			out.write(Segmentation.PARAMETER_CODE);
			byte[] b = segmentation.encode();
			out.write(b.length);
			out.write(b);
		}

		if (importance != null) {
			optionalPresent = true;
			out.write(Importance.PARAMETER_CODE);
			byte[] b = importance.encode();
			out.write(b.length);
			out.write(b);
		}

		if (optionalPresent) {
			out.write(0x00);
		}

	}

	public void decode(InputStream in) throws IOException {

		protocolClass = new ProtocolClassImpl();
		((AbstractParameter) protocolClass).decode(new byte[] { (byte) in.read() });

		this.hopCounter = new HopCounterImpl((byte) in.read());
		if (this.hopCounter.getValue() > HopCounter.COUNT_HIGH || this.hopCounter.getValue() <= HopCounter.COUNT_LOW) {
			throw new IOException("Hop Counter must be between 1 and 15, it is: " + this.hopCounter);
		}

		int pointer = in.read() & 0xff;
		in.mark(in.available());
		if (pointer - 1 != in.skip(pointer - 1)) {
			throw new IOException("Not enough data in buffer");
		}
		int len = in.read() & 0xff;

		byte[] buffer = new byte[len];
		in.read(buffer);

		calledParty = addressCodec.decode(buffer);

		in.reset();

		pointer = in.read() & 0xff;

		in.mark(in.available());

		if (pointer - 1 != in.skip(pointer - 1)) {
			throw new IOException("Not enough data in buffer");
		}
		len = in.read() & 0xff;

		buffer = new byte[len];
		in.read(buffer);

		callingParty = addressCodec.decode(buffer);

		in.reset();
		pointer = in.read() & 0xff;
		in.mark(in.available());
		if (pointer - 1 != in.skip(pointer - 1)) {
			throw new IOException("Not enough data in buffer");
		}
		len = in.read() & 0xff;

		data = new byte[len];
		in.read(data);

		in.reset();
		pointer = in.read() & 0xff;
		in.mark(in.available());

		if (pointer == 0) {
			// we are done
			return;
		}
		if (pointer - 1 != in.skip(pointer - 1)) {
			throw new IOException("Not enough data in buffer");
		}

		// FIXME: detect if there is only EOP present?
		int paramCode = 0;
		// EOP
		while ((paramCode = in.read() & 0xFF) != 0) {
			len = in.read() & 0xff;
			buffer = new byte[len];
			in.read(buffer);
			this.decodeOptional(paramCode, buffer);

			// we should have one octet more here
		}

	}

	private void decodeOptional(int code, byte[] buffer) throws IOException {

		switch (code) {
		case Segmentation.PARAMETER_CODE:
			this.segmentation = new SegmentationImpl();
			this.segmentation.decode(buffer);
			break;
		case Importance.PARAMETER_CODE:
			this.importance = new ImportanceImpl();
			this.importance.decode(buffer);
			break;

		default:
			throw new IOException("Uknown optional parameter code: " + code);
		}
	}

	public void setImportance(Importance p) {
		this.importance = (ImportanceImpl) p;
	}

	public void setSegmentation(Segmentation p) {
		this.segmentation = (SegmentationImpl) p;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("XUDT[").append(super.toString()).append(" DataSize=").append(data.length).append("]");
		return sb.toString();
	}
}
