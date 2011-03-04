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

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CollectCallRequest;

/**
 * Start time:15:02:53 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CollectCallRequestImpl extends AbstractISUPParameter implements CollectCallRequest {

	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	private boolean collectCallRequested = false;

	public CollectCallRequestImpl(boolean collectCallRequested) {
		super();
		this.collectCallRequested = collectCallRequested;
	}

	public CollectCallRequestImpl() {
		super();
		
	}

	public CollectCallRequestImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 1) {
			throw new ParameterException("byte[] must not be null and length must be 1");
		}
		this.collectCallRequested = (b[0] & 0x01) == _TURN_ON;

		return 1;
	}

	public byte[] encode() throws ParameterException {
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
