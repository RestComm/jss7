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

package org.mobicents.protocols.ss7.map.api.errors;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 * absentSubscriberSM  ERROR ::= {
 *	PARAMETER
 *		AbsentSubscriberSM-Param
 *		-- optional
 *	CODE	local:6 }
 *
 *
 * AbsentSubscriberSM-Param ::= SEQUENCE {
 *	absentSubscriberDiagnosticSM	AbsentSubscriberDiagnosticSM	OPTIONAL,
 *	-- AbsentSubscriberDiagnosticSM can be either for non-GPRS 
 *	-- or for GPRS
 *	extensionContainer	ExtensionContainer	OPTIONAL,
 *	...,
 *	additionalAbsentSubscriberDiagnosticSM  	[0] 	AbsentSubscriberDiagnosticSM	OPTIONAL }
 *	-- if received, additionalAbsentSubscriberDiagnosticSM 
 *	-- is for GPRS and absentSubscriberDiagnosticSM is 
 *	-- for non-GPRS
 *
 *
 * 
 * @author sergey vetyutnev
 * 
 */
public interface MAPErrorMessageAbsentSubscriberSM extends MAPErrorMessage {
	
	public MAPExtensionContainer getExtensionContainer();

	public Integer getAbsentSubscriberDiagnosticSM();

	public Integer getAdditionalAbsentSubscriberDiagnosticSM();

}

