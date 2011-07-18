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

import java.util.ArrayList;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
*
* @author sergey vetyutnev
* 
*/
public class LocationInfoWithLMSIImpl extends MAPPrimitiveBase implements LocationInfoWithLMSI {

	private static final int _TAG_NetworkNodeNumber = 1;
	private static final int _TAG_GprsNodeIndicator = 5;
	private static final int _TAG_AdditionalNumber = 6;
	
	private ISDNAddressString networkNodeNumber;
	private LMSI lmsi;
	private MAPExtensionContainer extensionContainer;
	private AdditionalNumberType additionalNumberType;
	private ISDNAddressString additionalNumber;

	
	public LocationInfoWithLMSIImpl() {
	}
	
	public LocationInfoWithLMSIImpl(ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
			AdditionalNumberType additionalNumberType, ISDNAddressString additionalNumber) {
		this.networkNodeNumber = networkNodeNumber;
		this.lmsi = lmsi;
		this.extensionContainer = extensionContainer;
		this.additionalNumberType = additionalNumberType;
		this.additionalNumber = additionalNumber;
	}

	
	@Override
	public ISDNAddressString getNetworkNodeNumber() {
		return this.networkNodeNumber;
	}

	@Override
	public LMSI getLMSI() {
		return this.lmsi;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public AdditionalNumberType getAdditionalNumberType() {
		return this.additionalNumberType;
	}

	@Override
	public ISDNAddressString getAdditionalNumber() {
		return this.additionalNumber;
	}


	public int getTag() {
		return Tag.SEQUENCE;
	}
	
	public void decode(Parameter par) throws MAPParsingComponentException {
		Parameter[] pp = par.getParameters();
		
		this.networkNodeNumber = null;
		this.lmsi = null;
		this.extensionContainer = null;
		this.additionalNumberType = null;
		this.additionalNumber = null;
		
		for (int i1 = 0; i1 < pp.length; i1++) {
			Parameter p = pp[i1];
			
			if (i1 == 0) {
				
				// first parameter is mandatory - networkNode-Number
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive() || p.getTag() != _TAG_NetworkNodeNumber)
					throw new MAPParsingComponentException(
							"Error when decoding LocationInfoWithLMSI: networkNode-Number: tagClass or tag is bad or element is not primitive: tagClass="
									+ p.getTagClass() + ", Tag=" + p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
				this.networkNodeNumber = new ISDNAddressStringImpl();
				((ISDNAddressStringImpl)this.networkNodeNumber).decode(p);
			} else {
				
				// optional parameters
				if(p.getTagClass() == Tag.CLASS_UNIVERSAL) {
					
					switch (p.getTag()) {
					case Tag.STRING_OCTET:
						if (!p.isPrimitive() || this.lmsi != null)
							throw new MAPParsingComponentException(
									"Error when decoding LocationInfoWithLMSI: lmsi: double element or element is not primitive",
									MAPParsingComponentExceptionReason.MistypedParameter);
						this.lmsi = new LMSIImpl();
						((LMSIImpl)this.lmsi).decode(p);
						break;
						
					case Tag.SEQUENCE:
						if (p.isPrimitive() || this.extensionContainer != null)
							throw new MAPParsingComponentException(
									"Error when decoding LocationInfoWithLMSI: extensionContainer: double element or element is primitive",
									MAPParsingComponentExceptionReason.MistypedParameter);
						this.extensionContainer = new MAPExtensionContainerImpl();
						((MAPExtensionContainerImpl)this.extensionContainer).decode(p);
						break;
					}
				}
				if(p.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
					
					switch (p.getTag()) {
					case _TAG_GprsNodeIndicator:
						if (!p.isPrimitive() || this.additionalNumberType != null)
							throw new MAPParsingComponentException(
									"Error when decoding LocationInfoWithLMSI: gprsNodeIndicator: double element or element is not primitive",
									MAPParsingComponentExceptionReason.MistypedParameter);
						this.additionalNumberType = AdditionalNumberType.sgsn;
						break;
						
					case _TAG_AdditionalNumber:
						if (!p.isPrimitive() || this.additionalNumber != null)
							throw new MAPParsingComponentException(
									"Error when decoding LocationInfoWithLMSI: additionalNumber: double element or element is not primitive",
									MAPParsingComponentExceptionReason.MistypedParameter);
						this.additionalNumber = new ISDNAddressStringImpl();
						((ISDNAddressStringImpl)this.additionalNumber).decode(p);
						break;
					}
				}

			}
		}
		
		if (this.additionalNumberType == null && this.additionalNumber != null)
			this.additionalNumberType = AdditionalNumberType.msc;
	}

	
	public Parameter encode() throws MAPException {

		if (this.networkNodeNumber == null)
			throw new MAPException("Error while decoding LocationInfoWithLMSI: networkNodeNumber must not be null");

		ArrayList<Parameter> lstPp = new ArrayList<Parameter>();

		Parameter p1 = ((ISDNAddressStringImpl) this.networkNodeNumber).encode();
		p1.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
		p1.setTag(_TAG_NetworkNodeNumber);
		lstPp.add(p1);

		if (this.lmsi != null) {
			Parameter p2 = ((LMSIImpl) this.lmsi).encode();
			lstPp.add(p2);
		}

		if (this.extensionContainer != null) {
			Parameter p3 = ((MAPExtensionContainerImpl) this.extensionContainer).encode();
			lstPp.add(p3);
		}

		if (this.additionalNumber != null ) {
			if (this.additionalNumberType == AdditionalNumberType.sgsn) {
				Parameter p4 = TcapFactory.createParameter();
				p4.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p4.setPrimitive(true);
				p4.setTag(_TAG_GprsNodeIndicator);
				p4.setData(new byte[0]);
				lstPp.add(p4);
			}
			
			Parameter p5 = ((ISDNAddressStringImpl) this.additionalNumber).encode();
			p5.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p5.setTag(_TAG_AdditionalNumber);
			lstPp.add(p5);
		}
		
		Parameter p = TcapFactory.createParameter();
		
		Parameter[] pp = new Parameter[lstPp.size()];
		lstPp.toArray(pp);
		p.setParameters(pp);
		
		p.setPrimitive(false);
		p.setTagClass(Tag.CLASS_UNIVERSAL);
		p.setTag(Tag.SEQUENCE);
		
		return p;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("LocationInfoWithLMSIImpl [");

		if (this.networkNodeNumber != null) {
			sb.append("networkNodeNumber=");
			sb.append(this.networkNodeNumber.toString());
		}
		if (this.lmsi != null) {
			sb.append(", lmsi=");
			sb.append(this.lmsi.toString());
		}
		if (this.extensionContainer != null) {
			sb.append(", extensionContainer=");
			sb.append(this.extensionContainer.toString());
		}
		if (this.additionalNumberType != null) {
			sb.append(", additionalNumberType=");
			sb.append(this.additionalNumberType.toString());
		}
		if (this.additionalNumber != null) {
			sb.append(", additionalNumber=");
			sb.append(this.additionalNumber.toString());
		}

		sb.append("]");

		return sb.toString();
	}

}

