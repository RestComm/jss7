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

package org.mobicents.protocols.ss7.map.errors;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessageExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * The MAP ReturnError message with the single parameter - ExtensionContainer
 *
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPErrorMessageExtensionContainerImpl extends MAPErrorMessageImpl implements MAPErrorMessageExtensionContainer {

	private MAPExtensionContainer extensionContainer; 

	
	public MAPErrorMessageExtensionContainerImpl(Long errorCode) {
		super(errorCode);
	}

	
	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
		this.extensionContainer = extensionContainer;
	}


	@Override
	public Parameter encodeParameter() throws MAPException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void decodeParameter(Parameter p) throws MAPException {
		// TODO Auto-generated method stub
		
	}

	

}
