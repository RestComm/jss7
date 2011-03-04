/**
 * Start time:13:29:12 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallTransferReference;

/**
 * Start time:13:29:12 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallTransferReferenceImpl extends AbstractISUPParameter implements CallTransferReference {

	private int callTransferReference = 0;

	public CallTransferReferenceImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public CallTransferReferenceImpl() {
		super();
		
	}

	public CallTransferReferenceImpl(int callTransferReference) {
		super();
		this.callTransferReference = callTransferReference;
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 1) {
			throw new ParameterException("byte[] must  not be null and length must  be 1");
		}
		this.callTransferReference = b[0];
		return 1;
	}

	public byte[] encode() throws ParameterException {
		return new byte[] { (byte) this.callTransferReference };
	}

	public int getCallTransferReference() {
		return callTransferReference;
	}

	public void setCallTransferReference(int callTransferReference) {
		this.callTransferReference = callTransferReference;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
