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

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumUsed;

/**
 * Start time:18:44:56 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class TransmissionMediumUsedImpl extends AbstractParameter implements TransmissionMediumUsed {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */

	public TransmissionMediumUsedImpl(int transimissionMediumUsed) {
		super();
		this.transimissionMediumUsed = transimissionMediumUsed;
	}

	public TransmissionMediumUsedImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TransmissionMediumUsedImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	// Defualt indicate speech
	private int transimissionMediumUsed;

	// FIXME: again wrapper class but hell there is a lot of statics....
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 1) {
			throw new ParameterRangeInvalidException("byte[] must  not be null and length must  be 1");
		}

		this.transimissionMediumUsed = b[0];

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
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
