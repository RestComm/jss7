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

package org.mobicents.protocols.ss7.map.service.sms;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ReportSMDeliveryStatusRequestIndicationImpl extends SmsServiceImpl implements ReportSMDeliveryStatusRequestIndication {
	
	protected static final int _TAG_AbsentSubscriberDiagnosticSM = 0; 
	protected static final int _TAG_ExtensionContainer = 1; 
	protected static final int _TAG_GprsSupportIndicator = 2; 
	protected static final int _TAG_DeliveryOutcomeIndicator = 3; 
	protected static final int _TAG_AdditionalSMDeliveryOutcome = 4; 
	protected static final int _TAG_AdditionalAbsentSubscriberDiagnosticSM = 5; 

	private ISDNAddressString msisdn;
	private AddressString serviceCentreAddress;
	private SMDeliveryOutcome sMDeliveryOutcome;
	private Integer absentSubscriberDiagnosticSM;
	private MAPExtensionContainer extensionContainer;
	private Boolean gprsSupportIndicator;
	private Boolean deliveryOutcomeIndicator;
	private SMDeliveryOutcome additionalSMDeliveryOutcome;
	private Integer additionalAbsentSubscriberDiagnosticSM;
		
	public ReportSMDeliveryStatusRequestIndicationImpl(ISDNAddressString msisdn, AddressString serviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome,
			Integer absentSubscriberDiagnosticSM, MAPExtensionContainer extensionContainer, Boolean gprsSupportIndicator, Boolean deliveryOutcomeIndicator,
			SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) {
		this.msisdn = msisdn;
		this.serviceCentreAddress = serviceCentreAddress;
		this.sMDeliveryOutcome = sMDeliveryOutcome;
		this.absentSubscriberDiagnosticSM = absentSubscriberDiagnosticSM;
		this.extensionContainer = extensionContainer;
		this.gprsSupportIndicator = gprsSupportIndicator;
		this.deliveryOutcomeIndicator = deliveryOutcomeIndicator;
		this.additionalSMDeliveryOutcome = additionalSMDeliveryOutcome;
		this.additionalAbsentSubscriberDiagnosticSM = additionalAbsentSubscriberDiagnosticSM;
	}	
	
	
	@Override
	public ISDNAddressString getMsisdn() {
		return this.msisdn;
	}

	@Override
	public AddressString getServiceCentreAddress() {
		return this.serviceCentreAddress;
	}

	@Override
	public SMDeliveryOutcome getSMDeliveryOutcome() {
		return this.sMDeliveryOutcome;
	}

	@Override
	public Integer getAbsentSubscriberDiagnosticSM() {
		return this.absentSubscriberDiagnosticSM;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public Boolean getGprsSupportIndicator() {
		return this.gprsSupportIndicator;
	}

	@Override
	public Boolean getDeliveryOutcomeIndicator() {
		return this.deliveryOutcomeIndicator;
	}

	@Override
	public SMDeliveryOutcome getAdditionalSMDeliveryOutcome() {
		return this.additionalSMDeliveryOutcome;
	}

	@Override
	public Integer getAdditionalAbsentSubscriberDiagnosticSM() {
		return this.additionalAbsentSubscriberDiagnosticSM;
	}

}
