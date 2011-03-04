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

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardGVNS;

/**
 * Start time:13:15:04 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class BackwardGVNSImpl extends AbstractISUPParameter implements BackwardGVNS {

	private byte[] backwardGVNS = null;

	public BackwardGVNSImpl(byte[] backwardGVNS) throws ParameterException {
		super();
		decode(backwardGVNS);
	}

	public BackwardGVNSImpl()  {
		super();

	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length == 0) {
			throw new ParameterException("byte[] must  not be null and length must  be greater than 0");
		}
		this.backwardGVNS = b;
		return b.length;
	}

	public byte[] encode() throws ParameterException {

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
