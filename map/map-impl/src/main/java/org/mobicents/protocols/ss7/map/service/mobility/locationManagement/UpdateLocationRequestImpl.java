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
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.VlrCapability;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class UpdateLocationRequestImpl extends MobilityMessageImpl implements UpdateLocationRequest {

	@Override
	public MAPMessageType getMessageType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOperationCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTag() throws MAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTagClass() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getIsPrimitive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IMSI getImsi() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISDNAddressString getMscNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISDNAddressString getVlrNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LMSI getLmsi() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VlrCapability getVlrCapability() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getInformPreviousNetworkEntity() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCsLCSNotSupportedByUE() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GSNAddress getVGmlcAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ADDInfo getADDInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagingArea getPagingArea() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getSkipSubscriberDataUpdate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getRestorationIndicator() {
		// TODO Auto-generated method stub
		return false;
	}

}
