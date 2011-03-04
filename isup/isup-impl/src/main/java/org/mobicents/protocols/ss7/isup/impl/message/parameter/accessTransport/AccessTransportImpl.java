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

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:13:39:50 2009-03-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AccessTransportImpl extends AbstractISUPParameter implements AccessTransport {

	// FIXME: Q763 3.3

	public AccessTransportImpl() {
		super();

	}

	public AccessTransportImpl(byte[] b) throws ParameterException {
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

	public int encode(ByteArrayOutputStream bos) throws ParameterException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCode() {
		return _PARAMETER_CODE;
	}

}
