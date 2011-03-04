/**
 * Start time:14:20:15 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.PropagationDelayCounter;

/**
 * Start time:14:20:15 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class PropagationDelayCounterImpl extends AbstractISUPParameter implements PropagationDelayCounter {

	private int propagationDelay;

	public PropagationDelayCounterImpl() {
		super();
		
	}

	public PropagationDelayCounterImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public PropagationDelayCounterImpl(int propagationDelay) {
		super();
		this.propagationDelay = propagationDelay;
	}

	public int decode(byte[] b) throws ParameterException {
		// This one is other way around, as Eduardo might say.
		if (b == null || b.length != 2) {
			throw new ParameterException("byte[] must  not be null and length must be 2");
		}

		this.propagationDelay = b[0] << 8;
		this.propagationDelay |= b[1];
		return b.length;
	}

	public byte[] encode() throws ParameterException {

		byte b0 = (byte) (this.propagationDelay >> 8);
		byte b1 = (byte) this.propagationDelay;
		return new byte[] { b0, b1 };
	}

	public int getPropagationDelay() {
		return propagationDelay;
	}

	public void setPropagationDelay(int propagationDelay) {
		this.propagationDelay = propagationDelay;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
