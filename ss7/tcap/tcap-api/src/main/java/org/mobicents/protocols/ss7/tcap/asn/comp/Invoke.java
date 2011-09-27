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
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;

/**
 * @author baranowb
 * @author amit bhayani
 * 
 */
public interface Invoke extends Component {
	// FIXME: add dialog field!
	// this is sequence
	public static final int _TAG = 0x01;
	public static final boolean _TAG_PC_PRIMITIVE = false;
	public static final int _TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;

	public static final int _TAG_IID = 0x02;
	public static final boolean _TAG_IID_PC_PRIMITIVE = true;
	public static final int _TAG_IID_CLASS = Tag.CLASS_UNIVERSAL;

	public static final int _TAG_LID = 0x00;
	public static final boolean _TAG_LID_PC_PRIMITIVE = true;
	public static final int _TAG_LID_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;

	// local, relevant only for send
	public InvokeClass getInvokeClass();

//	/**
//	 * @return the invokeTimeout
//	 */
//	public long getInvokeTimeout();
//
//	/**
//	 * Sets timeout for this invoke operation in miliseconds. If no indication
//	 * on operation status is received, before this value passes, operation
//	 * timesout.
//	 * 
//	 * @param invokeTimeout
//	 *            the invokeTimeout to set
//	 */
//	public void setInvokeTimeout(long invokeTimeout);

	// optional
	public void setLinkedId(Long i);

	public Long getLinkedId();

	// mandatory
	public void setOperationCode(OperationCode i);

	public OperationCode getOperationCode();

	// optional
	public void setParameter(Parameter p);

	public Parameter getParameter();
	
	/**
	 * @return the current invokeTimeout value
	 */
	public long getTimeout();
	
	/**
	 * Setting the Invoke timeout in milliseconds
	 * Must be invoked before sendComponent() invoking
	 * @param invokeTimeout
	 */
	public void setTimeout(long invokeTimeout);
}
