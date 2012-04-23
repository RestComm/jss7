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

package org.mobicents.protocols.ss7.map.api.service.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
*
LocationInformationEPS ::= SEQUENCE {
	e-utranCellGlobalIdentity	[0] E-UTRAN-CGI		OPTIONAL,
	trackingAreaIdentity	[1] TA-Id		OPTIONAL,
	extensionContainer	[2] ExtensionContainer		OPTIONAL,
	geographicalInformation	[3] GeographicalInformation		OPTIONAL,
	geodeticInformation	[4] GeodeticInformation		OPTIONAL,
	currentLocationRetrieved	[5] NULL			OPTIONAL,
	ageOfLocationInformation	[6] AgeOfLocationInformation		OPTIONAL,
	...,
	mme-Name		[7] DiameterIdentity		OPTIONAL }
-- currentLocationRetrieved shall be present if the location information
-- was retrieved after successful paging.

* 
* @author sergey vetyutnev
* 
*/
public interface LocationInformationEPS {

	public EUtranCgi getEUtranCellGlobalIdentity();

	public TAId getTrackingAreaIdentity();

	public MAPExtensionContainer getExtensionContainer();

	public GeographicalInformation getGeographicalInformation();

	public GeodeticInformation getGeodeticInformation();

	public boolean getCurrentLocationRetrieved();

	public Integer getAgeOfLocationInformation();

	public DiameterIdentity getMmeName();

}
