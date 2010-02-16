/**
 * Start time:15:02:53 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.CollectCallRequest;

/**
 * Start time:15:02:53 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CollectCallRequestImpl extends AbstractParameter implements CollectCallRequest {

	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	private boolean collectCallRequested = false;

	public CollectCallRequestImpl(boolean collectCallRequested) {
		super();
		this.collectCallRequested = collectCallRequested;
	}

	public CollectCallRequestImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CollectCallRequestImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
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
		this.collectCallRequested = (b[0] & 0x01) == _TURN_ON;

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		return new byte[] { (byte) (this.collectCallRequested ? _TURN_ON : _TURN_OFF) };
	}

	public boolean isCollectCallRequested() {
		return collectCallRequested;
	}

	public void setCollectCallRequested(boolean collectCallRequested) {
		this.collectCallRequested = collectCallRequested;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
