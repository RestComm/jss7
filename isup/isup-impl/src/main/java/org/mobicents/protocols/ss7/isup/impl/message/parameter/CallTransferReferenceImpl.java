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

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.CallTransferReference;

/**
 * Start time:13:29:12 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallTransferReferenceImpl extends AbstractParameter implements CallTransferReference {

	private int callTransferReference = 0;

	public CallTransferReferenceImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public CallTransferReferenceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CallTransferReferenceImpl(int callTransferReference) {
		super();
		this.callTransferReference = callTransferReference;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 1) {
			throw new ParameterRangeInvalidException("byte[] must  not be null and length must  be 1");
		}
		this.callTransferReference = b[0];
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
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
