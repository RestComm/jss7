/**
 * Start time:16:49:41 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitGroupSuperVisionMessageType;

/**
 * Start time:16:49:41 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class CircuitGroupSuperVisionMessageTypeImpl extends AbstractParameter implements CircuitGroupSuperVisionMessageType{

	
	

	private int circuitGroupSuperVisionMessageTypeIndicator = 0;

	public CircuitGroupSuperVisionMessageTypeImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public CircuitGroupSuperVisionMessageTypeImpl(int circuitGroupSuperVisionMessageTypeIndicator) {
		super();
		this.circuitGroupSuperVisionMessageTypeIndicator = circuitGroupSuperVisionMessageTypeIndicator;
	}

	public CircuitGroupSuperVisionMessageTypeImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length != 1) {
			throw new ParameterRangeInvalidException("byte[] must not be null or has size different than 1.");
		}
		this.circuitGroupSuperVisionMessageTypeIndicator = b[0] & 0x03;
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		byte[] b = new byte[] { (byte) (this.circuitGroupSuperVisionMessageTypeIndicator & 0x03) };

		return b;
	}

	@Override
	public int encodeElement(ByteArrayOutputStream bos) throws IOException {
		byte[] b = this.encodeElement();
		bos.write(b);
		return b.length;
	}

	public int getCircuitGroupSuperVisionMessageTypeIndicator() {
		return circuitGroupSuperVisionMessageTypeIndicator;
	}

	public void setCircuitGroupSuperVisionMessageTypeIndicator(int circuitGroupSuperVisionMessageTypeIndicator) {
		this.circuitGroupSuperVisionMessageTypeIndicator = circuitGroupSuperVisionMessageTypeIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
