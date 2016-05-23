/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.sccp.impl;

import java.io.IOException;
import java.util.Map;

import javolution.util.FastMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.LongMessageRule;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.Rule;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.impl.message.EncodingResultData;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpAddressedMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpNoticeMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCause;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class SccpRoutingControl {
    private static final Logger logger = Logger.getLogger(SccpRoutingControl.class);

    private SccpStackImpl sccpStackImpl = null;
    private SccpProviderImpl sccpProviderImpl = null;

    private SccpManagement sccpManagement = null;

    private MessageFactoryImpl messageFactory;

    public SccpRoutingControl(SccpProviderImpl sccpProviderImpl, SccpStackImpl sccpStackImpl) {
        this.messageFactory = sccpStackImpl.messageFactory;
        this.sccpProviderImpl = sccpProviderImpl;
        this.sccpStackImpl = sccpStackImpl;
    }

    public SccpManagement getSccpManagement() {
        return sccpManagement;
    }

    public void setSccpManagement(SccpManagement sccpManagement) {
        this.sccpManagement = sccpManagement;
    }

    public void start() {
        // NOP for now

    }

    public void stop() {
        // NOP for now

    }

    protected void routeMssgFromMtp(SccpAddressedMessageImpl msg) throws Exception {
        if (this.sccpStackImpl.isPreviewMode()) {
            // we route all incoming message (except management messages) to all registered SccpListeners

            int ssn = msg.getCalledPartyAddress().getSubsystemNumber();
            if (ssn == 1) {
                return;
            }

            FastMap<Integer, SccpListener> lstListn = this.sccpProviderImpl.getAllSccpListeners();
            for (Map.Entry<Integer, SccpListener> val : lstListn.entrySet()) {
                SccpListener listener = val.getValue();
                if (msg instanceof SccpDataMessage) {
                    SccpDataMessage dataMsg = (SccpDataMessage) msg;
                    listener.onMessage(dataMsg);
                }
            }

            return;
        }

        // TODO if the local SCCP or node is in an overload condition, SCRC
        // shall inform SCMG

        SccpAddress calledPartyAddress = msg.getCalledPartyAddress();
        RoutingIndicator ri = calledPartyAddress.getAddressIndicator().getRoutingIndicator();
        switch (ri) {
            case ROUTING_BASED_ON_DPC_AND_SSN:
                int ssn = msg.getCalledPartyAddress().getSubsystemNumber();
                if (ssn == 1) {
                    // This is for management
                    if (msg instanceof SccpDataMessage) {
                        this.sccpManagement.onManagementMessage((SccpDataMessage) msg);
                    }
                    return;
                }

                SccpListener listener = this.sccpProviderImpl.getSccpListener(ssn);
                if (listener == null) {
                    // SCCP user with received SSN is not available - Notify Management
                    this.sccpManagement.recdMsgForProhibitedSsn(msg, ssn);

                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format(
                                "Received SccpMessage=%s from MTP but the SSN is not available for local routing", msg));
                    }
                    this.sendSccpError(msg, ReturnCauseValue.SUBSYSTEM_FAILURE);
                    return;
                }

                // Notify Listener
                try {
                    if (msg instanceof SccpDataMessage) {
                        if (logger.isDebugEnabled()) {
                            logger.debug(String.format("Local deliver : SCCP Data Message=%s", msg.toString()));
                        }
                        listener.onMessage((SccpDataMessage) msg);
                    } else if (msg instanceof SccpNoticeMessage) {
                        if (logger.isDebugEnabled()) {
                            logger.debug(String.format("Local deliver : SCCP Notice Message=%s", msg.toString()));
                        }
                        listener.onNotice((SccpNoticeMessage) msg);
                    } else {
                        // TODO: process connection-oriented messages
                    }

                } catch (Exception e) {
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format(
                                "Exception from the listener side when delivering SccpData to ssn=%d: Message=%s",
                                msg.getOriginLocalSsn(), msg), e);
                    }
                }
                break;
            case ROUTING_BASED_ON_GLOBAL_TITLE:
                this.translationFunction(msg);
                break;
            default:
                // This can never happen
                logger.error(String.format("Invalid Routing Indictaor received for message=%s from MTP3", msg));
                break;
        }
    }

    protected void routeMssgFromSccpUser(SccpAddressedMessageImpl msg) throws Exception {
        if (this.sccpStackImpl.isPreviewMode()) {
            // we drop off local originated message in pereviewMode
            return;
        }

        this.route(msg);
    }

    private long lastCongAnnounseTime;

    protected ReturnCauseValue send(SccpMessageImpl message) throws Exception {

        int dpc = message.getOutgoingDpc();
        int sls = message.getSls();

        // outgoing congestion control
        RemoteSignalingPointCode remoteSpc = this.sccpStackImpl.getSccpResource().getRemoteSpcByPC(dpc);
        int currentRestrictionLevel = remoteSpc.getCurrentRestrictionLevel();
        int msgImportance = 8;
        if (message instanceof SccpDataMessageImpl) {
            // UDT, XUDT, LUDT
            msgImportance = 5;
        }
        if (message instanceof SccpNoticeMessageImpl) {
            // UDTS, XUDTS, LUDTS
            msgImportance = 3;
        }
        if (this.sccpManagement.getSccpCongestionControl().isCongControl_blockingOutgoungScpMessages()
                && msgImportance < currentRestrictionLevel) {
            // we are dropping a message because of outgoing congestion

            long curTime = System.currentTimeMillis();
            if (lastCongAnnounseTime + 1000 < curTime) {
                lastCongAnnounseTime = curTime;
                logger.warn(String.format("Outgoing congestion control: SCCP: SccpMessage for sending=%s was dropped because of congestion level %d to dpc %d",
                        message, currentRestrictionLevel, dpc));
            }
            return ReturnCauseValue.NETWORK_CONGESTION;
        }

        Mtp3ServiceAccessPoint sap = this.sccpStackImpl.router.findMtp3ServiceAccessPoint(dpc, sls, message.getNetworkId());
        if (sap == null) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn(String.format("SccpMessage for sending=%s but no matching dpc=%d & sls=%d SAP found", message, dpc,
                        sls));
            }
            return ReturnCauseValue.SCCP_FAILURE;
        }

        Mtp3UserPart mup = this.sccpStackImpl.getMtp3UserPart(sap.getMtp3Id());
        if (mup == null) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn(String.format("SccpMessage for sending=%s but no matching Mtp3UserPart found for Id=%d", message,
                        sap.getMtp3Id()));
            }
            return ReturnCauseValue.SCCP_FAILURE;
        }

        LongMessageRule lmr = this.sccpStackImpl.router.findLongMessageRule(dpc);
        LongMessageRuleType lmrt = LongMessageRuleType.LONG_MESSAGE_FORBBIDEN;
        if (lmr != null)
            lmrt = lmr.getLongMessageRuleType();
        EncodingResultData erd = message.encode(sccpStackImpl, lmrt, mup.getMaxUserDataLength(dpc), logger, this.sccpStackImpl.isRemoveSpc(),
                this.sccpStackImpl.getSccpProtocolVersion());
        switch (erd.getEncodingResult()) {
            case Success:
                Mtp3TransferPrimitiveFactory factory = mup.getMtp3TransferPrimitiveFactory();
                if (erd.getSolidData() != null) {
                    // nonsegmented data
                    Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(Mtp3._SI_SERVICE_SCCP, sap.getNi(), 0,
                            sap.getOpc(), dpc, sls, erd.getSolidData());
                    mup.sendMessage(msg);
                } else {
                    // segmented data
                    for (byte[] bf : erd.getSegementedData()) {
                        Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(Mtp3._SI_SERVICE_SCCP, sap.getNi(), 0,
                                sap.getOpc(), dpc, sls, bf);
                        mup.sendMessage(msg);
                    }
                }
                return null;

            case ReturnFailure:
                return erd.getReturnCause();

            default:
                String em = String.format("Error %s when encoding a SccpMessage\n%s", erd.getEncodingResult().toString(),
                        message.toString());
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(em);
                }
                throw new IOException(em);
        }
    }

    protected ReturnCauseValue sendManagementMessage(SccpDataMessageImpl message) throws Exception {
        int dpc = message.getOutgoingDpc();

        Mtp3ServiceAccessPoint sap = this.sccpStackImpl.router.findMtp3ServiceAccessPoint(dpc, 0);
        if (sap == null) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn(String.format("Sccp management message for sending=%s but no matching dpc=%d SAP found", message,
                        dpc));
            }
            return ReturnCauseValue.SCCP_FAILURE;
        }

        Mtp3UserPart mup = this.sccpStackImpl.getMtp3UserPart(sap.getMtp3Id());
        if (mup == null) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn(String.format("Sccp management message for sending=%s but no matching Mtp3UserPart found for Id=%d", message,
                        sap.getMtp3Id()));
            }
            return ReturnCauseValue.SCCP_FAILURE;
        }

        LongMessageRuleType lmrt = LongMessageRuleType.LONG_MESSAGE_FORBBIDEN;
        EncodingResultData erd = message.encode(sccpStackImpl, lmrt, mup.getMaxUserDataLength(dpc), logger, this.sccpStackImpl.isRemoveSpc(),
                this.sccpStackImpl.getSccpProtocolVersion());
        switch (erd.getEncodingResult()) {
            case Success:
                Mtp3TransferPrimitiveFactory factory = mup.getMtp3TransferPrimitiveFactory();
                if (erd.getSolidData() != null) {
                    // nonsegmented data
                    Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(Mtp3._SI_SERVICE_SCCP, sap.getNi(), 0,
                            sap.getOpc(), dpc, 0, erd.getSolidData());
                    mup.sendMessage(msg);
                } else {
                    // segmented data - not possible for a management message
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format(
                                "Sccp management message for sending=%s was encoded with segments, it is forbidded", message));
                    }
                    return ReturnCauseValue.SCCP_FAILURE;
                }
                return null;

            case ReturnFailure:
                return erd.getReturnCause();

            default:
                String em = String.format("Error %s when encoding a SccpMessage\n%s", erd.getEncodingResult().toString(),
                        message.toString());
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(em);
                }
                throw new IOException(em);
        }
    }

    private enum TranslationAddressCheckingResult {
        destinationAvailable, destinationUnavailable_SubsystemFailure, destinationUnavailable_MtpFailure, destinationUnavailable_Congestion, translationFailure;
    }

    private TranslationAddressCheckingResult checkTranslationAddress(SccpAddressedMessageImpl msg, Rule rule,
            SccpAddress translationAddress, String destName) {

        if (translationAddress == null) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn(String.format(
                        "Received SccpMessage=%s for Translation but no matching %s Address defined for Rule=%s for routing",
                        msg, destName, rule));
            }
            return TranslationAddressCheckingResult.translationFailure;
        }

        if (!translationAddress.getAddressIndicator().isPCPresent()) {

            // destination PC is absent - bad rule
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn(String.format("Received SccpMessage=%s for Translation but no PC is present for %s Address ", msg,
                        destName));
            }
            return TranslationAddressCheckingResult.translationFailure;
        }

        int targetSsn = translationAddress.getSubsystemNumber();
        if (targetSsn == 0)
            targetSsn = msg.getCalledPartyAddress().getSubsystemNumber();

        if (this.sccpStackImpl.router.spcIsLocal(translationAddress.getSignalingPointCode())) {
            // destination PC is local
            if (targetSsn == 1 || this.sccpProviderImpl.getSccpListener(targetSsn) != null) {
                return TranslationAddressCheckingResult.destinationAvailable;
            } else {
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(String.format(
                            "Received SccpMessage=%s for Translation but no local SSN is present for %s Address ", msg,
                            destName));
                }
                return TranslationAddressCheckingResult.destinationUnavailable_SubsystemFailure;
            }
        }

        // Check if the DPC is prohibited
        RemoteSignalingPointCode remoteSpc = this.sccpStackImpl.getSccpResource().getRemoteSpcByPC(
                translationAddress.getSignalingPointCode());
        if (remoteSpc == null) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn(String.format(
                        "Received SccpMessage=%s for Translation but no %s Remote Signaling Pointcode = %d resource defined ",
                        msg, destName, translationAddress.getSignalingPointCode()));
            }
            return TranslationAddressCheckingResult.translationFailure;
        }

        if (remoteSpc.isRemoteSpcProhibited()) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn(String.format(
                        "Received SccpMessage=%s for Translation but %s Remote Signaling Pointcode = %d is prohibited ", msg,
                        destName, translationAddress.getSignalingPointCode()));
            }
            return TranslationAddressCheckingResult.destinationUnavailable_MtpFailure;
        }

        // Check if the DPC is congested
        if (remoteSpc.getCurrentRestrictionLevel() > 1) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn(String
                        .format("Received SccpMessage=%s for Translation but %s Remote Signaling Pointcode = %d is congested with level %d ",
                                msg, destName, translationAddress.getSignalingPointCode(),
                                remoteSpc.getCurrentRestrictionLevel()));
            }
            return TranslationAddressCheckingResult.destinationUnavailable_Congestion;
        }

        if (translationAddress.getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN) {
            if (targetSsn != 1) {
                RemoteSubSystem remoteSubSystem = this.sccpStackImpl.getSccpResource().getRemoteSsn(
                        translationAddress.getSignalingPointCode(), targetSsn);
                if (remoteSubSystem == null) {
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format("Received SccpMessage=%s for Translation but no %s Remote SubSystem = %d (dpc=%d) resource defined ", msg,
                                destName, targetSsn, translationAddress.getSignalingPointCode()));
                    }
                    return TranslationAddressCheckingResult.translationFailure;
                }
                if (remoteSubSystem.isRemoteSsnProhibited()) {
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format("Received SccpMessage=%s for Translation but %s Remote SubSystem = %d (dpc=%d) is prohibited ", msg,
                                destName, targetSsn, translationAddress.getSignalingPointCode()));
                    }
                    return TranslationAddressCheckingResult.destinationUnavailable_SubsystemFailure;
                }
            }
        }

        return TranslationAddressCheckingResult.destinationAvailable;
    }

    private void translationFunction(SccpAddressedMessageImpl msg) throws Exception {

        // checking for hop counter
        if (!msg.reduceHopCounter()) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn(String.format(
                        "Received SccpMessage for Translation but hop counter violation detected\nSccpMessage=%s", msg));
            }
            this.sendSccpError(msg, ReturnCauseValue.HOP_COUNTER_VIOLATION);
            return;
        }

        SccpAddress calledPartyAddress = msg.getCalledPartyAddress();

        Rule rule = this.sccpStackImpl.router.findRule(calledPartyAddress, msg.getIsMtpOriginated(), msg.getNetworkId());
        if (rule == null) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn(String.format(
                        "Received SccpMessage for Translation but no matching Rule found for local routing\nSccpMessage=%s",
                        msg));
            }
            // Translation failed return error
            this.sendSccpError(msg, ReturnCauseValue.NO_TRANSLATION_FOR_ADDRESS);
            return;
        }

        // Check whether to use primary or backup address
        SccpAddress translationAddressPri = this.sccpStackImpl.router.getRoutingAddress(rule.getPrimaryAddressId());
        TranslationAddressCheckingResult resPri = this.checkTranslationAddress(msg, rule, translationAddressPri, "primary");
        if (resPri == TranslationAddressCheckingResult.translationFailure) {
            this.sendSccpError(msg, ReturnCauseValue.NO_TRANSLATION_FOR_ADDRESS);
            return;
        }

        SccpAddress translationAddressSec = null;
        TranslationAddressCheckingResult resSec = TranslationAddressCheckingResult.destinationUnavailable_SubsystemFailure;
        if (rule.getRuleType() != RuleType.SOLITARY) {
            translationAddressSec = this.sccpStackImpl.router.getRoutingAddress(rule.getSecondaryAddressId());
            resSec = this.checkTranslationAddress(msg, rule, translationAddressSec, "secondary");
            if (resSec == TranslationAddressCheckingResult.translationFailure) {
                this.sendSccpError(msg, ReturnCauseValue.NO_TRANSLATION_FOR_ADDRESS);
                return;
            }
        }

        if (resPri != TranslationAddressCheckingResult.destinationAvailable
                && resPri != TranslationAddressCheckingResult.destinationUnavailable_Congestion
                && resSec != TranslationAddressCheckingResult.destinationAvailable
                && resSec != TranslationAddressCheckingResult.destinationUnavailable_Congestion) {
            switch (resPri) {
                case destinationUnavailable_SubsystemFailure:
                    this.sendSccpError(msg, ReturnCauseValue.SUBSYSTEM_FAILURE);
                    return;
                case destinationUnavailable_MtpFailure:
                    this.sendSccpError(msg, ReturnCauseValue.MTP_FAILURE);
                    return;
                case destinationUnavailable_Congestion:
                    this.sendSccpError(msg, ReturnCauseValue.NETWORK_CONGESTION);
                    return;
                default:
                    this.sendSccpError(msg, ReturnCauseValue.SCCP_FAILURE);
                    return;
            }
        }

        SccpAddress translationAddress = null;
        SccpAddress translationAddress2 = null;
        if (resPri == TranslationAddressCheckingResult.destinationAvailable
                && resSec != TranslationAddressCheckingResult.destinationAvailable) {
            translationAddress = translationAddressPri;
        } else if (resPri != TranslationAddressCheckingResult.destinationAvailable
                && resSec == TranslationAddressCheckingResult.destinationAvailable) {
            translationAddress = translationAddressSec;
        } else if (resPri == TranslationAddressCheckingResult.destinationUnavailable_Congestion
                && resSec != TranslationAddressCheckingResult.destinationAvailable) {
            translationAddress = translationAddressPri;
        } else if (resPri != TranslationAddressCheckingResult.destinationAvailable
                && resSec == TranslationAddressCheckingResult.destinationUnavailable_Congestion) {
            translationAddress = translationAddressSec;
        } else {
            switch (rule.getRuleType()) {
                case SOLITARY:
                case DOMINANT:
                    translationAddress = translationAddressPri;
                    break;

                case LOADSHARED:
                    // loadsharing case and both destinations are available
                    if (msg.getSccpCreatesSls()) {
                        if (this.sccpStackImpl.newSelector())
                            translationAddress = translationAddressPri;
                        else
                            translationAddress = translationAddressSec;
                    } else {
                        if (this.selectLoadSharingRoute(rule.getLoadSharingAlgorithm(), msg))
                            translationAddress = translationAddressPri;
                        else
                            translationAddress = translationAddressSec;
                    }
                    break;

                case BROADCAST:
                    // Broadcast case and both destinations are available
                    translationAddress = translationAddressPri;
                    translationAddress2 = translationAddressSec;
                    break;
            }
        }

        // changing calling party address if a rule has NewCallingPartyAddress
        if (rule.getNewCallingPartyAddressId() != null) {
            SccpAddress newCallingPartyAddress = this.sccpStackImpl.router
                    .getRoutingAddress(rule.getNewCallingPartyAddressId());
            if (newCallingPartyAddress != null) {
                msg.setCallingPartyAddress(newCallingPartyAddress);
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("New CallingPartyAddress assigned after translation = %s",
                            newCallingPartyAddress));
                }
            }
        }

        // translate address
        SccpAddress address = rule.translate(calledPartyAddress, translationAddress);
        msg.setCalledPartyAddress(address);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Matching rule found: [%s] CalledPartyAddress after translation = %s", rule, address));
        }

        // routing procedures then continue's
        this.route(msg);

        if (translationAddress2 != null) {
            // for broadcast mode - route to a secondary destination if it is available
            address = rule.translate(calledPartyAddress, translationAddress2);
            msg.setCalledPartyAddress(address);
            msg.clearReturnMessageOnError();

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("CalledPartyAddress after translation - a second broadcast address = %s", address));
            }

            // routing procedures then continue's
            this.route(msg);
        }
    }

    private boolean selectLoadSharingRoute(LoadSharingAlgorithm loadSharingAlgo, SccpAddressedMessageImpl msg) {

        if (loadSharingAlgo == LoadSharingAlgorithm.Bit4) {
            if ((msg.getSls() & 0x10) == 0)
                return true;
            else
                return false;
        } else if (loadSharingAlgo == LoadSharingAlgorithm.Bit3) {
            if ((msg.getSls() & 0x08) == 0)
                return true;
            else
                return false;
        } else if (loadSharingAlgo == LoadSharingAlgorithm.Bit2) {
            if ((msg.getSls() & 0x04) == 0)
                return true;
            else
                return false;
        } else if (loadSharingAlgo == LoadSharingAlgorithm.Bit1) {
            if ((msg.getSls() & 0x02) == 0)
                return true;
            else
                return false;
        } else if (loadSharingAlgo == LoadSharingAlgorithm.Bit0) {
            if ((msg.getSls() & 0x01) == 0)
                return true;
            else
                return false;
        } else {
            // TODO: implement complicated algorithms for selecting a destination
            // (CallingPartyAddress & SLS depended)
            // Look at Q.815 8.1.3 - active loadsharing
            return true;
        }
    }

    private void route(SccpAddressedMessageImpl msg) throws Exception {

        SccpAddress calledPartyAddress = msg.getCalledPartyAddress();

        int dpc = calledPartyAddress.getSignalingPointCode();
        int ssn = calledPartyAddress.getSubsystemNumber();
        GlobalTitle gt = calledPartyAddress.getGlobalTitle();

        if (calledPartyAddress.getAddressIndicator().isPCPresent()) {
            // DPC present

            if (this.sccpStackImpl.router.spcIsLocal(dpc)) {
                // This message is for local routing

                if (ssn > 0) {
                    // if a non-zero SSN is present but not the GT (case 2 a) of
                    // 2.2.2), then the message is passed based on the message
                    // type to either connection-oriented control or
                    // connectionless control and based on the availability of
                    // the subsystem;
                    if (ssn == 1) {
                        // This is for management
                        if (msg instanceof SccpDataMessage) {
                            this.sccpManagement.onManagementMessage((SccpDataMessage) msg);
                        }
                        return;
                    }

                    SccpListener listener = this.sccpProviderImpl.getSccpListener(ssn);
                    if (listener == null) {
                        if (logger.isEnabledFor(Level.WARN)) {
                            logger.warn(String.format(
                                    "Received SccpMessage=%s for routing but the SSN is not available for local routing", msg));
                        }
                        this.sendSccpError(msg, ReturnCauseValue.SUBSYSTEM_FAILURE);
                        return;
                    }
                    // Notify Listener
                    try {
                        // JIC: user may behave bad and throw something here.
                        if (msg instanceof SccpDataMessage) {
                            if (logger.isDebugEnabled()) {
                                logger.debug(String.format("Local deliver : SCCP Data Message=%s", msg.toString()));
                            }
                            listener.onMessage((SccpDataMessage) msg);
                        } else if (msg instanceof SccpNoticeMessage) {
                            if (logger.isDebugEnabled()) {
                                logger.debug(String.format("Local deliver : SCCP Notice Message=%s", msg.toString()));
                            }
                            listener.onNotice((SccpNoticeMessage) msg);
                        } else {
                            // TODO: process connection-oriented messages
                        }
                    } catch (Exception e) {
                        if (logger.isEnabledFor(Level.WARN)) {
                            logger.warn(String.format(
                                    "Exception from the listener side when delivering SccpData to ssn=%d: Message=%s",
                                    msg.getOriginLocalSsn(), msg), e);
                        }
                    }
                } else if (gt != null) {
                    // if the GT is present but no SSN or a zero SSN is present
                    // (case 2 b) of 2.2.2), then the message is passed to the
                    // translation function;

                    if (calledPartyAddress.isTranslated()) {
                        // Called address already translated once. This is loop
                        // condition and error
                        logger.error(String
                                .format("Droping message. Received SCCPMessage=%s for routing but CalledPartyAddress is already translated once",
                                        msg));
                        this.sendSccpError(msg, ReturnCauseValue.SCCP_FAILURE);
                        return;
                    }

                    this.translationFunction(msg);

                } else {
                    // if an SSN equal to zero is present but not a GT (case 2
                    // d) of 2.2.2), then the address information is incomplete
                    // and the message shall be discarded. This abnormality is
                    // similar to the one described in 3.8.3.3, item 1) b6.

                    logger.error(String.format("Received SCCPMessage=%s for routing, but neither SSN nor GT present", msg));
                    this.sendSccpError(msg, ReturnCauseValue.NO_TRANSLATION_FOR_NATURE);
                }

            } else {
                // DPC present but its not local pointcode. This message should be Tx to MTP

                // Check if the DPC is not prohibited
                RemoteSignalingPointCode remoteSpc = this.sccpStackImpl.getSccpResource().getRemoteSpcByPC(dpc);
                if (remoteSpc == null) {
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format(
                                "Received SccpMessage=%s for routing but no Remote Signaling Pointcode = %d resource defined ",
                                msg, dpc));
                    }
                    this.sendSccpError(msg, ReturnCauseValue.SCCP_FAILURE);
                    return;
                }
                if (remoteSpc.isRemoteSpcProhibited()) {
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format(
                                "Received SccpMessage=%s for routing but Remote Signaling Pointcode = %d is prohibited", msg,
                                dpc));
                    }
                    this.sendSccpError(msg, ReturnCauseValue.MTP_FAILURE);
                    return;
                }

                if (ssn > 1) { // was: ssn > 1 ???
                    if (calledPartyAddress.getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN) {
                        // if a non-zero SSN is present but not the GT (case 2a) of 2.2.2),
                        // then the called party address provided shall
                        // contain this SSN and the routing indicator shall be set
                        // to "Route on SSN"; See 2.2.2.1 point 2 of ITU-T Q.714
                        // If routing based on SSN, check remote SSN is available
                        RemoteSubSystem remoteSsn = this.sccpStackImpl.getSccpResource().getRemoteSsn(dpc,
                                calledPartyAddress.getSubsystemNumber());
                        if (remoteSsn == null) {
                            if (logger.isEnabledFor(Level.WARN)) {
                                logger.warn(String.format(
                                        "Received SCCPMessage=%s for routing, but no Remote SubSystem = %d resource defined ",
                                        msg, calledPartyAddress.getSubsystemNumber()));
                            }
                            // Routing failed return error
                            this.sendSccpError(msg, ReturnCauseValue.SCCP_FAILURE);
                            return;
                        }

                        if (remoteSsn.isRemoteSsnProhibited()) {
                            if (logger.isEnabledFor(Level.WARN)) {
                                logger.warn(String.format(
                                        "Routing of Sccp Message=%s failed as Remote SubSystem = %d is prohibited ", msg,
                                        calledPartyAddress.getSubsystemNumber()));
                            }
                            this.sendSccpError(msg, ReturnCauseValue.SUBSYSTEM_FAILURE);
                            return;
                        }
                    }

                    // send to MTP
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Tx : SCCP Message=%s", msg.toString()));
                    }
                    this.sendMessageToMtp(msg);
                } else if (gt != null) {

                    // if the GT is present but no SSN or a zero SSN is present
                    // (case 2 b) of 2.2.2), then the DPC identifies where the
                    // global title translation occurs. The called party address
                    // provided shall contain this GT and the routing indicator
                    // shall be set to "Route on GT"; See 2.2.2.1 point 3 of
                    // ITU-T Q.714

                    // send to MTP
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Tx : SCCP Message=%s", msg.toString()));
                    }
                    this.sendMessageToMtp(msg);
                } else {

                    logger.error(String.format("Received SCCPMessage=%s for routing, but neither SSN nor GT present", msg));
                    this.sendSccpError(msg, ReturnCauseValue.NO_TRANSLATION_FOR_NATURE);
                }
            }
        } else {
            // DPC not present

            // If the DPC is not present, (case 3 of 2.2.2), then a global title
            // translation is required before the message can be sent out.
            // Translation results in a DPC and possibly a new SSN or new GT or
            // both.

            if (gt == null) {
                // No DPC, and no GT. This is insufficient information
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(String
                            .format("Received SccpMessage=%s for routing from local SCCP user part but no pointcode and no GT or SSN included",
                                    msg, dpc));
                }
                this.sendSccpError(msg, ReturnCauseValue.NO_TRANSLATION_FOR_NATURE);
                return;
            }

            if (calledPartyAddress.isTranslated()) {
                // Called address already translated once. This is loop
                // condition and error
                logger.error(String
                        .format("Droping message. Received SCCPMessage=%s for Routing , but CalledPartyAddress is already translated once",
                                msg));
                this.sendSccpError(msg, ReturnCauseValue.SCCP_FAILURE);
                return;
            }

            this.translationFunction(msg);
        }
    }

    protected void sendMessageToMtp(SccpAddressedMessageImpl msg) throws Exception {

        msg.setOutgoingDpc(msg.getCalledPartyAddress().getSignalingPointCode());

        // if (msg.getSccpCreatesSls()) {
        // msg.setSls(this.sccpStackImpl.newSls());
        // }

        ReturnCauseValue er = this.send(msg);
        if (er != null) {
            this.sendSccpError(msg, er);
        }
    }

    protected void sendSccpError(SccpAddressedMessageImpl msg, ReturnCauseValue returnCauseInt) throws Exception {

        // sending only if "ReturnMessageOnError" flag of the origin message
        if (!msg.getReturnMessageOnError())
            return;

        // in case we did not consume and this message has arrived from
        // other end.... we have to reply in some way Q.714 4.2 for now
        SccpNoticeMessageImpl ans = null;
        // not sure if its proper
        ReturnCause returnCause = ((ParameterFactoryImpl) this.sccpProviderImpl.getParameterFactory())
                .createReturnCause(returnCauseInt);
        if (msg instanceof SccpDataMessageImpl) {
            SccpDataMessageImpl msgData = (SccpDataMessageImpl) msg;
            ans = (SccpNoticeMessageImpl) messageFactory.createNoticeMessage(msg.getType(), returnCause,
                    msg.getCallingPartyAddress(), msg.getCalledPartyAddress(), msgData.getData(), msgData.getHopCounter(),
                    msgData.getImportance());
        } else {
            // TODO: Implement return errors for connection-oriented messages
        }

        if (ans != null) {
            if (msg.getIsMtpOriginated()) {

                // send to MTP3
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("sendSccpError to a remote user: SCCP Message=%s", msg.toString()));
                }
                this.route(ans);
            } else {

                // deliver locally
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("sendSccpError to a local user: SCCP Message=%s", msg.toString()));
                }
                SccpListener listener = this.sccpProviderImpl.getSccpListener(msg.getOriginLocalSsn());
                if (listener != null) {
                    try {
                        listener.onNotice(ans);
                    } catch (Exception e) {
                        if (logger.isEnabledFor(Level.WARN)) {
                            logger.warn(String.format(
                                    "Exception from the listener side when delivering SccpNotice to ssn=%d: Message=%s",
                                    msg.getOriginLocalSsn(), msg), e);
                        }
                    }
                }
            }
        }
    }
}
