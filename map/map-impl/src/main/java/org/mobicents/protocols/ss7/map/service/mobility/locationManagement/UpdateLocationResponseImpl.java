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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class UpdateLocationResponseImpl extends MobilityMessageImpl implements UpdateLocationResponse {

	public MAPMessageType getMessageType() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getOperationCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTag() throws MAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTagClass() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean getIsPrimitive() {
		// TODO Auto-generated method stub
		return false;
	}

	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		// TODO Auto-generated method stub
		
	}

	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		// TODO Auto-generated method stub
		
	}

	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	public ISDNAddressString getHlrNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	public MAPExtensionContainer getExtensionContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getAddCapability() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getPagingAreaCapability() {
		// TODO Auto-generated method stub
		return false;
	}

}
