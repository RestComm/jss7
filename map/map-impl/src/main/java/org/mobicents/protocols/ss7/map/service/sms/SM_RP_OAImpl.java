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
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SM_RP_OAImpl extends MAPPrimitiveBase implements SM_RP_OA {

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


	public void decode(AsnInputStream ansIS, int tagClass, boolean isPrimitive, int tag, int length) throws MAPParsingComponentException {

		this.msisdn = null;
		this.serviceCentreAddressOA = null;

		if (tagClass != Tag.CLASS_CONTEXT_SPECIFIC || !isPrimitive)
			throw new MAPParsingComponentException("Error while decoding SM_RP_OA: bad tag class or is not primitive: TagClass=" + tagClass,
					MAPParsingComponentExceptionReason.MistypedParameter);

		switch(tag) {
		case _TAG_Msisdn:
			ISDNAddressStringImpl as = new ISDNAddressStringImpl();
			as.decode(ansIS, tagClass, isPrimitive, tag, length);
			this.msisdn = as;
			break;
			
		case _TAG_ServiceCentreAddressOA:
			AddressStringImpl as2 = new AddressStringImpl();
			as2.decode(ansIS, tagClass, isPrimitive, tag, length);
			this.serviceCentreAddressOA = as2;
			break;
			
		case _TAG_noSM_RP_OA:
			try {
				ansIS.readNullData(ansIS.available());
			} catch (AsnException e) {
				throw new MAPParsingComponentException("AsnException when decoding SM_RP_OA: " + e.getMessage(), e,
						MAPParsingComponentExceptionReason.MistypedParameter);
			} catch (IOException e) {
				throw new MAPParsingComponentException("IOException when decoding SM_RP_OA: " + e.getMessage(), e,
						MAPParsingComponentExceptionReason.MistypedParameter);
			}
			break;
			
		default:
			throw new MAPParsingComponentException("Error while SM_RP_OA: bad tag: " + tag, MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	public void encode(AsnOutputStream asnOs) throws MAPException {
		
		if( this.msisdn != null )
			((ISDNAddressStringImpl)this.msisdn).encode(asnOs);
		else if(this.serviceCentreAddressOA != null )
			((AddressStringImpl)this.serviceCentreAddressOA).encode(asnOs);
		else
			try {
				asnOs.writeNULLData();
			} catch (IOException e) {
				throw new MAPException("IOException when encoding SM_RP_OA: " + e.getMessage(), e);
			}
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
