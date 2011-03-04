/**
 * Start time:18:44:56 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumUsed;

/**
 * Start time:18:44:56 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class TransmissionMediumUsedImpl extends AbstractISUPParameter implements TransmissionMediumUsed {

	public TransmissionMediumUsedImpl(int transimissionMediumUsed) {
		super();
		this.transimissionMediumUsed = transimissionMediumUsed;
	}

	public TransmissionMediumUsedImpl() {
		super();
		
	}

	public TransmissionMediumUsedImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	// Defualt indicate speech
	private int transimissionMediumUsed;

	// FIXME: again wrapper class but hell there is a lot of statics....

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 1) {
			throw new ParameterException("byte[] must  not be null and length must  be 1");
		}

		this.transimissionMediumUsed = b[0];

		return 1;
	}

	public byte[] encode() throws ParameterException {
		return new byte[] { (byte) this.transimissionMediumUsed };
	}

	public int getTransimissionMediumUsed() {
		return transimissionMediumUsed;
	}

	public void setTransimissionMediumUsed(int transimissionMediumUsed) {
		this.transimissionMediumUsed = transimissionMediumUsed;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
