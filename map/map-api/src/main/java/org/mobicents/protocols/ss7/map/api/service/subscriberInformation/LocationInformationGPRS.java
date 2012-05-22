/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.api.service.subscriberInformation;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 * LocationInformationGPRS ::= SEQUENCE {
 *		cellGlobalIdOrServiceAreaIdOrLAI		[0] 	CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL,
 *		routeingAreaIdentity       				[1] 	RAIdentity OPTIONAL,
 *		geographicalInformation					[2] 	GeographicalInformation	OPTIONAL,
 *		sgsn-Number								[3] 	ISDN-AddressString	OPTIONAL,
 *		selectedLSAIdentity						[4] 	LSAIdentity	OPTIONAL,
 *		extensionContainer						[5] 	ExtensionContainer	OPTIONAL,
 *		...,
 *		sai-Present 							[6] 	NULL	OPTIONAL,
 *		geodeticInformation						[7] 	GeodeticInformation	OPTIONAL,
 *		currentLocationRetrieved				[8] 	NULL	OPTIONAL,
 *		ageOfLocationInformation				[9] 	AgeOfLocationInformation	OPTIONAL }
 *			-- sai-Present indicates that the cellGlobalIdOrServiceAreaIdOrLAI parameter contains
 *			-- a Service Area Identity.
 *			-- currentLocationRetrieved shall be present if the location information
 *		-- was retrieved after successful paging.
 *
 *
 * 
 * @author amit bhayani
 *
 */
public interface LocationInformationGPRS extends Serializable {
	CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI();
	RAIdentity getRouteingAreaIdentity();
	GeographicalInformation getGeographicalInformation();
	ISDNAddressString getSGSNNumber();
	LSAIdentity getLSAIdentity();
	MAPExtensionContainer getExtensionContainer();
	Boolean isSaiPresent();
	GeodeticInformation getGeodeticInformation();
	Boolean isCurrentLocationRetrieved();
	Integer getAgeOfLocationInformation();
	
}
