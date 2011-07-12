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

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;


/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MoForwardShortMessageRequestIndicationImpl extends SmsServiceImpl implements MoForwardShortMessageRequestIndication {
	
	private SM_RP_DA sm_RP_DA;
	private SM_RP_OA sm_RP_OA;
	private byte[] sm_RP_UI;
	private MAPExtensionContainer extensionContainer;
	private IMSI imsi;

	
	public MoForwardShortMessageRequestIndicationImpl(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, byte[] sm_RP_UI, MAPExtensionContainer extensionContainer, IMSI imsi) {
		this.sm_RP_DA = sm_RP_DA;
		this.sm_RP_OA = sm_RP_OA;
		this.sm_RP_UI = sm_RP_UI;
		this.extensionContainer = extensionContainer;
		this.imsi = imsi;
	}
	
	@Override
	public SM_RP_DA getSM_RP_DA() {
		return this.sm_RP_DA;
	}

	@Override
	public SM_RP_OA getSM_RP_OA() {
		return this.sm_RP_OA;
	}

	@Override
	public byte[] getSM_RP_UI() {
		return this.sm_RP_UI;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public IMSI getIMSI() {
		return this.imsi;
	}

}
