/**
 * Start time:13:33:13 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.CallOfferingTreatmentIndicators;

/**
 * Start time:13:33:13 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallOfferingTreatmentIndicatorsImpl extends AbstractParameter implements CallOfferingTreatmentIndicators {

	private byte[] callOfferingTreatmentIndicators = null;

	public CallOfferingTreatmentIndicatorsImpl() throws ParameterRangeInvalidException {
		super();

	}

	public CallOfferingTreatmentIndicatorsImpl(byte[] b) throws ParameterRangeInvalidException {
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
			throw new ParameterRangeInvalidException("byte[] must not be null and length must be greater than 0");
		}
		setCallOfferingTreatmentIndicators(b);
		return b.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {

		for (int index = 0; index < this.callOfferingTreatmentIndicators.length; index++) {
			this.callOfferingTreatmentIndicators[index] = (byte) ((this.callOfferingTreatmentIndicators[index] & 0x03) | 0x80);
		}

		this.callOfferingTreatmentIndicators[this.callOfferingTreatmentIndicators.length - 1] = (byte) ((this.callOfferingTreatmentIndicators[this.callOfferingTreatmentIndicators.length - 1]) & 0x7F);
		return this.callOfferingTreatmentIndicators;
	}

	public byte[] getCallOfferingTreatmentIndicators() {
		return callOfferingTreatmentIndicators;
	}

	public void setCallOfferingTreatmentIndicators(byte[] callOfferingTreatmentIndicators) {
		if (callOfferingTreatmentIndicators == null || callOfferingTreatmentIndicators.length == 0) {
			throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
		}

		this.callOfferingTreatmentIndicators = callOfferingTreatmentIndicators;
	}

	public int getCallOfferingIndicator(byte b) {
		return b & 0x03;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}