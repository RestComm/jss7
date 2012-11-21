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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentity;

/**
*
LocationInformation ::= SEQUENCE {
	ageOfLocationInformation	AgeOfLocationInformation	OPTIONAL,
	geographicalInformation	[0] GeographicalInformation	OPTIONAL,
	vlr-number	[1] ISDN-AddressString	OPTIONAL,
	locationNumber	[2] LocationNumber	OPTIONAL,
	cellGlobalIdOrServiceAreaIdOrLAI	[3] CellGlobalIdOrServiceAreaIdOrLAI	OPTIONAL,
	extensionContainer	[4] ExtensionContainer	OPTIONAL,
	... ,
	selectedLSA-Id	[5] LSAIdentity	OPTIONAL,
	msc-Number	[6] ISDN-AddressString	OPTIONAL,
	geodeticInformation	[7] GeodeticInformation	OPTIONAL, 
	currentLocationRetrieved	[8] NULL		OPTIONAL,
	sai-Present	[9] NULL		OPTIONAL,
	locationInformationEPS	[10] LocationInformationEPS	OPTIONAL,
	userCSGInformation	[11] UserCSGInformation	OPTIONAL }
-- sai-Present indicates that the cellGlobalIdOrServiceAreaIdOrLAI parameter contains
-- a Service Area Identity.
-- currentLocationRetrieved shall be present 
-- if the location information were retrieved after a successfull paging.
-- if the locationinformationEPS IE is present then the cellGlobalIdOrServiceAreaIdOrLAI IE,
-- the ageOfLocationInformation IE, the geographicalInformation IE, the geodeticInformation IE
-- and the currentLocationRetrieved IE (outside the locationInformationEPS IE) shall be
-- absent. 
-- UserCSGInformation contains the CSG ID, Access mode, and the CSG Membership Indication in
-- the case the Access mode is Hybrid Mode.

AgeOfLocationInformation ::= INTEGER (0..32767)
-- the value represents the elapsed time in minutes since the last
-- network contact of the mobile station (i.e. the actuality of the
-- location information).
-- value 0 indicates that the MS is currently in contact with the
--           network
-- value 32767 indicates that the location information is at least
--               32767 minutes old

* 
* @author sergey vetyutnev
* 
*/
public interface LocationInformation {

	public Integer getAgeOfLocationInformation();

	public GeographicalInformation getGeographicalInformation();

	public ISDNAddressString getVlrNumber();

	public LocationNumberMap getLocationNumber();

	public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI();

	public MAPExtensionContainer getExtensionContainer();

	public LSAIdentity getSelectedLSAId();

	public ISDNAddressString getMscNumber();

	public GeodeticInformation getGeodeticInformation();

	public boolean getCurrentLocationRetrieved();

	public boolean getSaiPresent();

	public LocationInformationEPS getLocationInformationEPS();

	public UserCSGInformation getUserCSGInformation();

}
