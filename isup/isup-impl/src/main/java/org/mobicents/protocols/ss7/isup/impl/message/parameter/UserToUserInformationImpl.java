/**
 * Start time:13:13:44 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserInformation;

/**
 * Start time:13:13:44 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class UserToUserInformationImpl extends AbstractISUPParameter implements UserToUserInformation{

	
	//FIXME: add Q.931
	
	public UserToUserInformationImpl() {
		super();
		
	}
	public UserToUserInformationImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
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
