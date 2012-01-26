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
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReturnCauseImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressCodec;
import org.mobicents.protocols.ss7.sccp.message.UnitDataService;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCause;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * See Q.713 4.18
 * 
 * @author Oleg Kulikov
 * @author baranowb
 */
public class UnitDataServiceImpl extends SccpMessageImpl implements UnitDataService {

	// //////////////////
	// Fixed parts //
	// //////////////////
	/**
	 * See Q.713 3.18
	 */
	// private byte hopCounter = HOP_COUNT_NOT_SET;

	private byte[] data;
	private ReturnCause returnCause;
	private SccpAddressCodec addressCodec;

	protected UnitDataServiceImpl(boolean removeSpc) {
		super(MESSAGE_TYPE, removeSpc);
		this.addressCodec = new SccpAddressCodec(removeSpc);
	}

	protected UnitDataServiceImpl(ReturnCause returnCause, SccpAddress calledParty, SccpAddress callingParty, boolean removeSpc) {
		this(removeSpc);

		this.returnCause = returnCause;
		this.calledParty = calledParty;
		this.callingParty = callingParty;
	}

	public ReturnCause getReturnCause() {
		return this.returnCause;
	}

	public void setReturnCause(ReturnCause rc) {
		this.returnCause = (ReturnCauseImpl) rc;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void encode(OutputStream out) throws IOException {
		out.write(this.getType());

		out.write(((AbstractParameter) this.returnCause).encode());

		byte[] cdp = addressCodec.encode(calledParty);
		byte[] cnp = addressCodec.encode(callingParty);

		int len = 3;
		out.write(len);

		len = (cdp.length + 3);
		out.write(len);

		len += (cnp.length);
		out.write(len);

		out.write((byte) cdp.length);
		out.write(cdp);

		out.write((byte) cnp.length);
		out.write(cnp);

		out.write((byte) data.length);
		out.write(data);

	}

	public void decode(InputStream in) throws IOException {

		this.returnCause = new ReturnCauseImpl();
		((AbstractParameter) this.returnCause).decode(new byte[] { (byte) in.read() });
		int cpaPointer = in.read() & 0xff;
		in.mark(in.available());

		in.skip(cpaPointer - 1);
		int len = in.read() & 0xff;

		byte[] buffer = new byte[len];
		in.read(buffer);

		calledParty = addressCodec.decode(buffer);

		in.reset();
		cpaPointer = in.read() & 0xff;
		in.mark(in.available());

		in.skip(cpaPointer - 1);
		len = in.read() & 0xff;

		buffer = new byte[len];
		in.read(buffer);

		callingParty = addressCodec.decode(buffer);

		in.reset();
		cpaPointer = in.read() & 0xff;

		in.skip(cpaPointer - 1);
		len = in.read() & 0xff;

		data = new byte[len];
		in.read(data);

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("UDTS[").append(super.toString()).append(" DataSize=").append(data.length).append(" ReturnCause=")
				.append(this.returnCause).append("]");
		return sb.toString();
	}
}
