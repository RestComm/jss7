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

/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.tc.component;

import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * @author baranowb
 * 
 */
public class ComponentPrimitiveFactoryImpl implements ComponentPrimitiveFactory {

	private TCAPProviderImpl provider;
	
	public ComponentPrimitiveFactoryImpl(TCAPProviderImpl tcaProviderImpl) {
		this.provider = tcaProviderImpl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#
	 * createTCInvokeRequest()
	 */
	public Invoke createTCInvokeRequest() {

		InvokeImpl t = (InvokeImpl) TcapFactory.createComponentInvoke();
		t.setProvider(provider);
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#
	 * createTCRejectRequest()
	 */
	public Reject createTCRejectRequest() {

		return TcapFactory.createComponentReject();
	}
	


	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#
	 * createTCResultRequest(boolean)
	 */
	public ReturnResultLast createTCResultLastRequest() {
		
		return TcapFactory.createComponentReturnResultLast();
		
	}
	public ReturnResult createTCResultRequest() {
		
		return TcapFactory.createComponentReturnResult();
	}

	public OperationCode createOperationCode(boolean isGlobal, Long code) {
		return TcapFactory.createOperationCode(isGlobal, code);
	}
	
	public Parameter createParameter() {
		return TcapFactory.createParameter();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#createParameter(int, int, boolean)
	 */
	public Parameter createParameter(int tag, int tagClass, boolean isPrimitive) {
		Parameter p = TcapFactory.createParameter();
		p.setTag(tag);
		p.setTagClass(tagClass);
		p.setPrimitive(isPrimitive);
		return p;
	}
	
	public Problem createProblem(ProblemType pt)
	{
		return TcapFactory.createProblem(pt);
	}
}
