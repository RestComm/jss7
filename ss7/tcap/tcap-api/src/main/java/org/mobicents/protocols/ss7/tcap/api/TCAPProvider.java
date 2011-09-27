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

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;

public interface TCAPProvider {

	/**
	 * Create new structured dialog.
	 * @param localAddress - desired local address
	 * @param remoteAddress - initial remote address, it can change after first TCContinue. 
	 * @return
	 */
	public Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException;
	/**
	 * Create new unstructured dialog.
	 * @param localAddress
	 * @param remoteAddress
	 * @return
	 * @throws TCAPException
	 */
	public Dialog getNewUnstructuredDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException;

	///////////////
	// Factories //
	///////////////
	
	public DialogPrimitiveFactory getDialogPrimitiveFactory();
	public ComponentPrimitiveFactory getComponentPrimitiveFactory();
	
	///////////////
	// Listeners //
	///////////////	
	
	public void addTCListener(TCListener lst);

	public void removeTCListener(TCListener lst);
}
