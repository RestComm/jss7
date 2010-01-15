/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.ss7.sccp.impl.parameter;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import org.mobicents.ss7.sccp.parameter.MandatoryFixedParameter;
import org.mobicents.ss7.sccp.parameter.ProtocolClass;

/**
 * 
 * @author Oleg Kulikov
 */
public class ProtocolClassImpl extends MandatoryFixedParameter implements ProtocolClass {

	private int pClass;
	private int msgHandling;

	/** Creates a new instance of UnitDataMandatotyFixedPart */
	public ProtocolClassImpl() {
	}

	public ProtocolClassImpl(int pClass, int msgHandling) {
		this.pClass = pClass;
		this.msgHandling = msgHandling;
	}

	public int getpClass() {
		return pClass;
	}

	public void setpClass(int pClass) {
		this.pClass = pClass;
	}

	public int getMsgHandling() {
		return msgHandling;
	}

	public void setMsgHandling(int msgHandling) {
		this.msgHandling = msgHandling;
	}

	public void decode(InputStream in) throws IOException {
		int b = in.read() & 0xff;

		pClass = b & 0x0f;
		msgHandling = (b & 0xf0) >> 4;
	}

	public void encode(OutputStream out) throws IOException {
		byte b = (byte) (pClass | (msgHandling << 4));
		out.write(b);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + msgHandling;
		result = prime * result + pClass;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (getClass() != obj.getClass())
			return false;
		ProtocolClassImpl other = (ProtocolClassImpl) obj;
		if (msgHandling != other.msgHandling)
			return false;
		if (pClass != other.pClass)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProtocolClass [msgHandling=" + msgHandling + ", pClass=" + pClass + "]";
	}

}
