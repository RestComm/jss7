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
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
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
import org.mobicents.protocols.ss7.isup.message.parameter.AccessDeliveryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ApplicationTransportParameter;
import org.mobicents.protocols.ss7.isup.message.parameter.AutomaticCongestionLevel;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardGVNS;
import org.mobicents.protocols.ss7.isup.message.parameter.CCNRPossibleIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.CCSS;
import org.mobicents.protocols.ss7.isup.message.parameter.CallDiversionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallHistoryInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.CallTransferNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitGroupSuperVisionMessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitStateIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.ClosedUserGroupInterlockCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectedNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectionRequest;
import org.mobicents.protocols.ss7.isup.message.parameter.DisplayInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.EchoControlInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.EventInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ForwardGVNS;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNotificationIndicator;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericReference;
import org.mobicents.protocols.ss7.isup.message.parameter.HTRInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.InformationIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.InformationRequestIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.MLPPPrecedence;
import org.mobicents.protocols.ss7.isup.message.parameter.NatureOfConnectionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkManagementControls;
import org.mobicents.protocols.ss7.isup.message.parameter.NetworkSpecificFacility;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalBackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.OptionalForwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginatingISCPointCode;
import org.mobicents.protocols.ss7.isup.message.parameter.ParameterCompatibilityInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingBackwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.PropagationDelayCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectBackwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectCounter;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectStatus;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionNumberRestriction;
import org.mobicents.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation;
import org.mobicents.protocols.ss7.isup.message.parameter.SignalingPointCode;
import org.mobicents.protocols.ss7.isup.message.parameter.SubsequentNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.TransimissionMediumRequierementPrime;
import org.mobicents.protocols.ss7.isup.message.parameter.TransitNetworkSelection;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumRequirement;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumUsed;
import org.mobicents.protocols.ss7.isup.message.parameter.UIDActionIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformationPrime;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.UserToUserInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

/**
 * Start time:12:19:59 2009-09-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class ISUPMessageFactoryImpl implements ISUPMessageFactory {

	private ISUPParameterFactory parameterFactory;

	public ISUPMessageFactoryImpl(ISUPParameterFactory parameterFactory) {

		this.parameterFactory = parameterFactory;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createACM()
	 */
	public AddressCompleteMessage createACM() {

		AddressCompleteMessageImpl acm = new AddressCompleteMessageImpl(_ACM_HOLDER.mandatoryCodes,
				_ACM_HOLDER.mandatoryVariableCodes, _ACM_HOLDER.optionalCodes, _ACM_HOLDER.mandatoryCodeToIndex,
				_ACM_HOLDER.mandatoryVariableCodeToIndex, _ACM_HOLDER.optionalCodeToIndex);

		return acm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createANM(int
	 * cic)
	 */
	public AnswerMessage createANM() {


		AnswerMessageImpl acm = new AnswerMessageImpl( _ANM_HOLDER.mandatoryCodes, _ANM_HOLDER.mandatoryVariableCodes,
				_ANM_HOLDER.optionalCodes, _ANM_HOLDER.mandatoryCodeToIndex, _ANM_HOLDER.mandatoryVariableCodeToIndex,
				_ANM_HOLDER.optionalCodeToIndex);

		return acm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createAPT(int
	 * cic)
	 */
	public ApplicationTransportMessage createAPT(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		ApplicationTransportMessageImpl apt = new ApplicationTransportMessageImpl();
		apt.setCircuitIdentificationCode(c);
		return apt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createBLA(int
	 * cic)
	 */
	public BlockingAckMessage createBLA() {

		BlockingAckMessageImpl bla = new BlockingAckMessageImpl( _BLA_HOLDER.mandatoryCodes,
				_BLA_HOLDER.mandatoryVariableCodes, _BLA_HOLDER.optionalCodes, _BLA_HOLDER.mandatoryCodeToIndex,
				_BLA_HOLDER.mandatoryVariableCodeToIndex, _BLA_HOLDER.optionalCodeToIndex);
	
		return bla;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createBLO(int
	 * cic)
	 */
	public BlockingMessage createBLO(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		BlockingMessageImpl blo = new BlockingMessageImpl( _BLO_HOLDER.mandatoryCodes, _BLO_HOLDER.mandatoryVariableCodes,
				_BLO_HOLDER.optionalCodes, _BLO_HOLDER.mandatoryCodeToIndex, _BLO_HOLDER.mandatoryVariableCodeToIndex,
				_BLO_HOLDER.optionalCodeToIndex);
		blo.setCircuitIdentificationCode(c);
		return blo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCCR(int
	 * cic)
	 */
	public ContinuityCheckRequestMessage createCCR(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		ContinuityCheckRequestMessageImpl ccr = new ContinuityCheckRequestMessageImpl( _CCR_HOLDER.mandatoryCodes,
				_CCR_HOLDER.mandatoryVariableCodes, _CCR_HOLDER.optionalCodes, _CCR_HOLDER.mandatoryCodeToIndex,
				_CCR_HOLDER.mandatoryVariableCodeToIndex, _CCR_HOLDER.optionalCodeToIndex);
		ccr.setCircuitIdentificationCode(c);
		return ccr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCGB(int
	 * cic)
	 */
	public CircuitGroupBlockingMessage createCGB(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		CircuitGroupBlockingMessage cgb = new CircuitGroupBlockingMessageImpl( _CGB_HOLDER.mandatoryCodes,
				_CGB_HOLDER.mandatoryVariableCodes, _CGB_HOLDER.optionalCodes, _CGB_HOLDER.mandatoryCodeToIndex,
				_CGB_HOLDER.mandatoryVariableCodeToIndex, _CGB_HOLDER.optionalCodeToIndex);
		cgb.setCircuitIdentificationCode(c);
		return cgb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCGBA(int
	 * cic)
	 */
	public CircuitGroupBlockingAckMessage createCGBA() {

		CircuitGroupBlockingAckMessageImpl cgba = new CircuitGroupBlockingAckMessageImpl( _CGBA_HOLDER.mandatoryCodes,
				_CGBA_HOLDER.mandatoryVariableCodes, _CGBA_HOLDER.optionalCodes, _CGBA_HOLDER.mandatoryCodeToIndex,
				_CGBA_HOLDER.mandatoryVariableCodeToIndex, _CGBA_HOLDER.optionalCodeToIndex);

		return cgba;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCGU(int
	 * cic)
	 */
	public CircuitGroupUnblockingMessage createCGU(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		CircuitGroupUnblockingMessage msg = new CircuitGroupUnblockingMessageImpl( _CGU_HOLDER.mandatoryCodes,
				_CGU_HOLDER.mandatoryVariableCodes, _CGU_HOLDER.optionalCodes, _CGU_HOLDER.mandatoryCodeToIndex,
				_CGU_HOLDER.mandatoryVariableCodeToIndex, _CGU_HOLDER.optionalCodeToIndex);

		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCGUA(int
	 * cic)
	 */
	public CircuitGroupUnblockingAckMessage createCGUA() {

		CircuitGroupUnblockingAckMessage msg = new CircuitGroupUnblockingAckMessageImpl(_CGUA_HOLDER.mandatoryCodes,
				_CGUA_HOLDER.mandatoryVariableCodes, _CGUA_HOLDER.optionalCodes, _CGUA_HOLDER.mandatoryCodeToIndex,
				_CGUA_HOLDER.mandatoryVariableCodeToIndex, _CGUA_HOLDER.optionalCodeToIndex);

		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCIM(int
	 * cic)
	 */
	public ChargeInformationMessage createCIM(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		ChargeInformationMessage msg = new ChargeInformationMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCNF(int
	 * cic)
	 */
	public ConfusionMessage createCNF(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		ConfusionMessage msg = new ConfusionMessageImpl(_CNF_HOLDER.mandatoryCodes,
				_CNF_HOLDER.mandatoryVariableCodes, _CNF_HOLDER.optionalCodes, _CNF_HOLDER.mandatoryCodeToIndex,
				_CNF_HOLDER.mandatoryVariableCodeToIndex, _CNF_HOLDER.optionalCodeToIndex);
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCON(int
	 * cic)
	 */
	public ConnectMessage createCON() {
	
		ConnectMessage msg = new ConnectMessageImpl(_CON_HOLDER.mandatoryCodes,
				_CON_HOLDER.mandatoryVariableCodes, _CON_HOLDER.optionalCodes, _CON_HOLDER.mandatoryCodeToIndex,
				_CON_HOLDER.mandatoryVariableCodeToIndex, _CON_HOLDER.optionalCodeToIndex);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCOT(int
	 * cic)
	 */
	public ContinuityMessage createCOT() {

		ContinuityMessage msg = new ContinuityMessageImpl();

		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCPG(int
	 * cic)
	 */
	public CallProgressMessage createCPG(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		CallProgressMessage msg = new CallProgressMessageImpl( _CPG_HOLDER.mandatoryCodes, _CPG_HOLDER.mandatoryVariableCodes,
				_CPG_HOLDER.optionalCodes, _CPG_HOLDER.mandatoryCodeToIndex, _CPG_HOLDER.mandatoryVariableCodeToIndex,
				_CPG_HOLDER.optionalCodeToIndex);
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCQM(int
	 * cic)
	 */
	public CircuitGroupQueryMessage createCQM(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		CircuitGroupQueryMessage msg = new CircuitGroupQueryMessageImpl( _CQM_HOLDER.mandatoryCodes,
				_CQM_HOLDER.mandatoryVariableCodes, _CQM_HOLDER.optionalCodes, _CQM_HOLDER.mandatoryCodeToIndex,
				_CQM_HOLDER.mandatoryVariableCodeToIndex, _CQM_HOLDER.optionalCodeToIndex);
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCQR(int
	 * cic)
	 */
	public CircuitGroupQueryResponseMessage createCQR() {

		CircuitGroupQueryResponseMessage msg = new CircuitGroupQueryResponseMessageImpl( _CQR_HOLDER.mandatoryCodes,
				_CQR_HOLDER.mandatoryVariableCodes, _CQR_HOLDER.optionalCodes, _CQR_HOLDER.mandatoryCodeToIndex,
				_CQR_HOLDER.mandatoryVariableCodeToIndex, _CQR_HOLDER.optionalCodeToIndex);

		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFAA(int
	 * cic)
	 */
	public FacilityAcceptedMessage createFAA() {

		FacilityAcceptedMessage msg = new FacilityAcceptedMessageImpl();

		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFAC(int
	 * cic)
	 */
	public FacilityMessage createFAC(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		FacilityMessage msg = new FacilityMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFAR(int
	 * cic)
	 */
	public FacilityRequestMessage createFAR(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		FacilityRequestMessage msg = new FacilityRequestMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFOT(int
	 * cic)
	 */
	public ForwardTransferMessage createFOT(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		ForwardTransferMessage msg = new ForwardTransferMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createFRJ(int
	 * cic)
	 */
	public FacilityRejectedMessage createFRJ(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		FacilityRejectedMessage msg = new FacilityRejectedMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createGRA(int
	 * cic)
	 */
	public CircuitGroupResetAckMessage createGRA() {

		CircuitGroupResetAckMessage msg = new CircuitGroupResetAckMessageImpl( _GRA_HOLDER.mandatoryCodes,
				_GRA_HOLDER.mandatoryVariableCodes, _GRA_HOLDER.optionalCodes, _GRA_HOLDER.mandatoryCodeToIndex,
				_GRA_HOLDER.mandatoryVariableCodeToIndex, _GRA_HOLDER.optionalCodeToIndex);

		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createGRS(int
	 * cic)
	 */
	public CircuitGroupResetMessage createGRS(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		CircuitGroupResetMessage msg = new CircuitGroupResetMessageImpl( _GRS_HOLDER.mandatoryCodes,
				_GRS_HOLDER.mandatoryVariableCodes, _GRS_HOLDER.optionalCodes, _GRS_HOLDER.mandatoryCodeToIndex,
				_GRS_HOLDER.mandatoryVariableCodeToIndex, _GRS_HOLDER.optionalCodeToIndex);
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createIAM(int
	 * cic)
	 */
	public InitialAddressMessage createIAM(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		InitialAddressMessage msg = new InitialAddressMessageImpl( _IAM_HOLDER.mandatoryCodes,
				_IAM_HOLDER.mandatoryVariableCodes, _IAM_HOLDER.optionalCodes, _IAM_HOLDER.mandatoryCodeToIndex,
				_IAM_HOLDER.mandatoryVariableCodeToIndex, _IAM_HOLDER.optionalCodeToIndex);
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createIDR(int
	 * cic)
	 */
	public IdentificationRequestMessage createIDR(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		IdentificationRequestMessage msg = new IdentificationRequestMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createINF(int
	 * cic)
	 */
	public InformationMessage createINF() {
		InformationMessage msg = new InformationMessageImpl( _INF_HOLDER.mandatoryCodes,
				_INF_HOLDER.mandatoryVariableCodes, _INF_HOLDER.optionalCodes, _INF_HOLDER.mandatoryCodeToIndex,
				_INF_HOLDER.mandatoryVariableCodeToIndex, _INF_HOLDER.optionalCodeToIndex);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createINR(int
	 * cic)
	 */
	public InformationRequestMessage createINR(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		InformationRequestMessage msg = new InformationRequestMessageImpl( _INR_HOLDER.mandatoryCodes,
				_INR_HOLDER.mandatoryVariableCodes, _INR_HOLDER.optionalCodes, _INR_HOLDER.mandatoryCodeToIndex,
				_INR_HOLDER.mandatoryVariableCodeToIndex, _INR_HOLDER.optionalCodeToIndex);
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createIRS(int
	 * cic)
	 */
	public IdentificationResponseMessage createIRS() {

		IdentificationResponseMessage msg = new IdentificationResponseMessageImpl();

		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createLPA(int
	 * cic)
	 */
	public LoopbackAckMessage createLPA() {

		LoopbackAckMessage msg = new LoopbackAckMessageImpl( _LPA_HOLDER.mandatoryCodes, _LPA_HOLDER.mandatoryVariableCodes,
				_LPA_HOLDER.optionalCodes, _LPA_HOLDER.mandatoryCodeToIndex, _LPA_HOLDER.mandatoryVariableCodeToIndex,
				_LPA_HOLDER.optionalCodeToIndex);

		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createLPP(int
	 * cic)
	 */
	public LoopPreventionMessage createLPP(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		LoopPreventionMessage msg = new LoopPreventionMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createNRM(int
	 * cic)
	 */
	public NetworkResourceManagementMessage createNRM(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		NetworkResourceManagementMessage msg = new NetworkResourceManagementMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createOLM(int
	 * cic)
	 */
	public OverloadMessage createOLM(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		OverloadMessage msg = new OverloadMessageImpl( _OLM_HOLDER.mandatoryCodes, _OLM_HOLDER.mandatoryVariableCodes,
				_OLM_HOLDER.optionalCodes, _OLM_HOLDER.mandatoryCodeToIndex, _OLM_HOLDER.mandatoryVariableCodeToIndex,
				_OLM_HOLDER.optionalCodeToIndex);
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createPAM(int
	 * cic)
	 */
	public PassAlongMessage createPAM(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		PassAlongMessage msg = new PassAlongMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createPRI(int
	 * cic)
	 */
	public PreReleaseInformationMessage createPRI(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		PreReleaseInformationMessage msg = new PreReleaseInformationMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createREL(int
	 * cic)
	 */
	public ReleaseMessage createREL(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		ReleaseMessage msg = new ReleaseMessageImpl( _REL_HOLDER.mandatoryCodes, _REL_HOLDER.mandatoryVariableCodes,
				_REL_HOLDER.optionalCodes, _REL_HOLDER.mandatoryCodeToIndex, _REL_HOLDER.mandatoryVariableCodeToIndex,
				_REL_HOLDER.optionalCodeToIndex);
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createRES(int
	 * cic)
	 */
	public ResumeMessage createRES(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		ResumeMessage msg = new ResumeMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createRLC(int
	 * cic)
	 */
	public ReleaseCompleteMessage createRLC() {

		ReleaseCompleteMessage msg = new ReleaseCompleteMessageImpl( _RLC_HOLDER.mandatoryCodes,
				_RLC_HOLDER.mandatoryVariableCodes, _RLC_HOLDER.optionalCodes, _RLC_HOLDER.mandatoryCodeToIndex,
				_RLC_HOLDER.mandatoryVariableCodeToIndex, _RLC_HOLDER.optionalCodeToIndex);

		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createRSC(int
	 * cic)
	 */
	public ResetCircuitMessage createRSC(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		ResetCircuitMessage msg = new ResetCircuitMessageImpl( _RSC_HOLDER.mandatoryCodes, _RSC_HOLDER.mandatoryVariableCodes,
				_RSC_HOLDER.optionalCodes, _RSC_HOLDER.mandatoryCodeToIndex, _RSC_HOLDER.mandatoryVariableCodeToIndex,
				_RSC_HOLDER.optionalCodeToIndex);
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createSAM(int
	 * cic)
	 */
	public SubsequentAddressMessage createSAM(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		SubsequentAddressMessage msg = new SubsequentAddressMessageImpl( _SAM_HOLDER.mandatoryCodes,
				_SAM_HOLDER.mandatoryVariableCodes, _SAM_HOLDER.optionalCodes, _SAM_HOLDER.mandatoryCodeToIndex,
				_SAM_HOLDER.mandatoryVariableCodeToIndex, _SAM_HOLDER.optionalCodeToIndex);
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createSDN(int
	 * cic)
	 */
	public SubsequentDirectoryNumberMessage createSDN(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		SubsequentDirectoryNumberMessage msg = new SubsequentDirectoryNumberMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createSGM(int
	 * cic)
	 */
	public SegmentationMessage createSGM(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		SegmentationMessage msg = new SegmentationMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createSUS(int
	 * cic)
	 */
	public SuspendMessage createSUS(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		SuspendMessage msg = new SuspendMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUBA(int
	 * cic)
	 */
	public UnblockingAckMessage createUBA() {
	
		UnblockingAckMessage msg = new UnblockingAckMessageImpl( _UBA_HOLDER.mandatoryCodes,
				_UBA_HOLDER.mandatoryVariableCodes, _UBA_HOLDER.optionalCodes, _UBA_HOLDER.mandatoryCodeToIndex,
				_UBA_HOLDER.mandatoryVariableCodeToIndex, _UBA_HOLDER.optionalCodeToIndex);
	
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUBL(int
	 * cic)
	 */
	public UnblockingMessage createUBL(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		UnblockingMessage msg = new UnblockingMessageImpl( _UBL_HOLDER.mandatoryCodes, _UBL_HOLDER.mandatoryVariableCodes,
				_UBL_HOLDER.optionalCodes, _UBL_HOLDER.mandatoryCodeToIndex, _UBL_HOLDER.mandatoryVariableCodeToIndex,
				_UBL_HOLDER.optionalCodeToIndex);
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUCIC(int
	 * cic)
	 */
	public UnequippedCICMessage createUCIC(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		UnequippedCICMessage msg = new UnequippedCICMessageImpl( _UCIC_HOLDER.mandatoryCodes,
				_UCIC_HOLDER.mandatoryVariableCodes, _UCIC_HOLDER.optionalCodes, _UCIC_HOLDER.mandatoryCodeToIndex,
				_UCIC_HOLDER.mandatoryVariableCodeToIndex, _UCIC_HOLDER.optionalCodeToIndex);
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUPA(int
	 * cic)
	 */
	public UserPartAvailableMessage createUPA(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		UserPartAvailableMessage msg = new UserPartAvailableMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUPT(int
	 * cic)
	 */
	public UserPartTestMessage createUPT(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		UserPartTestMessage msg = new UserPartTestMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createUSR(int
	 * cic)
	 */
	public User2UserInformationMessage createUSR(int cic) {
		CircuitIdentificationCode c = this.parameterFactory.createCircuitIdentificationCode();
		c.setCIC(cic);
		User2UserInformationMessage msg = new User2UserInformationMessageImpl();
		msg.setCircuitIdentificationCode(c);
		return msg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.isup.ISUPMessageFactory#createCommand(int)
	 */
	public ISUPMessage createCommand(int commandCode) {
		switch (commandCode) {
		case InitialAddressMessage.MESSAGE_CODE:
			InitialAddressMessage IAM = createIAM(0);
			return IAM;
		case AddressCompleteMessage.MESSAGE_CODE:
			AddressCompleteMessage ACM = createACM();
			return ACM;
		case ReleaseMessage.MESSAGE_CODE:
			ReleaseMessage REL = createREL(0);
			return REL;
		case ReleaseCompleteMessage.MESSAGE_CODE:
			ReleaseCompleteMessage RLC = createRLC();
			return RLC;

		case ApplicationTransportMessage.MESSAGE_CODE:
			ApplicationTransportMessage APT = createAPT(0);
			return APT;

		case AnswerMessage.MESSAGE_CODE:
			AnswerMessage ANM = createANM();
			return ANM;

		case CallProgressMessage.MESSAGE_CODE:
			CallProgressMessage CPG = createCPG(0);
			return CPG;

		case CircuitGroupResetAckMessage.MESSAGE_CODE:
			CircuitGroupResetAckMessage GRA = createGRA();
			return GRA;

		case ConfusionMessage.MESSAGE_CODE:
			ConfusionMessage CFN = createCNF(0);
			return CFN;

		case ConnectMessage.MESSAGE_CODE:
			ConnectMessage CON = createCON();
			return CON;

		case ContinuityMessage.MESSAGE_CODE:
			ContinuityMessage COT = createCOT();
			return COT;

		case FacilityRejectedMessage.MESSAGE_CODE:
			FacilityRejectedMessage FRJ = createFRJ(0);
			return FRJ;

		case InformationMessage.MESSAGE_CODE:
			InformationMessage INF = createINF();
			return INF;

		case InformationRequestMessage.MESSAGE_CODE:
			InformationRequestMessage INR = createINR(0);
			return INR;

		case SubsequentAddressMessage.MESSAGE_CODE:
			SubsequentAddressMessage SAM = createSAM(0);
			return SAM;

		case SubsequentDirectoryNumberMessage.MESSAGE_CODE:
			SubsequentDirectoryNumberMessage SDN = createSDN(0);
			return SDN;

		case ForwardTransferMessage.MESSAGE_CODE:
			ForwardTransferMessage FOT = createFOT(0);
			return FOT;

		case ResumeMessage.MESSAGE_CODE:
			ResumeMessage RES = createRES(0);
			return RES;
		case BlockingMessage.MESSAGE_CODE:
			BlockingMessage BLO = createBLO(0);
			return BLO;

		case BlockingAckMessage.MESSAGE_CODE:
			BlockingAckMessage BLA = createBLA();
			return BLA;

		case ContinuityCheckRequestMessage.MESSAGE_CODE:
			ContinuityCheckRequestMessage CCR = createCCR(0);
			return CCR;

		case LoopbackAckMessage.MESSAGE_CODE:
			LoopbackAckMessage LPA = createLPA();
			return LPA;

		case LoopPreventionMessage.MESSAGE_CODE:
			LoopPreventionMessage LPP = createLPP(0);
			return LPP;

		case OverloadMessage.MESSAGE_CODE:
			OverloadMessage OLM = createOLM(0);
			return OLM;

		case SuspendMessage.MESSAGE_CODE:
			SuspendMessage SUS = createSUS(0);
			return SUS;

		case ResetCircuitMessage.MESSAGE_CODE:
			ResetCircuitMessage RSC = createRSC(0);
			return RSC;

		case UnblockingMessage.MESSAGE_CODE:
			UnblockingMessage UBL = createUBL(0);
			return UBL;

		case UnblockingAckMessage.MESSAGE_CODE:
			UnblockingAckMessage UBA = createUBA();
			return UBA;

		case UnequippedCICMessage.MESSAGE_CODE:
			UnequippedCICMessage UCIC = createUCIC(0);
			return UCIC;

		case CircuitGroupBlockingMessage.MESSAGE_CODE:
			CircuitGroupBlockingMessage CGB = createCGB(0);
			return CGB;

		case CircuitGroupBlockingAckMessage.MESSAGE_CODE:
			CircuitGroupBlockingAckMessage CGBA = createCGBA();
			return CGBA;

		case CircuitGroupUnblockingMessage.MESSAGE_CODE:
			CircuitGroupUnblockingMessage CGU = createCGU(0);
			return CGU;

		case CircuitGroupUnblockingAckMessage.MESSAGE_CODE:
			CircuitGroupUnblockingAckMessage CGUA = createCGUA();
			return CGUA;

		case CircuitGroupResetMessage.MESSAGE_CODE:
			CircuitGroupResetMessage GRS = createGRS(0);
			return GRS;

		case CircuitGroupQueryResponseMessage.MESSAGE_CODE:
			CircuitGroupQueryResponseMessage CQR = createCQR();
			return CQR;

		case CircuitGroupQueryMessage.MESSAGE_CODE:
			CircuitGroupQueryMessage CQM = createCQM(0);
			return CQM;

		case FacilityAcceptedMessage.MESSAGE_CODE:
			FacilityAcceptedMessage FAA = createFAA();
			return FAA;

		case FacilityRequestMessage.MESSAGE_CODE:
			FacilityRequestMessage FAR = createFAR(0);
			return FAR;

		case PassAlongMessage.MESSAGE_CODE:
			PassAlongMessage PAM = createPAM(0);
			return PAM;

		case PreReleaseInformationMessage.MESSAGE_CODE:
			PreReleaseInformationMessage PRI = createPRI(0);
			return PRI;

		case FacilityMessage.MESSAGE_CODE:
			FacilityMessage FAC = createFAC(0);
			return FAC;

		case NetworkResourceManagementMessage.MESSAGE_CODE:
			NetworkResourceManagementMessage NRM = createNRM(0);
			return NRM;

		case IdentificationRequestMessage.MESSAGE_CODE:
			IdentificationRequestMessage IDR = createIDR(0);
			return IDR;

		case IdentificationResponseMessage.MESSAGE_CODE:
			IdentificationResponseMessage IRS = createIRS();
			return IRS;

		case SegmentationMessage.MESSAGE_CODE:
			SegmentationMessage SGM = createSGM(0);
			return SGM;

		case ChargeInformationMessage.MESSAGE_CODE:
			ChargeInformationMessage CIM = createCIM(0);
			return CIM;

		case UserPartAvailableMessage.MESSAGE_CODE:
			UserPartAvailableMessage UPA = createUPA(0);
			return UPA;

		case UserPartTestMessage.MESSAGE_CODE:
			UserPartTestMessage UPT = createUPT(0);
			return UPT;

		case User2UserInformationMessage.MESSAGE_CODE:
			User2UserInformationMessage USR = createUSR(0);
			return USR;
		default:
			throw new IllegalArgumentException("Not supported comamnd code: " + commandCode);
		}
	}

	// private final static Map<Integer,MessageIndexingPlaceHolder>
	// _COMMAND_CODE_2_COMMAND_INDEXES;
	// FIXME: this will be moved once MSG switches to []. 

	// ACM
	private static final MessageIndexingPlaceHolder _ACM_HOLDER;
	// ANM
	private static final MessageIndexingPlaceHolder _ANM_HOLDER;
	// FIXME: APT
	//private static final MessageIndexingPlaceHolder _APT_HOLDER;
	// BLO
	private static final MessageIndexingPlaceHolder _BLO_HOLDER;
	// BLA
	private static final MessageIndexingPlaceHolder _BLA_HOLDER;
	// CPG
	private static final MessageIndexingPlaceHolder _CPG_HOLDER;
	// CGB
	private static final MessageIndexingPlaceHolder _CGB_HOLDER;
	// CGBA
	private static final MessageIndexingPlaceHolder _CGBA_HOLDER;
	// CQM
	private static final MessageIndexingPlaceHolder _CQM_HOLDER;
	// CQR
	private static final MessageIndexingPlaceHolder _CQR_HOLDER;
	// GRS
	private static final MessageIndexingPlaceHolder _GRS_HOLDER;
	// GRA
	private static final MessageIndexingPlaceHolder _GRA_HOLDER;
	// CGU
	private static final MessageIndexingPlaceHolder _CGU_HOLDER;
	// CGUA
	private static final MessageIndexingPlaceHolder _CGUA_HOLDER;
	// CNF
	private static final MessageIndexingPlaceHolder _CNF_HOLDER;
	// CON
	private static final MessageIndexingPlaceHolder _CON_HOLDER;
	// FIXME: COT
	// CCR
	private static final MessageIndexingPlaceHolder _CCR_HOLDER;
	// FIXME: FAC
	// FIXME: FAA
	// FIXME: FRJ
	// FIXME: FAR
	// FIXME: FOT
	// FIXME: IDR
	// FIXME: IRS
	// FIXME: INR
	private static final MessageIndexingPlaceHolder _INR_HOLDER;
	// FIXME: INF
	private static final MessageIndexingPlaceHolder _INF_HOLDER;
	// IAM
	private static final MessageIndexingPlaceHolder _IAM_HOLDER;
	// LPA
	private static final MessageIndexingPlaceHolder _LPA_HOLDER;
	// FIXME: LPP
	// FIXME: NRM
	// OLM
	private static final MessageIndexingPlaceHolder _OLM_HOLDER;
	// FIXME: PAM
	// FIXME: PRI
	// REL
	private static final MessageIndexingPlaceHolder _REL_HOLDER;
	// RLC
	private static final MessageIndexingPlaceHolder _RLC_HOLDER;
	// RSC
	private static final MessageIndexingPlaceHolder _RSC_HOLDER;
	// FIXME: RES
	// FIXME: SGM
	// FIXME: SAM
	private static final MessageIndexingPlaceHolder _SAM_HOLDER;
	// FIXME: SDN
	// FIXME: SUS
	// UBL
	private static final MessageIndexingPlaceHolder _UBL_HOLDER;
	// UBA
	private static final MessageIndexingPlaceHolder _UBA_HOLDER;
	// UCIC
	private static final MessageIndexingPlaceHolder _UCIC_HOLDER;
	// FIXME: UPA
	// FIXME: UPT
	// FIXME: U2UI

	//TODO: remove this, change to use arrays.
	static {
		// Map<Integer,MessageIndexingPlaceHolder> _commandCode2CommandIndexes =
		// new HashMap<Integer, MessageIndexingPlaceHolder>();
		Set<Integer> mandatoryCodes = new HashSet<Integer>();
		Set<Integer> mandatoryVariableCodes = new HashSet<Integer>();
		Set<Integer> optionalCodes = new HashSet<Integer>();

		Map<Integer, Integer> mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		Map<Integer, Integer> mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		Map<Integer, Integer> optionalCodeToIndex = new HashMap<Integer, Integer>();

		// ACM
		mandatoryCodes.add(BackwardCallIndicators._PARAMETER_CODE);
		mandatoryCodeToIndex.put(BackwardCallIndicators._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_F_BackwardCallIndicators);

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
		optionalCodes.add(ApplicationTransportParameter._PARAMETER_CODE);
		optionalCodes.add(CCNRPossibleIndicator._PARAMETER_CODE);
		optionalCodes.add(HTRInformation._PARAMETER_CODE);
		optionalCodes.add(PivotRoutingBackwardInformation._PARAMETER_CODE);
		optionalCodes.add(RedirectStatus._PARAMETER_CODE);

		optionalCodeToIndex.put(OptionalBackwardCallIndicators._PARAMETER_CODE,
				AddressCompleteMessageImpl._INDEX_O_OptionalBackwardCallIndicators);
		optionalCodeToIndex.put(CallReference._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_CallReference);
		optionalCodeToIndex.put(CauseIndicators._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_CauseIndicators);
		optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_UserToUserIndicators);
		optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_UserToUserInformation);
		optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_AccessTransport);
		optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE,
				AddressCompleteMessageImpl._INDEX_O_GenericNotificationIndicator);
		optionalCodeToIndex.put(TransmissionMediumUsed._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_TransmissionMediumUsed);
		optionalCodeToIndex.put(EchoControlInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_EchoControlInformation);
		optionalCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_AccessDeliveryInformation);
		optionalCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_RedirectionNumber);
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
				AddressCompleteMessageImpl._INDEX_O_ParameterCompatibilityInformation);
		optionalCodeToIndex.put(CallDiversionInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_CallDiversionInformation);
		optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_NetworkSpecificFacility);
		optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_RemoteOperations);
		optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_ServiceActivation);
		optionalCodeToIndex.put(RedirectionNumberRestriction._PARAMETER_CODE,
				AddressCompleteMessageImpl._INDEX_O_RedirectionNumberRestriction);
		optionalCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE,
				AddressCompleteMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
		optionalCodeToIndex.put(UIDActionIndicators._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_UIDActionIndicators);
		optionalCodeToIndex.put(ApplicationTransportParameter._PARAMETER_CODE,
				AddressCompleteMessageImpl._INDEX_O_ApplicationTransportParameter);
		optionalCodeToIndex.put(CCNRPossibleIndicator._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_CCNRPossibleIndicator);
		optionalCodeToIndex.put(HTRInformation._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_HTRInformation);
		optionalCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE,
				AddressCompleteMessageImpl._INDEX_O_PivotRoutingBackwardInformation);
		optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE, AddressCompleteMessageImpl._INDEX_O_RedirectStatus);

		MessageIndexingPlaceHolder ACM_HOLDER = new MessageIndexingPlaceHolder();
		ACM_HOLDER.commandCode = AddressCompleteMessage.MESSAGE_CODE;
		ACM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		ACM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		ACM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		ACM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		ACM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		ACM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);
	
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(ACM_HOLDER.commandCode, ACM_HOLDER);
		_ACM_HOLDER = ACM_HOLDER;

		// ANM
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
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
				AnswerMessageImpl._INDEX_O_ParameterCompatibilityInformation);
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
		optionalCodeToIndex
				.put(PivotRoutingBackwardInformation._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_PivotRoutingBackwardInformation);
		optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE, AnswerMessageImpl._INDEX_O_RedirectStatus);

		MessageIndexingPlaceHolder ANM_HOLDER = new MessageIndexingPlaceHolder();
		ANM_HOLDER.commandCode = AnswerMessage.MESSAGE_CODE;
		ANM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		ANM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		ANM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		ANM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		ANM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		ANM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(ANM_HOLDER.commandCode, ANM_HOLDER);
		_ANM_HOLDER = ANM_HOLDER;

		// FIXME: APT

		// BLO
		MessageIndexingPlaceHolder BLO_HOLDER = new MessageIndexingPlaceHolder();
		BLO_HOLDER.commandCode = BlockingMessage.MESSAGE_CODE;
		BLO_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		BLO_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		BLO_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		BLO_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		BLO_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		BLO_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(BLO_HOLDER.commandCode, BLO_HOLDER);
		_BLO_HOLDER = BLO_HOLDER;
		// BLA
		MessageIndexingPlaceHolder BLA_HOLDER = new MessageIndexingPlaceHolder();
		BLA_HOLDER.commandCode = BlockingAckMessage.MESSAGE_CODE;
		BLA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		BLA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		BLA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		BLA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		BLA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		BLA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(BLA_HOLDER.commandCode, BLA_HOLDER);
		_BLA_HOLDER = BLA_HOLDER;

		// CPG
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
		optionalCodeToIndex.put(OptionalBackwardCallIndicators._PARAMETER_CODE,
				CallProgressMessageImpl._INDEX_O_OptionalBackwardCallIndicators);
		optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_AccessTransport);
		optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_UserToUserIndicators);
		optionalCodeToIndex.put(RedirectionNumber._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_RedirectionNumber);
		optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_UserToUserInformation);
		optionalCodeToIndex
				.put(GenericNotificationIndicator._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_GenericNotificationIndicator);
		optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_NetworkSpecificFacility);
		optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_RemoteOperations);
		optionalCodeToIndex.put(TransmissionMediumUsed._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_TransmissionMediumUsed);
		optionalCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_AccessDeliveryInformation);
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
				CallProgressMessageImpl._INDEX_O_ParameterCompatibilityInformation);
		optionalCodeToIndex.put(CallDiversionInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CallDiversionInformation);
		optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_ServiceActivation);
		optionalCodeToIndex
				.put(RedirectionNumberRestriction._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_RedirectionNumberRestriction);
		optionalCodeToIndex.put(CallTransferNumber._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CallTransferNumber);
		optionalCodeToIndex.put(EchoControlInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_EchoControlInformation);
		optionalCodeToIndex.put(ConnectedNumber._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_ConnectedNumber);
		optionalCodeToIndex.put(BackwardGVNS._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_BackwardGVNS);
		optionalCodeToIndex.put(GenericNumber._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_GenericNumber);
		optionalCodeToIndex.put(CallHistoryInformation._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CallHistoryInformation);
		optionalCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE,
				CallProgressMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
		optionalCodeToIndex.put(UIDActionIndicators._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_UIDActionIndicators);
		optionalCodeToIndex.put(ApplicationTransportParameter._PARAMETER_CODE,
				CallProgressMessageImpl._INDEX_O_ApplicationTransportParameter);
		optionalCodeToIndex.put(CCNRPossibleIndicator._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_CCNRPossibleIndicator);
		optionalCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE,
				CallProgressMessageImpl._INDEX_O_PivotRoutingBackwardInformation);
		optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE, CallProgressMessageImpl._INDEX_O_RedirectStatus);

		MessageIndexingPlaceHolder CPG_HOLDER = new MessageIndexingPlaceHolder();
		CPG_HOLDER.commandCode = CallProgressMessage.MESSAGE_CODE;
		CPG_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CPG_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CPG_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CPG_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		CPG_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CPG_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(CPG_HOLDER.commandCode, CPG_HOLDER);
		_CPG_HOLDER = CPG_HOLDER;

		// CGB
		mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
		mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE,
				CircuitGroupBlockingMessageImpl._INDEX_F_CircuitGroupSuperVisionMessageType);

		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupBlockingMessageImpl._INDEX_V_RangeAndStatus);

		MessageIndexingPlaceHolder CGB_HOLDER = new MessageIndexingPlaceHolder();
		CGB_HOLDER.commandCode = CircuitGroupBlockingMessage.MESSAGE_CODE;
		CGB_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CGB_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CGB_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CGB_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		CGB_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CGB_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(CGB_HOLDER.commandCode, CGB_HOLDER);
		_CGB_HOLDER = CGB_HOLDER;

		// CGBA
		mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
		mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE,
				CircuitGroupBlockingAckMessageImpl._INDEX_F_CircuitGroupSuperVisionMessageType);

		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupBlockingAckMessageImpl._INDEX_V_RangeAndStatus);

		MessageIndexingPlaceHolder CGBA_HOLDER = new MessageIndexingPlaceHolder();
		CGBA_HOLDER.commandCode = CircuitGroupBlockingAckMessage.MESSAGE_CODE;
		CGBA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CGBA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CGBA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CGBA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		CGBA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CGBA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(CGBA_HOLDER.commandCode,
		// CGBA_HOLDER);
		_CGBA_HOLDER = CGBA_HOLDER;

		// CQM
		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupQueryMessageImpl._INDEX_V_RangeAndStatus);

		MessageIndexingPlaceHolder CQM_HOLDER = new MessageIndexingPlaceHolder();
		CQM_HOLDER.commandCode = CircuitGroupQueryResponseMessage.MESSAGE_CODE;
		CQM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CQM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CQM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CQM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		CQM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CQM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(CQM_HOLDER.commandCode, CQM_HOLDER);
		_CQM_HOLDER = CQM_HOLDER;

		// CQR
		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodes.add(CircuitStateIndicator._PARAMETER_CODE);

		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupQueryResponseMessageImpl._INDEX_V_RangeAndStatus);
		mandatoryVariableCodeToIndex.put(CircuitStateIndicator._PARAMETER_CODE,
				CircuitGroupQueryResponseMessageImpl._INDEX_V_CircuitStateIndicator);

		MessageIndexingPlaceHolder CQR_HOLDER = new MessageIndexingPlaceHolder();
		CQR_HOLDER.commandCode = CircuitGroupQueryMessage.MESSAGE_CODE;
		CQR_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CQR_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CQR_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CQR_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		CQR_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CQR_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(CQR_HOLDER.commandCode, CQR_HOLDER);
		_CQR_HOLDER = CQR_HOLDER;

		// GRS
		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupResetMessageImpl._INDEX_V_RangeAndStatus);

		MessageIndexingPlaceHolder GRS_HOLDER = new MessageIndexingPlaceHolder();
		GRS_HOLDER.commandCode = CircuitGroupResetMessage.MESSAGE_CODE;
		GRS_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		GRS_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		GRS_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		GRS_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		GRS_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		GRS_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(GRS_HOLDER.commandCode, GRS_HOLDER);
		_GRS_HOLDER = GRS_HOLDER;

		// GRA
		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupResetAckMessageImpl._INDEX_V_RangeAndStatus);

		MessageIndexingPlaceHolder GRA_HOLDER = new MessageIndexingPlaceHolder();
		GRA_HOLDER.commandCode = CircuitGroupResetAckMessage.MESSAGE_CODE;
		GRA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		GRA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		GRA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		GRA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		GRA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		GRA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(GRA_HOLDER.commandCode, GRA_HOLDER);
		_GRA_HOLDER = GRA_HOLDER;

		// CGU
		mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
		mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE,
				CircuitGroupUnblockingMessageImpl._INDEX_F_CircuitGroupSuperVisionMessageType);

		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupUnblockingMessageImpl._INDEX_V_RangeAndStatus);

		MessageIndexingPlaceHolder CGU_HOLDER = new MessageIndexingPlaceHolder();
		CGU_HOLDER.commandCode = CircuitGroupUnblockingMessage.MESSAGE_CODE;
		CGU_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CGU_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CGU_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CGU_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		CGU_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CGU_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(CGU_HOLDER.commandCode, CGU_HOLDER);
		_CGU_HOLDER = CGU_HOLDER;

		// CGUA
		mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
		mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE,
				CircuitGroupUnblockingAckMessageImpl._INDEX_F_CircuitGroupSuperVisionMessageType);

		mandatoryVariableCodes.add(RangeAndStatus._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(RangeAndStatus._PARAMETER_CODE, CircuitGroupUnblockingAckMessageImpl._INDEX_V_RangeAndStatus);

		MessageIndexingPlaceHolder CGUA_HOLDER = new MessageIndexingPlaceHolder();
		CGUA_HOLDER.commandCode = CircuitGroupUnblockingAckMessage.MESSAGE_CODE;
		CGUA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CGUA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CGUA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CGUA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		CGUA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CGUA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();

		_CGUA_HOLDER = CGUA_HOLDER;

		// CNF
		mandatoryCodes.add(CircuitGroupSuperVisionMessageType._PARAMETER_CODE);
		mandatoryCodeToIndex.put(CircuitGroupSuperVisionMessageType._PARAMETER_CODE,
				CircuitGroupUnblockingAckMessageImpl._INDEX_F_CircuitGroupSuperVisionMessageType);

		mandatoryVariableCodes.add(CauseIndicators._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(CauseIndicators._PARAMETER_CODE, ConfusionMessageImpl._INDEX_V_CauseIndicators);

		MessageIndexingPlaceHolder CNF_HOLDER = new MessageIndexingPlaceHolder();
		CNF_HOLDER.commandCode = ConfusionMessage.MESSAGE_CODE;
		CNF_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CNF_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CNF_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CNF_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		CNF_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CNF_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();

		_CNF_HOLDER = CNF_HOLDER;
		
		// CON
		MessageIndexingPlaceHolder CON_HOLDER = new MessageIndexingPlaceHolder();
		mandatoryCodes.add(BackwardCallIndicators._PARAMETER_CODE);

		optionalCodes.add(OptionalBackwardCallIndicators._PARAMETER_CODE);
		optionalCodes.add(BackwardGVNS._PARAMETER_CODE);
		optionalCodes.add(ConnectedNumber._PARAMETER_CODE);
		optionalCodes.add(CallReference._PARAMETER_CODE);
		optionalCodes.add(UserToUserIndicators._PARAMETER_CODE);
		optionalCodes.add(UserToUserInformation._PARAMETER_CODE);
		optionalCodes.add(AccessTransport._PARAMETER_CODE);
		optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
		optionalCodes.add(GenericNotificationIndicator._PARAMETER_CODE);
		optionalCodes.add(RemoteOperations._PARAMETER_CODE);
		optionalCodes.add(TransmissionMediumUsed._PARAMETER_CODE);
		optionalCodes.add(EchoControlInformation._PARAMETER_CODE);
		optionalCodes.add(AccessDeliveryInformation._PARAMETER_CODE);
		optionalCodes.add(CallHistoryInformation._PARAMETER_CODE);
		optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
		optionalCodes.add(ServiceActivation._PARAMETER_CODE);
		optionalCodes.add(GenericNumber._PARAMETER_CODE);
		optionalCodes.add(RedirectionNumberRestriction._PARAMETER_CODE);
		optionalCodes.add(ConferenceTreatmentIndicators._PARAMETER_CODE);
		optionalCodes.add(ApplicationTransportParameter._PARAMETER_CODE);
		optionalCodes.add(HTRInformation._PARAMETER_CODE);
		optionalCodes.add(PivotRoutingBackwardInformation._PARAMETER_CODE);
		optionalCodes.add(RedirectStatus._PARAMETER_CODE);
		
		mandatoryCodeToIndex.put(BackwardCallIndicators._PARAMETER_CODE,ConnectMessageImpl._INDEX_F_BackwardCallIndicators);

	
		optionalCodeToIndex.put(OptionalBackwardCallIndicators._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_OptionalBackwardCallIndicators);
		optionalCodeToIndex.put(BackwardGVNS._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_BackwardGVNS);
		optionalCodeToIndex.put(ConnectedNumber._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_ConnectedNumber);
		optionalCodeToIndex.put(CallReference._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_CallReference);
		optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_UserToUserIndicators);
		optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_UserToUserInformation);
		optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_AccessTransport);
		optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_NetworkSpecificFacility);
		optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_GenericNotificationIndicator);
		optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_RemoteOperations);
		optionalCodeToIndex.put(TransmissionMediumUsed._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_TransmissionMediumUsed);
		optionalCodeToIndex.put(EchoControlInformation._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_EchoControlInformation);
		optionalCodeToIndex.put(AccessDeliveryInformation._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_AccessDeliveryInformation);
		optionalCodeToIndex.put(CallHistoryInformation._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_CallHistoryInformation);
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_ParameterCompatibilityInformation);
		optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_ServiceActivation);
		optionalCodeToIndex.put(GenericNumber._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_GenericNumber);
		optionalCodeToIndex.put(RedirectionNumberRestriction._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_RedirectionNumberRestriction);
		optionalCodeToIndex.put(ConferenceTreatmentIndicators._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
		optionalCodeToIndex.put(ApplicationTransportParameter._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_ConferenceTreatmentIndicators);
		optionalCodeToIndex.put(HTRInformation._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_HTRInformation);
		optionalCodeToIndex.put(PivotRoutingBackwardInformation._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_PivotRoutingBackwardInformation);
		optionalCodeToIndex.put(RedirectStatus._PARAMETER_CODE,ConnectMessageImpl._INDEX_O_RedirectStatus);
		
		CON_HOLDER.commandCode = ConnectMessage.MESSAGE_CODE;
		CON_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CON_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CON_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CON_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		CON_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CON_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		_CON_HOLDER = CON_HOLDER;
		
		// FIXME: COT
		// CCR
		MessageIndexingPlaceHolder CCR_HOLDER = new MessageIndexingPlaceHolder();
		CCR_HOLDER.commandCode = ContinuityCheckRequestMessage.MESSAGE_CODE;
		CCR_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		CCR_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		CCR_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		CCR_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		CCR_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		CCR_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(CCR_HOLDER.commandCode, CCR_HOLDER);
		_CCR_HOLDER = CCR_HOLDER;
		// FIXME: FAC
		// FIXME: FAA
		// FIXME: FRJ
		// FIXME: FAR
		// FIXME: FOT
		// FIXME: IDR
		// FIXME: IRS
		// FIXME: INR
		MessageIndexingPlaceHolder INR_HOLDER = new MessageIndexingPlaceHolder();
		
		mandatoryCodes.add(InformationRequestIndicators._PARAMETER_CODE);
		
		optionalCodes.add(CallReference._PARAMETER_CODE);
		optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
		optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
		 
		mandatoryCodeToIndex.put(InformationRequestIndicators._PARAMETER_CODE,InformationRequestMessageImpl._INDEX_F_InformationRequestIndicators);

		optionalCodeToIndex.put(CallReference._PARAMETER_CODE,InformationRequestMessageImpl._INDEX_O_CallReference);
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,InformationRequestMessageImpl._INDEX_O_ParameterCompatibilityInformation);
		optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE,InformationRequestMessageImpl._INDEX_O_NetworkSpecificFacility);
		
		INR_HOLDER.commandCode = InformationRequestMessage.MESSAGE_CODE;
		INR_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		INR_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		INR_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		INR_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		INR_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		INR_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(CCR_HOLDER.commandCode, CCR_HOLDER);
		_INR_HOLDER = INR_HOLDER;
		// FIXME: INF
		MessageIndexingPlaceHolder INF_HOLDER = new MessageIndexingPlaceHolder();
		
		mandatoryCodes.add(InformationIndicators._PARAMETER_CODE);
		
		optionalCodes.add(CallingPartyCategory._PARAMETER_CODE);
		optionalCodes.add(CallingPartyNumber._PARAMETER_CODE);
		optionalCodes.add(CallReference._PARAMETER_CODE);
		optionalCodes.add(ConnectionRequest._PARAMETER_CODE);
		optionalCodes.add(ParameterCompatibilityInformation._PARAMETER_CODE);
		optionalCodes.add(NetworkSpecificFacility._PARAMETER_CODE);
		
		 
		mandatoryCodeToIndex.put(InformationIndicators._PARAMETER_CODE,InformationMessageImpl._INDEX_F_InformationIndicators);

		optionalCodeToIndex.put(CallingPartyCategory._PARAMETER_CODE,InformationMessageImpl._INDEX_O_CallingPartyCategory);
		optionalCodeToIndex.put(CallingPartyNumber._PARAMETER_CODE,InformationMessageImpl._INDEX_O_CallingPartyNumber);
		optionalCodeToIndex.put(CallReference._PARAMETER_CODE,InformationMessageImpl._INDEX_O_CallReference);
		optionalCodeToIndex.put(ConnectionRequest._PARAMETER_CODE,InformationMessageImpl._INDEX_O_ConnectionRequest);
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,InformationMessageImpl._INDEX_O_ParameterCompatibilityInformation);
		optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE,InformationMessageImpl._INDEX_O_NetworkSpecificFacility);
		
		
		INF_HOLDER.commandCode = InformationMessage.MESSAGE_CODE;
		INF_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		INF_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		INF_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		INF_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		INF_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		INF_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(CCR_HOLDER.commandCode, CCR_HOLDER);
		_INF_HOLDER = INF_HOLDER;
		// IAM
		mandatoryCodes.add(NatureOfConnectionIndicators._PARAMETER_CODE);
		mandatoryCodes.add(ForwardCallIndicators._PARAMETER_CODE);
		mandatoryCodes.add(CallingPartyCategory._PARAMETER_CODE);
		mandatoryCodes.add(TransmissionMediumRequirement._PARAMETER_CODE);

		mandatoryCodeToIndex.put(NatureOfConnectionIndicators._PARAMETER_CODE,
				InitialAddressMessageImpl._INDEX_F_NatureOfConnectionIndicators);
		mandatoryCodeToIndex.put(ForwardCallIndicators._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_F_NatureOfConnectionIndicators);
		mandatoryCodeToIndex.put(CallingPartyCategory._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_F_CallingPartyCategory);
		mandatoryCodeToIndex.put(TransmissionMediumRequirement._PARAMETER_CODE,
				InitialAddressMessageImpl._INDEX_F_TransmissionMediumRequirement);

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
		optionalCodeToIndex.put(OptionalForwardCallIndicators._PARAMETER_CODE,
				InitialAddressMessageImpl._INDEX_O_OptionalForwardCallIndicators);
		optionalCodeToIndex.put(RedirectingNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_RedirectingNumber);
		optionalCodeToIndex.put(RedirectionInformation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_RedirectionInformation);
		optionalCodeToIndex.put(ClosedUserGroupInterlockCode._PARAMETER_CODE,
				InitialAddressMessageImpl._INDEX_O_ClosedUserGroupInterlockCode);
		optionalCodeToIndex.put(ConnectionRequest._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_ConnectionRequest);
		optionalCodeToIndex.put(OriginalCalledNumberImpl._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_OriginalCalledNumber);
		optionalCodeToIndex.put(UserToUserInformation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_UserToUserInformation);
		optionalCodeToIndex.put(AccessTransport._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_AccessTransport);
		optionalCodeToIndex.put(UserServiceInformation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_UserServiceInformation);
		optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_User2UIndicators);
		optionalCodeToIndex.put(GenericNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_GenericNumber);
		optionalCodeToIndex.put(PropagationDelayCounter._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_PropagationDelayCounter);
		optionalCodeToIndex
				.put(UserServiceInformationPrime._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_UserServiceInformationPrime);
		optionalCodeToIndex.put(NetworkSpecificFacility._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_NetworkSPecificFacility);
		optionalCodeToIndex.put(GenericDigits._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_GenericDigits);
		optionalCodeToIndex.put(OriginatingISCPointCode._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_OriginatingISCPointCode);
		optionalCodeToIndex.put(UserTeleserviceInformation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_UserTeleserviceInformation);
		optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_RemoteOperations);
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
				InitialAddressMessageImpl._INDEX_O_ParameterCompatibilityInformation);
		optionalCodeToIndex.put(GenericNotificationIndicator._PARAMETER_CODE,
				InitialAddressMessageImpl._INDEX_O_GenericNotificationIndicator);
		optionalCodeToIndex.put(ServiceActivation._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_ServiceActivation);
		optionalCodeToIndex.put(GenericReference._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_GenericReference);
		optionalCodeToIndex.put(MLPPPrecedence._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_MLPPPrecedence);
		optionalCodeToIndex.put(TransimissionMediumRequierementPrime._PARAMETER_CODE,
				InitialAddressMessageImpl._INDEX_O_TransimissionMediumRequierementPrime);
		optionalCodeToIndex.put(LocationNumber._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_LocationNumber);
		optionalCodeToIndex.put(ForwardGVNS._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_ForwardGVNS);
		optionalCodeToIndex.put(CCSS._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_CCSS);
		optionalCodeToIndex.put(NetworkManagementControls._PARAMETER_CODE, InitialAddressMessageImpl._INDEX_O_NetworkManagementControls);

		MessageIndexingPlaceHolder IAM_HOLDER = new MessageIndexingPlaceHolder();
		IAM_HOLDER.commandCode = InitialAddressMessage.MESSAGE_CODE;
		IAM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		IAM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		IAM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		IAM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		IAM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		IAM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(IAM_HOLDER.commandCode, IAM_HOLDER);
		_IAM_HOLDER = IAM_HOLDER;

		// LPA
		MessageIndexingPlaceHolder LPA_HOLDER = new MessageIndexingPlaceHolder();
		LPA_HOLDER.commandCode = LoopbackAckMessage.MESSAGE_CODE;
		LPA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		LPA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		LPA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		LPA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		LPA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		LPA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(LPA_HOLDER.commandCode, LPA_HOLDER);
		_LPA_HOLDER = LPA_HOLDER;
		// FIXME: LPP
		// FIXME: NRM
		// OLM
		MessageIndexingPlaceHolder OLM_HOLDER = new MessageIndexingPlaceHolder();
		OLM_HOLDER.commandCode = OverloadMessage.MESSAGE_CODE;
		OLM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		OLM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		OLM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		OLM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		OLM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		OLM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(OLM_HOLDER.commandCode, OLM_HOLDER);
		_OLM_HOLDER = OLM_HOLDER;
		// FIXME: PAM
		// FIXME: PRI

		// REL
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
		optionalCodeToIndex.put(ParameterCompatibilityInformation._PARAMETER_CODE,
				ReleaseMessageImpl._INDEX_O_ParameterCompatibilityInformation);
		optionalCodeToIndex.put(UserToUserIndicators._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_U2UIndicators);
		optionalCodeToIndex.put(DisplayInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_DisplayInformation);
		optionalCodeToIndex.put(RemoteOperations._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_RemoteOperations);
		optionalCodeToIndex.put(HTRInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_HTRInformation);
		optionalCodeToIndex.put(RedirectCounter._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_RedirectCounter);
		optionalCodeToIndex.put(RedirectBackwardInformation._PARAMETER_CODE, ReleaseMessageImpl._INDEX_O_RedirectBackwardInformation);

		MessageIndexingPlaceHolder REL_HOLDER = new MessageIndexingPlaceHolder();
		REL_HOLDER.commandCode = ReleaseMessage.MESSAGE_CODE;
		REL_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		REL_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		REL_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		REL_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		REL_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		REL_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(REL_HOLDER.commandCode, REL_HOLDER);
		_REL_HOLDER = REL_HOLDER;

		// RLC
		optionalCodes.add(CauseIndicators._PARAMETER_CODE);
		optionalCodeToIndex.put(CauseIndicators._PARAMETER_CODE, ReleaseCompleteMessageImpl._INDEX_O_CauseIndicators);

		MessageIndexingPlaceHolder RLC_HOLDER = new MessageIndexingPlaceHolder();
		RLC_HOLDER.commandCode = ReleaseCompleteMessage.MESSAGE_CODE;
		RLC_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		RLC_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		RLC_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		RLC_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		RLC_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		RLC_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(RLC_HOLDER.commandCode, RLC_HOLDER);
		_RLC_HOLDER = RLC_HOLDER;

		// RSC
		MessageIndexingPlaceHolder RSC_HOLDER = new MessageIndexingPlaceHolder();
		RSC_HOLDER.commandCode = ResetCircuitMessage.MESSAGE_CODE;
		RSC_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		RSC_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		RSC_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		RSC_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		RSC_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		RSC_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(RSC_HOLDER.commandCode, RSC_HOLDER);
		_RSC_HOLDER = RSC_HOLDER;
		// FIXME: RES
		// FIXME: SGM
		// SAM
		MessageIndexingPlaceHolder SAM_HOLDER = new MessageIndexingPlaceHolder();
		
		mandatoryVariableCodes.add(SubsequentNumber._PARAMETER_CODE);
		mandatoryVariableCodeToIndex.put(SubsequentNumber._PARAMETER_CODE,SubsequentAddressMessageImpl._INDEX_V_SubsequentNumber);
				
		SAM_HOLDER.commandCode = SubsequentAddressMessage.MESSAGE_CODE;
		SAM_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		SAM_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		SAM_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		SAM_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		SAM_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		SAM_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);
		
		
		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		_SAM_HOLDER = SAM_HOLDER;
		// FIXME: SDN
		// FIXME: SUS
		// UBL
		MessageIndexingPlaceHolder UBL_HOLDER = new MessageIndexingPlaceHolder();
		UBL_HOLDER.commandCode = UnblockingMessage.MESSAGE_CODE;
		UBL_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		UBL_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		UBL_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		UBL_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		UBL_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		UBL_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(UBL_HOLDER.commandCode, UBL_HOLDER);
		_UBL_HOLDER = UBL_HOLDER;
		// UBA
		MessageIndexingPlaceHolder UBA_HOLDER = new MessageIndexingPlaceHolder();
		UBA_HOLDER.commandCode = UnblockingAckMessage.MESSAGE_CODE;
		UBA_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		UBA_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		UBA_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		UBA_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		UBA_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		UBA_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(UBA_HOLDER.commandCode, UBA_HOLDER);
		_UBA_HOLDER = UBA_HOLDER;
		// UCIC
		MessageIndexingPlaceHolder UCIC_HOLDER = new MessageIndexingPlaceHolder();
		UCIC_HOLDER.commandCode = UnequippedCICMessage.MESSAGE_CODE;
		UCIC_HOLDER.mandatoryCodes = Collections.unmodifiableSet(mandatoryCodes);
		UCIC_HOLDER.mandatoryVariableCodes = Collections.unmodifiableSet(mandatoryVariableCodes);
		UCIC_HOLDER.optionalCodes = Collections.unmodifiableSet(optionalCodes);
		UCIC_HOLDER.mandatoryCodeToIndex = Collections.unmodifiableMap(mandatoryCodeToIndex);
		UCIC_HOLDER.mandatoryVariableCodeToIndex = Collections.unmodifiableMap(mandatoryVariableCodeToIndex);
		UCIC_HOLDER.optionalCodeToIndex = Collections.unmodifiableMap(optionalCodeToIndex);

		mandatoryCodes = new HashSet<Integer>();
		mandatoryVariableCodes = new HashSet<Integer>();
		optionalCodes = new HashSet<Integer>();
		mandatoryCodeToIndex = new HashMap<Integer, Integer>();
		mandatoryVariableCodeToIndex = new HashMap<Integer, Integer>();
		optionalCodeToIndex = new HashMap<Integer, Integer>();
		// _commandCode2CommandIndexes.put(UCIC_HOLDER.commandCode,
		// UCIC_HOLDER);
		_UCIC_HOLDER = UCIC_HOLDER;
		// FIXME: UPA
		// FIXME: UPT
		// FIXME: U2UI
		// _COMMAND_CODE_2_COMMAND_INDEXES =
		// Collections.unmodifiableMap(_commandCode2CommandIndexes);
	}

	private static class MessageIndexingPlaceHolder {
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
