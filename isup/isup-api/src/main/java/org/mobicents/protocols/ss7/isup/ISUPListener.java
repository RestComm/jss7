/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
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
 *
 * @author kulikov
 * @author baranowb
 */
public interface ISUPListener {
	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_IAM}
	 * 
	 * @param ANM
	 */
	public void onIAM(InitialAddressMessage IAM);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_ACM}
	 * 
	 * @param ANM
	 */
	public void onACM(AddressCompleteMessage ACM);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_RLN}
	 * 
	 * @param ANM
	 */
	public void onRLC(ReleaseCompleteMessage RLC);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_REL}
	 * 
	 * @param ANM
	 */
	public void onREL(ReleaseMessage REL);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_APT}
	 * 
	 * @param APT
	 */
	public void onAPT(ApplicationTransportMessage APT);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_ANM}
	 * 
	 * @param ANM
	 */
	public void onANM(AnswerMessage ANM);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_CPG}
	 * 
	 * @param CPG
	 */
	public void onCPG(CallProgressMessage CPG);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_CQR}
	 * 
	 * @param CQR
	 */
	public void onCQR(CircuitGroupQueryResponseMessage CQR);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_GRA}
	 * 
	 * @param GRA
	 */
	public void onGRA(CircuitGroupResetAckMessage GRA);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_CNF}
	 * 
	 * @param CNF
	 */
	public void onCFN(ConfusionMessage CNF);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_CON}
	 * 
	 * @param CON
	 */
	public void onCON(ConnectMessage CON);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_COT}
	 * 
	 * @param COT
	 */
	public void onCOT(ContinuityMessage COT);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_FRJ}
	 * 
	 * @param FRJ
	 */
	public void onFRJ(FacilityRejectedMessage FRJ);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_INF}
	 * 
	 * @param INF
	 */
	public void onINF(InformationMessage INF);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_INR}
	 * 
	 * @param INR
	 */
	public void onINR(InformationRequestMessage INR);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_SAM}
	 * 
	 * @param SAM
	 */
	public void onSAM(SubsequentAddressMessage SAM);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_SDN}
	 * 
	 * @param SDN
	 */
	public void onSDN(SubsequentDirectoryNumberMessage SDN);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_FOT}
	 * 
	 * @param FOT
	 */
	public void onFOT(ForwardTransferMessage FOT);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_RES}
	 * 
	 * @param RES
	 */
	public void onRES(ResumeMessage RES);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_BLO}
	 * 
	 * @param BLO
	 */
	public void onBLO(BlockingMessage BLO);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_BLA}
	 * 
	 * @param BLA
	 */
	public void onBLA(BlockingAckMessage BLA);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_CCR}
	 * 
	 * @param CCR
	 */
	public void onCCR(ContinuityCheckRequestMessage CCR);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_LPA}
	 * 
	 * @param LPA
	 */
	public void onLPA(LoopbackAckMessage LPA);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_LPP}
	 * 
	 * @param LPP
	 */
	public void onLPP(LoopPreventionMessage LPP);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_OLM}
	 * 
	 * @param OLM
	 */
	public void onOLM(OverloadMessage OLM);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_SUS}
	 * 
	 * @param SUS
	 */
	public void onSUS(SuspendMessage SUS);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_RSC}
	 * 
	 * @param RSC
	 */
	public void onRSC(ResetCircuitMessage RSC);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_UBL}
	 * 
	 * @param UBL
	 */
	public void onUBL(UnblockingMessage UBL);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_UBA}
	 * 
	 * @param UBA
	 */
	public void onUBA(UnblockingAckMessage UBA);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_UCIC}
	 * 
	 * @param UCIC
	 */
	public void onUCIC(UnequippedCICMessage UCIC);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_CGB}
	 * 
	 * @param CGB
	 */
	public void onCGB(CircuitGroupBlockingMessage CGB);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_CGBA}
	 * 
	 * @param CGBA
	 */
	public void onCGBA(CircuitGroupBlockingAckMessage CGBA);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_CGU}
	 * 
	 * @param CGU
	 */
	public void onCGU(CircuitGroupUnblockingMessage CGU);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_CGUA}
	 * 
	 * @param CGUA
	 */
	public void onCGUA(CircuitGroupUnblockingAckMessage CGUA);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_GRS}
	 * 
	 * @param GRS
	 */
	public void onGRS(CircuitGroupResetMessage GRS);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_CQM}
	 * 
	 * @param CQRM
	 */
	public void onCQM(CircuitGroupQueryMessage CQM);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_FAA}
	 * 
	 * @param FAA
	 */
	public void onFAA(FacilityAcceptedMessage FAA);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_FAR}
	 * 
	 * @param FAR
	 */
	public void onFAR(FacilityRequestMessage FAR);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_PAM}
	 * 
	 * @param PAM
	 */
	public void onPAM(PassAlongMessage PAM);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_PRI}
	 * 
	 * @param PRI
	 */
	public void onPRI(PreReleaseInformationMessage PRI);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_FAC}
	 * 
	 * @param FAC
	 */
	public void onFAC(FacilityMessage FAC);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_NRM}
	 * 
	 * @param NRM
	 */
	public void onNRM(NetworkResourceManagementMessage NRM);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_IDR}
	 * 
	 * @param IDR
	 */
	public void onIDR(IdentificationRequestMessage IDR);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_IRS}
	 * 
	 * @param IRS
	 */
	public void onIRS(IdentificationResponseMessage IRS);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_SGM}
	 * 
	 * @param SGM
	 */
	public void onSGM(SegmentationMessage SGM);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_CIM}
	 * 
	 * @param CIM
	 */
	public void onCIM(ChargeInformationMessage CIM);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_UPA}
	 * 
	 * @param UPA
	 */
	public void onUPA(UserPartAvailableMessage UPA);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_UPT}
	 * 
	 * @param UPT
	 */
	public void onUPT(UserPartTestMessage UPT);

	/**
	 * receiver for {@link ISUPMessage#_MESSAGE_CODE_USR}
	 * 
	 * @param USR
	 */
	public void onUSR(User2UserInformationMessage USR);

	// etc

	public void onTransactionTimeout(ISUPClientTransaction tx);

	public void onTransactionTimeout(ISUPServerTransaction tx);

	public void onTransactionEnded(ISUPClientTransaction tx);

	public void onTransactionEnded(ISUPServerTransaction tx);
}
