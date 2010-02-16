/**
 * Start time:17:30:47 2009-03-29<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledNumber;

/**
 * Start time:17:30:47 2009-03-29<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class OriginalCalledNumberImpl extends CalledNumberImpl implements OriginalCalledNumber{

	
	public OriginalCalledNumberImpl(byte[] representation) throws ParameterRangeInvalidException {
		super(representation);
		// TODO Auto-generated constructor stub
	}

	public OriginalCalledNumberImpl(ByteArrayInputStream bis) throws ParameterRangeInvalidException {
		super(bis);
		// TODO Auto-generated constructor stub
	}

	public OriginalCalledNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator, int addressRepresentationREstrictedIndicator) {
		super(natureOfAddresIndicator, address, numberingPlanIndicator, addressRepresentationREstrictedIndicator);
		// TODO Auto-generated constructor stub
	}

	public OriginalCalledNumberImpl() throws ParameterRangeInvalidException {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
