/**
 * Start time:14:58:14 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.HopCounter;

/**
 * Start time:14:58:14 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class HopCounterImpl extends AbstractParameter implements HopCounter{


	private int hopCounter;

	public HopCounterImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public HopCounterImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HopCounterImpl(int hopCounter) {
		super();
		this.hopCounter = hopCounter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 1) {
			throw new ParameterRangeInvalidException("byte[] must not be null and length must be 1");
		}
		this.hopCounter = b[0] & 0x1F;
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		return new byte[] { (byte) (this.hopCounter & 0x1F) };
	}

	public int getHopCounter() {
		return hopCounter;
	}

	public void setHopCounter(int hopCounter) {
		this.hopCounter = hopCounter;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
