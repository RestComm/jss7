/**
 * Start time:12:19:59 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPProvider;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.OriginalCalledNumberImpl;
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
import org.mobicents.protocols.ss7.isup.message.parameter.*;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;


/**
 * Start time:12:19:59 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class ISUPMessageFactoryImpl implements ISUPMessageFactory {

	private ISUPProvider providerImpl;
	
	/**
	 * 
	 * @param providerImpl
	 */
	public ISUPMessageFactoryImpl(ISUPProvider providerImpl) {
		this.providerImpl = providerImpl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createACM()
	 */
	public AddressCompleteMessage createACM() {
		return new AddressCompleteMessageImpl(providerImpl,_ACM_HOLDER.mandatoryCodes,_ACM_HOLDER.mandatoryVariableCodes,_ACM_HOLDER.optionalCodes,
				_ACM_HOLDER.mandatoryCodeToIndex,_ACM_HOLDER.mandatoryVariableCodeToIndex,_ACM_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createANM()
	 */
	public AnswerMessage createANM() {
		return new AnswerMessageImpl(providerImpl,_ANM_HOLDER.mandatoryCodes,_ANM_HOLDER.mandatoryVariableCodes,_ANM_HOLDER.optionalCodes,
				_ANM_HOLDER.mandatoryCodeToIndex,_ANM_HOLDER.mandatoryVariableCodeToIndex,_ANM_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createAPT()
	 */
	public ApplicationTransportMessage createAPT() {
		return new ApplicationTransportMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createBLA()
	 */
	public BlockingAckMessage createBLA() {
		return new BlockingAckMessageImpl(providerImpl,_BLA_HOLDER.mandatoryCodes,_BLA_HOLDER.mandatoryVariableCodes,_BLA_HOLDER.optionalCodes,
				_BLA_HOLDER.mandatoryCodeToIndex,_BLA_HOLDER.mandatoryVariableCodeToIndex,_BLA_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createBLO()
	 */
	public BlockingMessage createBLO() {
		return new BlockingMessageImpl(providerImpl,_BLO_HOLDER.mandatoryCodes,_BLO_HOLDER.mandatoryVariableCodes,_BLO_HOLDER.optionalCodes,
				_BLO_HOLDER.mandatoryCodeToIndex,_BLO_HOLDER.mandatoryVariableCodeToIndex,_BLO_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCCR()
	 */
	public ContinuityCheckRequestMessage createCCR() {
		return new ContinuityCheckRequestMessageImpl(providerImpl,_CCR_HOLDER.mandatoryCodes,_CCR_HOLDER.mandatoryVariableCodes,_CCR_HOLDER.optionalCodes,
				_CCR_HOLDER.mandatoryCodeToIndex,_CCR_HOLDER.mandatoryVariableCodeToIndex,_CCR_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCGB()
	 */
	public CircuitGroupBlockingMessage createCGB() {
		return new CircuitGroupBlockingMessageImpl(providerImpl,_CGB_HOLDER.mandatoryCodes,_CGB_HOLDER.mandatoryVariableCodes,_CGB_HOLDER.optionalCodes,
				_CGB_HOLDER.mandatoryCodeToIndex,_CGB_HOLDER.mandatoryVariableCodeToIndex,_CGB_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCGBA()
	 */
	public CircuitGroupBlockingAckMessage createCGBA() {
		return new CircuitGroupBlockingAckMessageImpl(providerImpl,_CGBA_HOLDER.mandatoryCodes,_CGBA_HOLDER.mandatoryVariableCodes,_CGBA_HOLDER.optionalCodes,
				_CGBA_HOLDER.mandatoryCodeToIndex,_CGBA_HOLDER.mandatoryVariableCodeToIndex,_CGBA_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCGU()
	 */
	public CircuitGroupUnblockingMessage createCGU() {
		return new CircuitGroupUnblockingMessageImpl(providerImpl,_CGU_HOLDER.mandatoryCodes,_CGU_HOLDER.mandatoryVariableCodes,_CGU_HOLDER.optionalCodes,
				_CGU_HOLDER.mandatoryCodeToIndex,_CGU_HOLDER.mandatoryVariableCodeToIndex,_CGU_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCGUA()
	 */
	public CircuitGroupUnblockingAckMessage createCGUA() {
		return new CircuitGroupUnblockingAckMessageImpl(providerImpl,_CGUA_HOLDER.mandatoryCodes,_CGUA_HOLDER.mandatoryVariableCodes,_CGUA_HOLDER.optionalCodes,
				_CGUA_HOLDER.mandatoryCodeToIndex,_CGUA_HOLDER.mandatoryVariableCodeToIndex,_CGUA_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCIM()
	 */
	public ChargeInformationMessage createCIM() {
		return new ChargeInformationMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCNF()
	 */
	public ConfusionMessage createCNF() {
		return new ConfusionMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCON()
	 */
	public ConnectMessage createCON() {
		return new ConnectMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCOT()
	 */
	public ContinuityMessage createCOT() {
		return new ContinuityMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCPG()
	 */
	public CallProgressMessage createCPG() {
		return new CallProgressMessageImpl(providerImpl,_CPG_HOLDER.mandatoryCodes,_CPG_HOLDER.mandatoryVariableCodes,_CPG_HOLDER.optionalCodes,
				_CPG_HOLDER.mandatoryCodeToIndex,_CPG_HOLDER.mandatoryVariableCodeToIndex,_CPG_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCQM()
	 */
	public CircuitGroupQueryMessage createCQM() {
		return new CircuitGroupQueryMessageImpl(providerImpl,_CQM_HOLDER.mandatoryCodes,_CQM_HOLDER.mandatoryVariableCodes,_CQM_HOLDER.optionalCodes,
				_CQM_HOLDER.mandatoryCodeToIndex,_CQM_HOLDER.mandatoryVariableCodeToIndex,_CQM_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCQR()
	 */
	public CircuitGroupQueryResponseMessage createCQR() {
		return new CircuitGroupQueryResponseMessageImpl(providerImpl,_CQR_HOLDER.mandatoryCodes,_CQR_HOLDER.mandatoryVariableCodes,_CQR_HOLDER.optionalCodes,
				_CQR_HOLDER.mandatoryCodeToIndex,_CQR_HOLDER.mandatoryVariableCodeToIndex,_CQR_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createFAA()
	 */
	public FacilityAcceptedMessage createFAA() {
		return new FacilityAcceptedMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createFAC()
	 */
	public FacilityMessage createFAC() {
		return new FacilityMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createFAR()
	 */
	public FacilityRequestMessage createFAR() {
		return new FacilityRequestMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createFOT()
	 */
	public ForwardTransferMessage createFOT() {
		return new ForwardTransferMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createFRJ()
	 */
	public FacilityRejectedMessage createFRJ() {
		return new FacilityRejectedMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createGRA()
	 */
	public CircuitGroupResetAckMessage createGRA() {
		return new CircuitGroupResetAckMessageImpl(providerImpl,_GRA_HOLDER.mandatoryCodes,_GRA_HOLDER.mandatoryVariableCodes,_GRA_HOLDER.optionalCodes,
				_GRA_HOLDER.mandatoryCodeToIndex,_GRA_HOLDER.mandatoryVariableCodeToIndex,_GRA_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createGRS()
	 */
	public CircuitGroupResetMessage createGRS() {
		return new CircuitGroupResetMessageImpl(providerImpl,_GRS_HOLDER.mandatoryCodes,_GRS_HOLDER.mandatoryVariableCodes,_GRS_HOLDER.optionalCodes,
				_GRS_HOLDER.mandatoryCodeToIndex,_GRS_HOLDER.mandatoryVariableCodeToIndex,_GRS_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createIAM()
	 */
	public InitialAddressMessage createIAM() {
		return new InitialAddressMessageImpl(providerImpl,_IAM_HOLDER.mandatoryCodes,_IAM_HOLDER.mandatoryVariableCodes,_IAM_HOLDER.optionalCodes,
				_IAM_HOLDER.mandatoryCodeToIndex,_IAM_HOLDER.mandatoryVariableCodeToIndex,_IAM_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createIDR()
	 */
	public IdentificationRequestMessage createIDR() {
		return new IdentificationRequestMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createINF()
	 */
	public InformationMessage createINF() {
		return new InformationMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createINR()
	 */
	public InformationRequestMessage createINR() {
		return new InformationRequestMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createIRS()
	 */
	public IdentificationResponseMessage createIRS() {
		return new IdentificationResponseMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createLPA()
	 */
	public LoopbackAckMessage createLPA() {
		return new LoopbackAckMessageImpl(providerImpl,_LPA_HOLDER.mandatoryCodes,_LPA_HOLDER.mandatoryVariableCodes,_LPA_HOLDER.optionalCodes,
				_LPA_HOLDER.mandatoryCodeToIndex,_LPA_HOLDER.mandatoryVariableCodeToIndex,_LPA_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createLPP()
	 */
	public LoopPreventionMessage createLPP() {
		return new LoopPreventionMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createNRM()
	 */
	public NetworkResourceManagementMessage createNRM() {
		return new NetworkResourceManagementMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createOLM()
	 */
	public OverloadMessage createOLM() {
		return new OverloadMessageImpl(providerImpl,_OLM_HOLDER.mandatoryCodes,_OLM_HOLDER.mandatoryVariableCodes,_OLM_HOLDER.optionalCodes,
				_OLM_HOLDER.mandatoryCodeToIndex,_OLM_HOLDER.mandatoryVariableCodeToIndex,_OLM_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createPAM()
	 */
	public PassAlongMessage createPAM() {
		return new PassAlongMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createPRI()
	 */
	public PreReleaseInformationMessage createPRI() {
		return new PreReleaseInformationMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createREL()
	 */
	public ReleaseMessage createREL() {
		return new ReleaseMessageImpl(providerImpl,_REL_HOLDER.mandatoryCodes,_REL_HOLDER.mandatoryVariableCodes,_REL_HOLDER.optionalCodes,
				_REL_HOLDER.mandatoryCodeToIndex,_REL_HOLDER.mandatoryVariableCodeToIndex,_REL_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createRES()
	 */
	public ResumeMessage createRES() {
		return new ResumeMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createRLC()
	 */
	public ReleaseCompleteMessage createRLC() {
		return new ReleaseCompleteMessageImpl(providerImpl,_RLC_HOLDER.mandatoryCodes,_RLC_HOLDER.mandatoryVariableCodes,_RLC_HOLDER.optionalCodes,
				_RLC_HOLDER.mandatoryCodeToIndex,_RLC_HOLDER.mandatoryVariableCodeToIndex,_RLC_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createRSC()
	 */
	public ResetCircuitMessage createRSC() {
		return new ResetCircuitMessageImpl(providerImpl,_RSC_HOLDER.mandatoryCodes,_RSC_HOLDER.mandatoryVariableCodes,_RSC_HOLDER.optionalCodes,
				_RSC_HOLDER.mandatoryCodeToIndex,_RSC_HOLDER.mandatoryVariableCodeToIndex,_RSC_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createSAM()
	 */
	public SubsequentAddressMessage createSAM() {
		return new SubsequentAddressMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createSDN()
	 */
	public SubsequentDirectoryNumberMessage createSDN() {
		return new SubsequentDirectoryNumberMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createSGM()
	 */
	public SegmentationMessage createSGM() {
		return new SegmentationMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createSUS()
	 */
	public SuspendMessage createSUS() {
		return new SuspendMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createUBA()
	 */
	public UnblockingAckMessage createUBA() {
		return new UnblockingAckMessageImpl(providerImpl,_UBA_HOLDER.mandatoryCodes,_UBA_HOLDER.mandatoryVariableCodes,_UBA_HOLDER.optionalCodes,
				_UBA_HOLDER.mandatoryCodeToIndex,_UBA_HOLDER.mandatoryVariableCodeToIndex,_UBA_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createUBL()
	 */
	public UnblockingMessage createUBL() {
		return new UnblockingMessageImpl(providerImpl,_UBL_HOLDER.mandatoryCodes,_UBL_HOLDER.mandatoryVariableCodes,_UBL_HOLDER.optionalCodes,
				_UBL_HOLDER.mandatoryCodeToIndex,_UBL_HOLDER.mandatoryVariableCodeToIndex,_UBL_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createUCIC()
	 */
	public UnequippedCICMessage createUCIC() {
		return new UnequippedCICMessageImpl(providerImpl,_UCIC_HOLDER.mandatoryCodes,_UCIC_HOLDER.mandatoryVariableCodes,_UCIC_HOLDER.optionalCodes,
				_UCIC_HOLDER.mandatoryCodeToIndex,_UCIC_HOLDER.mandatoryVariableCodeToIndex,_UCIC_HOLDER.optionalCodeToIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createUPA()
	 */
	public UserPartAvailableMessage createUPA() {
		return new UserPartAvailableMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createUPT()
	 */
	public UserPartTestMessage createUPT() {
		return new UserPartTestMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createUSR()
	 */
	public User2UserInformationMessage createUSR() {
		return new User2UserInformationMessageImpl(providerImpl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.ss7.isup.ISUPMessageFactory#createCommand(int)
	 */
	public ISUPMessage createCommand(int commandCode) {
		switch (commandCode) {
		case ISUPMessage._MESSAGE_CODE_IAM:
			InitialAddressMessage IAM = createIAM();
			return IAM;
		case ISUPMessage._MESSAGE_CODE_ACM:
			AddressCompleteMessage ACM = createACM();
			return ACM;
		case ISUPMessage._MESSAGE_CODE_REL:
			ReleaseMessage REL = createREL();
			return REL;
		case ISUPMessage._MESSAGE_CODE_RLC:
			ReleaseCompleteMessage RLC = createRLC();
			return RLC;

		case ISUPMessage._MESSAGE_CODE_APT:
			ApplicationTransportMessage APT = createAPT();
			return APT;

		case ISUPMessage._MESSAGE_CODE_ANM:
			AnswerMessage ANM = createANM();
			return ANM;

		case ISUPMessage._MESSAGE_CODE_CPG:
			CallProgressMessage CPG = createCPG();
			return CPG;

		case ISUPMessage._MESSAGE_CODE_GRA:
			CircuitGroupResetAckMessage GRA = createGRA();
			return GRA;

		case ISUPMessage._MESSAGE_CODE_CFN:
			ConfusionMessage CFN = createCNF();
			return CFN;

		case ISUPMessage._MESSAGE_CODE_CON:
			ConnectMessage CON = createCON();
			return CON;

		case ISUPMessage._MESSAGE_CODE_COT:
			ContinuityMessage COT = createCOT();
			return COT;

		case ISUPMessage._MESSAGE_CODE_FRJ:
			FacilityRejectedMessage FRJ = createFRJ();
			return FRJ;

		case ISUPMessage._MESSAGE_CODE_INF:
			InformationMessage INF = createINF();
			return INF;

		case ISUPMessage._MESSAGE_CODE_INR:
			InformationRequestMessage INR = createINR();
			return INR;

		case ISUPMessage._MESSAGE_CODE_SAM:
			SubsequentAddressMessage SAM = createSAM();
			return SAM;

		case ISUPMessage._MESSAGE_CODE_SDN:
			SubsequentDirectoryNumberMessage SDN = createSDN();
			return SDN;

		case ISUPMessage._MESSAGE_CODE_FOT:
			ForwardTransferMessage FOT = createFOT();
			return FOT;

		case ISUPMessage._MESSAGE_CODE_RES:
			ResumeMessage RES = createRES();
			return RES;
		case ISUPMessage._MESSAGE_CODE_BLO:
			BlockingMessage BLO = createBLO();
			return BLO;

		case ISUPMessage._MESSAGE_CODE_BLA:
			BlockingAckMessage BLA = createBLA();
			return BLA;

		case ISUPMessage._MESSAGE_CODE_CCR:
			ContinuityCheckRequestMessage CCR = createCCR();
			return CCR;

		case ISUPMessage._MESSAGE_CODE_LPA:
			LoopbackAckMessage LPA = createLPA();
			return LPA;

		case ISUPMessage._MESSAGE_CODE_LPP:
			LoopPreventionMessage LPP = createLPP();
			return LPP;

		case ISUPMessage._MESSAGE_CODE_OLM:
			OverloadMessage OLM = createOLM();
			return OLM;

		case ISUPMessage._MESSAGE_CODE_SUS:
			SuspendMessage SUS = createSUS();
			return SUS;

		case ISUPMessage._MESSAGE_CODE_RSC:
			ResetCircuitMessage RSC = createRSC();
			return RSC;

		case ISUPMessage._MESSAGE_CODE_UBL:
			UnblockingMessage UBL = createUBL();
			return UBL;

		case ISUPMessage._MESSAGE_CODE_UBA:
			UnblockingAckMessage UBA = createUBA();
			return UBA;

		case ISUPMessage._MESSAGE_CODE_UCIC:
			UnequippedCICMessage UCIC = createUCIC();
			return UCIC;

		case ISUPMessage._MESSAGE_CODE_CGB:
			CircuitGroupBlockingMessage CGB = createCGB();
			return CGB;

		case ISUPMessage._MESSAGE_CODE_CGBA:
			CircuitGroupBlockingAckMessage CGBA = createCGBA();
			return CGBA;

		case ISUPMessage._MESSAGE_CODE_CGU:
			CircuitGroupUnblockingMessage CGU = createCGU();
			return CGU;

		case ISUPMessage._MESSAGE_CODE_CGUA:
			CircuitGroupUnblockingAckMessage CGUA = createCGUA();
			return CGUA;

		case ISUPMessage._MESSAGE_CODE_GRS:
			CircuitGroupResetMessage GRS = createGRS();
			return GRS;

		case ISUPMessage._MESSAGE_CODE_CQR:
			CircuitGroupQueryResponseMessage CQR = createCQR();
			return CQR;

		case ISUPMessage._MESSAGE_CODE_CQM:
			CircuitGroupQueryMessage CQM = createCQM();
			return CQM;

		case ISUPMessage._MESSAGE_CODE_FAA:
			FacilityAcceptedMessage FAA = createFAA();
			return FAA;

		case ISUPMessage._MESSAGE_CODE_FAR:
			FacilityRequestMessage FAR = createFAR();
			return FAR;

		case ISUPMessage._MESSAGE_CODE_PAM:
			PassAlongMessage PAM = createPAM();
			return PAM;

		case ISUPMessage._MESSAGE_CODE_PRI:
			PreReleaseInformationMessage PRI = createPRI();
			return PRI;

		case ISUPMessage._MESSAGE_CODE_FAC:
			FacilityMessage FAC = createFAC();
			return FAC;

		case ISUPMessage._MESSAGE_CODE_NRM:
			NetworkResourceManagementMessage NRM = createNRM();
			return NRM;

		case ISUPMessage._MESSAGE_CODE_IDR:
			IdentificationRequestMessage IDR = createIDR();
			return IDR;

		case ISUPMessage._MESSAGE_CODE_IRS:
			IdentificationResponseMessage IRS = createIRS();
			return IRS;

		case ISUPMessage._MESSAGE_CODE_SGM:
			SegmentationMessage SGM = createSGM();
			return SGM;

		case ISUPMessage._MESSAGE_CODE_CIM:
			ChargeInformationMessage CIM = createCIM();
			return CIM;

		case ISUPMessage._MESSAGE_CODE_UPA:
			UserPartAvailableMessage UPA = createUPA();
			return UPA;

		case ISUPMessage._MESSAGE_CODE_UPT:
			UserPartTestMessage UPT = createUPT();
			return UPT;

		case ISUPMessage._MESSAGE_CODE_USR:
			User2UserInformationMessage USR = createUSR();
			return USR;
		default:
			throw new IllegalArgumentException("Not supported comamnd code: " + commandCode);
		}
	}
	//private final static Map<Integer,MessageIndexingPlaceHolder> _COMMAND_CODE_2_COMMAND_INDEXES;
	
	
	//ACM
	private static final MessageIndexingPlaceHolder _ACM_HOLDER;
	//ANM
	private static final MessageIndexingPlaceHolder _ANM_HOLDER;
	//FIXME: APT
	//BLO
	private static final MessageIndexingPlaceHolder _BLO_HOLDER;
	//BLA
	private static final MessageIndexingPlaceHolder _BLA_HOLDER;
	//CPG
	private static final MessageIndexingPlaceHolder _CPG_HOLDER;
	//CGB
	private static final MessageIndexingPlaceHolder _CGB_HOLDER;
	//CGBA
	private static final MessageIndexingPlaceHolder _CGBA_HOLDER;
	//CQM
	private static final MessageIndexingPlaceHolder _CQM_HOLDER;
	//CQR
	private static final MessageIndexingPlaceHolder _CQR_HOLDER;
	//GRS
	private static final MessageIndexingPlaceHolder _GRS_HOLDER;
	//GRA
	private static final MessageIndexingPlaceHolder _GRA_HOLDER;
	//CGU
	private static final MessageIndexingPlaceHolder _CGU_HOLDER;
	//CGUA
	private static final MessageIndexingPlaceHolder _CGUA_HOLDER;
	//FIXME: CNF
	//FIXME: CON
	//FIXME: COT
	//CCR
	private static final MessageIndexingPlaceHolder _CCR_HOLDER;
	//FIXME: FAC
	//FIXME: FAA
	//FIXME: FRJ
	//FIXME: FAR
	//FIXME: FOT
	//FIXME: IDR
	//FIXME: IRS
	//FIXME: INF
	//IAM
	private static final MessageIndexingPlaceHolder _IAM_HOLDER;
	// LPA
	private static final MessageIndexingPlaceHolder _LPA_HOLDER;
	//FIXME: LPP
	//FIXME: NRM
	// OLM
	private static final MessageIndexingPlaceHolder _OLM_HOLDER;
	//FIXME: PAM
	//FIXME: PRI
	//REL
	private static final MessageIndexingPlaceHolder _REL_HOLDER;
	//RLC
	private static final MessageIndexingPlaceHolder _RLC_HOLDER;
	// RSC
	private static final MessageIndexingPlaceHolder _RSC_HOLDER;
	//FIXME: RES
	//FIXME: SGM
	//FIXME: SAM
	//FIXME: SDN
	//FIXME: SUS
	// UBL
	private static final MessageIndexingPlaceHolder _UBL_HOLDER;
	// UBA
	private static final MessageIndexingPlaceHolder _UBA_HOLDER;
	// UCIC
	private static final MessageIndexingPlaceHolder _UCIC_HOLDER;
	//FIXME: UPA
	//FIXME: UPT
	//FIXME: U2UI
	
	static
	{
		//Map<Integer,MessageIndexingPlaceHolder> _commandCode2CommandIndexes = new HashMap<Integer, MessageIndexingPlaceHolder>();
		Set<Integer> mandatoryCodes = new HashSet<Integer>();
		Set<Integer> mandatoryVariableCodes= new HashSet<Integer>();
		Set<Integer> optionalCodes= new HashSet<Integer>();

		Map<Integer, Integer> mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		Map<Integer, Integer> mandatoryVariableCodeToIndex= new HashMap<Integer, Integer>();
		Map<Integer, Integer> optionalCodeToIndex= new HashMap<Integer, Integer>();

		//ACM
		mandatoryCodes.add(BackwardCallIndicators._PARAMETER_CODE);
		mandatoryCodeToIndex.put(BackwardCallIndicators._PARAMETER_CODE,AddressCompleteMessageImpl._INDEX_F_BackwardCallIndicators);

		
		
		optionalCodes.add(OptionalBackwardCallIndicators._PARAMETER_CODE);
		optionalCodes.add(CallReference._PARAMETER_CODE);
		optionalCodes.add(CauseIndicators._PARAMETER_CODE);
		optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
		optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
		optionalCodes.add(AccessTransport._PARAMETER_CODE);
		optionalCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
		optionalCodes.add(TransmissionMediumUsed._PARAMETER_CODE);
		optionalCodes.add(EchoControlInformation._PARAMETER_CODE);
		optionalCodes.add(AccessDeliveryInformation._PARAMETER_CODE);
		optionalCodes.add(RedirectionNumber._PARAMETER_CODE);
		optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
		optionalCodes.add(CallDiversionInformation._PARAMETER_CODE);
		optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
		optionalCodes.add(RemoteOperations._PARAMETER_CODE);
		optionalCodes.add(ServiceActivation._PARAMETER_CODE);
		optionalCodes.add(RedirectionNumberRestriction._PARAMETER_CODE);
		optionalCodes.add(ConferenceTreatmentIndicators._PARAMETER_CODE);
		optionalCodes.add(UIDActionIndicators._PARAMETER_CODE);
		optionalCodes.add(ApplicationTransportParameter ._PARAMETER_CODE);
		optionalCodes.add(CCNRPossibleIndicator._PARAMETER_CODE);
		optionalCodes.add(HTRInformation._PARAMETER_CODE);
		optionalCodes.add(PivotRoutingBackwardInformation._PARAMETER_CODE);
		optionalCodes.add(RedirectStatus._PARAMETER_CODE);
		
		
		
		optionalCodeToIndex.put(OptionalBackwardCallIndicators._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_OptionalBakwardCallIndicators);
		optionalCodeToIndex.put(CallReference._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_CallReference);
		optionalCodeToIndex.put(CauseIndicators._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_CauseIndicators);
		optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_UserToUserIndicators);
		optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_UserToUserInformation);
		optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_AccessTransport);
		optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_GenericNotificationIndicator);
		optionalCodeToIndex.put(TransmissionMediumUsed._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_TransmissionMediumUsed);
		optionalCodeToIndex.put(EchoControlInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_EchoControlInformation);
		optionalCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_AccessDeliveryInformation);
		optionalCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_RedirectionNumber);
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_ParameterCompatibilityInformation);
		optionalCodeToIndex.put(CallDiversionInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_CallDiversionInformation);
		optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_NetworkSpecificFacility);
		optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_RemoteOperations);
		optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_ServiceActivation);
		optionalCodeToIndex.put(RedirectionNumberRestriction._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_RedirectionNumberRestriction);
		optionalCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
		optionalCodeToIndex.put(UIDActionIndicators._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_UIDActionIndicators);
		optionalCodeToIndex.put(ApplicationTransportParameter._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_ApplicationTransportParameter);
		optionalCodeToIndex.put(CCNRPossibleIndicator._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_CCNRPossibleIndicator);
		optionalCodeToIndex.put(HTRInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_HTRInformation);
		optionalCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_PivotRoutingBackwardInformation);
		optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_RedirectStatus);
		
		MessageIndexingPlaceHolder ACM_HOLDER = new MessageIndexingPlaceHolder();
		ACM_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_ACM;
		ACM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		ACM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		ACM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		ACM_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		ACM_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		ACM_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(ACM_HOLDER.commandCode, ACM_HOLDER);
		_ACM_HOLDER = ACM_HOLDER;
		
		
		//ANM
		optionalCodes.add(BackwardCallIndicators._PARAMETER_CODE);
		optionalCodes.add(OptionalBackwardCallIndicators._PARAMETER_CODE);
		optionalCodes.add(CallReference._PARAMETER_CODE);
		optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
		optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
		optionalCodes.add(ConnectedNumber._PARAMETER_CODE);
		optionalCodes.add(AccessTransport._PARAMETER_CODE);
		optionalCodes.add(AccessDeliveryInformation._PARAMETER_CODE);
		optionalCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
		optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
		optionalCodes.add(BackwardGVNS._PARAMETER_CODE);
		optionalCodes.add(CallHistoryInformation._PARAMETER_CODE);
		optionalCodes.add(GenericNumber._PARAMETER_CODE);
		optionalCodes.add(TransmissionMediumUsed._PARAMETER_CODE);
		optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
		optionalCodes.add(RemoteOperations._PARAMETER_CODE);
		optionalCodes.add(RedirectionNumber._PARAMETER_CODE);
		optionalCodes.add(ServiceActivation._PARAMETER_CODE);
		optionalCodes.add(EchoControlInformation._PARAMETER_CODE);
		optionalCodes.add(RedirectionNumberRestriction._PARAMETER_CODE);
		optionalCodes.add(DisplayInformation._PARAMETER_CODE);
		optionalCodes.add(ConferenceTreatmentIndicators._PARAMETER_CODE);
		optionalCodes.add(ApplicationTransportParameter._PARAMETER_CODE);
		optionalCodes.add(PivotRoutingBackwardInformation._PARAMETER_CODE);
		optionalCodes.add(RedirectStatus._PARAMETER_CODE);


		optionalCodeToIndex.put(BackwardCallIndicators._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_BackwardCallIndicators);
		optionalCodeToIndex.put(OptionalBackwardCallIndicators._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_OptionalBackwardCallIndicators);
		optionalCodeToIndex.put(CallReference._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_CallReference);
		optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_UserToUserIndicators);
		optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_UserToUserInformation);
		optionalCodeToIndex.put(ConnectedNumber._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_ConnectedNumber);
		optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_AccessTransport);
		optionalCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_AccessDeliveryInformation);
		optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_GenericNotificationIndicator);
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_ParameterCompatibilityInformation);
		optionalCodeToIndex.put(BackwardGVNS._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_BackwardGVNS);
		optionalCodeToIndex.put(CallHistoryInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_CallHistoryInformation);
		optionalCodeToIndex.put(GenericNumber._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_GenericNumber);
		optionalCodeToIndex.put(TransmissionMediumUsed._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_TransmissionMediumUsed);
		optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_NetworkSpecificFacility);
		optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_RemoteOperations);
		optionalCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_RedirectionNumber);
		optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_ServiceActivation);
		optionalCodeToIndex.put(EchoControlInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_EchoControlInformation);
		optionalCodeToIndex.put(RedirectionNumberRestriction._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_RedirectionNumberRestriction);
		optionalCodeToIndex.put(DisplayInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_DisplayInformation);
		optionalCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
		optionalCodeToIndex.put(ApplicationTransportParameter._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_ApplicationTransportParameter);
		optionalCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_PivotRoutingBackwardInformation);
		optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_RedirectStatus);
		
		
		MessageIndexingPlaceHolder ANM_HOLDER = new MessageIndexingPlaceHolder();
		ANM_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_ANM;
		ANM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		ANM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		ANM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		ANM_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		ANM_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		ANM_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(ANM_HOLDER.commandCode, ANM_HOLDER);
		_ANM_HOLDER = ANM_HOLDER;
		
		//FIXME: APT
		//BLO
		MessageIndexingPlaceHolder BLO_HOLDER = new MessageIndexingPlaceHolder();
		BLO_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_BLO;
		BLO_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		BLO_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		BLO_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		BLO_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		BLO_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		BLO_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(BLO_HOLDER.commandCode, BLO_HOLDER);
		_BLO_HOLDER = BLO_HOLDER;
		//BLA
		MessageIndexingPlaceHolder BLA_HOLDER = new MessageIndexingPlaceHolder();
		BLA_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_BLA;
		BLA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		BLA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		BLA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		BLA_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		BLA_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		BLA_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(BLA_HOLDER.commandCode, BLA_HOLDER);
		_BLA_HOLDER = BLA_HOLDER;

		//CPG
		mandatoryCodes.add(EventInformation._PARAMETER_CODE);
		mandatoryCodeToIndex.put(EventInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_F_EventInformation);

		optionalCodes.add(CauseIndicators._PARAMETER_CODE);
		optionalCodes.add(CallReference._PARAMETER_CODE);
		optionalCodes.add(BackwardCallIndicators._PARAMETER_CODE);
		optionalCodes.add(OptionalBackwardCallIndicators._PARAMETER_CODE);
		optionalCodes.add(AccessTransport._PARAMETER_CODE);
		optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
		optionalCodes.add(RedirectionNumber._PARAMETER_CODE);
		optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
		optionalCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
		optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
		optionalCodes.add(RemoteOperations._PARAMETER_CODE);
		optionalCodes.add(TransmissionMediumUsed._PARAMETER_CODE);
		optionalCodes.add(AccessDeliveryInformation._PARAMETER_CODE);
		optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
		optionalCodes.add(CallDiversionInformation._PARAMETER_CODE);
		optionalCodes.add(ServiceActivation._PARAMETER_CODE);
		optionalCodes.add(RedirectionNumberRestriction._PARAMETER_CODE);
		optionalCodes.add(CallTransferNumber._PARAMETER_CODE);
		optionalCodes.add(EchoControlInformation._PARAMETER_CODE);
		optionalCodes.add(ConnectedNumber._PARAMETER_CODE);
		optionalCodes.add(BackwardGVNS._PARAMETER_CODE);
		optionalCodes.add(GenericNumber._PARAMETER_CODE);
		optionalCodes.add(CallHistoryInformation._PARAMETER_CODE);
		optionalCodes.add(ConferenceTreatmentIndicators._PARAMETER_CODE);
		optionalCodes.add(UIDActionIndicators._PARAMETER_CODE);
		optionalCodes.add(ApplicationTransportParameter._PARAMETER_CODE);
		optionalCodes.add(CCNRPossibleIndicator._PARAMETER_CODE);
		optionalCodes.add(PivotRoutingBackwardInformation._PARAMETER_CODE);
		optionalCodes.add(RedirectStatus._PARAMETER_CODE);

		optionalCodeToIndex.put(CauseIndicators._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CauseIndicators);
		optionalCodeToIndex.put(CallReference._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CallReference);
		optionalCodeToIndex.put(BackwardCallIndicators._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_BackwardCallIndicators);
		optionalCodeToIndex.put(OptionalBackwardCallIndicators._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_OptionalBackwardCallIndicators);
		optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_AccessTransport);
		optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_UserToUserIndicators);
		optionalCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_RedirectionNumber);
		optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_UserToUserInformation);
		optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_GenericNotificationIndicator);
		optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_NetworkSpecificFacility);
		optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_RemoteOperations);
		optionalCodeToIndex.put(TransmissionMediumUsed._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_TransmissionMediumUsed);
		optionalCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_AccessDeliveryInformation);
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_ParameterCompatibilityInformation);
		optionalCodeToIndex.put(CallDiversionInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CallDiversionInformation);
		optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_ServiceActivation);
		optionalCodeToIndex.put(RedirectionNumberRestriction._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_RedirectionNumberRestriction);
		optionalCodeToIndex.put(CallTransferNumber._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CallTransferNumber);
		optionalCodeToIndex.put(EchoControlInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_EchoControlInformation);
		optionalCodeToIndex.put(ConnectedNumber._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_ConnectedNumber);
		optionalCodeToIndex.put(BackwardGVNS._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_BackwardGVNS);
		optionalCodeToIndex.put(GenericNumber._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_GenericNumber);
		optionalCodeToIndex.put(CallHistoryInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CallHistoryInformation);
		optionalCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
		optionalCodeToIndex.put(UIDActionIndicators._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_UIDActionIndicators);
		optionalCodeToIndex.put(ApplicationTransportParameter._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_ApplicationTransportParameter);
		optionalCodeToIndex.put(CCNRPossibleIndicator._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CCNRPossibleIndicator);
		optionalCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_PivotRoutingBackwardInformation);
		optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_RedirectStatus);
		
		
		
		MessageIndexingPlaceHolder CPG_HOLDER = new MessageIndexingPlaceHolder();
		CPG_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_CPG;
		CPG_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CPG_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CPG_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CPG_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		CPG_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CPG_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(CPG_HOLDER.commandCode, CPG_HOLDER);
		_CPG_HOLDER = CPG_HOLDER;

		
		//CGB
		mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
		mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE, CircuitGroupBlockingMessageImpl._INDEX_F_CircuitGroupSupervisionMessageType);

		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupBlockingMessageImpl._INDEX_V_RangeAndStatus);
		
		MessageIndexingPlaceHolder CGB_HOLDER = new MessageIndexingPlaceHolder();
		CGB_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_CGB;
		CGB_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CGB_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CGB_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CGB_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		CGB_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CGB_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(CGB_HOLDER.commandCode, CGB_HOLDER);
		_CGB_HOLDER = CGB_HOLDER;
		
		//CGBA
		mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
		mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE, CircuitGroupBlockingAckMessageImpl._INDEX_F_CircuitGroupSupervisionMessageType);

		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupBlockingAckMessageImpl._INDEX_V_RangeAndStatus);
		
		MessageIndexingPlaceHolder CGBA_HOLDER = new MessageIndexingPlaceHolder();
		CGBA_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_CGBA;
		CGBA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CGBA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CGBA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CGBA_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		CGBA_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CGBA_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(CGBA_HOLDER.commandCode, CGBA_HOLDER);
		_CGBA_HOLDER = CGBA_HOLDER;
		
		//CQM
		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupQueryMessageImpl._INDEX_V_RangeAndStatus);
		
		MessageIndexingPlaceHolder CQM_HOLDER = new MessageIndexingPlaceHolder();
		CQM_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_CQM;
		CQM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CQM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CQM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CQM_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		CQM_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CQM_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(CQM_HOLDER.commandCode, CQM_HOLDER);
		_CQM_HOLDER = CQM_HOLDER;
		
		//CQR
		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodes.add(CircuitStateIndicator._PARAMETER_CODE);

		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupQueryResponseMessageImpl._INDEX_V_RangeAndStatus);
		mandatoryVariableCodeToIndex.put(CircuitStateIndicator._PARAMETER_CODE, CircuitGroupQueryResponseMessageImpl._INDEX_V_CircuitStateIndicator);
		
		MessageIndexingPlaceHolder CQR_HOLDER = new MessageIndexingPlaceHolder();
		CQR_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_CQR;
		CQR_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CQR_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CQR_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CQR_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		CQR_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CQR_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(CQR_HOLDER.commandCode, CQR_HOLDER);
		_CQR_HOLDER = CQR_HOLDER;
		
		//GRS
		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupResetMessageImpl._INDEX_V_RangeAndStatus);
		
		
		MessageIndexingPlaceHolder GRS_HOLDER = new MessageIndexingPlaceHolder();
		GRS_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_GRS;
		GRS_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		GRS_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		GRS_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		GRS_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		GRS_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		GRS_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(GRS_HOLDER.commandCode, GRS_HOLDER);
		_GRS_HOLDER = GRS_HOLDER;
		
		
		
		//GRA
		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupResetAckMessageImpl._INDEX_V_RangeAndStatus);
		
		
		MessageIndexingPlaceHolder GRA_HOLDER = new MessageIndexingPlaceHolder();
		GRA_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_GRA;
		GRA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		GRA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		GRA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		GRA_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		GRA_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		GRA_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(GRA_HOLDER.commandCode, GRA_HOLDER);
		_GRA_HOLDER = GRA_HOLDER;
		
		
		//CGU
		mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
		mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE, CircuitGroupUnblockingMessageImpl._INDEX_F_CircuitGroupSupervisionMessageType);

		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupUnblockingMessageImpl._INDEX_V_RangeAndStatus);
		
		MessageIndexingPlaceHolder CGU_HOLDER = new MessageIndexingPlaceHolder();
		CGU_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_CGU;
		CGU_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CGU_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CGU_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CGU_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		CGU_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CGU_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(CGU_HOLDER.commandCode, CGU_HOLDER);
		_CGU_HOLDER = CGU_HOLDER;
		
		
		//CGUA
		mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
		mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE, CircuitGroupUnblockingAckMessageImpl._INDEX_F_CircuitGroupSupervisionMessageType);

		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupUnblockingAckMessageImpl._INDEX_V_RangeAndStatus);
		
		MessageIndexingPlaceHolder CGUA_HOLDER = new MessageIndexingPlaceHolder();
		CGUA_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_CGUA;
		CGUA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CGUA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CGUA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CGUA_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		CGUA_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CGUA_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(CGUA_HOLDER.commandCode, CGUA_HOLDER);
		_CGUA_HOLDER = CGUA_HOLDER;
		
		//FIXME: CNF
		//FIXME: CON
		//FIXME: COT
		// CCR
		MessageIndexingPlaceHolder CCR_HOLDER = new MessageIndexingPlaceHolder();
		CCR_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_CCR;
		CCR_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CCR_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CCR_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CCR_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		CCR_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CCR_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(CCR_HOLDER.commandCode, CCR_HOLDER);
		_CCR_HOLDER = CCR_HOLDER;
		//FIXME: FAC
		//FIXME: FAA
		//FIXME: FRJ
		//FIXME: FAR
		//FIXME: FOT
		//FIXME: IDR
		//FIXME: IRS
		//FIXME: INF
		
		//IAM
		mandatoryCodes.add(NatureOfConnectionIndicators._PARAMETER_CODE);
		mandatoryCodes.add(ForwardCallIndicators._PARAMETER_CODE);
		mandatoryCodes.add(CallingPartyCategory._PARAMETER_CODE);
		mandatoryCodes.add(TransmissionMediumRequirement._PARAMETER_CODE);

		mandatoryCodeToIndex.put(NatureOfConnectionIndicators._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_F_NatureOfConnectionIndicators);
		mandatoryCodeToIndex.put(ForwardCallIndicators._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_F_NatureOfConnectionIndicators);
		mandatoryCodeToIndex.put(CallingPartyCategory._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_F_CallingPartyCategory);
		mandatoryCodeToIndex.put(TransmissionMediumRequirement._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_F_TransmissionMediumRequirement);

		mandatoryVariableCodes.add(CalledPartyNumber._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(CalledPartyNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_V_CalledPartyNumber);

		optionalCodes.add(TransitNetworkSelection._PARAMETER_CODE);
		optionalCodes.add(CallReference._PARAMETER_CODE);
		optionalCodes.add(CallingPartyNumber._PARAMETER_CODE);
		optionalCodes.add(OptionalForwardCallIndicators._PARAMETER_CODE);
		optionalCodes.add(RedirectingNumber._PARAMETER_CODE);
		optionalCodes.add(RedirectionInformation._PARAMETER_CODE);
		optionalCodes.add(ClosedUserGroupInterlockCode._PARAMETER_CODE);
		optionalCodes.add(ConnectionRequest._PARAMETER_CODE);
		optionalCodes.add(OriginalCalledNumberImpl._PARAMETER_CODE);
		optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
		optionalCodes.add(AccessTransport._PARAMETER_CODE);
		optionalCodes.add(UserServiceInformation._PARAMETER_CODE);
		optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
		optionalCodes.add(GenericNumber._PARAMETER_CODE);
		optionalCodes.add(PropagationDelayCounter._PARAMETER_CODE);
		optionalCodes.add(UserServiceInformationPrime._PARAMETER_CODE);
		optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
		optionalCodes.add(GenericDigits._PARAMETER_CODE);
		optionalCodes.add(OriginatingISCPointCode._PARAMETER_CODE);
		optionalCodes.add(UserTeleserviceInformation._PARAMETER_CODE);
		optionalCodes.add(RemoteOperations._PARAMETER_CODE);
		optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
		optionalCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
		optionalCodes.add(ServiceActivation._PARAMETER_CODE);
		optionalCodes.add(GenericReference._PARAMETER_CODE);
		optionalCodes.add(MLPPPrecedence._PARAMETER_CODE);
		optionalCodes.add(TransimissionMediumRequierementPrime._PARAMETER_CODE);
		optionalCodes.add(LocationNumber._PARAMETER_CODE);
		optionalCodes.add(ForwardGVNS._PARAMETER_CODE);
		optionalCodes.add(CCSS._PARAMETER_CODE);
		optionalCodes.add(NetworkManagementControls._PARAMETER_CODE);

		optionalCodeToIndex.put(TransitNetworkSelection._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_TransitNetworkSelection);
		optionalCodeToIndex.put(CallReference._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_CallReference);
		optionalCodeToIndex.put(CallingPartyNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_CallingPartyNumber);
		optionalCodeToIndex.put(OptionalForwardCallIndicators._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_OptionalForwardCallIndicators);
		optionalCodeToIndex.put(RedirectingNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_RedirectingNumber);
		optionalCodeToIndex.put(RedirectionInformation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_RedirectionInformation);
		optionalCodeToIndex.put(ClosedUserGroupInterlockCode._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_ClosedUserGroupInterlockCode);
		optionalCodeToIndex.put(ConnectionRequest._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_ConnectionRequest);
		optionalCodeToIndex.put(OriginalCalledNumberImpl._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_OriginalCalledNumber);
		optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_UserToUserInformation);
		optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_AccessTransport);
		optionalCodeToIndex.put(UserServiceInformation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_UserServiceInformation);
		optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_User2UIndicators);
		optionalCodeToIndex.put(GenericNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_GenericNumber);
		optionalCodeToIndex.put(PropagationDelayCounter._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_PropagationDelayCounter);
		optionalCodeToIndex.put(UserServiceInformationPrime._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_UserServiceInformationPrime);
		optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_NetworkSPecificFacility);
		optionalCodeToIndex.put(GenericDigits._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_GenericDigits);
		optionalCodeToIndex.put(OriginatingISCPointCode._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_OriginatingISCPointCode);
		optionalCodeToIndex.put(UserTeleserviceInformation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_UserTeleserviceInformation);
		optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_RemoteOperations);
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_ParameterCompatibilityInformation);
		optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_GenericNotificationIndicator);
		optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_ServiceActivation);
		optionalCodeToIndex.put(GenericReference._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_GenericReference);
		optionalCodeToIndex.put(MLPPPrecedence._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_MLPPPrecedence);
		optionalCodeToIndex.put(TransimissionMediumRequierementPrime._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_TransimissionMediumRequierementPrime);
		optionalCodeToIndex.put(LocationNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_LocationNumber);
		optionalCodeToIndex.put(ForwardGVNS._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_ForwardGVNS);
		optionalCodeToIndex.put(CCSS._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_CCSS);
		optionalCodeToIndex.put(NetworkManagementControls._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_NetworkManagementControls);

		
		MessageIndexingPlaceHolder IAM_HOLDER = new MessageIndexingPlaceHolder();
		IAM_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_IAM;
		IAM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		IAM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		IAM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		IAM_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		IAM_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		IAM_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(IAM_HOLDER.commandCode, IAM_HOLDER);
		_IAM_HOLDER = IAM_HOLDER;
		
		
		// LPA
		MessageIndexingPlaceHolder LPA_HOLDER = new MessageIndexingPlaceHolder();
		LPA_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_LPA;
		LPA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		LPA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		LPA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		LPA_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		LPA_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		LPA_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(LPA_HOLDER.commandCode, LPA_HOLDER);
		_LPA_HOLDER = LPA_HOLDER;
		//FIXME: LPP
		//FIXME: NRM
		// OLM
		MessageIndexingPlaceHolder OLM_HOLDER = new MessageIndexingPlaceHolder();
		OLM_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_OLM;
		OLM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		OLM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		OLM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		OLM_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		OLM_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		OLM_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(OLM_HOLDER.commandCode, OLM_HOLDER);
		_OLM_HOLDER = OLM_HOLDER;
		//FIXME: PAM
		//FIXME: PRI
		
		
		//REL
		mandatoryVariableCodes.add(CauseIndicators._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(CauseIndicators._PARAMETER_CODE, ReleaseMessageImpl._INDEX_V_CauseIndicators);

		optionalCodes.add(RedirectionInformation._PARAMETER_CODE);
		optionalCodes.add(RedirectionNumber._PARAMETER_CODE);
		optionalCodes.add(AccessTransport._PARAMETER_CODE);
		optionalCodes.add(SignalingPointCode._PARAMETER_CODE);
		optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
		optionalCodes.add(AutomaticCongestionLevel._PARAMETER_CODE);
		optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
		optionalCodes.add(AccessDeliveryInformation._PARAMETER_CODE);
		optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
		optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
		optionalCodes.add(DisplayInformation._PARAMETER_CODE);
		optionalCodes.add(RemoteOperations._PARAMETER_CODE);
		optionalCodes.add(HTRInformation._PARAMETER_CODE);
		optionalCodes.add(RedirectCounter._PARAMETER_CODE);
		optionalCodes.add(RedirectBackwardInformation._PARAMETER_CODE);

		optionalCodeToIndex.put(RedirectionInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_RedirectionInformation);
		optionalCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_RedirectionNumber);
		optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_AccessTransport);
		optionalCodeToIndex.put(SignalingPointCode._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_SignalingPointCode);
		optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_U2UInformation);
		optionalCodeToIndex.put(AutomaticCongestionLevel._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_AutomaticCongestionLevel);
		optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_NetworkSpecificFacility);
		optionalCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_AccessDeliveryInformation);
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_ParameterCompatibilityInformation);
		optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_U2UIndicators);
		optionalCodeToIndex.put(DisplayInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_DisplayInformation);
		optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_RemoteOperations);
		optionalCodeToIndex.put(HTRInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_HTRInformation);
		optionalCodeToIndex.put(RedirectCounter._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_RedirectCounter);
		optionalCodeToIndex.put(RedirectBackwardInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_RedirectBackwardInformation);
		
		MessageIndexingPlaceHolder REL_HOLDER = new MessageIndexingPlaceHolder();
		REL_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_REL;
		REL_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		REL_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		REL_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		REL_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		REL_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		REL_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(REL_HOLDER.commandCode, REL_HOLDER);
		_REL_HOLDER = REL_HOLDER;
		
		
		//RLC
		optionalCodes.add(CauseIndicators._PARAMETER_CODE);
		optionalCodeToIndex.put(CauseIndicators._PARAMETER_CODE, ReleaseCompleteMessageImpl._INDEX_O_CauseIndicators);
		
		MessageIndexingPlaceHolder RLC_HOLDER = new MessageIndexingPlaceHolder();
		RLC_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_RLC;
		RLC_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		RLC_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		RLC_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		RLC_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		RLC_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		RLC_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(RLC_HOLDER.commandCode, RLC_HOLDER);
		_RLC_HOLDER = RLC_HOLDER;
		

		// RSC
		MessageIndexingPlaceHolder RSC_HOLDER = new MessageIndexingPlaceHolder();
		RSC_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_RSC;
		RSC_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		RSC_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		RSC_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		RSC_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		RSC_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		RSC_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(RSC_HOLDER.commandCode, RSC_HOLDER);
		_RSC_HOLDER = RSC_HOLDER;
		//FIXME: RES
		//FIXME: SGM
		//FIXME: SAM
		//FIXME: SDN
		//FIXME: SUS
		// UBL
		MessageIndexingPlaceHolder UBL_HOLDER = new MessageIndexingPlaceHolder();
		UBL_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_UBL;
		UBL_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		UBL_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		UBL_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		UBL_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		UBL_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		UBL_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(UBL_HOLDER.commandCode, UBL_HOLDER);
		_UBL_HOLDER = UBL_HOLDER;
		// UBA
		MessageIndexingPlaceHolder UBA_HOLDER = new MessageIndexingPlaceHolder();
		UBA_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_UBA;
		UBA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		UBA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		UBA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		UBA_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		UBA_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		UBA_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(UBA_HOLDER.commandCode, UBA_HOLDER);
		_UBA_HOLDER = UBA_HOLDER;
		// UCIC
		MessageIndexingPlaceHolder UCIC_HOLDER = new MessageIndexingPlaceHolder();
		UCIC_HOLDER.commandCode = ISUPMessage._MESSAGE_CODE_UCIC;
		UCIC_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		UCIC_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		UCIC_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		UCIC_HOLDER.mandatoryCodeToIndex=Collections.unmodifiableMap(mandatoryCodeToIndex);
		UCIC_HOLDER.mandatoryVariableCodeToIndex=Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		UCIC_HOLDER.optionalCodeToIndex=Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes= new HashSet<Integer>();
		optionalCodes= new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		//_commandCode2CommandIndexes.put(UCIC_HOLDER.commandCode, UCIC_HOLDER);
		_UCIC_HOLDER = UCIC_HOLDER;
		//FIXME: UPA
		//FIXME: UPT
		//FIXME: U2UI
		//_COMMAND_CODE_2_COMMAND_INDEXES = Collections.unmodifiableMap(_commandCode2CommandIndexes);
	}
	
	
	
	private static class MessageIndexingPlaceHolder
	{
		int commandCode;
		// magic
		Set<Integer> mandatoryCodes;
		Set<Integer> mandatoryVariableCodes;
		Set<Integer> optionalCodes;

		Map<Integer, Integer> mandatoryCodeToIndex;
		Map<Integer, Integer> mandatoryVariableCodeToIndex;
		Map<Integer, Integer> optionalCodeToIndex;
		
		
	}
}
