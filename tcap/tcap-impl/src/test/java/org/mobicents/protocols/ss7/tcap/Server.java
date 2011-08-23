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

import java.util.List;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * @author baranowb
 *
 */
public class Server extends EventTestHarness {

	private Component[] components;

	/**
	 * @param stack
	 * @param thisAddress
	 * @param remoteAddress
	 */
	public Server(TCAPStack stack, SccpAddress thisAddress, SccpAddress remoteAddress) {
		super(stack, thisAddress, remoteAddress);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTCBegin(TCBeginIndication ind) {
		// TODO Auto-generated method stub
		super.onTCBegin(ind);
		this.components = ind.getComponents();
	}

	@Override
	public void sendContinue() throws TCAPSendException {

	
		Component[] comps = components;
		if(comps == null || comps.length!=1)
		{
			throw new TCAPSendException("Bad comps!"); 
		}
		Component c = comps[0];
		if(c.getType()!= ComponentType.Invoke)
		{
			throw new TCAPSendException("Bad type: "+c.getType());
		}
		//lets kill this Invoke
		Invoke invoke = (Invoke) c;
		ReturnResultLast rrlast = this.tcapProvider.getComponentPrimitiveFactory().createTCResultLastRequest();
		rrlast.setInvokeId(invoke.getInvokeId());
		rrlast.setOperationCode(invoke.getOperationCode());
		
		super.dialog.sendComponent(rrlast);
		super.sendContinue();
	}
	
	

}
