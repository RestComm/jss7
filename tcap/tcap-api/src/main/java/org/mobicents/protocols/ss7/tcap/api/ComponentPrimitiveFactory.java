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

package org.mobicents.protocols.ss7.tcap.api;

import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * 
 * @author amit bhayani
 * @author baranowb
 */
public interface ComponentPrimitiveFactory {

	/**
	 * Create a new Invoke. Class of this Invoke will be 1
	 * 
	 * @return return new instance of ({@link Invoke}
	 */
	public Invoke createTCInvokeRequest();

	/**
	 * <p>
	 * Create a new {@link Invoke}. Set the {@link InvokeClass} as per bellow
	 * consideration
	 * </p>
	 * <ul>
	 * <li>Class 1 – Both success and failure are reported.</li>
	 * <li>Class 2 – Only failure is reported.</li>
	 * <li>Class 3 – Only success is reported.</li>
	 * <li>Class 4 – Neither success, nor failure is reported.</li>
	 * </ul>
	 * 
	 * @param invokeClass
	 *            The Class of Operation
	 * @return new instance of ({@link Invoke}
	 */
	public Invoke createTCInvokeRequest(InvokeClass invokeClass);

	public Reject createTCRejectRequest();

	public ReturnResultLast createTCResultLastRequest();

	public ReturnResult createTCResultRequest();

	public ReturnError createTCReturnErrorRequest();

	public OperationCode createOperationCode();

	public ErrorCode createErrorCode();

	public Parameter createParameter();

	public Parameter createParameter(int tag, int tagClass, boolean isPrimitive);

	public Problem createProblem(ProblemType pt);
}
