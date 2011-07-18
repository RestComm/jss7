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

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class InformServiceCentreRequestIndicationImpl extends SmsServiceImpl implements InformServiceCentreRequestIndication {
	
	protected static final int _TAG_AdditionalAbsentSubscriberDiagnosticSM = 0;

	private ISDNAddressString storedMSISDN;
	private MWStatus mwStatus;
	private MAPExtensionContainer extensionContainer;
	private Integer absentSubscriberDiagnosticSM;
	private Integer additionalAbsentSubscriberDiagnosticSM;
	
	public InformServiceCentreRequestIndicationImpl(ISDNAddressString storedMSISDN, MWStatus mwStatus, MAPExtensionContainer extensionContainer,
			Integer absentSubscriberDiagnosticSM, Integer additionalAbsentSubscriberDiagnosticSM) {
		this.storedMSISDN = storedMSISDN;
		this.mwStatus = mwStatus;
		this.extensionContainer = extensionContainer;
		this.absentSubscriberDiagnosticSM = absentSubscriberDiagnosticSM;
		this.additionalAbsentSubscriberDiagnosticSM = additionalAbsentSubscriberDiagnosticSM;
	}
	
	@Override
	public ISDNAddressString getStoredMSISDN() {
		return this.storedMSISDN;
	}

	@Override
	public MWStatus getMwStatus() {
		return this.mwStatus;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public Integer getAbsentSubscriberDiagnosticSM() {
		return this.absentSubscriberDiagnosticSM;
	}

	@Override
	public Integer getAdditionalAbsentSubscriberDiagnosticSM() {
		return this.additionalAbsentSubscriberDiagnosticSM;
	}

}
