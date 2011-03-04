package org.mobicents.protocols.ss7.isup.message;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ISUPParameter;
import org.mobicents.protocols.ss7.isup.message.parameter.MessageType;

/**
 * Start time:08:55:07 2009-08-30<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ISUPMessage {

	/**
	 * Get mandatory field, CIC.
	 * @return
	 */
	public CircuitIdentificationCode getCircuitIdentificationCode();
	/**
	 * Set mandatory field, CIC.
	 * @return
	 */
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
	 * @throws ParameterException - thrown if parameter is not part of message.
	 */
	public void addParameter(ISUPParameter param) throws ParameterException;
	/**
	 * Returns parameter with passed code.
	 * @param parameterCode
	 * @return
	 * @throws ParameterException - thrown if code does not match any parameter.
	 */
	public ISUPParameter getParameter(int parameterCode) throws ParameterException;
	/**
	 * Removes parameter from this message.
	 * @param parameterCode
	 * @throws ParameterException
	 */
	public void removeParameter(int parameterCode) throws ParameterException;

	/**
	 * @return <ul>
	 *         <li><b>true</b> - if all requried parameters are set</li>
	 *         <li><b>false</b> - otherwise</li>
	 *         </ul>
	 */
	public boolean hasAllMandatoryParameters();
	
	
	
}
