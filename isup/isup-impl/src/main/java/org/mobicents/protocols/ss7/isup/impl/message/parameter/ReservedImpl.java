/**
 * Start time:14:30:39 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.Reserved;

/**
 * Start time:14:30:39 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class ReservedImpl extends AbstractISUPParameter implements Reserved{
	

public ReservedImpl() {
		super();
		
	}

	public int decode(byte[] b) throws ParameterException {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte[] encode() throws ParameterException {
		// TODO Auto-generated method stub
		return null;
	}
	public int getCode() {

		return _PARAMETER_CODE;
	}
}
