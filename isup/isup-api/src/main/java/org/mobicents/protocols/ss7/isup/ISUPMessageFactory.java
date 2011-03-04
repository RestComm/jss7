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
	//InitialAddressMessage createIAM(CircuitIdentificationCode ci);
	InitialAddressMessage createIAM(int cic);

	/**
	 * @return
	 */
	AddressCompleteMessage createACM();

	/**
	 * @return
	 */
	ReleaseMessage createREL(int cic);

	/**
	 * @return
	 */
	ReleaseCompleteMessage createRLC();

	/**
	 * @return
	 */
	ApplicationTransportMessage createAPT(int cic);

	/**
	 * @return
	 */
	AnswerMessage createANM();

	/**
	 * @return
	 */
	CallProgressMessage createCPG(int cic);

	/**
	 * @return
	 */
	CircuitGroupResetAckMessage createGRA();

	/**
	 * @return
	 */
	ConfusionMessage createCNF(int cic);



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
	FacilityRejectedMessage createFRJ(int cic);

	/**
	 * @return
	 */
	InformationMessage createINF();

	/**
	 * @return
	 */
	InformationRequestMessage createINR(int cic);

	/**
	 * @return
	 */
	SubsequentAddressMessage createSAM(int cic);

	/**
	 * @return
	 */
	SubsequentDirectoryNumberMessage createSDN(int cic);

	/**
	 * @return
	 */
	ForwardTransferMessage createFOT(int cic);

	/**
	 * @return
	 */
	ResumeMessage createRES(int cic);

	/**
	 * @return
	 */
	BlockingMessage createBLO(int cic);

	/**
	 * @return
	 */
	BlockingAckMessage createBLA();

	/**
	 * @return
	 */
	ContinuityCheckRequestMessage createCCR(int cic);

	/**
	 * @return
	 */
	LoopbackAckMessage createLPA();

	/**
	 * @return
	 */
	LoopPreventionMessage createLPP(int cic);

	/**
	 * @return
	 */
	OverloadMessage createOLM(int cic);

	/**
	 * @return
	 */
	SuspendMessage createSUS(int cic);

	/**
	 * @return
	 */
	ResetCircuitMessage createRSC(int cic);

	/**
	 * @return
	 */
	UnblockingMessage createUBL(int cic);

	/**
	 * @return
	 */
	UnblockingAckMessage createUBA();

	/**
	 * @return
	 */
	UnequippedCICMessage createUCIC(int cic);

	/**
	 * @return
	 */
	CircuitGroupBlockingMessage createCGB(int cic);

	/**
	 * @return
	 */
	CircuitGroupBlockingAckMessage createCGBA();

	/**
	 * @return
	 */
	CircuitGroupUnblockingMessage createCGU(int cic);

	/**
	 * @return
	 */
	CircuitGroupUnblockingAckMessage createCGUA();

	/**
	 * @return
	 */
	CircuitGroupResetMessage createGRS(int cic);

	/**
	 * @return
	 */
	CircuitGroupQueryResponseMessage createCQR();

	/**
	 * @return
	 */
	CircuitGroupQueryMessage createCQM(int cic);

	/**
	 * @return
	 */
	FacilityAcceptedMessage createFAA();

	/**
	 * @return
	 */
	FacilityRequestMessage createFAR(int cic);

	/**
	 * @return
	 */
	PassAlongMessage createPAM(int cic);

	/**
	 * @return
	 */
	PreReleaseInformationMessage createPRI(int cic);

	/**
	 * @return
	 */
	FacilityMessage createFAC(int cic);

	/**
	 * @return
	 */
	NetworkResourceManagementMessage createNRM(int cic);

	/**
	 * @return
	 */
	IdentificationRequestMessage createIDR(int cic);

	/**
	 * @return
	 */
	IdentificationResponseMessage createIRS();

	/**
	 * @return
	 */
	SegmentationMessage createSGM(int cic);

	/**
	 * @return
	 */
	ChargeInformationMessage createCIM(int cic);

	/**
	 * @return
	 */
	UserPartAvailableMessage createUPA(int cic);

	/**
	 * @return
	 */
	UserPartTestMessage createUPT(int cic);

	/**
	 * @return
	 */
	User2UserInformationMessage createUSR(int cic);
	
	
	ISUPMessage createCommand(int commandCode);

}
