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

package org.mobicents.protocols.ss7.map.api.service.sms;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPDialogSms extends MAPDialog {

    /**
     * Sending MAP-FORWARD-SHORT-MESSAGE request
     *
     * @param sm_RP_DA mandatory
     * @param sm_RP_OA mandatory
     * @param sm_RP_UI mandatory
     * @param moreMessagesToSend optional, default: false
     * @return invokeId
     * @throws MAPException
     */
    Long addForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI,
            boolean moreMessagesToSend) throws MAPException;

    Long addForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA,
            SmsSignalInfo sm_RP_UI, boolean moreMessagesToSend) throws MAPException;

    /**
     * Sending MAP-FORWARD-SHORT-MESSAGE response
     *
     * @param invokeId
     * @throws MAPException
     */
    void addForwardShortMessageResponse(long invokeId) throws MAPException;

    /**
     * Sending MAP-MO-FORWARD-SHORT-MESSAGE request
     *
     * @param sm_RP_DA mandatory
     * @param sm_RP_OA mandatory
     * @param sm_RP_UI mandatory
     * @param extensionContainer optional
     * @param imsi optional
     * @return invokeId
     * @throws MAPException
     */
    Long addMoForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI,
            MAPExtensionContainer extensionContainer, IMSI imsi) throws MAPException;

    Long addMoForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA,
            SmsSignalInfo sm_RP_UI, MAPExtensionContainer extensionContainer, IMSI imsi) throws MAPException;

    /**
     * Sending MAP-MO-FORWARD-SHORT-MESSAGE response
     *
     * @param invokeId
     * @param sm_RP_UI optional
     * @param extensionContainer optional
     * @throws MAPException
     */
    void addMoForwardShortMessageResponse(long invokeId, SmsSignalInfo sm_RP_UI, MAPExtensionContainer extensionContainer)
            throws MAPException;

    /**
     * Sending MAP-MT-FORWARD-SHORT-MESSAGE request
     *
     * @param sm_RP_DA mandatory
     * @param sm_RP_OA mandatory
     * @param sm_RP_UI mandatory
     * @param moreMessagesToSend optional
     * @param extensionContainer optional
     * @return
     * @throws MAPException
     */
    Long addMtForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI,
            boolean moreMessagesToSend, MAPExtensionContainer extensionContainer) throws MAPException;

    Long addMtForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA,
            SmsSignalInfo sm_RP_UI, boolean moreMessagesToSend, MAPExtensionContainer extensionContainer) throws MAPException;

    /**
     * Sending MAP-MT-FORWARD-SHORT-MESSAGE response
     *
     * @param invokeId
     * @param sm_RP_UI optional
     * @param extensionContainer optional
     * @throws MAPException
     */
    void addMtForwardShortMessageResponse(long invokeId, SmsSignalInfo sm_RP_UI, MAPExtensionContainer extensionContainer)
            throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM request
     *
     * @param msisdn mandatory
     * @param sm_RP_PRI mandatory
     * @param serviceCentreAddress mandatory
     * @param extensionContainer optional
     * @param gprsSupportIndicator optional
     * @param sM_RP_MTI optional
     * @param sM_RP_SMEA optional
     * @return
     * @throws MAPException
     */
    Long addSendRoutingInfoForSMRequest(ISDNAddressString msisdn, boolean sm_RP_PRI, AddressString serviceCentreAddress,
            MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, SM_RP_MTI sM_RP_MTI, SM_RP_SMEA sM_RP_SMEA,
            TeleserviceCode teleservice) throws MAPException;

    Long addSendRoutingInfoForSMRequest(int customInvokeTimeout, ISDNAddressString msisdn, boolean sm_RP_PRI,
            AddressString serviceCentreAddress, MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator,
            SM_RP_MTI sM_RP_MTI, SM_RP_SMEA sM_RP_SMEA, TeleserviceCode teleservice) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     *
     * @param imsi mandatory
     * @param locationInfoWithLMSI mandatory
     * @param extensionContainer optional
     * @return
     * @throws MAPException
     */
    void addSendRoutingInfoForSMResponse(long invokeId, IMSI imsi, LocationInfoWithLMSI locationInfoWithLMSI,
            MAPExtensionContainer extensionContainer, Boolean mwdSet) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM request
     *
     * @param msisdn mandatory
     * @param serviceCentreAddress mandatory
     * @param sMDeliveryOutcome mandatory
     * @param sbsentSubscriberDiagnosticSM mandatory
     * @param extensionContainer optional
     * @param gprsSupportIndicator optional
     * @param deliveryOutcomeIndicator optional
     * @param additionalSMDeliveryOutcome optional
     * @param additionalAbsentSubscriberDiagnosticSM optional
     * @return
     * @throws MAPException
     */
    Long addReportSMDeliveryStatusRequest(ISDNAddressString msisdn, AddressString serviceCentreAddress,
            SMDeliveryOutcome sMDeliveryOutcome, Integer absentSubscriberDiagnosticSM,
            MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, boolean deliveryOutcomeIndicator,
            SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

    Long addReportSMDeliveryStatusRequest(int customInvokeTimeout, ISDNAddressString msisdn,
            AddressString serviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome, Integer absentSubscriberDiagnosticSM,
            MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, boolean deliveryOutcomeIndicator,
            SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     *
     * @param invokeId
     * @param storedMSISDN optional
     * @param extensionContainer optional
     * @return
     * @throws MAPException
     */
    void addReportSMDeliveryStatusResponse(long invokeId, ISDNAddressString storedMSISDN,
            MAPExtensionContainer extensionContainer) throws MAPException;

    /**
     * Sending MAP-INFORM-SERVICE-CENTRE request
     *
     * @param storedMSISDN optional
     * @param mwStatus optional
     * @param extensionContainer optional
     * @param absentSubscriberDiagnosticSM optional
     * @param additionalAbsentSubscriberDiagnosticSM optional
     * @return
     * @throws MAPException
     */
    Long addInformServiceCentreRequest(ISDNAddressString storedMSISDN, MWStatus mwStatus,
            MAPExtensionContainer extensionContainer, Integer absentSubscriberDiagnosticSM,
            Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

    Long addInformServiceCentreRequest(int customInvokeTimeout, ISDNAddressString storedMSISDN, MWStatus mwStatus,
            MAPExtensionContainer extensionContainer, Integer absentSubscriberDiagnosticSM,
            Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM request
     *
     * @param msisdn mandatory
     * @param serviceCentreAddress mandatory
     * @return
     * @throws MAPException
     */
    Long addAlertServiceCentreRequest(ISDNAddressString msisdn, AddressString serviceCentreAddress) throws MAPException;

    Long addAlertServiceCentreRequest(int customInvokeTimeout, ISDNAddressString msisdn,
            AddressString serviceCentreAddress) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     *
     * @param invokeId
     * @throws MAPException
     */
    void addAlertServiceCentreResponse(long invokeId) throws MAPException;

}
