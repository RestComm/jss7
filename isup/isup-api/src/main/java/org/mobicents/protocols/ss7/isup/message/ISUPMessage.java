package org.mobicents.protocols.ss7.isup.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
	 * @return <ul>
	 *         <li><b>true</b> - if all requried parameters are set</li>
	 *         <li><b>false</b> - otherwise</li>
	 *         </ul>
	 */
	public boolean hasAllMandatoryParameters();

	/**
	 * Returns message code. See Q.763 Table 4. It simply return value of static
	 * constant, where value of parameter is value of MESSAGE_CODE
	 * 
	 * @return
	 */
	public MessageType getMessageType();

	public byte[] encodeElement() throws IOException;

	public int encodeElement(ByteArrayOutputStream bos) throws IOException;

	public int decodeElement(byte[] b) throws ParameterRangeInvalidException;

	public void addParameter(ISUPParameter param) throws ParameterRangeInvalidException;

	public ISUPParameter getParameter(int parameterCode) throws ParameterRangeInvalidException;

	public void removeParameter(int parameterCode) throws ParameterRangeInvalidException;

	public ISUPTransaction getTransaction();

	/**
	 * Generates TX key for fast matching, messages must have some part static,
	 * so it can be used to match tx, other than that there is no way to match
	 * incoming response to transaction
	 * 
	 * @return
	 */
	public TransactionKey generateTransactionKey();
	
	public CircuitIdentificationCode getCircuitIdentificationCode();
	public void setCircuitIdentificationCode(CircuitIdentificationCode cic);

}
