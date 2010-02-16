/**
 * Start time:12:50:23 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionTreatmentIndicators;

/**
 * Start time:12:50:23 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class CallDiversionTreatmentIndicatorsImpl extends AbstractParameter implements CallDiversionTreatmentIndicators{
	
	

	private byte[] callDivertedIndicators = null;

	
	
	public CallDiversionTreatmentIndicatorsImpl() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CallDiversionTreatmentIndicatorsImpl(byte[] b) throws ParameterRangeInvalidException{
		super();
		decodeElement(b);
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
		this.callDivertedIndicators = b;
		return b.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		for (int index = 0; index < this.callDivertedIndicators.length; index++) {
			this.callDivertedIndicators[index] = (byte) (this.callDivertedIndicators[index] & 0x03);
		}

		this.callDivertedIndicators[this.callDivertedIndicators.length - 1] = (byte) ((this.callDivertedIndicators[this.callDivertedIndicators.length - 1]) | (0x01 << 7));
		return this.callDivertedIndicators;
	}

	public byte[] getCallDivertedIndicators() {
		return callDivertedIndicators;
	}

	public void setCallDivertedIndicators(byte[] callDivertedIndicators) {
		this.callDivertedIndicators = callDivertedIndicators;
	}

	public int getDiversionIndicator(byte b) {
		return b & 0x03;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
