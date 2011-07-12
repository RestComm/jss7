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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SM_RP_DAImpl extends MAPPrimitiveBase implements SM_RP_DA {
	
	private static final int _TAG_IMSI = 0;
	private static final int _TAG_LMSI = 1;
	private static final int _TAG_ServiceCentreAddressDA = 4;
	private static final int _TAG_NoSM_RP_DA = 5;

	private IMSI imsi;
	private LMSI lmsi;
	private AddressString serviceCentreAddressDA;
	
	public SM_RP_DAImpl() {
	}
	
	public SM_RP_DAImpl(IMSI imsi) {
		this.imsi = imsi;
	}
	
	public SM_RP_DAImpl(LMSI lmsi) {
		this.lmsi = lmsi;
	}
	
	public SM_RP_DAImpl(AddressString serviceCentreAddressDA) {
		this.serviceCentreAddressDA = serviceCentreAddressDA;
	}
	
	
	@Override
	public IMSI getIMSI() {
		return this.imsi;
	}
	
	@Override
	public LMSI getLMSI() {
		return this.lmsi;
	}

	@Override
	public AddressString getServiceCentreAddressDA() {
		return serviceCentreAddressDA;
	}

	
	public int getTagClass() {
		return Tag.CLASS_CONTEXT_SPECIFIC;
	}

	public int getTag() {
		if( this.imsi != null )
			return _TAG_IMSI;
		else if(this.lmsi != null )
			return _TAG_LMSI;
		else if(this.serviceCentreAddressDA != null )
			return _TAG_ServiceCentreAddressDA;
		else
			return _TAG_NoSM_RP_DA;
	}

	public void decode(AsnInputStream ansIS, int tagClass, boolean isPrimitive, int tag, int length) throws MAPParsingComponentException {

		this.imsi = null;
		this.lmsi = null;
		this.serviceCentreAddressDA = null;

		if (tagClass != Tag.CLASS_CONTEXT_SPECIFIC || !isPrimitive)
			throw new MAPParsingComponentException("Error while decoding SM_RP_DA: bad tag class or is not primitive: TagClass=" + tagClass,
					MAPParsingComponentExceptionReason.MistypedParameter);

		switch(tag) {
		case _TAG_IMSI:
			IMSIImpl imsi = new IMSIImpl();
			imsi.decode(ansIS, tagClass, isPrimitive, tag, length);
			this.imsi = imsi;
			break;
			
		case _TAG_LMSI:
			LMSIImpl lmsi = new LMSIImpl();
			lmsi.decode(ansIS, tagClass, isPrimitive, tag, length);
			this.lmsi = lmsi;
			break;
			
		case _TAG_ServiceCentreAddressDA:
			AddressStringImpl as = new AddressStringImpl();
			as.decode(ansIS, tagClass, isPrimitive, tag, length);
			this.serviceCentreAddressDA = as;
			break;
			
		case _TAG_NoSM_RP_DA:
			try {
				ansIS.readNullData(ansIS.available());
			} catch (AsnException e) {
				throw new MAPParsingComponentException("AsnException when decoding SM_RP_DA: " + e.getMessage(), e,
						MAPParsingComponentExceptionReason.MistypedParameter);
			} catch (IOException e) {
				throw new MAPParsingComponentException("IOException when decoding SM_RP_DA: " + e.getMessage(), e,
						MAPParsingComponentExceptionReason.MistypedParameter);
			}
			break;
			
		default:
			throw new MAPParsingComponentException("Error while SM_RP_DA: bad tag: " + tag, MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	public void encode(AsnOutputStream asnOs) throws MAPException {
		
		if( this.imsi != null )
			((IMSIImpl)this.imsi).encode(asnOs);
		else if(this.lmsi != null )
			((LMSIImpl)this.lmsi).encode(asnOs);
		else if(this.serviceCentreAddressDA != null )
			((AddressStringImpl)this.serviceCentreAddressDA).encode(asnOs);
		else {
			try {
				asnOs.writeNULLData();
			} catch (IOException e) {
				throw new MAPException("IOException when encoding SM_RP_DA: " + e.getMessage(), e);
			}
		}
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SM_RP_DA [");

		if (this.imsi != null) {
			sb.append("imsi=");
			sb.append(this.imsi.toString());
		}
		if (this.lmsi != null) {
			sb.append("lmsi=");
			sb.append(this.lmsi.toString());
		}
		if (this.serviceCentreAddressDA != null) {
			sb.append("serviceCentreAddressDA=");
			sb.append(this.serviceCentreAddressDA.toString());
		}

		sb.append("]");

		return sb.toString();
	}

}
