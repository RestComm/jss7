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

package org.mobicents.protocols.ss7.cap.api;

import org.mobicents.protocols.ss7.cap.api.dialog.CAPComponentErrorReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public interface CAPServiceListener {
	/**
	 * Invoked when TC-U-ERROR primitive is received from the other peer
	 * 
	 * @param capDialog
	 * @param invokeId
	 * @param capErrorMessage
	 */
	public void onErrorComponent(CAPDialog capDialog, Long invokeId, CAPErrorMessage capErrorMessage);

	/**
	 * Invoked when TC-U-REJECT primitive is received from the other peer
	 * @param capDialog
	 * @param invokeId
	 *            This parameter is optional and may be the null
	 * @param problem
	 */
	public void onRejectComponent(CAPDialog capDialog, Long invokeId, Problem problem);

	/**
	 * Invoked when no answer from the other peer for a long time - for sending
	 * the a reject for the Invoke
	 * 
	 * @param capDialog
	 * @param invokeId
	 */
	public void onInvokeTimeout(CAPDialog capDialog, Long invokeId);
	
	/**
	 * Invoked when bad component has beed received from the peer
	 *  
	 * @param mapDialog
	 * @param invokeId
	 * @param providerError
	 */
	public void onProviderErrorComponent(CAPDialog mapDialog, Long invokeId, CAPComponentErrorReason providerError);

	/**
	 * Called when any CAPMessage received (Invoke, ReturnResult, ReturnResultLast components)
	 * This component will be invoked before the special service component 
	 * 
	 * @param mapDialog
	 */
	public void onCAPMessage(CAPMessage capMessage);

}
