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

package org.mobicents.protocols.ss7.map.api.service.mobility.authentication;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 * 
 * V 3:
 * SendAuthenticationInfoArg ::= SEQUENCE {
 *	imsi			[0] IMSI,
 *	numberOfRequestedVectors	NumberOfRequestedVectors,
 *	segmentationProhibited	NULL			OPTIONAL,
 *	immediateResponsePreferred	[1] NULL			OPTIONAL,
 *	re-synchronisationInfo	Re-synchronisationInfo	OPTIONAL,
 *	extensionContainer	[2] ExtensionContainer	OPTIONAL,
 *	...,
 *	requestingNodeType	[3] RequestingNodeType	OPTIONAL,
 *	requestingPLMN-Id	[4] PLMN-Id	OPTIONAL,
 *	numberOfRequestedAdditional-Vectors	[5] NumberOfRequestedVectors	OPTIONAL,
 *	additionalVectorsAreForEPS	[6] NULL		OPTIONAL }	
 *
 * NumberOfRequestedVectors ::= INTEGER (1..5)
 * 
 * V 2:
 * SendAuthenticationInfoArg ::= IMSI
 * 
 * 
 * @author sergey vetyutnev
 * 
 */
public interface SendAuthenticationInfoRequest extends MobilityMessage {

	public IMSI getImsi();

	public int getNumberOfRequestedVectors();

	public boolean getSegmentationProhibited();

	public boolean getImmediateResponsePreferred();

	public ReSynchronisationInfo getReSynchronisationInfo();

	public MAPExtensionContainer getExtensionContainer();

	public RequestingNodeType getRequestingNodeType();

	public PlmnId getRequestingPlmnId();

	public Integer getNumberOfRequestedAdditionalVectors();

	public boolean getAdditionalVectorsAreForEPS();

	public long getMapProtocolVersion();

}

