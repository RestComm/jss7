/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.map.api.service.sms;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;


/**
 * 
 * @author sergey vetyutnev
 * 
 */
public interface MAPDialogSms extends MAPDialog {

	/**
	 * Sending MAP-MO-FORWARD-SHORT-MESSAGE request
	 * 
	 * @param sm_RP_DA
	 *            mandatory
	 * @param sm_RP_OA
	 *            mandatory
	 * @param sm_RP_UI
	 *            mandatory
	 * @param extensionContainer
	 *            optional
	 * @param imsi
	 *            optional
	 * @return invokeId
	 * @throws MAPException
	 */
	public Long addMoForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, byte[] sm_RP_UI, MAPExtensionContainer extensionContainer, IMSI imsi)
			throws MAPException;

	/**
	 * Sending MAP-MO-FORWARD-SHORT-MESSAGE response
	 * 
	 * @param invokeId
	 * @param sm_RP_UI
	 *            optional
	 * @param extensionContainer
	 *            optional
	 * @throws MAPException
	 */
	public void addMoForwardShortMessageResponse(long invokeId, byte[] sm_RP_UI, MAPExtensionContainer extensionContainer) throws MAPException;

	/**
	 * Sending MAP-MT-FORWARD-SHORT-MESSAGE request
	 * 
	 * @param sm_RP_DA
	 *            mandatory
	 * @param sm_RP_OA
	 *            mandatory
	 * @param sm_RP_UI
	 *            mandatory
	 * @param moreMessagesToSend
	 *            optional
	 * @param extensionContainer
	 *            optional
	 * @return
	 * @throws MAPException
	 */
	public Long addMtForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, byte[] sm_RP_UI, Boolean moreMessagesToSend,
			MAPExtensionContainer extensionContainer) throws MAPException;

	/**
	 * Sending MAP-MT-FORWARD-SHORT-MESSAGE response
	 * 
	 * @param invokeId
	 * @param sm_RP_UI
	 *            optional
	 * @param extensionContainer
	 *            optional
	 * @throws MAPException
	 */
	public void addMtForwardShortMessageResponse(long invokeId, byte[] sm_RP_UI, MAPExtensionContainer extensionContainer) throws MAPException;

	/**
	 * Sending MAP-SEND-ROUTING-INFO-FOR-SM request
	 * 
	 * @param msisdn
	 *            mandatory
	 * @param sm_RP_PRI
	 *            mandatory
	 * @param serviceCentreAddress
	 *            mandatory
	 * @param extensionContainer
	 *            optional
	 * @param gprsSupportIndicator
	 *            optional
	 * @param sM_RP_MTI
	 *            optional
	 * @param sM_RP_SMEA
	 *            optional
	 * @return
	 * @throws MAPException
	 */
	public Long addSendRoutingInfoForSMRequest(ISDNAddressString msisdn, Boolean sm_RP_PRI, AddressString serviceCentreAddress,
			MAPExtensionContainer extensionContainer, Boolean gprsSupportIndicator, SM_RP_MTI sM_RP_MTI, byte[] sM_RP_SMEA) throws MAPException;
	
	/**
	 * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
	 * 
	 * @param imsi
	 *            mandatory
	 * @param locationInfoWithLMSI
	 *            mandatory
	 * @param extensionContainer
	 *            optional
	 * @return
	 * @throws MAPException
	 */
	public void addSendRoutingInfoForSMResponse(long invokeId, IMSI imsi, LocationInfoWithLMSI locationInfoWithLMSI, MAPExtensionContainer extensionContainer)
			throws MAPException;
	
	/**
	 * Sending MAP-SEND-ROUTING-INFO-FOR-SM request
	 * 
	 * @param msisdn
	 *            mandatory
	 * @param derviceCentreAddress
	 *            mandatory
	 * @param sMDeliveryOutcome
	 *            mandatory
	 * @param sbsentSubscriberDiagnosticSM
	 *            mandatory
	 * @param extensionContainer
	 *            optional
	 * @param gprsSupportIndicator
	 *            optional
	 * @param deliveryOutcomeIndicator
	 *            optional
	 * @param additionalSMDeliveryOutcome
	 *            optional
	 * @param additionalAbsentSubscriberDiagnosticSM
	 *            optional
	 * @return
	 * @throws MAPException
	 */
	public Long addReportSMDeliveryStatusRequest(ISDNAddressString msisdn, AddressString derviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome,
			Integer sbsentSubscriberDiagnosticSM, MAPExtensionContainer extensionContainer, Boolean gprsSupportIndicator, Boolean deliveryOutcomeIndicator,
			SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;
	
	/**
	 * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
	 * 
	 * @param invokeId
	 * @param storedMSISDN
	 *            optional
	 * @param extensionContainer
	 *            optional
	 * @return
	 * @throws MAPException
	 */
	public void addReportSMDeliveryStatusResponse(long invokeId, ISDNAddressString storedMSISDN, MAPExtensionContainer extensionContainer) throws MAPException;
	
	/**
	 * Sending MAP-INFORM-SERVICE-CENTRE request
	 * 
	 * @param storedMSISDN
	 *            optional
	 * @param mwStatus
	 *            optional
	 * @param extensionContainer
	 *            optional
	 * @param absentSubscriberDiagnosticSM
	 *            optional
	 * @param additionalAbsentSubscriberDiagnosticSM
	 *            optional
	 * @return
	 * @throws MAPException
	 */
	public Long addInformServiceCentreRequest(ISDNAddressString storedMSISDN, MWStatus mwStatus, MAPExtensionContainer extensionContainer,
			Integer absentSubscriberDiagnosticSM, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

	/**
	 * Sending MAP-SEND-ROUTING-INFO-FOR-SM request
	 * 
	 * @param getMsisdn
	 *            mandatory
	 * @param serviceCentreAddress
	 *            mandatory
	 * @return
	 * @throws MAPException
	 */
	public Long addAlertServiceCentreRequest(ISDNAddressString getMsisdn, AddressString serviceCentreAddress) throws MAPException;

	/**
	 * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
	 * 
	 * @param invokeId
	 * @throws MAPException
	 */
	public void addAlertServiceCentreResponse(long invokeId) throws MAPException;

}


