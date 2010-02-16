/**
 * Start time:13:39:50 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter.accessTransport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AbstractParameter;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:13:39:50 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AccessTransportImpl extends AbstractParameter implements AccessTransport {

	// FIXME: Q763 3.3
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */

	public AccessTransportImpl() {
		super();

	}

	public AccessTransportImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.ISUPComponent#encodeElement(java.io.ByteArrayOutputStream
	 * )
	 */
	public int encodeElement(ByteArrayOutputStream bos) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.parameters.ISUPParameter#getCode()
	 */
	public int getCode() {
		return _PARAMETER_CODE;
	}

}
