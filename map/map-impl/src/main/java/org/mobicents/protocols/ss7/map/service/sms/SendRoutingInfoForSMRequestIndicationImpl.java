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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequestIndication;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SendRoutingInfoForSMRequestIndicationImpl extends SmsServiceImpl implements SendRoutingInfoForSMRequestIndication {
	
	protected static final int _TAG_msisdn = 0; 
	protected static final int _TAG_sm_RP_PRI = 1; 
	protected static final int _TAG_serviceCentreAddress = 2; 
	protected static final int _TAG_extensionContainer = 6; 
	protected static final int _TAG_gprsSupportIndicator = 7; 
	protected static final int _TAG_sm_RP_MTI = 8; 
	protected static final int _TAG_sm_RP_SMEA = 9; 

	private ISDNAddressString msisdn;
	private Boolean sm_RP_PRI;
	private AddressString serviceCentreAddress;
	private MAPExtensionContainer extensionContainer;
	private Boolean gprsSupportIndicator;
	private SM_RP_MTI sM_RP_MTI;
	private byte[] sM_RP_SMEA;

	
	public SendRoutingInfoForSMRequestIndicationImpl() {
	}

	public SendRoutingInfoForSMRequestIndicationImpl(ISDNAddressString msisdn, Boolean sm_RP_PRI, AddressString serviceCentreAddress,
			MAPExtensionContainer extensionContainer, Boolean gprsSupportIndicator, SM_RP_MTI sM_RP_MTI, byte[] sM_RP_SMEA) {
		this.msisdn = msisdn;
		this.sm_RP_PRI = sm_RP_PRI;
		this.serviceCentreAddress = serviceCentreAddress;
		this.extensionContainer = extensionContainer;
		this.gprsSupportIndicator = gprsSupportIndicator;
		this.sM_RP_MTI = sM_RP_MTI;
		this.sM_RP_SMEA = sM_RP_SMEA;
	}
	
	@Override
	public ISDNAddressString getMsisdn() {
		return this.msisdn;
	}

	@Override
	public Boolean getSm_RP_PRI() {
		return this.sm_RP_PRI;
	}

	@Override
	public AddressString getServiceCentreAddress() {
		return this.serviceCentreAddress;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public Boolean getGprsSupportIndicator() {
		return this.gprsSupportIndicator;
	}

	@Override
	public SM_RP_MTI getSM_RP_MTI() {
		return this.sM_RP_MTI;
	}

	@Override
	public byte[] getSM_RP_SMEA() {
		return this.sM_RP_SMEA;
	}

	
	public void decode(Parameter parameter) throws MAPParsingComponentException {
		
		Parameter[] parameters = parameter.getParameters();

		if (parameters.length < 3)
			throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForSMRequest: Needs at least 3 mandatory parameters, found"
					+ parameters.length, MAPParsingComponentExceptionReason.MistypedParameter);

		try {
			AsnInputStream ais;

			// msisdn
			Parameter p = parameters[0];
			if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive() || p.getTag() != SendRoutingInfoForSMRequestIndicationImpl._TAG_msisdn)
				throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForSMRequest: Parameter 0 bad tag class or tag or not primitive",
						MAPParsingComponentExceptionReason.MistypedParameter);
			this.msisdn = new ISDNAddressStringImpl();
			((ISDNAddressStringImpl) msisdn).decode(p);

			// sm-RP-PRI
			p = parameters[1];
			if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive() || p.getTag() != SendRoutingInfoForSMRequestIndicationImpl._TAG_sm_RP_PRI)
				throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForSMRequest: Parameter 1 bad tag class or tag or not primitive",
						MAPParsingComponentExceptionReason.MistypedParameter);
			ais = new AsnInputStream(new ByteArrayInputStream(p.getData()));
			this.sm_RP_PRI = ais.readBooleanData(p.getData().length);

			// serviceCentreAddress
			p = parameters[2];
			if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()
					|| p.getTag() != SendRoutingInfoForSMRequestIndicationImpl._TAG_serviceCentreAddress)
				throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForSMRequest: Parameter 2 bad tag class or tag or not primitive",
						MAPParsingComponentExceptionReason.MistypedParameter);
			this.serviceCentreAddress = new ISDNAddressStringImpl();
			((ISDNAddressStringImpl)serviceCentreAddress).decode(p);

			for (int i1 = 3; i1 < parameters.length; i1++) {
				p = parameters[i1];

				if (p.getTag() == SendRoutingInfoForSMRequestIndicationImpl._TAG_extensionContainer && p.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
					if (p.isPrimitive())
						throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForSMRequest: Parameter extensionContainer is primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl)this.extensionContainer).decode(p);
				} else if (p.getTag() == SendRoutingInfoForSMRequestIndicationImpl._TAG_gprsSupportIndicator && p.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
					if (!p.isPrimitive())
						throw new MAPParsingComponentException(
								"Error while decoding sendRoutingInfoForSMRequest: Parameter gprsSupportIndicator is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.gprsSupportIndicator = true;
				} else if (p.getTag() == SendRoutingInfoForSMRequestIndicationImpl._TAG_sm_RP_MTI && p.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
					if (!p.isPrimitive())
						throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForSMRequest: Parameter sm-RP-MTI is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					byte[] buf = p.getData();
					if (buf.length != 1)
						throw new MAPParsingComponentException(
								"Error while decoding sendRoutingInfoForSMRequest: Parameter sm-RP-MTI expected length 1, found: " + buf.length,
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.sM_RP_MTI = SM_RP_MTI.getInstance(buf[0]);
				} else if (p.getTag() == SendRoutingInfoForSMRequestIndicationImpl._TAG_sm_RP_SMEA && p.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
					if (!p.isPrimitive())
						throw new MAPParsingComponentException("Error while decoding sendRoutingInfoForSMRequest: Parameter sm-RP-SMEA is not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					this.sM_RP_SMEA = p.getData();
				}
			}
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException while decoding sendRoutingInfoForSMRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException while decoding sendRoutingInfoForSMRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	public Parameter encode(ComponentPrimitiveFactory factory) throws MAPException {

		if (msisdn == null || sm_RP_PRI == null || serviceCentreAddress == null)
			throw new MAPException("msisdn, sm_RP_PRI and serviceCentreAddress must not be null");

		try {
			// Sequence of Parameter
			
			AsnOutputStream aos = new AsnOutputStream();
			ArrayList<Parameter> lstPar = new ArrayList<Parameter>();

			Parameter p = ((ISDNAddressStringImpl) this.msisdn).encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(SendRoutingInfoForSMRequestIndicationImpl._TAG_msisdn);
			lstPar.add(p);
			
			p = factory.createParameter();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(SendRoutingInfoForSMRequestIndicationImpl._TAG_sm_RP_PRI);
			aos.reset();
			aos.writeBooleanData(this.sm_RP_PRI);
			p.setData(aos.toByteArray());
			lstPar.add(p);

			p = ((AddressStringImpl) this.serviceCentreAddress).encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setTag(SendRoutingInfoForSMRequestIndicationImpl._TAG_serviceCentreAddress);
			lstPar.add(p);
			
			if (this.extensionContainer != null) {
				p = ((MAPExtensionContainerImpl) this.extensionContainer).encode();
				p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p.setTag(SendRoutingInfoForSMRequestIndicationImpl._TAG_extensionContainer);
				lstPar.add(p);
			}

			if (this.gprsSupportIndicator != null && this.gprsSupportIndicator == true) {
				p = factory.createParameter();
				p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p.setTag(SendRoutingInfoForSMRequestIndicationImpl._TAG_gprsSupportIndicator);
				p.setData(new byte[0]);
				lstPar.add(p);
			}

			if (this.sM_RP_MTI != null) {
				p = factory.createParameter();
				p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p.setTag(SendRoutingInfoForSMRequestIndicationImpl._TAG_sm_RP_MTI);
				p.setData(new byte[] { (byte) this.sM_RP_MTI.getCode() });
				lstPar.add(p);
			}

			if (this.sM_RP_SMEA != null) {
				p = factory.createParameter();
				p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
				p.setTag(SendRoutingInfoForSMRequestIndicationImpl._TAG_sm_RP_SMEA);
				p.setData(this.sM_RP_SMEA);
				lstPar.add(p);
			}

			p = factory.createParameter();

			Parameter[] pp = new Parameter[lstPar.size()];
			lstPar.toArray(pp);
			p.setParameters(pp);

			return p;

		} catch (IOException e) {
			throw new MAPException(e.getMessage(), e);
		}
	}
}
