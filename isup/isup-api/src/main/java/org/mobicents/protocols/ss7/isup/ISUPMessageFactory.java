/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
import org.mobicents.protocols.ss7.isup.message.UserToUserInformationMessage;
import org.mobicents.protocols.ss7.isup.message.UserPartAvailableMessage;
import org.mobicents.protocols.ss7.isup.message.UserPartTestMessage;

/**
 * Start time:12:04:59 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ISUPMessageFactory {

    AddressCompleteMessage createACM();

    AddressCompleteMessage createACM(int cic);

    AnswerMessage createANM();

    AnswerMessage createANM(int cic);

    ApplicationTransportMessage createAPT();

    ApplicationTransportMessage createAPT(int cic);

    BlockingAckMessage createBLA();

    BlockingAckMessage createBLA(int cic);

    BlockingMessage createBLO();

    BlockingMessage createBLO(int cic);

    ContinuityCheckRequestMessage createCCR();

    ContinuityCheckRequestMessage createCCR(int cic);

    CircuitGroupBlockingMessage createCGB();

    CircuitGroupBlockingMessage createCGB(int cic);

    CircuitGroupBlockingAckMessage createCGBA();

    CircuitGroupBlockingAckMessage createCGBA(int cic);

    CircuitGroupUnblockingMessage createCGU();

    CircuitGroupUnblockingMessage createCGU(int cic);

    CircuitGroupUnblockingAckMessage createCGUA();

    CircuitGroupUnblockingAckMessage createCGUA(int cic);

    ChargeInformationMessage createCIM();

    ChargeInformationMessage createCIM(int cic);

    ConfusionMessage createCNF();

    ConfusionMessage createCNF(int cic);

    ISUPMessage createCommand(int commandCode);

    ISUPMessage createCommand(int commandCode, int cic);

    ConnectMessage createCON();

    ConnectMessage createCON(int cic);

    ContinuityMessage createCOT();

    ContinuityMessage createCOT(int cic);

    CallProgressMessage createCPG();

    CallProgressMessage createCPG(int cic);

    CircuitGroupQueryMessage createCQM();

    CircuitGroupQueryMessage createCQM(int cic);

    CircuitGroupQueryResponseMessage createCQR();

    CircuitGroupQueryResponseMessage createCQR(int cic);

    FacilityAcceptedMessage createFAA();

    FacilityAcceptedMessage createFAA(int cic);

    FacilityMessage createFAC();

    FacilityMessage createFAC(int cic);

    FacilityRequestMessage createFAR();

    FacilityRequestMessage createFAR(int cic);

    ForwardTransferMessage createFOT();

    ForwardTransferMessage createFOT(int cic);

    FacilityRejectedMessage createFRJ();

    FacilityRejectedMessage createFRJ(int cic);

    CircuitGroupResetAckMessage createGRA();

    CircuitGroupResetAckMessage createGRA(int cic);

    CircuitGroupResetMessage createGRS();

    CircuitGroupResetMessage createGRS(int cic);

    InitialAddressMessage createIAM();

    InitialAddressMessage createIAM(int cic);

    IdentificationRequestMessage createIDR();

    IdentificationRequestMessage createIDR(int cic);

    InformationMessage createINF();

    InformationMessage createINF(int cic);

    InformationRequestMessage createINR();

    InformationRequestMessage createINR(int cic);

    IdentificationResponseMessage createIRS();

    IdentificationResponseMessage createIRS(int cic);

    LoopbackAckMessage createLPA();

    LoopbackAckMessage createLPA(int cic);

    LoopPreventionMessage createLPP();

    LoopPreventionMessage createLPP(int cic);

    NetworkResourceManagementMessage createNRM();

    NetworkResourceManagementMessage createNRM(int cic);

    OverloadMessage createOLM();

    OverloadMessage createOLM(int cic);

    PassAlongMessage createPAM();

    PassAlongMessage createPAM(int cic);

    PreReleaseInformationMessage createPRI();

    PreReleaseInformationMessage createPRI(int cic);

    ReleaseMessage createREL();

    ReleaseMessage createREL(int cic);

    ResumeMessage createRES();

    ResumeMessage createRES(int cic);

    ReleaseCompleteMessage createRLC();

    ReleaseCompleteMessage createRLC(int cic);

    ResetCircuitMessage createRSC();

    ResetCircuitMessage createRSC(int cic);

    SubsequentAddressMessage createSAM();

    SubsequentAddressMessage createSAM(int cic);

    SubsequentDirectoryNumberMessage createSDN();

    SubsequentDirectoryNumberMessage createSDN(int cic);

    SegmentationMessage createSGM();

    SegmentationMessage createSGM(int cic);

    SuspendMessage createSUS();

    SuspendMessage createSUS(int cic);

    UnblockingAckMessage createUBA();

    UnblockingAckMessage createUBA(int cic);

    UnblockingMessage createUBL();

    UnblockingMessage createUBL(int cic);

    UnequippedCICMessage createUCIC();

    UnequippedCICMessage createUCIC(int cic);

    UserPartAvailableMessage createUPA();

    UserPartAvailableMessage createUPA(int cic);

    UserPartTestMessage createUPT();

    UserPartTestMessage createUPT(int cic);

    UserToUserInformationMessage createUSR();

    UserToUserInformationMessage createUSR(int cic);

}
