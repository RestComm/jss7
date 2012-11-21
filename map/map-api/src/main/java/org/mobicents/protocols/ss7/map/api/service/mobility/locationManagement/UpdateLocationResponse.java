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

package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**

MAP V3:
UpdateLocationRes ::= SEQUENCE {
	hlr-Number	ISDN-AddressString,
	extensionContainer	ExtensionContainer	OPTIONAL,
	...,
	add-Capability	NULL			OPTIONAL,
	pagingArea-Capability	[0]NULL			OPTIONAL }

MAP V2:
  UpdateLocationRes ::= CHOICE {
	hlr-Number						ISDN-AddressString,
	-- hlr-Number must not be used in version greater 1
	extensibleUpdateLocationRes		ExtensibleUpdateLocationRes}
	-- extensibleUpdateLocationRes must not be used in version 1
  
  ExtensibleUpdateLocationRes ::= SEQUENCE {
	hlr-Number						ISDN-AddressString,
	...}
 
 * 
 * @author sergey vetyutnev
 * 
 */
public interface UpdateLocationResponse extends MobilityMessage {

	public ISDNAddressString getHlrNumber();

	public MAPExtensionContainer getExtensionContainer();

	public boolean getAddCapability();

	public boolean getPagingAreaCapability();

	public long getMapProtocolVersion();

}

