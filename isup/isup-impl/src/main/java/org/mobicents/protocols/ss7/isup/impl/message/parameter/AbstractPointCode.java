/**
 * Start time:12:23:47 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.AbstractPointCodeInterface;

/**
 * Start time:12:23:47 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public abstract class AbstractPointCode extends AbstractParameter implements AbstractPointCodeInterface{

	
	
	protected int signalingPointCode;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 2) {
			throw new ParameterRangeInvalidException("byte[] must  not be null and length must  be 1");
		}

		this.signalingPointCode = b[0];
		// FIXME: should we kill spare bits ?
		this.signalingPointCode |= b[1] << 8;
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {

		byte[] b = new byte[2];
		b[0] = (byte) this.signalingPointCode;
		b[1] = (byte) (this.signalingPointCode >> 8);

		return b;
	}

	public AbstractPointCode() {
		super();
		
	}
	public AbstractPointCode(byte[] b) throws ParameterRangeInvalidException{
		super();
		decodeElement(b);
	}

	public int getSignalingPointCode() {
		return signalingPointCode;
	}

	public void setSignalingPointCode(int signalingPointCode) {
		this.signalingPointCode = signalingPointCode;
	}
	
}
