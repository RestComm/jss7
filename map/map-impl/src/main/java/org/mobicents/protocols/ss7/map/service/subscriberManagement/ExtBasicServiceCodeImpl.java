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

package org.mobicents.protocols.ss7.map.service.subscriberManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ExtBasicServiceCodeImpl implements ExtBasicServiceCode, MAPAsnPrimitive  {

	public static final int _ID_ext_BearerService = 2;
	public static final int _ID_ext_Teleservice = 3;

	public static final String _PrimitiveName = "ExtBasicServiceCode";

	private ExtBearerServiceCode extBearerService;
	private ExtTeleserviceCode extTeleservice;	

	public ExtBasicServiceCodeImpl() {
	}

	public ExtBasicServiceCodeImpl(ExtBearerServiceCode extBearerService) {
		this.extBearerService = extBearerService;
	}

	public ExtBasicServiceCodeImpl(ExtTeleserviceCode extTeleservice) {
		this.extTeleservice = extTeleservice;
	}
	
	@Override
	public ExtBearerServiceCode getExtBearerService() {
		return extBearerService;
	}

	@Override
	public ExtTeleserviceCode getExtTeleservice() {
		return extTeleservice;
	}

	
	
	@Override
	public int getTag() throws MAPException {
		if (extBearerService != null)
			return _ID_ext_BearerService;
		else
			return _ID_ext_Teleservice;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_CONTEXT_SPECIFIC;
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
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {

		this.extBearerService = null;
		this.extTeleservice = null;	

		int tag = ais.getTag();

		if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
			switch (tag) {
			case _ID_ext_BearerService:
				this.extBearerService = new ExtBearerServiceCodeImpl();
				((ExtBearerServiceCodeImpl) this.extBearerService).decodeData(ais, length);
				break;
			case _ID_ext_Teleservice:
				this.extTeleservice = new ExtTeleserviceCodeImpl();
				((ExtTeleserviceCodeImpl) this.extTeleservice).decodeData(ais, length);
				break;

			default:
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
						MAPParsingComponentExceptionReason.MistypedParameter);
			}
		} else {
			throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {

		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.extBearerService == null && this.extTeleservice == null)
			throw new MAPException("Both extBearerService and extTeleservice must not be null");
		if (this.extBearerService != null && this.extTeleservice != null)
			throw new MAPException("Both extBearerService and extTeleservice must not be not null");

		if (this.extBearerService != null) {
			((ExtBearerServiceCodeImpl) this.extBearerService).encodeData(asnOs);
		} else {
			((ExtTeleserviceCodeImpl) this.extTeleservice).encodeData(asnOs);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ExtBasicServiceCode [");

		if (this.extBearerService != null) {
			sb.append(this.extBearerService.toString());
		}
		if (this.extTeleservice != null) {
			sb.append(this.extTeleservice.toString());
		}

		sb.append("]");

		return sb.toString();
	}
}
