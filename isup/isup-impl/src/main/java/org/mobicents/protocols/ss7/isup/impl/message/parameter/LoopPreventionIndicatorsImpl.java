/**
 * Start time:11:31:36 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.LoopPreventionIndicators;

/**
 * Start time:11:31:36 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class LoopPreventionIndicatorsImpl extends AbstractISUPParameter implements LoopPreventionIndicators{


	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	
	private boolean response ;
	private int responseIndicator;

	public LoopPreventionIndicatorsImpl() {
		super();
		
	}
	
	public LoopPreventionIndicatorsImpl(boolean response, int responseIndicator) {
		super();
		this.response = response;
		this.responseIndicator = responseIndicator;
	}

	public LoopPreventionIndicatorsImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 1) {
			throw new ParameterException("byte[] must  not be null and length must  be 1");
		}

		this.response = (b[0] & 0x01) == _TURN_ON;

		if (response) {
			this.responseIndicator = (b[0] >> 1) & 0x03;
		}
		return 1;
	}

	public byte[] encode() throws ParameterException {
		int v = this.response ? _TURN_ON : _TURN_OFF;
		if (this.response) {
			v |= (this.responseIndicator & 0x03) << 1;
		}
		return new byte[] { (byte) v };
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}

	public int getResponseIndicator() {
		return responseIndicator;
	}

	public void setResponseIndicator(int responseIndicator) {
		this.responseIndicator = responseIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
