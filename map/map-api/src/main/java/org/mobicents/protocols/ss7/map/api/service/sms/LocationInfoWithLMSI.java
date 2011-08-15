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

import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrimitive;

/**
 * 
 * LocationInfoWithLMSI ::= SEQUENCE {
 *	networkNode-Number	[1] ISDN-AddressString,
 *	lmsi			LMSI			OPTIONAL,
 *	extensionContainer	ExtensionContainer	OPTIONAL,
 *	...,
 *	gprsNodeIndicator	[5]	NULL		OPTIONAL,
 *	-- gprsNodeIndicator is set only if the SGSN number is sent as the 
 *	-- Network Node Number
 *	additional-Number	[6] Additional-Number	OPTIONAL 
 *	-- NetworkNode-number can be either msc-number or sgsn-number
 *	}
 *
 *
 * 
 * @author sergey vetyutnev
 * 
 */
public interface LocationInfoWithLMSI extends MAPAsnPrimitive {
	
	public ISDNAddressString getNetworkNodeNumber();

	public LMSI getLMSI();

	public MAPExtensionContainer getExtensionContainer();
	
	public AdditionalNumberType getAdditionalNumberType();
	
	public ISDNAddressString getAdditionalNumber();
	
}

