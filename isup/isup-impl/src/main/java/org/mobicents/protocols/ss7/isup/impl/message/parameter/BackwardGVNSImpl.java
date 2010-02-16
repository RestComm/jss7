/**
 * Start time:13:15:04 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardGVNS;

/**
 * Start time:13:15:04 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class BackwardGVNSImpl extends AbstractParameter implements BackwardGVNS {

	private byte[] backwardGVNS = null;

	public BackwardGVNSImpl(byte[] backwardGVNS) throws ParameterRangeInvalidException {
		super();
		decodeElement(backwardGVNS);
	}

	public BackwardGVNSImpl() throws ParameterRangeInvalidException {
		super();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length == 0) {
			throw new ParameterRangeInvalidException("byte[] must  not be null and length must  be greater than 0");
		}
		this.backwardGVNS = b;
		return b.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {

		for (int index = 0; index < this.backwardGVNS.length; index++) {
			this.backwardGVNS[index] = (byte) (this.backwardGVNS[index] & 0x7F);
		}

		this.backwardGVNS[this.backwardGVNS.length - 1] = (byte) (this.backwardGVNS[this.backwardGVNS.length - 1] & (1 << 7));
		return this.backwardGVNS;
	}

	public byte[] getBackwardGVNS() {
		return backwardGVNS;
	}

	public void setBackwardGVNS(byte[] backwardGVNS) {
		if (backwardGVNS == null || backwardGVNS.length == 0) {
			throw new IllegalArgumentException("byte[] must  not be null and length must  be greater than 0");
		}
		this.backwardGVNS = backwardGVNS;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
