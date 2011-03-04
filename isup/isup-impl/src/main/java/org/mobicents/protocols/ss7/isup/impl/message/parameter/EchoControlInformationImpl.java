/**
 * Start time:18:36:08 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.EchoControlInformation;

/**
 * Start time:18:36:08 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EchoControlInformationImpl extends AbstractISUPParameter implements EchoControlInformation {

	private int outgoingEchoControlDeviceInformationIndicator;
	private int incomingEchoControlDeviceInformationIndicator;
	private int outgoingEchoControlDeviceInformationRequestIndicator;
	private int incomingEchoControlDeviceInformationRequestIndicator;

	public EchoControlInformationImpl() {
		super();
		
	}

	public EchoControlInformationImpl(int outgoingEchoControlDeviceInformationIndicator, int incomingEchoControlDeviceInformationIndicator, int outgoingEchoControlDeviceInformationRequestIndicator,
			int incomingEchoControlDeviceInformationRequestIndicator) {
		super();
		this.outgoingEchoControlDeviceInformationIndicator = outgoingEchoControlDeviceInformationIndicator;
		this.incomingEchoControlDeviceInformationIndicator = incomingEchoControlDeviceInformationIndicator;
		this.outgoingEchoControlDeviceInformationRequestIndicator = outgoingEchoControlDeviceInformationRequestIndicator;
		this.incomingEchoControlDeviceInformationRequestIndicator = incomingEchoControlDeviceInformationRequestIndicator;
	}

	public EchoControlInformationImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 1) {
			throw new ParameterException("byte[] must not be null or have different size than 1");
		}
		byte v = b[0];

		this.outgoingEchoControlDeviceInformationIndicator = v & 0x03;
		this.incomingEchoControlDeviceInformationIndicator = (v >> 2) & 0x03;
		this.outgoingEchoControlDeviceInformationRequestIndicator = (v >> 4) & 0x03;
		this.incomingEchoControlDeviceInformationRequestIndicator = (v >> 6) & 0x03;
		return 1;
	}

	public byte[] encode() throws ParameterException {
		byte v = 0;

		v |= this.outgoingEchoControlDeviceInformationIndicator & 0x03;
		v |= (this.incomingEchoControlDeviceInformationIndicator & 0x03) << 2;
		v |= (this.outgoingEchoControlDeviceInformationRequestIndicator & 0x03) << 4;
		v |= (this.incomingEchoControlDeviceInformationRequestIndicator & 0x03) << 6;

		byte[] b = { v };
		return b;
	}

	public int getOutgoingEchoControlDeviceInformationIndicator() {
		return outgoingEchoControlDeviceInformationIndicator;
	}

	public void setOutgoingEchoControlDeviceInformationIndicator(int outgoingEchoControlDeviceInformationIndicator) {
		this.outgoingEchoControlDeviceInformationIndicator = outgoingEchoControlDeviceInformationIndicator;
	}

	public int getIncomingEchoControlDeviceInformationIndicator() {
		return incomingEchoControlDeviceInformationIndicator;
	}

	public void setIncomingEchoControlDeviceInformationIndicator(int incomingEchoControlDeviceInformationIndicator) {
		this.incomingEchoControlDeviceInformationIndicator = incomingEchoControlDeviceInformationIndicator;
	}

	public int getOutgoingEchoControlDeviceInformationRequestIndicator() {
		return outgoingEchoControlDeviceInformationRequestIndicator;
	}

	public void setOutgoingEchoControlDeviceInformationRequestIndicator(int outgoingEchoControlDeviceInformationRequestIndicator) {
		this.outgoingEchoControlDeviceInformationRequestIndicator = outgoingEchoControlDeviceInformationRequestIndicator;
	}

	public int getIncomingEchoControlDeviceInformationRequestIndicator() {
		return incomingEchoControlDeviceInformationRequestIndicator;
	}

	public void setIncomingEchoControlDeviceInformationRequestIndicator(int incomingEchoControlDeviceInformationRequestIndicator) {
		this.incomingEchoControlDeviceInformationRequestIndicator = incomingEchoControlDeviceInformationRequestIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
