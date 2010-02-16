/**
 * Start time:13:04:01 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericReference;

/**
 * Start time:13:04:01 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class GenericReferenceImpl extends AbstractParameter implements GenericReference{

	
	
	/**
	 * 	
	 */
	public GenericReferenceImpl() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @throws ParameterRangeInvalidException 
	 * @throws ParameterRangeInvalidException 
	 * 	
	 */
	public GenericReferenceImpl(byte[] b) throws ParameterRangeInvalidException {
		decodeElement(b);
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.isup.parameters.ISUPParameter#getCode()
	 */
	public int getCode() {
		// TODO Auto-generated method stub
		return _PARAMETER_CODE;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
