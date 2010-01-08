/**
 * Start time:13:42:38 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.ConferenceTreatmentIndicators;

/**
 * Start time:13:42:38 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class ConferenceTreatmentIndicatorsImpl extends AbstractParameter implements ConferenceTreatmentIndicators{

	

	

	private byte[] conferenceAcceptance = null;

	public ConferenceTreatmentIndicatorsImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public ConferenceTreatmentIndicatorsImpl() {
		super();
		// TODO Auto-generated constructor stub
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
		setConferenceAcceptance(b);
		return b.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {

		for (int index = 0; index < this.conferenceAcceptance.length; index++) {
			this.conferenceAcceptance[index] = (byte) (this.conferenceAcceptance[index] & 0x03);
		}

		this.conferenceAcceptance[this.conferenceAcceptance.length - 1] = (byte) ((this.conferenceAcceptance[this.conferenceAcceptance.length - 1]) | (0x01 << 7));
		return this.conferenceAcceptance;
	}

	public byte[] getConferenceAcceptance() {
		return conferenceAcceptance;
	}

	public void setConferenceAcceptance(byte[] conferenceAcceptance) {
		if (conferenceAcceptance == null || conferenceAcceptance.length == 0) {
			throw new IllegalArgumentException("byte[] must not be null and length must be greater than 0");
		}

		this.conferenceAcceptance = conferenceAcceptance;
	}

	public int getConferenceTreatmentIndicator(byte b) {
		return b & 0x03;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
