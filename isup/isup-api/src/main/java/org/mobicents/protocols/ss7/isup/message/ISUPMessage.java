package org.mobicents.protocols.ss7.isup.message;

import org.mobicents.protocols.ss7.isup.ISUPComponent;
import org.mobicents.protocols.ss7.isup.ISUPTransaction;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.TransactionKey;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ISUPParameter;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageType;

/**
 * Start time:08:55:07 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ISUPMessage extends ISUPComponent {

	/**
	 * Set mandatory field, CIC.
	 * @return
	 */
	public CircuitIdentificationCode getCircuitIdentificationCode();

	public void setCircuitIdentificationCode(CircuitIdentificationCode cic);

	/**
	 * Returns message code. See Q.763 Table 4. It simply return value of static
	 * constant, where value of parameter is value of MESSAGE_CODE
	 * 
	 * @return
	 */
	public MessageType getMessageType();
	
	/**
	 * Adds parameter to this message.
	 * @param param
	 * @throws ParameterRangeInvalidException - thrown if parameter is not part of message.
	 */
	public void addParameter(ISUPParameter param) throws ParameterRangeInvalidException;
	/**
	 * Returns parameter with passed code.
	 * @param parameterCode
	 * @return
	 * @throws ParameterRangeInvalidException - thrown if code does not match any parameter.
	 */
	public ISUPParameter getParameter(int parameterCode) throws ParameterRangeInvalidException;
	/**
	 * Removes parameter from this message.
	 * @param parameterCode
	 * @throws ParameterRangeInvalidException
	 */
	public void removeParameter(int parameterCode) throws ParameterRangeInvalidException;
	
	/**
	 * Return reference to transaction if it exists.
	 * @return
	 */
	public ISUPTransaction getTransaction();

	/**
	 * Generates TX key for fast matching.
	 * 
	 * @return
	 */
	public TransactionKey generateTransactionKey();

	/**
	 * @return <ul>
	 *         <li><b>true</b> - if all requried parameters are set</li>
	 *         <li><b>false</b> - otherwise</li>
	 *         </ul>
	 */
	public boolean hasAllMandatoryParameters();

}
