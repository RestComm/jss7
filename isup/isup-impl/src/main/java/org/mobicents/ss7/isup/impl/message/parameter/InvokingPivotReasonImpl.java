/**
 * Start time:09:11:07 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.InvokingPivotReason;

/**
 * Start time:09:11:07 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class InvokingPivotReasonImpl extends AbstractParameter implements InvokingPivotReason {

	private byte[] reasons = null;

	public InvokingPivotReasonImpl(byte[] reasons) throws ParameterRangeInvalidException {
		super();
		decodeElement(reasons);
	}

	public InvokingPivotReasonImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		try {
			this.setReasons(b);
		} catch (Exception e) {
			throw new ParameterRangeInvalidException(e);
		}
		return b.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		for (int index = 0; index < this.reasons.length; index++) {
			this.reasons[index] = (byte) (this.reasons[index] & 0x7F);
		}

		this.reasons[this.reasons.length - 1] = (byte) ((this.reasons[this.reasons.length - 1]) | (0x01 << 7));
		return this.reasons;
	}

	public byte[] getReasons() {
		return reasons;
	}

	public void setReasons(byte[] reasons) throws IllegalArgumentException {
		if (reasons == null || reasons.length == 0) {
			throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
		}
		this.reasons = reasons;
	}

	public int getReason(byte b) {
		return b & 0x7F;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
