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
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
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
public class MtForwardShortMessageRequestIndicationImpl extends SmsServiceImpl implements MtForwardShortMessageRequestIndication {

	private SM_RP_DA sM_RP_DA;
	private SM_RP_OA sM_RP_OA;
	private byte[] sM_RP_UI;
	private Boolean moreMessagesToSend;
	private MAPExtensionContainer extensionContainer;
	
	public MtForwardShortMessageRequestIndicationImpl() {
	}	
	
	public MtForwardShortMessageRequestIndicationImpl(SM_RP_DA sM_RP_DA, SM_RP_OA sM_RP_OA, byte[] sM_RP_UI, Boolean moreMessagesToSend,
			MAPExtensionContainer extensionContainer) {
		this.sM_RP_DA = sM_RP_DA;
		this.sM_RP_OA = sM_RP_OA;
		this.sM_RP_UI = sM_RP_UI;
		this.moreMessagesToSend = moreMessagesToSend;
		this.extensionContainer = extensionContainer;
	}	
	

	@Override
	public SM_RP_DA getSM_RP_DA() {
		return this.sM_RP_DA;
	}

	@Override
	public SM_RP_OA getSM_RP_OA() {
		return this.sM_RP_OA;
	}

	@Override
	public byte[] getSM_RP_UI() {
		return this.sM_RP_UI;
	}

	@Override
	public Boolean getMoreMessagesToSend() {
		return this.moreMessagesToSend;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	
	public void decode(Parameter parameter) throws MAPParsingComponentException {

		Parameter[] parameters = parameter.getParameters();

		if (parameters.length < 3)
			throw new MAPParsingComponentException("Error while decoding mtForwardShortMessageRequest: Needs at least 3 mandatory parameters, found"
					+ parameters.length, MAPParsingComponentExceptionReason.MistypedParameter);

		// SM_RP_DA
		Parameter p = parameters[0];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive())
			throw new MAPParsingComponentException("Error while decoding mtForwardShortMessageRequest: Parameter 0 bad tag class or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		this.sM_RP_DA = new SM_RP_DAImpl();
		((SM_RP_DAImpl)this.sM_RP_DA).decode(p);

		// SM_RP_OA
		p = parameters[1];
		if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive())
			throw new MAPParsingComponentException("Error while decoding mtForwardShortMessageRequest: Parameter 1 bad tag class or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		this.sM_RP_OA = new SM_RP_OAImpl();
		((SM_RP_OAImpl)this.sM_RP_OA).decode(p);

		// sm-RP-UI
		p = parameters[2];
		if (p.getTagClass() != Tag.CLASS_UNIVERSAL || !p.isPrimitive())
			throw new MAPParsingComponentException("Error while decoding mtForwardShortMessageRequest: Parameter 2 bad tag class or not primitive",
					MAPParsingComponentExceptionReason.MistypedParameter);
		if (p.getTag() != Tag.STRING_OCTET)
			throw new MAPParsingComponentException("Error while decoding mtForwardShortMessageRequest: Parameter 2 tag must be STRING_OCTET, found: "
					+ p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
		this.sM_RP_UI = p.getData();
		
		for (int i1 = 3; i1 < parameters.length; i1++) {
			p = parameters[i1];
			
			if (p.getTag() == Tag.NULL && p.getTagClass() == Tag.CLASS_UNIVERSAL) {
				if (!p.isPrimitive())
					throw new MAPParsingComponentException("Error while decoding mtForwardShortMessageRequest: Parameter moreMessagesToSend is not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				this.moreMessagesToSend = true;
			} else if (p.getTag() == Tag.SEQUENCE && p.getTagClass() == Tag.CLASS_UNIVERSAL) {
				if (p.getTagClass() != Tag.CLASS_UNIVERSAL || p.isPrimitive())
					throw new MAPParsingComponentException("Error while decoding mtForwardShortMessageRequest: Parameter extensionContainer is primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				this.extensionContainer = new MAPExtensionContainerImpl();
				((MAPExtensionContainerImpl) this.extensionContainer).decode(p);
			}
		}
	}

	public Parameter encode(ComponentPrimitiveFactory factory) throws MAPException {

		if (this.sM_RP_DA == null || this.sM_RP_OA == null || this.sM_RP_UI == null)
			throw new MAPException("sm_RP_DA,sm_RP_OA and sm_RP_UI must not be null");

		// Sequence of Parameter
		ArrayList<Parameter> lstPar = new ArrayList<Parameter>();

		Parameter p1 = ((SM_RP_DAImpl) this.sM_RP_DA).encode();
		lstPar.add(p1);

		Parameter p2 = ((SM_RP_OAImpl) this.sM_RP_OA).encode();
		lstPar.add(p2);

		Parameter p3 = factory.createParameter();
		p3.setTagClass(Tag.CLASS_UNIVERSAL);
		p3.setTag(Tag.STRING_OCTET);
		p3.setData(this.sM_RP_UI);
		lstPar.add(p3);

		Parameter p4 = null;
		if (this.moreMessagesToSend != null && this.moreMessagesToSend == true) {
			p4 = factory.createParameter();
			p4.setTagClass(Tag.CLASS_UNIVERSAL);
			p4.setTag(Tag.NULL);
			p4.setData(new byte[0]);
			lstPar.add(p4);
		}

		Parameter p5 = null;
		if (this.extensionContainer != null) {
			p5 = ((MAPExtensionContainerImpl) this.extensionContainer).encode();
			lstPar.add(p5);
		}

		Parameter p = factory.createParameter();

		Parameter[] pp = new Parameter[lstPar.size()];
		lstPar.toArray(pp);
		p.setParameters(pp);

		return p;
	}
}

