/**
 * Start time:17:24:08 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperations;

/**
 * Start time:17:24:08 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class RemoteOperationsImpl extends AbstractISUPParameter implements RemoteOperations{
	
//FIXME: Impl


	public RemoteOperationsImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public RemoteOperationsImpl() {
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
