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

package org.mobicents.protocols.ss7.tcap;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;

/**
 * @author baranowb
 *
 */
public class Client extends EventTestHarness{

	
	
	/**
	 * @param stack
	 * @param thisAddress
	 * @param remoteAddress
	 */
	public Client(TCAPStack stack, SccpAddress thisAddress, SccpAddress remoteAddress) {
		super(stack, thisAddress, remoteAddress);
		
	}

	@Override
	public void sendBegin() throws TCAPException, TCAPSendException {
		ComponentPrimitiveFactory cpFactory = this.tcapProvider.getComponentPrimitiveFactory();
		
		//create some INVOKE
		Invoke invoke = cpFactory.createTCInvokeRequest(InvokeClass.Class1);
		invoke.setInvokeId(this.dialog.getNewInvokeId());
		OperationCode oc = cpFactory.createOperationCode();
		oc.setLocalOperationCode(new Long(12));
		invoke.setOperationCode(oc);
		//no parameter
		this.dialog.sendComponent(invoke);
		super.sendBegin();
	}

	
	
}
