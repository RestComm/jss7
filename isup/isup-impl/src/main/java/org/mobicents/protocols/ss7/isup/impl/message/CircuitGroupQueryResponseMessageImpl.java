/**
 * Start time:00:09:10 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.Map;
import java.util.Set;

import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.AbstractISUPParameter;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CircuitStateIndicatorImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.MessageTypeImpl;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryResponseMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitStateIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;

/**
 * Start time:00:09:10 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class CircuitGroupQueryResponseMessageImpl extends ISUPMessageImpl implements CircuitGroupQueryResponseMessage {

	public static final MessageType _MESSAGE_TYPE = new MessageTypeImpl(MESSAGE_CODE);
	private static final int _MANDATORY_VAR_COUNT = 2;
	
	static final int _INDEX_F_MessageType = 0;
	static final int _INDEX_V_RangeAndStatus = 0;
	static final int _INDEX_V_CircuitStateIndicator = 1;

	CircuitGroupQueryResponseMessageImpl(Set<Integer> mandatoryCodes, Set<Integer> mandatoryVariableCodes, Set<Integer> optionalCodes, Map<Integer, Integer> mandatoryCode2Index,
			Map<Integer, Integer> mandatoryVariableCode2Index, Map<Integer, Integer> optionalCode2Index) {
		super( mandatoryCodes, mandatoryVariableCodes, optionalCodes, mandatoryCode2Index, mandatoryVariableCode2Index, optionalCode2Index);

		super.f_Parameters.put(_INDEX_F_MessageType, this.getMessageType());

	}
	
	public void setRangeAndStatus(RangeAndStatus ras)
	{
		super.v_Parameters.put(_INDEX_V_RangeAndStatus, ras);
	}
	public RangeAndStatus getRangeAndStatus()
	{
		return (RangeAndStatus) super.v_Parameters.get(_INDEX_V_RangeAndStatus);
	}
	
	public void setCircuitStateIndicator(CircuitStateIndicator ras)
	{
		super.v_Parameters.put(_INDEX_V_CircuitStateIndicator, ras);
	}
	public CircuitStateIndicator getCircuitStateIndicator()
	{
		return (CircuitStateIndicator) super.v_Parameters.get(_INDEX_V_CircuitStateIndicator);
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeMandatoryVariableBody(byte
	 * [], int)
	 */
	
	protected void decodeMandatoryVariableBody(ISUPParameterFactory parameterFactory,byte[] parameterBody, int parameterIndex) throws ParameterException {
		switch (parameterIndex) {
		case _INDEX_V_RangeAndStatus:

			RangeAndStatus ras = parameterFactory.createRangeAndStatus();
			((AbstractISUPParameter)ras).decode(parameterBody);
			this.setRangeAndStatus(ras);
			break;
		case _INDEX_V_CircuitStateIndicator:
			CircuitStateIndicator csi = new CircuitStateIndicatorImpl(parameterBody);
			this.setCircuitStateIndicator(csi);
			break;
			
		default:
			throw new ParameterException("Unrecognized parameter index for mandatory variable part, index: " + parameterIndex);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#decodeOptionalBody(byte[],
	 * byte)
	 */
	
	protected void decodeOptionalBody(ISUPParameterFactory parameterFactory,byte[] parameterBody, byte parameterCode) throws ParameterException {
		throw new ParameterException("This message does not support optional parameters");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#getMessageType()
	 */
	
	public MessageType getMessageType() {
		return this._MESSAGE_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.isup.ISUPMessageImpl#
	 * getNumberOfMandatoryVariableLengthParameters()
	 */
	
	protected int getNumberOfMandatoryVariableLengthParameters() {

		return _MANDATORY_VAR_COUNT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageImpl#hasAllMandatoryParameters()
	 */
	
	public boolean hasAllMandatoryParameters() {
		return  super.v_Parameters.get(_INDEX_V_RangeAndStatus) != null;
	}

	
	protected boolean optionalPartIsPossible() {

		return false;
	}

}
