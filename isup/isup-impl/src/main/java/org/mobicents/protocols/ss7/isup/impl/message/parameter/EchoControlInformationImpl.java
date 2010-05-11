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

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.EchoControlInformation;

/**
 * Start time:18:36:08 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EchoControlInformationImpl extends AbstractParameter implements EchoControlInformation {

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

	public EchoControlInformationImpl(byte[] b) throws ParameterRangeInvalidException {
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
			throw new ParameterRangeInvalidException("byte[] must not be null or have different size than 1");
		}
		byte v = b[0];

		this.outgoingEchoControlDeviceInformationIndicator = v & 0x03;
		this.incomingEchoControlDeviceInformationIndicator = (v >> 2) & 0x03;
		this.outgoingEchoControlDeviceInformationRequestIndicator = (v >> 4) & 0x03;
		this.incomingEchoControlDeviceInformationRequestIndicator = (v >> 6) & 0x03;
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
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
