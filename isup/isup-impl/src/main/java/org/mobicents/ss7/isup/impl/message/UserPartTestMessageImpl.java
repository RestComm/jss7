/**
 * Start time:00:12:55 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.ss7.isup.impl.message;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.UserPartTestMessage;
import org.mobicents.ss7.isup.message.parameter.MessageType;

/**
 * Start time:00:12:55 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class UserPartTestMessageImpl extends ISUPMessageImpl implements UserPartTestMessage {

	/**
	 * 	
	 * @param source
	 * @throws ParameterRangeInvalidException
	 */
	public UserPartTestMessageImpl(Object source){
		super(source);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	
	 */
	public UserPartTestMessageImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.mobicents.ss7.isup.ISUPMessageImpl#decodeMandatoryParameters(byte[], int)
	 */
	@Override
	protected int decodeMandatoryParameters(byte[] b, int index) throws ParameterRangeInvalidException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.ss7.isup.ISUPMessageImpl#decodeMandatoryVariableBody(byte[], int)
	 */
	@Override
	protected void decodeMandatoryVariableBody(byte[] parameterBody, int parameterIndex) throws ParameterRangeInvalidException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.mobicents.ss7.isup.ISUPMessageImpl#decodeOptionalBody(byte[], byte)
	 */
	@Override
	protected void decodeOptionalBody(byte[] parameterBody, byte parameterCode) throws ParameterRangeInvalidException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.mobicents.ss7.isup.ISUPMessageImpl#getMessageType()
	 */
	@Override
	public MessageType getMessageType() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.ss7.isup.ISUPMessageImpl#getNumberOfMandatoryVariableLengthParameters()
	 */
	@Override
	protected int getNumberOfMandatoryVariableLengthParameters() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.ss7.isup.ISUPMessageImpl#hasAllMandatoryParameters()
	 */
	@Override
	public boolean hasAllMandatoryParameters() {
		throw new UnsupportedOperationException();
	}
	@Override
	protected boolean optionalPartIsPossible() {
		
		throw new UnsupportedOperationException();
	}

}
