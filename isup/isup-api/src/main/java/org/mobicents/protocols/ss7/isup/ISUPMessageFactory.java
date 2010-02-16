/**
 * Start time:12:04:59 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup;

import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.AnswerMessage;
import org.mobicents.protocols.ss7.isup.message.ApplicationTransportMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.BlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CallProgressMessage;
import org.mobicents.protocols.ss7.isup.message.ChargeInformationMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupQueryResponseMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupResetMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupUnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.ConfusionMessage;
import org.mobicents.protocols.ss7.isup.message.ConnectMessage;
import org.mobicents.protocols.ss7.isup.message.ContinuityCheckRequestMessage;
import org.mobicents.protocols.ss7.isup.message.ContinuityMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityAcceptedMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityRejectedMessage;
import org.mobicents.protocols.ss7.isup.message.FacilityRequestMessage;
import org.mobicents.protocols.ss7.isup.message.ForwardTransferMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.IdentificationRequestMessage;
import org.mobicents.protocols.ss7.isup.message.IdentificationResponseMessage;
import org.mobicents.protocols.ss7.isup.message.InformationMessage;
import org.mobicents.protocols.ss7.isup.message.InformationRequestMessage;
import org.mobicents.protocols.ss7.isup.message.InitialAddressMessage;
import org.mobicents.protocols.ss7.isup.message.LoopPreventionMessage;
import org.mobicents.protocols.ss7.isup.message.LoopbackAckMessage;
import org.mobicents.protocols.ss7.isup.message.NetworkResourceManagementMessage;
import org.mobicents.protocols.ss7.isup.message.OverloadMessage;
import org.mobicents.protocols.ss7.isup.message.PassAlongMessage;
import org.mobicents.protocols.ss7.isup.message.PreReleaseInformationMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ReleaseMessage;
import org.mobicents.protocols.ss7.isup.message.ResetCircuitMessage;
import org.mobicents.protocols.ss7.isup.message.ResumeMessage;
import org.mobicents.protocols.ss7.isup.message.SegmentationMessage;
import org.mobicents.protocols.ss7.isup.message.SubsequentAddressMessage;
import org.mobicents.protocols.ss7.isup.message.SubsequentDirectoryNumberMessage;
import org.mobicents.protocols.ss7.isup.message.SuspendMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.UnblockingMessage;
import org.mobicents.protocols.ss7.isup.message.UnequippedCICMessage;
import org.mobicents.protocols.ss7.isup.message.User2UserInformationMessage;
import org.mobicents.protocols.ss7.isup.message.UserPartAvailableMessage;
import org.mobicents.protocols.ss7.isup.message.UserPartTestMessage;

/**
 * Start time:12:04:59 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public interface ISUPMessageFactory {

	/**
	 * @return
	 */
	InitialAddressMessage createIAM();

	/**
	 * @return
	 */
	AddressCompleteMessage createACM();

	/**
	 * @return
	 */
	ReleaseMessage createREL();

	/**
	 * @return
	 */
	ReleaseCompleteMessage createRLC();

	/**
	 * @return
	 */
	ApplicationTransportMessage createAPT();

	/**
	 * @return
	 */
	AnswerMessage createANM();

	/**
	 * @return
	 */
	CallProgressMessage createCPG();

	/**
	 * @return
	 */
	CircuitGroupResetAckMessage createGRA();

	/**
	 * @return
	 */
	ConfusionMessage createCNF();



	/**
	 * @return
	 */
	ConnectMessage createCON();

	/**
	 * @return
	 */
	ContinuityMessage createCOT();

	/**
	 * @return
	 */
	FacilityRejectedMessage createFRJ();

	/**
	 * @return
	 */
	InformationMessage createINF();

	/**
	 * @return
	 */
	InformationRequestMessage createINR();

	/**
	 * @return
	 */
	SubsequentAddressMessage createSAM();

	/**
	 * @return
	 */
	SubsequentDirectoryNumberMessage createSDN();

	/**
	 * @return
	 */
	ForwardTransferMessage createFOT();

	/**
	 * @return
	 */
	ResumeMessage createRES();

	/**
	 * @return
	 */
	BlockingMessage createBLO();

	/**
	 * @return
	 */
	BlockingAckMessage createBLA();

	/**
	 * @return
	 */
	ContinuityCheckRequestMessage createCCR();

	/**
	 * @return
	 */
	LoopbackAckMessage createLPA();

	/**
	 * @return
	 */
	LoopPreventionMessage createLPP();

	/**
	 * @return
	 */
	OverloadMessage createOLM();

	/**
	 * @return
	 */
	SuspendMessage createSUS();

	/**
	 * @return
	 */
	ResetCircuitMessage createRSC();

	/**
	 * @return
	 */
	UnblockingMessage createUBL();

	/**
	 * @return
	 */
	UnblockingAckMessage createUBA();

	/**
	 * @return
	 */
	UnequippedCICMessage createUCIC();

	/**
	 * @return
	 */
	CircuitGroupBlockingMessage createCGB();

	/**
	 * @return
	 */
	CircuitGroupBlockingAckMessage createCGBA();

	/**
	 * @return
	 */
	CircuitGroupUnblockingMessage createCGU();

	/**
	 * @return
	 */
	CircuitGroupUnblockingAckMessage createCGUA();

	/**
	 * @return
	 */
	CircuitGroupResetMessage createGRS();

	/**
	 * @return
	 */
	CircuitGroupQueryResponseMessage createCQR();

	/**
	 * @return
	 */
	CircuitGroupQueryMessage createCQM();

	/**
	 * @return
	 */
	FacilityAcceptedMessage createFAA();

	/**
	 * @return
	 */
	FacilityRequestMessage createFAR();

	/**
	 * @return
	 */
	PassAlongMessage createPAM();

	/**
	 * @return
	 */
	PreReleaseInformationMessage createPRI();

	/**
	 * @return
	 */
	FacilityMessage createFAC();

	/**
	 * @return
	 */
	NetworkResourceManagementMessage createNRM();

	/**
	 * @return
	 */
	IdentificationRequestMessage createIDR();

	/**
	 * @return
	 */
	IdentificationResponseMessage createIRS();

	/**
	 * @return
	 */
	SegmentationMessage createSGM();

	/**
	 * @return
	 */
	ChargeInformationMessage createCIM();

	/**
	 * @return
	 */
	UserPartAvailableMessage createUPA();

	/**
	 * @return
	 */
	UserPartTestMessage createUPT();

	/**
	 * @return
	 */
	User2UserInformationMessage createUSR();
	
	
	ISUPMessage createCommand(int commandCode);

}
