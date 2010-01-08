/**
 * Start time:12:31:58 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.OriginatingISCPointCode;

/**
 * Start time:12:31:58 2009-04-02<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class OriginatingISCPointCodeImpl extends AbstractPointCode implements OriginatingISCPointCode{
	

	public int getCode() {

		return _PARAMETER_CODE;
	}

	public OriginatingISCPointCodeImpl() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OriginatingISCPointCodeImpl(byte[] b) throws ParameterRangeInvalidException {
		super(b);
		
	}
}
