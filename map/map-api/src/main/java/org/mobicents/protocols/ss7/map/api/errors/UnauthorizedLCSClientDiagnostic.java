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

/**
*
* UnauthorizedLCSClient-Diagnostic ::= ENUMERATED {
* 	noAdditionalInformation (0),
* 	clientNotInMSPrivacyExceptionList (1),
* 	callToClientNotSetup (2),
* 	privacyOverrideNotApplicable (3),
* 	disallowedByLocalRegulatoryRequirements (4),
* 	...,
* 	unauthorizedPrivacyClass (5),
* 	unauthorizedCallSessionUnrelatedExternalClient (6),
* 	unauthorizedCallSessionRelatedExternalClient (7) }
* --	exception handling:
* --	any unrecognized value shall be ignored
* 
*
*
* @author sergey vetyutnev
*
*/
public enum UnauthorizedLCSClientDiagnostic {
	noAdditionalInformation (0),
	clientNotInMSPrivacyExceptionList (1),
	callToClientNotSetup (2),
	privacyOverrideNotApplicable (3),
	disallowedByLocalRegulatoryRequirements (4),
	unauthorizedPrivacyClass (5),
	unauthorizedCallSessionUnrelatedExternalClient (6),
	unauthorizedCallSessionRelatedExternalClient (7);

	private int code;

	private UnauthorizedLCSClientDiagnostic(int code) {
		this.code = code;
	}
	

	public int getCode() {
		return code;
	}

	public static UnauthorizedLCSClientDiagnostic getInstance(int code) {
		switch (code) {
		case 0:
			return noAdditionalInformation;
		case 1:
			return clientNotInMSPrivacyExceptionList;
		case 2:
			return callToClientNotSetup;
		case 3:
			return privacyOverrideNotApplicable;
		case 4:
			return disallowedByLocalRegulatoryRequirements;
		case 5:
			return unauthorizedPrivacyClass;
		case 6:
			return unauthorizedCallSessionUnrelatedExternalClient;
		case 7:
			return unauthorizedCallSessionRelatedExternalClient;
		default:
			return null;
		}
	}	

}
