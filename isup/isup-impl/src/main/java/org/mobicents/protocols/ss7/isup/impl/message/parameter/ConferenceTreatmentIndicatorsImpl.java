/**
 * Start time:13:42:38 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;

/**
 * Start time:13:42:38 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class ConferenceTreatmentIndicatorsImpl extends AbstractISUPParameter implements ConferenceTreatmentIndicators{

	

	

	private byte[] conferenceAcceptance = null;

	public ConferenceTreatmentIndicatorsImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public ConferenceTreatmentIndicatorsImpl() {
		super();
		
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length == 0) {
			throw new ParameterException("byte[] must not be null and length must be greater than 0");
		}
		setConferenceAcceptance(b);
		return b.length;
	}

	public byte[] encode() throws ParameterException {

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
