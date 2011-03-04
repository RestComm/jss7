/**
 * Start time:13:33:13 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallOfferingTreatmentIndicators;

/**
 * Start time:13:33:13 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallOfferingTreatmentIndicatorsImpl extends AbstractISUPParameter implements CallOfferingTreatmentIndicators {

	private byte[] callOfferingTreatmentIndicators = null;

	public CallOfferingTreatmentIndicatorsImpl() {
		super();

	}

	public CallOfferingTreatmentIndicatorsImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length == 0) {
			throw new ParameterException("byte[] must not be null and length must be greater than 0");
		}
		setCallOfferingTreatmentIndicators(b);
		return b.length;
	}

	public byte[] encode() throws ParameterException {

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