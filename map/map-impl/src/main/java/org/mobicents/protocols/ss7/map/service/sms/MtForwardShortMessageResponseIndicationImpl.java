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

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MtForwardShortMessageResponseIndicationImpl extends SmsServiceImpl implements MtForwardShortMessageResponseIndication {

	private byte[] sM_RP_UI;
	private MAPExtensionContainer extensionContainer;
	
	
	public MtForwardShortMessageResponseIndicationImpl() {
	}
	
	public MtForwardShortMessageResponseIndicationImpl(byte[] sM_RP_UI, MAPExtensionContainer extensionContainer) {
		this.sM_RP_UI = sM_RP_UI;
		this.extensionContainer = extensionContainer;
	}
	

	@Override
	public byte[] getSM_RP_UI() {
		return this.sM_RP_UI;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}
	
	
	public void decode(Parameter parameter) throws MAPParsingComponentException {

		Parameter[] parameters = parameter.getParameters();

		for (Parameter p : parameters) {

			if (p.getTag() == Tag.STRING_OCTET && p.getTagClass() == Tag.CLASS_UNIVERSAL) {
				// sm-RP-UI
				if (!p.isPrimitive())
					throw new MAPParsingComponentException("Error while decoding moForwardShortMessageResponse: Parameter sm_RP_UI is not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				this.sM_RP_UI = p.getData();
			} else if (p.getTag() == Tag.SEQUENCE && p.getTagClass() == Tag.CLASS_UNIVERSAL) {
				// ExtensionContainer
				if (p.isPrimitive())
					throw new MAPParsingComponentException("Error while decoding moForwardShortMessageResponse: Parameter extensionContainer not is primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				this.extensionContainer = new MAPExtensionContainerImpl();
				((MAPExtensionContainerImpl) this.extensionContainer).decode(p);
			}
		}
	}

	public Parameter encode(ComponentPrimitiveFactory factory) throws MAPException {

		// if (sm_RP_UI != null || extensionContainer != null) {

		// Sequence of Parameter
		AsnOutputStream aos = new AsnOutputStream();

		ArrayList<Parameter> lstPar = new ArrayList<Parameter>();

		Parameter p1 = null;
		if (this.sM_RP_UI != null) {
			aos.reset();
			p1 = factory.createParameter();
			p1.setTagClass(Tag.CLASS_UNIVERSAL);
			p1.setTag(Tag.STRING_OCTET);
			p1.setData(this.sM_RP_UI);
			lstPar.add(p1);
		}

		Parameter p2 = null;
		if (this.extensionContainer != null) {
			p2 = ((MAPExtensionContainerImpl) extensionContainer).encode();
			lstPar.add(p2);
		}

		Parameter p = factory.createParameter();

		Parameter[] pp = new Parameter[lstPar.size()];
		lstPar.toArray(pp);
		p.setParameters(pp);

		return p;
	}

}
