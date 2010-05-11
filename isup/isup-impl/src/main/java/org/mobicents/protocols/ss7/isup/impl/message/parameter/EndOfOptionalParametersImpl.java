/**
 * Start time:11:21:05 2009-03-31<br>
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
import org.mobicents.protocols.ss7.isup.message.parameter.EndOfOptionalParameters;

/**
 * Start time:11:21:05 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * This class represent element that encodes end of parameters
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class EndOfOptionalParametersImpl extends AbstractParameter implements EndOfOptionalParameters{

	public EndOfOptionalParametersImpl() {
		super();
		
	}

	public EndOfOptionalParametersImpl(byte[] b) {
		super();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	/**
	 * heeh, value is zero actually :D
	 */
	public static final int _PARAMETER_CODE = 0;

	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		// TODO Auto-generated method stub
		return new byte[] { 0 };
	}

	@Override
	public int encodeElement(ByteArrayOutputStream bos) throws IOException {
		bos.write(0);
		return 1;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
