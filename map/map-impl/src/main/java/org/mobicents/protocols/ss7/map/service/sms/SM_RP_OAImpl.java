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

package org.mobicents.protocols.ss7.map.service.sms;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SM_RP_OAImpl implements SM_RP_OA {

	private static final int _TAG_Msisdn = 2;
	private static final int _TAG_ServiceCentreAddressOA = 4;
	private static final int _TAG_noSM_RP_OA = 5;

	private ISDNAddressString msisdn;
	private AddressString serviceCentreAddressOA;
	
	
	public SM_RP_OAImpl() {
	}
	
	public void setMsisdn(ISDNAddressString msisdn) {
		this.msisdn = msisdn;
	}

	public void setServiceCentreAddressOA(AddressString serviceCentreAddressOA) {
		this.serviceCentreAddressOA = serviceCentreAddressOA;
	}
	
	
	@Override
	public ISDNAddressString getMsisdn() {
		return this.msisdn;
	}

	@Override
	public AddressString getServiceCentreAddressOA() {
		return this.serviceCentreAddressOA;
	}

	
	public int getTagClass() {
		return Tag.CLASS_CONTEXT_SPECIFIC;
	}
	
	public int getTag() {
		if( this.msisdn != null )
			return _TAG_Msisdn;
		else if(this.serviceCentreAddressOA != null )
			return _TAG_ServiceCentreAddressOA;
		else
			return _TAG_noSM_RP_OA;
	}

	@Override
	public boolean getIsPrimitive() {
		return true;
	}

	
	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding SM_RP_OA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SM_RP_OA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding SM_RP_OA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding SM_RP_OA: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		this.msisdn = null;
		this.serviceCentreAddressOA = null;

		if (ansIS.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ansIS.isTagPrimitive())
			throw new MAPParsingComponentException("Error while decoding SM_RP_OA: bad tag class or is not primitive: TagClass=" + ansIS.getTagClass(),
					MAPParsingComponentExceptionReason.MistypedParameter);

		switch(ansIS.getTag()) {
		case _TAG_Msisdn:
			this.msisdn = new ISDNAddressStringImpl();
			this.msisdn.decodeData(ansIS, length);
			break;
			
		case _TAG_ServiceCentreAddressOA:
			this.serviceCentreAddressOA = new AddressStringImpl();
			this.serviceCentreAddressOA.decodeData(ansIS, length);
			break;
			
		case _TAG_noSM_RP_OA:
			try {
				ansIS.readNullData(length);
			} catch (AsnException e) {
				throw new MAPParsingComponentException("AsnException when decoding SM_RP_OA: " + e.getMessage(), e,
						MAPParsingComponentExceptionReason.MistypedParameter);
			} catch (IOException e) {
				throw new MAPParsingComponentException("IOException when decoding SM_RP_OA: " + e.getMessage(), e,
						MAPParsingComponentExceptionReason.MistypedParameter);
			}
			break;
			
		default:
			throw new MAPParsingComponentException("Error while SM_RP_OA: bad tag: " + ansIS.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}
	

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {

		this.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding SM_RP_OA: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		
		if (this.msisdn != null)
			this.msisdn.encodeData(asnOs);
		else if (this.serviceCentreAddressOA != null)
			this.serviceCentreAddressOA.encodeData(asnOs);
		else
			asnOs.writeNullData();
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SM_RP_OA [");

		if (this.msisdn != null) {
			sb.append("msisdn=");
			sb.append(this.msisdn.toString());
		}
		if (this.serviceCentreAddressOA != null) {
			sb.append("serviceCentreAddressOA=");
			sb.append(this.serviceCentreAddressOA.toString());
		}

		sb.append("]");

		return sb.toString();
	}
}
