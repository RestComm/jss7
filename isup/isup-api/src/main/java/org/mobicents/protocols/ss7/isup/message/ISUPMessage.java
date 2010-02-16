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
	
	// FIXME: those should be in each message, not sure why Oleg wnts it like that ;/
	// ///////////
	// STATICS //
	// ///////////
	/**
	 * Address Complete Message, Q.763 reference table 21 <br>
	 * {@link AddressCompleteMessage}
	 */
	public static final int _MESSAGE_CODE_ACM = 0x06;
	/**
	 * Initial Address Message, Q.763 reference table 32 <br>
	 * {@link InitialAddressMessage}
	 */
	public static final int _MESSAGE_CODE_IAM = 0x01;
	/**
	 * Release Complete Message, Q.763 reference table 34 <br>
	 * {@link ReleaseCompleteMessage}
	 */
	public static final int _MESSAGE_CODE_RLC = 0x10;
	/**
	 * Release Message, Q.763 reference table 33 <br> {@link ReleaseMessage}
	 */
	public static final int _MESSAGE_CODE_REL = 0x0C;
	/**
	 * Application Transport Message, Q.763 reference table 51 <br>
	 * {@link ApplicationTransportMessage}
	 */
	public static final int _MESSAGE_CODE_APT = 0x41;
	/**
	 * Answer Message, Q.763 reference table 22 <br> {@link AnswerMessage}
	 */
	public static final int _MESSAGE_CODE_ANM = 0x09;
	/**
	 * Call Progres Message, Q.763 reference table 23 <br>
	 * {@link CallProgressMessage}
	 */
	public static final int _MESSAGE_CODE_CPG = 0x2C;
	/**
	 * Circuit Group Query Message, Q.763 reference table 21 <br>
	 * {@link CircuitGroupQueryMessage}
	 */
	public static final int _MESSAGE_CODE_CQM = 0x2A;
	/**
	 * Circuit GroupReset Ack Message, Q.763 reference table 25 <br>
	 * {@link CircuitGroupResetAckMessage}
	 */
	public static final int _MESSAGE_CODE_GRA = 0x29;
	/**
	 * Confussion Message, Q.763 reference table 26 <br> {@link ConfusionMessage}
	 */
	public static final int _MESSAGE_CODE_CFN = 0x2F;
	/**
	 * Connect Message, Q.763 reference table 27 <br> {@link ConnectMessage}
	 */
	public static final int _MESSAGE_CODE_CON = 0x07;
	/**
	 * Continuity Message, Q.763 reference table 28 <br> {@link ContinuityMessage}
	 */
	public static final int _MESSAGE_CODE_COT = 0x05;
	/**
	 * Facility Rejected Message, Q.763 reference table 29 <br>
	 * {@link FacilityRejectedMessage}
	 */
	public static final int _MESSAGE_CODE_FRJ = 0x21;
	/**
	 * Information Message, Q.763 reference table 30 <br> {@link InformationMessage}
	 */
	public static final int _MESSAGE_CODE_INF = 0x04;
	/**
	 * Information Request Message, Q.763 reference table 31 <br>
	 * {@link InformationRequestMessage}
	 */
	public static final int _MESSAGE_CODE_INR = 0x03;
	/**
	 * Subsequent Address Message, Q.763 reference table 35 <br>
	 * {@link SubsequentAddressMessage}
	 */
	public static final int _MESSAGE_CODE_SAM = 0x02;
	/**
	 * Subsequent Directory Number Message, Q.763 reference table 53 <br>
	 * {@link SubsequentDirectoryNumberMessage}
	 */
	public static final int _MESSAGE_CODE_SDN = 0x43;

	/**
	 * Forward Transfer Message, Q.763 reference table 37 <br>
	 * {@link ForwardTransferMessage}
	 */
	public static final int _MESSAGE_CODE_FOT = 0x08;
	/**
	 * Resume Message, Q.763 reference table 38 <br> {@link ResumeMessage}
	 */
	public static final int _MESSAGE_CODE_RES = 0x0E;
	/**
	 * Blocking Message, Q.763 reference table 39 <br> {@link BlockingMessage}
	 */
	public static final int _MESSAGE_CODE_BLO = 0x13;
	/**
	 * Blocking Ack Message, Q.763 reference table 39 <br>
	 * {@link BlockingAckMessage}
	 */
	public static final int _MESSAGE_CODE_BLA = 0x15;
	/**
	 * Continuity Check Request Message, Q.763 reference table 39 <br>
	 * {@link ContinuityCheckRequestMessage}
	 */
	public static final int _MESSAGE_CODE_CCR = 0x11;
	/**
	 * Loopback Ack Message, Q.763 reference table 39 <br>
	 * {@link LoopbackAckMessage}
	 */
	public static final int _MESSAGE_CODE_LPA = 0x24;
	/**
	 * Loopback Prevention Message, Q.763 reference table 50 <br>
	 * {@link LoopPreventionMessage}
	 */
	public static final int _MESSAGE_CODE_LPP = 0x40;
	/**
	 * Overload Message, Q.763 reference table 39 <br> {@link OverloadMessage}
	 */
	public static final int _MESSAGE_CODE_OLM = 0x30;
	/**
	 * Suspend Message, Q.763 reference table 38 <br> {@link SuspendMessage}
	 */
	public static final int _MESSAGE_CODE_SUS = 0x0D;
	/**
	 * Reset Circuit Message, Q.763 reference table 39 <br>
	 * {@link ResetCircuitMessage}
	 */
	public static final int _MESSAGE_CODE_RSC = 0x12;
	/**
	 * Unblocking Message, Q.763 reference table 39 <br> {@link UnblockingMessage}
	 */
	public static final int _MESSAGE_CODE_UBL = 0x14;
	/**
	 * Unblocking Ack Message, Q.763 reference table 39 <br>
	 * {@link UnblockingAckMessage}
	 */
	public static final int _MESSAGE_CODE_UBA = 0x16;
	/**
	 * Unequipped CIC Message, Q.763 reference table 39 <br>
	 * {@link UnequippedCICMessage}
	 */
	public static final int _MESSAGE_CODE_UCIC = 0x2E;
	/**
	 * Circuit Group Blocking Message, Q.763 reference table 40 <br>
	 * {@link CircuitGroupBlockingMessage}
	 */
	public static final int _MESSAGE_CODE_CGB = 0x18;
	/**
	 * ircuit Group Blocking Ack Message, Q.763 reference table 40 <br>
	 * {@link CircuitGroupBlockingAckMessage}
	 */
	public static final int _MESSAGE_CODE_CGBA = 0x1A;
	/**
	 * Circuit Group Unblocking Message, Q.763 reference table 40 <br>
	 * {@link AddressCompleteMessage}
	 */
	public static final int _MESSAGE_CODE_CGU = 0x19;
	/**
	 * Circuit Group Unblocking Ack Message, Q.763 reference table 40 <br>
	 * {@link AddressCompleteMessage}
	 */
	public static final int _MESSAGE_CODE_CGUA = 0x1B;

	/**
	 * Circuit Group Reset Message, Q.763 reference table 41 <br>
	 * {@link CircuitGroupResetMessage}
	 */
	public static final int _MESSAGE_CODE_GRS = 0x17;
	/**
	 * Circuit Group Query Response Message, Q.763 reference table 24 <br>
	 * {@link CircuitGroupQueryResponseMessage}
	 */
	public static final int _MESSAGE_CODE_CQR = 0x2B;
	/**
	 * Facility Accepted Message, Q.763 reference table 42 <br>
	 * {@link FacilityAcceptedMessage}
	 */
	public static final int _MESSAGE_CODE_FAA = 0x20;
	/**
	 * Facility Request Message, Q.763 reference table 42 <br>
	 * {@link FacilityRequestMessage}
	 */
	public static final int _MESSAGE_CODE_FAR = 0x1F;
	/**
	 * Pass-along Message, Q.763 reference table 43 <br> {@link PassAlongMessage}
	 */
	public static final int _MESSAGE_CODE_PAM = 0x28;
	/**
	 * Pre-release information Message, Q.763 reference table 52 <br>
	 * {@link PreReleaseInformationMessage}
	 */
	public static final int _MESSAGE_CODE_PRI = 0x42;
	/**
	 * Facility Message, Q.763 reference table 45 <br>
	 * {@link FacilityMessage}
	 */
	public static final int _MESSAGE_CODE_FAC = 0x33;
	/**
	 * Network Resource Management Message, Q.763 reference table 46 <br>
	 * {@link NetworkResourceManagementMessage}
	 */
	public static final int _MESSAGE_CODE_NRM = 0x32;
	/**
	 * Identification Request Message, Q.763 reference table 47 <br>
	 * {@link IdentificationRequestMessage}
	 */
	public static final int _MESSAGE_CODE_IDR = 0x36;
	/**
	 * Identification Response Message, Q.763 reference table 48 <br>
	 * {@link IdentificationResponseMessage}
	 */
	public static final int _MESSAGE_CODE_IRS = 0x37;
	/**
	 * Segmentation Message, Q.763 reference table 49 <br>
	 * {@link SegmentationMessage}
	 */
	public static final int _MESSAGE_CODE_SGM = 0x38;

	/**
	 * Charge Information Message, Q.763 reference table (Note) <br>
	 * {@link ChargeInformationMessage}
	 */
	public static final int _MESSAGE_CODE_CIM = 0x31;
	/**
	 * User Part Available Message, Q.763 reference table 44 <br>
	 * {@link UserPartAvailableMessage}
	 */
	public static final int _MESSAGE_CODE_UPA = 0x35;
	/**
	 * User Part Test Message, Q.763 reference table 44 <br>
	 * {@link UserPartTestMessage}
	 */
	public static final int _MESSAGE_CODE_UPT = 0x34;
	/**
	 * User To User Information Message, Q.763 reference table 36 <br>
	 * {@link User2UserInformationMessage}
	 */
	public static final int _MESSAGE_CODE_USR = 0x2D;

	/**
	 * @return <ul>
	 *         <li><b>true</b> - if all requried parameters are set</li>
	 *         <li><b>false</b> - otherwise</li>
	 *         </ul>
	 */
	public boolean hasAllMandatoryParameters();

	/**
	 * Returns message code. See Q.763 Table 4. It simply return value of static
	 * constant - _MESSAGE_TYPE, where value of parameter is value _MESSAGE_CODE
	 * 
	 * @return
	 */
	public MessageType getMessageType();

	// FIXME: above will be changed

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
